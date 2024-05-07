package fr.abes.theses_batch_indexation.utils;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.JsonpMapper;
import fr.abes.theses_batch_indexation.configuration.ElasticClient;
import fr.abes.theses_batch_indexation.configuration.ElasticConfig;
import fr.abes.theses_batch_indexation.dto.personne.PersonneModelES;
import fr.abes.theses_batch_indexation.dto.personne.PersonneModelESAvecId;
import fr.abes.theses_batch_indexation.dto.personne.RecherchePersonneModelES;
import fr.abes.theses_batch_indexation.dto.personne.RecherchePersonneModelESAvecId;
import fr.abes.theses_batch_indexation.model.bdd.PersonnesCacheModel;
import jakarta.json.spi.JsonProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public class ElasticSearchUtils {

    private String nomIndex;

    private AtomicInteger page = new AtomicInteger(0);

    public ElasticSearchUtils(String nomIndex) {
        this.nomIndex = nomIndex;
    }


    public void indexerPersonnesDansEsBulk(
            String tablePersonneName,
            int chunkPersonneES,
            JdbcTemplate jdbcTemplate,
            ElasticConfig elasticConfig) throws Exception {
        log.debug("Table personne name : " + tablePersonneName);
        log.debug("Index nom : " + nomIndex);

        page.set(0);

        while (true) {
            BulkRequest.Builder br = new BulkRequest.Builder();

            int pageCourante = page.getAndIncrement();

            log.debug("Indexation de la page " + pageCourante);

            List<Map<String, Object>> r = jdbcTemplate.queryForList(
                    "select * from " + tablePersonneName + " where nom_index = ? " +
                            "OFFSET " + chunkPersonneES * pageCourante +
                            " ROWS FETCH NEXT " + chunkPersonneES + " ROWS ONLY",
                    nomIndex);

            if (r.size() == 0) {
                page.set(0);
                log.debug("Fin de ce thread");
                break;
            }

            List<PersonnesCacheModel> items = r.stream()
                    .map(p -> new PersonnesCacheModel((String) p.get("PPN"),(String) p.get("NOM_INDEX"),(String) p.get("PERSONNE")))
                    .collect(Collectors.toList());
            boolean auMoinsUneOperation = false;
            for (PersonnesCacheModel personnesCacheModel : items) {

                JsonData json = readJson(
                        new ByteArrayInputStream(
                                personnesCacheModel.getPersonne().getBytes()),
                        ElasticClient.getElasticsearchClient()
                );

                br.operations(op -> op
                        .index(idx -> idx
                                .index(nomIndex.toLowerCase())
                                .id(personnesCacheModel.getPpn())
                                .document(json)
                        )
                );
                auMoinsUneOperation = true;
            }
            if (auMoinsUneOperation) {
                BulkRequest bulkRequest = br.build();
                BulkResponse result = null;
                try {
                    result = ElasticClient.getElasticsearchClient().bulk(bulkRequest);
                } catch (IOException e) {
                    log.error("IOException, retry ...");
                    ElasticClient.chargeClient(
                            elasticConfig.getHostname(),
                            elasticConfig.getPort(),
                            elasticConfig.getScheme(),
                            elasticConfig.getUserName(),
                            elasticConfig.getPassword(),
                            elasticConfig.getProtocol());
                    log.error("Reload elastic client done");
                    result = ElasticClient.getElasticsearchClient().bulk(bulkRequest);
                }


                if (result.errors()) {
                    log.error("Erreurs dans le bulk : ");
                    for (BulkResponseItem item : result.items()) {
                        if (item.error() != null) {
                            log.error(item.id() + item.error());
                        }
                    }
                }
            }
        }
    }

    private static JsonData readJson(InputStream input, ElasticsearchClient esClient) {
        JsonpMapper jsonpMapper = esClient._transport().jsonpMapper();
        JsonProvider jsonProvider = jsonpMapper.jsonProvider();

        return JsonData.from(jsonProvider.createParser(input), jsonpMapper);
    }

    public void deletePersonneModelESSansPPN(String theseId) throws IOException {
        List<PersonneModelESAvecId> personneModelESAvecIds = getPersonnesModelESAvecId(theseId);
        personneModelESAvecIds.stream().filter(p -> !p.isHas_idref())
                .forEach(p -> {
                    try {
                        deletePersonnesES(p.get_id());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public void deleteRecherchePersonneModelESSansPPN(String theseId) throws IOException {
        List<RecherchePersonneModelESAvecId> recherchePersonnesModelESAvecId = getRecherchePersonnesModelESAvecId(theseId);
        recherchePersonnesModelESAvecId.stream().filter(p -> !p.isHas_idref())
                .forEach(p -> {
                    try {
                        deletePersonnesES(p.get_id());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }


    public void deletePersonnesSansPPN(List<PersonneModelESAvecId> personnes) {
        personnes.forEach(p ->
                {
                    if (!p.isHas_idref()) {
                        try {
                            deletePersonnesES(p.get_id());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
    }

    public void deleteRecherchePersonnesSansPPN(List<RecherchePersonneModelESAvecId> personnes) {
        personnes.forEach(p ->
                {
                    if (!p.isHas_idref()) {
                        try {
                            deletePersonnesES(p.get_id());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
    }

    public void deletePersonnesAvecUniquementTheseId(List<PersonneModelESAvecId> personnes, String theseModelId) {
        personnes.forEach(p -> {
            p.getTheses_id().remove(theseModelId);
            if (p.getTheses_id().isEmpty()) {
                try {
                    deletePersonnesES(p.get_id());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void deleteRecherchePersonnesAvecUniquementTheseId(List<RecherchePersonneModelESAvecId> personnes, String theseModelId) {
        personnes.forEach(p -> {
            p.getTheses_id().remove(theseModelId);
            if (p.getTheses_id().isEmpty()) {
                try {
                    deletePersonnesES(p.get_id());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void deleteAllPersonneES(List<String> ppns) {
        ppns.forEach(ppn -> {
            try {
                deletePersonnesES(ppn);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


    private void deletePersonnesES(String personneId) throws IOException {
        try {
            ElasticClient.getElasticsearchClient()
                    .delete(d ->
                            d.index(nomIndex.toLowerCase())
                                    .id(personneId));
        } catch (Exception e) {
            log.error("Erreur dans deletePersonnesSansPPN : " + e);
            throw e;
        }
    }

    public List<PersonneModelESAvecId> getPersonnesModelESAvecId(String theseId) throws IOException {

        List<FieldValue> thesesIdList = new ArrayList<>();
        thesesIdList.add(FieldValue.of(theseId));

        try {
            SearchResponse<PersonneModelES> response = ElasticClient.getElasticsearchClient().search(s -> s
                            .index(nomIndex.toLowerCase())
                            .size(100000)
                            .query(q -> q
                                    .terms(t -> t.field("theses_id")
                                            .terms(builder ->
                                                    builder.value(
                                                            thesesIdList
                                                    ))
                                    )),
                    PersonneModelES.class
            );

            return response.hits().hits().stream().map(p ->
                    new PersonneModelESAvecId(p.id(), p.source())
            ).collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Erreur dans getPersonneModelES : " + e);
            throw e;
        }
    }
    public List<RecherchePersonneModelESAvecId> getRecherchePersonnesModelESAvecId(String theseId) throws IOException {

        List<FieldValue> thesesIdList = new ArrayList<>();
        thesesIdList.add(FieldValue.of(theseId));

        try {
            SearchResponse<RecherchePersonneModelES> response = ElasticClient.getElasticsearchClient().search(s -> s
                            .index(nomIndex.toLowerCase())
                            .size(100000)
                            .query(q -> q
                                    .terms(t -> t.field("theses_id")
                                            .terms(builder ->
                                                    builder.value(
                                                            thesesIdList
                                                    ))
                                    )),
                    RecherchePersonneModelES.class
            );

            return response.hits().hits().stream().map( p->
                    new RecherchePersonneModelESAvecId(p.id(), p.source())
            ).collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Erreur dans getPersonneModelES : " + e);
            throw e;
        }
    }

    public List<PersonneModelES> getPersonnesModelESFromES(List<String> ppnList) {
        List<PersonneModelES> personneModelES = new ArrayList<>();

        ppnList.forEach(p -> {
            SearchResponse<PersonneModelES> response = null;
            try {
                TermQuery termQuery = QueryBuilders.term().field("_id").value(p).build();
                Query query = new Query.Builder().term(termQuery).build();

                response = ElasticClient.getElasticsearchClient().search(s -> s
                                .index(nomIndex.toLowerCase())
                                .query(query),
                        PersonneModelES.class
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (response.hits().total().value() > 0) {
                personneModelES.addAll(response.hits().hits().stream().map(per -> per.source()
                ).collect(Collectors.toList()));
            }

        });

        return personneModelES;
    }

    public List<RecherchePersonneModelES> getRecherchePersonnesModelESFromES(List<String> ppnList) {
        List<RecherchePersonneModelES> recherchePersonneModelES = new ArrayList<>();

        ppnList.forEach(p -> {
            SearchResponse<RecherchePersonneModelES> response = null;
            try {
                TermQuery termQuery = QueryBuilders.term().field("_id").value(p).build();
                Query query = new Query.Builder().term(termQuery).build();

                response = ElasticClient.getElasticsearchClient().search(s -> s
                                .index(nomIndex.toLowerCase())
                                .query(query),
                        RecherchePersonneModelES.class
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (response.hits().total().value() > 0) {
                recherchePersonneModelES.addAll(response.hits().hits().stream().map(per -> per.source()
                ).collect(Collectors.toList()));
            }

        });

        return recherchePersonneModelES;
    }

    public void indexerPersonnesDansEs(List<PersonneModelES> items, ElasticConfig elasticConfig) throws Exception {

        boolean auMoinsUneOperation = false;
        BulkRequest.Builder br = new BulkRequest.Builder();

        for (PersonneModelES personneModelES : items) {

            JsonData json = readJson(new ByteArrayInputStream(PersonneCacheUtils.readJson(personneModelES).getBytes()),
                    ElasticClient.getElasticsearchClient());

            br.operations(op -> op
                    .index(idx -> idx
                            .index(nomIndex.toLowerCase())
                            .id(personneModelES.getPpn())
                            .document(json)
                    )
            );
            auMoinsUneOperation = true;
        }
        if (auMoinsUneOperation) {
            BulkRequest bulkRequest = br.build();
            BulkResponse result = null;
            try {
                result = ElasticClient.getElasticsearchClient().bulk(bulkRequest);
            } catch (IOException e) {
                log.error("IOException, retry ...");
                ElasticClient.chargeClient(
                        elasticConfig.getHostname(),
                        elasticConfig.getPort(),
                        elasticConfig.getScheme(),
                        elasticConfig.getUserName(),
                        elasticConfig.getPassword(),
                        elasticConfig.getProtocol());
                log.error("Reload elastic client done");
                result = ElasticClient.getElasticsearchClient().bulk(bulkRequest);
            }


            if (result.errors()) {
                log.error("Erreurs dans le bulk : ");
                for (BulkResponseItem item : result.items()) {
                    if (item.error() != null) {
                        log.error(item.id() + item.error());
                    }
                }
            }
        }
    }
}

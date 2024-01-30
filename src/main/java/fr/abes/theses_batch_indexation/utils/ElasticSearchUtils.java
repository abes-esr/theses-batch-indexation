package fr.abes.theses_batch_indexation.utils;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import fr.abes.theses_batch_indexation.configuration.ElasticClient;
import fr.abes.theses_batch_indexation.dto.personne.PersonneModelES;
import fr.abes.theses_batch_indexation.dto.personne.PersonneModelESAvecId;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ElasticSearchUtils {

    private String nomIndex;

    public ElasticSearchUtils(String nomIndex) {
        this.nomIndex = nomIndex;
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
}

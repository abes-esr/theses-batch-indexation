package fr.abes.theses_batch_indexation.processor;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import fr.abes.theses_batch_indexation.configuration.ElasticClient;
import fr.abes.theses_batch_indexation.database.TheseModel;
import fr.abes.theses_batch_indexation.database.TheseRowMapper;
import fr.abes.theses_batch_indexation.dto.personne.PersonneMapee;
import fr.abes.theses_batch_indexation.dto.personne.PersonneModelES;
import fr.abes.theses_batch_indexation.dto.personne.PersonneModelESAvecId;
import fr.abes.theses_batch_indexation.model.oaisets.Set;
import fr.abes.theses_batch_indexation.model.tef.Mets;
import fr.abes.theses_batch_indexation.utils.MappingJobName;
import fr.abes.theses_batch_indexation.utils.PersonneCacheUtils;
import fr.abes.theses_batch_indexation.utils.XMLJsonMarshalling;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SupprimerThesesPersonneProcessor implements ItemProcessor<TheseModel, TheseModel>, StepExecutionListener {

    MappingJobName mappingJobName = new MappingJobName();
    private final XMLJsonMarshalling marshall;
    String nomIndex;

    @Value("${table.personne.name}")
    private String tablePersonneName;

    List<String> ppnList = new ArrayList<>();
    java.util.Set<String> nntSet = new HashSet<>();
    List<Set> oaiSets;
    PersonneCacheUtils personneCacheUtils = new PersonneCacheUtils();

    private final JdbcTemplate jdbcTemplate;

    public SupprimerThesesPersonneProcessor(XMLJsonMarshalling marshall, JdbcTemplate jdbcTemplate) {
        this.marshall = marshall;
        this.jdbcTemplate = jdbcTemplate;
    }


    public void beforeStep(StepExecution stepExecution) {
        JobExecution jobExecution = stepExecution.getJobExecution();
        nomIndex = mappingJobName.getNomIndexES().get(jobExecution.getJobInstance().getJobName());
        this.oaiSets = (List<Set>) jobExecution.getExecutionContext().get("oaiSets");

        this.personneCacheUtils = new PersonneCacheUtils(
                jdbcTemplate,
                tablePersonneName,
                nomIndex
        );
    }

    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

    @Override
    public TheseModel process(TheseModel theseModel) throws Exception {

        // Initialisation de la table en BDD (donc pas de multi-thread possible)
        personneCacheUtils.initialisePersonneCacheBDD();

        // sortir la liste des personnes de la thèse
        List<PersonneModelESAvecId> personnes = getPersonnesModelESAvecId(theseModel.getId());

        // supprimer les personnes sans ppn
        deletePersonnesSansPPN(personnes);

        deletePersonnesAvecUniquementTheseId(personnes, theseModel.getId());

        // recuperation des ppn
        ppnList = personnes.stream().filter(PersonneModelES::isHas_idref).map(PersonneModelES::getPpn)
                .collect(Collectors.toList());

        // recuperation des ids des theses
        personnes.stream().map(PersonneModelES::getTheses_id).forEach(nntSet::addAll);

        // Suppression de l'id qu'on supprime
        nntSet = nntSet.stream().filter(t -> !t.equals(theseModel.getId()))
                .collect(Collectors.toSet());

        // Ré-indexer la liste des thèses
        //  Récupérer les theses avec JDBCTemplate
        List<TheseModel> theseModels = getTheses(nntSet);

        for (TheseModel theseModelToAdd : theseModels) {
            //   Utiliser PersonneMappee
            Mets mets = marshall.chargerMets(new ByteArrayInputStream(theseModelToAdd.getDoc().getBytes()));
            PersonneMapee personneMapee = new PersonneMapee(mets, theseModelToAdd.getId(), oaiSets);
            theseModelToAdd.setPersonnes(personneMapee.getPersonnes());
        }

        //   MàJ dans la BDD
        for (TheseModel theseModelToAddBdd : theseModels) {
            for (PersonneModelES personneModelES : theseModelToAddBdd.getPersonnes()) {
                if (personneCacheUtils.estPresentDansBDD(personneModelES.getPpn())) {
                    personneCacheUtils.updatePersonneDansBDD(personneModelES);
                } else {
                    personneCacheUtils.ajoutPersonneDansBDD(personneModelES);
                }
            }
        }

        // Nettoyer la table personne_cache des personnes qui ne sont pas dans ppnList
        List<PersonneModelES> personneModelEsEnBDD = personneCacheUtils.getAllPersonneModelBDD();

        personneModelEsEnBDD.forEach(p -> {
            if (p.isHas_idref() && !ppnList.contains(p.getPpn())) {
                try {
                    personneCacheUtils.deletePersonneBDD(p.getPpn());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        jdbcTemplate.execute("commit");
        // Rechargement de la BDD vers ES (à faire avec le job)


        return theseModel;
    }

    private void deletePersonnesAvecUniquementTheseId(List<PersonneModelESAvecId> personnes, String theseModelId) {
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

    private List<TheseModel> getTheses(java.util.Set<String> nntSet) {
        if (nntSet.isEmpty()) {
            return new ArrayList<>();
        }
        String nnts = nntSet.stream().map(i -> "'" + i + "', ").reduce(String::concat).get();
        nnts = nnts.substring(0, nnts.lastIndexOf("', ") + 1);

        return jdbcTemplate.query("select * from Document where nnt in (" + nnts + ")", new TheseRowMapper());
    }

    private void deletePersonnesSansPPN(List<PersonneModelESAvecId> personnes) {
        personnes.forEach(p ->
                {
                    if ( !p.isHas_idref()) {
                        try {
                            deletePersonnesES(p.get_id());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
    }

    private List<PersonneModelESAvecId> getPersonnesModelESAvecId(String theseId) throws IOException {

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
}

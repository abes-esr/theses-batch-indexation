package fr.abes.theses_batch_indexation.processor;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import fr.abes.theses_batch_indexation.configuration.ElasticClient;
import fr.abes.theses_batch_indexation.database.DbService;
import fr.abes.theses_batch_indexation.database.TableIndexationES;
import fr.abes.theses_batch_indexation.database.TheseModel;
import fr.abes.theses_batch_indexation.database.TheseRowMapper;
import fr.abes.theses_batch_indexation.dto.personne.PersonneMapee;
import fr.abes.theses_batch_indexation.dto.personne.PersonneModelES;
import fr.abes.theses_batch_indexation.dto.personne.PersonneModelESAvecId;
import fr.abes.theses_batch_indexation.model.oaisets.Set;
import fr.abes.theses_batch_indexation.model.tef.Mets;
import fr.abes.theses_batch_indexation.utils.ElasticSearchUtils;
import fr.abes.theses_batch_indexation.utils.MappingJobName;
import fr.abes.theses_batch_indexation.utils.PersonneCacheUtils;
import fr.abes.theses_batch_indexation.utils.XMLJsonMarshalling;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Value("${table.suppression.personne.name}")
    private String tablePersonneName;

    List<String> ppnList = new ArrayList<>();
    java.util.Set<String> nntSet = new HashSet<>();
    List<Set> oaiSets;
    PersonneCacheUtils personneCacheUtils = new PersonneCacheUtils();

    ElasticSearchUtils elasticSearchUtils;

    private final JdbcTemplate jdbcTemplate;

    final DbService dbService;

    public SupprimerThesesPersonneProcessor(XMLJsonMarshalling marshall, JdbcTemplate jdbcTemplate, DbService dbService) {
        this.marshall = marshall;
        this.jdbcTemplate = jdbcTemplate;
        this.dbService = dbService;
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

        this.elasticSearchUtils = new ElasticSearchUtils(nomIndex);
    }

    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

    @Override
    public TheseModel process(TheseModel theseModel) throws Exception {

        // Initialisation de la table en BDD (donc pas de multi-thread possible)
        personneCacheUtils.initialisePersonneCacheBDD();

        // sortir la liste des personnes de la thèse
        List<PersonneModelESAvecId> personnes = elasticSearchUtils.getPersonnesModelESAvecId(theseModel.getId());

        // supprimer les personnes sans ppn
        elasticSearchUtils.deletePersonnesSansPPN(personnes);

        elasticSearchUtils.deletePersonnesAvecUniquementTheseId(personnes, theseModel.getId());

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
        List<TheseModel> theseModels = personneCacheUtils.getTheses(nntSet);

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
        personneCacheUtils.initialisePersonneCacheBDD();

        personneModelEsEnBDD.forEach(p -> {
            if (p.isHas_idref() && ppnList.contains(p.getPpn())) {
                personneCacheUtils.ajoutPersonneDansBDD(p);
            }
        });

        dbService.supprimerTheseATraiter(theseModel.getId(), TableIndexationES.suppression_es_personne);

        jdbcTemplate.execute("commit");
        // Rechargement de la BDD vers ES (à faire avec le job)


        return theseModel;
    }
}

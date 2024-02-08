package fr.abes.theses_batch_indexation.processor;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import fr.abes.theses_batch_indexation.configuration.ElasticClient;
import fr.abes.theses_batch_indexation.database.DbService;
import fr.abes.theses_batch_indexation.database.TableIndexationES;
import fr.abes.theses_batch_indexation.database.TheseModel;
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
public class AjouterThesesPersonnesProcessor implements ItemProcessor<TheseModel, TheseModel>, StepExecutionListener {
    MappingJobName mappingJobName = new MappingJobName();
    private final XMLJsonMarshalling marshall;
    String nomIndex;

    @Value("${table.ajout.personne.name}")
    private String tablePersonneName;

    List<String> ppnList = new ArrayList<>();
    java.util.Set<String> nntSet = new HashSet<>();
    List<Set> oaiSets;
    PersonneCacheUtils personneCacheUtils = new PersonneCacheUtils();

    ElasticSearchUtils elasticSearchUtils;

    private final JdbcTemplate jdbcTemplate;

    final DbService dbService;

    public AjouterThesesPersonnesProcessor(XMLJsonMarshalling marshall, JdbcTemplate jdbcTemplate, DbService dbService) {
        this.marshall = marshall;
        this.jdbcTemplate = jdbcTemplate;
        this.dbService = dbService;
    }

    @Override
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

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

    @Override
    public TheseModel process(TheseModel theseModel) throws Exception {

        //TODO: Utiliser une table different pour la suppression et l'ajout

        // Initialisation de la table en BDD (donc pas de multi-thread possible)
        personneCacheUtils.initialisePersonneCacheBDD();

        // Rechercher les personnes qui ont cette thèse dans leur list et suppimer les personnes sans nnt
        elasticSearchUtils.deletePersonneModelESSansPPN(theseModel.getId());

        // sortir la liste des personnes de la thèse
        List<PersonneModelES> personnesTef = getPersonnesModelESFromTef(theseModel.getId());

        // recuperation des ppn
        ppnList = personnesTef.stream().filter(PersonneModelES::isHas_idref).map(PersonneModelES::getPpn)
                .collect(Collectors.toList());

        // récupérer les personnes dans ES
        List<PersonneModelES> personnesES = elasticSearchUtils.getPersonnesModelESFromES(ppnList);

        // recuperation des ids des theses
        personnesES.stream().map(PersonneModelES::getTheses_id).forEach(nntSet::addAll);

        //  Vérifier qu'on ne transforme pas un sujet en NNT, dans ce cas, il faut supprimer IdSujet de nntSet
        if (nntSet.stream().anyMatch(n -> n.equals(theseModel.getIdSujet()))) {
            nntSet = nntSet.stream().filter(n -> !n.equals(theseModel.getIdSujet()))
                    .collect(Collectors.toSet());
        }

        // Ajout de l'id de thèse qu'on indexe
        nntSet.add(theseModel.getId());

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

        // Nettoyage de la table personne_cache des personnes sans ppn et sans la thèse dans leur theses_id
        List<PersonneModelES> personneModelEsEnBDD = personneCacheUtils.getAllPersonneModelBDD();
        personneCacheUtils.initialisePersonneCacheBDD();

        personneModelEsEnBDD.stream().filter(p ->
                (!p.isHas_idref() && p.getTheses_id().contains(theseModel.getId()))
        ).forEach(p -> {
            personneCacheUtils.ajoutPersonneDansBDD(p);
        });

        // Nettoyer la table personne_cache des personnes qui ne sont pas dans ppnList
        personneModelEsEnBDD.stream().filter(p ->
                p.isHas_idref() && ppnList.contains(p.getPpn())
        ).forEach(p -> {
            personneCacheUtils.ajoutPersonneDansBDD(p);
        });

        dbService.supprimerTheseATraiter(theseModel.getId(), TableIndexationES.suppression_es_personne);

        jdbcTemplate.execute("commit");
        // Rechargement de la BDD vers ES (à faire avec le job)

        return theseModel;
    }

    private List<PersonneModelES> getPersonnesModelESFromTef(String id) throws Exception {

        java.util.Set<String> nnt = new HashSet<>();
        nnt.add(id);
        TheseModel theseModelToAdd = personneCacheUtils.getTheses(nnt).get(0);

        Mets mets = marshall.chargerMets(new ByteArrayInputStream(theseModelToAdd.getDoc().getBytes()));
        PersonneMapee personneMapee = new PersonneMapee(mets, theseModelToAdd.getId(), oaiSets);

        return personneMapee.getPersonnes();
    }
}

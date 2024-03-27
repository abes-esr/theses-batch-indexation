package fr.abes.theses_batch_indexation.processor;

import fr.abes.theses_batch_indexation.configuration.ElasticConfig;
import fr.abes.theses_batch_indexation.database.DbService;
import fr.abes.theses_batch_indexation.database.TableIndexationES;
import fr.abes.theses_batch_indexation.database.TheseModel;
import fr.abes.theses_batch_indexation.dto.personne.*;
import fr.abes.theses_batch_indexation.model.oaisets.Set;
import fr.abes.theses_batch_indexation.model.tef.Mets;
import fr.abes.theses_batch_indexation.utils.ElasticSearchUtils;
import fr.abes.theses_batch_indexation.utils.MappingJobName;
import fr.abes.theses_batch_indexation.utils.PersonneCacheUtils;
import fr.abes.theses_batch_indexation.utils.XMLJsonMarshalling;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AjouterThesesRecherchePersonnesProcessor implements ItemProcessor<TheseModel, TheseModel>, StepExecutionListener, ChunkListener {
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

    private final ElasticConfig elasticConfig;
    final DataSource dataSourceLecture;

    public AjouterThesesRecherchePersonnesProcessor(XMLJsonMarshalling marshall,
                                           JdbcTemplate jdbcTemplate,
                                           DbService dbService,
                                           ElasticConfig elasticConfig,
                                           @Qualifier("dataSourceLecture") DataSource dataSourceLecture) {
        this.marshall = marshall;
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcTemplate.setDataSource(dataSourceLecture);
        this.dbService = dbService;
        this.elasticConfig = elasticConfig;
        this.dataSourceLecture = dataSourceLecture;
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

        log.info("Début afterStep");
        return null;
    }

    @Override
    public void beforeChunk(ChunkContext chunkContext) {
        ppnList = new ArrayList<>();
        nntSet = new HashSet<>();
    }

    @Override
    public void afterChunk(ChunkContext chunkContext) {
        log.info("Début after chunk");
        try {
            elasticSearchUtils.indexerPersonnesDansEsBulk(tablePersonneName,
                    10,
                    jdbcTemplate,
                    elasticConfig);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afterChunkError(ChunkContext chunkContext) {

    }

    @Override
    public TheseModel process(TheseModel theseModel) throws Exception {

        log.info("Début execute");

        // Initialisation de la table en BDD (donc pas de multi-thread possible)
        personneCacheUtils.initialisePersonneCacheBDD();

        // Rechercher les personnes qui ont cette thèse dans leur list et suppimer les personnes sans nnt
        elasticSearchUtils.deleteRecherchePersonneModelESSansPPN(theseModel.getId());

        // sortir la liste des personnes de la thèse
        List<PersonneModelES> personnesTef = getPersonnesModelESFromTef(theseModel.getId());

        List<PersonneModelESAvecId> personneModelESAvecIds = elasticSearchUtils.getPersonnesModelESAvecId(theseModel.getId());

        personnesTef.addAll(personneModelESAvecIds);

        // recuperation des ppn
        ppnList = personnesTef.stream().filter(PersonneModelES::isHas_idref).map(PersonneModelES::getPpn)
                .collect(Collectors.toList());

        // récupérer les personnes dans ES
        List<RecherchePersonneModelES> recherchePersonnesES = elasticSearchUtils.getRecherchePersonnesModelESFromES(ppnList);

        // recuperation des ids des theses
        recherchePersonnesES.stream().map(RecherchePersonneModelES::getTheses_id).forEach(nntSet::addAll);

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
            //   Utiliser RecherchePersonneMappee
            Mets mets = marshall.chargerMets(new ByteArrayInputStream(theseModelToAdd.getDoc().getBytes()));
            RecherchePersonneMappe recherchePersonneMappe = new RecherchePersonneMappe(mets, theseModelToAdd.getId(), oaiSets);
            theseModelToAdd.setRecherchePersonnes(recherchePersonneMappe.getPersonnes());
        }

        //   MàJ dans la BDD
        for (TheseModel theseModelToAddBdd : theseModels) {
            for (RecherchePersonneModelES recherchePersonneModelES : theseModelToAddBdd.getRecherchePersonnes()) {
                if (personneCacheUtils.estPresentDansBDD(recherchePersonneModelES.getPpn())) {
                    personneCacheUtils.updateRecherchePersonneDansBDD(recherchePersonneModelES);
                } else {
                    personneCacheUtils.ajoutPersonneDansBDD(recherchePersonneModelES, recherchePersonneModelES.getPpn());
                }
            }
        }

        // Nettoyage de la table personne_cache des personnes sans ppn et sans la thèse dans leur theses_id
        List<RecherchePersonneModelES> recherchePersonneModelESEnBDD = personneCacheUtils.getAllRecherchePersonneModelBDD();
        personneCacheUtils.initialisePersonneCacheBDD();

        recherchePersonneModelESEnBDD.stream().filter(p ->
                (!p.isHas_idref() && p.getTheses_id().contains(theseModel.getId()))
        ).forEach(p -> {
            personneCacheUtils.ajoutPersonneDansBDD(p, p.getPpn());
        });

        // Nettoyer la table personne_cache des personnes qui ne sont pas dans ppnList
        recherchePersonneModelESEnBDD.stream().filter(p ->
                p.isHas_idref() && ppnList.contains(p.getPpn())
        ).forEach(p -> {
            personneCacheUtils.ajoutPersonneDansBDD(p, p.getPpn());
        });

        dbService.supprimerTheseATraiter(theseModel.getId(), TableIndexationES.indexation_es_recherche_personne);

        jdbcTemplate.execute("commit");


        // Rechargement de la BDD vers ES (à faire avec l'afterChunk)

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

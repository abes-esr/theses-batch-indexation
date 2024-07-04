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
import org.springframework.beans.factory.annotation.Autowired;
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
public class SupprimerThesesRecherchePersonneProcessor implements ItemProcessor<TheseModel, TheseModel>, StepExecutionListener, ChunkListener {

    private final MappingJobName mappingJobName;
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

    final DataSource dataSourceLecture;

    final DbService dbService;
    private final ElasticConfig elasticConfig;

    @Autowired
    public SupprimerThesesRecherchePersonneProcessor(MappingJobName mappingJobName, XMLJsonMarshalling marshall,
                                                     JdbcTemplate jdbcTemplate,
                                                     DbService dbService,
                                                     ElasticConfig elasticConfig,
                                                     @Qualifier("dataSourceLecture") DataSource dataSourceLecture) {
        this.mappingJobName = mappingJobName;
        this.marshall = marshall;
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcTemplate.setDataSource(dataSourceLecture);
        this.dbService = dbService;
        this.elasticConfig = elasticConfig;
        this.dataSourceLecture = dataSourceLecture;
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
    public void beforeChunk(ChunkContext chunkContext) {
        ppnList = new ArrayList<>();
        nntSet = new HashSet<>();
    }

    @Override
    public void afterChunk(ChunkContext chunkContext) {
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

        // Initialisation de la table en BDD (donc pas de multi-thread possible)
        personneCacheUtils.initialisePersonneCacheBDD();

        if ( dbService.estPresentDansTableDocument(theseModel.getIdDoc())) {
            dbService.supprimerTheseATraiter(theseModel.getId(), TableIndexationES.suppression_es_recherche_personne);
            return theseModel;
        }

        // sortir la liste des personnes de la thèse
        List<RecherchePersonneModelESAvecId> recherchePersonnesModelESAvecId = elasticSearchUtils.getRecherchePersonnesModelESAvecId(theseModel.getId());

        // supprimer les personnes sans ppn
        elasticSearchUtils.deleteRecherchePersonnesSansPPN(recherchePersonnesModelESAvecId);

        elasticSearchUtils.deleteRecherchePersonnesAvecUniquementTheseId(recherchePersonnesModelESAvecId, theseModel.getId());

        // recuperation des ppn
        ppnList = recherchePersonnesModelESAvecId.stream().filter(RecherchePersonneModelES::isHas_idref).map(RecherchePersonneModelES::getPpn)
                .collect(Collectors.toList());

        // recuperation des ids des theses
        recherchePersonnesModelESAvecId.stream().map(RecherchePersonneModelES::getTheses_id).forEach(nntSet::addAll);

        // Suppression de l'id qu'on supprime
        nntSet = nntSet.stream().filter(t -> !t.equals(theseModel.getId()))
                .collect(Collectors.toSet());

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
                if (recherchePersonneModelES.isHas_idref() && ppnList.contains(recherchePersonneModelES.getPpn())) {
                    if (personneCacheUtils.estPresentDansBDD(recherchePersonneModelES.getPpn())) {
                        personneCacheUtils.updateRecherchePersonneDansBDD(recherchePersonneModelES);
                    } else {
                        personneCacheUtils.ajoutPersonneDansBDD(recherchePersonneModelES, recherchePersonneModelES.getPpn());
                    }
                }
            }
        }

        dbService.supprimerTheseATraiter(theseModel.getId(), TableIndexationES.suppression_es_recherche_personne);

        jdbcTemplate.execute("commit");
        // Rechargement de la BDD vers ES (à faire avec le job)


        return theseModel;
    }
}

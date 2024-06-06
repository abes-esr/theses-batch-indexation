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
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AjouterThesesRecherchePersonnesProcessor implements ItemProcessor<TheseModel, TheseModel>, StepExecutionListener, ChunkListener {
    @Autowired
    MappingJobName mappingJobName;
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

    java.util.Set<String> thesesEnTraitement = Collections.synchronizedSet(new HashSet<>());

    private final ReentrantLock mutex = new ReentrantLock();

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

        java.util.Set nntLies = elasticSearchUtils.getNntLies(theseModel.getId());
        try {
            log.info("Dans la liste des thesesEnTraitement  : " + thesesEnTraitement.size());
            mutex.lock();

            while (
                    nntLies.stream().anyMatch(
                            n -> thesesEnTraitement.contains(n)
                    )
            ) {
                mutex.unlock();
                log.info("On attends ...");
                Thread.sleep(200);
                mutex.lock();
            }
            thesesEnTraitement.addAll(nntLies);
        } finally {
            mutex.unlock();
        }

        try {
            if (!dbService.estPresentDansTableDocument(theseModel.getIdDoc())) {
                dbService.supprimerTheseATraiter(theseModel.getId(), TableIndexationES.indexation_es_recherche_personne);
                return theseModel;
            }

            // Rechercher les personnes qui ont cette thèse dans leur list et suppimer les personnes sans nnt
            elasticSearchUtils.deleteRecherchePersonneModelESSansPPN(theseModel.getId());

            // sortir la liste des personnes de la thèse
            List<RecherchePersonneModelES> recherchePersonnesTefList = getRecherchePersonnesModelESFromTef(theseModel.getId());

            List<RecherchePersonneModelES> recherchePersonnesESList = new ArrayList<>(elasticSearchUtils.getRecherchePersonnesModelESAvecId(theseModel.getId()))
                    .stream().map(RecherchePersonneModelES::new).filter(RecherchePersonneModelES::isHas_idref).collect(Collectors.toList());

            log.info("fin recupération");

            // Dédoublonage des personnes en gardant celles de ES
            List<RecherchePersonneModelES> recherchePersonnesEsEtTef = new ArrayList<>(recherchePersonnesESList);

            for (RecherchePersonneModelES recherchePersonneTef : recherchePersonnesTefList) {
                if (recherchePersonnesESList.stream().noneMatch(p -> p.getPpn().equals(recherchePersonneTef.getPpn()))) {
                    recherchePersonnesEsEtTef.add(recherchePersonneTef);
                }
            }

            log.info("Début traitement");

            Optional<TheseModelES> theseModelES = Optional.empty();

            for (RecherchePersonneModelES recherchePersonnesES : recherchePersonnesEsEtTef) {

                Optional<RecherchePersonneModelES> recherchePersonneModelES;
/*                if (recherchePersonnesES.isHas_idref()) {
                    recherchePersonneModelES = recherchePersonnesTefList.stream().filter(p -> p.isHas_idref() && p.getPpn().equals(recherchePersonnesES.getPpn())).findFirst();
                } else {
                    // si pas de idref, construction des personnes (sans idref); pas sur que ca fonctionne avec NomPrenom car si on change de nomPrenom
                    recherchePersonneModelES = recherchePersonnesTefList.stream().filter(p -> p.getNom().equals(recherchePersonnesES.getNom()) && p.getPrenom().equals(recherchePersonnesES.getPrenom())).findFirst();
                }
                if (recherchePersonneModelES.isPresent()) {
                    theseModelES = recherchePersonneModelES.get().getTheses().stream().findFirst();
                }

                // Enlever la thèse en cours
                recherchePersonnesES.getTheses().removeIf(t -> t.getId().equals(theseModel.getIdSujet()) || t.getId().equals(theseModel.getNnt()));
                recherchePersonnesES.getTheses_id().removeIf(t -> t.equals(theseModel.getIdSujet()) || t.equals(theseModel.getNnt()));

                if (theseModelES.isPresent()) {
                    // Ajout de la thèse en cours
                    recherchePersonnesES.getTheses().add(theseModelES.get());
                    recherchePersonnesES.getTheses_id().add(theseModelES.get().getId());
                }*/
            }

            elasticSearchUtils.indexerRecherchePersonnesDansEs(recherchePersonnesEsEtTef, elasticConfig);

            log.info("Fin traitement");

        }
        finally {
            try {
                mutex.lock();
                thesesEnTraitement.removeAll(nntLies);
                log.info("nntLies supprimés");
            } catch (Exception e) {
                for (Object nnt : nntLies) {
                    log.error("nnt lies " + nnt);
                }
                log.error("removeall de nntlies ne fonctionne pas : " + e);
            } finally {
                mutex.unlock();
            }
        }

            dbService.supprimerTheseATraiter(theseModel.getId(), TableIndexationES.indexation_es_recherche_personne);

            return theseModel;
        }

        private List<RecherchePersonneModelES> getRecherchePersonnesModelESFromTef(String id) throws Exception {

            java.util.Set<String> nnt = new HashSet<>();
            nnt.add(id);

            if (personneCacheUtils.getTheses(nnt).size() == 0) {
                return new ArrayList<>();
            }

            TheseModel theseModelToAdd = personneCacheUtils.getTheses(nnt).get(0);

            Mets mets = marshall.chargerMets(new ByteArrayInputStream(theseModelToAdd.getDoc().getBytes()));
            RecherchePersonneMappe recherchePersonneMappe = new RecherchePersonneMappe(mets, theseModelToAdd.getId(), oaiSets);

            return recherchePersonneMappe.getPersonnes();
        }
    }

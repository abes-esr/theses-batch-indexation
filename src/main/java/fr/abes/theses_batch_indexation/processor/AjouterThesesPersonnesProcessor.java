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
import java.util.stream.Collectors;

@Slf4j
@Component
public class AjouterThesesPersonnesProcessor implements ItemProcessor<TheseModel, TheseModel>, StepExecutionListener, ChunkListener {

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

    public AjouterThesesPersonnesProcessor(XMLJsonMarshalling marshall,
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

    }

    @Override
    public void afterChunkError(ChunkContext chunkContext) {

    }

    @Override
    public TheseModel process(TheseModel theseModel) throws Exception {

        log.info("Début execute : " + theseModel.getNnt());

        log.info("Dans la liste des thesesEnTraitement  : " + thesesEnTraitement.size());

        while (
                elasticSearchUtils.getNntLies(theseModel.getId()).stream().anyMatch(
                        n -> {
                           return thesesEnTraitement.contains(n);
                        }
                )
        ) {
            log.info("On attends ...");
            Thread.sleep(100);
        }

        java.util.Set nntLies = elasticSearchUtils.getNntLies(theseModel.getId());

        thesesEnTraitement.addAll(nntLies);

        if (!dbService.estPresentDansTableDocument(theseModel.getIdDoc())) {
            dbService.supprimerTheseATraiter(theseModel.getId(), TableIndexationES.indexation_es_personne);
            return theseModel;
        }

        // Rechercher les personnes qui ont cette thèse dans leur list et suppimer les personnes sans nnt
        elasticSearchUtils.deletePersonneModelESSansPPN(theseModel.getId());

        log.info("fin suppression");
        // sortir la liste des personnes de la thèse
        List<PersonneModelES> personnesTefList = getPersonnesModelESFromTef(theseModel.getId());

        log.info("1");
        List<PersonneModelES> personneModelESList = new ArrayList<>(elasticSearchUtils.getPersonnesModelESAvecId(theseModel.getId()))
                .stream().map(PersonneModelES::new).filter(PersonneModelES::isHas_idref).collect(Collectors.toList());

        log.info("fin recupération");
        //personneModelESAvecIds.addAll(personnesTef);
        //personnesTef.addAll(personneModelESAvecIds);

        List<PersonneModelES> personneModelESEtTef = new ArrayList<>(personneModelESList);

        // Dédoublonage des personnes en gardant celles de ES
        for (PersonneModelES personneTef : personnesTefList) {

            if (personneModelESList.stream().noneMatch(p -> {
                if (p.getPpn() == null) {
                    log.info("null");
                }
                return p.getPpn().equals(personneTef.getPpn());
            })) {
                personneModelESEtTef.add(personneTef);
            }
        }

        log.info("2");

        // recuperation des ppn
        ppnList = personneModelESEtTef.stream().filter(PersonneModelES::isHas_idref).map(PersonneModelES::getPpn)
                .collect(Collectors.toList());

        log.info("3");
        // récupérer les personnes dans ES
        //List<PersonneModelES> personnesES = elasticSearchUtils.getPersonnesModelESFromES(ppnList);

        // recuperation des ids des theses
        personneModelESEtTef.stream().map(PersonneModelES::getTheses_id).forEach(nntSet::addAll);

        //  Vérifier qu'on ne transforme pas un sujet en NNT, dans ce cas, il faut supprimer IdSujet de nntSet
        if (nntSet.stream().anyMatch(n -> n.equals(theseModel.getIdSujet()))) {
            nntSet = nntSet.stream().filter(n -> !n.equals(theseModel.getIdSujet()))
                    .collect(Collectors.toSet());
        }

        // Ajout de l'id de thèse qu'on indexe
        nntSet.add(theseModel.getId());

        log.info("4 début traitement");

        //Mets mets = marshall.chargerMets(new ByteArrayInputStream(theseModel.getDoc().getBytes()));
        //PersonneMapee personneMapee = new PersonneMapee(mets, theseModel.getId(), oaiSets);

        TheseModelES theseModelES = personnesTefList.stream().findFirst().orElseThrow().getTheses().stream().findFirst().orElseThrow();

        for (PersonneModelES personneES : personneModelESEtTef) {

            // Enlever la thèse en cours
            personneES.setTheses(
                    personneES.getTheses().stream()
                            .filter(t -> !Objects.equals(t.getId(), theseModel.getIdSujet())
                                    || !Objects.equals(t.getId(), theseModel.getNnt()))
                            .collect(Collectors.toList()));

            personneES.setTheses_id(personneES.getTheses_id().stream().filter(
                            t -> !Objects.equals(t, theseModel.getIdSujet())
                                    || !Objects.equals(t, theseModel.getNnt()))
                    .collect(Collectors.toSet()
                    ));

            // Ajout de la thèse en cours
            personneES.getTheses().add(theseModelES);
            personneES.getTheses_id().add(theseModelES.getId());
        }

        log.info("5");

        elasticSearchUtils.indexerPersonnesDansEs(personneModelESEtTef, elasticConfig);

        log.info("6 fin traitement");

        thesesEnTraitement.removeAll(nntLies);

        dbService.supprimerTheseATraiter(theseModel.getId(), TableIndexationES.indexation_es_personne);

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

package fr.abes.theses_batch_indexation.writer;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.JsonpMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.abes.theses_batch_indexation.configuration.ElasticClient;
import fr.abes.theses_batch_indexation.database.TableIndexationES;
import fr.abes.theses_batch_indexation.database.TheseModel;
import fr.abes.theses_batch_indexation.dto.personne.PersonneModelES;
import fr.abes.theses_batch_indexation.model.bdd.PersonnesCacheModel;
import fr.abes.theses_batch_indexation.utils.MappingJobName;
import fr.abes.theses_batch_indexation.utils.PersonneCacheUtils;
import fr.abes.theses_batch_indexation.utils.ProxyRetry;
import jakarta.json.spi.JsonProvider;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PersonnesBDDWriter implements ItemWriter<TheseModel>, StepExecutionListener {

    /*
    todo: OK-Ajouter l'index dans la table
          OK-Ajouter l'initialisation de l'index dans la table (parametrique: 0/1)
          OK-Ajout de la tasklet pour le bulk dans ES
          -Ajout possiblilité de repartir d'un iddoc

     */

    private final JdbcTemplate jdbcTemplate;
    private final Environment env;
    private final MappingJobName mappingJobName;
    @Autowired
    ProxyRetry proxyRetry;
    private String nomIndex;
    @Value("${table.personne.name}")
    private String tablePersonneName;
    private AtomicInteger nombreDeTheses = new AtomicInteger(0);
    private AtomicInteger nombreDePersonnes = new AtomicInteger(0);
    private AtomicInteger nombreDePersonnesUpdated = new AtomicInteger(0);
    private AtomicInteger nombreDePersonnesUpdatedDansCeChunk = new AtomicInteger(0);
    private List<PersonneModelES> personneCacheList = Collections.synchronizedList(new ArrayList<>());
    private PersonneCacheUtils personneCacheUtils;
    @Value("${job.chunk}")
    private int chunkPersonneES;
    private AtomicInteger page = new AtomicInteger(0);

    public PersonnesBDDWriter(JdbcTemplate jdbcTemplate, Environment env, MappingJobName mappingJobName) {
        this.jdbcTemplate = jdbcTemplate;
        this.env = env;
        this.mappingJobName = mappingJobName;
    }

    public static JsonData readJson(InputStream input, ElasticsearchClient esClient) {
        JsonpMapper jsonpMapper = esClient._transport().jsonpMapper();
        JsonProvider jsonProvider = jsonpMapper.jsonProvider();

        return JsonData.from(jsonProvider.createParser(input), jsonpMapper);
    }

    public static String writeJson(Object personneModelES) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(personneModelES);
    }

    @Override
    public void write(List<? extends TheseModel> items) throws Exception {

        nomIndex = mappingJobName.getNomIndexES().get(env.getProperty("spring.batch.job.names"));

        this.personneCacheUtils = new PersonneCacheUtils(
                jdbcTemplate,
                tablePersonneName,
                nomIndex,
                personneCacheList
        );

        nombreDePersonnesUpdatedDansCeChunk.set(0);

        for (TheseModel theseModel : items) {
            nombreDeTheses.incrementAndGet();
            logSiPasAssezDePersonnesDansLaThese(theseModel);
            for (PersonneModelES personneModelES : theseModel.getPersonnes()) {
                nombreDePersonnes.incrementAndGet();
                log.debug("ppn : " + personneModelES.getPpn());
                log.debug("nom : " + personneModelES.getNom());
                if (personneCacheUtils.estPresentEnMemoire(personneModelES.getPpn())) {
                    log.debug("update");
                    personneCacheUtils.updatePersonneEnMemoire(personneModelES);
                    nombreDePersonnesUpdated.incrementAndGet();
                    nombreDePersonnesUpdatedDansCeChunk.incrementAndGet();
                } else {
                    log.debug("ajout");
                    personneCacheUtils.ajoutPersonneEnMemoire(personneModelES);
                }
            }
        }
        jdbcTemplate.update("commit");
        log.info("Nombre de thèses traitées : " + nombreDeTheses.get());
        log.info("Nombre de personnes traitées : " + nombreDePersonnes.get());
        log.info("Nombre de personnes mis à jour dans ce chunk : " + nombreDePersonnesUpdatedDansCeChunk.get());
        log.info("Nombre de personnes mis à jour en tout : " + nombreDePersonnesUpdated.get());
        log.info("Nombre de personnes dans l'index : " + (nombreDePersonnes.intValue() - nombreDePersonnesUpdated.intValue()));

    }

    private void logSiPasAssezDePersonnesDansLaThese(TheseModel theseModel) {
        if (theseModel.getPersonnes().size() < 2) {
            log.warn("Moins de personnes que prévu dans cette theses");
        }
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
    }

    @SneakyThrows
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        log.debug("IndexerDansES afterStep");
        log.debug("Table personne name : " + tablePersonneName);
        log.debug("Index nom : " + nomIndex);
        while (true) {
            BulkRequest.Builder br = new BulkRequest.Builder();

            int pageCourante = page.getAndIncrement();

            log.debug("Indexation de la page " + pageCourante);


            List<PersonnesCacheModel> items = new ArrayList<>();

            for (int i = ((pageCourante) * chunkPersonneES);
                 (i < ((pageCourante) * chunkPersonneES) + chunkPersonneES) && (i<personneCacheList.size());
                 i++) {

                PersonneModelES personneModelES = personneCacheList.get(i);
                items.add(new PersonnesCacheModel(personneModelES.getPpn(), nomIndex, writeJson(personneModelES)));
            }

            if (items.size() == 0) {
                log.debug("Fin de ce thread");
                break;
            }




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
            }

            BulkResponse result = proxyRetry.executerDansES(br);


            if (result.errors()) {
                log.error("Erreurs dans le bulk : ");
                for (BulkResponseItem item : result.items()) {
                    if (item.error() != null) {
                        log.error(item.id() + item.error());
                    }
                }
            }
        }

        return ExitStatus.COMPLETED;
    }

}

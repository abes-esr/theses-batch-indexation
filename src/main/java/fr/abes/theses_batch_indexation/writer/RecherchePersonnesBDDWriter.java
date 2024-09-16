package fr.abes.theses_batch_indexation.writer;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.JsonpMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import fr.abes.theses_batch_indexation.configuration.ElasticClient;
import fr.abes.theses_batch_indexation.database.TheseModel;
import fr.abes.theses_batch_indexation.dto.personne.IModelES;
import fr.abes.theses_batch_indexation.dto.personne.PersonneModelES;
import fr.abes.theses_batch_indexation.dto.personne.RecherchePersonneModelES;
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
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class RecherchePersonnesBDDWriter implements ItemWriter<TheseModel>, StepExecutionListener {

    final ProxyRetry proxyRetry;
    private final MappingJobName mappingJobName;
    private final Environment env;
    private String nomIndex;
    private AtomicInteger nombreDeTheses = new AtomicInteger(0);
    private AtomicInteger nombreDePersonnes = new AtomicInteger(0);
    private AtomicInteger nombreDePersonnesUpdated = new AtomicInteger(0);
    private Map<String, IModelES> recherchePersonneCacheListPpn = new HashMap<>();
    private List<IModelES> recherchePersonneCacheListSansPpn = new ArrayList<>();
    @Value("${job.chunk}")
    private int chunkPersonneES;
    private AtomicInteger page = new AtomicInteger(0);

    private PersonneCacheUtils personneCacheUtils;

    public RecherchePersonnesBDDWriter(
            ProxyRetry proxyRetry, MappingJobName mappingJobName, Environment env) {
        this.proxyRetry = proxyRetry;
        this.mappingJobName = mappingJobName;
        this.env = env;
    }

    @Override
    public void write(List<? extends TheseModel> items) {

        nomIndex = mappingJobName.getNomIndexES().get(env.getProperty("spring.batch.job.names"));

        this.personneCacheUtils = new PersonneCacheUtils(
                nomIndex,
                recherchePersonneCacheListPpn,
                recherchePersonneCacheListSansPpn
        );

        personneCacheUtils.ecrireEnMemoire(items, nombreDeTheses, nombreDePersonnes, nombreDePersonnesUpdated);

    }

    @Override
    public void beforeStep(StepExecution stepExecution) {

    }

    @SneakyThrows
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        log.debug("IndexerDansES afterStep");
        log.debug("Index nom : " + nomIndex);

        recherchePersonneCacheListSansPpn.addAll(recherchePersonneCacheListPpn.values());

        log.debug("Fin du merge des listes");

        personneCacheUtils.indexerDansES(page, chunkPersonneES, recherchePersonneCacheListSansPpn, proxyRetry);

        return ExitStatus.COMPLETED;
    }
}

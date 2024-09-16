package fr.abes.theses_batch_indexation.writer;

import fr.abes.theses_batch_indexation.database.TheseModel;
import fr.abes.theses_batch_indexation.dto.personne.IModelES;
import fr.abes.theses_batch_indexation.utils.MappingJobName;
import fr.abes.theses_batch_indexation.utils.PersonneCacheUtils;
import fr.abes.theses_batch_indexation.utils.ProxyRetry;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class PersonnesBDDWriter implements ItemWriter<TheseModel>, StepExecutionListener {

    final
    ProxyRetry proxyRetry;
    private final Environment env;
    private final MappingJobName mappingJobName;
    private String nomIndex;
    private AtomicInteger nombreDeTheses = new AtomicInteger(0);
    private AtomicInteger nombreDePersonnes = new AtomicInteger(0);
    private AtomicInteger nombreDePersonnesUpdated = new AtomicInteger(0);
    private Map<String, IModelES> personneCacheListPpn = new HashMap<>();
    private List<IModelES> personneCacheListSansPpn = new ArrayList<>();
    private PersonneCacheUtils personneCacheUtils;
    @Value("${job.chunk}")
    private int chunkPersonneES;
    private AtomicInteger page = new AtomicInteger(0);

    public PersonnesBDDWriter(Environment env, MappingJobName mappingJobName, ProxyRetry proxyRetry) {
        this.env = env;
        this.mappingJobName = mappingJobName;
        this.proxyRetry = proxyRetry;
    }

    @Override
    public void write(List<? extends TheseModel> items) {

        nomIndex = mappingJobName.getNomIndexES().get(env.getProperty("spring.batch.job.names"));

        this.personneCacheUtils = new PersonneCacheUtils(
                nomIndex,
                personneCacheListPpn,
                personneCacheListSansPpn
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

        personneCacheListSansPpn.addAll(personneCacheListPpn.values());

        log.debug("Fin du merge des listes");

        personneCacheUtils.indexerDansES(page, chunkPersonneES, personneCacheListSansPpn, proxyRetry);

        return ExitStatus.COMPLETED;
    }

}

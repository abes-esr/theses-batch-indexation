package fr.abes.theses_batch_indexation.configuration;

import fr.abes.theses_batch_indexation.database.TheseModel;
import fr.abes.theses_batch_indexation.notification.JobTheseCompletionNotificationListener;
import fr.abes.theses_batch_indexation.reader.JdbcPagingCustomReader;
import fr.abes.theses_batch_indexation.reader.JdbcPagingDeleteReader;
import fr.abes.theses_batch_indexation.reader.JdbcPersonneReader;
import fr.abes.theses_batch_indexation.utils.XMLJsonMarshalling;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Slf4j
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    protected final DataSource dataSourceLecture;
    private final JobConfig config;
    private final ItemWriteListener<TheseModel> theseWriteListener;
    private final ItemProcessListener<TheseModel, TheseModel> theseProcessListener;
    private final PlatformTransactionManager transactionManager;
    private final JobRepository jobRepository;

    public BatchConfiguration(
            @Qualifier("dataSourceLecture") DataSource dataSourceLecture,
            JobConfig config,
            @Qualifier("theseWriteListener") ItemWriteListener<TheseModel> theseWriteListener,
            @Qualifier("theseProcessListener") ItemProcessListener<TheseModel, TheseModel> theseProcessListener,
            PlatformTransactionManager transactionManager,
            JobRepository jobRepository
    ) {
        this.dataSourceLecture = dataSourceLecture;
        this.config = config;
        this.theseWriteListener = theseWriteListener;
        this.theseProcessListener = theseProcessListener;
        this.transactionManager = transactionManager;
        this.jobRepository = jobRepository;
    }

    // ---------- JOB ---------------------------------------------

    @Bean
    public Job jobIndexationThesesDansES(Step stepIndexThesesDansES,
                                         JobRepository jobRepository,
                                         Tasklet initialiserIndexESTasklet,
                                         Tasklet chargerOaiSetsTasklet,
                                         JobTheseCompletionNotificationListener listener) {
        log.debug("debut du job indexation des theses dans ES...");

        return new JobBuilder("indexationThesesDansES", jobRepository).incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(stepChargerListeOaiSets(chargerOaiSetsTasklet))
                .next(stepInitialiserIndexES(initialiserIndexESTasklet))
                .next(stepIndexThesesDansES)
                .build();
    }

    @Bean
    public Job jobIndexationPersonnesDansES(Step stepIndexPersonnesDansBDD,
                                            Tasklet initialiserIndexEsPersonneTasklet,
                                            Tasklet chargerOaiSetsTasklet,
                                            Tasklet changerIndexAliasTasklet,
                                            JobTheseCompletionNotificationListener listener) {
        return new JobBuilder("indexationPersonnesDansES", jobRepository).incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(stepChargerListeOaiSets(chargerOaiSetsTasklet))
                .next(stepInitialiserIndexESPersonne(initialiserIndexEsPersonneTasklet))
                .next(stepIndexPersonnesDansBDD)
                .next(stepChangerIndexAlias(changerIndexAliasTasklet))
                .build();
    }

    @Bean
    public Job jobIndexationPersonnesDeBddVersES(Tasklet initialiserIndexEsPersonneTasklet,
                                                 JobTheseCompletionNotificationListener listener,
                                                 Tasklet indexerPersonnesDansESTasklet) {
        return new JobBuilder("indexationPersonnesDeBddVersES", jobRepository).incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(stepInitialiserIndexESPersonne(initialiserIndexEsPersonneTasklet))
                .next(stepIndexerPersonnesDansESTasklet(indexerPersonnesDansESTasklet))
                .build();
    }

    @Bean
    public Job jobIndexationRecherchePersonnesDansES(Step stepIndexRecherchePersonnesDansBDD,
                                                     Tasklet initialiserIndexEsPersonneTasklet,
                                                     Tasklet chargerOaiSetsTasklet,
                                                     Tasklet changerIndexAliasTasklet,
                                                     JobTheseCompletionNotificationListener listener) {
        return new JobBuilder("indexationRecherchePersonnesDansES", jobRepository).incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(stepChargerListeOaiSets(chargerOaiSetsTasklet))
                .next(stepInitialiserIndexESPersonne(initialiserIndexEsPersonneTasklet))
                .next(stepIndexRecherchePersonnesDansBDD)
                .next(stepChangerIndexAlias(changerIndexAliasTasklet))
                .build();
    }
    @Bean
    public Job jobIndexationRecherchePersonnesDeBddVersES(Tasklet initialiserIndexEsPersonneTasklet,
                                                 JobTheseCompletionNotificationListener listener,
                                                 Tasklet indexerPersonnesDansESTasklet) {
        return new JobBuilder("indexationRecherchePersonnesDeBddVersES", jobRepository).incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(stepInitialiserIndexESPersonne(initialiserIndexEsPersonneTasklet))
                .next(stepIndexerPersonnesDansESTasklet(indexerPersonnesDansESTasklet))
                .build();
    }

    @Bean
    public Job jobIndexationThematiquesDansES(Step stepIndexThematiquesDansES,
                                              JobRepository jobRepository,
                                              Tasklet initialiserIndexESTasklet,
                                              JobTheseCompletionNotificationListener listener) {
        log.debug("debut du job indexation des thematiques dans ES...");

        return new JobBuilder("indexationThematiquesDansES", jobRepository).incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(stepInitialiserIndexES(initialiserIndexESTasklet))
                .next(stepIndexThematiquesDansES)
                .build();
    }

    // ---------- JOB SUPPRESSION ---------------------------------

    @Bean
    public Job jobSuppressionThesesDansES(Step stepSupprimeThesesOuThematiquesDansES,
                                          JobRepository jobRepository,
                                          JobTheseCompletionNotificationListener listener) {
        log.debug("debut du job de suppression des theses dans ES...");

        return new JobBuilder("suppressionThesesDansES", jobRepository).incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(stepSupprimeThesesOuThematiquesDansES)
                .build();
    }

    @Bean
    public Job jobSuppressionThematiquesDansES(Step stepSupprimeThesesOuThematiquesDansES,
                                               JobRepository jobRepository,
                                               JobTheseCompletionNotificationListener listener) {
        log.debug("debut du job de suppression des thématiques dans ES...");

        return new JobBuilder("suppressionThematiquesDansES", jobRepository).incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(stepSupprimeThesesOuThematiquesDansES)
                .build();
    }

    // ---------- STEP --------------------------------------------
    @Bean
    public Step stepIndexThesesDansES(@Qualifier("jdbcPagingCustomReader") JdbcPagingCustomReader itemReader,
                                      @Qualifier("theseItemProcessor") ItemProcessor itemProcessor,
                                      @Qualifier("ESItemWriter") ItemWriter itemWriter) {
        return new StepBuilder("stepIndexationThese", jobRepository)
                .<TheseModel, TheseModel>chunk(config.getChunk(), transactionManager)
                .listener(theseWriteListener)
                .reader(itemReader)
                .processor(itemProcessor)
                .listener(theseProcessListener)
                .writer(itemWriter)
                .taskExecutor(taskExecutor())
                .throttleLimit(config.getThrottle())
                .build();
    }

    @Bean
    public Step stepIndexThematiquesDansES(@Qualifier("jdbcPagingCustomReader") JdbcPagingCustomReader itemReader,
                                           @Qualifier("thematiqueItemProcessor") ItemProcessor itemProcessor,
                                           @Qualifier("ESItemWriter") ItemWriter itemWriter) {
        return new StepBuilder("stepIndexationThematique", jobRepository)
                .<TheseModel, TheseModel>chunk(config.getChunk(), transactionManager)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .taskExecutor(taskExecutor())
                .throttleLimit(config.getThrottle())
                .build();
    }

    @Bean
    public Step stepIndexPersonnesDansBDD(@Qualifier("jdbcPagingPersonnesCustomReader") ItemReader itemReader,
                                          @Qualifier("personneItemProcessor") ItemProcessor itemProcessor,
                                          @Qualifier("personnesBDDWriter") ItemWriter itemWriter) {
        return new StepBuilder("stepIndexationPersonne", jobRepository)
                .chunk(config.getChunk(), transactionManager)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }

    @Bean
    public Step stepIndexRecherchePersonnesDansBDD(@Qualifier("jdbcPagingPersonnesCustomReader") ItemReader itemReader,
                                                   @Qualifier("recherchePersonneItemProcessor") ItemProcessor itemProcessor,
                                                   @Qualifier("recherchePersonnesBDDWriter") ItemWriter itemWriter) {
        return new StepBuilder("stepIndexationRecherchePersonne", jobRepository)
                .chunk(config.getChunk(), transactionManager)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }

    @Bean
    public Step stepInitialiserIndexES(@Qualifier("initialiserIndexESTasklet") Tasklet t) {
        return new StepBuilder("InitialiserIndexESTasklet", jobRepository).allowStartIfComplete(true)
                .tasklet(t, transactionManager).build();
    }

    @Bean
    public Step stepChangerIndexAlias(@Qualifier("changerIndexAliasTasklet") Tasklet t) {
        return new StepBuilder("ChangerIndexAliasTasklet", jobRepository).allowStartIfComplete(true)
                .tasklet(t, transactionManager).build();
    }

    @Bean
    public Step stepInitialiserIndexESPersonne(@Qualifier("initialiserIndexEsPersonneTasklet") Tasklet t) {
        return new StepBuilder("stepInitialiserIndexESPersonne", jobRepository).allowStartIfComplete(true)
                .tasklet(t, transactionManager).build();
    }

    @Bean
    public Step stepInitiliserIndexBDDTasklet(@Qualifier("initiliserIndexBDDTasklet") Tasklet t) {
        return new StepBuilder("stepInitiliserIndexBDDTasklet", jobRepository).allowStartIfComplete(true)
                .tasklet(t, transactionManager).build();
    }

    @Bean
    public Step stepIndexerPersonnesDansESTasklet(@Qualifier("indexerPersonnesDansESTasklet") Tasklet t) {
        return new StepBuilder("stepIndexerPersonnesDansESTasklet", jobRepository).allowStartIfComplete(true)
                .tasklet(t, transactionManager)
                .taskExecutor(taskExecutor())
                .throttleLimit(config.getThrottle())
                .build();
    }

    @Bean
    public Step stepChargerListeOaiSets(@Qualifier("chargerOaiSetsTasklet") Tasklet t) {
        return new StepBuilder("ChargerOaiSetsTasklet", jobRepository).allowStartIfComplete(true)
                .tasklet(t, transactionManager).build();
    }

    @Bean
    public Step stepSupprimeThesesOuThematiquesDansES(@Qualifier("jdbcPagingDeleteReader") JdbcPagingDeleteReader itemReader,
                                                      @Qualifier("ESDeleteWriter") ItemWriter itemWriter) {
        return new StepBuilder("stepSuppressionThese", jobRepository)
                .<TheseModel, TheseModel>chunk(config.getChunk(), transactionManager)
                .listener(theseWriteListener)
                .reader(itemReader)
                .writer(itemWriter)
                .taskExecutor(taskExecutor())
                .throttleLimit(config.getThrottle())
                .build();
    }


    // ---------------- TASK EXECUTOR ----------------------------
    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }

    // --------------------- Utilitaires --------------------------------

    @Bean
    public XMLJsonMarshalling xmlJsonMarshalling() {
        return new XMLJsonMarshalling();
    }
}

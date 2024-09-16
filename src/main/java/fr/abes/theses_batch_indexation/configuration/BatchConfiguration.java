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
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import javax.sql.DataSource;

@Slf4j
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    protected final JobBuilderFactory jobs;
    protected final StepBuilderFactory stepBuilderFactory;
    protected final DataSource dataSourceLecture;
    private final JobConfig config;
    private final ItemWriteListener<TheseModel> theseWriteListener;
    private final ItemProcessListener<TheseModel, TheseModel> theseProcessListener;

    public BatchConfiguration(JobBuilderFactory jobs, StepBuilderFactory stepBuilderFactory, @Qualifier("dataSourceLecture") DataSource dataSourceLecture, JobConfig config, @Qualifier("theseWriteListener") ItemWriteListener<TheseModel> theseWriteListener, @Qualifier("theseProcessListener") ItemProcessListener<TheseModel, TheseModel> theseProcessListener) {
        this.jobs = jobs;
        this.stepBuilderFactory = stepBuilderFactory;
        this.dataSourceLecture = dataSourceLecture;
        this.config = config;
        this.theseWriteListener = theseWriteListener;
        this.theseProcessListener = theseProcessListener;
    }

    // ---------- JOB ---------------------------------------------

    @Bean
    public Job jobIndexationThesesDansES(Step stepIndexThesesDansES,
                                         JobRepository jobRepository,
                                         Tasklet initialiserIndexESTasklet,
                                         Tasklet chargerOaiSetsTasklet,
                                         JobTheseCompletionNotificationListener listener) {
        log.debug("debut du job indexation des theses dans ES...");

        return jobs.get("indexationThesesDansES").repository(jobRepository).incrementer(new RunIdIncrementer())
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
        return jobs.get("indexationPersonnesDansES").incrementer(new RunIdIncrementer())
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
        return jobs.get("indexationPersonnesDeBddVersES").incrementer(new RunIdIncrementer())
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
        return jobs.get("indexationRecherchePersonnesDansES").incrementer(new RunIdIncrementer())
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
        return jobs.get("indexationRecherchePersonnesDeBddVersES").incrementer(new RunIdIncrementer())
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

        return jobs.get("indexationThematiquesDansES").repository(jobRepository).incrementer(new RunIdIncrementer())
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

        return jobs.get("suppressionThesesDansES").repository(jobRepository).incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(stepSupprimeThesesOuThematiquesDansES)
                .build();
    }

    @Bean
    public Job jobSuppressionThematiquesDansES(Step stepSupprimeThesesOuThematiquesDansES,
                                               JobRepository jobRepository,
                                               JobTheseCompletionNotificationListener listener) {
        log.debug("debut du job de suppression des thématiques dans ES...");

        return jobs.get("suppressionThematiquesDansES").repository(jobRepository).incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(stepSupprimeThesesOuThematiquesDansES)
                .build();
    }

    // ---------- STEP --------------------------------------------
    @Bean
    public Step stepIndexThesesDansES(@Qualifier("jdbcPagingCustomReader") JdbcPagingCustomReader itemReader,
                                      @Qualifier("theseItemProcessor") ItemProcessor itemProcessor,
                                      @Qualifier("ESItemWriter") ItemWriter itemWriter) {
        return stepBuilderFactory.get("stepIndexationThese").<TheseModel, TheseModel>chunk(config.getChunk())
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
        return stepBuilderFactory.get("stepIndexationThematique").<TheseModel, TheseModel>chunk(config.getChunk())
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
        return stepBuilderFactory.get("stepIndexationPersonne").chunk(config.getChunk())
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }

    @Bean
    public Step stepIndexRecherchePersonnesDansBDD(@Qualifier("jdbcPagingPersonnesCustomReader") ItemReader itemReader,
                                                   @Qualifier("recherchePersonneItemProcessor") ItemProcessor itemProcessor,
                                                   @Qualifier("recherchePersonnesBDDWriter") ItemWriter itemWriter) {
        return stepBuilderFactory.get("stepIndexationRecherchePersonne").chunk(config.getChunk())
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }

    @Bean
    public Step stepInitialiserIndexES(@Qualifier("initialiserIndexESTasklet") Tasklet t) {
        return stepBuilderFactory.get("InitialiserIndexESTasklet").allowStartIfComplete(true)
                .tasklet(t).build();
    }

    @Bean
    public Step stepChangerIndexAlias(@Qualifier("changerIndexAliasTasklet") Tasklet t) {
        return stepBuilderFactory.get("ChangerIndexAliasTasklet").allowStartIfComplete(true)
                .tasklet(t).build();
    }

    @Bean
    public Step stepInitialiserIndexESPersonne(@Qualifier("initialiserIndexEsPersonneTasklet") Tasklet t) {
        return stepBuilderFactory.get("stepInitialiserIndexESPersonne").allowStartIfComplete(true)
                .tasklet(t).build();
    }

    @Bean
    public Step stepInitiliserIndexBDDTasklet(@Qualifier("initiliserIndexBDDTasklet") Tasklet t) {
        return stepBuilderFactory.get("stepInitiliserIndexBDDTasklet").allowStartIfComplete(true)
                .tasklet(t).build();
    }

    @Bean
    public Step stepIndexerPersonnesDansESTasklet(@Qualifier("indexerPersonnesDansESTasklet") Tasklet t) {
        return stepBuilderFactory.get("stepIndexerPersonnesDansESTasklet").allowStartIfComplete(true)
                .tasklet(t)
                .taskExecutor(taskExecutor())
                .throttleLimit(config.getThrottle())
                .build();
    }

    @Bean
    public Step stepChargerListeOaiSets(@Qualifier("chargerOaiSetsTasklet") Tasklet t) {
        return stepBuilderFactory.get("ChargerOaiSetsTasklet").allowStartIfComplete(true)
                .tasklet(t).build();
    }

    @Bean
    public Step stepSupprimeThesesOuThematiquesDansES(@Qualifier("jdbcPagingDeleteReader") JdbcPagingDeleteReader itemReader,
                                                      @Qualifier("ESDeleteWriter") ItemWriter itemWriter) {
        return stepBuilderFactory.get("stepSuppressionThese").<TheseModel, TheseModel>chunk(config.getChunk())
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

package fr.abes.theses_batch_indexation.configuration;

import fr.abes.theses_batch_indexation.dto.these.TheseDTO;
import fr.abes.theses_batch_indexation.notification.JobTheseCompletionNotificationListener;
import fr.abes.theses_batch_indexation.reader.TheseItemReader;
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
import org.springframework.batch.item.ItemProcessor;
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

    private final ItemWriteListener<TheseDTO> theseWriteListener;
    private final ItemProcessListener<TheseDTO, TheseDTO> theseProcessListener;

    private final TheseItemReader theseItemReader;

    public BatchConfiguration(JobBuilderFactory jobs, StepBuilderFactory stepBuilderFactory, @Qualifier("dataSourceLecture") DataSource dataSourceLecture, JobConfig config, @Qualifier("theseWriteListener") ItemWriteListener<TheseDTO> theseWriteListener, @Qualifier("theseProcessListener") ItemProcessListener<TheseDTO, TheseDTO> theseProcessListener, TheseItemReader theseItemReader) {
        this.jobs = jobs;
        this.stepBuilderFactory = stepBuilderFactory;
        this.dataSourceLecture = dataSourceLecture;
        this.config = config;
        this.theseWriteListener = theseWriteListener;
        this.theseProcessListener = theseProcessListener;
        this.theseItemReader = theseItemReader;
    }

    // ---------- JOB ---------------------------------------------

    @Bean
    public Job jobIndexationThesesDansES(Step stepIndexThesesDansES, JobRepository jobRepository,
            JobTheseCompletionNotificationListener listener) {
        log.info("debut du job indexation des theses dans ES...");

        return jobs.get("indexationThesesDansES").repository(jobRepository).incrementer(new RunIdIncrementer())
                .listener(listener).flow(stepIndexThesesDansES).end().build();
    }

    // ---------- STEP --------------------------------------------
    @Bean
    public Step stepIndexThesesDansES(@Qualifier("theseItemProcessor") ItemProcessor itemProcessor,
                                      @Qualifier("thesesESItemWriter")ItemWriter itemWriter) {
        return stepBuilderFactory.get("stepIndexationThese").<TheseDTO, TheseDTO>chunk(config.getChunk())
                .listener(theseWriteListener)
                .reader(theseItemReader.read())
                .processor(itemProcessor)
                .listener(theseProcessListener)
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
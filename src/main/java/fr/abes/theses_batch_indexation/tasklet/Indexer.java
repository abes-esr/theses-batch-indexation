package fr.abes.theses_batch_indexation.tasklet;

import fr.abes.theses_batch_indexation.notification.JobTheseCompletionNotificationListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Indexer implements Tasklet {
    private final Job jobIndexationThesesDansES;
    private final Step stepIndexThesesDansES;
    private final InitialiserIndexESTasklet initialiserIndexESTasklet;
    private final ChargerOaiSetsTasklet chargerOaiSetsTasklet;
    private final JobTheseCompletionNotificationListener listener;

    private final JobRepository jobRepository;

    public Indexer(@Qualifier("jobIndexationThesesDansES") Job jobIndexationThesesDansES,
                   @Qualifier("stepIndexThesesDansES") Step stepIndexThesesDansES,
                   InitialiserIndexESTasklet initialiserIndexESTasklet,
                   ChargerOaiSetsTasklet chargerOaiSetsTasklet,
                   JobTheseCompletionNotificationListener listener,
                   JobRepository jobRepository) {
        this.jobIndexationThesesDansES = jobIndexationThesesDansES;
        this.stepIndexThesesDansES = stepIndexThesesDansES;
        this.initialiserIndexESTasklet = initialiserIndexESTasklet;
        this.chargerOaiSetsTasklet = chargerOaiSetsTasklet;
        this.listener = listener;
        this.jobRepository = jobRepository;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {


        assert jobIndexationThesesDansES(stepIndexThesesDansES,
                jobRepository,
                initialiserIndexESTasklet,
                chargerOaiSetsTasklet,
                listener) != null;
        jobIndexationThesesDansES(stepIndexThesesDansES,
                jobRepository,
                initialiserIndexESTasklet,
                chargerOaiSetsTasklet,
                listener)
                .execute(stepContribution, chunkContext);

        chunkContext.get


        return null;
    }

    private Tasklet jobIndexationThesesDansES(Step stepIndexThesesDansES,
                                              JobRepository jobRepository,
                                              InitialiserIndexESTasklet initialiserIndexESTasklet,
                                              ChargerOaiSetsTasklet chargerOaiSetsTasklet,
                                              JobTheseCompletionNotificationListener listener) {
        return null;
    }
}

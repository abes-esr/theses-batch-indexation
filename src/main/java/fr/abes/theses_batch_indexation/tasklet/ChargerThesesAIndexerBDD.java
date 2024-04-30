package fr.abes.theses_batch_indexation.tasklet;

import fr.abes.theses_batch_indexation.database.DbService;
import fr.abes.theses_batch_indexation.utils.MappingJobName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ChargerThesesAIndexerBDD implements Tasklet {

    @Autowired
    Environment env;

    @Autowired
    DbService dbService;

    @Autowired
    MappingJobName mappingJobName;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        dbService.mettreToutesLesThesesAIndexer(mappingJobName.getNomTableES().get(env.getProperty("spring.batch.job.names")));
        log.info("table d'indexation "+ mappingJobName.getNomTableES().get(env.getProperty("spring.batch.job.names"))+" remplie dans la base.");

        return RepeatStatus.FINISHED;
    }
}

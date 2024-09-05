package fr.abes.theses_batch_indexation.tasklet;

import fr.abes.theses_batch_indexation.utils.MappingJobName;
import fr.abes.theses_batch_indexation.utils.NomIndexSelecteur;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class InitiliserIndexBDDTasklet implements Tasklet {


    private final MappingJobName mappingJobName;

    @Value("${table.personne.name}")
    private String tablePersonneName;

    private final JdbcTemplate jdbcTemplate;


    public InitiliserIndexBDDTasklet(JdbcTemplate jdbcTemplate, MappingJobName mappingJobName) {
        this.jdbcTemplate = jdbcTemplate;
        this.mappingJobName = mappingJobName;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {


        String nomIndex = mappingJobName.getNomIndexES().get(chunkContext.getStepContext().getJobName());

        jdbcTemplate.update("delete from " + tablePersonneName + " where nom_index = ?", nomIndex);
        jdbcTemplate.update("commit");

        log.info("Suppresion de l'index " + nomIndex + " dans la table " + tablePersonneName);

        return RepeatStatus.FINISHED;
    }
}

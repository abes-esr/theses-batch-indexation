package fr.abes.theses_batch_indexation.tasklet;

import fr.abes.theses_batch_indexation.utils.ElasticSearchUtils;
import fr.abes.theses_batch_indexation.utils.MappingJobName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Slf4j
public class InitialiserIndexEsPersonneTasklet implements Tasklet {

    @Value("${index.pathTheses}")
    private String pathTheses;

    @Value("${index.pathPersonnes}")
    private String pathPersonnes;

    @Value("${index.pathThematiques}")
    private String pathThematiques;

    @Value("${index.pathRecherchePersonnes}")
    private String pathRecherchePersonnes;

    @Autowired
    Environment env;

    @Autowired
    MappingJobName mappingJobName;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        String nomIndex = mappingJobName.getNomIndexES().get(env.getProperty("spring.batch.job.names"));
        ElasticSearchUtils elasticSearchUtils = new ElasticSearchUtils(nomIndex);

        if (env.getProperty("initialiseIndexPersonnes") != null &&
                env.getProperty("initialiseIndexPersonnes").equals("true")) {

            log.warn("Réinitialisation de l'index " + nomIndex);
            File f = selectIndex();

            if (f != null) {
                elasticSearchUtils.deleteIndexES(nomIndex);
                log.info("Index " + nomIndex + " supprimé");
                elasticSearchUtils.createIndexES(f, nomIndex);
                log.info("Index " + nomIndex + " créé avec le schéma présent dans " + f.getPath());
            }
        }

        if (elasticSearchUtils.countIndex() > 1) {
            throw new Exception("Index non réinitialisé");
        }



        return RepeatStatus.FINISHED;
    }

    private File selectIndex() {
        File f = null;

        switch (env.getProperty("spring.batch.job.names")) {
            case "indexationThesesDansES" :
                f = new File(pathTheses);
                break;
            case "indexationPersonnesDeBddVersES" :
            case "indexationPersonnesDansES" :
                f = new File(pathPersonnes);
                break;
            case "indexationRecherchePersonnesDansES" :
            case "indexationRecherchePersonnesDeBddVersES" :
                f = new File(pathRecherchePersonnes);
                break;
            case "indexationThematiquesDansES" :
                f = new File(pathThematiques);
                break;
        }
        return f;
    }


}

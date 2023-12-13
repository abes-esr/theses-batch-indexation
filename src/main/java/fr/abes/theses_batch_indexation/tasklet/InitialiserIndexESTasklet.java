package fr.abes.theses_batch_indexation.tasklet;

import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import fr.abes.theses_batch_indexation.configuration.ElasticClient;
import fr.abes.theses_batch_indexation.database.DbService;
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
import java.io.FileInputStream;
import java.io.IOException;

@Component
@Slf4j
public class InitialiserIndexESTasklet implements Tasklet {

    @Value("${index.pathTheses}")
    private String pathTheses;

    @Value("${index.pathPersonnes}")
    private String pathPersonnes;

    @Value("${index.pathThematiques}")
    private String pathThematiques;

    @Value("${index.pathRecherchePersonnes}")
    private String pathRecherchePersonnes;

    @Value("${initialiseIndex}")
    private Boolean initialiseIndex;

    @Autowired
    Environment env;

    @Autowired
    DbService dbService;

    @Autowired
    MappingJobName mappingJobName;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        if (env.getProperty("initialiseIndex").equals(true)) {
            String nomIndex = mappingJobName.getNomIndexES().get(env.getProperty("spring.batch.job.names"));
            log.warn("Réinitialisation de l'index " + nomIndex);
            File f = selectIndex();

            if (f != null) {
                deleteIndexES(nomIndex);
                log.info("Index " + nomIndex + " supprimé");
                createIndexES(f, nomIndex);
                log.info("Index " + nomIndex + " créé avec le schéma présent dans " + f.getPath());
                dbService.mettreToutesLesThesesAIndexer(mappingJobName.getNomTableES().get(env.getProperty("spring.batch.job.names")));
                log.info("table d'indexation "+ mappingJobName.getNomTableES().get(env.getProperty("spring.batch.job.names"))+" remplie dans la base.");
            }
        }
        return RepeatStatus.FINISHED;
    }

    private File selectIndex() {
        File f = null;

        switch (env.getProperty("spring.batch.job.names")) {
            case "indexationThesesDansES" :
                f = new File(pathTheses);
                break;
            case "indexationPersonnesDansES" :
                f = new File(pathPersonnes);
                break;
            case "indexationRecherchePersonnesDansES" :
                f = new File(pathRecherchePersonnes);
                break;
            case "indexationThematiquesDansES" :
                f = new File(pathThematiques);
                break;
        }
        return f;
    }

    private void createIndexES(File f, String nomIndex) throws IOException {
        CreateIndexRequest.Builder builder = new CreateIndexRequest.Builder();
        builder.index(nomIndex.toLowerCase());
        builder.withJson(new FileInputStream(f));

        CreateIndexResponse response = ElasticClient.getElasticsearchClient().indices().create(builder.build());

        if (!response.acknowledged()) {
            log.error("Erreur dans createIndexES()");
        }
    }

    private void deleteIndexES(String nomIndex) throws IOException {
        DeleteIndexRequest.Builder builder = new DeleteIndexRequest.Builder();
        builder.index(nomIndex.toLowerCase());
        builder.ignoreUnavailable(true);

        DeleteIndexRequest deleteIndexRequest = builder.build();
        DeleteIndexResponse response = ElasticClient.getElasticsearchClient().indices().delete(deleteIndexRequest);

        if (!response.acknowledged()) {
            log.error("Erreur dans deleteIndexES()");
        }
    }
}

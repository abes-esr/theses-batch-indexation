package fr.abes.theses_batch_indexation.tasklet;

import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import fr.abes.theses_batch_indexation.configuration.ElasticClient;
import fr.abes.theses_batch_indexation.database.DbService;
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

    @Value("${index.name}")
    private String nomIndex;

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

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        if (initialiseIndex) {
            log.warn("Réinitialisation de l'index " + nomIndex.toLowerCase());
            File f = selectIndex();

            if (f != null) {
                //delete
                deleteIndexES();
                log.info("Index " + nomIndex.toLowerCase() + " supprimé");
                //create
                createIndexES(f);
                log.info("Index " + nomIndex.toLowerCase() + " créé avec le schéma présent dans " + f.getPath());
                dbService.mettreToutesLesThesesAIndexer();
                log.info("table d'indexation remplie dans la base.");
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

    private void createIndexES(File f) throws IOException {
        CreateIndexRequest.Builder builder = new CreateIndexRequest.Builder();
        builder.index(nomIndex.toLowerCase());
        builder.withJson(new FileInputStream(f));

        CreateIndexResponse response = ElasticClient.getElasticsearchClient().indices().create(builder.build());

        if (!response.acknowledged()) {
            log.error("Erreur dans createIndexES()");
        }
    }

    private void deleteIndexES() throws IOException {
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

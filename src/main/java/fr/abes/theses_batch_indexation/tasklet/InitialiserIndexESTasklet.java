package fr.abes.theses_batch_indexation.tasklet;

import fr.abes.theses_batch_indexation.configuration.ElasticConfig;
import fr.abes.theses_batch_indexation.utils.UnsafeOkHttpClient;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.Nullable;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Component
@Slf4j
public class InitialiserIndexESTasklet implements Tasklet {

    @Value("${index.name}")
    private String nomIndex;

    @Value("${elastic.basicAuth}")
    private String basicAuth;

    private final ElasticConfig elasticConfig;

    public InitialiserIndexESTasklet(ElasticConfig elasticConfig) {
        this.elasticConfig = elasticConfig;
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        OkHttpClient client = new UnsafeOkHttpClient().getUnsafeOkHttpClient();

        File f = selectIndex();

        if (f != null) {
            //delete
            deleteIndexES(client);
            log.info("Index " + nomIndex.toLowerCase()+ " supprimé");
            //create
            createIndexES(client, f);
            log.info("Index " + nomIndex.toLowerCase()+ " créé avec le schéma présent dans " + f.getPath());
        }

        return RepeatStatus.FINISHED;
    }

    @Nullable
    private File selectIndex() {
        File f = null;

        if (nomIndex.startsWith("THE_")) {
            f = new File("src/main/resources/indexs/theses.json");
        }
        if (nomIndex.startsWith("PER_")) {
            f = new File("src/main/resources/indexs/personnes.json");
        }
        return f;
    }

    private void createIndexES(OkHttpClient client, File f) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, Files.readString(f.toPath()));
        Request request = new Request.Builder()
                .url(elasticConfig.getScheme()
                        + "://" + elasticConfig.getHostname() + ":"
                        + elasticConfig.getPort() + "/"
                        + nomIndex.toLowerCase() )
                .method("PUT", body)
                .addHeader("Authorization", basicAuth)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
    }

    private void deleteIndexES(OkHttpClient client) throws IOException {
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(elasticConfig.getScheme()
                        + "://" + elasticConfig.getHostname() + ":"
                        + elasticConfig.getPort() + "/"
                        + nomIndex.toLowerCase() )
                .method("DELETE", body)
                .addHeader("Authorization", basicAuth)
                .build();
        Response response = client.newCall(request).execute();
    }
}

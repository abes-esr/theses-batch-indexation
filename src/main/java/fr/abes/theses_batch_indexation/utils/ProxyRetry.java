package fr.abes.theses_batch_indexation.utils;

import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import fr.abes.theses_batch_indexation.configuration.ElasticClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class ProxyRetry {

    @Retryable(maxAttempts = 4, backoff = @Backoff(delay = 300000, multiplier = 2) )
    public BulkResponse executerDansES(BulkRequest.Builder br) throws IOException {
        return ElasticClient.getElasticsearchClient().bulk(br.build());
    }
}

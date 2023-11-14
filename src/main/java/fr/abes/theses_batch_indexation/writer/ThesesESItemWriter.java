package fr.abes.theses_batch_indexation.writer;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.JsonpMapper;
import fr.abes.theses_batch_indexation.configuration.ElasticClient;
import fr.abes.theses_batch_indexation.database.DbService;
import fr.abes.theses_batch_indexation.database.TableIndexationES;
import fr.abes.theses_batch_indexation.database.TheseModel;
import fr.abes.theses_batch_indexation.utils.ProxyRetry;
import jakarta.json.spi.JsonProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class ThesesESItemWriter implements ItemWriter<TheseModel> {

    @Value("${index.name}")
    private String nomIndex;

    @Autowired
    ProxyRetry proxyRetry;

    @Autowired
    DbService dbService;

    @Autowired
    private Environment env;

    @Override
    public void write(List<? extends TheseModel> items) throws Exception {

        BulkRequest.Builder br = new BulkRequest.Builder();

        for (TheseModel theseModel : items) {
            if (
                    theseModel.getCodeEtab() != null &&
                    (theseModel.getCodeEtab().equals("FOR1") || theseModel.getCodeEtab().equals("FOR2"))
                    && Arrays.asList(env.getActiveProfiles()).contains("prod")
            )
                continue;

            JsonData json = readJson(new ByteArrayInputStream(theseModel.getJsonThese().getBytes()), ElasticClient.getElasticsearchClient());

            br.operations(op -> op
                    .index(idx -> idx
                            .index(nomIndex.toLowerCase())
                            .id(theseModel.getNnt() == null? theseModel.getIdSujet() : theseModel.getNnt())
                            .document(json)
                    )
            );
        }

        BulkResponse result = proxyRetry.executerDansES(br);

        for (BulkResponseItem item: result.items()) {

            if (item.error() != null) {
                log.error(item.error().reason().concat(" pour ").concat(item.id()));
            }
            else {
                dbService.supprimerTheseATraiter(item.id(), TableIndexationES.indexation_es_these);
            }
        }
    }

    public static JsonData readJson(InputStream input, ElasticsearchClient esClient) {
        JsonpMapper jsonpMapper = esClient._transport().jsonpMapper();
        JsonProvider jsonProvider = jsonpMapper.jsonProvider();

        return JsonData.from(jsonProvider.createParser(input), jsonpMapper);
    }
}

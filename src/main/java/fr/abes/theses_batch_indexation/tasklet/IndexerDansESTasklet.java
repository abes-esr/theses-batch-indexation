package fr.abes.theses_batch_indexation.tasklet;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.JsonpMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.abes.theses_batch_indexation.configuration.ElasticClient;
import fr.abes.theses_batch_indexation.database.TheseModel;
import fr.abes.theses_batch_indexation.dto.personne.PersonneModelES;
import jakarta.json.spi.JsonProvider;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
@Slf4j
public class IndexerDansESTasklet implements Tasklet {

    @Value("${index.name}")
    private String nomIndex;

    @Value("${chunk.personneES}")
    private int chunkPersonneES;

    @Value("${table.personne.name}")
    private String tablePersonneName;

    private AtomicInteger page = new AtomicInteger(0);

    private final JdbcTemplate jdbcTemplate;

    public IndexerDansESTasklet(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        log.info("IndexerDansESTasklet");
        while (true) {
            BulkRequest.Builder br = new BulkRequest.Builder();

            log.info("Indexation de la page " + page.get());

            List<Map<String, Object>> r = jdbcTemplate.queryForList(
                    "select * from " + tablePersonneName + " where nom_index = ? " +
                            "OFFSET " + chunkPersonneES * page.getAndIncrement() +
                            " ROWS FETCH NEXT " + chunkPersonneES + " ROWS ONLY",
                    nomIndex);

            List<PersonneModelES> items = r.stream().map(p -> mapperJson((String) p.get("PERSONNE"))).collect(Collectors.toList());
            boolean auMoinsUneOperation = false;
            for (PersonneModelES personneModelES : items) {
                JsonData json = readJson(
                        new ByteArrayInputStream(
                                modelToJson(personneModelES).getBytes()),
                        ElasticClient.getElasticsearchClient()
                );

                br.operations(op -> op
                        .index(idx -> idx
                                .index(nomIndex.toLowerCase())
                                .id(personneModelES.getPpn())
                                .document(json)
                        )
                );
                auMoinsUneOperation = true;
            }
            if (auMoinsUneOperation) {
                BulkResponse result = ElasticClient.getElasticsearchClient().bulk(br.build());

                if (result.errors()) {
                    log.error("Erreurs dans le bulk : ");
                    for (BulkResponseItem item : result.items()) {
                        if (item.error() != null) {
                            log.error(item.error().reason());
                        }
                    }
                }
            }
            if (r.size() < chunkPersonneES) {
                break;
            }
        }

        return RepeatStatus.FINISHED;

    }

    public static PersonneModelES mapperJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, PersonneModelES.class);
        } catch (Exception e) {
            log.error("mapperJson");
        }
        return null;
    }

    public static JsonData readJson(InputStream input, ElasticsearchClient esClient) {
        JsonpMapper jsonpMapper = esClient._transport().jsonpMapper();
        JsonProvider jsonProvider = jsonpMapper.jsonProvider();

        return JsonData.from(jsonProvider.createParser(input), jsonpMapper);
    }

    public static String modelToJson(PersonneModelES personneModelES) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(personneModelES);
    }
}

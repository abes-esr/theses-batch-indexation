package fr.abes.theses_batch_indexation.writer;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.JsonpMapper;
import com.google.gson.Gson;
import fr.abes.theses_batch_indexation.configuration.ElasticClient;
import fr.abes.theses_batch_indexation.configuration.ElasticConfig;
import fr.abes.theses_batch_indexation.dto.personne.PersonneModelES;
import fr.abes.theses_batch_indexation.database.TheseModel;
import fr.abes.theses_batch_indexation.utils.UnsafeOkHttpClient;
import jakarta.json.spi.JsonProvider;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.util.List;

@Component
@Slf4j
public class PersonnesESWriter implements ItemWriter<TheseModel> {

    @Value("${index.name}")
    private String nomIndex;

    @Value("${elastic.basicAuth}")
    private String basicAuth;


    private final ElasticConfig elasticConfig;

    public PersonnesESWriter(ElasticConfig elasticConfig) {
        this.elasticConfig = elasticConfig;
    }

    @Override
    public void write(List<? extends TheseModel> items) throws Exception {


        BulkRequest.Builder br = new BulkRequest.Builder();

        for (TheseModel theseModel : items) {
            for (PersonneModelES personneModelES : theseModel.getPersonnes()) {
                if (estPresentDansES(personneModelES.getPpn())) {
                    updatePersonneDansES(personneModelES);
                } else {
                    ajoutPersoneDansES(br, personneModelES);
                }
            }


        }

        BulkResponse result = ElasticClient.getElasticsearchClient().bulk(br.build());

        if (result.errors()) {
            log.error("Erreurs dans le bulk : ");
            for (BulkResponseItem item: result.items()) {
                if (item.error() != null) {
                    log.error(item.error().reason());
                }
            }
        }
    }

    private void ajoutPersoneDansES(BulkRequest.Builder br, PersonneModelES personneModelES) {
        String jsonPersonne = new Gson().toJson(personneModelES);
        JsonData json = readJson(new ByteArrayInputStream(jsonPersonne.getBytes()), ElasticClient.getElasticsearchClient());

        br.operations(op -> op
                .index(idx -> idx
                        .index(nomIndex.toLowerCase())
                        .id(personneModelES.getPpn() == null? "" : personneModelES.getPpn())
                        .document(json)
                )
        );
    }


    public boolean estPresentDansES(String ppn) throws IOException {
        SearchResponse response = ElasticClient.getElasticsearchClient().search(s -> s
                        .index(nomIndex.toLowerCase())
                        .query(q->q
                                .match(t->t
                                        .query(ppn)
                                        .field("ppn"))),
                Object.class
        );

        return response.hits().total().value() > 0;
    }

    public void updatePersonneDansES(PersonneModelES personneModelES) throws IOException {
        OkHttpClient client = new UnsafeOkHttpClient().getUnsafeOkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String json = new Gson().toJson(personneModelES.getTheses().get(0));
        RequestBody body = RequestBody.create(mediaType,
                "{\"script\" : {\"source\":\"ctx._source.theses.add(params.theses)\"," +
                "\"lang\":\"painless\"," +
                "\"params\": {" +
                    "\"theses\": " +
                        json +
                        "}}}");
        Request request = new Request.Builder().url(elasticConfig.getScheme()
                        + "://" + elasticConfig.getHostname() + ":"
                        + elasticConfig.getPort() + "/"
                        + nomIndex.toLowerCase() +"/_update/"
                        + personneModelES.getPpn())
                .method("POST", body).addHeader("Authorization", basicAuth)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
    }

    public static JsonData readJson(InputStream input, ElasticsearchClient esClient) {
        JsonpMapper jsonpMapper = esClient._transport().jsonpMapper();
        JsonProvider jsonProvider = jsonpMapper.jsonProvider();

        return JsonData.from(jsonProvider.createParser(input), jsonpMapper);
    }

}

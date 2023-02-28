package fr.abes.theses_batch_indexation.writer;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.JsonpMapper;
import com.google.gson.Gson;
import fr.abes.theses_batch_indexation.configuration.ElasticClient;
import fr.abes.theses_batch_indexation.configuration.ElasticConfig;
import fr.abes.theses_batch_indexation.database.TheseModel;
import fr.abes.theses_batch_indexation.dto.personne.PersonneModelES;
import jakarta.json.spi.JsonProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

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


        for (TheseModel theseModel : items) {
            for (PersonneModelES personneModelES : theseModel.getPersonnes()) {
                if (personneModelES.getPpn() != null && personneModelES.getPpn() != "") {
                    log.info("ppn : " + personneModelES.getPpn());
                    log.info("nom : " + personneModelES.getNom());
                    if (estPresentDansES(personneModelES.getPpn())) {
                        updatePersonneDansES(personneModelES);
                    } else {
                        ajoutPersonneDansES(personneModelES);
                    }
                }
            }


        }
    }

    private void ajoutPersonneDansES(PersonneModelES personneModelES) {

        try {
            String jsonPersonne = new Gson().toJson(personneModelES);
            JsonData json = readJson(new ByteArrayInputStream(jsonPersonne.getBytes()), ElasticClient.getElasticsearchClient());

            CreateRequest.Builder<JsonData> cr = new CreateRequest.Builder<>();

            cr.index(nomIndex.toLowerCase());
            cr.id(personneModelES.getPpn());
            cr.refresh(Refresh.True);

            cr.document(json);

            CreateResponse result = ElasticClient.getElasticsearchClient().create(cr.build());

            if (!result.result().equals(Result.Created)) {
                log.error("Erreurs dans le ajoutPersonneDansES : " + result.result());
            }

        } catch (Exception e) {
            log.error("Dans ajoutPersonneDansES : " + e);
        }
    }

    private PersonneModelES getPersonneModelES(String ppn) throws IOException {
        try {
            SearchResponse<PersonneModelES> response = ElasticClient.getElasticsearchClient().search(s -> s
                            .index(nomIndex.toLowerCase())
                            .query(q -> q
                                    .match(t -> t
                                            .query(ppn)
                                            .field("ppn"))),
                    PersonneModelES.class
            );
            Optional<PersonneModelES> a = response.hits().hits().stream().map(Hit::source).findFirst();

            return a.orElse(null);

        } catch (Exception e) {
            log.error("Erreur dans getPersonneModelES : " + e);
            throw e;
        }
    }

    public boolean estPresentDansES(String ppn) throws IOException {
        if (ppn != null && ppn != "") {
            return getPersonneModelES(ppn) != null;
        } else {
            return false;
        }

    }

    public void updatePersonneDansES(PersonneModelES personneCourante) throws IOException, InterruptedException {
        PersonneModelES personnePresentDansES = getPersonneModelES(personneCourante.getPpn());
        deletePersonneES(personneCourante.getPpn());
        personnePresentDansES.getTheses().addAll(personneCourante.getTheses());
        ajoutPersonneDansES(personnePresentDansES);
    }

    private boolean deletePersonneES(String ppn) throws IOException {
        try {
            DeleteRequest.Builder builder = new DeleteRequest.Builder();

            builder.index(nomIndex.toLowerCase());
            builder.id(ppn);
            builder.refresh(Refresh.True);
            DeleteResponse result = ElasticClient.getElasticsearchClient().delete(builder.build());

            if (!result.result().equals(Result.Deleted)) {
                log.error("Erreurs dans le deletePersonneES : " + result.result());
            }

            return result.result().equals(Result.Deleted);
        } catch (Exception e) {
            log.error("Erreur dans deletePersonneES " + e);
            throw e;
        }
    }

    public static JsonData readJson(InputStream input, ElasticsearchClient esClient) {
        JsonpMapper jsonpMapper = esClient._transport().jsonpMapper();
        JsonProvider jsonProvider = jsonpMapper.jsonProvider();

        return JsonData.from(jsonProvider.createParser(input), jsonpMapper);
    }

}

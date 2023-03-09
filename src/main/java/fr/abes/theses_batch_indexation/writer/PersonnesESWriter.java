package fr.abes.theses_batch_indexation.writer;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.JsonpMapper;
import com.google.gson.Gson;
import fr.abes.theses_batch_indexation.configuration.ElasticClient;
import fr.abes.theses_batch_indexation.configuration.ElasticConfig;
import fr.abes.theses_batch_indexation.database.TheseModel;
import fr.abes.theses_batch_indexation.dto.personne.PersonneModelES;
import fr.abes.theses_batch_indexation.dto.personne.TheseModelES;
import jakarta.json.spi.JsonProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PersonnesESWriter implements ItemWriter<TheseModel> {

    @Value("${index.name}")
    private String nomIndex;

    private AtomicInteger nombreDeTheses = new AtomicInteger(0);
    private AtomicInteger nombreDePersonnes = new AtomicInteger(0);
    private AtomicInteger nombreDePersonnesUpdated = new AtomicInteger(0);

    @Override
    public void write(List<? extends TheseModel> items) throws Exception {


        for (TheseModel theseModel : items) {
            nombreDeTheses.incrementAndGet();
            logSiPasAssezDePersonnesDansLaThese(theseModel);
            for (PersonneModelES personneModelES : theseModel.getPersonnes()) {
                nombreDePersonnes.incrementAndGet();
                log.info("ppn : " + personneModelES.getPpn());
                log.info("nom : " + personneModelES.getNom());
                if (estPresentDansES(personneModelES.getPpn())) {
                    updatePersonneDansES(personneModelES);
                    nombreDePersonnesUpdated.incrementAndGet();
                } else {
                    ajoutPersonneDansES(personneModelES);
                }
            }


        }
        log.info("Nombre de thèses traitées : " + nombreDeTheses.get());
        log.info("Nombre de personnes traitées : " + nombreDePersonnes.get());
        log.info("Nombre de personnes mis à jour : " + nombreDePersonnesUpdated.get());
        log.info("Nombre de personnes dans l'index : " + (nombreDePersonnes.intValue() - nombreDePersonnesUpdated.intValue()));

    }

    private void logSiPasAssezDePersonnesDansLaThese(TheseModel theseModel) {
        if (theseModel.getPersonnes().size() < 2) {
            log.warn("Moins de personnes que prévu dans cette theses");
        }
    }

    private void ajoutPersonneDansES(PersonneModelES personneModelES) {

        try {
            String jsonPersonne = new Gson().toJson(personneModelES);
            JsonData json = readJson(new ByteArrayInputStream(jsonPersonne.getBytes()), ElasticClient.getElasticsearchClient());

            IndexRequest.Builder<JsonData> cr = new IndexRequest.Builder<>();

            cr.index(nomIndex.toLowerCase());
            cr.id(Objects.equals(personneModelES.getPpn(), "") ? null : personneModelES.getPpn());
            cr.refresh(Refresh.True);

            cr.document(json);

            IndexResponse result = ElasticClient.getElasticsearchClient().index(cr.build());

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
        if (ppn != null && !ppn.equals("")) {
            return getPersonneModelES(ppn) != null;
        } else {
            return false;
        }

    }

    public void updatePersonneDansES(PersonneModelES personneCourante) throws IOException, InterruptedException {
        PersonneModelES personnePresentDansES = getPersonneModelES(personneCourante.getPpn());
        deletePersonneES(personneCourante.getPpn());
        personnePresentDansES.getTheses().addAll(personneCourante.getTheses());
        addRoles(personnePresentDansES);
        ajoutPersonneDansES(personnePresentDansES);
    }

    private void addRoles(PersonneModelES personnePresentDansES) {
        for (String role: personnePresentDansES.getTheses().stream().map(TheseModelES::getRole).collect(Collectors.toList())) {
            boolean alreadyInRoles = personnePresentDansES.getRoles().stream().anyMatch(r -> r.equals(role));

            if (!alreadyInRoles) {
                personnePresentDansES.getRoles().add(role);
            }
        }
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

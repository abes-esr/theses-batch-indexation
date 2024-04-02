package fr.abes.theses_batch_indexation.writer;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.JsonpMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.google.gson.Gson;
import fr.abes.theses_batch_indexation.configuration.ElasticClient;
import fr.abes.theses_batch_indexation.database.TheseModel;
import fr.abes.theses_batch_indexation.dto.personne.PersonneModelES;
import fr.abes.theses_batch_indexation.dto.personne.TheseModelES;
import fr.abes.theses_batch_indexation.utils.PersonneCacheUtils;
import jakarta.json.spi.JsonProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PersonnesBDDWriter implements ItemWriter<TheseModel> {

    /*
    todo: OK-Ajouter l'index dans la table
          OK-Ajouter l'initialisation de l'index dans la table (parametrique: 0/1)
          OK-Ajout de la tasklet pour le bulk dans ES
          -Ajout possiblilité de repartir d'un iddoc

     */

    @Value("${index.name}")
    private String nomIndex;

    @Value("${table.personne.name}")
    private String tablePersonneName;

    private AtomicInteger nombreDeTheses = new AtomicInteger(0);
    private AtomicInteger nombreDePersonnes = new AtomicInteger(0);
    private AtomicInteger nombreDePersonnesUpdated = new AtomicInteger(0);
    private AtomicInteger nombreDePersonnesUpdatedDansCeChunk = new AtomicInteger(0);

    private PersonneCacheUtils personneCacheUtils;

    private final JdbcTemplate jdbcTemplate;

    public PersonnesBDDWriter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void write(List<? extends TheseModel> items) throws Exception {

        this.personneCacheUtils = new PersonneCacheUtils(
                jdbcTemplate,
                tablePersonneName,
                nomIndex
        );

        nombreDePersonnesUpdatedDansCeChunk.set(0);

        for (TheseModel theseModel : items) {
            nombreDeTheses.incrementAndGet();
            logSiPasAssezDePersonnesDansLaThese(theseModel);
            for (PersonneModelES personneModelES : theseModel.getPersonnes()) {
                nombreDePersonnes.incrementAndGet();
                log.debug("ppn : " + personneModelES.getPpn());
                log.debug("nom : " + personneModelES.getNom());
                if (personneCacheUtils.estPresentDansBDD(personneModelES.getPpn())) {
                    personneCacheUtils.updatePersonneDansBDD(personneModelES);
                    nombreDePersonnesUpdated.incrementAndGet();
                    nombreDePersonnesUpdatedDansCeChunk.incrementAndGet();
                } else {
                    personneCacheUtils.ajoutPersonneDansBDD(personneModelES, personneModelES.getPpn());
                }
            }
        }
        jdbcTemplate.update("commit");
        log.info("Nombre de thèses traitées : " + nombreDeTheses.get());
        log.info("Nombre de personnes traitées : " + nombreDePersonnes.get());
        log.info("Nombre de personnes mis à jour dans ce chunk : " + nombreDePersonnesUpdatedDansCeChunk.get());
        log.info("Nombre de personnes mis à jour en tout : " + nombreDePersonnesUpdated.get());
        log.info("Nombre de personnes dans l'index : " + (nombreDePersonnes.intValue() - nombreDePersonnesUpdated.intValue()));

    }

    private void logSiPasAssezDePersonnesDansLaThese(TheseModel theseModel) {
        if (theseModel.getPersonnes().size() < 2) {
            log.warn("Moins de personnes que prévu dans cette theses");
        }
    }

}

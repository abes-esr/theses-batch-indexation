package fr.abes.theses_batch_indexation.writer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import fr.abes.theses_batch_indexation.database.TheseModel;
import fr.abes.theses_batch_indexation.dto.personne.PersonneModelES;
import fr.abes.theses_batch_indexation.dto.personne.RecherchePersonneModelES;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class RecherchePersonnesBDDWriter implements ItemWriter<TheseModel> {

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

    private final JdbcTemplate jdbcTemplate;

    public RecherchePersonnesBDDWriter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void write(List<? extends TheseModel> items) throws Exception {

        nombreDePersonnesUpdatedDansCeChunk.set(0);

        for (TheseModel theseModel : items) {
            nombreDeTheses.incrementAndGet();
            logSiPasAssezDePersonnesDansLaThese(theseModel);
            for (RecherchePersonneModelES personneModelES : theseModel.getRecherchePersonnes()) {
                nombreDePersonnes.incrementAndGet();
                log.debug("ppn : " + personneModelES.getPpn());
                log.debug("nom : " + personneModelES.getNom());
                if (estPresentDansBDD(personneModelES.getPpn())) {
                    updatePersonneDansBDD(personneModelES);
                    nombreDePersonnesUpdated.incrementAndGet();
                    nombreDePersonnesUpdatedDansCeChunk.incrementAndGet();
                } else {
                    ajoutPersonneDansBDD(personneModelES);
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
        if (theseModel.getRecherchePersonnes().size() < 2) {
            log.warn("Moins de personnes que prévu dans cette theses");
        }
    }

    private void ajoutPersonneDansBDD(RecherchePersonneModelES personneModelES) {

        try {

            jdbcTemplate.update("insert into " + tablePersonneName + "(ppn, personne, nom_index) VALUES (?,?,?)",
                    personneModelES.getPpn(),
                    readJson(personneModelES),
                    nomIndex);
            //jdbcTemplate.update("commit");

        } catch (Exception e) {
            log.error("Dans ajoutPersonneDansES : " + e);
        }
    }

    private RecherchePersonneModelES getPersonneModelBDD(String ppn) throws IOException {
        try {

            List<Map<String, Object>> r = jdbcTemplate.queryForList("select * from " + tablePersonneName + " where ppn = ? and nom_index = ?", ppn, nomIndex);

            return mapperJson((String) r.get(0).get("PERSONNE"));

        } catch (Exception e) {
            log.error("Erreur dans getPersonneModelES : " + e);
            throw e;
        }
    }

    public boolean estPresentDansBDD(String ppn) throws IOException {
        if (ppn != null && !ppn.equals("")) {
            return jdbcTemplate.queryForList("select * from " + tablePersonneName + " where ppn = ? and nom_index = ?", ppn, nomIndex).size() > 0;
        } else {
            return false;
        }

    }

    public void updatePersonneDansBDD(RecherchePersonneModelES personneCourante) throws IOException, InterruptedException {

        try {
            RecherchePersonneModelES personnePresentDansES = getPersonneModelBDD(personneCourante.getPpn());
            personnePresentDansES.getTheses_id().addAll(personneCourante.getTheses_id());
            personnePresentDansES.getTheses_date().addAll(personneCourante.getTheses_date());
            personnePresentDansES.setNb_theses(personnePresentDansES.getTheses_id().size());

            personnePresentDansES.getRoles().addAll((personneCourante.getRoles()));
            personnePresentDansES.getEtablissements().addAll(personneCourante.getEtablissements());
            personnePresentDansES.getDisciplines().addAll(personneCourante.getDisciplines());

            // Facettes
            personnePresentDansES.getFacette_roles().addAll(personneCourante.getFacette_roles());
            personnePresentDansES.getFacette_etablissements().addAll(personneCourante.getFacette_etablissements());
            personnePresentDansES.getFacette_domaines().addAll(personneCourante.getFacette_domaines());

            jdbcTemplate.update("update " + tablePersonneName + " set personne = ?" +
                            " where ppn = ? and nom_index = ?",
                    readJson(personnePresentDansES),
                    personnePresentDansES.getPpn(),
                    nomIndex);
        } catch (MismatchedInputException ex) {
            log.error("Le JSON stocké dans la base et le modèle Java ne correspondent pas : " + ex);
            log.info("On remplace la personne " + personneCourante.getPpn() + " de la base par le modèle Java");
            deletePersonneBDD(personneCourante.getPpn());
            ajoutPersonneDansBDD(personneCourante);
        }
        //jdbcTemplate.update("commit");
    }

    private boolean deletePersonneBDD(String ppn) throws IOException {
        try {
            Object[] args = new Object[]{ppn};
            jdbcTemplate.update("delete from " + tablePersonneName + " where ppn = ? and nom_index = ?", args, nomIndex);
            //jdbcTemplate.update("commit");
            return true;
        } catch (Exception e) {
            log.error("Erreur dans deletePersonneES " + e);
            throw e;
        }
    }

    public static RecherchePersonneModelES mapperJson(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, RecherchePersonneModelES.class);
    }

    public static String readJson(RecherchePersonneModelES personneModelES) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(personneModelES);
    }

}

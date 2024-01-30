package fr.abes.theses_batch_indexation.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import fr.abes.theses_batch_indexation.database.TheseModel;
import fr.abes.theses_batch_indexation.database.TheseRowMapper;
import fr.abes.theses_batch_indexation.dto.personne.PersonneModelES;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
public class PersonneCacheUtils {

    private JdbcTemplate jdbcTemplate;
    private String tablePersonneName;
    private String nomIndex;

    public PersonneCacheUtils(JdbcTemplate jdbcTemplate, String tablePersonneName, String nomIndex) {
        this.jdbcTemplate = jdbcTemplate;
        this.tablePersonneName = tablePersonneName;
        this.nomIndex = nomIndex;
    }

    public void initialisePersonneCacheBDD() {
        jdbcTemplate.update("delete from " + tablePersonneName + " where nom_index = ?", nomIndex);
        jdbcTemplate.update("commit");
    }

    public void ajoutPersonneDansBDD(PersonneModelES personneModelES) {

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

    public PersonneModelES getPersonneModelBDD(String ppn) throws IOException {
        try {

            List<Map<String, Object>> r = jdbcTemplate.queryForList("select * from " + tablePersonneName + " where ppn = ? and nom_index = ?", ppn, nomIndex);

            return mapperJson((String) r.get(0).get("PERSONNE"));

        } catch (Exception e) {
            log.error("Erreur dans getPersonneModelES : " + e);
            throw e;
        }
    }

    public List<PersonneModelES> getAllPersonneModelBDD() throws IOException {
        try {
            List<Map<String, Object>> r = jdbcTemplate.queryForList("select * from " + tablePersonneName + " where nom_index = ?", nomIndex);

            return r.stream().map(p -> {
                try {
                    return mapperJson((String) p.get("PERSONNE"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());

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

    public void updatePersonneDansBDD(PersonneModelES personneCourante) throws IOException, InterruptedException {

        try {
            PersonneModelES personnePresentDansBdd = getPersonneModelBDD(personneCourante.getPpn());
            personnePresentDansBdd.getTheses_id().addAll(personneCourante.getTheses_id());
            personnePresentDansBdd.getTheses().addAll(personneCourante.getTheses());
            personnePresentDansBdd.getRoles().addAll(personneCourante.getRoles());

            jdbcTemplate.update("update " + tablePersonneName + " set personne = ?" +
                            " where ppn = ? and nom_index = ?",
                    readJson(personnePresentDansBdd),
                    personnePresentDansBdd.getPpn(),
                    nomIndex);
        } catch (MismatchedInputException ex) {
            log.error("Le JSON stocké dans la base et le modèle Java ne correspondent pas : " + ex);
            log.info("On remplace la personne " + personneCourante.getPpn() + " de la base par le modèle Java");
            deletePersonneBDD(personneCourante.getPpn());
            ajoutPersonneDansBDD(personneCourante);
        }
        //jdbcTemplate.update("commit");
    }

    public boolean deletePersonneBDD(String ppn) throws IOException {
        try {
            Object[] args = new Object[]{ppn};
            jdbcTemplate.update("delete from " + tablePersonneName + " where ppn = ? and nom_index = ?", ppn, nomIndex);
            //jdbcTemplate.update("commit");
            return true;
        } catch (Exception e) {
            log.error("Erreur dans deletePersonneES " + e);
            throw e;
        }
    }

    public List<TheseModel> getTheses(java.util.Set<String> nntSet) {
        if (nntSet.isEmpty()) {
            return new ArrayList<>();
        }
        String nnts = nntSet.stream().map(i -> "'" + i + "', ").reduce(String::concat).get();
        nnts = nnts.substring(0, nnts.lastIndexOf("', ") + 1);

        return jdbcTemplate.query("select * from Document where nnt in (" + nnts + ")" +
                "or numsujet in (" + nnts +")",
                new TheseRowMapper());
    }

    public static PersonneModelES mapperJson(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, PersonneModelES.class);
    }

    public static String readJson(PersonneModelES personneModelES) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(personneModelES);
    }
}

package fr.abes.theses_batch_indexation.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import fr.abes.theses_batch_indexation.database.TheseModel;
import fr.abes.theses_batch_indexation.database.TheseRowMapper;
import fr.abes.theses_batch_indexation.dto.personne.PersonneModelES;
import fr.abes.theses_batch_indexation.dto.personne.RecherchePersonneModelES;
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

    private List<PersonneModelES> personneCacheList;

    public PersonneCacheUtils(JdbcTemplate jdbcTemplate, String tablePersonneName, String nomIndex, List<PersonneModelES> personneCacheList) {
        this.jdbcTemplate = jdbcTemplate;
        this.tablePersonneName = tablePersonneName;
        this.nomIndex = nomIndex;
        this.personneCacheList = personneCacheList;
    }

    public void initialisePersonneCacheBDD() {
        jdbcTemplate.update("delete from " + tablePersonneName + " where nom_index = ?", nomIndex);
        jdbcTemplate.update("commit");
    }

    public void ajoutPersonneDansBDD(Object personneModelES, String ppn) {

        try {

            jdbcTemplate.update("insert into " + tablePersonneName + "(ppn, personne, nom_index) VALUES (?,?,?)",
                    ppn,
                    readJson(personneModelES),
                    nomIndex);
            //jdbcTemplate.update("commit");

        } catch (Exception e) {
            log.error("Dans ajoutPersonneDansES : " + e);
        }
    }

    public void ajoutPersonneEnMemoire(PersonneModelES personneModelES) {
        personneCacheList.add(personneModelES);
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

    public List<RecherchePersonneModelES> getAllRecherchePersonneModelBDD() throws IOException {
        try {
            List<Map<String, Object>> r = jdbcTemplate.queryForList("select * from " + tablePersonneName + " where nom_index = ?", nomIndex);

            return r.stream().map(p -> {
                try {
                    return mapperJsonRecherchePersonne((String) p.get("PERSONNE"));
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
        if (ppn != null && !ppn.isEmpty()) {
            return jdbcTemplate.queryForList("select * from " + tablePersonneName + " where ppn = ? and nom_index = ?", ppn, nomIndex).size() > 0;
        } else {
            return false;
        }

    }

    public boolean estPresentEnMemoire(String ppn) {
        if (ppn != null && !ppn.isEmpty()) {
            return personneCacheList.stream().anyMatch(personneModelES -> personneModelES.getPpn().equals(ppn));
        }
        return false;
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
            ajoutPersonneDansBDD(personneCourante, personneCourante.getPpn());
        }
        //jdbcTemplate.update("commit");
    }

    public void updatePersonneEnMemoire(PersonneModelES personneCourante) {
        PersonneModelES personnePresentEnMemoire =
                personneCacheList.stream().filter(personneModelES -> personneModelES.getPpn().equals(personneCourante.getPpn())).findAny().get();

        personnePresentEnMemoire.getTheses_id().addAll(personneCourante.getTheses_id());
        personnePresentEnMemoire.getTheses().addAll(personneCourante.getTheses());
        personnePresentEnMemoire.getRoles().addAll(personneCourante.getRoles());
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
    public static RecherchePersonneModelES mapperJsonRecherchePersonne(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, RecherchePersonneModelES.class);
    }

    public static String readJson(Object personneModelES) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(personneModelES);
    }

    public void updateRecherchePersonneDansBDD(RecherchePersonneModelES personneCourante) throws IOException {
        try {
            RecherchePersonneModelES personnePresentDansES = getRecherchePersonneModelBDD(personneCourante.getPpn());
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
            ajoutPersonneDansBDD(personneCourante, personneCourante.getPpn());
        }
    }

    private RecherchePersonneModelES getRecherchePersonneModelBDD(String ppn) throws IOException {
        try {

            List<Map<String, Object>> r = jdbcTemplate.queryForList("select * from " + tablePersonneName + " where ppn = ? and nom_index = ?", ppn, nomIndex);

            return mapperJsonRecherchePersonne((String) r.get(0).get("PERSONNE"));

        } catch (Exception e) {
            log.error("Erreur dans getPersonneModelES : " + e);
            throw e;
        }
    }
}

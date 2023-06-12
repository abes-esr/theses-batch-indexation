package fr.abes.theses_batch_indexation.dto.personne;

import fr.abes.theses_batch_indexation.dto.these.OrganismeDTO;
import fr.abes.theses_batch_indexation.dto.these.SujetDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Représente une these du point de vue d'une personne
 */
@Getter
@Setter
@NoArgsConstructor
public class TheseModelES {

    String nnt;

    /**
     * Une personne peut avoir plusieurs rôles dans la thèse.
     * Précision : une personne peut avoir plusieurs rôles dans une même thèse.
     * On compte chaque rôle, donc une même thèse peut être comptée plusieurs fois.
     * Par ailleurs, une même thèse peut apparaître plusieurs fois sur la page d’une même personne : une fois par rôle.
     */
    String role;
    String titre;

    Map<String, String> titres = new HashMap<String, String>();

    List<SujetRameauES> sujets_rameau = new ArrayList<>();

    Map<String, List<String>> sujets = new HashMap<>();

    String discipline;

    Map<String, String> resumes = new HashMap<String, String>();

    String date_soutenance;

    String date_inscription;

    OrganismeDTO etablissement_soutenance = new OrganismeDTO();

    List<OrganismeDTO> etablissements_cotutelle = new ArrayList<OrganismeDTO>();

    String status;

    String source;

    List<PersonneLiteES> auteurs = new ArrayList<>();

    List<PersonneLiteES> directeurs = new ArrayList<>();

    List<String> oaiSetNames = new ArrayList<>();

    /**
     * Ce constructeur permet de faire une copie de la thèse avec le nouveau rôle
     *
     * @param these
     * @param role Rôle de la personne pour cette thèse
     */
    public TheseModelES(TheseModelES these, String role) {
        this.nnt = these.getNnt();
        this.role = role;
        this.titre = these.getTitre();
        this.titres = these.getTitres();
        this.sujets_rameau = these.getSujets_rameau();
        this.sujets = these.getSujets();
        this.discipline = these.getDiscipline();
        this.resumes = these.getResumes();
        this.date_inscription = these.getDate_inscription();
        this.date_soutenance = these.getDate_soutenance();
        this.etablissement_soutenance = these.getEtablissement_soutenance();
        this.etablissements_cotutelle = these.getEtablissements_cotutelle();
        this.status = these.getStatus();
        this.source = these.getSource();
        this.auteurs = these.getAuteurs();
        this.directeurs = these.getDirecteurs();
        this.oaiSetNames = these.getOaiSetNames();
    }

}

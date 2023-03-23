package fr.abes.theses_batch_indexation.dto.personne;

import fr.abes.theses_batch_indexation.dto.these.OrganismeDTO;
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
    String role;
    String titre;

    Map<String, String> titres = new HashMap<String, String>();

    List<String> sujets_rameau = new ArrayList<>();

    Map<String, List<String>> sujets = new HashMap<>();

    String discipline;

    Map<String, String> resumes = new HashMap<String, String>();

    String date_soutenance;

    OrganismeDTO etablissement_soutenance = new OrganismeDTO();

    List<OrganismeDTO> etablissements_cotutelle = new ArrayList<OrganismeDTO>();

    String status;

    String source;

    /**
     * Ce constructeur permet de faire une copie de la thèse avec le bon rôle
     *
     * @param another
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
        this.date_soutenance = these.getDate_soutenance();
        this.etablissement_soutenance = these.getEtablissement_soutenance();
        this.etablissements_cotutelle = these.getEtablissements_cotutelle();
        this.status = these.getStatus();
        this.source = these.getSource();
    }

}

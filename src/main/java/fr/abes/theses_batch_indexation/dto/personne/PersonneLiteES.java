package fr.abes.theses_batch_indexation.dto.personne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente une personne simplifié au format de l'index Elastic Search.
 * Cet objet sera transformé en JSON au moment de l'appel API pour l'indexation.
 * Le nom des attributs doivent correspondre exactement à ceux de l'index {@link /resources/indexs/personnes.json} theses.auteurs ou theses.directeurs
 */
@Getter
@Setter
@NoArgsConstructor
public class PersonneLiteES {

    private String ppn;
    private boolean has_idref;
    private String nom;
    private String prenom;

    public PersonneLiteES(String ppn, String nom, String prenom) {
        if (ppn != null && !ppn.equals("")) {
            this.ppn = ppn;
            this.has_idref = true;
        } else {
            this.has_idref = false;
        }
        this.nom = nom;
        this.prenom = prenom;
    }
}

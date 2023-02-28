package fr.abes.theses_batch_indexation.dto.personne;

import lombok.Getter;
import lombok.Setter;

/**
 * Représente une these du point de vue d'une personne
 */
@Getter
@Setter
public class TheseModelES {
    String role;
    String titre;
    String nnt;

    /**
     * Constructeur par defaut necessaire pour la deserialisation json :
     * Car il y a deja un autre constructeur mais pas utilisablepar Jackson
     */
    public TheseModelES() {}
    public TheseModelES(TheseModelESPartiel theseModelESPartiel, String role) {
        this.role = role;
        this.nnt = theseModelESPartiel.getNnt();
        this.titre = theseModelESPartiel.getTitre();
    }
}

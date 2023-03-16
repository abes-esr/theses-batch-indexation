package fr.abes.theses_batch_indexation.dto.personne;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

/**
 * Représente une these du point de vue d'une personne
 */
@Getter
@Setter
@NoArgsConstructor
public class TheseModelES {
    String role;
    String titre;
    String nnt;

    /**
     * Constructeur par defaut necessaire pour la deserialisation json :
     * Car il y a deja un autre constructeur mais pas utilisablepar Jackson
     */
    public TheseModelES(TheseModelESPartiel theseModelESPartiel, String role) {
        this.role = role;
        this.nnt = theseModelESPartiel.getNnt();
        this.titre = theseModelESPartiel.getTitre();
    }
}

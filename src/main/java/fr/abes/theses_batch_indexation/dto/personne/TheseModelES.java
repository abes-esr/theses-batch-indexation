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
}

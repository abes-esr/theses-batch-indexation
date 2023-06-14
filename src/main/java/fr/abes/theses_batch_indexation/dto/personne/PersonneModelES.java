package fr.abes.theses_batch_indexation.dto.personne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente une personne au format de l'index Elastic Search.
 * Cet objet sera transformé en JSON au moment de l'appel API pour l'indexation.
 * Le nom des attributs doivent correspondre exactement à ceux de l'index {@link /resources/indexs/personnes.json}
 */
@Getter
@Setter
@NoArgsConstructor
public class PersonneModelES {

    private String ppn;
    private boolean has_idref;
    private String nom;
    private String prenom;
    private List<String> nom_complet = new ArrayList<>();
    private List<SuggestionES> completion_nom = new ArrayList<>();

    private List<TheseModelES> theses = new ArrayList<>();

    /**
     * Récapitulatif des rôles de la personne, utilisé pour la fonction de filtre
     */
    private List<String> roles = new ArrayList<>();

    private List<String> etablissements = new ArrayList<>();

    private List<String> domaines = new ArrayList<>();

    private List<String> theses_id = new ArrayList<>();

    private List<String> theses_date = new ArrayList<>();

    public PersonneModelES(String ppn, String nom, String prenom) {
        if (ppn != null && !ppn.equals("")) {
            this.ppn = ppn;
            this.has_idref = true;
        } else {
            this.has_idref = false;
        }
        this.nom = nom;
        this.prenom = prenom;

        nom_complet.add(String.format("%1$s %2$s",prenom,nom));
        nom_complet.add(String.format("%1$s %2$s",nom,prenom));

        completion_nom.add(SuggestionES.builder().input(String.format("%1$s %2$s",prenom,nom)).weight(10).build());
        completion_nom.add(SuggestionES.builder().input(String.format("%1$s %2$s",nom,prenom)).weight(10).build());
    }

    /**
     * Recherche une thèse dans la liste des thèses
     * @param nnt Identifiant de la thèse
     * @return TheseModelES La thèse ou null
     */
    public TheseModelES findThese(String nnt) {
        return theses.stream()
                .filter(item -> (item.getId() != null && item.getId().equals(nnt)))
                .findAny()
                .orElse(null);
    }
}

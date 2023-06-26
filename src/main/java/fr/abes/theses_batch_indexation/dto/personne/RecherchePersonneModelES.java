package fr.abes.theses_batch_indexation.dto.personne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

/**
 * Représente une personne simplifiée pour la recherche au format de l'index Elastic Search.
 * Cet objet sera transformé en JSON au moment de l'appel API pour l'indexation.
 * Le nom des attributs doit correspondre exactement à ceux de l'index {@link /resources/indexs/recherche_personnes.json}
 */
@Getter
@Setter
@NoArgsConstructor
public class RecherchePersonneModelES {

    private String ppn;
    private boolean has_idref;
    private String nom;
    private String prenom;
    private List<String> nom_complet = new ArrayList<>();
    private List<SuggestionES> completion_nom = new ArrayList<>();

    private Set<String> theses_id = new LinkedHashSet<>();

    private Integer nb_theses;

    private Set<String> theses_date = new LinkedHashSet<>();

    private List<String> roles = new ArrayList<>();

    private List<String> etablissements = new ArrayList<>();

    private List<String> discipline = new ArrayList<>();

    Map<String, List<String>> thematiques = new HashMap<>();

    /**
     * Champs utilisés pour les filtres
     */
    private Set<String> facette_roles = new LinkedHashSet<>();

    private Set<String> facette_etablissements = new LinkedHashSet<>();

    private Set<String> facette_domaines = new LinkedHashSet<>();

    public RecherchePersonneModelES(String ppn, String nom, String prenom) {
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
}

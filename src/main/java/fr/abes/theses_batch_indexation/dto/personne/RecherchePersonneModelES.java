package fr.abes.theses_batch_indexation.dto.personne;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class RecherchePersonneModelES {
    protected String ppn;
    protected boolean has_idref;
    protected String nom;
    protected String prenom;
    protected List<String> nom_complet = new ArrayList<>();
    protected List<SuggestionES> completion_nom = new ArrayList<>();

    protected Set<String> theses_id = new LinkedHashSet<>();

    protected Integer nb_theses;

    protected Set<String> theses_date = new LinkedHashSet<>();

    protected List<String> roles = new ArrayList<>();

    protected List<String> etablissements = new ArrayList<>();

    protected List<String> disciplines = new ArrayList<>();

    Map<String, List<String>> thematiques = new HashMap<>();

    /**
     * Champs utilisés pour les filtres
     */
    protected Set<String> facette_roles = new LinkedHashSet<>();

    protected Set<String> facette_etablissements = new LinkedHashSet<>();

    protected Set<String> facette_domaines = new LinkedHashSet<>();

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

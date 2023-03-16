package fr.abes.theses_batch_indexation.dto.personne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

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
    private List<SuggestionES> suggestion = new ArrayList<>();

    private List<TheseModelES> theses = new ArrayList<>();

    /*
    TODO : Pourquoi rajouter un champs roles à ce niveau
     alors qu'il peut être retrouver depuis la liste des thèses ?
     */
    private List<String> roles = new ArrayList<>();

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

        suggestion.add(SuggestionES.builder().input(String.format("%1$s %2$s",prenom,nom)).weight(10).build());
        suggestion.add(SuggestionES.builder().input(String.format("%1$s %2$s",nom,prenom)).weight(10).build());
    }
}

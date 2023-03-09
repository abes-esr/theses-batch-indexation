package fr.abes.theses_batch_indexation.dto.personne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PersonneModelES {
    private String ppn;
    private boolean has_idref;
    private String nom;
    private String prenom;
    private List<TheseModelES> theses = new ArrayList<>();
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
    }
}

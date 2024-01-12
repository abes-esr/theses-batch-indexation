package fr.abes.theses_batch_indexation.dto.personne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;

@Getter
@Setter
public class PersonneModelESAvecId extends PersonneModelES{

    private String _id;

    public PersonneModelESAvecId (String id, PersonneModelES autreObjet) {
        this._id = id;
        this.ppn = autreObjet.ppn;
        this.has_idref = autreObjet.has_idref;
        this.nom = autreObjet.nom;
        this.prenom = autreObjet.prenom;
        this.nom_complet = new ArrayList<>(autreObjet.nom_complet);
        this.theses_id = new HashSet<>(autreObjet.theses_id);
        this.roles = new ArrayList<>(autreObjet.roles);
        this.theses = new ArrayList<>(autreObjet.theses);
    }
}

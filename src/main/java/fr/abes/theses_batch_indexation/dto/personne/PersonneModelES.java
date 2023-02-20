package fr.abes.theses_batch_indexation.dto.personne;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PersonneModelES {
    private String ppn;
    private String nom;
    private String prenom;
    private List<TheseModelES> theses = new ArrayList<>();
}

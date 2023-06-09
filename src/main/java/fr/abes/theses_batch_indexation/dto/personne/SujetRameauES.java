package fr.abes.theses_batch_indexation.dto.personne;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@NoArgsConstructor
public class SujetRameauES {
    private String ppn;
    private String libelle;

    public SujetRameauES(String lib) {
        this.libelle = lib;
    }

    public SujetRameauES(String ppn, String lib) {
        this.ppn = ppn;
        this.libelle = lib;
    }
}

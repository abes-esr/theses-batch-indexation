package fr.abes.theses_batch_indexation.dto.personne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonneDTO {
    private int idDoc;
    private String nnt;
    private String doc;
    private String json;
    private String idSujet;
}

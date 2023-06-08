package fr.abes.theses_batch_indexation.dto.personne;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

/**
 * Représente une suggestion pour l'autocomplétion des personnes
 */
@Getter
@Setter
@Builder
@Jacksonized
public class SuggestionES {

    String input;
    Integer weight;

}

package fr.abes.theses_batch_indexation.dto.personne;

import fr.abes.theses_batch_indexation.dto.these.OrganismeDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Représente une these du point de vue d'une personne
 */
@Getter
@Setter
@NoArgsConstructor
public class TheseModelES {
    String role;
    String titre;

    Map<String, String> titres = new HashMap<String, String>();

    String nnt;

    List<String> sujets_rameau = new ArrayList<>();

    Map<String, List<String>> sujets = new HashMap<>();

    String discipline;

    Map<String, String> resumes = new HashMap<String, String>();

    String date_soutenance;

    OrganismeDTO etablissement_soutenance = new OrganismeDTO();

    List<OrganismeDTO> etablissements_cotutelle = new ArrayList<OrganismeDTO>();

    String status;

    String source;

}

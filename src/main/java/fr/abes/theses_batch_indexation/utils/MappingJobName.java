package fr.abes.theses_batch_indexation.utils;

import fr.abes.theses_batch_indexation.database.TableIndexationES;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Component
@Getter
public class MappingJobName {

    @Value("${index.name.theses}")
    private String theses;

    @Value("${index.name.personnes}")
    private String personnes;

    @Value("${index.name.thematiques}")
    private String thematiques;

    @Value("${index.name.recherche_personnes}")
    private String recherche_personnes;

    HashMap<String, TableIndexationES> nomTableES = new HashMap<String, TableIndexationES>();
    HashMap<String, String> nomIndexES = new HashMap<>();

    public MappingJobName() {
    }

    @PostConstruct
    public void init() {
        // correspondance nom du job / nom de la table dans la BD
        nomTableES.put("indexationThesesDansES", TableIndexationES.indexation_es_these);
        nomTableES.put("indexationPersonnesDansES", TableIndexationES.indexation_es_personne);
        nomTableES.put("indexationPersonnesDeBddVersES", TableIndexationES.indexation_es_personne);
        nomTableES.put("ajoutPersonnesDansES", TableIndexationES.indexation_es_personne);
        nomTableES.put("ajoutRecherchePersonnesDansES", TableIndexationES.indexation_es_recherche_personne);
        nomTableES.put("indexationRecherchePersonnesDansES", TableIndexationES.indexation_es_recherche_personne);
        nomTableES.put("indexationThematiquesDansES", TableIndexationES.indexation_es_thematique);
        nomTableES.put("suppressionThesesDansES", TableIndexationES.suppression_es_these);
        nomTableES.put("suppressionPersonnesDansES", TableIndexationES.suppression_es_personne);
        nomTableES.put("suppressionRecherchePersonnesDansES", TableIndexationES.suppression_es_recherche_personne);
        nomTableES.put("suppressionThematiquesDansES", TableIndexationES.suppression_es_thematique);

        // correspondance nom du job / nom de l'index dans ES
        nomIndexES.put("indexationThesesDansES", theses);
        nomIndexES.put("suppressionThesesDansES", theses);
        nomIndexES.put("indexationPersonnesDansES", personnes);
        nomIndexES.put("suppressionPersonnesDansES", personnes);
        nomIndexES.put("indexationPersonnesDeBddVersES", personnes);
        nomIndexES.put("ajoutPersonnesDansES", personnes);
        nomIndexES.put("ajoutRecherchePersonnesDansES", recherche_personnes);
        nomIndexES.put("indexationRecherchePersonnesDansES", recherche_personnes);
        nomIndexES.put("suppressionRecherchePersonnesDansES", recherche_personnes);
        nomIndexES.put("indexationThematiquesDansES", thematiques);
        nomIndexES.put("suppressionThematiquesDansES", thematiques);
    }
}

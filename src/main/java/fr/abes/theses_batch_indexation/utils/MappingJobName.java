package fr.abes.theses_batch_indexation.utils;

import fr.abes.theses_batch_indexation.configuration.JobConfig;
import fr.abes.theses_batch_indexation.database.TableIndexationES;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Component
@Getter
public class MappingJobName {

    @Autowired
    JobConfig jobConfig;

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
        nomTableES.put("indexationThematiquesDansES", TableIndexationES.indexation_es_thematique);
        nomTableES.put("suppressionThesesDansES", TableIndexationES.suppression_es_these);
        nomTableES.put("suppressionPersonnesDansES", TableIndexationES.suppression_es_personne);
        nomTableES.put("suppressionRecherchePersonnesDansES", TableIndexationES.suppression_es_recherche_personne);
        nomTableES.put("suppressionThematiquesDansES", TableIndexationES.suppression_es_thematique);

        // correspondance nom du job / nom de l'index dans ES
        nomIndexES.put("indexationThesesDansES", jobConfig.getThesesIndex());
        nomIndexES.put("suppressionThesesDansES", jobConfig.getThesesIndex());
        nomIndexES.put("indexationPersonnesDansES", jobConfig.getPersonnesIndex());
        nomIndexES.put("suppressionPersonnesDansES", jobConfig.getPersonnesIndex());
        nomIndexES.put("indexationPersonnesDeBddVersES", jobConfig.getPersonnesIndex());
        nomIndexES.put("ajoutPersonnesDansES", jobConfig.getPersonnesIndex());
        nomIndexES.put("ajoutRecherchePersonnesDansES", jobConfig.getRecherche_personnesIndex());
        nomIndexES.put("suppressionRecherchePersonnesDansES", jobConfig.getRecherche_personnesIndex());
        nomIndexES.put("indexationThematiquesDansES", jobConfig.getThematiquesIndex());
        nomIndexES.put("suppressionThematiquesDansES", jobConfig.getThematiquesIndex());
    }
}
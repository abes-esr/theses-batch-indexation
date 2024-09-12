package fr.abes.theses_batch_indexation.utils;

import fr.abes.theses_batch_indexation.configuration.JobConfig;
import fr.abes.theses_batch_indexation.database.TableIndexationES;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Component
@Getter
public class MappingJobName {

    @Autowired
    JobConfig jobConfig;

    private final NomIndexSelecteur nomIndexSelecteur;
    private final Environment env;

    HashMap<String, TableIndexationES> nomTableES = new HashMap<String, TableIndexationES>();
    HashMap<String, String> nomIndexES = new HashMap<>();
    HashMap<String, String> nomAliasES = new HashMap<>();

    public MappingJobName(NomIndexSelecteur nomIndexSelecteur, Environment env) {
        this.nomIndexSelecteur = nomIndexSelecteur;
        this.env = env;
    }

    @PostConstruct
    public void init() throws Exception {
        // correspondance nom du job / nom de la table dans la BD
        nomTableES.put("indexationThesesDansES", TableIndexationES.indexation_es_these);
        nomTableES.put("indexationPersonnesDansES", TableIndexationES.indexation_es_personne);
        nomTableES.put("indexationPersonnesDeBddVersES", TableIndexationES.indexation_es_personne);
        nomTableES.put("ajoutPersonnesDansES", TableIndexationES.indexation_es_personne);
        nomTableES.put("ajoutRecherchePersonnesDansES", TableIndexationES.indexation_es_recherche_personne);
        nomTableES.put("indexationRecherchePersonnesDansES", TableIndexationES.indexation_es_recherche_personne);
        nomTableES.put("indexationRecherchePersonnesDeBddVersES", TableIndexationES.indexation_es_recherche_personne);
        nomTableES.put("indexationThematiquesDansES", TableIndexationES.indexation_es_thematique);
        nomTableES.put("suppressionThesesDansES", TableIndexationES.suppression_es_these);
        nomTableES.put("suppressionPersonnesDansES", TableIndexationES.suppression_es_personne);
        nomTableES.put("suppressionRecherchePersonnesDansES", TableIndexationES.suppression_es_recherche_personne);
        nomTableES.put("suppressionThematiquesDansES", TableIndexationES.suppression_es_thematique);

        // correspondance nom du job / nom de l'index dans ES
        nomIndexES.put("indexationThesesDansES", jobConfig.getThesesIndex());
        nomIndexES.put("suppressionThesesDansES", jobConfig.getThesesIndex());
        nomIndexES.put("indexationPersonnesDansES", jobConfig.getPersonnesIndex());
        nomIndexES.put("indexationPersonnesDeBddVersES", jobConfig.getPersonnesIndex());
        nomIndexES.put("indexationThematiquesDansES", jobConfig.getThematiquesIndex());
        nomIndexES.put("suppressionThematiquesDansES", jobConfig.getThematiquesIndex());
        nomIndexES.put("indexationRecherchePersonnesDansES", jobConfig.getRecherche_personnesIndex());
        nomIndexES.put("indexationRecherchePersonnesDeBddVersES", jobConfig.getRecherche_personnesIndex());


        // correspondance nom du job / nom de l'index dans ES
        nomAliasES.put("indexationPersonnesDansES", "personnes_alias");
        //nomAliasES.put("indexationPersonnesDeBddVersES", "personnes_alias");
        nomAliasES.put("indexationRecherchePersonnesDansES", "recherche_personnes_alias");
        //nomAliasES.put("indexationRecherchePersonnesDeBddVersES", "recherche_personnes_alias");

        // Mise à jour de l'index personne en fonction de l'alias
        if (nomAliasES.get(env.getProperty("spring.batch.job.names")) != null) {

            String indexSuivant = nomIndexSelecteur.getNomIndexSuivant(
                    nomAliasES.get(env.getProperty("spring.batch.job.names")));

            nomIndexES.replace(env.getProperty("spring.batch.job.names"), indexSuivant);
        }
    }
}
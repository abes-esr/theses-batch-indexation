package fr.abes.theses_batch_indexation.dto.personne;

import fr.abes.theses_batch_indexation.model.oaisets.Set;
import fr.abes.theses_batch_indexation.model.tef.*;
import fr.abes.theses_batch_indexation.utils.OutilsTef;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Getter
public class RecherchePersonneMappe {

    private List<PersonneModelES> personnes = new ArrayList<>();


    private TheseModelES theseModelES = new TheseModelES();

    private final String AUTEUR = "auteur";

    public RecherchePersonneMappe(Mets mets, String id, List<Set> oaiSets) {

        DmdSec dmdSec = mets.getDmdSec().get(1);
        AmdSec amdSec = mets.getAmdSec().get(0);

        TechMD techMD = null;

        try {
            techMD = amdSec.getTechMD().stream().filter(d -> d.getMdWrap().getXmlData().getThesisAdmin() != null).findFirst().orElse(null);
        } catch (NullPointerException e) {
            log.error(String.format("%s - Champs '%s' : La valeur est nulle dans le TEF", id, "techMD"));
        } catch (Exception e) {
            log.error(String.format("%s - Champs '%s' : Erreur de traitement : %s", id, "techMD", e.getMessage()));
        }

        /************************************
         * Parsing des auteurs de la thèse
         * ***********************************/
        log.info("traitement des auteurs");
        try {
            traiterAuteurs(techMD.getMdWrap().getXmlData().getThesisAdmin()
                    .getAuteur());

        } catch (NullPointerException e) {
            log.error(String.format("%s - Champs '%s' : La valeur est nulle dans le TEF", id, "Rôle " + AUTEUR));
        } catch (Exception e) {
            log.error(String.format("%s - Champs '%s' : Erreur de traitement : %s", id, "Rôle " + AUTEUR, e.getMessage()));
        }
    }


    private void traiterAuteurs(List<Auteur> collection) throws NullPointerException {

        Iterator<Auteur> iter = collection.iterator();
        while (iter.hasNext()) {
            Auteur item = iter.next();

            PersonneModelES personne = findPersonne(OutilsTef.getPPN(item.getAutoriteExterne()));

            if (personne == null) {
                // On créé la personne
                personne = new PersonneModelES(
                        OutilsTef.getPPN(item.getAutoriteExterne()),
                        item.getNom(),
                        item.getPrenom()
                );
                personnes.add(personne);
            }

            traiterThese(personne, AUTEUR);
        }
    }

    private void traiterThese(PersonneModelES item, String role) {
        TheseModelES these = new TheseModelES(theseModelES, role);
        item.getTheses().add(these);
        item.getRoles().add(role.substring(0, 1).toUpperCase() + role.substring(1));
        item.getTheses_id().add(these.getId());
        if (these.getDate_soutenance() != null) {
            item.getTheses_date().add(these.getDate_soutenance());
        }

        // On ajoute l'établissement de soutenance
        item.getEtablissements().add(these.getEtablissement_soutenance().getNom());

        // On ajoute les domaines
        item.getDomaines().addAll(these.getOaiSetNames());
    }
        private PersonneModelES findPersonne(String id) {
            return personnes.stream()
                    .filter(item -> (item.isHas_idref() && item.getPpn().equals(id)))
                    .findAny()
                    .orElse(null);
        }

}

package fr.abes.theses_batch_indexation.dto.personne;

import fr.abes.theses_batch_indexation.model.tef.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * L'objet PersonneMappe correspond à un objet transitoire entre le format TEF (XML) et le format JSON attendu par Elastic Search.
 * L'objectif est de faire correspondre les données du format TEF (préparser par la libraire Mets) avec les données au format Elastic Search.
 * Les transformations sont réalisées directement dans le constructeur de l'objet.
 */
@Slf4j
@Getter
public class PersonneMapee {

    /**
     * Liste des personnes (au format Elastic Search) figurant dans le fichier TEF
     */
    private List<PersonneModelES> personnes = new ArrayList<>();

    private TheseModelESPartiel theseModelESPartiel = new TheseModelESPartiel();

    /**
     * Identifiant de la thèse
     */
    private String nnt;

    private final String AUTEUR = "auteur";
    private final String DIRECTEUR = "directeur de thèse";
    private final String RAPPORTEUR = "rapporteur";
    private final String PRESIDENT = "président du jury";
    private final String MEMBRE_DU_JURY = "membre du jury";

    /*Todo: à partir de Mets extraire toutes les infos à indexer
       (s'inpirer de TheseMapee vu que la plupart des champs sont déjà extrait)*/

    /**
     * CTOR
     * Cette fonction permet de remplir la liste des personnes à partir du TEF.
     * En cas d'erreur de lecture, les erreurs sont affichées dans les logs.
     * @param mets
     */
    public PersonneMapee(Mets mets) {

        DmdSec dmdSec = mets.getDmdSec().get(1);
        AmdSec amdSec = mets.getAmdSec().get(0);

        TechMD techMD = null;

        /************************************
         * Parsing du NNT
         * ***********************************/
        try {

            techMD = amdSec.getTechMD().stream().filter(d -> d.getMdWrap().getXmlData().getThesisAdmin() != null).findFirst().orElse(null);

            Iterator<Identifier> iteIdentifiers = techMD.getMdWrap().getXmlData().getThesisAdmin().getIdentifier().iterator();
            while (iteIdentifiers.hasNext()) {
                Identifier i = iteIdentifiers.next();
                if (isNnt(i.getValue()))
                    nnt = i.getValue();
            }
            theseModelESPartiel.setNnt(nnt);
        } catch (NullPointerException e) {
            log.error("PB pour nnt " + e.toString());
        }

        /************************************
         * Parsing du titre principal de la thèse
         * ***********************************/
        try {
            theseModelESPartiel.setTitre(dmdSec.getMdWrap().getXmlData().getThesisRecord().getTitle().getContent());
        } catch (NullPointerException e) {
            log.error("PB pour titrePrincipal de " + nnt + e.getMessage());
        }

        /************************************
         * Parsing des auteurs de la thèse
         * ***********************************/
        log.info("traitement des rapporteurs");
        try {
            personnes.addAll(parseAuteurs(techMD.getMdWrap().getXmlData().getThesisAdmin()
                    .getAuteur()));

        } catch (NullPointerException e) {
            log.error("PB pour auteurs de " + nnt + "," + e.getMessage());
        }

        /************************************
         * Parsing des directeurs de la thèse
         * ***********************************/
        log.info("traitement de directeurs");
        try {
            personnes.addAll(parseDirecteurs(techMD.getMdWrap().getXmlData().getThesisAdmin()
                    .getDirecteurThese()));

        } catch (NullPointerException e) {
            log.error("PB pour directeurs de " + nnt + "," + e.getMessage());
        }

        /************************************
         * Parsing des rapporteurs de la thèse
         * ***********************************/
        log.info("traitement des rapporteurs");
        try {
            personnes.addAll(parseRapporteurs(techMD.getMdWrap().getXmlData().getThesisAdmin()
                    .getDirecteurThese()));

        } catch (NullPointerException e) {
            log.error("PB pour les rapporteurs de " + nnt + "," + e.getMessage());
        }

        /************************************
         * Parsing des présidents du jury de la thèse
         * ***********************************/
        log.info("traitement des présidents du jury de la thèse");
        try {
            personnes.addAll(parsePresident(techMD.getMdWrap().getXmlData().getThesisAdmin()
                    .getDirecteurThese()));

        } catch (NullPointerException e) {
            log.error("PB pour les présidents du jury de " + nnt + "," + e.getMessage());
        }

        /************************************
         * Parsing des membres du jury de la thèse
         * ***********************************/
        log.info("traitement des membres du jury de la thèse");
        try {
            personnes.addAll(parseMembreJury(techMD.getMdWrap().getXmlData().getThesisAdmin()
                    .getDirecteurThese()));

        } catch (NullPointerException e) {
            log.error("PB pour les membres du jury de " + nnt + "," + e.getMessage());
        }

    }

    private boolean isNnt(String identifier) {
        if (identifier.length() == 12)
            return true;
        return false;
    }

    private PersonneModelES buildPersonne(String id, String nom, String prenom, String role, TheseModelESPartiel theses) {
        // Construction d'un objet Personne
        PersonneModelES item = new PersonneModelES(
                id,
                nom,
                prenom
        );
        TheseModelES e = new TheseModelES(theseModelESPartiel, role);
        item.getTheses().add(e);
        item.getRoles().add(role);

        return item;
    }

    private List<PersonneModelES> parseAuteurs(List<Auteur> auteursDepuisTef) throws NullPointerException {
        List<PersonneModelES> candidates = new ArrayList<>();

        Iterator<Auteur> auteurIterator = auteursDepuisTef.iterator();
        while (auteurIterator.hasNext()) {
            Auteur item = auteurIterator.next();

            // Construction d'un objet Personne
            PersonneModelES adto = buildPersonne(
                    item.getAutoriteExterne() != null ? item.getAutoriteExterne().getValue() : null,
                    item.getNom(),
                    item.getPrenom(),
                    AUTEUR,
                    theseModelESPartiel
            );
            candidates.add(adto);
        }

        return candidates;
    }

    private List<PersonneModelES> parseDirecteurs(List<DirecteurThese> directeursDepuisTef) throws NullPointerException {
        List<PersonneModelES> candidates = new ArrayList<>();

        Iterator<DirecteurThese> directeurTheseIterator = directeursDepuisTef.iterator();
        while (directeurTheseIterator.hasNext()) {
            DirecteurThese item = directeurTheseIterator.next();

            // Construction d'un objet Personne
            PersonneModelES adto = buildPersonne(
                    item.getAutoriteExterne() != null ? item.getAutoriteExterne().getValue() : null,
                    item.getNom(),
                    item.getPrenom(),
                    DIRECTEUR,
                    theseModelESPartiel
            );
            candidates.add(adto);
        }

        return candidates;
    }

    private List<PersonneModelES> parseRapporteurs(List<DirecteurThese> directeursDepuisTef) throws NullPointerException {
        List<PersonneModelES> candidates = new ArrayList<>();

        Iterator<DirecteurThese> directeurTheseIterator = directeursDepuisTef.iterator();
        while (directeurTheseIterator.hasNext()) {
            DirecteurThese item = directeurTheseIterator.next();

            // Construction d'un objet Personne
            PersonneModelES adto = buildPersonne(
                    item.getAutoriteExterne() != null ? item.getAutoriteExterne().getValue() : null,
                    item.getNom(),
                    item.getPrenom(),
                    RAPPORTEUR,
                    theseModelESPartiel
            );
            candidates.add(adto);
        }

        return candidates;
    }

    private List<PersonneModelES> parsePresident(List<DirecteurThese> directeursDepuisTef) throws NullPointerException {
        List<PersonneModelES> candidates = new ArrayList<>();

        Iterator<DirecteurThese> directeurTheseIterator = directeursDepuisTef.iterator();
        while (directeurTheseIterator.hasNext()) {
            DirecteurThese item = directeurTheseIterator.next();

            // Construction d'un objet Personne
            PersonneModelES adto = buildPersonne(
                    item.getAutoriteExterne() != null ? item.getAutoriteExterne().getValue() : null,
                    item.getNom(),
                    item.getPrenom(),
                    PRESIDENT,
                    theseModelESPartiel
            );
            candidates.add(adto);
        }

        return candidates;
    }

    private List<PersonneModelES> parseMembreJury(List<DirecteurThese> directeursDepuisTef) throws NullPointerException {
        List<PersonneModelES> candidates = new ArrayList<>();

        Iterator<DirecteurThese> directeurTheseIterator = directeursDepuisTef.iterator();
        while (directeurTheseIterator.hasNext()) {
            DirecteurThese item = directeurTheseIterator.next();

            // Construction d'un objet Personne
            PersonneModelES adto = buildPersonne(
                    item.getAutoriteExterne() != null ? item.getAutoriteExterne().getValue() : null,
                    item.getNom(),
                    item.getPrenom(),
                    MEMBRE_DU_JURY,
                    theseModelESPartiel
            );
            candidates.add(adto);
        }

        return candidates;
    }


}

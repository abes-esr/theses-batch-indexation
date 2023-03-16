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

    /**
     * CTOR
     * Cette fonction permet de remplir la liste des personnes à partir du TEF.
     * En cas d'erreur de lecture, les erreurs sont affichées dans les logs.
     *
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
                    .getRapporteur()));

        } catch (NullPointerException e) {
            log.error("PB pour les rapporteurs de " + nnt + "," + e.getMessage());
        }

        /************************************
         * Parsing du président du jury de la thèse
         * ***********************************/
        log.info("traitement du président du jury de la thèse");
        try {
            personnes.add(parsePresident(techMD.getMdWrap().getXmlData().getThesisAdmin()
                    .getPresidentJury()));

        } catch (NullPointerException e) {
            log.error("PB pour le président du jury de " + nnt + "," + e.getMessage());
        }

        /************************************
         * Parsing des membres du jury de la thèse
         * ***********************************/
        log.info("traitement des membres du jury de la thèse");
        try {
            personnes.addAll(parseMembreJury(techMD.getMdWrap().getXmlData().getThesisAdmin()
                    .getMembreJury()));

        } catch (NullPointerException e) {
            log.error("PB pour les membres du jury de " + nnt + "," + e.getMessage());
        }

    }

    private boolean isNnt(String identifier) {
        if (identifier.length() == 12)
            return true;
        return false;
    }

    /**
     * Instancie un objet Personne à partir des informations de base
     * @param id Identifiant de la personne
     * @param nom Nom de la personne
     * @param prenom Prénom de la personne
     * @param role Rôle de la personne
     * @param theses Thèses de la personne au format ThesesModelESPartiel
     * @return Un objet PersonneModelES
     */
    private PersonneModelES buildPersonne(String id, String nom, String prenom, String role, TheseModelESPartiel theses) {
        // Construction d'un objet Personne
        PersonneModelES item = new PersonneModelES(
                id,
                nom,
                prenom
        );
        TheseModelES e = new TheseModelES(theses, role);
        item.getTheses().add(e);
        item.getRoles().add(role);

        return item;
    }

    /**
     * Transforme les informations sur les auteurs de la thèse au format Elastic Search
     * @param collection Liste des auteurs au format TEF
     * @return La liste des auteurs de la thèse au format ES
     * @throws NullPointerException si une erreur de lecture du TEF
     */
    private List<PersonneModelES> parseAuteurs(List<Auteur> collection) throws NullPointerException {
        List<PersonneModelES> candidates = new ArrayList<>();

        Iterator<Auteur> iter = collection.iterator();
        while (iter.hasNext()) {
            Auteur item = iter.next();

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

    /**
     * Transforme les informations sur les directeurs de la thèse au format Elastic Search
     * @param collection Liste des directeurs au format TEF
     * @return La liste des directeurs de la thèse au format ES
     * @throws NullPointerException si une erreur de lecture du TEF
     */
    private List<PersonneModelES> parseDirecteurs(List<DirecteurThese> collection) throws NullPointerException {
        List<PersonneModelES> candidates = new ArrayList<>();

        Iterator<DirecteurThese> iter = collection.iterator();
        while (iter.hasNext()) {
            DirecteurThese item = iter.next();

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

    /**
     * Transforme les informations sur les rapporteurs de la thèse au format Elastic Search
     * @param collection Liste des rapporteurs au format TEF
     * @return La liste des rapporteurs de la thèse au format ES
     * @throws NullPointerException si une erreur de lecture du TEF
     */
    private List<PersonneModelES> parseRapporteurs(List<Rapporteur> collection) throws NullPointerException {
        List<PersonneModelES> candidates = new ArrayList<>();

        Iterator<Rapporteur> iter = collection.iterator();
        while (iter.hasNext()) {
            Rapporteur item = iter.next();

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

    /**
     * Transforme les informations du président du jury de la thèse au format Elastic Search
     * @param item Information du président du jury au format TEF
     * @return Les informations du président du jury de la thèse au format ES
     * @throws NullPointerException si une erreur de lecture du TEF
     */
    private PersonneModelES parsePresident(PresidentJury item) throws NullPointerException {

        // Construction d'un objet Personne
        PersonneModelES adto = buildPersonne(
                item.getAutoriteExterne() != null ? item.getAutoriteExterne().getValue() : null,
                item.getNom(),
                item.getPrenom(),
                PRESIDENT,
                theseModelESPartiel
        );
        return adto;
    }

    /**
     * Transforme les informations sur les membre du jury de la thèse au format Elastic Search
     * @param collection Liste des membres du jury au format TEF
     * @return La liste des membres du jury de la thèse au format ES
     * @throws NullPointerException si une erreur de lecture du TEF
     */
    private List<PersonneModelES> parseMembreJury(List<MembreJury> collection) throws NullPointerException {
        List<PersonneModelES> candidates = new ArrayList<>();

        Iterator<MembreJury> iter = collection.iterator();
        while (iter.hasNext()) {
            MembreJury item = iter.next();

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

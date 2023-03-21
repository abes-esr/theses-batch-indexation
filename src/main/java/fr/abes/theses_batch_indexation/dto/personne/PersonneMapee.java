package fr.abes.theses_batch_indexation.dto.personne;

import fr.abes.theses_batch_indexation.model.tef.*;
import fr.abes.theses_batch_indexation.utils.OutilsTef;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

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


    private TheseModelES theseModelES = new TheseModelES();

    /**
     * Identifiant de la thèse
     */
    private String nnt;

    /**
     * Libellé à indexer pour les rôles
     */
    private final String AUTEUR = "auteur";
    private final String DIRECTEUR = "directeur de thèse";
    private final String RAPPORTEUR = "rapporteur";
    private final String PRESIDENT = "président du jury";
    private final String MEMBRE_DU_JURY = "membre du jury";

    /**
     * CTOR
     * Cette fonction permet de remplir la liste des personnes à partir du TEF.
     * On lit d'abord les informations de la thèse puis on lit les informations des thèses
     * car les informations de la thèse sont communes à toutes les personnes liées à la thèse.
     * En cas d'erreur de lecture, les erreurs sont affichées dans les logs.
     *
     * @param mets
     */
    public PersonneMapee(Mets mets) {

        DmdSec dmdSec = mets.getDmdSec().get(1);
        AmdSec amdSec = mets.getAmdSec().get(0);

        TechMD techMD = null;

        /************************************************************************
         *                    Parsing des informations de la thèse
         * *********************************************************************/

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
            theseModelES.setNnt(nnt);
        } catch (NullPointerException e) {
            log.error("PB pour nnt " + e.toString());
        }

        /************************************
         * Parsing du titre principal de la thèse
         * ***********************************/
        try {
            theseModelES.setTitre(dmdSec.getMdWrap().getXmlData().getThesisRecord().getTitle().getContent());
        } catch (NullPointerException e) {
            log.error("PB pour titrePrincipal de " + nnt + e.getMessage());
        }

        /************************************
         * Parsing des sujets
         * ***********************************/
        log.info("traitement de sujets");
        try {
            List<Subject> subjects = dmdSec.getMdWrap().getXmlData().getThesisRecord().getSubject();
            Iterator<Subject> subjectIterator = subjects.iterator();
            while (subjectIterator.hasNext()) {
                Subject s = subjectIterator.next();

                if (s != null && s.getLang() != null) {
                    if (theseModelES.getSujets().containsKey(s.getLang())) {
                        theseModelES.getSujets().get(s.getLang()).add(s.getContent());
                    } else {
                        List<String> list = new ArrayList<>();
                        list.add(s.getContent());
                        theseModelES.getSujets().put(s.getLang(),list);
                    }
                }
            }
        } catch (NullPointerException e) {
            log.error("PB pour sujets de " + nnt + "," + e.getMessage());
        }

        /************************************
         * Parsing des sujets Rameau
         * ***********************************/
        log.info("traitement de sujetsRameau");

        try {
            List<VedetteRameauNomCommun> sujetsRameauDepuisTef = dmdSec.getMdWrap().getXmlData()
                    .getThesisRecord().getSujetRameau().getVedetteRameauNomCommun();
            Iterator<VedetteRameauNomCommun> vedetteRameauNomCommunIterator = sujetsRameauDepuisTef.iterator();
            while (vedetteRameauNomCommunIterator.hasNext()) {
                VedetteRameauNomCommun vdto = vedetteRameauNomCommunIterator.next();
                if (vdto.getElementdEntree() != null)
                    theseModelES.getSujets_rameau().add(vdto.getElementdEntree().getContent());
            }
        } catch (NullPointerException e) {
            log.error("PB pour sujetsRameau de " + nnt + ", " + e.getMessage());
        }

        /************************************
         * Parsing du résumé
         * ***********************************/
        log.info("traitement de resumes");
        try {
            List<Abstract> abstracts = dmdSec.getMdWrap().getXmlData().getThesisRecord().getAbstract();
            Iterator<Abstract> abstractIterator = abstracts.iterator();
            while (abstractIterator.hasNext()) {
                Abstract a = abstractIterator.next();
                theseModelES.getResumes().put(a.getLang(), a.getContent());
            }
        } catch (NullPointerException e) {
            log.error("PB pour resumes de " + nnt + e.getMessage());
        }

        /************************************
         * Parsing de la discipline
         * ***********************************/
        log.info("traitement de discipline");
        try {
            ThesisDegreeDiscipline tddisc = techMD.getMdWrap().getXmlData().getThesisAdmin()
                    .getThesisDegree().getThesisDegreeDiscipline();
            theseModelES.setDiscipline(tddisc.getValue());
        } catch (NullPointerException e) {
            log.error("PB pour discipline de " + nnt + "," + e.getMessage());
        }

        /************************************************************************
         *                    Parsing des personnes liées à la thèse
         * *********************************************************************/

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
     * @return Un objet PersonneModelES
     */
    private PersonneModelES buildPersonne(String id, String nom, String prenom, String role) {
        // Construction d'un objet Personne
        PersonneModelES item = new PersonneModelES(
                id,
                nom,
                prenom
        );

        theseModelES.setRole(role);
        item.getTheses().add(theseModelES);
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
                    OutilsTef.getPPN(item.getAutoriteExterne()),
                    item.getNom(),
                    item.getPrenom(),
                    AUTEUR
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
                    OutilsTef.getPPN(item.getAutoriteExterne()),
                    item.getNom(),
                    item.getPrenom(),
                    DIRECTEUR
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
                    OutilsTef.getPPN(item.getAutoriteExterne()),
                    item.getNom(),
                    item.getPrenom(),
                    RAPPORTEUR
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
                OutilsTef.getPPN(item.getAutoriteExterne()),
                item.getNom(),
                item.getPrenom(),
                PRESIDENT
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
                    OutilsTef.getPPN(item.getAutoriteExterne()),
                    item.getNom(),
                    item.getPrenom(),
                    MEMBRE_DU_JURY
            );
            candidates.add(adto);
        }

        return candidates;
    }
}

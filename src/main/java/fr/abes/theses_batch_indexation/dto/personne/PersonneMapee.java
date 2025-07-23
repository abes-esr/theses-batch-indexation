package fr.abes.theses_batch_indexation.dto.personne;

import fr.abes.theses_batch_indexation.dto.these.OrganismeDTO;
import fr.abes.theses_batch_indexation.dto.these.Source;
import fr.abes.theses_batch_indexation.dto.these.Status;
import fr.abes.theses_batch_indexation.model.oaisets.Set;
import fr.abes.theses_batch_indexation.model.tef.*;
import fr.abes.theses_batch_indexation.utils.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
     * CTOR
     * Cette fonction permet de remplir la liste des personnes à partir du TEF.
     * On lit d'abord les informations de la thèse puis on lit les informations de la personne.
     * Les informations de la thèse sont communes à toutes les personnes liées à la thèse.
     * On duplique la thèse pour chaque rôle des personnes
     * En cas d'erreur de lecture, les erreurs sont affichées dans les logs.
     *
     * @param mets    Fichier TEF
     * @param id      Identifiant de la thèse
     *                NNT pour une thèse soutenue
     *                idStep pour une thèse en préparation
     *                Source : base Oracle champs 'iddoc'
     * @param oaiSets Liste des domaines
     */
    public PersonneMapee(Mets mets, String id, List<Set> oaiSets) {

        DmdSec dmdSec = mets.getDmdSec().get(1);
        AmdSec amdSec = mets.getAmdSec().get(0);

        TechMD techMD = null;

        try {
            techMD = amdSec.getTechMD().stream().filter(d -> d.getMdWrap().getXmlData().getThesisAdmin() != null).findFirst().orElse(null);
        } catch (NullPointerException e) {
            log.info(String.format("%s - Champs '%s' : La valeur est nulle dans le TEF", id, "techMD"));
        } catch (Exception e) {
            log.info(String.format("%s - Champs '%s' : Erreur de traitement : %s", id, "techMD", e.getMessage()));
        }

        /************************************************************************
         *                    Parsing des informations de la thèse
         * *********************************************************************/

        /************************************
         * Parsing de l'identifiant
         * ***********************************/
        theseModelES.setId(id);

        /************************************
         * Parsing du status
         * ***********************************/
        log.debug("traitement de status");
        theseModelES.setStatus(Status.SOUTENUE);
        try {
            Optional<DmdSec> dmdSecPourStepGestion = mets.getDmdSec().stream().filter(d -> d.getMdWrap().getXmlData().getStepGestion() != null).findFirst();

            if (dmdSecPourStepGestion.isPresent())
                theseModelES.setStatus(dmdSecPourStepGestion.get().getMdWrap().getXmlData().getStepGestion().getStepEtat().equals("these")
                        || dmdSecPourStepGestion.get().getMdWrap().getXmlData().getStepGestion().getStepEtat().equals("soutenu")
                        ? Status.SOUTENUE : Status.EN_PREPARATION);
        } catch (NullPointerException e) {
            log.info(String.format("%s - Champs '%s' : La valeur est nulle dans le TEF", id, "Status"));
        } catch (Exception e) {
            log.info(String.format("%s - Champs '%s' : Erreur de traitement : %s", id, "Status", e.getMessage()));
        }


        /************************************
         * Parsing du titre principal de la thèse
         * ***********************************/
        try {
            theseModelES.setTitre(dmdSec.getMdWrap().getXmlData().getThesisRecord().getTitle().getContent());
        } catch (NullPointerException e) {
            log.info(String.format("%s - Champs '%s' : La valeur est nulle dans le TEF", id, "Titre principal"));
        } catch (Exception e) {
            log.info(String.format("%s - Champs '%s' : Erreur de traitement : %s", id, "Titre principal", e.getMessage()));
        }

        /************************************
         * Parsing des sujets
         * ***********************************/
        log.debug("traitement de sujets");
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
                        theseModelES.getSujets().put(s.getLang(), list);
                    }
                }
            }
        } catch (NullPointerException e) {
            log.info(String.format("%s - Champs '%s' : La valeur est nulle dans le TEF", id, "Sujets"));
        } catch (Exception e) {
            log.info(String.format("%s - Champs '%s' : Erreur de traitement : %s", id, "Sujets", e.getMessage()));
        }

        /************************************
         * Parsing des sujets Rameau
         * ***********************************/
        log.debug("traitement de sujetsRameau");

        try {
            VedetteProcessor vedetteProcessor = new VedetteProcessor(dmdSec, theseModelES);
            vedetteProcessor.processAllVedettes();
        } catch (NullPointerException e) {
            if (theseModelES.getStatus() == Status.SOUTENUE) {
                log.info(String.format("%s - Champs '%s' : La valeur est nulle dans le TEF", id, "Sujets Rameau"));
            } else {
                // Pas de sujets Rameau pour les thèses en préparation
            }
        } catch (Exception e) {
            log.info(String.format("%s - Champs '%s' : Erreur de traitement : %s", id, "Sujets Rameau", e.getMessage()));
        }

        /************************************
         * Parsing du résumé
         * ***********************************/
        log.debug("traitement de resumes");
        try {
            List<Abstract> abstracts = dmdSec.getMdWrap().getXmlData().getThesisRecord().getAbstract();
            Iterator<Abstract> abstractIterator = abstracts.iterator();
            while (abstractIterator.hasNext()) {
                Abstract a = abstractIterator.next();
                if (!a.getLang().isEmpty()) {
                    theseModelES.getResumes().put(a.getLang(), a.getContent());
                } else {
                    log.info(String.format("%s - Champs '%s' : Le code langue est vide dans le TEF. Valeur du résumé : %s", id, "Résumé", a.getContent().substring(0, 30) + "..."));
                }
            }
        } catch (NullPointerException e) {
            log.info(String.format("%s - Champs '%s' : La valeur est nulle dans le TEF", id, "Résumé"));
        } catch (Exception e) {
            log.info(String.format("%s - Champs '%s' : Erreur de traitement : %s", id, "Résumé", e.getMessage()));
        }

        /************************************
         * Parsing de la discipline
         * ***********************************/
        log.debug("traitement de discipline");
        try {
            ThesisDegreeDiscipline tddisc = techMD.getMdWrap().getXmlData().getThesisAdmin()
                    .getThesisDegree().getThesisDegreeDiscipline();
            theseModelES.setDiscipline(tddisc.getValue());
        } catch (NullPointerException e) {
            log.info(String.format("%s - Champs '%s' : La valeur est nulle dans le TEF", id, "Discipline"));
        } catch (Exception e) {
            log.info(String.format("%s - Champs '%s' : Erreur de traitement : %s", id, "Discipline", e.getMessage()));
        }

        /************************************
         * Parsing de la date de soutenance
         * ***********************************/
        log.debug("traitement de dateSoutenance");
        try {
            theseModelES.setDate_soutenance(techMD.getMdWrap().getXmlData().getThesisAdmin().getDateAccepted().getValue().toString());
        } catch (NullPointerException e) {
            if (theseModelES.getStatus() == Status.SOUTENUE) {
                log.info(String.format("%s - Champs '%s' : La valeur est nulle dans le TEF", id, "Date de soutenance"));
            }
        } catch (Exception e) {
            log.info(String.format("%s - Champs '%s' : Erreur de traitement : %s", id, "Date de soutenance", e.getMessage()));
        }

        if (theseModelES.getStatus() == Status.EN_PREPARATION) {
            /************************************
             * Parsing de la date d'inscription
             * ***********************************/
            log.debug("traitement de datePremiereInscriptionDoctorat");
            try {
                theseModelES.setDate_inscription(techMD.getMdWrap().getXmlData().getThesisAdmin().getThesisDegree().getDatePremiereInscriptionDoctorat().toString());
            } catch (NullPointerException e) {
                log.info(String.format("%s - Champs '%s' : La valeur est nulle dans le TEF", id, "Date d'inscription"));
            } catch (Exception e) {
                log.info(String.format("%s - Champs '%s' : Erreur de traitement : %s", id, "Date d'inscription", e.getMessage()));
            }
        }

        /************************************
         * Parsing des etablissements
         * ***********************************/
        log.debug("traitement de etablissements");

        try {
            List<ThesisDegreeGrantor> grantors = techMD.getMdWrap().getXmlData().getThesisAdmin()
                    .getThesisDegree().getThesisDegreeGrantor();
            Iterator<ThesisDegreeGrantor> iteGrantor = grantors.iterator();
            // l'étab de soutenance est le premier de la liste
            ThesisDegreeGrantor premier = iteGrantor.next();
            if (premier.getAutoriteExterne() != null && OutilsTef.ppnEstPresent(premier.getAutoriteExterne())) {
                theseModelES.getEtablissement_soutenance().setPpn(OutilsTef.getPPN(premier.getAutoriteExterne()));
            }
            theseModelES.getEtablissement_soutenance().setNom(premier.getNom());
            // les potentiels suivants sont les cotutelles
            while (iteGrantor.hasNext()) {
                ThesisDegreeGrantor a = iteGrantor.next();
                OrganismeDTO ctdto = new OrganismeDTO();
                if (a.getAutoriteExterne() != null && OutilsTef.ppnEstPresent(a.getAutoriteExterne()))
                    ctdto.setPpn(OutilsTef.getPPN(a.getAutoriteExterne()));
                ctdto.setNom(a.getNom());
                theseModelES.getEtablissements_cotutelle().add(ctdto);
            }
        } catch (NullPointerException e) {
            log.info(String.format("%s - Champs '%s' : La valeur est nulle dans le TEF", id, "Etablissements"));
        } catch (Exception e) {
            log.info(String.format("%s - Champs '%s' : Erreur de traitement : %s", id, "Etablissements", e.getMessage()));
        }

        /************************************
         * Parsing de la source
         * ***********************************/
        log.debug("traitement de source");
        theseModelES.setSource(Source.SUDOC);
        if (theseModelES.getStatus().equals(Status.EN_PREPARATION)) {
            theseModelES.setSource(Source.STEP);
        }
        try {
            if (theseModelES.getStatus().equals(Status.SOUTENUE) &&
                    mets.getDmdSec().stream().filter(d -> d.getMdWrap().getXmlData().getStarGestion() != null).findFirst().orElse(null)
                            .getMdWrap().getXmlData().getStarGestion().getTraitements().getSorties().getCines().getIndicCines().equals("OK"))
                theseModelES.setSource(Source.STAR);
        } catch (NullPointerException e) {
            // Il s'agit d'une thèse Sudoc, il n’y a pas de lien avec le CINES (ce sont des thèses imprimées)
            //log.info(String.format("%s - Champs '%s' : La valeur (getIndicCines) est nulle dans le TEF", nnt, "Source"));
        } catch (Exception e) {
            log.info(String.format("%s - Champs '%s' : Erreur de traitement : %s", id, "Source", e.getMessage()));
        }

        /************************************
         * Parsing des titres
         * ***********************************/
        log.debug("traitement de titres");
        try {

            // Titre principal
            if (!dmdSec.getMdWrap().getXmlData().getThesisRecord().getTitle().getLang().isEmpty()) {
                theseModelES.getTitres().put(
                        dmdSec.getMdWrap().getXmlData().getThesisRecord().getTitle().getLang(),
                        dmdSec.getMdWrap().getXmlData().getThesisRecord().getTitle().getContent());
            } else {
                log.info(String.format("%s - Champs '%s' : Le code langue est vide dans le TEF. Valeur du titre : %s", id, "Titre principal", dmdSec.getMdWrap().getXmlData().getThesisRecord().getTitle().getContent().substring(0, 30) + "..."));
            }

            // Titres alternatifs
            if (dmdSec.getMdWrap().getXmlData().getThesisRecord().getAlternative() != null) {
                Iterator<Alternative> titreAlternativeIterator = dmdSec.getMdWrap().getXmlData().getThesisRecord().getAlternative().iterator();
                while (titreAlternativeIterator.hasNext()) {
                    Alternative a = titreAlternativeIterator.next();

                    if (!a.getLang().isEmpty()) {
                        theseModelES.getTitres().put(
                                a.getLang(), a.getContent());
                    } else {
                        log.info(String.format("%s - Champs '%s' : Le code langue est vide dans le TEF. Valeur du titre : %s", id, "Titres", a.getContent().substring(0, 30) + "..."));
                    }

                }
            }
        } catch (NullPointerException e) {
            log.info(String.format("%s - Champs '%s' : La valeur est nulle dans le TEF", id, "Titres"));
        } catch (Exception e) {
            log.info(String.format("%s - Champs '%s' : Erreur de traitement : %s", id, "Titres", e.getMessage()));
        }

        /************************************
         * Parsing des Domaines
         * ***********************************/
        log.debug("traitement de oaiSets");
        try {
            Iterator<String> oaiSetSpecIterator = techMD.getMdWrap().getXmlData().getThesisAdmin()
                    .getOaiSetSpec().iterator();
            while (oaiSetSpecIterator.hasNext()) {
                String oaiSetSpec = oaiSetSpecIterator.next();
                Optional<Set> leSet = oaiSets.stream().filter(d -> d.getSetSpec().equals(oaiSetSpec)).findFirst();
                theseModelES.getOaiSetNames().add(leSet.get().getSetName().trim());
            }

        } catch (NullPointerException e) {
            log.info(String.format("%s - Champs '%s' : La valeur est nulle dans le TEF", id, "Domaines (oaiSets)"));
        } catch (Exception e) {
            log.info(String.format("%s - Champs '%s' : Erreur de traitement : %s", id, "Domaines (oaiSets)", e.getMessage()));
        }

        /************************************
         * Parsing des auteurs de la thèse
         * ***********************************/
        log.debug("traitement des auteurs");
        try {
            Iterator<Auteur> iter = techMD.getMdWrap().getXmlData().getThesisAdmin()
                    .getAuteur().iterator();
            while (iter.hasNext()) {
                Auteur item = iter.next();
                theseModelES.getAuteurs().add(new PersonneLiteES(OutilsTef.getPPN(item.getAutoriteExterne()),
                        item.getNom(),
                        item.getPrenom()));
            }
        } catch (NullPointerException e) {
            log.info(String.format("%s - Champs '%s' : La valeur est nulle dans le TEF", id, "Auteurs"));
        } catch (Exception e) {
            log.info(String.format("%s - Champs '%s' : Erreur de traitement : %s", id, "Auteurs", e.getMessage()));
        }

        /************************************
         * Parsing des directeurs de la thèse
         * ***********************************/
        log.debug("traitement de directeurs");
        try {
            Iterator<DirecteurThese> iter = techMD.getMdWrap().getXmlData().getThesisAdmin()
                    .getDirecteurThese().iterator();
            while (iter.hasNext()) {
                DirecteurThese item = iter.next();
                theseModelES.getDirecteurs().add(new PersonneLiteES(OutilsTef.getPPN(item.getAutoriteExterne()),
                        item.getNom(),
                        item.getPrenom()));
            }

        } catch (NullPointerException e) {
            log.info(String.format("%s - Champs '%s' : La valeur est nulle dans le TEF", id, "Directeurs"));
        } catch (Exception e) {
            log.info(String.format("%s - Champs '%s' : Erreur de traitement : %s", id, "Directeurs", e.getMessage()));
        }

        /************************************************************************
         *                    Traitement des personnes liées à la thèse
         * *********************************************************************/

        /************************************
         * Parsing des auteurs de la thèse
         * ***********************************/
        log.debug("traitement des auteurs");
        try {
            traiterAuteurs(techMD.getMdWrap().getXmlData().getThesisAdmin()
                    .getAuteur());

        } catch (NullPointerException e) {
            log.info(String.format("%s - Champs '%s' : La valeur est nulle dans le TEF", id, "Rôle " + Roles.AUTEUR));
        } catch (Exception e) {
            log.info(String.format("%s - Champs '%s' : Erreur de traitement : %s", id, "Rôle " + Roles.AUTEUR, e.getMessage()));
        }

        /************************************
         * Parsing des directeurs de la thèse
         * ***********************************/
        log.debug("traitement de directeurs");
        try {
            traiterDirecteurs(techMD.getMdWrap().getXmlData().getThesisAdmin()
                    .getDirecteurThese());
        } catch (NullPointerException e) {
            log.info(String.format("%s - Champs '%s' : La valeur est nulle dans le TEF", id, "Rôle " + Roles.DIRECTEUR));
        } catch (Exception e) {
            log.info(String.format("%s - Champs '%s' : Erreur de traitement : %s", id, "Rôle " + Roles.DIRECTEUR, e.getMessage()));
        }

        /************************************
         * Parsing des rapporteurs de la thèse
         * ***********************************/
        log.debug("traitement des rapporteurs");
        try {
            traiterRapporteurs(techMD.getMdWrap().getXmlData().getThesisAdmin()
                    .getRapporteur());

        } catch (NullPointerException e) {
            // Ce champs est optionnel
            //log.info(String.format("%s - Champs '%s' : La valeur est nulle dans le TEF", nnt, "Rôle "+Roles.RAPPORTEUR));
        } catch (Exception e) {
            log.info(String.format("%s - Champs '%s' : Erreur de traitement : %s", id, "Rôle " + Roles.RAPPORTEUR, e.getMessage()));
        }

        /************************************
         * Parsing du président du jury de la thèse
         * ***********************************/
        log.debug("traitement du président du jury de la thèse");
        try {
            traiterPresident(techMD.getMdWrap().getXmlData().getThesisAdmin()
                    .getPresidentJury());

        } catch (NullPointerException e) {
            // Ce champs est optionnel
            //log.info(String.format("%s - Champs '%s' : La valeur est nulle dans le TEF", nnt, "Rôle "+Roles.PRESIDENT));
        } catch (Exception e) {
            log.info(String.format("%s - Champs '%s' : Erreur de traitement : %s", id, "Rôle " + Roles.PRESIDENT, e.getMessage()));
        }

        /************************************
         * Parsing des membres du jury de la thèse
         * ***********************************/
        log.debug("traitement des membres du jury de la thèse");
        try {
            traiterMembreJury(techMD.getMdWrap().getXmlData().getThesisAdmin()
                    .getMembreJury());

        } catch (NullPointerException e) {
            // Ce champs est optionnel
            //log.info(String.format("%s - Champs '%s' : La valeur est nulle dans le TEF", nnt, "Rôle "+Roles.MEMBRE_DU_JURY));
        } catch (Exception e) {
            log.info(String.format("%s - Champs '%s' : Erreur de traitement : %s", id, "Rôle " + Roles.MEMBRE_DU_JURY, e.getMessage()));
        }
    }

    /**
     * Instancie un objet Thèse à partir des informations de la thèse
     * Assigne le rôle à la personne
     *
     * @param item La personne
     * @param role Rôle de la personne
     */
    private void traiterThese(PersonneModelES item, String role) {
        TheseModelES these = new TheseModelES(theseModelES, role);
        // On ajoute l'identifiant de la thèse
        item.getTheses_id().add(these.getId());

        // On ajoute le rôle
        item.getRoles().add(role);

        // On ajoute les thèses
        item.getTheses().add(these);
    }

    /**
     * Traite les auteurs de la thèse
     * Cette méthode transforme les informations des auteurs au format Elastic Search, crée la personne si besoin
     * et ajoute la thèse avec le rôle
     * La thèse est dupliquée pour chaque rôle
     *
     * @param collection Liste des auteurs au format TEF
     * @throws NullPointerException si une erreur de lecture du TEF
     */
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

            traiterThese(personne, Roles.AUTEUR);
        }
    }

    /**
     * Traite les directeurs de la thèse
     * Cette méthode transforme les informations des directeurs au format Elastic Search, crée la personne si besoin
     * et ajoute la thèse avec le rôle
     * La thèse est dupliquée pour chaque rôle
     *
     * @param collection Liste des directeurs au format TEF
     * @throws NullPointerException si une erreur de lecture du TEF
     */
    private void traiterDirecteurs(List<DirecteurThese> collection) throws NullPointerException {

        Iterator<DirecteurThese> iter = collection.iterator();
        while (iter.hasNext()) {
            DirecteurThese item = iter.next();

            PersonneModelES personne = findPersonne(OutilsTef.getPPN(item.getAutoriteExterne()));

            if (personne == null) {
                // On ajoute la personne
                personne = new PersonneModelES(
                        OutilsTef.getPPN(item.getAutoriteExterne()),
                        item.getNom(),
                        item.getPrenom()
                );
                personnes.add(personne);
            }

            traiterThese(personne, Roles.DIRECTEUR);
        }
    }

    /**
     * Traite les rapporteurs de la thèse
     * Cette méthode transforme les informations des rapporteurs au format Elastic Search, crée la personne si besoin
     * et ajoute la thèse avec le rôle
     * La thèse est dupliquée pour chaque rôle
     *
     * @param collection Liste des rapporteurs au format TEF
     * @throws NullPointerException si une erreur de lecture du TEF
     */
    private void traiterRapporteurs(List<Rapporteur> collection) throws NullPointerException {

        Iterator<Rapporteur> iter = collection.iterator();
        while (iter.hasNext()) {
            Rapporteur item = iter.next();

            PersonneModelES personne = findPersonne(OutilsTef.getPPN(item.getAutoriteExterne()));

            if (personne == null) {
                // On ajoute la personne
                personne = new PersonneModelES(
                        OutilsTef.getPPN(item.getAutoriteExterne()),
                        item.getNom(),
                        item.getPrenom()
                );
                personnes.add(personne);
            }
            traiterThese(personne, Roles.RAPPORTEUR);
        }
    }

    /**
     * Traite les informations du président du jury de la thèse
     * Cette méthode transforme les informations du président du jury au format Elastic Search, crée la personne si besoin
     * et ajoute la thèse avec le rôle
     * La thèse est dupliquée pour chaque rôle
     *
     * @param item Président du jury au format TEF
     * @throws NullPointerException si une erreur de lecture du TEF
     */
    private void traiterPresident(PresidentJury item) throws NullPointerException {
        PersonneModelES personne = findPersonne(OutilsTef.getPPN(item.getAutoriteExterne()));

        if (personne == null) {
            // On ajoute la personne
            personne = new PersonneModelES(
                    OutilsTef.getPPN(item.getAutoriteExterne()),
                    item.getNom(),
                    item.getPrenom()
            );
            personnes.add(personne);
        }

        traiterThese(personne, Roles.PRESIDENT);
    }

    /**
     * Traite les informations sur les membres du jury de la thèse
     * Cette méthode transforme les informations les membres du jury au format Elastic Search, crée la personne si besoin
     * et ajoute la thèse avec le rôle
     * La thèse est dupliquée pour chaque rôle
     *
     * @param collection Liste des membres du jury au format TEF
     * @throws NullPointerException si une erreur de lecture du TEF
     */
    private void traiterMembreJury(List<MembreJury> collection) throws NullPointerException {
        Iterator<MembreJury> iter = collection.iterator();
        while (iter.hasNext()) {
            MembreJury item = iter.next();

            PersonneModelES personne = findPersonne(OutilsTef.getPPN(item.getAutoriteExterne()));

            if (personne == null) {
                // On ajoute la personne
                personne = new PersonneModelES(
                        OutilsTef.getPPN(item.getAutoriteExterne()),
                        item.getNom(),
                        item.getPrenom()
                );
                personnes.add(personne);
            }

            traiterThese(personne, Roles.MEMBRE_DU_JURY);
        }
    }

    /**
     * Recherche une personne identifiée dans la liste des personnes de la thèse
     *
     * @param id Identifiant de la personne
     * @return PersonneModelES La personne ou null
     */
    private PersonneModelES findPersonne(String id) {
        return personnes.stream()
                .filter(item -> (item.isHas_idref() && item.getPpn().equals(id)))
                .findAny()
                .orElse(null);
    }
}

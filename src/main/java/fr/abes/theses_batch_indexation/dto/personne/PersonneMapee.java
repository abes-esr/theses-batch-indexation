package fr.abes.theses_batch_indexation.dto.personne;

import fr.abes.theses_batch_indexation.dto.these.OrganismeDTO;
import fr.abes.theses_batch_indexation.model.oaisets.Set;
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
     * On lit d'abord les informations de la thèse puis on lit les informations de la personne.
     * Les informations de la thèse sont communes à toutes les personnes liées à la thèse.
     * On duplique la thèse pour chaque rôle des personnes
     * En cas d'erreur de lecture, les erreurs sont affichées dans les logs.
     *
     * @param mets
     */
    public PersonneMapee(Mets mets, List<Set> oaiSets) {

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
                        theseModelES.getSujets().put(s.getLang(), list);
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

        /************************************
         * Parsing de la date de soutenance
         * ***********************************/
        log.info("traitement de dateSoutenance");
        try {
            theseModelES.setDate_soutenance(techMD.getMdWrap().getXmlData().getThesisAdmin().getDateAccepted().getValue().toString());
        } catch (NullPointerException e) {
            log.error("PB pour dateSoutenance de " + nnt);
        }

        /************************************
         * Parsing des etablissements
         * ***********************************/
        log.info("traitement de etablissements");

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
            log.error("PB pour etablissements de " + nnt + "," + e.getMessage());
        }

        /************************************
         * Parsing du status
         * ***********************************/
        log.info("traitement de status");
        theseModelES.setStatus("soutenue");
        try {
            Optional<DmdSec> stepGestion = mets.getDmdSec().stream().filter(d -> d.getMdWrap().getXmlData().getStepGestion() != null).findFirst();
            if (stepGestion.isPresent())
                theseModelES.setStatus("enCours");
        } catch (NullPointerException e) {
            log.error("PB pour status de " + nnt + e.getMessage());
        }

        /************************************
         * Parsing de la source
         * ***********************************/
        log.info("traitement de source");
        theseModelES.setSource("sudoc");
        if (theseModelES.getStatus().equals("enCours"))
            theseModelES.setSource("step");
        try {
            if (theseModelES.getStatus().equals("soutenue") &&
                    mets.getDmdSec().stream().filter(d -> d.getMdWrap().getXmlData().getStarGestion() != null).findFirst().orElse(null)
                            .getMdWrap().getXmlData().getStarGestion().getTraitements().getSorties().getCines().getIndicCines().equals("OK"))
                theseModelES.setSource("star");
        } catch (NullPointerException ex) {
            log.error("impossible de récupérer le getIndicCines pour " + nnt + "(NullPointerException)");
        }

        /************************************
         * Parsing des titres
         * ***********************************/
        log.info("traitement de titres");
        try {
            theseModelES.getTitres().put(
                    dmdSec.getMdWrap().getXmlData().getThesisRecord().getTitle().getLang(),
                    dmdSec.getMdWrap().getXmlData().getThesisRecord().getTitle().getContent());

            if (dmdSec.getMdWrap().getXmlData().getThesisRecord().getAlternative() != null) {
                Iterator<Alternative> titreAlternativeIterator = dmdSec.getMdWrap().getXmlData().getThesisRecord().getAlternative().iterator();
                while (titreAlternativeIterator.hasNext()) {
                    Alternative a = titreAlternativeIterator.next();
                    theseModelES.getTitres().put(
                            a.getLang(), a.getContent());
                }
            }
        } catch (NullPointerException e) {
            log.error("PB pour titres de " + nnt + e.getMessage());
        }

        /************************************
         * Parsing des Domaines
         * ***********************************/
        log.info("traitement de oaiSets");
        try {
            Iterator<String> oaiSetSpecIterator = techMD.getMdWrap().getXmlData().getThesisAdmin()
                    .getOaiSetSpec().iterator();
            while (oaiSetSpecIterator.hasNext()) {
                String oaiSetSpec  = oaiSetSpecIterator.next();
                Optional<Set> leSet = oaiSets.stream().filter(d -> d.getSetSpec().equals(oaiSetSpec)).findFirst();
                theseModelES.getOaiSetNames().add(leSet.get().getSetName());
            }

        } catch (NullPointerException e) {
            log.error("PB pour oaisets de " + nnt + "," + e.getMessage());
        }

        /************************************
         * Parsing des auteurs de la thèse
         * ***********************************/
        log.info("traitement des auteurs");
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
            log.error("PB pour auteurs de " + nnt + "," + e.getMessage());
        }

        /************************************
         * Parsing des directeurs de la thèse
         * ***********************************/
        log.info("traitement de directeurs");
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
            log.error("PB pour directeurs de " + nnt + "," + e.getMessage());
        }

        /************************************************************************
         *                    Traitement des personnes liées à la thèse
         * *********************************************************************/

        /************************************
         * Parsing des auteurs de la thèse
         * ***********************************/
        log.info("traitement des auteurs");
        try {
            traiterAuteurs(techMD.getMdWrap().getXmlData().getThesisAdmin()
                    .getAuteur());

        } catch (NullPointerException e) {
            log.error("PB pour auteurs de " + nnt + "," + e.getMessage());
        }

        /************************************
         * Parsing des directeurs de la thèse
         * ***********************************/
        log.info("traitement de directeurs");
        try {
            traiterDirecteurs(techMD.getMdWrap().getXmlData().getThesisAdmin()
                    .getDirecteurThese());

        } catch (NullPointerException e) {
            log.error("PB pour directeurs de " + nnt + "," + e.getMessage());
        }

        /************************************
         * Parsing des rapporteurs de la thèse
         * ***********************************/
        log.info("traitement des rapporteurs");
        try {
            traiterRapporteurs(techMD.getMdWrap().getXmlData().getThesisAdmin()
                    .getRapporteur());

        } catch (NullPointerException e) {
            log.error("PB pour les rapporteurs de " + nnt + "," + e.getMessage());
        }

        /************************************
         * Parsing du président du jury de la thèse
         * ***********************************/
        log.info("traitement du président du jury de la thèse");
        try {
            traiterPresident(techMD.getMdWrap().getXmlData().getThesisAdmin()
                    .getPresidentJury());

        } catch (NullPointerException e) {
            log.error("PB pour le président du jury de " + nnt + "," + e.getMessage());
        }

        /************************************
         * Parsing des membres du jury de la thèse
         * ***********************************/
        log.info("traitement des membres du jury de la thèse");
        try {
            traiterMembreJury(techMD.getMdWrap().getXmlData().getThesisAdmin()
                    .getMembreJury());

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
     * Instancie un objet Thèse à partir des informations de la thèse
     * Assigne le rôle à la personne
     * Renseigne les champs d'autocomplétion sur la thématique
     *
     * @param item La personne
     * @param role Rôle de la personne
     */
    private void traiterThese(PersonneModelES item, String role) {
        TheseModelES these = new TheseModelES(theseModelES, role);
        item.getTheses().add(these);
        item.getRoles().add(role);

        // On configure l'autocomplétion sur la thématique
        these.getSujets_rameau().stream().forEach((sujet) -> item.getCompletion_thematique().add(SuggestionES.builder().input(sujet).weight(10).build()));
        these.getSujets().forEach((k, v) -> v.stream().forEach((sujet) -> item.getCompletion_thematique().add(SuggestionES.builder().input(sujet).weight(10).build())));
        item.getCompletion_thematique().add(SuggestionES.builder().input(these.getDiscipline()).weight(10).build());

        // On ajoute l'établissement de soutenance
        item.getEtablissements().add(these.getEtablissement_soutenance().getNom());

        // On ajoute les domaines
        item.getDomaines().addAll(these.getOaiSetNames());
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

            traiterThese(personne, AUTEUR);
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

            traiterThese(personne, DIRECTEUR);
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
            traiterThese(personne, RAPPORTEUR);
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

        traiterThese(personne, PRESIDENT);
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

            traiterThese(personne, MEMBRE_DU_JURY);
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

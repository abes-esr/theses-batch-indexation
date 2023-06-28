package fr.abes.theses_batch_indexation.dto.personne;

import fr.abes.theses_batch_indexation.dto.these.OrganismeDTO;
import fr.abes.theses_batch_indexation.dto.these.Source;
import fr.abes.theses_batch_indexation.dto.these.Status;
import fr.abes.theses_batch_indexation.model.oaisets.Set;
import fr.abes.theses_batch_indexation.model.tef.*;
import fr.abes.theses_batch_indexation.utils.OutilsTef;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Getter
public class RecherchePersonneMappe {

    private List<RecherchePersonneModelES> personnes = new ArrayList<>();
    private TheseModelES theseModelES = new TheseModelES();

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

        /************************************************************************
         *                    Parsing des informations de la thèse
         * *********************************************************************/

        /************************************
         * Parsing de l'identifiant
         * ***********************************/
        theseModelES.setId(id);

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
            log.error(String.format("%s - Champs '%s' : La valeur est nulle dans le TEF", id, "Sujets"));
        } catch (Exception e) {
            log.error(String.format("%s - Champs '%s' : Erreur de traitement : %s", id, "Sujets", e.getMessage()));
        }

        /************************************
         * Parsing des sujets Rameau
         * ***********************************/
        log.info("traitement de sujetsRameau");

        try {
            List<VedetteRameauNomCommun> sujetsRameauNomCommunDepuisTef = dmdSec.getMdWrap().getXmlData()
                    .getThesisRecord().getSujetRameau().getVedetteRameauNomCommun();
            Iterator<VedetteRameauNomCommun> vedetteRameauNomCommunIterator = sujetsRameauNomCommunDepuisTef.iterator();
            while (vedetteRameauNomCommunIterator.hasNext()) {
                VedetteRameauNomCommun vedette = vedetteRameauNomCommunIterator.next();
                if (vedette.getElementdEntree() != null) {
                    theseModelES.getSujets_rameau().add(new SujetRameauES(vedette.getElementdEntree().getAutoriteExterne(), vedette.getElementdEntree().getContent()));
                }
            }
            List<VedetteRameauAuteurTitre> sujetsRameauAuteurTitreDepuisTef = dmdSec.getMdWrap().getXmlData()
                    .getThesisRecord().getSujetRameau().getVedetteRameauAuteurTitre();
            Iterator<VedetteRameauAuteurTitre> vedetteRameauAuteurTitreIterator = sujetsRameauAuteurTitreDepuisTef.iterator();
            while (vedetteRameauAuteurTitreIterator.hasNext()) {
                VedetteRameauAuteurTitre vedette = vedetteRameauAuteurTitreIterator.next();
                if (vedette.getElementdEntree() != null) {
                    theseModelES.getSujets_rameau().add(new SujetRameauES(vedette.getElementdEntree().getAutoriteExterne(), vedette.getElementdEntree().getContent()));
                }
            }
            List<VedetteRameauCollectivite> sujetsRameauCollectiviteDepuisTef = dmdSec.getMdWrap().getXmlData()
                    .getThesisRecord().getSujetRameau().getVedetteRameauCollectivite();
            Iterator<VedetteRameauCollectivite> vedetteRameauCollectiviteIterator = sujetsRameauCollectiviteDepuisTef.iterator();
            while (vedetteRameauCollectiviteIterator.hasNext()) {
                VedetteRameauCollectivite vedette = vedetteRameauCollectiviteIterator.next();
                if (vedette.getElementdEntree() != null) {
                    theseModelES.getSujets_rameau().add(new SujetRameauES(vedette.getElementdEntree().getAutoriteExterne(), vedette.getElementdEntree().getContent()));
                }
            }
            List<VedetteRameauFamille> sujetsRameauFamilleDepuisTef = dmdSec.getMdWrap().getXmlData()
                    .getThesisRecord().getSujetRameau().getVedetteRameauFamille();
            Iterator<VedetteRameauFamille> vedetteRameauFamilleIterator = sujetsRameauFamilleDepuisTef.iterator();
            while (vedetteRameauFamilleIterator.hasNext()) {
                VedetteRameauFamille vedette = vedetteRameauFamilleIterator.next();
                if (vedette.getElementdEntree() != null) {
                    theseModelES.getSujets_rameau().add(new SujetRameauES(vedette.getElementdEntree().getAutoriteExterne(), vedette.getElementdEntree().getContent()));
                }
            }
            List<VedetteRameauPersonne> sujetsRameauPersonneDepuisTef = dmdSec.getMdWrap().getXmlData()
                    .getThesisRecord().getSujetRameau().getVedetteRameauPersonne();
            Iterator<VedetteRameauPersonne> vedetteRameauPersonneIterator = sujetsRameauPersonneDepuisTef.iterator();
            while (vedetteRameauPersonneIterator.hasNext()) {
                VedetteRameauPersonne vedette = vedetteRameauPersonneIterator.next();
                if (vedette.getElementdEntree() != null) {
                    theseModelES.getSujets_rameau().add(new SujetRameauES(vedette.getElementdEntree().getAutoriteExterne(), vedette.getElementdEntree().getContent()));
                }
            }
            List<VedetteRameauNomGeographique> sujetsRameauNomGeographiqueDepuisTef = dmdSec.getMdWrap().getXmlData()
                    .getThesisRecord().getSujetRameau().getVedetteRameauNomGeographique();
            Iterator<VedetteRameauNomGeographique> vedetteRameauNomGeographiqueIterator = sujetsRameauNomGeographiqueDepuisTef.iterator();
            while (vedetteRameauNomGeographiqueIterator.hasNext()) {
                VedetteRameauNomGeographique vedette = vedetteRameauNomGeographiqueIterator.next();
                if (vedette.getElementdEntree() != null) {
                    theseModelES.getSujets_rameau().add(new SujetRameauES(vedette.getElementdEntree().getAutoriteExterne(), vedette.getElementdEntree().getContent()));
                }
            }
            List<VedetteRameauTitre> sujetsRameauTitreDepuisTef = dmdSec.getMdWrap().getXmlData()
                    .getThesisRecord().getSujetRameau().getVedetteRameauTitre();
            Iterator<VedetteRameauTitre> vedetteRameauTitreIterator = sujetsRameauTitreDepuisTef.iterator();
            while (vedetteRameauTitreIterator.hasNext()) {
                VedetteRameauTitre vedette = vedetteRameauTitreIterator.next();
                if (vedette.getElementdEntree() != null) {
                    theseModelES.getSujets_rameau().add(new SujetRameauES(vedette.getElementdEntree().getAutoriteExterne(), vedette.getElementdEntree().getContent()));
                }
            }
        } catch (NullPointerException e) {
            if (theseModelES.getStatus() == Status.SOUTENUE) {
                log.error(String.format("%s - Champs '%s' : La valeur est nulle dans le TEF", id, "Sujets Rameau"));
            } else {
                // Pas de sujets Rameau pour les thèses en préparation
            }
        } catch (Exception e) {
            log.error(String.format("%s - Champs '%s' : Erreur de traitement : %s", id, "Sujets Rameau", e.getMessage()));
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
                if (!a.getLang().isEmpty()) {
                    theseModelES.getResumes().put(a.getLang(), a.getContent());
                } else {
                    log.error(String.format("%s - Champs '%s' : Le code langue est vide dans le TEF. Valeur du résumé : %s", id, "Résumé", a.getContent().substring(0, 30) + "..."));
                }
            }
        } catch (NullPointerException e) {
            log.error(String.format("%s - Champs '%s' : La valeur est nulle dans le TEF", id, "Résumé"));
        } catch (Exception e) {
            log.error(String.format("%s - Champs '%s' : Erreur de traitement : %s", id, "Résumé", e.getMessage()));
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
            log.error(String.format("%s - Champs '%s' : La valeur est nulle dans le TEF", id, "Discipline"));
        } catch (Exception e) {
            log.error(String.format("%s - Champs '%s' : Erreur de traitement : %s", id, "Discipline", e.getMessage()));
        }

        /************************************
         * Parsing de la date de soutenance
         * ***********************************/
        log.info("traitement de dateSoutenance");
        try {
            theseModelES.setDate_soutenance(techMD.getMdWrap().getXmlData().getThesisAdmin().getDateAccepted().getValue().toString());
        } catch (NullPointerException e) {
            if (theseModelES.getStatus() == Status.SOUTENUE) {
                log.error(String.format("%s - Champs '%s' : La valeur est nulle dans le TEF", id, "Date de soutenance"));
            }
        } catch (Exception e) {
            log.error(String.format("%s - Champs '%s' : Erreur de traitement : %s", id, "Date de soutenance", e.getMessage()));
        }

        if (theseModelES.getStatus() == Status.EN_PREPARATION) {
            /************************************
             * Parsing de la date d'inscription
             * ***********************************/
            log.info("traitement de datePremiereInscriptionDoctorat");
            try {
                theseModelES.setDate_inscription(techMD.getMdWrap().getXmlData().getThesisAdmin().getThesisDegree().getDatePremiereInscriptionDoctorat().toString());
            } catch (NullPointerException e) {
                log.error(String.format("%s - Champs '%s' : La valeur est nulle dans le TEF", id, "Date d'inscription"));
            } catch (Exception e) {
                log.error(String.format("%s - Champs '%s' : Erreur de traitement : %s", id, "Date d'inscription", e.getMessage()));
            }
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
            log.error(String.format("%s - Champs '%s' : La valeur est nulle dans le TEF", id, "Etablissements"));
        } catch (Exception e) {
            log.error(String.format("%s - Champs '%s' : Erreur de traitement : %s", id, "Etablissements", e.getMessage()));
        }

        /************************************
         * Parsing des Domaines
         * ***********************************/
        log.info("traitement de oaiSets");
        try {
            Iterator<String> oaiSetSpecIterator = techMD.getMdWrap().getXmlData().getThesisAdmin()
                    .getOaiSetSpec().iterator();
            while (oaiSetSpecIterator.hasNext()) {
                String oaiSetSpec = oaiSetSpecIterator.next();
                Optional<Set> leSet = oaiSets.stream().filter(d -> d.getSetSpec().equals(oaiSetSpec)).findFirst();
                theseModelES.getOaiSetNames().add(leSet.get().getSetName().trim());
            }

        } catch (NullPointerException e) {
            log.error(String.format("%s - Champs '%s' : La valeur est nulle dans le TEF", id, "Domaines (oaiSets)"));
        } catch (Exception e) {
            log.error(String.format("%s - Champs '%s' : Erreur de traitement : %s", id, "Domaines (oaiSets)", e.getMessage()));
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
            log.error(String.format("%s - Champs '%s' : La valeur est nulle dans le TEF", id, "Rôle " + Roles.AUTEUR));
        } catch (Exception e) {
            log.error(String.format("%s - Champs '%s' : Erreur de traitement : %s", id, "Rôle " + Roles.AUTEUR, e.getMessage()));
        }

        /************************************
         * Parsing des directeurs de la thèse
         * ***********************************/
        log.info("traitement de directeurs");
        try {
            traiterDirecteurs(techMD.getMdWrap().getXmlData().getThesisAdmin()
                    .getDirecteurThese());
        } catch (NullPointerException e) {
            log.error(String.format("%s - Champs '%s' : La valeur est nulle dans le TEF", id, "Rôle " + Roles.DIRECTEUR));
        } catch (Exception e) {
            log.error(String.format("%s - Champs '%s' : Erreur de traitement : %s", id, "Rôle " + Roles.DIRECTEUR, e.getMessage()));
        }

        /************************************
         * Parsing des rapporteurs de la thèse
         * ***********************************/
        log.info("traitement des rapporteurs");
        try {
            traiterRapporteurs(techMD.getMdWrap().getXmlData().getThesisAdmin()
                    .getRapporteur());

        } catch (NullPointerException e) {
            // Ce champs est optionnel
            //log.error(String.format("%s - Champs '%s' : La valeur est nulle dans le TEF", nnt, "Rôle "+Roles.RAPPORTEUR));
        } catch (Exception e) {
            log.error(String.format("%s - Champs '%s' : Erreur de traitement : %s", id, "Rôle " + Roles.RAPPORTEUR, e.getMessage()));
        }

        /************************************
         * Parsing du président du jury de la thèse
         * ***********************************/
        log.info("traitement du président du jury de la thèse");
        try {
            traiterPresident(techMD.getMdWrap().getXmlData().getThesisAdmin()
                    .getPresidentJury());

        } catch (NullPointerException e) {
            // Ce champs est optionnel
            //log.error(String.format("%s - Champs '%s' : La valeur est nulle dans le TEF", nnt, "Rôle "+Roles.PRESIDENT));
        } catch (Exception e) {
            log.error(String.format("%s - Champs '%s' : Erreur de traitement : %s", id, "Rôle " + Roles.PRESIDENT, e.getMessage()));
        }

        /************************************
         * Parsing des membres du jury de la thèse
         * ***********************************/
        log.info("traitement des membres du jury de la thèse");
        try {
            traiterMembreJury(techMD.getMdWrap().getXmlData().getThesisAdmin()
                    .getMembreJury());

        } catch (NullPointerException e) {
            // Ce champs est optionnel
            //log.error(String.format("%s - Champs '%s' : La valeur est nulle dans le TEF", nnt, "Rôle "+Roles.MEMBRE_DU_JURY));
        } catch (Exception e) {
            log.error(String.format("%s - Champs '%s' : Erreur de traitement : %s", id, "Rôle " + Roles.MEMBRE_DU_JURY, e.getMessage()));
        }
    }

    /**
     * Instancie un objet Thèse à partir des informations de la thèse
     * Assigne le rôle à la personne
     * Renseigne les champs d'autocomplétion sur la thématique
     *
     * @param item La personne
     * @param role Rôle de la personne
     */
    private void traiterThese(RecherchePersonneModelES item, String role) {
        TheseModelES these = new TheseModelES(theseModelES, role);

        // On ajoute l'identifiant de la thèse et le nombre de thèses
        item.getTheses_id().add(these.getId());
        item.setNb_theses(item.getTheses_id().size());

        // On ajoute le rôle
        item.getRoles().add(role);

        // On ajoute la date de soutenance
        if (these.getDate_soutenance() != null) {
            item.getTheses_date().add(these.getDate_soutenance());
        }

        // On ajoute l'établissement de soutenance
        item.getEtablissements().add(these.getEtablissement_soutenance().getNom());

        // On ajoute les disciplines
        item.getDisciplines().add(these.getDiscipline());

        // On ajoute les thématiques (sujets libres, sujets Rameau, resumes, discipline)
        // Sujets libres
        if (these.getSujets() != null) {
            for (String lang : these.getSujets().keySet()) {
                if (item.getThematiques().containsKey(lang)) {
                    item.getThematiques().get(lang).addAll(these.getSujets().get(lang));
                } else {
                    item.getThematiques().put(lang, new ArrayList<>());
                    item.getThematiques().get(lang).addAll(these.getSujets().get(lang));
                }
            }
        }

        // Sujets Rameau
        if (these.getSujets_rameau() != null) {
            if (!item.getThematiques().containsKey("fr")) {
                item.getThematiques().put("fr", new ArrayList<>());
            }

            item.getThematiques().get("fr").addAll(these.getSujets_rameau().stream()
                    .map(e -> e.getLibelle())
                    .collect(Collectors.toList()));
        }

        // Resumes
        if (these.getResumes() != null) {
            for (String lang : these.getResumes().keySet()) {
                if (item.getThematiques().containsKey(lang)) {
                    item.getThematiques().get(lang).add(these.getResumes().get(lang));
                } else {
                    item.getThematiques().put(lang, new ArrayList<>());
                    item.getThematiques().get(lang).add(these.getResumes().get(lang));
                }
            }
        }

        // Disciplines
        if (these.getDiscipline() != null) {
            if (!item.getThematiques().containsKey("fr")) {
                item.getThematiques().put("fr", new ArrayList<>());
            }

            item.getThematiques().get("fr").add(these.getDiscipline());
        }

        // Facettes
        item.getFacette_roles().add(role.substring(0, 1).toUpperCase() + role.substring(1));
        item.getFacette_etablissements().add(these.getEtablissement_soutenance().getNom());
        item.getFacette_domaines().addAll(these.getOaiSetNames());
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

            RecherchePersonneModelES personne = findPersonne(OutilsTef.getPPN(item.getAutoriteExterne()));

            if (personne == null) {
                // On créé la personne
                personne = new RecherchePersonneModelES(
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

            RecherchePersonneModelES personne = findPersonne(OutilsTef.getPPN(item.getAutoriteExterne()));

            if (personne == null) {
                // On ajoute la personne
                personne = new RecherchePersonneModelES(
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

            RecherchePersonneModelES personne = findPersonne(OutilsTef.getPPN(item.getAutoriteExterne()));

            if (personne == null) {
                // On ajoute la personne
                personne = new RecherchePersonneModelES(
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
        RecherchePersonneModelES personne = findPersonne(OutilsTef.getPPN(item.getAutoriteExterne()));

        if (personne == null) {
            // On ajoute la personne
            personne = new RecherchePersonneModelES(
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

            RecherchePersonneModelES personne = findPersonne(OutilsTef.getPPN(item.getAutoriteExterne()));

            if (personne == null) {
                // On ajoute la personne
                personne = new RecherchePersonneModelES(
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
     * @return RecherchePersonneModelES La personne ou null
     */
    private RecherchePersonneModelES findPersonne(String id) {
        return personnes.stream()
                .filter(item -> (item.isHas_idref() && item.getPpn().equals(id)))
                .findAny()
                .orElse(null);
    }
}

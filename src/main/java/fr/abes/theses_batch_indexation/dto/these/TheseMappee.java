package fr.abes.theses_batch_indexation.dto.these;

import fr.abes.theses_batch_indexation.model.tef.*;
import fr.abes.theses_batch_indexation.model.oaisets.Set;
import fr.abes.theses_batch_indexation.utils.OutilsTef;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.*;

@Slf4j
public class TheseMappee {

    //String id;
    String dateInsertionDansES;
    String cas;
    String accessible;
    String source;
    String status;
    Boolean isSoutenue;
    String codeEtab;
    String nnt;
    String dateSoutenance;
    String datePremiereInscriptionDoctorat;
    String dateFinEmbargo;
    String dateFiltre;
    List<String> ppn;

    Map<String, String> titres = new HashMap<>();
    String titrePrincipal; // on veut ce titre dans un index à part pour faciliter l'affichage dans le front
    Map<String, String> resumes = new HashMap<>();
    List<String> langues = new ArrayList<>();
    OrganismeDTO etabSoutenance = new OrganismeDTO();
    String etabSoutenanceN;
    List<OrganismeDTO> etabsCotutelle = new ArrayList<>();
    List<String> etabsCotutelleN = new ArrayList<>();
    List<OrganismeDTO> ecolesDoctorales = new ArrayList<>();
    List<String> ecolesDoctoralesN = new ArrayList<>();
    List<OrganismeDTO> partenairesRecherche = new ArrayList<>();
    List<String> partenairesRechercheN = new ArrayList<>();


    String discipline;
    List<PersonneDTO> auteurs = new ArrayList<>();
    List<String> auteursNP = new ArrayList<>();
    List<PersonneDTO> directeurs = new ArrayList<>();
    List<String> directeursNP = new ArrayList<>();
    PersonneDTO presidentJury = new PersonneDTO();
    String presidentJuryNP;
    List<PersonneDTO> membresJury = new ArrayList<>();
    List<String> membresJuryNP = new ArrayList<>();
    List<PersonneDTO> rapporteurs = new ArrayList<>();
    List<String> rapporteursNP = new ArrayList<>();
    List<SujetRameauDTO> sujetsRameau = new ArrayList<>();
    List<String> sujetsRameauPpn = new ArrayList<>();
    List<String> sujetsRameauLibelle = new ArrayList<>();
    List<SujetDTO> sujets = new ArrayList<>();
    List<String> sujetsLibelle = new ArrayList<>();
    List<String> oaiSetNames = new ArrayList<>();
    String theseTravaux = "non";

    public TheseMappee(Mets mets, List<Set> oaiSets) {
        try {

            DmdSec dmdSec = mets.getDmdSec().get(1);
            AmdSec amdSec = mets.getAmdSec().get(0);

            TechMD techMD = null;

            try {

                // dateInsertionDansES
                dateInsertionDansES = java.time.Clock.systemUTC().instant().toString();

                // nnt
                techMD = amdSec.getTechMD().stream().filter(d -> d.getMdWrap().getXmlData().getThesisAdmin() != null).findFirst().orElse(null);

                Iterator<Identifier> iteIdentifiers = techMD.getMdWrap().getXmlData().getThesisAdmin().getIdentifier().iterator();
                while (iteIdentifiers.hasNext()) {
                    Identifier i = iteIdentifiers.next();
                    if (isNnt(i.getValue()))
                        nnt = i.getValue();
                }
                log.info("traitement de " + nnt);
            } catch (NullPointerException e) {
                log.error("PB pour nnt " + e);
            }

            // id
            //id = dmdSec.getID();

            // cas et codeEtab
            log.info("traitement de cas et codeEtab");
            try {
                Optional<DmdSec> starGestion = mets.getDmdSec().stream().filter(d -> d.getMdWrap().getXmlData().getStarGestion() != null).findFirst();
                if (starGestion.isPresent()) {
                    cas = starGestion.get().getMdWrap().getXmlData().getStarGestion().getTraitements().getScenario();
                    codeEtab = starGestion.get().getMdWrap().getXmlData().getStarGestion().getCodeEtab();
                }
            } catch (NullPointerException e) {
                log.error("PB pour cas de " + nnt + e.getMessage());
            }

            // titrePrincipal

            log.info("traitement de titrePrincipal");
            try {
                titrePrincipal = dmdSec.getMdWrap().getXmlData().getThesisRecord().getTitle().getContent();
            } catch (NullPointerException e) {
                log.error("PB pour titrePrincipal de " + nnt + e.getMessage());
            }
            // titres
            log.info("traitement de titres");
            try {
                titres.put(
                        dmdSec.getMdWrap().getXmlData().getThesisRecord().getTitle().getLang(),
                        dmdSec.getMdWrap().getXmlData().getThesisRecord().getTitle().getContent());

                if (dmdSec.getMdWrap().getXmlData().getThesisRecord().getAlternative() != null) {
                    Iterator<Alternative> titreAlternativeIterator = dmdSec.getMdWrap().getXmlData().getThesisRecord().getAlternative().iterator();
                    while (titreAlternativeIterator.hasNext()) {
                        Alternative a = titreAlternativeIterator.next();
                        titres.put(a.getLang(), a.getContent());
                    }
                }
            } catch (NullPointerException e) {
                log.error("PB pour titres de " + nnt + e.getMessage());
            }

            // resumes

            log.info("traitement de resumes");
            try {
                List<Abstract> abstracts = dmdSec.getMdWrap().getXmlData().getThesisRecord().getAbstract();
                Iterator<Abstract> abstractIterator = abstracts.iterator();
                while (abstractIterator.hasNext()) {
                    Abstract a = abstractIterator.next();
                    resumes.put(a.getLang(), a.getContent());
                }
            } catch (NullPointerException e) {
                log.error("PB pour resumes de " + nnt + e.getMessage());
            }

            // langues

            log.info("traitement de langues");
            try {
                List<Language> languages = dmdSec.getMdWrap().getXmlData().getThesisRecord().getLanguage();
                Iterator<Language> languageIterator = languages.iterator();
                while (languageIterator.hasNext()) {
                    Language l = languageIterator.next();
                    langues.add(l.getValue());
                }
            } catch (NullPointerException e) {
                log.error("PB pour langue de " + nnt + e.getMessage());
            }

            // date de soutenance

            log.info("traitement de dateSoutenance");
            try {
                dateSoutenance = techMD.getMdWrap().getXmlData().getThesisAdmin().getDateAccepted().getValue().toString();
            } catch (NullPointerException e) {
                log.error("PB pour dateSoutenance de " + nnt);
            }

            // date de datePremiereInscriptionDoctorat
            try {
                datePremiereInscriptionDoctorat = techMD.getMdWrap().getXmlData().getThesisAdmin().getThesisDegree().getDatePremiereInscriptionDoctorat().toString();
            } catch (NullPointerException e) {
                log.error("PB pour datePremiereInscriptionDoctorat de " + nnt);
            }

            // date de fin d'embargo

            log.info("traitement de datefinembargo ");
            try {
                if (!mets.getDmdSec().stream().filter(d -> d.getMdWrap().getXmlData().getStarGestion() != null).findFirst().orElse(null)
                        .getMdWrap().getXmlData().getStarGestion().getTraitements().getSorties().getDiffusion().getRestrictionTemporelleFin().isEmpty())
                    dateFinEmbargo = mets.getDmdSec().stream().filter(d -> d.getMdWrap().getXmlData().getStarGestion() != null).findFirst().orElse(null)
                            .getMdWrap().getXmlData().getStarGestion().getTraitements().getSorties().getDiffusion().getRestrictionTemporelleFin();
            } catch (NullPointerException e) {
                log.error("PB pour date fin embargo de " + nnt + "," + e.getMessage());
            }

            // accessible

            log.info("traitement de accessible");
            accessible = "non";

            try {
                if ((cas.equals("cas1") || cas.equals("cas2") || cas.equals("cas3") || cas.equals("cas4"))
                        && (dateFinEmbargo == null || dateFinEmbargo.isEmpty() || LocalDate.parse(dateFinEmbargo).isBefore(LocalDate.now()))) {
                    accessible = "oui";
                }
            } catch (NullPointerException e) {
                log.error("PB pour accessible de " + nnt);
            }

            // source
            log.info("traitement de source");

            boolean sourceIsSet = false;
            try {
                if (!nnt.equals("") &&
                        mets.getDmdSec().stream().filter(d -> d.getMdWrap().getXmlData().getStarGestion() != null).findFirst().orElse(null)
                                .getMdWrap().getXmlData().getStarGestion().getTraitements().getSorties().getCines().getIndicCines().equals("OK")) {
                    source = "star";
                    sourceIsSet = true;
                }
            } catch (NullPointerException ex) {
                log.error("impossible de récupérer le getIndicCines pour " + nnt + "(NullPointerException)");
            }
            if (!sourceIsSet) {
                if (nnt.equals("")) {
                    source = "step";
                } else {
                    source = "sudoc";
                }
            }


            // status
            log.info("traitement de status");
            if (source.equals("star") ||  source.equals("sudoc")) {
                status = "soutenu";
            } else {
                // source == "step"
                status = "enCours";
            }

            // isSoutenue
            log.info("traitement de isSoutenue");

            isSoutenue = true;
            try {
                Optional<DmdSec> dmdSecPourStepGestion = mets.getDmdSec().stream().filter(d -> d.getMdWrap().getXmlData().getStepGestion() != null).findFirst();

                if (dmdSecPourStepGestion.isPresent())
                    isSoutenue = dmdSecPourStepGestion.get().getMdWrap().getXmlData().getStepGestion().getStepEtat().equals("these")
                            || dmdSecPourStepGestion.get().getMdWrap().getXmlData().getStepGestion().getStepEtat().equals("soutenu");

            } catch (NullPointerException e) {
                log.error("PB pour isSoutenue de " + nnt + e.getMessage());
            }

            // date filtre
            log.info("traitement de datefiltre ");

            if (status.equals("enCours"))
                dateFiltre = datePremiereInscriptionDoctorat;
            else
                dateFiltre = dateSoutenance;

            // etablissements

            log.info("traitement de etablissements");

            try {
                List<ThesisDegreeGrantor> grantors = techMD.getMdWrap().getXmlData().getThesisAdmin()
                        .getThesisDegree().getThesisDegreeGrantor();
                Iterator<ThesisDegreeGrantor> iteGrantor = grantors.iterator();
                // l'étab de soutenance est le premier de la liste
                ThesisDegreeGrantor premier = iteGrantor.next();
                if (premier.getAutoriteExterne() != null && OutilsTef.ppnEstPresent(premier.getAutoriteExterne()))
                    etabSoutenance.setPpn(OutilsTef.getPPN(premier.getAutoriteExterne()));
                etabSoutenance.setNom(premier.getNom());
                etabSoutenanceN = premier.getNom();
                // les potentiels suivants sont les cotutelles
                while (iteGrantor.hasNext()) {
                    ThesisDegreeGrantor a = iteGrantor.next();
                    OrganismeDTO ctdto = new OrganismeDTO();
                    if (a.getAutoriteExterne() != null && OutilsTef.ppnEstPresent(a.getAutoriteExterne()))
                        ctdto.setPpn(OutilsTef.getPPN(a.getAutoriteExterne()));
                    ctdto.setNom(a.getNom());
                    if ("".equals(a.getNom())) {
                        log.warn("Pas de nom de cotutelle");
                    } else {
                        etabsCotutelle.add(ctdto);
                        etabsCotutelleN.add(a.getNom());
                    }
                }
            } catch (NullPointerException e) {
                log.error("PB pour etablissements de " + nnt + "," + e.getMessage());
            }

            // partenaires

            log.info("traitement de partenaires");
            try {
                List<PartenaireRecherche> partenairesDepuisTef = techMD.getMdWrap().getXmlData().getThesisAdmin()
                        .getPartenaireRecherche();
                Iterator<PartenaireRecherche> partenairesIterator = partenairesDepuisTef.iterator();
                while (partenairesIterator.hasNext()) {
                    PartenaireRecherche p = partenairesIterator.next();
                    OrganismeDTO pdto = new OrganismeDTO();
                    if (p.getAutoriteExterne() != null && OutilsTef.ppnEstPresent(p.getAutoriteExterne()))
                        pdto.setPpn(OutilsTef.getPPN(p.getAutoriteExterne()));
                    pdto.setNom(p.getNom());
                    pdto.setType(p.getType());
                    if ("".equals(p.getNom()) || "NON RENSEIGNE".equals(p.getNom())) {
                        log.warn("Pas de partenaires");
                    } else {
                        partenairesRecherche.add(pdto);
                        partenairesRechercheN.add(p.getNom());
                    }
                }
            } catch (NullPointerException e) {
                log.error("PB pour partenaire  " + nnt + "," + e.getMessage());

            }

            // ecoles doctorales

            log.info("traitement de ecolesDoctorales");
            try {
                List<EcoleDoctorale> ecolesDoctoralesDepuisTef = techMD.getMdWrap().getXmlData().getThesisAdmin()
                        .getEcoleDoctorale();
                Iterator<EcoleDoctorale> ecoleDoctoraleIterator = ecolesDoctoralesDepuisTef.iterator();
                while (ecoleDoctoraleIterator.hasNext()) {
                    EcoleDoctorale ecole = ecoleDoctoraleIterator.next();
                    if (!ecole.getNom().equals("")) {
                        OrganismeDTO edto = new OrganismeDTO();
                        if (ecole.getAutoriteExterne() != null && OutilsTef.ppnEstPresent(ecole.getAutoriteExterne()))
                            edto.setPpn(OutilsTef.getPPN(ecole.getAutoriteExterne()));
                        edto.setNom(ecole.getNom());
                        if ("".equals(ecole.getNom())) {
                            log.warn("Pas d'école doctorale");
                        } else {
                            ecolesDoctorales.add(edto);
                            ecolesDoctoralesN.add(ecole.getNom());
                        }
                    }
                }
            } catch (NullPointerException e) {
                log.error("PB pour ecolesDoctorales de " + nnt + "," + e.getMessage());

            }

            // discipline

            log.info("traitement de discipline");
            try {
                ThesisDegreeDiscipline tddisc = techMD.getMdWrap().getXmlData().getThesisAdmin()
                        .getThesisDegree().getThesisDegreeDiscipline();
                discipline = tddisc.getValue();
            } catch (NullPointerException e) {
                log.error("PB pour discipline de " + nnt + "," + e.getMessage());

            }

            // auteurs

            log.info("traitement de auteurs");
            try {
                List<Auteur> auteursDepuisTef = techMD.getMdWrap().getXmlData().getThesisAdmin()
                        .getAuteur();
                Iterator<Auteur> auteurIterator = auteursDepuisTef.iterator();
                while (auteurIterator.hasNext()) {
                    Auteur a = auteurIterator.next();
                    PersonneDTO adto = new PersonneDTO();
                    if (a.getAutoriteExterne() != null && OutilsTef.ppnEstPresent(a.getAutoriteExterne()))
                        adto.setPpn(OutilsTef.getPPN(a.getAutoriteExterne()));
                    adto.setNom(a.getNom());
                    adto.setPrenom(a.getPrenom());
                    auteurs.add(adto);
                    auteursNP.add(a.getNom() + " " + a.getPrenom());
                }
            } catch (NullPointerException e) {
                log.error("PB pour auteurs de " + nnt + "," + e.getMessage());
            }

            // directeurs
            log.info("traitement de directeurs");
            try {
                List<DirecteurThese> directeursDepuisTef = techMD.getMdWrap().getXmlData().getThesisAdmin()
                        .getDirecteurThese();
                Iterator<DirecteurThese> directeurTheseIterator = directeursDepuisTef.iterator();
                while (directeurTheseIterator.hasNext()) {
                    DirecteurThese dt = directeurTheseIterator.next();
                    PersonneDTO dtdto = new PersonneDTO();
                    if (dt.getAutoriteExterne() != null && OutilsTef.ppnEstPresent(dt.getAutoriteExterne()))
                        dtdto.setPpn(OutilsTef.getPPN(dt.getAutoriteExterne()));
                    dtdto.setNom(dt.getNom());
                    dtdto.setPrenom(dt.getPrenom());
                    directeurs.add(dtdto);
                    directeursNP.add(dt.getNom() + " " + dt.getPrenom());
                }
            } catch (NullPointerException e) {
                log.error("PB pour directeurs de " + nnt + "," + e.getMessage());
            }

            // presidentJury

            log.info("traitement de president jury");
            try {
                if (techMD.getMdWrap().getXmlData().getThesisAdmin().getPresidentJury() != null) {
                    PresidentJury presidentDepuisTef = techMD.getMdWrap().getXmlData().getThesisAdmin().getPresidentJury();
                    if (presidentDepuisTef.getAutoriteExterne() != null && OutilsTef.ppnEstPresent(presidentDepuisTef.getAutoriteExterne()))
                        presidentJury.setPpn(OutilsTef.getPPN(presidentDepuisTef.getAutoriteExterne()));
                    presidentJury.setNom(presidentDepuisTef.getNom());
                    presidentJury.setPrenom(presidentDepuisTef.getPrenom());
                    presidentJuryNP = presidentDepuisTef.getNom() + " " + presidentDepuisTef.getPrenom();
                }
            } catch (NullPointerException e) {
                log.error("PB pour president jury de " + nnt + "," + e.getMessage());
            }

            // membres Jury

            log.info("traitement de membres jury");
            try {
                List<MembreJury> membresDepuisTef = techMD.getMdWrap().getXmlData().getThesisAdmin()
                        .getMembreJury();
                Iterator<MembreJury> membresIterator = membresDepuisTef.iterator();
                while (membresIterator.hasNext()) {
                    MembreJury m = membresIterator.next();
                    PersonneDTO mdto = new PersonneDTO();
                    if (m.getAutoriteExterne() != null && OutilsTef.ppnEstPresent(m.getAutoriteExterne()))
                        mdto.setPpn(OutilsTef.getPPN(m.getAutoriteExterne()));
                    mdto.setNom(m.getNom());
                    mdto.setPrenom(m.getPrenom());
                    membresJury.add(mdto);
                    membresJuryNP.add(m.getNom() + " " + m.getPrenom());
                }
            } catch (NullPointerException e) {
                log.error("PB pour membres jury de " + nnt + "," + e.getMessage());
            }

            // rapporteurs

            log.info("traitement de rapporteurs");
            try {
                List<Rapporteur> rapporteursDepuisTef = techMD.getMdWrap().getXmlData().getThesisAdmin()
                        .getRapporteur();
                Iterator<Rapporteur> rapporteurIterator = rapporteursDepuisTef.iterator();
                while (rapporteurIterator.hasNext()) {
                    Rapporteur r = rapporteurIterator.next();
                    PersonneDTO rdto = new PersonneDTO();
                    if (r.getAutoriteExterne() != null && OutilsTef.ppnEstPresent(r.getAutoriteExterne()))
                        rdto.setPpn(OutilsTef.getPPN(r.getAutoriteExterne()));
                    rdto.setNom(r.getNom());
                    rdto.setPrenom(r.getPrenom());
                    rapporteurs.add(rdto);
                    rapporteursNP.add(r.getNom() + " " + r.getPrenom());
                }
            } catch (NullPointerException e) {
                log.error("PB pour rapporteurs de " + nnt + "," + e.getMessage());
            }

            // sujets

            log.info("traitement de sujets");
            try {
                List<Subject> subjects = dmdSec.getMdWrap().getXmlData().getThesisRecord().getSubject();
                Iterator<Subject> subjectIterator = subjects.iterator();
                while (subjectIterator.hasNext()) {
                    Subject s = subjectIterator.next();
                    SujetDTO sujetDTO = new SujetDTO();

                    if (s != null && s.getLang() != null) {
                        sujetDTO.setLangue(s.getLang());
                        sujetDTO.setLibelle(s.getContent());
                        sujets.add(sujetDTO);
                        sujetsLibelle.add(s.getContent());
                    }
                }
            } catch (NullPointerException e) {
                log.error("PB pour sujets de " + nnt + "," + e.getMessage());
            }

            // sujetsRameau

            log.info("traitement de sujetsRameau");

            try {
                List<VedetteRameauNomCommun> sujetsRameauNomCommunDepuisTef = dmdSec.getMdWrap().getXmlData()
                        .getThesisRecord().getSujetRameau().getVedetteRameauNomCommun();
                Iterator<VedetteRameauNomCommun> vedetteRameauNomCommunIterator = sujetsRameauNomCommunDepuisTef.iterator();
                while (vedetteRameauNomCommunIterator.hasNext()) {
                    VedetteRameauNomCommun vedette = vedetteRameauNomCommunIterator.next();
                    SujetRameauDTO sujetRameauDTO = new SujetRameauDTO();
                    if (vedette.getElementdEntree() != null) {
                        sujetRameauDTO.setPpn(vedette.getElementdEntree().getAutoriteExterne());
                        sujetRameauDTO.setLibelle(vedette.getElementdEntree().getContent());
                        sujetsRameau.add(sujetRameauDTO);
                        sujetsRameauPpn.add(vedette.getElementdEntree().getAutoriteExterne());
                        sujetsRameauLibelle.add(vedette.getElementdEntree().getContent());
                    }
                }
                List<VedetteRameauAuteurTitre> sujetsRameauAuteurTitreDepuisTef = dmdSec.getMdWrap().getXmlData()
                        .getThesisRecord().getSujetRameau().getVedetteRameauAuteurTitre();
                Iterator<VedetteRameauAuteurTitre> vedetteRameauAuteurTitreIterator = sujetsRameauAuteurTitreDepuisTef.iterator();
                while (vedetteRameauAuteurTitreIterator.hasNext()) {
                    VedetteRameauAuteurTitre vedette = vedetteRameauAuteurTitreIterator.next();
                    SujetRameauDTO sujetRameauDTO = new SujetRameauDTO();
                    if (vedette.getElementdEntree() != null) {
                        sujetRameauDTO.setPpn(vedette.getElementdEntree().getAutoriteExterne());
                        sujetRameauDTO.setLibelle(vedette.getElementdEntree().getContent());
                        sujetsRameau.add(sujetRameauDTO);
                        sujetsRameauPpn.add(vedette.getElementdEntree().getAutoriteExterne());
                        sujetsRameauLibelle.add(vedette.getElementdEntree().getContent());
                    }
                }
                List<VedetteRameauCollectivite> sujetsRameauCollectiviteDepuisTef = dmdSec.getMdWrap().getXmlData()
                        .getThesisRecord().getSujetRameau().getVedetteRameauCollectivite();
                Iterator<VedetteRameauCollectivite> vedetteRameauCollectiviteIterator = sujetsRameauCollectiviteDepuisTef.iterator();
                while (vedetteRameauCollectiviteIterator.hasNext()) {
                    VedetteRameauCollectivite vedette = vedetteRameauCollectiviteIterator.next();
                    SujetRameauDTO sujetRameauDTO = new SujetRameauDTO();
                    if (vedette.getElementdEntree() != null) {
                        sujetRameauDTO.setPpn(vedette.getElementdEntree().getAutoriteExterne());
                        sujetRameauDTO.setLibelle(vedette.getElementdEntree().getContent());
                        sujetsRameau.add(sujetRameauDTO);
                        sujetsRameauPpn.add(vedette.getElementdEntree().getAutoriteExterne());
                        sujetsRameauLibelle.add(vedette.getElementdEntree().getContent());
                    }
                }
                List<VedetteRameauFamille> sujetsRameauFamilleDepuisTef = dmdSec.getMdWrap().getXmlData()
                        .getThesisRecord().getSujetRameau().getVedetteRameauFamille();
                Iterator<VedetteRameauFamille> vedetteRameauFamilleIterator= sujetsRameauFamilleDepuisTef.iterator();
                while (vedetteRameauFamilleIterator.hasNext()) {
                    VedetteRameauFamille vedette = vedetteRameauFamilleIterator.next();
                    SujetRameauDTO sujetRameauDTO = new SujetRameauDTO();
                    if (vedette.getElementdEntree() != null) {
                        sujetRameauDTO.setPpn(vedette.getElementdEntree().getAutoriteExterne());
                        sujetRameauDTO.setLibelle(vedette.getElementdEntree().getContent());
                        sujetsRameau.add(sujetRameauDTO);
                        sujetsRameauPpn.add(vedette.getElementdEntree().getAutoriteExterne());
                        sujetsRameauLibelle.add(vedette.getElementdEntree().getContent());
                    }
                }
                List<VedetteRameauPersonne> sujetsRameauPersonneDepuisTef = dmdSec.getMdWrap().getXmlData()
                        .getThesisRecord().getSujetRameau().getVedetteRameauPersonne();
                Iterator<VedetteRameauPersonne> vedetteRameauPersonneIterator = sujetsRameauPersonneDepuisTef.iterator();
                while (vedetteRameauPersonneIterator.hasNext()) {
                    VedetteRameauPersonne vedette = vedetteRameauPersonneIterator.next();
                    SujetRameauDTO sujetRameauDTO = new SujetRameauDTO();
                    if (vedette.getElementdEntree() != null) {
                        sujetRameauDTO.setPpn(vedette.getElementdEntree().getAutoriteExterne());
                        sujetRameauDTO.setLibelle(vedette.getElementdEntree().getContent());
                        sujetsRameau.add(sujetRameauDTO);
                        sujetsRameauPpn.add(vedette.getElementdEntree().getAutoriteExterne());
                        sujetsRameauLibelle.add(vedette.getElementdEntree().getContent());
                    }
                }
                List<VedetteRameauNomGeographique> sujetsRameauNomGeographiqueDepuisTef = dmdSec.getMdWrap().getXmlData()
                        .getThesisRecord().getSujetRameau().getVedetteRameauNomGeographique();
                Iterator<VedetteRameauNomGeographique> vedetteRameauNomGeographiqueIterator = sujetsRameauNomGeographiqueDepuisTef.iterator();
                while (vedetteRameauNomGeographiqueIterator.hasNext()) {
                    VedetteRameauNomGeographique vedette = vedetteRameauNomGeographiqueIterator.next();
                    SujetRameauDTO sujetRameauDTO = new SujetRameauDTO();
                    if (vedette.getElementdEntree() != null) {
                        sujetRameauDTO.setPpn(vedette.getElementdEntree().getAutoriteExterne());
                        sujetRameauDTO.setLibelle(vedette.getElementdEntree().getContent());
                        sujetsRameau.add(sujetRameauDTO);
                        sujetsRameauPpn.add(vedette.getElementdEntree().getAutoriteExterne());
                        sujetsRameauLibelle.add(vedette.getElementdEntree().getContent());
                    }
                }
                List<VedetteRameauTitre> sujetsRameauTitreDepuisTef = dmdSec.getMdWrap().getXmlData()
                        .getThesisRecord().getSujetRameau().getVedetteRameauTitre();
                Iterator<VedetteRameauTitre> vedetteRameauTitreIterator = sujetsRameauTitreDepuisTef.iterator();
                while (vedetteRameauTitreIterator.hasNext()) {
                    VedetteRameauTitre vedette = vedetteRameauTitreIterator.next();
                    SujetRameauDTO sujetRameauDTO = new SujetRameauDTO();
                    if (vedette.getElementdEntree() != null) {
                        sujetRameauDTO.setPpn(vedette.getElementdEntree().getAutoriteExterne());
                        sujetRameauDTO.setLibelle(vedette.getElementdEntree().getContent());
                        sujetsRameau.add(sujetRameauDTO);
                        sujetsRameauPpn.add(vedette.getElementdEntree().getAutoriteExterne());
                        sujetsRameauLibelle.add(vedette.getElementdEntree().getContent());
                    }
                }
            } catch (NullPointerException e) {
                log.error("PB pour sujetsRameau de " + nnt + ", " + e.getMessage());
            }

            // oaiSets

            log.info("traitement de oaiSets");
            try {
                Iterator<String> oaiSetSpecIterator = techMD.getMdWrap().getXmlData().getThesisAdmin()
                        .getOaiSetSpec().iterator();
                while (oaiSetSpecIterator.hasNext()) {
                    String oaiSetSpec  = oaiSetSpecIterator.next();
                    Optional<Set> leSet = oaiSets.stream().filter(d -> d.getSetSpec().equals(oaiSetSpec)).findFirst();
                    oaiSetNames.add(leSet.get().getSetName());
                }

            } catch (NullPointerException e) {
                log.error("PB pour oaisets de " + nnt + "," + e.getMessage());
            }

            // theseTravaux

            log.info("traitement de theseTravaux");
            try {
                theseTravaux = techMD.getMdWrap().getXmlData().getThesisAdmin()
                        .getTheseSurTravaux();
            } catch (NullPointerException e) {
                log.error("PB pour thesesTravaux de " + nnt + "," + e.getMessage());
            }
        } catch (Exception e) {
            log.error("erreur lors du mappage = " + e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
            throw e;
        }
    }

    private boolean isNnt(String identifier) {
        if (identifier.length() == 12)
            return true;
        return false;
    }


    public String getNnt() {
        return nnt;
    }

    public void setNnt(String nnt) {
        this.nnt = nnt;
    }

    public OrganismeDTO getEtabSoutenance() {
        return etabSoutenance;
    }

    public void setEtabSoutenance(OrganismeDTO etabSoutenance) {
        this.etabSoutenance = etabSoutenance;
    }

    public List<OrganismeDTO> getPartenairesRecherche() {
        return partenairesRecherche;
    }

    public void setPartenairesRecherche(List<OrganismeDTO> partenairesRecherche) {
        this.partenairesRecherche = partenairesRecherche;
    }
    public List<PersonneDTO> getRapporteurs() {
        return rapporteurs;
    }
    public void setRapporteurs(List<PersonneDTO> rapporteurs) {
        this.rapporteurs = rapporteurs;
    }
    public List<PersonneDTO> getMembresJury() {
        return membresJury;
    }
    public void setMembresJury(List<PersonneDTO> membresJury) {
        this.membresJury = membresJury;
    }
    public List<PersonneDTO> getAuteurs() {
        return auteurs;
    }
    public void setAuteurs(List<PersonneDTO> auteurs) {
        this.auteurs = auteurs;
    }
    public List<PersonneDTO> getDirecteurs() {
        return directeurs;
    }
    public void setDirecteurs(List<PersonneDTO> directeurs) {
        this.directeurs = directeurs;
    }
    public Map<String, String> getResumes() {
        return resumes;
    }
    public void setResumes(Map<String, String> resumes) {
        this.resumes = resumes;
    }
    public String getDateSoutenance() {
        return dateSoutenance;
    }
    public void setDateSoutenance(String dateSoutenance) {
        this.dateSoutenance = dateSoutenance;
    }
    public String getDatePremiereInscriptionDoctorat() {
        return datePremiereInscriptionDoctorat;
    }

    public void setDatePremiereInscriptionDoctorat(String datePremiereInscriptionDoctorat) {
        this.datePremiereInscriptionDoctorat = datePremiereInscriptionDoctorat;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public List<String> getPpn() {
        return ppn;
    }
    public void setPpn(List<String> ppn) {
        this.ppn = ppn;
    }
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public String getAccessible() {
        return accessible;
    }
    public void setAccessible(String accessible) {
        this.accessible = accessible;
    }
    public Map<String, String> getTitres() {
        return titres;
    }
    public void setTitres(Map<String, String> titres) {
        this.titres = titres;
    }
    public List<OrganismeDTO> getEtabsCotutelle() {
        return etabsCotutelle;
    }
    public void setEtabsCotutelle(List<OrganismeDTO> etabsCotutelle) {
        this.etabsCotutelle = etabsCotutelle;
    }
    public String getDiscipline() {
        return discipline;
    }
    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }
    public List<OrganismeDTO> getEcolesDoctorales() {
        return ecolesDoctorales;
    }
    public void setEcolesDoctorales(List<OrganismeDTO> ecolesDoctorales) {
        this.ecolesDoctorales = ecolesDoctorales;
    }
    public String getCodeEtab() {
        return codeEtab;
    }
    public void setCodeEtab(String codeEtab) {
        this.codeEtab = codeEtab;
    }
    public String getDateFinEmbargo() {
        return dateFinEmbargo;
    }
    public void setDateFinEmbargo(String dateFinEmbargo) {
        this.dateFinEmbargo = dateFinEmbargo;
    }
    public List<String> getLangues() {
        return langues;
    }
    public void setLangues(List<String> langues) {
        this.langues = langues;
    }
    public List<String> getOaiSetNames() {
        return oaiSetNames;
    }
    public void setOaiSetNames(List<String> oaiSetNames) {
        this.oaiSetNames = oaiSetNames;
    }
    public List<SujetDTO> getSujets() {
        return sujets;
    }
    public void setSujets(List<SujetDTO> sujets) {
        this.sujets = sujets;
    }
}

package fr.abes.theses_batch_indexation.dto.these;

import fr.abes.theses_batch_indexation.model.tef.*;
import fr.abes.theses_batch_indexation.model.oaisets.Set;
import fr.abes.theses_batch_indexation.utils.OutilsTef;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Getter
@Setter
public class TheseMappee {

    String idDoc;
    String dateInsertionDansES;
    String cas;
    String accessible;
    String source;
    String status;
    Boolean isSoutenue;
    String codeEtab;
    String nnt;
    String numSujet;
    String numSujetSansS;
    String dateSoutenance;
    String dateCines;
    String datePremiereInscriptionDoctorat;
    String dateFinEmbargo;
    String dateFiltre;
    List<String> ppn;

    Map<String, String> titres = new HashMap<>();
    String titrePrincipal; // on veut ce titre dans un index à part pour faciliter l'affichage dans le front
    Map<String, String> resumes = new HashMap<>();
    List<String> langues = new ArrayList<>();
    OrganismeDTO etabSoutenance = new OrganismeDTO();
    String etabSoutenancePpn;
    String etabSoutenanceN;
    List<OrganismeDTO> etabsCotutelle = new ArrayList<>();
    List<String> etabsCotutellePpn = new ArrayList<>();
    List<String> etabsCotutelleN = new ArrayList<>();
    List<OrganismeDTO> ecolesDoctorales = new ArrayList<>();
    List<String> ecolesDoctoralesPpn = new ArrayList<>();
    List<String> ecolesDoctoralesN = new ArrayList<>();
    List<OrganismeDTO> partenairesRecherche = new ArrayList<>();
    List<String> partenairesRecherchePpn = new ArrayList<>();
    List<String> partenairesRechercheN = new ArrayList<>();


    String discipline;
    List<PersonneDTO> auteurs = new ArrayList<>();
    List<String> auteursNP = new ArrayList<>();
    List<String> auteursPN = new ArrayList<>();
    List<String> auteursPpn = new ArrayList<>();
    List<PersonneDTO> directeurs = new ArrayList<>();
    List<String> directeursNP = new ArrayList<>();
    List<String> directeursPN = new ArrayList<>();
    List<String> directeursPpn = new ArrayList<>();
    PersonneDTO presidentJury = new PersonneDTO();
    String presidentJuryNP;
    String presidentJuryPN;
    String presidentJuryPpn;
    List<PersonneDTO> membresJury = new ArrayList<>();
    List<String> membresJuryNP = new ArrayList<>();
    List<String> membresJuryPN = new ArrayList<>();
    List<String> membresJuryPpn = new ArrayList<>();
    List<PersonneDTO> rapporteurs = new ArrayList<>();
    List<String> rapporteursNP = new ArrayList<>();
    List<String> rapporteursPN = new ArrayList<>();
    List<String> rapporteursPpn = new ArrayList<>();
    List<SujetRameauDTO> sujetsRameau = new ArrayList<>();
    List<String> sujetsRameauPpn = new ArrayList<>();
    List<String> sujetsRameauLibelle = new ArrayList<>();
    List<SujetDTO> sujets = new ArrayList<>();
    List<String> sujetsLibelle = new ArrayList<>();
    List<String> oaiSetNames = new ArrayList<>();
    String theseTravaux = "non";

    public TheseMappee(Mets mets, List<Set> oaiSets, int idDoc) {
        try {

            DmdSec dmdSec = mets.getDmdSec().get(1);
            AmdSec amdSec = mets.getAmdSec().get(0);

            TechMD techMD = null;

            try {

                this.idDoc = String.valueOf(idDoc);

                // dateInsertionDansES
                dateInsertionDansES = java.time.Clock.systemUTC().instant().toString();

                // nnt
                log.debug("traitement de nnt");
                techMD = amdSec.getTechMD().stream().filter(d -> d.getMdWrap().getXmlData().getThesisAdmin() != null).findFirst().orElse(null);

                Iterator<Identifier> iteIdentifiers = techMD.getMdWrap().getXmlData().getThesisAdmin().getIdentifier().iterator();
                while (iteIdentifiers.hasNext()) {
                    Identifier i = iteIdentifiers.next();
                    if (isNnt(i.getValue()))
                        nnt = i.getValue();
                }
                log.info("traitement de " + nnt);

                // numsujet
                log.debug("traitement de numSujet");
                Optional<DmdSec> stepGestion = mets.getDmdSec().stream().filter(d -> d.getMdWrap().getXmlData().getStepGestion() != null).findFirst();

                if (stepGestion.isPresent()) {
                    if (stepGestion.get().getMdWrap().getXmlData().getStepGestion().getIDSUJET() != null) {
                        String numSujetAIndexer = stepGestion.get().getMdWrap().getXmlData().getStepGestion().getIDSUJET().substring(6,
                                stepGestion.get().getMdWrap().getXmlData().getStepGestion().getIDSUJET().length());
                        numSujet = "s".concat(numSujetAIndexer);
                        numSujetSansS = numSujetAIndexer;
                    }
                }
            } catch (NullPointerException e) {
                log.warn("PB pour nnt " + e);
            }

            // id
            //id = dmdSec.getID();

            // cas et codeEtab
            log.debug("traitement de cas et codeEtab");
            try {
                Optional<DmdSec> starGestion = mets.getDmdSec().stream().filter(d -> d.getMdWrap().getXmlData().getStarGestion() != null).findFirst();
                Optional<DmdSec> stepGestion = mets.getDmdSec().stream().filter(d -> d.getMdWrap().getXmlData().getStepGestion() != null).findFirst();
                if (starGestion.isPresent()) {
                    cas = starGestion.get().getMdWrap().getXmlData().getStarGestion().getTraitements().getScenario();
                    codeEtab = starGestion.get().getMdWrap().getXmlData().getStarGestion().getCodeEtab();
                }
                if (stepGestion.isPresent()) {
                    codeEtab = stepGestion.get().getMdWrap().getXmlData().getStepGestion().getCodeEtab();
                }
                // Ici

            } catch (NullPointerException e) {
                log.warn("PB pour cas de " + nnt + e.getMessage());
            }

            // titrePrincipal

            log.debug("traitement de titrePrincipal");
            try {
                titrePrincipal = dmdSec.getMdWrap().getXmlData().getThesisRecord().getTitle().getContent();
            } catch (NullPointerException e) {
                log.warn("PB pour titrePrincipal de " + nnt + e.getMessage());
            }
            // titres
            log.debug("traitement de titres");
            try {
                if (!dmdSec.getMdWrap().getXmlData().getThesisRecord().getTitle().getLang().isEmpty()) {
                    titres.put(
                            dmdSec.getMdWrap().getXmlData().getThesisRecord().getTitle().getLang(),
                            dmdSec.getMdWrap().getXmlData().getThesisRecord().getTitle().getContent());
                }

                if (dmdSec.getMdWrap().getXmlData().getThesisRecord().getAlternative() != null) {
                    Iterator<Alternative> titreAlternativeIterator = dmdSec.getMdWrap().getXmlData().getThesisRecord().getAlternative().iterator();
                    while (titreAlternativeIterator.hasNext()) {
                        Alternative a = titreAlternativeIterator.next();
                        if (!a.getLang().isEmpty())
                            titres.put(a.getLang(), a.getContent());
                    }
                }
            } catch (NullPointerException e) {
                log.warn("PB pour titres de " + nnt + e.getMessage());
            }

            // resumes

            log.debug("traitement de resumes");
            try {
                List<Abstract> abstracts = dmdSec.getMdWrap().getXmlData().getThesisRecord().getAbstract();
                Iterator<Abstract> abstractIterator = abstracts.iterator();
                while (abstractIterator.hasNext()) {
                    Abstract a = abstractIterator.next();
                    if (!a.getLang().isEmpty())
                        resumes.put(a.getLang(), a.getContent());
                }
            } catch (NullPointerException e) {
                log.warn("PB pour resumes de " + nnt + e.getMessage());
            }

            // langues

            log.debug("traitement de langues");
            try {
                List<Language> languages = dmdSec.getMdWrap().getXmlData().getThesisRecord().getLanguage();
                Iterator<Language> languageIterator = languages.iterator();
                while (languageIterator.hasNext()) {
                    Language l = languageIterator.next();
                    langues.add(l.getValue());
                }
            } catch (NullPointerException e) {
                log.warn("PB pour langue de " + nnt + e.getMessage());
            }

            // date de soutenance

            log.debug("traitement de dateSoutenance");
            try {
                dateSoutenance = techMD.getMdWrap().getXmlData().getThesisAdmin().getDateAccepted().getValue().toString();
            } catch (NullPointerException e) {
                log.warn("PB pour dateSoutenance de " + nnt);
            }

            // date d'archivage au Cines
            log.debug("traitement de dateCines");
            try {
                Optional<DmdSec> starGestion = mets.getDmdSec().stream().filter(d -> d.getMdWrap().getXmlData().getStarGestion() != null).findFirst();
                if (starGestion.isPresent()) {
                    dateCines = starGestion.get().getMdWrap().getXmlData().getStarGestion().getTraitements().getSorties().getCines().getDateCines().toString();
                }

            } catch (NullPointerException e) {
                log.warn("PB pour dateCines de " + nnt);
            }

            // date de datePremiereInscriptionDoctorat
            try {
                datePremiereInscriptionDoctorat = techMD.getMdWrap().getXmlData().getThesisAdmin().getThesisDegree().getDatePremiereInscriptionDoctorat().toString();
            } catch (NullPointerException e) {
                log.warn("PB pour datePremiereInscriptionDoctorat de " + nnt);
            }

            // date de fin d'embargo

            log.debug("traitement de datefinembargo ");
            try {
                if (!mets.getDmdSec().stream().filter(d -> d.getMdWrap().getXmlData().getStarGestion() != null).findFirst().orElse(null)
                        .getMdWrap().getXmlData().getStarGestion().getTraitements().getSorties().getDiffusion().getRestrictionTemporelleFin().isEmpty())
                    dateFinEmbargo = mets.getDmdSec().stream().filter(d -> d.getMdWrap().getXmlData().getStarGestion() != null).findFirst().orElse(null)
                            .getMdWrap().getXmlData().getStarGestion().getTraitements().getSorties().getDiffusion().getRestrictionTemporelleFin();
            } catch (NullPointerException e) {
                log.warn("PB pour date fin embargo de " + nnt + "," + e.getMessage());
            }

            // accessible

            log.debug("traitement de accessible");
            accessible = "non";

            try {
                if ((cas.equals("cas1") || cas.equals("cas2") || cas.equals("cas3") || cas.equals("cas4"))
                        && (dateFinEmbargo == null || dateFinEmbargo.isEmpty() || LocalDate.parse(dateFinEmbargo).isBefore(LocalDate.now()))) {
                    accessible = "oui";
                }

                Iterator<DmdSec> iteratorDmdSec = mets.getDmdSec().iterator();
                while (iteratorDmdSec.hasNext()) {
                    DmdSec dmdSecCourante = iteratorDmdSec.next();
                    if (dmdSecCourante.getID().contains("EDITION_AO"))
                        accessible = "oui";
                }

            } catch (NullPointerException e) {
                log.warn("PB pour accessible de " + nnt);
            }

            // source
            log.debug("traitement de source");

            boolean sourceIsSet = false;
            try {
                source = "sudoc";

                if (nnt == null || "".equals(nnt)) {
                    source = "step";
                }

                if (!(nnt == null || "".equals(nnt)) &&
                        mets.getDmdSec().stream().filter(d -> d.getMdWrap().getXmlData().getStarGestion() != null).findFirst().orElse(null)
                                .getMdWrap().getXmlData().getStarGestion().getTraitements().getSorties().getCines().getIndicCines().equals("OK")) {
                    source = "star";
                }

            } catch (NullPointerException ex) {
                log.warn("impossible de récupérer le getIndicCines pour " + nnt + "(NullPointerException)");
            }

            // isSoutenue
            log.debug("traitement de isSoutenue");

            isSoutenue = false;
            try {
                XMLGregorianCalendar dateAccepted = techMD.getMdWrap().getXmlData().getThesisAdmin().getDateAccepted().getValue();

                if (dateAccepted.isValid())
                    isSoutenue = true;

            } catch (NullPointerException e) {
                log.warn("PB pour isSoutenue de " + nnt + e.getMessage());
            }

            // status
            try {
                log.debug("traitement de status");

                status = "soutenue";

                Optional<DmdSec> starGestion = mets.getDmdSec().stream().filter(d -> d.getMdWrap().getXmlData().getStarGestion() != null).findFirst();
                Optional<DmdSec> stepGestion = mets.getDmdSec().stream().filter(d -> d.getMdWrap().getXmlData().getStepGestion() != null).findFirst();
                if (starGestion.isPresent()) {
                    String urlperene = starGestion.get().getMdWrap().getXmlData().getStarGestion().getTraitements().getSorties().getDiffusion().getUrlPerenne();
                    if (urlperene != null) {
                        Pattern pattern = Pattern.compile(".*\\/([0-9,A-Z]*)", Pattern.MULTILINE);
                        Matcher matcher = pattern.matcher(urlperene);
                        if (matcher.matches() && this.isNnt(matcher.group(1))) {
                            this.status = "soutenue";
                        } else {
                            this.status = "enCours";
                        }
                    }
                }

                if (stepGestion.isPresent()) {
                    this.status = "enCours";
                }

            } catch (NullPointerException e) {
                log.warn("PB pour status de " + nnt + "," + e.getMessage());
            }

            // date filtre
            log.debug("traitement de datefiltre ");

            if (status.equals("enCours"))
                dateFiltre = datePremiereInscriptionDoctorat;
            else
                dateFiltre = dateSoutenance;

            // etablissements

            log.debug("traitement de etablissements");

            try {
                List<ThesisDegreeGrantor> grantors = techMD.getMdWrap().getXmlData().getThesisAdmin()
                        .getThesisDegree().getThesisDegreeGrantor();
                Iterator<ThesisDegreeGrantor> iteGrantor = grantors.iterator();
                // l'étab de soutenance est le premier de la liste
                if (iteGrantor.hasNext()) {
                    ThesisDegreeGrantor premier = iteGrantor.next();
                    if (premier.getAutoriteExterne() != null && OutilsTef.ppnEstPresent(premier.getAutoriteExterne())) {
                        etabSoutenance.setPpn(OutilsTef.getPPN(premier.getAutoriteExterne()));
                        etabSoutenancePpn = OutilsTef.getPPN(premier.getAutoriteExterne());
                    }
                    etabSoutenance.setNom(premier.getNom());
                    etabSoutenanceN = premier.getNom();
                }

                // les potentiels suivants sont les cotutelles
                while (iteGrantor.hasNext()) {
                    ThesisDegreeGrantor a = iteGrantor.next();
                    OrganismeDTO ctdto = new OrganismeDTO();
                    if (a.getAutoriteExterne() != null && OutilsTef.ppnEstPresent(a.getAutoriteExterne())) {
                        ctdto.setPpn(OutilsTef.getPPN(a.getAutoriteExterne()));
                        etabsCotutellePpn.add(OutilsTef.getPPN(a.getAutoriteExterne()));
                    }
                    ctdto.setNom(a.getNom());
                    if ("".equals(a.getNom())) {
                        log.warn("Pas de nom de cotutelle");
                    } else {
                        etabsCotutelle.add(ctdto);
                        etabsCotutelleN.add(a.getNom());
                    }
                }
            } catch (NullPointerException e) {
                log.warn("PB pour etablissements de " + nnt + "," + e.getMessage());
            }

            // partenaires

            log.debug("traitement de partenaires");

            HashMap<String, String> type_partenaire_recherche = new HashMap<String, String>();
            type_partenaire_recherche.put("laboratoire", "Laboratoire");
            type_partenaire_recherche.put("equipeRecherche", "Equipe de recherche");
            type_partenaire_recherche.put("entreprise", "Entreprise");
            type_partenaire_recherche.put("fondation", "Fondation");

            try {
                List<PartenaireRecherche> partenairesDepuisTef = techMD.getMdWrap().getXmlData().getThesisAdmin()
                        .getPartenaireRecherche();
                Iterator<PartenaireRecherche> partenairesIterator = partenairesDepuisTef.iterator();
                while (partenairesIterator.hasNext()) {
                    PartenaireRecherche p = partenairesIterator.next();
                    OrganismeDTO pdto = new OrganismeDTO();
                    if (p.getAutoriteExterne() != null && OutilsTef.ppnEstPresent(p.getAutoriteExterne())) {
                        pdto.setPpn(OutilsTef.getPPN(p.getAutoriteExterne()));
                        partenairesRecherchePpn.add(OutilsTef.getPPN(p.getAutoriteExterne()));
                    }
                    pdto.setNom(p.getNom());
                    try {
                        if (type_partenaire_recherche.get(p.getType()) != null) {
                            pdto.setType(type_partenaire_recherche.get(p.getType()));
                        } else if (p.getType().equals("autreType")) {
                            pdto.setType(p.getAutreType());
                        }
                    } catch (Exception eTypePartenaireRecherche) {
                        log.warn("pb lors de la récupération du type du partenaire de recherche pour nnt = " + nnt);
                    }


                    if ("".equals(p.getNom()) || "NON RENSEIGNE".equals(p.getNom())) {
                        log.warn("Pas de partenaires");
                    } else {
                        partenairesRecherche.add(pdto);
                        partenairesRechercheN.add(p.getNom());
                    }
                }
            } catch (NullPointerException e) {
                log.warn("PB pour partenaire  " + nnt + "," + e.getMessage());

            }

            // ecoles doctorales

            log.debug("traitement de ecolesDoctorales");
            try {
                List<EcoleDoctorale> ecolesDoctoralesDepuisTef = techMD.getMdWrap().getXmlData().getThesisAdmin()
                        .getEcoleDoctorale();
                Iterator<EcoleDoctorale> ecoleDoctoraleIterator = ecolesDoctoralesDepuisTef.iterator();
                while (ecoleDoctoraleIterator.hasNext()) {
                    EcoleDoctorale ecole = ecoleDoctoraleIterator.next();
                    if (!ecole.getNom().equals("")) {
                        OrganismeDTO edto = new OrganismeDTO();
                        if (ecole.getAutoriteExterne() != null && OutilsTef.ppnEstPresent(ecole.getAutoriteExterne())) {
                            edto.setPpn(OutilsTef.getPPN(ecole.getAutoriteExterne()));
                            ecolesDoctoralesPpn.add(OutilsTef.getPPN(ecole.getAutoriteExterne()));
                        }
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
                log.warn("PB pour ecolesDoctorales de " + nnt + "," + e.getMessage());

            }

            // discipline

            log.debug("traitement de discipline");
            try {
                ThesisDegreeDiscipline tddisc = techMD.getMdWrap().getXmlData().getThesisAdmin()
                        .getThesisDegree().getThesisDegreeDiscipline();
                discipline = tddisc.getValue();
            } catch (NullPointerException e) {
                log.warn("PB pour discipline de " + nnt + "," + e.getMessage());

            }

            // auteurs

            log.debug("traitement de auteurs");
            try {
                List<Auteur> auteursDepuisTef = techMD.getMdWrap().getXmlData().getThesisAdmin()
                        .getAuteur();
                Iterator<Auteur> auteurIterator = auteursDepuisTef.iterator();
                while (auteurIterator.hasNext()) {
                    Auteur a = auteurIterator.next();
                    PersonneDTO adto = new PersonneDTO();
                    if (a.getAutoriteExterne() != null && OutilsTef.ppnEstPresent(a.getAutoriteExterne())) {
                        adto.setPpn(OutilsTef.getPPN(a.getAutoriteExterne()));
                        auteursPpn.add(OutilsTef.getPPN(a.getAutoriteExterne()));
                    }

                    adto.setNom(a.getNom());
                    adto.setPrenom(a.getPrenom());
                    auteurs.add(adto);
                    auteursNP.add(a.getNom() + " " + a.getPrenom());
                    auteursPN.add(a.getPrenom() + " " + a.getNom());
                }
            } catch (NullPointerException e) {
                log.warn("PB pour auteurs de " + nnt + "," + e.getMessage());
            }

            // co-auteurs

            log.debug("traitement de co-auteurs");
            try {
                List<CoAuteur> coAuteursDepuisTef = techMD.getMdWrap().getXmlData().getThesisAdmin()
                        .getCoAuteur();
                Iterator<CoAuteur> coAuteurIterator = coAuteursDepuisTef.iterator();
                while (coAuteurIterator.hasNext()) {
                    CoAuteur ca = coAuteurIterator.next();
                    PersonneDTO adto = new PersonneDTO();
                    if (ca.getAutoriteExterne() != null && OutilsTef.ppnEstPresent(ca.getAutoriteExterne())) {
                        adto.setPpn(OutilsTef.getPPN(ca.getAutoriteExterne()));
                        auteursPpn.add(OutilsTef.getPPN(ca.getAutoriteExterne()));
                    }

                    adto.setNom(ca.getNom());
                    adto.setPrenom(ca.getPrenom());
                    auteurs.add(adto);
                    auteursNP.add(ca.getNom() + " " + ca.getPrenom());
                    auteursPN.add(ca.getPrenom() + " " + ca.getNom());
                }
            } catch (NullPointerException e) {
                log.warn("PB pour co-auteurs de " + nnt + "," + e.getMessage());
            }

            // directeurs
            log.debug("traitement de directeurs");
            try {
                List<DirecteurThese> directeursDepuisTef = techMD.getMdWrap().getXmlData().getThesisAdmin()
                        .getDirecteurThese();
                Iterator<DirecteurThese> directeurTheseIterator = directeursDepuisTef.iterator();
                while (directeurTheseIterator.hasNext()) {
                    DirecteurThese dt = directeurTheseIterator.next();
                    PersonneDTO dtdto = new PersonneDTO();
                    if (dt.getAutoriteExterne() != null && OutilsTef.ppnEstPresent(dt.getAutoriteExterne())) {
                        dtdto.setPpn(OutilsTef.getPPN(dt.getAutoriteExterne()));
                        directeursPpn.add(OutilsTef.getPPN(dt.getAutoriteExterne()));
                    }
                    dtdto.setNom(dt.getNom());
                    dtdto.setPrenom(dt.getPrenom());
                    directeurs.add(dtdto);
                    directeursNP.add(dt.getNom() + " " + dt.getPrenom());
                    directeursPN.add(dt.getPrenom() + " " + dt.getNom());
                }
            } catch (NullPointerException e) {
                log.warn("PB pour directeurs de " + nnt + "," + e.getMessage());
            }

            // presidentJury

            log.debug("traitement de president jury");
            try {
                if (techMD.getMdWrap().getXmlData().getThesisAdmin().getPresidentJury() != null) {
                    PresidentJury presidentDepuisTef = techMD.getMdWrap().getXmlData().getThesisAdmin().getPresidentJury();
                    if (presidentDepuisTef.getAutoriteExterne() != null && OutilsTef.ppnEstPresent(presidentDepuisTef.getAutoriteExterne())) {
                        presidentJury.setPpn(OutilsTef.getPPN(presidentDepuisTef.getAutoriteExterne()));
                        presidentJuryPpn = OutilsTef.getPPN(presidentDepuisTef.getAutoriteExterne());
                    }

                    presidentJury.setNom(presidentDepuisTef.getNom());
                    presidentJury.setPrenom(presidentDepuisTef.getPrenom());
                    presidentJuryNP = presidentDepuisTef.getNom() + " " + presidentDepuisTef.getPrenom();
                    presidentJuryPN = presidentDepuisTef.getPrenom() + " " + presidentDepuisTef.getNom();
                }
            } catch (NullPointerException e) {
                log.warn("PB pour president jury de " + nnt + "," + e.getMessage());
            }

            // membres Jury

            log.debug("traitement de membres jury");
            try {
                List<MembreJury> membresDepuisTef = techMD.getMdWrap().getXmlData().getThesisAdmin()
                        .getMembreJury();
                Iterator<MembreJury> membresIterator = membresDepuisTef.iterator();
                while (membresIterator.hasNext()) {
                    MembreJury m = membresIterator.next();
                    PersonneDTO mdto = new PersonneDTO();
                    if (m.getAutoriteExterne() != null && OutilsTef.ppnEstPresent(m.getAutoriteExterne())) {
                        mdto.setPpn(OutilsTef.getPPN(m.getAutoriteExterne()));
                        membresJuryPpn.add(OutilsTef.getPPN(m.getAutoriteExterne()));
                    }
                    mdto.setNom(m.getNom());
                    mdto.setPrenom(m.getPrenom());
                    membresJury.add(mdto);
                    membresJuryNP.add(m.getNom() + " " + m.getPrenom());
                    membresJuryPN.add(m.getPrenom() + " " + m.getNom());
                }
            } catch (NullPointerException e) {
                log.warn("PB pour membres jury de " + nnt + "," + e.getMessage());
            }

            // rapporteurs

            log.debug("traitement de rapporteurs");
            try {
                List<Rapporteur> rapporteursDepuisTef = techMD.getMdWrap().getXmlData().getThesisAdmin()
                        .getRapporteur();
                Iterator<Rapporteur> rapporteurIterator = rapporteursDepuisTef.iterator();
                while (rapporteurIterator.hasNext()) {
                    Rapporteur r = rapporteurIterator.next();
                    PersonneDTO rdto = new PersonneDTO();
                    if (r.getAutoriteExterne() != null && OutilsTef.ppnEstPresent(r.getAutoriteExterne())) {
                        rdto.setPpn(OutilsTef.getPPN(r.getAutoriteExterne()));
                        rapporteursPpn.add(OutilsTef.getPPN(r.getAutoriteExterne()));
                    }
                    rdto.setNom(r.getNom());
                    rdto.setPrenom(r.getPrenom());
                    rapporteurs.add(rdto);
                    rapporteursNP.add(r.getNom() + " " + r.getPrenom());
                    rapporteursPN.add(r.getPrenom() + " " + r.getNom());
                }
            } catch (NullPointerException e) {
                log.warn("PB pour rapporteurs de " + nnt + "," + e.getMessage());
            }

            // sujets

            log.debug("traitement de sujets");
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
                log.warn("PB pour sujets de " + nnt + "," + e.getMessage());
            }

            // sujetsRameau

            log.debug("traitement de sujetsRameau");

            try {
                List<VedetteRameauNomCommun> sujetsRameauNomCommunDepuisTef = dmdSec.getMdWrap().getXmlData()
                        .getThesisRecord().getSujetRameau().getVedetteRameauNomCommun();
                Iterator<VedetteRameauNomCommun> vedetteRameauNomCommunIterator = sujetsRameauNomCommunDepuisTef.iterator();
                while (vedetteRameauNomCommunIterator.hasNext()) {
                    VedetteRameauNomCommun vedette = vedetteRameauNomCommunIterator.next();
                    SujetRameauDTO sujetRameauDTO = new SujetRameauDTO();
                    if (vedette.getElementdEntree() != null && !sujetsRameauPpn.contains(vedette.getElementdEntree().getAutoriteExterne())) {
                        sujetRameauDTO.setPpn(vedette.getElementdEntree().getAutoriteExterne());
                        sujetRameauDTO.setLibelle(vedette.getElementdEntree().getContent());
                        sujetsRameau.add(sujetRameauDTO);
                        sujetsRameauPpn.add(vedette.getElementdEntree().getAutoriteExterne());
                        sujetsRameauLibelle.add(vedette.getElementdEntree().getContent());
                    }
                    List<Subdivision> subdivisions = vedette.getSubdivision();
                        Iterator<Subdivision> subdivisionIterator = subdivisions.iterator();
                        while (subdivisionIterator.hasNext()) {
                            Subdivision subdivision1 = subdivisionIterator.next();
                            if (!sujetsRameauPpn.contains(subdivision1.getAutoriteExterne())) {
                                SujetRameauDTO subdivision = new SujetRameauDTO();
                                subdivision.setPpn(subdivision1.getAutoriteExterne());
                                subdivision.setLibelle(subdivision1.getContent());
                                sujetsRameau.add(subdivision);
                                sujetsRameauPpn.add(subdivision1.getAutoriteExterne());
                                sujetsRameauLibelle.add(subdivision1.getContent());
                            }
                        }
                }
                List<VedetteRameauAuteurTitre> sujetsRameauAuteurTitreDepuisTef = dmdSec.getMdWrap().getXmlData()
                        .getThesisRecord().getSujetRameau().getVedetteRameauAuteurTitre();
                Iterator<VedetteRameauAuteurTitre> vedetteRameauAuteurTitreIterator = sujetsRameauAuteurTitreDepuisTef.iterator();
                while (vedetteRameauAuteurTitreIterator.hasNext()) {
                    VedetteRameauAuteurTitre vedette = vedetteRameauAuteurTitreIterator.next();
                    SujetRameauDTO sujetRameauDTO = new SujetRameauDTO();
                    if (vedette.getElementdEntree() != null && !sujetsRameauPpn.contains(vedette.getElementdEntree().getAutoriteExterne())) {
                        sujetRameauDTO.setPpn(vedette.getElementdEntree().getAutoriteExterne());
                        sujetRameauDTO.setLibelle(vedette.getElementdEntree().getContent());
                        sujetsRameau.add(sujetRameauDTO);
                        sujetsRameauPpn.add(vedette.getElementdEntree().getAutoriteExterne());
                        sujetsRameauLibelle.add(vedette.getElementdEntree().getContent());
                    }
                    List<Subdivision> subdivisions = vedette.getSubdivision();
                    Iterator<Subdivision> subdivisionIterator = subdivisions.iterator();
                    while (subdivisionIterator.hasNext()) {
                        Subdivision subdivision1 = subdivisionIterator.next();
                        if (!sujetsRameauPpn.contains(subdivision1.getAutoriteExterne())) {
                            SujetRameauDTO subdivision = new SujetRameauDTO();
                            subdivision.setPpn(subdivision1.getAutoriteExterne());
                            subdivision.setLibelle(subdivision1.getContent());
                            sujetsRameau.add(subdivision);
                            sujetsRameauPpn.add(subdivision1.getAutoriteExterne());
                            sujetsRameauLibelle.add(subdivision1.getContent());
                        }
                    }
                }
                List<VedetteRameauCollectivite> sujetsRameauCollectiviteDepuisTef = dmdSec.getMdWrap().getXmlData()
                        .getThesisRecord().getSujetRameau().getVedetteRameauCollectivite();
                Iterator<VedetteRameauCollectivite> vedetteRameauCollectiviteIterator = sujetsRameauCollectiviteDepuisTef.iterator();
                while (vedetteRameauCollectiviteIterator.hasNext()) {
                    VedetteRameauCollectivite vedette = vedetteRameauCollectiviteIterator.next();
                    SujetRameauDTO sujetRameauDTO = new SujetRameauDTO();
                    if (vedette.getElementdEntree() != null && !sujetsRameauPpn.contains(vedette.getElementdEntree().getAutoriteExterne())) {
                        sujetRameauDTO.setPpn(vedette.getElementdEntree().getAutoriteExterne());
                        sujetRameauDTO.setLibelle(vedette.getElementdEntree().getContent());
                        sujetsRameau.add(sujetRameauDTO);
                        sujetsRameauPpn.add(vedette.getElementdEntree().getAutoriteExterne());
                        sujetsRameauLibelle.add(vedette.getElementdEntree().getContent());
                    }
                    List<Subdivision> subdivisions = vedette.getSubdivision();
                    Iterator<Subdivision> subdivisionIterator = subdivisions.iterator();
                    while (subdivisionIterator.hasNext()) {
                        Subdivision subdivision1 = subdivisionIterator.next();
                        if (!sujetsRameauPpn.contains(subdivision1.getAutoriteExterne())) {
                            SujetRameauDTO subdivision = new SujetRameauDTO();
                            subdivision.setPpn(subdivision1.getAutoriteExterne());
                            subdivision.setLibelle(subdivision1.getContent());
                            sujetsRameau.add(subdivision);
                            sujetsRameauPpn.add(subdivision1.getAutoriteExterne());
                            sujetsRameauLibelle.add(subdivision1.getContent());
                        }
                    }
                }
                List<VedetteRameauFamille> sujetsRameauFamilleDepuisTef = dmdSec.getMdWrap().getXmlData()
                        .getThesisRecord().getSujetRameau().getVedetteRameauFamille();
                Iterator<VedetteRameauFamille> vedetteRameauFamilleIterator = sujetsRameauFamilleDepuisTef.iterator();
                while (vedetteRameauFamilleIterator.hasNext()) {
                    VedetteRameauFamille vedette = vedetteRameauFamilleIterator.next();
                    SujetRameauDTO sujetRameauDTO = new SujetRameauDTO();
                    if (vedette.getElementdEntree() != null && !sujetsRameauPpn.contains(vedette.getElementdEntree().getAutoriteExterne())) {
                        sujetRameauDTO.setPpn(vedette.getElementdEntree().getAutoriteExterne());
                        sujetRameauDTO.setLibelle(vedette.getElementdEntree().getContent());
                        sujetsRameau.add(sujetRameauDTO);
                        sujetsRameauPpn.add(vedette.getElementdEntree().getAutoriteExterne());
                        sujetsRameauLibelle.add(vedette.getElementdEntree().getContent());
                    }
                    List<Subdivision> subdivisions = vedette.getSubdivision();
                    Iterator<Subdivision> subdivisionIterator = subdivisions.iterator();
                    while (subdivisionIterator.hasNext()) {
                        Subdivision subdivision1 = subdivisionIterator.next();
                        if (!sujetsRameauPpn.contains(subdivision1.getAutoriteExterne())) {
                            SujetRameauDTO subdivision = new SujetRameauDTO();
                            subdivision.setPpn(subdivision1.getAutoriteExterne());
                            subdivision.setLibelle(subdivision1.getContent());
                            sujetsRameau.add(subdivision);
                            sujetsRameauPpn.add(subdivision1.getAutoriteExterne());
                            sujetsRameauLibelle.add(subdivision1.getContent());
                        }
                    }
                }
                List<VedetteRameauPersonne> sujetsRameauPersonneDepuisTef = dmdSec.getMdWrap().getXmlData()
                        .getThesisRecord().getSujetRameau().getVedetteRameauPersonne();
                Iterator<VedetteRameauPersonne> vedetteRameauPersonneIterator = sujetsRameauPersonneDepuisTef.iterator();
                while (vedetteRameauPersonneIterator.hasNext()) {
                    VedetteRameauPersonne vedette = vedetteRameauPersonneIterator.next();
                    SujetRameauDTO sujetRameauDTO = new SujetRameauDTO();
                    if (vedette.getElementdEntree() != null && !sujetsRameauPpn.contains(vedette.getElementdEntree().getAutoriteExterne())) {
                        sujetRameauDTO.setPpn(vedette.getElementdEntree().getAutoriteExterne());
                        sujetRameauDTO.setLibelle(vedette.getElementdEntree().getContent());
                        sujetsRameau.add(sujetRameauDTO);
                        sujetsRameauPpn.add(vedette.getElementdEntree().getAutoriteExterne());
                        sujetsRameauLibelle.add(vedette.getElementdEntree().getContent());
                    }
                    List<Subdivision> subdivisions = vedette.getSubdivision();
                    Iterator<Subdivision> subdivisionIterator = subdivisions.iterator();
                    while (subdivisionIterator.hasNext()) {
                        Subdivision subdivision1 = subdivisionIterator.next();
                        if (!sujetsRameauPpn.contains(subdivision1.getAutoriteExterne())) {
                            SujetRameauDTO subdivision = new SujetRameauDTO();
                            subdivision.setPpn(subdivision1.getAutoriteExterne());
                            subdivision.setLibelle(subdivision1.getContent());
                            sujetsRameau.add(subdivision);
                            sujetsRameauPpn.add(subdivision1.getAutoriteExterne());
                            sujetsRameauLibelle.add(subdivision1.getContent());
                        }
                    }
                }
                List<VedetteRameauNomGeographique> sujetsRameauNomGeographiqueDepuisTef = dmdSec.getMdWrap().getXmlData()
                        .getThesisRecord().getSujetRameau().getVedetteRameauNomGeographique();
                Iterator<VedetteRameauNomGeographique> vedetteRameauNomGeographiqueIterator = sujetsRameauNomGeographiqueDepuisTef.iterator();
                while (vedetteRameauNomGeographiqueIterator.hasNext()) {
                    VedetteRameauNomGeographique vedette = vedetteRameauNomGeographiqueIterator.next();
                    SujetRameauDTO sujetRameauDTO = new SujetRameauDTO();
                    if (vedette.getElementdEntree() != null && !sujetsRameauPpn.contains(vedette.getElementdEntree().getAutoriteExterne())) {
                        sujetRameauDTO.setPpn(vedette.getElementdEntree().getAutoriteExterne());
                        sujetRameauDTO.setLibelle(vedette.getElementdEntree().getContent());
                        sujetsRameau.add(sujetRameauDTO);
                        sujetsRameauPpn.add(vedette.getElementdEntree().getAutoriteExterne());
                        sujetsRameauLibelle.add(vedette.getElementdEntree().getContent());
                    }
                    List<Subdivision> subdivisions = vedette.getSubdivision();
                    Iterator<Subdivision> subdivisionIterator = subdivisions.iterator();
                    while (subdivisionIterator.hasNext()) {
                        Subdivision subdivision1 = subdivisionIterator.next();
                        if (!sujetsRameauPpn.contains(subdivision1.getAutoriteExterne())) {
                            SujetRameauDTO subdivision = new SujetRameauDTO();
                            subdivision.setPpn(subdivision1.getAutoriteExterne());
                            subdivision.setLibelle(subdivision1.getContent());
                            sujetsRameau.add(subdivision);
                            sujetsRameauPpn.add(subdivision1.getAutoriteExterne());
                            sujetsRameauLibelle.add(subdivision1.getContent());
                        }
                    }
                }
                List<VedetteRameauTitre> sujetsRameauTitreDepuisTef = dmdSec.getMdWrap().getXmlData()
                        .getThesisRecord().getSujetRameau().getVedetteRameauTitre();
                Iterator<VedetteRameauTitre> vedetteRameauTitreIterator = sujetsRameauTitreDepuisTef.iterator();
                while (vedetteRameauTitreIterator.hasNext()) {
                    VedetteRameauTitre vedette = vedetteRameauTitreIterator.next();
                    SujetRameauDTO sujetRameauDTO = new SujetRameauDTO();
                    if (vedette.getElementdEntree() != null && !sujetsRameauPpn.contains(vedette.getElementdEntree().getAutoriteExterne())) {
                        sujetRameauDTO.setPpn(vedette.getElementdEntree().getAutoriteExterne());
                        sujetRameauDTO.setLibelle(vedette.getElementdEntree().getContent());
                        sujetsRameau.add(sujetRameauDTO);
                        sujetsRameauPpn.add(vedette.getElementdEntree().getAutoriteExterne());
                        sujetsRameauLibelle.add(vedette.getElementdEntree().getContent());
                    }
                    List<Subdivision> subdivisions = vedette.getSubdivision();
                    Iterator<Subdivision> subdivisionIterator = subdivisions.iterator();
                    while (subdivisionIterator.hasNext()) {
                        Subdivision subdivision1 = subdivisionIterator.next();
                        if (!sujetsRameauPpn.contains(subdivision1.getAutoriteExterne())) {
                            SujetRameauDTO subdivision = new SujetRameauDTO();
                            subdivision.setPpn(subdivision1.getAutoriteExterne());
                            subdivision.setLibelle(subdivision1.getContent());
                            sujetsRameau.add(subdivision);
                            sujetsRameauPpn.add(subdivision1.getAutoriteExterne());
                            sujetsRameauLibelle.add(subdivision1.getContent());
                        }
                    }
                }
            } catch (NullPointerException e) {
                log.warn("PB pour sujetsRameau de " + nnt + ", " + e.getMessage());
            }

            // oaiSets

            log.debug("traitement de oaiSets");
            try {
                Iterator<String> oaiSetSpecIterator = techMD.getMdWrap().getXmlData().getThesisAdmin()
                        .getOaiSetSpec().iterator();
                while (oaiSetSpecIterator.hasNext()) {
                    String oaiSetSpec = oaiSetSpecIterator.next();
                    Optional<Set> leSet = oaiSets.stream().filter(d -> d.getSetSpec().equals(oaiSetSpec)).findFirst();
                    oaiSetNames.add(leSet.get().getSetName());
                }

            } catch (NullPointerException e) {
                log.warn("PB pour oaisets de " + nnt + "," + e.getMessage());
            }

            // theseTravaux

            log.debug("traitement de theseTravaux");
            try {
                theseTravaux = techMD.getMdWrap().getXmlData().getThesisAdmin()
                        .getTheseSurTravaux();
            } catch (NullPointerException e) {
                log.warn("PB pour thesesTravaux de " + nnt + "," + e.getMessage());
            }
        } catch (Exception e) {
            log.error("erreur lors du mappage = " + e.getMessage());
            log.error(Arrays.toString(e.getStackTrace()));
            throw e;
        }
    }

    private boolean isNnt(String identifier) {
        String regex = "\\d{4}[A-Z]{2}[0-9A-Z]{2}[0-9A-Z]{4}";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(identifier);
        return m.matches();
    }
}

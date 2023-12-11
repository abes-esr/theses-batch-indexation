package fr.abes.theses_batch_indexation.dto.these;

import fr.abes.theses_batch_indexation.model.tef.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Getter
public class TheseThematiquesMappee {

    String dateInsertionDansES;
    String nnt;
    List<String> libelles = new ArrayList<>();

    private boolean isNnt(String identifier) {
        if (identifier.length() == 12)
            return true;
        return false;
    }

    public TheseThematiquesMappee(Mets mets) {

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

                log.debug("traitement de " + nnt);
            }
        } catch (NullPointerException e) {
            log.error("PB pour nnt " + e);
        }


        log.debug("traitement de sujets");
        try {
            List<Subject> subjects = dmdSec.getMdWrap().getXmlData().getThesisRecord().getSubject();
            Iterator<Subject> subjectIterator = subjects.iterator();
            while (subjectIterator.hasNext()) {
                Subject s = subjectIterator.next();

                if (s != null && s.getLang() != null) {
                    libelles.add(s.getContent());
                }
            }
        } catch (NullPointerException e) {
            log.error("PB pour sujets de " + nnt + "," + e.getMessage());
        }

        log.debug("traitement de discipline");
        try {
            ThesisDegreeDiscipline tddisc = techMD.getMdWrap().getXmlData().getThesisAdmin()
                    .getThesisDegree().getThesisDegreeDiscipline();
            libelles.add(tddisc.getValue());
        } catch (NullPointerException e) {
            log.error("PB pour discipline de " + nnt + "," + e.getMessage());

        }

        log.debug("traitement de sujetsRameau");

        try {
            List<VedetteRameauNomCommun> sujetsRameauNomCommunDepuisTef = dmdSec.getMdWrap().getXmlData()
                    .getThesisRecord().getSujetRameau().getVedetteRameauNomCommun();
            Iterator<VedetteRameauNomCommun> vedetteRameauNomCommunIterator = sujetsRameauNomCommunDepuisTef.iterator();
            while (vedetteRameauNomCommunIterator.hasNext()) {
                VedetteRameauNomCommun vedette = vedetteRameauNomCommunIterator.next();
                if (vedette.getElementdEntree() != null)
                    libelles.add(vedette.getElementdEntree().getContent());
            }
            List<VedetteRameauAuteurTitre> sujetsRameauAuteurTitreDepuisTef = dmdSec.getMdWrap().getXmlData()
                    .getThesisRecord().getSujetRameau().getVedetteRameauAuteurTitre();
            Iterator<VedetteRameauAuteurTitre> vedetteRameauAuteurTitreIterator = sujetsRameauAuteurTitreDepuisTef.iterator();
            while (vedetteRameauAuteurTitreIterator.hasNext()) {
                VedetteRameauAuteurTitre vedette = vedetteRameauAuteurTitreIterator.next();
                if (vedette.getElementdEntree() != null)
                    libelles.add(vedette.getElementdEntree().getContent());

            }
            List<VedetteRameauCollectivite> sujetsRameauCollectiviteDepuisTef = dmdSec.getMdWrap().getXmlData()
                    .getThesisRecord().getSujetRameau().getVedetteRameauCollectivite();
            Iterator<VedetteRameauCollectivite> vedetteRameauCollectiviteIterator = sujetsRameauCollectiviteDepuisTef.iterator();
            while (vedetteRameauCollectiviteIterator.hasNext()) {
                VedetteRameauCollectivite vedette = vedetteRameauCollectiviteIterator.next();
                if (vedette.getElementdEntree() != null)
                    libelles.add(vedette.getElementdEntree().getContent());

            }
            List<VedetteRameauFamille> sujetsRameauFamilleDepuisTef = dmdSec.getMdWrap().getXmlData()
                    .getThesisRecord().getSujetRameau().getVedetteRameauFamille();
            Iterator<VedetteRameauFamille> vedetteRameauFamilleIterator= sujetsRameauFamilleDepuisTef.iterator();
            while (vedetteRameauFamilleIterator.hasNext()) {
                VedetteRameauFamille vedette = vedetteRameauFamilleIterator.next();
                if (vedette.getElementdEntree() != null)
                    libelles.add(vedette.getElementdEntree().getContent());

            }
            List<VedetteRameauPersonne> sujetsRameauPersonneDepuisTef = dmdSec.getMdWrap().getXmlData()
                    .getThesisRecord().getSujetRameau().getVedetteRameauPersonne();
            Iterator<VedetteRameauPersonne> vedetteRameauPersonneIterator = sujetsRameauPersonneDepuisTef.iterator();
            while (vedetteRameauPersonneIterator.hasNext()) {
                VedetteRameauPersonne vedette = vedetteRameauPersonneIterator.next();
                if (vedette.getElementdEntree() != null)
                    libelles.add(vedette.getElementdEntree().getContent());

            }
            List<VedetteRameauNomGeographique> sujetsRameauNomGeographiqueDepuisTef = dmdSec.getMdWrap().getXmlData()
                    .getThesisRecord().getSujetRameau().getVedetteRameauNomGeographique();
            Iterator<VedetteRameauNomGeographique> vedetteRameauNomGeographiqueIterator = sujetsRameauNomGeographiqueDepuisTef.iterator();
            while (vedetteRameauNomGeographiqueIterator.hasNext()) {
                VedetteRameauNomGeographique vedette = vedetteRameauNomGeographiqueIterator.next();
                if (vedette.getElementdEntree() != null)
                    libelles.add(vedette.getElementdEntree().getContent());
            }
            List<VedetteRameauTitre> sujetsRameauTitreDepuisTef = dmdSec.getMdWrap().getXmlData()
                    .getThesisRecord().getSujetRameau().getVedetteRameauTitre();
            Iterator<VedetteRameauTitre> vedetteRameauTitreIterator = sujetsRameauTitreDepuisTef.iterator();
            while (vedetteRameauTitreIterator.hasNext()) {
                VedetteRameauTitre vedette = vedetteRameauTitreIterator.next();
                if (vedette.getElementdEntree() != null)
                    libelles.add(vedette.getElementdEntree().getContent());
            }
        } catch (NullPointerException e) {
            log.error("PB pour sujetsRameau de " + nnt + ", " + e.getMessage());
        }
    }


}

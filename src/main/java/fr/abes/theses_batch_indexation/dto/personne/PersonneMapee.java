package fr.abes.theses_batch_indexation.dto.personne;

import fr.abes.theses_batch_indexation.model.jaxb.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Getter
public class PersonneMapee {

    //Todo: Ajouter les champs à indexer dans personneModel ou thesesDOT
    private List<PersonneModelES> personnes = new ArrayList<>();

    private String nnt;

    /*Todo: à partir de Mets extraire toutes les infos à indexer
       (s'inpirer de TheseMapee vu que la plupart des champs sont déjà extrait)*/
    public PersonneMapee(Mets mets) {

        DmdSec dmdSec = mets.getDmdSec().get(1);
        AmdSec amdSec = mets.getAmdSec();

        TechMD techMD = null;
        // nnt

        try {

            techMD = amdSec.getTechMD().stream().filter(d -> d.getMdWrap().getXmlData().getThesisAdmin() != null).findFirst().orElse(null);

            Iterator<Identifier> iteIdentifiers = techMD.getMdWrap().getXmlData().getThesisAdmin().getIdentifier().iterator();
            while (iteIdentifiers.hasNext()) {
                Identifier i = iteIdentifiers.next();
                if (isNnt(i.getValue()))
                    nnt = i.getValue();
            }
        } catch (NullPointerException e) {
            log.error("PB pour nnt " + e.toString());
        }

        log.info("traitement de auteurs");
        try {
            List<PersonneModelES> auteurs = new ArrayList<>();

            List<Auteur> auteursDepuisTef = techMD.getMdWrap().getXmlData().getThesisAdmin()
                    .getAuteur();
            Iterator<Auteur> auteurIterator = auteursDepuisTef.iterator();
            while (auteurIterator.hasNext()) {
                Auteur a = auteurIterator.next();
                PersonneModelES adto = new PersonneModelES();
                if (a.getAutoriteExterne() != null)
                    adto.setPpn(a.getAutoriteExterne().getValue());
                adto.setNom(a.getNom());
                adto.setPrenom(a.getPrenom());
                TheseModelES thedto = new TheseModelES();
                thedto.setNnt(nnt);
                thedto.setRole("auteur");
                thedto.setTitre("titre de test");
                adto.getTheses().add(thedto);
                auteurs.add(adto);
            }

            personnes.addAll(auteurs);

        } catch (NullPointerException e) {
            log.error("PB pour auteurs de " + nnt + "," + e.getMessage());
        }
    }

    private boolean isNnt(String identifier) {
        if (identifier.length() == 12)
            return true;
        return false;
    }
}

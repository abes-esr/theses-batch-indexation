package fr.abes.theses_batch_indexation.utils;

import fr.abes.theses_batch_indexation.dto.personne.SujetRameauES;
import fr.abes.theses_batch_indexation.dto.personne.TheseModelES;
import fr.abes.theses_batch_indexation.dto.these.SujetRameauDTO;
import fr.abes.theses_batch_indexation.model.tef.DmdSec;
import fr.abes.theses_batch_indexation.model.tef.Subdivision;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

public class VedetteProcessor {
    // Utilisé par TheseMappee
    List<String> sujetsRameauPpn;
    List<String> sujetsRameauLibelle;
    List<SujetRameauDTO> sujetsRameau;
    // Utilisé par PersonneMappee
    TheseModelES theseModelES;
    // Utilisé dans les deux
    DmdSec dmdSec;


    // Constructeur Personnes
    public VedetteProcessor(DmdSec newDmdSec, TheseModelES newTheseModelES) {
        this.dmdSec = newDmdSec;
        this.theseModelES = newTheseModelES;
    }

    // Constructeur Theses
    public VedetteProcessor(DmdSec newDmdSec, List<String> newSujetsRameauPpn, List<String> newSujetsRameauLibelle, List<SujetRameauDTO> newSujetsRameau) {
        this.dmdSec = newDmdSec;
        this.sujetsRameauPpn = newSujetsRameauPpn;
        this.sujetsRameauLibelle = newSujetsRameauLibelle;
        this.sujetsRameau = newSujetsRameau;
    }

    /**
     * Sélectionne toutes les méthodes getVedetteRameau générées par jaxb
     * getVedetteRameauNomCommun, getVedetteRameauAuteurTitre, getVedetteRameauCollectivite, getVedetteRameauFamille
     * getVedetteRameauNomGeographique, getVedetteRameauPersonne, getVedetteRameauTitre
     */
    public void processAllVedettes() {
        try {
            Method[] getMethods = this.dmdSec.getMdWrap().getXmlData().getThesisRecord().getSujetRameau().getClass().getMethods();
            for (Method method : getMethods) {
                if (method.getName().startsWith("getVedetteRameau")) {
                    List<?> vedetteList = (List<?>) method.invoke(this.dmdSec.getMdWrap().getXmlData().getThesisRecord().getSujetRameau());
                    processVedetteList(vedetteList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param vedetteList Le sujet rameau
     */
    public void processVedetteList(List<?> vedetteList) {
        if (vedetteList == null) {
            return;
        }

        Iterator<?> iterator = vedetteList.iterator();
        while (iterator.hasNext()) {
            Object vedette = iterator.next();
            processVedette(vedette);
        }
    }

    /**
     * Récupère les données des balises Rameau
     * Vérifie si un sujet rameau a des subdivisions et les ajoute à theseModelES.sujetsRameau (à plat)
     *
     * @param vedette Le sujet rameau
     */
    public void processVedette(Object vedette) {
        try {
            Method getElementdEntreeMethod = vedette.getClass().getMethod("getElementdEntree");
            Object elementdEntree = getElementdEntreeMethod.invoke(vedette);

            if (elementdEntree != null) {
                Method getAutoriteExterneMethod = elementdEntree.getClass().getMethod("getAutoriteExterne");
                Method getContentMethod = elementdEntree.getClass().getMethod("getContent");

                String ppn = (String) getAutoriteExterneMethod.invoke(elementdEntree);
                String libelle = (String) getContentMethod.invoke(elementdEntree);

                if (this.theseModelES != null) {
                    // Personnes
                    this.theseModelES.getSujets_rameau().add(new SujetRameauES(ppn, libelle));
                } else if(this.sujetsRameauPpn != null && !this.sujetsRameauPpn.contains(elementdEntree)) {
                    // Theses
                    addSujetToLists(ppn, libelle);
                }
            }

            // Vérifie si la balise vedette contient des subdivisions
            Method processSubdivisionsMethod = this.getClass().getDeclaredMethod("processVedetteSubdivisions", Object.class);
            processSubdivisionsMethod.invoke(this, vedette);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Ajouter un sujet Rameau aux listes de TheseMappee
     *
     * @param ppn
     * @param libelle
     */
    private void addSujetToLists(String ppn, String libelle) {
        SujetRameauDTO sujetRameauDTO = new SujetRameauDTO();
        sujetRameauDTO.setPpn(ppn);
        sujetRameauDTO.setLibelle(libelle);

        sujetsRameau.add(sujetRameauDTO);
        sujetsRameauPpn.add(ppn);
        sujetsRameauLibelle.add(libelle);
    }

    /**
     * Vérifie si un sujet rameau a des subdivisions et les ajoute aux listes le cas échéant
     *
     * @param vedette Le sujet rameau
     */
    public void processVedetteSubdivisions(Object vedette) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Réflexion, car le modèle est auto-généré par jaxb et n'est donc pas modifiable : pas d'interface possible
        Method getSubdivisionMethod = vedette.getClass().getMethod("getSubdivision");
        List<?> subdivisions = (List<?>) getSubdivisionMethod.invoke(vedette);

        Iterator<?> subdivisionIterator = subdivisions.iterator();

        while (subdivisionIterator.hasNext()) {
            Subdivision subdivision = (Subdivision) subdivisionIterator.next();

            Method getAutoriteExterneMethod = subdivision.getClass().getMethod("getAutoriteExterne");
            Method getContentMethod = subdivision.getClass().getMethod("getContent");

            String ppn = (String) getAutoriteExterneMethod.invoke(subdivision);
            String libelle = (String) getContentMethod.invoke(subdivision);

            if (this.theseModelES != null && !this.theseModelES.getSujets_rameau().contains(ppn)) {
                // Personnes
                this.theseModelES.getSujets_rameau().add(new SujetRameauES(ppn, libelle));
            } else if (this.sujetsRameauPpn != null && !this.sujetsRameauPpn.contains(subdivision)){
                // Theses
                addSujetToLists(ppn, libelle);
            }
        }
    }

}

package fr.abes.theses_batch_indexation.utils;

import fr.abes.theses_batch_indexation.model.tef.Auteur;
import fr.abes.theses_batch_indexation.model.tef.AutoriteExterne;
import fr.abes.theses_batch_indexation.model.tef.CoAuteur;

import java.util.List;

public class OutilsTef {

    public static boolean ppnEstPresent(List<AutoriteExterne> autoriteExternes) {
        for (AutoriteExterne autoriteExterne:
             autoriteExternes) {
            if (autoriteExterne.getAutoriteSource().equals("Sudoc"))
            {
                return true;
            }
        }
        return false;
    }

    public static String getPPN(List<AutoriteExterne> autoriteExternes) {
        if (autoriteExternes != null && ppnEstPresent(autoriteExternes)) {
            for (AutoriteExterne autoriteExterne:
                    autoriteExternes) {
                if (autoriteExterne.getAutoriteSource().equals("Sudoc"))
                {
                    return autoriteExterne.getValue();
                }
            }
        }
        return null;
    }


    public static Auteur coAuteurToAuteur(CoAuteur coAuteur){
        Auteur res = null;

        if(coAuteur != null){
            res = new Auteur();

            res.setPrenom(coAuteur.getPrenom());
            res.setNom(coAuteur.getNom());
            res.setDateNaissance(coAuteur.getDateNaissance());
            res.setNationalite(coAuteur.getNationalite());
            res.getAutoriteExterne().addAll(coAuteur.getAutoriteExterne());
        }

        return res;
    }

}

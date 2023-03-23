package fr.abes.theses_batch_indexation.utils;

import fr.abes.theses_batch_indexation.model.tef.AutoriteExterne;

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

}

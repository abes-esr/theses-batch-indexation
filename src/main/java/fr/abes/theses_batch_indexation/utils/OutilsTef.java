package fr.abes.theses_batch_indexation.utils;

import fr.abes.theses_batch_indexation.model.tef.AutoriteExterne;

public class OutilsTef {

    public static boolean isPPN(AutoriteExterne autoriteExterne) {
        return autoriteExterne.getAutoriteSource().equals("Sudoc");
    }

    public static String getPPN(AutoriteExterne autoriteExterne) {
        if (autoriteExterne != null && isPPN(autoriteExterne)) {
            return autoriteExterne.getValue();
        } else {
            return null;
        }
    }

}

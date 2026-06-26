package fr.abes.theses_batch_indexation.dto.personne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Représente une personne au format de l'index Elastic Search.
 * Cet objet sera transformé en JSON au moment de l'appel API pour l'indexation.
 * Le nom des attributs doivent correspondre exactement à ceux de l'index {@link /resources/indexs/personnes.json}
 */
@Getter
@Setter
@NoArgsConstructor
public class PersonneModelES implements IModelES {

    protected String ppn;
    protected boolean has_idref;
    protected String nom;
    protected String prenom;
    protected String date_naissance;
    protected String idxDoublon;
    protected List<String> nom_complet = new ArrayList<>();
    protected Set<String> theses_id = new LinkedHashSet<>();

    protected List<String> roles = new ArrayList<>();
    protected List<TheseModelES> theses = new ArrayList<>();

    public PersonneModelES(String ppn, String nom, String prenom) {
        if (ppn != null && !ppn.equals("")) {
            this.ppn = ppn;
            this.has_idref = true;
        } else {
            this.has_idref = false;
        }
        this.nom = nom;
        this.prenom = prenom;

        nom_complet.add(String.format("%1$s %2$s", prenom, nom));
        nom_complet.add(String.format("%1$s %2$s", nom, prenom));
    }


    //set la date de naissance pour les auteurs, et instancie le champ "idxDoublon"
    public void setDate_Naissance(XMLGregorianCalendar dateNaissance) {
            String date =String.format("%04d%02d%02d",dateNaissance.getYear(),dateNaissance.getMonth(),dateNaissance.getDay());
            this.idxDoublon = String.join("|", this.nom, this.prenom, date);
            this.date_naissance = dateNaissance.toString();
    }


    /**
     * Recherche une thèse dans la liste des thèses
     *
     * @param nnt Identifiant de la thèse
     * @return TheseModelES La thèse ou null
     */
    public TheseModelES findThese(String nnt) {
        return theses.stream()
                .filter(item -> (item.getId() != null && item.getId().equals(nnt)))
                .findAny()
                .orElse(null);
    }
}

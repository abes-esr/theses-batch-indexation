package fr.abes.theses_batch_indexation.database;

import fr.abes.theses_batch_indexation.dto.personne.PersonneModelES;

import java.util.List;

/** Objet "valise" qui modelise une entrée dans la base de données
 * jsonThese : vide au départ ; après le processor : json à indexer dans ES
 * personnes : Liste de personnes à indexer dans ES
 */
public class TheseModel {

    private int idDoc;
    private String nnt;
    private String doc;
    private String jsonThese;
    private List<PersonneModelES> personnes;
    private String jsonThematiques;
    private String idSujet;


    public List<PersonneModelES> getPersonnes() {
        return personnes;
    }

    public void setPersonnes(List<PersonneModelES> personnes) {
        this.personnes = personnes;
    }

    public int getIdDoc() {
        return idDoc;
    }

    public void setIdDoc(int idDoc) {
        this.idDoc = idDoc;
    }

    public String getNnt() {
        return nnt;
    }

    public void setNnt(String nnt) {
        this.nnt = nnt;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public String getJsonThese() {
        return jsonThese;
    }

    public void setJsonThese(String jsonThese) {
        this.jsonThese = jsonThese;
    }

    public String getJsonThematiques() {
        return jsonThematiques;
    }
    public void setJsonThematiques(String thematiques) {
        this.jsonThematiques = thematiques;
    }

    public String getIdSujet() {
        return idSujet;
    }

    public void setIdSujet(String idSujet) {
        this.idSujet = idSujet;
    }

}

package fr.abes.theses_batch_indexation.database;

import fr.abes.theses_batch_indexation.dto.personne.PersonneModelES;
import fr.abes.theses_batch_indexation.dto.personne.RecherchePersonneModelES;
import lombok.Getter;
import lombok.Setter;
import oracle.xdb.XMLType;

import java.sql.SQLException;
import java.util.List;

/** Objet "valise" qui modelise une entrée dans la base de données
 * jsonThese : vide au départ ; après le processor : json à indexer dans ES
 * personnes : Liste de personnes à indexer dans ES
 */
@Getter
@Setter
public class TheseModel {
    private int idDoc;
    private String nnt;
    private XMLType doc;
    private String jsonThese;
    private List<PersonneModelES> personnes;

    private List<RecherchePersonneModelES> recherchePersonnes;
    private String jsonThematiques;
    private String idSujet;
    private String codeEtab;

    /**
     * Retourne l'identifiant de la thèse
     * Numéro NNT pour une thèse soutenue
     * Numéro idSujet pour une thèse en préparation (source : idStep)
     * @return Identifiant de la thèse
     */
    public String getId() {
        if(this.getNnt() != null) {
            return this.getNnt();
        } else {
            return this.getIdSujet();
        }
    }

    public String getDoc() throws SQLException {
        return doc.getStringVal();
    }

    public XMLType getDocXmlType() {
        return doc;
    }

}

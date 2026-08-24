//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.8-b130911.1802 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2023.03.09 à 03:40:55 PM CET 
//


package fr.abes.theses_batch_indexation.model.oaisets;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour anonymous complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}ListSets"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "listSets"
})
@XmlRootElement(name = "OAI-PMH")
public class OAIPMH {

    @XmlElement(name = "ListSets", required = true)
    protected ListSets listSets;

    /**
     * Obtient la valeur de la propriété listSets.
     * 
     * @return
     *     possible object is
     *     {@link ListSets }
     *     
     */
    public ListSets getListSets() {
        return listSets;
    }

    /**
     * Définit la valeur de la propriété listSets.
     * 
     * @param value
     *     allowed object is
     *     {@link ListSets }
     *     
     */
    public void setListSets(ListSets value) {
        this.listSets = value;
    }

}

//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.8-b130911.1802 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2024.05.14 à 04:50:23 PM CEST 
//


package fr.abes.theses_batch_indexation.model.tef;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element ref="{http://www.abes.fr/abes/documents/tef}elementdEntree"/>
 *         &lt;element ref="{http://www.abes.fr/abes/documents/tef}subdivision" maxOccurs="unbounded"/>
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
    "elementdEntree",
    "subdivision"
})
@XmlRootElement(name = "vedetteRameauNomGeographique")
public class VedetteRameauNomGeographique {

    @XmlElement(required = true)
    protected ElementdEntree elementdEntree;
    @XmlElement(required = true)
    protected List<Subdivision> subdivision;

    /**
     * Obtient la valeur de la propriété elementdEntree.
     * 
     * @return
     *     possible object is
     *     {@link ElementdEntree }
     *     
     */
    public ElementdEntree getElementdEntree() {
        return elementdEntree;
    }

    /**
     * Définit la valeur de la propriété elementdEntree.
     * 
     * @param value
     *     allowed object is
     *     {@link ElementdEntree }
     *     
     */
    public void setElementdEntree(ElementdEntree value) {
        this.elementdEntree = value;
    }

    /**
     * Gets the value of the subdivision property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the subdivision property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubdivision().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Subdivision }
     * 
     * 
     */
    public List<Subdivision> getSubdivision() {
        if (subdivision == null) {
            subdivision = new ArrayList<Subdivision>();
        }
        return this.subdivision;
    }

}

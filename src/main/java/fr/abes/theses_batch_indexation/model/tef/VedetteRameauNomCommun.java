//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.8-b130911.1802 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2023.04.27 à 04:36:39 PM CEST 
//


package fr.abes.theses_batch_indexation.model.tef;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element ref="{http://www.abes.fr/abes/documents/tef}subdivision" minOccurs="0"/>
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
@XmlRootElement(name = "vedetteRameauNomCommun")
public class VedetteRameauNomCommun {

    @XmlElement(required = true)
    protected ElementdEntree elementdEntree;
    protected Subdivision subdivision;

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
     * Obtient la valeur de la propriété subdivision.
     * 
     * @return
     *     possible object is
     *     {@link Subdivision }
     *     
     */
    public Subdivision getSubdivision() {
        return subdivision;
    }

    /**
     * Définit la valeur de la propriété subdivision.
     * 
     * @param value
     *     allowed object is
     *     {@link Subdivision }
     *     
     */
    public void setSubdivision(Subdivision value) {
        this.subdivision = value;
    }

}

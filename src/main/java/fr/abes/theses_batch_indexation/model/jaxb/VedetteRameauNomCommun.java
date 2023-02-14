//
// Ce fichier a �t� g�n�r� par l'impl�mentation de r�f�rence JavaTM Architecture for XML Binding (JAXB), v2.2.8-b130911.1802 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apport�e � ce fichier sera perdue lors de la recompilation du sch�ma source. 
// G�n�r� le : 2022.11.17 � 02:52:49 PM CET 
//


package fr.abes.theses_batch_indexation.model.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour anonymous complex type.
 * 
 * <p>Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette classe.
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
     * Obtient la valeur de la propri�t� elementdEntree.
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
     * D�finit la valeur de la propri�t� elementdEntree.
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
     * Obtient la valeur de la propri�t� subdivision.
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
     * D�finit la valeur de la propri�t� subdivision.
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

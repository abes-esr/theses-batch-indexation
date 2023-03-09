//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.8-b130911.1802 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2023.03.08 à 02:24:30 PM CET 
//


package fr.abes.theses_batch_indexation.model.oaisets;

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
 *         &lt;element ref="{http://www.openarchives.org/OAI/2.0/oai_dc/}dc"/>
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
    "dc"
})
@XmlRootElement(name = "setDescription")
public class SetDescription {

    @XmlElement(namespace = "http://www.openarchives.org/OAI/2.0/oai_dc/", required = true)
    protected Dc dc;

    /**
     * Obtient la valeur de la propriété dc.
     * 
     * @return
     *     possible object is
     *     {@link Dc }
     *     
     */
    public Dc getDc() {
        return dc;
    }

    /**
     * Définit la valeur de la propriété dc.
     * 
     * @param value
     *     allowed object is
     *     {@link Dc }
     *     
     */
    public void setDc(Dc value) {
        this.dc = value;
    }

}

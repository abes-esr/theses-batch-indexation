//
// Ce fichier a �t� g�n�r� par l'impl�mentation de r�f�rence JavaTM Architecture for XML Binding (JAXB), v2.2.8-b130911.1802 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apport�e � ce fichier sera perdue lors de la recompilation du sch�ma source. 
// G�n�r� le : 2023.03.08 � 02:24:30 PM CET 
//


package fr.abes.theses_batch_indexation.model.oaisets;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


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
 *         &lt;element ref="{}setSpec"/>
 *         &lt;element ref="{}setName"/>
 *         &lt;element ref="{}setDescription" minOccurs="0"/>
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
    "setSpec",
    "setName",
    "setDescription"
})
@XmlRootElement(name = "set")
public class Set {

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String setSpec;
    @XmlElement(required = true)
    protected String setName;
    protected SetDescription setDescription;

    /**
     * Obtient la valeur de la propri�t� setSpec.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSetSpec() {
        return setSpec;
    }

    /**
     * D�finit la valeur de la propri�t� setSpec.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSetSpec(String value) {
        this.setSpec = value;
    }

    /**
     * Obtient la valeur de la propri�t� setName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSetName() {
        return setName;
    }

    /**
     * D�finit la valeur de la propri�t� setName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSetName(String value) {
        this.setName = value;
    }

    /**
     * Obtient la valeur de la propri�t� setDescription.
     * 
     * @return
     *     possible object is
     *     {@link SetDescription }
     *     
     */
    public SetDescription getSetDescription() {
        return setDescription;
    }

    /**
     * D�finit la valeur de la propri�t� setDescription.
     * 
     * @param value
     *     allowed object is
     *     {@link SetDescription }
     *     
     */
    public void setSetDescription(SetDescription value) {
        this.setDescription = value;
    }

}

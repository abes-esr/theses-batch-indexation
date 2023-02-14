//
// Ce fichier a �t� g�n�r� par l'impl�mentation de r�f�rence JavaTM Architecture for XML Binding (JAXB), v2.2.8-b130911.1802 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apport�e � ce fichier sera perdue lors de la recompilation du sch�ma source. 
// G�n�r� le : 2022.11.17 � 02:52:49 PM CET 
//


package fr.abes.theses_batch_indexation.model.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *         &lt;element ref="{http://www.loc.gov/METS/}div" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="ADMID" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="CONTENTIDS" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="DMDID" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="TYPE" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "div"
})
@XmlRootElement(name = "div", namespace = "http://www.loc.gov/METS/")
public class Div {

    @XmlElement(namespace = "http://www.loc.gov/METS/")
    protected Div div;
    @XmlAttribute(name = "ADMID")
    @XmlSchemaType(name = "anySimpleType")
    protected String admid;
    @XmlAttribute(name = "CONTENTIDS", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String contentids;
    @XmlAttribute(name = "DMDID", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String dmdid;
    @XmlAttribute(name = "TYPE", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String type;

    /**
     * Obtient la valeur de la propri�t� div.
     * 
     * @return
     *     possible object is
     *     {@link Div }
     *     
     */
    public Div getDiv() {
        return div;
    }

    /**
     * D�finit la valeur de la propri�t� div.
     * 
     * @param value
     *     allowed object is
     *     {@link Div }
     *     
     */
    public void setDiv(Div value) {
        this.div = value;
    }

    /**
     * Obtient la valeur de la propri�t� admid.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getADMID() {
        return admid;
    }

    /**
     * D�finit la valeur de la propri�t� admid.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setADMID(String value) {
        this.admid = value;
    }

    /**
     * Obtient la valeur de la propri�t� contentids.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCONTENTIDS() {
        return contentids;
    }

    /**
     * D�finit la valeur de la propri�t� contentids.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCONTENTIDS(String value) {
        this.contentids = value;
    }

    /**
     * Obtient la valeur de la propri�t� dmdid.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDMDID() {
        return dmdid;
    }

    /**
     * D�finit la valeur de la propri�t� dmdid.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDMDID(String value) {
        this.dmdid = value;
    }

    /**
     * Obtient la valeur de la propri�t� type.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTYPE() {
        return type;
    }

    /**
     * D�finit la valeur de la propri�t� type.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTYPE(String value) {
        this.type = value;
    }

}

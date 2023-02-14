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
 *         &lt;element ref="{http://www.loc.gov/METS/}xmlData"/>
 *       &lt;/sequence>
 *       &lt;attribute name="MDTYPE" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="OTHERMDTYPE" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "xmlData"
})
@XmlRootElement(name = "mdWrap", namespace = "http://www.loc.gov/METS/")
public class MdWrap {

    @XmlElement(namespace = "http://www.loc.gov/METS/", required = true)
    protected XmlData xmlData;
    @XmlAttribute(name = "MDTYPE", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String mdtype;
    @XmlAttribute(name = "OTHERMDTYPE", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String othermdtype;

    /**
     * Obtient la valeur de la propri�t� xmlData.
     * 
     * @return
     *     possible object is
     *     {@link XmlData }
     *     
     */
    public XmlData getXmlData() {
        return xmlData;
    }

    /**
     * D�finit la valeur de la propri�t� xmlData.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlData }
     *     
     */
    public void setXmlData(XmlData value) {
        this.xmlData = value;
    }

    /**
     * Obtient la valeur de la propri�t� mdtype.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMDTYPE() {
        return mdtype;
    }

    /**
     * D�finit la valeur de la propri�t� mdtype.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMDTYPE(String value) {
        this.mdtype = value;
    }

    /**
     * Obtient la valeur de la propri�t� othermdtype.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOTHERMDTYPE() {
        return othermdtype;
    }

    /**
     * D�finit la valeur de la propri�t� othermdtype.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOTHERMDTYPE(String value) {
        this.othermdtype = value;
    }

}

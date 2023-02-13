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
 *         &lt;element ref="{http://www.theses.fr/namespace/tefudoc}rcr"/>
 *         &lt;element ref="{http://www.theses.fr/namespace/tefudoc}cote"/>
 *         &lt;element ref="{http://www.theses.fr/namespace/tefudoc}peb"/>
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
    "rcr",
    "cote",
    "peb"
})
@XmlRootElement(name = "exemplaire", namespace = "http://www.theses.fr/namespace/tefudoc")
public class Exemplaire {

    @XmlElement(namespace = "http://www.theses.fr/namespace/tefudoc", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String rcr;
    @XmlElement(namespace = "http://www.theses.fr/namespace/tefudoc", required = true)
    protected Cote cote;
    @XmlElement(namespace = "http://www.theses.fr/namespace/tefudoc", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String peb;

    /**
     * Obtient la valeur de la propri�t� rcr.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRcr() {
        return rcr;
    }

    /**
     * D�finit la valeur de la propri�t� rcr.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRcr(String value) {
        this.rcr = value;
    }

    /**
     * Obtient la valeur de la propri�t� cote.
     * 
     * @return
     *     possible object is
     *     {@link Cote }
     *     
     */
    public Cote getCote() {
        return cote;
    }

    /**
     * D�finit la valeur de la propri�t� cote.
     * 
     * @param value
     *     allowed object is
     *     {@link Cote }
     *     
     */
    public void setCote(Cote value) {
        this.cote = value;
    }

    /**
     * Obtient la valeur de la propri�t� peb.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPeb() {
        return peb;
    }

    /**
     * D�finit la valeur de la propri�t� peb.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPeb(String value) {
        this.peb = value;
    }

}

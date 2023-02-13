//
// Ce fichier a �t� g�n�r� par l'impl�mentation de r�f�rence JavaTM Architecture for XML Binding (JAXB), v2.2.8-b130911.1802 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apport�e � ce fichier sera perdue lors de la recompilation du sch�ma source. 
// G�n�r� le : 2022.11.17 � 02:52:49 PM CET 
//


package fr.abes.theses_batch_indexation.model.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element ref="{http://www.loc.gov/METS/}agent" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.loc.gov/METS/}altRecordID"/>
 *       &lt;/sequence>
 *       &lt;attribute name="CREATEDATE" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="ID" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="LASTMODDATE" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="RECORDSTATUS" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "agent",
    "altRecordID"
})
@XmlRootElement(name = "metsHdr", namespace = "http://www.loc.gov/METS/")
public class MetsHdr {

    @XmlElement(namespace = "http://www.loc.gov/METS/", required = true)
    protected List<Agent> agent;
    @XmlElement(namespace = "http://www.loc.gov/METS/", required = true)
    protected AltRecordID altRecordID;
    @XmlAttribute(name = "CREATEDATE", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar createdate;
    @XmlAttribute(name = "ID", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String id;
    @XmlAttribute(name = "LASTMODDATE", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastmoddate;
    @XmlAttribute(name = "RECORDSTATUS", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String recordstatus;

    /**
     * Gets the value of the agent property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the agent property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAgent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Agent }
     * 
     * 
     */
    public List<Agent> getAgent() {
        if (agent == null) {
            agent = new ArrayList<Agent>();
        }
        return this.agent;
    }

    /**
     * Obtient la valeur de la propri�t� altRecordID.
     * 
     * @return
     *     possible object is
     *     {@link AltRecordID }
     *     
     */
    public AltRecordID getAltRecordID() {
        return altRecordID;
    }

    /**
     * D�finit la valeur de la propri�t� altRecordID.
     * 
     * @param value
     *     allowed object is
     *     {@link AltRecordID }
     *     
     */
    public void setAltRecordID(AltRecordID value) {
        this.altRecordID = value;
    }

    /**
     * Obtient la valeur de la propri�t� createdate.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCREATEDATE() {
        return createdate;
    }

    /**
     * D�finit la valeur de la propri�t� createdate.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCREATEDATE(XMLGregorianCalendar value) {
        this.createdate = value;
    }

    /**
     * Obtient la valeur de la propri�t� id.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getID() {
        return id;
    }

    /**
     * D�finit la valeur de la propri�t� id.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setID(String value) {
        this.id = value;
    }

    /**
     * Obtient la valeur de la propri�t� lastmoddate.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLASTMODDATE() {
        return lastmoddate;
    }

    /**
     * D�finit la valeur de la propri�t� lastmoddate.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLASTMODDATE(XMLGregorianCalendar value) {
        this.lastmoddate = value;
    }

    /**
     * Obtient la valeur de la propri�t� recordstatus.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRECORDSTATUS() {
        return recordstatus;
    }

    /**
     * D�finit la valeur de la propri�t� recordstatus.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRECORDSTATUS(String value) {
        this.recordstatus = value;
    }

}

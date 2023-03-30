//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.8-b130911.1802 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2023.03.30 à 12:02:06 PM CEST 
//


package fr.abes.theses_batch_indexation.model.tef;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.abes.fr/abes/documents/tef}thesis.degree.discipline"/>
 *         &lt;element ref="{http://www.abes.fr/abes/documents/tef}thesis.degree.grantor" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.abes.fr/abes/documents/tef}thesis.degree.level"/>
 *         &lt;element ref="{http://www.abes.fr/abes/documents/tef}thesis.degree.name"/>
 *         &lt;element ref="{http://www.theses.fr/namespace/sujets}datePremiereInscriptionDoctorat"/>
 *         &lt;element ref="{http://www.theses.fr/namespace/sujets}dateInscriptionEtab"/>
 *         &lt;element ref="{http://www.theses.fr/namespace/sujets}contratDoctoral"/>
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
    "thesisDegreeDiscipline",
    "thesisDegreeGrantor",
    "thesisDegreeLevel",
    "thesisDegreeName",
    "datePremiereInscriptionDoctorat",
    "dateInscriptionEtab",
    "contratDoctoral"
})
@XmlRootElement(name = "thesis.degree")
public class ThesisDegree {

    @XmlElement(name = "thesis.degree.discipline", required = true)
    protected ThesisDegreeDiscipline thesisDegreeDiscipline;
    @XmlElement(name = "thesis.degree.grantor", required = true)
    protected List<ThesisDegreeGrantor> thesisDegreeGrantor;
    @XmlElement(name = "thesis.degree.level", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String thesisDegreeLevel;
    @XmlElement(name = "thesis.degree.name", required = true)
    protected ThesisDegreeName thesisDegreeName;
    @XmlElement(namespace = "http://www.theses.fr/namespace/sujets", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar datePremiereInscriptionDoctorat;
    @XmlElement(namespace = "http://www.theses.fr/namespace/sujets", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dateInscriptionEtab;
    @XmlElement(namespace = "http://www.theses.fr/namespace/sujets", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String contratDoctoral;

    /**
     * Obtient la valeur de la propriété thesisDegreeDiscipline.
     * 
     * @return
     *     possible object is
     *     {@link ThesisDegreeDiscipline }
     *     
     */
    public ThesisDegreeDiscipline getThesisDegreeDiscipline() {
        return thesisDegreeDiscipline;
    }

    /**
     * Définit la valeur de la propriété thesisDegreeDiscipline.
     * 
     * @param value
     *     allowed object is
     *     {@link ThesisDegreeDiscipline }
     *     
     */
    public void setThesisDegreeDiscipline(ThesisDegreeDiscipline value) {
        this.thesisDegreeDiscipline = value;
    }

    /**
     * Gets the value of the thesisDegreeGrantor property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the thesisDegreeGrantor property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getThesisDegreeGrantor().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ThesisDegreeGrantor }
     * 
     * 
     */
    public List<ThesisDegreeGrantor> getThesisDegreeGrantor() {
        if (thesisDegreeGrantor == null) {
            thesisDegreeGrantor = new ArrayList<ThesisDegreeGrantor>();
        }
        return this.thesisDegreeGrantor;
    }

    /**
     * Obtient la valeur de la propriété thesisDegreeLevel.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getThesisDegreeLevel() {
        return thesisDegreeLevel;
    }

    /**
     * Définit la valeur de la propriété thesisDegreeLevel.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setThesisDegreeLevel(String value) {
        this.thesisDegreeLevel = value;
    }

    /**
     * Obtient la valeur de la propriété thesisDegreeName.
     * 
     * @return
     *     possible object is
     *     {@link ThesisDegreeName }
     *     
     */
    public ThesisDegreeName getThesisDegreeName() {
        return thesisDegreeName;
    }

    /**
     * Définit la valeur de la propriété thesisDegreeName.
     * 
     * @param value
     *     allowed object is
     *     {@link ThesisDegreeName }
     *     
     */
    public void setThesisDegreeName(ThesisDegreeName value) {
        this.thesisDegreeName = value;
    }

    /**
     * Obtient la valeur de la propriété datePremiereInscriptionDoctorat.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDatePremiereInscriptionDoctorat() {
        return datePremiereInscriptionDoctorat;
    }

    /**
     * Définit la valeur de la propriété datePremiereInscriptionDoctorat.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDatePremiereInscriptionDoctorat(XMLGregorianCalendar value) {
        this.datePremiereInscriptionDoctorat = value;
    }

    /**
     * Obtient la valeur de la propriété dateInscriptionEtab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateInscriptionEtab() {
        return dateInscriptionEtab;
    }

    /**
     * Définit la valeur de la propriété dateInscriptionEtab.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateInscriptionEtab(XMLGregorianCalendar value) {
        this.dateInscriptionEtab = value;
    }

    /**
     * Obtient la valeur de la propriété contratDoctoral.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContratDoctoral() {
        return contratDoctoral;
    }

    /**
     * Définit la valeur de la propriété contratDoctoral.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContratDoctoral(String value) {
        this.contratDoctoral = value;
    }

}

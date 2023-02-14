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
 *         &lt;element ref="{http://www.abes.fr/abes/documents/tef}thesis.degree.discipline"/>
 *         &lt;element ref="{http://www.abes.fr/abes/documents/tef}thesis.degree.grantor" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.abes.fr/abes/documents/tef}thesis.degree.level"/>
 *         &lt;element ref="{http://www.abes.fr/abes/documents/tef}thesis.degree.name"/>
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
    "thesisDegreeName"
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

    /**
     * Obtient la valeur de la propri�t� thesisDegreeDiscipline.
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
     * D�finit la valeur de la propri�t� thesisDegreeDiscipline.
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
     * Obtient la valeur de la propri�t� thesisDegreeLevel.
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
     * D�finit la valeur de la propri�t� thesisDegreeLevel.
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
     * Obtient la valeur de la propri�t� thesisDegreeName.
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
     * D�finit la valeur de la propri�t� thesisDegreeName.
     * 
     * @param value
     *     allowed object is
     *     {@link ThesisDegreeName }
     *     
     */
    public void setThesisDegreeName(ThesisDegreeName value) {
        this.thesisDegreeName = value;
    }

}

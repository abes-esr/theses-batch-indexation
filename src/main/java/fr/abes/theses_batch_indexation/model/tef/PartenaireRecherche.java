//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.8-b130911.1802 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2025.06.20 à 02:47:21 PM CEST 
//


package fr.abes.theses_batch_indexation.model.tef;

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
 *         &lt;element ref="{http://www.abes.fr/abes/documents/tef}nom"/>
 *         &lt;element ref="{http://www.abes.fr/abes/documents/tef}autoriteInterne"/>
 *         &lt;element ref="{http://www.abes.fr/abes/documents/tef}autoriteExterne" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="autreType" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "nom",
    "autoriteInterne",
    "autoriteExterne"
})
@XmlRootElement(name = "partenaireRecherche")
public class PartenaireRecherche {

    @XmlElement(required = true)
    protected String nom;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String autoriteInterne;
    @XmlElement(required = true)
    protected List<AutoriteExterne> autoriteExterne;
    @XmlAttribute(name = "autreType")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String autreType;
    @XmlAttribute(name = "type", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String type;

    /**
     * Obtient la valeur de la propriété nom.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNom() {
        return nom;
    }

    /**
     * Définit la valeur de la propriété nom.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNom(String value) {
        this.nom = value;
    }

    /**
     * Obtient la valeur de la propriété autoriteInterne.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAutoriteInterne() {
        return autoriteInterne;
    }

    /**
     * Définit la valeur de la propriété autoriteInterne.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAutoriteInterne(String value) {
        this.autoriteInterne = value;
    }

    /**
     * Gets the value of the autoriteExterne property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the autoriteExterne property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAutoriteExterne().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AutoriteExterne }
     * 
     * 
     */
    public List<AutoriteExterne> getAutoriteExterne() {
        if (autoriteExterne == null) {
            autoriteExterne = new ArrayList<AutoriteExterne>();
        }
        return this.autoriteExterne;
    }

    /**
     * Obtient la valeur de la propriété autreType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAutreType() {
        return autreType;
    }

    /**
     * Définit la valeur de la propriété autreType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAutreType(String value) {
        this.autreType = value;
    }

    /**
     * Obtient la valeur de la propriété type.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Définit la valeur de la propriété type.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

}

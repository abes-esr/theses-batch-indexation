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
 *         &lt;element ref="{}ListSets"/>
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
    "listSets"
})
@XmlRootElement(name = "OAI-PMH")
public class OAIPMH {

    @XmlElement(name = "ListSets", required = true)
    protected ListSets listSets;

    /**
     * Obtient la valeur de la propri�t� listSets.
     * 
     * @return
     *     possible object is
     *     {@link ListSets }
     *     
     */
    public ListSets getListSets() {
        return listSets;
    }

    /**
     * D�finit la valeur de la propri�t� listSets.
     * 
     * @param value
     *     allowed object is
     *     {@link ListSets }
     *     
     */
    public void setListSets(ListSets value) {
        this.listSets = value;
    }

}

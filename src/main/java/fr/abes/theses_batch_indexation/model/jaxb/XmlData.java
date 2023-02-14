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
import javax.xml.bind.annotation.XmlValue;
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
 *       &lt;choice>
 *         &lt;element ref="{http://www.abes.fr/abes/documents/tef}edition"/>
 *         &lt;element ref="{http://www.abes.fr/abes/documents/tef}thesisAdmin"/>
 *         &lt;element ref="{http://www.abes.fr/abes/documents/tef}thesisRecord"/>
 *         &lt;sequence>
 *           &lt;element name="star_gestion" form="unqualified">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="traitements" form="unqualified">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence>
 *                               &lt;element name="entree" form="unqualified">
 *                                 &lt;complexType>
 *                                   &lt;complexContent>
 *                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                       &lt;attribute name="agentImport" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
 *                                       &lt;attribute name="completudeImport" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                                       &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *                                       &lt;attribute name="idSource" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                                       &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                                     &lt;/restriction>
 *                                   &lt;/complexContent>
 *                                 &lt;/complexType>
 *                               &lt;/element>
 *                               &lt;element name="step" form="unqualified">
 *                                 &lt;complexType>
 *                                   &lt;complexContent>
 *                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                       &lt;attribute name="agentImport" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                                       &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                                       &lt;attribute name="idStep" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                                     &lt;/restriction>
 *                                   &lt;/complexContent>
 *                                 &lt;/complexType>
 *                               &lt;/element>
 *                               &lt;element name="maj" form="unqualified">
 *                                 &lt;complexType>
 *                                   &lt;complexContent>
 *                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                       &lt;attribute name="BIBL" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                                       &lt;attribute name="FICH" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                                       &lt;attribute name="SCOL" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                                       &lt;attribute name="VALID" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                                       &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *                                       &lt;attribute name="import" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                                     &lt;/restriction>
 *                                   &lt;/complexContent>
 *                                 &lt;/complexType>
 *                               &lt;/element>
 *                               &lt;element name="facile" form="unqualified">
 *                                 &lt;complexType>
 *                                   &lt;complexContent>
 *                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                       &lt;attribute name="BIBL" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                                       &lt;attribute name="FICH" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                                       &lt;attribute name="SCOL" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                                       &lt;attribute name="VALID" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                                       &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *                                       &lt;attribute name="import" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                                       &lt;attribute name="indicFacile" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                                       &lt;attribute name="trace" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                                     &lt;/restriction>
 *                                   &lt;/complexContent>
 *                                 &lt;/complexType>
 *                               &lt;/element>
 *                               &lt;element name="ctrlUrl" form="unqualified">
 *                                 &lt;complexType>
 *                                   &lt;complexContent>
 *                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                       &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                                       &lt;attribute name="erreurUrl" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                                       &lt;attribute name="indicCtrlUrl" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                                     &lt;/restriction>
 *                                   &lt;/complexContent>
 *                                 &lt;/complexType>
 *                               &lt;/element>
 *                               &lt;element name="remonteeArchive" form="unqualified">
 *                                 &lt;complexType>
 *                                   &lt;complexContent>
 *                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                       &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                                       &lt;attribute name="indicRemonteeArchive" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                                       &lt;attribute name="trace" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                                     &lt;/restriction>
 *                                   &lt;/complexContent>
 *                                 &lt;/complexType>
 *                               &lt;/element>
 *                               &lt;element name="purge" form="unqualified">
 *                                 &lt;complexType>
 *                                   &lt;complexContent>
 *                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                       &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                                       &lt;attribute name="indicPurge" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                                     &lt;/restriction>
 *                                   &lt;/complexContent>
 *                                 &lt;/complexType>
 *                               &lt;/element>
 *                               &lt;element name="invalidation" form="unqualified">
 *                                 &lt;complexType>
 *                                   &lt;complexContent>
 *                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                       &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                                       &lt;attribute name="indicInvalidation" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                                       &lt;attribute name="note" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                                     &lt;/restriction>
 *                                   &lt;/complexContent>
 *                                 &lt;/complexType>
 *                               &lt;/element>
 *                               &lt;element name="sorties" form="unqualified">
 *                                 &lt;complexType>
 *                                   &lt;complexContent>
 *                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                       &lt;sequence>
 *                                         &lt;element name="cines" form="unqualified">
 *                                           &lt;complexType>
 *                                             &lt;complexContent>
 *                                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                                 &lt;attribute name="dateCines" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *                                                 &lt;attribute name="indicCines" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                                                 &lt;attribute name="trace" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                                               &lt;/restriction>
 *                                             &lt;/complexContent>
 *                                           &lt;/complexType>
 *                                         &lt;/element>
 *                                         &lt;element name="sudoc" form="unqualified">
 *                                           &lt;complexType>
 *                                             &lt;complexContent>
 *                                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                                 &lt;sequence>
 *                                                   &lt;element name="RCR" form="unqualified">
 *                                                     &lt;complexType>
 *                                                       &lt;complexContent>
 *                                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                                           &lt;attribute name="code" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
 *                                                         &lt;/restriction>
 *                                                       &lt;/complexContent>
 *                                                     &lt;/complexType>
 *                                                   &lt;/element>
 *                                                   &lt;element name="EPN" form="unqualified">
 *                                                     &lt;complexType>
 *                                                       &lt;complexContent>
 *                                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                                         &lt;/restriction>
 *                                                       &lt;/complexContent>
 *                                                     &lt;/complexType>
 *                                                   &lt;/element>
 *                                                 &lt;/sequence>
 *                                                 &lt;attribute name="PPN" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
 *                                                 &lt;attribute name="dateSudoc" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *                                                 &lt;attribute name="indicSudoc" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                                                 &lt;attribute name="majSudoc" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *                                                 &lt;attribute name="trace" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                                               &lt;/restriction>
 *                                             &lt;/complexContent>
 *                                           &lt;/complexType>
 *                                         &lt;/element>
 *                                         &lt;element name="diffusion" form="unqualified">
 *                                           &lt;complexType>
 *                                             &lt;complexContent>
 *                                               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                                 &lt;sequence>
 *                                                   &lt;element name="etabDiffuseur" form="unqualified">
 *                                                     &lt;complexType>
 *                                                       &lt;complexContent>
 *                                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                                           &lt;sequence>
 *                                                             &lt;element name="urlEtabDiffuseur" form="unqualified">
 *                                                               &lt;complexType>
 *                                                                 &lt;complexContent>
 *                                                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                                                     &lt;attribute name="majUrlEtabDiffuseur" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                                                                   &lt;/restriction>
 *                                                                 &lt;/complexContent>
 *                                                               &lt;/complexType>
 *                                                             &lt;/element>
 *                                                           &lt;/sequence>
 *                                                           &lt;attribute name="editeurScientifiqueLieu" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                                                           &lt;attribute name="editeurScientifiqueNom" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                                                           &lt;attribute name="etabDiffuseurPolEtablissement" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                                                         &lt;/restriction>
 *                                                       &lt;/complexContent>
 *                                                     &lt;/complexType>
 *                                                   &lt;/element>
 *                                                   &lt;element name="abesDiffuseur" form="unqualified">
 *                                                     &lt;complexType>
 *                                                       &lt;complexContent>
 *                                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                                           &lt;attribute name="FichEtabDiffAbesIntranet" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                                                           &lt;attribute name="abesDiffuseurPolEtablissement" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                                                           &lt;attribute name="dateAbesDiffuseur" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *                                                           &lt;attribute name="indicAbesDiffuseur" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                                                           &lt;attribute name="majAbesDiffuseur" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *                                                           &lt;attribute name="urlAbesDiffuseur" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                                                         &lt;/restriction>
 *                                                       &lt;/complexContent>
 *                                                     &lt;/complexType>
 *                                                   &lt;/element>
 *                                                   &lt;element name="ccsd" form="unqualified">
 *                                                     &lt;complexType>
 *                                                       &lt;complexContent>
 *                                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                                           &lt;attribute name="ccsdDiffuseurPolEtablissement" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                                                           &lt;attribute name="dateCcsd" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                                                           &lt;attribute name="identifiantCcsd" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                                                           &lt;attribute name="indicCcsd" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                                                           &lt;attribute name="majCcsd" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                                                           &lt;attribute name="numVersion" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                                                           &lt;attribute name="pwdCcsd" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                                                           &lt;attribute name="trace" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                                                           &lt;attribute name="urlCcsd" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                                                         &lt;/restriction>
 *                                                       &lt;/complexContent>
 *                                                     &lt;/complexType>
 *                                                   &lt;/element>
 *                                                   &lt;element name="autresEtabDiffuseurs" form="unqualified">
 *                                                     &lt;complexType>
 *                                                       &lt;complexContent>
 *                                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                                         &lt;/restriction>
 *                                                       &lt;/complexContent>
 *                                                     &lt;/complexType>
 *                                                   &lt;/element>
 *                                                   &lt;element name="oai" form="unqualified">
 *                                                     &lt;complexType>
 *                                                       &lt;complexContent>
 *                                                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                                           &lt;attribute name="dateOai" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *                                                           &lt;attribute name="indicOai" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                                                           &lt;attribute name="majOai" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *                                                           &lt;attribute name="trace" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                                                         &lt;/restriction>
 *                                                       &lt;/complexContent>
 *                                                     &lt;/complexType>
 *                                                   &lt;/element>
 *                                                 &lt;/sequence>
 *                                                 &lt;attribute name="conformitePolDiffusion" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                                                 &lt;attribute name="restrictionTemporelleFin" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                                                 &lt;attribute name="restrictionTemporelleType" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                                                 &lt;attribute name="typeDiffusion" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                                                 &lt;attribute name="urlPerenne" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *                                               &lt;/restriction>
 *                                             &lt;/complexContent>
 *                                           &lt;/complexType>
 *                                         &lt;/element>
 *                                       &lt;/sequence>
 *                                       &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                                     &lt;/restriction>
 *                                   &lt;/complexContent>
 *                                 &lt;/complexType>
 *                               &lt;/element>
 *                             &lt;/sequence>
 *                             &lt;attribute name="scenario" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                   &lt;/sequence>
 *                   &lt;attribute name="ID_THESE" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                   &lt;attribute name="codeEtab" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                   &lt;attribute name="enProd" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                   &lt;attribute name="etat" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                   &lt;attribute name="libEtab" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                   &lt;attribute name="ppnEtab" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
 *                   &lt;attribute name="scenarioEtab" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="step_gestion" form="unqualified">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/sequence>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "edition",
    "thesisAdmin",
    "thesisRecord",
    "starGestion",
    "stepGestion"
})
@XmlRootElement(name = "xmlData", namespace = "http://www.loc.gov/METS/")
public class XmlData {

    protected Edition edition;
    protected ThesisAdmin thesisAdmin;
    protected ThesisRecord thesisRecord;
    @XmlElement(name = "star_gestion", namespace = "")
    protected XmlData.StarGestion starGestion;
    @XmlElement(name = "step_gestion", namespace = "")
    protected XmlData.StepGestion stepGestion;

    /**
     * Obtient la valeur de la propri�t� edition.
     * 
     * @return
     *     possible object is
     *     {@link Edition }
     *     
     */
    public Edition getEdition() {
        return edition;
    }

    /**
     * D�finit la valeur de la propri�t� edition.
     * 
     * @param value
     *     allowed object is
     *     {@link Edition }
     *     
     */
    public void setEdition(Edition value) {
        this.edition = value;
    }

    /**
     * Obtient la valeur de la propri�t� thesisAdmin.
     * 
     * @return
     *     possible object is
     *     {@link ThesisAdmin }
     *     
     */
    public ThesisAdmin getThesisAdmin() {
        return thesisAdmin;
    }

    /**
     * D�finit la valeur de la propri�t� thesisAdmin.
     * 
     * @param value
     *     allowed object is
     *     {@link ThesisAdmin }
     *     
     */
    public void setThesisAdmin(ThesisAdmin value) {
        this.thesisAdmin = value;
    }

    /**
     * Obtient la valeur de la propri�t� thesisRecord.
     * 
     * @return
     *     possible object is
     *     {@link ThesisRecord }
     *     
     */
    public ThesisRecord getThesisRecord() {
        return thesisRecord;
    }

    /**
     * D�finit la valeur de la propri�t� thesisRecord.
     * 
     * @param value
     *     allowed object is
     *     {@link ThesisRecord }
     *     
     */
    public void setThesisRecord(ThesisRecord value) {
        this.thesisRecord = value;
    }

    /**
     * Obtient la valeur de la propri�t� starGestion.
     * 
     * @return
     *     possible object is
     *     {@link XmlData.StarGestion }
     *     
     */
    public XmlData.StarGestion getStarGestion() {
        return starGestion;
    }

    /**
     * D�finit la valeur de la propri�t� starGestion.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlData.StarGestion }
     *     
     */
    public void setStarGestion(XmlData.StarGestion value) {
        this.starGestion = value;
    }

    /**
     * Obtient la valeur de la propri�t� stepGestion.
     * 
     * @return
     *     possible object is
     *     {@link XmlData.StepGestion }
     *     
     */
    public XmlData.StepGestion getStepGestion() {
        return stepGestion;
    }

    /**
     * D�finit la valeur de la propri�t� stepGestion.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlData.StepGestion }
     *     
     */
    public void setStepGestion(XmlData.StepGestion value) {
        this.stepGestion = value;
    }


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
     *         &lt;element name="traitements" form="unqualified">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="entree" form="unqualified">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;attribute name="agentImport" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
     *                           &lt;attribute name="completudeImport" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *                           &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
     *                           &lt;attribute name="idSource" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                           &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="step" form="unqualified">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;attribute name="agentImport" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                           &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                           &lt;attribute name="idStep" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="maj" form="unqualified">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;attribute name="BIBL" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *                           &lt;attribute name="FICH" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *                           &lt;attribute name="SCOL" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *                           &lt;attribute name="VALID" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *                           &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
     *                           &lt;attribute name="import" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="facile" form="unqualified">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;attribute name="BIBL" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *                           &lt;attribute name="FICH" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *                           &lt;attribute name="SCOL" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *                           &lt;attribute name="VALID" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *                           &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
     *                           &lt;attribute name="import" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *                           &lt;attribute name="indicFacile" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *                           &lt;attribute name="trace" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="ctrlUrl" form="unqualified">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                           &lt;attribute name="erreurUrl" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *                           &lt;attribute name="indicCtrlUrl" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="remonteeArchive" form="unqualified">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                           &lt;attribute name="indicRemonteeArchive" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *                           &lt;attribute name="trace" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="purge" form="unqualified">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                           &lt;attribute name="indicPurge" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="invalidation" form="unqualified">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                           &lt;attribute name="indicInvalidation" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *                           &lt;attribute name="note" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="sorties" form="unqualified">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="cines" form="unqualified">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;attribute name="dateCines" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
     *                                     &lt;attribute name="indicCines" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *                                     &lt;attribute name="trace" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                             &lt;element name="sudoc" form="unqualified">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="RCR" form="unqualified">
     *                                         &lt;complexType>
     *                                           &lt;complexContent>
     *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                               &lt;attribute name="code" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
     *                                             &lt;/restriction>
     *                                           &lt;/complexContent>
     *                                         &lt;/complexType>
     *                                       &lt;/element>
     *                                       &lt;element name="EPN" form="unqualified">
     *                                         &lt;complexType>
     *                                           &lt;complexContent>
     *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                             &lt;/restriction>
     *                                           &lt;/complexContent>
     *                                         &lt;/complexType>
     *                                       &lt;/element>
     *                                     &lt;/sequence>
     *                                     &lt;attribute name="PPN" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
     *                                     &lt;attribute name="dateSudoc" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
     *                                     &lt;attribute name="indicSudoc" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *                                     &lt;attribute name="majSudoc" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
     *                                     &lt;attribute name="trace" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                             &lt;element name="diffusion" form="unqualified">
     *                               &lt;complexType>
     *                                 &lt;complexContent>
     *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                     &lt;sequence>
     *                                       &lt;element name="etabDiffuseur" form="unqualified">
     *                                         &lt;complexType>
     *                                           &lt;complexContent>
     *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                               &lt;sequence>
     *                                                 &lt;element name="urlEtabDiffuseur" form="unqualified">
     *                                                   &lt;complexType>
     *                                                     &lt;complexContent>
     *                                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                                         &lt;attribute name="majUrlEtabDiffuseur" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                                                       &lt;/restriction>
     *                                                     &lt;/complexContent>
     *                                                   &lt;/complexType>
     *                                                 &lt;/element>
     *                                               &lt;/sequence>
     *                                               &lt;attribute name="editeurScientifiqueLieu" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                                               &lt;attribute name="editeurScientifiqueNom" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                                               &lt;attribute name="etabDiffuseurPolEtablissement" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *                                             &lt;/restriction>
     *                                           &lt;/complexContent>
     *                                         &lt;/complexType>
     *                                       &lt;/element>
     *                                       &lt;element name="abesDiffuseur" form="unqualified">
     *                                         &lt;complexType>
     *                                           &lt;complexContent>
     *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                               &lt;attribute name="FichEtabDiffAbesIntranet" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *                                               &lt;attribute name="abesDiffuseurPolEtablissement" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *                                               &lt;attribute name="dateAbesDiffuseur" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
     *                                               &lt;attribute name="indicAbesDiffuseur" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *                                               &lt;attribute name="majAbesDiffuseur" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
     *                                               &lt;attribute name="urlAbesDiffuseur" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                                             &lt;/restriction>
     *                                           &lt;/complexContent>
     *                                         &lt;/complexType>
     *                                       &lt;/element>
     *                                       &lt;element name="ccsd" form="unqualified">
     *                                         &lt;complexType>
     *                                           &lt;complexContent>
     *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                               &lt;attribute name="ccsdDiffuseurPolEtablissement" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *                                               &lt;attribute name="dateCcsd" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                                               &lt;attribute name="identifiantCcsd" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                                               &lt;attribute name="indicCcsd" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *                                               &lt;attribute name="majCcsd" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                                               &lt;attribute name="numVersion" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                                               &lt;attribute name="pwdCcsd" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                                               &lt;attribute name="trace" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                                               &lt;attribute name="urlCcsd" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                                             &lt;/restriction>
     *                                           &lt;/complexContent>
     *                                         &lt;/complexType>
     *                                       &lt;/element>
     *                                       &lt;element name="autresEtabDiffuseurs" form="unqualified">
     *                                         &lt;complexType>
     *                                           &lt;complexContent>
     *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                             &lt;/restriction>
     *                                           &lt;/complexContent>
     *                                         &lt;/complexType>
     *                                       &lt;/element>
     *                                       &lt;element name="oai" form="unqualified">
     *                                         &lt;complexType>
     *                                           &lt;complexContent>
     *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                                               &lt;attribute name="dateOai" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
     *                                               &lt;attribute name="indicOai" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *                                               &lt;attribute name="majOai" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
     *                                               &lt;attribute name="trace" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                                             &lt;/restriction>
     *                                           &lt;/complexContent>
     *                                         &lt;/complexType>
     *                                       &lt;/element>
     *                                     &lt;/sequence>
     *                                     &lt;attribute name="conformitePolDiffusion" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *                                     &lt;attribute name="restrictionTemporelleFin" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                                     &lt;attribute name="restrictionTemporelleType" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *                                     &lt;attribute name="typeDiffusion" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *                                     &lt;attribute name="urlPerenne" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
     *                                   &lt;/restriction>
     *                                 &lt;/complexContent>
     *                               &lt;/complexType>
     *                             &lt;/element>
     *                           &lt;/sequence>
     *                           &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *                 &lt;attribute name="scenario" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="ID_THESE" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *       &lt;attribute name="codeEtab" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *       &lt;attribute name="enProd" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *       &lt;attribute name="etat" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *       &lt;attribute name="libEtab" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
     *       &lt;attribute name="ppnEtab" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
     *       &lt;attribute name="scenarioEtab" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "traitements"
    })
    public static class StarGestion {

        @XmlElement(namespace = "", required = true)
        protected XmlData.StarGestion.Traitements traitements;
        @XmlAttribute(name = "ID_THESE", required = true)
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlSchemaType(name = "NCName")
        protected String idthese;
        @XmlAttribute(name = "codeEtab", required = true)
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlSchemaType(name = "NCName")
        protected String codeEtab;
        @XmlAttribute(name = "enProd", required = true)
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlSchemaType(name = "NCName")
        protected String enProd;
        @XmlAttribute(name = "etat", required = true)
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlSchemaType(name = "NCName")
        protected String etat;
        @XmlAttribute(name = "libEtab", required = true)
        @XmlSchemaType(name = "anySimpleType")
        protected String libEtab;
        @XmlAttribute(name = "ppnEtab", required = true)
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlSchemaType(name = "NMTOKEN")
        protected String ppnEtab;
        @XmlAttribute(name = "scenarioEtab", required = true)
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlSchemaType(name = "NCName")
        protected String scenarioEtab;

        /**
         * Obtient la valeur de la propri�t� traitements.
         * 
         * @return
         *     possible object is
         *     {@link XmlData.StarGestion.Traitements }
         *     
         */
        public XmlData.StarGestion.Traitements getTraitements() {
            return traitements;
        }

        /**
         * D�finit la valeur de la propri�t� traitements.
         * 
         * @param value
         *     allowed object is
         *     {@link XmlData.StarGestion.Traitements }
         *     
         */
        public void setTraitements(XmlData.StarGestion.Traitements value) {
            this.traitements = value;
        }

        /**
         * Obtient la valeur de la propri�t� idthese.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIDTHESE() {
            return idthese;
        }

        /**
         * D�finit la valeur de la propri�t� idthese.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIDTHESE(String value) {
            this.idthese = value;
        }

        /**
         * Obtient la valeur de la propri�t� codeEtab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodeEtab() {
            return codeEtab;
        }

        /**
         * D�finit la valeur de la propri�t� codeEtab.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodeEtab(String value) {
            this.codeEtab = value;
        }

        /**
         * Obtient la valeur de la propri�t� enProd.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getEnProd() {
            return enProd;
        }

        /**
         * D�finit la valeur de la propri�t� enProd.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEnProd(String value) {
            this.enProd = value;
        }

        /**
         * Obtient la valeur de la propri�t� etat.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getEtat() {
            return etat;
        }

        /**
         * D�finit la valeur de la propri�t� etat.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEtat(String value) {
            this.etat = value;
        }

        /**
         * Obtient la valeur de la propri�t� libEtab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLibEtab() {
            return libEtab;
        }

        /**
         * D�finit la valeur de la propri�t� libEtab.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLibEtab(String value) {
            this.libEtab = value;
        }

        /**
         * Obtient la valeur de la propri�t� ppnEtab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPpnEtab() {
            return ppnEtab;
        }

        /**
         * D�finit la valeur de la propri�t� ppnEtab.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPpnEtab(String value) {
            this.ppnEtab = value;
        }

        /**
         * Obtient la valeur de la propri�t� scenarioEtab.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getScenarioEtab() {
            return scenarioEtab;
        }

        /**
         * D�finit la valeur de la propri�t� scenarioEtab.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setScenarioEtab(String value) {
            this.scenarioEtab = value;
        }


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
         *         &lt;element name="entree" form="unqualified">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;attribute name="agentImport" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
         *                 &lt;attribute name="completudeImport" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *                 &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
         *                 &lt;attribute name="idSource" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *                 &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="step" form="unqualified">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;attribute name="agentImport" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *                 &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *                 &lt;attribute name="idStep" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="maj" form="unqualified">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;attribute name="BIBL" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *                 &lt;attribute name="FICH" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *                 &lt;attribute name="SCOL" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *                 &lt;attribute name="VALID" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *                 &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
         *                 &lt;attribute name="import" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="facile" form="unqualified">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;attribute name="BIBL" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *                 &lt;attribute name="FICH" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *                 &lt;attribute name="SCOL" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *                 &lt;attribute name="VALID" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *                 &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
         *                 &lt;attribute name="import" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *                 &lt;attribute name="indicFacile" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *                 &lt;attribute name="trace" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="ctrlUrl" form="unqualified">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *                 &lt;attribute name="erreurUrl" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *                 &lt;attribute name="indicCtrlUrl" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="remonteeArchive" form="unqualified">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *                 &lt;attribute name="indicRemonteeArchive" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *                 &lt;attribute name="trace" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="purge" form="unqualified">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *                 &lt;attribute name="indicPurge" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="invalidation" form="unqualified">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *                 &lt;attribute name="indicInvalidation" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *                 &lt;attribute name="note" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="sorties" form="unqualified">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="cines" form="unqualified">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;attribute name="dateCines" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
         *                           &lt;attribute name="indicCines" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *                           &lt;attribute name="trace" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                   &lt;element name="sudoc" form="unqualified">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="RCR" form="unqualified">
         *                               &lt;complexType>
         *                                 &lt;complexContent>
         *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                                     &lt;attribute name="code" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
         *                                   &lt;/restriction>
         *                                 &lt;/complexContent>
         *                               &lt;/complexType>
         *                             &lt;/element>
         *                             &lt;element name="EPN" form="unqualified">
         *                               &lt;complexType>
         *                                 &lt;complexContent>
         *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                                   &lt;/restriction>
         *                                 &lt;/complexContent>
         *                               &lt;/complexType>
         *                             &lt;/element>
         *                           &lt;/sequence>
         *                           &lt;attribute name="PPN" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
         *                           &lt;attribute name="dateSudoc" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
         *                           &lt;attribute name="indicSudoc" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *                           &lt;attribute name="majSudoc" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
         *                           &lt;attribute name="trace" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                   &lt;element name="diffusion" form="unqualified">
         *                     &lt;complexType>
         *                       &lt;complexContent>
         *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                           &lt;sequence>
         *                             &lt;element name="etabDiffuseur" form="unqualified">
         *                               &lt;complexType>
         *                                 &lt;complexContent>
         *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                                     &lt;sequence>
         *                                       &lt;element name="urlEtabDiffuseur" form="unqualified">
         *                                         &lt;complexType>
         *                                           &lt;complexContent>
         *                                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                                               &lt;attribute name="majUrlEtabDiffuseur" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *                                             &lt;/restriction>
         *                                           &lt;/complexContent>
         *                                         &lt;/complexType>
         *                                       &lt;/element>
         *                                     &lt;/sequence>
         *                                     &lt;attribute name="editeurScientifiqueLieu" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *                                     &lt;attribute name="editeurScientifiqueNom" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *                                     &lt;attribute name="etabDiffuseurPolEtablissement" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *                                   &lt;/restriction>
         *                                 &lt;/complexContent>
         *                               &lt;/complexType>
         *                             &lt;/element>
         *                             &lt;element name="abesDiffuseur" form="unqualified">
         *                               &lt;complexType>
         *                                 &lt;complexContent>
         *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                                     &lt;attribute name="FichEtabDiffAbesIntranet" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *                                     &lt;attribute name="abesDiffuseurPolEtablissement" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *                                     &lt;attribute name="dateAbesDiffuseur" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
         *                                     &lt;attribute name="indicAbesDiffuseur" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *                                     &lt;attribute name="majAbesDiffuseur" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
         *                                     &lt;attribute name="urlAbesDiffuseur" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *                                   &lt;/restriction>
         *                                 &lt;/complexContent>
         *                               &lt;/complexType>
         *                             &lt;/element>
         *                             &lt;element name="ccsd" form="unqualified">
         *                               &lt;complexType>
         *                                 &lt;complexContent>
         *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                                     &lt;attribute name="ccsdDiffuseurPolEtablissement" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *                                     &lt;attribute name="dateCcsd" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *                                     &lt;attribute name="identifiantCcsd" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *                                     &lt;attribute name="indicCcsd" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *                                     &lt;attribute name="majCcsd" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *                                     &lt;attribute name="numVersion" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *                                     &lt;attribute name="pwdCcsd" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *                                     &lt;attribute name="trace" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *                                     &lt;attribute name="urlCcsd" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *                                   &lt;/restriction>
         *                                 &lt;/complexContent>
         *                               &lt;/complexType>
         *                             &lt;/element>
         *                             &lt;element name="autresEtabDiffuseurs" form="unqualified">
         *                               &lt;complexType>
         *                                 &lt;complexContent>
         *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                                   &lt;/restriction>
         *                                 &lt;/complexContent>
         *                               &lt;/complexType>
         *                             &lt;/element>
         *                             &lt;element name="oai" form="unqualified">
         *                               &lt;complexType>
         *                                 &lt;complexContent>
         *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                                     &lt;attribute name="dateOai" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
         *                                     &lt;attribute name="indicOai" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *                                     &lt;attribute name="majOai" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
         *                                     &lt;attribute name="trace" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *                                   &lt;/restriction>
         *                                 &lt;/complexContent>
         *                               &lt;/complexType>
         *                             &lt;/element>
         *                           &lt;/sequence>
         *                           &lt;attribute name="conformitePolDiffusion" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *                           &lt;attribute name="restrictionTemporelleFin" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *                           &lt;attribute name="restrictionTemporelleType" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *                           &lt;attribute name="typeDiffusion" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *                           &lt;attribute name="urlPerenne" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
         *                         &lt;/restriction>
         *                       &lt;/complexContent>
         *                     &lt;/complexType>
         *                   &lt;/element>
         *                 &lt;/sequence>
         *                 &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *       &lt;/sequence>
         *       &lt;attribute name="scenario" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "entree",
            "step",
            "maj",
            "facile",
            "ctrlUrl",
            "remonteeArchive",
            "purge",
            "invalidation",
            "sorties"
        })
        public static class Traitements {

            @XmlElement(namespace = "", required = true)
            protected XmlData.StarGestion.Traitements.Entree entree;
            @XmlElement(namespace = "", required = true)
            protected XmlData.StarGestion.Traitements.Step step;
            @XmlElement(namespace = "", required = true)
            protected XmlData.StarGestion.Traitements.Maj maj;
            @XmlElement(namespace = "", required = true)
            protected XmlData.StarGestion.Traitements.Facile facile;
            @XmlElement(namespace = "", required = true)
            protected XmlData.StarGestion.Traitements.CtrlUrl ctrlUrl;
            @XmlElement(namespace = "", required = true)
            protected XmlData.StarGestion.Traitements.RemonteeArchive remonteeArchive;
            @XmlElement(namespace = "", required = true)
            protected XmlData.StarGestion.Traitements.Purge purge;
            @XmlElement(namespace = "", required = true)
            protected XmlData.StarGestion.Traitements.Invalidation invalidation;
            @XmlElement(namespace = "", required = true)
            protected XmlData.StarGestion.Traitements.Sorties sorties;
            @XmlAttribute(name = "scenario", required = true)
            @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
            @XmlSchemaType(name = "NCName")
            protected String scenario;

            /**
             * Obtient la valeur de la propri�t� entree.
             * 
             * @return
             *     possible object is
             *     {@link XmlData.StarGestion.Traitements.Entree }
             *     
             */
            public XmlData.StarGestion.Traitements.Entree getEntree() {
                return entree;
            }

            /**
             * D�finit la valeur de la propri�t� entree.
             * 
             * @param value
             *     allowed object is
             *     {@link XmlData.StarGestion.Traitements.Entree }
             *     
             */
            public void setEntree(XmlData.StarGestion.Traitements.Entree value) {
                this.entree = value;
            }

            /**
             * Obtient la valeur de la propri�t� step.
             * 
             * @return
             *     possible object is
             *     {@link XmlData.StarGestion.Traitements.Step }
             *     
             */
            public XmlData.StarGestion.Traitements.Step getStep() {
                return step;
            }

            /**
             * D�finit la valeur de la propri�t� step.
             * 
             * @param value
             *     allowed object is
             *     {@link XmlData.StarGestion.Traitements.Step }
             *     
             */
            public void setStep(XmlData.StarGestion.Traitements.Step value) {
                this.step = value;
            }

            /**
             * Obtient la valeur de la propri�t� maj.
             * 
             * @return
             *     possible object is
             *     {@link XmlData.StarGestion.Traitements.Maj }
             *     
             */
            public XmlData.StarGestion.Traitements.Maj getMaj() {
                return maj;
            }

            /**
             * D�finit la valeur de la propri�t� maj.
             * 
             * @param value
             *     allowed object is
             *     {@link XmlData.StarGestion.Traitements.Maj }
             *     
             */
            public void setMaj(XmlData.StarGestion.Traitements.Maj value) {
                this.maj = value;
            }

            /**
             * Obtient la valeur de la propri�t� facile.
             * 
             * @return
             *     possible object is
             *     {@link XmlData.StarGestion.Traitements.Facile }
             *     
             */
            public XmlData.StarGestion.Traitements.Facile getFacile() {
                return facile;
            }

            /**
             * D�finit la valeur de la propri�t� facile.
             * 
             * @param value
             *     allowed object is
             *     {@link XmlData.StarGestion.Traitements.Facile }
             *     
             */
            public void setFacile(XmlData.StarGestion.Traitements.Facile value) {
                this.facile = value;
            }

            /**
             * Obtient la valeur de la propri�t� ctrlUrl.
             * 
             * @return
             *     possible object is
             *     {@link XmlData.StarGestion.Traitements.CtrlUrl }
             *     
             */
            public XmlData.StarGestion.Traitements.CtrlUrl getCtrlUrl() {
                return ctrlUrl;
            }

            /**
             * D�finit la valeur de la propri�t� ctrlUrl.
             * 
             * @param value
             *     allowed object is
             *     {@link XmlData.StarGestion.Traitements.CtrlUrl }
             *     
             */
            public void setCtrlUrl(XmlData.StarGestion.Traitements.CtrlUrl value) {
                this.ctrlUrl = value;
            }

            /**
             * Obtient la valeur de la propri�t� remonteeArchive.
             * 
             * @return
             *     possible object is
             *     {@link XmlData.StarGestion.Traitements.RemonteeArchive }
             *     
             */
            public XmlData.StarGestion.Traitements.RemonteeArchive getRemonteeArchive() {
                return remonteeArchive;
            }

            /**
             * D�finit la valeur de la propri�t� remonteeArchive.
             * 
             * @param value
             *     allowed object is
             *     {@link XmlData.StarGestion.Traitements.RemonteeArchive }
             *     
             */
            public void setRemonteeArchive(XmlData.StarGestion.Traitements.RemonteeArchive value) {
                this.remonteeArchive = value;
            }

            /**
             * Obtient la valeur de la propri�t� purge.
             * 
             * @return
             *     possible object is
             *     {@link XmlData.StarGestion.Traitements.Purge }
             *     
             */
            public XmlData.StarGestion.Traitements.Purge getPurge() {
                return purge;
            }

            /**
             * D�finit la valeur de la propri�t� purge.
             * 
             * @param value
             *     allowed object is
             *     {@link XmlData.StarGestion.Traitements.Purge }
             *     
             */
            public void setPurge(XmlData.StarGestion.Traitements.Purge value) {
                this.purge = value;
            }

            /**
             * Obtient la valeur de la propri�t� invalidation.
             * 
             * @return
             *     possible object is
             *     {@link XmlData.StarGestion.Traitements.Invalidation }
             *     
             */
            public XmlData.StarGestion.Traitements.Invalidation getInvalidation() {
                return invalidation;
            }

            /**
             * D�finit la valeur de la propri�t� invalidation.
             * 
             * @param value
             *     allowed object is
             *     {@link XmlData.StarGestion.Traitements.Invalidation }
             *     
             */
            public void setInvalidation(XmlData.StarGestion.Traitements.Invalidation value) {
                this.invalidation = value;
            }

            /**
             * Obtient la valeur de la propri�t� sorties.
             * 
             * @return
             *     possible object is
             *     {@link XmlData.StarGestion.Traitements.Sorties }
             *     
             */
            public XmlData.StarGestion.Traitements.Sorties getSorties() {
                return sorties;
            }

            /**
             * D�finit la valeur de la propri�t� sorties.
             * 
             * @param value
             *     allowed object is
             *     {@link XmlData.StarGestion.Traitements.Sorties }
             *     
             */
            public void setSorties(XmlData.StarGestion.Traitements.Sorties value) {
                this.sorties = value;
            }

            /**
             * Obtient la valeur de la propri�t� scenario.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getScenario() {
                return scenario;
            }

            /**
             * D�finit la valeur de la propri�t� scenario.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setScenario(String value) {
                this.scenario = value;
            }


            /**
             * <p>Classe Java pour anonymous complex type.
             * 
             * <p>Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette classe.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
             *       &lt;attribute name="erreurUrl" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *       &lt;attribute name="indicCtrlUrl" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class CtrlUrl {

                @XmlAttribute(name = "date", required = true)
                @XmlSchemaType(name = "anySimpleType")
                protected String date;
                @XmlAttribute(name = "erreurUrl", required = true)
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String erreurUrl;
                @XmlAttribute(name = "indicCtrlUrl", required = true)
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String indicCtrlUrl;

                /**
                 * Obtient la valeur de la propri�t� date.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getDate() {
                    return date;
                }

                /**
                 * D�finit la valeur de la propri�t� date.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setDate(String value) {
                    this.date = value;
                }

                /**
                 * Obtient la valeur de la propri�t� erreurUrl.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getErreurUrl() {
                    return erreurUrl;
                }

                /**
                 * D�finit la valeur de la propri�t� erreurUrl.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setErreurUrl(String value) {
                    this.erreurUrl = value;
                }

                /**
                 * Obtient la valeur de la propri�t� indicCtrlUrl.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getIndicCtrlUrl() {
                    return indicCtrlUrl;
                }

                /**
                 * D�finit la valeur de la propri�t� indicCtrlUrl.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setIndicCtrlUrl(String value) {
                    this.indicCtrlUrl = value;
                }

            }


            /**
             * <p>Classe Java pour anonymous complex type.
             * 
             * <p>Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette classe.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;attribute name="agentImport" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
             *       &lt;attribute name="completudeImport" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *       &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
             *       &lt;attribute name="idSource" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
             *       &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class Entree {

                @XmlAttribute(name = "agentImport", required = true)
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NMTOKEN")
                protected String agentImport;
                @XmlAttribute(name = "completudeImport", required = true)
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String completudeImport;
                @XmlAttribute(name = "date", required = true)
                @XmlSchemaType(name = "dateTime")
                protected XMLGregorianCalendar date;
                @XmlAttribute(name = "idSource", required = true)
                @XmlSchemaType(name = "anySimpleType")
                protected String idSource;
                @XmlAttribute(name = "type", required = true)
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String type;

                /**
                 * Obtient la valeur de la propri�t� agentImport.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getAgentImport() {
                    return agentImport;
                }

                /**
                 * D�finit la valeur de la propri�t� agentImport.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setAgentImport(String value) {
                    this.agentImport = value;
                }

                /**
                 * Obtient la valeur de la propri�t� completudeImport.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getCompletudeImport() {
                    return completudeImport;
                }

                /**
                 * D�finit la valeur de la propri�t� completudeImport.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setCompletudeImport(String value) {
                    this.completudeImport = value;
                }

                /**
                 * Obtient la valeur de la propri�t� date.
                 * 
                 * @return
                 *     possible object is
                 *     {@link XMLGregorianCalendar }
                 *     
                 */
                public XMLGregorianCalendar getDate() {
                    return date;
                }

                /**
                 * D�finit la valeur de la propri�t� date.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link XMLGregorianCalendar }
                 *     
                 */
                public void setDate(XMLGregorianCalendar value) {
                    this.date = value;
                }

                /**
                 * Obtient la valeur de la propri�t� idSource.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getIdSource() {
                    return idSource;
                }

                /**
                 * D�finit la valeur de la propri�t� idSource.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setIdSource(String value) {
                    this.idSource = value;
                }

                /**
                 * Obtient la valeur de la propri�t� type.
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
                 * D�finit la valeur de la propri�t� type.
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


            /**
             * <p>Classe Java pour anonymous complex type.
             * 
             * <p>Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette classe.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;attribute name="BIBL" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *       &lt;attribute name="FICH" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *       &lt;attribute name="SCOL" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *       &lt;attribute name="VALID" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *       &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
             *       &lt;attribute name="import" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *       &lt;attribute name="indicFacile" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *       &lt;attribute name="trace" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class Facile {

                @XmlAttribute(name = "BIBL", required = true)
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String bibl;
                @XmlAttribute(name = "FICH", required = true)
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String fich;
                @XmlAttribute(name = "SCOL", required = true)
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String scol;
                @XmlAttribute(name = "VALID", required = true)
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String valid;
                @XmlAttribute(name = "date", required = true)
                @XmlSchemaType(name = "dateTime")
                protected XMLGregorianCalendar date;
                @XmlAttribute(name = "import", required = true)
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String _import;
                @XmlAttribute(name = "indicFacile", required = true)
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String indicFacile;
                @XmlAttribute(name = "trace", required = true)
                @XmlSchemaType(name = "anySimpleType")
                protected String trace;

                /**
                 * Obtient la valeur de la propri�t� bibl.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getBIBL() {
                    return bibl;
                }

                /**
                 * D�finit la valeur de la propri�t� bibl.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setBIBL(String value) {
                    this.bibl = value;
                }

                /**
                 * Obtient la valeur de la propri�t� fich.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getFICH() {
                    return fich;
                }

                /**
                 * D�finit la valeur de la propri�t� fich.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setFICH(String value) {
                    this.fich = value;
                }

                /**
                 * Obtient la valeur de la propri�t� scol.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getSCOL() {
                    return scol;
                }

                /**
                 * D�finit la valeur de la propri�t� scol.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setSCOL(String value) {
                    this.scol = value;
                }

                /**
                 * Obtient la valeur de la propri�t� valid.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getVALID() {
                    return valid;
                }

                /**
                 * D�finit la valeur de la propri�t� valid.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setVALID(String value) {
                    this.valid = value;
                }

                /**
                 * Obtient la valeur de la propri�t� date.
                 * 
                 * @return
                 *     possible object is
                 *     {@link XMLGregorianCalendar }
                 *     
                 */
                public XMLGregorianCalendar getDate() {
                    return date;
                }

                /**
                 * D�finit la valeur de la propri�t� date.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link XMLGregorianCalendar }
                 *     
                 */
                public void setDate(XMLGregorianCalendar value) {
                    this.date = value;
                }

                /**
                 * Obtient la valeur de la propri�t� import.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getImport() {
                    return _import;
                }

                /**
                 * D�finit la valeur de la propri�t� import.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setImport(String value) {
                    this._import = value;
                }

                /**
                 * Obtient la valeur de la propri�t� indicFacile.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getIndicFacile() {
                    return indicFacile;
                }

                /**
                 * D�finit la valeur de la propri�t� indicFacile.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setIndicFacile(String value) {
                    this.indicFacile = value;
                }

                /**
                 * Obtient la valeur de la propri�t� trace.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getTrace() {
                    return trace;
                }

                /**
                 * D�finit la valeur de la propri�t� trace.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setTrace(String value) {
                    this.trace = value;
                }

            }


            /**
             * <p>Classe Java pour anonymous complex type.
             * 
             * <p>Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette classe.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
             *       &lt;attribute name="indicInvalidation" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *       &lt;attribute name="note" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class Invalidation {

                @XmlAttribute(name = "date", required = true)
                @XmlSchemaType(name = "anySimpleType")
                protected String date;
                @XmlAttribute(name = "indicInvalidation", required = true)
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String indicInvalidation;
                @XmlAttribute(name = "note", required = true)
                @XmlSchemaType(name = "anySimpleType")
                protected String note;

                /**
                 * Obtient la valeur de la propri�t� date.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getDate() {
                    return date;
                }

                /**
                 * D�finit la valeur de la propri�t� date.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setDate(String value) {
                    this.date = value;
                }

                /**
                 * Obtient la valeur de la propri�t� indicInvalidation.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getIndicInvalidation() {
                    return indicInvalidation;
                }

                /**
                 * D�finit la valeur de la propri�t� indicInvalidation.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setIndicInvalidation(String value) {
                    this.indicInvalidation = value;
                }

                /**
                 * Obtient la valeur de la propri�t� note.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getNote() {
                    return note;
                }

                /**
                 * D�finit la valeur de la propri�t� note.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setNote(String value) {
                    this.note = value;
                }

            }


            /**
             * <p>Classe Java pour anonymous complex type.
             * 
             * <p>Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette classe.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;attribute name="BIBL" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *       &lt;attribute name="FICH" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *       &lt;attribute name="SCOL" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *       &lt;attribute name="VALID" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *       &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
             *       &lt;attribute name="import" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class Maj {

                @XmlAttribute(name = "BIBL", required = true)
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String bibl;
                @XmlAttribute(name = "FICH", required = true)
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String fich;
                @XmlAttribute(name = "SCOL", required = true)
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String scol;
                @XmlAttribute(name = "VALID", required = true)
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String valid;
                @XmlAttribute(name = "date", required = true)
                @XmlSchemaType(name = "dateTime")
                protected XMLGregorianCalendar date;
                @XmlAttribute(name = "import", required = true)
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String _import;

                /**
                 * Obtient la valeur de la propri�t� bibl.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getBIBL() {
                    return bibl;
                }

                /**
                 * D�finit la valeur de la propri�t� bibl.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setBIBL(String value) {
                    this.bibl = value;
                }

                /**
                 * Obtient la valeur de la propri�t� fich.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getFICH() {
                    return fich;
                }

                /**
                 * D�finit la valeur de la propri�t� fich.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setFICH(String value) {
                    this.fich = value;
                }

                /**
                 * Obtient la valeur de la propri�t� scol.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getSCOL() {
                    return scol;
                }

                /**
                 * D�finit la valeur de la propri�t� scol.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setSCOL(String value) {
                    this.scol = value;
                }

                /**
                 * Obtient la valeur de la propri�t� valid.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getVALID() {
                    return valid;
                }

                /**
                 * D�finit la valeur de la propri�t� valid.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setVALID(String value) {
                    this.valid = value;
                }

                /**
                 * Obtient la valeur de la propri�t� date.
                 * 
                 * @return
                 *     possible object is
                 *     {@link XMLGregorianCalendar }
                 *     
                 */
                public XMLGregorianCalendar getDate() {
                    return date;
                }

                /**
                 * D�finit la valeur de la propri�t� date.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link XMLGregorianCalendar }
                 *     
                 */
                public void setDate(XMLGregorianCalendar value) {
                    this.date = value;
                }

                /**
                 * Obtient la valeur de la propri�t� import.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getImport() {
                    return _import;
                }

                /**
                 * D�finit la valeur de la propri�t� import.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setImport(String value) {
                    this._import = value;
                }

            }


            /**
             * <p>Classe Java pour anonymous complex type.
             * 
             * <p>Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette classe.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
             *       &lt;attribute name="indicPurge" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class Purge {

                @XmlAttribute(name = "date", required = true)
                @XmlSchemaType(name = "anySimpleType")
                protected String date;
                @XmlAttribute(name = "indicPurge", required = true)
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String indicPurge;

                /**
                 * Obtient la valeur de la propri�t� date.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getDate() {
                    return date;
                }

                /**
                 * D�finit la valeur de la propri�t� date.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setDate(String value) {
                    this.date = value;
                }

                /**
                 * Obtient la valeur de la propri�t� indicPurge.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getIndicPurge() {
                    return indicPurge;
                }

                /**
                 * D�finit la valeur de la propri�t� indicPurge.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setIndicPurge(String value) {
                    this.indicPurge = value;
                }

            }


            /**
             * <p>Classe Java pour anonymous complex type.
             * 
             * <p>Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette classe.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
             *       &lt;attribute name="indicRemonteeArchive" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *       &lt;attribute name="trace" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class RemonteeArchive {

                @XmlAttribute(name = "date", required = true)
                @XmlSchemaType(name = "anySimpleType")
                protected String date;
                @XmlAttribute(name = "indicRemonteeArchive", required = true)
                @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                @XmlSchemaType(name = "NCName")
                protected String indicRemonteeArchive;
                @XmlAttribute(name = "trace", required = true)
                @XmlSchemaType(name = "anySimpleType")
                protected String trace;

                /**
                 * Obtient la valeur de la propri�t� date.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getDate() {
                    return date;
                }

                /**
                 * D�finit la valeur de la propri�t� date.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setDate(String value) {
                    this.date = value;
                }

                /**
                 * Obtient la valeur de la propri�t� indicRemonteeArchive.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getIndicRemonteeArchive() {
                    return indicRemonteeArchive;
                }

                /**
                 * D�finit la valeur de la propri�t� indicRemonteeArchive.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setIndicRemonteeArchive(String value) {
                    this.indicRemonteeArchive = value;
                }

                /**
                 * Obtient la valeur de la propri�t� trace.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getTrace() {
                    return trace;
                }

                /**
                 * D�finit la valeur de la propri�t� trace.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setTrace(String value) {
                    this.trace = value;
                }

            }


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
             *         &lt;element name="cines" form="unqualified">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;attribute name="dateCines" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
             *                 &lt;attribute name="indicCines" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *                 &lt;attribute name="trace" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="sudoc" form="unqualified">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="RCR" form="unqualified">
             *                     &lt;complexType>
             *                       &lt;complexContent>
             *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                           &lt;attribute name="code" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
             *                         &lt;/restriction>
             *                       &lt;/complexContent>
             *                     &lt;/complexType>
             *                   &lt;/element>
             *                   &lt;element name="EPN" form="unqualified">
             *                     &lt;complexType>
             *                       &lt;complexContent>
             *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                         &lt;/restriction>
             *                       &lt;/complexContent>
             *                     &lt;/complexType>
             *                   &lt;/element>
             *                 &lt;/sequence>
             *                 &lt;attribute name="PPN" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
             *                 &lt;attribute name="dateSudoc" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
             *                 &lt;attribute name="indicSudoc" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *                 &lt;attribute name="majSudoc" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
             *                 &lt;attribute name="trace" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *         &lt;element name="diffusion" form="unqualified">
             *           &lt;complexType>
             *             &lt;complexContent>
             *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                 &lt;sequence>
             *                   &lt;element name="etabDiffuseur" form="unqualified">
             *                     &lt;complexType>
             *                       &lt;complexContent>
             *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                           &lt;sequence>
             *                             &lt;element name="urlEtabDiffuseur" form="unqualified">
             *                               &lt;complexType>
             *                                 &lt;complexContent>
             *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                                     &lt;attribute name="majUrlEtabDiffuseur" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
             *                                   &lt;/restriction>
             *                                 &lt;/complexContent>
             *                               &lt;/complexType>
             *                             &lt;/element>
             *                           &lt;/sequence>
             *                           &lt;attribute name="editeurScientifiqueLieu" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
             *                           &lt;attribute name="editeurScientifiqueNom" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
             *                           &lt;attribute name="etabDiffuseurPolEtablissement" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *                         &lt;/restriction>
             *                       &lt;/complexContent>
             *                     &lt;/complexType>
             *                   &lt;/element>
             *                   &lt;element name="abesDiffuseur" form="unqualified">
             *                     &lt;complexType>
             *                       &lt;complexContent>
             *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                           &lt;attribute name="FichEtabDiffAbesIntranet" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *                           &lt;attribute name="abesDiffuseurPolEtablissement" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *                           &lt;attribute name="dateAbesDiffuseur" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
             *                           &lt;attribute name="indicAbesDiffuseur" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *                           &lt;attribute name="majAbesDiffuseur" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
             *                           &lt;attribute name="urlAbesDiffuseur" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
             *                         &lt;/restriction>
             *                       &lt;/complexContent>
             *                     &lt;/complexType>
             *                   &lt;/element>
             *                   &lt;element name="ccsd" form="unqualified">
             *                     &lt;complexType>
             *                       &lt;complexContent>
             *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                           &lt;attribute name="ccsdDiffuseurPolEtablissement" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *                           &lt;attribute name="dateCcsd" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
             *                           &lt;attribute name="identifiantCcsd" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
             *                           &lt;attribute name="indicCcsd" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *                           &lt;attribute name="majCcsd" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
             *                           &lt;attribute name="numVersion" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
             *                           &lt;attribute name="pwdCcsd" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
             *                           &lt;attribute name="trace" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
             *                           &lt;attribute name="urlCcsd" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
             *                         &lt;/restriction>
             *                       &lt;/complexContent>
             *                     &lt;/complexType>
             *                   &lt;/element>
             *                   &lt;element name="autresEtabDiffuseurs" form="unqualified">
             *                     &lt;complexType>
             *                       &lt;complexContent>
             *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                         &lt;/restriction>
             *                       &lt;/complexContent>
             *                     &lt;/complexType>
             *                   &lt;/element>
             *                   &lt;element name="oai" form="unqualified">
             *                     &lt;complexType>
             *                       &lt;complexContent>
             *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *                           &lt;attribute name="dateOai" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
             *                           &lt;attribute name="indicOai" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *                           &lt;attribute name="majOai" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
             *                           &lt;attribute name="trace" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
             *                         &lt;/restriction>
             *                       &lt;/complexContent>
             *                     &lt;/complexType>
             *                   &lt;/element>
             *                 &lt;/sequence>
             *                 &lt;attribute name="conformitePolDiffusion" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *                 &lt;attribute name="restrictionTemporelleFin" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
             *                 &lt;attribute name="restrictionTemporelleType" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *                 &lt;attribute name="typeDiffusion" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
             *                 &lt;attribute name="urlPerenne" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
             *               &lt;/restriction>
             *             &lt;/complexContent>
             *           &lt;/complexType>
             *         &lt;/element>
             *       &lt;/sequence>
             *       &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "cines",
                "sudoc",
                "diffusion"
            })
            public static class Sorties {

                @XmlElement(namespace = "", required = true)
                protected XmlData.StarGestion.Traitements.Sorties.Cines cines;
                @XmlElement(namespace = "", required = true)
                protected XmlData.StarGestion.Traitements.Sorties.Sudoc sudoc;
                @XmlElement(namespace = "", required = true)
                protected XmlData.StarGestion.Traitements.Sorties.Diffusion diffusion;
                @XmlAttribute(name = "date", required = true)
                @XmlSchemaType(name = "anySimpleType")
                protected String date;

                /**
                 * Obtient la valeur de la propri�t� cines.
                 * 
                 * @return
                 *     possible object is
                 *     {@link XmlData.StarGestion.Traitements.Sorties.Cines }
                 *     
                 */
                public XmlData.StarGestion.Traitements.Sorties.Cines getCines() {
                    return cines;
                }

                /**
                 * D�finit la valeur de la propri�t� cines.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link XmlData.StarGestion.Traitements.Sorties.Cines }
                 *     
                 */
                public void setCines(XmlData.StarGestion.Traitements.Sorties.Cines value) {
                    this.cines = value;
                }

                /**
                 * Obtient la valeur de la propri�t� sudoc.
                 * 
                 * @return
                 *     possible object is
                 *     {@link XmlData.StarGestion.Traitements.Sorties.Sudoc }
                 *     
                 */
                public XmlData.StarGestion.Traitements.Sorties.Sudoc getSudoc() {
                    return sudoc;
                }

                /**
                 * D�finit la valeur de la propri�t� sudoc.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link XmlData.StarGestion.Traitements.Sorties.Sudoc }
                 *     
                 */
                public void setSudoc(XmlData.StarGestion.Traitements.Sorties.Sudoc value) {
                    this.sudoc = value;
                }

                /**
                 * Obtient la valeur de la propri�t� diffusion.
                 * 
                 * @return
                 *     possible object is
                 *     {@link XmlData.StarGestion.Traitements.Sorties.Diffusion }
                 *     
                 */
                public XmlData.StarGestion.Traitements.Sorties.Diffusion getDiffusion() {
                    return diffusion;
                }

                /**
                 * D�finit la valeur de la propri�t� diffusion.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link XmlData.StarGestion.Traitements.Sorties.Diffusion }
                 *     
                 */
                public void setDiffusion(XmlData.StarGestion.Traitements.Sorties.Diffusion value) {
                    this.diffusion = value;
                }

                /**
                 * Obtient la valeur de la propri�t� date.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getDate() {
                    return date;
                }

                /**
                 * D�finit la valeur de la propri�t� date.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setDate(String value) {
                    this.date = value;
                }


                /**
                 * <p>Classe Java pour anonymous complex type.
                 * 
                 * <p>Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette classe.
                 * 
                 * <pre>
                 * &lt;complexType>
                 *   &lt;complexContent>
                 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *       &lt;attribute name="dateCines" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
                 *       &lt;attribute name="indicCines" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
                 *       &lt;attribute name="trace" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "")
                public static class Cines {

                    @XmlAttribute(name = "dateCines", required = true)
                    @XmlSchemaType(name = "dateTime")
                    protected XMLGregorianCalendar dateCines;
                    @XmlAttribute(name = "indicCines", required = true)
                    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                    @XmlSchemaType(name = "NCName")
                    protected String indicCines;
                    @XmlAttribute(name = "trace", required = true)
                    @XmlSchemaType(name = "anySimpleType")
                    protected String trace;

                    /**
                     * Obtient la valeur de la propri�t� dateCines.
                     * 
                     * @return
                     *     possible object is
                     *     {@link XMLGregorianCalendar }
                     *     
                     */
                    public XMLGregorianCalendar getDateCines() {
                        return dateCines;
                    }

                    /**
                     * D�finit la valeur de la propri�t� dateCines.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link XMLGregorianCalendar }
                     *     
                     */
                    public void setDateCines(XMLGregorianCalendar value) {
                        this.dateCines = value;
                    }

                    /**
                     * Obtient la valeur de la propri�t� indicCines.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getIndicCines() {
                        return indicCines;
                    }

                    /**
                     * D�finit la valeur de la propri�t� indicCines.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setIndicCines(String value) {
                        this.indicCines = value;
                    }

                    /**
                     * Obtient la valeur de la propri�t� trace.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getTrace() {
                        return trace;
                    }

                    /**
                     * D�finit la valeur de la propri�t� trace.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setTrace(String value) {
                        this.trace = value;
                    }

                }


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
                 *         &lt;element name="etabDiffuseur" form="unqualified">
                 *           &lt;complexType>
                 *             &lt;complexContent>
                 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *                 &lt;sequence>
                 *                   &lt;element name="urlEtabDiffuseur" form="unqualified">
                 *                     &lt;complexType>
                 *                       &lt;complexContent>
                 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *                           &lt;attribute name="majUrlEtabDiffuseur" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
                 *                         &lt;/restriction>
                 *                       &lt;/complexContent>
                 *                     &lt;/complexType>
                 *                   &lt;/element>
                 *                 &lt;/sequence>
                 *                 &lt;attribute name="editeurScientifiqueLieu" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
                 *                 &lt;attribute name="editeurScientifiqueNom" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
                 *                 &lt;attribute name="etabDiffuseurPolEtablissement" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
                 *               &lt;/restriction>
                 *             &lt;/complexContent>
                 *           &lt;/complexType>
                 *         &lt;/element>
                 *         &lt;element name="abesDiffuseur" form="unqualified">
                 *           &lt;complexType>
                 *             &lt;complexContent>
                 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *                 &lt;attribute name="FichEtabDiffAbesIntranet" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
                 *                 &lt;attribute name="abesDiffuseurPolEtablissement" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
                 *                 &lt;attribute name="dateAbesDiffuseur" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
                 *                 &lt;attribute name="indicAbesDiffuseur" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
                 *                 &lt;attribute name="majAbesDiffuseur" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
                 *                 &lt;attribute name="urlAbesDiffuseur" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
                 *               &lt;/restriction>
                 *             &lt;/complexContent>
                 *           &lt;/complexType>
                 *         &lt;/element>
                 *         &lt;element name="ccsd" form="unqualified">
                 *           &lt;complexType>
                 *             &lt;complexContent>
                 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *                 &lt;attribute name="ccsdDiffuseurPolEtablissement" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
                 *                 &lt;attribute name="dateCcsd" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
                 *                 &lt;attribute name="identifiantCcsd" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
                 *                 &lt;attribute name="indicCcsd" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
                 *                 &lt;attribute name="majCcsd" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
                 *                 &lt;attribute name="numVersion" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
                 *                 &lt;attribute name="pwdCcsd" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
                 *                 &lt;attribute name="trace" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
                 *                 &lt;attribute name="urlCcsd" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
                 *               &lt;/restriction>
                 *             &lt;/complexContent>
                 *           &lt;/complexType>
                 *         &lt;/element>
                 *         &lt;element name="autresEtabDiffuseurs" form="unqualified">
                 *           &lt;complexType>
                 *             &lt;complexContent>
                 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *               &lt;/restriction>
                 *             &lt;/complexContent>
                 *           &lt;/complexType>
                 *         &lt;/element>
                 *         &lt;element name="oai" form="unqualified">
                 *           &lt;complexType>
                 *             &lt;complexContent>
                 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *                 &lt;attribute name="dateOai" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
                 *                 &lt;attribute name="indicOai" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
                 *                 &lt;attribute name="majOai" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
                 *                 &lt;attribute name="trace" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
                 *               &lt;/restriction>
                 *             &lt;/complexContent>
                 *           &lt;/complexType>
                 *         &lt;/element>
                 *       &lt;/sequence>
                 *       &lt;attribute name="conformitePolDiffusion" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
                 *       &lt;attribute name="restrictionTemporelleFin" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
                 *       &lt;attribute name="restrictionTemporelleType" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
                 *       &lt;attribute name="typeDiffusion" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
                 *       &lt;attribute name="urlPerenne" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
                 *     &lt;/restriction>
                 *   &lt;/complexContent>
                 * &lt;/complexType>
                 * </pre>
                 * 
                 * 
                 */
                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                    "etabDiffuseur",
                    "abesDiffuseur",
                    "ccsd",
                    "autresEtabDiffuseurs",
                    "oai"
                })
                public static class Diffusion {

                    @XmlElement(namespace = "", required = true)
                    protected XmlData.StarGestion.Traitements.Sorties.Diffusion.EtabDiffuseur etabDiffuseur;
                    @XmlElement(namespace = "", required = true)
                    protected XmlData.StarGestion.Traitements.Sorties.Diffusion.AbesDiffuseur abesDiffuseur;
                    @XmlElement(namespace = "", required = true)
                    protected XmlData.StarGestion.Traitements.Sorties.Diffusion.Ccsd ccsd;
                    @XmlElement(namespace = "", required = true)
                    protected XmlData.StarGestion.Traitements.Sorties.Diffusion.AutresEtabDiffuseurs autresEtabDiffuseurs;
                    @XmlElement(namespace = "", required = true)
                    protected XmlData.StarGestion.Traitements.Sorties.Diffusion.Oai oai;
                    @XmlAttribute(name = "conformitePolDiffusion", required = true)
                    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                    @XmlSchemaType(name = "NCName")
                    protected String conformitePolDiffusion;
                    @XmlAttribute(name = "restrictionTemporelleFin", required = true)
                    @XmlSchemaType(name = "anySimpleType")
                    protected String restrictionTemporelleFin;
                    @XmlAttribute(name = "restrictionTemporelleType", required = true)
                    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                    @XmlSchemaType(name = "NCName")
                    protected String restrictionTemporelleType;
                    @XmlAttribute(name = "typeDiffusion", required = true)
                    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                    @XmlSchemaType(name = "NCName")
                    protected String typeDiffusion;
                    @XmlAttribute(name = "urlPerenne", required = true)
                    @XmlSchemaType(name = "anyURI")
                    protected String urlPerenne;

                    /**
                     * Obtient la valeur de la propri�t� etabDiffuseur.
                     * 
                     * @return
                     *     possible object is
                     *     {@link XmlData.StarGestion.Traitements.Sorties.Diffusion.EtabDiffuseur }
                     *     
                     */
                    public XmlData.StarGestion.Traitements.Sorties.Diffusion.EtabDiffuseur getEtabDiffuseur() {
                        return etabDiffuseur;
                    }

                    /**
                     * D�finit la valeur de la propri�t� etabDiffuseur.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link XmlData.StarGestion.Traitements.Sorties.Diffusion.EtabDiffuseur }
                     *     
                     */
                    public void setEtabDiffuseur(XmlData.StarGestion.Traitements.Sorties.Diffusion.EtabDiffuseur value) {
                        this.etabDiffuseur = value;
                    }

                    /**
                     * Obtient la valeur de la propri�t� abesDiffuseur.
                     * 
                     * @return
                     *     possible object is
                     *     {@link XmlData.StarGestion.Traitements.Sorties.Diffusion.AbesDiffuseur }
                     *     
                     */
                    public XmlData.StarGestion.Traitements.Sorties.Diffusion.AbesDiffuseur getAbesDiffuseur() {
                        return abesDiffuseur;
                    }

                    /**
                     * D�finit la valeur de la propri�t� abesDiffuseur.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link XmlData.StarGestion.Traitements.Sorties.Diffusion.AbesDiffuseur }
                     *     
                     */
                    public void setAbesDiffuseur(XmlData.StarGestion.Traitements.Sorties.Diffusion.AbesDiffuseur value) {
                        this.abesDiffuseur = value;
                    }

                    /**
                     * Obtient la valeur de la propri�t� ccsd.
                     * 
                     * @return
                     *     possible object is
                     *     {@link XmlData.StarGestion.Traitements.Sorties.Diffusion.Ccsd }
                     *     
                     */
                    public XmlData.StarGestion.Traitements.Sorties.Diffusion.Ccsd getCcsd() {
                        return ccsd;
                    }

                    /**
                     * D�finit la valeur de la propri�t� ccsd.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link XmlData.StarGestion.Traitements.Sorties.Diffusion.Ccsd }
                     *     
                     */
                    public void setCcsd(XmlData.StarGestion.Traitements.Sorties.Diffusion.Ccsd value) {
                        this.ccsd = value;
                    }

                    /**
                     * Obtient la valeur de la propri�t� autresEtabDiffuseurs.
                     * 
                     * @return
                     *     possible object is
                     *     {@link XmlData.StarGestion.Traitements.Sorties.Diffusion.AutresEtabDiffuseurs }
                     *     
                     */
                    public XmlData.StarGestion.Traitements.Sorties.Diffusion.AutresEtabDiffuseurs getAutresEtabDiffuseurs() {
                        return autresEtabDiffuseurs;
                    }

                    /**
                     * D�finit la valeur de la propri�t� autresEtabDiffuseurs.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link XmlData.StarGestion.Traitements.Sorties.Diffusion.AutresEtabDiffuseurs }
                     *     
                     */
                    public void setAutresEtabDiffuseurs(XmlData.StarGestion.Traitements.Sorties.Diffusion.AutresEtabDiffuseurs value) {
                        this.autresEtabDiffuseurs = value;
                    }

                    /**
                     * Obtient la valeur de la propri�t� oai.
                     * 
                     * @return
                     *     possible object is
                     *     {@link XmlData.StarGestion.Traitements.Sorties.Diffusion.Oai }
                     *     
                     */
                    public XmlData.StarGestion.Traitements.Sorties.Diffusion.Oai getOai() {
                        return oai;
                    }

                    /**
                     * D�finit la valeur de la propri�t� oai.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link XmlData.StarGestion.Traitements.Sorties.Diffusion.Oai }
                     *     
                     */
                    public void setOai(XmlData.StarGestion.Traitements.Sorties.Diffusion.Oai value) {
                        this.oai = value;
                    }

                    /**
                     * Obtient la valeur de la propri�t� conformitePolDiffusion.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getConformitePolDiffusion() {
                        return conformitePolDiffusion;
                    }

                    /**
                     * D�finit la valeur de la propri�t� conformitePolDiffusion.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setConformitePolDiffusion(String value) {
                        this.conformitePolDiffusion = value;
                    }

                    /**
                     * Obtient la valeur de la propri�t� restrictionTemporelleFin.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getRestrictionTemporelleFin() {
                        return restrictionTemporelleFin;
                    }

                    /**
                     * D�finit la valeur de la propri�t� restrictionTemporelleFin.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setRestrictionTemporelleFin(String value) {
                        this.restrictionTemporelleFin = value;
                    }

                    /**
                     * Obtient la valeur de la propri�t� restrictionTemporelleType.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getRestrictionTemporelleType() {
                        return restrictionTemporelleType;
                    }

                    /**
                     * D�finit la valeur de la propri�t� restrictionTemporelleType.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setRestrictionTemporelleType(String value) {
                        this.restrictionTemporelleType = value;
                    }

                    /**
                     * Obtient la valeur de la propri�t� typeDiffusion.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getTypeDiffusion() {
                        return typeDiffusion;
                    }

                    /**
                     * D�finit la valeur de la propri�t� typeDiffusion.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setTypeDiffusion(String value) {
                        this.typeDiffusion = value;
                    }

                    /**
                     * Obtient la valeur de la propri�t� urlPerenne.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getUrlPerenne() {
                        return urlPerenne;
                    }

                    /**
                     * D�finit la valeur de la propri�t� urlPerenne.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setUrlPerenne(String value) {
                        this.urlPerenne = value;
                    }


                    /**
                     * <p>Classe Java pour anonymous complex type.
                     * 
                     * <p>Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette classe.
                     * 
                     * <pre>
                     * &lt;complexType>
                     *   &lt;complexContent>
                     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                     *       &lt;attribute name="FichEtabDiffAbesIntranet" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
                     *       &lt;attribute name="abesDiffuseurPolEtablissement" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
                     *       &lt;attribute name="dateAbesDiffuseur" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
                     *       &lt;attribute name="indicAbesDiffuseur" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
                     *       &lt;attribute name="majAbesDiffuseur" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
                     *       &lt;attribute name="urlAbesDiffuseur" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
                     *     &lt;/restriction>
                     *   &lt;/complexContent>
                     * &lt;/complexType>
                     * </pre>
                     * 
                     * 
                     */
                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "")
                    public static class AbesDiffuseur {

                        @XmlAttribute(name = "FichEtabDiffAbesIntranet", required = true)
                        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                        @XmlSchemaType(name = "NCName")
                        protected String fichEtabDiffAbesIntranet;
                        @XmlAttribute(name = "abesDiffuseurPolEtablissement", required = true)
                        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                        @XmlSchemaType(name = "NCName")
                        protected String abesDiffuseurPolEtablissement;
                        @XmlAttribute(name = "dateAbesDiffuseur", required = true)
                        @XmlSchemaType(name = "dateTime")
                        protected XMLGregorianCalendar dateAbesDiffuseur;
                        @XmlAttribute(name = "indicAbesDiffuseur", required = true)
                        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                        @XmlSchemaType(name = "NCName")
                        protected String indicAbesDiffuseur;
                        @XmlAttribute(name = "majAbesDiffuseur", required = true)
                        @XmlSchemaType(name = "dateTime")
                        protected XMLGregorianCalendar majAbesDiffuseur;
                        @XmlAttribute(name = "urlAbesDiffuseur", required = true)
                        @XmlSchemaType(name = "anySimpleType")
                        protected String urlAbesDiffuseur;

                        /**
                         * Obtient la valeur de la propri�t� fichEtabDiffAbesIntranet.
                         * 
                         * @return
                         *     possible object is
                         *     {@link String }
                         *     
                         */
                        public String getFichEtabDiffAbesIntranet() {
                            return fichEtabDiffAbesIntranet;
                        }

                        /**
                         * D�finit la valeur de la propri�t� fichEtabDiffAbesIntranet.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link String }
                         *     
                         */
                        public void setFichEtabDiffAbesIntranet(String value) {
                            this.fichEtabDiffAbesIntranet = value;
                        }

                        /**
                         * Obtient la valeur de la propri�t� abesDiffuseurPolEtablissement.
                         * 
                         * @return
                         *     possible object is
                         *     {@link String }
                         *     
                         */
                        public String getAbesDiffuseurPolEtablissement() {
                            return abesDiffuseurPolEtablissement;
                        }

                        /**
                         * D�finit la valeur de la propri�t� abesDiffuseurPolEtablissement.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link String }
                         *     
                         */
                        public void setAbesDiffuseurPolEtablissement(String value) {
                            this.abesDiffuseurPolEtablissement = value;
                        }

                        /**
                         * Obtient la valeur de la propri�t� dateAbesDiffuseur.
                         * 
                         * @return
                         *     possible object is
                         *     {@link XMLGregorianCalendar }
                         *     
                         */
                        public XMLGregorianCalendar getDateAbesDiffuseur() {
                            return dateAbesDiffuseur;
                        }

                        /**
                         * D�finit la valeur de la propri�t� dateAbesDiffuseur.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link XMLGregorianCalendar }
                         *     
                         */
                        public void setDateAbesDiffuseur(XMLGregorianCalendar value) {
                            this.dateAbesDiffuseur = value;
                        }

                        /**
                         * Obtient la valeur de la propri�t� indicAbesDiffuseur.
                         * 
                         * @return
                         *     possible object is
                         *     {@link String }
                         *     
                         */
                        public String getIndicAbesDiffuseur() {
                            return indicAbesDiffuseur;
                        }

                        /**
                         * D�finit la valeur de la propri�t� indicAbesDiffuseur.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link String }
                         *     
                         */
                        public void setIndicAbesDiffuseur(String value) {
                            this.indicAbesDiffuseur = value;
                        }

                        /**
                         * Obtient la valeur de la propri�t� majAbesDiffuseur.
                         * 
                         * @return
                         *     possible object is
                         *     {@link XMLGregorianCalendar }
                         *     
                         */
                        public XMLGregorianCalendar getMajAbesDiffuseur() {
                            return majAbesDiffuseur;
                        }

                        /**
                         * D�finit la valeur de la propri�t� majAbesDiffuseur.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link XMLGregorianCalendar }
                         *     
                         */
                        public void setMajAbesDiffuseur(XMLGregorianCalendar value) {
                            this.majAbesDiffuseur = value;
                        }

                        /**
                         * Obtient la valeur de la propri�t� urlAbesDiffuseur.
                         * 
                         * @return
                         *     possible object is
                         *     {@link String }
                         *     
                         */
                        public String getUrlAbesDiffuseur() {
                            return urlAbesDiffuseur;
                        }

                        /**
                         * D�finit la valeur de la propri�t� urlAbesDiffuseur.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link String }
                         *     
                         */
                        public void setUrlAbesDiffuseur(String value) {
                            this.urlAbesDiffuseur = value;
                        }

                    }


                    /**
                     * <p>Classe Java pour anonymous complex type.
                     * 
                     * <p>Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette classe.
                     * 
                     * <pre>
                     * &lt;complexType>
                     *   &lt;complexContent>
                     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                     *     &lt;/restriction>
                     *   &lt;/complexContent>
                     * &lt;/complexType>
                     * </pre>
                     * 
                     * 
                     */
                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "")
                    public static class AutresEtabDiffuseurs {


                    }


                    /**
                     * <p>Classe Java pour anonymous complex type.
                     * 
                     * <p>Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette classe.
                     * 
                     * <pre>
                     * &lt;complexType>
                     *   &lt;complexContent>
                     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                     *       &lt;attribute name="ccsdDiffuseurPolEtablissement" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
                     *       &lt;attribute name="dateCcsd" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
                     *       &lt;attribute name="identifiantCcsd" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
                     *       &lt;attribute name="indicCcsd" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
                     *       &lt;attribute name="majCcsd" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
                     *       &lt;attribute name="numVersion" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
                     *       &lt;attribute name="pwdCcsd" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
                     *       &lt;attribute name="trace" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
                     *       &lt;attribute name="urlCcsd" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
                     *     &lt;/restriction>
                     *   &lt;/complexContent>
                     * &lt;/complexType>
                     * </pre>
                     * 
                     * 
                     */
                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "")
                    public static class Ccsd {

                        @XmlAttribute(name = "ccsdDiffuseurPolEtablissement", required = true)
                        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                        @XmlSchemaType(name = "NCName")
                        protected String ccsdDiffuseurPolEtablissement;
                        @XmlAttribute(name = "dateCcsd", required = true)
                        @XmlSchemaType(name = "anySimpleType")
                        protected String dateCcsd;
                        @XmlAttribute(name = "identifiantCcsd", required = true)
                        @XmlSchemaType(name = "anySimpleType")
                        protected String identifiantCcsd;
                        @XmlAttribute(name = "indicCcsd", required = true)
                        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                        @XmlSchemaType(name = "NCName")
                        protected String indicCcsd;
                        @XmlAttribute(name = "majCcsd", required = true)
                        @XmlSchemaType(name = "anySimpleType")
                        protected String majCcsd;
                        @XmlAttribute(name = "numVersion", required = true)
                        @XmlSchemaType(name = "anySimpleType")
                        protected String numVersion;
                        @XmlAttribute(name = "pwdCcsd", required = true)
                        @XmlSchemaType(name = "anySimpleType")
                        protected String pwdCcsd;
                        @XmlAttribute(name = "trace", required = true)
                        @XmlSchemaType(name = "anySimpleType")
                        protected String trace;
                        @XmlAttribute(name = "urlCcsd", required = true)
                        @XmlSchemaType(name = "anySimpleType")
                        protected String urlCcsd;

                        /**
                         * Obtient la valeur de la propri�t� ccsdDiffuseurPolEtablissement.
                         * 
                         * @return
                         *     possible object is
                         *     {@link String }
                         *     
                         */
                        public String getCcsdDiffuseurPolEtablissement() {
                            return ccsdDiffuseurPolEtablissement;
                        }

                        /**
                         * D�finit la valeur de la propri�t� ccsdDiffuseurPolEtablissement.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link String }
                         *     
                         */
                        public void setCcsdDiffuseurPolEtablissement(String value) {
                            this.ccsdDiffuseurPolEtablissement = value;
                        }

                        /**
                         * Obtient la valeur de la propri�t� dateCcsd.
                         * 
                         * @return
                         *     possible object is
                         *     {@link String }
                         *     
                         */
                        public String getDateCcsd() {
                            return dateCcsd;
                        }

                        /**
                         * D�finit la valeur de la propri�t� dateCcsd.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link String }
                         *     
                         */
                        public void setDateCcsd(String value) {
                            this.dateCcsd = value;
                        }

                        /**
                         * Obtient la valeur de la propri�t� identifiantCcsd.
                         * 
                         * @return
                         *     possible object is
                         *     {@link String }
                         *     
                         */
                        public String getIdentifiantCcsd() {
                            return identifiantCcsd;
                        }

                        /**
                         * D�finit la valeur de la propri�t� identifiantCcsd.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link String }
                         *     
                         */
                        public void setIdentifiantCcsd(String value) {
                            this.identifiantCcsd = value;
                        }

                        /**
                         * Obtient la valeur de la propri�t� indicCcsd.
                         * 
                         * @return
                         *     possible object is
                         *     {@link String }
                         *     
                         */
                        public String getIndicCcsd() {
                            return indicCcsd;
                        }

                        /**
                         * D�finit la valeur de la propri�t� indicCcsd.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link String }
                         *     
                         */
                        public void setIndicCcsd(String value) {
                            this.indicCcsd = value;
                        }

                        /**
                         * Obtient la valeur de la propri�t� majCcsd.
                         * 
                         * @return
                         *     possible object is
                         *     {@link String }
                         *     
                         */
                        public String getMajCcsd() {
                            return majCcsd;
                        }

                        /**
                         * D�finit la valeur de la propri�t� majCcsd.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link String }
                         *     
                         */
                        public void setMajCcsd(String value) {
                            this.majCcsd = value;
                        }

                        /**
                         * Obtient la valeur de la propri�t� numVersion.
                         * 
                         * @return
                         *     possible object is
                         *     {@link String }
                         *     
                         */
                        public String getNumVersion() {
                            return numVersion;
                        }

                        /**
                         * D�finit la valeur de la propri�t� numVersion.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link String }
                         *     
                         */
                        public void setNumVersion(String value) {
                            this.numVersion = value;
                        }

                        /**
                         * Obtient la valeur de la propri�t� pwdCcsd.
                         * 
                         * @return
                         *     possible object is
                         *     {@link String }
                         *     
                         */
                        public String getPwdCcsd() {
                            return pwdCcsd;
                        }

                        /**
                         * D�finit la valeur de la propri�t� pwdCcsd.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link String }
                         *     
                         */
                        public void setPwdCcsd(String value) {
                            this.pwdCcsd = value;
                        }

                        /**
                         * Obtient la valeur de la propri�t� trace.
                         * 
                         * @return
                         *     possible object is
                         *     {@link String }
                         *     
                         */
                        public String getTrace() {
                            return trace;
                        }

                        /**
                         * D�finit la valeur de la propri�t� trace.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link String }
                         *     
                         */
                        public void setTrace(String value) {
                            this.trace = value;
                        }

                        /**
                         * Obtient la valeur de la propri�t� urlCcsd.
                         * 
                         * @return
                         *     possible object is
                         *     {@link String }
                         *     
                         */
                        public String getUrlCcsd() {
                            return urlCcsd;
                        }

                        /**
                         * D�finit la valeur de la propri�t� urlCcsd.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link String }
                         *     
                         */
                        public void setUrlCcsd(String value) {
                            this.urlCcsd = value;
                        }

                    }


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
                     *         &lt;element name="urlEtabDiffuseur" form="unqualified">
                     *           &lt;complexType>
                     *             &lt;complexContent>
                     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                     *                 &lt;attribute name="majUrlEtabDiffuseur" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
                     *               &lt;/restriction>
                     *             &lt;/complexContent>
                     *           &lt;/complexType>
                     *         &lt;/element>
                     *       &lt;/sequence>
                     *       &lt;attribute name="editeurScientifiqueLieu" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
                     *       &lt;attribute name="editeurScientifiqueNom" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
                     *       &lt;attribute name="etabDiffuseurPolEtablissement" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
                     *     &lt;/restriction>
                     *   &lt;/complexContent>
                     * &lt;/complexType>
                     * </pre>
                     * 
                     * 
                     */
                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "", propOrder = {
                        "urlEtabDiffuseur"
                    })
                    public static class EtabDiffuseur {

                        @XmlElement(namespace = "", required = true)
                        protected XmlData.StarGestion.Traitements.Sorties.Diffusion.EtabDiffuseur.UrlEtabDiffuseur urlEtabDiffuseur;
                        @XmlAttribute(name = "editeurScientifiqueLieu", required = true)
                        @XmlSchemaType(name = "anySimpleType")
                        protected String editeurScientifiqueLieu;
                        @XmlAttribute(name = "editeurScientifiqueNom", required = true)
                        @XmlSchemaType(name = "anySimpleType")
                        protected String editeurScientifiqueNom;
                        @XmlAttribute(name = "etabDiffuseurPolEtablissement", required = true)
                        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                        @XmlSchemaType(name = "NCName")
                        protected String etabDiffuseurPolEtablissement;

                        /**
                         * Obtient la valeur de la propri�t� urlEtabDiffuseur.
                         * 
                         * @return
                         *     possible object is
                         *     {@link XmlData.StarGestion.Traitements.Sorties.Diffusion.EtabDiffuseur.UrlEtabDiffuseur }
                         *     
                         */
                        public XmlData.StarGestion.Traitements.Sorties.Diffusion.EtabDiffuseur.UrlEtabDiffuseur getUrlEtabDiffuseur() {
                            return urlEtabDiffuseur;
                        }

                        /**
                         * D�finit la valeur de la propri�t� urlEtabDiffuseur.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link XmlData.StarGestion.Traitements.Sorties.Diffusion.EtabDiffuseur.UrlEtabDiffuseur }
                         *     
                         */
                        public void setUrlEtabDiffuseur(XmlData.StarGestion.Traitements.Sorties.Diffusion.EtabDiffuseur.UrlEtabDiffuseur value) {
                            this.urlEtabDiffuseur = value;
                        }

                        /**
                         * Obtient la valeur de la propri�t� editeurScientifiqueLieu.
                         * 
                         * @return
                         *     possible object is
                         *     {@link String }
                         *     
                         */
                        public String getEditeurScientifiqueLieu() {
                            return editeurScientifiqueLieu;
                        }

                        /**
                         * D�finit la valeur de la propri�t� editeurScientifiqueLieu.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link String }
                         *     
                         */
                        public void setEditeurScientifiqueLieu(String value) {
                            this.editeurScientifiqueLieu = value;
                        }

                        /**
                         * Obtient la valeur de la propri�t� editeurScientifiqueNom.
                         * 
                         * @return
                         *     possible object is
                         *     {@link String }
                         *     
                         */
                        public String getEditeurScientifiqueNom() {
                            return editeurScientifiqueNom;
                        }

                        /**
                         * D�finit la valeur de la propri�t� editeurScientifiqueNom.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link String }
                         *     
                         */
                        public void setEditeurScientifiqueNom(String value) {
                            this.editeurScientifiqueNom = value;
                        }

                        /**
                         * Obtient la valeur de la propri�t� etabDiffuseurPolEtablissement.
                         * 
                         * @return
                         *     possible object is
                         *     {@link String }
                         *     
                         */
                        public String getEtabDiffuseurPolEtablissement() {
                            return etabDiffuseurPolEtablissement;
                        }

                        /**
                         * D�finit la valeur de la propri�t� etabDiffuseurPolEtablissement.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link String }
                         *     
                         */
                        public void setEtabDiffuseurPolEtablissement(String value) {
                            this.etabDiffuseurPolEtablissement = value;
                        }


                        /**
                         * <p>Classe Java pour anonymous complex type.
                         * 
                         * <p>Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette classe.
                         * 
                         * <pre>
                         * &lt;complexType>
                         *   &lt;complexContent>
                         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                         *       &lt;attribute name="majUrlEtabDiffuseur" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
                         *     &lt;/restriction>
                         *   &lt;/complexContent>
                         * &lt;/complexType>
                         * </pre>
                         * 
                         * 
                         */
                        @XmlAccessorType(XmlAccessType.FIELD)
                        @XmlType(name = "")
                        public static class UrlEtabDiffuseur {

                            @XmlAttribute(name = "majUrlEtabDiffuseur", required = true)
                            @XmlSchemaType(name = "anySimpleType")
                            protected String majUrlEtabDiffuseur;

                            /**
                             * Obtient la valeur de la propri�t� majUrlEtabDiffuseur.
                             * 
                             * @return
                             *     possible object is
                             *     {@link String }
                             *     
                             */
                            public String getMajUrlEtabDiffuseur() {
                                return majUrlEtabDiffuseur;
                            }

                            /**
                             * D�finit la valeur de la propri�t� majUrlEtabDiffuseur.
                             * 
                             * @param value
                             *     allowed object is
                             *     {@link String }
                             *     
                             */
                            public void setMajUrlEtabDiffuseur(String value) {
                                this.majUrlEtabDiffuseur = value;
                            }

                        }

                    }


                    /**
                     * <p>Classe Java pour anonymous complex type.
                     * 
                     * <p>Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette classe.
                     * 
                     * <pre>
                     * &lt;complexType>
                     *   &lt;complexContent>
                     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                     *       &lt;attribute name="dateOai" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
                     *       &lt;attribute name="indicOai" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
                     *       &lt;attribute name="majOai" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
                     *       &lt;attribute name="trace" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
                     *     &lt;/restriction>
                     *   &lt;/complexContent>
                     * &lt;/complexType>
                     * </pre>
                     * 
                     * 
                     */
                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "")
                    public static class Oai {

                        @XmlAttribute(name = "dateOai", required = true)
                        @XmlSchemaType(name = "dateTime")
                        protected XMLGregorianCalendar dateOai;
                        @XmlAttribute(name = "indicOai", required = true)
                        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                        @XmlSchemaType(name = "NCName")
                        protected String indicOai;
                        @XmlAttribute(name = "majOai", required = true)
                        @XmlSchemaType(name = "dateTime")
                        protected XMLGregorianCalendar majOai;
                        @XmlAttribute(name = "trace", required = true)
                        @XmlSchemaType(name = "anySimpleType")
                        protected String trace;

                        /**
                         * Obtient la valeur de la propri�t� dateOai.
                         * 
                         * @return
                         *     possible object is
                         *     {@link XMLGregorianCalendar }
                         *     
                         */
                        public XMLGregorianCalendar getDateOai() {
                            return dateOai;
                        }

                        /**
                         * D�finit la valeur de la propri�t� dateOai.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link XMLGregorianCalendar }
                         *     
                         */
                        public void setDateOai(XMLGregorianCalendar value) {
                            this.dateOai = value;
                        }

                        /**
                         * Obtient la valeur de la propri�t� indicOai.
                         * 
                         * @return
                         *     possible object is
                         *     {@link String }
                         *     
                         */
                        public String getIndicOai() {
                            return indicOai;
                        }

                        /**
                         * D�finit la valeur de la propri�t� indicOai.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link String }
                         *     
                         */
                        public void setIndicOai(String value) {
                            this.indicOai = value;
                        }

                        /**
                         * Obtient la valeur de la propri�t� majOai.
                         * 
                         * @return
                         *     possible object is
                         *     {@link XMLGregorianCalendar }
                         *     
                         */
                        public XMLGregorianCalendar getMajOai() {
                            return majOai;
                        }

                        /**
                         * D�finit la valeur de la propri�t� majOai.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link XMLGregorianCalendar }
                         *     
                         */
                        public void setMajOai(XMLGregorianCalendar value) {
                            this.majOai = value;
                        }

                        /**
                         * Obtient la valeur de la propri�t� trace.
                         * 
                         * @return
                         *     possible object is
                         *     {@link String }
                         *     
                         */
                        public String getTrace() {
                            return trace;
                        }

                        /**
                         * D�finit la valeur de la propri�t� trace.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link String }
                         *     
                         */
                        public void setTrace(String value) {
                            this.trace = value;
                        }

                    }

                }


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
                 *         &lt;element name="RCR" form="unqualified">
                 *           &lt;complexType>
                 *             &lt;complexContent>
                 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *                 &lt;attribute name="code" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
                 *               &lt;/restriction>
                 *             &lt;/complexContent>
                 *           &lt;/complexType>
                 *         &lt;/element>
                 *         &lt;element name="EPN" form="unqualified">
                 *           &lt;complexType>
                 *             &lt;complexContent>
                 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                 *               &lt;/restriction>
                 *             &lt;/complexContent>
                 *           &lt;/complexType>
                 *         &lt;/element>
                 *       &lt;/sequence>
                 *       &lt;attribute name="PPN" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
                 *       &lt;attribute name="dateSudoc" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
                 *       &lt;attribute name="indicSudoc" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
                 *       &lt;attribute name="majSudoc" use="required" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
                 *       &lt;attribute name="trace" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
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
                    "epn"
                })
                public static class Sudoc {

                    @XmlElement(name = "RCR", namespace = "", required = true)
                    protected XmlData.StarGestion.Traitements.Sorties.Sudoc.RCR rcr;
                    @XmlElement(name = "EPN", namespace = "", required = true)
                    protected XmlData.StarGestion.Traitements.Sorties.Sudoc.EPN epn;
                    @XmlAttribute(name = "PPN", required = true)
                    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                    @XmlSchemaType(name = "NMTOKEN")
                    protected String ppn;
                    @XmlAttribute(name = "dateSudoc", required = true)
                    @XmlSchemaType(name = "dateTime")
                    protected XMLGregorianCalendar dateSudoc;
                    @XmlAttribute(name = "indicSudoc", required = true)
                    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                    @XmlSchemaType(name = "NCName")
                    protected String indicSudoc;
                    @XmlAttribute(name = "majSudoc", required = true)
                    @XmlSchemaType(name = "dateTime")
                    protected XMLGregorianCalendar majSudoc;
                    @XmlAttribute(name = "trace", required = true)
                    @XmlSchemaType(name = "anySimpleType")
                    protected String trace;

                    /**
                     * Obtient la valeur de la propri�t� rcr.
                     * 
                     * @return
                     *     possible object is
                     *     {@link XmlData.StarGestion.Traitements.Sorties.Sudoc.RCR }
                     *     
                     */
                    public XmlData.StarGestion.Traitements.Sorties.Sudoc.RCR getRCR() {
                        return rcr;
                    }

                    /**
                     * D�finit la valeur de la propri�t� rcr.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link XmlData.StarGestion.Traitements.Sorties.Sudoc.RCR }
                     *     
                     */
                    public void setRCR(XmlData.StarGestion.Traitements.Sorties.Sudoc.RCR value) {
                        this.rcr = value;
                    }

                    /**
                     * Obtient la valeur de la propri�t� epn.
                     * 
                     * @return
                     *     possible object is
                     *     {@link XmlData.StarGestion.Traitements.Sorties.Sudoc.EPN }
                     *     
                     */
                    public XmlData.StarGestion.Traitements.Sorties.Sudoc.EPN getEPN() {
                        return epn;
                    }

                    /**
                     * D�finit la valeur de la propri�t� epn.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link XmlData.StarGestion.Traitements.Sorties.Sudoc.EPN }
                     *     
                     */
                    public void setEPN(XmlData.StarGestion.Traitements.Sorties.Sudoc.EPN value) {
                        this.epn = value;
                    }

                    /**
                     * Obtient la valeur de la propri�t� ppn.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getPPN() {
                        return ppn;
                    }

                    /**
                     * D�finit la valeur de la propri�t� ppn.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setPPN(String value) {
                        this.ppn = value;
                    }

                    /**
                     * Obtient la valeur de la propri�t� dateSudoc.
                     * 
                     * @return
                     *     possible object is
                     *     {@link XMLGregorianCalendar }
                     *     
                     */
                    public XMLGregorianCalendar getDateSudoc() {
                        return dateSudoc;
                    }

                    /**
                     * D�finit la valeur de la propri�t� dateSudoc.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link XMLGregorianCalendar }
                     *     
                     */
                    public void setDateSudoc(XMLGregorianCalendar value) {
                        this.dateSudoc = value;
                    }

                    /**
                     * Obtient la valeur de la propri�t� indicSudoc.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getIndicSudoc() {
                        return indicSudoc;
                    }

                    /**
                     * D�finit la valeur de la propri�t� indicSudoc.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setIndicSudoc(String value) {
                        this.indicSudoc = value;
                    }

                    /**
                     * Obtient la valeur de la propri�t� majSudoc.
                     * 
                     * @return
                     *     possible object is
                     *     {@link XMLGregorianCalendar }
                     *     
                     */
                    public XMLGregorianCalendar getMajSudoc() {
                        return majSudoc;
                    }

                    /**
                     * D�finit la valeur de la propri�t� majSudoc.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link XMLGregorianCalendar }
                     *     
                     */
                    public void setMajSudoc(XMLGregorianCalendar value) {
                        this.majSudoc = value;
                    }

                    /**
                     * Obtient la valeur de la propri�t� trace.
                     * 
                     * @return
                     *     possible object is
                     *     {@link String }
                     *     
                     */
                    public String getTrace() {
                        return trace;
                    }

                    /**
                     * D�finit la valeur de la propri�t� trace.
                     * 
                     * @param value
                     *     allowed object is
                     *     {@link String }
                     *     
                     */
                    public void setTrace(String value) {
                        this.trace = value;
                    }


                    /**
                     * <p>Classe Java pour anonymous complex type.
                     * 
                     * <p>Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette classe.
                     * 
                     * <pre>
                     * &lt;complexType>
                     *   &lt;complexContent>
                     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                     *     &lt;/restriction>
                     *   &lt;/complexContent>
                     * &lt;/complexType>
                     * </pre>
                     * 
                     * 
                     */
                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "")
                    public static class EPN {


                    }


                    /**
                     * <p>Classe Java pour anonymous complex type.
                     * 
                     * <p>Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette classe.
                     * 
                     * <pre>
                     * &lt;complexType>
                     *   &lt;complexContent>
                     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
                     *       &lt;attribute name="code" use="required" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
                     *     &lt;/restriction>
                     *   &lt;/complexContent>
                     * &lt;/complexType>
                     * </pre>
                     * 
                     * 
                     */
                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "", propOrder = {
                        "content"
                    })
                    public static class RCR {

                        @XmlValue
                        protected String content;
                        @XmlAttribute(name = "code", required = true)
                        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
                        @XmlSchemaType(name = "NMTOKEN")
                        protected String code;

                        /**
                         * Obtient la valeur de la propri�t� content.
                         * 
                         * @return
                         *     possible object is
                         *     {@link String }
                         *     
                         */
                        public String getContent() {
                            return content;
                        }

                        /**
                         * D�finit la valeur de la propri�t� content.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link String }
                         *     
                         */
                        public void setContent(String value) {
                            this.content = value;
                        }

                        /**
                         * Obtient la valeur de la propri�t� code.
                         * 
                         * @return
                         *     possible object is
                         *     {@link String }
                         *     
                         */
                        public String getCode() {
                            return code;
                        }

                        /**
                         * D�finit la valeur de la propri�t� code.
                         * 
                         * @param value
                         *     allowed object is
                         *     {@link String }
                         *     
                         */
                        public void setCode(String value) {
                            this.code = value;
                        }

                    }

                }

            }


            /**
             * <p>Classe Java pour anonymous complex type.
             * 
             * <p>Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette classe.
             * 
             * <pre>
             * &lt;complexType>
             *   &lt;complexContent>
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       &lt;attribute name="agentImport" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
             *       &lt;attribute name="date" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
             *       &lt;attribute name="idStep" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "")
            public static class Step {

                @XmlAttribute(name = "agentImport", required = true)
                @XmlSchemaType(name = "anySimpleType")
                protected String agentImport;
                @XmlAttribute(name = "date", required = true)
                @XmlSchemaType(name = "anySimpleType")
                protected String date;
                @XmlAttribute(name = "idStep", required = true)
                @XmlSchemaType(name = "anySimpleType")
                protected String idStep;

                /**
                 * Obtient la valeur de la propri�t� agentImport.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getAgentImport() {
                    return agentImport;
                }

                /**
                 * D�finit la valeur de la propri�t� agentImport.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setAgentImport(String value) {
                    this.agentImport = value;
                }

                /**
                 * Obtient la valeur de la propri�t� date.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getDate() {
                    return date;
                }

                /**
                 * D�finit la valeur de la propri�t� date.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setDate(String value) {
                    this.date = value;
                }

                /**
                 * Obtient la valeur de la propri�t� idStep.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getIdStep() {
                    return idStep;
                }

                /**
                 * D�finit la valeur de la propri�t� idStep.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setIdStep(String value) {
                    this.idStep = value;
                }

            }

        }

    }


    /**
     * <p>Classe Java pour anonymous complex type.
     * 
     * <p>Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class StepGestion {


    }

}

//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.8-b130911.1802 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2023.03.09 à 03:40:55 PM CET 
//


package fr.abes.theses_batch_indexation.model.oaisets;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the fr.abes.theses_batch_indexation.model.oaisets package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SetName_QNAME = new QName("", "setName");
    private final static QName _SetSpec_QNAME = new QName("", "setSpec");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: fr.abes.theses_batch_indexation.model.oaisets
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ListSets }
     * 
     */
    public ListSets createListSets() {
        return new ListSets();
    }

    /**
     * Create an instance of {@link Set }
     * 
     */
    public Set createSet() {
        return new Set();
    }

    /**
     * Create an instance of {@link SetDescription }
     * 
     */
    public SetDescription createSetDescription() {
        return new SetDescription();
    }

    /**
     * Create an instance of {@link Dc }
     * 
     */
    public Dc createDc() {
        return new Dc();
    }

    /**
     * Create an instance of {@link Description }
     * 
     */
    public Description createDescription() {
        return new Description();
    }

    /**
     * Create an instance of {@link ResumptionToken }
     * 
     */
    public ResumptionToken createResumptionToken() {
        return new ResumptionToken();
    }

    /**
     * Create an instance of {@link OAIPMH }
     * 
     */
    public OAIPMH createOAIPMH() {
        return new OAIPMH();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "setName")
    public JAXBElement<String> createSetName(String value) {
        return new JAXBElement<String>(_SetName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "setSpec")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createSetSpec(String value) {
        return new JAXBElement<String>(_SetSpec_QNAME, String.class, null, value);
    }

}

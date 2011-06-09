
package org.hupo.psi.ms.traml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Container for the HUPO PSI TraML format for encoding selected reaction monitoring transitions and other target lists
 * 
 * <p>Java class for TraMLType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TraMLType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cvList" type="{http://psi.hupo.org/ms/traml}cvListType"/>
 *         &lt;element name="SourceFileList" type="{http://psi.hupo.org/ms/traml}SourceFileListType" minOccurs="0"/>
 *         &lt;element name="ContactList" type="{http://psi.hupo.org/ms/traml}ContactListType" minOccurs="0"/>
 *         &lt;element name="PublicationList" type="{http://psi.hupo.org/ms/traml}PublicationListType" minOccurs="0"/>
 *         &lt;element name="InstrumentList" type="{http://psi.hupo.org/ms/traml}InstrumentListType" minOccurs="0"/>
 *         &lt;element name="SoftwareList" type="{http://psi.hupo.org/ms/traml}SoftwareListType" minOccurs="0"/>
 *         &lt;element name="ProteinList" type="{http://psi.hupo.org/ms/traml}ProteinListType" minOccurs="0"/>
 *         &lt;element name="CompoundList" type="{http://psi.hupo.org/ms/traml}CompoundListType" minOccurs="0"/>
 *         &lt;element name="TransitionList" type="{http://psi.hupo.org/ms/traml}TransitionListType" minOccurs="0"/>
 *         &lt;element name="TargetList" type="{http://psi.hupo.org/ms/traml}TargetListType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TraMLType", namespace = "http://psi.hupo.org/ms/traml", propOrder = {
    "cvList",
    "sourceFileList",
    "contactList",
    "publicationList",
    "instrumentList",
    "softwareList",
    "proteinList",
    "compoundList",
    "transitionList",
    "targetList"
})
public class TraMLType {

    @XmlElement(namespace = "http://psi.hupo.org/ms/traml", required = true)
    protected CvListType cvList;
    @XmlElement(name = "SourceFileList", namespace = "http://psi.hupo.org/ms/traml")
    protected SourceFileListType sourceFileList;
    @XmlElement(name = "ContactList", namespace = "http://psi.hupo.org/ms/traml")
    protected ContactListType contactList;
    @XmlElement(name = "PublicationList", namespace = "http://psi.hupo.org/ms/traml")
    protected PublicationListType publicationList;
    @XmlElement(name = "InstrumentList", namespace = "http://psi.hupo.org/ms/traml")
    protected InstrumentListType instrumentList;
    @XmlElement(name = "SoftwareList", namespace = "http://psi.hupo.org/ms/traml")
    protected SoftwareListType softwareList;
    @XmlElement(name = "ProteinList", namespace = "http://psi.hupo.org/ms/traml")
    protected ProteinListType proteinList;
    @XmlElement(name = "CompoundList", namespace = "http://psi.hupo.org/ms/traml")
    protected CompoundListType compoundList;
    @XmlElement(name = "TransitionList", namespace = "http://psi.hupo.org/ms/traml")
    protected TransitionListType transitionList;
    @XmlElement(name = "TargetList", namespace = "http://psi.hupo.org/ms/traml")
    protected TargetListType targetList;
    @XmlAttribute
    protected String id;
    @XmlAttribute(required = true)
    protected String version;

    /**
     * Gets the value of the cvList property.
     * 
     * @return
     *     possible object is
     *     {@link CvListType }
     *     
     */
    public CvListType getCvList() {
        return cvList;
    }

    /**
     * Sets the value of the cvList property.
     * 
     * @param value
     *     allowed object is
     *     {@link CvListType }
     *     
     */
    public void setCvList(CvListType value) {
        this.cvList = value;
    }

    /**
     * Gets the value of the sourceFileList property.
     * 
     * @return
     *     possible object is
     *     {@link SourceFileListType }
     *     
     */
    public SourceFileListType getSourceFileList() {
        return sourceFileList;
    }

    /**
     * Sets the value of the sourceFileList property.
     * 
     * @param value
     *     allowed object is
     *     {@link SourceFileListType }
     *     
     */
    public void setSourceFileList(SourceFileListType value) {
        this.sourceFileList = value;
    }

    /**
     * Gets the value of the contactList property.
     * 
     * @return
     *     possible object is
     *     {@link ContactListType }
     *     
     */
    public ContactListType getContactList() {
        return contactList;
    }

    /**
     * Sets the value of the contactList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContactListType }
     *     
     */
    public void setContactList(ContactListType value) {
        this.contactList = value;
    }

    /**
     * Gets the value of the publicationList property.
     * 
     * @return
     *     possible object is
     *     {@link PublicationListType }
     *     
     */
    public PublicationListType getPublicationList() {
        return publicationList;
    }

    /**
     * Sets the value of the publicationList property.
     * 
     * @param value
     *     allowed object is
     *     {@link PublicationListType }
     *     
     */
    public void setPublicationList(PublicationListType value) {
        this.publicationList = value;
    }

    /**
     * Gets the value of the instrumentList property.
     * 
     * @return
     *     possible object is
     *     {@link InstrumentListType }
     *     
     */
    public InstrumentListType getInstrumentList() {
        return instrumentList;
    }

    /**
     * Sets the value of the instrumentList property.
     * 
     * @param value
     *     allowed object is
     *     {@link InstrumentListType }
     *     
     */
    public void setInstrumentList(InstrumentListType value) {
        this.instrumentList = value;
    }

    /**
     * Gets the value of the softwareList property.
     * 
     * @return
     *     possible object is
     *     {@link SoftwareListType }
     *     
     */
    public SoftwareListType getSoftwareList() {
        return softwareList;
    }

    /**
     * Sets the value of the softwareList property.
     * 
     * @param value
     *     allowed object is
     *     {@link SoftwareListType }
     *     
     */
    public void setSoftwareList(SoftwareListType value) {
        this.softwareList = value;
    }

    /**
     * Gets the value of the proteinList property.
     * 
     * @return
     *     possible object is
     *     {@link ProteinListType }
     *     
     */
    public ProteinListType getProteinList() {
        return proteinList;
    }

    /**
     * Sets the value of the proteinList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProteinListType }
     *     
     */
    public void setProteinList(ProteinListType value) {
        this.proteinList = value;
    }

    /**
     * Gets the value of the compoundList property.
     * 
     * @return
     *     possible object is
     *     {@link CompoundListType }
     *     
     */
    public CompoundListType getCompoundList() {
        return compoundList;
    }

    /**
     * Sets the value of the compoundList property.
     * 
     * @param value
     *     allowed object is
     *     {@link CompoundListType }
     *     
     */
    public void setCompoundList(CompoundListType value) {
        this.compoundList = value;
    }

    /**
     * Gets the value of the transitionList property.
     * 
     * @return
     *     possible object is
     *     {@link TransitionListType }
     *     
     */
    public TransitionListType getTransitionList() {
        return transitionList;
    }

    /**
     * Sets the value of the transitionList property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransitionListType }
     *     
     */
    public void setTransitionList(TransitionListType value) {
        this.transitionList = value;
    }

    /**
     * Gets the value of the targetList property.
     * 
     * @return
     *     possible object is
     *     {@link TargetListType }
     *     
     */
    public TargetListType getTargetList() {
        return targetList;
    }

    /**
     * Sets the value of the targetList property.
     * 
     * @param value
     *     allowed object is
     *     {@link TargetListType }
     *     
     */
    public void setTargetList(TargetListType value) {
        this.targetList = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

}

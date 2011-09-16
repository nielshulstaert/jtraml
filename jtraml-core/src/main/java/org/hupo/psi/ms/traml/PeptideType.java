
package org.hupo.psi.ms.traml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * Peptide for which one or more transitions are intended to identify
 * 
 * <p>Java class for PeptideType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PeptideType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cvParam" type="{http://psi.hupo.org/ms/traml}cvParamType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ProteinRef" type="{http://psi.hupo.org/ms/traml}ProteinRefType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Modification" type="{http://psi.hupo.org/ms/traml}ModificationType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="RetentionTimeList" type="{http://psi.hupo.org/ms/traml}RetentionTimeListType" minOccurs="0"/>
 *         &lt;element name="Evidence" type="{http://psi.hupo.org/ms/traml}EvidenceType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *       &lt;attribute name="sequence" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PeptideType", namespace = "http://psi.hupo.org/ms/traml", propOrder = {
    "cvParam",
    "proteinRef",
    "modification",
    "retentionTimeList",
    "evidence"
})
public class PeptideType {

    @XmlElement(namespace = "http://psi.hupo.org/ms/traml")
    protected List<CvParamType> cvParam;
    @XmlElement(name = "ProteinRef", namespace = "http://psi.hupo.org/ms/traml")
    protected List<ProteinRefType> proteinRef;
    @XmlElement(name = "Modification", namespace = "http://psi.hupo.org/ms/traml")
    protected List<ModificationType> modification;
    @XmlElement(name = "RetentionTimeList", namespace = "http://psi.hupo.org/ms/traml")
    protected RetentionTimeListType retentionTimeList;
    @XmlElement(name = "Evidence", namespace = "http://psi.hupo.org/ms/traml")
    protected EvidenceType evidence;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;
    @XmlAttribute(required = true)
    protected String sequence;

    /**
     * Gets the value of the cvParam property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cvParam property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCvParam().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CvParamType }
     * 
     * 
     */
    public List<CvParamType> getCvParam() {
        if (cvParam == null) {
            cvParam = new ArrayList<CvParamType>();
        }
        return this.cvParam;
    }

    /**
     * Gets the value of the proteinRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the proteinRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProteinRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProteinRefType }
     * 
     * 
     */
    public List<ProteinRefType> getProteinRef() {
        if (proteinRef == null) {
            proteinRef = new ArrayList<ProteinRefType>();
        }
        return this.proteinRef;
    }

    /**
     * Gets the value of the modification property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the modification property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getModification().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ModificationType }
     * 
     * 
     */
    public List<ModificationType> getModification() {
        if (modification == null) {
            modification = new ArrayList<ModificationType>();
        }
        return this.modification;
    }

    /**
     * Gets the value of the retentionTimeList property.
     * 
     * @return
     *     possible object is
     *     {@link RetentionTimeListType }
     *     
     */
    public RetentionTimeListType getRetentionTimeList() {
        return retentionTimeList;
    }

    /**
     * Sets the value of the retentionTimeList property.
     * 
     * @param value
     *     allowed object is
     *     {@link RetentionTimeListType }
     *     
     */
    public void setRetentionTimeList(RetentionTimeListType value) {
        this.retentionTimeList = value;
    }

    /**
     * Gets the value of the evidence property.
     * 
     * @return
     *     possible object is
     *     {@link EvidenceType }
     *     
     */
    public EvidenceType getEvidence() {
        return evidence;
    }

    /**
     * Sets the value of the evidence property.
     * 
     * @param value
     *     allowed object is
     *     {@link EvidenceType }
     *     
     */
    public void setEvidence(EvidenceType value) {
        this.evidence = value;
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
     * Gets the value of the sequence property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSequence() {
        return sequence;
    }

    /**
     * Sets the value of the sequence property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSequence(String value) {
        this.sequence = value;
    }

}

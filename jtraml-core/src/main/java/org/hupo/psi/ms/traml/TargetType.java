
package org.hupo.psi.ms.traml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * A peptide or compound that is to be included or excluded from a target list of precursor m/z values.
 * 
 * <p>Java class for TargetType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TargetType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Precursor" type="{http://psi.hupo.org/ms/traml}PrecursorType"/>
 *         &lt;element name="RetentionTime" type="{http://psi.hupo.org/ms/traml}RetentionTimeType" minOccurs="0"/>
 *         &lt;element name="ConfigurationList" type="{http://psi.hupo.org/ms/traml}ConfigurationListType" minOccurs="0"/>
 *         &lt;element name="cvParam" type="{http://psi.hupo.org/ms/traml}cvParamType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="peptideRef" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *       &lt;attribute name="compoundRef" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TargetType", namespace = "http://psi.hupo.org/ms/traml", propOrder = {
    "precursor",
    "retentionTime",
    "configurationList",
    "cvParam"
})
public class TargetType {

    @XmlElement(name = "Precursor", namespace = "http://psi.hupo.org/ms/traml", required = true)
    protected PrecursorType precursor;
    @XmlElement(name = "RetentionTime", namespace = "http://psi.hupo.org/ms/traml")
    protected RetentionTimeType retentionTime;
    @XmlElement(name = "ConfigurationList", namespace = "http://psi.hupo.org/ms/traml")
    protected ConfigurationListType configurationList;
    @XmlElement(namespace = "http://psi.hupo.org/ms/traml")
    protected List<CvParamType> cvParam;
    @XmlAttribute(required = true)
    protected String id;
    @XmlAttribute
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object peptideRef;
    @XmlAttribute
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object compoundRef;

    /**
     * Gets the value of the precursor property.
     * 
     * @return
     *     possible object is
     *     {@link PrecursorType }
     *     
     */
    public PrecursorType getPrecursor() {
        return precursor;
    }

    /**
     * Sets the value of the precursor property.
     * 
     * @param value
     *     allowed object is
     *     {@link PrecursorType }
     *     
     */
    public void setPrecursor(PrecursorType value) {
        this.precursor = value;
    }

    /**
     * Gets the value of the retentionTime property.
     * 
     * @return
     *     possible object is
     *     {@link RetentionTimeType }
     *     
     */
    public RetentionTimeType getRetentionTime() {
        return retentionTime;
    }

    /**
     * Sets the value of the retentionTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link RetentionTimeType }
     *     
     */
    public void setRetentionTime(RetentionTimeType value) {
        this.retentionTime = value;
    }

    /**
     * Gets the value of the configurationList property.
     * 
     * @return
     *     possible object is
     *     {@link ConfigurationListType }
     *     
     */
    public ConfigurationListType getConfigurationList() {
        return configurationList;
    }

    /**
     * Sets the value of the configurationList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConfigurationListType }
     *     
     */
    public void setConfigurationList(ConfigurationListType value) {
        this.configurationList = value;
    }

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
     * Gets the value of the peptideRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getPeptideRef() {
        return peptideRef;
    }

    /**
     * Sets the value of the peptideRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setPeptideRef(Object value) {
        this.peptideRef = value;
    }

    /**
     * Gets the value of the compoundRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getCompoundRef() {
        return compoundRef;
    }

    /**
     * Sets the value of the compoundRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setCompoundRef(Object value) {
        this.compoundRef = value;
    }

}

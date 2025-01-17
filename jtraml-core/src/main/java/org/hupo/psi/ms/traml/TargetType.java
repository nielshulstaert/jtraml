//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.02.09 at 02:09:45 PM CET 
//


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
 * &lt;complexType name="TargetType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Precursor" type="{http://psi.hupo.org/ms/traml}PrecursorType"/&gt;
 *         &lt;element name="RetentionTime" type="{http://psi.hupo.org/ms/traml}RetentionTimeType" minOccurs="0"/&gt;
 *         &lt;element name="ConfigurationList" type="{http://psi.hupo.org/ms/traml}ConfigurationListType" minOccurs="0"/&gt;
 *         &lt;element name="cvParam" type="{http://psi.hupo.org/ms/traml}cvParamType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="userParam" type="{http://psi.hupo.org/ms/traml}UserParamType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="peptideRef" type="{http://www.w3.org/2001/XMLSchema}IDREF" /&gt;
 *       &lt;attribute name="compoundRef" type="{http://www.w3.org/2001/XMLSchema}IDREF" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TargetType", propOrder = {
    "precursor",
    "retentionTime",
    "configurationList",
    "cvParam",
    "userParam"
})
public class TargetType {

    @XmlElement(name = "Precursor", required = true)
    protected PrecursorType precursor;
    @XmlElement(name = "RetentionTime")
    protected RetentionTimeType retentionTime;
    @XmlElement(name = "ConfigurationList")
    protected ConfigurationListType configurationList;
    protected List<CvParamType> cvParam;
    protected List<UserParamType> userParam;
    @XmlAttribute(name = "id", required = true)
    protected String id;
    @XmlAttribute(name = "peptideRef")
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object peptideRef;
    @XmlAttribute(name = "compoundRef")
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
     * Gets the value of the userParam property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the userParam property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUserParam().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UserParamType }
     * 
     * 
     */
    public List<UserParamType> getUserParam() {
        if (userParam == null) {
            userParam = new ArrayList<UserParamType>();
        }
        return this.userParam;
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

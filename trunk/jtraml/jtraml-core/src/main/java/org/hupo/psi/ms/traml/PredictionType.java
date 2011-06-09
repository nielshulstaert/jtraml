
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
 * Information about a prediction for a suitable transition using some software
 * 
 * <p>Java class for PredictionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PredictionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cvParam" type="{http://psi.hupo.org/ms/traml}cvParamType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="softwareRef" use="required" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *       &lt;attribute name="contactRef" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PredictionType", namespace = "http://psi.hupo.org/ms/traml", propOrder = {
    "cvParam"
})
public class PredictionType {

    @XmlElement(namespace = "http://psi.hupo.org/ms/traml")
    protected List<CvParamType> cvParam;
    @XmlAttribute(required = true)
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object softwareRef;
    @XmlAttribute
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object contactRef;

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
     * Gets the value of the softwareRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getSoftwareRef() {
        return softwareRef;
    }

    /**
     * Sets the value of the softwareRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setSoftwareRef(Object value) {
        this.softwareRef = value;
    }

    /**
     * Gets the value of the contactRef property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getContactRef() {
        return contactRef;
    }

    /**
     * Sets the value of the contactRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setContactRef(Object value) {
        this.contactRef = value;
    }

}


package org.hupo.psi.ms.traml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Product (Q3) of the transition
 * 
 * <p>Java class for ProductType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ProductType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cvParam" type="{http://psi.hupo.org/ms/traml}cvParamType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="InterpretationList" type="{http://psi.hupo.org/ms/traml}InterpretationListType" minOccurs="0"/>
 *         &lt;element name="ConfigurationList" type="{http://psi.hupo.org/ms/traml}ConfigurationListType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProductType", namespace = "http://psi.hupo.org/ms/traml", propOrder = {
    "cvParam",
    "interpretationList",
    "configurationList"
})
public class ProductType {

    @XmlElement(namespace = "http://psi.hupo.org/ms/traml")
    protected List<CvParamType> cvParam;
    @XmlElement(name = "InterpretationList", namespace = "http://psi.hupo.org/ms/traml")
    protected InterpretationListType interpretationList;
    @XmlElement(name = "ConfigurationList", namespace = "http://psi.hupo.org/ms/traml")
    protected ConfigurationListType configurationList;

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
     * Gets the value of the interpretationList property.
     * 
     * @return
     *     possible object is
     *     {@link InterpretationListType }
     *     
     */
    public InterpretationListType getInterpretationList() {
        return interpretationList;
    }

    /**
     * Sets the value of the interpretationList property.
     * 
     * @param value
     *     allowed object is
     *     {@link InterpretationListType }
     *     
     */
    public void setInterpretationList(InterpretationListType value) {
        this.interpretationList = value;
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

}

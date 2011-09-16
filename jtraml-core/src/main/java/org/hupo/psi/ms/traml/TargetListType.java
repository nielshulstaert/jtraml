
package org.hupo.psi.ms.traml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * List of precursor m/z targets to include or exclude
 * 
 * <p>Java class for TargetListType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TargetListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cvParam" type="{http://psi.hupo.org/ms/traml}cvParamType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="TargetIncludeList" type="{http://psi.hupo.org/ms/traml}TargetIncludeListType" minOccurs="0"/>
 *         &lt;element name="TargetExcludeList" type="{http://psi.hupo.org/ms/traml}TargetExcludeListType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TargetListType", namespace = "http://psi.hupo.org/ms/traml", propOrder = {
    "cvParam",
    "targetIncludeList",
    "targetExcludeList"
})
public class TargetListType {

    @XmlElement(namespace = "http://psi.hupo.org/ms/traml")
    protected List<CvParamType> cvParam;
    @XmlElement(name = "TargetIncludeList", namespace = "http://psi.hupo.org/ms/traml")
    protected TargetIncludeListType targetIncludeList;
    @XmlElement(name = "TargetExcludeList", namespace = "http://psi.hupo.org/ms/traml")
    protected TargetExcludeListType targetExcludeList;

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
     * Gets the value of the targetIncludeList property.
     * 
     * @return
     *     possible object is
     *     {@link TargetIncludeListType }
     *     
     */
    public TargetIncludeListType getTargetIncludeList() {
        return targetIncludeList;
    }

    /**
     * Sets the value of the targetIncludeList property.
     * 
     * @param value
     *     allowed object is
     *     {@link TargetIncludeListType }
     *     
     */
    public void setTargetIncludeList(TargetIncludeListType value) {
        this.targetIncludeList = value;
    }

    /**
     * Gets the value of the targetExcludeList property.
     * 
     * @return
     *     possible object is
     *     {@link TargetExcludeListType }
     *     
     */
    public TargetExcludeListType getTargetExcludeList() {
        return targetExcludeList;
    }

    /**
     * Sets the value of the targetExcludeList property.
     * 
     * @param value
     *     allowed object is
     *     {@link TargetExcludeListType }
     *     
     */
    public void setTargetExcludeList(TargetExcludeListType value) {
        this.targetExcludeList = value;
    }

}

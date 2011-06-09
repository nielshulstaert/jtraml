
package org.hupo.psi.ms.traml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * List of software packages used in the generation of one of more transitions described in the document
 * 
 * <p>Java class for SoftwareListType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SoftwareListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Software" type="{http://psi.hupo.org/ms/traml}SoftwareType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SoftwareListType", namespace = "http://psi.hupo.org/ms/traml", propOrder = {
    "software"
})
public class SoftwareListType {

    @XmlElement(name = "Software", namespace = "http://psi.hupo.org/ms/traml", required = true)
    protected List<SoftwareType> software;

    /**
     * Gets the value of the software property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the software property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSoftware().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SoftwareType }
     * 
     * 
     */
    public List<SoftwareType> getSoftware() {
        if (software == null) {
            software = new ArrayList<SoftwareType>();
        }
        return this.software;
    }

}

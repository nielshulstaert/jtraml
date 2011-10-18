
package org.hupo.psi.ms.traml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * List of retention time information entries
 * 
 * <p>Java class for RetentionTimeListType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RetentionTimeListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RetentionTime" type="{http://psi.hupo.org/ms/traml}RetentionTimeType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RetentionTimeListType", namespace = "http://psi.hupo.org/ms/traml", propOrder = {
    "retentionTime"
})
public class RetentionTimeListType {

    @XmlElement(name = "RetentionTime", namespace = "http://psi.hupo.org/ms/traml", required = true)
    protected List<RetentionTimeType> retentionTime;

    /**
     * Gets the value of the retentionTime property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the retentionTime property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRetentionTime().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RetentionTimeType }
     * 
     * 
     */
    public List<RetentionTimeType> getRetentionTime() {
        if (retentionTime == null) {
            retentionTime = new ArrayList<RetentionTimeType>();
        }
        return this.retentionTime;
    }

}

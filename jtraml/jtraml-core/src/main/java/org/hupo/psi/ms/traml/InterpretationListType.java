
package org.hupo.psi.ms.traml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * List of possible interprations of fragment ions for a transition
 * 
 * <p>Java class for InterpretationListType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InterpretationListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Interpretation" type="{http://psi.hupo.org/ms/traml}InterpretationType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InterpretationListType", namespace = "http://psi.hupo.org/ms/traml", propOrder = {
    "interpretation"
})
public class InterpretationListType {

    @XmlElement(name = "Interpretation", namespace = "http://psi.hupo.org/ms/traml", required = true)
    protected List<InterpretationType> interpretation;

    /**
     * Gets the value of the interpretation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the interpretation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInterpretation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InterpretationType }
     * 
     * 
     */
    public List<InterpretationType> getInterpretation() {
        if (interpretation == null) {
            interpretation = new ArrayList<InterpretationType>();
        }
        return this.interpretation;
    }

}

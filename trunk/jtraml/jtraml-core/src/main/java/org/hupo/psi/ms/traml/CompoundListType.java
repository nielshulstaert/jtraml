
package org.hupo.psi.ms.traml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * List of compounds (including peptides) for which one or more transitions are intended to identify
 * 
 * <p>Java class for CompoundListType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CompoundListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cvParam" type="{http://psi.hupo.org/ms/traml}cvParamType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Peptide" type="{http://psi.hupo.org/ms/traml}PeptideType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Compound" type="{http://psi.hupo.org/ms/traml}CompoundType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CompoundListType", namespace = "http://psi.hupo.org/ms/traml", propOrder = {
    "cvParam",
    "peptide",
    "compound"
})
public class CompoundListType {

    @XmlElement(namespace = "http://psi.hupo.org/ms/traml")
    protected List<CvParamType> cvParam;
    @XmlElement(name = "Peptide", namespace = "http://psi.hupo.org/ms/traml")
    protected List<PeptideType> peptide;
    @XmlElement(name = "Compound", namespace = "http://psi.hupo.org/ms/traml")
    protected List<CompoundType> compound;

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
     * Gets the value of the peptide property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the peptide property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPeptide().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PeptideType }
     * 
     * 
     */
    public List<PeptideType> getPeptide() {
        if (peptide == null) {
            peptide = new ArrayList<PeptideType>();
        }
        return this.peptide;
    }

    /**
     * Gets the value of the compound property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the compound property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCompound().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CompoundType }
     * 
     * 
     */
    public List<CompoundType> getCompound() {
        if (compound == null) {
            compound = new ArrayList<CompoundType>();
        }
        return this.compound;
    }

}

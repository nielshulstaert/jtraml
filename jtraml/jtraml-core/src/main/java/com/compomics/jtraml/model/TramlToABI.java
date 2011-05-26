package com.compomics.jtraml.model;

import com.compomics.jtraml.enumeration.FrequentOBoEnum;
import com.compomics.jtraml.interfaces.TSVFileExportModel;
import org.hupo.psi.ms.traml.*;

import java.util.List;

/**
 * This class is a FileExportModel to write separated file format of transitions similar to the ABI QTRAP 5500.
 */
public class TramlToABI implements TSVFileExportModel {

    /**
     * Returns whether this export model has a header.
     *
     * @return
     */
    public boolean hasHeader() {
        return false;
    }

    /**
     * Returns whether the header of this export model.
     *
     * @return
     */
    public String getHeader() {
        return "";
    }

    /**
     * Returns the separator character associated to this filemodel.
     *
     * @return
     */
    public char getSeparator() {
        return ',';
    }

    /**
     * Parse a TransitionType instance into a single line compatible with the ABI QTRAP 5500
     *
     * @param aTransitionType
     * @param aTraMLType
     * @return * e.g.;
     *         returns
     *         <i>564.9618,663.4081,10,LSTADPADASTIYAVVV.O95866.O95866-3.O95866-5.3y6,29.3</i>
     *         <br>from
     *         <nu>
     *         <li >564.9618 = precursor mass
     *         <li >663.4081 = fragment mass
     *         <li >10 = ???
     *         <li >LSTADPADASTIYAVVV.O95866.O95866-3.O95866-5.3y6 = peptide sequence, protein accession a,b,c, precursor charge, fragment y6
     *         <li >29.3 = retention time
     *         </nu>
     */
    public String parseTransitionType(TransitionType aTransitionType, TraMLType aTraMLType) {

        String lQ1 = "NA";
        String lQ3 = "NA";
        String lEnergy = "NA";
        String lID = "NA";
        String lRt = "NA";

        // Set the identifier.
        lID = aTransitionType.getId();

        // Set precursor mass.
        CvParamType lPrecursor = aTransitionType.getPrecursor().getCvParam().get(0);
        lQ1 = lPrecursor.getValue();

        // Set product mass.
        CvParamType lProduct = aTransitionType.getProduct().getCvParam().get(0);
        lQ3 = lProduct.getValue();

        // Get the peptide instance of the current transition.
        PeptideType lPeptideType = (PeptideType) aTransitionType.getPeptideRef();

        // Get the retention time.
        List<RetentionTimeType> lRetentionTime = lPeptideType.getRetentionTimeList().getRetentionTime();
        for (RetentionTimeType aLRetentionTime : lRetentionTime) {
            CvParamType lCvParamType = aLRetentionTime.getCvParam().get(0);
            if (lCvParamType.getName().equals(FrequentOBoEnum.RETENTION_TIME.getName())) {
                lRt = lCvParamType.getValue();
            }
        }

        // Get the configuration options.
        List<CvParamType> ConfigurationList = aTransitionType.getConfigurationList().getConfiguration().getCvParam();
        for (CvParamType lCvParamType : ConfigurationList) {
            // cvparam on energy?
            if (lCvParamType.getName().equals(FrequentOBoEnum.COLLISION_ENERGY.getName())) {
                lEnergy = lCvParamType.getValue();
            }
        }

        StringBuffer sb = new StringBuffer();

        sb.append(lQ1);
        sb.append(getSeparator());

        sb.append(lQ3);
        sb.append(getSeparator());

        // @TODO what does this parameter mean?
        sb.append(lEnergy);
        sb.append(getSeparator());

        sb.append(lID);
        sb.append(getSeparator());

        sb.append(lRt);

        return sb.toString();

    }
}

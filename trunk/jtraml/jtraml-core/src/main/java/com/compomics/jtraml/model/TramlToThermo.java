package com.compomics.jtraml.model;

import com.compomics.jtraml.enumeration.FrequentOBoEnum;
import com.compomics.jtraml.interfaces.TSVFileExportModel;
import org.hupo.psi.ms.traml.*;

import java.util.List;

/**
 * This class is aFileExportModel to write separated file format of transitions similar to the Thermo TSQ.
 */
public class TramlToThermo implements TSVFileExportModel {

    /**
     * Returns whether this export model has a header.
     *
     * @return
     */
    public boolean hasHeader() {
        return true;
    }

    /**
     * Returns whether the header of this export model.
     *
     * @return
     */
    public String getHeader() {
        return "Q1,Q3,CE,Start time (min),Stop time (min),Polarity,Trigger,Reaction category,Name";
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
     * @return * e.g.
     *         ""
     *         Q1,Q3,CE,Start time (min),Stop time (min),Polarity,Trigger,Reaction category,Name
     *         651.8366,790.4038,25.5,18.61,28.61,1,1.00E+04,0,AAELQTGLETNR.2y7-1
     */
    public String parseTransitionType(TransitionType aTransitionType, TraMLType aTraMLType) {
        String lQ1 = "NA";
        String lQ3 = "NA";

        String lEnergy = "NA";

        String lStartTime = "NA";
        String lStopTime = "NA";

        String lPolarity = "NA";
        String lTrigger = "NA";

        String lReactionCategory = "NA";

        String lID = "NA";

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
        for (RetentionTimeType lRetentionTimeType : lRetentionTime) {
            List<CvParamType> lCvParams = lRetentionTimeType.getCvParam();
            for (CvParamType lCvParamType : lCvParams) {
                if (lCvParamType.getName().equals(FrequentOBoEnum.RETENTION_TIME_LOWER.getName())) {
                    lStartTime = lCvParamType.getValue();
                } else if (lCvParamType.getName().equals(FrequentOBoEnum.RETENTION_TIME_UPPER.getName())) {
                    lStopTime = lCvParamType.getValue();
                }
            }
        }

        // Get the configuration options.
        List<ConfigurationType> ConfigurationList = aTransitionType.getProduct().getConfigurationList().getConfiguration();
        for (ConfigurationType lConfigurationType : ConfigurationList) {
            List<CvParamType> lCvParam = lConfigurationType.getCvParam();

            for (CvParamType lCvParamType : lCvParam) {
                // cvparam on energy?
                if (lCvParamType.getName().equals(FrequentOBoEnum.COLLISION_ENERGY.getName())) {
                    lEnergy = lCvParamType.getValue();
                }
            }
        }

        StringBuffer sb = new StringBuffer();

        // 1
        sb.append(lQ1);
        sb.append(getSeparator());

        // 2
        sb.append(lQ3);
        sb.append(getSeparator());

        // 3
        sb.append(lEnergy);
        sb.append(getSeparator());

        //4
        sb.append(lStartTime);
        sb.append(getSeparator());

        // 5
        sb.append(lStopTime);
        sb.append(getSeparator());

        // 6
        sb.append(lPolarity);
        sb.append(getSeparator());

        // 7
        sb.append(lTrigger);
        sb.append(getSeparator());

        // 8
        sb.append(lReactionCategory);
        sb.append(getSeparator());

        // 9
        sb.append(lID);

        return sb.toString();

    }
}

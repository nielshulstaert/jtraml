package com.compomics.jtraml.model;

import com.compomics.jtraml.MessageBean;
import com.compomics.jtraml.RetentionTimeEvaluation;
import com.compomics.jtraml.enumeration.FrequentOBoEnum;
import com.compomics.jtraml.interfaces.TSVFileExportModel;
import org.hupo.psi.ms.traml.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * This class is a FileExportModel to write separated file format of transitions similar to the ABI QTRAP 5500.
 */
public class TramlToABI extends TSVFileExportModel {

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
     * Returns whether the given TransitionType is convertable.
     * The ABI export separated file format expects the collision energy, as well as a centroid
     *
     * @return
     */
    public boolean isConvertable(TransitionType aTransitionType, TraMLType aTraMLType) {

        RetentionTimeEvaluation lEvaluation = new RetentionTimeEvaluation(aTransitionType, aTraMLType);

        if (lEvaluation.hasRt()) {
            // Ok!
            return true;
        } else if (lEvaluation.hasRtLower()) {
            // Ok, we are trying to make assumptions now.
            iMessageBean = new MessageBean("The Centroid Retention Time is missing.\nThe lower retention time will be used instead.", false);
            return false;
        } else {
            iMessageBean = new MessageBean("The required retention times have not been found.\nAll retention times will be 'NA'", false);
            return false;
        }
    }

    /**
     * Returns the conversion Message if needed. Null if the TransitionType is convertable.
     *
     * @return
     */
    public MessageBean getConversionMessage() {
        return iMessageBean;
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

        lRt = new RetentionTimeParser(aTransitionType, aTraMLType, lRt, lPeptideType).invoke();

        if (boolShiftRetentionTime) { // Do we need to shift the retention time?
            Double d = Double.parseDouble(lRt);
            d = d + iRetentionTimeShift;
            BigDecimal bd = new BigDecimal(d);
            bd = bd.setScale(4, RoundingMode.HALF_UP);
            lRt = bd.toString();
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

    /**
     * The ABI export model does not need the retention time delta.
     */
    public void setRetentionTimeDelta(double aRetentionTimeDelta) {
        //
    }


    /**
     * This private class can parse retention time information specific to the ABI output format.
     */
    private class RetentionTimeParser {
        private TransitionType iTransitionType;
        private TraMLType iTraMLType;
        private String iRt;
        private PeptideType iPeptideType;

        public RetentionTimeParser(TransitionType aTransitionType, TraMLType aTraMLType, String aRt, PeptideType aPeptideType) {
            iTransitionType = aTransitionType;
            iTraMLType = aTraMLType;
            iRt = aRt;
            iPeptideType = aPeptideType;
        }

        public String invoke() {
            // Get the retention time.
            RetentionTimeEvaluation lEvaluation = new RetentionTimeEvaluation(iTransitionType, iTraMLType);

            if (lEvaluation.hasRt()) {
                // Ok!
                List<RetentionTimeType> lRetentionTime = iPeptideType.getRetentionTimeList().getRetentionTime();
                for (RetentionTimeType aLRetentionTime : lRetentionTime) {
                    CvParamType lCvParamType = aLRetentionTime.getCvParam().get(0);
                    if (lCvParamType.getName().equals(FrequentOBoEnum.RETENTION_TIME.getName())) {
                        iRt = lCvParamType.getValue();
                    }
                }

            } else if (lEvaluation.hasRtLower()) {
                // Ok, use the lower retention time as the centroid time.
                List<RetentionTimeType> lRetentionTime = iPeptideType.getRetentionTimeList().getRetentionTime();
                for (RetentionTimeType aLRetentionTime : lRetentionTime) {
                    CvParamType lCvParamType = aLRetentionTime.getCvParam().get(0);
                    if (lCvParamType.getName().equals(FrequentOBoEnum.RETENTION_TIME_LOWER.getName())) {
                        iRt = lCvParamType.getValue();
                    }
                }

            } else {
                // Leave lRt as 'NA'.
            }
            return iRt;
        }
    }
}

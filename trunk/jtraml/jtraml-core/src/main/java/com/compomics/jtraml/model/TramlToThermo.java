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
 * This class is aFileExportModel to write separated file format of transitions similar to the Thermo TSQ.
 */
public class TramlToThermo extends TSVFileExportModel {


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
     * Returns whether the given TransitionType is convertable.
     * The ABI export separated file format expects the collision energy, as well as a centroid
     *
     * @return
     */
    public boolean isConvertable(TransitionType aTransitionType, TraMLType aTraMLType) {

        RetentionTimeEvaluation lEvaluation = new RetentionTimeEvaluation(aTransitionType, aTraMLType);

        if (lEvaluation.hasRtLower() && lEvaluation.hasRtUpper()) {
            // Ok!
            return true;
        } else if (lEvaluation.hasRt() && lEvaluation.hasRtDelta()) {
            // We are missing the Delta RetentionTime!
            iMessageBean = new MessageBean("No lower and upper retention times are found.\nYet, a centroid retention time and a window have been found that will be used as lower and upper retentiontimes.", false);
            return false;
        } else if (lEvaluation.hasRt() && !lEvaluation.hasRtDelta()) {
            // We are missing the Delta RetentionTime!
            iMessageBean = new MessageBean("No lower and upper retention times are found.\nYet, a centroid retention time has been found. Please provide a window time to create the required lower and upper retention times.", true);
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
        // The Thermo format requires a lower and upper time.


        RetentionTimeParser retentionTimeParser = new RetentionTimeParser(aTransitionType, aTraMLType, lStartTime, lStopTime, lPeptideType).invoke();
        lStartTime = retentionTimeParser.getStartTime();
        lStopTime = retentionTimeParser.getStopTime();


        if (boolShiftRetentionTime) { // Do we need to shift the retention time?
            // Modify the start time.
            Double d = Double.parseDouble(lStartTime);
            d = d + iRetentionTimeShift;
            BigDecimal bd = new BigDecimal(d);
            bd = bd.setScale(4, RoundingMode.HALF_UP);
            lStartTime = bd.toString();

            // Modify the stop time.
            d = Double.parseDouble(lStopTime);
            d = d + iRetentionTimeShift;
            bd = new BigDecimal(d);
            bd = bd.setScale(4, RoundingMode.HALF_UP);
            lStopTime = bd.toString();
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

    /**
     * Add a constant CVParamType to be used by the ExportType
     */

    public void setRetentionTimeDelta(double aRetentionTimeDelta) {
        iRetentionTimeWindow = aRetentionTimeDelta;
    }


    /**
     * This private class can parse retention time information specific to the Thermo output format.
     */
    private class RetentionTimeParser {
        private TransitionType iTransitionType;
        private TraMLType iTraMLType;
        private String iStartTime;
        private String iStopTime;
        private PeptideType iPeptideType;


        public RetentionTimeParser(TransitionType aTransitionType, TraMLType aTraMLType, String aStartTime, String aStopTime, PeptideType aPeptideType) {
            iTransitionType = aTransitionType;
            iTraMLType = aTraMLType;
            iStartTime = aStartTime;
            iStopTime = aStopTime;
            iPeptideType = aPeptideType;
        }

        public String getStartTime() {
            return iStartTime;
        }

        public String getStopTime() {
            return iStopTime;
        }

        public RetentionTimeParser invoke() {
            RetentionTimeEvaluation lEvaluation = new RetentionTimeEvaluation(iTransitionType, iTraMLType);

            if (lEvaluation.hasRtLower() && lEvaluation.hasRtUpper()) {
                // Ok!
                List<RetentionTimeType> lRetentionTime = iPeptideType.getRetentionTimeList().getRetentionTime();
                for (RetentionTimeType lRetentionTimeType : lRetentionTime) {
                    List<CvParamType> lCvParams = lRetentionTimeType.getCvParam();
                    for (CvParamType lCvParamType : lCvParams) {
                        if (lCvParamType.getName().equals(FrequentOBoEnum.RETENTION_TIME_LOWER.getName())) {
                            iStartTime = lCvParamType.getValue();
                        } else if (lCvParamType.getName().equals(FrequentOBoEnum.RETENTION_TIME_UPPER.getName())) {
                            iStopTime = lCvParamType.getValue();
                        }
                    }
                }


            } else if (lEvaluation.hasRt() && lEvaluation.hasRtDelta()) {
                // Calculate it from the retention time and the delta retention time!
                double lRt = 0;
                double lRtDelta = 0;

                List<RetentionTimeType> lRetentionTime = iPeptideType.getRetentionTimeList().getRetentionTime();
                for (RetentionTimeType lRetentionTimeType : lRetentionTime) {
                    List<CvParamType> lCvParams = lRetentionTimeType.getCvParam();
                    for (CvParamType lCvParamType : lCvParams) {
                        if (lCvParamType.getName().equals(FrequentOBoEnum.RETENTION_TIME.getName())) {
                            lRt = Double.parseDouble(lCvParamType.getValue());
                        } else if (lCvParamType.getName().equals(FrequentOBoEnum.RETENTION_TIME_WINDOW.getName())) {
                            lRtDelta = Double.parseDouble(lCvParamType.getValue());
                        }
                    }
                }

                iStartTime = "" + (lRt - lRtDelta);
                iStopTime = "" + (lRt + lRtDelta);

            } else if (lEvaluation.hasRt() && !lEvaluation.hasRtDelta()) {
                // Calculate it from the retention time and the user-specified delta retention time!

                if (iRetentionTimeWindow != Double.MAX_VALUE) {
                    // The window has been defined!
                    double lRt = 0;
                    double lRtDelta = 0;

                    List<RetentionTimeType> lRetentionTime = iPeptideType.getRetentionTimeList().getRetentionTime();
                    for (RetentionTimeType lRetentionTimeType : lRetentionTime) {
                        List<CvParamType> lCvParams = lRetentionTimeType.getCvParam();
                        for (CvParamType lCvParamType : lCvParams) {
                            if (lCvParamType.getName().equals(FrequentOBoEnum.RETENTION_TIME.getName())) {
                                lRt = Double.parseDouble(lCvParamType.getValue());
                            }
                        }
                    }
                    iStartTime = "" + (lRt - iRetentionTimeWindow);
                    iStopTime = "" + (lRt + iRetentionTimeWindow);
                }

            } else {
                // Leave lRt and lRtdelta as 'NA'.
            }
            return this;
        }
    }
}

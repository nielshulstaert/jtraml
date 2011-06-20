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
 * This class is aFileExportModel to write separated file format of transitions similar to the Agilent QQQ.
 */
public class TramlToAgilent implements TSVFileExportModel {


    /**
     * This MessageBean keeps track of the
     */
    private MessageBean iMessageBean = null;

    /**
     * This double represents the delta retention time window for the Agilent export model.
     */
    private double iRetentionTimeWindow = Double.MAX_VALUE;

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
        return "Dynamic MRM\n" +
                "Compound Name\tISTD?\tPrecursor Ion\tMS1 Res\tProduct Ion\tMS2 Res\tFragmentor\tCollision Energy\tCell Accelerator Voltage\tRet Time (min)\tDelta Ret Time\tPolarity";
    }

    /**
     * Returns whether the given TransitionType is convertable.
     * The ABI export separated file format expects the collision energy, as well as a centroid
     *
     * @return
     */
    public boolean isConvertable(TransitionType aTransitionType, TraMLType aTraMLType) {

        RetentionTimeEvaluation lEvaluation = new RetentionTimeEvaluation(aTransitionType, aTraMLType);

        if (lEvaluation.hasRt() && lEvaluation.hasRtDelta()) {
            // Ok!
            return true;
        } else if (lEvaluation.hasRt() && !lEvaluation.hasRtDelta()) {
            // We are missing the Delta RetentionTime!
            iMessageBean = new MessageBean("The window around the centroid retention time is missing.\nPlease return the required window:", true);
            return false;
        } else if (lEvaluation.hasRtLower() && lEvaluation.hasRtUpper()) {
            // We are missing the Delta RetentionTime!
            iMessageBean = new MessageBean("No centroid retention time is found.\nYet, a lower and upper retention time have been found and will be averaged and windowed.", false);
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
        return null;
    }

    /**
     * Returns the separator character associated to this filemodel.
     *
     * @return
     */
    public char getSeparator() {
        return '\t';
    }

    /**
     * Parse a TransitionType instance into a single line compatible with the ABI QTRAP 5500
     *
     * @param aTransitionType
     * @param aTraMLType
     * @return * e.g.
     *         ""
     *         Dynamic MRM
     *         Compound Name	ISTD?	Precursor Ion	MS1 Res	Product Ion	MS2 Res	Fragmentor	Collision Energy	Cell Accelerator Voltage	Ret Time (min)	Delta Ret Time	Polarity
     *         CSASVLPVDVQTLNSSGPPFGK.2y16-1	FALSE	1130.5681	Wide	1642.8233	Unit	125	39.8	5	42.35	5.00	Positive
     */
    public String parseTransitionType(TransitionType aTransitionType, TraMLType aTraMLType) {
        String lID = "NA";

        String ISTD = "NA";// todo

        String lQ1 = "NA";
        String lRes1 = "NA";// todo

        String lQ3 = "NA";
        String lRes3 = "NA";// todo

        String lFragmentor = "NA";// todo
        String lEnergy = "NA";
        String lAccVoltage = "NA";// todo

        String lRt = "NA";
        String lRtdelta = "NA";

        String lPolarity = "NA";


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
        RetentionTimeParser retentionTimeParser = new RetentionTimeParser(aTransitionType, aTraMLType, lRt, lRtdelta, lPeptideType).invoke();
        lRt = retentionTimeParser.getRt();
        lRtdelta = retentionTimeParser.getRtdelta();


        // Get the configuration options.
        List<ConfigurationType> ConfigurationList = aTransitionType.getProduct().getConfigurationList().getConfiguration();
        for (ConfigurationType lConfigurationType : ConfigurationList) {
            List<CvParamType> lCvParam = lConfigurationType.getCvParam();

            for (CvParamType lCvParamType : lCvParam) {
                // cvparam on energy?
                if (lCvParamType.getName().equals(FrequentOBoEnum.COLLISION_ENERGY.getName())) {
                    lEnergy = lCvParamType.getValue();
                } else if (lCvParamType.getName().equals(FrequentOBoEnum.ACCELERATING_VOLTAGE.getName())) {
                    lAccVoltage = lCvParamType.getValue();
                }
            }
        }


        StringBuffer sb = new StringBuffer();

        // 1
        sb.append(lID);
        sb.append(getSeparator());

        // 2
        sb.append(ISTD);
        sb.append(getSeparator());

        // 3
        sb.append(lQ1);
        sb.append(getSeparator());

        //4
        sb.append(lRes1);
        sb.append(getSeparator());

        // 5
        sb.append(lQ3);
        sb.append(getSeparator());

        // 6
        sb.append(lRes3);
        sb.append(getSeparator());

        // 7
        sb.append(lFragmentor);
        sb.append(getSeparator());

        // 8
        sb.append(lEnergy);
        sb.append(getSeparator());

        // 9
        sb.append(lAccVoltage);
        sb.append(getSeparator());

        // 10
        sb.append(lRt);
        sb.append(getSeparator());

        // 11
        sb.append(lRtdelta);
        sb.append(getSeparator());

        // 12
        sb.append(lPolarity);

        return sb.toString();

    }

    /**
     * Add a constant CVParamType to be used by the ExportType
     */
    public void setRetentionTimeDelta(double aRetentionTimeWindow) {
        iRetentionTimeWindow = aRetentionTimeWindow;
    }

    /**
     * This private class can parse retention time information specific to the Agilent output format.
     */
    private class RetentionTimeParser {
        private TransitionType iTransitionType;
        private TraMLType iTraMLType;
        private String iRt;
        private String iRtdelta;
        private PeptideType iPeptideType;

        public RetentionTimeParser(TransitionType aTransitionType, TraMLType aTraMLType, String aRt, String aRtdelta, PeptideType aPeptideType) {
            iTransitionType = aTransitionType;
            iTraMLType = aTraMLType;
            iRt = aRt;
            iRtdelta = aRtdelta;
            iPeptideType = aPeptideType;
        }

        public String getRt() {
            return iRt;
        }

        public String getRtdelta() {
            return iRtdelta;
        }

        public RetentionTimeParser invoke() {
            // Get the retention time.
            // Agilent needs a Centroid Retention Time and a Delta Retention Time.
            RetentionTimeEvaluation lEvaluation = new RetentionTimeEvaluation(iTransitionType, iTraMLType);

            if (lEvaluation.hasRt() && lEvaluation.hasRtDelta()) {
                // Ok!
                List<RetentionTimeType> lRetentionTime = iPeptideType.getRetentionTimeList().getRetentionTime();
                for (RetentionTimeType lRetentionTimeType : lRetentionTime) {
                    List<CvParamType> lCvParams = lRetentionTimeType.getCvParam();
                    for (CvParamType lCvParamType : lCvParams) {
                        if (lCvParamType.getName().equals(FrequentOBoEnum.RETENTION_TIME.getName())) {
                            iRt = lCvParamType.getValue();
                        } else if (lCvParamType.getName().equals(FrequentOBoEnum.RETENTION_TIME_WINDOW.getName())) {
                            iRtdelta = lCvParamType.getValue();
                        }
                    }
                }

            } else if (lEvaluation.hasRt() && !lEvaluation.hasRtDelta()) {
                // We miss the Delta!! Ask for it.
                if (iRetentionTimeWindow != Double.MAX_VALUE) {
                    // The window has been defined!

                    List<RetentionTimeType> lRetentionTime = iPeptideType.getRetentionTimeList().getRetentionTime();
                    for (RetentionTimeType lRetentionTimeType : lRetentionTime) {
                        List<CvParamType> lCvParams = lRetentionTimeType.getCvParam();
                        for (CvParamType lCvParamType : lCvParams) {
                            if (lCvParamType.getName().equals(FrequentOBoEnum.RETENTION_TIME.getName())) {
                                iRt = lCvParamType.getValue();
                            }
                        }
                    }

                    // We are missing the Delta RetentionTime, see if it was set by the user.
                    if (iRetentionTimeWindow != -1) {
                        iRtdelta = "" + iRetentionTimeWindow;
                    }
                }


            } else if (lEvaluation.hasRtLower() && lEvaluation.hasRtUpper()) {
                // We are missing the Delta RetentionTime!
                // But we do have the lower an the upper times.
                // Ok!
                double lRtUpper = 0;
                double lRtLower = 0;

                List<RetentionTimeType> lRetentionTime = iPeptideType.getRetentionTimeList().getRetentionTime();
                for (RetentionTimeType lRetentionTimeType : lRetentionTime) {
                    List<CvParamType> lCvParams = lRetentionTimeType.getCvParam();
                    for (CvParamType lCvParamType : lCvParams) {
                        if (lCvParamType.getName().equals(FrequentOBoEnum.RETENTION_TIME_LOWER.getName())) {
                            lRtLower = Double.parseDouble(lCvParamType.getValue());
                        } else if (lCvParamType.getName().equals(FrequentOBoEnum.RETENTION_TIME_WINDOW.getName())) {
                            lRtUpper = Double.parseDouble(lCvParamType.getValue());
                        }
                    }
                }


                BigDecimal lRtBD = new BigDecimal((lRtLower + lRtUpper) / 2).setScale(2, RoundingMode.HALF_UP);
                iRt = "" + lRtBD.toString();

                BigDecimal lRtDeltaBD = new BigDecimal((lRtLower - lRtUpper) / 2).setScale(2, RoundingMode.HALF_UP);
                iRtdelta = "" + lRtDeltaBD.toString();

            } else {
                // Leave lRt and lRtdelta as 'NA'.
            }
            return this;
        }
    }
}

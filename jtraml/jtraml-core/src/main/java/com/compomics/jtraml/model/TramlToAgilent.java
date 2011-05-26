package com.compomics.jtraml.model;

import com.compomics.jtraml.enumeration.FrequentOBoEnum;
import com.compomics.jtraml.interfaces.TSVFileExportModel;
import org.hupo.psi.ms.traml.*;

import java.util.List;

/**
 * This class is aFileExportModel to write separated file format of transitions similar to the Agilent QQQ.
 */
public class TramlToAgilent implements TSVFileExportModel {

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

        // Get the retention time.
        List<RetentionTimeType> lRetentionTime = lPeptideType.getRetentionTimeList().getRetentionTime();
        for (RetentionTimeType lRetentionTimeType : lRetentionTime) {
            List<CvParamType> lCvParams = lRetentionTimeType.getCvParam();
            for (CvParamType lCvParamType : lCvParams) {
                if (lCvParamType.getName().equals(FrequentOBoEnum.RETENTION_TIME.getName())) {
                    lRt = lCvParamType.getValue();
                } else if (lCvParamType.getName().equals(FrequentOBoEnum.RETENTION_TIME_WINDOW.getName())) {
                    lRtdelta = lCvParamType.getValue();
                }
            }
        }

        // Get the configuration options.
        List<CvParamType> ConfigurationList = aTransitionType.getConfigurationList().getConfiguration().getCvParam();
        for (CvParamType lCvParamType : ConfigurationList) {
            // cvparam on energy?
            if (lCvParamType.getName().equals(FrequentOBoEnum.COLLISION_ENERGY.getName())) {
                lEnergy = lCvParamType.getValue();
            }else if (lCvParamType.getName().equals(FrequentOBoEnum.ACCELERATING_VOLTAGE.getName())) {
                lAccVoltage = lCvParamType.getValue();
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
}

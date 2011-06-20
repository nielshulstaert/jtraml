package com.compomics.jtraml;

import com.compomics.jtraml.enumeration.FrequentOBoEnum;
import org.hupo.psi.ms.traml.*;

import java.util.List;

/**
 * This class is a
 */
public class RetentionTimeEvaluation {

    boolean iRt = false;
    boolean iRtDelta = false;
    boolean iRtLower = false;
    boolean iRtUpper = false;

    public RetentionTimeEvaluation(TransitionType aTransitionType, TraMLType aTraMLType) {
        // Get the peptide instance of the current transition.
        PeptideType lPeptideType = (PeptideType) aTransitionType.getPeptideRef();
        List<RetentionTimeType> lRetentionTimes = lPeptideType.getRetentionTimeList().getRetentionTime();

        // Get the retention time.
        for (RetentionTimeType lRetentionTimeType : lRetentionTimes) {
            List<CvParamType> lCvParams = lRetentionTimeType.getCvParam();
            for (CvParamType lCvParamType : lCvParams) {
                if (lCvParamType.getName().equals(FrequentOBoEnum.RETENTION_TIME.getName())) {
                    iRt = true;
                } else if (lCvParamType.getName().equals(FrequentOBoEnum.RETENTION_TIME_WINDOW.getName())) {
                    iRtDelta = true;
                } else if (lCvParamType.getName().equals(FrequentOBoEnum.RETENTION_TIME_LOWER.getName())) {
                    iRtLower = true;
                } else if (lCvParamType.getName().equals(FrequentOBoEnum.RETENTION_TIME_UPPER.getName())) {
                    iRtUpper = true;
                }
            }
        }
    }

    public boolean hasRt() {
        return iRt;
    }

    public boolean hasRtDelta() {
        return iRtDelta;
    }

    public boolean hasRtLower() {
        return iRtLower;
    }

    public boolean hasRtUpper() {
        return iRtUpper;
    }


}


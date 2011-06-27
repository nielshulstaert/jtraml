package com.compomics.jtraml.interfaces;

import org.hupo.psi.ms.traml.SourceFileListType;
import org.hupo.psi.ms.traml.TraMLType;

/**
 * This class is a generic model for a number of Transition specifying columns.
 */
public abstract class TSVFileImportModel {

    /**
     * This double represents the retention time shift that is added/subtracted to the retention time.
     */
    protected double iRetentionTimeShift = 0;

    /**
     * This boolean indicates if the retention time must be shifted.
     */
    protected boolean boolShiftRetentionTime = false;

    /**
     * This double represents the delta retention time window.
     */
    protected double iRetentionTimeWindow = Double.MAX_VALUE;


    protected TSVFileImportModel() {

    }

    /**
     * Implementing classes must be capable of writing an array of rowvalues into a TramlType instance.
     *
     * @param aTraMLType The TraMLType instance to store the rows into.
     * @param aRowValues The separates values from a single row.
     */
    public abstract void addRowToTraml(TraMLType aTraMLType, String[] aRowValues);

    /**
     * Implementing classes must be capable to describe a SourceFileListType to insert into a TraMLType
     *
     * @return SourceFileListType for the implementing converting classes.
     */
    public abstract SourceFileListType getSourceTypeList();

    /**
     * Returns the separator character associated to this filemodel.
     *
     * @return
     */
    public abstract char getSeparator();

    /**
     * Set a Retention Time Delta that can be used if needed.
     */
    public void setRetentionTimeDelta(double aRetentionTimeDelta) {
        iRetentionTimeWindow = aRetentionTimeDelta;
    }

    /**
     * Set a Retention Time Shift value that will always be used.
     */
    public void setRetentionTimeShift(double aRetentionTimeShift) {
        iRetentionTimeShift = aRetentionTimeShift;
    }

    /**
     * Set a Retention Time Shift value that will always be used.
     *
     * @return
     */
    public double getRetentionTimeShift() {
        return iRetentionTimeShift;
    }

    /**
     * Set whether the retention time will be shifted.
     * The shift must be specified in via the setter.
     *
     * @return
     */
    public void shiftRetentionTime(boolean aRetentionTimeShift) {
        boolShiftRetentionTime = aRetentionTimeShift;
    }

}



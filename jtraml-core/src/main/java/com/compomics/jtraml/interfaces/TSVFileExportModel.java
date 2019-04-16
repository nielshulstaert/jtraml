package com.compomics.jtraml.interfaces;

import com.compomics.jtraml.model.Constants;
import com.compomics.jtraml.MessageBean;
import org.hupo.psi.ms.traml.TraMLType;
import org.hupo.psi.ms.traml.TransitionType;

/**
 * This interface declares how separated file formats must be structured
 */
public abstract class TSVFileExportModel {

    /**
     * This MessageBean keeps track of the
     */
    protected MessageBean iMessageBean = null;

    /**
     * This double represents the delta retention time window.
     */
    protected double iRetentionTimeWindow = Double.MAX_VALUE;

    /**
     * This double represents the retention time shift that is added/subtracted to the retention time.
     */
    protected double iRetentionTimeShift = 0;

    /**
     * This boolean indicates if the retention time must be shifted.
     */
    protected boolean boolShiftRetentionTime = false;

    /**
     * The constants instance to be used by the model.
     */
    protected Constants iConstants = new Constants();


    public TSVFileExportModel() {
        // Do nothing.
    }

    /**
     * Returns whether this export model has a header.
     *
     * @return
     */
    public abstract boolean hasHeader();

    /**
     * Returns whether the header of this export model.
     *
     * @return
     */
    public abstract String getHeader();

    /**
     * Returns whether the given TransitionType is convertable.
     *
     * @return
     */
    public abstract boolean isConvertable(TransitionType aTransitionType, TraMLType aTraMLType);

    /**
     * Returns the conversion Message if needed. Null if the TransitionType is convertable.
     *
     * @return
     */
    public abstract MessageBean getConversionMessage();

    /**
     * Returns the separator character associated to this filemodel.
     *
     * @return
     */
    public abstract char getSeparator();

    /**
     * Append a TransitionType instance as a single line.
     */
    public abstract String parseTransitionType(TransitionType aTransitionType, TraMLType aTraMLType);

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

    /**
     * Gets the iConstants.
     *
     * @return The iConstants value.
     */
    public Constants getConstants() {
        return iConstants;
    }

    /**
     * Sets the iConstants.
     *
     * @param aConstants The iConstants value to set.
     */
    public void setConstants(Constants aConstants) {
        this.iConstants = aConstants;
    }
}
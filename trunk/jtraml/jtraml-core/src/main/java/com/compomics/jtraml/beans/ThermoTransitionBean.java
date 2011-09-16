package com.compomics.jtraml.beans;

/**
 * This class is a Bean representation of a single transition in the .csv file format of Thermo instruments
 */
public class ThermoTransitionBean {


    /**
     * The precursor ion mass
     */
    private double iQ1Mass = -1;

    /**
     * The product ion mass
     */
    private double iQ3Mass = -1;

    /**
     * The start time
     */
    private double iStartTime = -1;

    /**
     * The end time
     */
    private double iEndTime = -1;


    /**
     * The polarity
     */
    private String iPolarity = "";


    /**
     * The trigger (cfr iSRM)
     */
    private int iTrigger = -1;

    /**
     * The identifier of this transition
     */

    private String iID = "";




    /**
     * Sets the iQ3Mass.
     *
     * @param aQ3Mass The iQ3Mass value to set.
     */
    public void setQ3Mass(double aQ3Mass) {
        this.iQ3Mass = aQ3Mass;
    }

    /**
     * Gets the iID.
     *
     * @return The iID value.
     */
    public String getID() {
        return iID;
    }

    /**
     * Sets the iID.
     *
     * @param aID The iID value to set.
     */
    public void setID(String aID) {
        this.iID = aID;
    }

    /**
     * Sets the iPolarity.
     *
     * @param aPolarity The iPolarity value to set.
     */
    public void setPolarity(String aPolarity) {
        this.iPolarity = aPolarity;
    }

    /**
     * Sets the iTrigger.
     *
     * @param aTrigger The iTrigger value to set.
     */
    public void setTrigger(int aTrigger) {
        this.iTrigger = aTrigger;
    }

    /**
     * Gets the iStartTime.
     *
     * @return The iStartTime value.
     */
    public double getStartTime() {
        return iStartTime;
    }

    /**
     * Gets the iTrigger.
     *
     * @return The iTrigger value.
     */
    public int getTrigger() {
        return iTrigger;
    }

    /**
     * Gets the iEndTime.
     *
     * @return The iEndTime value.
     */
    public double getEndTime() {
        return iEndTime;
    }

    /**
     * Sets the iEndTime.
     *
     * @param aEndTime The iEndTime value to set.
     */
    public void setEndTime(double aEndTime) {
        this.iEndTime = aEndTime;
    }

    /**
     * Sets the iQ1Mass.
     *
     * @param aQ1Mass The iQ1Mass value to set.
     */
    public void setQ1Mass(double aQ1Mass) {
        this.iQ1Mass = aQ1Mass;
    }

    /**
     * Sets the iStartTime.
     *
     * @param aStartTime The iStartTime value to set.
     */
    public void setIStartTime(double aStartTime) {
        this.iStartTime = aStartTime;
    }

    /**
     * Gets the iQ3Mass.
     *
     * @return The iQ3Mass value.
     */
    public double getQ3Mass() {
        return iQ3Mass;
    }

    /**
     * Gets the iPolarity.
     *
     * @return The iPolarity value.
     */
    public String getIPolarity() {
        return iPolarity;
    }

    /**
     * Gets the iQ1Mass.
     *
     * @return The iQ1Mass value.
     */
    public double getIQ1Mass() {
        return iQ1Mass;
    }
}

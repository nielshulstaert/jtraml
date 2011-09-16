package com.compomics.jtraml.beans;

import com.google.common.base.Joiner;

/**
 * This class is a Bean representation of a single transition in the .csv file format of Thermo instruments
 */
public class ThermoTransitionBean {

    /**
     * Empty constructor
     */
    public ThermoTransitionBean() {
    }

    /**
     * Create a new Bean from the specified Strings.
     *
     * @param aQ1Mass
     * @param aQ3Mass
     * @param aStartTime
     * @param aEndTime
     * @param aPolarity
     * @param aTrigger
     * @param aReactionCategory
     * @param aID
     */
    public ThermoTransitionBean(String aQ1Mass, String aQ3Mass, String aStartTime, String aEndTime, String aPolarity, String aTrigger, String aReactionCategory, String aID) {
        setQ1Mass(Double.parseDouble(aQ1Mass));
        setQ3Mass(Double.parseDouble(aQ3Mass));
        setStartTime(Double.parseDouble(aStartTime));
        setEndTime(Double.parseDouble(aEndTime));
        setPolarity(aPolarity);
        setTrigger(aTrigger);
        setReactionCategory(aReactionCategory);
        setID(aID);
    }

    /**
     * Create a new Bean from an array of Strings. Note that the order in the array is essential!!
     *
     * @param aValues size = 9
     */
    public ThermoTransitionBean(String[] aValues) {
        this(
                aValues[0],
                aValues[1],
                aValues[2],
                aValues[3],
                aValues[4],
                aValues[5],
                aValues[7], aValues[6]
        );
    }

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
    private String iTrigger = "";


    /**
     * The reaction category value of this transition
     */
    private String iReactionCategory = "";

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
    public void setTrigger(String aTrigger) {
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
    public String getTrigger() {
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
    public void setStartTime(double aStartTime) {
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

    /**
     * Sets the iReactionCategory.
     *
     * @param aReactionCategory The iReactionCategory value to set.
     */
    public void setReactionCategory(String aReactionCategory) {
        this.iReactionCategory = aReactionCategory;
    }

    /**
     * Gets the iReactionCategory.
     *
     * @return The iReactionCategory value.
     */
    public String getReactionCategory() {
        return iReactionCategory;
    }

    /**
     * Returns a String[] with the specific order as they variables would appear in the Thermo CSV file.
     * @return
     */
    public String[] getCSVOrder() {
        return new String[]{
                "" + iQ1Mass,
                "" + iQ3Mass,
                "" + iStartTime,
                "" + iEndTime,
                iPolarity,
                iTrigger,
                iReactionCategory,
                iID};
    }

    /**
     *
     * @return
     */
    public String asCSVLine(){
        return Joiner.on(',').join(getCSVOrder());
    }
}

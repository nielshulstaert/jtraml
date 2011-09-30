package com.compomics.jtraml.beans;

/**
 * This class is a
 */
public abstract class TransitionBean {
    /**
     * The precursor ion mass
     */
    private double iQ1Mass = -1;
    /**
     * The product ion mass
     */
    private double iQ3Mass = -1;
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
     * Sets the iQ1Mass.
     *
     * @param aQ1Mass The iQ1Mass value to set.
     */
    public void setQ1Mass(double aQ1Mass) {
        this.iQ1Mass = aQ1Mass;
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
     * Gets the iQ1Mass.
     *
     * @return The iQ1Mass value.
     */
    public double getQ1Mass() {
        return iQ1Mass;
    }

    public abstract String[] getSeparatedOrder();

    public abstract String asCSVLine();
}

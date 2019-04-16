package com.compomics.jtraml.model;

/**
 * This interface holds defaults to be used in the TraML conversion proces.
 */
public class Constants {
    /**
     * Class: THERMO_CSV
     * When the trigger variable is set to "1", then the a full MS/MS spectrum is recorded upon detecting the transition.
     * If set to "0", then the MS/MS trigger is disabled.
     */
    private String TRIGGER = "0";

    /**
     * Class: THERMO_CSV
     * Is specific to iSRM.  The possible values are 0 and 1 where 0 is for primaries and 1 is for secondaries.
     * When not performing iSRM, then all SRM transitions would be 0's.
     */

    private String REACTION_CATEGORY = "0";


    /**
     * Class: AGILENT_CSV
     * FALSE - compound or transition is considered as target compound.
     * TRUE - it is signed as internal standard (ISTD).
     * <p/>
     * This plays a role in quantitation and this information is then read automatically
     * when quant method is created from MRM data.
     * <p/>
     * If you set it to FALSE as default, you are save.
     */
    private String ISTD = "FALSE";


    /**
     * Class: AGILENT_CSV
     * The precursor ion resolution.
     * Unit -> mass resolution is 0.7 AMU
     * Wide->Mass resolution is 1.2 AMU
     * Widest->Mass resolution is 2.5 AMU
     */
    private String MS1_RESOLUTION = "Wide";

    /**
     * Class: AGILENT_CSV
     * The precursor ion resolution.
     * Unit -> mass resolution is 0.7 AMU
     * Wide->Mass resolution is 1.2 AMU
     * Widest->Mass resolution is 2.5 AMU
     */
    private String MS2_RESOLUTION = "Unit";

    /**
     * Class: AGILENT_CSV
     * This is the LCMSD value and not been used with GCMSMS unit.
     * You can use 125 as a hard coded value.
     */
    private String FRAGMENTOR = "125";

    /**
     * Class: ABI_CSV
     * The meaning of this variable is unknown but always seems to be '10'.
     */
    private String QTRAP_COL3 = "10";

    /**
     * Gets the FRAGMENTOR.
     *
     * @return The FRAGMENTOR value.
     */
    public String getFRAGMENTOR() {
        return FRAGMENTOR;
    }

    /**
     * Gets the ISTD.
     *
     * @return The ISTD value.
     */
    public String getISTD() {
        return ISTD;
    }

    /**
     * Gets the MS1_RESOLUTION.
     *
     * @return The MS1_RESOLUTION value.
     */
    public String getMS1_RESOLUTION() {
        return MS1_RESOLUTION;
    }

    /**
     * Gets the MS2_RESOLUTION.
     *
     * @return The MS2_RESOLUTION value.
     */
    public String getMS2_RESOLUTION() {
        return MS2_RESOLUTION;
    }

    /**
     * Gets the QTRAP_COL3.
     *
     * @return The QTRAP_COL3 value.
     */
    public String getQTRAP_COL3() {
        return QTRAP_COL3;
    }

    /**
     * Gets the REACTION_CATEGORY.
     *
     * @return The REACTION_CATEGORY value.
     */
    public String getREACTION_CATEGORY() {
        return REACTION_CATEGORY;
    }

    /**
     * Gets the TRIGGER.
     *
     * @return The TRIGGER value.
     */
    public String getTRIGGER() {
        return TRIGGER;
    }

    /**
     * Sets the FRAGMENTOR.
     *
     * @param newFRAGMENTOR The FRAGMENTOR value to set.
     */
    public void setFRAGMENTOR(String newFRAGMENTOR) {
        this.FRAGMENTOR = newFRAGMENTOR;
    }

    /**
     * Sets the ISTD.
     *
     * @param newISTD The ISTD value to set.
     */
    public void setISTD(String newISTD) {
        this.ISTD = newISTD;
    }

    /**
     * Sets the MS1_RESOLUTION.
     *
     * @param newMS1_RESOLUTION The MS1_RESOLUTION value to set.
     */
    public void setMS1_RESOLUTION(String newMS1_RESOLUTION) {
        this.MS1_RESOLUTION = newMS1_RESOLUTION;
    }

    /**
     * Sets the MS2_RESOLUTION.
     *
     * @param newMS2_RESOLUTION The MS2_RESOLUTION value to set.
     */
    public void setMS2_RESOLUTION(String newMS2_RESOLUTION) {
        this.MS2_RESOLUTION = newMS2_RESOLUTION;
    }

    /**
     * Sets the QTRAP_COL3.
     *
     * @param newQTRAP_COL3 The QTRAP_COL3 value to set.
     */
    public void setQTRAP_COL3(String newQTRAP_COL3) {
        this.QTRAP_COL3 = newQTRAP_COL3;
    }

    /**
     * Sets the REACTION_CATEGORY.
     *
     * @param newREACTION_CATEGORY The REACTION_CATEGORY value to set.
     */
    public void setREACTION_CATEGORY(String newREACTION_CATEGORY) {
        this.REACTION_CATEGORY = newREACTION_CATEGORY;
    }

    /**
     * Sets the TRIGGER.
     *
     * @param newTRIGGER The TRIGGER value to set.
     */
    public void setTRIGGER(String newTRIGGER) {
        this.TRIGGER = newTRIGGER;
    }
}

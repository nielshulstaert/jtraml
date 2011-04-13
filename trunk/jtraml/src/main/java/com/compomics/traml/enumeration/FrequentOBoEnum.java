package com.compomics.traml.enumeration;

/**
 * This enum is a wraps the distinct input types.
 */
public enum FrequentOBoEnum {
    MZ("m/z"),
    ELECTRON_VOLT("electronvolt"),
    ISOLATION_WINDOW("isolation window target m/z"),
    COLLISION_ENERGY("collision energy"),
    TSV_ABI("ABI_TSV");

    /**
     * The name for the input type enumeration.
     */
    private String iName;

    /**
     * Simple constructor.
     * @param aName
     */
    FrequentOBoEnum(String aName) {
        iName = aName;
    }

    /**
     * Return the name of the InputType.
     * @return
     */
    public String getName() {
        return iName;
    }
}

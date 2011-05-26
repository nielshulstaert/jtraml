package com.compomics.jtraml.enumeration;

/**
 * This enum is a wraps the distinct input type term names.
 */
public enum FrequentOBoEnum {

    MZ("m/z"),

    ISOLATION_WINDOW("isolation window target m/z"),
    COLLISION_ENERGY("collision energy"),
    ACCELERATING_VOLTAGE("accelerating voltage"),

    RETENTION_TIME("retention time"),
    RETENTION_TIME_LOWER("retention time window lower offset"),
    RETENTION_TIME_UPPER("retention time window upper offset"),
    RETENTION_TIME_WINDOW("retention time window attribute"),

    ELECTRON_VOLT("electronvolt"),
    VOLT("volt"),
    MINUTES("minute"),

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

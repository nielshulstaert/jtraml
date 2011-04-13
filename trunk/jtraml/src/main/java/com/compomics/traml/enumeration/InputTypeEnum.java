package com.compomics.traml.enumeration;

/**
 * This enum is a wraps the distinct input types.
 */
public enum InputTypeEnum {
    TRAML("PSI_TraML"), TSV_THERMO("Thermo_TSV"), TSV_ABI("ABI_TSV");

    /**
     * The name for the input type enumeration.
     */
    private String iName;

    /**
     * Simple constructor.
     * @param aName
     */
    InputTypeEnum(String aName) {
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

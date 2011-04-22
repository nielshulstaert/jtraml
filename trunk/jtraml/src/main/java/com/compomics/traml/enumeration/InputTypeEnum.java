package com.compomics.traml.enumeration;

/**
 * This enum is a wraps the distinct input types.
 */
public enum InputTypeEnum {
    TRAML("psi_traml"), TSV_THERMO_TSQ("thermo_tsv"), TSV_AGILENT_QQQ("agilent_tsv"), TSV_ABI("abi_tsv");

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

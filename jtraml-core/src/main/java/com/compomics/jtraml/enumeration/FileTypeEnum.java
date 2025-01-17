package com.compomics.jtraml.enumeration;

/**
 * This enum is a wraps the distinct input types.
 */
public enum FileTypeEnum {
    TRAML("TraML", ".traml"),
    TSV_THERMO_TSQ("thermo_csv", ".csv"),
    TSV_AGILENT_QQQ("agilent_tsv", ".tsv"),
    TSV_ABI("abi_csv", ".csv");

    /**
     * The name for the input type enumeration.
     */
    private String iName;

    /**
     * The default extension for the filename.
     */
    private String iExtension;

    /**
     * Simple constructor.
     *
     * @param aName
     * @param aExtension
     */
    FileTypeEnum(String aName, String aExtension) {
        iName = aName;
        iExtension = aExtension;
    }

    /**
     * Return the default extension of the InputType.
     *
     * @return
     */
    public String getExtension() {
        return iExtension;
    }

    /**
     * Return the name of the InputType.
     *
     * @return
     */
    public String getName() {
        return iName;
    }

    @Override
    public String toString() {
        if(iName.indexOf("_") != -1){
            return iName.substring(0, iName.indexOf("_")) + " (" + iExtension + ")";
        }else{
            return iName;
        }
    }

}

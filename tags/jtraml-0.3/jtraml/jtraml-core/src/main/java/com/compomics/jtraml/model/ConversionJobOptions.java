package com.compomics.jtraml.model;

import com.compomics.jtraml.enumeration.FileTypeEnum;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.Serializable;
import java.util.UUID;

/**
 * This class is a models the data of the GUI application.
 */
public class ConversionJobOptions implements Serializable {

    private static Logger logger = Logger.getLogger(ConversionJobOptions.class);

    /**
     * The user specified delta retention time.
     */
    private double iRtDelta = Double.MIN_VALUE;

    /**
     * The user input file.
     */
    private File iInputFile = null;

    /**
     * The user output file.
     */
    private File iOutputFile;

    /**
     * The specified import type.
     */
    private FileTypeEnum iImportType = FileTypeEnum.TSV_THERMO_TSQ;

    /**
     * The specified export type.
     */
    private FileTypeEnum iExportType = FileTypeEnum.TRAML;

    private UUID uuid;

    /**
     * empty constructor.
     */
    public ConversionJobOptions() {
        uuid = UUID.fromString("3856c3da-ea56-4717-9f58-85f6c5f560a5");
    }


    public FileTypeEnum getImportType() {
        return iImportType;
    }

    public void setImportType(FileTypeEnum aImportType) {
        iImportType = aImportType;
    }

    public FileTypeEnum getExportType() {
        return iExportType;
    }

    public void setExportType(FileTypeEnum aExportType) {
        iExportType = aExportType;
    }

    /**
     * Set the delta retention time specified by the user.
     *
     * @param aRtDelta
     */
    public void setRtDelta(double aRtDelta) {
        iRtDelta = aRtDelta;
    }

    /**
     * Returns whether an inputfile has been set.
     *
     * @return
     */
    public boolean hasInputFile() {
        return !(iInputFile == null);
    }

    /**
     * Return the user InputFile
     *
     * @return
     */
    public File getInputFile() {
        return iInputFile;
    }

    /**
     * Set the user inputfile.
     *
     * @param aInputFile
     */
    public void setInputFile(File aInputFile) {
        iInputFile = aInputFile;
    }

    /**
     * Returns whether an outputfile has been set.
     *
     * @return
     */
    public boolean hasOutputFile() {
        return !(iOutputFile == null);
    }

    /**
     * Returns true if the RtDelta time was specified by the user.
     *
     * @return
     */
    public boolean hasRtDelta() {
        return (iRtDelta != Double.MIN_VALUE);
    }


    /**
     * Returns the user specified delta retention time.
     *
     * @return
     */
    public double getRtDelta() {
        return iRtDelta;
    }

    /**
     * Return the user OutputFile
     *
     * @return
     */
    public File getOutputFile() {
        return iOutputFile;
    }

    /**
     * Set the user Outputfile.
     *
     * @param aOutputFile
     */
    public void setOutputFile(File aOutputFile) {
        iOutputFile = aOutputFile;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return "ApplicationDataModel{" +
                "iInputFile=" + iInputFile +
                ", iOutputFile=" + iOutputFile +
                ", iImportType=" + iImportType +
                ", iExportType=" + iExportType +
                '}';
    }

}

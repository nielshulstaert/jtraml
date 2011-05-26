package com.compomics.traml.gui.forms;

import com.compomics.traml.enumeration.FileTypeEnum;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * This class is a models the data of the GUI application.
 */
public class ApplicationDataModel {

    private static Logger logger = Logger.getLogger(ApplicationDataModel.class);

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
    private TramlConverterGUI iTramlConverterGUI;

    /**
     * empty constructor.
     */
    public ApplicationDataModel() {

    }


    public FileTypeEnum getImportType() {
        return iImportType;
    }

    public void setImportType(FileTypeEnum aImportType) {
        iImportType = aImportType;
        notifyObserver();
    }

    public FileTypeEnum getExportType() {
        return iExportType;
    }

    public void setExportType(FileTypeEnum aExportType) {
        iExportType = aExportType;
        notifyObserver();
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
        notifyObserver();
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
        notifyObserver();
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

    public void setObserver(TramlConverterGUI aTramlConverterGUI) {
        iTramlConverterGUI = aTramlConverterGUI;
    }

    private void notifyObserver(){
        iTramlConverterGUI.update();
    }
}

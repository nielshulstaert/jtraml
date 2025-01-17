package com.compomics.jtraml.thread;

import com.compomics.jtraml.enumeration.FileTypeEnum;
import com.compomics.jtraml.exception.JTramlException;
import com.compomics.jtraml.factory.CVFactory;
import com.compomics.jtraml.interfaces.TSVFileImportModel;
import com.compomics.jtraml.model.ABIToTraml;
import com.compomics.jtraml.model.AgilentToTraml;
import com.compomics.jtraml.model.ConversionJobOptions;
import com.compomics.jtraml.model.ThermoToTraml;
import com.google.common.io.Files;
import org.apache.log4j.Logger;
import org.hupo.psi.ms.traml.InstrumentListType;
import org.hupo.psi.ms.traml.InstrumentType;
import org.hupo.psi.ms.traml.ObjectFactory;
import org.hupo.psi.ms.traml.TraMLType;
import org.systemsbiology.apps.tramlcreator.TraMLCreator;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Observable;
import org.systemsbiology.constants.JTRAML_URL;

/**
 * This class converts a TSV file to a TRAML file in a single thread.
 */
public class SepToTRAMLJob extends Observable implements Job {
    private static Logger logger = Logger.getLogger(SepToTRAMLJob.class);

    /**
     * The TSV TSVFileImportModel of the input file.
     */
    private TSVFileImportModel iTSVFileImportModel = null;

    /**
     * The input TSV file.
     */
    private File iInputFile = null;

    /**
     * The output TRAML file.
     */
    private File iOutputFile = null;

    /**
     * This descriptive String returns the current state of the job.
     */
    private String iStatus;


    /**
     * Creates a new job to convert a separated file with transitions into the TraML specification.
     *
     * @param aConversionJobOptions
     */
    public SepToTRAMLJob(ConversionJobOptions aConversionJobOptions) {
        this(
                aConversionJobOptions.getInputFile(),
                aConversionJobOptions.getOutputFile(),
                aConversionJobOptions.getImportType()
        );

        if (aConversionJobOptions.hasRtShift()) { // Retention time shift has been set.
            iTSVFileImportModel.shiftRetentionTime(true);
            iTSVFileImportModel.setRetentionTimeShift(aConversionJobOptions.getRtShift());
        }
    }


    /**
     * Creates a new job to convert a separated file with transitions into the TraML specification.
     *
     * @param aInputFile
     * @param aOutputFile
     * @param aImportModel
     */
    public SepToTRAMLJob(File aInputFile, File aOutputFile, TSVFileImportModel aImportModel) {
        iInputFile = aInputFile;
        iOutputFile = aOutputFile;
        logger.debug("using TraML importmodel " + aImportModel.getClass().getName());
        iTSVFileImportModel = aImportModel;
    }

    /**
     * Creates a new job to convert a separated file with transitions into the TraML specification.
     *
     * @param aInputFile
     * @param aOutputFile
     * @param aImportType
     */
    public SepToTRAMLJob(File aInputFile, File aOutputFile, FileTypeEnum aImportType) {
        iInputFile = aInputFile;
        iOutputFile = aOutputFile;


        if (aImportType == FileTypeEnum.TSV_THERMO_TSQ) {
            iTSVFileImportModel = new ThermoToTraml(iInputFile);
        } else if (aImportType == FileTypeEnum.TSV_AGILENT_QQQ) {
            iTSVFileImportModel = new AgilentToTraml(iInputFile);
        } else if (aImportType == FileTypeEnum.TSV_ABI) {
            iTSVFileImportModel = new ABIToTraml(iInputFile);
        } else {
            throw new JTramlException("unsupported import format!!", this);
        }

    }

    /**
     * Run the conversion.
     */
    public void run() {

        try {
            BufferedReader br = null;
            br = Files.newReader(iInputFile, Charset.defaultCharset());

            ObjectFactory lObjectFactory = new ObjectFactory();
            TraMLType lTraMLType = lObjectFactory.createTraMLType();
            
            // set version
            lTraMLType.setVersion(JTRAML_URL.TRAML_XSD_VERSION);

            String line = "";

            if (iTSVFileImportModel instanceof AgilentToTraml) {
                // skip the first two lines.
                br.readLine();
                br.readLine();

            } else if (iTSVFileImportModel instanceof ThermoToTraml) {
                // skip the first line.
                br.readLine();
            }


            String sep = "" + iTSVFileImportModel.getSeparator();
            logger.debug("reading input file\t" + iInputFile);

            int lineCount = 0;
            while ((line = br.readLine()) != null) {
                lineCount++;
                iStatus = "reading line " + lineCount;
                String[] lValues = line.split(sep);
                iTSVFileImportModel.addRowToTraml(lTraMLType, lValues);
            }

            lTraMLType.setCvList(CVFactory.getCvListType());
            lTraMLType.setSourceFileList(iTSVFileImportModel.getSourceTypeList());
            lTraMLType.setInstrumentList(iTSVFileImportModel.getInstrumentTypeList());

            if (iTSVFileImportModel.hasPolarity()) { // Does the tsv file has any polarity information?
                InstrumentType lInstrumentType = lObjectFactory.createInstrumentType();
                lInstrumentType.setId("1");

                // Get the polarity type.
                lInstrumentType.setCvParam(iTSVFileImportModel.getPolarityCVParam());

                InstrumentListType lInstrumentListType = lObjectFactory.createInstrumentListType();
                lInstrumentListType.getInstrument().add(lInstrumentType);

                lTraMLType.setInstrumentList(lInstrumentListType);
            }


            // Ok, all rows have been added.
            TraMLCreator lTraMLCreator = new TraMLCreator();
            lTraMLCreator.setTraML(lTraMLType);

            if (iOutputFile.exists()) {
                iOutputFile.delete();
            }

            BufferedWriter lWriter = Files.newWriter(iOutputFile, Charset.defaultCharset());

            lWriter.write(lTraMLCreator.asString());
            // Ok. The File should have been written!
            lWriter.flush();
            lWriter.close();

            // finished, spread the word!
            setChanged();
            notifyObservers();

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getStatus() {
        return iStatus;
    }

    @Override
    public String toString() {
        return "SepToTRAMLJob{" +
                "iTSVFileImportModel=" + iTSVFileImportModel +
                ", iInputFile=" + iInputFile +
                ", iOutputFile=" + iOutputFile +
                ", iStatus='" + iStatus + '\'' +
                '}';
    }
}

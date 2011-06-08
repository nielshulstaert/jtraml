package com.compomics.jtraml.thread;

import com.compomics.jtraml.enumeration.FileTypeEnum;
import com.compomics.jtraml.exception.JTramlException;
import com.compomics.jtraml.interfaces.TSVFileExportModel;
import com.compomics.jtraml.model.ConversionJobOptions;
import com.compomics.jtraml.model.TramlToABI;
import com.compomics.jtraml.model.TramlToAgilent;
import com.compomics.jtraml.model.TramlToThermo;
import com.google.common.io.Files;
import org.apache.log4j.Logger;
import org.hupo.psi.ms.traml.TraMLType;
import org.hupo.psi.ms.traml.TransitionType;
import org.systemsbiology.apps.tramlparser.TraMLParser;

import javax.xml.bind.JAXBException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Observable;

/**
 * This class converts a a TRAML file into a TSV/CSV file in a single thread.
 */
public class TRAMLToSepJob extends Observable implements Runnable {
    private static Logger logger = Logger.getLogger(TRAMLToSepJob.class);

    /**
     * The TSV TSVFileExportModel of the input file.
     */
    private TSVFileExportModel iTSVFileExportModel = null;

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
    public int iCounter;


    /**
     * Creates a new job to convert a separated file with transitions into the TraML specification.
     *
     * @param aConversionJobOptions
     */
    public TRAMLToSepJob(ConversionJobOptions aConversionJobOptions) {
        this(
                aConversionJobOptions.getInputFile(),
                aConversionJobOptions.getOutputFile(),
                aConversionJobOptions.getExportType()
        );
    }


    /**
     * Creates a new job to convert a separated file with transitions into the TraML specification.
     *
     * @param aInputFile
     * @param aOutputFile
     * @param aExportType
     */
    public TRAMLToSepJob(File aInputFile, File aOutputFile, FileTypeEnum aExportType) {
        iInputFile = aInputFile;
        iOutputFile = aOutputFile;


        if (aExportType == FileTypeEnum.TSV_THERMO_TSQ) {
            iTSVFileExportModel = new TramlToThermo();
        } else if (aExportType == FileTypeEnum.TSV_AGILENT_QQQ) {
            iTSVFileExportModel = new TramlToAgilent();
        } else if (aExportType == FileTypeEnum.TSV_ABI) {
            iTSVFileExportModel = new TramlToABI();
        } else {
            throw new JTramlException("unsupported export format!!");
        }
    }

    /**
     * Run the conversion.
     */
    public void run() {

        try {
            BufferedWriter lWriter = Files.newWriter(iOutputFile, Charset.defaultCharset());

            if (iTSVFileExportModel.hasHeader()) {
                lWriter.write(iTSVFileExportModel.getHeader());
                lWriter.write("\n");
            }

            // Now read the traml file.
            TraMLParser lTraMLParser = new TraMLParser();
            lTraMLParser.parse_file(iInputFile.getCanonicalPath(), logger);

            TraMLType lTraML = lTraMLParser.getTraML();
            List<TransitionType> lTransitionTypeList = lTraML.getTransitionList().getTransition();
            iCounter = 0;
            for (TransitionType lTransitionType : lTransitionTypeList) {
                iCounter++;
                iStatus = "writing transition " + iCounter;
                String line = iTSVFileExportModel.parseTransitionType(lTransitionType, lTraML);
                lWriter.write(line);
                lWriter.write("\n");
            }

            // Ok. The File should have completed!
            lWriter.flush();
            lWriter.close();

            // finished, spread the word!
            setChanged();
            notifyObservers();

        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (JAXBException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    public String getStatus() {
        return iStatus;
    }
}

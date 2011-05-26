package com.compomics.traml.thread;

import com.compomics.traml.factory.CustomTypeFactory;
import com.compomics.traml.interfaces.FileModel;
import com.compomics.traml.model.rowmodel.AgilentQQQImpl;
import com.compomics.traml.model.rowmodel.ThermoTSQImpl;
import com.google.common.io.Files;
import org.apache.log4j.Logger;
import org.hupo.psi.ms.traml.ObjectFactory;
import org.hupo.psi.ms.traml.TraMLType;
import org.systemsbiology.apps.tramlcreator.TraMLCreator;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Observable;

/**
 * This class converts a TSV file to a TRAML file in a single thread.
 */
public class TSVToTRAMLJob extends Observable implements Runnable {
    private static Logger logger = Logger.getLogger(TSVToTRAMLJob.class);

    /**
     * The TSV FileModel of the input file.
     */
    private FileModel iFileModel = null;

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


    public TSVToTRAMLJob(FileModel aFileModel, File aInputFile, File aOutputFile) {
        iFileModel = aFileModel;
        iInputFile = aInputFile;
        iOutputFile = aOutputFile;
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

            String line = "";


            if (iFileModel instanceof AgilentQQQImpl) {
                // skip the first two lines.
                br.readLine();
                br.readLine();

            } else if (iFileModel instanceof ThermoTSQImpl) {
                // skip the first line.
                br.readLine();
            }

            String sep = "" + iFileModel.getSeparator();
            logger.debug("reading input file\t" + iInputFile);

            int lineCount = 0;
            while ((line = br.readLine()) != null) {
                lineCount++;
                iStatus = "reading line " + lineCount;
                String[] lValues = line.split(sep);
                iFileModel.addRowToTraml(lTraMLType, lValues);
            }

            lTraMLType.setCvList(CustomTypeFactory.getCvListType());
            lTraMLType.setSourceFileList(iFileModel.getSourceTypeList());

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

        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (JAXBException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

    }

    public String getStatus() {
        return iStatus;
    }
}

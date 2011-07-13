package com.compomics.jtraml.thread;

import com.compomics.jtraml.MessageBean;
import com.compomics.jtraml.enumeration.FileTypeEnum;
import com.compomics.jtraml.exception.JTramlException;
import com.compomics.jtraml.interfaces.Interruptible;
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
import sun.misc.ConditionLock;

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
public class TRAMLToSepJob extends Observable implements Runnable, Interruptible {
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
     * The Target export type.
     */
    private FileTypeEnum iExportType;

    /**
     * Keeps track whether this Job is exerted via a Graphical User Interface.
     */
    private boolean iGraphical = false;

    /**
     * This boolean tracks the interruption status of the Converter.
     */
    private boolean boolInterrupted = false;

    /**
     * This descriptive String returns the current state of the job.
     */
    private String iStatus;
    public int iCounter;
    public TraMLType iTraML;


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

        if (aConversionJobOptions.hasRtDelta()) {
            iTSVFileExportModel.setRetentionTimeDelta(aConversionJobOptions.getRtDelta());
        }

        if (aConversionJobOptions.hasRtShift()) { // Retention time shift has been set.
            iTSVFileExportModel.shiftRetentionTime(true);
            iTSVFileExportModel.setRetentionTimeShift(aConversionJobOptions.getRtShift());
        }
        iTSVFileExportModel.setConstants(aConversionJobOptions.getConstants());
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
        iExportType = aExportType;

        if (aExportType == FileTypeEnum.TSV_THERMO_TSQ) {
            iTSVFileExportModel = new TramlToThermo();
        } else if (aExportType == FileTypeEnum.TSV_AGILENT_QQQ) {
            iTSVFileExportModel = new TramlToAgilent();
        } else if (aExportType == FileTypeEnum.TSV_ABI) {
            iTSVFileExportModel = new TramlToABI();
        } else {
            throw new JTramlException("export format is not implemented!!");
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

            iTraML = lTraMLParser.getTraML();
            List<TransitionType> lTransitionTypeList = iTraML.getTransitionList().getTransition();
            iCounter = 0;
            for (TransitionType lTransitionType : lTransitionTypeList) {
                iCounter++;

                // Validate the first transition.
                if (iCounter == 1) {
                    boolean lConvertable = iTSVFileExportModel.isConvertable(lTransitionType, iTraML);
                    if(isGraphical() == false && lConvertable == false){
                        String lMessage = iTSVFileExportModel.getConversionMessage().getMessage();
                        lMessage = lMessage + "\n" + "Start without parameters to see all options.";
                        if(iTSVFileExportModel.getConversionMessage().isRequiresAnswer()){
                            // We need additional input, so stop the converter.
                            throw new JTramlException(lMessage, this);

                        }else{
                            // No additional input required, simply log the conversion message.
                            logger.debug(lMessage);
                        }

                    }else if (lConvertable == false) {
                        // Cannot be converted, at the moment. Try and solve!
                        MessageBean lConversionMessage = iTSVFileExportModel.getConversionMessage();
                        lConversionMessage.setInterruptible(this);
                        setChanged();
                        notifyObservers(lConversionMessage);
                        interrupt();
                    }
                }

                iStatus = "writing transition " + iCounter;
                String line = iTSVFileExportModel.parseTransitionType(lTransitionType, iTraML);
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

    /**
     * Interrupt the implementing class.
     */
    public void interrupt() throws InterruptedException {
        boolInterrupted = true;
        String o = "sync";

        ConditionLock lConditionLock = new ConditionLock();
        synchronized (lConditionLock) {
            while (boolInterrupted) {
                lConditionLock.wait(1000);
                System.out.println(".");
            }
        }

    }

    /**
     * Proceed the operations of the current Class.
     *
     * @param o
     */
    public void proceed(Object o) {
        String s = o.toString();
        if (!s.equals("")) {
            Double d = new Double(s);
            iTSVFileExportModel.setRetentionTimeDelta(d.doubleValue());
        }
        boolInterrupted = false;
    }

    public boolean isGraphical() {
        return iGraphical;
    }

    public void setGraphical(boolean aGraphical) {
        iGraphical = aGraphical;
    }

    @Override
    public String toString() {
        return "TRAMLToSepJob{" +
                "iTSVFileExportModel=" + iTSVFileExportModel +
                ", iInputFile=" + iInputFile +
                ", iOutputFile=" + iOutputFile +
                ", iExportType=" + iExportType +
                ", iGraphical=" + iGraphical +
                ", boolInterrupted=" + boolInterrupted +
                ", iStatus='" + iStatus + '\'' +
                ", iCounter=" + iCounter +
                ", iTraML=" + iTraML +
                '}';
    }
}

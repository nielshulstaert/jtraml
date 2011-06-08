package com.compomics.jtraml;

import com.compomics.jtraml.enumeration.FileTypeEnum;
import com.compomics.jtraml.exception.JTramlException;
import com.compomics.jtraml.model.ConversionJobOptions;
import com.compomics.jtraml.thread.SepToTRAMLJob;
import com.compomics.jtraml.thread.TRAMLToSepJob;
import com.compomics.jtraml.validation.ConversionJobOptionValidator;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;
import sun.misc.ConditionLock;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This class has a main method for converting tab separated files to TraML.
 */
public class TramlConverter {
    private static Logger logger = Logger.getLogger(TramlConverter.class);

    public static void main(String[] args) {

        try {
            Options lOptions = new Options();
            createOptions(lOptions);

            BasicParser parser = new BasicParser();
            CommandLine line = parser.parse(lOptions, args);

            if (isValidStartup(line) == false) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("TraMLConverter", getHeader(), lOptions, getFooter());
                System.exit(0);
            } else {
                logger.debug("parameters ok!");

                File lInputFile = new File(line.getOptionValue("input"));
                File lOutputFile = new File(line.getOptionValue("output"));
                FileTypeEnum lInputType = getFileTypeEnum(line.getOptionValue("importtype"));
                FileTypeEnum lExportType = getFileTypeEnum(line.getOptionValue("exporttype"));

                ConversionJobOptions lConversionJobOptions = new ConversionJobOptions();
                lConversionJobOptions.setOutputFile(lOutputFile);
                lConversionJobOptions.setInputFile(lInputFile);
                lConversionJobOptions.setExportType(lExportType);
                lConversionJobOptions.setImportType(lInputType);

                boolean valid = ConversionJobOptionValidator.isValid(lConversionJobOptions);
                String lStatus = ConversionJobOptionValidator.getStatus();

                if (valid) {

                    if (lConversionJobOptions.getImportType() != FileTypeEnum.TRAML) {
                        SepToTRAMLJob job = new SepToTRAMLJob(lConversionJobOptions);
                        Future lSubmit = Executors.newSingleThreadExecutor().submit(job);

                        ConditionLock lConditionLock = new ConditionLock();
                        synchronized (lConditionLock) {
                            while (lSubmit.isDone() != true) {
                                try {
                                    lConditionLock.wait(1000);
                                    logger.debug(job.getStatus());
                                } catch (InterruptedException e) {
                                    logger.error(e.getMessage(), e);
                                }
                            }
                        }
                    } else {
                        TRAMLToSepJob job = new TRAMLToSepJob(lConversionJobOptions);
                        Future lSubmit = Executors.newSingleThreadExecutor().submit(job);

                        ConditionLock lConditionLock = new ConditionLock();
                        synchronized (lConditionLock) {
                            while (lSubmit.isDone() != true) {
                                try {
                                    lConditionLock.wait(1000);
                                    logger.debug(job.getStatus());
                                } catch (InterruptedException e) {
                                    logger.error(e.getMessage(), e);
                                }
                            }
                        }
                    }

                    logger.debug("finished writing \t" + lOutputFile.getName());
                } else {
                    logger.error(lStatus, new JTramlException(lStatus));
                }
            }
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * This method attempts to decode reqruied FileType for a String by its name.
     *
     * @param aInputtype
     */
    private static FileTypeEnum getFileTypeEnum(String aInputtype) {
        FileTypeEnum lFileTypeEnum = null;

        FileTypeEnum[] lValues = FileTypeEnum.values();
        for (FileTypeEnum lValue : lValues) {
            if (aInputtype.equals(lValue.getName())) {
                lFileTypeEnum = lValue;
            }
        }

        return lFileTypeEnum;
    }

    /**
     * @return
     */
    private static String getHeader() {
        return ""
                + "----------------------\n"
                + "INFO"
                + "\n"
                + "----------------------\n"
                + "\n"
                + "The TraML converter command line tool takes an input file and an input type and generates an output file.\n"
                + "If the input type is a .TraML file, then the converter generates be a .TSV file.\n"
                + "Otherwise, if the input type is a .TSV file, then the converter generates a .TraML file.\n"
                + "\n"
                + "----------------------\n"
                + "OPTIONS\n"
                + "\n"
                + "----------------------\n"
                + "";

    }

    private static String getFooter() {
        return ""
                + "\n"
                + "----------------------\n"
                + "http://code.google.com/p/jtraml/";
    }


    private static void createOptions(Options aOptions) {
        // Prepare.
        StringBuffer sb = new StringBuffer("The available file types:");
        FileTypeEnum[] lTypeEnums = FileTypeEnum.values();
        for (FileTypeEnum lFileType : lTypeEnums) {
            sb.append("<").append(lFileType.getName()).append(">");
        }
        String lFileTypes = sb.toString();

        // Set.
        aOptions.addOption("importtype", true, lFileTypes);
        aOptions.addOption("exporttype", true, lFileTypes);
        aOptions.addOption("input", true, "The transition input file");
        aOptions.addOption("output", true, "The converted transition output file");
    }

    /**
     * Verifies the command line start parameters.
     *
     * @return
     */
    public static boolean isValidStartup(CommandLine aLine) {
        // No params.
        if (aLine.getOptions().length == 0) {
            return false;
        }

        // Required params.
        if (aLine.getOptionValue("input") == null || aLine.getOptionValue("output") == null) {
            logger.error("input/output file not given!!");

            return false;
        }

        // input exists?
        String lFile = aLine.getOptionValue("input");
        File lInputFile = new File(lFile);
        if (lInputFile.exists() == false) {
            logger.error("input file does not exist!!");
            return false;
        }

        // if output given, does it exist? if not, make it!
        String lOutputFileName = aLine.getOptionValue("output");
        if (lOutputFileName != null) {
            File lOutputFile = new File(lOutputFileName);
            if (lOutputFile.exists() == false) {
                try {
                    lOutputFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }

        // has import and export type?
        if (aLine.hasOption("importtype") && aLine.hasOption("exporttype")) {
            logger.error("importtype and exporttype must be supplied!!");
            return false;
        }


        // All is fine!
        return true;
    }
}
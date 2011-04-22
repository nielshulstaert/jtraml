package com.compomics.traml;

import com.compomics.traml.enumeration.InputTypeEnum;
import com.compomics.traml.interfaces.FileModel;
import com.compomics.traml.model.rowmodel.AgilentQQQImpl;
import com.compomics.traml.thread.TSVToTRAMLJob;
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
                String lInputtype = line.getOptionValue("inputtype");

                if (lInputtype.equals(InputTypeEnum.TRAML.getName())) {
                    // TraML -> TSV
                    logger.info("traml is not yet implemented.");
                    System.exit(1);
                } else {
                    // TSV -> TraML
                    FileModel lFileModel = null;
                    if (lInputtype.equals(InputTypeEnum.TSV_ABI.getName())) {
                        logger.info("ABI TSV input is not yet implemented.");
                        System.exit(1);

                    } else if (lInputtype.equals(InputTypeEnum.TSV_THERMO_TSQ.getName())) {
                        logger.info("Thermo TSQ TSV input is not yet implemented.");
                        System.exit(1);

                    } else if (lInputtype.equals(InputTypeEnum.TSV_AGILENT_QQQ.getName())) {
                        logger.info("starting conversion from Agilent QQQ to TraML");
                        lFileModel = new AgilentQQQImpl(lInputFile);
                    }

                    TSVToTRAMLJob job = new TSVToTRAMLJob(lFileModel, lInputFile, lOutputFile);
                    Future lSubmit = Executors.newSingleThreadExecutor().submit(job);

                    ConditionLock lConditionLock = new ConditionLock();
                    synchronized (lConditionLock) {
                        while (lSubmit.isDone() != true) {
                            try {
                                lConditionLock.wait(1000);
                            } catch (InterruptedException e) {
                                logger.error(e.getMessage(), e);
                            }
                        }
                    }
                    logger.debug("finished writing \t" + lOutputFile.getName());
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
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
        StringBuffer sb = new StringBuffer("The available input types:");
        InputTypeEnum[] lTypeEnums = InputTypeEnum.values();
        for (InputTypeEnum lInputType : lTypeEnums) {
            sb.append("<").append(lInputType.getName()).append(">");
        }
        String lInputTypes = sb.toString();

        // Set.
        aOptions.addOption("input", true, "The transition input file");
        aOptions.addOption("inputtype", true, lInputTypes);
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

        // All is fine!
        return true;
    }
}
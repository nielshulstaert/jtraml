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
import java.io.PrintWriter;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This class has a main method for converting tab separated files to TraML.
 */
public class TramlConverter {
    private static Logger logger = Logger.getLogger(TramlConverter.class);

    public static void main(String[] args) {

        try {
            Options lCoreOptions = new Options();
            Options lConstantOptions = new Options();

            createOptions(lCoreOptions, lConstantOptions);

            BasicParser parser = new BasicParser();
            CommandLine line = parser.parse(lCoreOptions, args);

            if (isValidStartup(line) == false) {
                HelpFormatter formatter = new HelpFormatter();
                PrintWriter lPrintWriter = new PrintWriter(System.out);
//                formatter.printHelp("TraMLConverter", getHeader(), lCoreOptions, getFooter());
                lPrintWriter.print("TraMLConverter\n");

                lPrintWriter.print(getHeader());

                lPrintWriter.print("\nCore options:\n");
                formatter.printOptions(lPrintWriter, 200, lCoreOptions, 0, 0);
                lPrintWriter.print("\n\nConstant options:\n");
                formatter.printOptions(lPrintWriter, 200, lConstantOptions, 0, 0);

                lPrintWriter.flush();
                lPrintWriter.close();

                System.exit(0);
            } else {
                logger.debug("parameters ok!");

                ConversionJobOptions lConversionJobOptions = new ConversionJobOptions();

                File lInputFile = new File(line.getOptionValue("input"));
                File lOutputFile = new File(line.getOptionValue("output"));
                FileTypeEnum lInputType = getFileTypeEnum(line.getOptionValue("importtype"));
                FileTypeEnum lExportType = getFileTypeEnum(line.getOptionValue("exporttype"));


                // Optional variables
                double lRtDelta = -1;
                if (line.hasOption("rtdelta")) {
                    lRtDelta = Double.parseDouble(line.getOptionValue("rtdelta"));
                    lConversionJobOptions.setRtDelta(lRtDelta);
                }

                double lRtShift = -1;
                if (line.hasOption("rtshift")) {
                    lRtShift= Double.parseDouble(line.getOptionValue("rtshift"));
                    lConversionJobOptions.setRtShift(lRtShift);
                }

                lConversionJobOptions.setOutputFile(lOutputFile);
                lConversionJobOptions.setInputFile(lInputFile);
                lConversionJobOptions.setExportType(lExportType);
                lConversionJobOptions.setImportType(lInputType);


                // Constant variables.
                parseConstantOptions();


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
                        job.setGraphical(false);
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

    private static void parseConstantOptions() {

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



    private static void createOptions(Options aCoreOptions, Options aConstantOptions) {
        // Prepare.
        StringBuffer sb = new StringBuffer("The available file types:");
        FileTypeEnum[] lTypeEnums = FileTypeEnum.values();
        for (FileTypeEnum lFileType : lTypeEnums) {
            sb.append("<").append(lFileType.getName()).append(">");
        }
        String lFileTypes = sb.toString();

        // Set.
        aCoreOptions.addOption("importtype", true, lFileTypes);
        aCoreOptions.addOption("exporttype", true, lFileTypes);
        aCoreOptions.addOption("input", true, "The transition input file");
        aCoreOptions.addOption("output", true, "The converted transition output file");
        aCoreOptions.addOption("rtdelta", true, "This delta retention time (minutes) is used when appropriate (cfr. Wiki)");
        aCoreOptions.addOption("rtshift", true, "The retention time shift (minutes) value is used to added to the present retention times. Can be positive or negative. (cfr. Wiki)");

        aConstantOptions.addOption(new Option("constant_trigger", true, "default variable - thermo - set 1 to trigger recording of full MS/MS spectra."));
        aConstantOptions.addOption(new Option("constant_rcategory", true, "default variable - thermo - reaction category (iSRM) - Set 0 for primaries, or 1 for secondaries."));
        aConstantOptions.addOption(new Option("constant_istd", true, "default variable - agilent - internal standard - TRUE/FALSE"));
        aConstantOptions.addOption(new Option("constant_resms1", true, "default variable - agilent - MS1 resolution  - 'Unit' (0.7AMU) - 'Wide' (1.2AMU) - 'Widest' (2.5AMU)"));
        aConstantOptions.addOption(new Option("constant_resms2", true, "default variable - agilent - MS2 resolution  - 'Unit' (0.7AMU) - 'Wide' (1.2AMU) - 'Widest' (2.5AMU)"));
        aConstantOptions.addOption(new Option("constant_fragmentor", true, "default variable - agilent - LC/MS value '125'"));
        aConstantOptions.addOption(new Option("constant_qtrapcol3", true, "default variable - abi - tsv column 3 variable '10'"));

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
        if (aLine.hasOption("importtype") == false || aLine.hasOption("exporttype") == false) {
            logger.error("importtype and exporttype must be supplied!!");
            return false;
        }

        // Is the rtDelta specified?
        if (aLine.hasOption("rtdelta")) {
            String lRtdelta = aLine.getOptionValue("rtdelta");
            try {
                Double.parseDouble(lRtdelta);
            } catch (NumberFormatException e) {
                logger.error("rtdelta must be specified in minutes!! e.g.: --rtdelta 5.0");
            }
        }

        // Is the rtShift specified?
        if (aLine.hasOption("rtshift")) {
            String lRtShift = aLine.getOptionValue("rtshift");
            try {
                Double.parseDouble(lRtShift);
            } catch (NumberFormatException e) {
                logger.error("rtshift must be specified in minutes!! e.g.: --rtshift 5.0");
            }
        }


        // All is fine!
        return true;
    }
}
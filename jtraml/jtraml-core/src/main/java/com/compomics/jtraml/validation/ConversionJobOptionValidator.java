package com.compomics.jtraml.validation;

import com.compomics.jtraml.model.ConversionJobOptions;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * This class validates an instance of ConversionJobOptions.
 */
public class ConversionJobOptionValidator {

    private static Logger logger = Logger.getLogger(ConversionJobOptionValidator.class);
    /**
     * This variable keeps track of the validation status.
     */
    private static String iStatus = "";


    public static boolean isValid(ConversionJobOptions aConversionJobOptions) {
        File lOutputFile = aConversionJobOptions.getOutputFile();
        if (lOutputFile == null) {
            iStatus = "output file is null";
            return false;
        }

        File lInputFile = aConversionJobOptions.getInputFile();
        if (lInputFile == null) {
            iStatus = "input file is null";
            return false;
        }


        // else return true!
        iStatus = "";
        return true;
    }

    /**
     * Returns the status message that was defined during the last validation call.
     *
     * @return String report of the last valiation.
     */
    public static String getStatus() {
        return iStatus;
    }

}

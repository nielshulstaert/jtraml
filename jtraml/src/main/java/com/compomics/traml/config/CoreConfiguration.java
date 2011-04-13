package com.compomics.traml.config;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.File;

/**
 * This class is a Configuration hub for the Sep To Traml tool.
 */
public class CoreConfiguration {

    private static PropertiesConfiguration input;
    private static File iWorkSpace = null;

// -------------------------- STATIC METHODS --------------------------


    public static String getSeparationChar() {
        return input.getString("sep.char");
    }

    static {
        try {
            input = new PropertiesConfiguration("config/input.properties");
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

}

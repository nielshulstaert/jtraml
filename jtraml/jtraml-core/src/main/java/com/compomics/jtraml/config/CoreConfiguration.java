package com.compomics.jtraml.config;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * This class is a Configuration hub for the Sep To Traml tool.
 */
public class CoreConfiguration {

    /**
     * The current version of the library.
     */
    public final static String VERSION = "0.2";

    /**
     * Properties instance to manage the properties of the library.
     */
    private static PropertiesConfiguration input;

    /**
     * Static initiatlizer.
     */
    static {
        try {
            input = new PropertiesConfiguration("config/input.properties");
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

}
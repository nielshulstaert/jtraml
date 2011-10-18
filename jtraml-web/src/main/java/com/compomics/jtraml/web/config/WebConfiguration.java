package com.compomics.jtraml.web.config;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * This class is a Configuration hub for the Sep To Traml tool.
 */
public class WebConfiguration {

    /**
     * Properties instance to manage the properties of the library.
     */
    private static PropertiesConfiguration input;

    /**
     * Static initiatlizer.
     */
    static {
        try {
            input = new PropertiesConfiguration("config/jtraml-web.properties");
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the predefined home path when redirecting parameters.
     * @return
     */
    public static String getHomeURL(){
        return input.getString("jtraml.home.url");
    }

}

package com.compomics.jtraml.web.analytics;

import com.compomics.jtraml.model.ConversionJobOptions;
import com.compomics.jtraml.web.config.WebConfiguration;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * This class is a
 */
public class AnalyticsLogger {
    final static Logger analytics = Logger.getLogger("ANALYTICS");
    private static Logger logger = Logger.getLogger(AnalyticsLogger.class);
    private static final char sep = ';';

    private static boolean doAnalytics = WebConfiguration.doAnalytics();
    /**
     * Log a statement when a new session is created
     * @param aSessionID
     */
    public static void newSession(String aSessionID){
        logMetric(aSessionID + sep + "NEW_SESSION");
    }


    public static void startConversion(String aSessionID) {
        logMetric( aSessionID + sep + "NEW_CONVERSION");
    }

    public static void endConversion(String aSessionID, ConversionJobOptions aOptions) {
        logMetric( aSessionID + sep + "END_CONVERSION");
        logMetric( aSessionID + sep + "JOB_IMPORTTYPE" + sep + aOptions.getImportType().getName());
        logMetric( aSessionID + sep + "JOB_EXPORTTYPE" + sep + aOptions.getExportType().getName());
    }

    public static void startConversionViaURL(String aSessionID, Map aParams) {
        logMetric( aSessionID + sep + "NEW_URL_JOB");
        for (Object lParam: aParams.keySet()) {
            logMetric( aSessionID + sep + "NEW_URL_JOB_" + lParam.toString().toUpperCase() + sep + aParams.get(lParam).toString());
        }
    }

    private static void logMetric(String aMessage){
        if(doAnalytics){
            analytics.info(aMessage);
        }
    }
}

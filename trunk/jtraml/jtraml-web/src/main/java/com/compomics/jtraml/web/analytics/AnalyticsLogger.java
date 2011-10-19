package com.compomics.jtraml.web.analytics;

import com.compomics.jtraml.model.ConversionJobOptions;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * This class is a
 */
public class AnalyticsLogger {
    final static Logger analytics = Logger.getLogger("ANALYTICS");
    private static Logger logger = Logger.getLogger(AnalyticsLogger.class);
    private static final char sep = ';';

    /**
     * Log a statement when a new session is created
     * @param aSessionID
     */
    public static void newSession(String aSessionID){
        analytics.info(aSessionID + sep + "NEW_SESSION");
    }


    public static void startConversion(String aSessionID) {
        analytics.info( aSessionID + sep + "NEW_CONVERSION");
    }

    public static void endConversion(String aSessionID, ConversionJobOptions aOptions) {
        analytics.info( aSessionID + sep + "END_CONVERSION");
        analytics.info( aSessionID + sep + "JOB_IMPORTTYPE" + sep + aOptions.getImportType().getName());
        analytics.info( aSessionID + sep + "JOB_EXPORTTYPE" + sep + aOptions.getExportType().getName());
    }

    public static void startConversionViaURL(String aSessionID, Map aParams) {
        analytics.info( aSessionID + sep + "NEW_URL_JOB");
        for (Object lParam: aParams.keySet()) {
            analytics.info( aSessionID + sep + "NEW_URL_JOB_" + lParam.toString().toUpperCase() + sep + aParams.get(lParam));
        }
    }
}

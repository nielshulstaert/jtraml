package com.compomics.jtraml.web.listener;

import com.compomics.jtraml.enumeration.FileTypeEnum;
import com.compomics.jtraml.model.ConversionJobOptions;
import com.compomics.jtraml.web.TramlConverterApplication;
import com.compomics.jtraml.web.config.WebConfiguration;
import com.google.common.base.Joiner;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.ParameterHandler;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * This class is a
 */
public class FileParameterHandler extends Observable implements ParameterHandler {
    private static Logger logger = Logger.getLogger(FileParameterHandler.class);

    private final ConversionJobOptions iConversionJobOptions;
    private final TramlConverterApplication iApplication;
    private final Observer iObserver;
    public String PARAM_IMPORTTYPE = "importtype";
    public String PARAM_FILE = "input";

    /**
     * @param aConversionJobOptions
     * @param aApplication
     */
    public FileParameterHandler(ConversionJobOptions aConversionJobOptions, TramlConverterApplication aApplication, Observer aObserver) {
        iConversionJobOptions = aConversionJobOptions;
        iApplication = aApplication;
        iObserver = aObserver;
    }

    /**
     * Handles the given parameters. All parameters names are of type
     * {@link String} and the values are {@link String} arrays.
     *
     * @param parameters an unmodifiable map which contains the parameter names and
     *                   values
     */
    public void handleParameters(Map<String, String[]> parameters) {
        if (parameters.containsKey(PARAM_FILE)) {
            String lFileName = parameters.get("input")[0];
            File lFile = new File(lFileName);
            if (lFile.exists()) {
                logger.debug("found input file '" + lFileName + "'");
                String[] lImportType = parameters.get(PARAM_IMPORTTYPE);
                if (lImportType != null && lImportType[0].equals(FileTypeEnum.TRAML.getName())) {
                    logger.debug("setting TraML file to the ConversionJobOptions");
                    iConversionJobOptions.setImportType(FileTypeEnum.TRAML);
                    iConversionJobOptions.setInputFile(lFile);

                    logger.debug("notifying the Observers");
                    iObserver.update(this, lFile);
                    this.setChanged();

                    logger.debug("reloading the application");
                    iApplication.getMainWindow().open(new ExternalResource(WebConfiguration.getHomeURL()));
                    iApplication.getMainWindow().requestRepaintAll();

                } else {
                    logger.debug("importtype not specified");
                }
            } else {
                logger.debug("failed to open input file (" + lFileName + ")");
                return;
            }
        } else {
            if(parameters.size() > 0){
                logger.debug("ignoring the following URL parameters:" + Joiner.on(", ").join(parameters.keySet()));
            }
        }
    }
}

package com.compomics.jtraml.web.runner;

import com.compomics.jtraml.enumeration.FileTypeEnum;
import com.compomics.jtraml.model.ConversionJobOptions;
import com.compomics.jtraml.web.TramlConverterApplication;
import com.compomics.jtraml.web.components.UploadComponent;
import com.google.common.io.Files;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * This class is a
 */
public class FileImportParser implements Runnable {

    private static Logger logger = Logger.getLogger(FileImportParser.class);

    private final TramlConverterApplication iApplication;
    private final ConversionJobOptions iConversionJobOptions;
    private final UploadComponent iComponent;
    private final File iFile;

    public FileImportParser(TramlConverterApplication aApplication, ConversionJobOptions aConversionJobOptions, UploadComponent aComponent, String aFileName) throws IOException {
        iApplication = aApplication;
        iConversionJobOptions = aConversionJobOptions;
        iComponent = aComponent;
        iFile = new File(iApplication.getTempDir(), aFileName);
    }

    public void run() {
        try {
            String lFileName = iFile.getCanonicalPath();
            if (lFileName.toLowerCase().endsWith(".traml")) {
                iConversionJobOptions.setImportType(FileTypeEnum.TRAML);
            } else if (lFileName.toLowerCase().endsWith(".tsv")) {
                iConversionJobOptions.setImportType(FileTypeEnum.TSV_AGILENT_QQQ);
            } else if (lFileName.toLowerCase().endsWith(".csv")) {
                BufferedReader lReader = Files.newReader(iFile, Charset.defaultCharset());
                String lFirstLine = lReader.readLine();
                if (lFirstLine.startsWith("Q1")) {
                    iConversionJobOptions.setImportType(FileTypeEnum.TSV_THERMO_TSQ);
                } else if (lFirstLine.split(",").length == 5) {
                    iConversionJobOptions.setImportType(FileTypeEnum.TSV_ABI);
                }
            }

            iApplication.getMainWindow().requestRepaintAll();

        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}

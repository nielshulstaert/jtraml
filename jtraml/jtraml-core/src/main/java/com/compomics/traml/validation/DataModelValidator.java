package com.compomics.traml.validation;

import com.compomics.traml.enumeration.FileTypeEnum;
import com.compomics.traml.gui.TramlConverterMessaging;
import com.compomics.traml.gui.forms.ApplicationDataModel;
import com.compomics.traml.gui.forms.TramlConverterGUI;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * This class is a
 */
public class DataModelValidator {

    private static Logger logger = Logger.getLogger(DataModelValidator.class);
    private TramlConverterGUI iSepToTRAML;

    public DataModelValidator(TramlConverterGUI aSepToTRAML) {
        iSepToTRAML = aSepToTRAML;
    }

    public boolean isValidApplicationModel(ApplicationDataModel aModel){
        File lOutputFile = aModel.getOutputFile();
        if(lOutputFile == null){
            TramlConverterMessaging.error("output file is null");
            return false;
        }

        File lInputFile = aModel.getInputFile();
        if(lInputFile == null){
            TramlConverterMessaging.error("input file is null");
            return false;
        }

        FileTypeEnum lImportType = aModel.getImportType();
        FileTypeEnum lExportType = aModel.getExportType();

        if(lImportType == FileTypeEnum.TRAML){
            TramlConverterMessaging.error("the converter does not support traml import conversions");
            return false;
        }

        if(lImportType == FileTypeEnum.TSV_ABI){
            TramlConverterMessaging.error("the converter does not support tsv_abi import conversions");
            return false;
        }

        if(lExportType != FileTypeEnum.TRAML){
            TramlConverterMessaging.error("the converter only supports traml export conversions");
            return false;
        }

        // else return true!
        return true;
    }

}

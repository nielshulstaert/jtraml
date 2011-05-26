package com.compomics.jtraml.interfaces;

import org.hupo.psi.ms.traml.SourceFileListType;
import org.hupo.psi.ms.traml.TraMLType;

/**
 * This class is a generic model for a number of Transition specifying columns.
 */
public interface TSVFileImportModel {

    /**
     * Implementing classes must be capable of writing an array of rowvalues into a TramlType instance.
     * @param aTraMLType The TraMLType instance to store the rows into.
     * @param aRowValues The separates values from a single row.
     */
    public void addRowToTraml(TraMLType aTraMLType, String[] aRowValues);

    /**
     * Implementing classes must be capable to describe a SourceFileListType to insert into a TraMLType
     * @return SourceFileListType for the implementing converting classes.
     */
    public SourceFileListType getSourceTypeList();

    /**
     * Returns the separator character associated to this filemodel.
     * @return
     */
    public char getSeparator();
}



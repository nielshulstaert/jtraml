package com.compomics.traml.interfaces;

import org.hupo.psi.ms.traml.TraMLType;

/**
 * This class is a generic model for a number of Transition specifying columns.
 */
public interface RowModel {

    /**
     * Implementing classes must be capable of writing an array of rowvalues into a TramlType instance.
     * @param aTraMLType The TraMLType instance to store the rows into.
     * @param aRowValues The separates values from a single row.
     */
    public void addRowToTraml(TraMLType aTraMLType, String[] aRowValues);


}



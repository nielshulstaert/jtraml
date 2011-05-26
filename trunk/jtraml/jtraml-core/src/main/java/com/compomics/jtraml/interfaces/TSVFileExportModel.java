package com.compomics.jtraml.interfaces;

import org.hupo.psi.ms.traml.TraMLType;
import org.hupo.psi.ms.traml.TransitionType;

/**
 * This interface declares how separated file formats must be structured
 */
public interface TSVFileExportModel {

    /**
     * Returns whether this export model has a header.
     * @return
     */
    public boolean hasHeader();

    /**
     * Returns whether the header of this export model.
     * @return
     */
    public String getHeader();

   /**
     * Returns the separator character associated to this filemodel.
     * @return
     */
    public char getSeparator();

    /**
     * Append a TransitionType instance as a single line.
     */
    public String parseTransitionType(TransitionType aTransitionType, TraMLType aTraMLType);
}
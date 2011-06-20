package com.compomics.jtraml.interfaces;

import com.compomics.jtraml.MessageBean;
import org.hupo.psi.ms.traml.TraMLType;
import org.hupo.psi.ms.traml.TransitionType;

/**
 * This interface declares how separated file formats must be structured
 */
public interface TSVFileExportModel {

    /**
     * Returns whether this export model has a header.
     *
     * @return
     */
    public boolean hasHeader();

    /**
     * Returns whether the header of this export model.
     *
     * @return
     */
    public String getHeader();

    /**
     * Returns whether the given TransitionType is convertable.
     *
     * @return
     */
    public boolean isConvertable(TransitionType aTransitionType, TraMLType aTraMLType);

    /**
     * Returns the conversion Message if needed. Null if the TransitionType is convertable.
     *
     * @return
     */
    public MessageBean getConversionMessage();

    /**
     * Returns the separator character associated to this filemodel.
     *
     * @return
     */
    public char getSeparator();

    /**
     * Append a TransitionType instance as a single line.
     */
    public String parseTransitionType(TransitionType aTransitionType, TraMLType aTraMLType);

    /**
     * Add a constant CVParamType to be used by the ExportType
     */
    public void setRetentionTimeDelta(double aRetentionTimeWindow);
}
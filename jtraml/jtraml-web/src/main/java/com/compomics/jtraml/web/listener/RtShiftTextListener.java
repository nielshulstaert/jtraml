package com.compomics.jtraml.web.listener;

import com.compomics.jtraml.model.ConversionJobOptions;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.Layout;

/**
 * This class is a TextChangeListener implementation to oragnize changes to the Retention Time shift parameter.
 */
public class RtShiftTextListener implements FieldEvents.TextChangeListener {

    /**
     * Object handle to the parent Layout.
     */
    private Layout iParent;

    private ConversionJobOptions iConversionJobOptions;

    /**
     * Create a new Retenetion Time Shift listener.
     * @param aParent
     * @param aConversionJobOptions
     */
    public RtShiftTextListener(Layout aParent, ConversionJobOptions aConversionJobOptions) {
        iParent = aParent;
        iConversionJobOptions = aConversionJobOptions;
    }

    /**
     * {@inheritDoc}
     */
    public void textChange(FieldEvents.TextChangeEvent event) {
        if (event.getText().equals("")) {
            // Text is blank.
            iConversionJobOptions.setRtShift(Double.MIN_VALUE);
        } else {
            Double lShift;
            try {
                lShift = Double.parseDouble(event.getText());
                // No number format exception, so set the value.
                iConversionJobOptions.setRtShift(lShift);
            } catch (NumberFormatException ignored) {
                iParent.getWindow().showNotification("set a postive or negative number!!");
                iConversionJobOptions.setRtShift(Double.MIN_VALUE);
            }
        }
    }
}
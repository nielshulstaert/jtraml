package com.compomics.jtraml.web.listener;

import com.vaadin.data.Property;
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

    /**
     * Property handle to set the text changes.
     */
    private Property iProperty;

    /**
     * Create a new Retenetion Time Shift listener.
     * @param aParent
     * @param aProperty
     */
    public RtShiftTextListener(Layout aParent, Property aProperty) {
        iParent = aParent;
        iProperty = aProperty;
    }

    /**
     * {@inheritDoc}
     */
    public void textChange(FieldEvents.TextChangeEvent event) {
        if (event.getText().equals("")) {
            // Text is blank.
            iProperty.setValue("");
        } else {
            Double lShift;
            try {
                lShift = Double.parseDouble(event.getText());
                // No number format exception, so set the value.
                iProperty.setValue(lShift);
            } catch (NumberFormatException ignored) {
                iParent.getWindow().showNotification("set a postive or negative number!!");
                iProperty.setValue("");
            }
        }
    }
}
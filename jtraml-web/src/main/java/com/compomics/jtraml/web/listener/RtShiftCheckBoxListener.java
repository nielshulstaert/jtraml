package com.compomics.jtraml.web.listener;

import com.compomics.jtraml.model.ConversionJobOptions;
import com.vaadin.ui.Button;

/**
 * This class is a
 */
public class RtShiftCheckBoxListener implements Button.ClickListener {

    private ConversionJobOptions iConversionJobOptions;

    public RtShiftCheckBoxListener(ConversionJobOptions aConversionJobOptions) {
        iConversionJobOptions = aConversionJobOptions;
    }

    /**
     * Called when a {@link com.vaadin.ui.Button} has been clicked. A reference to the
     * button is given by {@link com.vaadin.ui.Button.ClickEvent#getButton()}.
     *
     * @param event An event containing information about the click.
     */
    public void buttonClick(Button.ClickEvent event) {
        if(((Boolean)event.getButton().getValue()) == false){
            iConversionJobOptions.setRtShift(Double.MIN_VALUE);
        }
    }
}

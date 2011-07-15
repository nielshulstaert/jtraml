package com.compomics.jtraml.web.components;

import com.compomics.jtraml.web.TramlConverterApplication;
import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Link;

/**
 * This class is a
 */
public class InfoLink extends Link {

    public enum InfoPages{RETENTION_CONVERSION};

    /**
     * Creates a new push button. The value of the push button is false and it
     * is immediate by default.
     */
    public InfoLink() {
        super();
        setResource(new ExternalResource("http://code.google.com/p/jtraml/wiki/converter", "_blank"));
        setDescription("jTraML Wiki - Conversion Formats");
        setIcon(new ClassResource("/images/info.gif", TramlConverterApplication.getApplication()));
    }

    /**
     * Creates a new push button. The value of the push button is false and it
     * is immediate by default.
     */
    public InfoLink(InfoPages aInfoPage) {
        super();
        setDescription("jTraML Wiki - Conversion Formats");
        setIcon(new ClassResource("/images/info.gif", TramlConverterApplication.getApplication()));
        if(aInfoPage.equals(InfoPages.RETENTION_CONVERSION)){
            setResource(new ExternalResource("http://code.google.com/p/jtraml/wiki/converter?updated=converter#TraML_Converter_Retention_Time_Conversion_Scenarios"));
        }else{
            setResource(new ExternalResource("http://code.google.com/p/jtraml/wiki/converter", "_blank"));
        }

    }
}

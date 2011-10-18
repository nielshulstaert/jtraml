package com.compomics.jtraml.web.components;

import com.compomics.jtraml.web.TramlConverterApplication;
import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Link;

/**
 * This class is a
 */
public class InfoLink extends Link {

    private TramlConverterApplication iApplication;

    public enum InfoPages{RETENTION_CONVERSION};

    /**
     * Creates a new push button. The value of the push button is false and it
     * is immediate by default.
     */
    public InfoLink(TramlConverterApplication aApplication) {
        super();
        iApplication = aApplication;
        setResource(new ExternalResource("http://code.google.com/p/jtraml/wiki/converter", "_blank"));
        setDescription("jTraML Wiki - Conversion Formats");
        setIcon(new ClassResource("/images/info.gif", iApplication));
    }

    /**
     * Creates a new push button. The value of the push button is false and it
     * is immediate by default.
     */
    public InfoLink(TramlConverterApplication aApplication, InfoPages aInfoPage) {
        super();
        iApplication = aApplication;
        setDescription("jTraML Wiki - Conversion Formats");
        setIcon(new ClassResource("/images/info.gif", iApplication));
        if(aInfoPage.equals(InfoPages.RETENTION_CONVERSION)){
            setResource(new ExternalResource("http://code.google.com/p/jtraml/wiki/converter?updated=converter#TraML_Converter_Retention_Time_Conversion_Scenarios"));
        }else{
            setResource(new ExternalResource("http://code.google.com/p/jtraml/wiki/converter", "_blank"));
        }

    }
}

package com.compomics.jtraml.web.container;


import com.compomics.jtraml.config.CoreConfiguration;
import com.compomics.jtraml.web.TramlConverterApplication;
import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class HeaderPanel extends com.vaadin.ui.HorizontalLayout {
    public HeaderPanel() {
        setSizeUndefined();
        setMargin(true);
        setSpacing(true);

        Link l = new Link();
        l.setResource(new ExternalResource("http://jtraml.googlecode.com"));
        l.setDescription("go to jTraML googlecode website");
        l.setIcon(new ClassResource("/images/jtraml_icon.png", TramlConverterApplication.getApplication()));
        addComponent(l);

        ComponentContainer labels = new VerticalLayout();
        Label title = new Label("<h1>TraML Converter v0.3</h1>");
        title.setContentMode(Label.CONTENT_XHTML);
        Label subtitle = new Label("<em>based on TraML XSD version " + CoreConfiguration.TRAML_VERSION + " </em>");
        subtitle.setContentMode(Label.CONTENT_XHTML);

        labels.addComponent(title);
        labels.addComponent(subtitle);

        addComponent(labels);
    }
}
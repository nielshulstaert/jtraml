package com.compomics.jtraml.web.panel;


import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;

@SuppressWarnings("serial")
public class HeaderPanel extends com.vaadin.ui.HorizontalLayout{


    public HeaderPanel() {
        setSizeUndefined();
        setMargin(true);
        setSpacing(true);

        Link l = new Link();
        l.setResource(new ExternalResource("http://www.psidev.info"));
        l.setDescription("go to psi website");
        l.setIcon(new ExternalResource("http://www.psidev.info/files/psi.gif"));

        addComponent(l);

        Label title = new Label("<h1>TraML Converter v0.1</h1>");
        title.setContentMode(Label.CONTENT_XHTML);
        addComponent(title);


    }

}
package com.compomics.jtraml.web.components;


import com.compomics.jtraml.web.TramlConverterApplication;
import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Link;
import com.vaadin.ui.themes.Reindeer;

@SuppressWarnings("serial")
public class FooterPanel extends com.vaadin.ui.HorizontalLayout {


    public FooterPanel() {


        // Create a grid layout
        HorizontalLayout horiz = new HorizontalLayout();
        horiz.setStyleName(Reindeer.LAYOUT_WHITE);

        horiz.setSpacing(true);
        horiz.setWidth("100%");

        Link l = new Link();
        l.setResource(new ExternalResource("http://www.psidev.info"));
        l.setDescription("go to psi website");
        l.setIcon(new ExternalResource("http://www.psidev.info/files/psi.gif"));
        addComponent(l);

        l = new Link();
        l.setResource(new ExternalResource("http://www.compomics.com/"));
        l.setDescription("go to psi website");
        l.setIcon(new ClassResource("/images/compomics.png", TramlConverterApplication.getApplication()));

        horiz.addComponent(l);
        horiz.setComponentAlignment(l, Alignment.BOTTOM_RIGHT);

        addComponent(horiz);
    }

}
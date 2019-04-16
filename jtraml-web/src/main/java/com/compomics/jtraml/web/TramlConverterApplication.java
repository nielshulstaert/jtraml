/*
 * Copyright 2009 IT Mill Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.compomics.jtraml.web;


import com.compomics.jtraml.model.ConversionJobOptions;
import com.compomics.jtraml.web.analytics.AnalyticsLogger;
import com.compomics.jtraml.web.container.ConversionForm;
import com.compomics.jtraml.web.container.FooterPanel;
import com.compomics.jtraml.web.container.HeaderPanel;
import com.compomics.jtraml.web.container.ResultsPanel;
import com.compomics.jtraml.web.listener.FileParameterHandler;
import com.google.common.io.Files;
import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Window;
import org.apache.log4j.Logger;
import org.vaadin.googleanalytics.tracking.GoogleAnalyticsTracker;

import javax.servlet.http.HttpSession;
import java.io.File;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class TramlConverterApplication extends Application {
    private static Logger logger = Logger.getLogger(TramlConverterApplication.class);

    private Window window;
    public File iTempDir;
    public ResultsPanel iOutputTable;
    public ConversionForm iInputForm;
    public Panel iSeparatorPanel;
    public FileParameterHandler iParameterHandler;
    public String iSessionID;

    @Override
    public void init() {
        logger.debug("opening new jTraML session");
        // initiate the window
        window = new Window("TraML converter");

        setTheme("jtraml");

        // Create a temporary folder for this application
        iTempDir = Files.createTempDir();

        initLayout();

        // Create a tracker for vaadin.com domain.
        GoogleAnalyticsTracker tracker = new GoogleAnalyticsTracker("UA-23742568-1", "ugent.be");

        // Create a parameter handler for
        iParameterHandler = new FileParameterHandler(iInputForm.getConversionJobOptions(), this, iInputForm);
        window.addParameterHandler(iParameterHandler);

        // Add only one tracker per window.
        window.addComponent(tracker);

        // Track the page view
        tracker.trackPageview("/jtraml");
        tracker.trackPageview("/jtraml2");

        setMainWindow(window);

        parseSessionId();

    }

    private void parseSessionId() {
        WebApplicationContext ctx = null;
        HttpSession session = null;
        try {
            ctx = ((WebApplicationContext) getContext());
            session = ctx.getHttpSession();
        } catch (ClassCastException cce) {
            logger.error(cce.getMessage(), cce);
        }

        if(session != null){
            iSessionID = session.getId();
        }else{
            iSessionID = "TIMESTAMP_ID_" + System.currentTimeMillis();
        }

        AnalyticsLogger.newSession(iSessionID);
    }

    /**
     * Initiate the main layout.
     */
    private void initLayout() {
        window.addComponent(new HeaderPanel(this));

        iInputForm = new ConversionForm(this);

        iOutputTable = new ResultsPanel(this);


        iSeparatorPanel = new Panel();
        iSeparatorPanel.setStyleName("v-split-line");


        if (iOutputTable.getNumberOfResults() == 0) {
            iOutputTable.setVisible(false);
            iSeparatorPanel.setVisible(false);
        }

        GridLayout grid = new GridLayout(3, 1);

        grid.addComponent(iInputForm, 0, 0);
        grid.addComponent(iSeparatorPanel, 1, 0);
        grid.addComponent(iOutputTable, 2, 0);

        window.addComponent(grid);
        window.addComponent(new FooterPanel(this));
    }

    public File getTempDir() {
        return iTempDir;
    }

    public void addResult(ConversionJobOptions aConversionJobOptions) {
        iOutputTable.addItem(aConversionJobOptions);
        if (iOutputTable.getNumberOfResults() > 0) {
            iOutputTable.setVisible(true);
            iSeparatorPanel.setVisible(true);
            iOutputTable.requestRepaintAll();
        }
    }

    /**
     * Returns the session id of the current application
     * @return
     */
    public String getSessionID() {
        return iSessionID;
    }
}

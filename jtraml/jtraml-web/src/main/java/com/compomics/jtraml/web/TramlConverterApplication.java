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
import com.compomics.jtraml.web.container.FooterPanel;
import com.compomics.jtraml.web.container.HeaderPanel;
import com.compomics.jtraml.web.container.*;
import com.compomics.jtraml.web.container.ConversionForm;
import com.google.common.io.Files;
import com.vaadin.Application;
import com.vaadin.ui.*;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;
import org.vaadin.googleanalytics.tracking.GoogleAnalyticsTracker;

import java.io.File;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class TramlConverterApplication extends Application {
    private Window window;
    Label header;
    public File iTempDir;

    public static TramlConverterApplication iApplication = null;
    public ResultsPanel iOutputTable;
    public ConversionForm iInputForm;
    public Panel iSeparatorPanel;

    @Override
    public void init() {
        iApplication = this;
        // initiate the window
        window = new Window("TraML converter");
        setMainWindow(window);

        setTheme("jtraml");

        // Create a temporary folder for this application
        iTempDir = Files.createTempDir();

        initLayout();

        // Create a tracker for vaadin.com domain.
        GoogleAnalyticsTracker tracker = new GoogleAnalyticsTracker("UA-23742568-1", "http://iomics.ugent.be/jtraml/");

        // Add only one tracker per window.
        window.addComponent(tracker);

        // Track the page view
//        tracker.trackPageview("/jtraml/analytics");


    }

    /**
     * Initiate the main layout.
     */
    private void initLayout() {
        window.addComponent(new HeaderPanel());

        iInputForm = new ConversionForm();
        iOutputTable = new ResultsPanel();


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
        window.addComponent(new FooterPanel());
    }

    public File getTempDir() {
        return iTempDir;
    }

    public static TramlConverterApplication getApplication() {
        return iApplication;
    }

    public void addResult(ConversionJobOptions aConversionJobOptions) {
        iOutputTable.addItem(aConversionJobOptions);
        if (iOutputTable.getNumberOfResults() > 0) {
            iOutputTable.setVisible(true);
            iSeparatorPanel.setVisible(true);
            iOutputTable.requestRepaintAll();
        }
    }
}

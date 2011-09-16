package com.compomics.jtraml.web.container;

import com.compomics.jtraml.model.ConversionJobOptions;
import com.compomics.jtraml.web.TramlConverterApplication;
import com.google.common.io.Files;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.Link;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * This class presents the conversion results within a single session.
 */
public class ResultsPanel extends VerticalLayout {

    /**
     * The container manages the conversion jobs.
     */
    private IndexedContainer iConversionData;

    /**
     * The Table component to display the conversion jobs.
     */
    private Table iConversionTable = new Table("TraML Converter Results Table");

    /**
     * The visible columns for the Table.
     */
    private static String[] visibleCols = new String[]{"Download", "Filename", "Input", "Output", "Date"};

    /**
     * The results counter displayed in the Table.
     */
    public int iItemCounter = 0;


    /**
     * Construct a new ResultsPanel to display the conversion job results.
     */
    public ResultsPanel() {
        super();
        initLayout();
        initResultsTable();
    }

    /**
     * Table initition method.
     *
     * @return
     */
    private String[] initResultsTable() {
        iConversionData = new IndexedContainer();

        iConversionData.addContainerProperty("Download", Link.class, null);
        iConversionData.addContainerProperty("Filename", String.class, "");
        iConversionData.addContainerProperty("Input", String.class, "");
        iConversionData.addContainerProperty("Output", String.class, "");
        iConversionData.addContainerProperty("Date", String.class, "");

        iConversionTable.setContainerDataSource(iConversionData);
//        iConversionTable.setVisibleColumns(visibleCols);
        iConversionTable.setSelectable(true);
        iConversionTable.setImmediate(true);
        iConversionTable.setWidth("100%");
        iConversionTable.setHeight("100px");

        return visibleCols;
    }

    /**
     * Layout initiation method.
     */
    private void initLayout() {
        addComponent(iConversionTable);
        setExpandRatio(iConversionTable, 1);
        iConversionTable.setSizeFull();
    }

    /**
     * Add the results of a conversion job to the Table.
     *
     * @param aConversionJobOptions
     */
    public void addItem(ConversionJobOptions aConversionJobOptions) {
        iItemCounter++;
        Object id = iConversionTable.addItem();

        // String values.
        iConversionTable.getContainerProperty(id, "Filename").setValue(aConversionJobOptions.getInputFile().getName());
        iConversionTable.getContainerProperty(id, "Input").setValue(aConversionJobOptions.getImportType().getName());
        iConversionTable.getContainerProperty(id, "Output").setValue(aConversionJobOptions.getExportType().getName());
        iConversionTable.getContainerProperty(id, "Date").setValue(new Date(System.currentTimeMillis()).toString());

        // Download link.
        File lOutputFile = aConversionJobOptions.getOutputFile();
        try {
            final InputStream is = Files.newInputStreamSupplier(lOutputFile).getInput();

            StreamResource.StreamSource lStreamSource = new StreamResource.StreamSource() {
                public InputStream getStream() {
                    return is;
                }
            };
            StreamResource lStreamResource = new StreamResource(lStreamSource, lOutputFile.getName(), TramlConverterApplication.getApplication());

            Link l = new Link("Download", lStreamResource);
            iConversionTable.getContainerProperty(id, "Download").setValue(l);

        } catch (IOException e) {
        }
    }

    /**
     * Return the number of conversion results displayed in the table.
     * @return
     */
    public int getNumberOfResults() {
        return iItemCounter;
    }
}

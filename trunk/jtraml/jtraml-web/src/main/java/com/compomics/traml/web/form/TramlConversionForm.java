package com.compomics.traml.web.form;

import com.compomics.traml.enumeration.FileTypeEnum;
import com.compomics.traml.interfaces.FileModel;
import com.compomics.traml.model.rowmodel.AgilentQQQImpl;
import com.compomics.traml.model.rowmodel.ThermoTSQImpl;
import com.compomics.traml.thread.TSVToTRAMLJob;
import com.compomics.traml.web.TramlConverterApplication;
import com.compomics.traml.web.data.ConversionItem;
import com.compomics.traml.web.panel.MyUpload;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executors;

/**
 * This class is a
 */
public class TramlConversionForm extends VerticalLayout implements Observer {

    ConversionItem lConversion;

    private static final String COMMON_FIELD_WIDTH = "12em";
    public ProgressIndicator iProgressIndicator;
    public Button btnConvert;
    public File iRunningOutputFile;
    public Form iConversionForm;

    public TramlConversionForm() {
        iProgressIndicator = new ProgressIndicator();
        iProgressIndicator.setIndeterminate(true);
        iProgressIndicator.setPollingInterval(1000);
        iProgressIndicator.setEnabled(false);

        lConversion = new ConversionItem(); // a person POJO
        BeanItem<ConversionItem> lConversionItem = new BeanItem<ConversionItem>(lConversion); // item from
        // POJO

        // Create the Form
        iConversionForm = new Form();
        iConversionForm.setWriteThrough(false); // we want explicit 'btnConvert'
        iConversionForm.setInvalidCommitted(false); // no invalid values in datamodel

        // FieldFactory for customizing the fields and adding validators
        iConversionForm.setFormFieldFactory(new ConversionFieldFactory());
        iConversionForm.setItemDataSource(lConversionItem); // bind to POJO via BeanItem

        // Determines which properties are shown, and in which order:
        iConversionForm.setVisibleItemProperties(Arrays.asList(new String[]{
                "inputFile", "importType", "exportType"}));

        // Add form to layout
        addComponent(iConversionForm);

        iConversionForm.setValidationVisible(false);
        iConversionForm.setValidationVisibleOnCommit(false);

        // The cancel / btnConvert buttons
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);

        btnConvert = new Button("Convert", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                try {
                    iConversionForm.commit();
                    startConversion();
                } catch (Exception e) {
                    e.printStackTrace();
                    // Ignored, we'll let the Form handle the errors
                }
            }
        });

        buttons.addComponent(btnConvert);
        buttons.addComponent(iProgressIndicator);

        iConversionForm.getFooter().addComponent(buttons);
        iConversionForm.getFooter().setMargin(false, false, true, true);

    }

    private void startConversion() throws IOException {
        if (iConversionForm.isValid()) {
            iProgressIndicator.setEnabled(true);
            iProgressIndicator.setImmediate(true);
            iProgressIndicator.setVisible(true);

            FileTypeEnum lInputtype = lConversion.getImportType();
            FileModel lFileModel = null;
            File lInputFile = lConversion.getInputFile();

            if (lInputtype.equals(FileTypeEnum.TSV_ABI)) {
                getWindow().showNotification("ABI TSV input is not yet implemented", Window.Notification.TYPE_WARNING_MESSAGE);
                System.exit(1);

            } else if (lInputtype.equals(FileTypeEnum.TSV_THERMO_TSQ)) {
                getWindow().showNotification("starting conversion from Thermo TSQ to TraML", Window.Notification.TYPE_HUMANIZED_MESSAGE);
                lFileModel = new ThermoTSQImpl(lInputFile);

            } else if (lInputtype.equals(FileTypeEnum.TSV_AGILENT_QQQ)) {
                getWindow().showNotification("starting conversion from Agilent QQQ to TraML", Window.Notification.TYPE_HUMANIZED_MESSAGE);
                lFileModel = new AgilentQQQImpl(lInputFile);
            }

            makeOutputFile();

            TSVToTRAMLJob job = new TSVToTRAMLJob(lFileModel, lInputFile, iRunningOutputFile);
            job.addObserver(this);

            Executors.newSingleThreadExecutor().submit(job);

            iProgressIndicator.setEnabled(true);
            iProgressIndicator.setVisible(true);
            btnConvert.setEnabled(false);
        }


//        getWindow().showNotification("finished conversion", Window.Notification.TYPE_TRAY_NOTIFICATION);

    }

    private void makeOutputFile() throws IOException {
        File lInputFile = lConversion.getInputFile();
        String lInputFileName = lInputFile.getName();
        lInputFileName = lInputFileName.substring(0, lInputFileName.lastIndexOf("."));

        String lOutputFileName = lInputFileName + "_converted" + lConversion.getExportType().getExtension();

        iRunningOutputFile = new File(lInputFile.getParentFile(), lOutputFileName);
        iRunningOutputFile.createNewFile();

    }

    public void update(Observable aObservable, Object o) {
        lConversion.setOutputFile(iRunningOutputFile);
        TramlConverterApplication.getApplication().addResult(lConversion);

        iProgressIndicator.setEnabled(false);
        iProgressIndicator.setVisible(false);
        btnConvert.setEnabled(true);

    }


    private class ConversionFieldFactory extends DefaultFieldFactory {

        ComboBox importTypes = null;
        ComboBox exportTypes = null;

        public ConversionFieldFactory() {

            importTypes = makeConversionTypeComboBox();
            importTypes.setCaption("import type");

            exportTypes = makeConversionTypeComboBox();
            exportTypes.setCaption("export type");
        }

        private ComboBox makeConversionTypeComboBox() {

            ComboBox lBox = new ComboBox();
            FileTypeEnum[] lValues = FileTypeEnum.values();

            for (FileTypeEnum lValue : lValues) {
                lBox.addItem(lValue);
            }

            lBox.setWidth(COMMON_FIELD_WIDTH);
            lBox.setFilteringMode(ComboBox.FILTERINGMODE_STARTSWITH);
            lBox.setRequired(true);

            return lBox;
        }

        @Override
        public Field createField(Item item, Object propertyId,
                                 Component uiContext) {
            Field f = null;

            if ("exportType".equals(propertyId)) {
                // filtering ComboBox w/ country names
                return exportTypes;
            } else if ("importType".equals(propertyId)) {
                return importTypes;
            } else if ("inputFile".equals(propertyId)) {
                return new MyUpload();
            } else {
//                Do nothing.
                f = super.createField(item, propertyId, uiContext);
            }
            return f;
        }


    }
}

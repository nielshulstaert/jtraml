package com.compomics.jtraml.web.components;


import com.compomics.jtraml.MessageBean;
import com.compomics.jtraml.enumeration.FileTypeEnum;
import com.compomics.jtraml.model.ConversionJobOptions;
import com.compomics.jtraml.thread.SepToTRAMLJob;
import com.compomics.jtraml.thread.TRAMLToSepJob;
import com.compomics.jtraml.validation.ConversionJobOptionValidator;
import com.compomics.jtraml.web.TramlConverterApplication;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class is a
 */
public class TramlConversionForm extends VerticalLayout implements Observer {
    private static Logger logger = Logger.getLogger(TramlConversionForm.class);

    ConversionJobOptions iConversionJobOptions;

    private static final String COMMON_FIELD_WIDTH = "12em";
    public ProgressIndicator iProgressIndicator;
    public Button btnConvert;
    public Button btnCancel;

    public Form iConversionForm;

    private boolean isCancelled = false;
    public ExecutorService iExecutorService;

    public TramlConversionForm() {
        iProgressIndicator = new ProgressIndicator();
        iProgressIndicator.setIndeterminate(true);
        iProgressIndicator.setPollingInterval(1000);
        iProgressIndicator.setEnabled(false);

        iConversionJobOptions = new ConversionJobOptions(); // a person POJO
        BeanItem<ConversionJobOptions> lConversionItem = new BeanItem<ConversionJobOptions>(iConversionJobOptions); // item from
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


        btnCancel = new Button("Cancel", new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                try {
                    if (iExecutorService != null) {
                        iExecutorService.shutdownNow();
                        getWindow().showNotification("Cancelled conversion process");
                        isCancelled = true;
                    }
                    resetButtons();
                } catch (Exception e) {
                    e.printStackTrace();
                    // Ignored, we'll let the Form handle the errors
                }
            }
        });
        btnCancel.setVisible(false);

        buttons.addComponent(btnConvert);
        buttons.addComponent(new InfoLink());
        buttons.addComponent(iProgressIndicator);
        buttons.addComponent(btnCancel);

        iConversionForm.getFooter().addComponent(buttons);
        iConversionForm.getFooter().setMargin(false, false, true, true);

    }

    private void startConversion() throws IOException {

        if (iConversionForm.isValid()) {
            // Reset any previous cancel states.
            isCancelled = false;

            // create a new outputf file for the upcomming Thread.
            File lOutputFile = makeOutputFile();
            iConversionJobOptions.setOutputFile(lOutputFile);

            boolean valid = ConversionJobOptionValidator.isValid(iConversionJobOptions);

            if (valid) {
                iProgressIndicator.setEnabled(true);
                iProgressIndicator.setImmediate(true);
                iProgressIndicator.setVisible(true);

                // create the job, listen for the finish, and start the job!
                iExecutorService = Executors.newSingleThreadExecutor();
                if (iConversionJobOptions.getImportType() != FileTypeEnum.TRAML) {
                    SepToTRAMLJob job = new SepToTRAMLJob(iConversionJobOptions);
                    job.addObserver(this);
                    iExecutorService.submit(job);

                } else {
                    TRAMLToSepJob job = new TRAMLToSepJob(iConversionJobOptions);
                    job.setGraphical(true);
                    job.addObserver(this);
                    iExecutorService.submit(job);
                }

            } else {
                // else, the validation went wrong - notify the user about what went wrong..
                getWindow().showNotification(ConversionJobOptionValidator.getStatus(), Window.Notification.TYPE_WARNING_MESSAGE);
            }

            // Update the buttons and indicators
            iProgressIndicator.setEnabled(true);
            iProgressIndicator.setVisible(true);
            btnConvert.setVisible(false);
            btnCancel.setVisible(true);

            logger.info("requesting repaint");
            requestRepaintAll();
        }
    }

    private File makeOutputFile() throws IOException {
        File lInputFile = iConversionJobOptions.getInputFile();
        File lOutputFile = null;

        // parse the inputfilename.
        String lInputFileName;
        lInputFileName = lInputFile.getName();
        lInputFileName = lInputFileName.substring(0, lInputFileName.lastIndexOf("."));

        // create the outputfilename
        String lOutputFileName;
        lOutputFileName = lInputFileName + "_converted" + iConversionJobOptions.getExportType().getExtension();

        // create the outputfile.
        lOutputFile = new File(lInputFile.getParentFile(), lOutputFileName);
        lOutputFile.createNewFile();

        return lOutputFile;
    }

    public void update(Observable aObservable, final Object o) {
        if (o instanceof MessageBean) {
            final MessageBean lMessageBean = (MessageBean) o;

            new InputDialog(getWindow(), lMessageBean.getMessage(),
                    new InputDialog.Recipient() {
                        public void gotInput(String input) {
                            lMessageBean.getInterruptible().proceed(input);
                        }
                    }, lMessageBean.isRequiresAnswer());
        } else {
            if (isCancelled == false) {
                TramlConverterApplication.getApplication().addResult(iConversionJobOptions);

                Field lField = iConversionForm.getField("inputFile");
                if (lField instanceof UploadField) {
                    UploadField lUploadField = (UploadField) lField;
                    lUploadField.reset();
                }

                iConversionJobOptions.setInputFile(null);
                iConversionJobOptions.setOutputFile(null);

                resetButtons();
            }
        }
    }

    /**
     * Reset the convert/cancel buttons and the progressbar.
     * This method is called either when the Observable Job calls for an update or when the Job is cancelled.
     */
    private void resetButtons() {

        iProgressIndicator.setEnabled(false);
        iProgressIndicator.setVisible(false);

        btnConvert.setVisible(true);
        btnCancel.setVisible(false);

        requestRepaintAll();
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
                return new UploadField();
            } else {
//                Do nothing.
                f = super.createField(item, propertyId, uiContext);
            }
            return f;
        }


    }
}

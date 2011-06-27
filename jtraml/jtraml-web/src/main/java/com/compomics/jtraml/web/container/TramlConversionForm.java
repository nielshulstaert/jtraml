package com.compomics.jtraml.web.container;


import com.compomics.jtraml.MessageBean;
import com.compomics.jtraml.enumeration.FileTypeEnum;
import com.compomics.jtraml.model.ConversionJobOptions;
import com.compomics.jtraml.thread.SepToTRAMLJob;
import com.compomics.jtraml.thread.TRAMLToSepJob;
import com.compomics.jtraml.validation.ConversionJobOptionValidator;
import com.compomics.jtraml.web.TramlConverterApplication;
import com.compomics.jtraml.web.components.*;
import com.compomics.jtraml.web.dialog.InputDialog;
import com.compomics.jtraml.web.listener.RtShiftTextListener;
import com.compomics.jtraml.web.validate.RtShiftValidator;
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
 * This Form takes the input to start a TraML Conversion Task.
 */
public class TramlConversionForm extends VerticalLayout implements Observer {
    private static Logger logger = Logger.getLogger(TramlConversionForm.class);

    /**
     * This instance keeps track of all conversion parameters.
     */
    ConversionJobOptions iConversionJobOptions;


    /**
     * Layout properties.
     */
    private static final String COMMON_FIELD_WIDTH = "12em";


    /**
     * The actual form.
     */
    public Form iConversionForm;

    /**
     * Start/Cancel buttons.
     */
    public Button btnConvert, btnCancel;

    /**
     * Layout components.
     */
    public ProgressIndicator iProgressIndicator;
    private UploadField iUploadField;
    private CheckBoxTextField iRtShiftCheckboxTextField;

    /**
     * Keep track of the cancellation state.
     */
    private boolean isCancelled = false;

    /**
     * ExcecutorService to launch new conversion Threads.
     */
    public ExecutorService iExecutorService;


    /**
     * Create a new TraML Conversion Form.
     */
    public TramlConversionForm() {

        // Initiate the progress bar.
        iProgressIndicator = new ProgressIndicator();
        iProgressIndicator.setIndeterminate(true);
        iProgressIndicator.setPollingInterval(1000);
        iProgressIndicator.setEnabled(false);

        // Initiate the ConversionJobOptions,
        iConversionJobOptions = new ConversionJobOptions(); // a person POJO

        // and create a BeanItem for it's getters/setters.
        BeanItem<ConversionJobOptions> lConversionItem = new BeanItem<ConversionJobOptions>(iConversionJobOptions); // item from

        iConversionForm = new Form(); // create the form
        iConversionForm.setWriteThrough(false); // we want explicit 'btnConvert'
        iConversionForm.setInvalidCommitted(false); // no invalid values in datamodel
        iConversionForm.getLayout().setMargin(false);
        ((FormLayout) iConversionForm.getLayout()).setSpacing(false);


        // First add The upload field.
        iUploadField = new UploadField(iConversionJobOptions);
        iConversionForm.getLayout().addComponent(iUploadField);

        // FieldFactory for customizing the fields and adding validators
        iConversionForm.setFormFieldFactory(new ConversionFieldFactory());
        iConversionForm.setItemDataSource(lConversionItem); // bind to POJO via BeanItem

        // Add pojo and filter based components
        iConversionForm.setVisibleItemProperties(Arrays.asList(new String[]{"importType", "exportType"}));

        // Add expert components.
        // CheckBoxTextField for the RetentionTimeShift option.
        iRtShiftCheckboxTextField = new CheckBoxTextField("±x min", "fill in the retention time shift");
        iRtShiftCheckboxTextField.addTextFieldValidation(new RtShiftValidator());
        iRtShiftCheckboxTextField.setCaption("retention shift");
        iRtShiftCheckboxTextField.addTextFieldListener(
                new RtShiftTextListener(
                        this,
                        iRtShiftCheckboxTextField));
        iConversionForm.getLayout().addComponent(iRtShiftCheckboxTextField);// Add to Form.


        // Add form to layout
        addComponent(iConversionForm);

        // Disable the validation icons.
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

    /**
     * This method will try to start a new Conversion process.
     *
     * @throws IOException
     */
    private void startConversion() throws IOException {

        // First, verify if the form is valid.
        if (iConversionForm.isValid()) {

            // Reset the cancel status.
            isCancelled = false;

            // Create a new output file for the upcomming Thread.
            File lOutputFile = makeOutputFile();
            iConversionJobOptions.setOutputFile(lOutputFile);

            // Verify whether the filled in form values are valid for conversion.
            boolean valid = ConversionJobOptionValidator.isValid(iConversionJobOptions);
            if (valid) {

                // Ok! Start the conversion!
                iProgressIndicator.setEnabled(true);
                iProgressIndicator.setImmediate(true);
                iProgressIndicator.setVisible(true);

                // Create the job, listen for the finish, and start the job!
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

                // Update the buttons and indicators
                iProgressIndicator.setEnabled(true);
                iProgressIndicator.setVisible(true);
                btnConvert.setVisible(false);
                btnCancel.setVisible(true);

                logger.info("requesting repaint");
                requestRepaintAll();

            } else {
                // Else, the conversion validation went wrong - notify the user about what went wrong..
                Window.Notification lNotification = new Window.Notification(ConversionJobOptionValidator.getStatus(), Window.Notification.TYPE_WARNING_MESSAGE);
                lNotification.setDelayMsec(-1);
                getWindow().showNotification(lNotification);
            }
        }
    }

    /**
     * Convenience method to create a temporary output file named like the input file.
     *
     * @return
     * @throws IOException
     */
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

    /**
     * The form can be updated by its observers.
     * A. The Job has been completed, and the results table must be updated
     * B. The Job requires additional user input.
     *
     * @param aObservable - The broadcasting observable instance.
     * @param o           - The object along the message.
     */
    public void update(Observable aObservable, final Object o) {

        /**
         * The Observable instance has submitted a MessageBean that should be displayed.
         */
        if (o instanceof MessageBean) {
            final MessageBean lMessageBean = (MessageBean) o;

            // The Recipient will listen for user input.
            InputDialog.Recipient lRecipient = new InputDialog.Recipient() {
                public void gotInput(String input) {
                    lMessageBean.getInterruptible().proceed(input);
                }
            };
            // Open a new InputDialog, and wait for its return.
            new InputDialog(getWindow(), lMessageBean.getMessage(),
                    lRecipient, lMessageBean.isRequiresAnswer());

        } else {
            // Any way else, the job has been finished!
            if (isCancelled == false) { // Verify that the job was not cancelled in meanwhile.

                try {
                    // Make a copy of the options instance.
                    ConversionJobOptions lOptions = (ConversionJobOptions) iConversionJobOptions.clone();
                    TramlConverterApplication.getApplication().addResult(lOptions);
                } catch (CloneNotSupportedException e) {
                    logger.error(e.getMessage(), e);
                }

                // Reset the Form.
                resetButtons();
                iUploadField.reset();
                iConversionJobOptions.setInputFile(null);
                iConversionJobOptions.setOutputFile(null);
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


    /**
     * This FieldFactory isolates the Field creation for a TraML conversion Form.
     */
    private class ConversionFieldFactory extends DefaultFieldFactory {

        ComboBox importTypes = null; // Layout components.

        ComboBox exportTypes = null; // Layout components.


        /**
         * Construct a new instance.
         */
        public ConversionFieldFactory() {
            importTypes = makeConversionTypeComboBox();
            importTypes.setCaption("import type");
            importTypes.setRequired(false);

            exportTypes = makeConversionTypeComboBox();
            exportTypes.setCaption("export type");
            exportTypes.setRequired(false);

        }

        /**
         * Convenience method to create a ComboBox that is aware of the different conversion types.
         * @return
         */
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
            } else {
                f = super.createField(item, propertyId, uiContext);
            }
            return f;
        }
    }
}

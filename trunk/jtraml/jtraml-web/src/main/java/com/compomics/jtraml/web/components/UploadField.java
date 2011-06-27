package com.compomics.jtraml.web.components;

import com.compomics.jtraml.model.ConversionJobOptions;
import com.compomics.jtraml.web.TramlConverterApplication;
import com.google.common.io.Files;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.terminal.ClassResource;
import com.vaadin.ui.*;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.themes.Reindeer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

@SuppressWarnings("serial")
public class UploadField extends FormLayout implements Property{

    private FileReceiver receiver = new FileReceiver();

    private Upload upload = new Upload("", receiver);
    private Label result = new Label("");

    private ConversionJobOptions iConversionJobOptions;


    public UploadField(ConversionJobOptions aConversionJobOptions) {
        super();
        iConversionJobOptions = aConversionJobOptions;

        setStyleName(Reindeer.PANEL_LIGHT);
        setCaption("input file");
        setWidth("40%");

//        getWindow().addComponent(upload);
//        getWindow().addComponent(result);

        upload.addListener(new Upload.FinishedListener() {
            public void uploadFinished(FinishedEvent event) {
                result.setCaption("uploaded " + receiver.getFileName());
                result.setIcon(new ClassResource("/images/16x16/success.png", TramlConverterApplication.getApplication()));
            }
        });

        VerticalLayout lLayout = new VerticalLayout();
        lLayout.addComponent(upload);
        lLayout.addComponent(result);

        addComponent(lLayout);
    }

    /**
     * This method will reset upload field.
     */
    public void reset(){
        upload = new Upload("", receiver);
        result.setCaption("");
        result.setIcon(null);
    }

    public class FileReceiver implements Receiver {

        private String fileName;
        private String mtype;


        /**
         * return an OutputStream that simply counts lineends
         */
        public OutputStream receiveUpload(String filename, String MIMEType) {
            fileName = filename;
            mtype = MIMEType;

            File lFile = new File(TramlConverterApplication.getApplication().getTempDir(), filename);
            setValue(lFile);
            try {
                return Files.newOutputStreamSupplier(lFile).getOutput();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
            return null;
        }

        public String getFileName() {
            return fileName;
        }
    }


    /**
     * Gets the value stored in the Property. The returned object is compatible
     * with the class returned by getType().
     *
     * @return the value stored in the Property
     */
    public Object getValue() {
        return iConversionJobOptions.getInputFile();
    }

    /**
     * Sets the value of the Property.
     * <p>
     * Implementing this functionality is optional. If the functionality is
     * missing, one should declare the Property to be in read-only mode and
     * throw <code>Property.ReadOnlyException</code> in this function.
     * </p>
     * Note : It is not required, but highly recommended to support setting the
     * value also as a <code>String</code> in addition to the native type of the
     * Property (as given by the <code>getType</code> method). If the
     * <code>String</code> conversion fails or is unsupported, the method should
     * throw <code>Property.ConversionException</code>. The string conversion
     * should at least understand the format returned by the
     * <code>toString</code> method of the Property.
     *
     * @param newValue New value of the Property. This should be assignable to the
     *                 type returned by getType, but also String type should be
     *                 supported
     * @throws com.vaadin.data.Property.ReadOnlyException
     *          if the object is in read-only mode
     * @throws com.vaadin.data.Property.ConversionException
     *          if newValue can't be converted into the Property's native
     *          type directly or through String
     */
    public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
        if (newValue instanceof File) {
            iConversionJobOptions.setInputFile((File) newValue);
        };
    }

    /**
     * Returns the type of the Property. The methods <code>getValue</code> and
     * <code>setValue</code> must be compatible with this type: one must be able
     * to safely cast the value returned from <code>getValue</code> to the given
     * type and pass any variable assignable to this type as an argument to
     * <code>setValue</code>.
     *
     * @return type of the Property
     */
    public Class<?> getType() {
        return File.class;
    }


    /**
     * <p>
     * Checks the validity of the validatable. If the validatable is valid this
     * method should do nothing, and if it's not valid, it should throw
     * <code>Validator.InvalidValueException</code>
     * </p>
     *
     * @throws com.vaadin.data.Validator.InvalidValueException
     *          if the value is not valid
     */
    public void validate() throws Validator.InvalidValueException {
        Object lValue = getValue();
        if (lValue instanceof File) {
            if (((File) lValue).exists() == false) {
//                throw new Validator.InvalidValueException("The Uploaded file does not exist!!");
            }else{
                // all fine!
                setComponentError(null);
            }
        } else {
            getWindow().showNotification("Please upload an input file", Window.Notification.TYPE_ERROR_MESSAGE);
//            setComponentError(new UserError("The Upload value is not an instance of 'File'!!"));
        }
    }



}
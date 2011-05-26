package com.compomics.traml.web.panel;

import com.compomics.traml.web.TramlConverterApplication;
import com.google.common.io.Files;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.terminal.ClassResource;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.*;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.themes.Reindeer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

@SuppressWarnings("serial")
public class MyUpload extends Panel implements Field {

    private FileReceiver receiver = new FileReceiver();

    private Upload upload = new Upload("", receiver);
    private Label result = new Label("");

    private TramlConverterApplication iApplication;

    public Property iDataSource;
    public AbstractField iAbstractField;

    public MyUpload() {
        super();

        iAbstractField = new AbstractField() {
            @Override
            public Class<?> getType() {
                return getType();
            }
        };

        setStyleName(Reindeer.PANEL_LIGHT);
        setCaption("Input File");
        setWidth("40%");

        addComponent(upload);
        addComponent(result);

        upload.addListener(new Upload.FinishedListener() {
            public void uploadFinished(FinishedEvent event) {
                result.setCaption("uploaded " + receiver.getFileName());
                result.setIcon(new ClassResource("/images/16x16/success.png", TramlConverterApplication.getApplication()));
            }
        });

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
            iDataSource.setValue(lFile);
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
     * Is this field required.
     * <p/>
     * Required fields must filled by the user.
     *
     * @return <code>true</code> if the field is required,otherwise
     *         <code>false</code>.
     * @since 3.1
     */
    public boolean isRequired() {
        return iAbstractField.isRequired();
    }

    /**
     * Sets the field required. Required fields must filled by the user.
     *
     * @param required Is the field required.
     * @since 3.1
     */
    public void setRequired(boolean required) {
        iAbstractField.setRequired(required);
    }

    /**
     * Sets the error message to be displayed if a required field is empty.
     *
     * @param requiredMessage Error message.
     * @since 5.2.6
     */
    public void setRequiredError(String requiredMessage) {
        iAbstractField.setRequiredError(requiredMessage);
    }

    /**
     * Gets the error message that is to be displayed if a required field is
     * empty.
     *
     * @return Error message.
     * @since 5.2.6
     */
    public String getRequiredError() {
        return iAbstractField.getRequiredError();
    }

    /**
     * Tests if the invalid data is committed to datasource. The default is
     * <code>false</code>.
     */
    public boolean isInvalidCommitted() {
        return iAbstractField.isInvalidCommitted();
    }

    /**
     * Sets if the invalid data should be committed to datasource. The default
     * is <code>false</code>.
     */
    public void setInvalidCommitted(boolean isCommitted) {
        iAbstractField.setInvalidCommitted(isCommitted);
    }

    /**
     * Updates all changes since the previous commit to the data source. The
     * value stored in the object will always be updated into the data source
     * when <code>commit</code> is called.
     *
     * @throws com.vaadin.data.Buffered.SourceException
     *          if the operation fails because of an exception is thrown by
     *          the data source. The cause is included in the exception.
     * @throws com.vaadin.data.Validator.InvalidValueException
     *          if the operation fails because validation is enabled and the
     *          values do not validate
     */
    public void commit() throws SourceException, Validator.InvalidValueException {
        iAbstractField.commit();
    }

    /**
     * Discards all changes since last commit. The object updates its value from
     * the data source.
     *
     * @throws com.vaadin.data.Buffered.SourceException
     *          if the operation fails because of an exception is thrown by
     *          the data source. The cause is included in the exception.
     */
    public void discard() throws SourceException {
        iAbstractField.discard();
    }

    /**
     * Tests if the object is in write-through mode. If the object is in
     * write-through mode, all modifications to it will result in
     * <code>commit</code> being called after the modification.
     *
     * @return <code>true</code> if the object is in write-through mode,
     *         <code>false</code> if it's not.
     */
    public boolean isWriteThrough() {
        return iAbstractField.isWriteThrough();
    }

    /**
     * Sets the object's write-through mode to the specified status. When
     * switching the write-through mode on, the <code>commit</code> operation
     * will be performed.
     *
     * @param writeThrough Boolean value to indicate if the object should be in
     *                     write-through mode after the call.
     * @throws com.vaadin.data.Buffered.SourceException
     *          If the operation fails because of an exception is thrown by
     *          the data source.
     * @throws com.vaadin.data.Validator.InvalidValueException
     *          If the implicit commit operation fails because of a
     *          validation error.
     */
    public void setWriteThrough(boolean writeThrough) throws SourceException, Validator.InvalidValueException {
        iAbstractField.setWriteThrough(writeThrough);
    }

    /**
     * Tests if the object is in read-through mode. If the object is in
     * read-through mode, retrieving its value will result in the value being
     * first updated from the data source to the object.
     * <p>
     * The only exception to this rule is that when the object is not in
     * write-through mode and it's buffer contains a modified value, the value
     * retrieved from the object will be the locally modified value in the
     * buffer which may differ from the value in the data source.
     * </p>
     *
     * @return <code>true</code> if the object is in read-through mode,
     *         <code>false</code> if it's not.
     */
    public boolean isReadThrough() {
        return iAbstractField.isReadThrough();
    }

    /**
     * Sets the object's read-through mode to the specified status. When
     * switching read-through mode on, the object's value is updated from the
     * data source.
     *
     * @param readThrough Boolean value to indicate if the object should be in
     *                    read-through mode after the call.
     * @throws com.vaadin.data.Buffered.SourceException
     *          If the operation fails because of an exception is thrown by
     *          the data source. The cause is included in the exception.
     */
    public void setReadThrough(boolean readThrough) throws SourceException {
        iAbstractField.setReadThrough(readThrough);
    }

    /**
     * Tests if the value stored in the object has been modified since it was
     * last updated from the data source.
     *
     * @return <code>true</code> if the value in the object has been modified
     *         since the last data source update, <code>false</code> if not.
     */
    public boolean isModified() {
        return iAbstractField.isModified();
    }

    /**
     * Gets the <i>tabulator index</i> of the {@code Focusable} component.
     *
     * @return tab index set for the {@code Focusable} component
     * @see #setTabIndex(int)
     */
    public int getTabIndex() {
        return iAbstractField.getTabIndex();
    }

    /**
     * Sets the <i>tabulator index</i> of the {@code Focusable} component.
     * The tab index property is used to specify the order in which the
     * fields are focused when the user presses the Tab key. Components with
     * a defined tab index are focused sequentially first, and then the
     * components with no tab index.
     * <p/>
     * <pre class='code'>
     * Form loginBox = new Form();
     * loginBox.setCaption(&quot;Login&quot;);
     * layout.addComponent(loginBox);
     * <p/>
     * // Create the first field which will be focused
     * TextField username = new TextField(&quot;User name&quot;);
     * loginBox.addField(&quot;username&quot;, username);
     * <p/>
     * // Set focus to the user name
     * username.focus();
     * <p/>
     * TextField password = new TextField(&quot;Password&quot;);
     * loginBox.addField(&quot;password&quot;, password);
     * <p/>
     * Button login = new Button(&quot;Login&quot;);
     * loginBox.getFooter().addComponent(login);
     * <p/>
     * // An additional component which natural focus order would
     * // be after the button.
     * CheckBox remember = new CheckBox(&quot;Remember me&quot;);
     * loginBox.getFooter().addComponent(remember);
     * <p/>
     * username.setTabIndex(1);
     * password.setTabIndex(2);
     * remember.setTabIndex(3); // Different than natural place
     * login.setTabIndex(4);
     * </pre>
     * <p/>
     * <p>
     * After all focusable user interface components are done, the browser
     * can begin again from the component with the smallest tab index, or it
     * can take the focus out of the page, for example, to the location bar.
     * </p>
     * <p/>
     * <p>
     * If the tab index is not set (is set to zero), the default tab order
     * is used. The order is somewhat browser-dependent, but generally
     * follows the HTML structure of the page.
     * </p>
     * <p/>
     * <p>
     * A negative value means that the component is completely removed from
     * the tabulation order and can not be reached by pressing the Tab key
     * at all.
     * </p>
     *
     * @param tabIndex the tab order of this component. Indexes usually start
     *                 from 1. Zero means that default tab order should be used.
     *                 A negative value means that the field should not be
     *                 included in the tabbing sequence.
     * @see #getTabIndex()
     */
    public void setTabIndex(int tabIndex) {
        iAbstractField.setTabIndex(tabIndex);
    }

    /**
     * Gets the value stored in the Property. The returned object is compatible
     * with the class returned by getType().
     *
     * @return the value stored in the Property
     */
    public Object getValue() {
        return iDataSource.getValue();
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
        iDataSource.setValue(newValue);
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
     * Adds a new validator for this object. The validator's
     * {@link com.vaadin.data.Validator#validate(Object)} method is activated every time the
     * object's value needs to be verified, that is, when the {@link #isValid()}
     * method is called. This usually happens when the object's value changes.
     * </p>
     *
     * @param validator the new validator
     */
    public void addValidator(Validator validator) {
        iAbstractField.addValidator(validator);
    }

    /**
     * <p>
     * Removes a previously registered validator from the object. The specified
     * validator is removed from the object and its <code>validate</code> method
     * is no longer called in {@link #isValid()}.
     * </p>
     *
     * @param validator the validator to remove
     */
    public void removeValidator(Validator validator) {
        iAbstractField.removeValidator(validator);
    }

    /**
     * <p>
     * Lists all validators currently registered for the object. If no
     * validators are registered, returns <code>null</code>.
     * </p>
     *
     * @return collection of validators or <code>null</code>
     */
    public Collection<Validator> getValidators() {
        return iAbstractField.getValidators();
    }

    /**
     * <p>
     * Tests the current value of the object against all registered validators.
     * The registered validators are iterated and for each the
     * {@link com.vaadin.data.Validator#validate(Object)} method is called. If any validator
     * throws the {@link com.vaadin.data.Validator.InvalidValueException} this method returns
     * <code>false</code>.
     * </p>
     *
     * @return <code>true</code> if the registered validators concur that the
     *         value is valid, <code>false</code> otherwise
     */
    public boolean isValid() {
        return iDataSource.getValue() != null;
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
        Object lValue = iDataSource.getValue();
        if (lValue instanceof File) {
            if (((File) lValue).exists() == false) {
//                throw new Validator.InvalidValueException("The Uploaded file does not exist!!");
            }else{
                // all fine!
                setComponentError(null);
            }
        } else {
            getWindow().showNotification("Please upload an input file", Window.Notification.TYPE_ERROR_MESSAGE);
            setComponentError(new UserError("The Upload value is not an instance of 'File'!!"));
        }
    }

    /**
     * <p>
     * Checks the validabtable object accept invalid values.The default value is
     * <code>true</code>.
     * </p>
     */
    public boolean isInvalidAllowed() {
        return iAbstractField.isInvalidAllowed();
    }

    /**
     * <p>
     * Should the validabtable object accept invalid values. Supporting this
     * configuration possibility is optional. By default invalid values are
     * allowed.
     * </p>
     *
     * @param invalidValueAllowed
     * @throws UnsupportedOperationException if the setInvalidAllowed is not supported.
     */
    public void setInvalidAllowed(boolean invalidValueAllowed) throws UnsupportedOperationException {
        iAbstractField.setInvalidAllowed(invalidValueAllowed);
    }

    /**
     * Notifies this listener that the Property's value has changed.
     *
     * @param event value change event object
     */
    public void valueChange(Property.ValueChangeEvent event) {
        iAbstractField.valueChange(event);
    }

    /**
     * Registers a new value change listener for this Property.
     *
     * @param listener the new Listener to be registered
     */
    public void addListener(ValueChangeListener listener) {
        iAbstractField.addListener(listener);
    }

    /**
     * Removes a previously registered value change listener.
     *
     * @param listener listener to be removed
     */
    public void removeListener(ValueChangeListener listener) {
        iAbstractField.removeListener(listener);
    }

    /**
     * Sets the Property that serves as the data source of the viewer.
     *
     * @param newDataSource the new data source Property
     */
    public void setPropertyDataSource(Property newDataSource) {
        iDataSource = newDataSource;
    }

    /**
     * Gets the Property serving as the data source of the viewer.
     *
     * @return the Property serving as the viewers data source
     */
    public Property getPropertyDataSource() {
        return iDataSource;
    }
}
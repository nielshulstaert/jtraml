package com.compomics.jtraml.web.components;

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.*;

/**
 * This class combines a Checkbox and a Textfield.
 * The TextField is enabled/disabled along with the CheckBox.
 */
public class CheckBoxTextField extends FormLayout implements Property {

    // Componnents
    CheckBox iCheckBox;
    TextField iTextField;


    /**
     * Constructs an empty <code>TextField</code> with no caption.
     */
    public CheckBoxTextField(String aPrompt, String aDescription) {
        super();

        iCheckBox = new CheckBox();
        iCheckBox.setValue(false);

        // Enable/Disable the Textfield by the CheckBox status.
        iCheckBox.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                iTextField.setEnabled((Boolean) iCheckBox.getValue());
                iTextField.requestRepaint();
            }
        });

        iTextField = new TextField();
        iTextField.setInputPrompt(aPrompt);
        iTextField.setDescription(aDescription);
        iTextField.setEnabled((Boolean) iCheckBox.getValue());

        // Make the components immediate.
        iCheckBox.setImmediate(true);
        iTextField.setImmediate(true);

        iTextField.setValidationVisible(false);


        HorizontalLayout lLayout = new HorizontalLayout();
        lLayout.addComponent(iCheckBox);
        lLayout.addComponent(iTextField);

        addComponent(lLayout);
    }

    /**
     * Add a listener for textfield changes.
     *
     * @param aListener
     */
    public void addTextFieldListener(FieldEvents.TextChangeListener aListener) {
        iTextField.addListener(aListener);
    }

    public void addTextFieldValidation(Validator aValidator){
        iTextField.addValidator(aValidator);
    }


    public Class<?> getType() {
        return String.class;
    }

    /**
     * Returns whether this component is selected.
     *
     * @return
     */
    private boolean isSelected() {
        return (Boolean) iCheckBox.getValue();
    }


    /**
     * Gets the value stored in the Property when the checkbox is enabled.
     *
     * @return the value stored in the Property when the checkbox is enabled. Else null.
     */
    public Object getValue() {
        if (isSelected()) {
            return iTextField.getValue();
        } else {
            return null;
        }
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
        iCheckBox.setValue(true);
        iTextField.setValue(newValue.toString());
    }
}

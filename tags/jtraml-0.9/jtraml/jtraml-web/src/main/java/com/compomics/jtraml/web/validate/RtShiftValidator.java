package com.compomics.jtraml.web.validate;

import com.vaadin.data.Validator;

/**
 * This class validates the value of the RetentionTime shift.
 */
public class RtShiftValidator implements Validator {
    /**
     * Checks the given value against this validator. If the value is valid the
     * method does nothing. If the value is invalid, an
     * {@link com.vaadin.data.Validator.InvalidValueException} is thrown.
     *
     * @param value the value to check
     * @throws com.vaadin.data.Validator.InvalidValueException
     *          if the value is invalid
     */
    public void validate(Object value) throws InvalidValueException {

        String text = value.toString();
        if (text.equals("")) {
            // Text is blank. Ok!
        } else {
            try {
                // Is the value readable as a number?
                Double.parseDouble(text);
            } catch (NumberFormatException ignored) {
                throw new InvalidValueException("Not a valid value for the retention time shift");
            }
        }
        // Ok! All went fine.
    }

    /**
     * Tests if the given value is valid. This method must be symmetric with
     * {@link #validate(Object)} so that {@link #validate(Object)} throws an
     * error iff this method returns false.
     *
     * @param value the value to check
     * @return <code>true</code> if the value is valid, <code>false</code>
     *         otherwise.
     */
    public boolean isValid(Object value) {
        try {
            validate(value);
        } catch (InvalidValueException e) {
            // Not valid!
            return false;
        }
        // No exceptions thrown.
        return true;
    }
}

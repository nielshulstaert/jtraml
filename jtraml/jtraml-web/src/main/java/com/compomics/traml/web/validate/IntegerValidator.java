package com.compomics.traml.web.validate;

import com.vaadin.data.Validator;


public class IntegerValidator implements Validator {

    private String message;

    public IntegerValidator(String message) {
        this.message = message;
    }

    public boolean isValid(Object value) {
        if (value == null || !(value instanceof String)) {
            return false;
        }
        try {
            Integer.parseInt((String) value);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void validate(Object value) throws Validator.InvalidValueException {
        if (!isValid(value)) {
            throw new InvalidValueException(message);
        }
    }

}
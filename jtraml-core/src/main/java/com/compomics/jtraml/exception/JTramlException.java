package com.compomics.jtraml.exception;

/**
 * This class is types JTraml specific runtime exceptions.
 */
public class JTramlException extends RuntimeException {
    /**
     * {@inheritDoc}
     */
    public JTramlException(String s) {
        super(s);
    }
    /**
     * {@inheritDoc}
     */
    public JTramlException(String s, Object o) {
        super(s + "\n" + o.toString());
    }
}

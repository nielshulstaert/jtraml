package com.compomics.traml.exception;

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
     * * {@inheritDoc}
     */
    public JTramlException(String s, Throwable aThrowable) {
        super(s, aThrowable);
    }

    /**
     * {@inheritDoc}
     */
    public JTramlException(Throwable aThrowable) {
        super(aThrowable);
    }
}

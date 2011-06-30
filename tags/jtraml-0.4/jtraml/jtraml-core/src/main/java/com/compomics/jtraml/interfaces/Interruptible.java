package com.compomics.jtraml.interfaces;

/**
 * Implementors of this class can interrupt themselves, and proceed with an Object o.
 */
public interface Interruptible {

    /**
     * Interrupt the implementing class.
     */
    public void interrupt() throws InterruptedException;

    /**
     * Proceed the operations of the current Class.
     * @param o
     */
    public void proceed(Object o);
}

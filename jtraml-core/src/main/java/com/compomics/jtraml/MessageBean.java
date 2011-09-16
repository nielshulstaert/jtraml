package com.compomics.jtraml;

import com.compomics.jtraml.interfaces.Interruptible;

/**
 * This class is a
 */
public class MessageBean {

    /**
     * The Message String.
     */
    private String iMessage = "";

    /**
     * This boolean keeps track of the input requirements.
     */
    private boolean iRequiresAnswer = false;

    /**
     * This boolean keeps track of the input requirements.
     */
    private boolean iCanBeSolved = true;

    /**
     * The answer to send to the Interupted class.
     */
    private Object iAnswer = null;

    /**
     * The waiting/interrupted class.
     */
    private Interruptible iInterruptible= null;


    /**
     * Create a new MessageBean
     * @param aMessage
     * @param aRequiresAnswer
     * @param aAnswer
     * @param aInterruptible
     */
    public MessageBean(String aMessage, boolean aRequiresAnswer, Object aAnswer, Interruptible aInterruptible) {
        iMessage = aMessage;
        iRequiresAnswer = aRequiresAnswer;
        iAnswer = aAnswer;
        iInterruptible = aInterruptible;
    }

    public MessageBean(String aMessage, boolean aRequiresAnswer) {
        iMessage = aMessage;
        iRequiresAnswer = aRequiresAnswer;
    }

    public void setAnswer(Object aAnswer) {
        iAnswer = aAnswer;
    }

    public String getMessage() {
        return iMessage;
    }

    public boolean isRequiresAnswer() {
        return iRequiresAnswer;
    }

    public Object getAnswer() {
        return iAnswer;
    }

    public Interruptible getInterruptible() {
        return iInterruptible;
    }

    public void setCanBeSolved(boolean aCanBeSolved) {
        iCanBeSolved = aCanBeSolved;
    }

    public void setInterruptible(Interruptible aInterruptible) {
        iInterruptible = aInterruptible;
    }
}

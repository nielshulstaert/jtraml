package com.compomics.jtraml.web.dialog;

import com.compomics.jtraml.web.components.InfoLink;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
/**
 * This Dialog can request input from a user and forward the reply to a Recipient.
 */
public class InputDialog extends Window {

    /**
     * The target object that will receive the result from the input dialog.
     */
    Recipient r;

    // Layout components.
    private AbsoluteLayout mainLayout;
    private TextArea txtMessage;
    private Button btnConfirm;
    private InfoLink lnkInfo;
    private TextField tf;


    /**
     * Create a new InputDialog.
     * @param parent The Parent Window component.
     * @param question The Question to be displayed in the input dialog.
     * @param recipient The Target receiever of the user's input.
     * @param showInput Boolean whether this input dialog requires a TextField input field.
     */
    public InputDialog(final Window parent, String question, Recipient recipient, boolean showInput) {

        r = recipient;

        final Window dialog = this;

        /**
         * If a button is clicked, then the textfield input is submitted to the receiver and the dialog is closed.
         */
        Button.ClickListener lClickListener = new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                r.gotInput(tf.toString());
                parent.removeWindow(dialog);
            }
        };

        // Set title.
        setCaption("TraML Converter Message:");

        // common part: create layout
        mainLayout = new AbsoluteLayout();
        mainLayout.setImmediate(false);
        mainLayout.setWidth("400px");
        mainLayout.setHeight("200px");
        mainLayout.setMargin(true);

        // top-level component properties
        this.setWidth("400px");
//        this.setHeight("300px");

        // tf
        tf = new TextField();
        tf.setCaption("retention time window");
        tf.setImmediate(false);
        tf.setWidth("-1px");
        tf.setHeight("-1px");
        mainLayout.addComponent(tf, "top:154.0px;left:133.0px;");

        if (showInput == false) {
            tf.setVisible(false);
        }

        // lnkInfo
        lnkInfo = new InfoLink(InfoLink.InfoPages.RETENTION_CONVERSION);
        lnkInfo.setImmediate(false);
        lnkInfo.setWidth("-1px");
        lnkInfo.setHeight("-1px");

        mainLayout.addComponent(lnkInfo, "top:152.0px;left:360.0px;");

        // btnConfirm
        btnConfirm = new Button();
        btnConfirm.setCaption("Ok");
        btnConfirm.setImmediate(true);
        btnConfirm.setWidth("-1px");
        btnConfirm.setHeight("-1px");
        btnConfirm.addListener(lClickListener);


        mainLayout.addComponent(btnConfirm, "top:152.0px;left:308.0px;");


        // txtMessage
        txtMessage = new TextArea("");
        txtMessage.setValue(question);
        txtMessage.setWordwrap(true);
        txtMessage.setImmediate(false);
        txtMessage.setWidth("300px");
        txtMessage.setHeight("-1px");
        mainLayout.addComponent(txtMessage, "top:33.0px;left:52.0px;");

        this.addComponent(mainLayout);
        parent.addWindow(this);
    }

    public interface Recipient {
        public void gotInput(String input);
    }
}
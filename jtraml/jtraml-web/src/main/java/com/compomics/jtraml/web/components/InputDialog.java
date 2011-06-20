package com.compomics.jtraml.web.components;

import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class InputDialog extends Window {
    Recipient r;
    TextField tf = new TextField();

    public InputDialog(final Window parent, String question, Recipient recipient, boolean showInput) {
        r = recipient;
        setCaption(question);
        setModal(true);
        getContent().setSizeUndefined();

        if (showInput == true) {
            addComponent(tf);
        }

        final Window dialog = this;
        addComponent(new Button("Ok", new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                r.gotInput(tf.toString());
                parent.removeWindow(dialog);
            }
        }));
        addComponent(new InfoLink());
        parent.addWindow(this);
    }

    public interface Recipient {
        public void gotInput(String input);
    }
}
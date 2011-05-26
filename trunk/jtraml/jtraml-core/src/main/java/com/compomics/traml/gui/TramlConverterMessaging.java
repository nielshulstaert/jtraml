package com.compomics.traml.gui;

import javax.swing.*;
import java.awt.*;

/**
 * This class is a central class for user messaging.
 */
public class TramlConverterMessaging {

    protected ImageIcon iconInfo = new ImageIcon(getClass().getResource("/images/info.png"));
    protected ImageIcon iconOk = new ImageIcon(getClass().getResource("/images/ok.png"));
    protected ImageIcon iconError = new ImageIcon(getClass().getResource("/images/error.png"));
    protected ImageIcon iconQuestion = new ImageIcon(getClass().getResource("/images/question.png"));
    protected ImageIcon iconWarning = new ImageIcon(getClass().getResource("/images/warning.png"));

    private static Component iAnchorComponent = null;

    private static TramlConverterMessaging iMessage = new TramlConverterMessaging();

    public static void setAnchorComponent(Component aAnchorComponent) {
        iAnchorComponent = aAnchorComponent;
    }

    public static void info(String aMessage){
        JOptionPane.showMessageDialog(iAnchorComponent, aMessage, "", JOptionPane.INFORMATION_MESSAGE, iMessage.iconInfo);
    }

    public static void confirm(String aMessage){
        JOptionPane.showMessageDialog(iAnchorComponent, aMessage, "", JOptionPane.INFORMATION_MESSAGE, iMessage.iconOk);
    }

    public static void warning(String aMessage){
        JOptionPane.showMessageDialog(iAnchorComponent, aMessage, "", JOptionPane.WARNING_MESSAGE, iMessage.iconWarning);
    }

    public static void error(String aMessage){
        JOptionPane.showMessageDialog(iAnchorComponent, aMessage, "", JOptionPane.ERROR_MESSAGE, iMessage.iconError);
    }

    public static boolean ask(String aQuestion){
        int lResult = JOptionPane.showConfirmDialog(iAnchorComponent, aQuestion, "", JOptionPane.YES_NO_OPTION, JOptionPane.YES_OPTION, iMessage.iconQuestion);
        if(lResult == JOptionPane.YES_OPTION){
            return true;
        }else{
            return false;
        }
    }
}

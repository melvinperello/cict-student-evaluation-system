package org.bsu.cict.alerts;

import java.awt.Dimension;
import javax.swing.JOptionPane;

/**
 * Standard Java Message Boxes Factory..
 *
 * @author Jhon Melvin
 */
public class MessageBox {

    public static void main(String[] args) {
        showInformation("Asd", "<h1>lkjkljk</h1>ljkljklj<p>kljkljkljkl</p><p>jkljkljkljkljkljkljkljkljjlkjkljkljkljkljkljkljkljkljkljkljkljkljkljkljkljkljjlkjkljkljkljkljkljkljkljkljkljkljkljkljkljkljkljkljjlkjkljkljkljkljkljkljkljkljkljkljkljkljkljkljkljkljjlkjkljkljkljkljkljkljkljkljkljkljkljkljkljkljkljkljj");
    }

    private final static String HTML_START = "<html><body wdth='10'>";
    private final static String HTML_END = "</body></html>";

    /**
     * private method to create a typical swing message dialog with void return.
     *
     * @param title
     * @param message
     * @param messageType
     */
    private static void createSwingMessage(String title, String message, int messageType) {
        String body = HTML_START + message + HTML_END;
        System.out.println(body);
        JOptionPane.showMessageDialog(null, body, title, messageType);
    }

    /**
     * Show information message.
     *
     * @param title
     * @param message
     */
    public static void showInformation(String title, String message) {
        createSwingMessage(title, message, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Warning Message.
     *
     * @param title
     * @param message
     */
    public static void showWarning(String title, String message) {
        createSwingMessage(title, message, JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Error Message.
     *
     * @param title
     * @param message
     */
    public static void showError(String title, String message) {
        createSwingMessage(title, message, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Plain Message.
     *
     * @param title
     * @param message
     */
    public static void show(String title, String message) {
        createSwingMessage(title, message, JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Show Question.
     *
     * @param title
     * @param message
     */
    public static void showQuestion(String title, String message) {
        createSwingMessage(title, message, JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * Create a standard swing confirmation.
     *
     * @param title
     * @param message
     * @param option
     * @return
     */
    private static int createSwingConfirm(String title, String message, int option) {
        return JOptionPane.showConfirmDialog(null, message, title,
                option,
                JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * Confirmation
     *
     * @param title
     * @param message
     * @return 0 and 1 for Yes or No Respectively.
     */
    public static int showConfirmation(String title, String message) {
        return createSwingConfirm(title, message, JOptionPane.YES_NO_OPTION);
    }

    /**
     * Confirmation with cancel.
     *
     * @param title
     * @param message
     * @return values 0, 1 and 2 respectively with YES, NO, CANCEL.
     */
    public static int showConfirmationCancelable(String title, String message) {
        return createSwingConfirm(title, message, JOptionPane.YES_NO_CANCEL_OPTION);
    }

}

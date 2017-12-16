package org.bsu.cict.alerts;

import java.util.Locale;
import javax.swing.JOptionPane;

/**
 * Standard Java Message Box Factory.
 *
 * Supports HTML 3.2 and simple CSS Styling.
 *
 * @author Jhon Melvin
 */
public class MessageBox {

    /**
     * Message display block alignment.
     */
    public enum TextAlignment {
        LEFT, RIGHT, CENTER;
    }

    //--------------------------------------------------------------------------
    // default values
    private static int width = 180;
    private static TextAlignment alignment = TextAlignment.LEFT;
    //--------------------------------------------------------------------------

    /**
     * overrides the default width.
     *
     * @param width
     */
    public static void setWidth(int width) {
        MessageBox.width = width;
    }

    /**
     * overrides the default alignment.
     *
     * @param alignment
     */
    public static void setAlignment(TextAlignment alignment) {
        MessageBox.alignment = alignment;
    }

    //--------------------------------------------------------------------------
    // fields for html initialization.
    private final static String HTML_START = "<html><body>";
    private final static String HTML_CONTENT = "<p style='width: " + MessageBox.width + "px;text-align: " + alignment.toString().toLowerCase(Locale.ENGLISH) + ";'>";
    private final static String HTML_END = "</p></body></html>";
    //--------------------------------------------------------------------------

    /**
     * Plain Message. with no added context.
     *
     * @param title
     * @param message
     * @param type JOptionPane.(Type).
     */
    public static void show(String title, String message, int type) {
        //----------------------------------------------------------------------
        // add to space padding for title.
        title = "  " + title;
        //----------------------------------------------------------------------
        JOptionPane.showMessageDialog(null, message, title, type);
    }

    /**
     * private method to create a typical swing message dialog with void return.
     *
     * @param title
     * @param message
     * @param messageType
     */
    private static void createSwingMessage(String title, String message, int messageType) {
        String body = HTML_START + HTML_CONTENT + message + HTML_END;
        MessageBox.show(title, body, messageType);
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
        String body = HTML_START + HTML_CONTENT + message + HTML_END;
        //----------------------------------------------------------------------
        // add to space padding for title.
        title = "  " + title;
        //----------------------------------------------------------------------
        return JOptionPane.showConfirmDialog(null, body, title,
                option,
                JOptionPane.QUESTION_MESSAGE);
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
     * Show Question.
     *
     * @param title
     * @param message
     */
    public static void showQuestion(String title, String message) {
        createSwingMessage(title, message, JOptionPane.QUESTION_MESSAGE);
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

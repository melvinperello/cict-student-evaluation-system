/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melvin.mono.classmeta;

/**
 *
 * @author Jhon Melvin
 */
public class ConsoleWriter {

    public final static String COSNOLE_NAME = "Monosync Framework: ";

    /**
     * Writes a simple console message with new line
     *
     * @param message
     */
    public static void writeText(Object message) {
        System.out.println(String.valueOf(message));
    }

    /**
     * Writes a simple error console message with new line
     *
     * @param message
     */
    public static void writeError(Object message) {
        System.err.println(String.valueOf(message));
    }

    //--------------------------------------------------------------------------
    /**
     * Writes a simple console message with new line used by the Mono Sync
     * Framework.
     *
     * @param message
     */
    public static void monoText(Object message) {
        System.out.println(COSNOLE_NAME + String.valueOf(message));
    }

    /**
     * Writes a simple console error message with new line used by the Mono Sync
     * Framework.
     *
     * @param message
     */
    public static void monoError(Object message) {
        System.err.println(COSNOLE_NAME + String.valueOf(message));
    }

    /**
     * Gives Tip to the user. this tips are only based by the developer's
     * experience.
     *
     * @param message
     */
    public static void monoTip(Object... message) {
        System.err.println("These are tips and not necessarily the cause of the problem");
        System.err.println("Framework TIPS: ");
        for (Object msg : message) {
            System.err.println(" - " + String.valueOf(msg));
        }
    }

    /**
     * Give details about the exception.
     *
     * @param ex
     */
    public static void monoException(Exception ex) {
        monoError("An Exception Has Occured in Your System.");
        monoError("Time: " + TimeAware.getSystemTime());
        monoError("Actual Name: " + ex.getClass().getName());
        monoError("Simple Name: " + ex.getClass().getSimpleName());
        monoError("Message: " + ex.getMessage());
        monoError("Localized: " + ex.getLocalizedMessage());
    }

    /**
     * Prints the stack trace of a given exception to tell the user it is not
     * from Uncaught Exception.
     *
     * @param ex
     */
    public static void monoStackTrace(Exception ex) {
        System.err.println("\n");
        monoError("This stack trace was called by the framework.");
        monoError("This was not called by an UNCAUGHT EXCEPTION");
        System.err.println("");
        monoError("<exception_details>");
        ex.printStackTrace();
        System.err.println("");
        monoError("</exception_details>");
        System.err.println("\n");
    }

}

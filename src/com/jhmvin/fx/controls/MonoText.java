/**
 * CAPSTONE PROJECT.
 * BSIT 4A-G1.
 * MONOSYNC TECHNOLOGIES.
 * MONOSYNC FRAMEWORK VERSION 1.0.0 TEACUP RICE ROLL.
 * THIS PROJECT IS PROPRIETARY AND CONFIDENTIAL ANY PART THEREOF.
 * COPYING AND DISTRIBUTION WITHOUT PERMISSION ARE NOT ALLOWED.
 *
 * COLLEGE OF INFORMATION AND COMMUNICATIONS TECHNOLOGY.
 * LINKED SYSTEM.
 *
 * PROJECT MANAGER: JHON MELVIN N. PERELLO
 * DEVELOPERS:
 * JOEMAR N. DELA CRUZ
 * GRETHEL EINSTEIN BERNARDINO
 *
 * OTHER LIBRARIES THAT ARE USED BELONGS TO THEIR RESPECTFUL OWNERS AND AUTHORS.
 * NO COPYRIGHT ARE INTENTIONAL OR INTENDED.
 * THIS PROJECT IS NOT PROFITABLE HENCE FOR EDUCATIONAL PURPOSES ONLY.
 * THIS PROJECT IS ONLY FOR COMPLIANCE TO OUR REQUIREMENTS.
 * THIS PROJECT DOES NOT INCLUDE DISTRIBUTION FOR OTHER PURPOSES.
 *
 */
package com.jhmvin.fx.controls;

import artifacts.MonoString;
import java.util.Locale;
import javafx.scene.control.TextField;

/**
 * String manipulation class.
 *
 * @author Jhon Melvin
 */
public class MonoText {

    /**
     * Clear TextFields.
     *
     * @param textFields
     */
    public static void clear(TextField... textFields) {
        for (TextField textField : textFields) {
            textField.setText("");
        }
    }

    /**
     * Get Text and formatted to uppercase.extra spaces in between are removed.
     *
     * @param textField
     * @return
     */
    public static String getFormatted(TextField textField) {
        return trimPaddedSpace(getTrim(textField).toUpperCase(Locale.ENGLISH));
    }

    /**
     * Trims the text from text field for extra spaces.
     *
     * @param textField
     * @return
     */
    public static String getTrim(TextField textField) {
        try {
            return textField.getText().trim();
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     *
     * @param textField
     * @return
     */
    public static Integer getInt(TextField textField) {
        try {
            return Integer.valueOf(getTrim(textField));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     *
     * @param textField
     * @return
     */
    public static Double getDouble(TextField textField) {
        try {
            return Double.valueOf(getTrim(textField));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param textField
     * @return
     */
    public static Float getFloat(TextField textField) {
        try {
            return Float.valueOf(getTrim(textField));
        } catch (Exception e) {
            return null;
        }
    }

    //--------------------------------------------------------------------------
    /**
     * Removes extra spaces in between.
     *
     * @param str
     * @return
     */
    public static String trimPaddedSpace(String str) {
        String[] words = str.split(" ");
        String temp = "";
        for (String word : words) {
            if (!word.isEmpty()) {
                temp += word.concat(" ");
            }
        }
        return temp.trim();
    }

    /**
     * Adds a padding in front of string.
     * @param lead
     * @param pad
     * @param str
     * @return 
     */
    public static String lead(int lead, String pad, String str) {
        int length = str.length();

        if (length >= lead) {
            // do nothing
        } else {
            int toPut = lead - length;
            return repeat(pad, toPut) + str;
        }

        return str;
    }

    /**
     * Repeats a string multiple times.
     *
     * @param str
     * @param multiple
     * @return
     */
    public static String repeat(String str, int multiple) {
        String temp = "";
        for (int x = 1; x <= multiple; x++) {
            temp += str;
        }
        return temp;
    }

}

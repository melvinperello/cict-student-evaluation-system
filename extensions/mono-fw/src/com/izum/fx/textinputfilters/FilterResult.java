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
package com.izum.fx.textinputfilters;

/**
 *
 * @author Jhon Melvin
 */
public class FilterResult {

    // Results messages - can be customized
    public static String RESULT_VALID = "VALID";
    public static String RESULT_MODE_VIOLATED = "Invalid Characters";
    public static String RESULT_MAX_VIOLATED = "Max Characters Exceeded";
    public static String RESULT_LTS_VIOLATED = "Leading or Trailing Spaces Not Allowed";

    // Results for Double
    public static String RESULT_NUMERIC_INVALID = "Invalid Value";

    // instance variables
    private final String message;
    private final boolean valid;

    public FilterResult(boolean valid, String result) {
        this.message = result;
        this.valid = valid;
    }

    /**
     * Gets the message of this filter.
     *
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * Checks whether the filter has a valid or invalid result.
     *
     * @return
     */
    public boolean isValid() {
        return valid;
    }

}

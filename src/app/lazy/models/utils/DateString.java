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
package app.lazy.models.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Please use as much as null safe method as possible. make all the methods here
 * null pointer safe. just return a default value if the value given was null.
 *
 * @author Jhon Melvin
 */
public class DateString {

    /**
     * Add Your different Time Formats here.
     */
    // whole month format.
    public final static SimpleDateFormat TIME_FORMAT_1
            = new SimpleDateFormat("MMMMMMMMMMM dd, yyyy hh:mm:ss a");

    /**
     * It's been very hard to format Dates every time that it will be fetched
     * from the database. it may be late but this is the app.lazy.models.utils
     * function library.
     *
     * this will format the saved date in the database into an actual time in a
     * proper format.
     *
     * @param date
     * @param format
     * @return
     */
    public final static String formatDate(Date date, SimpleDateFormat format) {
        try {
            return date == null ? "No Date" : format.format(date);
        } catch (Exception e) {
            // the date is not in a proper format that is readable by the system.
            return "Unreadable Data";
        }
    }

    /**
     * Default is TIME_FORMAT_1
     *
     * @param date
     * @return
     */
    public final static String formatDate(Date date) {

        return formatDate(date, TIME_FORMAT_1);
    }
}

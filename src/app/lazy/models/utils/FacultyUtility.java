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

import app.lazy.models.Database;
import app.lazy.models.FacultyMapping;

/**
 * This functions are limited and should be use only for faculty Mapping.
 *
 * @author Jhon Melvin
 */
public class FacultyUtility {

    public static enum NameFormat {
        SURNAME_FIRST, FIRST_NAME_FIRST
    }

    /**
     * This one is really disturbing. getting the faculty name especially with
     * null values.
     *
     * @return
     */
    public final static String getFacultyName(FacultyMapping facultyMapping, NameFormat format) {
        if (facultyMapping == null) {
            return "No Data";
        }
        try {
            String firstName = facultyMapping.getFirst_name() == null ? "" : facultyMapping.getFirst_name();
            String middleName = facultyMapping.getMiddle_name() == null ? "" : facultyMapping.getMiddle_name();
            String lastName = facultyMapping.getLast_name() == null ? "" : facultyMapping.getLast_name();

            if (format.equals(NameFormat.SURNAME_FIRST)) {
                return lastName + ", " + firstName + " " + middleName;
            } else {
                // unknown format
                return "Cannot Load Data";
            }
        } catch (Exception e) {
            return "Cannot Load Data";
        }
    }

    /**
     * Default name format which is surname first format.
     *
     * @param facultyMapping
     * @return
     */
    public final static String getFacultyName(FacultyMapping facultyMapping) {
        return getFacultyName(facultyMapping, FacultyUtility.NameFormat.SURNAME_FIRST);
    }

    /**
     * Get Faculty mapping using the primary key. please use this with
     * consideration that the values are being fetched in the database. Do not
     * Use this inside a loop.
     *
     * @param id
     * @return
     */
    public final static FacultyMapping getFaculty(Integer id) {
        if (id == null) {
            return null;
        }
        //
        try {
            return Database.connect().faculty().getPrimary(id);
        } catch (Exception e) {
            System.err.println("There was an error getting the faculty mapping here at Faculty Utility: " + e.getLocalizedMessage());
            return null;
        }
    }

}

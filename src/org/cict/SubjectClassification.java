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
package org.cict;

import java.util.ArrayList;

/**
 *
 * @author Jhon Melvin
 */
public class SubjectClassification {

    // majors
    public final static String TYPE_INTERNSHIP = "INTERNSHIP";
    public final static String TYPE_CAPSTONE = "CAPSTONE";
    public final static String TYPE_MAJOR = "MAJOR";

    // minors
    public final static String TYPE_MINOR = "MINOR";
    public final static String TYPE_ELECTIVE = "ELECTIVE";
    public final static String TYPE_PE = "PE";
    public final static String TYPE_NSTP = "NSTP";

    public final static String SUBTYPE_GRAPHICS = "GRAPHICS";
    public final static String SUBTYPE_PROGRAMMING = "PROGRAMMING";
    public final static String SUBTYPE_HARDWARE = "HARDWARE";
    public final static String SUBTYPE_NONE = "NONE";

    /**
     * Determines a subject type whether it belongs to the major or minor block.
     *
     * @param type
     * @return
     */
    public static boolean isMajor(String type) {
        // categorized in major block
        if (type.equalsIgnoreCase(SubjectClassification.TYPE_CAPSTONE)
                || type.equalsIgnoreCase(SubjectClassification.TYPE_MAJOR)
                || type.equalsIgnoreCase(SubjectClassification.TYPE_INTERNSHIP)) {
            return true;
        } else {
            // all subject types that are not major are categorized in the minor block
            return false;
        }
    }

    /**
     * Determines if a subject type should be credited or counted in computing
     * for average.
     *
     * @param type
     * @return
     */
    public static boolean isCreditted(String type) {
        ArrayList<String> credittedSubjects = new ArrayList<>();
        credittedSubjects.add(TYPE_MAJOR);
        credittedSubjects.add(TYPE_MINOR);
        credittedSubjects.add(TYPE_ELECTIVE);
        credittedSubjects.add(TYPE_PE);
        credittedSubjects.add(TYPE_INTERNSHIP);
        credittedSubjects.add(TYPE_NSTP);

        for (String credittedSubject : credittedSubjects) {
            if (credittedSubject.equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }

    //--------------------------------------------------------------------------
    /**
     * REQUISITES TYPE EXTENSION
     */
    public final static String REQUITE_TYPE_CO = "COREQUISITE";

}

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
package sys.org.cict.enumerations;

import java.util.HashMap;

/**
 *
 * @author Jhon Melvin
 */
public class GradeValues {

    //--------------------------------------------------------------------------
    /**
     * Since many ratings are using Passed as their remarks.
     */
    private final static String PASSED_REMARKS = GradeValues.Remarks.PASSED.getString();

    public final static HashMap<String, String> RATING_REMAKRS = new HashMap<String, String>() {
        {
            put("1.00", PASSED_REMARKS);
            put("1.25", PASSED_REMARKS);
            put("1.50", PASSED_REMARKS);
            put("2.00", PASSED_REMARKS);
            put("2.25", PASSED_REMARKS);
            put("2.50", PASSED_REMARKS);
            put("3.00", PASSED_REMARKS);
            put("4.00", GradeValues.Remarks.CONDITIONALLY_PASSED.getString());
            put("INC", GradeValues.Remarks.INCOMPLETE.getString());
            put("DRP", GradeValues.Remarks.DROPPED.getString());
            put("UD", GradeValues.Remarks.UNOFICIALLY_DROPPPED.getString());
            put("5.00", GradeValues.Remarks.FAILED.getString());
            put("EXP", GradeValues.Remarks.EXPIRED.getString());
        }
    };
    //--------------------------------------------------------------------------

    /**
     * Gives the equivalent remarks on a given rating.
     *
     * @param rating input your rating and get your remarks.
     * @return
     */
    public final static String getRemarks(String rating) {
        return GradeValues.RATING_REMAKRS.get(rating);
    }

    //--------------------------------------------------------------------------
    /**
     * Enumeration for remarks
     */
    public static enum Remarks {
        //----------------------------------------------------------------------
        // Valid Values
        PASSED("PASSED"),
        CONDITIONALLY_PASSED("CONDITIONALLY PASSED"),
        INCOMPLETE("INCOMPLETE"),
        DROPPED("DROPPED"),
        UNOFICIALLY_DROPPPED("UNOFFICIALLY DROPPED"),
        FAILED("FAILED"),
        /*Expired is an exception only the system can use this value*/
        EXPIRED("EXPIRED");
        //----------------------------------------------------------------------

        private String string;

        private Remarks(String insertString) {
            this.string = insertString;
        }

        /**
         * A Formatted string for the use of the database.
         *
         * @return
         */
        public String getString() {
            return string;
        }

    }
    //--------------------------------------------------------------------------
}

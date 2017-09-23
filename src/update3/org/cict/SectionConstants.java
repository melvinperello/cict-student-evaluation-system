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
package update3.org.cict;

import java.util.HashMap;

/**
 *
 * @author Jhon Melvin
 */
public class SectionConstants {

    /**
     * Transition Speed in the Section Module.
     */
    public static double FADE_SPEED = 150;

    /**
     * Section types.
     */
    public static final String REGULAR = "REGULAR";
    public static final String SPECIAL = "SPECIAL";
    public static final String MIDYEAR = "MIDYEAR";
    public static final String TUTORIAL = "TUTORIAL";
    public static final String CONJUNCTION = "CONJUNCTION";

    public static final HashMap<String, String> SECTION_TYPES() {
        HashMap<String, String> sTypes = new HashMap<>();
        sTypes.put("RC", REGULAR);
        sTypes.put("SC", SPECIAL);
        sTypes.put("MC", MIDYEAR);
        sTypes.put("TC", TUTORIAL);
        sTypes.put("CC", CONJUNCTION);
        return sTypes;
    }
;
}

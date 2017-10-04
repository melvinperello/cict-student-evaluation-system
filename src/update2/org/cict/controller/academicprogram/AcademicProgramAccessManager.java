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
package update2.org.cict.controller.academicprogram;

import update3.org.cict.access.Access;

/**
 *
 * @author Jhon Melvin
 */
public class AcademicProgramAccessManager {

    /**
     * Deny Destructive operations if not from these Account types.
     *
     * @return
     */
    public final static boolean denyIfNotAdmin() {
        return Access.isDeniedIfNotFrom(Access.ACCESS_ADMIN, Access.ACCESS_ASST_ADMIN);
    }

    /**
     * Deny Implementation if not from this users.
     *
     * @return
     */
    public final static boolean denyIfNotRegistrar() {
        return Access.isDeniedIfNotFrom(Access.ACCESS_LOCAL_REGISTRAR);
    }
}

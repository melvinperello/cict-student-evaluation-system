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
package org.cict.authentication.authenticator;

/**
 *
 * @author Jhon Melvin
 */
public class Authenticator implements com.jhmvin.fx.async.Process {

    @Override
    public void restart() {

    }

    private static Authenticator AUTHENTICATOR_INSTANCE;

    public static Authenticator instance() {
        if (AUTHENTICATOR_INSTANCE == null) {
            AUTHENTICATOR_INSTANCE = new Authenticator();
        }
        return AUTHENTICATOR_INSTANCE;
    }

    public HibernateLauncher createHibernateLauncher() {
        return new HibernateLauncher();
    }

    public ValidateLogin createLoginValidator() {
        return new ValidateLogin();
    }
}

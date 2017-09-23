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
 * JOEMAR N. DE LA CRUZ
 * GRETHEL EINSTEIN BERNARDINO
 *
 * OTHER LIBRARIES THAT ARE USED BELONGS TO THEIR RESPECTFUL OWNERS AND AUTHORS.
 * NO COPYRIGHT ARE INTENTIONAL OR INTENDED.
 * THIS PROJECT IS NOT PROFITABLE HENCE FOR EDUCATIONAL PURPOSES ONLY.
 * THIS PROJECT IS ONLY FOR COMPLIANCE TO OUR REQUIREMENTS.
 * THIS PROJECT DOES NOT INCLUDE DISTRIBUTION FOR OTHER PURPOSES.
 *
 */
package org.cict.management.registrar;

/**
 *
 * @author Jhon Melvin
 */
public class Registrar implements com.jhmvin.fx.async.Process {

    @Override
    public void restart() {
        //
    }

    // <Singleton>
    private static Registrar REGISTRAR_INSTANCE;

    private Registrar() {
        //
    }

    /**
     * Singleton caller.
     *
     * @return
     */
    public static Registrar instance() {
        if (REGISTRAR_INSTANCE == null) {
            REGISTRAR_INSTANCE = new Registrar();
        }
        return REGISTRAR_INSTANCE;
    }

    public RevokeEvaluation createRevokeEvaluation() {
        return new RevokeEvaluation();
    }

}

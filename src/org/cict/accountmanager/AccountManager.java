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
package org.cict.accountmanager;

/**
 *
 * @author Joemar
 */
public class AccountManager implements com.jhmvin.fx.async.Process{

    @Override
    public void restart() {
    
    }

    private static AccountManager ACCOUNT_MANAGER_INSTANCE;

    public static AccountManager instance() {
        if (ACCOUNT_MANAGER_INSTANCE == null) {
            ACCOUNT_MANAGER_INSTANCE = new AccountManager();
        }
        return ACCOUNT_MANAGER_INSTANCE;
    }
    
    public ValidateRegister createRegistration() {
        return new ValidateRegister();
    }
    
    public Logout createLogout() {
        return new Logout();
    }
    
}

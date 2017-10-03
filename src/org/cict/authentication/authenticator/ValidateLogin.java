/**
 * CAPSTONE PROJECT.
 * BSIT 4A-G1.
 * MONOSYNC TECHNOLOGIES.
 * MONOSYNC FRAMEWORK VERSION 1.0.0 TEACUP RICE ROLL.
 * THIS PROJECT IS PROPRIETARY AND CONFIDENTIAL ANY PART THEREOF.
 * COPYING AND DISTRIBUTION WITHOUT PERMISSION ARE NOT ALLOWED.
 * <p>
 * COLLEGE OF INFORMATION AND COMMUNICATIONS TECHNOLOGY. LINKED SYSTEM.
 * <p>
 * PROJECT MANAGER: JHON MELVIN N. PERELLO DEVELOPERS: JOEMAR N. DELA CRUZ
 * GRETHEL EINSTEIN BERNARDINO
 * <p>
 * OTHER LIBRARIES THAT ARE USED BELONGS TO THEIR RESPECTFUL OWNERS AND AUTHORS.
 * NO COPYRIGHT ARE INTENTIONAL OR INTENDED. THIS PROJECT IS NOT PROFITABLE
 * HENCE FOR EDUCATIONAL PURPOSES ONLY. THIS PROJECT IS ONLY FOR COMPLIANCE TO
 * OUR REQUIREMENTS. THIS PROJECT DOES NOT INCLUDE DISTRIBUTION FOR OTHER
 * PURPOSES.
 */
package org.cict.authentication.authenticator;

import app.lazy.models.*;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.jhmvin.fx.async.CronTimer;
import org.cict.PublicConstants;
import org.cict.ThreadMill;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * General Notice For This Class:
 * <p>
 * <strong>Login Attempts</strong>
 * <ul>
 * <li> This class records every attempts.</li>
 * <li> However it is not checked whether the attempts are recorded
 * successfully.</li>
 * <li> On upcoming revisions insertion of login attempts must be checked.</li>
 * </ul>
 *
 * @author Jhon Melvin
 */
public class ValidateLogin extends Transaction {

    private void logs(Object message) {
        System.out.println("@ValidateLogin: " + message.toString().toUpperCase());
    }

    public String username;
    public String password;

    /**
     * Search cached values.
     */
    private AccountFacultyMapping facultyAccount;
    private Integer createdSessionID;

    /**
     * Flag Values.
     */
    private boolean accountExisting = true;
    private String accountStatus = "OK";
    private boolean wrongPassword = false;
    private String blocked_until = "";
    /**
     * Flag Passed Values (Results)
     */
    private boolean authenticated = false;
    private String authenticatorMessage = "";

    public boolean isAuthenticated() {
        return authenticated;
    }

    public String getAuthenticatorMessage() {
        return authenticatorMessage;
    }

    public Integer getCreatedSessionID() {
        return createdSessionID;
    }

    @Override
    protected boolean transaction() {
        /**
         * Check if existing
         */
        facultyAccount = Mono.orm()
                .newSearch(Database.connect().account_faculty())
                .eq("username", username)
                .execute()
                .first();

        if (facultyAccount == null) {
            logs("account not existing; end of validation");
            /**
             * Set Not Existing message
             */
            authenticatorMessage = "The account you are trying to use is not "
                    + "existing. Please check your account details carefully. Thank You!";
            accountExisting = false;
            return true; // transaction success but there is no account
        }

        /**
         * check if the session end is null
         */
        AccountFacultySessionMapping accountSession = Mono.orm()
                .newSearch(Database.connect().account_faculty_session())
                .eq("FACULTY_account_id", facultyAccount.getId())
                .put(Restrictions.isNull("session_end"))
                .active()
                .first();

        if (accountSession != null) {
            if (!Mono.orm().getServerTime().isPastServerTime(accountSession.getKeep_alive())) {
                authenticatorMessage = "Your account is currently logged-in. "
                        + "Someone maybe using your account on another pc or "
                        + "your account is not properly logged-out.";
                return true;
            } else {
                accountSession.setSession_end(Mono.orm().getServerTime()
                        .getDateWithFormat());
                if (Database.connect().account_faculty_session().update(accountSession)) {
                    logs("@ValidateLogin: SESSION END UPDATED");
                }
            }
        }

        /**
         * If existing check account status.
         */
        logs("checking account status");
        if (facultyAccount.getActive() == 0) {
            /**
             * Set deactivated message.
             */
            authenticatorMessage = "Your account is currently DEACTIVATED. "
                    + "Please contact your local registrar for more details "
                    + "regarding your account. Thank You !";
            //
            createLoginAttempt("DEACTIVATED");
            accountStatus = "DEACTIVATED";
        } else if (facultyAccount.getBlocked_count() >= 3) {
            /**
             * Set Disabled Messaged
             */
            authenticatorMessage = "Your account is currently DISABLED since "
                    + facultyAccount.getDisabled_since() + ", due to "
                    + "many incorrect password attempts. To safeguard your "
                    + "account we have permanently disabled it to disallow "
                    + "further logins. If you really own this account use the "
                    + "recovery options provided or you may contact your local "
                    + "registrar to get more details in recovering your account. "
                    + "Thank You !";
            //
            createLoginAttempt("DISABLED");
            accountStatus = "DISABLED";
        } else if (facultyAccount.getBlocked_until() != null) {
            // checks if the blocked time is past server time (expired).
            if (Mono.orm().getServerTime().isPastServerTime(facultyAccount.getBlocked_until())) {
                // already passed.
            } else {
                // if not yet passed
                SimpleDateFormat hms = new SimpleDateFormat("hh:mm:ss a");
                createLoginAttempt("BLOCKED");
                accountStatus = "BLOCKED";
                blocked_until = hms.format(facultyAccount.getBlocked_until());

                /**
                 * Set Blocked Messaged
                 */
                authenticatorMessage = "Your account is TEMPORARILY BLOCKED until "
                        + blocked_until
                        + ", due to three (3) consecutive incorrect password attempts. "
                        + "Please remember your password carefully before trying again. Thank You!";
            }
        }

        logs("account status is: " + accountStatus);
        logs("blocked_until: " + blocked_until);
        /**
         * If Not OK.
         */
        if (!accountStatus.equalsIgnoreCase("OK")) {
            // transaction is success but account is not allowed.
            logs("account status is not ok; end of validation.");
            return true;
        }

        /**
         * If OK Continue login sequence.
         */
        /**
         * If password do not match.
         */
        String hashedPassword = Mono.security().hashFactory().hash_sha512(password);
        if (!facultyAccount.getPassword().equals(hashedPassword)) {
            logs("wrong password; end of validation");
            createLoginAttempt("WRONG_PASSWORD");
            addWrongCount();
            wrongPassword = true;
            /**
             * Set Wrong Password Message.
             */
            authenticatorMessage = "You have entered an incorrect password. "
                    + "Please remember your password carefully before trying again. "
                    + "Thank You !";

            return true;
        }

        /**
         * If password is correct, Allow Login.
         */
        createLoginAttempt("SUCCESS");
        logs("authentication success account verified");
        logs("setting up authentication gateway");
        /**
         * Authentication Gateway Sequence Starts Here !
         */
        /**
         * Stage 1: Initialize faculty object.
         */
        initializeFaculty();
        /**
         * Stage 2: Create Session.
         */
        createSession();
        /**
         * Stage 3: Initialize Computer object
         */
        initializeComputer();

        /**
         * Reset blocked counters of this account.
         */
        facultyAccount.setBlocked_count(0);
        facultyAccount.setDisabled_since(null);
        facultyAccount.setWrong_count(0);
        facultyAccount.setBlocked_until(null);
        // update
        Database.connect().account_faculty().update(facultyAccount);
        /**
         * End of sequence may now proceed to next window.
         */

        /**
         * Check For current Academic Term 08-19-2017
         */
        AcademicTermMapping currentTerm = Mono.orm()
                .newSearch(Database.connect().academic_term())
                .eq("current", 1)
                .active()
                .first();

        SystemProperties.instance().setCurrentAcademicTerm(currentTerm);

        // -
        //----------------------------------------------------------------------
        /**
         * Authentication Completed.
         */
        authenticated = true; // user is now authenticated.
        // end of transaction
        return true;
    }

    @Override
    protected void after() {

    }

    //--------------------------------------------------------------------------
    /**
     * Registers every attempt in the database.
     *
     * @param res the result description of the attempt.
     */
    private void createLoginAttempt(String res) {
        Integer id = facultyAccount.getId();

        AccountFacultyAttemptMapping attempt = new AccountFacultyAttemptMapping();
        attempt.setAccount_id(id);
        attempt.setResult(res);
        attempt.setPlatform(PublicConstants.PLATFORM_NAME);

        attempt.setIp_address(Mono.sys().getIP());
        attempt.setPc_name(Mono.sys().getTerminal());
        attempt.setPc_username(Mono.sys().getLoggedUser());
        attempt.setOs_version(Mono.sys().getOS());

        Database
                .connect()
                .account_faculty_attempt()
                .insert(attempt);

    }

    /**
     * Updates the wrong count to the database.
     */
    private void addWrongCount() {
        int wrong_entry = facultyAccount.getWrong_count();
        wrong_entry++;
        if (wrong_entry >= 3) {
            Calendar block_until = Mono.orm().getServerTime().getCalendar();
            // block for 5 minutes
            block_until.add(Calendar.MINUTE, 5);
            facultyAccount.setBlocked_until(block_until.getTime());
            wrong_entry = 0;
            // after 3 consecutive blocks the account will be disabled
            facultyAccount.setBlocked_count(facultyAccount.getBlocked_count() + 1);
            /**
             * If more than 3 times to be blocked, disabled the account.
             */
            if (facultyAccount.getBlocked_count() >= 3) {
                facultyAccount.setDisabled_since(Mono.orm().getServerTime().getDateWithFormat());
            }
        }
        facultyAccount.setWrong_count(wrong_entry);
        // update
        Database.connect().account_faculty().update(facultyAccount);
    }

    /**
     * Stores the faculty instance to the JVM.
     */
    private void initializeFaculty() {
        logs("initializing faculty object . . .");

        FacultyMapping collegeFaculty = Mono.orm()
                .newSearch(Database.connect().faculty())
                .eq("id", facultyAccount.getFACULTY_id())
                .execute()
                .first();

        /**
         * Faculty Information Stored in the Java Virtual Machine.
         */
        CollegeFaculty.instance().setFACULTY_ID(collegeFaculty.getId());
        CollegeFaculty.instance().setLAST_NAME(collegeFaculty.getLast_name());
        CollegeFaculty.instance().setFIRST_NAME(collegeFaculty.getFirst_name());
        CollegeFaculty.instance().setMIDDLE_NAME(collegeFaculty.getMiddle_name());
        CollegeFaculty.instance().setGENDER(collegeFaculty.getGender());
        CollegeFaculty.instance().setRANK(collegeFaculty.getRank());
        CollegeFaculty.instance().setDESIGNATION(collegeFaculty.getDesignation());

        /**
         * Faculty Account Information stored in the JVM.
         */
        logs("setting up account details . . .");
        CollegeFaculty.instance().setACCOUNT_ID(facultyAccount.getId());
        CollegeFaculty.instance().setUSERNAME(facultyAccount.getUsername());
        CollegeFaculty.instance().setACCESS_LEVEL(facultyAccount.getAccess_level());
        CollegeFaculty.instance().setTRANSACTION_PIN(facultyAccount.getTransaction_pin());
    }

    private void createSession() {
        logs("creating session");
        AccountFacultySessionMapping loginSession = MapFactory
                .map()
                .account_faculty_session();

        loginSession.setFACULTY_account_id(facultyAccount.getId());
        Calendar sessionTime = Mono.orm().getServerTime().getCalendar();
        loginSession.setSession_start(sessionTime.getTime());
        // add 1 min alive time
        /**
         * Removed, the first increment will happen during first thread run.
         */
        //sessionTime.add(Calendar.MINUTE, 1);
        loginSession.setKeep_alive(sessionTime.getTime());

        /**
         * Session Environment.
         */
        loginSession.setIp_address(Mono.sys().getIP());
        loginSession.setPc_name(Mono.sys().getTerminal());
        loginSession.setPc_username(Mono.sys().getLoggedUser());
        loginSession.setOs(Mono.sys().getOS());
        loginSession.setSession_end(null); // logged out if not null
        loginSession.setPlatform(PublicConstants.PLATFORM_NAME);
        logs("saving session . . .");
        createdSessionID = Database.connect()
                .account_faculty_session()
                .insert(loginSession);
        logs("session saved!");
    }

    /**
     * Stores the hardware information in the JVM.
     */
    private void initializeComputer() {
        logs("verifying session from server");
        AccountFacultySessionMapping localSession = Mono.orm()
                .newSearch(Database.connect().account_faculty_session())
                .eq("session_id", createdSessionID)
                .execute()
                .first();
        logs("verfied successfully");
        CollegeComputer.instance().setSESSION_ID(localSession.getSession_id());
        CollegeComputer.instance().setSESSION_START(localSession.getSession_start());
        CollegeComputer.instance().setIP_ADDRESS(localSession.getIp_address());
        CollegeComputer.instance().setPC_NAME(localSession.getPc_name());
        CollegeComputer.instance().setPC_USERNAME(localSession.getPc_username());
        CollegeComputer.instance().setOS(localSession.getOs());
        CollegeComputer.instance().setPLATFORM(localSession.getPlatform());
        logs("terminal object saved to JVM successfully");
    }

}

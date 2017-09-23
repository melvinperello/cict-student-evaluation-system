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

import app.lazy.models.AccountFacultyMapping;
import app.lazy.models.AccountFacultySessionMapping;
import app.lazy.models.Database;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import java.util.ArrayList;
import org.cict.ThreadMill;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Joemar
 */
public class Logout extends Transaction {

    @Override
    protected boolean transaction() {
        Integer accountFacultyId = this.getAccountFacultyId();
        log("FacultyID " + CollegeFaculty.instance().getFACULTY_ID()
                + " | AccountFacultyID " + accountFacultyId);
        /**
         * @date 08/17/2017
         */
        Session local_session = Mono.orm().openSession();
        if (local_session == null) {
            return false;
        }
        //----------------------------------------------------------------------
        log("Session created ...");
        org.hibernate.Transaction dataTx = local_session.beginTransaction();
        ArrayList<AccountFacultySessionMapping> sessions = Mono.orm()
                .newSearch(Database.connect().account_faculty_session())
                .eq("FACULTY_account_id", accountFacultyId)
                .eq("platform", "EVALUATION_SYSTEM")
                .put(Restrictions.isNull("session_end"))
                .active()
                .all();
        boolean allDone = true;
        for (AccountFacultySessionMapping faculty_session : sessions) {
            faculty_session.setSession_end(Mono.orm().getServerTime().getDateWithFormat());
            boolean isOk = Database.connect()
                    .account_faculty_session()
                    .transactionalSingleUpdate(local_session, faculty_session);
            if (!isOk) {
                allDone = false;
                break;
            } else {
                log("Session success update ...");
                ThreadMill.threads().KEEP_ALIVE_THREAD.pause();
                ThreadMill.threads().KEEP_ALIVE_THREAD.stop();
            }
        }

        if (allDone) {
            dataTx.commit();
            log("Transaction commit ...");
        } else {
            dataTx.rollback();
            log("Transaction rollback ...");
        }
        return true;
    }

    @Override
    protected void after() {

    }

    private Integer getAccountFacultyId() {
        AccountFacultyMapping accountFaculty = Mono.orm()
                .newSearch(Database.connect().account_faculty())
                .eq("FACULTY_id", CollegeFaculty.instance().getFACULTY_ID())
                .active()
                .first();
        return accountFaculty.getId();
    }

    private void log(String log) {
        System.out.println("@Logout: " + log);
    }

}

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
package update3.org.cict.controller.sectionmain.deltesection;

import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.LoadGroupMapping;
import app.lazy.models.LoadGroupScheduleMapping;
import app.lazy.models.LoadSectionMapping;
import app.lazy.models.LoadSubjectMapping;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import java.util.ArrayList;
import java.util.Date;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.hibernate.Session;

/**
 * This class will be use in deleting regular and irregular sections.
 *
 * @author Jhon Melvin
 */
public class DeleteSectionTransaction extends Transaction {

    /**
     * Required Values.
     */
    private Integer loadSectionID;

    public void setLoadSectionID(Integer loadSectionID) {
        this.loadSectionID = loadSectionID;
    }

    private void logTx(Object message) {
        System.out.println(this.getClass().getSimpleName() + ": " + message.toString());
    }

    @Override
    protected boolean transaction() {
        Date currentDate = Mono.orm().getServerTime().getDateWithFormat();

        Session localSession = Mono.orm().openSession();
        org.hibernate.Transaction dataTx = localSession.beginTransaction();

        // find the section map
        LoadSectionMapping section = Database.connect()
                .load_section()
                .getPrimary(loadSectionID);

        if (section == null) {
            logTx("Section Not Found");
            dataTx.rollback();
            return false;
        }

        // update valeus
        section.setUpdated_by(CollegeFaculty.instance().getFACULTY_ID());
        section.setUpdated_date(currentDate);
        section.setActive(0);

        boolean section_updated = Database
                .connect().load_section()
                .transactionalSingleUpdate(localSession, section);
        // cancel
        if (!section_updated) {
            logTx("Failed To Update Section");
            dataTx.rollback();
            return false;
        }

        // get load groups
        ArrayList<LoadGroupMapping> section_subjects = Mono.orm()
                .newSearch(Database.connect().load_group())
                .eq(DB.load_group().LOADSEC_id, section.getId())
                .execute()
                .all();

        if (section_subjects == null) {
            // there are no subjects for this section
            // we can already commit here because if no subjects there are also
            // no schedules and no students so it is safe to delete the section
            logTx("There are no subject . . . completed");
            dataTx.commit();
            localSession.close();
            return true;
        }

        // if not empty check if there are students enrolled
        boolean hasStudent = false;
        for (LoadGroupMapping section_subject : section_subjects) {
            // students
            ArrayList<LoadSubjectMapping> section_subject_students = Mono.orm()
                    .newSearch(Database.connect().load_subject())
                    .eq(DB.load_subject().LOADGRP_id, section_subject.getId())
                    .execute()
                    .all();

            if (section_subject_students != null) {
                // there are students
                hasStudent = true;
                break;
            }
        }

        // cannot delete already has students
        if (hasStudent) {
            logTx("Cannot proceed there are students");
            dataTx.rollback();
            return false;
        }

        // if no students proceed to deletion
        for (LoadGroupMapping section_subject : section_subjects) {
            section_subject.setRemoved_by(CollegeFaculty.instance().getFACULTY_ID());
            section_subject.setRemoved_date(currentDate);
            section_subject.setActive(0);
            boolean groupUpdated = Database.connect()
                    .load_group()
                    .transactionalSingleUpdate(localSession, section_subject);

            if (!groupUpdated) {
                // failed to update
                logTx("Failed to update group");
                dataTx.rollback();
                return false;
            }

            // while it is not failing delete also the schedules
            ArrayList<LoadGroupScheduleMapping> schedules = Mono.orm()
                    .newSearch(Database.connect().load_group_schedule())
                    .eq(DB.load_group_schedule().load_group_id, section_subject.getId())
                    .execute()
                    .all();

            if (schedules == null) {
                continue;
            }

            for (LoadGroupScheduleMapping sched : schedules) {
                sched.setUpdated_by(CollegeFaculty.instance().getFACULTY_ID());
                sched.setUpdated_date(currentDate);
                sched.setActive(0);
                boolean updatedSched = Database.connect()
                        .load_group_schedule()
                        .transactionalSingleUpdate(localSession, sched);
                // if failed to update.
                if (!updatedSched) {
                    logTx("Failed to update sched");
                    dataTx.rollback();
                    return false;
                }
            }

        } // end super loop

        // completed without any errors.
        logTx("Completed Withou Errors");
        dataTx.commit();
        return true;
    }

    @Override
    protected void after() {

    }

}

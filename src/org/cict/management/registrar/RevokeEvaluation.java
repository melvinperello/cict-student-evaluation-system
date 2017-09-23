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

import app.lazy.models.*;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import java.util.ArrayList;
import org.hibernate.Session;

/**
 *
 * @author Jhon Melvin
 */
public class RevokeEvaluation extends Transaction {

    /**
     * Transaction require.
     */
    public int cict_id;
    public int academic_term;
    public int registrar_id;

    /**
     *
     */
    private EvaluationMapping revokingEvaluation;

    @Override
    protected boolean transaction() {

        revokingEvaluation = Mono.orm()
                .newSearch(Database.connect().evaluation())
                .eq("STUDENT_id", cict_id)
                .eq("ACADTERM_id", academic_term)
                .eq(DB.evaluation().type,"REGULAR")
                .active()
                .first();

        if (revokingEvaluation == null) {
            System.out.println("RevokeEvaluation: Evaluation is null");
            return false;
        }

        /**
         * Always use this when there is a updates or insert with multiple
         * tables.
         */
        // create local session
        Session currentSession = Mono.orm().session();
        // start your transaction
        org.hibernate.Transaction dataTransaction = currentSession.beginTransaction();

        revokingEvaluation.setRemarks("REVOKED");
        revokingEvaluation.setCancelled_by(registrar_id);
        revokingEvaluation.setCancelled_date(Mono.orm().getServerTime().getDateWithFormat());
        revokingEvaluation.setActive(0);

        /**
         * update evaluation entry.
         */
        Boolean update_result = Database
                .connect()
                .evaluation()
                .transactionalSingleUpdate(currentSession, revokingEvaluation);

        /**
         * If failed rollback update.
         */
        if (!update_result) {
            System.out.println("RevokeEvaluation: Error in evaluation update");
            dataTransaction.rollback();
            return false;
        }

        /**
         * Revoke all load_subject.
         */
        ArrayList<LoadSubjectMapping> revoke_load_subjects = Mono
                .orm()
                .newSearch(Database.connect().load_subject())
                .eq("STUDENT_id", cict_id)
                .eq("EVALUATION_id", revokingEvaluation.getId())
                .active()
                .all();

        for (LoadSubjectMapping revoke_load_subject : revoke_load_subjects) {
            revoke_load_subject.setRemarks("REVOKED_EVALUATION");
            revoke_load_subject.setRemoved_date(Mono.orm().getServerTime().getDateWithFormat());
            revoke_load_subject.setRemoved_by(registrar_id);
            revoke_load_subject.setActive(0);

            Boolean revoke_load = Database
                    .connect()
                    .load_subject()
                    .transactionalSingleUpdate(currentSession, revoke_load_subject);

            if (!revoke_load) {
                System.out.println("RevokeEvaluation: Error in load subject update");
                dataTransaction.rollback();
                return false;
            }
        }

        /**
         * Revoke all grades.
         */
        ArrayList<GradeMapping> revoke_grades = Mono.orm()
                .newSearch(Database.connect().grade())
                .eq("STUDENT_id", cict_id)
                .eq("ACADTERM_id", academic_term)
                .eq("rating", "UNPOSTED")
                .eq("remarks", "UNPOSTED")
                .eq("posted", 0)
                .active()
                .all();

        for (GradeMapping revoke_grade : revoke_grades) {
            revoke_grade.setRating("CANCELLED");
            revoke_grade.setRemarks("CANCELLED");
            revoke_grade.setCredit(0.00);
            revoke_grade.setUpdated_by(registrar_id);
            revoke_grade.setUpdated_date(Mono.orm().getServerTime().getDateWithFormat());
            revoke_grade.setReason_for_update("REVOKED_EVALUATION");
            revoke_grade.setActive(0);

            Boolean is_revoke_grade = Database
                    .connect()
                    .grade()
                    .transactionalSingleUpdate(currentSession, revoke_grade);

            if (!is_revoke_grade) {
                System.out.println("RevokeEvaluation: Error in grade update");
                dataTransaction.rollback();
                return false;
            }
        }

        /**
         * Commit changes.
         */
        dataTransaction.commit();
        return true;
    }

    @Override
    protected void after() {

    }

}

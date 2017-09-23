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
package org.cict.evaluation.student.credit;

import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.GradeMapping;
import app.lazy.models.MapFactory;
import app.lazy.models.SubjectMapping;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import java.util.ArrayList;
import org.cict.PublicConstants;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

/**
 *
 * @author Jhon Melvin
 */
public class CreditEncode extends Transaction {

    private void logs(Object message) {
        System.out.println("@CreditEncode: " + message.toString());
    }
    public Integer cict_id;
    public ArrayList<Object[]> grades;

    /**
     * When using credit tree academic terms are always null.
     *
     * @return
     */
    @Override
    protected boolean transaction() {

        Session local_session = Mono.orm().openSession();
        // if server cannot create session.
        if (local_session == null) {
            return false;
        }

        org.hibernate.Transaction dataTx = local_session.beginTransaction();

        for (Object[] grade : grades) {
            /**
             * Iteration values values.
             */
            Integer subject_id = (Integer) grade[0];
            String rating = (String) grade[1];

            /**
             * Checks if subject is existing.
             */
            SubjectMapping subject = Mono.orm()
                    .newSearch(Database.connect().subject())
                    .eq(DB.subject().id, subject_id)
                    .execute()
                    .first();

            if (subject == null) {
                // cancel transaction
                return false;
            }

            /**
             * Check if student has grade in a particular subject.
             */
            GradeMapping studentGrade = Mono.orm()
                    .newSearch(Database.connect().grade())
                    .eq(DB.grade().STUDENT_id, cict_id)
                    .eq(DB.grade().SUBJECT_id, subject_id)
                    .active(Order.desc(DB.grade().id))
                    .first();

            /**
             * if no grade insert new
             */
            if (studentGrade == null) {
                logs("Student GRADE is null");
                studentGrade = MapFactory.map().grade();
                studentGrade.setSTUDENT_id(cict_id);
                studentGrade.setSUBJECT_id(subject_id);
                studentGrade.setCreated_by(CollegeFaculty.instance().getFACULTY_ID());
                studentGrade.setCreated_date(Mono.orm().getServerTime().getDateWithFormat());
                studentGrade.setPosted(1);
                studentGrade.setPosted_by(CollegeFaculty.instance().getFACULTY_ID());
                studentGrade.setPosting_date(Mono.orm().getServerTime().getDateWithFormat());
                studentGrade.setCredit_method("REGULAR");
                studentGrade.setCredit(subject.getLab_units() + subject.getLec_units());

                studentGrade.setRating(rating);
                studentGrade.setRemarks(PublicConstants.getRemarks(rating));

                Integer id = Database.connect().grade()
                        .transactionalInsert(local_session, studentGrade);
                if (id < 0) {
                    // error in insert
                    dataTx.rollback();
                    logs("Failed to insert new");
                    return false;
                }
            } else {

                /**
                 * Checks if the grade is not changed.
                 */
                if (rating.equalsIgnoreCase(studentGrade.getRating())) {
                    // continue to next iteration of the loop
                    logs("Skipping unmodified grades");
                    continue;
                    /**
                     * Everything below will be skipped.
                     */
                }
                logs("Updating grades.");
                
                
                /**
                 * When there is already a grade, make that record inactive.
                 */
                studentGrade.setActive(0);
                boolean res = Database.connect().grade().transactionalSingleUpdate(local_session, studentGrade);

                if (!res) {

                    dataTx.rollback();
                    logs("Failed to update old");
                    return false;
                }

                /**
                 * Insert a new record with updated values.
                 */
                GradeMapping grade_copy = MapFactory.map().grade();
                grade_copy.setRating(rating);
                grade_copy.setRemarks(PublicConstants.getRemarks(rating));

                /**
                 * If the grade is already posted. copy values from previous.
                 */
                if (studentGrade.getPosted() == 1) {
                    grade_copy.setPosted(studentGrade.getPosted());
                    grade_copy.setPosted_by(studentGrade.getPosted_by());
                    grade_copy.setPosting_date(studentGrade.getPosting_date());
                } else {
                    /**
                     * If grade is not posted. assign new values
                     */
                    grade_copy.setPosted(1);
                    grade_copy.setPosted_by(CollegeFaculty.instance().getFACULTY_ID());
                    grade_copy.setPosting_date(Mono.orm().getServerTime().getDateWithFormat());
                }

                grade_copy.setUpdated_by(CollegeFaculty.instance().getFACULTY_ID());
                grade_copy.setUpdated_date(Mono.orm().getServerTime().getDateWithFormat());
                // copied values
                grade_copy.setSTUDENT_id(studentGrade.getSTUDENT_id());
                grade_copy.setSUBJECT_id(studentGrade.getSUBJECT_id());
                grade_copy.setCreated_by(studentGrade.getCreated_by());
                grade_copy.setCreated_date(studentGrade.getCreated_date());
                //
                grade_copy.setCredit_method("CREDIT");
                grade_copy.setCredit(subject.getLab_units() + subject.getLec_units());

                Integer new_id = Database.connect().grade().transactionalInsert(local_session, grade_copy);

                if (new_id < 0) {
                    // error in insert
                    dataTx.rollback();
                    logs("Failed to upsert");
                    return false;
                }
            }
        } // end of loop

        /**
         * If the loop was finished without errors.
         */
        dataTx.commit();
        local_session.close();
        logs("complete");
        return true;
    }

    @Override
    protected void after() {

    }

}

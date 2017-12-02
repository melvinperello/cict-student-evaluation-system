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
import java.util.Calendar;
import java.util.Date;
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
    //--------------------------------------------------------------------------
    // passed values
    public Integer cict_id;
    public ArrayList<Object[]> grades;
    //--------------------------------------------------------------------------
    // current transaction values
    //----------------------------------------------------------------------
    private Integer facultyUpdater;
    private Date updateDate;
    private Calendar incExpireTime;

    //--------------------------------------------------------------------------
    /**
     * When using credit tree academic terms are always null.
     *
     * @return
     */
    @Override
    protected boolean transaction() {
        this.facultyUpdater = CollegeFaculty.instance().getFACULTY_ID();
        this.updateDate = Mono.orm().getServerTime().getDateWithFormat();
        //----------------------------------------------------------------------
        this.incExpireTime = Mono.orm().getServerTime().getCalendar();
        this.incExpireTime.add(Calendar.MONTH, PublicConstants.INC_EXPIRE);
        //----------------------------------------------------------------------
        // start local transaction using open session
        Session local_session = Mono.orm().openSession();
        // if server cannot create session.
        if (local_session == null) {
            return false;
        }
        org.hibernate.Transaction dataTx = local_session.beginTransaction();
        //----------------------------------------------------------------------
        // iterate grades from the credit tree
        // @loopName: gradeObjectLoop
        for (Object[] grade : grades) {
            //------------------------------------------------------------------
            // Get values
            Integer subject_id = (Integer) grade[0];
            String rating = (String) grade[1];
            //------------------------------------------------------------------
            // check if subject is existing
            SubjectMapping subject = Mono.orm()
                    .newSearch(Database.connect().subject())
                    .eq(DB.subject().id, subject_id)
                    .execute()
                    .first();
            if (subject == null) {
                // cancel transaction
                return false;
            }
            //------------------------------------------------------------------
            // Check if already has a grade
            GradeMapping studentGrade = Mono.orm()
                    .newSearch(Database.connect().grade())
                    .eq(DB.grade().STUDENT_id, cict_id)
                    .eq(DB.grade().SUBJECT_id, subject_id)
                    .active(Order.desc(DB.grade().id))
                    .first();
            //------------------------------------------------------------------
            if (studentGrade == null) {
                if (rating.equalsIgnoreCase("")) {
                    continue;
                }
                //--------------------------------------------------------------
                // THIS IS THE FIRST GRADE OF THIS STUDENT TO THIS SUBJECT
                //--------------------------------------------------------------
                // create new grade
                GradeMapping newGrade = this.whenNoGrade(subject, rating);
                // insert new grade
                Integer id = Database.connect().grade()
                        .transactionalInsert(local_session, newGrade);
                if (id < 0) {
                    // error in insert
                    dataTx.rollback();
                    logs("Failed to insert new");
                    return false;
                }
            } else {
                //--------------------------------------------------------------
                // GRADE ALREADY EXISTING UPDATE BLOCK
                //--------------------------------------------------------------
                // when grade is not modified do no do anything
                if (rating.equalsIgnoreCase(studentGrade.getRating())) {
                    // continue to next iteration of the loop
                    logs("Skipping unmodified grades");
                    continue;
                }
                //--------------------------------------------------------------
                // If the grade was updated to empty it means grade will be deleted
                if (rating.equalsIgnoreCase("")) {
                    //----------------------------------------------------------
                    // DELETE GRADE
                    //----------------------------------------------------------
                    // copy every values before updating anything
                    // INTRODUCING NEW FEATURE COPY
                    GradeMapping gradeCopy = studentGrade.copy();
                    System.out.println("CLONE" + gradeCopy.getRating());
                    //----------------------------------------------------------
                    // deactivate current student grade
                    studentGrade.setActive(0);
                    boolean res = Database.connect().grade().transactionalSingleUpdate(local_session, studentGrade);
                    if (!res) {
                        dataTx.rollback();
                        logs("Failed to update old");
                        return false;
                    }
                    //----------------------------------------------------------
                    // update the copied value and who initiated the delete
                    gradeCopy.setUpdated_by(facultyUpdater);
                    gradeCopy.setUpdated_date(updateDate);
                    gradeCopy.setReason_for_update("GRADE REMOVED");
                    // since this is a removed grade active must be 0 for history only
                    gradeCopy.setActive(0);
                    // insert this grade
                    Integer new_id = Database.connect().grade().transactionalInsert(local_session, gradeCopy);
                    if (new_id < 0) {
                        // error in insert
                        dataTx.rollback();
                        logs("Failed to upsert");
                        return false;
                    } else {
                        // grade updated successfully
                        System.out.println("DELETED GRADE");
                        continue; // do not proceed below
                    }
                }
                //--------------------------------------------------------------
                // DEACTIVATE OLD BUT INSERT NEW ACTIVE WITH UPDATED VALUES
                //--------------------------------------------------------------
                // copy every values before updating anything
                // INTRODUCING NEW FEATURE COPY
                GradeMapping toUpdateCopy = studentGrade.copy();
                //--------------------------------------------------------------
                // After copying deactivate the old one
                studentGrade.setActive(0);
                boolean updateOld = Database.connect().grade().transactionalSingleUpdate(local_session, studentGrade);
                if (!updateOld) {
                    dataTx.rollback();
                    logs("Failed to update old");
                    return false;
                }
                //--------------------------------------------------------------
                // Proceed to updating values of the new entry
                toUpdateCopy.setRating(rating);
                toUpdateCopy.setRemarks(PublicConstants.getRemarks(rating));
                //--------------------------------------------------------------
                if (toUpdateCopy.getPosted() == 0) {
                    // If not posted post it
                    toUpdateCopy.setPosted(1);
                    toUpdateCopy.setPosted_by(CollegeFaculty.instance().getFACULTY_ID());
                    toUpdateCopy.setPosting_date(Mono.orm().getServerTime().getDateWithFormat());
                }
                //--------------------------------------------------------------
                toUpdateCopy.setUpdated_by(facultyUpdater);
                toUpdateCopy.setUpdated_date(updateDate);
                //--------------------------------------------------------------
                toUpdateCopy.setCredit_method("CREDIT");
                toUpdateCopy.setCredit(subject.getLab_units() + subject.getLec_units());
                //--------------------------------------------------------------
                if (toUpdateCopy.getRating().equalsIgnoreCase("INC")) {
                    toUpdateCopy.setInc_expire(incExpireTime.getTime());
                }
                //--------------------------------------------------------------
                toUpdateCopy.setReason_for_update("GRADE MODIFICATION USING CREDIT TREE");
                Integer new_id = Database.connect().grade().transactionalInsert(local_session, toUpdateCopy);
                if (new_id < 0) {
                    // error in insert
                    dataTx.rollback();
                    logs("Failed to upsert");
                    return false;
                }
                //--------------------------------------------------------------
            }
        } // @endLoop: gradeObjectLoop

        /**
         * If the loop was finished without errors.
         */
        dataTx.commit();
        local_session.close();
        logs("complete");
        return true;
    }

    private GradeMapping whenNoGrade(
            SubjectMapping subject,
            String rating
    ) {

        logs("No Active Grade Going To Insert New");
        GradeMapping studentGrade = MapFactory.map().grade();
        studentGrade.setSTUDENT_id(cict_id);
        studentGrade.setSUBJECT_id(subject.getId());
        studentGrade.setRating(rating);
        studentGrade.setRemarks(PublicConstants.getRemarks(rating));
        //--------------------------------------------------------------
        studentGrade.setCreated_by(facultyUpdater);
        studentGrade.setCreated_date(updateDate);
        // set also the update
        studentGrade.setUpdated_by(facultyUpdater);
        studentGrade.setUpdated_date(updateDate);
        //--------------------------------------------------------------
        studentGrade.setPosted(1);
        studentGrade.setPosted_by(facultyUpdater);
        studentGrade.setPosting_date(updateDate);
        studentGrade.setCredit_method("CREDIT");
        studentGrade.setCredit(subject.getLab_units() + subject.getLec_units());
        //--------------------------------------------------------------
        if (studentGrade.getRating().equalsIgnoreCase("INC")) {
            studentGrade.setInc_expire(incExpireTime.getTime());
        }
        //--------------------------------------------------------------
        studentGrade.setReason_for_update("ADDED USING CREDIT TREE");
        //----------------------------------------------------------------------
        return studentGrade;
    }

}

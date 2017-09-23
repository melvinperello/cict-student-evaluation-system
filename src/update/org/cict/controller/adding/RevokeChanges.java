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
package update.org.cict.controller.adding;

import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.EvaluationMapping;
import app.lazy.models.GradeMapping;
import app.lazy.models.LoadSubjectMapping;
import app.lazy.models.SubjectMapping;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import java.util.ArrayList;
import java.util.Date;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

/**
 *
 * @author Joemar
 */
public class RevokeChanges extends Transaction{

    public EvaluationMapping currentEvaluation;
    
    private Integer CANCELLED_BY = CollegeFaculty.instance().getFACULTY_ID();
    private Date CANCELLED_DATE = Mono.orm().getServerTime().getDateWithFormat();
    private EvaluationMapping OLD_EVALUATION;
    private String REMARKS_REVOKED_ADD_CHANGE = "REVOKED_ADD_CHANGE";
    private String REMARKS_ACCEPTED = "ACCEPTED";
    private String UNPOSTED = "UNPOSTED";
    private String CANCELLED = "CANCELLED";
    
    @Override
    protected boolean transaction() {
        
        Session local_session = Mono.orm().openSession();
        org.hibernate.Transaction dataTx = local_session.beginTransaction();

        /**
         * revoke current evaluation
         * 
         */
        currentEvaluation.setActive(0);
        currentEvaluation.setRemarks(REMARKS_REVOKED_ADD_CHANGE);
        currentEvaluation.setCancelled_by(CANCELLED_BY);
        currentEvaluation.setCancelled_date(CANCELLED_DATE);
        if(Database.connect().evaluation().update(currentEvaluation)) {
            System.out.println("SUCCESSFULLY REVOKED EVALUATION");
        } else {
            dataTx.rollback();
            return false;
        }
        
        /**
         * retrive old evaluation
         */
        Integer referenceId = currentEvaluation.getAdding_reference_id();
        OLD_EVALUATION = Mono.orm()
                .newSearch(Database.connect().evaluation())
                .eq(DB.evaluation().id, referenceId)
                .execute()
                .first();
        
        if(OLD_EVALUATION == null) {
            System.out.println("No existing evaluation found");
            dataTx.rollback();
            return false;
        }
        
        OLD_EVALUATION.setActive(1);
        OLD_EVALUATION.setRemarks(REMARKS_ACCEPTED);
        /**
         * *************************************************
         */
        OLD_EVALUATION.setCancelled_by(null);
        OLD_EVALUATION.setCancelled_date(null);
        if(Database.connect().evaluation().update(OLD_EVALUATION)) {
            System.out.println("SUCCESSFULLY RETRIVED EVALUATION");
        } else {
            dataTx.rollback();
            return false;
        }
        
        /**
         * revoked current load subjects
         */
        Integer studentId = currentEvaluation.getSTUDENT_id();
        ArrayList<LoadSubjectMapping> currentLoadSubjects = Mono.orm()
                .newSearch(Database.connect().load_subject())
                .eq(DB.load_subject().EVALUATION_id, currentEvaluation.getId())
                .execute()
                .all();
        for(LoadSubjectMapping currentLoadSubject: currentLoadSubjects) {
            currentLoadSubject.setActive(0);
            currentLoadSubject.setRemarks(REMARKS_REVOKED_ADD_CHANGE);
            currentLoadSubject.setRemoved_by(CANCELLED_BY);
            currentLoadSubject.setRemoved_date(CANCELLED_DATE);
            
            if(!Database.connect().load_subject().update(currentLoadSubject)) {
                System.out.println("Error in current load subjects");
                dataTx.rollback();
                return false;
            }
              
            /**
             * revoke current grades
             */
            GradeMapping currentGrade = Mono.orm()
                    .newSearch(Database.connect().grade())
                    .eq(DB.grade().STUDENT_id, studentId)
                    .eq(DB.grade().SUBJECT_id, currentLoadSubject.getSUBJECT_id())
                    .eq(DB.grade().remarks, UNPOSTED)
                    .active(Order.desc(DB.grade().id))
                    .first();
            currentGrade.setActive(0);
            currentGrade.setRemarks(CANCELLED);
            currentGrade.setRating(CANCELLED);
            currentGrade.setCredit(0.0);
            currentGrade.setUpdated_by(CANCELLED_BY);
            currentGrade.setUpdated_date(CANCELLED_DATE);
            currentGrade.setReason_for_update(REMARKS_REVOKED_ADD_CHANGE);
            Boolean res = Database.connect().grade().update(currentGrade);
            if(res == false) {
                System.out.println("Error in current grade");
                dataTx.rollback();
                return false;
            }
        }
        
        /**
         * retrive old load subjects
         */
        ArrayList<LoadSubjectMapping> oldLoadSubjects = Mono.orm()
                .newSearch(Database.connect().load_subject())
                .eq(DB.load_subject().EVALUATION_id, referenceId)
                .execute()
                .all();
        for(LoadSubjectMapping oldLoadSubject: oldLoadSubjects) {
            oldLoadSubject.setActive(1);
            oldLoadSubject.setRemarks(REMARKS_ACCEPTED);
            /**
             * ****************************************************
             */
            oldLoadSubject.setRemoved_by(null);
            oldLoadSubject.setRemoved_date(null);
            if(!Database.connect().load_subject().update(oldLoadSubject)) {
                System.out.println("Error in old load subjects");
                dataTx.rollback();
                return false;
            }
             
            /**
             * retrieve old grades
             */
            SubjectMapping subjectMap = Mono.orm()
                    .newSearch(Database.connect().subject())
                    .eq(DB.subject().id, oldLoadSubject.getSUBJECT_id())
                    .execute()
                    .first();
            GradeMapping oldGrade = Mono.orm()
                    .newSearch(Database.connect().grade())
                    .eq(DB.grade().STUDENT_id, studentId)
                    .eq(DB.grade().SUBJECT_id, oldLoadSubject.getSUBJECT_id())
                    .eq(DB.grade().remarks, CANCELLED)
                    .execute(Order.desc(DB.grade().id))
                    .first();
            oldGrade.setActive(1);
            oldGrade.setRemarks(UNPOSTED);
            oldGrade.setRating(UNPOSTED);
            oldGrade.setCredit(subjectMap.getLec_units() + subjectMap.getLab_units());
            oldGrade.setUpdated_by(CANCELLED_BY);
            oldGrade.setUpdated_date(CANCELLED_DATE);
            oldGrade.setReason_for_update(REMARKS_REVOKED_ADD_CHANGE);
            Boolean res = Database.connect().grade().update(oldGrade);
            if(res == false) {
                System.out.println("Error in old grade");
                dataTx.rollback();
                return false;
            }
        
        }
       
        dataTx.commit();
        local_session.close();
        return true;
    }

    @Override
    protected void after() {
    
    }
    
}

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
package update.org.cict.controller.adding;

import app.lazy.models.AcademicProgramMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.EvaluationMapping;
import app.lazy.models.GradeMapping;
import app.lazy.models.LoadGroupMapping;
import app.lazy.models.LoadSectionMapping;
import app.lazy.models.LoadSubjectMapping;
import app.lazy.models.MapFactory;
import app.lazy.models.SubjectMapping;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import java.util.ArrayList;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.hibernate.Session;

/**
 *
 * @author Jhon Melvin
 */
public class SaveChanges extends Transaction {

    //required
    public EvaluationMapping evaluationDetails;
    public ArrayList<Object[]> details;
    //

    private final ArrayList<SubjectInformationHolder> transactionReport = new ArrayList<>();
    private Integer newEvaluationID;
    
    public Integer getNewEvaluationID() {
        return newEvaluationID;
    }
    
    public ArrayList<SubjectInformationHolder> getTransactionReport() {
        return transactionReport;
    }

    @Override
    protected boolean transaction() {

        Session local_session = Mono.orm().openSession();
        org.hibernate.Transaction dataTx = local_session.beginTransaction();

        // deactivate current evaluation
        /**
         * Unload: load_subjects;
         */
        ArrayList<LoadSubjectMapping> load_subjects = Mono.orm()
                .newSearch(Database.connect().load_subject())
                .eq(DB.load_subject().EVALUATION_id, this.evaluationDetails.getId())
                .execute()
                .all();

        for (LoadSubjectMapping load_subject : load_subjects) {
            load_subject.setActive(0);
            load_subject.setRemarks("ADDING_CHANGING");
            load_subject.setRemoved_date(Mono.orm().getServerTime().getDateWithFormat());
            load_subject.setRemoved_by(CollegeFaculty.instance().getFACULTY_ID());

            boolean res = Database.connect()
                    .load_subject()
                    .transactionalSingleUpdate(local_session, load_subject);

            if (!res) {
                dataTx.rollback();
                return false;
            }
        }

        /**
         * Unload evaluation
         */
        EvaluationMapping previous_evaluation = Mono.orm()
                .newSearch(Database.connect().evaluation())
                .eq(DB.evaluation().id, this.evaluationDetails.getId())
                .execute()
                .first();

        previous_evaluation.setRemarks("ADDING_CHANGING_REFERENCE");
        previous_evaluation.setCancelled_by(CollegeFaculty.instance().getFACULTY_ID());
        previous_evaluation.setCancelled_date(Mono.orm().getServerTime().getDateWithFormat());
        previous_evaluation.setActive(0);

        boolean res_inactivate_eval = Database.connect()
                .evaluation().transactionalSingleUpdate(local_session, previous_evaluation);

        if (!res_inactivate_eval) {
            dataTx.rollback();
            return false;
        }
        /**
         * Cancel all un posted grades.
         */
        ArrayList<GradeMapping> unposted_grades = Mono.orm()
                .newSearch(Database.connect().grade())
                .eq(DB.grade().STUDENT_id, previous_evaluation.getSTUDENT_id())
                .eq(DB.grade().ACADTERM_id, previous_evaluation.getACADTERM_id())
                .eq(DB.grade().rating, "UNPOSTED")
                .eq(DB.grade().remarks, "UNPOSTED")
                .eq(DB.grade().posted, 0)
                .active()
                .all();
        if(unposted_grades != null) {
            for (GradeMapping unposted_grade : unposted_grades) {
                unposted_grade.setRemarks("CANCELLED");
                unposted_grade.setRating("CANCELLED");
                unposted_grade.setCredit(0.00);
                unposted_grade.setUpdated_by(CollegeFaculty.instance().getFACULTY_ID());
                unposted_grade.setUpdated_date(Mono.orm().getServerTime().getDateWithFormat());
                unposted_grade.setReason_for_update("ADDING_CHANGING");
                unposted_grade.setActive(0);

                boolean _grade = Database.connect().grade().transactionalSingleUpdate(local_session, unposted_grade);

                if (!_grade) {
                    dataTx.rollback();
                    return false;
                }
            }
        }

        /**
         * Create new evaluation set previous evaluation as reference
         */
        EvaluationMapping new_evaluation = MapFactory.map().evaluation();
        new_evaluation.setSTUDENT_id(previous_evaluation.getSTUDENT_id());
        new_evaluation.setACADTERM_id(previous_evaluation.getACADTERM_id());
        new_evaluation.setFACULTY_id(CollegeFaculty.instance().getFACULTY_ID());
        //reference
        new_evaluation.setAdding_reference_id(previous_evaluation.getId());
        new_evaluation.setEvaluation_date(Mono.orm().getServerTime().getDateWithFormat());
        new_evaluation.setYear_level(previous_evaluation.getYear_level());
        new_evaluation.setType("ADDING_CHANGING");
        new_evaluation.setRemarks("ACCEPTED");

        newEvaluationID = Database.connect().evaluation().transactionalInsert(local_session, new_evaluation);
        if (newEvaluationID <= 0) {
            dataTx.rollback();
            return false;
        }

        for (Object[] detail : details) {
            
            SubjectInformationHolder info = (SubjectInformationHolder) detail[1];

            String status = (String) detail[0];
            SubjectInformationHolder old_info = null;
            LoadSubjectMapping new_load = MapFactory.map().load_subject();
            if (status.equalsIgnoreCase("REMOVED")) {
                // skip rows marked as removed.
                continue;
            } else if(status.equalsIgnoreCase("CHANGED")) {
                old_info = (SubjectInformationHolder) detail[2];
                LoadGroupMapping lgMap = old_info.getLoadGroup();
                System.out.println("FROM " + old_info.getSubjectMap().getCode()
                + " - " + info.getSubjectMap().getCode());
                System.out.println("FROM " + old_info.getFullSectionName()
                + " - " + info.getFullSectionName());
                new_load.setChanging_reference(lgMap.getId());
            }

//            SubjectInformationHolder info = (SubjectInformationHolder) detail[1];

            // 4 table join
            LoadSectionMapping load_sec = info.getSectionMap();
            SubjectMapping subject_map = info.getSubjectMap();
            AcademicProgramMapping acad_map = info.getAcademicProgramMapping();
            LoadGroupMapping load_group = info.getLoadGroup();

            /**
             * Create new load_subjects
             */
            new_load.setSUBJECT_id(subject_map.getId());
            new_load.setLOADGRP_id(load_group.getId());
            new_load.setSTUDENT_id(new_evaluation.getSTUDENT_id());
            new_load.setEVALUATION_id(newEvaluationID);
            new_load.setAdded_date(Mono.orm().getServerTime().getDateWithFormat());
            new_load.setAdded_by(CollegeFaculty.instance().getFACULTY_ID());
            new_load.setRemarks("ACCEPTED");

            int load_new = Database.connect().load_subject().transactionalInsert(local_session, new_load);
            if (load_new <= 0) {
                dataTx.rollback();
                return false;
            }

            /**
             * Create New Grades for updated subjects.
             */
            GradeMapping new_grades = MapFactory.map().grade();
            new_grades.setSTUDENT_id(new_evaluation.getSTUDENT_id());
            new_grades.setSUBJECT_id(subject_map.getId());
            new_grades.setACADTERM_id(new_evaluation.getACADTERM_id());
            new_grades.setRemarks("UNPOSTED");
            new_grades.setRating("UNPOSTED");
            new_grades.setCredit(subject_map.getLec_units() + subject_map.getLab_units());
            new_grades.setCredit_method("REGULAR");
            new_grades.setCreated_by(CollegeFaculty.instance().getFACULTY_ID());
            new_grades.setCreated_date(Mono.orm().getServerTime().getDateWithFormat());
            new_grades.setPosted(0);
            new_grades.setActive(1);

            int _grade_insert = Database.connect().grade().transactionalInsert(local_session, new_grades);

            if (_grade_insert <= 0) {
                dataTx.rollback();
                return false;
            }

            /**
             * if this was successfully inserted return add list for return.
             */
            if (status.equalsIgnoreCase("ADDED") || status.equalsIgnoreCase("CHANGED")) {
                transactionReport.add(info);
            }
        }

        /**
         * No errors occurred.
         */
        dataTx.commit();
        local_session.close();
        return true;
    }

    @Override
    protected void after() {

    }

}

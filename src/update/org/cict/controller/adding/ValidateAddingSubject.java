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

import app.lazy.models.CurriculumRequisiteLineMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.GradeMapping;
import app.lazy.models.LoadGroupMapping;
import app.lazy.models.LoadSectionMapping;
import app.lazy.models.StudentMapping;
import app.lazy.models.SubjectMapping;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import com.jhmvin.orm.SQL;
import java.util.ArrayList;
import org.cict.PublicConstants;
import org.hibernate.criterion.Order;

/**
 *
 * @author Joemar
 */
public class ValidateAddingSubject extends Transaction {

    public Integer studentCICT_id;
    public Integer subjectID;

    private SubjectMapping subjectToAdd;
    private boolean isEligibleToAdd = false;
    private boolean isPreReqNotAllTaken = false;
    private boolean isAlreadyTaken = true;
    private boolean noSectionOffered = false;
    private ArrayList<Integer> preReqRequiredIds = new ArrayList<>();

    public boolean isEligibleToAdd() {
        return this.isEligibleToAdd;
    }

    public boolean isPreReqNotAllTaken() {
        return this.isPreReqNotAllTaken;
    }

    public boolean isAlreadyTaken() {
        return this.isAlreadyTaken;
    }
    
    public boolean isNoSectionOffered() {
        return noSectionOffered;
    }

    public ArrayList<Integer> getPreReqRequiredIds() {
        return preReqRequiredIds;
    }

    private StudentMapping studentMap;

    private String status;
    public String getStatus() {
        return status;
    }
    
    private String rating;
    public String getRating() {
        return rating;
    }
    
    @Override
    protected boolean transaction() {

        /**
         * Get curriculum from student.
         */
        studentMap = (StudentMapping) Database
                .connect()
                .student()
                .getPrimary(this.studentCICT_id);

        //-
        subjectToAdd = Mono.orm().newSearch(Database.connect().subject())
                .eq(DB.subject().id, this.subjectID)
                .active()
                .first();

        if (subjectToAdd == null) {
            logs("SUBJECT NOT FOUND, NO SUBJECT TO BE VALIDATED");
            return false;
        }

        /**
         * check grade of subject
         */
        logs("CHECK " + subjectToAdd.getCode());
        GradeMapping grade = Mono.orm()
                .newSearch(Database.connect().grade())
                .eq(DB.grade().STUDENT_id, this.studentCICT_id)
                .eq(DB.grade().SUBJECT_id, subjectToAdd.getId())
                .put(PublicConstants.OK_SUBJECT_REMARKS)
                .active(Order.desc(DB.grade().id))
                .first();

        if (grade == null) {
            this.isAlreadyTaken = false;
        } else {
            logs("ALREADY TAKEN, SUBJECT " + subjectToAdd.getCode());
            this.isAlreadyTaken = true;
            if(grade.getRemarks().equalsIgnoreCase("FAILED") || grade.getRemarks().equalsIgnoreCase("INCOMPLETE")) {
                this.status = grade.getRemarks();
                this.rating = grade.getRating();
                return true;
            } else
                return false;
        }

        /**
         * get all the pre req of the subject
         */
        ArrayList<CurriculumRequisiteLineMapping> preRequisiteSubjects = Mono.orm()
                .newSearch(Database.connect().curriculum_requisite_line())
                .eq(DB.curriculum_requisite_line().SUBJECT_id_get, subjectToAdd.getId())
                .put(PublicConstants.getCurriculumRequisite(studentMap))
                .active()
                .all();

        LoadSectionMapping loadSection = null;
        if (preRequisiteSubjects == null) {
            this.isPreReqNotAllTaken = false;
            logs("PRE-REQ ALL TAKEN");
            // no pre requisite
            // get load_section
            loadSection = getLoadSection(subjectToAdd);
            if (loadSection != null) {
                this.isEligibleToAdd = true;
                return true;
            } else {
                this.isEligibleToAdd = false;
                this.noSectionOffered = true;
                logs("NO SECTION FOUND FOR ADDING SUBJECT " + subjectToAdd.getCode());
                return false;
            }
        } else {

            boolean allTaken = true;
            for (CurriculumRequisiteLineMapping prereqCRL : preRequisiteSubjects) {
                logs("PRE_REQ ID: "+prereqCRL.getSUBJECT_id_req());
                GradeMapping gradeOfPreReq = Mono.orm()
                        .newSearch(Database.connect().grade())
                        .eq(DB.grade().STUDENT_id, this.studentCICT_id)
                        .eq(DB.grade().SUBJECT_id, prereqCRL.getSUBJECT_id_req())
                        .put(PublicConstants.OK_SUBJECT_REMARKS)
                        .active(Order.desc(DB.grade().id))
                        .first();
                if (gradeOfPreReq == null) {
                    allTaken = false;
                    logs("ADDED TO PRE-REQ LIST " + prereqCRL.getSUBJECT_id_req());
                    preReqRequiredIds.add(prereqCRL.getSUBJECT_id_req());
                }
            }
            //all taken
            if (allTaken) {
                //get all the pre req ids
                /**
                 * Used the original Requisite Tracker from original package.
                 */
                preReqRequiredIds = org.cict.evaluation.evaluator.RequisitesTracker.viewRequisites(preRequisiteSubjects, studentMap);
                //preReqRequiredIds = RequisitesTracker.viewRequisites(preRequisiteSubjects, studentCICT_id);

                if (preReqRequiredIds.isEmpty()) {

                    this.isPreReqNotAllTaken = false;
                    logs("ALL PRE_REQUISITE/S OF " + subjectToAdd.getCode()
                            + " ARE TAKEN");
                    loadSection = getLoadSection(subjectToAdd);
                    if (loadSection != null) {
                        this.isEligibleToAdd = true;
                        return true;
                    } else {
                        this.isEligibleToAdd = false;
                        this.noSectionOffered = true;
                        logs("NO SECTION FOR ADDING SUBJECT " + subjectToAdd.getCode());
                        return false;
                    }
                } else {
                    logs("(2) NOT ALL PRE-REQUISITE/S ARE TAKEN, SUBJECT " + subjectToAdd.getCode());
                    this.isPreReqNotAllTaken = true;
                    return false;
                }
            } else {
                logs("(1) NOT ALL PRE-REQUISITE/S ARE TAKEN, SUBJECT " + subjectToAdd.getCode());
                this.isPreReqNotAllTaken = true;
                return false;
            }
        }
    }

    @Override
    protected void after() {

    }

    private void logs(String message) {
        if (true) {
            System.out.println("@ValidateAddingChangingSubject: " + message);
        }
    }

    private LoadSectionMapping getLoadSection(SubjectMapping subjectWithNoGrade) {
        ArrayList<LoadGroupMapping> loadGrps = Mono.orm()
                .newSearch(Database.connect().load_group())
                .eq(DB.load_group().SUBJECT_id, subjectWithNoGrade.getId())
                .active()
                .all();

        if (loadGrps == null) {
            return null;
        }

        LoadSectionMapping loadSection = null;
        for (int i = 0; i < loadGrps.size(); i++) {
            /**
             * check open section
             */
            loadSection = Mono.orm().newSearch(Database.connect().load_section())
                    .eq(DB.load_section().id, loadGrps.get(i).getLOADSEC_id())
                    //.eq(DB.load_section().state, "OPEN")
                    .active()
                    .first();
            // return only one instance, any of the sections 
            if (loadSection != null) {
                return loadSection;
            }
        }
        return loadSection;
    }

}

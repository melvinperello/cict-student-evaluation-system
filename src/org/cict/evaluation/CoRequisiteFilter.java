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
package org.cict.evaluation;

import app.lazy.models.CurriculumRequisiteExtMapping;
import app.lazy.models.CurriculumSubjectMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.SubjectMapping;
import com.jhmvin.Mono;
import java.util.ArrayList;
import javafx.scene.layout.VBox;
import org.cict.evaluation.views.SubjectView;
import org.hibernate.criterion.Order;
import update.org.cict.controller.adding.SubjectInformationHolder;

/**
 *
 * @author Jhon Melvin
 */
public class CoRequisiteFilter {

    /**
     * for evaluation.
     *
     * @param subjectContainer the vbox that contains the subject since this
     * does not use simple table.
     * @param studentCurriculumID
     * @return
     */
    public static boolean checkCoReqEval(VBox subjectContainer, int studentCurriculumID) {
        for (Object object : subjectContainer.getChildrenUnmodifiable()) {
            if (!(object instanceof SubjectView)) {
                continue;
            }
            //------------------------------------------------------------------
            SubjectView view = (SubjectView) object;
            //------------------------------------------------------------------
            ArrayList<CurriculumRequisiteExtMapping> corequisites
                    = getCoRequisites(studentCurriculumID, view.subjectID);
            //------------------------------------------------------------------
            if (corequisites == null) {
                // ok no requisites
                continue; // go to next subject view object
            }

            //------------------------------------------------------------------
            // check for coreq of this subject in the view
            int thisIterationSize = corequisites.size();
            int thisIterationGot = 0;
            for (CurriculumRequisiteExtMapping coreq : corequisites) {
                int requiredSubject = coreq.getSUBJECT_id_req();
                //--------------------------------------------------------------
                // iterate all over the subjects
                //--------------------------------------------------------------
                if (checkIfInList(requiredSubject, subjectContainer)) {
                    thisIterationGot++;
                }
                //--------------------------------------------------------------
            }
            //------------------------------------------------------------------
            if (thisIterationSize != thisIterationGot) {
                // some co requisite is not found in the list
                return false;
            }
            //------------------------------------------------------------------

        }

        // all coreq found
        return true;
    }

    /**
     * private method to check if exist in list.
     *
     * @param requiredSubject
     * @param subjectContainer
     * @return
     */
    private static boolean checkIfInList(int requiredSubject, VBox subjectContainer) {
        boolean exist = false;
        for (Object object2 : subjectContainer.getChildrenUnmodifiable()) {
            if (!(object2 instanceof SubjectView)) {
                continue;
            }
            //----------------------------------------------------------
            SubjectView view2 = (SubjectView) object2;
            //----------------------------------------------------------
            if (view2.subjectID.equals(requiredSubject)) {
                // if existing
                // ok 
                exist = true;
                break;
            }
        }

        return exist;
    }

    /**
     * Checks whether there is a corequisite.
     *
     * if returned null pre requisites are complete.
     *
     * @param collection the collection without the removed subject.
     * @param curriculumID curriculum ID of the student.
     * @param studentID cict id
     * @return CurriculumRequisiteExtMapping list of missing required.
     */
    public static ArrayList<CurriculumRequisiteExtMapping> checkCoReqAdd(
            // Parameters
            ArrayList<SubjectMapping> collection,
            Integer curriculumID,
            Integer studentID
    ) {
        // iterate all over the collection
        for (SubjectMapping subInfo : collection) {
            // get info
            SubjectMapping subjectMap = subInfo;
            Integer subjectID = subjectMap.getId();
            //------------------------------------------------------------------
            ArrayList<CurriculumRequisiteExtMapping> corequisites
                    = getCoRequisites(curriculumID, subjectID);
            //------------------------------------------------------------------
            if (corequisites == null) {
                // ok no requisites
                continue; // go to next subject view object
            }
            //------------------------------------------------------------------
            // clone co requisites.
            ArrayList<CurriculumRequisiteExtMapping> cloneReq
                    = new ArrayList<>();
            cloneReq.addAll(corequisites);
            //------------------------------------------------------------------
            // check for coreq of this subject in the view
            int thisIterationSize = corequisites.size();
            int thisIterationGot = 0;
            for (CurriculumRequisiteExtMapping coreq : corequisites) {
                int requiredSubject = coreq.getSUBJECT_id_req();
                //--------------------------------------------------------------
                if (checkIfInListAdding(requiredSubject, collection)) {
                    // if found
                    // remove from clone
                    cloneReq.remove(coreq);
                    // increment
                    thisIterationGot++;
                }
                //--------------------------------------------------------------
            }
            //------------------------------------------------------------------
            if (thisIterationSize != thisIterationGot) {
                // some co requisite is not found in the list
                // if not equal check the remaining entry in clone req
                //--------------------------------------------------------------
                // check missing if taken
                for (CurriculumRequisiteExtMapping ext : cloneReq) {
                    if (isCoTaken(studentID, ext.getSUBJECT_id_req())) {
                        cloneReq.remove(ext);
                    }
                }
                //--------------------------------------------------------------
                if (!cloneReq.isEmpty()) {
                    return cloneReq; // if not empty.
                }
                //--------------------------------------------------------------

            }
            //------------------------------------------------------------------
        } // loop completed
        //----------------------------------------------------------------------
        return null;
    }

    /**
     * check if the corequisite is in the collection of adding.
     *
     * @param requiredSubject
     * @param collection
     * @return
     */
    private static boolean checkIfInListAdding(
            int requiredSubject,
            ArrayList<SubjectMapping> collection
    ) {
        boolean exist = false;
        for (SubjectMapping subInfo : collection) {
            SubjectMapping subjectMap = subInfo;
            int subjectID = subjectMap.getId();
            //----------------------------------------------------------
            if (subjectID == requiredSubject) {
                // if existing
                // ok 
                exist = true;
                break;
            }
        }

        return exist;
    }

    /**
     * Get he co requisites of a particular subject if there is one.
     *
     * @param curriculumID
     * @param subjectID
     * @return
     */
    private static ArrayList<CurriculumRequisiteExtMapping>
            getCoRequisites(Integer curriculumID, Integer subjectID) {
        return Mono.orm()
                .newSearch(Database.connect().curriculum_requisite_ext())
                .eq(DB.curriculum_requisite_ext().CURRICULUM_id, curriculumID)
                .eq(DB.curriculum_requisite_ext().SUBJECT_id_get, subjectID)
                .eq(DB.curriculum_requisite_ext().type, "COREQUISITE")
                .active(Order.desc(DB.curriculum_requisite_ext().id))
                .all();
    }

    /**
     * get the subject information of the subject from the curriculum
     * curriculum.
     *
     * @param curriculumID
     * @param subjectID
     * @return
     */
    @Deprecated
    private static ArrayList<CurriculumSubjectMapping>
            getCoRequisiteInfo(Integer curriculumID, Integer subjectID) {
        return Mono.orm()
                .newSearch(Database.connect().curriculum_subject())
                .eq(DB.curriculum_subject().CURRICULUM_id, curriculumID)
                .eq(DB.curriculum_subject().SUBJECT_id, subjectID)
                .active(Order.desc(DB.curriculum_requisite_ext().id))
                .all();
    }

    /**
     * If the corequisite is not enrolled at the same time check whether it is
     * already taken. passed or failed.
     *
     * @param cictID
     * @param subjectID
     * @return
     */
    private static boolean isCoTaken(Integer cictID, Integer subjectID) {
        Object hasGrade = Mono.orm().newSearch(Database.connect().grade())
                .eq(DB.grade().STUDENT_id, cictID)
                .eq(DB.grade().SUBJECT_id, subjectID)
                .eq(DB.grade().posted, 1)
                .active(Order.desc(DB.grade().id))
                .all();
        if (hasGrade == null) {
            return false;
        }
        return true;
    }

}

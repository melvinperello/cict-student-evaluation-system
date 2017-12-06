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
            ArrayList<CurriculumRequisiteExtMapping> corequisites = Mono.orm().newSearch(Database.connect().curriculum_requisite_ext())
                    .eq(DB.curriculum_requisite_ext().CURRICULUM_id, studentCurriculumID)
                    .eq(DB.curriculum_requisite_ext().SUBJECT_id_get, view.subjectID)
                    .eq(DB.curriculum_requisite_ext().type, "COREQUISITE")
                    .active(Order.desc(DB.curriculum_requisite_ext().id))
                    .all();

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

    //--------------------------------------------------------------------------
    // for adding
    public static boolean checkCoReqAdd(ArrayList<SubjectInformationHolder> collection, int curriculumID) {
        for (SubjectInformationHolder subInfo : collection) {
            SubjectMapping subjectMap = subInfo.getSubjectMap();
            int subjectID = subjectMap.getId();
            //------------------------------------------------------------------
            ArrayList<CurriculumRequisiteExtMapping> corequisites = Mono.orm().newSearch(Database.connect().curriculum_requisite_ext())
                    .eq(DB.curriculum_requisite_ext().CURRICULUM_id, curriculumID)
                    .eq(DB.curriculum_requisite_ext().SUBJECT_id_get, subjectID)
                    .eq(DB.curriculum_requisite_ext().type, "COREQUISITE")
                    .active(Order.desc(DB.curriculum_requisite_ext().id))
                    .all();
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
                if (checkIfInListAdding(requiredSubject, collection)) {
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

        //----------------------------------------------------------------------
        return true;
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
            ArrayList<SubjectInformationHolder> collection
    ) {
        boolean exist = false;
        for (SubjectInformationHolder subInfo : collection) {
            SubjectMapping subjectMap = subInfo.getSubjectMap();
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
}

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
import com.jhmvin.Mono;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import org.cict.PublicConstants;
import org.hibernate.criterion.Order;

/**
 * Extended class for validating subjects.
 *
 * @author Jhon Melvin
 */
public class RequisitesTracker {
    /**
     * Used the original from the evaluation package.
     */
//
//    public static ArrayList<Integer> viewRequisites(ArrayList<CurriculumRequisiteLineMapping> starting_list, Integer cict_id) {
//        ArrayList<Integer> current_list = new ArrayList<>();
//        ArrayList<Integer> next_list = new ArrayList<>();
//        // add primary pre-requisites.
//        for (CurriculumRequisiteLineMapping crm : starting_list) {
//            current_list.add(crm.getSUBJECT_id_req());
//        }
//
//        /**
//         * List subjects that are missing.
//         */
//        ArrayList<Integer> subjectNeeded = new ArrayList<>();
//
//        Iterator<Integer> current_iterator = current_list.iterator();
//        while (current_iterator.hasNext()) {
//            Integer current_iteration = current_iterator.next();
//
//            ArrayList<CurriculumRequisiteLineMapping> a = listRequisites(current_iteration);
//
////            System.out.println("PreRequisites of " + current_iteration);
//            if (a == null) {
////                System.out.println("\t has no pre-requisites");
//            } else {
//                for (CurriculumRequisiteLineMapping crm : a) {
//                    next_list.add(crm.getSUBJECT_id_req());
//                    Integer subject_id = crm.getSUBJECT_id_req();
////                    System.out.println("\t" + subject_id);
//                    // if no grade
//                    if (!checkHasGrade(cict_id, subject_id)) {
//                        subjectNeeded.add(subject_id);
//                    }
//                }
//            }
//
//            if (!current_iterator.hasNext()) {
//                current_list.clear();
//                current_list.addAll(next_list);
//                current_iterator = current_list.iterator();
//                next_list.clear();
//            }
//
//        }
//
//        return subjectNeeded;
//    }
//
//    public static boolean checkHasGrade(Integer cict_id, Integer subject_id) {
//
//        Object preq = Mono.orm().newSearch(Database.connect().grade())
//                .eq("STUDENT_id", cict_id)
//                .eq("SUBJECT_id", subject_id)
//                .put(PublicConstants.OK_SUBJECT_REMARKS)
//                .active(Order.desc("id")).first();
//        if (Objects.isNull(preq)) {
//            // if the pre requisite is not found
//            return false;
//        }
//        return true;
//    }
//
//    public static ArrayList<CurriculumRequisiteLineMapping> listRequisites(Integer id) {
//        ArrayList<CurriculumRequisiteLineMapping> require = Mono.orm()
//                .newSearch(Database.connect().curriculum_requisite_line())
//                .eq(DB.curriculum_requisite_line().SUBJECT_id_get, id)
//                .active()
//                .all();
//
//        return require;
//    }
}

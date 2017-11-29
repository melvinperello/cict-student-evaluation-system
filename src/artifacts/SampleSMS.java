/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artifacts;

import app.lazy.models.AcademicTermMapping;
import app.lazy.models.CurriculumSubjectMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.GradeMapping;
import app.lazy.models.SubjectMapping;
import com.jhmvin.Mono;
import java.text.DecimalFormat;
import java.util.ArrayList;
import org.hibernate.criterion.Order;

/**
 *
 * @author Jhon Melvin
 */
public class SampleSMS {

    public static void main(String... args) {
        ListersChecker checker = new ListersChecker();
        AcademicTermMapping current = Database.connect().academic_term().getPrimary(19);
        checker.setCurrentTerm(current);
        checker.setListerMode(ListersChecker.ListerMode.DEANS_LIST);
        ArrayList<ListersChecker.ListerData> listers = checker.check();
        for (ListersChecker.ListerData lister : listers) {
            
        }
    }

//    public static void main(String[] args) {
//        //Non Blocking
////        SMSWrapper.send("+639368955866", "Hellow World", WordUtils.capitalizeFully(CollegeFaculty.instance().getFirstLastName()), response -> {
////            System.out.println("RESPONSE: " + response);
////        });
////
////        // Blocking When Used in Loops
////        String res = SMSWrapper.send("+639368955866", "Hellow World", WordUtils.capitalizeFully(CollegeFaculty.instance().getFirstLastName()));
////        System.out.println(res);
////
////        System.out.println("Sa Huli");
//        Integer CUR_id = 9;
//        Integer STUDENT_id = 102;
//        Integer STUDENT_yrlvl = 3;
//        if (STUDENT_yrlvl.equals(1)) {
//            // first year students are not yet valid candidate for listers
//            return;
//        }
//
//        ArrayList<CurriculumSubjectMapping> csMaps_phase1 = Mono.orm().newSearch(Database.connect().curriculum_subject())
//                .eq(DB.curriculum_subject().CURRICULUM_id, CUR_id)
//                .eq(DB.curriculum_subject().year, STUDENT_yrlvl - 1)
//                .eq(DB.curriculum_subject().semester, 2).active().all();
//
//        ArrayList<CurriculumSubjectMapping> csMaps_phase2 = Mono.orm().newSearch(Database.connect().curriculum_subject())
//                .eq(DB.curriculum_subject().CURRICULUM_id, CUR_id)
//                .eq(DB.curriculum_subject().year, STUDENT_yrlvl)
//                .eq(DB.curriculum_subject().semester, 1).active().all();
//
//        DecimalFormat df = new DecimalFormat("0.0000");
//        double gwaPhase1 = getGWA(csMaps_phase1, STUDENT_id);
//        System.out.println("GWA1: " + df.format(gwaPhase1));
//        double gwaPhase2 = getGWA(csMaps_phase2, STUDENT_id);
//        System.out.println("GWA2: " + df.format(gwaPhase2));
//        double overallGWA = (gwaPhase1 + gwaPhase2) / 2;
//        System.out.println("OVERALL: " + df.format(overallGWA));
//        if (overallGWA >= 1.20) {
//            System.out.println("PRESIDENT'S LISTER");
//        } else if (overallGWA <= 1.21 && overallGWA >= 1.75) {
//            System.out.println("DEAN'S LISTER");
//        }
//    }
    public static double getGWA(ArrayList<CurriculumSubjectMapping> csMaps, Integer STUDENT_id) {
        double gwa = 0.0;
        if (csMaps != null) {
            double gradeTotal = 0.0;
            double totalUnits = 0.0;
            for (CurriculumSubjectMapping csMap : csMaps) {
                Integer SUBJECT_id = csMap.getSUBJECT_id();
                SubjectMapping subject = Database.connect().subject().getPrimary(SUBJECT_id);
                GradeMapping grade = Mono.orm().newSearch(Database.connect().grade())
                        .eq(DB.grade().STUDENT_id, STUDENT_id)
                        .eq(DB.grade().SUBJECT_id, SUBJECT_id)
                        .ne(DB.grade().grade_state, "CORRECTION").active(Order.desc(DB.grade().id)).first();
                //
                try {
                    if (grade == null) {
                        continue;
                    }
                    double rating = Double.parseDouble(grade.getRating());
                    totalUnits += (subject.getLab_units() + subject.getLec_units());
                    gradeTotal += rating * (subject.getLab_units() + subject.getLec_units());
                } catch (NumberFormatException e) {
                }
            }
            gwa = gradeTotal / totalUnits;
        }
        return gwa;
    }
}

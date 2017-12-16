/**
 * CAPSTONE PROJECT.
 * BSIT 4A-G1.
 * MONOSYNC TECHNOLOGIES.
 * MONOSYNC FRAMEWORK VERSION 1.0.0 TEACUP RICE ROLL.
 * THIS PROJECT IS PROPRIETARY AND CONFIDENTIAL ANY PART OF THEREOF.
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
 * NO COPYRIGHT ARE INTENTIONALLY OR INTENDED.
 * THIS PROJECT IS NOT PROFITABLE HENCE FOR EDUCATIONAL PURPOSES ONLY.
 * THIS PROJECT IS ONLY FOR COMPLIANCE TO OUR REQUIREMENTS.
 * THIS PROJECT DOES NOT INCLUDE DISTRIBUTION FOR OTHER PURPOSES.
 *
 */
package org.cict.evaluation.sectionviewer;

import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import com.jhmvin.orm.SQL;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import app.lazy.models.AcademicTermMapping;
import app.lazy.models.CurriculumRequisiteLineMapping;
import app.lazy.models.CurriculumSubjectMapping;
import app.lazy.models.DB;
import app.lazy.models.GradeMapping;
import app.lazy.models.StudentMapping;
import app.lazy.models.SubjectMapping;
import app.lazy.models.Database;
import com.jhmvin.orm.Searcher;
import org.cict.PublicConstants;
import org.cict.evaluation.views.SubjectSuggestionView;
import org.hibernate.criterion.Order;

/**
 *
 * @author Jhon Melvin
 */
public class SearchSuggestions extends Transaction {

    private void log(Object message) {
        boolean logging = true;
        if (logging) {
            System.out.println("@SearchSuggestions: " + message.toString());
        }
    }

    public String studentNumber;

    /**
     * Search cache values
     */
    private StudentMapping student;
    private AcademicTermMapping acadTerm;
    private ArrayList<CurriculumSubjectMapping> subjects;

    private ArrayList<SubjectMapping> suggestedSubjects;

    @Override
    protected boolean transaction() {
        log("transaction started . . .");
        student = Mono.orm()
                .newSearch(Database.connect().student())
                .eq("id", studentNumber)
                .active()
                .first();

        if (student == null) {
            log("student not found");
            return false;
        }
        log("student found");

        acadTerm = Mono.orm()
                .newSearch(Database.connect().academic_term())
                .eq("current", 1)
                .active()
                .first();

        if (acadTerm == null) {
            log("academic term not found");
            return false;
        }
        log("academic term found");

        int semester = acadTerm.getSemester().equalsIgnoreCase("FIRST SEMESTER") ? 1
                : acadTerm.getSemester().equalsIgnoreCase("SECOND SEMESTER") ? 2
                : 0;

        log("semester " + semester);

        subjects = Mono.orm()
                .newSearch(Database.connect().curriculum_subject())
                .eq("CURRICULUM_id", student.getCURRICULUM_id())
                .eq("semester", semester)
                .active()
                .all();

        if (subjects == null) {
            log("No possible subjects");
            return false;
        }

        /**
         * Check for grades
         */
        ArrayList<SubjectMapping> subjectWithNoGrades = new ArrayList<>();
        for (CurriculumSubjectMapping subject : subjects) {
            SubjectMapping sub = (SubjectMapping) Database.connect().subject().getBy("id", subject.getSUBJECT_id());
            log("Checking subject " + sub.getDescriptive_title());
            // check if taken with passed or inc remarks
            GradeMapping grade = Mono.orm()
                    .newSearch(Database.connect().grade())
                    .eq("SUBJECT_id", sub.getId())
                    .put(SQL
                            .or(SQL.where("remarks").equalTo("PASSED"),
                                    SQL.where("remarks").equalTo("INCOMPLETE")))
                    .active(Order.desc("id"))
                    .first();

            if (grade == null) {
                // no grade
                log("The Subjet has no grade");
                subjectWithNoGrades.add(sub);
            } else {
                log("TAKEN WITH PASSED OR INC");
            }
        }

        // all subjects are taken
        if (subjectWithNoGrades.isEmpty()) {
            return false;
        }

        ArrayList<SubjectMapping> canBeTaken = new ArrayList<>();

        for (SubjectMapping subjectWithNoGrade : subjectWithNoGrades) {
            log("Checking for pre-requisites . . .");
            ArrayList<CurriculumRequisiteLineMapping> preRequisiteID = Mono.orm()
                    .newSearch(Database.connect().curriculum_requisite_line())
                    .eq("SUBJECT_id_get", subjectWithNoGrade.getId())
                    .put(PublicConstants.getCurriculumRequisite(student))
                    .active()
                    .all();
            //------------------------------------------------------------------
            // check if there is a section for this subject
            Searcher search = Mono.orm().newSearch(Database.connect().load_group())
                    .eq(DB.load_group().SUBJECT_id, subjectWithNoGrade.getId())
                    .eq(DB.load_group().active, 1);
            String count = Mono.orm().projection(search).count(DB.load_group().id);

            //------------------------------------------------------------------
            if (count.equals("0")) {
                // do not add no section found
            } else if (preRequisiteID == null) {
                // no pre requisite
                canBeTaken.add(subjectWithNoGrade);
            } else {
                log("Validating Pre-Requisites");
                boolean takenAll = true;
                for (CurriculumRequisiteLineMapping curriculumRequisiteLineMapping : preRequisiteID) {
                    Object preq = Mono.orm().newSearch(Database.connect().grade())
                            .eq("STUDENT_id", student.getCict_id())
                            .eq("SUBJECT_id", curriculumRequisiteLineMapping.getSUBJECT_id_req())
                            .put(SQL
                                    .or(SQL.where("remarks").equalTo("PASSED"),
                                            SQL.where("remarks").equalTo("INCOMPLETE")))
                            .active(Order.desc("id"))
                            .first();

                    if (preq == null) {
                        takenAll = false;
                        break;
                    }
                }

                if (takenAll) {
                    canBeTaken.add(subjectWithNoGrade);
                }

                // with pre requisite
            }
        }

        if (canBeTaken.isEmpty()) {
            return false;
        }
        suggestedSubjects = canBeTaken;
        return true;
    }

    private ObservableList<SubjectSuggestionView> suggestions = FXCollections.observableArrayList();

    public ObservableList<SubjectSuggestionView> getSuggestions() {
        return suggestions;
    }

    @Override
    protected void after() {
        for (SubjectMapping suggestedSubject : suggestedSubjects) {
            SubjectSuggestionView suggest = new SubjectSuggestionView();
            suggest.code.setText(suggestedSubject.getCode());
            suggest.title.setText(suggestedSubject.getDescriptive_title());
            suggestions.add(suggest);
        }
    }

}

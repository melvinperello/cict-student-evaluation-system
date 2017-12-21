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

import app.lazy.models.AcademicProgramMapping;
import app.lazy.models.AcademicTermMapping;
import app.lazy.models.CurriculumMapping;
import app.lazy.models.CurriculumSubjectMapping;
import app.lazy.models.DB;
import static app.lazy.models.DB.curriculum;
import app.lazy.models.Database;
import app.lazy.models.LoadGroupMapping;
import app.lazy.models.LoadSectionMapping;
import app.lazy.models.StudentMapping;
import app.lazy.models.SubjectMapping;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import java.util.ArrayList;
import java.util.HashMap;
import org.cict.authentication.authenticator.SystemProperties;
import org.hibernate.criterion.Order;

/**
 *
 * @author Joemar
 */
public class AddSubjectSuggestion extends Transaction {

//    public String studentNumber;

    public StudentMapping student;
    private AcademicTermMapping acadTerm;
    private ArrayList<CurriculumSubjectMapping> subjects;
    private ArrayList<SubjectInformationHolder> suggestedSubjects = new ArrayList<>();

    @Override
    protected boolean transaction() {
        /**
         * Get student information.
         */
//        student = Mono.orm()
//                .newSearch(Database.connect().student())
//                .eq(DB.student().id, studentNumber)
//                .active()
//                .first();

        /**
         * Check Student.
         */
        if (student == null) {
            log("student not found");
            return false;
        }

        /**
         * Check academic term. where current is 1
         */
        acadTerm = SystemProperties.instance().getCurrentAcademicTerm();/*Mono.orm()
                .newSearch(Database.connect().academic_term())
                .eq(DB.academic_term().current, 1)
                .active()
                .first();*/

        if (acadTerm == null) {
            log("academic term not set");
            return false;
        }

        // get semester integer value
        int semester = acadTerm.getSemester().equalsIgnoreCase("FIRST SEMESTER") ? 1
                : acadTerm.getSemester().equalsIgnoreCase("SECOND SEMESTER") ? 2
                : 0;

        /**
         * Get all subjects from the curriculum offered in a given semester.
         */
        subjects = Mono.orm()
                .newSearch(Database.connect().curriculum_subject())
//                .eq(DB.curriculum_subject().CURRICULUM_id, student.getCURRICULUM_id())
//                .eq(DB.curriculum_subject().semester, semester)
                .active(Order.asc(DB.curriculum_subject().id))
                .all();

        if (subjects == null) {
            log("No possible subjects");
            return false;
        }
        
        ArrayList<Object[]> subjectWithNoGrades = new ArrayList<>();
         for (CurriculumSubjectMapping subject : subjects) {
            SubjectMapping sub = (SubjectMapping) Database.connect().subject().getBy(DB.subject().id, subject.getSUBJECT_id());
            CurriculumMapping curriculum = Database.connect().curriculum().getPrimary(subject.getCURRICULUM_id());
            Object[] obj = new Object[2];
            obj[0] = sub;
            obj[1] = curriculum;
            subjectWithNoGrades.add(obj);
        }

        // all subjects are taken
        if (subjectWithNoGrades.isEmpty()) {
            log("No subject with no grade found");
            return false;
        }
        for (int i=0; i<subjectWithNoGrades.size(); i++) {
            SubjectMapping subjectWithNoGrade = (SubjectMapping) subjectWithNoGrades.get(i)[0];
            CurriculumMapping curriculum = (CurriculumMapping) subjectWithNoGrades.get(i)[1];
            
            LoadSectionMapping loadSection = null;
            loadSection = getLoadSection(subjectWithNoGrade);
            if (loadSection != null) {
                // with section open and active
                log(subjectWithNoGrade.getId() + " : "
                        + subjectWithNoGrade.getCode() + " ADDED TO THE LIST");
                LoadGroupMapping loadGroup = this.getLoadGroupMapping(loadSection.getId(),
                        subjectWithNoGrade.getId());
                addNewSubjectInformationHolder(subjectWithNoGrade, loadSection, loadGroup, curriculum);
            }
        }
        return !this.suggestedSubjects.isEmpty();
    }

    public ArrayList<SubjectInformationHolder> getSubjectSuggestion() {
        return suggestedSubjects;
    }

    @Override
    protected void after() {

    }

    private void log(String message) {
        boolean logging = false;
        if (logging) {
            System.out.println("@AddSubjectSuggestion: " + message);
        }
    }

    private void addNewSubjectInformationHolder(SubjectMapping subject,
            LoadSectionMapping loadSection,
            LoadGroupMapping loadGroup, CurriculumMapping curriculum) {
        SubjectInformationHolder suggested = new SubjectInformationHolder();
        suggested.setSubjectMap(subject);
        suggested.setSectionMap(loadSection);
        suggested.setLoadGroup(loadGroup);

        AcademicProgramMapping acadProg = Mono.orm()
                .newSearch(Database.connect().academic_program())
                /**
                 *
                 * ACADPROG_id will be truncated to subject table.
                 *
                 * @revision 001
                 * @date: 8282017
                 */
                .eq(DB.academic_program().id, loadSection.getACADPROG_id())
                //.eq(DB.academic_program().id, subject.getACADPROG_id())
                .active()
                .first();

        //-----------------------------
        suggested.setCurriculum(curriculum);
        
        suggested.setAcademicProgramMapping(acadProg);
        suggestedSubjects.add(suggested);
    }

    private LoadSectionMapping getLoadSection(SubjectMapping subjectWithNoGrade) {
        ArrayList<LoadGroupMapping> loadGrps = Mono.orm()
                .newSearch(Database.connect().load_group())
                .eq(DB.load_group().SUBJECT_id, subjectWithNoGrade.getId())
                .active()
                .all();

        if(loadGrps == null) {
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

    private LoadGroupMapping getLoadGroupMapping(Integer loadSectionID, Integer subjectID) {
        LoadGroupMapping loadGroup = Mono.orm().newSearch(Database.connect().load_group())
                .eq(DB.load_group().SUBJECT_id, subjectID)
                .eq(DB.load_group().LOADSEC_id, loadSectionID)
                .active()
                .first();
        return loadGroup;
    }

}

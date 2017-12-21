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
import app.lazy.models.CurriculumRequisiteLineMapping;
import app.lazy.models.CurriculumSubjectMapping;
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
import java.util.Objects;
import org.cict.PublicConstants;
import org.cict.authentication.authenticator.SystemProperties;
import org.hibernate.criterion.Order;

/**
 *
 * @author Joemar
 */
public class ChangeSubjectSuggestion extends Transaction {
    
    public Integer studentNumber;
    public SubjectInformationHolder subjectInfoHolder;
    public double totalUnits;
    
    private StudentMapping student;
    private AcademicTermMapping acadTerm;
    private ArrayList<CurriculumSubjectMapping> subjects;
    private ArrayList<SubjectInformationHolder> suggestedSubjects = new ArrayList<>();
    
    @Override
    protected boolean transaction() {
        /**
         * Get student information.
         */
        student = Database.connect().student().getPrimary(studentNumber);/*Mono.orm()
                .newSearch(Database.connect().student())
                .eq(DB.student().cict_id, studentNumber)
                .active()
                .first();*/

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
        int semester = acadTerm.getSemester_regular();

        /**
         * Get all subjects from the curriculum offered in a given semester.
         */
        subjects = Mono.orm()
                .newSearch(Database.connect().curriculum_subject())
                .eq(DB.curriculum_subject().CURRICULUM_id, student.getCURRICULUM_id())
                .eq(DB.curriculum_subject().semester, semester)
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
            SubjectMapping sub = Database.connect().subject().getPrimary(subject.getSUBJECT_id());
            // check if taken with passed or inc remarks
            GradeMapping grade = Mono.orm()
                    .newSearch(Database.connect().grade())
                    .eq(DB.grade().STUDENT_id, student.getCict_id())
                    .eq(DB.grade().SUBJECT_id, sub.getId())
                    .put(SQL
                            .or(SQL.where(DB.grade().remarks).equalTo("PASSED"),
                                    SQL.where(DB.grade().remarks).equalTo("INCOMPLETE")))
                    .active(Order.desc(DB.grade().id))
                    .first();
            
            if (grade == null) {
                // no grade
                double totalSubjectUnits = (sub.getLab_units() + sub.getLec_units());
                if(totalSubjectUnits == totalUnits) {
                    subjectWithNoGrades.add(sub);
                }
            }
        }

        // all subjects are taken
        if (subjectWithNoGrades.isEmpty()) {
            log("No subject with no grade found");
            return false;
        }

        // get the subject to be change
        SubjectMapping subjectToChange = this.subjectInfoHolder.getSubjectMap();
        
        for (SubjectMapping subjectWithNoGrade : subjectWithNoGrades) {
            /**
             * get all the pre req of the subject
             */
            ArrayList<CurriculumRequisiteLineMapping> preRequisiteID = Mono.orm()
                    .newSearch(Database.connect().curriculum_requisite_line())
                    .eq(DB.curriculum_requisite_line().SUBJECT_id_get, subjectWithNoGrade.getId())
                    .put(PublicConstants.getCurriculumRequisite(student))
                    .active()
                    .all();
            
            LoadSectionMapping loadSection = null;
            if (preRequisiteID == null) {
                // no pre requisite
                if (/*(Objects.equals(subjectWithNoGrade.getLab_units(), subjectToChange.getLab_units())
                        && Objects.equals(subjectWithNoGrade.getLec_units(), subjectToChange.getLec_units()))
                        &&*/!Objects.equals(subjectToChange.getId(), subjectWithNoGrade.getId())) {
                    // get load_section
                    loadSection = getLoadSection(subjectWithNoGrade);
                    if (loadSection != null) {
                        // with section open and active
                        System.out.println("1 ADDED " + subjectWithNoGrade.getCode());
                        addNewSubjectInformationHolder(subjectWithNoGrade, loadSection);
                    } else {
                        System.out.println("2 NO SECTION " + subjectWithNoGrade.getCode());
                    }
                }
            } else {
                // with pre req
                boolean takenAll = true;
                for (CurriculumRequisiteLineMapping curriculumRequisiteLineMapping : preRequisiteID) {
                    /**
                     * check the grade of the pre req subject if passed or inc
                     */
                    Object preq = Mono.orm().newSearch(Database.connect().grade())
                            .eq(DB.grade().STUDENT_id, student.getCict_id())
                            .eq(DB.grade().SUBJECT_id, curriculumRequisiteLineMapping.getSUBJECT_id_req())
                            .put(SQL
                                    .or(SQL.where(DB.grade().remarks).equalTo("PASSED"),
                                            SQL.where(DB.grade().remarks).equalTo("INCOMPLETE")))
                            .active(Order.desc(DB.grade().id))
                            .first();
                    
                    if (preq == null) {
                        // no grade found 
                        log("No prereq 1");
                        takenAll = false;
                        break;
                    }
                }
                
                if (takenAll) {
                    if (/*(Objects.equals(subjectWithNoGrade.getLab_units(), subjectToChange.getLab_units())
                            && Objects.equals(subjectWithNoGrade.getLec_units(), subjectToChange.getLec_units()))
                            &&*/!Objects.equals(subjectToChange.getId(), subjectWithNoGrade.getId())) {
                        loadSection = getLoadSection(subjectWithNoGrade);
                        if (loadSection != null) {
                            addNewSubjectInformationHolder(subjectWithNoGrade, loadSection);
                        } else {
                            log("3");
                        }
                    }
                }
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
        boolean logging = true;
        if (logging) {
            System.out.println("@ChangeSubjectSuggestion: " + message);
        }
    }
    
    private void addNewSubjectInformationHolder(SubjectMapping subject,
            LoadSectionMapping loadSection) {
        SubjectInformationHolder suggested = new SubjectInformationHolder();
        suggested.setSubjectMap(subject);
        suggested.setSectionMap(loadSection);
        
        AcademicProgramMapping acadProg = Mono.orm()
                .newSearch(Database.connect().academic_program())
                /**
                 * ACADPROG_id will be truncated to subject table.
                 *
                 * @revision 001
                 * @date: 8282017
                 */
                .eq(DB.academic_program().id, loadSection.getACADPROG_id())
                //.eq(DB.academic_program().id, subject.getACADPROG_id())
                .active()
                .first();
        
        suggested.setAcademicProgramMapping(acadProg);
        suggestedSubjects.add(suggested);
    }
    
    private LoadSectionMapping getLoadSection(SubjectMapping subjectWithNoGrade) {
        ArrayList<LoadGroupMapping> loadGrps = Mono.orm()
                .newSearch(Database.connect().load_group())
                .eq(DB.load_group().SUBJECT_id, subjectWithNoGrade.getId())
                .active().all();
        LoadSectionMapping loadSection = null;
        if(loadGrps != null) {
            for (int i = 0; i < loadGrps.size(); i++) {
                /**
                 * check open section
                 */
                loadSection = Mono.orm().newSearch(Database.connect().load_section())
                        .eq(DB.load_section().id, loadGrps.get(i).getLOADSEC_id())
                        //.eq(DB.load_section().state, "OPEN")
                        .active()
                        .first();
                System.out.println("LOAD SECTION ID " + loadGrps.get(i).getLOADSEC_id());
                // return only one instance, any of the sections 
                if (loadSection != null) {
                    log("2");
                    return loadSection;
                } else {
                    log("1");
                }
            }
        }
        return loadSection;
    }
    
}

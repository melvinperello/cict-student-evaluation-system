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
package org.cict.evaluation.evaluator;

import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import java.util.ArrayList;
import java.util.Objects;
import app.lazy.models.AcademicProgramMapping;
import app.lazy.models.CurriculumMapping;
import app.lazy.models.DB;
import app.lazy.models.EvaluationMapping;
import app.lazy.models.LoadGroupMapping;
import app.lazy.models.LoadSectionMapping;
import app.lazy.models.StudentMapping;
import app.lazy.models.SubjectMapping;
import app.lazy.models.Database;
import app.lazy.models.MapFactory;

/**
 *
 * @author Jhon Melvin
 */
public class SearchStudent extends Transaction {

    public Integer academicTerm = Evaluator.instance().getCurrentAcademicTerm().getId();
    public String studentNumber;

    /**
     * search cache values
     */
    private StudentMapping currentStudent;
    private CurriculumMapping studentCurriculum;
    private AcademicProgramMapping studentProgram;
    private LoadSectionMapping studentSection;
    private ArrayList<LoadGroupMapping> studentLoadGroup;
    private ArrayList<SubjectMapping> studentSubject;
    private boolean isAlreadyEvaluated = false;

    public StudentMapping getCurrentStudent() {
        return currentStudent;
    }

    public CurriculumMapping getStudentCurriculum() {
        return studentCurriculum;
    }

    public AcademicProgramMapping getStudentProgram() {
        return studentProgram;
    }

    public LoadSectionMapping getStudentSection() {
        return studentSection;
    }

    public ArrayList<LoadGroupMapping> getStudentLoadGroup() {
        return studentLoadGroup;
    }

    public ArrayList<SubjectMapping> getStudentSubject() {
        return studentSubject;
    }

    public boolean isAlreadyEvaluated() {
        return isAlreadyEvaluated;
    }
    
    @Override
    protected boolean transaction() {
        currentStudent = (StudentMapping) Mono.orm()
                .newSearch(Database.connect().student())
                .eq("id", studentNumber)
                .active()
                .first();
        
        boolean thereIsNull = false;
        
        if(currentStudent == null) {
            // student not exist;
            return false;
        }
        
        if(currentStudent.getCURRICULUM_id() == null) {
            thereIsNull = true;
        }
        
        if(currentStudent.getYear_level() == null) {
            thereIsNull = true;
        } 
        
        if(currentStudent.getSection() == null) {
            thereIsNull = true;
        }
        
        if(currentStudent.get_group()== null) {
            thereIsNull = true;
        }
        
        if(currentStudent.getAdmission_year().isEmpty() || currentStudent.getAdmission_year().equalsIgnoreCase("NOT_SET")) {
            thereIsNull = true;
        }
        
        if(thereIsNull)
            return false;
        
        
        // if there is a result
        if (!Objects.isNull(currentStudent)) {

            // check if the student is already evaluated
            EvaluationMapping studentEvaluation = Mono.orm()
                    .newSearch(Database.connect().evaluation())
                    .eq("STUDENT_id", currentStudent.getCict_id())
                    .eq("ACADTERM_id", academicTerm)
                    .active()
                    .first();

            if (studentEvaluation != null) {
                // proceed to onCancel

                isAlreadyEvaluated = true;
                return true;
            }

            // get curriculum id
            studentCurriculum = (CurriculumMapping) Mono.orm()
                    .newSearch(Database.connect().curriculum())
                    .eq("id", currentStudent.getCURRICULUM_id())
                    .execute()
                    .first();
            
           

            // prepare the section details
            int year = currentStudent.getYear_level();
            int group = currentStudent.get_group();
            String section = currentStudent.getSection();

            // get academic program
            studentProgram = (AcademicProgramMapping) Mono.orm()
                    .newSearch(Database.connect().academic_program())
                    .eq("id", studentCurriculum.getACADPROG_id())
                    .execute()
                    .first();

            if (Objects.isNull(studentProgram)) {
                // the student has no assigned curriculum
                studentProgram = MapFactory.map().academic_program();
            }

            int acadProg = studentProgram.getId();
            // get section

            studentSection = Mono.orm()
                    .newSearch(Database.connect().load_section())
                    .eq("ACADTERM_id", academicTerm)
                    .eq("ACADPROG_id", acadProg)
                    .eq("section_name", section)
                    .eq("year_level", year)
                    .eq("_group", group)
                    .eq("type", "REGULAR")
                    // revision: added curriculum_id
                    .eq(DB.load_section().CURRICULUM_id, studentCurriculum.getId())
                    .active().first();

            if (!Objects.isNull(studentSection)) {
                // prepare section details
                int section_id = studentSection.getId();
                // get load group // subjects
                studentLoadGroup = Mono.orm().
                        newSearch(Database.connect().load_group())
                        .eq("LOADSEC_id", section_id)
                        .active().<LoadGroupMapping>all();

                // reset subject
                studentSubject = new ArrayList<>();
                // iterate all groups from section
                studentLoadGroup.forEach(load_groups -> {
                    int subject_id = load_groups.getSUBJECT_id();
                    // retrieve each mapping of the subject
                    SubjectMapping subject_mapping = Mono.orm()
                            .newSearch(Database.connect().subject())
                            .eq(DB.subject().id, subject_id)
                            //@removed since primary key was used already
                            //@revision: 001
                            //.eq("ACADPROG_id", acadProg)
                            .active().<SubjectMapping>first();
                    // add every resutl
                    studentSubject.add(subject_mapping);
                });
                // end of worker thread proceed to @preview

            } else {
                // no section found
                // no subject will be added
                return true;

            } // end of if else

        } else {
            // student not exist;
            return false;
        }
        return true;
    }

    @Override
    protected void after() {

    }

}

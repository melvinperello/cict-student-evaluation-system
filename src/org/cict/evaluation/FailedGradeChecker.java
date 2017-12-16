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
package org.cict.evaluation;

import app.lazy.models.AcademicProgramMapping;
import app.lazy.models.AcademicTermMapping;
import app.lazy.models.CurriculumMapping;
import app.lazy.models.CurriculumSubjectMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.GradeMapping;
import app.lazy.models.StudentMapping;
import app.lazy.models.SubjectMapping;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import java.util.ArrayList;
import org.apache.commons.lang3.text.WordUtils;
import org.cict.authentication.authenticator.SystemProperties;
import org.hibernate.criterion.Order;

/**
 *
 * @author Joemar
 */
public class FailedGradeChecker extends Transaction{

    private StudentMapping student;
    private AcademicTermMapping currentAcadTerm;
    private AcademicTermMapping prevAcadTerm;
    private CurriculumMapping curriculum;
    private AcademicProgramMapping acadProgram;

    public void setStudent(StudentMapping student) {
        this.student = student;
    }

    private boolean qualifiedForRetention = false;
    private String log;
    private int failedSubject = 0;

    public String getLog() {
        return log;
    }
    @Override
    protected boolean transaction() {
        if(student == null) {
            log = "No student found.";
            return false;
        }
        
        currentAcadTerm = SystemProperties.instance().getCurrentAcademicTerm();
        if(currentAcadTerm == null) {
            log = "No current academic term found.";
            return false;
        }
        
        Integer curriculumID = student.getCURRICULUM_id();
        if(curriculumID == null) {
            log = "The student have no curriculum yet.";
            return false;
        }
        curriculum = Database.connect().curriculum().getPrimary(curriculumID);
        
        if(student.getYear_level().equals(1) && currentAcadTerm.getSemester_regular().equals(1)) {
            log = "Student must be atleast in second semester to proceed.";
            return false;
        }
        
        int year = 1, semester = 1;
        if(currentAcadTerm.getSemester_regular().equals(1)) {
            year = (student.getYear_level()-1);
            semester = 2;
        } else if(currentAcadTerm.getSemester_regular().equals(2)) {
            year = student.getYear_level();
            semester = 1;
        }
        
        // check if ladderized curriculum
        CurriculumMapping prep_curriculum = null;
        if(curriculum.getLadderization().equalsIgnoreCase("YES")) {
            if(student.getYear_level().equals(3)) {
                if(student.getPREP_id()==null) {
                    log = "Please proceed moving up process first to proceed.";
                    return false;
                }
                prep_curriculum = Database.connect().curriculum().getPrimary(student.getPREP_id());
            }
            if(year == 3) {
                curriculum = prep_curriculum;
            }
        }
        
        acadProgram = Database.connect().academic_program().getPrimary(curriculum.getACADPROG_id());
        
        ArrayList<CurriculumSubjectMapping> subjectsToBeChecked = Mono.orm().newSearch(Database.connect().curriculum_subject())
                .eq(DB.curriculum_subject().CURRICULUM_id, curriculum.getId())
                .eq(DB.curriculum_subject().year, year)
                .eq(DB.curriculum_subject().semester, semester)
                .active(Order.asc(DB.curriculum_subject().id)).all();
        if(subjectsToBeChecked == null) {
            log = "No subject to checked found.";
            return false;
        }
        
        Integer prevAcadTermID = null;
        for(CurriculumSubjectMapping csMap : subjectsToBeChecked) {
            SubjectMapping subject = Database.connect().subject().getPrimary(csMap.getSUBJECT_id());
            System.out.println("SUBJECT " + subject.getCode());
            
            GradeMapping grade = Mono.orm().newSearch(Database.connect().grade())
                    .eq(DB.grade().STUDENT_id, student.getCict_id())
                    .eq(DB.grade().SUBJECT_id, subject.getId())
                    .eq(DB.grade().remarks, "FAILED")
                    .active(Order.desc(DB.grade().id)).first();
            
            if(grade != null) {
                if(semester == 2) {
                    prevAcadTermID = currentAcadTerm.getId();
                }
                System.out.println("IT'S FAILED " + subject.getCode());
                failedSubject++;
            }
        }
        
        if(prevAcadTermID != null) {
            prevAcadTerm = Database.connect().academic_term().getPrimary(prevAcadTermID);
            if(semester == 2) {
               prevAcadTerm.setSemester_regular(1);
            }
        }
        if(failedSubject>=2) {
            log = "PRINTING";
        }
        return true;
    }

    @Override
    protected void after() {
        // print if qualified for retention letter
        if(failedSubject >= 2) {
            System.out.println(student.getFirst_name()+ (student.getMiddle_name()==null? "" : " " + WordUtils.initials(student.getMiddle_name())) + " " + student.getLast_name());
            System.out.println((failedSubject==2)?"Two(2)" : "("+failedSubject+")");
            if(acadProgram != null) {
                System.out.println(acadProgram.getCode() + " " + student.getYear_level() + student.getSection() + (student.get_group()==null? "" : "-G" + student.get_group()));
            }
            if(prevAcadTerm != null) {
                System.out.println(prevAcadTerm.getSchool_year() + " Semester " + (prevAcadTerm.getSemester_regular()==1? "1st" : (prevAcadTerm.getSemester_regular()==2? "2nd" : "Midyear")));
            }
        }
        super.after(); //To change body of generated methods, choose Tools | Templates.
    }
    
}
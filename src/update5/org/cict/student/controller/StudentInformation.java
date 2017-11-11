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
package update5.org.cict.student.controller;

import app.lazy.models.AcademicProgramMapping;
import app.lazy.models.CurriculumMapping;
import app.lazy.models.Database;
import app.lazy.models.StudentMapping;

/**
 *
 * @author Joemar
 */
public class StudentInformation {
    
    private StudentMapping student;
    public StudentMapping getStudentMapping() {
        return student;
    }
    
    public String getFullName(){
        if(student!=null) {
            return student.getLast_name() + ", " + student.getFirst_name() + " " + (student.getMiddle_name()==null? "" : student.getMiddle_name());
        }
        return "NO STUDENT FOUND";
    }
    public StudentInformation(StudentMapping student) {
        this.student = student;
        run();
    }
    
    private AcademicProgramMapping acadProg;
    public AcademicProgramMapping getAcademicProgramMapping() {
        return acadProg;
    }
    
    private CurriculumMapping curriculum;
    public CurriculumMapping getCurriculumMapping() {
        return curriculum;
    }
    
    public String getSection() {
        if(student.getYear_level()==null || student.getSection()==null || student.get_group()==null) {
            return "NONE";
        } else {
            if(acadProg!=null){
                return acadProg.getCode() + " "+ student.getYear_level() + student.getSection() + "-G"+student.get_group(); 
            } else
                return "NONE";
        }
    }
   
    private void run() {
        if(student.getCURRICULUM_id() != null) {
            curriculum = Database.connect().curriculum().getPrimary(student.getCURRICULUM_id());
            acadProg = Database.connect().academic_program().getPrimary(curriculum.getACADPROG_id());
        }
    }
}

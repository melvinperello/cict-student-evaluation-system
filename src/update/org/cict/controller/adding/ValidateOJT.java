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
package update.org.cict.controller.adding;

import app.lazy.models.StudentMapping;
import app.lazy.models.SubjectMapping;
import java.util.ArrayList;
import org.cict.SubjectClassification;
import org.cict.evaluation.assessment.AssessmentResults;
import org.cict.evaluation.assessment.CurricularLevelAssesor;
import org.cict.evaluation.assessment.SubjectAssessmentDetials;

/**
 *
 * @author Joemar
 */
public class ValidateOJT {
    
    private static StudentMapping currentStudent;
    public ValidateOJT() {
        
    }
    
    public static boolean isValidForOJT(StudentMapping student) {
        currentStudent = student;
        if(student != null) {
            CurricularLevelAssesor assessor = new CurricularLevelAssesor(currentStudent);
            assessor.assess();
            for (int yrCtr = 1; yrCtr <= currentStudent.getYear_level(); yrCtr++) {
                AssessmentResults annualAsses = assessor.getAnnualAssessment(yrCtr);
                ArrayList<SubjectAssessmentDetials> temp_array = annualAsses.getUnacquiredSubjects();
                if(temp_array != null) {
                    int count = 0;
                    for(SubjectAssessmentDetials temp_value: temp_array) {
                        SubjectMapping subject = temp_value.getSubjectDetails();
                        if(SubjectClassification.isMajor(subject.getType())) {
                            if(!subject.getType().equalsIgnoreCase(SubjectClassification.TYPE_INTERNSHIP)) {
                                System.out.println("A MAJOR SUBJECT HAS NO GRADE");
                                return false;
                            }
                        } else {
                            if(count>1) {
                                System.out.println("MISSING GRADE EXCEED TO 1");
                                return false;
                            } else {
                                if(subject.getType().equalsIgnoreCase(SubjectClassification.TYPE_ELECTIVE)
                                        || subject.getType().equalsIgnoreCase(SubjectClassification.TYPE_MINOR))
                                    count++;
                                else {
                                    System.out.println("MISSING GRADE OF A NONE ELECTIVE OR MINOR TYPE");
                                    return false;
                                }
                            }
                        }
                    }
                } else
                    System.out.println("It's NULL");
            }
            return true;
        } else {
            System.out.println("STUDENT IS NULL");
            return false;
        }
    }
    
}

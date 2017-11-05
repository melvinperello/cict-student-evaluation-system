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

import app.lazy.models.CurriculumMapping;
import app.lazy.models.CurriculumSubjectMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.StudentMapping;
import app.lazy.models.SubjectMapping;
import com.jhmvin.Mono;
import java.util.ArrayList;
import org.cict.SubjectClassification;
import org.cict.authentication.authenticator.SystemProperties;
import org.cict.evaluation.assessment.AssessmentResults;
import org.cict.evaluation.assessment.CurricularLevelAssesor;
import org.cict.evaluation.assessment.SubjectAssessmentDetials;

/**
 *
 * @author Joemar
 */
public class ValidateOJT {

    /**
     * static variables are stateless they do not need to be instantiated its
     * value will remain on runtime. values will be release until restart of
     * program. can be used as A Global Variable if set to public. Class
     * Variables on the other hand are variables bound by the instance of the
     * class. if Object A has a variable aVariable the variable will be release
     * until new A() was called.
     */
    private static StudentMapping currentStudent;

    /**
     * Static classes do not need constructors they are not constructed anyway.
     * they have no instance. we can refer to instance as the soul and class is
     * the body. A body can exist without a soul but a soul can not exist if not
     * born with a body. instantiation is like birth you give the body as the
     * class and its instance using the 'new' keyword as the soul.
     */
    public ValidateOJT() {

    }

    private static ValidateOJT classInstance;

    public static boolean isValidForOJT(StudentMapping currentStudent) {
        /**
         * You cannot instantiate an inner-class without the instance of the
         * parent class.
         */
        if (classInstance == null) {
            classInstance = new ValidateOJT();
        }
        InternshipValidation iv = classInstance.new InternshipValidation();
        iv.currentStudent = currentStudent;
        return iv.check();
    }

    /**
     * This is an inner-class the body can have living organisms in it like
     * bacteria or tape worms or anything else. you can think inner-classes in
     * that way a Class is the human being and it's inner-classes are living
     * organisms inside it. using static methods of classes and inner classes
     * are great combination for programming it will be a combination of
     * state-full (with instance) and state-less (without instance), this will
     * increase flexibility of code usages. just imagine having a function
     * inside a function inside a function etc.
     */
    private class InternshipValidation {

        public StudentMapping currentStudent;

        private Integer yearOfOJT, semesterOfOJT;

        /**
         * This method can be dismantle to different pieces on future updates.
         *
         * @return
         */
        public boolean check() {
            //------------------------------------------------------------------
            // find year level and semester
            run();
            //------------------------------------------------------------------
            if (currentStudent == null) {
                System.out.println("STUDENT IS NULL");
                return false;
            }
            //------------------------------------------------------------------
            if (yearOfOJT > currentStudent.getYear_level()) {
                System.out.println("YEAR LEVEL IS NOT VALID FOR OJT");
                return false;
            }
            //------------------------------------------------------------------
            // run assessor.
            CurricularLevelAssesor assessor = new CurricularLevelAssesor(currentStudent);
            assessor.assess();
            //------------------------------------------------------------------
            Integer studentYearLevel = currentStudent.getYear_level();
            //------------------------------------------------------------------
            for (int yrCtr = 1; yrCtr <= studentYearLevel; yrCtr++) {
                AssessmentResults annualAsses = assessor.getAnnualAssessment(yrCtr);
                ArrayList<SubjectAssessmentDetials> temp_array = annualAsses.getUnacquiredSubjects();
                if (temp_array == null) {
                    // if no un acquired subjects during this year continue
                    continue;
                }
                //--------------------------------------------------------------
                int count = 0; // Defficiency Count this year. (Annually)
                Integer currentSemester = SystemProperties.instance().getCurrentAcademicTerm().getSemester_regular();
                //--------------------------------------------------------------
                // Get Subject details
                inner: // loop marker inner
                for (SubjectAssessmentDetials temp_value : temp_array) {
                    SubjectMapping subject = temp_value.getSubjectDetails();
                    boolean valid = false;
                    //----------------------------------------------------------
                    if (temp_value.getYearLevel() > studentYearLevel) {
                        System.out.println("Subject " + subject.getCode() + " is not checked with as year of " + temp_value.getYearLevel()
                                + " with a semester of " + temp_value.getSemester());
                    } else if (studentYearLevel > temp_value.getYearLevel()) {
                        valid = true;
                    } else {
                        //------------------------------------------------------
                        if (currentSemester.equals(2)) {
                            if (temp_value.getSemester() < currentSemester) {
                                valid = true;
                            }
                        }
                        //------------------------------------------------------
                    }
                    //----------------------------------------------------------
                    if (!valid) {
                        continue inner;
                    }
                    //----------------------------------------------------------
                    if (SubjectClassification.isMajor(subject.getType())) {
                        // if the missing subject is major invalid
                        if (!subject.getType().equalsIgnoreCase(SubjectClassification.TYPE_INTERNSHIP)) {
                            System.out.println("A MAJOR SUBJECT HAS NO GRADE");
                            return false;
                        }
                    } else {
                        //------------------------------------------------------
                        if (count > 1) {
                            // if missing minor subjects are greater than 1
                            System.out.println("MISSING GRADE EXCEED TO 1");
                            return false;
                        } else {
                            // if less than 1 test the following
                            if (subject.getType().equalsIgnoreCase(SubjectClassification.TYPE_ELECTIVE)
                                    || subject.getType().equalsIgnoreCase(SubjectClassification.TYPE_MINOR)) {
                                count++;
                            } else {
                                // if the subject is non elective or major invalid
                                System.out.println("MISSING GRADE OF A NONE ELECTIVE OR MINOR TYPE");
                                return false;
                            }
                        }
                    }
                    //----------------------------------------------------------
                }
            }
            return true; // default return.
        }

        /**
         * Find year and semester of an Internship subject.
         */
        private void run() {
            ArrayList<CurriculumSubjectMapping> csMaps = Mono.orm().newSearch(Database.connect().curriculum_subject())
                    .eq(DB.curriculum_subject().CURRICULUM_id, currentStudent.getCURRICULUM_id())
                    .active().all();
            for (CurriculumSubjectMapping csMap : csMaps) {
                SubjectMapping subject = Mono.orm().newSearch(Database.connect().subject())
                        .eq(DB.subject().id, csMap.getSUBJECT_id())
                        .active().first();

                if (subject == null) {
                    System.out.println("@ValidateOJT: SUBJECT ID" + csMap.getSUBJECT_id() + " NOT FOUND");
                    return;
                }

                if (subject.getType().equalsIgnoreCase(SubjectClassification.TYPE_INTERNSHIP)) {
                    yearOfOJT = csMap.getYear();
                    semesterOfOJT = csMap.getSemester();
                    break;
                }
            }
            System.out.println("YEAR: " + yearOfOJT + " SEMESTER: " + semesterOfOJT);
        }
    }

}

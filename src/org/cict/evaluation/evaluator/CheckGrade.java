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
package org.cict.evaluation.evaluator;

import app.lazy.models.AcademicTermMapping;
import app.lazy.models.CurriculumSubjectMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.GradeMapping;
import app.lazy.models.SubjectMapping;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Joemar
 */
public class CheckGrade extends Transaction{

    public Integer studentId;
    public Integer studentYearLevel;
    public Integer curriculumId;
    
    public String RATING_TO_CHECK = "CANCELLED";
//    private AcademicTermMapping ACADEMIC_TERM;
    private GradeMapping CURRENT_GRADE;
    private ArrayList<CurriculumSubjectMapping> SUBJECTS_WITH_NO_GRADE;
    private ArrayList<ArrayList<SubjectMapping>> FILTERED_SUBJECTS;
    private ArrayList<String> TITLES;
    
    private void log(String message){
        boolean logging = false;
        if(logging)
            System.out.println("@CheckGrade: " + message);
    }
    
    public ArrayList<String> getTitles() {
        return this.TITLES;
    }
    
    public ArrayList<ArrayList<SubjectMapping>> getSubjectsWithNoGrade(){
        return this.FILTERED_SUBJECTS;
    }
    
    @Override
    protected boolean transaction() {
        // get the current academic program map
//        this.ACADEMIC_TERM = Mono.orm()
//                .newSearch(Database.connect().academic_term())
//                .eq(DB.academic_term().id, Evaluator.instance().getCurrentAcademicTerm().getId())
//                .active()
//                .first();
        int flag = 1;
        int year = this.studentYearLevel;
        int semester = Evaluator.instance().getCurrentAcademicTerm().getSemester_regular();
        this.SUBJECTS_WITH_NO_GRADE = new ArrayList<>();
        while(flag != 0){
            if(semester == 1){
                if(year != 0)
                    year -= 1;
                semester = 2;
                this.validateSubjectsOf(year, semester);
            } else if(semester == 2){
                semester = 1;
                this.validateSubjectsOf(year, semester);
            }
            if(year == 0){
                flag = 0;
                log("FLAG 0");
            }
        }
        this.filterSubjects();
        return true;
    }

    @Override
    protected void after() {
    
    }
    
    private void validateSubjectsOf(int year, int semester){
        //get list of subjects of the student's curriculum
        List studentSubjects = Mono.orm()
                .newSearch(Database.connect().curriculum_subject())
                .eq(DB.curriculum_subject().CURRICULUM_id, this.curriculumId)
                .eq(DB.curriculum_subject().year, year)
                .eq(DB.curriculum_subject().semester, semester)
                .active();
        for (Object studentSubject : studentSubjects) {
            CurriculumSubjectMapping currentSubject = (CurriculumSubjectMapping) studentSubject;
            this.CURRENT_GRADE = Mono.orm().newSearch(Database.connect().grade())
                    .eq(DB.grade().STUDENT_id, this.studentId)
                    .eq(DB.grade().SUBJECT_id, currentSubject.getSUBJECT_id())
                    .active()
                    .first();
            if(this.CURRENT_GRADE == null || this.CURRENT_GRADE.getRating().equalsIgnoreCase(RATING_TO_CHECK)){
                this.SUBJECTS_WITH_NO_GRADE.add(currentSubject);
            }
        }
     }
      
    private void filterSubjects(){
        try{
            // filter the subjects from subjectsWithNoGrade
            // count must be from last index
            CurriculumSubjectMapping subjectReference = this.SUBJECTS_WITH_NO_GRADE.get(0);
            Integer lastYearLevelTobeChecked = subjectReference.getYear();
            Integer lastSemesterToBeChecked = subjectReference.getSemester();

            Integer counterForYearLevel = 1;
            Integer counterForSemester = 1;
            this.FILTERED_SUBJECTS = new ArrayList<>();
            ArrayList<SubjectMapping> subjectsPerSemester;
            this.TITLES = new ArrayList<>();
            int flag = 1;
            do{
                if(Objects.equals(counterForYearLevel, lastYearLevelTobeChecked) && 
                    Objects.equals(counterForSemester, lastSemesterToBeChecked))
                    flag = 0;
                subjectsPerSemester = new ArrayList<>();
                for (int i = this.SUBJECTS_WITH_NO_GRADE.size()-1; i >= 0; i--) {
                    CurriculumSubjectMapping currentCurriculumSubject = this.SUBJECTS_WITH_NO_GRADE.get(i);
                    if(Objects.equals(counterForYearLevel, currentCurriculumSubject.getYear()) && 
                            Objects.equals(counterForSemester, currentCurriculumSubject.getSemester())){
                        SubjectMapping subject = Mono.orm()
                                .newSearch(Database.connect().subject())
                                .eq(DB.subject().id, currentCurriculumSubject.getSUBJECT_id())
                                .active()
                                .first();
                        String yearSem = this.formatYearAndSemester(
                                currentCurriculumSubject.getYear(),
                                counterForSemester);
                        if(!this.TITLES.contains(yearSem)){
                            this.TITLES.add(yearSem);
                            log(yearSem);
                        }
                        subjectsPerSemester.add(subject);
                    }
                }
                if(0 != subjectsPerSemester.size())
                    this.FILTERED_SUBJECTS.add(subjectsPerSemester);
                if(counterForSemester.equals(1)){
                    counterForSemester = 2;
                }
                else if(counterForSemester.equals(2)){
                    counterForYearLevel += 1;
                    counterForSemester = 1;
                }
            }while(flag != 0);
            log("NUMBER OF SEMESTER WITH NO GRADE: " + this.FILTERED_SUBJECTS.size());
        } catch(IndexOutOfBoundsException a){
            log("NO SUBJECT FOUND WITH NO GRADE");
        }
    }
    
    private String formatYearAndSemester(Integer year, Integer semester){
        String formatted_string = "";
        if(null != year)
            switch (year) {
            case 1:
                formatted_string = "First Year - ";
                break;
            case 2:
                formatted_string = "Second Year - ";
                break;
            case 3:
                formatted_string = "Third Year - ";
                break;
            case 4:
                formatted_string = "Fourth Year - ";
                break;
            default:
                break;
        }
        if(semester.equals(1))
            formatted_string += "First Semester";
        else if(semester.equals(2))
            formatted_string += "Second Semester";
        return formatted_string;
    }
}

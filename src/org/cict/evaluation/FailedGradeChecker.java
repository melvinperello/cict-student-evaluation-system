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
import app.lazy.models.RetentionPolicyMapping;
import app.lazy.models.RetentionSubjectMapping;
import app.lazy.models.StudentMapping;
import app.lazy.models.SubjectMapping;
import com.itextpdf.text.Document;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import java.util.ArrayList;
import org.apache.commons.lang3.text.WordUtils;
import org.cict.PublicConstants;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.cict.authentication.authenticator.SystemProperties;
import org.cict.reports.ReportsDirectory;
import org.cict.reports.retention.RetentionLetter;
import org.hibernate.criterion.Order;

/**
 *
 * @author Joemar
 */
public class FailedGradeChecker extends Transaction{

    private StudentMapping student;
    private AcademicTermMapping currentAcadTerm;
    private AcademicTermMapping prevAcadTerm = new AcademicTermMapping();
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
    
    private Integer year = 1, semester = 1;
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
            if(year.equals(3)) {
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
                prevAcadTermID = grade.getACADTERM_id();
                System.out.println("IT'S FAILED " + subject.getCode());
                failedSubject++;
                failedSubject_lst.add(subject);
            }
        }
        if(currentAcadTerm.getSemester_regular().equals(2)) {
            prevAcadTermID = currentAcadTerm.getId();
        }
        
        if(prevAcadTermID != null) {
            prevAcadTerm = Database.connect().academic_term().getPrimary(prevAcadTermID);
            if(currentAcadTerm.getSemester_regular().equals(2)) {
               prevAcadTerm.setSemester_regular(1);
               prevAcadTerm.setSemester("FIRST SEMESTER");
            }
        } else {
            if(currentAcadTerm.getSemester_regular().equals(1)) {
               prevAcadTerm.setSemester_regular(2);
               prevAcadTerm.setSemester("SECOND SEMESTER");
               prevAcadTerm.setSchool_year("");
            }
        }
        if(failedSubject>=2) {
            log = "PRINTING";
        }
        return true;
    }

    public static final String[] numNames = {
        "",
        " One",
        " Two",
        " Three",
        " Four",
        " Five",
        " Six",
        " Seven",
        " Eight",
        " Nine",
        " Ten",
        " Eleven",
        " Twelve",
        " Thirteen",
        " Fourteen",
        " Fifteen"};
    
    private ArrayList<SubjectMapping> failedSubject_lst = new ArrayList<>();
    @Override
    protected void after() {
        // print if qualified for retention letter
        if(failedSubject >= 2) {
            String name = student.getFirst_name()+ (student.getMiddle_name()==null? "" : " " + WordUtils.initials(student.getMiddle_name())) + " " + student.getLast_name();
            String section = "";
            String failed = numNames[failedSubject] + " ("+failedSubject+")";
            
            if(acadProgram != null) {
                section = acadProgram.getCode() + " " + student.getYear_level() + student.getSection() + (student.get_group()==null? "" : "-G" + student.get_group());
            }
            if(prevAcadTerm != null) {
                System.out.println(prevAcadTerm.getSchool_year() + " Semester " + (prevAcadTerm.getSemester_regular()==1? "1st" : (prevAcadTerm.getSemester_regular()==2? "2nd" : "Midyear")));
            }
            
            RetentionPolicyMapping existing = Mono.orm().newSearch(Database.connect().retention_policy())
                    .eq(DB.retention_policy().CURRICULUM_id, curriculum.getId())
                    .eq(DB.retention_policy().STUDENT_id, this.student.getCict_id())
                    .eq(DB.retention_policy().academic_semester, currentAcadTerm.getSemester())
                    .eq(DB.retention_policy().academic_year, currentAcadTerm.getSchool_year())
                    .eq(DB.retention_policy().section, section)
                    .eq(DB.retention_policy().semester, prevAcadTerm.getSemester())
                    .eq(DB.retention_policy().year_level, this.getYearLevel(year))
                    .active(Order.desc(DB.retention_policy().id)).first();
            if(existing == null) {
                // create a retentionPolicyMap for the record of the student
                RetentionPolicyMapping retentionPolicy = new RetentionPolicyMapping();
                retentionPolicy.setActive(1);
                retentionPolicy.setAcademic_year(currentAcadTerm.getSchool_year());
                retentionPolicy.setAcademic_semester(currentAcadTerm.getSemester());
                retentionPolicy.setCURRICULUM_id(curriculum.getId());
                retentionPolicy.setSTUDENT_id(this.student.getCict_id());
                retentionPolicy.setSection(section);
                retentionPolicy.setSemester(prevAcadTerm.getSemester());
                retentionPolicy.setVerified_by(CollegeFaculty.instance().getFACULTY_ID());
                retentionPolicy.setYear_level(this.getYearLevel(year));
                int retentionPolicy_id = Database.connect().retention_policy().insert(retentionPolicy);
                if(retentionPolicy_id != -1) {
                    for(SubjectMapping each : failedSubject_lst) {
                        RetentionSubjectMapping retentionSubject = new RetentionSubjectMapping();
                        retentionSubject.setActive(1);
                        retentionSubject.setRetention_id(retentionPolicy_id);
                        retentionSubject.setSubject_code(each.getCode());
                        retentionSubject.setSubject_title(each.getDescriptive_title());
                        retentionSubject.setUnits((each.getLab_units() + each.getLec_units()) + "");
                        int res = Database.connect().retention_subject().insert(retentionSubject);
                    }
                } else {
                    System.out.println("RETENTION POLICY & SUBJECT NOT SAVED");
                }
            }
            
            //print letter
            this.printeLetter(name, section, failed);
        }
        super.after(); //To change body of generated methods, choose Tools | Templates.
    }
    
    public final static String SAVE_DIRECTORY = "reports/retention";
    private void printeLetter(String name, String section, String failedGrades) {
        String doc = "Retention Letter For " + student.getId() + "_" + Mono.orm().getServerTime().getCalendar().getTimeInMillis();

        String RESULT = SAVE_DIRECTORY + "/" + doc + ".pdf";

        //------------------------------------------------------------------------------
        //------------------------------------------------------------------------------
        /**
         * Check if the report save directory is already existing and created if
         * not this will try to create the needed directories.
         */
        boolean isCreated = ReportsDirectory.check(SAVE_DIRECTORY);

        if (!isCreated) {
            // some error message that the directory is not created
            System.err.println("Directory is not created.");
            return;
        }
        //------------------------------------------------------------------
        //------------------------------------------------------------------
        RetentionLetter retention = new RetentionLetter(RESULT);
        retention.STUDENT_NAME = name;
        retention.STUDENT_SECTION = section;
        retention.DEAN = PublicConstants.getSystemVar_Noted_By().toString();
        retention.SERVER_DATE = Mono.orm().getServerTime().getDateWithFormat();
        retention.NUMBER_OF_FAILED_SUBJECTS = failedGrades;
        retention.PREV_SCHOOL_YEAR = "";
        retention.PREV_SEMESTER = "";
        if(prevAcadTerm != null) {
            retention.PREV_SCHOOL_YEAR = prevAcadTerm.getSchool_year();
            retention.PREV_SEMESTER = (prevAcadTerm.getSemester_regular()==1? "1st" : (prevAcadTerm.getSemester_regular()==2? "2nd" : "Midyear"));
        }
        String localReg1 = PublicConstants.getSystemVar_LocalRegistrar1().toString();
        String localReg2 = PublicConstants.getSystemVar_LocalRegistrar2().toString();
        if(!localReg1.isEmpty() && !localReg2.isEmpty()) {
            retention.SENDER_NAMES = new String[] {localReg1, localReg2};
        } else if(!localReg1.isEmpty()) {
            retention.SENDER_NAMES = new String[] {localReg1};
        } else if(!localReg2.isEmpty()) {
            retention.SENDER_NAMES = new String[] {localReg2};
        } else {
            retention.SENDER_NAMES = new String[] {};
        }
        retention.setDocumentFormat(document);
        retention.print();
    }
    
    private Document document;
    public void setDocument(Document parentStage) {
        this.document = parentStage;
    }
    
    private String getYearLevel(Integer yrlvl) {
        switch(yrlvl) {
            case 1:
                return "FIRST YEAR";
            case 2:
                return "SECOND YEAR";
            case 3:
                return "THIRD YEAR";
            case 4:
                return "FOURTH YEAR";
        }
        return null;
    }
}

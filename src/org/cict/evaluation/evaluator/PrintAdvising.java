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
package org.cict.evaluation.evaluator;

import app.lazy.models.AcademicProgramMapping;
import app.lazy.models.AcademicTermMapping;
import app.lazy.models.CurriculumMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.EvaluationMapping;
import app.lazy.models.FacultyMapping;
import app.lazy.models.LoadGroupMapping;
import app.lazy.models.LoadSectionMapping;
import app.lazy.models.LoadSubjectMapping;
import app.lazy.models.PrintLogsMapping;
import app.lazy.models.StudentMapping;
import app.lazy.models.SubjectMapping;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import org.cict.authentication.authenticator.SystemProperties;
import org.cict.reports.ReportsUtility;
import org.cict.reports.advisingslip.AdvisingSlip;
import org.cict.reports.advisingslip.AdvisingSlipData;

/**
 *
 * @author Jhon Melvin
 */
public class PrintAdvising extends Transaction {

    /**
     * static values.
     */
    public final static String OLD = "Old";
    public final static String NEW = "New";
    public final static String REGULAR = "Regular";
    public final static String IRREGULAR = "Irregular";

    public String studentNumber, type; // 2014113844
    public Integer academicTerm;
    public boolean secondCopy = false;

    /**
     * Search cache
     */
    private EvaluationMapping evaluation;
    private FacultyMapping evaluator;
    private StudentMapping student;
    private ArrayList<LoadSubjectMapping> evaluatedSubjects;
    private AcademicTermMapping term;
    private AcademicProgramMapping course;
    private ArrayList<JoinTables> subjectInformation;
    private String major;

    class JoinTables {

        public SubjectMapping info;
        public LoadSectionMapping section;

    }

    @Override
    protected boolean transaction() {

        term = Mono.orm()
                .newSearch(Database.connect().academic_term())
                .eq(DB.academic_term().id, academicTerm)
                .execute()
                .first();

        student = Mono.orm()
                .newSearch(Database.connect().student())
                .eq(DB.student().id, studentNumber)
                .execute()
                .first();
        if(student==null) {
            System.out.println(this.getClass().getSimpleName() + ": NO STUDENT FOUND");
            return false;
        }
        
        CurriculumMapping curriculum = Mono.orm()
                .newSearch(Database.connect().curriculum())
                .eq(DB.curriculum().id, student.getCURRICULUM_id())
                .execute()
                .first();
        if(curriculum==null) {
            System.out.println(this.getClass().getSimpleName() + ": NO CURRICULUM FOUND");
        } else {
            if (curriculum.getMajor() != null) {
                if (!curriculum.getMajor().isEmpty()) {
                    if (!curriculum.getMajor().equalsIgnoreCase("none")) {
                        major = curriculum.getMajor();
                    }
                }
            }

            course = Mono.orm().newSearch(Database.connect().academic_program())
                    .eq(DB.academic_program().id, curriculum.getACADPROG_id())
                    .execute()
                    .first();
        }
        
        evaluation = Mono.orm()
                .newSearch(Database.connect().evaluation())
                .eq(DB.evaluation().STUDENT_id, student.getCict_id())
                .eq(DB.evaluation().ACADTERM_id, academicTerm)
                .active()
                .first();

        /**
         * If the student had undergone adding and changing. print the original
         * evaluation.
         */
        if (evaluation.getAdding_reference_id() != null) {
            EvaluationMapping original = Database
                    .connect()
                    .evaluation()
                    .getPrimary(evaluation.getAdding_reference_id());

            evaluation = original;
        }

        evaluator = Mono.orm()
                .newSearch(Database.connect().faculty())
                .eq(DB.faculty().id, evaluation.getFACULTY_id())
                .execute()
                .first();

        evaluatedSubjects = Mono.orm()
                .newSearch(Database.connect().load_subject())
                .eq(DB.load_subject().EVALUATION_id, evaluation.getId())
                .eq(DB.load_subject().STUDENT_id, student.getCict_id())
                //                .eq("remarks", "ACCEPTED")
                .execute()
                .all();
        if(evaluatedSubjects==null) {
            System.out.println(this.getClass().getSimpleName() + ": NO SUBJECT EVALUATED FOUND");
            return false;
        }

        subjectInformation = new ArrayList<>();

        
        for (LoadSubjectMapping evaluatedSubject : evaluatedSubjects) {
            SubjectMapping subject = Mono.orm()
                    .newSearch(Database.connect().subject())
                    .eq(DB.subject().id, evaluatedSubject.getSUBJECT_id())
                    .execute()
                    .first();

            LoadGroupMapping load_group = Mono.orm()
                    .newSearch(Database.connect().load_group())
                    .eq(DB.load_group().id, evaluatedSubject.getLOADGRP_id())
                    .execute()
                    .first();

            LoadSectionMapping load_section = Mono.orm()
                    .newSearch(Database.connect().load_section())
                    .eq(DB.load_section().id, load_group.getLOADSEC_id())
                    .execute()
                    .first();

            JoinTables tableJoin = new JoinTables();
            tableJoin.info = subject;
            tableJoin.section = load_section;

            subjectInformation.add(tableJoin);
        }

        return true;
    }

    @Override
    protected void after() {

        //SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
        SimpleDateFormat date_format = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
        /**
         * Preserve the date of the evaluation.
         */
//        String date_now = date_format.format(Mono.orm().getServerTime().getDateWithFormat());
        String date_now = date_format.format(this.evaluation.getEvaluation_date());
        AdvisingSlip slip = new AdvisingSlip(studentNumber + "_"
                + Mono.orm()
                        .getServerTime()
                        .getCalendar()
                        .getTimeInMillis());
        slip.INFO_DATE = date_now;
        slip.INFO_ACAD_YEAR = term.getSchool_year();
        slip.INFO_TERM = term.getSemester_regular().toString();
        slip.INFO_CAMPUS = (student.getCampus()==null? "MAIN" : student.getCampus());

        /**
         * Code added 8/2/17 ********************
         *
         */
//        Integer admission_yr = Integer.valueOf(student.getAdmission_year());
//        Integer current_yr = Integer.valueOf(term.getSchool_year().split("-")[0]);
//        if(current_yr>admission_yr)
//            slip.INFO_OLD = true;
//        else 
//            slip.INFO_NEW = true;
//        String enrollment_type = student.getEnrollment_type();
//        if(enrollment_type.equalsIgnoreCase("regular"))
//            slip.INFO_REGULAR = true;
//        else
//            slip.INFO_IRREGULAR = true;
        // MODIFIED: 9/5/17
        // by: Joemar
        if (type.equalsIgnoreCase(OLD)) {
            slip.INFO_OLD = true;
        } else if (type.equalsIgnoreCase(NEW)) {
            slip.INFO_NEW = true;
        } else if (type.equalsIgnoreCase(REGULAR)) {
            slip.INFO_REGULAR = true;
        } else if (type.equalsIgnoreCase(IRREGULAR)) {
            slip.INFO_IRREGULAR = true;
        }
        /**
         * END************************************
         */

        // student
        slip.INFO_STUD_NUM = student.getId();
        slip.INFO_STUD_NAME = (student.getLast_name() + ", " + student.getFirst_name() + " " + (student.getMiddle_name()==null? "": student.getMiddle_name())).toUpperCase(Locale.ENGLISH);
        slip.INFO_COURSE = course==null? "":course.getName();
        slip.INFO_MAJOR = (major==null? "": major);
        
        ArrayList<AdvisingSlipData> table = new ArrayList<>();
        for (JoinTables join : subjectInformation) {
            SubjectMapping subject = join.info;
            LoadSectionMapping section = join.section;

            /**
             * code added 8/28/17 by: joemar
             */
            String sectionName = "";
            try {
                if (section.getYear_level() != 0) {
                    AcademicProgramMapping apMap = Database.connect().academic_program().getPrimary(section.getACADPROG_id());
                    if(apMap!=null){
                        sectionName = apMap.getCode() + " ";
                    }
                    sectionName += section.getYear_level().toString()
                            + section.getSection_name()
                            + " - G" + (section.get_group()==null? "":section.get_group().toString());
                } else {
                    sectionName = section.getSection_name();
                }
            } catch (NullPointerException a) {
                sectionName = section.getSection_name();
            }
            AdvisingSlipData tableData = new AdvisingSlipData();
            tableData.lab_units = Double.toString(subject.getLab_units());
            tableData.lec_units = Double.toString(subject.getLec_units());
            tableData.room = "";
            tableData.schedule = "";
            tableData.section = sectionName;
            tableData.subj_title = subject.getDescriptive_title();
            tableData.subject_code = subject.getCode();
            table.add(tableData);
        }

        slip.INFO_SUBJECTS = table;
        slip.INFO_REMARKS = "";
        slip.INFO_ADVISER = evaluator.getLast_name() + ", " + evaluator.getFirst_name();

        
        PrintLogsMapping logs = Mono.orm().newSearch(Database.connect().print_logs())
                .eq(DB.print_logs().STUDENT_id, this.student.getCict_id())
                .eq(DB.print_logs().title, "ADVISING SLIP")
                .eq(DB.print_logs().EVALUATION_id, evaluation.getId())
                .active().first();
        boolean secondCopy = false;
        if(ReportsUtility.savePrintLogs(this.student.getCict_id(), "ADVISING SLIP", "EVALUATION", (logs!=null)? "REPRINT" : "INITIAL", evaluation.getId().toString())){
            secondCopy = (logs!=null);
        } else {
            System.err.println("PRINT LOGS NOT SAVED");
            return;
        }
        slip.print(secondCopy);
    }

}

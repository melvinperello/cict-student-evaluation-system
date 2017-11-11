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
package org.cict.reports.deficiency;

import app.lazy.models.AcademicProgramMapping;
import app.lazy.models.CurriculumMapping;
import app.lazy.models.CurriculumRequisiteExtMapping;
import app.lazy.models.CurriculumRequisiteLineMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.StudentMapping;
import app.lazy.models.StudentProfileMapping;
import app.lazy.models.SubjectMapping;
import app.lazy.models.utils.FacultyUtility;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import java.util.ArrayList;
import org.cict.SubjectClassification;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.cict.evaluation.assessment.AssessmentResults;
import org.cict.evaluation.assessment.CurricularLevelAssesor;
import org.cict.evaluation.assessment.SubjectAssessmentDetials;
import org.cict.reports.ReportsDirectory;
import org.hibernate.criterion.Order;

/**
 *
 * @author Joemar
 */
public class PrintDeficiency extends Transaction {

    public Integer CICT_id;

    private CurricularLevelAssesor assessmentResults;
    private StudentMapping student;
    private CurriculumMapping curriculum;
    private AcademicProgramMapping acadProg;

    @Override
    protected boolean transaction() {
        //----------------------------------------------------------------------
        student = Database.connect().student().getPrimary(CICT_id);
        if (student == null) {
            System.out.println("NO STUDENT FOUND");
            message = "No student found.";
            return false;
        }
        //----------------------------------------------------------------------
        // if student has profile get address
        if (student.getHas_profile() == 1) {
            StudentProfileMapping spMap = Mono.orm().newSearch(Database.connect().student_profile())
                    .eq(DB.student_profile().STUDENT_id, student.getCict_id())
                    .active(Order.desc(DB.student_profile().id))
                    .first();
            //------------------------------------------------------------------
            // Address Information
            if (spMap != null) {
                String hNum = spMap.getHouse_no(),
                        brgy = spMap.getBrgy(),
                        city = spMap.getCity(),
                        province = spMap.getProvince();
                if (hNum != null) {
                    address = hNum;
                }
                if (brgy != null) {
                    if (!address.isEmpty()) {
                        address += " " + spMap.getBrgy();
                    } else {
                        address = brgy;
                    }
                }
                if (city != null) {
                    if (!address.isEmpty()) {
                        address += " " + city;
                    } else {
                        address = city;
                    }
                }
                if (province != null) {
                    if (!address.isEmpty()) {
                        address += ", " + province;
                    } else {
                        address = province;
                    }
                }
            }
            //------------------------------------------------------------------

        }
        //----------------------------------------------------------------------
        if (student.getCURRICULUM_id() == null) {
            System.out.println("NO CURRICULUM FOUND");
            message = "No curriculum found.";
            return false;
        }
        //----------------------------------------------------------------------
        curriculum = Database.connect()
                .curriculum().getPrimary(student.getCURRICULUM_id());
        if (curriculum == null) {
            System.out.println("Cannot Retrieve Curriculum");
            message = "Cannot Retrieve Curriculum.";
            return false;
        }
        acadProg = Database.connect()
                .academic_program().getPrimary(curriculum.getACADPROG_id());
        if (acadProg == null) {
            System.out.println("Cannot Retrieve Academic Program");
            message = "Cannot Retrieve Academic Program.";
            return false;
        }
        course = acadProg == null ? "" : acadProg.getName();
        //----------------------------------------------------------------------
        CurricularLevelAssesor cla = new CurricularLevelAssesor(student);
        cla.assess();
        //----------------------------------------------------------------------
        if (cla.hasPrepData()) {
            String text = cla.getPrepCurriculum().getName() + " -> " + cla.getConsCurriculum().getName();
            System.out.println(text);
        } else {
            System.out.println(cla.getConsCurriculum().getName());
        }
        getResult(cla, 1, cla.hasPrepData());
        getResult(cla, 2, cla.hasPrepData());
        getResult(cla, 3, cla.hasPrepData());
        getResult(cla, 4, cla.hasPrepData());
        //----------------------------------------------------------------------
        return true;
    }
    
    private ArrayList<Object[]> details = new ArrayList<>();

    private void getResult(CurricularLevelAssesor cla, int year, boolean hasPrepData) {
        AssessmentResults result = cla.getAnnualAssessment(year);
        if (hasPrepData) {
            if (year <= 2) {
                curriculum = cla.getPrepCurriculum();
            } else {
                curriculum = cla.getConsCurriculum();
            }
        }

        for (int semester = 1; semester <= 2; semester++) {
            ArrayList<SubjectAssessmentDetials> sadetails;
            try {
                sadetails = result.getUnacquiredSubjects();
            } catch (Exception e) {
                continue;
            }
            for (SubjectAssessmentDetials sadetail : sadetails) {
                // ------------------------------------------
                if(!sadetail.getSemester().equals(semester))
                    continue;
                // -------------------------------------
                SubjectMapping subject = sadetail.getSubjectDetails();
                Object[] detail = new Object[5];
                if (subject != null) {
                    detail[0] = subject; //code
                    String hrs = "";
                    if (subject.getLab_units() != 0.0) {
                        hrs = "5"; //hrs per wk;
                    } else if (subject.getType().equalsIgnoreCase(SubjectClassification.TYPE_PE)) {
                        hrs = "2"; //hrs per wk;
                    } else if (subject.getType().equalsIgnoreCase(SubjectClassification.TYPE_NSTP)) {
                        hrs = "";
                    } else {
                        hrs = "3"; //hrs per wk;
                    }
                    detail[1] = hrs;

                    String prereq = "";
                    ArrayList<CurriculumRequisiteLineMapping> crlMaps = sadetail.getSubjectRequisites();
                    if (crlMaps != null) {
                        for (CurriculumRequisiteLineMapping crlMap : crlMaps) {
                            SubjectMapping subjectPrereq = Database.connect().subject().getPrimary(crlMap.getSUBJECT_id_req());
                            if (prereq.isEmpty()) {
                                prereq = subjectPrereq.getCode();
                            } else {
                                prereq += ", " + subjectPrereq.getCode();
                            }
                        }
                    } else {
                        prereq = "NONE";
                    }
                    detail[2] = prereq;

                    String coreq = "";//coreq
                    ArrayList<CurriculumRequisiteExtMapping> creMaps = Mono.orm().newSearch(Database.connect().curriculum_requisite_ext())
                            .eq(DB.curriculum_requisite_ext().CURRICULUM_id, curriculum.getId())
                            .eq(DB.curriculum_requisite_ext().SUBJECT_id_get, subject.getId())
                            .eq(DB.curriculum_requisite_ext().type, "COREQUISITE")
                            .active()
                            .all();
                    if (creMaps != null) {
                        for (CurriculumRequisiteExtMapping creMap : creMaps) {
                            SubjectMapping subjectCoreq = Database.connect().subject().getPrimary(creMap.getSUBJECT_id_req());
                            if (coreq.isEmpty()) {
                                coreq = subjectCoreq.getCode();
                            } else {
                                coreq += ", " + subjectCoreq.getCode();
                            }
                        }
                    } else {
                        coreq = "NONE";
                    }
                    detail[3] = coreq;
                    detail[4] = year + "" + semester;
                    details.add(detail);
                }
            }
        }
    }

    public final static String SAVE_DIRECTORY = "reports/deficiency";
    private ArrayList<Object[]> fyrfsem = new ArrayList<>();
    private ArrayList<Object[]> fyrssem = new ArrayList<>();
    private ArrayList<Object[]> syrfsem = new ArrayList<>();
    private ArrayList<Object[]> syrssem = new ArrayList<>();
    private ArrayList<Object[]> tyrfsem = new ArrayList<>();
    private ArrayList<Object[]> tyrssem = new ArrayList<>();
    private ArrayList<Object[]> fryrfsem = new ArrayList<>();
    private ArrayList<Object[]> fryrssem = new ArrayList<>();
    private String address = "";
    private String course = "";

    @Override
    protected void after() {
        String doc = student.getId() + "_"
                + Mono.orm().getServerTime().getCalendar().getTimeInMillis();

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

        String fullName = student.getLast_name() + ", " + student.getFirst_name()
                + (student.getMiddle_name() == null ? "" : (" " + student.getMiddle_name()));

        Deficiency def = new Deficiency(RESULT);
        def.STUDENT_NUMBER = student.getId() == null ? "NONE" : student.getId();
        def.STUDENT_NAME = fullName == null ? "NONE" : fullName;
        def.STUDENT_ADDRESS = address;
        def.CURRICULUM_NAME = course;
        for (Object[] detail : details) {
            String key = (String) detail[4];
            SubjectMapping subject = (SubjectMapping) detail[0];
            switch (key) {
                case "11":
                    store(fyrfsem, detail);
                    break;
                case "12":
                    store(fyrssem, detail);
                    break;
                case "21":
                    store(syrfsem, detail);
                    break;
                case "22":
                    store(syrssem, detail);
                    break;
                case "31":
                    store(tyrfsem, detail);
                    break;
                case "32":
                    store(tyrssem, detail);
                    break;
                case "41":
                    store(fryrfsem, detail);
                    break;
                case "42":
                    store(fryrssem, detail);
                    break;
            }

        }
        def.SUBJECTS_PER_SEM.put("11", fyrfsem);
        def.SUBJECTS_PER_SEM.put("12", fyrssem);
        def.SUBJECTS_PER_SEM.put("21", syrfsem);
        def.SUBJECTS_PER_SEM.put("22", syrssem);
        def.SUBJECTS_PER_SEM.put("31", tyrfsem);
        def.SUBJECTS_PER_SEM.put("32", tyrssem);
        def.SUBJECTS_PER_SEM.put("41", fryrfsem);
        def.SUBJECTS_PER_SEM.put("42", fryrssem);
        def.DATETIME = Mono.orm().getServerTime().getDateWithFormat().toString();
        def.USER = CollegeFaculty.instance().getFirstLastName();
        def.print();
    }

    private String message = "";

    public String getMessage() {
        return message;
    }

    private void store(ArrayList<Object[]> subjectDetails, Object[] detail) {
        subjectDetails.add(detail);
    }
}

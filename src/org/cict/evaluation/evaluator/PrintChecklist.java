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
import app.lazy.models.CurriculumMapping;
import app.lazy.models.CurriculumRequisiteExtMapping;
import app.lazy.models.CurriculumRequisiteLineMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.GradeMapping;
import app.lazy.models.StudentMapping;
import app.lazy.models.StudentProfileMapping;
import app.lazy.models.SubjectMapping;
import artifacts.FTPManager;
import com.itextpdf.text.Document;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import java.io.File;
import java.util.ArrayList;
import org.apache.commons.io.FileUtils;
import org.cict.PublicConstants;
import org.cict.SubjectClassification;
import org.cict.authentication.authenticator.SystemProperties;
import org.cict.evaluation.assessment.AssessmentResults;
import org.cict.evaluation.assessment.CurricularLevelAssesor;
import org.cict.evaluation.assessment.SubjectAssessmentDetials;
import org.cict.reports.ReportsDirectory;
import org.cict.reports.checklist.ACT;
import org.cict.reports.checklist.BITCT;
import org.cict.reports.checklist.BSIT1112;
import org.cict.reports.checklist.BSIT1516;
import org.hibernate.criterion.Order;

/**
 *
 * @author Joemar
 */
public class PrintChecklist extends Transaction {

    public Integer CICT_id;
    public Integer CURRICULUM_id;
    public Boolean printLegacy = false;

    private StudentMapping student;
    private String address = "", sectionName = "", highSchool = "";
    private AcademicProgramMapping acadProg;
    private CurriculumMapping curriculum;
    private ArrayList<Object[]> details = new ArrayList<>();
    private StudentProfileMapping spMap;
    
    //-------------------
    private Document documentFormat;

    public void setDocumentFormat(Document documentFormat) {
        this.documentFormat = documentFormat;
    }

    @Override
    protected boolean transaction() {
        //----------------------------------------------------------------------
        student = Database.connect().student().getPrimary(CICT_id);
        if (student == null) {
            System.out.println("NO STUDENT");
            return false;
        }
        //----------------------------------------------------------------------
        // @Question: What is the difference between 'curriculum_' and 'curriculum'
        CurriculumMapping /*This One*/ curriculum_ = Database.connect().curriculum().getPrimary(student.getCURRICULUM_id());
        acadProg = Database.connect().academic_program()
                .getPrimary(curriculum_.getACADPROG_id());

        if (CURRICULUM_id != null) {
            curriculum /*And This One*/ = Database.connect().curriculum().getPrimary(CURRICULUM_id);
            AcademicProgramMapping apMap = Database.connect().academic_program().getPrimary(curriculum.getACADPROG_id());
            sectionName = apMap.getCode() + " " + student.getYear_level() + student.getSection()
                    + "-G" + student.get_group();
        } else {
            System.out.println("NO CURRICULUM ID");
            curriculum = Database.connect().curriculum().getPrimary(student.getCURRICULUM_id());
            sectionName = acadProg.getCode() + " " + student.getYear_level() + student.getSection()
                    + "-G" + student.get_group();
        }

        if (curriculum == null) {
            System.out.println("NO CURRICULUM");
            return false;
        }

        // Get Student Address.
        //----------------------------------------------------------------------
        if (student.getHas_profile().equals(1)) {
            //------------------------------------------------------------------
            // search the latest profile order desc
            spMap = Mono.orm()
                    .newSearch(Database.connect().student_profile())
                    .eq(DB.student_profile().STUDENT_id, student.getCict_id())
                    .active(Order.desc(DB.student_profile().id))
                    .first();
            //------------------------------------------------------------------
            // check if profile was found
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
                highSchool = "";
            }
            //------------------------------------------------------------------
        }
        // (End of Getting Address)---------------------------------------------
        // CLA
        CurricularLevelAssesor cla = new CurricularLevelAssesor(student);
        cla.assess();
        getResult(cla, 1, cla.hasPrepData());
        getResult(cla, 2, cla.hasPrepData());
        getResult(cla, 3, cla.hasPrepData());
        getResult(cla, 4, cla.hasPrepData());
        //----------------------------------------------------------------------
        return true;
    }

    private void getResult(CurricularLevelAssesor cla, int year, boolean hasPrepData) {
        AssessmentResults result = cla.getAnnualAssessment(year);
        for (int semester = 1; semester <= 2; semester++) {
            ArrayList<SubjectAssessmentDetials> sadetails;
            try {
                sadetails = result.getSemestralResults(semester);
            } catch (Exception e) {
                continue;
            }
            for (SubjectAssessmentDetials sadetail : sadetails) {
                SubjectMapping subject = sadetail.getSubjectDetails();
                Object[] detail = new Object[6];
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
                    //----------------------------------------------------------
                    // Add Grade Column
                    detail[5] = tryToWriteGrade(sadetail);
                    //----------------------------------------------------------
                    details.add(detail);
                }
            }
        }
    }

    /**
     * try to get the grade of that subject.
     *
     * @param sadetail
     * @return
     */
    private String tryToWriteGrade(SubjectAssessmentDetials sadetail) {
        //----------------------------------------------------------
        String gradeToWrite = "";
        // Get Latest Grade even it is not passing
        //----------------------------------------------------------
        GradeMapping assessedGrades = Mono.orm()
                .newSearch(Database.connect().grade())
                .eq(DB.grade().SUBJECT_id, sadetail.getSubjectDetails().getId())
                // must be posted
                .eq(DB.grade().posted, 1)
                // from this student
                .eq(DB.grade().STUDENT_id, this.student.getCict_id())
                .active(Order.desc(DB.grade().id))
                .first();
        //----------------------------------------------------------
        if (assessedGrades == null) {
            // No Grade
            gradeToWrite = "";
        } else {
            gradeToWrite = assessedGrades.getRating();
            if (gradeToWrite.length() > 4) {
                // must be 3 chars max
                gradeToWrite = gradeToWrite.substring(0, 3);
            }
        }
        //----------------------------------------------------------
        return gradeToWrite;
    }

    public final static String SAVE_DIRECTORY = "reports/checklist";
    private ArrayList<Object[]> fyrfsem = new ArrayList<>();
    private ArrayList<Object[]> fyrssem = new ArrayList<>();
    private ArrayList<Object[]> syrfsem = new ArrayList<>();
    private ArrayList<Object[]> syrssem = new ArrayList<>();
    private ArrayList<Object[]> tyrfsem = new ArrayList<>();
    private ArrayList<Object[]> tyrssem = new ArrayList<>();
    private ArrayList<Object[]> fryrfsem = new ArrayList<>();
    private ArrayList<Object[]> fryrssem = new ArrayList<>();

    @Override
    protected void after() {
        String LEGACY = null;
        if (printLegacy) {
            for (String legacy : PublicConstants.LEGACY_CURRICULUM) {
                if (legacy.equalsIgnoreCase(curriculum.getName())) {
                    LEGACY = legacy;
                    System.out.println("Print Legacy " + LEGACY);
                    break;
                }
            }
        }

        String doc = student.getId() + "_"
                + Mono.orm().getServerTime().getCalendar().getTimeInMillis();

        String RESULT = SAVE_DIRECTORY + "/" + doc + ".pdf";

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

        if (LEGACY == null) {
            System.out.println("Print Standard");
            printStandard(RESULT, false);
            return;
        }

        if (LEGACY.equalsIgnoreCase(PublicConstants.LEGACY_CURRICULUM[0]) && printLegacy) {
            ACT act = new ACT(RESULT);
            act.STUDENT_NUMBER = student.getId();
            String fullName = student.getLast_name() + ", " + student.getFirst_name();
            String midName = student.getMiddle_name();
            if (midName != null) {
                fullName += " " + midName;
            }
            act.STUDENT_NAME = fullName;
            act.STUDENT_COURSE_YR_SEC_GRP = sectionName;
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
                }
            }
            act.SUBJECTS_PER_SEM.put("11", fyrfsem);
            act.SUBJECTS_PER_SEM.put("12", fyrssem);
            act.SUBJECTS_PER_SEM.put("21", syrfsem);
            act.SUBJECTS_PER_SEM.put("22", syrssem);
            int val = act.print();
        } else if (LEGACY.equalsIgnoreCase(PublicConstants.LEGACY_CURRICULUM[1]) && printLegacy) {
            BSIT1112 bsit1112 = new BSIT1112(RESULT);
            bsit1112.STUDENT_NUMBER = student.getId();
            String fullName = student.getLast_name() + ", " + student.getFirst_name();
            String midName = student.getMiddle_name();
            if (midName != null) {
                fullName += " " + midName;
            }
            bsit1112.STUDENT_NAME = fullName;
            bsit1112.STUDENT_ADDRESS = address;
            bsit1112.STUDENT_HS = highSchool;
            bsit1112.IMAGE_LOCATION = this.getImageLocation();

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
            bsit1112.SUBJECTS_PER_SEM.put("11", fyrfsem);
            bsit1112.SUBJECTS_PER_SEM.put("12", fyrssem);
            bsit1112.SUBJECTS_PER_SEM.put("21", syrfsem);
            bsit1112.SUBJECTS_PER_SEM.put("22", syrssem);
            bsit1112.SUBJECTS_PER_SEM.put("31", tyrfsem);
            bsit1112.SUBJECTS_PER_SEM.put("32", tyrssem);
            bsit1112.SUBJECTS_PER_SEM.put("41", fryrfsem);
            bsit1112.SUBJECTS_PER_SEM.put("42", fryrssem);
            bsit1112.print();
        } else if (LEGACY.equalsIgnoreCase(PublicConstants.LEGACY_CURRICULUM[2]) && printLegacy) {
            BITCT bitct = new BITCT(RESULT);
            bitct.STUDENT_NUMBER = student.getId();
            String fullName = student.getLast_name() + ", " + student.getFirst_name();
            String midName = (student.getMiddle_name() == null ? "" : student.getMiddle_name());
            fullName += (midName != null ? (" " + midName) : "");
            bitct.STUDENT_NAME = fullName;
            bitct.STUDENT_ADDRESS = address;
            bitct.STUDENT_HS = highSchool;
            bitct.IMAGE_LOCATION = this.getImageLocation();

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
            bitct.SUBJECTS_PER_SEM.put("11", fyrfsem);
            bitct.SUBJECTS_PER_SEM.put("12", fyrssem);
            bitct.SUBJECTS_PER_SEM.put("21", syrfsem);
            bitct.SUBJECTS_PER_SEM.put("22", syrssem);
            bitct.SUBJECTS_PER_SEM.put("31", tyrfsem);
            bitct.SUBJECTS_PER_SEM.put("32", tyrssem);
            bitct.SUBJECTS_PER_SEM.put("41", fryrfsem);
            bitct.SUBJECTS_PER_SEM.put("42", fryrssem);
            bitct.print();
        } else if (LEGACY.equalsIgnoreCase(PublicConstants.LEGACY_CURRICULUM[3]) && printLegacy) {
            BSIT1516 bsit1516 = new BSIT1516(RESULT, false);
            bsit1516.PRINT_ORIGINAL = true;
            bsit1516.STUDENT_NUMBER = student.getId();

            bsit1516.setDocumentFormat(documentFormat);
            
            String fullName = student.getLast_name() + ", " + student.getFirst_name();
            String midName = (student.getMiddle_name() == null ? "" : student.getMiddle_name());
            fullName += (midName != null ? (" " + midName) : "");

            bsit1516.STUDENT_NAME = fullName;
            bsit1516.STUDENT_ADDRESS = address;
            if (acadProg != null) {
                bsit1516.COURSE = acadProg.getName();
            }

            Boolean isLadderized = curriculum.getLadderization().equalsIgnoreCase("yes");
            String major = curriculum.getMajor();
            try {
                if (!major.equalsIgnoreCase("none")) {
                    bsit1516.MAJOR = major;
                }
            } catch (Exception e) {
            }
            for (Object[] detail : details) {
                String key = (String) detail[4];
                SubjectMapping subject = (SubjectMapping) detail[0];
                if (subject.getCode().equalsIgnoreCase("CAPS 01")) {
                    detail[2] = "Regular 3rd year \nStanding";
                    detail[3] = "Regular 3rd year \nStanding";
                }
                if (subject.getType().equalsIgnoreCase(SubjectClassification.TYPE_INTERNSHIP)) {
                    detail[1] = "486 hours";
                    detail[2] = "4th Year Standing";
                }
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
            bsit1516.SUBJECTS_PER_SEM.put("11", fyrfsem);
            bsit1516.SUBJECTS_PER_SEM.put("12", fyrssem);
            bsit1516.SUBJECTS_PER_SEM.put("21", syrfsem);
            bsit1516.SUBJECTS_PER_SEM.put("22", syrssem);
            bsit1516.SUBJECTS_PER_SEM.put("31", tyrfsem);
            bsit1516.SUBJECTS_PER_SEM.put("32", tyrssem);
            bsit1516.SUBJECTS_PER_SEM.put("41", fryrfsem);
            bsit1516.SUBJECTS_PER_SEM.put("42", fryrssem);

            int val = bsit1516.print();
        } else {
            Boolean isBSIT1516 = false;
            if (LEGACY.equalsIgnoreCase(PublicConstants.LEGACY_CURRICULUM[3])) {
                isBSIT1516 = true;
            }
            printStandard(RESULT, isBSIT1516);
        }
    }

    private void printStandard(String RESULT, Boolean printOriginal) {
        BSIT1516 standard = new BSIT1516(RESULT, true);
        standard.STUDENT_NUMBER = student.getId();
        standard.PRINT_ORIGINAL = printOriginal;

        standard.setDocumentFormat(documentFormat);
        
        String fullName = student.getLast_name() + ", " + student.getFirst_name();
        String midName = (student.getMiddle_name() == null ? "" : student.getMiddle_name());
        fullName += (midName != null ? (" " + midName) : "");

        standard.STUDENT_NAME = fullName;
        standard.STUDENT_ADDRESS = address;
        if (acadProg != null) {
            standard.COURSE = acadProg.getName();
        }

        String year = SystemProperties.instance().getCurrentAcademicTerm().getSchool_year();
        standard.SCHOOL_YEAR = year;
        String major = curriculum.getMajor();
        try {
            if (!major.equalsIgnoreCase("none")) {
                standard.MAJOR = major;
            }
        } catch (Exception e) {
        }
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
        standard.SUBJECTS_PER_SEM.put("11", fyrfsem);
        standard.SUBJECTS_PER_SEM.put("12", fyrssem);
        standard.SUBJECTS_PER_SEM.put("21", syrfsem);
        standard.SUBJECTS_PER_SEM.put("22", syrssem);
        standard.SUBJECTS_PER_SEM.put("31", tyrfsem);
        standard.SUBJECTS_PER_SEM.put("32", tyrssem);
        standard.SUBJECTS_PER_SEM.put("41", fryrfsem);
        standard.SUBJECTS_PER_SEM.put("42", fryrssem);
        
        // conclude that when third yr first sem is empty, the curriculum is
        // a 2 year course
        if(tyrfsem.isEmpty()) {
            standard.STUDY_YEARS = 2;
        }
        
        int val = standard.print();
    }

    private void store(ArrayList<Object[]> subjectDetails, Object[] detail) {
        subjectDetails.add(detail);
    }

    //--------------------------------------------------------------------------
    // use this variable as comparison if the image will use the default
    public final static String DEFAULT_IMAGE_LOC = "src/org/cict/reports/images/checklist/default2x2.png";
    //--------------------------------------------------------------------------

    private String getImageLocation() {
        String studentImage = (spMap == null ? DEFAULT_IMAGE_LOC : spMap.getProfile_picture());
        if (studentImage == null
                || studentImage.isEmpty()
                || studentImage.equalsIgnoreCase("NONE")) {
            return DEFAULT_IMAGE_LOC;
        } else {
            String tempProfilePath = "temp/images/profile";
            String tempProfileImagePath = tempProfilePath + "/" + studentImage;
            File tempProfileDir = new File(tempProfilePath);
            File tempProfileImage = new File(tempProfileImagePath);
            try {
                FileUtils.forceMkdir(tempProfileDir);
                //--------------------------------------------------------------
                // Check if the file was downloaded successfully
                boolean result = FTPManager.download(PublicConstants.FTP_STUDENT_AVATAR, studentImage, tempProfileImage.getAbsolutePath());
                //--------------------------------------------------------------
                // Check the results
                if (result) {
                    return (tempProfileImage.getAbsolutePath());
                } else {
                    return DEFAULT_IMAGE_LOC;
                }
                //--------------------------------------------------------------
            } catch (Exception e) {
                return DEFAULT_IMAGE_LOC;
            }
        }
    }
}

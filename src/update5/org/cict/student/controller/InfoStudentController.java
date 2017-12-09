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
import app.lazy.models.AccountStudentMapping;
import app.lazy.models.CurriculumMapping;
import app.lazy.models.CurriculumPreMapping;
import app.lazy.models.CurriculumSubjectMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.EvaluationMapping;
import app.lazy.models.GradeMapping;
import app.lazy.models.StudentCourseHistoryMapping;
import app.lazy.models.StudentMapping;
import app.lazy.models.StudentProfileMapping;
import app.lazy.models.utils.DateString;
import app.lazy.models.utils.FacultyUtility;
import artifacts.ImageUtility;
import artifacts.MonoString;
import com.izum.fx.textinputfilters.StringFilter;
import com.izum.fx.textinputfilters.TextInputFilters;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.SimpleTask;
import com.jhmvin.fx.async.Transaction;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.LayoutDataFX;
import com.jhmvin.fx.display.SceneFX;
import com.jhmvin.transitions.Animate;
import com.melvin.mono.fx.bootstrap.M;
import java.util.ArrayList;
import java.util.Objects;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.cict.GenericLoadingShow;
import org.cict.PublicConstants;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.cict.authentication.authenticator.SystemProperties;
import org.cict.evaluation.evaluator.PrintChecklist;
import org.cict.evaluation.student.StudentValues;
import org.cict.evaluation.student.credit.CreditController;
import org.cict.evaluation.student.history.StudentHistoryController;
import org.cict.reports.ReportsUtility;
import org.cict.reports.deficiency.PrintDeficiency;
import org.cict.reports.profile.student.PrintStudentProfile;
import org.controlsfx.control.Notifications;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import update3.org.cict.CurriculumConstants;
import update3.org.cict.controller.sectionmain.SectionHomeController;
import update3.org.collegechooser.ChooserHome;
import update5.org.cict.student.layout.CourseHistoryRow;
import update5.org.cict.student.layout.CurriculumChooser;

/**
 *
 * @author Joemar
 */
public class InfoStudentController extends SceneFX implements ControllerFX {

    @FXML
    private VBox application_root;

    @FXML
    private VBox vbox_verify;

    @FXML
    private JFXButton btn_back;

    @FXML
    private Label lbl_lastname;

    @FXML
    private Label lbl_firstname;

    @FXML
    private Label lbl_middlename;

    @FXML
    private JFXButton btn_edit_grades;

    @FXML
    private JFXButton btn_verify;

    @FXML
    private JFXButton btn_change_college;

    @FXML
    private JFXButton btn_remove_student;

    @FXML
    private JFXButton btn_view_profile;

    @FXML
    private JFXButton btn_view_deficiency;

    @FXML
    private TextField txt_studnum;

    @FXML
    private TextField txt_lastname;

    @FXML
    private TextField txt_firstname;

    @FXML
    private TextField txt_middlename;

    @FXML
    private ComboBox cmb_yrlvl;

    @FXML
    private Label lbl_cictid;

    @FXML
    private ComboBox cmb_campus;

    @FXML
    private TextField txt_section;

    @FXML
    private TextField txt_group;

    @FXML
    private JFXButton btn_editsave;

    @FXML
    private JFXButton btn_shift_course;

    @FXML
    private JFXButton btn_shift_course1;

    @FXML
    private RadioButton rbtn_male;

    @FXML
    private RadioButton rbtn_female;

    @FXML
    private Label lbl_verified_by;

    @FXML
    private Label lbl_verified_date;

    @FXML
    private Label lbl_acad_prog;

    @FXML
    private Label lbl_currriculum;

    @FXML
    private HBox hbox_main_view;

    @FXML
    private JFXButton btn_show_course_history;

    @FXML
    private JFXButton btn_show_course_history1;

    @FXML
    private VBox vbox_history;

    @FXML
    private HBox hbox_home;

    @FXML
    private VBox vbox_shown;

    @FXML
    private VBox vbox_removed;

    @FXML
    private ImageView img_icon;

    @FXML
    private Label lbl_college;

    @FXML
    private JFXButton btn_view_checklist;

    @FXML
    private JFXButton btn_view_history;

    private StudentValues studentValues = new StudentValues();
    private CurriculumMapping curriculum;
    private AcademicProgramMapping acadProg;

    public InfoStudentController(StudentInformation currentStudent) {
        this.CURRENT_STUDENT = currentStudent.getStudentMapping();
        this.curriculum = currentStudent.getCurriculumMapping();
        this.acadProg = currentStudent.getAcademicProgramMapping();
    }

    private void setStudentInformation(StudentInformation currentStudent) {
        this.CURRENT_STUDENT = currentStudent.getStudentMapping();
        this.curriculum = currentStudent.getCurriculumMapping();
        this.acadProg = currentStudent.getAcademicProgramMapping();
    }

    private LayoutDataFX homeFX;

    public void setHomeFX(LayoutDataFX home) {
        this.homeFX = home;
    }

    @Override
    public void onInitialization() {
        super.bindScene(application_root);
        ToggleGroup group2 = new ToggleGroup();
        rbtn_male.setToggleGroup(group2);
        rbtn_female.setToggleGroup(group2);

        this.isVerified(false);
        this.setCmbYearLevel();
        this.setCmbCampus();
        this.setValues();
        addTextFieldFilters();

        vbox_shown.setVisible(false);
        hbox_home.setVisible(true);

        //-------------------------------
        // set image
        SimpleTask set_profile = new SimpleTask("set_profile");
        set_profile.setTask(() -> {
            this.setImageView();
        });
        set_profile.whenCancelled(() -> {
            Notifications.create().text("Loading of image is cancelled")
                    .showInformation();
        });
        set_profile.whenFailed(() -> {
            Notifications.create().text("Failed to load image.")
                    .showInformation();
        });
        set_profile.whenSuccess(() -> {
        });
        set_profile.start();
        //-------------------------------
    }

    private void addTextFieldFilters() {
        StringFilter textFilter = TextInputFilters.string()
                .setFilterMode(StringFilter.LETTER_SPACE)
                .setMaxCharacters(100)
                .setNoLeadingTrailingSpaces(false)
                .setFilterManager(filterManager -> {
                    if (!filterManager.isValid()) {
                        Mono.fx().alert().createWarning().setHeader("Warning")
                                .setMessage(filterManager.getMessage())
                                .show();
                    }
                });
        textFilter.clone().setTextSource(txt_firstname).applyFilter();
        textFilter.clone().setTextSource(txt_lastname).applyFilter();
        textFilter.clone().setTextSource(txt_middlename).applyFilter();
        textFilter.clone().setFilterMode(StringFilter.LETTER_DIGIT).setTextSource(txt_section).applyFilter();

        textFilter.clone().setFilterMode(StringFilter.LETTER_DIGIT)
                .setMaxCharacters(50)
                .setNoLeadingTrailingSpaces(true)
                .setTextSource(txt_studnum).applyFilter();

        textFilter.clone().setFilterMode(StringFilter.DIGIT)
                .setMaxCharacters(11)
                .setNoLeadingTrailingSpaces(true)
                .setTextSource(txt_group).applyFilter();

    }

    @Override
    public void onEventHandling() {
        btn_editsave.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            onSave();
        });

        super.addClickEvent(btn_back, () -> {
            back();
        });

        Mono.fx().key(KeyCode.ENTER).release(application_root, () -> {
            this.onSave();
        });

        super.addClickEvent(btn_edit_grades, () -> {
            this.onShowCredit();
        });

        super.addClickEvent(btn_verify, () -> {
            this.onVerify();
        });

        super.addClickEvent(btn_view_deficiency, () -> {
            this.printDeficiency();
        });

        super.addClickEvent(btn_view_profile, () -> {
            this.printProfile();
        });

        // Code to change student college
        super.addClickEvent(btn_change_college, () -> {
            this.showCollegeChanger();
        });

        super.addClickEvent(btn_shift_course, () -> {
            this.onShiftCourse();
        });

        super.addClickEvent(btn_show_course_history, () -> {
            this.loadHistory();
            Animate.fade(hbox_home, 150, () -> {
                hbox_home.setVisible(false);
                vbox_shown.setVisible(true);
            }, vbox_shown);
        });
        super.addClickEvent(btn_show_course_history1, () -> {
            Animate.fade(vbox_shown, 150, () -> {
                vbox_shown.setVisible(false);
                hbox_home.setVisible(true);
            }, hbox_home);
        });

        super.addClickEvent(btn_remove_student, () -> {
            this.onRemove();
        });

        super.addClickEvent(btn_view_checklist, () -> {
            this.printChecklist();
        });

        super.addClickEvent(btn_view_history, () -> {
            this.onShowHistory();
        });
    }

    private void printChecklist() {
        // disallows cross enrollees to print a check list.
        if (this.CURRENT_STUDENT.getResidency().equalsIgnoreCase("CROSS_ENROLLEE")) {
            Mono.fx().snackbar().showInfo(application_root, "No Check List for Cross Enrollees");
            return;
        }
        //----------------------------------------------------------------------
        // current curriculum
        CurriculumMapping curriculum = Database.connect()
                .curriculum().getPrimary(CURRENT_STUDENT.getCURRICULUM_id());

        if (curriculum == null) {
            System.err.println("Curriculum Was Not Found !");
            return;
        }
        // check if the curriculum is ladderized and it is consequent before trying to get the prep.
        CurriculumMapping curriculum_prep = null;
        if (curriculum.getLadderization_type().equalsIgnoreCase(CurriculumConstants.TYPE_CONSEQUENT)) {
            // get prep curriculum
            if (CURRENT_STUDENT.getPREP_id() == null) {
                System.err.println("Student Has No Preparatory. !");
                return;
            }
            curriculum_prep = Database.connect()
                    .curriculum().getPrimary(CURRENT_STUDENT.getPREP_id());
            if (curriculum_prep == null) {
                // since the curriculum was a CONSEQUENT
                // a preparatory is required and must exist.
                System.err.println("Preparatory Was Not Found !");
                return;
            }
        }
        //----------------------------------------------------------------------
        /*
        CurriculumMapping curriculum_prep = null;
        if (currentStudent.getPREP_id() != null) {
            curriculum_prep = Database.connect().curriculum().getPrimary(currentStudent.getPREP_id());
        }
         */
        //----------------------------------------------------------------------
        // try to check for legacy curriculum
        Boolean hasLegacyExisting = false;
        for (String legacy : PublicConstants.LEGACY_CURRICULUM) {
            if (legacy.equalsIgnoreCase(curriculum.getName())) {
                hasLegacyExisting = true;
                break;
            }
            if (curriculum_prep != null) {
                if (legacy.equalsIgnoreCase(curriculum_prep.getName())) {
                    hasLegacyExisting = true;
                    break;
                }
            }
        }
        //----------------------------------------------------------------------
        // ask to print if legacy or standard.
        Boolean printLegacy = false;
        if (hasLegacyExisting) {
            int res = Mono.fx().alert().createConfirmation()
                    .setHeader("Checklist Format")
                    .setMessage("Please choose a format.")
                    .confirmCustom("Legacy", "Standard");
            if (res == 1) {
                printLegacy = true;
            }
        }
        //----------------------------------------------------------------------
        if (curriculum_prep != null) {
            if (printLegacy) {
                printCheckList(printLegacy, curriculum.getId(), curriculum_prep.getId());
            } else {
                printCheckList(printLegacy, curriculum_prep.getId(), curriculum_prep.getId());
            }
        } else {
            printCheckList(printLegacy, curriculum.getId(), null);
        }
    }

    private void printCheckList(Boolean printLegacy, Integer curriculum_ID, Integer prep_id) {
        PrintChecklist printCheckList = new PrintChecklist();
        printCheckList.printLegacy = printLegacy;
        printCheckList.CICT_id = CURRENT_STUDENT.getCict_id();
        printCheckList.CURRICULUM_id = curriculum_ID;
        printCheckList.setOnStart(onStart -> {
            GenericLoadingShow.instance().show();
        });
        printCheckList.setOnSuccess(onSuccess -> {
            GenericLoadingShow.instance().hide();
            if (prep_id == null) {
                Notifications.create().title("Please wait, we're nearly there.")
                        .text("Printing the Checklist.").showInformation();
            } else {
                printCheckList(printLegacy, prep_id, null);
            }
        });
        printCheckList.setOnCancel(onCancel -> {
            GenericLoadingShow.instance().hide();
            Notifications.create().title("Cannot Produce a Checklist")
                    .text("Something went wrong, sorry for the inconvinience.").showWarning();
        });
        if (!printLegacy) {
            printCheckList.setDocumentFormat(ReportsUtility.paperSizeChooser(this.getStage()));
        }

        printCheckList.transact();
    }

    private void onRemove() {
        String btn = btn_remove_student.getText(),
                title, message, header;
        Integer activeness = 0;
        if (btn.equalsIgnoreCase("Remove Student")) {
            activeness = 0;
            title = "Removed Successfully";
            message = "Student " + this.CURRENT_STUDENT.getId()
                    + " is successfully removed.";
            btn = "Restore Student";
            header = "Remove Student";
        } else {
            activeness = 1;
            title = "Restored Successfully";
            message = "Student " + this.CURRENT_STUDENT.getId()
                    + " is successfully restored.";
            btn = "Remove Student";
            header = "Restore Student";
        }
        int res = Mono.fx().alert()
                .createConfirmation()
                .setHeader(header)
                .setMessage("Are you sure you want to " + header.toLowerCase() + " "
                        + CURRENT_STUDENT.getId() + "?")
                .confirmYesNo();
        if (res == -1) {
            return;
        }
        this.CURRENT_STUDENT.setActive(activeness);
        if (Database.connect().student().update(this.CURRENT_STUDENT)) {
            if (asMap != null) {
                this.asMap.setActive(activeness);
                Database.connect().student().update(this.asMap);
            }
            Notifications.create().darkStyle()
                    .title(title)
                    .text(message).showInformation();
            btn_remove_student.setText(btn);
            this.setValues();
        } else {
            Notifications.create().darkStyle()
                    .title("Nothing Happened")
                    .text("Can't remove student at this moment.").showWarning();
        }
    }

    private void loadHistory() {
        ArrayList<StudentCourseHistoryMapping> results = Mono.orm().newSearch(Database.connect().student_course_history())
                .eq(DB.student_course_history().student_id, CURRENT_STUDENT.getCict_id())
                .active().all();
        if (results == null) {

        } else {
            createHistoryTable(results);
        }
    }

    /**
     * Changes the current college of the student.
     */
    private void showCollegeChanger() {
        ChooserHome.showCICT(true);
        String selectedCollege = ChooserHome.open();
        if (selectedCollege == null || selectedCollege.equalsIgnoreCase("cancel")) {
            // operation was cancelled
            return;
        }

        if (this.CURRENT_STUDENT.getCollege().equalsIgnoreCase(selectedCollege)) {
            Mono.fx().snackbar().showInfo(application_root, "No Changes Were Made.");
            return;
        }

        // save changes
        this.CURRENT_STUDENT.setCollege(selectedCollege);
        boolean updated = Database.connect().student().update(CURRENT_STUDENT);
        if (updated) {
            this.setValues();
            Mono.fx().snackbar().showSuccess(application_root, "Information was updated.");
        } else {
            Mono.fx().snackbar().showInfo(application_root, "Cannot Save Changes, Please Try Again.");
        }
    }

    private final String KEY_MORE_INFO = "MORE_INFO";

    private void createHistoryTable(ArrayList<StudentCourseHistoryMapping> results) {
        vbox_history.getChildren().clear();
        SimpleTable tblHistory = new SimpleTable();
        for (StudentCourseHistoryMapping each : results) {
            SimpleTableRow row = new SimpleTableRow();
            row.setRowHeight(83.0);
            CourseHistoryRow rowFX = M.load(CourseHistoryRow.class);
            Label lbl_name_current = rowFX.getLbl_current_curriculum();
            Label lbl_name_current_date = rowFX.getLbl_current_curriculum_date();
            Label lbl_name_prev = rowFX.getLbl_prevoius_curriculum();
            Label lbl_name_prev_date = rowFX.getLbl_prevoius_curriculum_date();

            String current = getCurriculumName(each.getCurriculum_id());
            lbl_name_current.setText((current == null ? "NONE" : current));
            lbl_name_current_date.setText(each.getCurriculum_assigment() == null ? "NOT SET" : "" + each.getCurriculum_assigment());
            String prep = getCurriculumName(each.getPrep_id());
            if (prep == null) {
                rowFX.getVbox_prep().setVisible(false);
            } else {
                lbl_name_prev.setText(prep);
                lbl_name_prev_date.setText(each.getPrep_assignment() == null ? "NOT SET" : "" + each.getPrep_assignment());
            }
            SimpleTableCell cellParent = new SimpleTableCell();
            cellParent.setResizePriority(Priority.ALWAYS);
            cellParent.setContentAsPane(rowFX.getApplicationRoot());

            row.addCell(cellParent);

            tblHistory.addRow(row);
        }

        // table view
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(tblHistory);
        simpleTableView.setFixedWidth(true);
        // attach to parent variable name in scene builder
        simpleTableView.setParentOnScene(vbox_history);
    }

    private String getCurriculumName(Integer id) {
        if (id == null) {
            return null;
        }
        CurriculumMapping curriculum = Database.connect().curriculum().getPrimary(id);
        if (curriculum == null) {
            return null;
        } else {
            return curriculum.getName();
        }
    }

    //--------------------------------------------------------------------------
    private void onShiftCourse() {
        // check if evaluated
        EvaluationMapping eMap = Mono.orm().newSearch(Database.connect().evaluation())
                .eq(DB.evaluation().ACADTERM_id, SystemProperties.instance().getCurrentAcademicTerm().getId())
                .eq(DB.evaluation().STUDENT_id, this.CURRENT_STUDENT.getCict_id())
                .active(Order.desc(DB.evaluation().id)).first();
        if (eMap != null) {
            Mono.fx().alert().createWarning()
                    .setHeader("Already Evaluated")
                    .setMessage("The student is already evaluated in the current semester and cannot proceed to shifting. Please revoke the evaluation first before continuing.")
                    .show();
            return;
        }

        CurriculumMapping selected = selectCurriculum("Are you sure you want to shift the student's course?");
        if (selected == null) {
            return;
        }
        //----------------------------------------------------------------------
        System.out.println(selected.getName());
        //----------------------------
        this.validateCurriculum(selected);
    }

    public void validateCurriculum(CurriculumMapping curriculumSelected) {

        // check if the curriculum is in the course history
        // if so, reactivate the grades related to the course history
        StudentCourseHistoryMapping schMap = Mono.orm().newSearch(Database.connect().student_course_history())
                .eq(DB.student_course_history().curriculum_id, curriculumSelected.getId())
                .eq(DB.student_course_history().student_id, CURRENT_STUDENT.getCict_id())
                .active(Order.desc(DB.student_course_history().id)).first();
        if (schMap != null) {
            int res = Mono.fx().alert().createConfirmation()
                    .setMessage("This curriculum is already taken by this student. Do you want to retain the previous grade?")
                    .confirmYesNo();
            Boolean retain = false;
            if (res == 1) {
                retain = true;
            }
            this.processGrades(curriculumSelected.getId(), retain, false, schMap.getId());
            return;
        }

        ArrayList<CurriculumSubjectMapping> subjects = Mono.orm().newSearch(Database.connect().curriculum_subject())
                .eq(DB.curriculum_subject().CURRICULUM_id, curriculumSelected.getId())
                .active().all();
        if (subjects == null) {
            return;
        }
        // ------------------------------
        // check the grade for each subject found
        // ------------------
        boolean hasGrade = false;
        for (CurriculumSubjectMapping subject : subjects) {
            GradeMapping grade = Mono.orm().newSearch(Database.connect().grade())
                    .eq(DB.grade().STUDENT_id, this.CURRENT_STUDENT.getCict_id())
                    .eq(DB.grade().SUBJECT_id, subject.getSUBJECT_id())
                    .active(Order.desc(DB.grade().id)).first();
            if (grade == null) {
            } else {
                hasGrade = true;
                break;
            }
        }
        Boolean retain = false;
        if (hasGrade) {
            int res = Mono.fx().alert().createConfirmation()
                    .setMessage("Some subjects in this curriculum are already taken by this student. Do you want to retain them?")
                    .confirmYesNo();
            if (res == 1) {
                retain = true;
            }
        }
        this.processGrades(curriculumSelected.getId(), retain, false, null);
    }

    private boolean retain(ArrayList<GradeMapping> currentGrades) {
        //------------------------------------------------------------------
        Session currentSession = Mono.orm().openSession();
        // start your transaction
        org.hibernate.Transaction dataTransaction = currentSession.beginTransaction();
        for (GradeMapping currentGrade : currentGrades) {
            GradeMapping retainedGrade = new GradeMapping();
            retainedGrade.setACADTERM_id(currentGrade.getACADTERM_id());
            retainedGrade.setActive(1);
            retainedGrade.setCourse_reference(currentGrade.getCourse_reference());
            retainedGrade.setCreated_by(currentGrade.getCreated_by());
            retainedGrade.setCreated_date(currentGrade.getCreated_date());
            retainedGrade.setCredit(currentGrade.getCredit());
            retainedGrade.setCredit_method(currentGrade.getCredit_method());
            retainedGrade.setInc_expire(currentGrade.getInc_expire());
            retainedGrade.setPosted(currentGrade.getPosted());
            retainedGrade.setPosted_by(currentGrade.getPosted_by());
            retainedGrade.setPosting_date(currentGrade.getPosting_date());
            retainedGrade.setRating(currentGrade.getRating());
            retainedGrade.setReason_for_update(currentGrade.getReason_for_update());
            retainedGrade.setReferrence_curriculum(currentGrade.getReferrence_curriculum());
            retainedGrade.setRemarks(currentGrade.getRemarks());
            retainedGrade.setSTUDENT_id(currentGrade.getSTUDENT_id());
            retainedGrade.setSUBJECT_id(currentGrade.getSUBJECT_id());
            retainedGrade.setUpdated_by(currentGrade.getUpdated_by());
            retainedGrade.setUpdated_date(currentGrade.getUpdated_date());
            int gradeID = Database.connect().grade()
                    .transactionalInsert(currentSession, retainedGrade);
            if (gradeID <= 0) {
                dataTransaction.rollback();
                return false;
            }
        }
        dataTransaction.commit();
        return true;
    }

    private void processGrades(Integer curriculum_id/*, Integer course_ref*/, Boolean retain, Boolean alreadyTaken, Integer prevCourseRef_id) {
        TransactGrades transact = new TransactGrades();
        transact.student_id = CURRENT_STUDENT.getCict_id();
        transact.curriculum_id = curriculum_id;
        transact.retain = retain;
        transact.alreadyTaken = alreadyTaken;
        transact.prevCourseRef_id = prevCourseRef_id;
        /*transact.course_reference = course_ref;*/
        transact.whenCancelled(() -> {
            Notifications.create()
                    .title("Request Cancelled")
                    .text(transact.log)
                    .showWarning();
        });
        transact.whenFailed(() -> {
            Notifications.create()
                    .title("Request Failed")
                    .text("Something went wrong.")
                    .showError();
        });
        transact.whenSuccess(() -> {
            StudentInformation currentStudent = new StudentInformation(Database.connect().student().getPrimary(CURRENT_STUDENT.getCict_id()));
            if (currentStudent.getStudentMapping() == null) {
                return;
            }

            this.setStudentInformation(currentStudent);
            this.setValues();
            Notifications.create()
                    .title("Successfully Shifted")
                    .text("Student's curriculum is changed"
                            + "\n successfully.")
                    .showInformation();
        });
        transact.transact();
    }

    class TransactGrades extends Transaction {

        public Integer curriculum_id;
        public Integer student_id;
        //----------------------------------------------------------------------
        public Integer course_reference; // must be retrieved within transaction
        //----------------------------------------------------------------------

        public String log;

        public Boolean retain;

        public Boolean alreadyTaken = false;
        public Integer prevCourseRef_id;

        @Override
        protected boolean transaction() {
            //------------------------------------------------------------------
            Session currentSession = Mono.orm().openSession();
            // start your transaction
            org.hibernate.Transaction dataTransaction = currentSession.beginTransaction();
            //------------------------------------------------------------------
            // include the insertion of the course history in the transaction
            StudentCourseHistoryMapping schMap = new StudentCourseHistoryMapping();
            schMap.setActive(1);
            schMap.setCurriculum_assigment(Mono.orm().getServerTime().getDateWithFormat());
            schMap.setCurriculum_id(curriculum.getId());
            schMap.setPrep_assignment(CURRENT_STUDENT.getPrep_assignment());
            schMap.setPrep_id(CURRENT_STUDENT.getPREP_id());
            schMap.setStudent_id(CURRENT_STUDENT.getCict_id());
            int courseHistoryID = Database.connect().student_course_history()
                    .transactionalInsert(currentSession, schMap);

            //------------------------------------------------------------------
            // check if history was properly inserted
            if (courseHistoryID <= 0) {
                dataTransaction.rollback();
                log = "Cannot Insert Course History";
                return false; // cancel the transaction
            } else {
                // assign this history ID
                this.course_reference = courseHistoryID;
            }
            //------------------------------------------------------------------
            ArrayList<GradeMapping> grades = Mono.orm().newSearch(Database.connect().grade())
                    .eq(DB.grade().STUDENT_id, student_id)
                    .active()
                    .all();
            //------------------------------------------------------------------
            // updating grades with course reference

            // --------------------------------
            // if retain is true, retain it
            if (grades != null) {
                for (GradeMapping grade : grades) {
                    if (retain) {
                        GradeMapping retainThis = this.getRetainedGradeMap(grade);
                        int gradeID = Database.connect().grade()
                                .transactionalInsert(currentSession, retainThis);
                        if (gradeID <= 0) {
                            dataTransaction.rollback();
                            log = "Grade is not properly retained.";
                            System.out.println(log);
                            return false;
                        }
                    }
                    grade.setActive(0);
                    grade.setUpdated_by(CollegeFaculty.instance().getFACULTY_ID());
                    grade.setUpdated_date(Mono.orm().getServerTime().getDateWithFormat());
                    grade.setReason_for_update("Student was shifted and this grade was removed".toUpperCase());
                    grade.setCourse_reference(course_reference);
                    if (!Database.connect().grade().transactionalSingleUpdate(currentSession, grade)) {
                        // if errors occured during temporary insert
                        dataTransaction.rollback();
                        log = "Curriculum is not properly updated.";
                        System.out.println(log);
                        return false;
                    }
                }
            }
            //------------------------------------------------------------------
            StudentMapping student = Database.connect().student().getPrimary(student_id);
            if (student != null) {
                student.setCURRICULUM_id(curriculum_id);
                student.setCurriculum_assignment(Mono.orm().getServerTime().getDateWithFormat());

                if (!Database.connect().student().transactionalSingleUpdate(currentSession, student)) {
                    dataTransaction.rollback();
                    log = "Cannot Update Curriculum For the moment.";
                    System.out.println(log);
                    return false;
                }
            } else {
                dataTransaction.rollback();
                log = "Cannot retrieve student data.";
                System.out.println(log);
                return false;
            }
            //------------------------------------------------------------------
            // reactivate if alreadyTaken is true
            //-----------------------------------
            if (alreadyTaken) {
                ArrayList<GradeMapping> previousGrades = Mono.orm().newSearch(Database.connect().grade())
                        .eq(DB.grade().course_reference, prevCourseRef_id)
                        .eq(DB.grade().STUDENT_id, student_id)
                        .execute().all();
                if (previousGrades != null) {
                    for (GradeMapping previousGrade : previousGrades) {
                        previousGrade.setActive(1);
                        if (!Database.connect().grade().transactionalSingleUpdate(currentSession, previousGrade)) {
                            dataTransaction.rollback();
                            log = "Cannot Update Grade For Now.";
                            System.out.println(log);
                            return false;
                        }
                    }
                }
            }

            dataTransaction.commit();
            return true;
        }

        private GradeMapping getRetainedGradeMap(GradeMapping currentGrade) {
            GradeMapping retainedGrade = new GradeMapping();
            retainedGrade.setACADTERM_id(currentGrade.getACADTERM_id());
            retainedGrade.setActive(1);
            retainedGrade.setCourse_reference(currentGrade.getCourse_reference());
            retainedGrade.setCreated_by(currentGrade.getCreated_by());
            retainedGrade.setCreated_date(currentGrade.getCreated_date());
            retainedGrade.setCredit(currentGrade.getCredit());
            retainedGrade.setCredit_method(currentGrade.getCredit_method());
            retainedGrade.setInc_expire(currentGrade.getInc_expire());
            retainedGrade.setPosted(currentGrade.getPosted());
            retainedGrade.setPosted_by(currentGrade.getPosted_by());
            retainedGrade.setPosting_date(currentGrade.getPosting_date());
            retainedGrade.setRating(currentGrade.getRating());
            retainedGrade.setReason_for_update(currentGrade.getReason_for_update());
            retainedGrade.setReferrence_curriculum(currentGrade.getReferrence_curriculum());
            retainedGrade.setRemarks(currentGrade.getRemarks());
            retainedGrade.setSTUDENT_id(currentGrade.getSTUDENT_id());
            retainedGrade.setSUBJECT_id(currentGrade.getSUBJECT_id());
            retainedGrade.setUpdated_by(currentGrade.getUpdated_by());
            retainedGrade.setUpdated_date(currentGrade.getUpdated_date());
            return retainedGrade;
        }

    }

//    private Integer insertCourseHistory(CurriculumMapping curriculum) {
//        StudentCourseHistoryMapping schMap = new StudentCourseHistoryMapping();
//        schMap.setActive(1);
//        schMap.setCurriculum_assigment(Mono.orm().getServerTime().getDateWithFormat());
//        schMap.setCurriculum_id(curriculum.getId());
//        schMap.setPrep_assignment(CURRENT_STUDENT.getPrep_assignment());
//        schMap.setPrep_id(CURRENT_STUDENT.getPREP_id());
//        schMap.setStudent_id(CURRENT_STUDENT.getCict_id());
//        return Database.connect().student_course_history().insert(schMap);
//    }
    private CurriculumMapping selectCurriculum(String message) {
        CurriculumChooser curriculumChooser = M.load(CurriculumChooser.class);
        curriculumChooser.setStudentID(CURRENT_STUDENT.getCict_id());
        curriculumChooser.onDelayedStart(); // do not put database transactions on startUp
        //----------------------------------------------------------------------
        try {
            System.out.println("Stage Recycled. ^^v");
            curriculumChooser.getCurrentStage().showAndWait();
        } catch (NullPointerException e) {
            Stage a = curriculumChooser.createChildStage(super.getStage());
            a.initStyle(StageStyle.UNDECORATED);
            a.showAndWait();
        }
        //----------------------------------------------------------------------

        CurriculumMapping selected = curriculumChooser.getSelected();
        if (selected != null && message != null) {
            int res = Mono.fx().alert().createConfirmation()
                    .setMessage(message).confirmYesNo();
            if (res == -1) {
                return null;
            }
        }
        if (selected == null) {
            return null;
        }
        if (selected.getId().equals(curriculum.getId())) {
            Notifications.create()
                    .title("Nothing Happened")
                    .text("Student is already in this"
                            + "\n curriculum.")
                    .showError();
            return null;
        }

        //-------------------------------------------
        // consiquent curriculum, check if all grades are taken and passed
        if (selected.getLadderization_type().equalsIgnoreCase(CurriculumConstants.TYPE_CONSEQUENT)) {
            int res = Mono.fx().alert().createConfirmation()
                    .setMessage("You are shifting to a consequent curriculum. "
                            + "This requires all the subjects in the current curriculum"
                            + " to be taken and passed. Do you still want to continue?")
                    .confirmYesNo();
            if (res == 1) {
                if (!canTakeConsequent(selected)) {
                    Notifications.create().darkStyle()
                            .title("Cannot Shift Course")
                            .text("Consequent curriculums requires a"
                                    + "\ncertain curriculum to make it"
                                    + "\navailable for shifting.")
                            .showWarning();
                    return null;
                }
            } else {
                return null;
            }
        }
        //------------------------------------------
        return selected;
    }

    private boolean canTakeConsequent(CurriculumMapping selected) {
        ArrayList<CurriculumPreMapping> prereqs = Mono.orm().newSearch(Database.connect().curriculum_pre())
                .eq(DB.curriculum_pre().curriculum_id_get, selected.getId())
                .active().all();
        if (prereqs == null) {
            return true;
        }
        Integer student_prep_id = CURRENT_STUDENT.getPREP_id();
        for (CurriculumPreMapping prereq : prereqs) {
            if (student_prep_id != null) {
                if (student_prep_id.equals(prereq.getCurriculum_id_req())) {
                    return true;
                }
            }
            StudentCourseHistoryMapping schMap = Mono.orm().newSearch(Database.connect().student_course_history())
                    .eq(DB.student_course_history().prep_id, prereq.getCurriculum_id_req())
                    .eq(DB.student_course_history().student_id, CURRENT_STUDENT.getCict_id())
                    .active(Order.desc(DB.student_course_history().id)).first();
            if (schMap != null) {
                return true;
            }
        }
        return false;
    }

    private void printProfile() {
        PrintStudentProfile profile = new PrintStudentProfile();
        profile.CICT_id = CURRENT_STUDENT.getCict_id();
        //----------------------------------------------------------------------
        // proper call back do not put disabling before this method call
        // always disable a button inside whenStarted to assure that the task had started.
        profile.whenStarted(() -> {
            // disable + wait cursor (Optional)
            btn_view_profile.setDisable(true);
            super.cursorWait();
        });
        // Cancel
        profile.whenCancelled(() -> {
            //------------------------------------------------------------------
            if (profile.hasNoProfile()) {
                Notifications.create()
                        .title("No Profile")
                        .text("The student has not yet submitted a profile")
                        .showInformation();
                return;
            }
            //------------------------------------------------------------------

            Notifications.create()
                    .title("Something went wrong.")
                    .text("Student not found.")
                    .showInformation();
        });
        // Failed
        profile.whenFailed(() -> {
            Notifications.create()
                    .title("Request Failed")
                    .text("Please try again later.")
                    .showInformation();
        });
        // If everything seems right
        // Success
        profile.whenSuccess(() -> {
            Notifications.create()
                    .title("Printing Student Profile")
                    .text("Please wait a moment.")
                    .showInformation();
        });
        // What Ever Happens
        profile.whenFinished(() -> {
            btn_view_profile.setDisable(false);
            super.cursorDefault();
        });
        profile.transact();
    }

    private void printDeficiency() {
        PrintDeficiency print = new PrintDeficiency();
        print.CICT_id = CURRENT_STUDENT.getCict_id();
        //----------------------------------------------------------------------
        print.whenStarted(() -> {
            btn_view_deficiency.setDisable(true);
            super.cursorWait();
        });
        print.whenCancelled(() -> {
            Notifications.create()
                    .title("Request Cancelled")
                    .text(print.getMessage()
                            + "\nSorry for the inconviniece.")
                    .showWarning();
        });
        print.whenFailed(() -> {
            Notifications.create()
                    .title("Request Failed")
                    .text("Something went wrong. Sorry for the inconviniece.")
                    .showInformation();
        });
        print.whenSuccess(() -> {
            btn_view_deficiency.setDisable(false);
            Notifications.create()
                    .title("Printing the Deficiency Report.")
                    .text("Please wait a moment.")
                    .showInformation();
        });
        print.whenFinished(() -> {
            btn_view_deficiency.setDisable(false);
            super.cursorDefault();
        });
        print.setDocumentFormat(ReportsUtility.paperSizeChooser(this.getStage()));
        //----------------------------------------------------------------------
        print.transact();
    }

    private void onVerify() {
        this.CURRENT_STUDENT.setVerified(1);
        this.CURRENT_STUDENT.setVerification_date(Mono.orm().getServerTime().getDateWithFormat());
        this.CURRENT_STUDENT.setVerfied_by(CollegeFaculty.instance().getFACULTY_ID());
        if (Database.connect().student().update(this.CURRENT_STUDENT)) {
            Notifications.create().title("Verified Successfully")
                    .text("Student Number: " + this.CURRENT_STUDENT.getId())
                    .showInformation();
            isVerified(true);
        }
    }

    private void onShowCredit() {
        String title = "Edit Grades";
        CreditController controller = new CreditController(this.CURRENT_STUDENT.getCict_id(), "CREDIT", "STUDENTS");
        Mono.fx().create()
                .setPackageName("org.cict.evaluation.student.credit")
                .setFxmlDocument("Credit")
                .makeFX()
                .setController(controller)
                .makeScene()
                .makeStageApplication()
                .stageResizeable(true)
                .stageMaximized(true)
                .stageShow();
    }

    private void isVerified(Boolean answer) {
        if (answer) {
            vbox_verify.setVisible(false);
            hbox_main_view.setDisable(false);
            lbl_verified_by.setText(FacultyUtility.getFacultyName(FacultyUtility.getFaculty(this.CURRENT_STUDENT.getVerfied_by()), FacultyUtility.NameFormat.SURNAME_FIRST));
            lbl_verified_date.setText(DateString.formatDate(this.CURRENT_STUDENT.getVerification_date(), DateString.TIME_FORMAT_2));
            btn_verify.setDisable(true);
        } else {
            hbox_main_view.setDisable(true);
            vbox_verify.setVisible(true);
        }
    }

    private AccountStudentMapping asMap;

    private void setValues() {
        try {
            lbl_firstname.setText(CURRENT_STUDENT.getFirst_name());
            lbl_lastname.setText(CURRENT_STUDENT.getLast_name());
            lbl_middlename.setText(CURRENT_STUDENT.getMiddle_name() == null ? "" : CURRENT_STUDENT.getMiddle_name());

            lbl_cictid.setText(CURRENT_STUDENT.getCict_id().toString());
            txt_firstname.setText(CURRENT_STUDENT.getFirst_name());
            txt_group.setText(CURRENT_STUDENT.get_group() == null ? "" : CURRENT_STUDENT.get_group().toString());
            txt_lastname.setText(CURRENT_STUDENT.getLast_name());
            txt_middlename.setText(CURRENT_STUDENT.getMiddle_name() == null ? "" : CURRENT_STUDENT.getMiddle_name());
            txt_section.setText(CURRENT_STUDENT.getSection() == null ? "" : CURRENT_STUDENT.getSection());
            txt_studnum.setText(CURRENT_STUDENT.getId());
            setComboBoxValue(CURRENT_STUDENT.getCampus() == null ? "" : CURRENT_STUDENT.getCampus(), cmb_campus);
            this.cmb_yrlvl.getSelectionModel().select(CURRENT_STUDENT.getYear_level() == null || CURRENT_STUDENT.getYear_level() == 1 ? 0 : CURRENT_STUDENT.getYear_level() - 1);

            Boolean verified = (this.CURRENT_STUDENT.getVerified() == null ? 0 : this.CURRENT_STUDENT.getVerified()) == 1;
            this.isVerified(verified);
            if (verified) {
                lbl_verified_by.setText(FacultyUtility.getFacultyName(FacultyUtility.getFaculty(this.CURRENT_STUDENT.getVerfied_by()), FacultyUtility.NameFormat.SURNAME_FIRST));
                lbl_verified_date.setText(DateString.formatDate(this.CURRENT_STUDENT.getVerification_date(), DateString.TIME_FORMAT_2));
            }

            if ((CURRENT_STUDENT.getGender() == null ? "" : CURRENT_STUDENT.getGender()).equalsIgnoreCase("MALE")) {
                rbtn_male.setSelected(true);
            } else if ((CURRENT_STUDENT.getGender() == null ? "" : CURRENT_STUDENT.getGender()).equalsIgnoreCase("FEMALE")) {
                rbtn_female.setSelected(true);
            } else {
                // if none was set in the database
                rbtn_male.setSelected(true);
            }

            // -----------------------------------------------------
            lbl_acad_prog.setText((acadProg == null ? "NONE" : acadProg.getName()));
            lbl_currriculum.setText((curriculum == null ? "NONE" : curriculum.getName()));

            if (this.CURRENT_STUDENT.getActive().equals(0)) {
                btn_remove_student.setText("Restore Student");
                vbox_removed.setVisible(true);
            } else {
                btn_remove_student.setText("Remove Student");
                vbox_removed.setVisible(false);
            }
            asMap = Database.connect().account_student().getPrimary(this.CURRENT_STUDENT.getCict_id());

            for (String[] strings : SectionHomeController.COLLEGE_LIST) {
                if (this.CURRENT_STUDENT.getCollege() != null && this.CURRENT_STUDENT.getCollege().equalsIgnoreCase(strings[0])) {
                    lbl_college.setText(strings[1]);
                }
            }
        } catch (NullPointerException f) {
            f.printStackTrace();
        }
    }

    public String removeExtraSpace(String str) {
        return MonoString.removeExtraSpace(str);
    }

    public void setComboBoxValue(String value, ComboBox cmb) {
        ObservableList cmb_items = cmb.getItems();
        for (int i = 0; i < cmb_items.size(); i++) {
            if (value.equalsIgnoreCase(cmb_items.get(i).toString())) {
                cmb.getSelectionModel().select(i);
                break;
            }
        }
    }

    public void back() {
        StudentHomeController controller = homeFX.getController();
        controller.onSearch();
        Animate.fade(application_root, 150, () -> {
            super.replaceRoot(homeFX.getApplicationRoot());
        }, homeFX.getApplicationRoot());
    }

    private void onSave() {
        update();
    }

    private void update() {
        //----------------------------------------------------------------------
        // create history object
        StudentDataHistory history = new StudentDataHistory();
        history.setCictID(this.CURRENT_STUDENT.getCict_id());
        history.setStudentNumber(this.CURRENT_STUDENT.getId());
        history.setLastName(this.CURRENT_STUDENT.getLast_name());
        history.setFirstName(this.CURRENT_STUDENT.getFirst_name());
        history.setMiddleName(this.CURRENT_STUDENT.getMiddle_name());
        history.setGender(this.CURRENT_STUDENT.getGender());
        history.setCampus(this.CURRENT_STUDENT.getCampus());
        history.setYearLevel(String.valueOf(this.CURRENT_STUDENT.getYear_level()));
        history.setSection(this.CURRENT_STUDENT.getSection());
        history.setGroup(String.valueOf(this.CURRENT_STUDENT.get_group()));
        if (this.CURRENT_STUDENT.getUpdated_by() == null) {
            history.setUpdatedby(this.CURRENT_STUDENT.getCreated_by());
            history.setUpdatedDate(this.CURRENT_STUDENT.getCreated_date());
        } else {
            history.setUpdatedby(this.CURRENT_STUDENT.getUpdated_by());
            history.setUpdatedDate(this.CURRENT_STUDENT.getUpdated_date());
        }
        //----------------------------------------------------------------------

        if (getValues()) {
            int res = this.validateStudent(this.CURRENT_STUDENT.getCict_id(),
                    txt_studnum.getText());
            switch (res) {
                case 0:
                    if (this.updateStudentData()) {
                        Notifications.create()
                                .title("Updated Successfully")
                                .text("Student successfully updated.")
                                .showInformation();
                        this.setValues();
                        //------------------------------------------------------
                        // create history if successfully evaluated
                        history.makeHistory();
                        //------------------------------------------------------
                    }
                    break;
            }
        }
    }

    private String studnum = "", lastname = "", firstname = "", middlename = "", campus = "", section = "", gender;
    private Integer yearLevel, group;

    private boolean getValues() {
        studnum = removeExtraSpace(txt_studnum.getText().toUpperCase());
        if (studnum == null ? false : studnum.isEmpty()) {
            Mono.fx().alert()
                    .createError()
                    .setHeader("Student Number")
                    .setMessage("Please enter the student number to proceed.")
                    .showAndWait();
            return false;
        }

        lastname = removeExtraSpace(txt_lastname.getText().toUpperCase());
        if (lastname == null ? false : lastname.isEmpty()) {
            Mono.fx().alert()
                    .createError()
                    .setHeader("Last Name")
                    .setMessage("Please enter the student last name to proceed.")
                    .showAndWait();
            return false;
        }

        firstname = removeExtraSpace(txt_firstname.getText().toUpperCase());
        if (firstname == null ? true : firstname.isEmpty()) {
            Mono.fx().alert()
                    .createError()
                    .setHeader("First Name")
                    .setMessage("Please enter the student first name to proceed.")
                    .showAndWait();
            return false;
        }

        middlename = removeExtraSpace(txt_middlename.getText().toUpperCase()) == null || removeExtraSpace(txt_middlename.getText().toUpperCase()).isEmpty() ? "" : removeExtraSpace(txt_middlename.getText().toUpperCase());

        gender = "";
        if (rbtn_male.isSelected()) {
            gender = "MALE";
        } else if (rbtn_female.isSelected()) {
            gender = "FEMALE";
        }

        try {
            campus = (cmb_campus.getSelectionModel().getSelectedItem().toString());
        } catch (NullPointerException a) {
            Mono.fx().alert()
                    .createError()
                    .setHeader("Campus")
                    .setMessage("Please select a campus to proceed.")
                    .showAndWait();
            return false;
        }
        try {
            String yrlvl = MonoString.removeExtraSpace(this.cmb_yrlvl.getSelectionModel().getSelectedItem().toString());
            yearLevel = (studentValues.getYearLevel(yrlvl)); // year lvl
        } catch (NullPointerException s) {
            Mono.fx().alert()
                    .createError()
                    .setHeader("Year Level")
                    .setMessage("Please select a student year level to proceed.")
                    .showAndWait();
            return false;
        }

        section = removeExtraSpace(txt_section.getText().toUpperCase()) == null || removeExtraSpace(txt_section.getText().toUpperCase()).isEmpty() ? "" : removeExtraSpace(txt_section.getText().toUpperCase());
        boolean invalidSection = false;
        try {
            Integer value = Integer.valueOf(section);
            if (value > 99 || value < 1) {
                invalidSection = true;
            }
        } catch (NumberFormatException e) {
            if (section.length() > 1) {
                invalidSection = true;
            }
        }
        if (invalidSection) {
            Mono.fx().alert()
                    .createError()
                    .setHeader("Section")
                    .setMessage("Please enter a valid section to proceed. "
                            + "Valid values are only 1-99 or A-Z.")
                    .showAndWait();
            return false;
        }

        group = (removeExtraSpace(txt_group.getText()) == null || removeExtraSpace(txt_group.getText()).isEmpty() ? null : 1);
        if (group != null && group.equals(1)) {
            boolean invalidGroup = false;
            try {
                group = (Integer.valueOf(removeExtraSpace(txt_group.getText())));
                if (!group.equals(1) && !group.equals(2)) {
                    invalidGroup = true;
                }
            } catch (NumberFormatException d) {
                invalidGroup = true;
            }
            if (invalidGroup) {
                Mono.fx().alert()
                        .createError()
                        .setHeader("Group")
                        .setMessage("Please enter a valid student group to proceed. "
                                + "Either one(1) or two(2) only.")
                        .showAndWait();
                return false;
            }
        }
        return true;
    }

    //------------------------------------------------
    //------------------------------------------------------------
    private final StudentValues sv = new StudentValues();

    public void setCmbYearLevel() {
        cmb_yrlvl.getItems().addAll(sv.getYearLevels());
        cmb_yrlvl.getSelectionModel().selectFirst();
        setSelectedYearLevel(cmb_yrlvl.getSelectionModel().getSelectedItem().toString());
    }

    public Integer selectedYearLevel;

    public void setSelectedYearLevel(String yrlvl) {
        selectedYearLevel = null;
        if (!yrlvl.equalsIgnoreCase(StudentValues.ALL)) {
            this.selectedYearLevel = sv.getYearLevel(yrlvl);
        }
    }

    public void setCmbCampus() {
        cmb_campus.getItems().add("Main");
        cmb_campus.getItems().add("Bustos");
        cmb_campus.getItems().add("Meneses");
        cmb_campus.getItems().add("Sarmiento");
        cmb_campus.getItems().add("Hagonoy");
        cmb_campus.getSelectionModel().selectFirst();;
    }

    public int validateStudent(Integer cict_id, String id) {
        if (findInactive(id)) {
            Mono.fx().alert()
                    .createWarning()
                    .setHeader("Content Restricted")
                    .setMessage("The student number you entered is already in the database but inactive.")
                    .show();
            return -1; //inactive
        } else if (checkExist(cict_id, id) != 0) {
            Mono.fx().alert()
                    .createWarning()
                    .setHeader("Student Exist")
                    .setMessage("The student with a student number of " + id
                            + " already exist.")
                    .show();
            System.out.println("@InfoStudent: Student exist");
            return -1; //exist
        } else {
            //update
            return 0;
        }
    }

    public int checkExist(Integer cict_id, String id) {
        StudentMapping student = Mono.orm().newSearch(Database.connect().student())
                .eq("id", id)
                .active()
                .first();
        if (student == null) {
            return 0;
        }
        if (Objects.equals(student.getCict_id(), cict_id)) {
            return 0;
        } else {
            return 1;
        }

    }

    public boolean findInactive(String id) {
        return !Mono.orm().newSearch(Database.connect().student())
                .eq("id", id)
                .inactive()
                .isEmpty();
    }

    public StudentMapping CURRENT_STUDENT = new StudentMapping();

    public boolean updateStudentData() {
        CURRENT_STUDENT.setId(studnum);
        CURRENT_STUDENT.setLast_name(lastname);
        CURRENT_STUDENT.setFirst_name(firstname);
        CURRENT_STUDENT.setMiddle_name(middlename);
        CURRENT_STUDENT.setGender(gender);
        CURRENT_STUDENT.setCampus(campus);
        CURRENT_STUDENT.setYear_level(yearLevel);
        CURRENT_STUDENT.setSection(section);
        CURRENT_STUDENT.set_group(group);
        Boolean res = Database.connect().student()
                .update(CURRENT_STUDENT);
        if (res) {
            System.out.println("@InfoStudent: UPDATE SUCCESS");
            return true;
        } else if (Objects.equals(res, Boolean.FALSE)) {
            Notifications.create()
                    .title("Nothing Happened")
                    .text("There is a referenced value violated "
                            + "in the values. Update is not yet possible.")
                    .showError();
        } else {
            Notifications.create()
                    .title("Connection Error")
                    .text("Please check your connectivity to the server.")
                    .showError();
        }
        return false;
    }

    public void setCmbGender(ComboBox cmb_gender) {
        cmb_gender.getItems().addAll(sv.getGenders());
        cmb_gender.getSelectionModel().selectFirst();
    }

    private void setImageView() {
        StudentProfileMapping spMap = null;
        if (this.CURRENT_STUDENT.getHas_profile().equals(1)) {
            spMap = Mono.orm().newSearch(Database.connect().student_profile())
                    .eq(DB.student_profile().STUDENT_id, this.CURRENT_STUDENT.getCict_id())
                    .active(Order.desc(DB.student_profile().id)).first();
        }
        String studentImage = (spMap == null ? null : spMap.getProfile_picture());
        if (studentImage == null
                || studentImage.isEmpty()
                || studentImage.equalsIgnoreCase("NONE")) {
            ImageUtility.addDefaultImageToFx(img_icon, 1, this.getClass());
        } else {
            ImageUtility.addImageToFX("temp/images/profile", "student_avatar", studentImage, img_icon, 1);
        }
    }

    private void onShowHistory() {
        StudentHistoryController controller = new StudentHistoryController(this.CURRENT_STUDENT,
                this.lbl_acad_prog.getText() + " | "
                + (this.CURRENT_STUDENT.getYear_level() == null ? "" : this.CURRENT_STUDENT.getYear_level()) + (this.CURRENT_STUDENT.getSection() == null ? "" : this.CURRENT_STUDENT.getSection())
                + (this.CURRENT_STUDENT.get_group() == null ? "" : "-G" + this.CURRENT_STUDENT.get_group())
                + " | " + this.lbl_currriculum.getText(), "STUDENTS");
        Mono.fx().create()
                .setPackageName("org.cict.evaluation.student.history")
                .setFxmlDocument("History")
                .makeFX()
                .setController(controller)
                .makeScene()
                .makeStageWithOwner(Mono.fx().getParentStage(application_root))
                .stageResizeable(true)
                .stageMaximized(true)
                .stageShow();
    }

}

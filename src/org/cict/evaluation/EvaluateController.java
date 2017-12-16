/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cict.evaluation;

import org.cict.evaluation.views.SubjectView;
import org.cict.evaluation.evaluator.Evaluator;
import org.cict.evaluation.evaluator.SaveEvaluation;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.display.ControllerFX;

import java.util.ArrayList;
import java.util.Objects;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import app.lazy.models.AcademicProgramMapping;
import app.lazy.models.AccountFacultyMapping;
import app.lazy.models.CurriculumMapping;
import app.lazy.models.CurriculumPreMapping;
import app.lazy.models.CurriculumRequisiteExtMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.EvaluationMapping;
import app.lazy.models.GradeMapping;
import app.lazy.models.LoadGroupMapping;
import app.lazy.models.LoadSectionMapping;
import app.lazy.models.MapFactory;
import app.lazy.models.StudentCourseHistoryMapping;
import app.lazy.models.StudentMapping;
import app.lazy.models.StudentProfileMapping;
import app.lazy.models.SubjectMapping;
import app.lazy.models.SystemOverrideLogsMapping;
import artifacts.ImageUtility;
import artifacts.StudentOverStay;
import com.itextpdf.text.Document;
import com.jhmvin.fx.async.CronThread;
import com.jhmvin.fx.async.SimpleTask;
import com.jhmvin.fx.async.Transaction;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.display.SceneFX;
import com.jhmvin.transitions.Animate;
import com.melvin.mono.fx.bootstrap.M;
import java.util.List;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.cict.GenericLoadingShow;
import org.cict.PublicConstants;
import org.cict.ThreadMill;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.cict.authentication.authenticator.SystemProperties;
import org.cict.evaluation.encoder.GradeEncoderController;
import org.cict.evaluation.evaluator.PrintChecklist;
import org.cict.evaluation.evaluator.SearchStudent;
import org.cict.evaluation.evaluator.SubjectValidation;
import org.cict.evaluation.moving_up.MovingUpController;
import org.cict.evaluation.sectionviewer.SectionsController;
import org.cict.evaluation.student.credit.CreditController;
import org.cict.evaluation.student.info.InfoStudentController;
import org.cict.evaluation.student.history.StudentHistoryController;
import org.cict.management.registrar.Registrar;
import org.cict.management.registrar.RevokeEvaluation;
import org.cict.reports.ReportsUtility;
import org.cict.reports.advisingslip.ChooseTypeController;
import org.cict.reports.deficiency.PrintDeficiency;
import org.controlsfx.control.Notifications;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import update.org.cict.controller.home.Home;
import update3.org.cict.CurriculumConstants;
import update3.org.cict.access.Access;
import update3.org.cict.access.SystemOverriding;
import update5.org.cict.student.layout.CurriculumChooser;

/**
 * FXML Controller class
 *
 * @author Jhon Melvin
 */
public class EvaluateController extends SceneFX implements ControllerFX {

    @FXML
    private AnchorPane anchor_evaluate;

    @FXML
    private AnchorPane anchor_right;

    @FXML
    private JFXButton btnFind;

    @FXML
    private TextField txtStudentNumber;

    @FXML
    private AnchorPane anchor_preview;

    @FXML
    private AnchorPane anchor_studentInfo;

    @FXML
    private Label lblName;

    @FXML
    private Label lblCourseSection;

    @FXML
    private JFXButton btn_studentOptions;

    @FXML
    private ScrollPane scroll_subjects;

    @FXML
    private JFXButton btnEvaluate;

    @FXML
    private Label lbl_subjectTotal;

    @FXML
    private Label lbl_unitsTotal;

    @FXML
    private VBox vbox_studentOptions;

    @FXML
    private JFXButton btnHistory;

    @FXML
    private JFXButton btn_checklist;

    @FXML
    private JFXButton btn_encoding;

    @FXML
    private JFXButton btn_deficiency;

    @FXML
    private JFXButton btnCreditUnits;

    @FXML
    private AnchorPane anchor_results;

    @FXML
    private HBox hbox_search;

    @FXML
    private HBox hbox_loading;

    @FXML
    private HBox hbox_none;

    @FXML
    private HBox hbox_already;

    @FXML
    private JFXButton btn_already_print;

    @FXML
    private JFXButton btn_already_evaluate;

    @FXML
    private JFXButton btn_winSection;

    @FXML
    private JFXButton btn_home;

    @FXML
    private AnchorPane anchor_main1;

    @FXML
    private VBox vbox_list;

    @FXML
    private ImageView img_profile;

    @FXML
    private Label lbl_total_queue;

    @FXML
    private VBox vbox_waiting_queue;

    
    public EvaluateController() {
        //
    }

    // Maximum allowed units
    private final static Double MAX_UNITS = PublicConstants.MAX_UNITS;

    private void log(Object message) {
        boolean logging = true;
        if (logging) {
            System.out.println("@EvaluationController: " + message.toString());
        }
    }

    //--------------------------------------------------------------------------
    // table
    private final VBox vbox_subjects = new VBox(4);
    // table holder
    private AnchorPane application_root;
    //--------------------------------------------------------------------------

    /**
     * Controller Initialization.
     */
    private boolean allowOverride = false;

    @Override
    public void onInitialization() {
        //----------------------------------------------------------------------
        application_root = anchor_evaluate;
        super.bindScene(application_root);
        //----------------------------------------------------------------------
        // Attach table to layout.
        vbox_subjects.prefWidthProperty().bind(scroll_subjects.widthProperty());
        scroll_subjects.setContent(vbox_subjects);
        scroll_subjects.setFitToWidth(true);
        scroll_subjects.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        //----------------------------------------------------------------------
        // initial values.
        this.anchor_preview.setVisible(false);
        this.anchor_results.setVisible(true);
        //----------------------------------------------------------------------
        // counts the total units and subject
        vbox_subjects.getChildren().addListener((ListChangeListener.Change<? extends Node> c) -> {
            onListChange(c);
        });
        // drage recieved when adding subjects using drag and drop
        scroll_subjects.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            Evaluator.mouseDropSubject(currentStudent, MAX_UNITS, unitCount, vbox_subjects, anchor_right);
        });

        Animate.fade(vbox_waiting_queue, 150, () -> {
            vbox_waiting_queue.setVisible(false);
            vbox_list.setVisible(false);
            vbox_waiting_queue.setVisible(true);
        }, vbox_waiting_queue, vbox_list);
        createQueueTable();

        allowOverride = Access.isGrantedIf(Access.ACCESS_LOCAL_REGISTRAR);
    }

    /**
     * Table Listener. Checks the contents of the evaluating subjects. updates
     * the total units and total subjects.
     *
     * @param c
     */
    private void onListChange(ListChangeListener.Change<? extends Node> c) {
        // watch the children of vbox to update subject count and total units
        // reset values
        int temp_subject = 0;
        double temp_unit = 0.0;
        try {
            for (Node node : c.getList()) {
                if (node instanceof SubjectView) {
                    // test if the child is a subject view
                    temp_subject++;
                    SubjectView sv = (SubjectView) node;
                    temp_unit += sv.units;
                }
            }
        } catch (Exception e) {
            System.err.println("@EvaluateController: ListChangeListener <error>");
            e.printStackTrace();
        }
        subjectCount = temp_subject;
        unitCount = temp_unit;

        // display values
        lbl_subjectTotal.setText(subjectCount.toString());
        lbl_unitsTotal.setText(unitCount.toString());
    }

    private void checkEvaluationService(Runnable runMe) {
        Integer evaluation_service = SystemProperties.instance().getCurrentAcademicTerm().getEvaluation_service();
        if (evaluation_service == null) {
            Mono.fx().alert().createWarning().setTitle("Checking Service")
                    .setHeader("Evaluation Service")
                    .setMessage("Cannot check the evaluation service. Please Try Again.")
                    .show();
        } else {
            switch (evaluation_service) {
                case 1:
                    runMe.run();
                    break;
                default:
                    Mono.fx().alert().createWarning().setTitle("Evaluation Service")
                            .setHeader("Evaluation is OFFLINE")
                            .setMessage("Evaluation Service is currently offline, no evaluation can be save at the moment")
                            .show();
                    Home.callHome(this);
                    break;
            }
        }
    }

    /**
     * Registered Events in the evaluation window.
     */
    @Override
    public void onEventHandling() {
        this.hideDropDownEvents();

        super.addClickEvent(btn_home, () -> {
            cronThreadQueue.stop();
            Home.callHome(this);
        });

        // show section viewer
        super.addClickEvent(this.btn_winSection, () -> {
            onShowSections();
        });

        // search student, this dependes on the value of the text field
        super.addClickEvent(btnFind, () -> {
            this.hideDropDown();
            checkEvaluationService(() -> {
                this.searchStudent();
            });
        });

        // enter key as click in btn_find will execute if button is enabled.
        Mono.fx().key(KeyCode.ENTER).release(anchor_evaluate, () -> {
            this.hideDropDown();
            if (!this.btnFind.isDisabled()) {
                this.searchStudent();
            }
        });

        //----------------------------------------------------------------------
        // vbox options pop up menu
        // show vbox pop up menu.
        super.addClickEvent(btn_studentOptions, () -> {
            vbox_studentOptions.setVisible(!vbox_studentOptions.isVisible());
        });
        super.addClickEvent(btn_encoding, () -> {
//            Mono.fx().snackbar().showInfo(application_root, "This feature is under construction.");
            this.onShowMovingUp();
        });
        super.addClickEvent(btnHistory, () -> {
            this.onShowHistory();
        });
        super.addClickEvent(btnCreditUnits, () -> {
            this.onShowInputMode();
        });
        super.addClickEvent(btn_checklist, () -> {
            printChecklist();
        });

        //----------------------------------------------------------------------
        // save student evaluation
        super.addClickEvent(btnEvaluate, () -> {
            this.hideDropDown();
            checkEvaluationService(() -> {
                if (Access.enterTransactionPin(super.getStage())) {
                    saveEvaluation();
                } else {
                    Mono.fx().snackbar().showError(application_root, "Transaction Denied");
                }
            });
        });

        //----------------------------------------------------------------------
        // Options when the student is already evaluated.
        super.addClickEvent(btn_already_print, () -> {
            // REPRINT ADVISING SLIP.
            this.hideDropDown();
            if (!Access.enterTransactionPin(this.getStage())) {
                return;
            }
            this.showChooseType(Evaluator.instance().getCurrentAcademicTerm().getId(), false);
            
            //---------------
            // for retention
            this.retentionChecker();
            //---------------
        });

        super.addClickEvent(btn_already_evaluate, () -> {
            // RE-EVALUATE Student.
            this.hideDropDown();
            checkEvaluationService(() -> {
                onRevokeEvaluation();
            });
        });
        //----------------------------------------------------------------------

        super.addClickEvent(btn_deficiency, () -> {
            this.printDeficiency();
        });
    }

    private void printDeficiency() {
        Document doc = ReportsUtility.paperSizeChooser(this.getStage());
        if (doc == null) {
            return;
        }
        PrintDeficiency print = new PrintDeficiency();
        print.CICT_id = this.currentStudent.getCict_id();
        //----------------------------------------------------------------------
        print.whenStarted(() -> {
            GenericLoadingShow.instance().show();
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
            Notifications.create()
                    .title("Printing the Deficiency Report.")
                    .text("Please wait a moment.")
                    .showInformation();
        });
        print.whenFinished(() -> {
            GenericLoadingShow.instance().hide();
            super.cursorDefault();
        });
        print.setDocumentFormat(doc);
        //----------------------------------------------------------------------
        if (ReportsUtility.savePrintLogs(this.currentStudent.getCict_id(), "DEFICIENCY REPORT", "EVALUATION", "INITIAL")) {
            print.transact();
        }
    }

    private void onShowMovingUp() {

        if (!Access.enterTransactionPin(this.getStage())) {
            Mono.fx().snackbar().showError(application_root, "Transaction Request Denied");
            return;
        }

        FetchCurriculumInfo fetch = new FetchCurriculumInfo();
        fetch.student = currentStudent;
        fetch.whenStarted(() -> {
            GenericLoadingShow.instance().show();
        });
        fetch.whenSuccess(() -> {
            GenericLoadingShow.instance().hide();
            if (!fetch.isLadderized()) {
                Notifications.create().title("Warning")
                        .text("This process is applicable only\n"
                                + "when the student is in ladderized curriculum.")
                        .showWarning();
                btn_encoding.setDisable(true);
            } else {
                ArrayList<CurriculumMapping> results = fetch.getCurriculumPre();
                if (results != null) {
                    this.onShowMoving_up(results);
                }
            }
        });
        fetch.whenCancelled(() -> {
            GenericLoadingShow.instance().hide();
            Notifications.create().title("Process Cancelled")
                    .text(fetch.getLog())
                    .showWarning();
            btn_encoding.setDisable(true);
        });
        fetch.whenFailed(() -> {
            GenericLoadingShow.instance().hide();
            Notifications.create().title("Process Failed")
                    .text("Something went wrong. Please"
                            + "\ntry again later.")
                    .showError();
        });
        fetch.transact();
    }

    private void onShowMoving_up(ArrayList<CurriculumMapping> results) {
        MovingUpController controller = new MovingUpController(currentStudent, results);
        Mono.fx().create()
                .setPackageName("org.cict.evaluation.moving_up")
                .setFxmlDocument("moving-up")
                .makeFX()
                .setController(controller)
                .makeScene()
                .makeStageWithOwner(Mono.fx().getParentStage(lblName))
                .stageResizeable(false)
                .stageTitle("Moving Up")
                .stageUndecorated(true)
                .stageShowAndWait();
        if (controller.isSaved()) {
            btn_encoding.setDisable(true);
            this.searchStudent();
        }
    }

    /**
     * Transaction for getting the curriculum of the student and checking if
     * ladderized or not. This will also give the curriculum_pre of the said
     * curriculum.
     *
     */
    class FetchCurriculumInfo extends Transaction {

        public StudentMapping student;

        private boolean ladderized = false;

        public boolean isLadderized() {
            return ladderized;
        }

        private ArrayList<CurriculumMapping> curriculum_pre = new ArrayList<>();

        public ArrayList<CurriculumMapping> getCurriculumPre() {
            return curriculum_pre;
        }

        private String log;

        public String getLog() {
            return log;
        }

        @Override
        protected boolean transaction() {
            if (student == null) {
                System.out.println("No student found.");
                log = "No student found.";
                return false;
            }
            if (student.getCURRICULUM_id() == null) {
                System.out.println("No curriculum id found.");
                log = "No curriculum found.";
                return false;
            }
            CurriculumMapping curriculum = Database.connect().curriculum().getPrimary(student.getCURRICULUM_id());
            if (curriculum == null) {
                System.out.println("No curriculum found.");
                log = "No curriculum found.";
                return false;
            }
            ladderized = ((curriculum.getLadderization().equalsIgnoreCase("YES")));
            if (ladderized) {
                ArrayList<CurriculumPreMapping> curriculum_pre_temp = Mono.orm().newSearch(Database.connect().curriculum_pre())
                        .eq(DB.curriculum_pre().curriculum_id_req, curriculum.getId())
                        .active().all();
                if (curriculum_pre_temp == null) {
                    System.out.println("No curriculum_pre found.");
                    log = "No available curriculum found"
                            + "\nfor moving up process.";
                    return false;
                }
                for (CurriculumPreMapping curriculum_pre_tem : curriculum_pre_temp) {
                    CurriculumMapping each = Database.connect().curriculum().getPrimary(curriculum_pre_tem.getCurriculum_id_get());
                    if (each == null) {
                        continue;
                    }
                    curriculum_pre.add(each);
                }
            }
            return true;
        }

        @Override
        protected void after() {
        }

    }

    /**
     * Changes the view of the evaluation window.
     *
     * @param view
     */
    private void setView(String view) {
        this.btnFind.setDisable(false);
        this.anchor_preview.setVisible(false);
        this.anchor_results.setVisible(false);
        this.hbox_loading.setVisible(false); // loading
        this.hbox_search.setVisible(false); // search
        this.hbox_none.setVisible(false); // no results
        this.hbox_already.setVisible(false);

        vbox_studentOptions.setVisible(false);
        //vbox_settings.setVisible(false);

        switch (view) {
            case "home":
                this.anchor_results.setVisible(true);
                this.hbox_search.setVisible(true); // search
                this.btnFind.setDisable(false);
                break;

            case "search":
                this.anchor_results.setVisible(true);
                this.hbox_loading.setVisible(true); // search
                this.btnFind.setDisable(true);
                break;

            case "no_results":
                this.anchor_results.setVisible(true);
                this.hbox_none.setVisible(true); // search
                break;

            case "preview":
                anchor_evaluate.setDisable(false);
                this.anchor_preview.setVisible(true);
                break;
            case "already":
                this.anchor_results.setVisible(true);
                this.hbox_already.setVisible(true); // search
                break;
        }
    }
    //--------------------------------------------------------------------------
    // since this process only caters one student at a time
    // is is more efficient to store current searches
    // student information cache
    private StudentMapping currentStudent;
    private AcademicProgramMapping studentProgram;
    private ArrayList<LoadGroupMapping> studentLoadGroup;
    private ArrayList<SubjectMapping> studentSubject;
    private LoadSectionMapping studentSection;
    // unused variables for student information
    private CurriculumMapping studentCurriculum;
    //--------------------------------------------------------------------------
    /**
     * This block is the process of searching the student and automatically
     * assigns subject that have a match with the sections available.
     */
    //--------------------------------------------------------------------------
    // required variables for search student function
    private Integer subjectCount = 0;
    private Double unitCount = 0.0;
    private boolean FLAG_ALREADY_EVALUATED;
    private boolean FLAG_CROSS_ENROLLEE;
    //--------------------------------------------------------------------------
    // show assistant values
    private Integer countSearch = 0;
    private Boolean FLAG_ASSISTANT_SHOW;
    //--------------------------------------------------------------------------

    /**
     * Assures that all values are re-initialized every search.
     */
    private void resetCache() {
        currentStudent = null;
        studentProgram = null;
        studentLoadGroup = null;
        studentSubject = null;
        studentSection = null;
        //
        subjectCount = 0;
        unitCount = 0.0;
        FLAG_ALREADY_EVALUATED = false;
        FLAG_CROSS_ENROLLEE = false;
        btn_encoding.setDisable(false);
        ThreadMill.searching = false;
    }

    /**
     * Finds the student indicated in the search box.
     */
    public void searchStudent() {
        this.resetCache();
        FLAG_ASSISTANT_SHOW = true;
        if (currentStudent != null) {
            if (txtStudentNumber.getText().equalsIgnoreCase(currentStudent.getId())) {
                countSearch = 1;
                if (countSearch == 1) {
                    FLAG_ASSISTANT_SHOW = false;
                }
            } else {
                countSearch = 0;
            }
        } else {
            countSearch = 0;
        }

        setView("search");

        SearchStudent search = new SearchStudent();
        search.studentNumber = txtStudentNumber.getText().trim();

        search.setOnSuccess(event -> {
            this.onSearchSuccess(search);
        });

        search.setOnFailure(event -> {
            setView("no_results");
        });

        search.setOnCancel(event -> {
            this.onSearchCancelled(search);
        });
        search.setRestTime(500);
        search.transact();
    }

    /**
     * Success Callback of search student. The following code will be executed
     * if the student was found without errors.
     *
     * @param search Task
     */
    private void onSearchSuccess(SearchStudent search) {
        //----------------------------------------------------------------------
        // Retrieve search results.
        this.FLAG_ALREADY_EVALUATED = search.isAlreadyEvaluated();
        this.currentStudent = search.getCurrentStudent();
        this.studentLoadGroup = search.getStudentLoadGroup();
        this.studentProgram = search.getStudentProgram();
        this.studentSection = search.getStudentSection();
        this.studentSubject = search.getStudentSubject();
        this.studentCurriculum = search.getStudentCurriculum();
        //----------------------------------------------------------------------
        // Test Results

        if (Objects.isNull(this.currentStudent)) {
            System.out.println("@EvaluateController: Search is Empty");
            setView("no_results");
            return;
        }

        if (FLAG_ALREADY_EVALUATED) {
            setView("already");
            return;
        }

        System.out.println("@EvaluateController: Search Success");

        //----------------------------------------------------------------------
        this.FLAG_CROSS_ENROLLEE = search.isCrossEnrollee();

        // run this if not a cross enrollee student
        if (!FLAG_CROSS_ENROLLEE) {

            //----------------------
            // if curriculum is obsolete, don't allow evaluation
            if (studentCurriculum != null) {
                if (studentCurriculum.getObsolete_term().equals(1)) {
                    Mono.fx().alert().createWarning()
                            .setHeader("Obsolete Curriculum")
                            .setMessage("The student is registered to an obsolete curriculum. Please reassign the student to a currently offered course to proceed evaluation.")
                            .show();
                    this.setView("home");
                    return;
                }
            }
            //-----------------------

            //------------------------------
            // check if overstaying
            if (StudentOverStay.check(currentStudent) && !allowOverStay) {
                this.setView("home");
                int res = Mono.fx().alert().createConfirmation()
                        .setHeader("Overstayed Student")
                        .setMessage("The student exceeded in the maximum of six (6) study years. Change the student's course to continue evaluation.")
                        .confirmCustom("Reassign Course", "Cancel Evaluation");
                if (res == -1) {
                    Notifications.create()
                            .title("Overstayed Student")
                            .text("Click here for more information.")
                            .onAction(pop -> {
                                this.goLang(SystemOverriding.EVAL_STUDENT_OVERSTAY);
                            })
                            .position(Pos.BOTTOM_RIGHT).showWarning();
                    return;
                } else {
                    if (!this.onShowCoursesOffered()) {
                        return;
                    }
                }
            }
            this.allowOverStay = false;
            //------------------------------

            if (!PublicConstants.DISABLE_ASSISTANCE) {
                if (FLAG_ASSISTANT_SHOW) {
                    showFirstAssistant();
                }
            }
            onShowCurricularLevel();
            if (!PublicConstants.DISABLE_ASSISTANCE) {
                showAssistant();
            }
        }
        
        //---------------
        // for retention
        this.retentionChecker();
        //---------------

        //----------------------------------------------------------------------
        // display the results.
        this.showPreview();
        setView("preview");
        this.vbox_list.setDisable(false);
    }

    /**
     * Canceled. callback of the search student transaction.
     *
     * @param search
     */
    private void onSearchCancelled(SearchStudent search) {
        // cancelled was called for new students that has null values,
        System.out.println("@EvaluateController: Search Failed");
        setView("no_results");
        StudentMapping student = search.getCurrentStudent();
        if (student != null) {
            currentStudent = showMissingInfo(student);
        }
    }

    /**
     * Print the checklist.
     */
//    private void printChecklist() {
//        // disallows cross enrollees to print a check list.
//        if (this.FLAG_CROSS_ENROLLEE) {
//            Mono.fx().snackbar().showInfo(application_root, "No Check List for Cross Enrollees");
//            return;
//        }
//
//        PrintChecklist printCheckList = new PrintChecklist();
//        printCheckList.CICT_id = currentStudent.getCict_id();
//        printCheckList.setOnStart(onStart -> {
//            GenericLoadingShow.instance().show();
//        });
//        printCheckList.setOnSuccess(onSuccess -> {
//            GenericLoadingShow.instance().hide();
//            Notifications.create().title("Please wait, we're nearly there.")
//                    .text("Printing the Checklist.").showInformation();
//        });
//        printCheckList.setOnCancel(onCancel -> {
//            GenericLoadingShow.instance().hide();
//            Notifications.create().title("Cannot Produce a Checklist")
//                    .text("Something went wrong, sorry for the inconvinience.").showWarning();
//        });
//        printCheckList.setOnFailure(onFailed -> {
//            GenericLoadingShow.instance().hide();
//            Notifications.create().title("Cannot Produce a Checklist")
//                    .text("Something went wrong, sorry for the inconvinience.").showError();
//            System.out.println("FAILED");
//        });
//        printCheckList.transact();
//    }
    private void printChecklist() {
        // disallows cross enrollees to print a check list.
        if (this.FLAG_CROSS_ENROLLEE) {
            Mono.fx().snackbar().showInfo(application_root, "No Check List for Cross Enrollees");
            return;
        }
        //----------------------------------------------------------------------
        // current curriculum
        CurriculumMapping curriculum = Database.connect()
                .curriculum().getPrimary(currentStudent.getCURRICULUM_id());

        if (curriculum == null) {
            System.err.println("Curriculum Was Not Found !");
            return;
        }
        // check if the curriculum is ladderized and it is consequent before trying to get the prep.
        CurriculumMapping curriculum_prep = null;
        if (curriculum.getLadderization_type().equalsIgnoreCase(CurriculumConstants.TYPE_CONSEQUENT)) {
            // get prep curriculum
            if (currentStudent.getPREP_id() == null) {
                System.err.println("Student Has No Preparatory. !");
                return;
            }
            curriculum_prep = Database.connect()
                    .curriculum().getPrimary(currentStudent.getPREP_id());
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
        printCheckList(printLegacy, curriculum.getId(), null);

    }

    private void printCheckList(Boolean printLegacy, Integer curriculum_ID, Integer prep_id) {
        PrintChecklist printCheckList = new PrintChecklist();
        printCheckList.printLegacy = printLegacy;
        printCheckList.CICT_id = currentStudent.getCict_id();
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

        if (ReportsUtility.savePrintLogs(this.currentStudent.getCict_id(), "CHECKLIST", "EVALUATION", "INITIAL")) {
            printCheckList.transact();
        }
    }

    private void hideDropDownEvents() {
        txtStudentNumber.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> {
            this.hideDropDown();
        });
        vbox_studentOptions.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent event) -> {
            this.hideDropDown();
        });
        anchor_studentInfo.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent event) -> {
            this.hideDropDown();
        });
    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    /**
     * Revoke The current evaluation of the student. based on the context of the
     * system requirements. only the Local Registrar and its constituents can
     * use this feature freely. evaluators are not allowed to use this feature
     * unless upon given proper permission.
     */
    private void onRevokeEvaluation() {

        /**
         * Confirmation
         */
        int res = Mono.fx().alert()
                .createConfirmation()
                .setHeader("Revoke Transaction")
                .setTitle("Confirmation")
                .setMessage("Are you sure you want to continue ?")
                .confirmYesNo();

        if (res != 1) {
            return;
        }
        /**
         * Only Local Registrar and Co-Registrars are allowed to re-evaluate.
         */
        boolean isAllowed = false;
        AccountFacultyMapping allowedUser = null;
        if (Access.isDeniedIfNotFrom(
                Access.ACCESS_LOCAL_REGISTRAR,
                Access.ACCESS_CO_REGISTRAR)) {
            /**
             * Check if the user was granted permission by the registrar.
             */
            allowedUser = Access.isAllowedToRevoke();
            if (allowedUser != null) {
                isAllowed = true;
            }
        } else {
            // allowed user
            isAllowed = true;
        }

        if (!isAllowed) {
            Mono.fx().snackbar().showError(application_root, "You Are Not Allowed To Re-evaluate Students.");
            return;
        }

        RevokeEvaluation revoked_evaluation = Registrar.instance().createRevokeEvaluation();
        revoked_evaluation.cict_id = currentStudent.getCict_id();
        if (allowedUser == null) {
            /**
             * the user is the registrar
             */
            revoked_evaluation.registrar_id = CollegeFaculty.instance().getFACULTY_ID();
        } else {
            revoked_evaluation.registrar_id = allowedUser.getFACULTY_id();
        }
        revoked_evaluation.academic_term = Evaluator.instance().getCurrentAcademicTerm().getId();

        /**
         * Events.
         */
        revoked_evaluation.whenStarted(() -> {
            GenericLoadingShow.instance().show();
        });
        revoked_evaluation.whenCancelled(() -> {
            // cancelled is called upon error.
        });
        revoked_evaluation.whenFailed(() -> {

        });
        revoked_evaluation.whenSuccess(() -> {

        });
        revoked_evaluation.whenFinished(() -> {
            GenericLoadingShow.instance().hide();
            if (revoked_evaluation.isAddedChange()) {
                Mono.fx().snackbar().showError(application_root, "Cannot re-evaluate students that undergone Adding/Changing Transactions.");
                return;
            }

            /**
             * Search the student after revoking.
             */
            setView("home");
            this.searchStudent();
        });

        revoked_evaluation.setRestTime(500);
        revoked_evaluation.transact();
    }

    /**
     * Call the section viewer.
     */
    private void onShowSections() {
        SectionsController sectionController = new SectionsController();
        Mono.fx().create()
                .setPackageName("org.cict.evaluation.sectionviewer")
                .setFxmlDocument("section_viewer")
                .makeFX()
                .setController(sectionController)
                .makeScene()
                .makeStage()
                .stageResizeable(false)
                .stageShow();
    }

    /**
     * Saves the evaluation.
     */
    private void saveEvaluation() {
        if (studentCurriculum != null) {
            //----------------------------------------------------------------------
            List<CurriculumRequisiteExtMapping> sub = CoRequisiteFilter.checkCoReqEval(vbox_subjects,
                    this.studentCurriculum.getId(),
                    this.currentStudent.getCict_id());
            //----------------------------------------------------------------------
            if (CoRequisiteFilter.checkCoRequisite(sub)) {
                return;
            }
        }
        //----------------------------------------------------------------------
        /**
         * if below minimum.
         */
        if (this.unitCount < PublicConstants.MIN_UNITS) {
            // ask if wants suggestions
            int choice = Mono.fx()
                    .alert()
                    .createConfirmation()
                    .setTitle("Evaluation System")
                    .setHeader("Units Below Minimum !")
                    .setMessage("The student will be evaluated under minimum load requirements. Do you want to add suggested subjects?")
                    .confirmCustom("Yes", "No, Continue Evaluating");
            // make chouces
            if (choice == 1) {
                SectionsController sectionController = new SectionsController();

                Mono.fx().create()
                        .setPackageName("org.cict.evaluation.sectionviewer")
                        .setFxmlDocument("section_viewer")
                        .makeFX()
                        .setController(sectionController)
                        .makeScene()
                        .makeStage()
                        .stageResizeable(false)
                        .stageShow();

                sectionController.searchCallBack("Suggestions For " + txtStudentNumber.getText().trim());

                return;
            }
        }

        /**
         * If no subjects are added.
         */
        if (this.subjectCount == 0) {
            Mono.fx()
                    .alert()
                    .createWarning()
                    .setTitle("Evaluation System")
                    .setHeader("No Subjects Are Added")
                    .setMessage("Sorry, but the student will not be evaluated without any subjects listed.")
                    .showAndWait();
            return;
        }

        /**
         * Save Evaluation.
         */
        // preapre values
        int student_id = this.currentStudent.getCict_id();
        int acad_term_id = Evaluator.instance().getCurrentAcademicTerm().getId();
        int faculty_id = CollegeFaculty.instance().getFACULTY_ID();

        // create task
        SaveEvaluation evaluateTask = Evaluator.instance().createSaveEvaluation();
        evaluateTask.studentID = student_id;
        evaluateTask.acadTermID = acad_term_id;
        evaluateTask.facultyID = faculty_id;
        ArrayList<SubjectView> toInsert = new ArrayList<>();
        // get subjects that are listed
        for (Node node : vbox_subjects.getChildren()) {
            if (node instanceof SubjectView) {
                toInsert.add((SubjectView) node);
            }
        }
        evaluateTask.subjects = toInsert;

        evaluateTask.whenStarted(() -> {
            GenericLoadingShow.instance().show();
        });
        evaluateTask.whenCancelled(() -> {
            Mono.fx().snackbar().showError(application_root, "Evaluation Was Cancelled.");
        });
        evaluateTask.whenFailed(() -> {
            Mono.fx().snackbar().showError(application_root, "Evaluation Failed.");
        });
        evaluateTask.whenSuccess(() -> {
            showChooseType(acad_term_id, true);
            this.allowOverStay = false;
        });
        evaluateTask.whenFinished(() -> {
            GenericLoadingShow.instance().hide();
            setView("home");
        });

        evaluateTask.setRestTime(500);
        evaluateTask.transact();
    }

    /**
     * Defines what type of student before printing the advising slip.
     *
     * @param acadTermID
     * @param isNew
     * @param evaluationID evaluationID to check if there is already a set print
     * Type.
     */
    private void showChooseType(Integer acadTermID, boolean isNew) {
        /**
         * Applied Changes. so that when the advising slip will be reprinted
         * there will be no need to select again the student type.
         */
        // check if the student is evaluated.
        EvaluationMapping evaluationMap = Mono.orm()
                .newSearch(Database.connect().evaluation())
                .eq("STUDENT_id", this.currentStudent.getCict_id())
                .eq("ACADTERM_id", SystemProperties.instance().getCurrentAcademicTerm().getId())
                .active()
                .first();

        // create print Type.
        ChooseTypeController controller = new ChooseTypeController(this.currentStudent.getId(), acadTermID);

        /**
         * check if already selected.
         */
        boolean printerExecuted = false;
        if (evaluationMap != null) {
            if (!evaluationMap.getPrint_type().equalsIgnoreCase("NOT_SET")) {
                /**
                 * There is already a defined print type in the evaluation.
                 */
                controller.print(evaluationMap.getPrint_type());
                printerExecuted = true;
            }
        } else {
            // there is no evaluation.
            // nothing to print.
            return;
        }

        /**
         * if the print was not called. show the chooser.
         */
        if (!printerExecuted) {
            Mono.fx().create()
                    .setPackageName("org.cict.reports.advisingslip")
                    .setFxmlDocument("choose-type")
                    .makeFX()
                    .setController(controller)
                    .makeScene()
                    .makeStageApplication()
                    .stageResizeable(false)
                    .stageCenter()
                    .stageShowAndWait();

            evaluationMap.setPrint_type(controller.getSelected() == null ? "NOT_SET" : controller.getSelected().toUpperCase());
            Database.connect().evaluation().update(evaluationMap);
        }

        if (controller.isPrinting()) {
            String text = "Printing Evaluation Slip";
            if (isNew) {
                text = "Evaluated Successfully, printing Evaluation Slip";
            }

            Notifications.create()
                    .title("Success (" + controller.getSelected() + " Student)")
                    .text(text)
                    .showInformation();
        }
    }

    /**
     * Initial evaluation of the student, when the student is added for the
     * first time in the system.
     */
    private StudentMapping showMissingInfo(StudentMapping student) {
        MissingInfoController controller = new MissingInfoController(student);
        Mono.fx().create()
                .setPackageName("org.cict.evaluation")
                .setFxmlDocument("missing-info")
                .makeFX()
                .setController(controller)
                .makeScene()
                .makeStageApplication()
                .stageResizeable(false)
                .stageCenter()
                .stageShowAndWait();
        return controller.getStudent();
    }

    private void showFirstAssistant() {
        anchor_evaluate.setDisable(true);
        setView("home");
        FirstAssistantController controller = new FirstAssistantController();
        Mono.fx().create()
                .setPackageName("org.cict.evaluation")
                .setFxmlDocument("assistant2")
                .makeFX()
                .setController(controller)
                .makeScene()
                .makeStageApplication()
                .stageResizeable(false)
                .stageUndecorated(true)
                .stageCenter()
                .stageShowAndWait();
    }

    private void showAssistant() {
        AssistantController controller = new AssistantController(currentStudent);
        Mono.fx().create()
                .setPackageName("org.cict.evaluation")
                .setFxmlDocument("assistant")
                .makeFX()
                .setController(controller)
                .makeScene()
                .makeStageApplication()
                .stageResizeable(false)
                .stageUndecorated(true)
                .stageCenter()
                .stageShowAndWait();
        currentStudent.setYear_level(controller.getNewYearLevel());
    }

    private void onShowCurricularLevel() {
        CurricularLevelController controller = new CurricularLevelController(this.currentStudent);
        Mono.fx().create()
                .setPackageName("org.cict.evaluation")
                .setFxmlDocument("curricular-level")
                .makeFX()
                .setController(controller)
                .makeScene()
                .makeStageApplication()
                .stageMinDimension(800.0, 400.0)
                .stageResizeable(false)
                .stageCenter()
                .stageShowAndWait();
    }

//    private void onEnrollmentTypeUnset() {
//        /**
//         * Code added 8/2/2017 ********************************** check if
//         * enrollment type is NOT_SET
//         */
//        if (this.currentStudent.getEnrollment_type().equalsIgnoreCase("not_set")) {
//            // first time to evaluate in the system
//            // know first if the grades are complete, if true saved it as REGULAR student
//            // else call EnrollmentTypePicker
//            System.out.println("@EvaluateController: ADDED: NOT SET");
////            this.verifyStudentIf("not_set");
//
//        } else if (this.currentStudent.getEnrollment_type().equalsIgnoreCase("regular")) {
//            System.out.println("@EvaluateController: ADDED: REGULAR");
////            this.verifyStudentIf("regular");
//
//        } else {
//            System.out.println("@EvaluateController: ADDED: ELSE");
//            setView("preview");
//            // FLAG_ASSISTANT_SHOW results
//            this.showPreview();
//            //System.out.println("@search: Section found - " + studentSection.getSection_name());
//        }
//        // end ***************************************************
//    }
    /**
     * If the student is existing it will automatically add the subjects if
     * sections are available.
     */
    private void showPreview() {
        SimpleTask previewTk = new SimpleTask("show-preview");
        previewTk.setTask(() -> {
            this.loadPreview();
            this.setImageView();
        });
        previewTk.whenStarted(() -> {
            // before displaying the subject.
        });
        previewTk.whenSuccess(() -> {
            log("EvaluateController: Success Loading Preview.");
        });
        previewTk.whenFailed(() -> {
            log("EvaluateController: Failed Loading Preview.");
        });

        previewTk.whenFinished(() -> {
            // done
            log("EvaluateController: Finished Loading Preview.");
        });
        previewTk.start();
    }

    /**
     * Load the student details including name and section.
     */
    private void loadStudentDetails() {
        //reset subject view
        Mono.fx().thread().wrap(() -> {
            //------------------------------------------------------------------
            // student's full name
            String studentName = this.currentStudent.getLast_name()
                    + ", "
                    + this.currentStudent.getFirst_name()
                    + " "
                    + (this.currentStudent.getMiddle_name() == null ? "" : this.currentStudent.getMiddle_name());
            this.lblName.setText(studentName);
            //------------------------------------------------------------------
            if (this.FLAG_CROSS_ENROLLEE) {
                this.lblCourseSection.setText("CROSS - ENROLLEE");
                // IF CROSS ENROLLEE DO NOT PROCEED BELOW CODE
                return;
            }

            try {
                // students section
                String section = this.currentStudent.getYear_level()
                        + " "
                        + this.currentStudent.getSection()
                        + " - G"
                        + this.currentStudent.get_group();
                currentSection = studentProgram.getCode() + " " + section;
                this.lblCourseSection.setText(this.studentProgram.getName() + " | " + section + " | " + this.studentCurriculum.getName());
            } catch (Exception e) {
                this.lblCourseSection.setText("No Data");
            }

            btn_encoding.setDisable((this.currentStudent.getPREP_id() != null));
        });
    }

    private static String currentSection;

    public static String getSection() {
        return currentSection;
    }

    private void loadPreview() {
        // load details
        this.loadStudentDetails();
        // clear table
        Platform.runLater(() -> {
            this.vbox_subjects.getChildren().clear();
        });

        log(" $showPreview: started");
        /**
         * If the student has no assigned sections or no matches have found.
         */
        if (Objects.isNull(this.studentLoadGroup) || Objects.isNull(this.studentSubject)) {
            // don't print anyting
        } else {
            /**
             * This section adds the subject of the student based on his/her
             * section assigned, if the class is existing the student will
             * automatically loaded with that section's subject
             */
            // add subjects
            // validates every subject before adding.
            if (this.studentLoadGroup.size() == this.studentSubject.size()) {
                log(" $showPreview: check ok");
                for (int x = 0; x < this.studentLoadGroup.size(); x++) {
                    isOkToAdd(x);
                }
            }

        }

        Mono.fx().thread().wrap(() -> {
            lbl_subjectTotal.setText(subjectCount.toString());
            lbl_unitsTotal.setText(unitCount.toString());
//            if(subjectCount.toString().equalsIgnoreCase("0")) {
//                Notifications.create().title("No Section Available")
//                        .text("No open section for the student's subject load.")
//                        .showInformation();
//            }
        });

    }

    /**
     * Verifies every subject.
     *
     * @param x from the loop of $showPreview
     */
    private void isOkToAdd(int x) {
        log(" $isOkToAdd: started");
        /**
         * Before creating the object for display it must be verified if it has
         * pre requisites.
         *
         */
        // validation object
        SubjectValidation sv = new SubjectValidation();
        sv.studentCictID = this.currentStudent.getCict_id();
        sv.loadGroupID = studentLoadGroup.get(x).getId();
        sv.loadSecID = studentLoadGroup.get(x).getLOADSEC_id();
        sv.subjectID = studentSubject.get(x).getId();

        sv.validate();

        log(" $isOkToAdd: verified");
        if (sv.isEligibleToTake()) {
            log(" $isOkToAdd: student is eligible");
            ifItsOkThenAdd(x, sv.getSectionWithFormat());
        }
//------------------------------------------------------------------------------
        /**
         * This segment of the code is used for multi threading validation but
         * the arrangement of subjects are jumbled.
         */
//        ValidateAddedSubject verificationTask = Evaluator.instance().createValidateAddedSubject();
//        verificationTask.studentCictID = this.currentStudent.getCict_id();
//        verificationTask.loadGroupID = studentLoadGroup.get(x).getId();
//        verificationTask.loadSecID = studentLoadGroup.get(x).getLOADSEC_id();
//        verificationTask.subjectID = studentSubject.get(x).getId();
//
//        verificationTask.setOnSuccess((WorkerStateEvent event) -> {
//            log(" $isOkToAdd: verified");
//            if (verificationTask.isEligibleToTake()) {
//                log(" $isOkToAdd: student is eligible");
//                ifItsOkThenAdd(x, verificationTask.getSectionWithFormat());
//            }
//        });
//
//        verificationTask.setOnCancel(cancel -> {
//
//        });
//
//        verificationTask.transact();
//------------------------------------------------------------------------------
    }

    /**
     * If validated add the subject to the table.
     */
    private void ifItsOkThenAdd(int x, String section) {
        log(" $isOkToAdd: adding subject");
        try {
            // Creating object
            SubjectView subjects = new SubjectView();
            subjects.title.setText(studentSubject.get(x).getDescriptive_title());
            subjects.code.setText(studentSubject.get(x).getCode());
            subjects.section.setText(section);
            subjects.units = studentSubject.get(x).getLab_units() + studentSubject.get(x).getLec_units();
            subjects.lec_units = studentSubject.get(x).getLec_units();
            subjects.lab_units = studentSubject.get(x).getLab_units();
            subjects.subjectID = studentSubject.get(x).getId();
            subjects.loadGroupID = studentLoadGroup.get(x).getId();
            subjects.loadSecID = studentLoadGroup.get(x).getLOADSEC_id();
            //event
            subjects.actionRemove.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
                int choice = Mono.fx().alert()
                        .createConfirmation()
                        .setHeader("Remove Subject")
                        .setMessage("Are you sure you want to remove the subject?")
                        .confirmYesNo();
                if (choice == 1) {
                    vbox_subjects.getChildren().remove(subjects);
                    Mono.fx()
                            .snackbar()
                            .showInfo(anchor_right, subjects.code.getText() + " Has Been Removed.");
                }
            });

            Mono.fx().thread().wrap(() -> {
                vbox_subjects.getChildren().add(subjects);
            });

        } catch (IndexOutOfBoundsException a) {
            log(" $isOkToAdd: IndexOutOfBoundsException");
        }
        log(" $isOkToAdd: added ------------------------------END");
    }

//    public void onShowEnrollmentTypePicker() {
//        EnrollmentTypePickerController controller = new EnrollmentTypePickerController(this.currentStudent, this.yearAndSemWithMissingRecord);
//        Mono.fx().create()
//                .setPackageName("org.cict.evaluation")
//                .setFxmlDocument("enrollment_type")
//                .makeFX()
//                .setController(controller)
//                .makeScene()
//                .makeStageWithOwner(Mono.fx().getParentStage(lblName))
//                .stageResizeable(false)
//                .stageShowAndWait();
//    }
//    private ArrayList<ArrayList<SubjectMapping>> checker;
//    private ArrayList<String> yearAndSemWithMissingRecord;
//    private void verifyStudentIf(String mode) {
////        if (mode.equalsIgnoreCase("regular")) {
////            this.updateYearLevelForRegular();
////        }
//        CheckGrade checkGrade = Evaluator.instance().createGradeChecker();
//        checkGrade.curriculumId = this.currentStudent.getCURRICULUM_id();
//        checkGrade.studentId = this.currentStudent.getCict_id();
//        checkGrade.studentYearLevel = this.currentStudent.getYear_level();
//
//        checkGrade.setOnSuccess(success -> {
//            try {
//                checker = checkGrade.getSubjectsWithNoGrade();
//                this.yearAndSemWithMissingRecord = checkGrade.getTitles();
//                if (!this.checker.isEmpty()) {
//                    // if mode is not_set, FLAG_ASSISTANT_SHOW enrollment type picker
//                    // if mode is regular, FLAG_ASSISTANT_SHOW missing records
//                    if (mode.equalsIgnoreCase("not_set")) {
//                        this.onShowEnrollmentTypePicker();
//                    } else if (mode.equalsIgnoreCase("regular")) {
////                        this.onShowMissingRecord();
//                    }
//                    System.out.println("@EvaluateController: HOME");
//                    setView("home");
//                }
//            } catch (NullPointerException a) {
//                System.out.println("@EvaluateController: CATCH NULL");
//                // if mode is not_set, update student into regular enrollment type 
//                if (mode.equalsIgnoreCase("not_set")) {
//                    this.currentStudent.setEnrollment_type("REGULAR");
//                    if (Database.connect().student().update(this.currentStudent)) {
//                        System.out.println("@EvaluateController: Student updated process success");
//                    }
////                    this.updateYearLevelForRegular();
//                }
//                setView("preview");
//                this.showPreview();
//            }
//        });
//
//        checkGrade.setOnCancel(onCancel -> {
//            this.onError();
//        });
//        checkGrade.setOnFailure(failure -> {
//            this.onError();
//        });
//        checkGrade.transact();
//    }
    private void onError() {
        Mono.fx().alert()
                .createWarning()
                .setHeader("Evaluation")
                .setMessage("We cannot process your request this moment. "
                        + "Sorry for the inconvinience.")
                .showAndWait();
    }

    private ArrayList<ArrayList<SubjectMapping>> subjectsWithNoGrade;

    @Deprecated
    /**
     * All encoding features are now in the curricular level assessor.
     */
    private void onEncoding() {
//        CheckGrade checkGrade = Evaluator.instance().createGradeChecker();
//        checkGrade.curriculumId = this.currentStudent.getCURRICULUM_id();
//        checkGrade.studentId = this.currentStudent.getCict_id();
//        checkGrade.studentYearLevel = this.currentStudent.getYear_level();
//        checkGrade.RATING_TO_CHECK = "UNPOSTED";
//        
//        checkGrade.setOnStart(onStart -> {
//            this.btnFind.setDisable(true);
//        });
//        checkGrade.setOnSuccess(success -> {
//            GenericLoadingShow.instance().hide();
//            try {
//                this.subjectsWithNoGrade = checkGrade.getSubjectsWithNoGrade();
//                if (subjectsWithNoGrade.size() > 0) {
//                    this.onShowGradeEncoderForRegular("unposted");
//                }
//            } catch (NullPointerException a) {
//                System.out.println("@EvaluateController: NO SUBJECT TO SHOW");
//                Mono.fx().alert()
//                        .createInfo()
//                        .setHeader("No Subject Found")
//                        .setMessage("There is no subject to show.")
//                        .showAndWait();
//            }
//            this.btnEvaluate.setDisable(false);
//            this.btnFind.setDisable(false);
//            setView("home");
//        });
//        
//        checkGrade.setOnStart(onStart -> {
//            GenericLoadingShow.instance().show();
//            this.vbox_studentOptions.setVisible(false);
//            this.btnEvaluate.setDisable(true);
//            this.btnFind.setDisable(true);
//        });
//        
//        checkGrade.setOnCancel(onCancel -> {
//            this.onError();
//        });
//        checkGrade.setOnFailure(failure -> {
//            this.onError();
//        });
//        checkGrade.setRestTime(300);
//        checkGrade.transact();
    }

    private void onShowGradeEncoderForRegular(String mode) {
        GradeEncoderController controller = new GradeEncoderController(mode, this.currentStudent,
                this.subjectsWithNoGrade.get(0),
                "");
        Mono.fx().create()
                .setPackageName("org.cict.evaluation.encoder")
                .setFxmlDocument("GradeEncoder")
                .makeFX()
                .setController(controller)
                .makeScene()
                .makeStageApplication()
                .stageMaximized(true)
                .stageShow();
        setView("home");
    }

    /**
     * This method is not used.
     */
    @Deprecated
    private void updateYearLevelForRegular() {
//        String[] schoolYear = Evaluator.instance()
//                .getCurrentAcademicTerm()
//                .getSchool_year().split("-");
//        Integer admissionYear = Integer.valueOf(this.currentStudent.getAdmission_year());
//        Integer yearLevel = Integer.valueOf(schoolYear[1]) - admissionYear;
//        if (!Objects.equals(this.currentStudent.getYear_level(), yearLevel)) {
//            this.currentStudent.setYear_level(yearLevel);
//            if (Database.connect().student().update(this.currentStudent)) {
//                System.out.println("@EvaluateController: STUDENT UPDATED YEAR LEVEL INTO " + yearLevel);
////                this.searchStudent();// students section
//                String section = this.currentStudent.getYear_level()
//                        + " "
//                        + this.currentStudent.getSection()
//                        + " - G"
//                        + this.currentStudent.get_group();
//                this.lblCourseSection.setText(this.studentProgram.getName() + " | " + section);
//
//            }
//        }
    }

    /**
     * Change student information.
     */
    @Deprecated
    private void onShowStudentInfo() {
        this.vbox_studentOptions.setVisible(false);
        InfoStudentController controller = new InfoStudentController(this.currentStudent);
        Mono.fx().create()
                .setPackageName("org.cict.evaluation.student.info")
                .setFxmlDocument("InfoStudent")
                .makeFX()
                .setController(controller)
                .makeScene()
                .makeStageWithOwner(Mono.fx().getParentStage(lblName))
                .stageResizeable(false)
                .stageShow();
        setView("home");
    }

    private void onShowHistory() {
        this.vbox_studentOptions.setVisible(false);
        StudentHistoryController controller = new StudentHistoryController(this.currentStudent,
                this.lblCourseSection.getText(), "EVALUATION");
        Mono.fx().create()
                .setPackageName("org.cict.evaluation.student.history")
                .setFxmlDocument("History")
                .makeFX()
                .setController(controller)
                .makeScene()
                .makeStageWithOwner(Mono.fx().getParentStage(lblName))
                .stageResizeable(true)
                .stageMaximized(true)
                .stageShow();
    }

    private void onShowInputMode() {
        this.vbox_studentOptions.setVisible(false);

        if (this.FLAG_CROSS_ENROLLEE) {
            Mono.fx().snackbar().showInfo(application_root, "Cross Enrollees cannot credit any subjects.");
            return;
        }
        String mode = CreditController.MODE_READ;
        if (Access.isGranted(Access.ACCESS_LOCAL_REGISTRAR)) {
            mode = CreditController.MODE_CREDIT;
        }
        System.out.println("ID FROM EVALUATION: " + String.valueOf(this.currentStudent.getCict_id()));
        CreditController controller = new CreditController(
                this.currentStudent.getCict_id(),
                mode, "EVALUATE");
        Mono.fx().create()
                .setPackageName("org.cict.evaluation.student.credit")
                .setFxmlDocument("Credit")
                .makeFX()
                .setController(controller)
                .makeScene()
                .makeStageApplication()
                .stageResizeable(true)
                .stageMaximized(true)
                .stageShowAndWait();
        //----------------------------------------------------------------------
        // search student again to refresh values
        if (mode.equalsIgnoreCase(CreditController.MODE_CREDIT)) {
            this.searchStudent();
        }
    }

    private void hideDropDown() {
        //this.vbox_settings.setVisible(false);
        this.vbox_studentOptions.setVisible(false);
    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    /**
     * Queue of student.
     */
    private SimpleTable studentTable = new SimpleTable();
    private CronThread cronThreadQueue;

    private void createQueueTable() {
        cronThreadQueue = new CronThread("evaluation_queue");
        cronThreadQueue.setInterval(3000);
        AccountFacultyMapping afMap = Database.connect().account_faculty().getPrimary(CollegeFaculty.instance().getACCOUNT_ID());
        Integer clusterNumber = afMap.getAssigned_cluster();
        cronThreadQueue.setTask(() -> {
            boolean result = ThreadMill.resfresh(vbox_list, txtStudentNumber, lbl_total_queue, clusterNumber);
            if (result) {
                Mono.fx().thread().wrap(() -> {
                    Animate.fade(vbox_list, 150, () -> {
                        vbox_waiting_queue.setVisible(false);
                        vbox_list.setVisible(true);
                    }, vbox_waiting_queue, vbox_list);
                });
            } else {
                Mono.fx().thread().wrap(() -> {
                    Animate.fade(vbox_waiting_queue, 150, () -> {
                        lbl_total_queue.setText("0");
                        vbox_list.setVisible(false);
                        vbox_waiting_queue.setVisible(true);
                    }, vbox_waiting_queue, vbox_list);
                });
            }
            if (ThreadMill.searching) {
                this.searchStudent();
                this.vbox_list.setDisable(true);
            }
        });
        cronThreadQueue.start();
    }

    private void setImageView() {
        StudentProfileMapping spMap = null;
        if (this.currentStudent.getHas_profile().equals(1)) {
            spMap = Mono.orm().newSearch(Database.connect().student_profile())
                    .eq(DB.student_profile().STUDENT_id, this.currentStudent.getCict_id())
                    .active(Order.desc(DB.student_profile().id)).first();
        }
        String studentImage = (spMap == null ? null : spMap.getProfile_picture());
        if (studentImage == null
                || studentImage.isEmpty()
                || studentImage.equalsIgnoreCase("NONE")) {
            ImageUtility.addDefaultImageToFx(img_profile, 1, this.getClass());
        } else {
            ImageUtility.addImageToFX("temp/images/profile", "student_avatar", studentImage, img_profile, 1);
        }
    }

    //-----------------------------
    // override over stay
    private boolean allowOverStay = false;

    private void goLang(String type) {
        Object[] result = Access.isEvaluationOverride(allowOverride, SystemOverriding.getACRONYM(15, type));
        boolean ok = (boolean) result[0];
        String fileName = (String) result[1];
        if (ok) {
            SystemOverrideLogsMapping map = MapFactory.map().system_override_logs();
            map.setCategory(SystemOverriding.CATEGORY_EVALUATION);
            map.setDescription(type);
            map.setExecuted_by(CollegeFaculty.instance().getFACULTY_ID());
            map.setExecuted_date(Mono.orm().getServerTime().getDateWithFormat());
            map.setAcademic_term(SystemProperties.instance().getCurrentAcademicTerm().getId());
            String conforme = currentStudent.getLast_name() + ", ";

            conforme += currentStudent.getFirst_name();
            if (currentStudent.getMiddle_name() != null) {
                conforme += " ";
                conforme += currentStudent.getMiddle_name();
            }
            map.setConforme(conforme);
            map.setConforme_type("STUDENT");
            map.setConforme_id(currentStudent.getCict_id());

            //-----------------
            map.setAttachment_file(fileName);
            //------------

            int id = Database.connect().system_override_logs().insert(map);
            if (id <= -1) {
                Mono.fx().snackbar().showError(anchor_right, "Something went wrong please try again.");
            } else {
                if (type.equalsIgnoreCase(SystemOverriding.EVAL_STUDENT_OVERSTAY)) {
                    this.allowOverStay = true;
                    this.searchStudent();
                }
            }
        }
    }

    //------------------------------------------
    // OVERSTAYED STUDENT
    private boolean onShowCoursesOffered() {
//        EvaluationMapping eMap = Mono.orm().newSearch(Database.connect().evaluation())
//                .eq(DB.evaluation().ACADTERM_id, SystemProperties.instance().getCurrentAcademicTerm().getId())
//                .eq(DB.evaluation().STUDENT_id, this.currentStudent.getCict_id())
//                .active(Order.desc(DB.evaluation().id)).first();
//        if(eMap != null) {
//            Mono.fx().alert().createWarning()
//                    .setHeader("Already Evaluated")
//                    .setMessage("The student is already evaluated in the current semester and cannot proceed to shifting. Please revoke the evaluation first before continuing.")
//                    .show();
//            return;
//        }

        CurriculumMapping selected = selectCurriculum("You can ask for the Local Registrar's assistance. Are you sure you want to continue?");
        if (selected == null) {
            return false;
        }

        if (!Access.enterTransactionPin(this.getStage())) {
            Mono.fx().snackbar().showError(application_root, "Transaction Request Denied");
            return false;
        }
        //----------------------------------------------------------------------
        System.out.println(selected.getName());
        this.processReassignment(selected.getId());
        //----------------------------
        return true;
    }

    private CurriculumMapping selectCurriculum(String message) {
        CurriculumChooser curriculumChooser = M.load(CurriculumChooser.class);
        curriculumChooser.setStudentID(currentStudent.getCict_id());
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

        //-------------------------------------------
        // consiquent curriculum, check if all grades are taken and passed
        if (selected.getLadderization_type().equalsIgnoreCase(CurriculumConstants.TYPE_CONSEQUENT)) {
            Mono.fx().alert().createWarning()
                    .setMessage("You are not allowed to take consequent curriculum.")
                    .show();
            return null;
        }
        //------------------------------------------
        return selected;
    }

    private void processReassignment(Integer curriculum_id) {
        TransactGrades transact = new TransactGrades();
        transact.student_id = currentStudent.getCict_id();
        transact.curriculum_id = curriculum_id;
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
//            StudentInformation currentStudent = new StudentInformation(Database.connect().student().getPrimary(this.currentStudent.getCict_id()));
//            if (currentStudent.getStudentMapping() == null) {
//                return;
//            }
            Notifications.create()
                    .title("Successfully Reassigned")
                    .text("Student's curriculum is changed"
                            + "\n successfully.")
                    .showInformation();
            this.searchStudent();
            this.allowOverStay = false;
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

//        public Boolean retain;
//        public Boolean alreadyTaken = false;
//        public Integer prevCourseRef_id;
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
            schMap.setCurriculum_id(studentCurriculum.getId());
            schMap.setPrep_assignment(currentStudent.getPrep_assignment());
            schMap.setPrep_id(currentStudent.getPREP_id());
            schMap.setStudent_id(currentStudent.getCict_id());
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
                    grade.setActive(0);
                    grade.setUpdated_by(CollegeFaculty.instance().getFACULTY_ID());
                    grade.setUpdated_date(Mono.orm().getServerTime().getDateWithFormat());
                    grade.setReason_for_update("Student Overstayed".toUpperCase());
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
                student.setPREP_id(null);
                student.setPrep_assignment(null);
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

            ArrayList<EvaluationMapping> oldEvaluationDetails = Mono.orm().newSearch(Database.connect().evaluation())
                    .eq(DB.evaluation().STUDENT_id, student_id)
                    .active().all();
            if (oldEvaluationDetails != null || !oldEvaluationDetails.isEmpty()) {
                for (EvaluationMapping each : oldEvaluationDetails) {
                    each.setCheck_mode("IGNORED");
                    if (!Database.connect().evaluation().transactionalSingleUpdate(currentSession, each)) {
                        dataTransaction.rollback();
                        log = "Cannot Update Evaluation For Now.";
                        System.out.println(log);
                        return false;
                    }
                }
            }

            dataTransaction.commit();
            return true;
        }
    }
    
    private void retentionChecker() {
        FailedGradeChecker checker = new FailedGradeChecker();
        checker.setStudent(currentStudent);
        checker.setDocument(ReportsUtility.createShortDocument());
        checker.whenCancelled(()->{
            Notifications.create().darkStyle()
                    .title("Warning")
                    .text(checker.getLog()).showWarning();
        });
        checker.whenSuccess(()->{
            if(checker.getLog()==null) {
            } else if(checker.getLog().equalsIgnoreCase("PRINTING")) {
                Notifications.create().darkStyle()
                        .title("Failed Grade Found")
                        .text("Printing the retention letter for the student.").showWarning();
            }
        });
        checker.transact();
    }
}

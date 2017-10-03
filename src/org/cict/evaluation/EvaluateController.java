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
import app.lazy.models.CurriculumMapping;
import app.lazy.models.Database;
import app.lazy.models.EvaluationMapping;
import app.lazy.models.LoadGroupMapping;
import app.lazy.models.LoadSectionMapping;
import app.lazy.models.StudentMapping;
import app.lazy.models.SubjectMapping;
import com.jhmvin.fx.async.SimpleTask;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import com.jhmvin.fx.display.SceneFX;
import javafx.scene.layout.Priority;
import org.cict.GenericLoadingShow;
import org.cict.PublicConstants;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.cict.authentication.authenticator.SystemProperties;
import org.cict.evaluation.encoder.GradeEncoderController;
import org.cict.evaluation.evaluator.CheckGrade;
import org.cict.evaluation.evaluator.PrintChecklist;
import org.cict.evaluation.evaluator.SearchStudent;
import org.cict.evaluation.evaluator.SubjectValidation;
import org.cict.evaluation.sectionviewer.SectionsController;
import org.cict.evaluation.student.credit.InputModeController;
import org.cict.evaluation.student.info.InfoStudentController;
import org.cict.evaluation.student.history.StudentHistoryController;
import org.cict.management.registrar.Registrar;
import org.cict.management.registrar.RevokeEvaluation;
import org.cict.reports.advisingslip.ChooseTypeController;
import org.controlsfx.control.Notifications;
import update.org.cict.controller.home.Home;
import update3.org.cict.access.Access;

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

    public EvaluateController() {
        //
    }

    private void log(Object message) {
        boolean logging = true;
        if (logging) {
            System.out.println("@EvaluationController: " + message.toString());
        }
    }

    /**
     * Contains all the subjects in the list for evaluation.
     */
    private final VBox vbox_subjects = new VBox(4);
    private AnchorPane application_root;

    @Override
    public void onInitialization() {
        application_root = anchor_evaluate;
        super.bindScene(application_root);
        /**
         * Faculty Details
         */
//        lbl_facultyName.setText(CollegeFaculty.instance().getFirstLastName());
//        lbl_facultyDesignation.setText(CollegeFaculty.instance().getDESIGNATION());

        this.anchor_preview.setVisible(false);
        this.anchor_results.setVisible(true);

        /**
         *
         */
        vbox_subjects.prefWidthProperty().bind(scroll_subjects.widthProperty());
        //
        scroll_subjects.setContent(vbox_subjects);
        scroll_subjects.setFitToWidth(true);
        scroll_subjects.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        //this.vbox_settings.setVisible(false);
        createTable();
    }

    // since this process only caters one student at a time
    // is is more efficient to store current searches
    private StudentMapping currentStudent;
    private CurriculumMapping studentCurriculum;
    private AcademicProgramMapping studentProgram;
    private LoadSectionMapping studentSection;
    private ArrayList<LoadGroupMapping> studentLoadGroup;
    private ArrayList<SubjectMapping> studentSubject;

    /**
     * Max units for adding subject.
     */
    private final static Double MAX_UNITS = PublicConstants.MAX_UNITS;

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    //##########################################################################
    @Override
    public void onEventHandling() {
        this.hideDropDownEvents();
        btn_already_print.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler -> {
            this.hideDropDown();
//            PrintAdvising slip = Evaluator.instance().printAdvising();
//            slip.studentNumber = currentStudent.getId();
//            slip.academicTerm = Evaluator.instance().getCurrentAcademicTerm().getId();
//            slip.transact();
            this.showChooseType(Evaluator.instance().getCurrentAcademicTerm().getId(), false);
        });

        /**
         * Full Screen.
         */
        Mono.fx().key(KeyCode.F11).release(anchor_evaluate, () -> {
            Mono.fx().getParentStage(anchor_evaluate).setFullScreen(true);
        });

        btn_already_evaluate.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            this.hideDropDown();
            onRevokeEvaluation();
        });

        btn_studentOptions.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            vbox_studentOptions.setVisible(!vbox_studentOptions.isVisible());
        });

        /**
         * Subject drag event
         */
        scroll_subjects.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            Evaluator.mouseDropSubject(currentStudent, MAX_UNITS, unitCount, vbox_subjects, anchor_right);
            //mouseDropSubject();
        });

        //----------------------------------------------------------------------
        /**
         * Adds listener to the list to check every changes
         */
        vbox_subjects.getChildren().addListener((ListChangeListener.Change<? extends Node> c) -> {
            onListChange(c);
        });

        /**
         * Search student
         */
        this.btnFind.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
            this.hideDropDown();
            this.searchStudent();
        });

        /**
         * Enter to evaluate
         */
        Mono.fx().key(KeyCode.ENTER).release(anchor_evaluate, () -> {
            this.hideDropDown();
            if (!this.btnFind.isDisabled()) {
                this.searchStudent();
            }
        });

        /**
         * Open class viewer
         */
        btn_winSection.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
            onShowSections();
        });

        /**
         * Save student evaluation
         */
        btnEvaluate.addEventHandler(MouseEvent.MOUSE_RELEASED, evaluate -> {
            this.hideDropDown();
            saveEvaluation();
        });

        btn_encoding.addEventHandler(MouseEvent.MOUSE_RELEASED, evaluate -> {
            this.onEncoding();
        });

//        btn_logout.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
//            this.onLogout();
//        });
//        btnLogout.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
//            this.onLogout();
//        });
//        btnStudentInfo.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
//            this.onShowStudentInfo();
//        });
        btnHistory.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
            this.onShowHistory();
        });

        btnCreditUnits.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
            this.onShowInputMode();
        });

//        btnSettings.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
//            vbox_settings.setVisible(!vbox_settings.isVisible());
//        });
        /**
         * Home Redirect
         */
        super.addClickEvent(btn_home, () -> {
            Home.callHome(this);
        });

        super.addClickEvent(btn_checklist, () -> {
            printChecklist();
        });
    }

    /**
     * Print the checklist.
     */
    private void printChecklist() {
        PrintChecklist printCheckList = new PrintChecklist();
        printCheckList.CICT_id = currentStudent.getCict_id();
        printCheckList.setOnStart(onStart -> {
            GenericLoadingShow.instance().show();
        });
        printCheckList.setOnSuccess(onSuccess -> {
            GenericLoadingShow.instance().hide();
            Notifications.create().title("Please wait, we're nearly there.")
                    .text("Printing the Checklist.").showInformation();
        });
        printCheckList.setOnCancel(onCancel -> {
            GenericLoadingShow.instance().hide();
            Notifications.create().title("Cannot Produce a Checklist")
                    .text("Something went wrong, sorry for the inconvinience.").showWarning();
        });
        printCheckList.setOnFailure(onFailed -> {
            GenericLoadingShow.instance().hide();
            Notifications.create().title("Cannot Produce a Checklist")
                    .text("Something went wrong, sorry for the inconvinience.").showError();
            System.out.println("FAILED");
        });
        printCheckList.transact();
    }

    private void hideDropDownEvents() {
        txtStudentNumber.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> {
            this.hideDropDown();
        });
        vbox_studentOptions.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent event) -> {
            this.hideDropDown();
        });
//        vbox_settings.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent event) -> {
//            this.hideDropDown();
//        });
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

        if(res != 1){
            return;
        }
        /**
         * Only Local Registrar and Co-Registrars are allowed to re-evaluate.
         */
        boolean isAllowed = false;
        if (Access.isDeniedIfNotFrom(
                Access.ACCESS_LOCAL_REGISTRAR,
                Access.ACCESS_CO_REGISTRAR)) {
            /**
             * Check if the user was granted permission by the registrar.
             */
            Access.isAllowedToRevoke();
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
        revoked_evaluation.registrar_id = CollegeFaculty.instance().getFACULTY_ID();
        revoked_evaluation.academic_term = Evaluator.instance().getCurrentAcademicTerm().getId();

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
            setView("home");
        });

        revoked_evaluation.setRestTime(500);
        revoked_evaluation.transact();
    }

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
     * When the list of the evaluated subjects changes.
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

//-----------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    /**
     * Saves the evaluation.
     */
    private void saveEvaluation() {
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
        if (this.unitCount == 0) {
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
        });
        evaluateTask.whenFinished(() -> {
            GenericLoadingShow.instance().hide();
            setView("home");
        });

        evaluateTask.setRestTime(500);
        evaluateTask.transact();
    }

    /**
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

            evaluationMap.setPrint_type(controller.getSelected().toUpperCase());
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

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    /**
     * This block is the process of searching the student and automatically
     * assigns subject that have a match with the sections available.
     */
    private Integer subjectCount = 0;
    private Double unitCount = 0.0;
    private boolean isAlreadyEvaluated;

    /**
     * Search student for evaluation.
     */
    private Integer countSearch = 0;
    private Boolean show;

    public void searchStudent() {
        show = true;
        if (currentStudent != null) {
            if (txtStudentNumber.getText().equalsIgnoreCase(currentStudent.getId())) {
                countSearch = 1;
                if (countSearch == 1) {
                    show = false;
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

            isAlreadyEvaluated = search.isAlreadyEvaluated();
            //
            currentStudent = search.getCurrentStudent();
            studentLoadGroup = search.getStudentLoadGroup();
            studentProgram = search.getStudentProgram();
            studentSection = search.getStudentSection();
            studentSubject = search.getStudentSubject();

            if (isAlreadyEvaluated) {
                setView("already");
                return;
            }
            if (Objects.isNull(this.currentStudent)) {
                System.out.println("@EvaluateController: Search is Empty");
                setView("no_results");
            } else {
                System.out.println("@EvaluateController: Search Success");
                /**
                 * Added code here. check if the student has an enrollment type.
                 */
                //this.onEnrollmentTypeUnset();
                if (show) {
                    showFirstAssistant();
                }

                onShowCurricularLevel();
                showAssistant();
                setView("preview");
                this.showPreview();
            }
        });

        search.setOnFailure(event -> {
            setView("no_results");
        });

        search.setOnCancel(event -> {
            System.out.println("@EvaluateController: Search Failed");
            setView("no_results");
            StudentMapping student = search.getCurrentStudent();
            if (student != null) {
                currentStudent = showMissingInfo(student);
            }
        });
        search.setRestTime(500);
        search.transact();
    } // end of task

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
//            // show results
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
            loadPreview();
        });
        previewTk.whenStarted(() -> {
            btnFind.setDisable(true);
            btnEvaluate.setDisable(true);
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
            btnFind.setDisable(false);
            btnEvaluate.setDisable(false);
            log("EvaluateController: Finished Loading Preview.");
        });
        previewTk.start();
    }

    private void loadPreview() {
        //reset subject view
        Mono.fx().thread().wrap(() -> {
            vbox_subjects.getChildren().clear();

            // student's full name
            String studentName = this.currentStudent.getLast_name()
                    + ", "
                    + this.currentStudent.getFirst_name()
                    + " "
                    + this.currentStudent.getMiddle_name();
            this.lblName.setText(studentName);

            // students section
            String section = this.currentStudent.getYear_level()
                    + " "
                    + this.currentStudent.getSection()
                    + " - G"
                    + this.currentStudent.get_group();
            this.lblCourseSection.setText(this.studentProgram.getName() + " | " + section);
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
    }

    /**
     * prepares the subject for display.
     *
     * @param x from $isOkToAdd
     * @param section $isOkToAdd
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

    //--------------------------------------------------------------------------
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
//                    // if mode is not_set, show enrollment type picker
//                    // if mode is regular, show missing records
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

//    private void onShowMissingRecord() {
//        System.out.println("@EvaluateController: Show Missing Records Controller");
//        MissingRecordController controller = new MissingRecordController(this.currentStudent,
//                this.checker,
//                this.yearAndSemWithMissingRecord);
//        Mono.fx().create()
//                .setPackageName("org.cict.evaluation.encoder")
//                .setFxmlDocument("missing_record")
//                .makeFX()
//                .setController(controller)
//                .makeScene()
//                .makeStageWithOwner(Mono.fx().getParentStage(lblName))
//                .stageResizeable(false)
//                .stageShowAndWait();
//    }
    private ArrayList<ArrayList<SubjectMapping>> subjectsWithNoGrade;

    private void onEncoding() {
        CheckGrade checkGrade = Evaluator.instance().createGradeChecker();
        checkGrade.curriculumId = this.currentStudent.getCURRICULUM_id();
        checkGrade.studentId = this.currentStudent.getCict_id();
        checkGrade.studentYearLevel = this.currentStudent.getYear_level();
        checkGrade.RATING_TO_CHECK = "UNPOSTED";

        checkGrade.setOnStart(onStart -> {
            this.btnFind.setDisable(true);
        });
        checkGrade.setOnSuccess(success -> {
            GenericLoadingShow.instance().hide();
            try {
                this.subjectsWithNoGrade = checkGrade.getSubjectsWithNoGrade();
                if (subjectsWithNoGrade.size() > 0) {
                    this.onShowGradeEncoderForRegular("unposted");
                }
            } catch (NullPointerException a) {
                System.out.println("@EvaluateController: NO SUBJECT TO SHOW");
                Mono.fx().alert()
                        .createInfo()
                        .setHeader("No Subject Found")
                        .setMessage("There is no subject to show.")
                        .showAndWait();
            }
            this.btnEvaluate.setDisable(false);
            this.btnFind.setDisable(false);
            setView("home");
        });

        checkGrade.setOnStart(onStart -> {
            GenericLoadingShow.instance().show();
            this.vbox_studentOptions.setVisible(false);
            this.btnEvaluate.setDisable(true);
            this.btnFind.setDisable(true);
        });

        checkGrade.setOnCancel(onCancel -> {
            this.onError();
        });
        checkGrade.setOnFailure(failure -> {
            this.onError();
        });
        checkGrade.setRestTime(300);
        checkGrade.transact();
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

    private void updateYearLevelForRegular() {
        String[] schoolYear = Evaluator.instance()
                .getCurrentAcademicTerm()
                .getSchool_year().split("-");
        Integer admissionYear = Integer.valueOf(this.currentStudent.getAdmission_year());
        Integer yearLevel = Integer.valueOf(schoolYear[1]) - admissionYear;
        if (!Objects.equals(this.currentStudent.getYear_level(), yearLevel)) {
            this.currentStudent.setYear_level(yearLevel);
            if (Database.connect().student().update(this.currentStudent)) {
                System.out.println("@EvaluateController: STUDENT UPDATED YEAR LEVEL INTO " + yearLevel);
//                this.searchStudent();// students section
                String section = this.currentStudent.getYear_level()
                        + " "
                        + this.currentStudent.getSection()
                        + " - G"
                        + this.currentStudent.get_group();
                this.lblCourseSection.setText(this.studentProgram.getName() + " | " + section);

            }
        }
    }

    /**
     * Change student information.
     */
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
                this.lblCourseSection.getText());
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

//    private void onShowCredit() {
//        this.vbox_studentOptions.setVisible(false);
//        CreditController controller = new CreditController(this.currentStudent.getCict_id());
//        Mono.fx().create()
//                .setPackageName("org.cict.evaluation.student.credit")
//                .setFxmlDocument("Credit")
//                .makeFX()
//                .setController(controller)
//                .makeScene()
//                .makeStageWithOwner(Mono.fx().getParentStage(lblName))
//                .stageResizeable(true)
//                .stageMaximized(true)
//                .stageShow();
//        //setView("home");
//    }
    private void onShowInputMode() {
        this.vbox_studentOptions.setVisible(false);
        InputModeController controller = new InputModeController(this.currentStudent.getCict_id());
        Mono.fx().create()
                .setPackageName("org.cict.evaluation.student.credit")
                .setFxmlDocument("mode")
                .makeFX()
                .setController(controller)
                .makeScene()
                .makeStageWithOwner(Mono.fx().getParentStage(lblName))
                .stageResizeable(false)
                .stageTitle("Input Mode")
                .stageShow();
        //setView("home");
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
    private ArrayList<StudentMapping> lst_student;

    private void createTable() {
//        for(StudentMapping student: lst_student) {
//            createRow(student);
//        }
        for (int i = 0; i < 5; i++) {
            createRow(i);
        }

        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(studentTable);
        simpleTableView.setFixedWidth(true);

        simpleTableView.setParentOnScene(vbox_list);
    }

    private void createRow(int num/*StudentMapping subject*/) {

        SimpleTableRow row = new SimpleTableRow();
        row.setRowHeight(70.0);

        HBox programRow = (HBox) Mono.fx().create()
                .setPackageName("org.cict.evaluation")
                .setFxmlDocument("eval-row")
                .makeFX()
                .pullOutLayout();
        Label lbl_number = searchAccessibilityText(programRow, "number");
        Label lbl_id = searchAccessibilityText(programRow, "id");
        Label lbl_name = searchAccessibilityText(programRow, "name");

        lbl_number.setText(num + "");

        SimpleTableCell cellParent = new SimpleTableCell();
        cellParent.setResizePriority(Priority.ALWAYS);
        cellParent.setContent(programRow);

        row.addCell(cellParent);

        studentTable.addRow(row);
    }
}

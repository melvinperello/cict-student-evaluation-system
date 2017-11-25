package update.org.cict.controller.adding;

import app.lazy.models.AccountFacultyMapping;
import app.lazy.models.CurriculumMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.EvaluationMapping;
import app.lazy.models.MapFactory;
import app.lazy.models.StudentMapping;
import app.lazy.models.StudentProfileMapping;
import app.lazy.models.SubjectMapping;
import app.lazy.models.SystemOverrideLogsMapping;
import artifacts.ImageUtility;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.CronThread;
import com.jhmvin.fx.async.SimpleTask;
import com.jhmvin.fx.controls.SimpleImage;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import com.jhmvin.transitions.Animate;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.cict.authentication.authenticator.SystemProperties;
import java.util.ArrayList;
import java.util.Objects;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.cict.GenericLoadingShow;
import org.cict.PublicConstants;
import org.cict.SubjectClassification;
import org.cict.ThreadMill;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.cict.evaluation.CurricularLevelController;
import org.cict.evaluation.FirstAssistantController;
import org.cict.evaluation.evaluator.PrintChecklist;
import org.cict.evaluation.student.credit.CreditController;
import org.cict.evaluation.student.history.StudentHistoryController;
import org.cict.evaluation.student.info.InfoStudentController;
import org.cict.reports.ReportsUtility;
import org.cict.reports.deficiency.PrintDeficiency;
import org.controlsfx.control.Notifications;
import org.hibernate.criterion.Order;
import update.org.cict.controller.adding.subjectviewer.AddingDataPipe;
import update.org.cict.controller.adding.subjectviewer.AddingSubjects;
import update.org.cict.controller.adding.subjectviewer.AssistantController2;
import update.org.cict.controller.adding.subjectviewer.ChangingSubjects;
import update.org.cict.controller.home.Home;
import update3.org.cict.access.Access;
import update3.org.cict.access.SystemOverriding;

public class AddingHome extends SceneFX implements ControllerFX {

//    private double max_units = 27.0;
    @FXML
    private AnchorPane anchor_add_change;
    @FXML
    private JFXButton btn_home;
    @FXML
    private TextField txtStudentNumber;
    @FXML
    private JFXButton btnFind;
    @FXML
    private JFXButton btn_winAdd;
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
    private VBox vbox_tableHandler;
    @FXML
    private JFXButton btnSaveChanges;
    @FXML
    private Label lbl_subjectTotal;
    @FXML
    private Label lbl_unitsTotal;
    @FXML
    private VBox vbox_studentOptions;
    @FXML
    private JFXButton btnStudentInfo;
    @FXML
    private JFXButton btnHistory;
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
    private JFXButton btn_add_change_again;
    @FXML
    private JFXButton btn_checklist;
    @FXML
    private ImageView img_profile;
    @FXML
    private AnchorPane anchor_main1;
    @FXML
    private VBox vbox_list;
    @FXML
    private Label lbl_total_queue;
    @FXML
    private VBox vbox_waiting_queue;

    private StudentMapping studentSearched;

    private ArrayList<SubjectInformationHolder> originalList = new ArrayList<>();
    private Integer newEvaluationID;

    private void logs(String str) {
        if (true) {
            System.out.println("@AddingHome: " + str);
        }
    }

    private AnchorPane application_root;

    @Override
    public void onInitialization() {
        /**
         * Just for the sake of uniformity every scene's root must be named
         * application_root.
         */
        application_root = anchor_add_change;
        super.bindScene(application_root);

        vbox_studentOptions.setVisible(false);
        anchor_preview.setVisible(false);
         
        Animate.fade(vbox_waiting_queue, 150, ()->{
            vbox_waiting_queue.setVisible(false);
            vbox_list.setVisible(false);
            vbox_waiting_queue.setVisible(true);
        }, vbox_waiting_queue, vbox_list);
        createQueueTable();
    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    /**
     * Queue of student.
     */
    private SimpleTable studentTable = new SimpleTable();
    private CronThread cronThreadQueue;
    private void createQueueTable() {
        AccountFacultyMapping afMap = Database.connect().account_faculty().getPrimary(CollegeFaculty.instance().getACCOUNT_ID());
        Integer clusterNumber = afMap.getAssigned_cluster();
        cronThreadQueue = new CronThread("adding_changing_queue");
        cronThreadQueue.setInterval(5000);
        cronThreadQueue.setTask(()->{
            boolean result = ThreadMill.resfresh(vbox_list, txtStudentNumber, lbl_total_queue, clusterNumber);
            if(result) {
                Mono.fx().thread().wrap(()->{
                    Animate.fade(vbox_list, 150, ()->{
                        vbox_waiting_queue.setVisible(false);
                        vbox_list.setVisible(true);
                    }, vbox_waiting_queue, vbox_list);
                });
            } else {
                Mono.fx().thread().wrap(()->{
                    Animate.fade(vbox_waiting_queue, 150, ()->{
                        lbl_total_queue.setText("0");
                        vbox_list.setVisible(false);
                        vbox_waiting_queue.setVisible(true);
                    }, vbox_waiting_queue, vbox_list);
                });
            }
            if(ThreadMill.searching) {
                this.onSearchStudent();
            }
        });
        cronThreadQueue.start();
    }
    
    @Override
    public void onEventHandling() {
        btnFind.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            onSearchStudent();
        });

        btn_home.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            onBackToHome();
        });

//        btn_home1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
//            onBackToHome();
//        });

        btnSaveChanges.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if(!Access.enterTransactionPin(super.getStage())){
                Mono.fx().snackbar().showError(application_root, "Transaction Denied");
                return;
            }
            onSaveChanges(); // -> save the adding to database.
        });

        btn_winAdd.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            this.onShowAddSubject();
        });

        addClickEvent(btn_add_change_again, () -> {
            revokeAddChange();
        });

        addClickEvent(btn_already_print, () -> {
            printAddChangeForm(null, "Processing your request.");
        });

        btn_studentOptions.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            vbox_studentOptions.setVisible(!vbox_studentOptions.isVisible());
        });

        hideDropDownEvents();

        Mono.fx().key(KeyCode.ENTER).release(anchor_add_change, () -> {
            if (!this.btnFind.isDisabled()) {
                this.onSearchStudent();
            }
        });

        btnStudentInfo.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
//            this.onShowStudentInfo();
            this.printDeficiency();

        });

        btnHistory.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
            this.onShowHistory();
        });

        addClickEvent(btnCreditUnits, () -> {
            onShowGrades();
        });

        this.addClickEvent(btn_checklist, () -> {
            printChecklist();
        });
    }
    
    private Boolean FLAG_CROSS_ENROLLEE;
    private void printChecklist() {
        // disallows cross enrollees to print a check list.
        if (this.FLAG_CROSS_ENROLLEE) {
            Mono.fx().snackbar().showInfo(application_root, "No Check List for Cross Enrollees");
            return;
        }
        
        CurriculumMapping curriculum = Database.connect().curriculum().getPrimary(studentSearched.getCURRICULUM_id());
        CurriculumMapping curriculum_prep = null;
        if(studentSearched.getPREP_id()!=null){
            curriculum_prep = Database.connect().curriculum().getPrimary(studentSearched.getPREP_id());
        }
        Boolean isLegacyExist = false;
        for(String legacy: PublicConstants.LEGACY_CURRICULUM) {
            if(legacy.equalsIgnoreCase(curriculum.getName())) {
                isLegacyExist = true;
                break;
            }
            if(curriculum_prep!=null){
                if(legacy.equalsIgnoreCase(curriculum_prep.getName())) {
                    isLegacyExist = true;
                    break;
                }
            }
        }
        
        Boolean printLegacy = false;
        if(isLegacyExist) {
            int res = Mono.fx().alert().createConfirmation()
                    .setHeader("Checklist Format")
                    .setMessage("Please choose a format.")
                    .confirmCustom("Legacy", "Standard");
            if(res==1)
                printLegacy = true;
        }
        if(curriculum_prep != null) {
            if(printLegacy)
                printCheckList(printLegacy, curriculum.getId(), curriculum_prep.getId());
            else
                printCheckList(printLegacy, curriculum_prep.getId(), null);
        } else
            printCheckList(printLegacy, curriculum.getId(), null);
    }
    
    private void printCheckList(Boolean printLegacy, Integer curriculum_ID, Integer prep_id){
        PrintChecklist printCheckList = new PrintChecklist();
        printCheckList.printLegacy = printLegacy;
        printCheckList.CICT_id = studentSearched.getCict_id();
        printCheckList.CURRICULUM_id = curriculum_ID;
        printCheckList.setOnStart(onStart -> {
            GenericLoadingShow.instance().show();
        });
        printCheckList.setOnSuccess(onSuccess -> {
            GenericLoadingShow.instance().hide();
            if(prep_id==null){
                Notifications.create().title("Please wait, we're nearly there.")
                        .text("Printing the Checklist.").showInformation();
            } else
                printCheckList(printLegacy, prep_id, null);
        });
        printCheckList.setOnCancel(onCancel -> {
            GenericLoadingShow.instance().hide();
            Notifications.create().title("Cannot Produce a Checklist")
                    .text("Something went wrong, sorry for the inconvinience.").showWarning();
        });
        
        if(!printLegacy)
            printCheckList.setDocumentFormat(ReportsUtility.paperSizeChooser(this.getStage()));
        printCheckList.transact();
    }

    private void onShowGrades() {
        CreditController controller = new CreditController(this.studentSearched.getCict_id(), CreditController.MODE_READ);
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

    private void onShowHistory() {
        this.vbox_studentOptions.setVisible(false);
        StudentHistoryController controller = new StudentHistoryController(this.studentSearched,
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

//    private void onShowStudentInfo() {
//        this.vbox_studentOptions.setVisible(false);
//        InfoStudentController controller = new InfoStudentController(this.studentSearched);
//        Mono.fx().create()
//                .setPackageName("org.cict.evaluation.student.info")
//                .setFxmlDocument("InfoStudent")
//                .makeFX()
//                .setController(controller)
//                .makeScene()
//                .makeStageWithOwner(Mono.fx().getParentStage(lblName))
//                .stageResizeable(false)
//                .stageShow();
//        setView("home");
//    }

    private void hideDropDownEvents() {
        txtStudentNumber.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> {
            this.vbox_studentOptions.setVisible(false);
        });
        vbox_studentOptions.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent event) -> {
            this.vbox_studentOptions.setVisible(false);
        });
        anchor_studentInfo.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent event) -> {
            this.vbox_studentOptions.setVisible(false);
        });
    }

    private void revokeAddChange() {
        
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
        
        
        RevokeChanges revoke = new RevokeChanges();
        revoke.currentEvaluation = this.currentStudentEvaluationDetails;

        revoke.setOnSuccess(onSuccess -> {
            setView("home");
            Notifications.create()
                    .title("Done")
                    .text("Succesfully retrived the old evaluation details.")
                    .showInformation();
        });
        revoke.setOnFailure(onFailure -> {
            setView("home");
            Notifications.create()
                    .title("Failed")
                    .text("Evaluation cannot be retrive at this moment.")
                    .showInformation();
        });

        revoke.transact();
    }

    private void onSaveChanges() {
        SaveChanges save = new SaveChanges();

        // boolean for tracking is there is changes made
        boolean isModified = false;
        // get details
        ArrayList<Object[]> details = new ArrayList<>();
        for (Node row : this.table.getRows()) {
            SimpleTableRow simplerow = (SimpleTableRow) row;
            SubjectInformationHolder row_info = (SubjectInformationHolder) simplerow.getRowMetaData().get(KEY_SUB_INFO);
            String row_status = (String) simplerow.getRowMetaData().get(KEY_ROW_STATUS);
            SubjectInformationHolder old_lg = null;
            switch (row_status) {
                case "CHANGED":
                    old_lg = (SubjectInformationHolder) simplerow.getRowMetaData().get(KEY_OLD_INFO);
                    isModified = true;
                    break;
                case "ADDED":
                    isModified = true;
                    break;
            }
            Object[] o = new Object[]{row_status, row_info, old_lg};
            details.add(o);
        }

        // pass the values to the transaction.
        save.evaluationDetails = this.currentStudentEvaluationDetails;
        save.details = details;

        save.setOnSuccess(onSuccess -> {
            ArrayList<SubjectInformationHolder> report_values = save.getTransactionReport();
            logs("ADD/CHANGE SAVED SUCCESSFULLY");
            newEvaluationID = save.getNewEvaluationID();
            printAddChangeForm("Saved Successfully", "Requesting for adding and changing letter.");
            setView("home");
        });

        save.setOnCancel(onCancel -> {
            logs("ERROR_INSERT");
        });

        save.setOnFailure(onFailure -> {
            logs("RUNTIME_ERROR");
        });

        if (!isModified) {
            int res = Mono.fx().alert()
                    .createConfirmation()
                    .setHeader("No Changes Made")
                    .setMessage("There are no subject modified or added. Do you still want to save?")
                    .confirmYesNo();
            if (res == 1) {
                save.transact();
            }
        } else {
            save.transact();
        }
    }

    private void onBackToHome() {
        cronThreadQueue.stop();
        Home.callHome(this);
    }

    private EvaluationMapping currentStudentEvaluationDetails;

    private void onSearchStudent() {
        ThreadMill.searching = false;
        currentStudentEvaluationDetails = null;

        this.table.getChildren().clear();
        CheckStudent checkStudentTx = new CheckStudent();
        checkStudentTx.studentNumber = this.txtStudentNumber.getText().trim();
        checkStudentTx.acadTermID = SystemProperties.instance().getCurrentAcademicTerm().getId();

        checkStudentTx.setOnSuccess(event -> {
            String result = checkStudentTx.getTxResult();
            if (result.equalsIgnoreCase("STUDENT_NOT_EXIST")) {
                setView("no_results");
            } else if (result.equalsIgnoreCase("NO_EVALUATION")) {
                setView("search");
                Mono.fx().alert().createInfo()
                        .setTitle("Search")
                        .setHeader("Student Is Not Evaluated")
                        .setMessage("Student must be evaluated first.")
                        .show();
                setView("home");
            } else if (result.equalsIgnoreCase("RE_EVALUATE_ADD_CHANGE")) {
                /**
                 * re evaluate adding_changing
                 */
                setView("already");
                this.studentSearched = checkStudentTx.getStudentMap();
                this.currentStudentEvaluationDetails = checkStudentTx.getEvaluationMap();
                this.newEvaluationID = checkStudentTx.getEvaluationMap().getId();
            } else {
                FLAG_CROSS_ENROLLEE = checkStudentTx.isCrossEnrollee();
                //----------------------------
                // if cross enrollee, must not continue adding and changing process
                if(FLAG_CROSS_ENROLLEE) {
                    Mono.fx().alert().createWarning()
                            .setMessage("Cross enrollee students are not allowed to take adding and changing transactions.")
                            .show();
                    return;
                }
                //-------------------------
                /**
                 * Show preview panel
                 */
                // added to get the evaluation history of the student.
                this.currentStudentEvaluationDetails = checkStudentTx.getEvaluationMap();
                this.studentSearched = checkStudentTx.getStudentMap();

                if(!PublicConstants.DISABLE_ASSISTANCE ) {
                    showFirstAssistant();
                }
                onShowCurricularLevel();
                if(!PublicConstants.DISABLE_ASSISTANCE ) {
                    showAssistant();
                }
                
                setView("preview");
                onShowStudent(checkStudentTx.getStudentMap(), checkStudentTx.getStudentSection());

                /**
                 * Load Evaluated subjects
                 */
                onLoadEvaluatedSubjects(checkStudentTx.getEvaluationMap(), checkStudentTx.getStudentMap());
            }
        });
        checkStudentTx.setOnCancel(onCancel->{
            if(checkStudentTx.getTxResult().equalsIgnoreCase("NO_CURRICULUM")) {
                Mono.fx().alert().createWarning()
                        .setMessage("Student must have a curriculum to enter adding and changing transaction.")
                        .show();
            }
        });
        checkStudentTx.transact();
    }

    private void onShowCurricularLevel() {
        CurricularLevelController controller = new CurricularLevelController(this.studentSearched);
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

    private void showFirstAssistant() {
        anchor_add_change.setDisable(true);
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
        AssistantController2 controller = new AssistantController2(this.currentStudentEvaluationDetails);
        Mono.fx().create()
                .setPackageName("update.org.cict.layout.home")
                .setFxmlDocument("sitti")
                .makeFX()
                .setController(controller)
                .makeScene()
                .makeStageWithOwner(Mono.fx().getParentStage(lblName))
                .stageResizeable(false)
                .stageUndecorated(true)
                .stageCenter()
                .stageShowAndWait();
    }

    private void onShowStudent(StudentMapping sMap, String section) {
        String studentFullName = sMap.getLast_name() + ", "
                + sMap.getFirst_name() + " "
                + (sMap.getMiddle_name()==null? "":sMap.getMiddle_name());

        this.lblCourseSection.setText(section);
        this.lblName.setText(studentFullName);

    }

    /**
     * Load evaluated subjects.
     *
     * @param evaluationMap
     * @param studentMap
     */
    private void onLoadEvaluatedSubjects(EvaluationMapping evaluationMap, StudentMapping studentMap) {
        
        //-------------------------------
        // set image
        SimpleTask set_profile = new SimpleTask("set_profile");
        set_profile.setTask(()->{
            this.setImageView();
        });
        set_profile.whenCancelled(()->{
            Notifications.create().text("Loading of image is cancelled")
                    .showInformation();
        });
        set_profile.whenFailed(()->{
            Notifications.create().text("Failed to load image.")
                    .showInformation();
        });
        set_profile.whenSuccess(()->{
        });
        set_profile.start();
        //-------------------------------
        
        
        CheckEvaluatedSubjects checkEvaluationTx = new CheckEvaluatedSubjects();
        checkEvaluationTx.evaluationMap = evaluationMap;
        checkEvaluationTx.studentMap = studentMap;

        checkEvaluationTx.setOnSuccess(event -> {
            createTable(checkEvaluationTx.getSubjectCollection());
        });
        checkEvaluationTx.transact();
    }

    private SimpleTable table = new SimpleTable();

    /**
     * Create Table
     *
     * @param collection
     */
    private void createTable(ArrayList<SubjectInformationHolder> collection) {

        // create the table object
//        SimpleTable table = new SimpleTable();
        // add listener to this table for changes.
        tableChangeListener(table);

        for (SubjectInformationHolder subInfo : collection) {

//            //create table row
//            SimpleTableRow row = new SimpleTableRow();
//            row.setRowHeight(50.0);
//
//            /**
//             * Row Extendsion (Optional)
//             */
//            SimpleTableCell cellExtension = new SimpleTableCell();
//            cellExtension.setPrefWidth(100.0);
//            ImageView content_icon = new ImageView();
//            content_icon.setFitWidth(30.0);
//            content_icon.setFitHeight(30.0);
//            content_icon.setImage(new Image("res/img/plus_sign.png"));
//
//            content_icon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
//                onShowExtension(table, row, cellExtension);
//            });
//            //
//            cellExtension.setContent(content_icon);
//            cellExtension.setContentPosition(Pos.CENTER);
//            cellExtension.addContentCssClass("table-cell-content-extend");
//            row.addCell(cellExtension);
//
//            /**
//             * Subject Code
//             */
//            SimpleTableCell cellCode = new SimpleTableCell();
//            cellCode.setPrefWidth(180.0);
//            cellCode.setContent(new Label(subInfo.getSubjectMap().getCode())); // you set this cell as a label
//            // assign css selector
//            cellCode.addContentCssClass("table-cell-content-code");
//            // add cell to row
//            row.addCell(cellCode);
//
//            /**
//             * Subject Description
//             */
//            SimpleTableCell cellDescription = new SimpleTableCell();
//            cellDescription.setResizePriority(Priority.ALWAYS);
//            cellDescription.setContent(new Label(subInfo.getSubjectMap().getDescriptive_title()));
//            // assign css selector
//            cellDescription.addContentCssClass("table-cell-content-description");
//            // add cell to row
//            row.addCell(cellDescription);
//
//            /**
//             * Lec Label
//             */
//            //lec icon
//            ImageView lecIcon = new ImageView();
//            lecIcon.setFitWidth(15.0);
//            lecIcon.setFitHeight(15.0);
//            lecIcon.setImage(new Image("res/img/lec.png"));
//            // icon column
//            SimpleTableCell cellLecLabel = new SimpleTableCell();
//            cellLecLabel.setPrefWidth(30.0);
//            //
//            Label content_lec = new Label();
//            content_lec.setText("LEC");
//            content_lec.setGraphic(lecIcon);
//            content_lec.setContentDisplay(ContentDisplay.TOP);
//            //
//            cellLecLabel.setContent(content_lec);
//            cellLecLabel.addContentCssClass("table-cell-content-image-label");
//            // add cell to row
//            row.addCell(cellLecLabel);
//
//            // lec units
//            SimpleTableCell cellLecUnits = new SimpleTableCell();
//            cellLecUnits.setPrefWidth(50.0);
//            //
//            Label content_lect_units = new Label();
//            content_lect_units.setText(subInfo.getSubjectMap().getLec_units().toString());
//            //
//            cellLecUnits.setContent(content_lect_units);
//            cellLecUnits.addContentCssClass("table-cell-content-units");
//            // add cell to row
//            row.addCell(cellLecUnits);
//
//            /**
//             * Lab Label
//             */
//            //lab icon
//            ImageView labIcon = new ImageView();
//            labIcon.setFitWidth(15.0);
//            labIcon.setFitHeight(15.0);
//            labIcon.setImage(new Image("res/img/lab.png"));
//            // icon column
//            SimpleTableCell cellLabLabel = new SimpleTableCell();
//            cellLabLabel.setPrefWidth(40.0);
//            cellLabLabel.setCellPadding(0.0, 10.0);
//            // node
//            Label content_lab = new Label();
//            content_lab.setText("LAB");
//            content_lab.setGraphic(labIcon);
//            content_lab.setContentDisplay(ContentDisplay.TOP);
//            //
//            cellLabLabel.setContent(content_lab);
//            cellLabLabel.addContentCssClass("table-cell-content-image-label");
//            // add cell to row
//            row.addCell(cellLabLabel);
//
//            // lab units
//            SimpleTableCell cellLabUnits = new SimpleTableCell();
//            cellLabUnits.setPrefWidth(50.0);
//            //
//            Label content_lab_units = new Label();
//            content_lab_units.setText(subInfo.getSubjectMap().getLab_units().toString());
//            //
//            cellLabUnits.setContent(content_lab_units);
//            cellLabUnits.addContentCssClass("table-cell-content-units");
//            // add cell to row
//            row.addCell(cellLabUnits);
//
//            /**
//             * Section Label
//             */
//            //lab icon
//            ImageView secIcon = new ImageView();
//            secIcon.setFitWidth(30.0);
//            secIcon.setFitHeight(30.0);
//            secIcon.setImage(new Image("res/img/Google Classroom_96px.png"));
//            // icon column
//            SimpleTableCell cellSection = new SimpleTableCell();
//            cellSection.setPrefWidth(200.0);
//            cellSection.setCellPadding(20.0, 10.0);
//            //
//            Label content_section = new Label();
//            content_section.setText(subInfo.getFullSectionName());
//            content_section.setGraphic(secIcon);
//            //
//            cellSection.setContent(content_section);
//            cellSection.addContentCssClass("table-cell-content-section");
//            // add cell to row
//            row.addCell(cellSection);
            /**
             * Create the extended panel for row.
             */
            // lets use Hbox in this example.
//            createDefaultRowExtension(table, row);
//
//            // add row to table
//            row.getRowMetaData().put(KEY_SUB_INFO, subInfo);
//            row.getRowMetaData().put(KEY_ROW_STATUS, "ORIGINAL");
//            //
//            originalList.add(subInfo);
////            savedSubjects.add(subInfo);
//            table.addRow(row);
            createRow(subInfo, false);
        }

        // when all details are added in the table;
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(table);
        simpleTableView.setFixedWidth(true);
        // attach to parent variable name in scene builder
        simpleTableView.setParentOnScene(vbox_tableHandler);
    }

    private void createRow(SubjectInformationHolder subInfo, boolean added) {

        SimpleTableRow row = new SimpleTableRow();
        row.setRowHeight(70.0);
        
        row.getStyleClass().add("table-mutate-as-default");
        HBox subjectRow = (HBox) Mono.fx().create()
                .setPackageName("update.org.cict.layout.adding_changing")
                .setFxmlDocument("home-row")
                .makeFX()
                .pullOutLayout();

        ImageView img_extension = searchAccessibilityText(subjectRow, "img_row_extension");

        Label lbl_code = searchAccessibilityText(subjectRow, "code");
        Label lbl_descriptive_title = searchAccessibilityText(subjectRow, "descript");
        Label lbl_lec = searchAccessibilityText(subjectRow, "lec");
        Label lbl_lab = searchAccessibilityText(subjectRow, "lab");
        Label lbl_section = searchAccessibilityText(subjectRow, "section");

        super.addClickEvent(img_extension, ()->{
            if (row.isExtensionShown()) {
                img_extension.setImage(SimpleImage.make("res.img", "plus_sign.png"));
                row.hideExtension();
            } else if (!row.isExtensionShown()) {
                // close all row extension
                for (Node tableRows : table.getRows()) {
                    SimpleTableRow simplerow = (SimpleTableRow) tableRows;
                    SimpleTableCell simplecell = simplerow.getCell(0);
                    HBox simplerowcontent = simplecell.getContent();
                    ImageView simplerowimage = findByAccessibilityText(simplerowcontent, "img_row_extension");

                    simplerowimage.setImage(SimpleImage.make("res.img", "plus_sign.png"));
                    simplerow.hideExtension();
                }

                // show row extension
                img_extension.setImage(SimpleImage.make("res.img", "negative_sign.png"));
                row.showExtension();
            } else {
                System.out.println("EXTENSION NOTHING HAPPENED");
            }
        });
        
        super.addDoubleClickEvent(row, ()->{
            if (row.isExtensionShown()) {
                img_extension.setImage(SimpleImage.make("res.img", "plus_sign.png"));
                row.hideExtension();
            } else if (!row.isExtensionShown()) {
                // close all row extension
                for (Node tableRows : table.getRows()) {
                    SimpleTableRow simplerow = (SimpleTableRow) tableRows;
                    SimpleTableCell simplecell = simplerow.getCell(0);
                    HBox simplerowcontent = simplecell.getContent();
                    ImageView simplerowimage = findByAccessibilityText(simplerowcontent, "img_row_extension");

                    simplerowimage.setImage(SimpleImage.make("res.img", "plus_sign.png"));
                    simplerow.hideExtension();
                }

                // show row extension
                img_extension.setImage(SimpleImage.make("res.img", "negative_sign.png"));
                row.showExtension();
            } else {
                System.out.println("EXTENSION NOTHING HAPPENED");
            }
        });

        SubjectMapping subject = subInfo.getSubjectMap();
        lbl_code.setText(subject.getCode());
        lbl_descriptive_title.setText(subject.getDescriptive_title());
        lbl_lec.setText(subject.getLec_units().toString());
        lbl_lab.setText(subject.getLab_units().toString());
        lbl_section.setText(subInfo.getFullSectionName());

        Label lbl_icon = searchAccessibilityText(subjectRow, "letter");
        lbl_icon.setText(subject.getCode().charAt(0) + "");

        SimpleTableCell cellParent = new SimpleTableCell();
        cellParent.setResizePriority(Priority.ALWAYS);
        cellParent.setContent(subjectRow);

        row.addCell(cellParent);

        /**
         * Create the extended panel for row.
         */
        if (added) {
            createAddedRowExtension(table, row);
            row.getRowMetaData().put(KEY_SUB_INFO, subInfo);
            row.getRowMetaData().put(KEY_ROW_STATUS, "ADDED");
        } else {
            createDefaultRowExtension(table, row);

            // add row to table
            row.getRowMetaData().put(KEY_SUB_INFO, subInfo);
            row.getRowMetaData().put(KEY_ROW_STATUS, "ORIGINAL");
        }
        table.addRow(row);
    }

    /**
     * What to do when the row extension button is clicked. this is optional, it
     * is not required in construction of table.
     *
     * @param table
     * @param row
     * @param cellExtension
     */
//    private void onShowExtension(SimpleTable table, SimpleTableRow row, ImageView cellExtension) {
//        if (row.isExtensionShown()) {
//            row.hideExtension();
////            cellExtension.<ImageView>getContent().setImage(new Image("res/img/plus_sign.png"));
//            cellExtension.setImage(new Image("res/img/plus_sign.png"));
//        } else {
//            // closes all row extensions.
//            table.getRows().forEach(rows -> {
//                SimpleTableRow simplerow = (SimpleTableRow) rows;
//                simplerow.<ImageView>getCellContent(0).setImage(new Image("res/img/plus_sign.png"));
//                simplerow.hideExtension();
//            });
//            // before opening another
//            // this is optional
//            row.showExtension();
////            cellExtension.<ImageView>getContent().setImage(new Image("res/img/negative_sign.png"));
//            cellExtension.setImage(new Image("res/img/negative_sign.png"));
//        }
//    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    /**
     * Inner class.
     */
    class RowExtensionFactory {

        public String imagePath = "res/img/ext_info.png";
        public String message = "You can only change subjects with the same amount"
                + " of Lab and Lec Units, You can also remove this subject and"
                + " replace it with another one, or just unload this subject "
                + "without replacement.";

        /**
         * private
         */
        private HBox button_container;
        private HBox extension;

        public HBox getButton_container() {
            return button_container;
        }

        public RowExtensionFactory() {
            // define the extension
            extension = new HBox(20);
            extension.setAlignment(Pos.TOP_LEFT);
            extension.setPadding(new Insets(20.0, 20.0, 0.0, 20.0));
            extension.setPrefHeight(120.0);
            //
            button_container = new HBox(10);
            button_container.setPadding(new Insets(0, 20, 10, 0));
            button_container.setMaxWidth(Double.MAX_VALUE);
            button_container.setAlignment(Pos.CENTER_RIGHT);
        }

        public void create(SimpleTableRow row) {
            ImageView image = new ImageView();
            image.setImage(new Image(this.imagePath));
            image.setFitHeight(60.0);
            image.setFitWidth(60.0);
            extension.getChildren().add(image);

            VBox ext_content = new VBox();
            ext_content.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(ext_content, Priority.ALWAYS);
            // create label
            Label ext_message = new Label();
            ext_message.setText(this.message);
            ext_message.setFont(new Font(18.0));
            ext_message.setWrapText(true);
            ext_message.setMaxWidth(Double.MAX_VALUE);
            ext_message.setMaxHeight(Double.MAX_VALUE);
            //
            VBox.setVgrow(ext_message, Priority.ALWAYS);
            ext_content.getChildren().add(ext_message);

            /**
             * Buttons
             */
            //
            ext_content.getChildren().add(button_container);

            extension.getChildren().add(ext_content);

            // add the row extension to this row
            row.setRowExtension(extension);
        }
    }

    /**
     * Create a row extension for removed rows.
     *
     * @param table
     * @param row
     */
    private void createRemovedRowExtension(SimpleTable table, SimpleTableRow row) {
        /**
         * Modify row details; when removed.
         */
        row.getRowMetaData().put(KEY_ROW_STATUS, "REMOVED");
        row.getStyleClass().remove("table-mutate-as-default");
        row.getStyleClass().add("table-mutate-as-removed");

        // create UI
//        RowExtensionFactory removedExtension = new RowExtensionFactory();
//        removedExtension.imagePath = "res/img/ext_info.png";
//        removedExtension.message = "This subject was marked as removed, if you want to "
//                + "add this subject again in the list please click the revert button.";
//
//        JFXButton btn_revert = new JFXButton();
        HBox programRowExtension = (HBox) Mono.fx().create()
                .setPackageName("update.org.cict.layout.adding_changing")
                .setFxmlDocument("remove-extension")
                .makeFX()
                .pullOutLayout();
        JFXButton btn_revert = searchAccessibilityText(programRowExtension, "revert");
        /**
         * Button Revert to get original state.
         */
//        btn_revert.setText("Revert");
//        btn_revert.getStyleClass().add("btn-change");

        /**
         * Revert event.
         */
        btn_revert.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            // when reverted back to normal state.

            SubjectInformationHolder old = (SubjectInformationHolder) row.getRowMetaData().get(KEY_SUB_INFO);
//            for (int i = 0; i < changedSubjects.size(); i++) {
//                SubjectInformationHolder subjInfoHolder = (SubjectInformationHolder) changedSubjects.get(i);
//                if (Objects.equals(subjInfoHolder.getSubjectMap().getId(), old.getSubjectMap().getId())) {
//                    changedSubjects.remove(subjInfoHolder);
//                }
//            }
            validateSubject("ORIGINAL", row, old);
//            row.getRowMetaData().put(KEY_ROW_STATUS, "ORIGINAL");
//            row.getStyleClass().remove("table-mutate-as-removed");
//            row.hideExtension();
//            createDefaultRowExtension(table, row);
//            row.showExtension();
//            // refresh total values.
//            invokeListener(table);
        });

//        removedExtension.getButton_container().getChildren().add(btn_revert);
//        removedExtension.create(row);
        row.setRowExtension(programRowExtension);
        /**
         * Invoke the listener to update the number of units and subjects.
         */
        invokeListener(table);
    }

    /**
     * Default row extension. from original subjects.
     *
     * @param table table where called
     * @param row contained in this row.
     */
    private void createDefaultRowExtension(SimpleTable table, SimpleTableRow row) {
//        RowExtensionFactory defaultExtension = new RowExtensionFactory();
//        defaultExtension.imagePath = "res/img/ext_info.png";
//        defaultExtension.message = "You can only change subjects with the same amount"
//                + " of Lab and Lec Units, You can also remove this subject and"
//                + " replace it with another one, or just unload this subject "
//                + "without replacement.";
//
//        JFXButton btn_remove = new JFXButton();
//        JFXButton btn_change = new JFXButton();
//        /**
//         * Change
//         */
//        btn_change.setText("Change");
//        btn_change.getStyleClass().add("btn-change");

        HBox programRowExtension = (HBox) Mono.fx().create()
                .setPackageName("update.org.cict.layout.adding_changing")
                .setFxmlDocument("default-extension")
                .makeFX()
                .pullOutLayout();
        JFXButton btn_change = searchAccessibilityText(programRowExtension, "change");
        JFXButton btn_remove = searchAccessibilityText(programRowExtension, "remove");

        btn_change.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            changeEvent(table, row);
        });
        /**
         * Removed
         */
//        btn_remove.setText("Remove");
//        btn_remove.getStyleClass().add("btn-remove");

        btn_remove.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            row.hideExtension();
            createRemovedRowExtension(table, row);
            row.showExtension();
        });

//        defaultExtension.getButton_container().getChildren().add(btn_change);
//        defaultExtension.getButton_container().getChildren().add(btn_remove);
//        defaultExtension.create(row);
        row.setRowExtension(programRowExtension);
    }

    private void createChangedRowExtension(SimpleTable table, SimpleTableRow row, SubjectInformationHolder newInfo, SubjectInformationHolder OLD_info) {
        /**
         * Modify row details; when removed.
         */
        // save the old value of this row.
        SubjectInformationHolder oldValue = (SubjectInformationHolder) row.getRowMetaData().get(KEY_SUB_INFO);
        // push the new value.
        row.getRowMetaData().put(KEY_SUB_INFO, newInfo);
        row.getRowMetaData().put(KEY_ROW_STATUS, "CHANGED");
        row.getRowMetaData().put(KEY_OLD_INFO, OLD_info);
        row.getStyleClass().remove("table-mutate-as-default");
        row.getStyleClass().add("table-mutate-as-changed");

        // create UI
//        RowExtensionFactory changedExtension = new RowExtensionFactory();
//        changedExtension.imagePath = "res/img/ext_info.png";
//            changedExtension.message = "This subject was changed if you want to revert to its previous value please click the revert button";
//        JFXButton btn_revert = new JFXButton();
        HBox programRowExtension = (HBox) Mono.fx().create()
                .setPackageName("update.org.cict.layout.adding_changing")
                .setFxmlDocument("changed-extension")
                .makeFX()
                .pullOutLayout();
        JFXButton btn_revert = searchAccessibilityText(programRowExtension, "revert");
        /**
         * Button Revert to get original state.
         */
//        btn_revert.setText("Revert");
//        btn_revert.getStyleClass().add("btn-change");

        /**
         * Revert event.
         */
        btn_revert.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
//            boolean addToList = true;
//            for (int i = 0; i < table.getChildren().size(); i++) {
//                SimpleTableRow tblRow = (SimpleTableRow) table.getChildren().get(i);
//                Label lblCode = (Label) tblRow.getCell(1).getChildren().get(0);
//
//                if (lblCode.getText().equalsIgnoreCase(oldValue.getSubjectMap().getCode())) {
//                    addToList = false;
//                }
//            }
//            if (addToList) {
            validateSubject("REVERT", row, oldValue);
//                // when reverted back to normal state.
//                row.getStyleClass().remove("table-mutate-as-changed");
//                row.getRowMetaData().put(KEY_ROW_STATUS, "REVERT");
//
//                // push the old labels
//                row.getCell(1).<Label>getContent().setText(oldValue.getSubjectMap().getCode());
//                row.getCell(2).<Label>getContent().setText(oldValue.getSubjectMap().getDescriptive_title());
//                row.getCell(7).<Label>getContent().setText(oldValue.getFullSectionName());
//                // push the old value again.
//                row.getRowMetaData().put(KEY_SUB_INFO, oldValue);
//                row.hideExtension();
//                createDefaultRowExtension(table, row);
//                row.showExtension();
//                // refresh total values.
//                invokeListener(table);
//            } else {
//                Mono.fx().alert()
//                        .createInfo()
//                        .setHeader("Subject Exist")
//                        .setMessage("The subject you are trying to revert is already in the list. \n"
//                                + "[" + oldValue.getSubjectMap().getCode() + "]")
//                        .showAndWait();
//            }
        });

//        changedExtension.getButton_container().getChildren().add(btn_revert);
//        changedExtension.create(row);
        row.setRowExtension(programRowExtension);
        /**
         * Invoke the listener to update the number of units and subjects.
         */
        invokeListener(table);
    }

    /**
     *
     * @param table
     * @param row
     */
    private void createAddedRowExtension(SimpleTable table, SimpleTableRow row) {
        /**
         * Modify row details; when removed.
         */
        row.getRowMetaData().put(KEY_ROW_STATUS, "ADDED");
        row.getStyleClass().remove("table-mutate-as-default");
        row.getStyleClass().add("table-mutate-as-added");

        // create UI
//        RowExtensionFactory addedExtension = new RowExtensionFactory();
//        addedExtension.imagePath = "res/img/ext_info.png";
//        addedExtension.message = "This subject was added manually. "
//                + "If you want to remove this, just click the remove button.";
//
//        JFXButton btn_remove = new JFXButton();
        HBox programRowExtension = (HBox) Mono.fx().create()
                .setPackageName("update.org.cict.layout.adding_changing")
                .setFxmlDocument("added-extension")
                .makeFX()
                .pullOutLayout();
        JFXButton btn_remove = searchAccessibilityText(programRowExtension, "remove");

        /**
         * Button Revert to get original state.
         */
//        btn_remove.setText("Remove");
//        btn_remove.getStyleClass().add("btn-remove");
        /**
         * Remove event.
         */
        btn_remove.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            table.getChildren().remove(row);
            invokeListener(table);
        });

//        addedExtension.getButton_container().getChildren().add(btn_remove);
//        addedExtension.create(row);
        row.setRowExtension(programRowExtension);
        /**
         * Invoke the listener to update the number of units and subjects.
         */
        invokeListener(table);
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    /**
     * CHANGE EVENT
     */
    private final String KEY_SUB_INFO = "ROW_INFO";
    private final String KEY_ROW_STATUS = "ROW_STATUS";
    private final String KEY_OLD_INFO = "OLD_INFO";
//    private ArrayList<Object> changedSubjects = new ArrayList<>();
//    private ArrayList<SubjectInformationHolder> savedSubjects = new ArrayList<>();

    /**
     * change event from default row extension.
     *
     * @param table
     * @param row
     */
    private void changeEvent(SimpleTable table, SimpleTableRow row) {
        ChangingSubjects changeSubjects = new ChangingSubjects(studentSearched.getCURRICULUM_id());
        // pass the values
        changeSubjects.setSubjectInfo((SubjectInformationHolder) row.getRowMetaData().get(KEY_SUB_INFO));
        changeSubjects.setStudentNumber(this.studentSearched.getId());
//        addSubjects.setSubjectInTheList(subjectInTheList);
        /**
         * This is the vent that will open the change window.
         */
        Stage change_stage = Mono.fx().create()
                .setPackageName("update.org.cict.layout.adding_changing.changing")
                .setFxmlDocument("changing-subjects")
                .makeFX()
                .setController(changeSubjects)
                .makeScene()
                .makeStage()
                .stageResizeable(false)
                //.stageShow();
                // revise the code execution sequence.
                .pullOutStage();
        // call first the callback before showing the stage
        changeSubjects.callBack();
        // set datapipe as change request.
        AddingDataPipe.instance().isChanged = true;
        // use show and wait instead
        change_stage.showAndWait();
        // this event will be paused
        // this event will resume after the change stage was closed
        // if the stage was closed
        // check if there is an attached value in the data pipe
        if (AddingDataPipe.instance().isChangedValueRecieved) {
            try {
                // if value was recieved
                SubjectInformationHolder subinfo = AddingDataPipe.instance().isChangedValue;
                
                validateSubject("CHANGE", row, subinfo);
                // if value was recieved
//                SubjectInformationHolder subinfo = AddingDataPipe.instance().isChangedValue;
//                // before mutating the row with new values
//                // get the prev subject and change if needed
//                SubjectInformationHolder old = (SubjectInformationHolder) row.getRowMetaData().get(KEY_SUB_INFO);
//                // mutate the row with the new values
//                // please save the old one before mutating.
//
//                // check first if the row is already modified before
//                // if this key is not existing it will give the default "ORIGINAL"
//                String row_status = (String) row.getRowMetaData().getOrDefault(KEY_ROW_STATUS, "ORIGINAL");
//                logs(row_status);
//                if (row_status.equalsIgnoreCase("ORIGINAL")) {
//                    // save old row meta data
//                    boolean canChange = true;
//                    for (int i = 0; i < changedSubjects.size(); i++) {
//                        SubjectInformationHolder subjInfoHolder = (SubjectInformationHolder) changedSubjects.get(i);
//                        if (Objects.equals(subjInfoHolder.getSubjectMap().getId(), old.getSubjectMap().getId())) {
//                            canChange = false;
//                        }
//                    }
//                    if (canChange) {
//                        changedSubjects.add(old);
//                    }
//                } else if (row_status.equalsIgnoreCase("CHANGED")) {
//                    // do not save
//                    // just modify
//                    for (int i = 0; i < originalList.size(); i++) {
//                        SubjectInformationHolder originalSubjectListHolder = (SubjectInformationHolder) originalList.get(i);
//                        if (Objects.equals(originalSubjectListHolder.getSubjectMap().getId(), old.getSubjectMap().getId())) {
//                            changedSubjects.add(originalSubjectListHolder);
//                            break;
//                        }
//                    }
//                } else if (row_status.equalsIgnoreCase("REVERT")) {
//                    System.out.println("REVERT STATUS");
//                    for (int i = 0; i < changedSubjects.size(); i++) {
//                        SubjectInformationHolder subjInfoHolder = (SubjectInformationHolder) changedSubjects.get(i);
//                        if (Objects.equals(subjInfoHolder.getSubjectMap().getId(), old.getSubjectMap().getId())) {
//                            changedSubjects.remove(subjInfoHolder);
//                        }
//                    }
//                }
//
//                /**
//                 * check first if the subject changed is already in the table;
//                 */
////                boolean addToList = true;
////                for (int i = 0; i < table.getChildren().size(); i++) {
////                    SimpleTableRow tblRow = (SimpleTableRow) table.getChildren().get(i);
////                    Label lblCode = (Label) tblRow.getCell(1).getChildren().get(0);
////                    if (lblCode.getText().equalsIgnoreCase(subinfo.getSubjectMap().getCode())) {
////                        addToList = false;
////                    }
////                }
////                System.out.println(subinfo.getSubjectMap().getCode());
////                if (addToList) {
//                    //make sure to get the right index when mutating the rw
//                    // subject code and this is a label
//                    row.getCell(1).<Label>getContent().setText(subinfo.getSubjectMap().getCode());
//                    // subject title
//                    row.getCell(2).<Label>getContent().setText(subinfo.getSubjectMap().getDescriptive_title());
//                    // since change requires the same units we do not need to mutate the units column
//                    // section column
//                    row.getCell(7).<Label>getContent().setText(subinfo.getFullSectionName());
//
//                    // changing the row meta data
//                    /**
//                     * This line
//                     *
//                     * @line row.getRowMetaData().put(KEY_SUB_INFO, subinfo); //
//                     * the old value will be over written.
//                     *
//                     * was transferred to
//                     * @function createChangedRowExtension(table, row);
//                     */
//                    // add another meta data that this row is changed and not part as the original subjects
//                    // when the row is not part of the original subjects
//                    // when this subject is being changed again
//                    // it does not need to saved its values before mutating
//                    // saving the old values from the original are required
//                    // to execute changes in the database.
//                    // if you add a subject that does not belonged to the original list
//                    // it will result to an error
//                    // skip this row in changing if not original data
//                    /**
//                     * This line
//                     *
//                     * @line row.getStyleClass().add("table-mutate-as-changed");
//                     *
//                     * was transferred to
//                     *
//                     * @function createChangedRowExtension(table, row);
//                     */
//                    // fancy stuffs
//                    // create a css class as changed in adding-table
//                    // then add this class to this row to change it's color
//                    // you can use different colors for different row status
//                    // instead of deleting the row in the table when remove is clicked
//                    // just add a status of REMOVED
//                    // and change it's background to red
//                    /**
//                     * Allow changing of this subject.
//                     */
//                    row.hideExtension();
//                    createChangedRowExtension(table, row, subinfo);
//                    row.showExtension();

                //----------------------------------------------------------
                //----------------------------------------------------------
//                } else {
//                    Mono.fx().alert()
//                            .createInfo()
//                            .setHeader("Subject Exist")
//                            .setMessage("The subject you are trying to add is already in the list.")
//                            .showAndWait();
//                }
            } catch (NullPointerException a) {
                System.err.println("IM AN ERROR IN @ADDING HOME");
                a.printStackTrace();
            }

//            savedSubjects.clear();
//            for (int i = 0; i < table.getChildren().size(); i++) {
//                SimpleTableRow tblRow = (SimpleTableRow) table.getChildren().get(i);
//                savedSubjects.add((SubjectInformationHolder) tblRow.getRowMetaData().get(KEY_SUB_INFO));
//            }
//
//            for (int i = 0; i < savedSubjects.size(); i++) {
//                SubjectInformationHolder subIH = savedSubjects.get(i);
//                for (int j = 0; j < changedSubjects.size(); j++) {
//                    SubjectInformationHolder subIH2 = (SubjectInformationHolder) changedSubjects.get(j);
//                    if (Objects.equals(subIH2.getSubjectMap().getId(), subIH.getSubjectMap().getId())) {
//                        changedSubjects.remove(subIH2);
//                    }
//                }
//            }
            //reset data pipe
            AddingDataPipe.instance().resetIsChanged();
        }

    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    /**
     * Since the table is bound to a listener to compute for total units and
     * subject count. the latter method will only be invoke upon change of the
     * table's children
     *
     * @param table
     */
    private void invokeListener(SimpleTable table) {
        /**
         * This imaginary element will be ignored. used to invoke only the
         * change listener.
         */
        SimpleTableRow pseudoRow = new SimpleTableRow();
        pseudoRow.getRowMetaData().put(KEY_ROW_STATUS, "REMOVED");
        table.addRow(pseudoRow);
        table.getChildren().remove(pseudoRow);
    }

    private Double totalSubjects;
    private Double totalUnitsAll;

    private void tableChangeListener(SimpleTable table) {
        try {
            table.getRows().addListener((ListChangeListener<Node>) c -> {
                totalSubjects = 0.0;
                totalUnitsAll = 0.0;

                //totalSubjects = (double) c.getList().size();
                //System.out.println(totalSubjects);
                // get total units
                // sample usage of metadata.
                for (Node node : c.getList()) {
                    SimpleTableRow simplerow = (SimpleTableRow) node;
                    /**
                     * skip from count, subjects that are marked as removed.
                     */
                    String row_status = (String) simplerow.getRowMetaData().getOrDefault(KEY_ROW_STATUS, "COUNTED");
                    if (row_status.equalsIgnoreCase("REMOVED")) {
                        continue;
                    }
                    //
                    SubjectInformationHolder meta = (SubjectInformationHolder) simplerow.getRowMetaData().get(KEY_SUB_INFO);
                    Double total_units = meta.getSubjectMap().getLab_units() + meta.getSubjectMap().getLec_units();
                    totalUnitsAll += total_units;
                    totalSubjects += 1.0;
                }

                //System.out.println(totalUnitsAll);
                lbl_subjectTotal.setText(totalSubjects.toString());
                lbl_unitsTotal.setText(totalUnitsAll.toString());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onShowAddSubject() {
        try {
            // if local registrar allow override.
            allowOverride = Access.isGrantedIf(Access.ACCESS_LOCAL_REGISTRAR);
            
            AddingSubjects addingSubjects = new AddingSubjects();
            addingSubjects.setStudentNumber(studentSearched.getId(), studentSearched.getCURRICULUM_id());

            Stage add_stage = Mono.fx().create()
                    .setPackageName("update.org.cict.layout.adding_changing.adding")
                    .setFxmlDocument("adding-subjects")
                    .makeFX()
                    .setController(addingSubjects)
                    .makeScene()
                    .makeStage()
                    .stageResizeable(false)
                    .pullOutStage();

            addingSubjects.callBack();

            AddingDataPipe.instance().isChanged = true;
            add_stage.showAndWait();
            if (AddingDataPipe.instance().isChangedValueRecieved) {
                
                try {
                    // if value was recieved
                    SubjectInformationHolder subinfo = AddingDataPipe.instance().isChangedValue;

                    //---------------------
                    // check if existing before validation
                    if(!checkIfNotExisting(subinfo.getSubjectMap())) {
                        showWarningNotification("Subject Exist", "The subject you are trying to add is already in the list.");
                        AddingDataPipe.instance().resetIsChanged();
                        return;
                    }
                    //--------------------------------------
                    if (subinfo.getSubjectMap().getType().equalsIgnoreCase(SubjectClassification.TYPE_INTERNSHIP)) {
                        if (!ValidateOJT.isValidForOJT(studentSearched)) {
                            
                            Notifications.create().title("Warning")
                                    .position(Pos.BOTTOM_RIGHT)
                                    .text("Student Number: " + studentSearched.getId()
                                            + "\nCannot take Internship based on the student's current status."
                                            + "\nClick This notification for more details.")
                                    .onAction(onAction -> {
                                        this.systemOverride(INTERNSHIP_WITH_OTHERS, subinfo, "add", null, null);
                                    })
                                    .showWarning();
                            int res = Mono.fx().alert()
                                    .createConfirmation()
                                    .setHeader("Deficieny Report")
                                    .setMessage("Do you want to print the student's subject with missing grade?")
                                    .confirmYesNo();
                            if(res==1) {
                                printDeficiency();
                            }
                            AddingDataPipe.instance().resetIsChanged();
                            return;
                        }
                    }

                    if(AddingDataPipe.instance().isMaxPopulationReached) {
                        Notifications.create().title("Max Population Reached")
                                .position(Pos.BOTTOM_RIGHT)
                                .text("Section reached the maximum population."
                                        + "\nClick This notification for more details.")
                                .onAction(onAction -> {
                                    this.systemOverride(MAX_POPULATION, subinfo, "add", null, null);
                                })
                                .showWarning();
                        return;
                    }
                    validateSubject("ADD", null, subinfo);
                } catch (NullPointerException a) {
                    a.printStackTrace();
                }
            }
        } catch (NullPointerException a) {
            Mono.fx().alert()
                    .createInfo()
                    .setHeader("No Student Selected")
                    .setMessage("Please search the student first by their student number"
                            + " before starting the add process.")
                    .showAndWait();
        }
    }
    
    private void printDeficiency() {
        PrintDeficiency print = new PrintDeficiency();
        print.CICT_id = studentSearched.getCict_id();
        print.whenSuccess(()->{
            Notifications.create()
                    .title("Nearly there.")
                    .text("Printing the deficiency report.")
                    .showInformation();
        });
        print.whenCancelled(()->{
            Notifications.create()
                    .title("Request Cancelled")
                    .text("Sorry for the inconviniece.")
                    .showWarning();
        });
        print.whenFailed(()->{
            Notifications.create()
                    .title("Request Failed")
                    .text("Something went wrong. Sorry for the inconviniece.")
                    .showInformation();
        });
        print.setDocumentFormat(ReportsUtility.paperSizeChooser(this.getStage()));
        print.transact();
    }
    
    /**
     * Override filters.
     */
    private boolean allowOverride = false;

    /**
     * Override Operations
     */
    private final String EXCEED_MAX_UNITS = SystemOverriding.EVAL_EXCEED_MAX_UNITS;
    private final String INTERNSHIP_WITH_OTHERS = SystemOverriding.EVAL_INTERNSHIP_WITH_OTHERS;
    private final String BYPASSED_PRE_REQUISITES = SystemOverriding.EVAL_BYPASSED_PRE_REQUISITES;
    private final String INTERN_GRADE_REQUIREMENT = SystemOverriding.EVAL_INTERN_GRADE_REQUIREMENT;
    
    private final String MAX_POPULATION = SystemOverriding.EVAL_EXCEED_MAX_POPULATION;

    private SubjectInformationHolder subinfo_CHANGE_SUBJECT;
    
    private void validateSubject(String mode, SimpleTableRow row, SubjectInformationHolder subinfo) {
        
        // if local registrar allow override.
        allowOverride = Access.isGrantedIf(Access.ACCESS_LOCAL_REGISTRAR);
        
        /**
         * validate the subject before anything else
         */
        ValidateAddingSubject validate = new ValidateAddingSubject();
        validate.studentCICT_id = this.studentSearched.getCict_id();
        SubjectMapping subjectToBeValidated =  subinfo.getSubjectMap();
        validate.subjectID = subjectToBeValidated.getId();

        try {
            subinfo_CHANGE_SUBJECT = (SubjectInformationHolder) row.getRowMetaData().get(KEY_SUB_INFO);
        } catch (Exception e) {
        }
        validate.setOnSuccess(onSuccess -> {

            SubjectMapping validatedSubject = subinfo.getSubjectMap();
            //check if subject exist
            boolean notExist = true,
                    isAllSubjectsValid = true,
                    majorSubjectExist = false,
                    isInternshipExist = false;
            //count all the subjects in the list
            int count = 0;
            SubjectInformationHolder subinfo_CURRENT_SUBJECT = null;
            if (mode.equalsIgnoreCase("REVERT")) {
                subinfo_CURRENT_SUBJECT = (SubjectInformationHolder) row.getRowMetaData().get(KEY_SUB_INFO);
            }
            for (int i = 0; i < table.getChildren().size(); i++) {
                SimpleTableRow currentTableRow = (SimpleTableRow) table.getChildren().get(i);
                SubjectInformationHolder subInfoOfCurrentRow = (SubjectInformationHolder) currentTableRow.getRowMetaData().get(KEY_SUB_INFO);
                SubjectMapping subjectInTheList = subInfoOfCurrentRow.getSubjectMap();
                String row_status = currentTableRow.getRowMetaData().get(KEY_ROW_STATUS).toString();
                if (!row_status.equalsIgnoreCase("REMOVED")) {
                    if (Objects.equals(validatedSubject.getId(), subjectInTheList.getId())) {
                        notExist = false;
                    }
                    count++;
                    if (!subjectInTheList.getType().equalsIgnoreCase(SubjectClassification.TYPE_ELECTIVE)
                            && !subjectInTheList.getType().equalsIgnoreCase(SubjectClassification.TYPE_MINOR)) {
                        //if revert, do not count as invalid if the subject found is the subject to be reverted
                        if (mode.equalsIgnoreCase("REVERT")) {
                            if (!Objects.equals(subjectInTheList.getId(), subinfo_CURRENT_SUBJECT.getSubjectMap().getId())) {
                                isAllSubjectsValid = false;
                            }
                        } else {
                            isAllSubjectsValid = false;
                        }
                    }
//                    if(SubjectClassification.isMajor(subjectInTheList.getType())) {
//                        if(mode.equalsIgnoreCase("REVERT")) {
//                            if(!Objects.equals(subjectInTheList.getId(), subinfo_CURRENT_SUBJECT.getSubjectMap().getId())) {
//                                majorSubjectExist = true;
//                            }
//                        } else
//                            majorSubjectExist = true;
//                    }
                    if (subjectInTheList.getType().equalsIgnoreCase(SubjectClassification.TYPE_INTERNSHIP)) {
                        isInternshipExist = true;
                    }
                }
            }
            logs("******************************************* START");
            logs("IS INTERNSHIP EXIST? " + isInternshipExist);
            logs("DOES A MAJOR SUBJECT EXIST? " + majorSubjectExist);
            logs("IS ALL SUBJECT VALID? " + isAllSubjectsValid);
            logs("COUNT: " + count);
            logs("MODE: " + mode);
            if (notExist) {
                logs("SUBJECT " + validatedSubject.getCode() + " IS NOT EXISTING");
                
                
                boolean isAdd = false;
                if(mode.equalsIgnoreCase("add"))
                    isAdd = true;
                boolean stopTransaction = isOverMaxUnits(subinfo, totalUnitsAll, subjectToBeValidated.getLab_units() + subjectToBeValidated.getLec_units(), PublicConstants.MAX_UNITS, isAdd, mode, row);
                if(stopTransaction)
                    return;
                
                if (mode.equalsIgnoreCase("ADD")) {
                    SimpleTableView tableView = (SimpleTableView) this.vbox_tableHandler.getChildren().get(0);
                    
                    if (validatedSubject.getType().equalsIgnoreCase(SubjectClassification.TYPE_INTERNSHIP)) {
                        logs("VALIDATED SUBJECT IS A TYPE INTERNSHIP");
                        if (count == 1) {
                            for (int i = 0; i < table.getChildren().size(); i++) {
                                SimpleTableRow currentTableRow = (SimpleTableRow) table.getChildren().get(i);
                                String row_status = (String) currentTableRow.getRowMetaData().get(KEY_ROW_STATUS);
                                if (!row_status.equalsIgnoreCase("REMOVED")) {
                                    SubjectInformationHolder subInfoOfCurrentRow = (SubjectInformationHolder) currentTableRow.getRowMetaData().get(KEY_SUB_INFO);
                                    SubjectMapping subjectInTheList = subInfoOfCurrentRow.getSubjectMap();
                                    if (SubjectClassification.isMajor(subjectInTheList.getType())) {
                                        //invalid
                                        logs("1");
                                        Notifications.create()
                                                .title("Warning")
                                                .text("Cannot take Internship with a Major Subject."
                                                        + "\nClick for more details.")
                                                .onAction(a -> {
                                                    this.systemOverride(this.INTERNSHIP_WITH_OTHERS, subinfo, mode, null, null);
                                                })
                                                .position(Pos.BOTTOM_RIGHT).showWarning();
                                        return;
                                    }
                                }
                            }
                        } else if (count == 0) {
                            //valid
                            logs("3");
                        } else {
                            //invalid
                            logs("4");

                            Notifications.create()
                                    .title("Warning")
                                    .text("Internship can only be taken with"
                                    + "\n 1 Minor or Elective subject."
                                            + "\nClick for more details.")
                                    .onAction(a -> {
                                        this.systemOverride(this.INTERNSHIP_WITH_OTHERS, subinfo, mode, null, null);
                                    })
                                    .position(Pos.BOTTOM_RIGHT).showWarning();
                            return;
                        }
                    } else if (isInternshipExist) {
                        if (count > 1) {
                            //invalid
                            logs("5");    
                            Notifications.create()
                                    .title("Warning")
                                    .text("Internship can only be taken with"
                                    + "\n 1 Minor or Elective subject."
                                            + "\nClick for more details.")
                                    .onAction(a -> {
                                        this.systemOverride(this.INTERNSHIP_WITH_OTHERS, subinfo, mode, null, null);
                                    })
                                    .position(Pos.BOTTOM_RIGHT).showWarning();
                            return;
                        } else {
                            if (SubjectClassification.isMajor(validatedSubject.getType())) {
                                //invalid
                                logs("6");    
                                Notifications.create()
                                        .title("Warning")
                                        .text("Cannot take Internship with a Major Subject."
                                                + "\nClick for more details.")
                                        .onAction(a -> {
                                            this.systemOverride(this.INTERNSHIP_WITH_OTHERS, subinfo, mode, null, null);
                                        })
                                        .position(Pos.BOTTOM_RIGHT).showWarning();
                                return;
                            } else {
                                //valid
                                logs("7");
                            }
                        }
                    }
                    createRow(subinfo, true);
                } else if (mode.equalsIgnoreCase("CHANGE")) {
                    int res = 1;
//                    SubjectInformationHolder subinfo_CHANGE_SUBJECT = (SubjectInformationHolder) row.getRowMetaData().get(KEY_SUB_INFO);
                    SubjectMapping OLD_subject = null;
                    OLD_subject = subinfo_CHANGE_SUBJECT.getSubjectMap();
                    if (validatedSubject.getType().equalsIgnoreCase(SubjectClassification.TYPE_INTERNSHIP)) {
                        if (count > 2) {
                            //invalid
                            logs("1");

                            Notifications.create()
                                    .title("Warning")
                                    .text("Internship can only be taken with"
                                    + "\n 1 Minor or Elective subject."
                                            + "\nClick for more details.")
                                    .onAction(a -> {
                                        this.systemOverride(this.INTERNSHIP_WITH_OTHERS, subinfo, mode, row, subinfo_CHANGE_SUBJECT);
                                    })
                                    .position(Pos.BOTTOM_RIGHT).showWarning();
                            return;
                        } else if (count <= 2) {
                            for (int i = 0; i < table.getChildren().size(); i++) {
                                SimpleTableRow currentTableRow = (SimpleTableRow) table.getChildren().get(i);

                                if (row.getId().equalsIgnoreCase(currentTableRow.getId())) {
                                    logs("SKIP");
                                    continue;
                                }

                                SubjectInformationHolder subInfoOfCurrentRow = (SubjectInformationHolder) currentTableRow.getRowMetaData().get(KEY_SUB_INFO);
                                SubjectMapping subjectInTheList = subInfoOfCurrentRow.getSubjectMap();
                                if (SubjectClassification.isMajor(subjectInTheList.getType())) {
                                    //invalid
                                    showWarningNotification("Warning", "Cannot take Internship with a Major Subject.");
                                    logs("3");    
                                    Notifications.create()
                                            .title("Warning")
                                            .text("Cannot take Internship with a Major Subject."
                                                    + "\nClick for more details.")
                                            .onAction(a -> {
                                                this.systemOverride(this.INTERNSHIP_WITH_OTHERS, subinfo, mode, row, subinfo_CHANGE_SUBJECT);
                                            })
                                            .position(Pos.BOTTOM_RIGHT).showWarning();
                                    return;
                                } else {
                                    //valid
                                }
                            }
                        } else {
                            //valid
                            logs("5");
                        }
                    }

                    if (OLD_subject.getType().equalsIgnoreCase(SubjectClassification.TYPE_INTERNSHIP)
                            && isInternshipExist) {
                        isInternshipExist = false;
                        //valid
                        logs("8");
                    }

                    if (isInternshipExist) {
                        if (count > 2) {
                            //invalid
                            logs("5"); 
                            Notifications.create()
                                    .title("Warning")
                                    .text("Internship can only be taken with"
                                    + "\n 1 Minor or Elective subject."
                                            + "\nClick for more details.")
                                    .onAction(a -> {
                                        this.systemOverride(this.INTERNSHIP_WITH_OTHERS, subinfo, mode, row, subinfo_CHANGE_SUBJECT);
                                    })
                                    .position(Pos.BOTTOM_RIGHT).showWarning();
                            return;
                        } else {
                            if (SubjectClassification.isMajor(validatedSubject.getType())) {
                                //invalid
                                logs("6");  
                                Notifications.create()
                                        .title("Warning")
                                        .text("Cannot take Internship with a Major Subject."
                                                + "\nClick for more details.")
                                        .onAction(a -> {
                                            this.systemOverride(this.INTERNSHIP_WITH_OTHERS, subinfo, mode, row, subinfo_CHANGE_SUBJECT);
                                        })
                                        .position(Pos.BOTTOM_RIGHT).showWarning();

                                return;
                            } else {
                                //valid
                                logs("7");
                            }
                        }
                    }
                    if (res == 1) {
                        //change here
                        //make sure to get the right index when mutating the row
                        // subject code and this is a label
                        SubjectMapping subject = subinfo.getSubjectMap();
                        double totalNew = subject.getLec_units() + subject.getLab_units();
                        double totalOld = OLD_subject.getLec_units() + OLD_subject.getLab_units();
                        if(totalNew != totalOld) {  
                            if (Access.isDeniedIfNotFrom(
                                Access.ACCESS_LOCAL_REGISTRAR,
                                Access.ACCESS_CO_REGISTRAR)) {
                                Mono.fx().alert().createWarning()
                                        .setHeader("Units Must Be Equal")
                                        .setMessage("Changing of subject requires an equal total"
                                            + "\nnumber of units.")
                                        .show();
                                return;
                            } else {
                                System.out.println("GRANTED");
                            }
                            
                        }
                        
                        SimpleTableCell simplecell = row.getCell(0);
                        HBox simplerowcontent = simplecell.getContent();

                        ImageView simplerowimage = findByAccessibilityText(simplerowcontent, "img_row_extension");
                        Label lbl_code = searchAccessibilityText(simplerowcontent, "code");
                        Label lbl_descriptive_title = searchAccessibilityText(simplerowcontent, "descript");
                        Label lbl_lec = searchAccessibilityText(simplerowcontent, "lec");
                        Label lbl_lab = searchAccessibilityText(simplerowcontent, "lab");
                        Label lbl_section = searchAccessibilityText(simplerowcontent, "section");
                        Label lbl_icon = searchAccessibilityText(simplerowcontent, "letter");
                        
                        lbl_icon.setText(subject.getCode().charAt(0) + "");
                        lbl_code.setText(subject.getCode());
                        lbl_descriptive_title.setText(subject.getDescriptive_title());
                        lbl_lec.setText(subject.getLec_units() + "");
                        lbl_lab.setText(subject.getLab_units() + "");
                        lbl_section.setText(subinfo.getFullSectionName());

                        row.hideExtension();
                        createChangedRowExtension(table, row, subinfo, subinfo_CHANGE_SUBJECT);
                        row.showExtension();
                    }
                } else if (mode.equalsIgnoreCase("REVERT")) {
//                    if (isNormalTransaction) {
                        SubjectMapping OLD_subject = subinfo_CHANGE_SUBJECT.getSubjectMap();
                        if (validatedSubject.getType().equalsIgnoreCase(SubjectClassification.TYPE_INTERNSHIP)) {
                            if (count > 2) {
                                //invalid
//                                showWarningNotification("Warning", "Internship can only be taken with"
//                                        + "\n 1 Minor or Elective subject.");
                                logs("1");
                                 
                                Notifications.create()
                                        .title("Warning")
                                        .text("Internship can only be taken with"
                                        + "\n1 Minor or Elective subject." +
                                                "\nClick for more details.")
                                        .onAction(a -> {
                                            this.systemOverride(this.INTERNSHIP_WITH_OTHERS, subinfo, mode, row, subinfo_CHANGE_SUBJECT);
                                        })
                                        .position(Pos.BOTTOM_RIGHT).showWarning();
                                return;
                            } else if (count <= 2) {
                                for (int i = 0; i < table.getChildren().size(); i++) {
                                    SimpleTableRow currentTableRow = (SimpleTableRow) table.getChildren().get(i);
                                    SubjectInformationHolder subInfoOfCurrentRow = (SubjectInformationHolder) currentTableRow.getRowMetaData().get(KEY_SUB_INFO);
                                    SubjectMapping subjectInTheList = subInfoOfCurrentRow.getSubjectMap();

                                    if (row == currentTableRow) {
                                        logs("SKIP");
                                        continue;
                                    }

                                    if (SubjectClassification.isMajor(subjectInTheList.getType())) {
                                        //invalid
//                                        showWarningNotification("Warning", "Cannot take Internship with a Major Subject.");
                                        logs("3");
                                        Notifications.create()
                                                .title("Warning")
                                                .text("Cannot take Internship with a Major Subject." +
                                                        "\nClick for more details.")
                                                .onAction(a -> {
                                                    this.systemOverride(this.INTERNSHIP_WITH_OTHERS, subinfo, mode, row, subinfo_CHANGE_SUBJECT);
                                                })
                                                .position(Pos.BOTTOM_RIGHT).showWarning();
                                        return;
                                    }
                                }
                            } else {
                                //valid
                                logs("5");
                            }
                        }

                        if (OLD_subject.getType().equalsIgnoreCase(SubjectClassification.TYPE_INTERNSHIP)
                                && isInternshipExist) {
                            isInternshipExist = false;
                            //valid
                            logs("8");
                        }

                        if (isInternshipExist) {
                            if (count > 2) {
                                //invalid
//                                showWarningNotification("Warning", "Internship can only be taken with"
//                                        + "\n 1 Minor or Elective subject.");
                                logs("5");
                                Notifications.create()
                                        .title("Warning")
                                        .text("Internship can only be taken with"
                                        + "\n1 Minor or Elective subject." +
                                                "\nClick for more details.")
                                        .onAction(a -> {
                                            this.systemOverride(this.INTERNSHIP_WITH_OTHERS, subinfo, mode, row, subinfo_CHANGE_SUBJECT);
                                        })
                                        .position(Pos.BOTTOM_RIGHT).showWarning();
                                return;
                            } else {
                                if (SubjectClassification.isMajor(validatedSubject.getType())) {
                                    //invalid
//                                    showWarningNotification("Warning", "Cannot take Internship with a Major Subject.");
                                    logs("6");
                                    Notifications.create()
                                            .title("Warning")
                                            .text("Cannot take Internship with a Major Subject." +
                                                    "\nClick for more details.")
                                            .onAction(a -> {
                                                this.systemOverride(this.INTERNSHIP_WITH_OTHERS, subinfo, mode, row, subinfo_CHANGE_SUBJECT);
                                            })
                                            .position(Pos.BOTTOM_RIGHT).showWarning();
                                    return;
                                } else {
                                    //valid
                                    logs("7");
                                }
                            }
                        }
//                    }

                    // when reverted back to normal state.
                    row.getStyleClass().remove("table-mutate-as-changed");
                    row.getStyleClass().add("table-mutate-as-default");
                    row.getRowMetaData().put(KEY_ROW_STATUS, "REVERT");

                    // push the old labels
//                    row.getCell(1).<Label>getContent().setText(subinfo.getSubjectMap().getCode());
//                    row.getCell(2).<Label>getContent().setText(subinfo.getSubjectMap().getDescriptive_title());
//                    row.getCell(4).<Label>getContent().setText(subinfo.getSubjectMap().getLec_units().toString());
//                    row.getCell(6).<Label>getContent().setText(subinfo.getSubjectMap().getLab_units().toString());
//                    row.getCell(7).<Label>getContent().setText(subinfo.getFullSectionName());
                    // push the old value again.
                    row.getRowMetaData().put(KEY_SUB_INFO, subinfo);
                    row.hideExtension();
//                    createDefaultRowExtension(table, row, row.getCell(0).<ImageView>getContent());
                    SimpleTableCell simplecell = row.getCell(0);
                    HBox simplerowcontent = simplecell.getContent();
                    SubjectMapping subject = subinfo.getSubjectMap();
                    Label lbl_code = searchAccessibilityText(simplerowcontent, "code");
                    Label lbl_descriptive_title = searchAccessibilityText(simplerowcontent, "descript");
                    Label lbl_lec = searchAccessibilityText(simplerowcontent, "lec");
                    Label lbl_lab = searchAccessibilityText(simplerowcontent, "lab");
                    Label lbl_section = searchAccessibilityText(simplerowcontent, "section");
                    ImageView simplerowimage = findByAccessibilityText(simplerowcontent, "img_row_extension");

                    lbl_code.setText(subject.getCode());
                    lbl_descriptive_title.setText(subject.getDescriptive_title());
                    lbl_lec.setText(subject.getLec_units() + "");
                    lbl_lab.setText(subject.getLab_units() + "");
                    lbl_section.setText(subinfo.getFullSectionName());
                    createDefaultRowExtension(table, row);
                    row.showExtension();
                    // refresh total values.
                    invokeListener(table);
                } else if (mode.equalsIgnoreCase("ORIGINAL")) {
//                    if (isNormalTransaction) {
                        if (validatedSubject.getType().equalsIgnoreCase(SubjectClassification.TYPE_INTERNSHIP)) {
                            if (count > 1) {
                                //invalid
                                logs("1");
//                                showWarningNotification("Warning", "Internship can only be taken with"
//                                        + "\n 1 Minor or Elective subject.");
                                Notifications.create()
                                        .title("Warning")
                                        .text("Internship can only be taken with 1 Minor or "
                                                + "\nElective subject. Click for more details.")
                                        .onAction(a -> {
                                            this.systemOverride(this.INTERNSHIP_WITH_OTHERS, subinfo, mode, row, null);
                                        })
                                        .position(Pos.BOTTOM_RIGHT).showWarning();
                                return;
                            } else if (count == 1) {
                                for (int i = 0; i < table.getChildren().size(); i++) {
                                    SimpleTableRow currentTableRow = (SimpleTableRow) table.getChildren().get(i);
                                    String row_status = (String) currentTableRow.getRowMetaData().get(KEY_ROW_STATUS);
                                    if (!row_status.equalsIgnoreCase("REMOVED")) {
                                        SubjectInformationHolder subInfoOfCurrentRow = (SubjectInformationHolder) currentTableRow.getRowMetaData().get(KEY_SUB_INFO);
                                        SubjectMapping subjectInTheList = subInfoOfCurrentRow.getSubjectMap();
                                        if (SubjectClassification.isMajor(subjectInTheList.getType())) {
                                            //invalid
//                                            showWarningNotification("Warning", "Cannot take Internship with a Major Subject.");
                                            logs("2");
                                            Notifications.create()
                                                    .title("Warning")
                                                    .text("Cannot take Internship with a Major Subject." +
                                                            "\nClick for more details.")
                                                    .onAction(a -> {
                                                        this.systemOverride(this.INTERNSHIP_WITH_OTHERS, subinfo, mode, row, null);
                                                    })
                                                    .position(Pos.BOTTOM_RIGHT).showWarning();
                                            return;
                                        } else {
                                            //valid
                                            logs("3");
                                        }
                                    }
                                }
                            } else {
                                //valid
                                logs("4");
                            }
                        }

                        if (isInternshipExist) {
                            if (count > 1) {
                                //invalid
                                logs("5");
//                                showWarningNotification("Warning", "Internship can only be taken with"
//                                        + "\n 1 Minor or Elective subject.");
                                Notifications.create()
                                        .title("Warning")
                                        .text("Internship can only be taken with 1 Minor or "
                                                + "\nElective subject. Click for more details.")
                                        .onAction(a -> {
                                            this.systemOverride(this.INTERNSHIP_WITH_OTHERS, subinfo, mode, row, null);
                                        })
                                        .position(Pos.BOTTOM_RIGHT).showWarning();
                                return;
                            } else {
                                if (SubjectClassification.isMajor(validatedSubject.getType())) {
                                    //invalid
//                                    showWarningNotification("Warning", "Cannot take Internship with a Major Subject.");
                                    logs("6");
                                    Notifications.create()
                                            .title("Warning")
                                            .text("Cannot take Internship with a Major Subject." +
                                                    "\nClick for more details.")
                                            .onAction(a -> {
                                                this.systemOverride(this.INTERNSHIP_WITH_OTHERS, subinfo, mode, row, null);
                                            })
                                            .position(Pos.BOTTOM_RIGHT).showWarning();
                                    return;
                                } else {
                                    //valid
                                    logs("7");
                                }
                            }
                        }
//                    }

                    row.getRowMetaData().put(KEY_ROW_STATUS, "ORIGINAL");
                    row.getStyleClass().add("table-mutate-as-default");
                    row.getStyleClass().remove("table-mutate-as-removed");
                    row.hideExtension();
//                    SimpleTableCell simplecell = row.getCell(0);
//                    HBox simplerowcontent = simplecell.getContent();
//                    ImageView simplerowimage = findByAccessibilityText(simplerowcontent, "img_row_extension");

                    createDefaultRowExtension(table, row);
                    row.showExtension();
                    // refresh total values.
                    invokeListener(table);
                }

            } else {
                showWarningNotification("Subject Exist", "The subject you are trying to add is already in the list.");
//                Mono.fx().alert()
//                        .createInfo()
//                        .setHeader("Subject Exist")
//                        .setMessage("The subject you are trying to add is already in the list.")
//                        .showAndWait();
            }
            logs("******************************************* END");
            AddingDataPipe.instance().resetIsChanged();

        });

        validate.setOnCancel(onCancel -> {
            if (validate.isAlreadyTaken()) {
                logs("SUBJECT ALREADY TAKEN");
                showWarningNotification("Subject Already Taken", subinfo.getSubjectMap().getCode() + " is already taken.\n"
                        + "Verified For S/N: " + studentSearched.getId() + ", " + studentSearched.getLast_name() + ".");
            } else if (validate.isPreReqNotAllTaken()) {
                logs("INCOMPLETE PRE-REQUISITE SUBJECT/S REQUIREMENT");
                ArrayList<Integer> preReqIds = validate.getPreReqRequiredIds();
                String prereqs = "";
                for (Integer id : preReqIds) {
                    SubjectMapping subject = Mono.orm().newSearch(Database.connect().subject())
                            .eq(DB.subject().id, id)
                            .execute()
                            .first();
                    prereqs += subject.getCode() + " | ";
                }
                int len = prereqs.length();
                prereqs = prereqs.substring(0, len - 3);
//                showWarningNotification("Pre-Requisites Required.", subinfo.getSubjectMap().getCode() + "\n"
//                        + "Verified For S/N: " + studentSearched.getId() + ", " + studentSearched.getLast_name() + ".\n"
//                        + "Requires: " + prereqs);
                
            Notifications.create()
                    .title("Pre-Requisites Required")
                    .text(subinfo.getSubjectMap().getCode() + "\n"
                        + "Verified For S/N: " + studentSearched.getId() + ", " + studentSearched.getLast_name() + ".\n"
                        + "Requires: " + prereqs)
                    .onAction(a -> {
                        this.systemOverride(this.BYPASSED_PRE_REQUISITES, subinfo, mode, row, subinfo_CHANGE_SUBJECT);
                    })
                    .position(Pos.BOTTOM_RIGHT).showWarning();
            }
            AddingDataPipe.instance().resetIsChanged();
        });
        
        validate.transact();
    }
    
    private boolean checkIfNotExisting(SubjectMapping validatedSubject) {
        boolean notExist = true;
        for (int i = 0; i < table.getChildren().size(); i++) {
            SimpleTableRow currentTableRow = (SimpleTableRow) table.getChildren().get(i);
            SubjectInformationHolder subInfoOfCurrentRow = (SubjectInformationHolder) currentTableRow.getRowMetaData().get(KEY_SUB_INFO);
            SubjectMapping subjectInTheList = subInfoOfCurrentRow.getSubjectMap();
            String row_status = currentTableRow.getRowMetaData().get(KEY_ROW_STATUS).toString();
            if (!row_status.equalsIgnoreCase("REMOVED")) {
                if (Objects.equals(validatedSubject.getId(), subjectInTheList.getId())) {
                    notExist = false;
                }
            }
        }
        return notExist;
    }
    
    
    /**
     * IS OVERRIDABLE BY SYSTEM. checks whether if the student is
     * overloaded.
     */
    private boolean isOverMaxUnits(SubjectInformationHolder subjinfo, double unit_count, double getSubjectUnits, double max_units, boolean isAdd, String mode, SimpleTableRow row) {
        if(AddingDataPipe.instance().isChanged)
            return false;
        double total = unit_count;
        if(isAdd) 
            total += getSubjectUnits;
        
        if (total > max_units) {
            String text = subjinfo.getSubjectMap().getCode() + ", Cannot be added."
                    + "\nThis will exceed the maximum units allowed."
                    + "\nClick This notification for more details.";
            Notifications.create()
                    .title("Max Units Reached")
                    .text(text)
                    .onAction(pop -> {
                        this.systemOverride(EXCEED_MAX_UNITS, subjinfo, mode, row, null);
                    })
                    .position(Pos.BOTTOM_RIGHT).showWarning();
            AddingDataPipe.instance().resetIsChanged();
            return true;
        } else {
            return false;
        }
    }
    
    
    
    private void systemOverride(String type, SubjectInformationHolder subinfo, String mode, SimpleTableRow row, SubjectInformationHolder subinfo_CHANGE_SUBJECT) {
        Object[] result = Access.isEvaluationOverride(allowOverride);
        boolean ok = (boolean) result[0];
        String fileName = (String) result[1];
        if (ok) {
            SystemOverrideLogsMapping map = MapFactory.map().system_override_logs();
            map.setCategory(SystemOverriding.CATEGORY_EVALUATION);
            map.setDescription(type);
            map.setExecuted_by(CollegeFaculty.instance().getFACULTY_ID());
            map.setExecuted_date(Mono.orm().getServerTime().getDateWithFormat());
            map.setAcademic_term(SystemProperties.instance().getCurrentAcademicTerm().getId());
            String conforme = studentSearched.getLast_name() + ", ";

            conforme += studentSearched.getFirst_name();
            if (studentSearched.getMiddle_name() != null) {
                conforme += " ";
                conforme += studentSearched.getMiddle_name();
            }
            map.setConforme(conforme);
            map.setConforme_type("STUDENT");
            map.setConforme_id(studentSearched.getCict_id());

            //-----------------
            map.setAttachment_file(fileName);
            //------------
                
            int id = Database.connect().system_override_logs().insert(map);
            if (id <= 0) {
                Mono.fx().snackbar().showError(anchor_add_change, "Something went wrong please try again.");
            } else {
                forceTransact(subinfo, mode, row, subinfo_CHANGE_SUBJECT);
            }
        }
    }
    
    private void forceTransact(SubjectInformationHolder subinfo, String mode, SimpleTableRow row, SubjectInformationHolder subinfo_CHANGE_SUBJECT) {
        if(mode.equalsIgnoreCase("ADD")) {
            createRow(subinfo, true);
        } else if(mode.equalsIgnoreCase("CHANGE")) {
            SubjectMapping subject = subinfo.getSubjectMap();

            SimpleTableCell simplecell = row.getCell(0);
            HBox simplerowcontent = simplecell.getContent();

            ImageView simplerowimage = findByAccessibilityText(simplerowcontent, "img_row_extension");
            Label lbl_code = searchAccessibilityText(simplerowcontent, "code");
            Label lbl_descriptive_title = searchAccessibilityText(simplerowcontent, "descript");
            Label lbl_lec = searchAccessibilityText(simplerowcontent, "lec");
            Label lbl_lab = searchAccessibilityText(simplerowcontent, "lab");
            Label lbl_section = searchAccessibilityText(simplerowcontent, "section");

            lbl_code.setText(subject.getCode());
            lbl_descriptive_title.setText(subject.getDescriptive_title());
            lbl_lec.setText(subject.getLec_units() + "");
            lbl_lab.setText(subject.getLab_units() + "");
            lbl_section.setText(subinfo.getFullSectionName());

            row.hideExtension();
            createChangedRowExtension(table, row, subinfo, subinfo_CHANGE_SUBJECT);
            row.showExtension();
        } else if(mode.equalsIgnoreCase("REVERT")) {
            // when reverted back to normal state.
            row.getStyleClass().remove("table-mutate-as-changed");
            row.getRowMetaData().put(KEY_ROW_STATUS, "REVERT");
            
            row.getRowMetaData().put(KEY_SUB_INFO, subinfo);
            row.hideExtension();
            
            SimpleTableCell simplecell = row.getCell(0);
            HBox simplerowcontent = simplecell.getContent();
            SubjectMapping subject = subinfo.getSubjectMap();
            Label lbl_code = searchAccessibilityText(simplerowcontent, "code");
            Label lbl_descriptive_title = searchAccessibilityText(simplerowcontent, "descript");
            Label lbl_lec = searchAccessibilityText(simplerowcontent, "lec");
            Label lbl_lab = searchAccessibilityText(simplerowcontent, "lab");
            Label lbl_section = searchAccessibilityText(simplerowcontent, "section");
            ImageView simplerowimage = findByAccessibilityText(simplerowcontent, "img_row_extension");

            lbl_code.setText(subject.getCode());
            lbl_descriptive_title.setText(subject.getDescriptive_title());
            lbl_lec.setText(subject.getLec_units() + "");
            lbl_lab.setText(subject.getLab_units() + "");
            lbl_section.setText(subinfo.getFullSectionName());
            createDefaultRowExtension(table, row);
            row.showExtension();
            // refresh total values.
            invokeListener(table);
        } else if (mode.equalsIgnoreCase("ORIGINAL")) {
             row.getRowMetaData().put(KEY_ROW_STATUS, "ORIGINAL");
            row.getStyleClass().remove("table-mutate-as-removed");
            row.hideExtension();
            
            createDefaultRowExtension(table, row);
            row.showExtension();
            // refresh total values.
            invokeListener(table);
        }
        /**
         * Display notifications
         */
        String text = subinfo.getSubjectMap().getCode()
                + "\nOverrided For S/N: "
                + studentSearched.getId() + " ," + studentSearched.getLast_name() + "."
                + "\nThis action was captured and logged.";
        Notifications.create()
                .title("System Rules Override")
                .text(text)
                .position(Pos.BOTTOM_RIGHT).showWarning();
            
    }


    private void showWarningNotification(String title, String message) {
        Notifications.create()
                .title(title)
                .text(message)
                .showWarning();
    }

//    private void createNewRow(SubjectInformationHolder subInfo,
//            SimpleTableView simpleTableView, SimpleTable table) {
//        //create table row
//        SimpleTableRow row = new SimpleTableRow();
//        row.setRowHeight(50.0);
//
//        /**
//         * Row Extension (Optional)
//         */
//        SimpleTableCell cellExtension = new SimpleTableCell();
//        cellExtension.setPrefWidth(100.0);
//        ImageView content_icon = new ImageView();
//        content_icon.setFitWidth(30.0);
//        content_icon.setFitHeight(30.0);
//        content_icon.setImage(new Image("res/img/plus_sign.png"));
//
//        content_icon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
//            onShowExtension(table, row, cellExtension);
//        });
//        //
//        cellExtension.setContent(content_icon);
//        cellExtension.setContentPosition(Pos.CENTER);
//        cellExtension.addContentCssClass("table-cell-content-extend");
//        row.addCell(cellExtension);
//
//        /**
//         * Subject Code
//         */
//        SimpleTableCell cellCode = new SimpleTableCell();
//        cellCode.setPrefWidth(180.0);
//        cellCode.setContent(new Label(subInfo.getSubjectMap().getCode())); // you set this cell as a label
//        // assign css selector
//        cellCode.addContentCssClass("table-cell-content-code");
//        // add cell to row
//        row.addCell(cellCode);
//
//        /**
//         * Subject Description
//         */
//        SimpleTableCell cellDescription = new SimpleTableCell();
//        cellDescription.setResizePriority(Priority.ALWAYS);
//        cellDescription.setContent(new Label(subInfo.getSubjectMap().getDescriptive_title()));
//        // assign css selector
//        cellDescription.addContentCssClass("table-cell-content-description");
//        // add cell to row
//        row.addCell(cellDescription);
//
//        /**
//         * Lec Label
//         */
//        //lec icon
//        ImageView lecIcon = new ImageView();
//        lecIcon.setFitWidth(15.0);
//        lecIcon.setFitHeight(15.0);
//        lecIcon.setImage(new Image("res/img/lec.png"));
//        // icon column
//        SimpleTableCell cellLecLabel = new SimpleTableCell();
//        cellLecLabel.setPrefWidth(30.0);
//        //
//        Label content_lec = new Label();
//        content_lec.setText("LEC");
//        content_lec.setGraphic(lecIcon);
//        content_lec.setContentDisplay(ContentDisplay.TOP);
//        //
//        cellLecLabel.setContent(content_lec);
//        cellLecLabel.addContentCssClass("table-cell-content-image-label");
//        // add cell to row
//        row.addCell(cellLecLabel);
//
//        // lec units
//        SimpleTableCell cellLecUnits = new SimpleTableCell();
//        cellLecUnits.setPrefWidth(50.0);
//        //
//        Label content_lect_units = new Label();
//        content_lect_units.setText(subInfo.getSubjectMap().getLec_units().toString());
//        //
//        cellLecUnits.setContent(content_lect_units);
//        cellLecUnits.addContentCssClass("table-cell-content-units");
//        // add cell to row
//        row.addCell(cellLecUnits);
//
//        /**
//         * Lab Label
//         */
//        //lab icon
//        ImageView labIcon = new ImageView();
//        labIcon.setFitWidth(15.0);
//        labIcon.setFitHeight(15.0);
//        labIcon.setImage(new Image("res/img/lab.png"));
//        // icon column
//        SimpleTableCell cellLabLabel = new SimpleTableCell();
//        cellLabLabel.setPrefWidth(40.0);
//        cellLabLabel.setCellPadding(0.0, 10.0);
//        // node
//        Label content_lab = new Label();
//        content_lab.setText("LAB");
//        content_lab.setGraphic(labIcon);
//        content_lab.setContentDisplay(ContentDisplay.TOP);
//        //
//        cellLabLabel.setContent(content_lab);
//        cellLabLabel.addContentCssClass("table-cell-content-image-label");
//        // add cell to row
//        row.addCell(cellLabLabel);
//
//        // lab units
//        SimpleTableCell cellLabUnits = new SimpleTableCell();
//        cellLabUnits.setPrefWidth(50.0);
//        //
//        Label content_lab_units = new Label();
//        content_lab_units.setText(subInfo.getSubjectMap().getLab_units().toString());
//        //
//        cellLabUnits.setContent(content_lab_units);
//        cellLabUnits.addContentCssClass("table-cell-content-units");
//        // add cell to row
//        row.addCell(cellLabUnits);
//
//        /**
//         * Section Label
//         */
//        //lab icon
//        ImageView secIcon = new ImageView();
//        secIcon.setFitWidth(30.0);
//        secIcon.setFitHeight(30.0);
//        secIcon.setImage(new Image("res/img/Google Classroom_96px.png"));
//        // icon column
//        SimpleTableCell cellSection = new SimpleTableCell();
//        cellSection.setPrefWidth(200.0);
//        cellSection.setCellPadding(20.0, 10.0);
//        //
//        Label content_section = new Label();
//        content_section.setText(subInfo.getFullSectionName());
//        content_section.setGraphic(secIcon);
//        //
//        cellSection.setContent(content_section);
//        cellSection.addContentCssClass("table-cell-content-section");
//        // add cell to row
//        row.addCell(cellSection);
//
//        /**
//         * Create the extended panel for row.
//         */
//        // lets use Hbox in this example.
//        createAddedRowExtension(table, row);
//
//        // add row to table
//        row.getRowMetaData().put(KEY_SUB_INFO, subInfo);
//        row.getRowMetaData().put(KEY_ROW_STATUS, "ADDED");
//
////        savedSubjects.add(subInfo);
//        table.addRow(row);
//
//        vbox_tableHandler.getChildren().clear();
//
//        simpleTableView.setParentOnScene(vbox_tableHandler);
//    }
    private void printAddChangeForm(String onStartTitle, String onStartMessage) {
        PrintAddingChangingForm print = new PrintAddingChangingForm();
        print.evaluationID = newEvaluationID;
        print.studentID = this.studentSearched.getCict_id();

        print.setOnStart(onStart -> {
            if (onStartTitle != null) {
                Notifications.create()
                        .title(onStartTitle)
                        .text(onStartMessage)
                        .showInformation();
            }
        });
        print.setOnSuccess(onSuccess -> {
            Notifications.create()
                    .title("Nearly there")
                    .text("Printing the adding and changing letter.")
                    .showInformation();
            setView("home");
        });

        print.setOnFailure(onFailure -> {
            logs("FAILED PRINTING ADDING/CHANGING FORM");
        });

        print.setOnCancel(onCancel -> {
            Notifications.create()
                    .title("No Changes Made")
                    .text("Nothing to print for adding and changing.")
                    .showInformation();
        });

        print.transact();
    }

    private void setView(String view) {
        switch (view) {
            case "home":
//                this.studentSearched = null;
//                this.anchor_results.setVisible(true);
//                this.hbox_search.setVisible(true); // search
//                this.btnFind.setDisable(false);
//                this.btn_winAdd.setDisable(true);
                
                Animate.fade(anchor_results, 150, ()->{
                    reset();
                    this.studentSearched = null;
                    this.anchor_results.setVisible(true);
                    this.hbox_search.setVisible(true); // search
                    this.btnFind.setDisable(false);
                    this.btn_winAdd.setDisable(true);
                }, this.anchor_preview, this.anchor_results);
                break;

            case "search":
//                this.anchor_results.setVisible(true);
//                this.hbox_loading.setVisible(true); // search
//                this.btnFind.setDisable(true);
//                this.btn_winAdd.setDisable(true);
                
                Animate.fade(anchor_results, 150, ()->{
                    reset();
                    this.anchor_results.setVisible(true);
                    this.hbox_loading.setVisible(true); // search
                    this.btnFind.setDisable(true);
                    this.btn_winAdd.setDisable(true);
                }, this.anchor_preview, this.anchor_results);
                break;

            case "no_results":
//                this.btn_winAdd.setDisable(true);
//                this.anchor_results.setVisible(true);
//                this.hbox_none.setVisible(true); // search
                
                Animate.fade(anchor_results, 150, ()->{
                    reset();
                    this.btn_winAdd.setDisable(true);
                    this.anchor_results.setVisible(true);
                    this.hbox_none.setVisible(true); // search
                }, this.anchor_preview, this.anchor_results);
                break;

            case "preview":
//                anchor_add_change.setDisable(false);
//                this.anchor_preview.setVisible(true); 
                
                Animate.fade(anchor_preview, 150, ()->{
                    reset();
                    anchor_add_change.setDisable(false);
                    this.anchor_preview.setVisible(true); 
                }, this.anchor_preview, this.anchor_results);
                break;
            case "already":
//                this.anchor_results.setVisible(true);
//                this.hbox_already.setVisible(true); // search
//                this.btn_winAdd.setDisable(true);
                
                Animate.fade(anchor_results, 150, ()->{
                    reset();
                    this.anchor_results.setVisible(true);
                    this.hbox_already.setVisible(true); // search
                    this.btn_winAdd.setDisable(true);
                }, this.anchor_preview, this.anchor_results);
                break;
        }
    }
    
    private void reset() {
        this.btnFind.setDisable(false);
        this.btn_winAdd.setDisable(false);
        this.anchor_preview.setVisible(false);
        this.anchor_results.setVisible(false);
        this.hbox_loading.setVisible(false); // loading
        this.hbox_search.setVisible(false); // search
        this.hbox_none.setVisible(false); // no results
        this.hbox_already.setVisible(false);
        vbox_studentOptions.setVisible(false);
    }
    
    private void setImageView() {
        StudentProfileMapping spMap = null;
        if(this.studentSearched.getHas_profile().equals(1)) {
            spMap = Mono.orm().newSearch(Database.connect().student_profile())
                    .eq(DB.student_profile().STUDENT_id, this.studentSearched.getCict_id())
                    .active(Order.desc(DB.student_profile().id)).first();
        }
        String studentImage = (spMap==null? null: spMap.getProfile_picture());
        if (studentImage == null
            || studentImage.isEmpty()
            || studentImage.equalsIgnoreCase("NONE")) {
            ImageUtility.addDefaultImageToFx(img_profile, 0, this.getClass());
        } else {
            ImageUtility.addImageToFX("temp/images/profile", "student_avatar", studentImage, img_profile, 0);
        }
    }
}

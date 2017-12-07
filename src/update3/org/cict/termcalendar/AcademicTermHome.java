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
package update3.org.cict.termcalendar;

import app.lazy.models.AcademicTermMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.LoadGroupMapping;
import app.lazy.models.LoadGroupScheduleMapping;
import app.lazy.models.LoadSectionMapping;
import app.lazy.models.LoadSubjectMapping;
import app.lazy.models.utils.DateString;
import app.lazy.models.utils.FacultyUtility;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import com.jhmvin.fx.async.TransactionException;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.LayoutDataFX;
import com.jhmvin.fx.display.SceneFX;
import com.melvin.mono.fx.bootstrap.M;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.lang3.text.WordUtils;
import org.cict.GenericLoadingShow;
import org.cict.MainApplication;
import org.cict.ThreadMill;
import org.cict.accountmanager.AccountManager;
import org.cict.accountmanager.Logout;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.cict.authentication.authenticator.SystemProperties;
import org.controlsfx.control.Notifications;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import update.org.cict.controller.home.Home;
import update3.org.cict.access.Access;
import update3.org.cict.access.AssistantRegistrarOverride;

/**
 *
 * @author Jhon Melvin
 */
public class AcademicTermHome extends SceneFX implements ControllerFX {

    @FXML
    private VBox application_root;

    @FXML
    private JFXButton btn_home;

    @FXML
    private Label lbl_evaluation_status;

    @FXML
    private Label lbl_eval_service_updater;

    @FXML
    private Label lbl_encoding_service_updater;

    @FXML
    private Label lbl_eval_service_date;

    @FXML
    private JFXButton btn_evaluation_service;

    @FXML
    private Label lbl_adding_status;

    @FXML
    private Label lbl_adding_service_updater;

    @FXML
    private Label lbl_adding_service_date;

    @FXML
    private JFXButton btn_adding_service;

    @FXML
    private Label lbl_encoding_status;

    @FXML
    private Label lbl_encoding_service_date;

    @FXML
    private JFXButton btn_encoding_service;

    @FXML
    private VBox vbox_home_term;

    @FXML
    private Label lbl_current_term;

    @FXML
    private JFXButton btn_change_term;

    @FXML
    private VBox vbox_change_term;

    @FXML
    private JFXButton btn_cancel_request;

    @FXML
    private VBox vbox_request_term;

    @FXML
    private Label lbl_school_year;

    @FXML
    private Label lbl_semester;

    @FXML
    private JFXButton btn_accept;

    @FXML
    private JFXButton btn_decline;

    @FXML
    private Label lbl_school_year1;

    @FXML
    private Label lbl_semester1;

    public AcademicTermHome() {
        //
    }

    private LayoutDataFX dataFx;

    public void setDataFx(LayoutDataFX dataFx) {
        this.dataFx = dataFx;
    }

    @Override
    public void onInitialization() {
        super.bindScene(application_root);

        // check request for acad term change
        if (Access.isGrantedIf(Access.ACCESS_CO_REGISTRAR, Access.ACCESS_LOCAL_REGISTRAR)) {
            this.checkTermRequest();
        } else {
            this.changeAcadTermView(vbox_home_term);
        }

        // set current term
        this.setCurrentTerm();

        //----------------------------------------------------------------------
        AcademicTermMapping currentAT = SystemProperties.instance().getCurrentAcademicTerm();
        //----------------------------------------------------------------------
        if (current == null) {
            this.btn_adding_service.setDisable(true);
            this.btn_encoding_service.setDisable(true);
            this.btn_evaluation_service.setDisable(true);
            return; // do not check status
        }

        // check services status
        this.checkStatus();

    }

    @Override
    public void onEventHandling() {
        super.addClickEvent(btn_home, () -> {
            Home.callHome(this);
        });

        super.addClickEvent(btn_evaluation_service, () -> {
            if (lbl_evaluation_status.getText().equalsIgnoreCase("OFFLINE")) {
                Boolean checked = this.checkIfTransactionIsOn(1);
                if (checked) {
                    int res = Mono.fx().alert().createConfirmation()
                            .setMessage("Adding & Changing service is currently online, this request will turn it as offline. Do you still want to continue?")
                            .confirmYesNo();
                    if (res == -1) {
                        return;
                    }
                }
                changeStatus(Boolean.TRUE, checked ? Boolean.FALSE : null, null, Boolean.FALSE);
            } else {
                changeStatus(Boolean.FALSE, null, null, Boolean.FALSE);
            }
        });

        super.addClickEvent(btn_adding_service, () -> {
            if (lbl_adding_status.getText().equalsIgnoreCase("OFFLINE")) {
                Boolean checked = this.checkIfTransactionIsOn(0);
                if (checked) {
                    int res = Mono.fx().alert().createConfirmation()
                            .setMessage("Evaluation service is currently online, this request will turn it as offline. Do you still want to continue?")
                            .confirmYesNo();
                    if (res == -1) {
                        return;
                    }
                }
                changeStatus(checked ? Boolean.FALSE : null, Boolean.TRUE, null, Boolean.FALSE);
            } else {
                changeStatus(null, Boolean.FALSE, null, Boolean.FALSE);
            }
        });

        super.addClickEvent(btn_encoding_service, () -> {
            if (lbl_encoding_status.getText().equalsIgnoreCase("OFFLINE")) {
                changeStatus(null, null, Boolean.TRUE, Boolean.FALSE);
            } else {
                changeStatus(null, null, Boolean.FALSE, Boolean.FALSE);
            }
        });

        super.addClickEvent(btn_change_term, () -> {
            this.changeTerm();
        });

        super.addClickEvent(btn_cancel_request, () -> {
            this.cancelRequest();
        });

        super.addClickEvent(btn_accept, () -> {
            this.acceptRequest();
        });

        super.addClickEvent(btn_decline, () -> {
            this.declineRequest();
        });
    }

    //------------------------------------------------------------
    //------------------------------------
    private void declineRequest() {
//        if(Access.isGranted(Access.ACCESS_LOCAL_REGISTRAR)) {

//            int res = Mono.fx().alert().createConfirmation()
//                    .setMessage("Are you sure you want to decline the request?")
//                    .confirmYesNo();
//            if(res==-1)
//                return;
        pending.setApproval_state("DECLINED");
        pending.setActive(0);
        if (!Database.connect().academic_term().update(pending)) {
            Notifications.create().darkStyle()
                    .text("Decline Process Failed")
                    .showError();
        } else {
            Notifications.create().darkStyle()
                    .text("Request Rejected")
                    .showInformation();
            this.checkTermRequest();
        }
//        } else
//            Mono.fx().snackbar().showError(application_root, "Request Denied. No access for this process.");
    }

    private void acceptRequest() {
//        if(Access.isGranted(Access.ACCESS_LOCAL_REGISTRAR)) {
        if (pending == null) {
            return;
        } else {
            int res = Mono.fx().alert().createConfirmation()
                    .setMessage("This will change the current Academic Term and will automatically logout the account. Do you still want to continue?")
                    .confirmYesNo();
            if (res == -1) {
                this.declineRequest();
            }

            ChangeCurrentTerm reset = new ChangeCurrentTerm();
            reset.current_term = current;
            reset.request = pending;
            btn_accept.setDisable(true);
            btn_decline.setDisable(true);
            reset.whenStarted(() -> {
                GenericLoadingShow.instance().show();
            });
            reset.whenFailed(() -> {
                GenericLoadingShow.instance().hide();
                Notifications.create().darkStyle()
                        .title("Oh no! Something went wrong.")
                        .text("Change of Academic Term declined.").showError();
            });
            reset.whenCancelled(() -> {
                GenericLoadingShow.instance().hide();
                if (reset.getLog() == null) {
                    return;
                }
                Notifications.create().darkStyle()
                        .text(reset.getLog()).showError();
            });
            reset.whenSuccess(() -> {
                GenericLoadingShow.instance().hide();
                this.setCurrentTerm();
                this.checkTermRequest();
                Notifications.create().darkStyle()
                        .title("Successfully Accepted")
                        .text("New Academic Term is set.").showInformation();
                this.onLogout();
            });
            reset.transact();
        }
//        } else 
//            Mono.fx().snackbar().showError(application_root, "Request Denied. No access for this process.");
    }

    private AcademicTermMapping current;

    private void setCurrentTerm() {
        current = SystemProperties.instance().getCurrentAcademicTerm();
        if (current == null) {
            lbl_current_term.setText("NO CURRENT ACADEMIC TERM FOUND");
        } else {
            lbl_current_term.setText("A.Y. " + (current.getSchool_year() == null ? "Not Set" : current.getSchool_year()) + " " + (current.getSemester() == null ? "Semester Not Specified" : WordUtils.capitalizeFully(current.getSemester())));
        }

    }

    private void changeTerm() {
        if (Access.isGrantedIf(Access.ACCESS_LOCAL_REGISTRAR, Access.ACCESS_CO_REGISTRAR)) {
            ChangeAcademicTerm change = M.load(ChangeAcademicTerm.class);
            change.onDelayedStart();
            try {
                change.getCurrentStage().showAndWait();
            } catch (Exception e) {
                Stage a = change.createChildStage(super.getStage());
                a.initStyle(StageStyle.UNDECORATED);
                a.showAndWait();
                this.checkTermRequest();
            }
        } else {
            Mono.fx().snackbar().showError(application_root, "Request Denied. No access for this process.");
        }
    }

    private AcademicTermMapping pending;

    private void checkTermRequest() {
        pending = Mono.orm().newSearch(Database.connect().academic_term())
                .eq(DB.academic_term().approval_state, "PENDING")
                .active(Order.desc(DB.academic_term().id)).first();
        if (pending == null) {
            this.changeAcadTermView(vbox_home_term);
            btn_adding_service.setDisable(false);
            btn_encoding_service.setDisable(false);
            btn_evaluation_service.setDisable(false);
        } else {
            if (Access.isGrantedIf(Access.ACCESS_LOCAL_REGISTRAR, Access.ACCESS_CO_REGISTRAR)) {
                lbl_school_year1.setText(pending.getSchool_year());
                lbl_semester1.setText(WordUtils.capitalizeFully(pending.getSemester()));
                this.changeAcadTermView(vbox_change_term);
            }/* else if(Access.isGranted(Access.ACCESS_LOCAL_REGISTRAR)) {
                lbl_school_year.setText(pending.getSchool_year());
                lbl_semester.setText(WordUtils.capitalizeFully(pending.getSemester()));
                this.changeAcadTermView(vbox_request_term);
            }*/
            btn_adding_service.setDisable(true);
            btn_encoding_service.setDisable(true);
            btn_evaluation_service.setDisable(true);
            changeStatus(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE);

            if (AssistantRegistrarOverride.isAuthorized(this.getStage(), "ACADTERMCHNGE")) {
                Mono.fx().snackbar().showSuccess(application_root, "Request Granted");
                this.acceptRequest();
            } else {
                Mono.fx().snackbar().showError(application_root, "Request Denied");
                this.cancelRequest();
            }
        }
    }

    private void changeAcadTermView(Node node) {
        vbox_request_term.setVisible(false);
        if (node.getId().equals(vbox_change_term.getId())) {
            vbox_change_term.setVisible(true);
            vbox_home_term.setDisable(true);
        } else if (node.getId().equals(vbox_request_term.getId())) {
            vbox_request_term.setVisible(true);
            vbox_change_term.setVisible(false);
            vbox_home_term.setDisable(true);
        } else {
            vbox_change_term.setVisible(false);
            vbox_home_term.setDisable(false);
        }
        vbox_home_term.setVisible(true);
    }

    private void cancelRequest() {
//        int res = Mono.fx().alert().createConfirmation()
//                .setMessage("Are you sure you want to cancel the request?")
//                .confirmYesNo();
//        if(res==-1)
//            return;

        pending.setActive(0);
        pending.setApproval_state("CANCELLED");
        if (Database.connect().academic_term().update(pending)) {
//            Mono.fx().alert().createInfo()
//                    .setHeader("Successfully Cancelled")
//                    .setMessage("Your prevoius request is cancelled successfully.")
//                    .show();
            this.checkTermRequest();
        } else {
            Mono.fx().alert().createError()
                    .setHeader("Cancel Request Failed")
                    .setMessage("Something went wrong in the process. Please try again later.")
                    .show();
        }
    }

    private void onLogout() {
        Logout logout = AccountManager.instance().createLogout();
        logout.whenStarted(() -> {
        });
        logout.whenCancelled(() -> {
        });
        logout.whenFailed(() -> {
            Notifications.create().darkStyle()
                    .title("Logout Failed")
                    .text("Account not properly logged out.")
                    .showWarning();
        });
        logout.whenSuccess(() -> {
        });
        logout.whenFinished(() -> {
            ThreadMill.threads().shutdown();
            this.getStage().close();
            MainApplication.die(0);
        });
        logout.transact();
    }

    // transaction for accepting the requested and changing the current term
    class ChangeCurrentTerm extends Transaction {

        public AcademicTermMapping request;
        public AcademicTermMapping current_term;

        private String log;

        public String getLog() {
            return log;
        }

        @Override
        protected boolean transaction() {

            // create local session
            Session currentSession = Mono.orm().session();
            // start your transaction
            org.hibernate.Transaction dataTransaction = currentSession.beginTransaction();
            if (request == null) {
                log = null;
                System.out.println("NO REQUESTED ACAD TERM FOUND");
                return false;
            }

            // accept the requested term
            request.setApproval_state("ACCEPTED");
            request.setCurrent(1);
            Boolean accept = Database.connect().academic_term().transactionalSingleUpdate(currentSession, request);
            if (!accept) {
                dataTransaction.rollback();
                log = "Requested Academic Term\n"
                        + "process failed.";
                System.out.println("ACAD TERM REQUEST NOT CHANGED INTO ACCEPTED");
                return false;
            }

            // remove the current term into use
            if(current_term!=null) {
                current_term.setCurrent(0);
                Boolean remove = Database.connect().academic_term().transactionalSingleUpdate(currentSession, current_term);
                if (!remove) {
                    dataTransaction.rollback();
                    log = "Current Academic Term cannot\n"
                            + "process at this moment.";
                    System.out.println("CURRENT ACAD TERM NOT CHANGED INTO [CURRENT = 0]");
                    return false;
                }
            }

            // set all active of the ff into active=0 and archived=1
            // LOAD GROUP
            ArrayList<LoadGroupMapping> loadGrps = Mono.orm().newSearch(Database.connect().load_group())
                    .active().all();
            if (loadGrps != null) {
                for (LoadGroupMapping loadGrp : loadGrps) {
                    loadGrp.setActive(0);
                    loadGrp.setArchived(1);
                    if (!Database.connect().load_group().transactionalSingleUpdate(currentSession, loadGrp)) {
                        dataTransaction.rollback();
                        log = "Archive process of \n"
                                + "load per group failed";
                        System.out.println("LOAD GRP NOT CHANGED INTO [ACTIVE = 0 ; ARCHIVED=1]");
                        return false;
                    }
                }
            }

            // LOAD GROUP SCHEDULE
            ArrayList<LoadGroupScheduleMapping> loadGrpScheds = Mono.orm().newSearch(Database.connect().load_group_schedule())
                    .active().all();
            if (loadGrpScheds != null) {
                for (LoadGroupScheduleMapping loadGrpSched : loadGrpScheds) {
                    loadGrpSched.setActive(0);
                    loadGrpSched.setArchived(1);
                    if (!Database.connect().load_group_schedule().transactionalSingleUpdate(currentSession, loadGrpSched)) {
                        dataTransaction.rollback();
                        log = "Archive process of \n"
                                + "schedules failed";
                        System.out.println("LOAD GRP SCHED NOT CHANGED INTO [ACTIVE = 0 ; ARCHIVED=1]");
                        return false;
                    }
                }
            }

            // LOAD SECTION
            ArrayList<LoadSectionMapping> loadSections = Mono.orm().newSearch(Database.connect().load_section())
                    .active().all();
            if (loadSections != null) {
                for (LoadSectionMapping loadSection : loadSections) {
                    loadSection.setActive(0);
                    loadSection.setArchived(1);
                    if (!Database.connect().load_section().transactionalSingleUpdate(currentSession, loadSection)) {
                        dataTransaction.rollback();
                        log = "Archive process of \n"
                                + "sections failed";
                        System.out.println("LOAD SECTION NOT CHANGED INTO [ACTIVE = 0 ; ARCHIVED=1]");
                        return false;
                    }
                }
            }

            // LOAD SUBJECT
            ArrayList<LoadSubjectMapping> loadSubjects = Mono.orm().newSearch(Database.connect().load_subject())
                    .active().all();
            if (loadSubjects != null) {
                for (LoadSubjectMapping loadSubject : loadSubjects) {
                    loadSubject.setActive(0);
                    // wrong spelling in ARCHIVED
                    loadSubject.setArhived(1);
                    if (!Database.connect().load_subject().transactionalSingleUpdate(currentSession, loadSubject)) {
                        dataTransaction.rollback();
                        log = "Archive process of \n"
                                + "load per student failed";
                        System.out.println("LOAD SUBJECT NOT CHANGED INTO [ACTIVE = 0 ; ARCHIVED=1]");
                        return false;
                    }
                }
            }
            dataTransaction.commit();
            return true;
        }

    }
    //--------------------------------------------------------
    // ---------------------------------

    private void setDisableSwitchButtons(boolean disabled) {
        try {
            if (disabled) {
                super.cursorWait();
            } else {
                super.cursorDefault();
            }
        } catch (NullPointerException e) {
            // never mind
        }

        if (pending == null) {
            btn_adding_service.setDisable(disabled);
            btn_evaluation_service.setDisable(disabled);
            btn_encoding_service.setDisable(disabled);
        }
    }

    private void changeStatus(
            Boolean evaluationService,
            Boolean addingService,
            Boolean encodingService, Boolean forceProcess) {

        ServiceStateChanger changeState = new ServiceStateChanger();
        changeState.setEvaluationService(evaluationService);
        changeState.setAddingService(addingService);
        changeState.setEncodingService(encodingService);
        changeState.forceProcess = forceProcess;

        changeState.whenStarted(() -> {
            this.setDisableSwitchButtons(true);
        });

        changeState.whenCancelled(() -> {
            if ((!forceProcess)) 
                Mono.fx().snackbar().showInfo(application_root, "State was Already Updated.");
        });

        changeState.whenFailed(() -> {
            Mono.fx().snackbar().showInfo(application_root, "State Update has Failed.");
        });

        changeState.whenSuccess(() -> {
            if ((!forceProcess)) {
                Mono.fx().snackbar().showSuccess(application_root, "State Successfully Changed.");
            }
        });

        changeState.whenFinished(() -> {
            this.checkStatus();
        });

        changeState.transact();

    }

    /**
     * Check academic term services status.
     */
    private void checkStatus() {
        ServiceStatusChecker ssc = new ServiceStatusChecker();
        ssc.whenStarted(() -> {
            this.setDisableSwitchButtons(true);
        });

        ssc.whenCancelled(() -> {
            // when there is no academic term disable
        });

        ssc.whenFailed(() -> {

        });

        ssc.whenSuccess(() -> {
            this.postStatusCheck(ssc);
        });

        ssc.whenFinished(() -> {
            // make buttons enabled
            this.setDisableSwitchButtons(false);
        });

        ssc.transact();
    }

    private void changeButtonColor(JFXButton button, String color) {
        button.setBackground(new Background(new BackgroundFill(Color.web(color), CornerRadii.EMPTY, Insets.EMPTY)));
    }

    private void postStatusCheck(ServiceStatusChecker ssc) {

        // display evaluation status
        if (ssc.isEvaluationActive()) {
            lbl_evaluation_status.setText("ONLINE");
            btn_evaluation_service.setText("TURN OFF");
            this.changeButtonColor(btn_evaluation_service, "#F791A6");
        } else {
            lbl_evaluation_status.setText("OFFLINE");
            btn_evaluation_service.setText("TURN ON");
            this.changeButtonColor(btn_evaluation_service, "#35BA9B");
        }

        if (ssc.isAddingActive()) {
            lbl_adding_status.setText("ONLINE");
            btn_adding_service.setText("TURN OFF");
            this.changeButtonColor(btn_adding_service, "#F791A6");
        } else {
            lbl_adding_status.setText("OFFLINE");
            btn_adding_service.setText("TURN ON");
            this.changeButtonColor(btn_adding_service, "#35BA9B");
        }

        if (ssc.isEncodingActive()) {
            lbl_encoding_status.setText("ONLINE");
            btn_encoding_service.setText("TURN OFF");
            this.changeButtonColor(btn_encoding_service, "#F791A6");
        } else {
            lbl_encoding_status.setText("OFFLINE");
            btn_encoding_service.setText("TURN ON");
            this.changeButtonColor(btn_encoding_service, "#35BA9B");
        }

        AcademicTermMapping term = ssc.getCurrentTerm();
        // please be cautious in using the Faculty Utility Read the informations carefully.
        String as_updater = FacultyUtility.getFacultyName(FacultyUtility.getFaculty(term.getAs_updater()));
        String es_updater = FacultyUtility.getFacultyName(FacultyUtility.getFaculty(term.getEs_updater()));
        String ens_updater = FacultyUtility.getFacultyName(FacultyUtility.getFaculty(term.getEns_updater()));

        lbl_adding_service_updater.setText(as_updater);
        lbl_eval_service_updater.setText(es_updater);
        lbl_encoding_service_updater.setText(ens_updater);

        lbl_adding_service_date.setText(DateString.formatDate(term.getAs_update_date()));
        lbl_eval_service_date.setText(DateString.formatDate(term.getEs_update_date()));
        lbl_encoding_service_date.setText(DateString.formatDate(term.getEns_update_date()));
    }

    /**
     * changes the status of a specific service.
     */
    public class ServiceStateChanger extends Transaction {

        private AcademicTermMapping currentTerm = SystemProperties.instance().getCurrentAcademicTerm();

        // mutated values
        private Boolean evaluationService;
        private Boolean addingService;
        private Boolean encodingService;

        public void setEvaluationService(Boolean evaluationService) {
            this.evaluationService = evaluationService;
        }

        public void setAddingService(Boolean addingService) {
            this.addingService = addingService;
        }

        public void setEncodingService(Boolean encodingService) {
            this.encodingService = encodingService;
        }

        private Boolean forceProcess = false;

        public void forceProcess(Boolean forceProcess) {
            this.forceProcess = forceProcess;
        }

        @Override
        protected boolean transaction() {

            if(currentTerm==null)
                return false;
            
            // reload values
            this.currentTerm = Database.connect()
                    .academic_term()
                    .getPrimary(currentTerm.getId());

            // for evaluation
            if (evaluationService != null) {
                // get proposed value to change
                Integer proposedValue = evaluationService ? 1 : 0;
                // if already equal cancel the transaction.
                if (this.currentTerm.getEvaluation_service().equals(proposedValue) && (!forceProcess)) {
                    return false;
                } else {
                    // if not equal change the value to the proposed state
                    this.currentTerm.setEvaluation_service(proposedValue);
                    this.currentTerm.setEs_update_date(Mono.orm().getServerTime().getDateWithFormat());
                    this.currentTerm.setEs_updater(CollegeFaculty.instance().getFACULTY_ID());
                }
            }
            // for adding and changing
            if (addingService != null) {
                // get proposed value to change
                Integer proposedValue = addingService ? 1 : 0;
                // if already equal cancel the transaction.
                if (this.currentTerm.getAdding_service().equals(proposedValue) && (!forceProcess)) {
                    return false;
                } else {
                    this.currentTerm.setAdding_service(proposedValue);
                    this.currentTerm.setAs_update_date(Mono.orm().getServerTime().getDateWithFormat());
                    this.currentTerm.setAs_updater(CollegeFaculty.instance().getFACULTY_ID());
                }
            }

            // for encoding
            if (encodingService != null) {
                // get proposed value to change
                Integer proposedValue = encodingService ? 1 : 0;
                // if already equal cancel the transaction.
                if (this.currentTerm.getEncoding_service().equals(proposedValue) && (!forceProcess)) {
                    return false;
                } else {
                    this.currentTerm.setEncoding_service(proposedValue);
                    this.currentTerm.setEns_update_date(Mono.orm().getServerTime().getDateWithFormat());
                    this.currentTerm.setEns_updater(CollegeFaculty.instance().getFACULTY_ID());
                }
            }

            // once all the values have set and not aborted
            // execute the update
            boolean stateUpdated = Database.connect()
                    .academic_term().update(currentTerm);

            if (!stateUpdated) {
                // failed to update
                // invoke whenFailed callback
                throw new TransactionException("Error in update");
            }

            return true;
        }

        @Override
        protected void after() {

        }

    }

    /**
     * This class checks the status of this academic term services.
     */
    public class ServiceStatusChecker extends Transaction {

        private AcademicTermMapping currentTerm = SystemProperties.instance().getCurrentAcademicTerm();

        // result values
        private boolean evaluationActive;
        private boolean addingActive;
        private boolean encodingActive;

        // getters for the result
        public boolean isEvaluationActive() {
            return evaluationActive;
        }

        public boolean isAddingActive() {
            return addingActive;
        }

        public boolean isEncodingActive() {
            return encodingActive;
        }

        public AcademicTermMapping getCurrentTerm() {
            return currentTerm;
        }

        @Override
        protected boolean transaction() {
            // preset
            evaluationActive = false;
            addingActive = false;
            encodingActive = false;

            // check if there is a current term
            if(currentTerm==null) {
                return false;
            }
            
            // reload values
            this.currentTerm = Database.connect()
                    .academic_term()
                    .getPrimary(currentTerm.getId());

            this.evaluationActive = this.currentTerm
                    .getEvaluation_service().equals(1);

            this.addingActive = this.currentTerm
                    .getAdding_service().equals(1);

            this.encodingActive = this.currentTerm
                    .getEncoding_service().equals(1);

            return true;
        }

        @Override
        protected void after() {

        }
    }

    /**
     *
     * @param transaction 0 if evaluation, 1 if adding & changing
     * @return
     */
    private Boolean checkIfTransactionIsOn(int transaction) {
        AcademicTermMapping acadTerm = SystemProperties.instance().getCurrentAcademicTerm();
        if (acadTerm == null) {
            return false;
        }
        if (transaction == 0) {
            return acadTerm.getEvaluation_service().equals(1);
        } else if (transaction == 1) {
            return acadTerm.getAdding_service().equals(1);
        } else {
            System.out.println("TRANSACTION NOT FOUND");
            return false;
        }
    }

}

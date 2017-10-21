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
package update3.org.cict.termcalendar;

import app.lazy.models.AcademicTermMapping;
import app.lazy.models.Database;
import app.lazy.models.utils.DateString;
import app.lazy.models.utils.FacultyUtility;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import com.jhmvin.fx.async.TransactionException;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.LayoutDataFX;
import com.jhmvin.fx.display.SceneFX;
import java.util.Date;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.cict.authentication.authenticator.SystemProperties;
import update.org.cict.controller.home.Home;

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
    private Label lbl_current_term;

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
                changeStatus(Boolean.TRUE, null, null);
            } else {
                changeStatus(Boolean.FALSE, null, null);
            }
        });

        super.addClickEvent(btn_adding_service, () -> {
            if (lbl_adding_status.getText().equalsIgnoreCase("OFFLINE")) {
                changeStatus(null, Boolean.TRUE, null);
            } else {
                changeStatus(null, Boolean.FALSE, null);
            }
        });

        super.addClickEvent(btn_encoding_service, () -> {
            if (lbl_encoding_status.getText().equalsIgnoreCase("OFFLINE")) {
                changeStatus(null, null, Boolean.TRUE);
            } else {
                changeStatus(null, null, Boolean.FALSE);
            }
        });

    }

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

        btn_adding_service.setDisable(disabled);
        btn_evaluation_service.setDisable(disabled);
        btn_encoding_service.setDisable(disabled);
    }

    private void changeStatus(
            Boolean evaluationService,
            Boolean addingService,
            Boolean encodingService) {

        ServiceStateChanger changeState = new ServiceStateChanger();
        changeState.setEvaluationService(evaluationService);
        changeState.setAddingService(addingService);
        changeState.setEncodingService(encodingService);

        changeState.whenStarted(() -> {
            this.setDisableSwitchButtons(true);
        });

        changeState.whenCancelled(() -> {
            Mono.fx().snackbar().showInfo(application_root, "State was Already Updated.");
        });

        changeState.whenFailed(() -> {
            Mono.fx().snackbar().showInfo(application_root, "State Update has Failed.");
        });

        changeState.whenSuccess(() -> {
            Mono.fx().snackbar().showSuccess(application_root, "State Successfully Changed.");
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
        lbl_encoding_service_date.setText(ens_updater);

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

        @Override
        protected boolean transaction() {
            // reload values
            this.currentTerm = Database.connect()
                    .academic_term()
                    .getPrimary(currentTerm.getId());

            // for evaluation
            if (evaluationService != null) {
                // get proposed value to change
                Integer proposedValue = evaluationService ? 1 : 0;
                // if already equal cancel the transaction.
                if (this.currentTerm.getEvaluation_service().equals(proposedValue)) {
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
                if (this.currentTerm.getAdding_service().equals(proposedValue)) {
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
                if (this.currentTerm.getEncoding_service().equals(proposedValue)) {
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

}

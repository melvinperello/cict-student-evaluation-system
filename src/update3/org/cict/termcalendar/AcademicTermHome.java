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
import com.jfoenix.controls.JFXButton;
import com.jhmvin.fx.async.Transaction;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.LayoutDataFX;
import com.jhmvin.fx.display.SceneFX;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    private JFXButton btn_evaluation_service;

    @FXML
    private Label lbl_adding_status;

    @FXML
    private HBox btn_adding;

    @FXML
    private JFXButton btn_adding_service;

    @FXML
    private Label lbl_encoding_status;

    @FXML
    private JFXButton btn_encoding_service;

    @FXML
    private Label lbl_current_term;

    @FXML
    private JFXButton btn_change_term;

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

    }

    /**
     * Check academic term services status.
     */
    private void checkStatus() {
        ServiceStatusChecker ssc = new ServiceStatusChecker();
        ssc.whenStarted(() -> {

        });

        ssc.whenCancelled(() -> {

        });

        ssc.whenFailed(() -> {

        });

        ssc.whenSuccess(() -> {
            this.postStatusCheck(ssc);
        });

        ssc.whenFinished(() -> {

        });

        ssc.transact();
    }

    private void postStatusCheck(ServiceStatusChecker ssc) {

        // display evaluation status
        if (ssc.isEvaluationActive()) {
            lbl_evaluation_status.setText("ONLINE");
            btn_evaluation_service.setText("TURN OFF");
            btn_evaluation_service.getStyleClass().remove("state-to-on");
            btn_evaluation_service.getStyleClass().add("state-to-off");
        } else {
            lbl_evaluation_status.setText("OFFLINE");
            btn_evaluation_service.setText("TURN ON");
            btn_evaluation_service.getStyleClass().remove("state-to-off");
            btn_evaluation_service.getStyleClass().add("state-to-on");
        }

        if (ssc.isAddingActive()) {
            lbl_adding_status.setText("ONLINE");
            btn_adding_service.setText("TURN OFF");
            btn_adding_service.getStyleClass().remove("state-to-on");
            btn_adding_service.getStyleClass().add("state-to-off");
        } else {
            lbl_adding_status.setText("OFFLINE");
            btn_adding_service.setText("TURN ON");
            btn_adding_service.getStyleClass().remove("state-to-off");
            btn_adding_service.getStyleClass().add("state-to-on");
        }

        if (ssc.isEncodingActive()) {
            lbl_encoding_status.setText("ONLINE");
            btn_encoding_service.setText("TURN OFF");
            btn_encoding_service.getStyleClass().remove("state-to-on");
            btn_encoding_service.getStyleClass().add("state-to-off");
        } else {
            lbl_encoding_status.setText("OFFLINE");
            btn_encoding_service.setText("TURN ON");
            btn_encoding_service.getStyleClass().remove("state-to-off");
            btn_encoding_service.getStyleClass().add("state-to-on");
        }

    }

    /**
     * changes the status of a specific service.
     */
    private class ServiceStateChanger extends Transaction {

        @Override
        protected boolean transaction() {

            return true;
        }

        @Override
        protected void after() {

        }

    }

    /**
     * This class checks the status of this academic term services.
     */
    private class ServiceStatusChecker extends Transaction {

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

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
package update3.org.cict.my_account;

import app.lazy.models.AccountFacultyAttemptMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.SimpleTask;
import com.jhmvin.fx.async.Transaction;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import com.jhmvin.transitions.Animate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.cict.authentication.authenticator.CollegeComputer;
import org.cict.authentication.authenticator.CollegeFaculty;
import update.org.cict.controller.home.Home;
import update3.org.cict.layout.default_loader.LoaderView;
import update3.org.cict.window_prompts.empty_prompt.EmptyView;
import update3.org.cict.window_prompts.fail_prompt.FailView;

/**
 *
 * @author Jhon Melvin
 */
public class MyAccountHome extends SceneFX implements ControllerFX {

    @FXML
    private VBox application_root;

    @FXML
    private JFXButton btn_home;

    @FXML
    private JFXButton btn_voew_access_history;

    @FXML
    private JFXButton btn_view_change_password;

    @FXML
    private JFXButton btn_view_change_pin;

    @FXML
    private JFXButton btn_view_change_question;

    @FXML
    private VBox vbox_access;

    @FXML
    private Label lbl_history_ip;

    @FXML
    private Label lbl_history_mac;

    @FXML
    private Label lbl_history_os;

    @FXML
    private Label lbl_history_terminal;

    @FXML
    private Label lbl_history_user;

    @FXML
    private StackPane stack_history;

    @FXML
    private VBox vbox_access_history;

    @FXML
    private VBox vbox_change_password;

    @FXML
    private PasswordField txt_cp_current;

    @FXML
    private PasswordField txt_cp_new;

    @FXML
    private Label txt_cp_confirm;

    @FXML
    private JFXButton btn_cp_change;

    @FXML
    private VBox vbox_change_pin;

    @FXML
    private VBox vbox_change_question;

    public MyAccountHome() {
        //
    }
    /**
     * View Attachments.
     */
    private LoaderView loaderView;
    private FailView failView;
    private EmptyView emptyView;

    public final static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

    @Override
    public void onInitialization() {
        super.bindScene(application_root);

        //
        this.loaderView = new LoaderView(stack_history);
        this.failView = new FailView(stack_history);
        this.emptyView = new EmptyView(stack_history);

        //
        this.changeView(this.vbox_access);
        this.showAccessHistory();

    }

    /**
     * Detaches all prompt views.
     */
    private void detachAll() {
        this.loaderView.detach();
        this.failView.detach();
        this.emptyView.detach();
    }

    private void changeView(Node whatView) {
        this.detachAll();
        Animate.fade(whatView, 150, () -> {
            // what to do after the fade animation
            vbox_access.setVisible(false);
            vbox_change_password.setVisible(false);
            vbox_change_pin.setVisible(false);
            vbox_change_question.setVisible(false);
            whatView.setVisible(true);
        }, vbox_change_password, vbox_access, vbox_change_pin, vbox_change_question);
    }

    @Override
    public void onEventHandling() {
        super.addClickEvent(btn_home, () -> {
            Home.callHome(this);
            this.detachAll();
        });

        super.addClickEvent(btn_voew_access_history, () -> {
            this.changeView(this.vbox_access);
            this.showAccessHistory();
        });
        super.addClickEvent(btn_view_change_password, () -> {
            this.changeView(this.vbox_change_password);
        });
        super.addClickEvent(btn_view_change_pin, () -> {
            this.changeView(this.vbox_change_pin);
        });
        super.addClickEvent(btn_view_change_question, () -> {
            this.changeView(this.vbox_change_question);
        });
    }

    private void showAccessHistory() {
        displayCurrentSession();
    }

    private void displayCurrentSession() {
        CollegeComputer cc = CollegeComputer.instance();

        String displayIP = "";
        String displayMAC = "";
        try {
            boolean firstOnly = true;
            String[] ipSet = cc.getIP_ADDRESS().split("%");
            for (String ipString : ipSet) {
                if (ipString.isEmpty()) {
                    continue;
                }

                if (!firstOnly) {
                    break;
                }
                String[] ipInfo = ipString.split("@");

                String ipAddr = ipInfo[0];
                String macAddr = ipInfo[1];

                displayIP += (ipAddr + "  ");
                displayMAC += (macAddr + "  ");

                firstOnly = false; // only the first set of ip
            }
        } catch (Exception e) {
            displayIP = "Unknown Host";
            displayMAC = "Unknown Host";
        }

        lbl_history_ip.setText(displayIP);
        lbl_history_mac.setText(displayMAC);
        lbl_history_os.setText(cc.getOS());
        lbl_history_terminal.setText(cc.getPC_NAME());
        lbl_history_user.setText(cc.getPC_USERNAME());

        FetchAccessHistory accessTx = new FetchAccessHistory();
        accessTx.whenStarted(() -> {
            this.vbox_access.setVisible(true);
            this.detachAll();
            this.loaderView.setMessage("Loading History");
            this.loaderView.attach();
        });
        accessTx.whenFailed(() -> {
            this.vbox_access_history.getChildren().clear();
            this.failView.setMessage("Failed to Load History");
            this.failView.attach();

        });
        accessTx.whenCancelled(() -> {
            this.vbox_access_history.getChildren().clear();
            System.out.println(this.vbox_access_history.isVisible());
            this.emptyView.setMessage("No History");
            this.emptyView.getButton().setVisible(false);
            this.emptyView.attach();

        });
        accessTx.whenSuccess(() -> {
            renderHistory(accessTx);
        });
        accessTx.whenFinished(() -> {
            this.loaderView.detach();
        });

        accessTx.transact();

        //--
    }

    /**
     * Reduces Lag Renders the FXML outside the main thread.
     *
     * @param accessTx
     */
    private void renderHistory(FetchAccessHistory accessTx) {
        SimpleTask renderTask = new SimpleTask("render-history");
        renderTask.setTask(() -> {
            displayAccessTable(accessTx.getAttempts());
        });
        renderTask.whenStarted(() -> {
            this.loaderView.setMessage("Preparing History");
            this.loaderView.attach();
        });
        renderTask.whenFailed(() -> {
        });
        renderTask.whenCancelled(() -> {
        });
        renderTask.whenSuccess(() -> {
            displayAccessTable(accessTx.getAttempts());
        });
        renderTask.whenFinished(() -> {
            this.loaderView.detach();
        });

        renderTask.start();
    }

    /**
     * Display access history row.
     *
     * @param attempts
     */
    private void displayAccessTable(ArrayList<AccountFacultyAttemptMapping> attempts) {
        /**
         * Create Table
         */
        SimpleTable tblSections = new SimpleTable();

        for (AccountFacultyAttemptMapping attempt : attempts) {
            SimpleTableRow row = new SimpleTableRow();
            row.setRowHeight(50.0);
            /**
             * Create FXML ROW.
             */
            RowAccessHistory accessRow = new RowAccessHistory();

            accessRow.lbl_time.setText(dateFormat.format(attempt.getTime()));
            accessRow.lbl_state.setText(attempt.getResult());

            //
            String displayIP = "";
            String displayMAC = "";
            try {
                boolean firstOnly = true;
                String[] ipSet = attempt.getIp_address().split("%");
                for (String ipString : ipSet) {
                    if (ipString.isEmpty()) {
                        continue;
                    }

                    if (!firstOnly) {
                        break;
                    }
                    String[] ipInfo = ipString.split("@");

                    String ipAddr = ipInfo[0];
                    String macAddr = ipInfo[1];

                    displayIP += (ipAddr + "");
                    displayMAC += (macAddr + "");

                    firstOnly = false; // only the first set of ip
                }
            } catch (Exception e) {
                displayIP = "Unknown Host";
                displayMAC = "Unknown Host";
            }

            //
            accessRow.lbl_ip.setText(displayIP + "@" + displayMAC);
            accessRow.lbl_os.setText(attempt.getOs_version());
            accessRow.lbl_terminal.setText(attempt.getPc_username() + "@" + attempt.getPc_name());
            //
            SimpleTableCell cellParent = new SimpleTableCell();
            cellParent.setResizePriority(Priority.ALWAYS);
            cellParent.setContentAsPane(accessRow.rowHistory);

            row.addCell(cellParent);
            tblSections.addRow(row);
        }

        //
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(tblSections);
        simpleTableView.setFixedWidth(true);
        Mono.fx().thread().wrap(() -> {
            simpleTableView.setParentOnScene(vbox_access_history);
        });

    }

    /**
     * Class that controls the access row.
     */
    private class RowAccessHistory extends SceneFX {

        public HBox rowHistory;
        public Label lbl_time, lbl_ip, lbl_os, lbl_terminal, lbl_state;

        public RowAccessHistory() {

            rowHistory = (HBox) Mono.fx().create()
                    .setPackageName("update3.org.cict.my_account")
                    .setFxmlDocument("row-access")
                    .makeFX()
                    .pullOutLayout();

            lbl_state = super.searchAccessibilityText(rowHistory, "lbl_state");
            this.lbl_time = super.searchAccessibilityText(rowHistory, "lbl_time");
            this.lbl_ip = super.searchAccessibilityText(rowHistory, "lbl_ip");
            this.lbl_os = super.searchAccessibilityText(rowHistory, "lbl_os");
            this.lbl_terminal = super.searchAccessibilityText(rowHistory, "lbl_terminal");
        }
    }

    /**
     * Class that fetch the access history.
     */
    private class FetchAccessHistory extends Transaction {

        private ArrayList<AccountFacultyAttemptMapping> attempts;

        public ArrayList<AccountFacultyAttemptMapping> getAttempts() {
            return attempts;
        }

        @Override
        protected boolean transaction() {
            Integer logged_account = CollegeFaculty.instance().getACCOUNT_ID();

            attempts = Mono.orm()
                    .newSearch(Database.connect().account_faculty_attempt())
                    .eq(DB.account_faculty_attempt().account_id, logged_account)
                    .active()
                    .take(50);

            if (attempts == null) {
                return false; // no history
            }

            return true;
        }

        @Override
        protected void after() {

        }

    }

    private class ChangePassword extends Transaction {

        private String oldPassword;
        private String newPassword;

        public void setOldPassword(String oldPassword) {
            this.oldPassword = oldPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }

        @Override
        protected boolean transaction() {
            
            

            return true;
        }

        @Override
        protected void after() {

        }
    }

}

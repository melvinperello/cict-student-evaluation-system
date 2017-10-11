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

import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import com.jhmvin.transitions.Animate;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.cict.authentication.authenticator.CollegeComputer;
import update.org.cict.controller.home.Home;

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
    private VBox vbox_access_history;

    @FXML
    private VBox vbox_change_password;

    @FXML
    private VBox vbox_change_pin;

    @FXML
    private VBox vbox_change_question;

    public MyAccountHome() {
        //
    }

    @Override
    public void onInitialization() {
        super.bindScene(application_root);
        //
        this.changeView(this.vbox_access);
        this.showAccessHistory();

    }

    private void changeView(Node whatView) {
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

    }

    private void displayAccessTable() {
        /**
         * Create Table
         */
        SimpleTable tblSections = new SimpleTable();
        SimpleTableRow row = new SimpleTableRow();
        row.setRowHeight(50.0);

        /**
         * Create FXML ROW.
         */
        RowAccessHistory accessRow = new RowAccessHistory();

        //
        SimpleTableCell cellParent = new SimpleTableCell();
        cellParent.setResizePriority(Priority.ALWAYS);
        cellParent.setContentAsPane(accessRow.rowHistory);

        //
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(tblSections);
        simpleTableView.setFixedWidth(true);
        simpleTableView.setParentOnScene(vbox_access_history);
    }

    private class RowAccessHistory extends SceneFX {

        public HBox rowHistory;
        public Label lbl_time, lbl_ip, lbl_os, lbl_terminal;

        public RowAccessHistory() {
            rowHistory = (HBox) Mono.fx().create()
                    .setPackageName("update3.org.cict.layout.sectionmain")
                    .setFxmlDocument("row-irreg-section")
                    .makeFX()
                    .pullOutLayout();

            this.lbl_time = super.searchAccessibilityText(rowHistory, "lbl_time");
            this.lbl_ip = super.searchAccessibilityText(rowHistory, "lbl_ip");
            this.lbl_os = super.searchAccessibilityText(rowHistory, "lbl_os");
            this.lbl_terminal = super.searchAccessibilityText(rowHistory, "lbl_terminal");
        }
    }

}

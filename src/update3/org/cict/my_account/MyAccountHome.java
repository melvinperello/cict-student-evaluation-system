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
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import com.jhmvin.transitions.Animate;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
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

}

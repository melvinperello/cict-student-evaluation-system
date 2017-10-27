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
package update4.org.cict.linked_manager;

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
public class LinkedHome extends SceneFX implements ControllerFX {

    @FXML
    private VBox application_root;

    @FXML
    private JFXButton btn_home;

    @FXML
    private JFXButton btn_linked_session;

    @FXML
    private JFXButton btn_new_session;

    @FXML
    private JFXButton btn_marshalls;

    @FXML
    private VBox vbox_home;

    @FXML
    private VBox vbox_new_session;

    @FXML
    private VBox vbox_marshalls;

    @Override
    public void onInitialization() {
        super.bindScene(application_root);
        // default view.
        this.changeView(vbox_home);
    }

    /**
     * Changes the layout view.
     *
     * @param whatView
     */
    private void changeView(Node whatView) {
        Animate.fade(whatView, 150, () -> {
            vbox_home.setVisible(false);
            vbox_new_session.setVisible(false);
            vbox_marshalls.setVisible(false);
            whatView.setVisible(true);
        }, vbox_home, vbox_new_session, vbox_marshalls);
    }

    @Override
    public void onEventHandling() {
        super.addClickEvent(btn_home, () -> {
            Home.callHome(this);
        });

        super.addClickEvent(btn_linked_session, () -> {
            this.changeView(vbox_home);
        });
        super.addClickEvent(btn_new_session, () -> {
            this.changeView(vbox_new_session);
        });
        super.addClickEvent(btn_marshalls, () -> {
            this.changeView(vbox_marshalls);
        });

    }

}

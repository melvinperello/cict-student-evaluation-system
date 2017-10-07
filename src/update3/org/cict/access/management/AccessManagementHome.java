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
package update3.org.cict.access.management;

import com.jfoenix.controls.JFXButton;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import update.org.cict.controller.home.Home;

/**
 *
 * @author Jhon Melvin
 */
public class AccessManagementHome extends SceneFX implements ControllerFX {

    @FXML
    private VBox application_root;

    @FXML
    private JFXButton btn_home;

    @FXML
    private VBox vbox_system;

    @FXML
    private VBox vbox_admin;

    @FXML
    private VBox vbox_local_registrar;

    public AccessManagementHome() {
        //
    }
    
  

    @Override
    public void onInitialization() {
        super.bindScene(application_root);
        vbox_system.setVisible(true);
        vbox_admin.setVisible(false);
        vbox_local_registrar.setVisible(false);
    }

    @Override
    public void onEventHandling() {
        super.addClickEvent(btn_home, () -> {
            Home.callHome(this);
        });

    }

}

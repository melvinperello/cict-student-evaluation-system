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
import com.jhmvin.transitions.Animate;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
    private JFXButton btn_view_system;
    
    @FXML
    private JFXButton btn_view_admin;
    
    @FXML
    private JFXButton btn_view_assistant_admin;
    
    @FXML
    private JFXButton btn_view_local_registrar;
    
    @FXML
    private JFXButton btn_view_co_registrar;
    
    @FXML
    private JFXButton btn_view_evaluator;
    
    @FXML
    private VBox vbox_system;
    
    @FXML
    private VBox vbox_admin;
    
    @FXML
    private VBox vbox_assistant_admin;
    
    @FXML
    private VBox vbox_local_registrar;
    
    @FXML
    private VBox vbox_co_registrar;
    
    @FXML
    private VBox vbox_evaluators;
    
    public AccessManagementHome() {
        //
    }
    
    @Override
    public void onInitialization() {
        super.bindScene(application_root);
        
        this.changeView(vbox_system);
    }
    
    private void changeView(Node whatView) {
        Animate.fade(whatView, 150, () -> {
            vbox_system.setVisible(false);
            vbox_admin.setVisible(false);
            vbox_assistant_admin.setVisible(false);
            vbox_local_registrar.setVisible(false);
            vbox_co_registrar.setVisible(false);
            vbox_evaluators.setVisible(false);
            whatView.setVisible(true);
        }, vbox_system, vbox_admin, vbox_assistant_admin, vbox_local_registrar,
                vbox_co_registrar, vbox_evaluators);
    }
    
    @Override
    public void onEventHandling() {
        super.addClickEvent(btn_home, () -> {
            Home.callHome(this);
        });
        super.addClickEvent(btn_view_system, () -> {
            this.changeView(vbox_system);
        });
        super.addClickEvent(btn_view_admin, () -> {
            this.changeView(vbox_admin);
        });
        super.addClickEvent(btn_view_assistant_admin, () -> {
            this.changeView(vbox_assistant_admin);
        });
        super.addClickEvent(btn_view_local_registrar, () -> {
            this.changeView(vbox_local_registrar);
        });
        super.addClickEvent(btn_view_co_registrar, () -> {
            this.changeView(vbox_co_registrar);
        });
        super.addClickEvent(btn_view_evaluator, () -> {
            this.changeView(vbox_evaluators);
        });
        
    }
    
}

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

import com.jfoenix.controls.JFXButton;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.LayoutDataFX;
import com.jhmvin.fx.display.SceneFX;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    private JFXButton btn_evaluation;
    
    @FXML
    private Label lbl_adding_status;
    
    @FXML
    private HBox btn_adding;
    
    @FXML
    private Label lbl_encoding_status;
    
    @FXML
    private JFXButton btn_encoding;
    
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
    }
    
    @Override
    public void onEventHandling() {
        super.addClickEvent(btn_home, () -> {
            Home.callHome(this);
        });
    }
    
}

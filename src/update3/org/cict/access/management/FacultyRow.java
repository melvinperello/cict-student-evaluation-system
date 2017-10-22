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
import com.melvin.mono.fx.MonoLauncher;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 *
 * @author Joemar
 */
public class FacultyRow extends MonoLauncher {

    @FXML
    private Label lbl_bulsu_id;

    @FXML
    private Label lbl_department;

    @FXML
    private Label lbl_name;

    @FXML
    private JFXButton btn_remove;

    @Override
    public void onStartUp() {
    
    }

    public Label getLbl_bulsu_id() {
        return lbl_bulsu_id;
    }

    public Label getLbl_department() {
        return lbl_department;
    }

    public Label getLbl_name() {
        return lbl_name;
    }

    public JFXButton getBtn_remove() {
        return btn_remove;
    }
    
}

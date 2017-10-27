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
 * JOEMAR N. DE LA CRUZ
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
import com.melvin.mono.fx.MonoLauncher;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 *
 * @author Joemar
 */
public class MarshallRow extends MonoLauncher {

    @FXML
    private Label lbl_name;

    @FXML
    private HBox hbox_view_org;

    @FXML
    private Label lbl_org;

    @FXML
    private Label lbl_student_num;
            
    @FXML
    private JFXButton btn_change_org;

    @FXML
    private HBox hbox_change_org;

    @FXML
    private ComboBox<String> cmb_org;

    @FXML
    private JFXButton btn_save_changes;

    @FXML
    private JFXButton btn_remove;
    
    @Override
    public void onStartUp() {
    }

    public Label getLbl_name() {
        return lbl_name;
    }

    public Label getLbl_org() {
        return lbl_org;
    }

    public JFXButton getBtn_remove() {
        return btn_remove;
    }

    public HBox getHbox_view_org() {
        return hbox_view_org;
    }

    public JFXButton getBtn_change_org() {
        return btn_change_org;
    }

    public HBox getHbox_change_org() {
        return hbox_change_org;
    }

    public JFXButton getBtn_save_changes() {
        return btn_save_changes;
    }

    public Label getLbl_student_num() {
        return lbl_student_num;
    }

    public ComboBox<String> getCmb_org() {
        return cmb_org;
    }
    
}

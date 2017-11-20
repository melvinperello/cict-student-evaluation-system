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
package sys.org.cict.layout.home.system_variables;

import com.jfoenix.controls.JFXButton;
import com.melvin.mono.fx.MonoLauncher;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 *
 * @author Joemar
 */
public class SystemValuesRowExtension extends MonoLauncher {

    @FXML
    private VBox vbox_new;

    @FXML
    private Label lbl_name;

    @FXML
    private TextField txt_name;

    @FXML
    private Label lbl_value;

    @FXML
    private Label lbl_count;
            
    @FXML
    private TextArea txtarea_message;

    @FXML
    private TextField txt_value;

    @FXML
    private JFXButton btn_add_new;

    @FXML
    private JFXButton btn_remove;
    
    @FXML
    private Label lbl_announced_by;
            
    @Override
    public void onStartUp() {
    }

    public VBox getVbox_new() {
        return vbox_new;
    }

    public TextField getTxt_name() {
        return txt_name;
    }

    public TextField getTxt_value() {
        return txt_value;
    }

    public JFXButton getBtn_add_new() {
        return btn_add_new;
    }

    public JFXButton getBtn_remove() {
        return btn_remove;
    }

    public Label getLbl_name() {
        return lbl_name;
    }

    public Label getLbl_value() {
        return lbl_value;
    }

    public TextArea getTxtarea_message() {
        return txtarea_message;
    }

    public Label getLbl_count() {
        return lbl_count;
    }

    public Label getLbl_announced_by() {
        return lbl_announced_by;
    }

}

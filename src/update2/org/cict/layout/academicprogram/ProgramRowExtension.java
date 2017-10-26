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
package update2.org.cict.layout.academicprogram;

import com.jfoenix.controls.JFXButton;
import com.melvin.mono.fx.MonoLauncher;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 *
 * @author Joemar
 */
public class ProgramRowExtension extends MonoLauncher {

    @FXML
    private VBox application_root;

    @FXML
    private JFXButton btn_delete;

    @FXML
    private TextField txt_program_code;

    @FXML
    private Label lbl_created_date;

    @FXML
    private Label lbl_created_by;

    @FXML
    private TextField txt_program_name;

    @FXML
    private TextField txt_floor_assignment;

    @FXML
    private JFXButton btn_save_changes;

    @FXML
    private Label lbl_status;

    @FXML
    private Label lbl_implementation_date;

    @FXML
    private Label lbl_implemented_by;

    @FXML
    private JFXButton btn_show;

    @FXML
    private JFXButton btn_newCurriculum;

    @FXML
    private VBox vbox_curriculum_table_holder;

    @FXML
    private VBox vbox_no_found_curriculum;

    @FXML
    private Label lbl_no_found_curriculum;

    public VBox getApplication_root() {
        return application_root;
    }

    public JFXButton getBtn_delete() {
        return btn_delete;
    }

    public TextField getTxt_program_code() {
        return txt_program_code;
    }

    public Label getLbl_created_date() {
        return lbl_created_date;
    }

    public Label getLbl_created_by() {
        return lbl_created_by;
    }

    public TextField getTxt_program_name() {
        return txt_program_name;
    }

    public JFXButton getBtn_save_changes() {
        return btn_save_changes;
    }

    public Label getLbl_status() {
        return lbl_status;
    }

    public Label getLbl_implementation_date() {
        return lbl_implementation_date;
    }

    public Label getLbl_implemented_by() {
        return lbl_implemented_by;
    }

    public JFXButton getBtn_show() {
        return btn_show;
    }

    public JFXButton getBtn_newCurriculum() {
        return btn_newCurriculum;
    }

    public VBox getVbox_curriculum_table_holder() {
        return vbox_curriculum_table_holder;
    }

    public VBox getVbox_no_found_curriculum() {
        return vbox_no_found_curriculum;
    }

    public Label getLbl_no_found_curriculum() {
        return lbl_no_found_curriculum;
    }

    public TextField getTxt_floor_assignment() {
        return txt_floor_assignment;
    }
    
    @Override
    public void onStartUp() {
    
    }
    
}

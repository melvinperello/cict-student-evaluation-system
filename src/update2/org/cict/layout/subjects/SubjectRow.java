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
package update2.org.cict.layout.subjects;

import com.jfoenix.controls.JFXButton;
import com.melvin.mono.fx.MonoLauncher;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 *
 * @author Joemar
 */
public class SubjectRow extends MonoLauncher {

    @FXML
    private HBox application_root;

    @FXML
    private Label lbl_subject_code;

    @FXML
    private Label lbl_descriptive_title;

    @FXML
    private Label lbl_lec;

    @FXML
    private Label lbl_lab;

    @FXML
    private Label lbl_subtype;

    @FXML
    private Label lbl_type;

    @FXML
    private JFXButton btn_more_info;

    @FXML
    private JFXButton btn_remove;
    
    @Override
    public void onStartUp() {
    }

    public HBox getApplication_root() {
        return application_root;
    }

    public Label getLbl_subject_code() {
        return lbl_subject_code;
    }

    public Label getLbl_descriptive_title() {
        return lbl_descriptive_title;
    }

    public Label getLbl_lec() {
        return lbl_lec;
    }

    public Label getLbl_lab() {
        return lbl_lab;
    }

    public Label getLbl_subtype() {
        return lbl_subtype;
    }

    public Label getLbl_type() {
        return lbl_type;
    }

    public JFXButton getBtn_more_info() {
        return btn_more_info;
    }

    public JFXButton getBtn_remove() {
        return btn_remove;
    }
    
}

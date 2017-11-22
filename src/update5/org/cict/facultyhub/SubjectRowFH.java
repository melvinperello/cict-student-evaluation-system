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
package update5.org.cict.facultyhub;

import com.jfoenix.controls.JFXButton;
import com.melvin.mono.fx.MonoLauncher;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 *
 * @author Joemar
 */
public class SubjectRowFH extends MonoLauncher{

    @FXML
    private Label lbl_section;
    
    @FXML
    private Label lbl_code;

    @FXML
    private Label lbl_description;

    @FXML
    private JFXButton btn_schedule;

    @FXML
    private JFXButton btn_print_pdf;

    @FXML
    private JFXButton print_export;

    @FXML
    private JFXButton print_import;
            
    @FXML
    private Label lbl_student_count;

    public Label getLbl_section() {
        return lbl_section;
    }
    
    @Override
    public void onStartUp() {
    }

    public Label getLbl_code() {
        return lbl_code;
    }

    public Label getLbl_description() {
        return lbl_description;
    }

    public JFXButton getBtn_schedule() {
        return btn_schedule;
    }

    public JFXButton getBtn_print_pdf() {
        return btn_print_pdf;
    }

    public JFXButton getPrint_export() {
        return print_export;
    }
    
    public Label getLbl_student_count() {
        return lbl_student_count;
    }

    public JFXButton getPrint_import() {
        return print_import;
    }

}

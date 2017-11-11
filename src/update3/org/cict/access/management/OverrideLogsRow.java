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
package update3.org.cict.access.management;

import com.jfoenix.controls.JFXButton;
import com.melvin.mono.fx.MonoLauncher;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 *
 * @author Joemar
 */
public class OverrideLogsRow extends MonoLauncher {

    @FXML
    private HBox application_root;

    @FXML
    private Label lbl_executed_by;

    @FXML
    private Label lbl_date;

    @FXML
    private Label lbl_conforme;

    @FXML
    private Label lbl_conforme_type;

    @FXML
    private Label lbl_description;

    @FXML
    private Label lbl_category;

    @FXML
    private JFXButton btn_download;
    
    @Override
    public void onStartUp() {
    
    }

    public HBox getApplication_root() {
        return application_root;
    }

    public Label getLbl_executed_by() {
        return lbl_executed_by;
    }

    public Label getLbl_date() {
        return lbl_date;
    }

    public Label getLbl_conforme() {
        return lbl_conforme;
    }

    public Label getLbl_conforme_type() {
        return lbl_conforme_type;
    }

    public Label getLbl_description() {
        return lbl_description;
    }

    public Label getLbl_category() {
        return lbl_category;
    }

    public JFXButton getBtn_download() {
        return btn_download;
    }
    
}

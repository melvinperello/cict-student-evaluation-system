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
package org.cict.evaluation.retention;

import com.jfoenix.controls.JFXButton;
import com.melvin.mono.fx.MonoLauncher;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 *
 * @author Joemar
 */
public class RetentionRow extends MonoLauncher{

    @FXML
    private Label lbl_curriculum_code;

    @FXML
    private Label lbl_yearlvl;

    @FXML
    private Label lbl_semester;

    @FXML
    private JFXButton btn_view_printing_details;

    
    @Override
    public void onStartUp() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Label getLbl_curriculum_code() {
        return lbl_curriculum_code;
    }

    public Label getLbl_yearlvl() {
        return lbl_yearlvl;
    }

    public Label getLbl_semester() {
        return lbl_semester;
    }

    public JFXButton getBtn_view_printing_details() {
        return btn_view_printing_details;
    }

}

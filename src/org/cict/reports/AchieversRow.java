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
package org.cict.reports;

import com.melvin.mono.fx.MonoLauncher;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 *
 * @author Joemar
 */
public class AchieversRow extends MonoLauncher {

    @FXML
    private Label lbl_name;

    @FXML
    private Label lbl_section;

    @FXML
    private Label lbl_gwa;

    @Override
    public void onStartUp() {
    
    }

    public Label getLbl_name() {
        return lbl_name;
    }

    public Label getLbl_section() {
        return lbl_section;
    }

    public Label getLbl_gwa() {
        return lbl_gwa;
    }
    
}

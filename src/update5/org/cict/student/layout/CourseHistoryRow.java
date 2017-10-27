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
package update5.org.cict.student.layout;

import com.melvin.mono.fx.MonoLauncher;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 *
 * @author Joemar
 */
public class CourseHistoryRow extends MonoLauncher {

    @FXML
    private Label lbl_current_curriculum;

    @FXML
    private Label lbl_current_curriculum_date;

    @FXML
    private Label lbl_prevoius_curriculum;

    @FXML
    private Label lbl_prevoius_curriculum_date;

    @Override
    public void onStartUp() {
    }

    public Label getLbl_current_curriculum() {
        return lbl_current_curriculum;
    }

    public Label getLbl_current_curriculum_date() {
        return lbl_current_curriculum_date;
    }

    public Label getLbl_prevoius_curriculum() {
        return lbl_prevoius_curriculum;
    }

    public Label getLbl_prevoius_curriculum_date() {
        return lbl_prevoius_curriculum_date;
    }
    
}

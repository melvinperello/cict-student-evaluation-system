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
package org.cict.evaluation.student.history;

import com.melvin.mono.fx.MonoLauncher;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 *
 * @author Joemar
 */
public class StudentInfoHistoryRow extends MonoLauncher{

    @FXML
    private Label lbl_student_number;

    @FXML
    private Label lbl_student_name;

    @FXML
    private Label lbl_yr_lvl;

    @FXML
    private Label lbl_gender;

    @FXML
    private Label lbl_section;

    @FXML
    private Label lbl_grp;

    @FXML
    private Label lbl_campus;

    @FXML
    private Label lbl_updated_by;

    @FXML
    private Label lbl_updated_date;
    
    @Override
    public void onStartUp() {
    }

    public Label getLbl_student_number() {
        return lbl_student_number;
    }

    public Label getLbl_student_name() {
        return lbl_student_name;
    }

    public Label getLbl_gender() {
        return lbl_gender;
    }

    public Label getLbl_section() {
        return lbl_section;
    }

    public Label getLbl_campus() {
        return lbl_campus;
    }

    public Label getLbl_updated_by() {
        return lbl_updated_by;
    }

    public Label getLbl_updated_date() {
        return lbl_updated_date;
    }

    public Label getLbl_yr_lvl() {
        return lbl_yr_lvl;
    }

    public Label getLbl_grp() {
        return lbl_grp;
    }

}

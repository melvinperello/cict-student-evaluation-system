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

import app.lazy.models.StudentMapping;
import com.jfoenix.controls.JFXTreeTableView;
import com.jhmvin.Mono;
import com.jhmvin.fx.display.ControllerFX;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Joemar
 */
public class StudentHistoryController implements ControllerFX{

    @FXML
    private Label lblName;

    @FXML
    private Label lblCourse;

    @FXML
    private JFXTreeTableView<History> tblEvaluation;
    
    @FXML
    private Button btnBack;
    
    private StudentMapping STUDENT;
    private String COURSE;
    
    public StudentHistoryController(StudentMapping student, String course) {
        this.STUDENT = student;
        this.COURSE = course;
    }
    
    @Override
    public void onInitialization() {
        this.setValues();
        load.setStudent(this.STUDENT);
        load.createTableView(this.tblEvaluation);
    }

    private LoadHistory load = new LoadHistory();
    @Override
    public void onEventHandling() {
        btnBack.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent e) -> {
            Mono.fx().getParentStage(lblName).close();
        });
    }
    
    private void setValues() {
        String fullName = this.STUDENT.getLast_name() + ", " +
                this.STUDENT.getFirst_name() + " " + (this.STUDENT.getMiddle_name()==null? "" :  this.STUDENT.getMiddle_name());
        this.lblName.setText(fullName);
        this.lblCourse.setText(this.COURSE);
    }
    
}

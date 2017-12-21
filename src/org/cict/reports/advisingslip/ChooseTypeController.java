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
package org.cict.reports.advisingslip;

import com.jfoenix.controls.JFXButton;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.cict.evaluation.evaluator.Evaluator;
import org.cict.evaluation.evaluator.PrintAdvising;

/**
 *
 * @author Joemar
 */
public class ChooseTypeController extends SceneFX implements ControllerFX {

    @FXML
    private VBox application_root;

    @FXML
    private JFXButton btn_old;

    @FXML
    private JFXButton btn_new;

    @FXML
    private JFXButton btn_reg;

    @FXML
    private JFXButton btn_irreg;
    
    private Integer ACADTERM_id;
    private String STUDENT_id;

    public ChooseTypeController(String studentID, Integer acadTermID) {
        this.STUDENT_id = studentID;
        this.ACADTERM_id = acadTermID;
    }

    @Override
    public void onInitialization() {
        /**
         * Always use bindScene in every controller that uses SceneFX.
         */
        super.bindScene(application_root);
    }

    @Override
    public void onEventHandling() {
        addClickEvent(btn_old, () -> {
            print(btn_old.getText());
        });
        addClickEvent(btn_new, () -> {
            print(btn_new.getText());
        });
        addClickEvent(btn_reg, () -> {
            print(btn_reg.getText());
        });
        addClickEvent(btn_irreg, () -> {
            print(btn_irreg.getText());
        });
    }

    public void print(String text) {
//        int res = Mono.fx().alert().createConfirmation()
//                    .setHeader("Print Evaluation Slip")
//                    .setMessage("Are you sure the student is a " + btn.getText() + " type?")
//                    .confirmYesNo();
//        if(res != 1)
//            return;
        selected = text;
        PrintAdvising slip = Evaluator.instance().printAdvising();
        slip.studentNumber = this.STUDENT_id;
        slip.academicTerm = this.ACADTERM_id;
        slip.type = selected;
        slip.transact();
        isPrinting = true;
        /**
         * If explicitly called without the viewer.
         */
        try {
            super.finish();
        } catch (Exception e) {
        }

    }

    private boolean isPrinting = false;

    public boolean isPrinting() {
        return isPrinting;
    }

    private String selected;

    public String getSelected() {
        return selected;
    }
}

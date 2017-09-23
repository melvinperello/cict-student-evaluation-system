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
package org.cict.evaluation.student.credit;

import com.jhmvin.Mono;
import com.jhmvin.fx.display.ControllerFX;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Joemar
 */
public class InputModeController implements ControllerFX {

    @FXML
    private Button btnCredit;

    @FXML
    private Button btnEncode;

    private Integer CICT_id;
    private String MODE;

    public InputModeController(Integer cict_id) {
        this.CICT_id = cict_id;
    }

    @Override
    public void onInitialization() {
    }

    @Override
    public void onEventHandling() {
        btnCredit.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            this.onClick("Credit");
        });
        btnEncode.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            this.onClick("Encode");
        });
    }

    private void onClick(String mode) {
        String message;
        if (mode.equalsIgnoreCase("encode")) {
            this.MODE = CreditController.MODE_ENCODE;
            message = "Here, pre-requisites are a must. Already encoded grades cannot be modified.";
        } else {
            this.MODE = CreditController.MODE_CREDIT;
            message = "No pre-requisites needed to input. Grades can be modified easily.";
        }
        int choice = Mono.fx().alert()
                .createConfirmation()
                .setHeader(mode
                        + " Mode")
                .setMessage(message)
                .confirmYesNo();
        if (choice == 1) {
            onShowCredit();
            Mono.fx().getParentStage(btnCredit).close();
        }
    }

    private void onShowCredit() {
        String title;
        if (this.MODE.equals(CreditController.MODE_CREDIT)) {
            title = "Credit Subjects";
        } else {
            title = "Encode Subjects";
        }
        CreditController controller = new CreditController(this.CICT_id, this.MODE, title);
        Mono.fx().create()
                .setPackageName("org.cict.evaluation.student.credit")
                .setFxmlDocument("Credit")
                .makeFX()
                .setController(controller)
                .makeScene()
                .makeStageApplication()
                .stageResizeable(true)
                .stageMaximized(true)
                .stageShow();
    }
}

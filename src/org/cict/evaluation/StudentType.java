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
package org.cict.evaluation;

import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.melvin.mono.fx.MonoLauncher;
import com.melvin.mono.fx.bootstrap.M;
import com.melvin.mono.fx.events.MonoClick;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Joemar
 */
public class StudentType extends MonoLauncher {

    @FXML
    private VBox application_root;

    @FXML
    private JFXButton btn_regular;

    @FXML
    private JFXButton btn_irreg;

    @FXML
    private JFXButton btn_cross_enrollee;

    @FXML
    private JFXButton btn_shifter;

    @FXML
    private JFXButton btn_transferee;
    
    @FXML
    private JFXButton btn_cancel;
    
    public static String REGULAR = "REGULAR";
    public static String IRREGULAR = "IRREGULAR";
    public static String CROSS_ENROLLEE = "CROSS_ENROLLEE";
    public static String SHIFTER = "SHIFTER";
    public static String TRANSFEREE = "TRANSFEREE";
    
    public static String showStudentTypeChooser(Stage parentStage) {
        StudentType chooser = M.load(StudentType.class);
        String type = null;
        try {
            chooser.getCurrentStage().showAndWait();
        } catch (NullPointerException e) {
            Stage a = chooser.createChildStage(parentStage);
            a.initStyle(StageStyle.UNDECORATED);
            a.showAndWait();
            type = chooser.getChoosen();
        }
        return type;
    }
    
    private String choosenType;
    public String getChoosen() {
        return choosenType;
    }
    
    @Override
    public void onStartUp() {
        MonoClick.addClickEvent(btn_cancel, ()->{
            choosenType = null;
            Mono.fx().getParentStage(btn_irreg).close();
        });
        MonoClick.addClickEvent(btn_cross_enrollee, ()->{
            choosenType = btn_cross_enrollee.getText();
            Mono.fx().getParentStage(btn_irreg).close();
        });
        MonoClick.addClickEvent(btn_irreg, ()->{
            choosenType = btn_irreg.getText();
            Mono.fx().getParentStage(btn_irreg).close();
        });
        MonoClick.addClickEvent(btn_regular, ()->{
            choosenType = btn_regular.getText();
            Mono.fx().getParentStage(btn_irreg).close();
        });
        MonoClick.addClickEvent(btn_shifter, ()->{
            choosenType = btn_shifter.getText();
            Mono.fx().getParentStage(btn_irreg).close();
        });
        MonoClick.addClickEvent(btn_transferee, ()->{
            choosenType = btn_transferee.getText();
            Mono.fx().getParentStage(btn_irreg).close();
        });
    }

}

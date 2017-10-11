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
package update2.org.cict.controller.academicprogram;

import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import com.jhmvin.transitions.Animate;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import update.org.cict.controller.home.Home;
import update2.org.cict.controller.subjects.AddNewSubjectController;

/**
 *
 * @author Joemar
 */
public class AcademicHome extends SceneFX implements ControllerFX {

    @FXML
    private AnchorPane application_root;

    @FXML
    private JFXButton btn_new_acad;

    @FXML
    private JFXButton btn_view_all_acad;

    @FXML
    private JFXButton btn_new_subject;

    @FXML
    private JFXButton btn_view_all_subj;

    @FXML
    private JFXButton btn_home;
    
    @Override
    public void onInitialization() {
    
    }

    @Override
    public void onEventHandling() {
        this.addClickEvent(btn_home, ()->{
            Home.callHome(this);
        });
        this.addClickEvent(btn_new_acad, ()->{
            ControllerFX controller = new AddNewProgramController();
            this.changeRoot(controller,
                    "update2.org.cict.layout.academicprogram",
                    "add-acad-program");
        });
        this.addClickEvent(btn_new_subject, ()->{
            ControllerFX controller = new AddNewSubjectController();
            this.changeRoot(controller,
                    "update2.org.cict.layout.subjects",
                    "add-new-subject");
        });
        this.addClickEvent(btn_view_all_acad, ()->{
            ControllerFX controller = new AcademicProgramHome();
            this.changeRoot(controller,
                    "update2.org.cict.layout.academicprogram",
                    "academic-program-home");
        });
    }
    
    private void changeRoot(ControllerFX controller, String packer, String fxml) {
        Pane fxRoot = Mono.fx().create()
                .setPackageName(packer)
                .setFxmlDocument(fxml)
                .makeFX()
                .setController(controller)
                .pullOutLayout();

        Animate.fade(application_root, 150, () -> {
            super.replaceRoot(fxRoot);
        }, fxRoot);
    }
    
}

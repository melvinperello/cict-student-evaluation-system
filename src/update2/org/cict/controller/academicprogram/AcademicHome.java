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
import com.jhmvin.fx.display.LayoutDataFX;
import com.jhmvin.fx.display.SceneFX;
import com.jhmvin.transitions.Animate;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.controlsfx.control.Notifications;
import update.org.cict.controller.home.Home;
import update2.org.cict.controller.subjects.AddNewSubjectController;
import update2.org.cict.controller.subjects.SubjectRepositoryController;

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
        bindScene(application_root);
        // all destructive operations should be disabled if not admin
        if (AcademicProgramAccessManager.denyIfNotAdmin()) {
            btn_new_acad.setDisable(true);
            btn_new_subject.setDisable(true);
        }
    }

    @Override
    public void onEventHandling() {
        this.addClickEvent(btn_home, ()->{
            Home.callHome(this);
        });
        this.addClickEvent(btn_new_acad, ()->{
            AddNewProgramController controller = new AddNewProgramController();
            LayoutDataFX homeFX = new LayoutDataFX(application_root, this);
            controller.setHomeFX(homeFX);
            this.changeRoot(controller,
                    "update2.org.cict.layout.academicprogram",
                    "add-acad-program");
        });
        this.addClickEvent(btn_new_subject, ()->{
            AddNewSubjectController controller = new AddNewSubjectController();
            LayoutDataFX homeFX = new LayoutDataFX(application_root, this);
            controller.setHomeFX(homeFX);
            this.changeRoot(controller,
                    "update2.org.cict.layout.subjects",
                    "add-new-subject");
        });
        this.addClickEvent(btn_view_all_acad, ()->{
            this.checkValues("program", ()->{
//                AcademicProgramHome controller = new AcademicProgramHome();
//                LayoutDataFX homeFX = new LayoutDataFX(application_root, this);
//                controller.setHomeFX(homeFX);
//                this.changeRoot(controller,
//                        "update2.org.cict.layout.academicprogram",
//                        "academic-program-home");
            });
        });
        this.addClickEvent(btn_view_all_subj, ()->{
            this.checkValues("subject", ()->{
//                SubjectRepositoryController controller = new SubjectRepositoryController();
//                LayoutDataFX homeFX = new LayoutDataFX(application_root, this);
//                controller.setHomeFX(homeFX);
//                this.changeRoot(controller,
//                        "update2.org.cict.layout.subjects",
//                        "subject-bank");
            });
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
    
    private void checkValues(String mode, Runnable next) {
        if(mode.equalsIgnoreCase("subject")) {
            SubjectRepositoryController controller = new SubjectRepositoryController();
            LayoutDataFX homeFX = new LayoutDataFX(application_root, this);
            controller.setHomeFX(homeFX);
            this.changeRoot(controller,
                    "update2.org.cict.layout.subjects",
                    "subject-bank");
        } else {
            FetchAcademicPrograms fetchProgramsTx = new FetchAcademicPrograms();
            fetchProgramsTx.whenStarted(()->{
                btn_view_all_acad.setDisable(true);
            });
            fetchProgramsTx.whenCancelled(()->{
                Notifications.create()
                        .title("No Academic Program Found")
                        .text("Create Academic Program to proceed here.").showWarning();
            });
            fetchProgramsTx.whenSuccess(()->{
                AcademicProgramHome controller = new AcademicProgramHome();
                LayoutDataFX homeFX = new LayoutDataFX(application_root, this);
                controller.setHomeFX(homeFX);
                this.changeRoot(controller,
                        "update2.org.cict.layout.academicprogram",
                        "academic-program-home");
            });
            fetchProgramsTx.whenFinished(()->{
                btn_view_all_acad.setDisable(false);
            });
            fetchProgramsTx.transact();
        }
    }
}

package update.org.cict.controller.home;

import com.jhmvin.Mono;
import static com.jhmvin.Mono.fx;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import update3.org.cict.controller.sectionmain.SectionHomeController;

public class Home extends SceneFX implements ControllerFX {

    @FXML
    private AnchorPane application_root;

    @FXML
    private Button btn_evaluation;

    @FXML
    private Button btn_adding;

    @FXML
    private Button btn_academic_programs;

    @FXML
    private Button btn_section;

    @Override
    public void onInitialization() {
        this.bindScene(application_root);
    }

    public static void callHome() {
        Mono.fx().create()
                .setPackageName("update.org.cict.layout.home")
                .setFxmlDocument("home")
                .makeFX()
                .makeScene()
                .makeStageApplication()
                .stageMaximized(false)
                .stageShow();
    }

    @Override
    public void onEventHandling() {

        btn_evaluation.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Mono.fx().getParentStage(btn_evaluation).close();

            Mono.fx().create()
                    .setPackageName("org.cict.evaluation")
                    .setFxmlDocument("evaluation_home")
                    .makeFX()
                    .makeScene()
                    .makeStageApplication()
                    .stageMinDimension(1024, 700)
                    .stageMaximized(true)
                    .stageShow();
        });

        btn_adding.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Mono.fx().getParentStage(btn_adding).close();
            Mono.fx().create()
                    .setPackageName("update.org.cict.layout.adding_changing")
                    .setFxmlDocument("adding-changing-home")
                    .makeFX()
                    .makeScene()
                    .makeStageApplication()
                    .stageMinDimension(1024, 700)
                    .stageMaximized(true)
                    .stageShow();
        });

        btn_academic_programs.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Mono.fx().getParentStage(btn_academic_programs).close();
            Mono.fx().create()
                    .setPackageName("update2.org.cict.layout.academicprogram")
                    .setFxmlDocument("academic-program-home")
                    .makeFX()
                    .makeScene()
                    .makeStageApplication()
                    .stageMinDimension(1024, 700)
                    .stageMaximized(true)
                    .stageShow();
        });

        this.addClickEvent(btn_section, () -> {
            this.finish(); // close the stage
            SectionHomeController controller = new SectionHomeController();
            Mono.fx().create()
                    .setPackageName("update3.org.cict.layout.sectionmain")
                    .setFxmlDocument("sectionHome")
                    .makeFX()
                    .setController(controller)
                    .makeScene()
                    .makeStageApplication()
                    .stageMinDimension(1024, 700)
                    .stageMaximized(true)
                    .stageShow();
        });

    }
}

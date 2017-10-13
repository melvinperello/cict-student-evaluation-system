
import com.jhmvin.Mono;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;
import update3.org.collegechooser.ChooserHome;

public class CollegeChooser extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        ChooserHome ch = new ChooserHome();
        Mono.fx().create()
                .setPackageName("update3.org.collegechooser")
                .setFxmlDocument("ChooserHome")
                .makeFX()
                .setController(ch)
                .makeScene()
                .makeStageApplication()
                .stageResizeable(false)
                .stageUndecorated(true)
                .stageShowAndWait();
        System.out.println(ch.getSelected());
    }

    public static void main(String[] args) {
        launch(args);
    }

}

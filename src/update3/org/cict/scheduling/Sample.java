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
package update3.org.cict.scheduling;

import com.jhmvin.Mono;
import java.util.HashMap;
import javafx.application.Application;
import javafx.stage.Stage;
import update3.org.cict.ScheduleConstants;

/**
 *
 * @author Jhon Melvin
 */
public class Sample extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        HashMap<String,ScheduleData> data = new HashMap<>();
        data.put(ScheduleConstants.MONDAY, value)
        TimeTableController controller = new TimeTableController(data);
        Mono.fx().create()
                .setPackageName("update3.org.cict.scheduling")
                .setFxmlDocument("TimeTableController")
                .makeFX()
                .setController(controller)
                .makeScene()
                .makeStageApplication()
                .stageTitle("Schedule Viewer")
                .stageShow();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

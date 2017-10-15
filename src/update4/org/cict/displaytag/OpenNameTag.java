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
package update4.org.cict.displaytag;

import com.jhmvin.Mono;
import javafx.beans.property.StringProperty;
import javafx.stage.Stage;

/**
 *
 * @author Jhon Melvin
 */
public class OpenNameTag {

    public static void display(StringProperty source, String evaluator) {
        DisplayTag controller = new DisplayTag();
        Stage stage = Mono.fx().create()
                .setPackageName("update4.org.cict.displaytag")
                .setFxmlDocument("DisplayTag")
                .makeFX()
                .setController(controller)
                .makeScene()
                .makeStage()
                .pullOutStage();
        controller.setEvaluatorName(evaluator);
        controller.bindTextField(source);
        stage.setMaximized(true);
        stage.show();
    }
}

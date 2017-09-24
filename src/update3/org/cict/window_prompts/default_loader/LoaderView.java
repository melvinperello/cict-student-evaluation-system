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
package update3.org.cict.layout.default_loader;

import com.jhmvin.Mono;
import com.jhmvin.fx.display.SceneFX;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import update3.org.cict.window_prompts.Attachment;
import update3.org.cict.window_prompts.Visibility;

/**
 *
 * @author Jhon Melvin
 */
public class LoaderView extends SceneFX {

    private VBox loadingBox;
    private Label loadingMessaage;
    private StackPane stackPane;

    public LoaderView(StackPane stackPane) {
        this.stackPane = stackPane;
        this.loadingBox = Mono.fx().create()
                .setPackageName("update3.org.cict.window_prompts.default_loader")
                .setFxmlDocument("pane-loading")
                .makeFX()
                .pullOutLayout();

        this.loadingMessaage = super.searchAccessibilityText(loadingBox, "lbl_loading");
    }

    public void setMessage(String text) {
        if (text == null) {
            return;
        }
        this.loadingMessaage.setText(text);
    }

    public void attach() {
        Attachment.attach(stackPane, loadingBox);
    }

    public void detach() {
        Attachment.detach(stackPane, loadingBox);
    }

}

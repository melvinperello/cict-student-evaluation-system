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
package update3.org.cict.window_prompts.empty_prompt;

import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.display.SceneFX;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import update3.org.cict.window_prompts.Visibility;

/**
 *
 * @author Jhon Melvin
 */
public class EmptyView extends SceneFX {

    private VBox emptyBox;
    private Label loadingMessaage;
    private StackPane stackPane;
    private JFXButton btn_refresh;

    public EmptyView(StackPane stackPane) {
        this.stackPane = stackPane;
        this.emptyBox = Mono.fx().create()
                .setPackageName("update3.org.cict.window_prompts.empty_prompt")
                .setFxmlDocument("pane-empty")
                .makeFX()
                .pullOutLayout();

        this.loadingMessaage = super.searchAccessibilityText(emptyBox, "lbl_message");
        this.btn_refresh = super.searchAccessibilityText(emptyBox, "btn_refresh");
    }

    public void setMessage(String text) {
        if (text == null) {
            return;
        }
        this.loadingMessaage.setText(text);
    }

    public JFXButton getButton() {
        return this.btn_refresh;
    }

    public void attach() {
        /**
         * Remove if existing.
         */
        this.stackPane.getChildren().remove(this.emptyBox);
        //
        Visibility.hideChildren(this.stackPane);
        stackPane.getChildren().add(this.emptyBox);
        this.emptyBox.toFront();
        /**
         * Bind Width to parent.
         */
        this.emptyBox.prefWidthProperty().bind(this.stackPane.widthProperty());
        this.emptyBox.maxWidthProperty().bind(this.stackPane.maxWidthProperty());
        this.emptyBox.minWidthProperty().bind(this.stackPane.minWidthProperty());
        /**
         * Bind height to parent.
         */
        this.emptyBox.prefHeightProperty().bind(this.stackPane.heightProperty());
        this.emptyBox.maxHeightProperty().bind(this.stackPane.maxHeightProperty());
        this.emptyBox.minHeightProperty().bind(this.stackPane.minHeightProperty());
    }

    public void detach() {
        this.stackPane.getChildren().remove(this.emptyBox);
        Visibility.showChildren(this.stackPane);
    }

}

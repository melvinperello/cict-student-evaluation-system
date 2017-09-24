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
package update3.org.cict.window_prompts;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Jhon Melvin
 */
public class Attachment {

    public static void attach(StackPane stack, Pane box) {
        /**
         * Remove if existing.
         */
        stack.getChildren().remove(box);
        //
        Visibility.hideChildren(stack);
        stack.getChildren().add(box);
        box.toFront();
        /**
         * Bind Width to parent.
         */
        box.prefWidthProperty().bind(stack.widthProperty());
        box.maxWidthProperty().bind(stack.maxWidthProperty());
        box.minWidthProperty().bind(stack.minWidthProperty());
        /**
         * Bind height to parent.
         */
        box.prefHeightProperty().bind(stack.heightProperty());
        box.maxHeightProperty().bind(stack.maxHeightProperty());
        box.minHeightProperty().bind(stack.minHeightProperty());
    }

    public static void detach(StackPane stack, Pane box) {
        stack.getChildren().remove(box);
        Visibility.showChildren(stack);
    }
}

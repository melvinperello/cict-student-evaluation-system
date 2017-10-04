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
package update3.org.cict.access;

import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

/**
 *
 * @author Jhon Melvin
 */
public class EvaluationOverride extends SceneFX implements ControllerFX {

    @FXML
    private VBox application_root;

    @FXML
    private JFXButton btn_continue;

    @FXML
    private JFXButton btn_cancel;

    public EvaluationOverride(boolean hasAccess) {
        this.access = hasAccess;
    }

    private boolean authorized;
    private final boolean access;

    public boolean isAuthorized() {
        return authorized;
    }

    @Override
    public void onInitialization() {
        super.bindScene(application_root);
        this.authorized = false;
    }

    @Override
    public void onEventHandling() {
        super.addClickEvent(btn_continue, () -> {
            onContinue();
        });

        super.addClickEvent(btn_cancel, () -> {
            this.authorized = false;
            super.finish();
        });
    }

    private void onContinue() {
        if (!this.access) {
            Mono.fx().snackbar().showError(application_root, "You are not Authorized.");
            return;
        }

        int sel = Mono.fx().alert().createConfirmation()
                .setTitle("Confirm")
                .setHeader("Override System ?")
                .setMessage("Do you really want to continue ?")
                .confirmYesNo();

        if (sel == 1) {
            // proceed
            this.authorized = true;
            super.finish();
        }
    }

}

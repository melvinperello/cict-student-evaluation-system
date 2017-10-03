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
import com.jhmvin.propertymanager.FormFormat;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Jhon Melvin
 */
public class ReEvaluationAccess extends SceneFX implements ControllerFX {

    @FXML
    private TextField txt_registrar_username;

    @FXML
    private PasswordField txt_pin;

    @FXML
    private JFXButton btn_grant;

    @FXML
    private JFXButton btn_cancel;

    @FXML
    private VBox application_root;

    public ReEvaluationAccess() {

    }

    private boolean allowedOperation = false;

    public boolean isAllowedOperation() {
        return allowedOperation;
    }

    @Override
    public void onInitialization() {
        super.bindScene(application_root);

        FormFormat formformatter = new FormFormat();
        FormFormat.CustomFormat pinFormat = formformatter.new CustomFormat();
        pinFormat.setMaxCharacters(6);
        pinFormat.setStringFilter((text) -> {
            return StringUtils.isNumeric(text);
        });
        pinFormat.setFilterAction((i) -> {
            if (i.getFilterMessage().equalsIgnoreCase("UNMATCHED")) {
                Mono.fx().snackbar().showInfo(application_root, "Only Digits are Allowed");
            }
        });
        pinFormat.filter(txt_pin.textProperty());

    }

    @Override
    public void onEventHandling() {
        super.addClickEvent(btn_grant, () -> {
            //
        });
        super.addClickEvent(btn_cancel, () -> {
            super.finish();
        });
    }

}

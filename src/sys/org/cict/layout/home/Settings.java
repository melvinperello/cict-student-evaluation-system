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
package sys.org.cict.layout.home;

import artifacts.ConfigurationManager;
import artifacts.MonoString;
import artifacts.ResourceManager;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.melvin.java.properties.PropertyFile;
import com.melvin.mono.fx.MonoLauncher;
import com.melvin.mono.fx.events.MonoClick;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.cict.MainApplication;
import org.cict.PublicConstants;

/**
 *
 * @author Joemar
 */
public class Settings extends MonoLauncher {

    @FXML
    private VBox application_root;

    @FXML
    private TextField txt_ip;

    @FXML
    private JFXButton btn_save;

    @FXML
    private JFXButton btn_cancel;

    @Override
    public void onStartUp() {
        //----------------------------------------------------------------------
        txt_ip.setText(PublicConstants.getServerIP());
        //----------------------------------------------------------------------
        MonoClick.addClickEvent(btn_cancel, () -> {
            Mono.fx().getParentStage(txt_ip).close();
        });
        //----------------------------------------------------------------------
        MonoClick.addClickEvent(btn_save, () -> {
            onSaveNewValues();
        });
    }

    private void onSaveNewValues() {
        String newIP = MonoString.removeAll(txt_ip.getText(), " ");
        if (newIP.isEmpty()) {
            Mono.fx().alert().createWarning()
                    .setMessage("Field must not be empty.")
                    .show();
            return;
        }
        int res = Mono.fx().alert().createConfirmation()
                .setMessage("This transaction will automatically close the application, do you still want to continue?")
                .confirmOkCancel();
        if (res == -1) {
            return;
        }

        boolean changed = PropertyFile.writePropertyFile(ConfigurationManager.EVALUATION_PROP.getAbsolutePath(), ConfigurationManager.PROP_HOST_IP, newIP);

        if (changed) {
            Mono.fx().alert().createInfo()
                    .setTitle("Property Changed")
                    .setHeader("Changes Saved")
                    .setMessage("The Application needs to be closed.")
                    .showAndWait();
            // upon ok exit app
            MainApplication.die(0);
        } else {
            Mono.fx().alert().createError()
                    .setTitle("Property Changed")
                    .setHeader("Changes Not Saved")
                    .setMessage("The following configuration was not saved.")
                    .show();
        }

    }
}

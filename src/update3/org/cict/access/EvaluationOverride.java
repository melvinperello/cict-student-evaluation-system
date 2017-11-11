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

import artifacts.FTPManager;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import com.jhmvin.transitions.Animate;
import java.io.File;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.controlsfx.control.Notifications;

/**
 *
 * @author Jhon Melvin
 */
public class EvaluationOverride extends SceneFX implements ControllerFX {

    @FXML
    private VBox application_root;

    @FXML
    private HBox vbox_continue;

    @FXML
    private JFXButton btn_continue;

    @FXML
    private JFXButton btn_cancel;

    @FXML
    private HBox vbox_upload;

    @FXML
    private JFXButton btn_upload;

    @FXML
    private JFXButton btn_cancel1;

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
        this.changeView(vbox_upload);
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
        
        super.addClickEvent(btn_cancel1, ()->{
            this.authorized = false;
            super.finish();
        });
        
        super.addClickEvent(btn_upload, ()->{
            if (!this.access) {
                Mono.fx().snackbar().showError(application_root, "You are not Authorized.");
                return;
            }
            
            //---------------------------
            if(!this.upload()) 
                return;
            //--------------------
            this.changeView(vbox_continue);
        
        });
    }

    private void onContinue() {
        
        int sel = Mono.fx().alert().createConfirmation()
                .setTitle("Confirm")
                .setHeader("Override System?")
                .setMessage("Do you really want to continue?")
                .confirmYesNo();

        if (sel == 1) {
            // proceed
            this.authorized = true;
            super.finish();
        }
    }
    
    private boolean upload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
                       
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("RAR File", "*.rar"),
            new FileChooser.ExtensionFilter("ZIP File", "*.zip"),
            new FileChooser.ExtensionFilter("7Z File", "*.7z")
        );
        File file = fileChooser.showOpenDialog(this.getStage());
        btn_upload.setDisable(true);
        try {
            boolean uploaded = FTPManager.upload(file.getAbsolutePath(),
                    "override_attachment", file.getName());
            if(uploaded) {
                attachmentFile = file.getName();
                return true;
            } else {
                System.err.println("Error");
                Notifications.create().text("Failed to save attachment file.")
                        .title("Attachment Not Saved").showWarning();
                return false;
            }
        } catch (IOException ex) {
            System.err.println("Error");
            Notifications.create().text("Failed to save attachment file.")
                    .title(ex.getMessage()).showError();
            return false;
        }
    }
    
    private String attachmentFile;
    public String getAttachedFile() {
        return attachmentFile;
    }
    
    private void changeView(Node node) {
        Animate.fade(node, 150, ()->{
            vbox_continue.setVisible(false);
            vbox_upload.setVisible(false);
            node.setVisible(true);
        }, vbox_continue, vbox_upload);
    }
    
}

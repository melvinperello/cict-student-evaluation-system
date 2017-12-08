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

import app.lazy.models.AccountFacultyMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.FacultyMapping;
import app.lazy.models.OtpGeneratorMapping;
import artifacts.FTPManager;
import artifacts.OTPGenerator;
import artifacts.SMSWrapper;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import com.jhmvin.transitions.Animate;
import com.melvin.mono.fx.bootstrap.M;
import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.lang3.text.WordUtils;
import org.cict.authentication.authenticator.CollegeFaculty;
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

    private String transacationRequest;
    public EvaluationOverride(boolean hasAccess, String trasactionDetails) {
        this.access = hasAccess;
        this.transacationRequest = trasactionDetails;
    }
    
    //----------------------------------
    // allow assistant registrar to override
    // but must enter OTP given to the local registrar
    //----------------------------------
    
//    private String contactNumber = "";
//    private void assistantRegistrarOverride() {
//        AccountFacultyMapping localRegistrarAcc = Mono.orm().newSearch(Database.connect().account_faculty())
//                .eq(DB.account_faculty().access_level, Access.ACCESS_LOCAL_REGISTRAR).active().first();
//        if(localRegistrarAcc != null) {
//            FacultyMapping localRegistrar = Database.connect().faculty().getPrimary(localRegistrarAcc.getFACULTY_id());
//            contactNumber = localRegistrar==null? "" : localRegistrar.getMobile_number();
//        }
//        if(contactNumber==null || contactNumber.isEmpty()) {
//            Notifications.create().darkStyle().title("Sending of OTP failed")
//                    .text("No mobile number found. Please update the\n"
//                            + "Local Registrar's account.").showWarning();
//            return;
//        }
//        int res = Mono.fx().alert().createConfirmation()
//                .setMessage("The system will send a One-Time Password (OTP) to the current Local Registrar. You will use this to authorize the override transaction. Continue?")
//                .setHeader("Send OTP To Authorize").confirmYesNo();
//        if(res==-1)
//            return;
//        OtpGeneratorMapping map = new OtpGeneratorMapping();
//        String OTP_raw = OTPGenerator.generateOTP();
//        map.setActive(1);
//        map.setCode(Mono.security().hashFactory().hash_sha512(OTP_raw));
//        map.setDate_created(Mono.orm().getServerTime().getDateWithFormat());
//        Integer refID = Database.connect().otp_generator().insert(map);
//        if(refID.equals(-1)) {
//            Notifications.create().darkStyle().title("Failed")
//                    .text("Please check your connectivity to\n"
//                            + "the server.").showWarning();
//            return;
//        }
//        System.out.println("Reference Number: " + refID);
//        System.out.println("Your One-Time Password (OTP) is " + OTP_raw);
//        SMSWrapper.send(contactNumber,"Your One-Time Password (OTP) is " + OTP_raw + ". Reference Number: " + refID 
//                + "\n\nSent by Monosync", WordUtils.capitalizeFully(CollegeFaculty.instance().getFirstLastName()), response -> {
//            System.out.println("RESPONSE: " + response);
//            if(response.equalsIgnoreCase("FAILED")) {
//                Mono.fx().thread().wrap(()->{
//                    Notifications.create().darkStyle().title("Failed")
//                            .text("Sending of OTP to the Local Registrar\nfailed.").showError();
//                });
//                return;
//            } else if(response.equalsIgnoreCase("NO_REPLY")) {
//                Mono.fx().thread().wrap(()->{
//                    Notifications.create().darkStyle().title("No Response")
//                            .text("Please try again later.").showError();
//                });
//                return;
//            }
//            AssistantRegistrarOverride asstRegistrar =  M.load(AssistantRegistrarOverride.class);
//            asstRegistrar.onDelayedStart();
//            asstRegistrar.setDetails(contactNumber, refID);
//            Mono.fx().thread().wrap(()->{
//                try {
//                    asstRegistrar.getCurrentStage().showAndWait();
//                } catch (NullPointerException e) {
//                    Stage a = asstRegistrar.createChildStage(super.getStage());
//                    a.initStyle(StageStyle.UNDECORATED);
//                    a.showAndWait();
//                    authorized = asstRegistrar.isAuthorized();
//                    if(asstRegistrar.isAuthorized()) {
//                        this.upload();
//                    } else {
//                        Mono.fx().snackbar().showError(application_root, "You are not Authorized.");
//                    }
//                }
//                btn_upload.setDisable(false);
//            });
//        });
//    }
    
    //----------------------------------

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
            //--------------------------------
            if(AssistantRegistrarOverride.isAuthorized(this.getStage(), transacationRequest)) {
                Mono.fx().alert().createInfo()
                        .setMessage("You can now override this transaction by uploading first "
                                + "a file with a format of RAR, ZIP or 7Z. Then click Continue")
                        .setHeader("Authorization Accepted").showAndWait();
                Mono.fx().snackbar().showSuccess(application_root, "Access Granted.");
            }
            //--------------------------------
            
            else if (!this.access) {
                Mono.fx().snackbar().showError(application_root, "You are not Authorized.");
                return;
            } else {
               return; 
            }
            
            //---------------------------
            this.upload();
            //--------------------
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
    
    private void upload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
                       
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("RAR File", "*.rar"),
            new FileChooser.ExtensionFilter("ZIP File", "*.zip"),
            new FileChooser.ExtensionFilter("7Z File", "*.7z")
        );
        File file = fileChooser.showOpenDialog(this.getStage());
        btn_upload.setDisable(true);
        if(file == null) {
            btn_upload.setDisable(false);
            return;
        }
        //----------------------------------------------
        // NON BLOCKING UPLOAD
        FTPManager.upload(file.getAbsolutePath(), "override_attachment", file.getName(), (boolean result, Exception e) -> {
            if(result) {
                attachmentFile = file.getName();
                Mono.fx().thread().wrap(()->{
                    this.changeView(vbox_continue);
                });
            } else {
                Mono.fx().thread().wrap(()->{
                    System.err.println("Error");
                    Notifications.create().text("Please check your connection to the server.")
                            .title("Attachment Not Saved").showWarning();
                });
            }
        });
//        try {
//            boolean uploaded = FTPManager.upload(file.getAbsolutePath(),
//                    "override_attachment", file.getName());
//            if(uploaded) {
//                attachmentFile = file.getName();
//                return true;
//            } else {
//                System.err.println("Error");
//                Notifications.create().text("Failed to save attachment file.")
//                        .title("Attachment Not Saved").showWarning();
//                return false;
//            }
//        } catch (IOException ex) {
//            System.err.println("Error");
//            Notifications.create().text("Failed to save attachment file.")
//                    .title(ex.getMessage()).showError();
//            return false;
//        }
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

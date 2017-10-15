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
package org.cict.accountmanager;

import app.lazy.models.AccountFacultyMapping;
import artifacts.MonoString;
import com.izum.fx.textinputfilters.StringFilter;
import com.izum.fx.textinputfilters.TextInputFilters;
import com.jhmvin.Mono;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import com.jhmvin.transitions.Animate;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.cict.authentication.LoginController;
import update3.org.cict.SectionConstants;

/**
 *
 * @author Joemar
 */
public class RegisterController extends SceneFX implements ControllerFX{

    @FXML
    private VBox application_root;
    
    @FXML
    private TextField txt_bulsuId;

    @FXML
    private TextField txt_username;

    @FXML
    private PasswordField txt_password;

    @FXML
    private PasswordField txt_reenterPass;

    @FXML
    private Button btn_Register;

    @FXML
    private Button btn_Cancel;

    @Override
    public void onInitialization() {
        bindScene(application_root);
        addTextFieldFilters();
    }
    
    private void addTextFieldFilters() {
        StringFilter textField = TextInputFilters.string()
                .setFilterMode(StringFilter.LETTER_DIGIT)
                .setMaxCharacters(50)
                .setNoLeadingTrailingSpaces(true)
                .setFilterManager(filterManager->{
                    if(!filterManager.isValid()) {
                        Mono.fx().alert().createWarning().setHeader("Warning")
                                .setMessage(filterManager.getMessage())
                                .show();
                    }
                });
        textField.clone().setTextSource(txt_username).applyFilter();
       
        StringFilter textPass = TextInputFilters.string()
                .setFilterMode(StringFilter.LETTER_DIGIT)
                .setMaxCharacters(50)
                .setNoLeadingTrailingSpaces(true)
                .setFilterManager(filterManager->{
                    if(!filterManager.isValid()) {
                        Mono.fx().alert().createWarning().setHeader("Warning")
                                .setMessage(filterManager.getMessage())
                                .show();
                    }
                });
        textPass.clone().setTextSource(txt_password).applyFilter();
        textPass.clone().setTextSource(txt_reenterPass).applyFilter();
    }

    @Override
    public void onEventHandling() {
        this.buttonEvents();
    }
    
    private void buttonEvents(){
        this.btn_Cancel.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
            this.onClose();
        });
        this.btn_Register.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
            this.onRegister();
        });
    }
    
    private void onRegister(){
        String bulsuId = MonoString.removeExtraSpace(this.txt_bulsuId.getText().toUpperCase().trim());
        String username = MonoString.removeSpaces(this.txt_username.getText().trim());
        String pass = this.txt_password.getText().trim();
        String confirmPass = this.txt_reenterPass.getText().trim();
        if(checkEmpty(bulsuId, username, pass, confirmPass)){
            ValidateRegister validateRegister = AccountManager
                    .instance()
                    .createRegistration();
            validateRegister.bulsuId = bulsuId;
            validateRegister.username = username;
            validateRegister.password = Mono.security()
                    .hashFactory()
                    .hash_sha512(pass);
            
            validateRegister.setOnStart(start -> {
                this.btn_Register.setDisable(true);
            });
            
            validateRegister.setOnSuccess(success -> {
                this.btn_Register.setDisable(false);
                if(validateRegister.isStage1Verified()){
                    this.onShowRecovery(validateRegister.getAccountFacultyMap(),bulsuId);
                } else {
                    Mono.fx()
                            .alert()
                            .createInfo()
                            .setHeader("Verification Failed")
                            .setMessage(validateRegister.getAuthenticatorMessage())
                            .showAndWait();
                }
            });
            
            validateRegister.setOnFailure(onFailure -> {
                this.onRegisterError();
            });
            
            validateRegister.setOnCancel(onCancel -> {
                this.onRegisterError();
            });
            
            validateRegister.setRestTime(500);
            validateRegister.transact();
        }
    }
    
    private void onClose(){
        this.onShowLogin();
    }
    
    private boolean checkEmpty(String bulsuId, String username, String pass, String confirmPass) {
        if (bulsuId.isEmpty()) {
            Mono.fx()
                    .alert()
                    .createWarning()
                    .setTitle("Credentials")
                    .setHeader("BulSU ID Field is Empty!")
                    .setMessage("Please fill up the field with your BulSU ID. Thank You !")
                    .showAndWait();
            this.requestFocus(this.txt_bulsuId);
            return false;
        }
        if (username.isEmpty()) {
            Mono.fx()
                    .alert()
                    .createWarning()
                    .setTitle("Credentials")
                    .setHeader("Username Field is Empty!")
                    .setMessage("Please fill up the field with your username. Thank You !")
                    .showAndWait();
            this.requestFocus(this.txt_username);
            return false;
        }
        if (pass.isEmpty()) {
            Mono.fx()
                    .alert()
                    .createWarning()
                    .setTitle("Credentials")
                    .setHeader("Password Field is Empty!")
                    .setMessage("Please fill up the field with your password. Thank You !")
                    .showAndWait();
            this.requestFocus(this.txt_password);
            return false;
        }
        if(pass.length()<5){
            Mono.fx()
                    .alert()
                    .createWarning()
                    .setTitle("Credentials")
                    .setHeader("Weak Password!")
                    .setMessage("Please fill up the field password with minimum number of six(6) characters. This is for a more secured password.")
                    .showAndWait();
            this.requestFocus(this.txt_reenterPass);
            return false;
        }
        if (!confirmPass.equals(pass)) {
            Mono.fx()
                    .alert()
                    .createWarning()
                    .setTitle("Credentials")
                    .setHeader("Password Not Matched!")
                    .setMessage("Please fill up the field confirm password correctly. Thank You !")
                    .showAndWait();
            this.requestFocus(this.txt_reenterPass);
            return false;
        }
        return true;
    }
    
    private void requestFocus(Node control) {
        Stage stage = Mono.fx().getParentStage(control);
        stage.requestFocus();
        control.requestFocus();
    }
    
    public void onShowRecovery(AccountFacultyMapping accntFclty, String id) {
//        Mono.fx().getParentStage(this.btn_Cancel).close();
        RecoveryController controller = new RecoveryController(accntFclty,id);
//        Mono.fx().create()
//                .setPackageName("org.cict.accountmanager")
//                .setFxmlDocument("Recovery")
//                .makeFX()
//                .setController(controller)
//                .makeScene()
//                .makeStage()
//                .stageResizeable(false)
//                .stageShow();
        
        Pane pane = Mono.fx().create()
                .setPackageName("org.cict.accountmanager")
                .setFxmlDocument("Recovery")
                .makeFX()
                .setController(controller)
                .pullOutLayout();

        super.setSceneColor("#2983D5"); // call once on entire scene lifecycle

        Animate.fade(this.application_root, SectionConstants.FADE_SPEED, () -> {
            super.replaceRoot(pane);
        }, pane);
    }
    
    public void onShowLogin() {
//        Mono.fx().getParentStage(this.btn_Cancel).close();
        LoginController controller = new LoginController();
//        Mono.fx().create()
//                .setPackageName("org.cict.authentication")
//                .setFxmlDocument("Login")
//                .makeFX()
//                .setController(controller)
//                .makeScene()
//                .makeStage()
//                .stageResizeable(false)
//                .stageShow();
        Pane pane = Mono.fx().create()
                .setPackageName("org.cict.authentication")
                .setFxmlDocument("Login")
                .makeFX()
                .setController(controller)
                .pullOutLayout();

        super.setSceneColor("#2983D5"); // call once on entire scene lifecycle

        Animate.fade(this.application_root, SectionConstants.FADE_SPEED, () -> {
            super.replaceRoot(pane);
        }, pane);
    }
    
    private void onRegisterError() {
        Mono.fx()
                .alert()
                .createError()
                .setTitle("Account Manager")
                .setHeader("Verification Error")
                .setMessage("We cannot process your registration request at "
                        + "the moment. Sorry for the inconvenience.")
                .showAndWait();
    }
}

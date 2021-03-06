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
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jhmvin.Mono;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import update3.org.cict.my_account.MyAccountHome;

/**
 *
 * @author Joemar
 */
public class RecoveryController extends SceneFX implements ControllerFX{

    @FXML
    private VBox application_root;

    @FXML
    private VBox vbox_login;

    @FXML
    private JFXPasswordField txt_pin;

    @FXML
    private JFXPasswordField txt_confirm_pin;

    @FXML
    private ComboBox<String> cmb_questions;

    @FXML
    private JFXPasswordField txt_answer;

    @FXML
    private JFXPasswordField txt_reenter;

    @FXML
    private JFXButton btn_Register;

    private final AccountFacultyMapping accountFaculty;
    private final String bulsu_id;
    public RecoveryController(AccountFacultyMapping accountFclty, String id){
        this.accountFaculty = accountFclty;
        this.bulsu_id = id;
    }
    
    @Override
    public void onInitialization() {
        bindScene(application_root);
//        this.init();
        addTextFieldFilters();
        cmb_questions.getItems().addAll(MyAccountHome.LIST_SECURITY_QUESTIONS);
        cmb_questions.getSelectionModel().selectFirst();
    }

    private void addTextFieldFilters() {
        StringFilter textPin = TextInputFilters.string()
                .setFilterMode(StringFilter.DIGIT)
                .setMaxCharacters(10)
                .setNoLeadingTrailingSpaces(true)
                .setFilterManager(filterManager->{
                    if(!filterManager.isValid()) {
                        Mono.fx().alert().createWarning().setHeader("Warning")
                                .setMessage(filterManager.getMessage())
                                .show();
                    }
                });
        textPin.clone().setTextSource(txt_pin).applyFilter();
        textPin.clone().setTextSource(txt_confirm_pin).applyFilter();
        StringFilter textAns = TextInputFilters.string()
//                .setFilterMode(StringFilter.LETTER_DIGIT)
                .setMaxCharacters(50)
                .setNoLeadingTrailingSpaces(false)
                .setFilterManager(filterManager->{
                    if(!filterManager.isValid()) {
                        Mono.fx().alert().createWarning().setHeader("Warning")
                                .setMessage(filterManager.getMessage())
                                .show();
                    }
                });
        textAns.clone().setTextSource(txt_answer).applyFilter();
        textAns.clone().setTextSource(txt_reenter).applyFilter();
    }

    @Override
    public void onEventHandling() {
        this.buttonEvents();
    }
    
//    private void init(){
//        this.setComboBoxQuestions(cmb_questions);
//    }
    
    private void buttonEvents(){
        this.btn_Register.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
            this.onSave();
        });
    }
     
    private boolean checkIfEmpty(String pin, String confirmPin){
        if(pin.isEmpty()){
            Mono.fx()
                    .alert()
                    .createWarning()
                    .setTitle("Credentials")
                    .setHeader("Transaction Pin Is Empty!")
                    .setMessage("Please enter your transaction pin. "
                            + "This will be used in mostly in evaluation and adding ang changing transactions.")
                    .showAndWait();
            this.requestFocus(this.txt_pin);
            return false;
        }
        if(pin.length() != 6) {
            Mono.fx()
                    .alert()
                    .createWarning()
                    .setTitle("Credentials")
                    .setHeader("Six Digit Transaction Pin")
                    .setMessage("Please provide a six (6) digit pin.")
                    .showAndWait();
            this.requestFocus(this.txt_pin);
            return false;
        }
        if(confirmPin.isEmpty()){
            Mono.fx()
                    .alert()
                    .createWarning()
                    .setTitle("Credentials")
                    .setHeader("Confirm Pin Is Empty!")
                    .setMessage("Please re-enter your pin.")
                    .showAndWait();
            this.requestFocus(this.txt_confirm_pin);
            return false;
        } else if(Mono.security().hashFactory().hash_sha512(pin).equals(this.accountFaculty.getPassword())) {
            Mono.fx()
                    .alert()
                    .createWarning()
                    .setTitle("Credentials")
                    .setHeader("Invalid Pin!")
                    .setMessage("Transaction pin must not equal to your password.")
                    .showAndWait();
            this.requestFocus(this.txt_confirm_pin);
            return false;
        }
        if(!pin.equals(confirmPin)){
            Mono.fx()
                    .alert()
                    .createWarning()
                    .setTitle("Credentials")
                    .setHeader("Pin Not Matched!")
                    .setMessage("Please re-enter your pin correctly.")
                    .showAndWait();
            this.requestFocus(this.txt_confirm_pin);
            return false;
        }
        return true;
    }
    
    private void onSave(){
        String pin = (this.txt_pin.getText());
        String confirmPin = (this.txt_confirm_pin.getText());
        if(!checkIfEmpty(pin, confirmPin))
            return;
        
        String question = this.cmb_questions.getSelectionModel().getSelectedItem().toString().toUpperCase();
        String answer = MonoString.removeExtraSpace(this.txt_answer.getText());
        String reenter = MonoString.removeExtraSpace(this.txt_reenter.getText());
        if(checkEmpty(answer, reenter)){
            ValidateRegister validateRegister= AccountManager
                    .instance()
                    .createRegistration();
            validateRegister.bulsuId = this.bulsu_id;
            validateRegister.username = this.accountFaculty.getUsername();
            validateRegister.password = this.accountFaculty.getPassword();
            validateRegister.question = question;
            validateRegister.answer = Mono.security().hashFactory().hash_sha512(answer);
            validateRegister.pin = Mono.security().hashFactory().hash_sha512(pin);
            validateRegister.complete = true;
            
            validateRegister.setOnStart(onStart ->{
                this.btn_Register.setDisable(true);
            });
            validateRegister.setOnSuccess(onSuccess -> {
                if(validateRegister.isSaved()){
                    Mono.fx()
                            .alert()
                            .createInfo()
                            .setHeader("Successfully Registered")
                            .setMessage(validateRegister.getAuthenticatorMessage())
                            .showAndWait();
                    this.onShowLogin();
                } else {
                    this.btn_Register.setDisable(false);
                    Mono.fx()
                            .alert()
                            .createInfo()
                            .setHeader("Verification Failed")
                            .setMessage(validateRegister.getAuthenticatorMessage())
                            .showAndWait();
                    this.onShowLogin();
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
    
    private boolean checkEmpty(String answer, String reenter){
        if(answer.isEmpty()){
            Mono.fx()
                    .alert()
                    .createWarning()
                    .setTitle("Credentials")
                    .setHeader("Answer Field Is Empty!")
                    .setMessage("Please enter your answer for the recovery question. "
                            + "This may help in retrieving your account.")
                    .showAndWait();
            this.requestFocus(this.txt_answer);
            return false;
        }
        if(reenter.isEmpty()){
            Mono.fx()
                    .alert()
                    .createWarning()
                    .setTitle("Credentials")
                    .setHeader("Confirm Answer Field Is Empty!")
                    .setMessage("Please re-enter your answer.")
                    .showAndWait();
            this.requestFocus(this.txt_reenter);
            return false;
        }
        if(!answer.equals(reenter)){
            Mono.fx()
                    .alert()
                    .createWarning()
                    .setTitle("Credentials")
                    .setHeader("Answer Not Matched!")
                    .setMessage("Please re-enter your answer correctly.")
                    .showAndWait();
            this.requestFocus(this.txt_reenter);
            return false;
        }
        return true;
    }
    
    private void requestFocus(Node control) {
        Stage stage = Mono.fx().getParentStage(control);
        stage.requestFocus();
        control.requestFocus();
    }
    
//    public void setComboBoxQuestions(ComboBox cmb_questions){
//        ArrayList<String> questions = new ArrayList<>();
//        questions.add("Where was your favorite place to visit as a child?");
//        questions.add("Who is your favorite actor, musician, or artist?");
//        questions.add("What is the name of your favorite pet?");
//        questions.add("In what city were you born?");
//        questions.add("Which is your favorite web browser?");
//        questions.add("What is the name of your first school?");
//        questions.add("What is your favorite movie?");
//        questions.add("What is your mother's maiden name?");
//        questions.add("What is your favorite color?");
//        questions.add("What is your father's middle name?");
//        cmb_questions.getItems().addAll(questions);
//        cmb_questions.getSelectionModel().selectFirst();
//    }
    
    public void onShowLogin() {
//        LoginController controller = new LoginController();
//        Pane pane = Mono.fx().create()
//                .setPackageName("org.cict.authentication")
//                .setFxmlDocument("Login")
//                .makeFX()
//                .setController(controller)
//                .pullOutLayout();
//
//        super.setSceneColor("#2983D5"); // call once on entire scene lifecycle
//
//        Animate.fade(this.application_root, SectionConstants.FADE_SPEED, () -> {
//            super.replaceRoot(pane);
//        }, pane);
        Mono.fx().getParentStage(application_root).close();
    }
}

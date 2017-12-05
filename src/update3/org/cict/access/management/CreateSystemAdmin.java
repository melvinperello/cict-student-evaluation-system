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
package update3.org.cict.access.management;

import app.lazy.models.AccountFacultyMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.FacultyMapping;
import artifacts.MonoString;
import com.izum.fx.textinputfilters.StringFilter;
import com.izum.fx.textinputfilters.TextInputFilters;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.transitions.Animate;
import com.melvin.mono.fx.MonoLauncher;
import com.melvin.mono.fx.events.MonoClick;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.text.WordUtils;
import org.cict.accountmanager.faculty.FacultyInformation;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.textfield.CustomPasswordField;
import update3.org.cict.access.Access;
import update3.org.cict.my_account.MyAccountHome;

/**
 *
 * @author Joemar
 */
public class CreateSystemAdmin extends MonoLauncher {

    @FXML
    private VBox application_root;

    @FXML
    private JFXButton btn_close;

    @FXML
    private VBox vbox_form1;

    @FXML
    private TextField txt_bulsu_id;

    @FXML
    private TextField txt_last_name;

    @FXML
    private TextField txt_first_name;

    @FXML
    private TextField txt_middle_name;

    @FXML
    private JFXButton btn_next1;

    @FXML
    private VBox vbox_form2;

    @FXML
    private TextField txt_username;

    @FXML
    private CustomPasswordField txt_password;

    @FXML
    private CustomPasswordField txt_password_confirm;

    @FXML
    private JFXButton btn_previous2;

    @FXML
    private JFXButton btn_next2;

    @FXML
    private VBox vbox_form3;

    @FXML
    private ComboBox<String> cmb_questions;

    @FXML
    private CustomPasswordField txt_answer;

    @FXML
    private CustomPasswordField txt_answer_confirm;

    @FXML
    private JFXButton btn_previous3;

    @FXML
    private JFXButton btn_next3;
    
    @FXML
    private VBox vbox_form4;
    
    @FXML
    private JFXButton btn_close2;
    
    @FXML
    private VBox vbox_form5;
    
    @FXML
    private JFXButton btn_previous5;

    @FXML
    private JFXButton btn_next5;
    
    @FXML
    private CustomPasswordField txt_transaction_pin;
    
    @FXML
    private CustomPasswordField txt_transaction_pin_confirm;
    
    private Boolean saved = false;
    public Boolean isCreationSuccess() {
        return saved;
    }
    private String bulsu_id, last_name, first_name, middle_name, username, password, pin, question, answer;
    
    @Override
    public void onStartUp() {
        // MAIN
        this.changeView(vbox_form1);
        MonoClick.addClickEvent(btn_close, ()->{
            this.onClose(false);
        });
        
        // FORM 1
        MonoClick.addClickEvent(btn_next1, ()->{
            if(this.thereIsNull(txt_bulsu_id, txt_first_name, txt_last_name, txt_middle_name)) {
                Mono.fx().alert().createWarning()
                        .setMessage("Some fields are empty. Please supply them all.")
                        .show();
                return;
            }
            bulsu_id = MonoString.removeExtraSpace(txt_bulsu_id.getText()).toUpperCase();
            FacultyMapping faculty = Mono.orm().newSearch(Database.connect().faculty())
                    .eq(DB.faculty().bulsu_id, bulsu_id)
                    .active().first();
            if(faculty!=null) {
                Mono.fx().alert().createWarning()
                        .setMessage("Faculty with a BulSU ID of " + bulsu_id + " is already existing. Please provide a new one.")
                        .show();
                return;
            }
            last_name = MonoString.removeExtraSpace(txt_last_name.getText()).toUpperCase();
            first_name = MonoString.removeExtraSpace(txt_first_name.getText()).toUpperCase();            
            middle_name = MonoString.removeExtraSpace(txt_middle_name.getText()).toUpperCase();
            this.changeView(vbox_form2);
        });
        
        // FORM 2
        MonoClick.addClickEvent(btn_next2, ()->{
            if(this.thereIsNull(txt_username)) {
                Mono.fx().alert().createWarning()
                        .setMessage("Please enter a username.")
                        .show();
                return;
            }             
            username = MonoString.removeExtraSpace(txt_username.getText()).toUpperCase();
            AccountFacultyMapping exist = Mono.orm().newSearch(Database.connect().account_faculty())
                    .eq(DB.account_faculty().username, username)
                    .active().first();
            if(exist!=null) {
                Mono.fx().alert().createWarning()
                        .setMessage("A faculty is already using that username. Please provide a unique one.")
                        .show();
                return;
            }
            
            password = this.validate(txt_password, txt_password_confirm, "password", true);
            if(password==null)
                return;
            this.changeView(vbox_form5);
        });
        MonoClick.addClickEvent(btn_previous2, ()->{
            this.changeView(vbox_form1);
        });
        
        // FORM 5
        MonoClick.addClickEvent(btn_next5, ()->{
            pin = this.validate(txt_transaction_pin, txt_transaction_pin_confirm, "transaction pin", false);
            if(pin==null)
                return;
            if(pin.length()!=6) {
                Mono.fx().alert().createWarning()
                        .setMessage("Transaction pin requires 6 characters.")
                        .show();
                return;
            }
            this.changeView(vbox_form3);
        });
        MonoClick.addClickEvent(btn_previous5, ()->{
            this.changeView(vbox_form2);
        });
        
        // FORM 3
        cmb_questions.getItems().addAll(MyAccountHome.LIST_SECURITY_QUESTIONS);
        cmb_questions.getSelectionModel().selectFirst();
        MonoClick.addClickEvent(btn_next3, ()->{
            question = cmb_questions.getSelectionModel().getSelectedItem().toUpperCase();
            answer = this.validate(txt_answer, txt_answer_confirm, "recovery answer", false).toUpperCase();
            if(answer==null)
                return;
            if(this.saveNewAdmin())
                this.changeView(vbox_form4);
        });
        MonoClick.addClickEvent(btn_previous3, ()->{
            this.changeView(vbox_form5);
        });
        
        // FORM 4
        MonoClick.addClickEvent(btn_close2, ()->{
            this.onClose(true);
        });
        
        
        this.addTextFieldFilters();
    }

    @Override
    public void onDelayedStart() {
        super.onDelayedStart(); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void setValues() {
        txt_bulsu_id.setText(bulsu_id==null? "" : bulsu_id);
        txt_first_name.setText(first_name==null? "" : first_name);
        txt_last_name.setText(last_name==null? "" : last_name);
        txt_middle_name.setText(middle_name==null? "" : middle_name);
        txt_password.setText(password==null? "" : password);
        txt_password_confirm.setText(password==null? "" : password);
        txt_username.setText(username==null? "" : username);
    }
    
    private boolean saveNewAdmin() {
        // get all the current admins before creating new one
        ArrayList<AccountFacultyMapping> afMaps = Mono.orm().newSearch(Database.connect().account_faculty())
                .eq(DB.account_faculty().access_level, Access.ACCESS_ADMIN)
                .active().all();
        FacultyMapping faculty = new FacultyMapping();
        faculty.setActive(1);
        faculty.setBulsu_id(bulsu_id);
        faculty.setDesignation(Access.ACCESS_ADMIN);
        faculty.setFirst_name(first_name);
        faculty.setLast_name(last_name);
        faculty.setMiddle_name(middle_name);
        Integer newFacultyId = Database.connect().faculty().insert(faculty);
        if(newFacultyId==null){
            Notifications.create().darkStyle()
                    .title("Process Failed")
                    .text("Something went wrong.")
                    .showError();
            return false;
        }
        AccountFacultyMapping afMap = new AccountFacultyMapping();
        afMap.setAccess_level(Access.ACCESS_ADMIN);
        afMap.setActive(1);
        afMap.setFACULTY_id(newFacultyId);
        afMap.setPassword(Mono.security().hashFactory().hash_sha512(password));
        afMap.setRecovery_answer(Mono.security().hashFactory().hash_sha512(answer));
        afMap.setRecovery_question(question);
        afMap.setUsername(username);
        afMap.setTransaction_pin(Mono.security().hashFactory().hash_sha512(pin));
        Integer id = Database.connect().account_faculty().insert(afMap);
        if(id==null) {
            Notifications.create().darkStyle()
                    .title("Process Failed")
                    .text("Something went wrong. Try again later.")
                    .showError();
            return false;
        } else {
            if(afMaps==null)
                return true;
            
            // set all the previously fetched admins into access faculty
            for(AccountFacultyMapping afMap_: afMaps) {
                FacultyInformation info = new FacultyInformation(afMap_);
                afMap_.setAccess_level(Access.ACCESS_FACULTY);
                info.getFacultyMapping().setDesignation(Access.ACCESS_FACULTY);
                if(!Database.connect().account_faculty().update(afMap_) ||
                        !Database.connect().faculty().update(info.getFacultyMapping())) {
                    Notifications.create().darkStyle().text("Admin with a username of " + afMap_.getUsername()
                            + "\n is not changed into access Faculty.").showWarning();
                }
            }
        }
        return true;
    }
    
    private String validate(CustomPasswordField node1, CustomPasswordField node2, String category, boolean checkLength) {
        int res = this.check(node1, node2);
        String result = node1.getText();
        if(res==1) {
            Mono.fx().alert().createWarning()
                    .setMessage("Please enter a " + category.toLowerCase()  +".")
                    .show();
            return null;
        } else if(res==2) {
            Mono.fx().alert().createWarning()
                    .setMessage("Please re-enter " + category.toLowerCase() +".")
                    .show();
            return null;
        } else if(res==-1) {
            Mono.fx().alert().createWarning()
                    .setMessage(WordUtils.capitalizeFully(category) + " not match.")
                    .show();
            return null;
        } else if(result.length()<6 && checkLength) {
            Mono.fx().alert().createWarning()
                    .setMessage("Weak " + category + ". Minimum of 6 characters.")
                    .show();
            return null;
        }
        return result;
    }
    /**
     * 
     * @param node1
     * @param node2
     * @return 1 node1 is empty; 2 node2 is empty; 0 if node1 and 2 are equal and -1 if not
     */
    private int check(CustomPasswordField node1, CustomPasswordField node2){
        if(node1.getText().isEmpty())
            return 1;
        else if(node2.getText().isEmpty())
            return 2;
        if(node1.getText().equals(node2.getText()))
            return 0;
        else
            return -1;
    }
    
    private boolean thereIsNull(TextField... nodes) {
        for(TextField node: nodes) {
            if(MonoString.removeExtraSpace(node.getText()).isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    private void addTextFieldFilters() {
        StringFilter textFilter = TextInputFilters.string()
                .setMaxCharacters(50)
                .setNoLeadingTrailingSpaces(false)
                .setFilterManager(filterManager -> {
                    if (!filterManager.isValid()) {
                        Mono.fx().alert().createWarning().setHeader("Warning")
                                .setMessage(filterManager.getMessage())
                                .show();
                    }
                });
        textFilter.clone().setTextSource(txt_bulsu_id).applyFilter();

        StringFilter textName = TextInputFilters.string()
                .setFilterMode(StringFilter.LETTER_SPACE)
                .setMaxCharacters(100)
                .setNoLeadingTrailingSpaces(false)
                .setFilterManager(filterManager -> {
                    if (!filterManager.isValid()) {
                        Mono.fx().alert().createWarning().setHeader("Warning")
                                .setMessage(filterManager.getMessage())
                                .show();
                    }
                });
        textName.clone().setTextSource(txt_last_name).applyFilter();
        textName.clone().setTextSource(txt_first_name).applyFilter();
        textName.clone().setTextSource(txt_middle_name).applyFilter();
        
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
        textPass.clone().setTextSource(txt_password_confirm).applyFilter();
        
        StringFilter textAns = TextInputFilters.string()
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
        textAns.clone().setTextSource(txt_answer_confirm).applyFilter();
        
        StringFilter textFilterPin = TextInputFilters.string()
                .setFilterMode(StringFilter.DIGIT)
                .setMaxCharacters(50)
                .setNoLeadingTrailingSpaces(false)
                .setFilterManager(filterManager -> {
//                    if (!filterManager.isValid()) {
//                        Mono.fx().alert().createWarning().setHeader("Warning")
//                                .setMessage(filterManager.getMessage())
//                                .show();
//                    }
                });
        textFilterPin.clone().setTextSource(txt_transaction_pin).applyFilter();
        textFilterPin.clone().setTextSource(txt_transaction_pin_confirm).applyFilter();
    }
    
    private void onClose(boolean forceClose) {
        if(forceClose) {
            Mono.fx().getParentStage(btn_close).close();
            return;
        }
        
        int res = Mono.fx().alert()
                .createConfirmation()
                .setMessage("Cancel creation process?")
                .confirmYesNo();
        if(res==1) 
            Mono.fx().getParentStage(btn_close).close();
    }
    
    private void changeView(VBox node) {
        this.setValues();
        Animate.fade(node, 150, ()->{
            vbox_form1.setVisible(false);
            vbox_form2.setVisible(false);
            vbox_form3.setVisible(false);
            vbox_form4.setVisible(false);
            vbox_form5.setVisible(false);
            node.setVisible(true);
        }, vbox_form1, vbox_form2, vbox_form3, vbox_form4, vbox_form5);
    }
}

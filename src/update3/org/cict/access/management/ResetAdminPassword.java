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
import artifacts.MonoString;
import com.izum.fx.textinputfilters.StringFilter;
import com.izum.fx.textinputfilters.TextInputFilters;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.transitions.Animate;
import com.melvin.mono.fx.MonoLauncher;
import com.melvin.mono.fx.events.MonoClick;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.text.WordUtils;
import org.controlsfx.control.textfield.CustomPasswordField;
import update3.org.cict.access.Access;

/**
 *
 * @author Joemar
 */
public class ResetAdminPassword extends MonoLauncher {

    @FXML
    private VBox application_root;

    @FXML
    private JFXButton btn_close;

    @FXML
    private VBox vbox_form1;

    @FXML
    private TextField txt_username;

    @FXML
    private CustomPasswordField txt_password;

    @FXML
    private CustomPasswordField txt_password_confirm;

    @FXML
    private JFXButton btn_submit;

    @FXML
    private VBox vbox_form2;

    @FXML
    private JFXButton btn_close2;
    
    private String username, password;
    private AccountFacultyMapping account;
    @Override
    public void onStartUp() {
        // MAIN
        this.addTextFieldFilters();
        this.changeView(vbox_form1);
        MonoClick.addClickEvent(btn_close, ()->{
            this.onClose(false);
        });
        
        // FORM 1
        MonoClick.addClickEvent(btn_submit, ()->{
            if(this.thereIsNull(txt_username)) {
                Mono.fx().alert().createWarning()
                        .setMessage("Please enter a username.")
                        .show();
                return;
            }             
            username = MonoString.removeExtraSpace(txt_username.getText());
            account = this.getAccountFacultyMapping();
            if(account==null) {
                Mono.fx().alert().createWarning()
                        .setMessage("No account found with a username of " + username + ".")
                        .show();
                return;
            }
            if(!(account.getAccess_level()==null? "" : account.getAccess_level()).equalsIgnoreCase(Access.ACCESS_ADMIN)) {
                Mono.fx().alert().createWarning()
                        .setMessage("Account found with a username of " + username + " is not a System Administrator.")
                        .show();
                return;
            }
            password = this.validate(txt_password, txt_password_confirm, "password", true);
            if(password==null)
                return;
            if(this.resetPassword())
                this.changeView(vbox_form2);
        });
        
        // FORM 2
        MonoClick.addClickEvent(btn_close2, ()->{
            this.onClose(true);
        });
    }
    
    private boolean resetPassword() {
        account.setPassword(Mono.security().hashFactory().hash_sha512(password));
        return Database.connect().account_faculty().update(account);
    }
    
    private AccountFacultyMapping getAccountFacultyMapping() {
        return Mono.orm().newSearch(Database.connect().account_faculty())
                .eq(DB.account_faculty().username, username)
                .active().first();
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
    
    private boolean thereIsNull(TextField... nodes) {
        for(TextField node: nodes) {
            if(MonoString.removeExtraSpace(node.getText()).isEmpty()) {
                return true;
            }
        }
        return false;
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
        textPass.clone().setTextSource(txt_password_confirm).applyFilter();
        
    }
    
    private void onClose(boolean forceClose) {
        if(forceClose) {
            Mono.fx().getParentStage(btn_close).close();
            return;
        }
        
        int res = Mono.fx().alert()
                .createConfirmation()
                .setMessage("Cancel reset process?")
                .confirmYesNo();
        if(res==1) 
            Mono.fx().getParentStage(btn_close).close();
    }
    
    private void changeView(VBox node) {
        Animate.fade(node, 150, ()->{
            vbox_form1.setVisible(false);
            vbox_form2.setVisible(false);
            node.setVisible(true);
        }, vbox_form1, vbox_form2);
    }
}

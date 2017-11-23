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
package update3.org.cict.access;

import app.lazy.models.AccountFacultyMapping;
import com.izum.fx.textinputfilters.StringFilter;
import com.izum.fx.textinputfilters.TextInputFilters;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.melvin.mono.fx.MonoLauncher;
import com.melvin.mono.fx.events.MonoClick;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

/**
 *
 * @author Joemar
 */
public class TransactionPin extends MonoLauncher{

    @FXML
    private VBox application_root;

    @FXML
    private JFXButton btn_close;

    @FXML
    private PasswordField txt_pin;

    @FXML
    private JFXButton btn_submit;
    
    private AccountFacultyMapping user;

    public void setUser(AccountFacultyMapping user) {
        this.user = user;
    }
    
    private void requestFocus(Node control) {
        Stage stage = Mono.fx().getParentStage(control);
        stage.requestFocus();
        control.requestFocus();
    }
    
    @Override
    public void onStartUp() {
        this.filter();
//        this.requestFocus(txt_pin);
        
        MonoClick.addClickEvent(btn_close, ()->{
            Mono.fx().getParentStage(btn_close).close();
        });
        
        MonoClick.addClickEvent(btn_submit, ()->{
            this.onEnter();
        });
        Mono.fx().key(KeyCode.ENTER).release(application_root, ()->{
            this.onEnter();
        });
    }
    
    private void filter() {
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
    }
    
    private void onEnter() {
        if(user!=null) {
            String pin = Mono.security().hashFactory().hash_sha512(txt_pin.getText());
            result = (pin.equals(user.getTransaction_pin()));
            if(!result) {
                Mono.fx().alert().createWarning().setHeader("Wrong Pin")
                        .setMessage("Please enter your transaction pin correctly.")
                        .show();
                txt_pin.setText("");
            } else
                Mono.fx().getParentStage(btn_close).close();
        } 
    }
    
    private boolean result;
    public boolean canProceed() {
        return result;
    }
}

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
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
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

    private AccountFacultyMapping allowedUser;

    public AccountFacultyMapping getAllowedUser() {
        return allowedUser;
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
            onVerification();
        });
        super.addClickEvent(btn_cancel, () -> {
            super.finish();
        });
    }

    private void onVerification() {
        Verify verify = new Verify();
        verify.username = txt_registrar_username.getText();
        verify.pin = txt_pin.getText();

        verify.whenStarted(() -> {
            btn_grant.setDisable(true);
            btn_cancel.setDisable(true);
            super.cursorWait();
        });

        verify.whenCancelled(() -> {
            
        });

        verify.whenFailed(() -> {
        });

        verify.whenSuccess(() -> {
        });

        verify.whenFinished(() -> {
            btn_grant.setDisable(false);
            btn_cancel.setDisable(false);
            super.cursorDefault();

            if (verify.faculty == null) {
                // access denied
                Mono.fx().snackbar().showError(application_root, "Access Denied");
            } else {
                this.allowedUser = verify.faculty;
                super.finish();
            }
        });

        verify.transact();
    }

    private class Verify extends Transaction {

        public String username;
        public String pin;
        public AccountFacultyMapping faculty;

        @Override
        protected boolean transaction() {
            AccountFacultyMapping account = Mono.orm()
                    .newSearch(Database.connect().account_faculty())
                    .eq(DB.account_faculty().username, username)
                    .active()
                    .first();

            /**
             * No account.
             */
            if (account == null) {
                System.out.println("Account Not Existing");
                return false;
            }

            /**
             * Check access level.
             */
            if (!Access.isGrantedIfFrom(account.getAccess_level(),
                    Access.ACCESS_LOCAL_REGISTRAR,
                    Access.ACCESS_CO_REGISTRAR)) {
                // not valid access
                System.out.println("No Valid Access");
                return false;
            }

            /**
             * Check Pin Code.
             */
            String hashPassword = Mono.security().hashFactory().hash_sha512(pin);
            if (hashPassword.equals(account.getTransaction_pin())) {
                // access granted.
                // user verified
                faculty = account;
                System.out.println("Access Granted");
            }

            return true;
        }

        @Override
        protected void after() {

        }
    }

}

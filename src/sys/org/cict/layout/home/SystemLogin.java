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
package sys.org.cict.layout.home;

import app.lazy.models.AccountFacultySessionMapping;
import app.lazy.models.Database;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jhmvin.Mono;
import com.jhmvin.fx.notify.AlertMessage;
import com.jhmvin.transitions.Animate;
import com.melvin.mono.fx.MonoLauncher;
import com.melvin.mono.fx.bootstrap.M;
import com.melvin.mono.fx.events.MonoClick;
import java.util.Calendar;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.cict.GenericLoadingShow;
import org.cict.MainApplication;
import org.cict.PublicConstants;
import org.cict.ThreadMill;
import org.cict.accountmanager.forgotpassword.SystemForgot;
import org.cict.authentication.authenticator.Authenticator;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.cict.authentication.authenticator.HibernateLauncher;
import org.cict.authentication.authenticator.ValidateLogin;
import org.hibernate.criterion.Order;
import update.org.cict.controller.home.Home;
import update3.org.cict.access.Access;

/**
 *
 * @author Jhon Melvin
 */
public class SystemLogin extends MonoLauncher {

    @FXML
    private VBox application_root;

    @FXML
    private VBox vbox_login;

    @FXML
    private JFXTextField txt_username;

    @FXML
    private JFXPasswordField txt_password;

    @FXML
    private JFXButton btn_login;

    @FXML
    private JFXButton btn_register;

    @FXML
    private JFXButton btn_forgot;

    @FXML
    private JFXButton btn_settings;

    @FXML
    private VBox vbox_loading;

    @Override
    public void onStartUp() {
        //----------------------------------------------------------------------
    }

    private void bootHibernate() {
        HibernateLauncher startHibernate = Authenticator
                .instance()
                .createHibernateLauncher();

        startHibernate.whenStarted(() -> {
            this.vbox_loading.setVisible(true);
            this.vbox_login.setVisible(false);
            this.removeEnterEvent();
        });
        startHibernate.whenCancelled(() -> {
            System.out.println("ERROR");
        });
        startHibernate.whenFailed(() -> {
            System.out.println("CANCELLED");
        });
        startHibernate.whenSuccess(() -> {
//            this.vbox_loading.setVisible(false);
//            this.vbox_login.setVisible(true);
            this.autoCreateSystemVariables();
            Animate.fade(vbox_loading, 150, () -> {
                this.vbox_loading.setVisible(false);
                this.vbox_login.setVisible(true);
            }, vbox_login);
        });
        startHibernate.whenFinished(() -> {
            this.addEnterEvent();
            if (Mono.orm().getSessionFactory() == null) {
                // no connection
                Mono.fx().alert().createError().setTitle("No Connection")
                        .setHeader("Server " + PublicConstants.getServerIP() + " Unreachable")
                        .setMessage("Please check your connection then try again or change your server, just click Server's IP.")
                        .showAndWait();
//                MainApplication.die(0);
            }
        });

        startHibernate.transact();
    }

    //--------------------------------------------------------------------------
    /**
     * Event handler variable for Login when enter.
     */
    private final EventHandler<KeyEvent> loginKeyEvent = (e) -> {
        if (e.getCode().equals(KeyCode.ENTER)) {
            this.onLogin();
        }
    };

    private void addEnterEvent() {
        this.getApplicationRoot().addEventHandler(KeyEvent.KEY_PRESSED, this.loginKeyEvent);
    }

    private void removeEnterEvent() {
        this.getApplicationRoot().removeEventHandler(KeyEvent.KEY_PRESSED, this.loginKeyEvent);
    }

    private void showMessage(String type, String title, String header, String messaages) {
        this.removeEnterEvent();
        //----------------------------------------------------------------------
        AlertMessage message = null;
        switch (type) {
            case "warn":
                message = Mono.fx().alert().createWarning();
                break;
            case "error":
                message = Mono.fx().alert().createError();
            case "info":
                message = Mono.fx().alert().createInfo();
                break;
            default:
                message = Mono.fx().alert().createInfo();
                break;
        }
        //----------------------------------------------------------------------
        message.setTitle(title)
                .setHeader(header)
                .setMessage(messaages)
                .showAndWait();
        //----------------------------------------------------------------------
        this.addEnterEvent();

    }
    //--------------------------------------------------------------------------

    //--------------------------------------------------------------------------
    @Override

    public void onDelayedStart() {
        if (Mono.orm().isStarted()) {
            System.out.println("Starting ORM");
            /**
             * Hibernate was already started and this login screen was a recall.
             */
        } else {
            /**
             * Initial Login Screen.
             */

            this.bootHibernate();
        }

        onStageClosing();

        //----------------------------------------------------------------------
//        Mono.fx().key(KeyCode.ENTER).release(this.getApplicationRoot(), () -> {
//            this.onLogin();
//        });
        this.addEnterEvent();
        MonoClick.addClickEvent(btn_login, () -> {
            this.onLogin();
        });

//        this.btnExit.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
//            this.onDestroyApplication();
//        });
        MonoClick.addClickEvent(btn_register, () -> {
//            MainApplication.HOST_SERVICE.showDocument(PublicConstants.FACULTY_REGISTRATION_LINK);
            this.onShowRegister();
        });
        MonoClick.addClickEvent(btn_forgot, () -> {
//            MainApplication.HOST_SERVICE.showDocument(PublicConstants.FACULTY_FORGOT_LINK);
            this.onShowForgotPassword();
        });
        MonoClick.addClickEvent(btn_settings, (() -> {
            Settings set = M.load(Settings.class);
            set.onDelayedStart();
            try {
                System.out.println("Stage Recycled. ^^v");
                set.getCurrentStage().showAndWait();
            } catch (NullPointerException e) {
                Stage a = set.createChildStage(super.getCurrentStage());
                a.initStyle(StageStyle.UNDECORATED);
                a.showAndWait();
            }
        }));

    }

    /**
     * Since only the login and the main application have their own stages. we
     * need to assign a closing event in this stage.
     */
    public void onStageClosing() {
        this.getCurrentStage().setOnCloseRequest(close -> {
            try {
                this.onDestroyApplication();
            } catch (Exception e) {
                // if the loading and booting up of hibernate and the user wanted to exit.
                // this will force the application to close.
                MainApplication.die(0);
            }
            close.consume();
        });
    }

    //--------------------------------------------------------------------------
    private void onLogin() {
        String user = this.txt_username.getText().trim();
        String pass = this.txt_password.getText().trim();
        if (this.checkEmpty(user, pass)) {
            boolean isSystem = SystemAccess.checkSystemAccess(user, pass);
            if (isSystem) {
                // launch app with system privilages.
                CollegeFaculty.instance().setACCESS_LEVEL(Access.ACCESS_SYSTEM);
                // execute system privelage
                this.close();
                Home.launchSystem();
                return;
            }

            ValidateLogin validateLogin = Authenticator
                    .instance()
                    .createLoginValidator();
            // transaction variables
            validateLogin.username = user;
            validateLogin.password = pass;

            validateLogin.whenStarted(() -> {
                GenericLoadingShow.instance().show();
            });

            validateLogin.whenSuccess(() -> {
                GenericLoadingShow.instance().hide();
                if (validateLogin.isAuthenticated()) {
                    checkAccess(validateLogin.getCreatedSessionID());
                } else {
//                    Mono.fx()
//                            .alert()
//                            .createInfo()
//                            .setTitle("Authentication Gateway")
//                            .setHeader("Authentication Failed")
//                            .setMessage(validateLogin.getAuthenticatorMessage())
//                            .showAndWait();
                    showMessage("info", "Authentication Gateway", "Authentication Failed", validateLogin.getAuthenticatorMessage());
                    if(!validateLogin.isAccountExisting()) {
                        txt_username.setText("");
                        txt_password.setText("");
                        this.requestFocus(txt_username);
                    } else if(validateLogin.isWrongPassword()) {
                        txt_password.setText("");
                        this.requestFocus(txt_password);
                    }
                }
            });

            validateLogin.whenCancelled(() -> {
                onLoginError();
            });

            validateLogin.whenFailed(() -> {
                onLoginError();
            });

            validateLogin.setRestTime(500);
            validateLogin.transact();
        }
    }

    private void onLoginError() {
        GenericLoadingShow.instance().hide();
//        Mono.fx()
//                .alert()
//                .createError()
//                .setTitle("Authentication Gateway")
//                .setHeader("Authentication Error")
//                .setMessage("We cannot process your login request at "
//                        + "the moment. Sorry For The Inconvenience, Thank You!")
//                .showAndWait();
        showMessage("error", "Authentication Gateway", "Authentication Error",
                "We cannot process your login request at the moment. Sorry For The Inconvenience, Thank You!");
    }

    /**
     * Login Success show main window close the old one.
     */
    private void showMainEvaluation() {
        this.close();
        /**
         * Change redirect to home.
         */
        Home.launchApp();
    }

    /**
     * Kill this application.
     */
    public void onDestroyApplication() {
        /**
         * DOuble Kill.
         */
        ThreadMill.threads().shutdown();
        /**
         * Shutdown the ORM
         */
        Mono.orm().shutdown();
        /**
         * Close the stage.
         */
        this.getCurrentStage().close();
        /**
         * When you try to kill someone make sure that they are dead before
         * leaving.
         */
        MainApplication.die(0);
    }

    /**
     * Check empty fields.
     *
     * @param user
     * @param pass
     * @return
     */
    private boolean checkEmpty(String user, String pass) {
        if (user.isEmpty()) {
//            this.removeEnterEvent();
//            Mono.fx()
//                    .alert()
//                    .createWarning()
//                    .setTitle("Credentials")
//                    .setHeader("Username Field is Empty!")
//                    .setMessage("Please fill up the field with your username. Thank You !")
//                    .showAndWait();
//            this.addEnterEvent();
            showMessage("warn", "Credentials", "Username Field is Empty!", "Please fill up the field with your username. Thank You !");

            return false;
        }
        if (pass.isEmpty()) {
//            Mono.fx()
//                    .alert()
//                    .createWarning()
//                    .setTitle("Credentials")
//                    .setHeader("Password Field is Empty!")
//                    .setMessage("Please fill up the field with your password. Thank You !")
//                    .showAndWait();

            showMessage("warn", "Credentials", "Password Field is Empty!", "Please fill up the field with your password. Thank You !");
            return false;
        }
        return true;
    }

    /**
     * Checks the access of the user.
     *
     * @param sessionID
     */
    private void checkAccess(Integer sessionID) {
        /**
         * Access is denied with true flag means that any access level above the
         * required can still access the system.
         */
//        if (Access.isDeniedIfNot(Access.ACCESS_EVALUATOR, true)) {
//            Mono.fx()
//                    .alert()
//                    .createError()
//                    .setTitle("Authentication Gateway")
//                    .setHeader("Access Denied")
//                    .setMessage("You do not have enough access permission to continue. Thank You !")
//                    .showAndWait();
//            return;
//        }
        /**
         * Create Keep Alive Thread.
         */
        ThreadMill.threads().KEEP_ALIVE_THREAD.setTask(() -> {
            try {
                AccountFacultySessionMapping accountSessionAlive = Mono.orm()
                        .newSearch(Database.connect().account_faculty_session())
                        .eq("FACULTY_account_id", CollegeFaculty.instance().getACCOUNT_ID())
                        .active(Order.desc("session_id"))
                        .first();
                Calendar now = Mono.orm().getServerTime().getCalendar();
                now.add(Calendar.MINUTE, 1);
                accountSessionAlive.setKeep_alive(now.getTime());
                Database.connect().account_faculty_session().update(accountSessionAlive);
            } catch (Exception e) {
                System.err.println("KEEP-ALIVE-THREAD ERROR");
            }
        });
        ThreadMill.threads().KEEP_ALIVE_THREAD.start();
        /**
         * Show window.
         */
        showMainEvaluation();
    }

    private void requestFocus(Node control) {
        Stage stage = Mono.fx().getParentStage(control);
        stage.requestFocus();
        control.requestFocus();
    }

    private void onShowRegister() {
        Mono.fx().create()
                .setPackageName("org.cict.accountmanager")
                .setFxmlDocument("SystemRegister")
                .makeFX()
                .makeScene()
                .makeStageApplication()
                .stageResizeable(false)
                .stageTitle("CICT | Enrollment Evaluation Management System | Register")
                .stageShowAndWait();
    }

    private void onShowForgotPassword() {
        SystemForgot forgotFx = M.load(SystemForgot.class);
        Stage forgotStage = forgotFx.createStageApplication();
        forgotStage.setResizable(false);
        forgotStage.setTitle("CICT | Enrollment Evaluation Management System | Recover Account");
        forgotFx.onDelayedStart();
        forgotStage.show();
    }
    
    private void autoCreateSystemVariables() {
        PublicConstants.getSystemVar_BULSU_TEL();
        PublicConstants.getSystemVar_FTP_PASSWORD();
        PublicConstants.getSystemVar_FTP_PORT();
        PublicConstants.getSystemVar_FTP_SERVER();
        PublicConstants.getSystemVar_FTP_USERNAME();
        PublicConstants.getSystemVar_MAX_POPULATION();
        PublicConstants.getSystemVar_RECOMMENDNG_APPRVL();
        PublicConstants.getSystemVar_REGISTRAR();
        PublicConstants.getSystemVar_SMS_SERVER();
        PublicConstants.getSystemVar_Noted_By();
        PublicConstants.getSystemVar_LocalRegistrar1();
        PublicConstants.getSystemVar_LocalRegistrar2();
    }
}

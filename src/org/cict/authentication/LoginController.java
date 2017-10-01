/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cict.authentication;

import app.lazy.models.AcademicTermMapping;
import app.lazy.models.AccountFacultySessionMapping;
import app.lazy.models.Database;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.display.ControllerFX;
import java.util.Calendar;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.cict.GenericLoadingShow;
import org.cict.ThreadMill;
import org.cict.authentication.authenticator.Authenticator;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.cict.authentication.authenticator.HibernateLauncher;
import org.cict.authentication.authenticator.ValidateLogin;
import org.cict.evaluation.evaluator.Evaluator;
import org.hibernate.criterion.Order;
import update.org.cict.controller.home.Home;
import update3.org.cict.access.Access;

/**
 * FXML Controller class
 *
 * @author Jhon Melvin
 */
public class LoginController implements ControllerFX {

    @FXML
    private AnchorPane pnlLoading;

    @FXML
    private AnchorPane pnlLogin;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private JFXButton btnLogin;

    @FXML
    private JFXButton btnExit;

    @FXML
    private ImageView imgInvUser;

    @FXML
    private ImageView imgInvPassword;

    @FXML
    private Label btnRegister;

    @FXML
    private Label btn_forgotpass;

    public LoginController() {

    }

    @Override
    public void onInitialization() {

        /**
         * Start Hibernate.
         */
        HibernateLauncher startHibernate = Authenticator
                .instance()
                .createHibernateLauncher();

        startHibernate.setOnStart(start -> {
            this.pnlLoading.setVisible(true);
            this.pnlLogin.setVisible(false);
        });

        startHibernate.setOnSuccess(onSuccess -> {
            this.pnlLoading.setVisible(false);
            this.pnlLogin.setVisible(true);
        });

        startHibernate.transact();

        imgInvUser.setVisible(false);
        imgInvPassword.setVisible(false);
    }

    @Override
    public void onEventHandling() {
        this.buttonEvents();
        this.textFieldEvents();
    }

    private void textFieldEvents() {
        this.txtUsername.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            this.imgInvUser.setVisible(false);
        });
        this.txtPassword.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            this.imgInvPassword.setVisible(false);
        });
    }

    private void buttonEvents() {

        Mono.fx().key(KeyCode.ENTER).release(pnlLogin, () -> {
            this.onLogin();
        });

        this.btnLogin.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
            this.onLogin();
        });
        this.btnExit.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
            this.onClose();
        });
        this.btnRegister.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
            this.onShowRegister();
        });
        this.btn_forgotpass.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
            this.onShowForgotPassword();
        });
    }

    /**
     * Login Success show main window close the old one.
     */
    private void showMainEvaluation() {
        AcademicTermMapping currentAcadTerm = Evaluator.instance()
                .getCurrentAcademicTerm();
        Mono.fx().getParentStage(this.btnLogin).close();
        /**
         * Change redirect to home.
         */
        Home.callHome();
    }

    private void onLogin() {
        String user = this.txtUsername.getText().trim();
        String pass = this.txtPassword.getText().trim();
        if (this.checkEmpty(user, pass)) {
            ValidateLogin validateLogin = Authenticator
                    .instance()
                    .createLoginValidator();
            // transaction variables
            validateLogin.username = user;
            validateLogin.password = pass;

            validateLogin.setOnStart(start -> {
                GenericLoadingShow.instance().show();
            });

            validateLogin.setOnSuccess(onSuccess -> {
                GenericLoadingShow.instance().hide();
                if (validateLogin.isAuthenticated()) {
                    checkAccess(validateLogin.getCreatedSessionID());
                } else {
                    Mono.fx()
                            .alert()
                            .createInfo()
                            .setTitle("Authentication Gateway")
                            .setHeader("Authentication Failed")
                            .setMessage(validateLogin.getAuthenticatorMessage())
                            .showAndWait();
                }
            });

            validateLogin.setOnCancel(cancel -> {
                onLoginError();
            });

            validateLogin.setOnFailure(onFailure -> {
                onLoginError();
            });

            validateLogin.setRestTime(500);
            validateLogin.transact();
        }
    }

    private void checkAccess(Integer sessionID) {
        /**
         * Access is denied with true flag means that any access level above the
         * required can still access the system.
         */
        if (Access.isDeniedIfNot(Access.ACCESS_EVALUATOR, true)) {
            Mono.fx()
                    .alert()
                    .createError()
                    .setTitle("Authentication Gateway")
                    .setHeader("Access Denied")
                    .setMessage("You do not have enough access permission to continue. Thank You !")
                    .showAndWait();
            return;
        }
        /**
         * Create Keep Alive Thread.
         */
        ThreadMill.threads().KEEP_ALIVE_THREAD.setTask(() -> {
            AccountFacultySessionMapping accountSessionAlive = Mono.orm()
                    .newSearch(Database.connect().account_faculty_session())
                    .eq("FACULTY_account_id", CollegeFaculty.instance().getACCOUNT_ID())
                    .active(Order.desc("session_id"))
                    .first();
            Calendar now = Mono.orm().getServerTime().getCalendar();
            now.add(Calendar.MINUTE, 1);
            accountSessionAlive.setKeep_alive(now.getTime());
            Database.connect().account_faculty_session().update(accountSessionAlive);
        });
        ThreadMill.threads().KEEP_ALIVE_THREAD.start();
        /**
         * Show window.
         */
        showMainEvaluation();

    }

    /**
     * If something went wrong.
     */
    private void onLoginError() {
        GenericLoadingShow.instance().hide();
        Mono.fx()
                .alert()
                .createError()
                .setTitle("Authentication Gateway")
                .setHeader("Authentication Error")
                .setMessage("We cannot process your login request at "
                        + "the moment. Sorry For The Inconvenience, Thank You!")
                .showAndWait();
    }

    private boolean checkEmpty(String user, String pass) {
        if (user.isEmpty()) {
            Mono.fx()
                    .alert()
                    .createWarning()
                    .setTitle("Credentials")
                    .setHeader("Username Field is Empty!")
                    .setMessage("Please fill up the field with your username. Thank You !")
                    .showAndWait();
            this.requestFocus(this.txtUsername);
            this.imgInvUser.setVisible(true);
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
            this.requestFocus(this.txtPassword);
            this.imgInvPassword.setVisible(true);
            return false;
        }
        return true;
    }

    private void requestFocus(Node control) {
        Stage stage = Mono.fx().getParentStage(control);
        stage.requestFocus();
        control.requestFocus();
    }

    public void onShowRegister() {
        Mono.fx().getParentStage(this.btnLogin).close();

        Mono.fx().create()
                .setPackageName("org.cict.accountmanager")
                .setFxmlDocument("Register")
                .makeFX()
                .makeScene()
                .makeStageApplication()
                .stageMinDimension(524, 480)
                .stageShowAndWait();
    }

    public void onShowForgotPassword() {
        //ForgotPasswordController controller = new ForgotPasswordController();
        //MonoWindow.startWindow("cict/auth", "ForgotPassword", controller, "Forgot Password");
    }

    public void onClose() {
        //close session
//        Mono.orm().closeSessionFactory();
        //system exit
        Runtime.getRuntime().halt(0);
    }
}

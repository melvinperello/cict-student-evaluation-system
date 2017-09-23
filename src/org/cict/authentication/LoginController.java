/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cict.authentication;

import app.lazy.models.AcademicTermMapping;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.display.ControllerFX;
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
import org.cict.authentication.authenticator.Authenticator;
import org.cict.authentication.authenticator.HibernateLauncher;
import org.cict.authentication.authenticator.ValidateLogin;
import org.cict.evaluation.evaluator.Evaluator;

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
        Mono.fx().create()
                .setPackageName("update.org.cict.layout.home")
                .setFxmlDocument("home")
                .makeFX()
                .makeScene()
                .makeStageApplication()
                .stageMaximized(true)
                .stageShow();

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
                    // next stage
                    showMainEvaluation();
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

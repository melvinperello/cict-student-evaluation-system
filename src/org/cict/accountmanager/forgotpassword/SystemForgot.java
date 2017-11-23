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
package org.cict.accountmanager.forgotpassword;

import app.lazy.models.AccountFacultyMapping;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jhmvin.Mono;
import com.jhmvin.transitions.Animate;
import com.melvin.mono.fx.MonoLauncher;
import com.melvin.mono.fx.events.MonoClick;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 *
 * @author Joemar
 */
public class SystemForgot extends MonoLauncher {

    @FXML
    private VBox application_root;

    @FXML
    private VBox vbox_home;

    @FXML
    private Label lbl_question1;

    @FXML
    private JFXTextField txt_bulsu_id;

    @FXML
    private JFXButton btn_reset_home;

    @FXML
    private JFXButton btn_close;

    @FXML
    private VBox vbox_forgot;

    @FXML
    private Label lbl_question;

    @FXML
    private JFXPasswordField txt_answer;

    @FXML
    private JFXButton btn_reset_forgot;

    @FXML
    private JFXButton btn_forgot_answer;

    @FXML
    private VBox vbox_otp;

    @FXML
    private Label lbl_mobile_no;

    @FXML
    private Label lbl_reference_no;

    @FXML
    private JFXTextField txt_otp;

    @FXML
    private Label lbl_resend;

    @FXML
    private JFXButton btn_reset_otp;

    @FXML
    private JFXButton btn_back_otp;

    @FXML
    private VBox vbox_reset;

    @FXML
    private JFXPasswordField txt_password;

    @FXML
    private JFXPasswordField txt_password_confirm;

    @FXML
    private JFXButton btn_reset_now;
    
    @Override
    public void onStartUp() {
        this.changeView(vbox_home);
        
        MonoClick.addClickEvent(btn_close, ()->{
            Mono.fx().getParentStage(btn_close).close();
        });
    }

    @Override
    public void onDelayedStart() {
        MonoClick.addClickEvent(btn_reset_home, ()->{
            this.changeView(vbox_forgot);
        });
        
        MonoClick.addClickEvent(btn_reset_forgot, ()->{
            this.changeView(vbox_reset);
        });
        
        MonoClick.addClickEvent(btn_forgot_answer, ()->{
            this.changeView(vbox_otp);
        });
        
        MonoClick.addClickEvent(btn_forgot_answer, ()->{
            this.changeView(vbox_otp);
        });
        
        MonoClick.addClickEvent(btn_reset_otp, ()->{
            this.changeView(vbox_reset);
        });
        
        MonoClick.addClickEvent(btn_back_otp, ()->{
            this.changeView(vbox_forgot);
        });
        
        MonoClick.addClickEvent(btn_reset_now, ()->{
            Mono.fx().alert().createInfo()
                    .setHeader("Successful!")
                    .setMessage("You can now login using your new password.")
                    .show();
            Mono.fx().getParentStage(btn_close).close();
        });
        
        super.onDelayedStart(); //To change body of generated methods, choose Tools | Templates.
    }
    
    private AccountFacultyMapping account;
    public void setAccount(AccountFacultyMapping account) {
        this.account = account;
    }
    
    private void setDetails() {
        
    }
    
    private void changeView(Node node) {
        Animate.fade(node, 150, ()->{
            vbox_home.setVisible(false);
            vbox_forgot.setVisible(false);
            vbox_otp.setVisible(false);
            vbox_reset.setVisible(false);
            node.setVisible(true);
        }, vbox_home, vbox_forgot, vbox_otp, vbox_reset);
    }
}

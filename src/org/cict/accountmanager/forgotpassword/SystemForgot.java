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

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.melvin.mono.fx.MonoLauncher;
import javafx.fxml.FXML;
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
    private VBox vbox_forgot;

    @FXML
    private Label lbl_question;

    @FXML
    private JFXPasswordField txt_answer;

    @FXML
    private JFXButton btn_reset;

    @FXML
    private JFXButton btn_forgot_answer;

    @FXML
    private VBox vbox_otp;

    @FXML
    private Label lbl_mobile_no;

    @FXML
    private Label lbl_reference_no;

    @FXML
    private JFXPasswordField txt_otp;

    @FXML
    private JFXButton btn_reset2;

    @FXML
    private JFXButton btn_back;

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
    
    }
    
}

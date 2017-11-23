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
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.FacultyMapping;
import app.lazy.models.OtpGeneratorMapping;
import artifacts.MonoString;
import artifacts.OTPGenerator;
import artifacts.SMSWrapper;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.CronThread;
import com.jhmvin.transitions.Animate;
import com.melvin.mono.fx.MonoLauncher;
import com.melvin.mono.fx.events.MonoClick;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.lang3.text.WordUtils;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.controlsfx.control.Notifications;
import org.hibernate.criterion.Order;

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
        
        this.setTimer();
        
        MonoClick.addClickEvent(lbl_resend, ()->{
            this.resendOTP();
        });
    }

    private void resendOTP() {
        int res = Mono.fx().alert().createConfirmation()
                .setHeader("Resend OTP")
                .setMessage("Resending OTPs will change the code "
                        + "sent to the receiver. Make sure to notice the "
                        + "reference number. Continue resending action?")
                .confirmYesNo();
        if(res==-1)
            return;
        if(this.sendOTP()){
            this.setTimer();
            timer.start();
        }
    }
    
    @Override
    public void onDelayedStart() {
        MonoClick.addClickEvent(btn_reset_home, ()->{
            this.validateBulSUID();
        });
        
        MonoClick.addClickEvent(btn_reset_forgot, ()->{
            this.checkAnswer();
        });
        
        MonoClick.addClickEvent(btn_forgot_answer, ()->{
            if(canReceiveMsg) {
                if(this.sendOTP())
                    this.changeView(vbox_otp);
            } else {
                Mono.fx().alert().createWarning()
                        .setHeader("No Mobile No. Found")
                        .setMessage("If this was the last choice you have to recover the account, please ask your admin to add or update your mobile number.")
                        .show();
                btn_forgot_answer.setDisable(true);
            }
        });
        
        MonoClick.addClickEvent(btn_reset_otp, ()->{
            String OTPinput = MonoString.removeSpaces(txt_otp.getText());
            if(Mono.security().hashFactory().hash_sha512(OTPinput).equals(OTPmap.getCode()))
                this.changeView(vbox_reset);
            else {
                Mono.fx().alert().createWarning()
                        .setHeader("OTP Not Matched!")
                        .setMessage("Please check the OTP sent.")
                        .show();
            }
        });
        
        MonoClick.addClickEvent(btn_back_otp, ()->{
            this.changeView(vbox_forgot);
        });
        
        MonoClick.addClickEvent(btn_reset_now, ()->{
            if(!this.checkPassword())
                return;
            account.setPassword(Mono.security().hashFactory().hash_sha512(this.txt_password.getText().trim()));
            if(Database.connect().account_faculty().update(account)) {
                Mono.fx().alert().createInfo()
                        .setHeader("Successful!")
                        .setMessage("You can now login using your new password.")
                        .show();
                Mono.fx().getParentStage(btn_close).close();
            } else {
                Mono.fx().alert().createError()
                        .setHeader("Failed")
                        .setMessage("Please check your connectivity to the server.")
                        .show();
            }
        });
        
        super.onDelayedStart(); //To change body of generated methods, choose Tools | Templates.
    }
    
    private boolean checkPassword() {
        String newPassword = this.txt_password.getText().trim();
        String confirmNewPassword = this.txt_password_confirm.getText().trim();
        if (newPassword.isEmpty()) {
            Mono.fx()
                    .alert()
                    .createWarning()
                    .setTitle("Credentials")
                    .setHeader("Password Field is Empty!")
                    .setMessage("Please fill up the field with your password. Thank You !")
                    .showAndWait();
            this.requestFocus(this.txt_password);
            return false;
        }
        if(newPassword.length()<5){
            Mono.fx()
                    .alert()
                    .createWarning()
                    .setTitle("Credentials")
                    .setHeader("Weak Password!")
                    .setMessage("Please fill up the field password with minimum number of six(6) characters. This is for a more secured password.")
                    .showAndWait();
            this.requestFocus(this.txt_password);
            return false;
        }
        if (!confirmNewPassword.equals(newPassword)) {
            Mono.fx()
                    .alert()
                    .createWarning()
                    .setTitle("Credentials")
                    .setHeader("Password Not Matched!")
                    .setMessage("Please fill up the field confirm password correctly. Thank You !")
                    .showAndWait();
            this.requestFocus(this.txt_password_confirm);
            return false;
        }
        return true;
    }
    
    private void requestFocus(Node control) {
        Stage stage = Mono.fx().getParentStage(control);
        stage.requestFocus();
        control.requestFocus();
    }
    
    private void checkAnswer() {
        String answer = MonoString.removeExtraSpace(this.txt_answer.getText().toUpperCase());
        if(Mono.security().hashFactory().hash_sha512(answer).equals(account.getRecovery_answer())) {
            this.changeView(vbox_reset);
        } else {
            Mono.fx().alert().createInfo()
                    .setHeader("Answer Not Matched!")
                    .setMessage("Think another answer.")
                    .show();
        }
    }
    
    private OtpGeneratorMapping OTPmap;
    private boolean sendOTP() {
        lbl_reference_no.setText("Loading...");
        OTPmap = new OtpGeneratorMapping();
        String OTP_raw = OTPGenerator.generateOTP();
        OTPmap.setActive(1);
        OTPmap.setCode(Mono.security().hashFactory().hash_sha512(OTP_raw));
        OTPmap.setDate_created(Mono.orm().getServerTime().getDateWithFormat());
        Integer refID = Database.connect().otp_generator().insert(OTPmap);
        if(refID.equals(-1)) {
            Notifications.create().darkStyle().title("Failed")
                    .text("Please check your connectivity to\n"
                            + "the server.").showWarning();
            return false;
        }
        System.out.println("Reference Number: " + refID);
        System.out.println("Your One-Time Password (OTP) is " + OTP_raw);
        SMSWrapper.send(faculty.getMobile_number(),"Your One-Time Password (OTP) is " + OTP_raw + ". Reference Number: " + refID 
                + "\n\nSent by Monosync", "SYSTEM", response -> {
            System.out.println("RESPONSE: " + response);
            if(response.equalsIgnoreCase("FAILED")) {
                Mono.fx().thread().wrap(()->{
                    Notifications.create().darkStyle().title("Failed")
                            .text("Sending of OTP to the Local Registrar\nfailed.").showError();
                });
            } else if(response.equalsIgnoreCase("NO_REPLY")) {
                Mono.fx().thread().wrap(()->{
                    Notifications.create().darkStyle().title("No Response")
                            .text("Please try again later.").showError();
                });
            } else if(response.equalsIgnoreCase("SENDING")) {
                Mono.fx().thread().wrap(()->{
                    Notifications.create().darkStyle().title("Sending")
                            .text("Please wait till you receive the OTP.").showInformation();
                    lbl_reference_no.setText(refID+"");
                });
            }
        });
        return true;
    }
    
    
    private AccountFacultyMapping account;
    private FacultyMapping faculty;
    
    private void validateBulSUID() {
        String bulsuID = MonoString.removeExtraSpace(txt_bulsu_id.getText().toUpperCase());
        if(bulsuID==null || bulsuID.isEmpty()) {
            Mono.fx().alert().createWarning()
                    .setHeader("BulSU ID Field is Empty!")
                    .setMessage("To recover account, you must enter your BulSU ID.")
                    .show();
            return;
        }
        faculty = Mono.orm().newSearch(Database.connect().faculty())
                .eq(DB.faculty().bulsu_id, bulsuID).active(Order.desc(DB.faculty().id)).first();
        if(faculty==null) {
            Mono.fx().alert().createWarning()
                    .setHeader("No Faculty Found!")
                    .setMessage("No faculty with a BulSU ID of \"" + bulsuID + "\".")
                    .show();
            return;
        }
        account = Mono.orm().newSearch(Database.connect().account_faculty())
                .eq(DB.account_faculty().FACULTY_id, faculty.getId()).active(Order.desc(DB.account_faculty().id)).first();
        if(account==null) {
            int res = Mono.fx().alert().createConfirmation()
                    .setHeader("No Account Found!")
                    .setMessage("Faculty is existing but with no account.")
                    .confirmCustom("Register", "Cancel");
            if(res==1) {
                Mono.fx().getParentStage(btn_close).close();
                this.onShowRegister();
            }
            return;
        }
        this.setDetails();
        this.changeView(vbox_forgot);
    }
    
    private boolean canReceiveMsg;
    private void setDetails() {
        String mobileNo = faculty.getMobile_number();
        lbl_question.setText(MonoString.defaultString(account.getRecovery_question()));
        if(mobileNo==null || mobileNo.isEmpty()) {
            canReceiveMsg = false;
            lbl_mobile_no.setText("No mobile number found.");
        } else {
            canReceiveMsg = true;
            lbl_mobile_no.setText("*******" + mobileNo.substring(9, 13));
        }
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
    
    private void changeView(Node node) {
        Animate.fade(node, 150, ()->{
            vbox_home.setVisible(false);
            vbox_forgot.setVisible(false);
            vbox_otp.setVisible(false);
            vbox_reset.setVisible(false);
            node.setVisible(true);
        }, vbox_home, vbox_forgot, vbox_otp, vbox_reset);
    }
    
    private CronThread timer;
    private int time = 60;
    private void setTimer() {
        timer = new CronThread("timer_for_resend");
        timer.setInterval(1000);
        timer.setTask(()->{
            this.time--;
            if(this.time==0){
                this.time = 60;
                Mono.fx().thread().wrap(()->{
                    lbl_resend.setText("Didn't receive the code? Click here to resend.");
                    lbl_resend.setDisable(false);
                });
                timer.stop();
            } else {
                Mono.fx().thread().wrap(()->{
                    lbl_resend.setText("Resend is disabled. " + this.time + " second" + ((time>1)? "s" : "") + " left.");
                    lbl_resend.setDisable(true);
                });
            }
        });
    }
}

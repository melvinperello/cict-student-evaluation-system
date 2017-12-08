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
import app.lazy.models.OtpGeneratorMapping;
import artifacts.OTPGenerator;
import artifacts.SMSWrapper;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.CronThread;
import com.melvin.mono.fx.MonoLauncher;
import com.melvin.mono.fx.bootstrap.M;
import com.melvin.mono.fx.events.MonoClick;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.lang3.text.WordUtils;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.controlsfx.control.Notifications;

/**
 *
 * @author Joemar
 */
public class AssistantRegistrarOverride extends MonoLauncher {

    @FXML
    private VBox application_root;

    @FXML
    private Label lbl_mobile_number;

    @FXML
    private Label lbl_ref_number;

    @FXML
    private Label lbl_resend;
            
    @FXML
    private TextField txt_otp;

    @FXML
    private JFXButton btn_proceed;

    @FXML
    private JFXButton btn_cancel;
    
    private static String contactNumber = "";
    private static String transactionRequest_;
    public static boolean isAuthorized(Stage stage, String transactionRequest) {
        transactionRequest_ = transactionRequest;
        if(!Access.isGrantedIf(Access.ACCESS_CO_REGISTRAR, Access.ACCESS_LOCAL_REGISTRAR))
            return false;
        AccountFacultyMapping localRegistrarAcc = Mono.orm().newSearch(Database.connect().account_faculty())
                .eq(DB.account_faculty().access_level, Access.ACCESS_LOCAL_REGISTRAR).active().first();
        if(localRegistrarAcc != null) {
            FacultyMapping localRegistrar = Database.connect().faculty().getPrimary(localRegistrarAcc.getFACULTY_id());
            contactNumber = localRegistrar==null? "" : localRegistrar.getMobile_number();
        }
        if(contactNumber==null || contactNumber.isEmpty()) {
            Notifications.create().darkStyle().title("Sending of OTP failed")
                    .text("No mobile number found. Please update the\n"
                            + "Local Registrar's account.").showWarning();
            return false;
        }
        int res = Mono.fx().alert().createConfirmation()
                .setMessage("The system will send a One-Time Password (OTP) to the current Local Registrar. You will use this to authorize the Access Local Registrar transaction. Continue?")
                .setHeader("Send OTP To Authorize").confirmYesNo();
        if(res==-1)
            return false;
        
        if(!Access.enterTransactionPin(stage))
            return false;
        
        OtpGeneratorMapping map = new OtpGeneratorMapping();
        String OTP_raw = OTPGenerator.generateOTP();
        map.setActive(1);
        map.setCode(Mono.security().hashFactory().hash_sha512(OTP_raw));
        map.setDate_created(Mono.orm().getServerTime().getDateWithFormat());
        Integer refID = Database.connect().otp_generator().insert(map);
        if(refID.equals(-1)) {
            Notifications.create().darkStyle().title("Failed")
                    .text("Please check your connectivity to\n"
                            + "the server.").showWarning();
            return false;
        }
        System.out.println("Reference Number: " + refID);
        System.out.println("Your One-Time Password (OTP) is " + OTP_raw);
//        String msg = SystemOverriding.getACRONYM(15, transactionRequest_) + " Code:" + OTP_raw + " REQ.BY:" + SystemOverriding.getACRONYM(15, CollegeFaculty.instance().getLAST_NAME(), "name") + " " +  WordUtils.initials(CollegeFaculty.instance().getFIRST_NAME()) + ". Ref.No:" + refID
//                + " <eems-no-reply>";

        String msg = Access.getFORMATTED_MESSAGE(SystemOverriding.getACRONYM(15, transactionRequest_), OTP_raw, CollegeFaculty.instance().getLAST_NAME().replaceAll(" ", "") + " " +  WordUtils.initials(CollegeFaculty.instance().getFIRST_NAME()), refID);
        System.out.println(msg);
        SMSWrapper.send(contactNumber, msg, WordUtils.capitalizeFully(CollegeFaculty.instance().getFirstLastName()), response -> {
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
                            .text("Please wait for the message.").showInformation();
                });
            }
        });
        
        AssistantRegistrarOverride asstRegistrar =  M.load(AssistantRegistrarOverride.class);
        asstRegistrar.onDelayedStart();
        asstRegistrar.setDetails(contactNumber, refID);
        boolean authorized = false;
        try {
            asstRegistrar.getCurrentStage().showAndWait();
        } catch (NullPointerException e) {
            Stage a = asstRegistrar.createChildStage(stage);
            a.initStyle(StageStyle.UNDECORATED);
            a.showAndWait();
            authorized = asstRegistrar.isAuthorized();
        }
        System.out.println(authorized);
        return authorized;
    }
    
//    public static boolean isAuthorized(Stage stage, String...authorizedAccessLevels) {
//        boolean access = Access.isGrantedIf(authorizedAccessLevels);
//        if(!access) {
//            return false;
//        }
//        AccountFacultyMapping localRegistrarAcc = Mono.orm().newSearch(Database.connect().account_faculty())
//                .eq(DB.account_faculty().access_level, Access.ACCESS_LOCAL_REGISTRAR).active().first();
//        if(localRegistrarAcc != null) {
//            FacultyMapping localRegistrar = Database.connect().faculty().getPrimary(localRegistrarAcc.getFACULTY_id());
//            contactNumber = localRegistrar==null? "" : localRegistrar.getMobile_number();
//        }
//        if(contactNumber==null || contactNumber.isEmpty()) {
//            Notifications.create().darkStyle().title("Sending of OTP failed")
//                    .text("No mobile number found. Please update the\n"
//                            + "Local Registrar's account.").showWarning();
//            return false;
//        }
//        int res = Mono.fx().alert().createConfirmation()
//                .setMessage("The system will send a One-Time Password (OTP) to the current Local Registrar. You will use this to authorize the transaction. Continue?")
//                .setHeader("Send OTP To Authorize").confirmYesNo();
//        if(res==-1)
//            return false;
//        OtpGeneratorMapping map = new OtpGeneratorMapping();
//        String OTP_raw = OTPGenerator.generateOTP();
//        map.setActive(1);
//        map.setCode(Mono.security().hashFactory().hash_sha512(OTP_raw));
//        map.setDate_created(Mono.orm().getServerTime().getDateWithFormat());
//        Integer refID = Database.connect().otp_generator().insert(map);
//        if(refID.equals(-1)) {
//            Notifications.create().darkStyle().title("Failed")
//                    .text("Please check your connectivity to\n"
//                            + "the server.").showWarning();
//            return false;
//        }
//        System.out.println("Reference Number: " + refID);
//        System.out.println("Your One-Time Password (OTP) is " + OTP_raw);
//        SMSWrapper.send(contactNumber,"Your One-Time Password (OTP) is " + OTP_raw + ". Reference Number: " + refID 
//                + "\n\nSent by Monosync", WordUtils.capitalizeFully(CollegeFaculty.instance().getFirstLastName()), response -> {
//            System.out.println("RESPONSE: " + response);
//            if(response.equalsIgnoreCase("FAILED")) {
//                Mono.fx().thread().wrap(()->{
//                    Notifications.create().darkStyle().title("Failed")
//                            .text("Sending of OTP to the Local Registrar\nfailed.").showError();
//                });
//            } else if(response.equalsIgnoreCase("NO_REPLY")) {
//                Mono.fx().thread().wrap(()->{
//                    Notifications.create().darkStyle().title("No Response")
//                            .text("Please try again later.").showError();
//                });
//            }
//        });
//        
//        AssistantRegistrarOverride asstRegistrar =  M.load(AssistantRegistrarOverride.class);
//        asstRegistrar.onDelayedStart();
//        asstRegistrar.setDetails(contactNumber, refID);
//        boolean authorized = false;
//        try {
//            asstRegistrar.getCurrentStage().showAndWait();
//        } catch (NullPointerException e) {
//            Stage a = asstRegistrar.createChildStage(stage);
//            a.initStyle(StageStyle.UNDECORATED);
//            a.showAndWait();
//            authorized = asstRegistrar.isAuthorized();
//        }
//        System.out.println(authorized);
//        return authorized;
//    }
    
    @Override
    public void onStartUp() {
        MonoClick.addClickEvent(btn_cancel, () -> {
            authorized = false;
            this.getCurrentStage().close();
        });
        this.setTimer();
    }

    @Override
    public void onDelayedStart() {
        MonoClick.addClickEvent(btn_proceed, ()->{
            this.checkIfMatches();
        });
        
        Mono.fx().key(KeyCode.ENTER).release(application_root, ()->{
            this.checkIfMatches();
        });
        
        MonoClick.addClickEvent(lbl_resend, ()->{
            this.resendOTP();
        });
        super.onDelayedStart(); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void checkIfMatches() {
        String enteredOTP = Mono.security().hashFactory().hash_sha512(txt_otp.getText());
        OtpGeneratorMapping otpMap = Database.connect().otp_generator().getPrimary(REF_id);
        if(otpMap != null) {
            if(enteredOTP.equals(otpMap.getCode())) {
                authorized = true;
                Mono.fx().getParentStage(application_root).close();
            } else {
                Mono.fx().alert().createWarning()
                        .setMessage("The entered One-Time Password (OTP) is not matched.")
                        .setHeader("Access Denied").showAndWait();
            }
        } else {
            Notifications.create().darkStyle().title("Failed")
                    .text("Please check your connectivity \nto the server.").showError();
        }
        
    }
    
    private Integer REF_id;
    public void setDetails(String mobileNo, Integer referenceNo) {
        contactNumber = mobileNo;
        REF_id = referenceNo;
        lbl_mobile_number.setText("*******" + mobileNo.substring(9, 13));
        lbl_ref_number.setText(referenceNo + "");
    }
    
    private boolean authorized = false;
    public boolean isAuthorized() {
        return authorized;
    }
    
    private boolean canResend = true;
    private int time = 60;
    private void resendOTP() {
        int res = Mono.fx().alert().createConfirmation()
                .setHeader("Resend OTP")
                .setMessage("Resending OTPs will change the code "
                        + "sent to the receiver. Make sure to notice the "
                        + "reference number. Continue resending action?")
                .confirmYesNo();
        if(res==-1)
            return;
        OtpGeneratorMapping map = new OtpGeneratorMapping();
        String OTP_raw = OTPGenerator.generateOTP();
        map.setActive(1);
        map.setCode(Mono.security().hashFactory().hash_sha512(OTP_raw));
        map.setDate_created(Mono.orm().getServerTime().getDateWithFormat());
        Integer refID = Database.connect().otp_generator().insert(map);
        if(refID.equals(-1)) {
            Notifications.create().darkStyle().title("Failed")
                    .text("Please check your connectivity to\n"
                            + "the server.").showWarning();
            return;
        }
        System.out.println("Reference Number: " + refID);
        System.out.println("Your One-Time Password (OTP) is " + OTP_raw);
        lbl_ref_number.setText(""+refID);
//        String msg = SystemOverriding.getACRONYM(15, transactionRequest_) + " Code:" + OTP_raw + " REQ.BY:" + SystemOverriding.getACRONYM(15, CollegeFaculty.instance().getLAST_NAME(), "name") + " " +  WordUtils.initials(CollegeFaculty.instance().getFIRST_NAME()) + ". Ref.No:" + refID
//                + " <eems-no-reply>";
        String msg = Access.getFORMATTED_MESSAGE(SystemOverriding.getACRONYM(15, transactionRequest_), OTP_raw, CollegeFaculty.instance().getLAST_NAME().replaceAll(" ", "") + " " +  WordUtils.initials(CollegeFaculty.instance().getFIRST_NAME()), refID);
        System.out.println(msg);
        SMSWrapper.send(contactNumber, msg, WordUtils.capitalizeFully(CollegeFaculty.instance().getFirstLastName()), response -> {
        System.out.println("RESPONSE: " + response);
        if(response.equalsIgnoreCase("FAILED")) {
            Mono.fx().thread().wrap(()->{
                Notifications.create().darkStyle().title("Failed")
                        .text("Sending of OTP to the Local Registrar\nfailed.").showError();
            });
            return;
        } else if(response.equalsIgnoreCase("NO_REPLY")) {
            Mono.fx().thread().wrap(()->{
                Notifications.create().darkStyle().title("No Response")
                        .text("Please try again later.").showError();
            });
            return;
        } else {
            Mono.fx().thread().wrap(()->{
                Notifications.create().darkStyle().title("Sending")
                        .text("Processing your request.").showInformation();
            });
            this.setTimer();
            timer.start();
        }
        Mono.fx().thread().wrap(()->{
            this.setDetails(contactNumber, refID);
        });
    });
                    
//        SMSWrapper.send(contactNumber,"Your One-Time Password (OTP) is " + OTP_raw + ". Reference Number: " + refID 
//                + "\n\nSent by Monosync", WordUtils.capitalizeFully(CollegeFaculty.instance().getFirstLastName()), response -> {
//            System.out.println("RESPONSE: " + response);
//            if(response.equalsIgnoreCase("FAILED")) {
//                Mono.fx().thread().wrap(()->{
//                    Notifications.create().darkStyle().title("Failed")
//                            .text("Sending of OTP to the Local Registrar\nfailed.").showError();
//                });
//                return;
//            } else if(response.equalsIgnoreCase("NO_REPLY")) {
//                Mono.fx().thread().wrap(()->{
//                    Notifications.create().darkStyle().title("No Response")
//                            .text("Please try again later.").showError();
//                });
//                return;
//            } else {
//                Mono.fx().thread().wrap(()->{
//                    Notifications.create().darkStyle().title("Sending")
//                            .text("Processing your request.").showInformation();
//                });
//                this.setTimer();
//                timer.start();
//            }
//            Mono.fx().thread().wrap(()->{
//                this.setDetails(contactNumber, refID);
//            });
//        });
    }
    
    private CronThread timer;
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

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
package update3.org.cict.my_account;

import app.lazy.models.AccountFacultyAttemptMapping;
import app.lazy.models.AccountFacultyMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.FacultyMapping;
import app.lazy.models.utils.FacultyUtility;
import artifacts.MonoString;
import com.izum.fx.textinputfilters.StringFilter;
import com.izum.fx.textinputfilters.TextGroup;
import com.izum.fx.textinputfilters.TextInputFilters;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.SimpleTask;
import com.jhmvin.fx.async.Transaction;
import com.jhmvin.fx.async.TransactionException;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import com.jhmvin.transitions.Animate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.cict.PublicConstants;
import org.cict.authentication.authenticator.CollegeComputer;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.cict.reports.ReportsUtility;
import org.cict.reports.result.PrintResult;
import org.controlsfx.control.Notifications;
import org.hibernate.criterion.Order;
import update.org.cict.controller.home.Home;
import update3.org.cict.ChoiceRange;
import update3.org.cict.access.management.OverrideLogs;
import update3.org.cict.layout.default_loader.LoaderView;
import update3.org.cict.window_prompts.empty_prompt.EmptyView;
import update3.org.cict.window_prompts.fail_prompt.FailView;

/**
 *
 * @author Jhon Melvin
 */
public class MyAccountHome extends SceneFX implements ControllerFX {

    @FXML
    private VBox application_root;

    @FXML
    private JFXButton btn_home;

    @FXML
    private JFXButton btn_view_update_profile;

    @FXML
    private JFXButton btn_voew_access_history;

    @FXML
    private JFXButton btn_view_change_password;

    @FXML
    private JFXButton btn_view_change_pin;

    @FXML
    private JFXButton btn_view_change_question;

    @FXML
    private VBox vbox_access;

    @FXML
    private Label lbl_history_ip;

    @FXML
    private Label lbl_history_mac;

    @FXML
    private Label lbl_history_os;

    @FXML
    private Label lbl_history_terminal;

    @FXML
    private Label lbl_history_user;

    @FXML
    private ComboBox<String> cmb_from;

    @FXML
    private ComboBox<String> cmb_to;

    @FXML
    private JFXButton btn_filter;

    @FXML
    private JFXButton btn_print;

    @FXML
    private StackPane stack_history;

    @FXML
    private VBox vbox_access_history;

    @FXML
    private VBox vbox_change_password;

    @FXML
    private PasswordField txt_cp_current;

    @FXML
    private PasswordField txt_cp_new;

    @FXML
    private PasswordField txt_cp_confirm;

    @FXML
    private JFXButton btn_cp_change;

    @FXML
    private VBox vbox_change_pin;

    @FXML
    private PasswordField txt_ct_new;

    @FXML
    private PasswordField txt_ct_confirm;

    @FXML
    private PasswordField txt_ct_password;

    @FXML
    private JFXButton btn_ct_change_pin;

    @FXML
    private VBox vbox_change_question;

    @FXML
    private ComboBox<String> cmb_cs_security_question;

    @FXML
    private PasswordField txt_cs_answer;

    @FXML
    private PasswordField txt_cs_confirm_answer;

    @FXML
    private PasswordField txt_cs_currnet_password;

    @FXML
    private JFXButton btn_cs_change;

    @FXML
    private VBox vbox_update_profile;

    @FXML
    private TextField txt_last;

    @FXML
    private TextField txt_first;

    @FXML
    private TextField txt_middle;

    @FXML
    private TextField txt_mobile;

    @FXML
    private JFXButton btn_update_info;

    @FXML
    private RadioButton rbtn_male;

    @FXML
    private RadioButton rbtn_female;
    
    
    public MyAccountHome() {
        //
    }

    /**
     * Password Minimum Required.
     */
    private final int MIN_PASSWORD_LENGTH = 6;

    /**
     * Final List of Security Questions.
     */
    public final static String[] LIST_SECURITY_QUESTIONS = new String[]{
        "What Is your favorite book?",
        "What is the name of the road you grew up on?",
        "What is your mother’s maiden name?",
        "What was the name of your first/current/favorite pet?",
        "What was the first company that you worked for?",
        "Where did you meet your spouse?",
        "Where did you go to high school/college?",
        "What is your favorite food?",
        "What city were you born in?",
        "Where is your favorite place to vacation?",
        "What was your favorite place to visit as a child?",
        "Who is your favorite actor, musician, or artist?",
        "What is your favorite movie?",};

    /**
     * View Attachments.
     */
    private LoaderView loaderView;
    private FailView failView;
    private EmptyView emptyView;

    public final static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

    @Override
    public void onInitialization() {
        super.bindScene(application_root);
        this.addFilters();
        this.createTextGroups();
        //
        this.loaderView = new LoaderView(stack_history);
        this.failView = new FailView(stack_history);
        this.emptyView = new EmptyView(stack_history);

        //
        ToggleGroup group2 = new ToggleGroup();
        rbtn_male.setToggleGroup(group2);
        rbtn_female.setToggleGroup(group2);
        this.changeView(this.vbox_update_profile);
        this.showProfile();

        /**
         * Load Security Questions.
         */
        this.cmb_cs_security_question.getItems().addAll(LIST_SECURITY_QUESTIONS);
        this.cmb_cs_security_question.setPromptText("Select Security Question For Account Recovery Purposes");

        //---------------------------------
        this.setComboBoxLimit(cmb_from, cmb_to, 0);
        this.setCmbValues();
        this.showAccessHistory();
    }
    // create text group
    private TextGroup tgrp_change_pass;
    private TextGroup tgrp_change_pin;
    private TextGroup tgrp_change_question;

    private void createTextGroups() {
        tgrp_change_pass = new TextGroup(txt_cp_current, txt_cp_new, txt_cp_confirm);
        tgrp_change_pin = new TextGroup(txt_ct_new, txt_ct_confirm, txt_ct_password);
        tgrp_change_question = new TextGroup(txt_cs_answer, txt_cs_confirm_answer,
                txt_cs_currnet_password);
    }

    /**
     * Apply Text Input Filters.
     */
    private void addFilters() {
        // Filter for Change Password
        StringFilter passwordPattern
                = TextInputFilters // caller class
                        .string() // string filter
                        .setFilterMode(StringFilter.LETTER_DIGIT) // mode
                        .setNoLeadingTrailingSpaces(true) // no leading or trailing spaces
                        .setMaxCharacters(50) // max characters allowed
                        .setFilterManager(filterManager -> {
                            if (!filterManager.isValid()) {
                                // if not valid
                                Mono.fx().snackbar().showError(application_root, filterManager.getMessage());
                            }
                        });

        passwordPattern.clone().setTextSource(txt_cp_current).applyFilter();
        passwordPattern.clone().setTextSource(txt_cp_new).applyFilter();
        passwordPattern.clone().setTextSource(txt_cp_confirm).applyFilter();

        // filter for Change Transaction Pin
        // copy the pattern and apply custom changes - Recyclability of settings.
        StringFilter pinPattern = passwordPattern.clone()
                .setFilterMode(StringFilter.DIGIT)
                .setMaxCharacters(6);

        pinPattern.clone().setTextSource(txt_ct_new).applyFilter();
        pinPattern.clone().setTextSource(txt_ct_confirm).applyFilter();
        passwordPattern.clone().setTextSource(txt_ct_password).applyFilter();

        // security question
        StringFilter answerPattern = passwordPattern.clone();
        answerPattern.clone().setTextSource(txt_cs_answer).applyFilter();
        answerPattern.clone().setTextSource(txt_cs_confirm_answer).applyFilter();
        passwordPattern.clone().setTextSource(txt_cs_currnet_password).applyFilter();
    
    
        StringFilter textName = TextInputFilters.string()
                .setFilterMode(StringFilter.LETTER_SPACE)
                .setMaxCharacters(100)
                .setNoLeadingTrailingSpaces(false)
                .setFilterManager(filterManager -> {
                    if (!filterManager.isValid()) {
                        Mono.fx().alert().createWarning().setHeader("Warning")
                                .setMessage(filterManager.getMessage())
                                .show();
                    }
                });

        textName.clone().setTextSource(txt_first).applyFilter();
        textName.clone().setTextSource(txt_last).applyFilter();
        textName.clone().setTextSource(txt_middle).applyFilter();
        
        StringFilter textContact = TextInputFilters.string()
                .setFilterMode(StringFilter.DIGIT)
                .setMaxCharacters(20)
                .setNoLeadingTrailingSpaces(false)
                .setFilterManager(filterManager -> {
                });

        textContact.clone().setTextSource(txt_mobile).applyFilter();
    }

    /**
     * Detaches all prompt views.
     */
    private void detachAll() {
        this.loaderView.detach();
        this.failView.detach();
        this.emptyView.detach();
    }

    private void changeView(Node whatView) {
        this.detachAll();
        Animate.fade(whatView, 150, () -> {
            // what to do after the fade animation
            vbox_access.setVisible(false);
            vbox_change_password.setVisible(false);
            vbox_change_pin.setVisible(false);
            vbox_change_question.setVisible(false);
            vbox_update_profile.setVisible(false);
            whatView.setVisible(true);
        }, vbox_change_password, vbox_access, vbox_change_pin, vbox_change_question, vbox_update_profile);
    }

    @Override
    public void onEventHandling() {
        super.addClickEvent(btn_home, () -> {
            Home.callHome(this);
            this.detachAll();
        });

        super.addClickEvent(btn_view_update_profile, ()->{
            this.showProfile();
            this.changeView(this.vbox_update_profile);
        });
        super.addClickEvent(btn_voew_access_history, () -> {
            this.changeView(this.vbox_access);
        });
        super.addClickEvent(btn_view_change_password, () -> {
            this.changeView(this.vbox_change_password);
        });
        super.addClickEvent(btn_view_change_pin, () -> {
            this.changeView(this.vbox_change_pin);
        });
        super.addClickEvent(btn_view_change_question, () -> {
            this.changeView(this.vbox_change_question);
        });

        //----------------------------------------------------------------------
        super.addClickEvent(btn_cp_change, () -> {
            changePassword();
        });

        super.addClickEvent(btn_ct_change_pin, () -> {
            changePin();
        });

        super.addClickEvent(btn_cs_change, () -> {
            changeSecurity();
        });
        
        super.addClickEvent(btn_print, ()->{
            this.printResult();
        });
        
        super.addClickEvent(btn_filter, ()->{
            this.filterResult();
        });
        
        cmb_from.valueProperty().addListener((a)->{
            cmbChanged = true;
            btn_filter.setDisable(false);
        });
        cmb_to.valueProperty().addListener((a)->{
            cmbChanged = true;
            btn_filter.setDisable(false);
        });
        
        super.addClickEvent(btn_update_info, ()->{
            this.updateProfile();
        });
    }

    //----------------------------------------------
    //--------------------------- 
    private SimpleDateFormat formatter_sql = new SimpleDateFormat(PublicConstants.SQL_DATETIME_FORMAT);
    private SimpleDateFormat formatter_display = new SimpleDateFormat("MMMM dd, yyyy");
    private SimpleDateFormat formatter_plain = new SimpleDateFormat("yyyy-MM-dd");
    
    private ArrayList<String> dateList = new ArrayList<>();
    private void setComboBoxLimit(ComboBox<String> source, ComboBox<String> self, int extra, ComboBox<String>... padding) {
        ChoiceRange.setComboBoxLimit(dateList, source, self, 0, padding);
    }
    
    private ArrayList<HashMap<String,Date>> dateStorage = new ArrayList<>();
    private void setCmbValues(){
        List<Date> dates = new ArrayList<Date>();
        try {
            AccountFacultyAttemptMapping start = Mono.orm()
                    .newSearch(Database.connect().account_faculty_attempt())
                    .eq(DB.account_faculty_attempt().account_id, CollegeFaculty.instance().getACCOUNT_ID())
                    .active(Order.asc(DB.account_faculty_attempt().try_id))
                    .first();
            AccountFacultyAttemptMapping end = Mono.orm()
                    .newSearch(Database.connect().account_faculty_attempt())
                    .eq(DB.account_faculty_attempt().account_id, CollegeFaculty.instance().getACCOUNT_ID())
                    .active(Order.desc(DB.account_faculty_attempt().try_id))
                    .first();  
            Date endDate = formatter_plain.parse(end.getTime().toString());//DateUtils.addDays(formatter_plain.parse(end.getTime().toString()), 1);
            Date startDate = formatter_plain.parse(start.getTime().toString());
        
            long interval = 24*1000 * 60 * 60; // 1 hour in millis
            long endTime = endDate.getTime(); // create your endtime here, possibly using Calendar or Date
            long curTime = startDate.getTime();
            while (curTime <= endTime) {
                dates.add(new Date(curTime));
                curTime += interval;
            }
            for(int i=0;i<dates.size();i++){
                Date lDate = dates.get(i);
                String ds = formatter_display.format(lDate); 
                dateList.add(ds);
                HashMap<String,Date> nameDate = new HashMap<>();
                nameDate.put(ds, dates.get(i));
                System.out.println("'"+ds + "' - " + nameDate.get(ds));
                dateStorage.add(nameDate);
            }
            cmb_from.getItems().addAll(dateList); 
            cmb_to.getItems().addAll(dateList);
            cmb_from.getSelectionModel().selectFirst();
            cmb_to.getSelectionModel().selectLast();
            cmbChanged = true;
        } catch (ParseException ex) {
            ex.printStackTrace();
            Logger.getLogger(OverrideLogs.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void filterResult(){
        cmbChanged = false;
        String from_str = cmb_from.getSelectionModel().getSelectedItem();
        String to_str =cmb_to.getSelectionModel().getSelectedItem();
        vbox_access_history.getChildren().clear();
        Date from = null, to = null;
        try {  
            int count = 0;
            for(int i=0; i<dateList.size(); i++) {
                String each = dateList.get(i);
                if(from_str.equalsIgnoreCase(each)) {
                    from = dateStorage.get(i).get(from_str);
                    count++;
                }
                if(to_str.equalsIgnoreCase(each)) {
                    to = dateStorage.get(i).get(to_str);
                    to = DateUtils.addDays(to,1);
                    count++;
                }
                if(count==2) {
                    break;
                }
            }
        } catch (Exception e) {
        }
        
        if(from==null || to==null) {
            return;
        }
        btn_filter.setDisable(true);
        this.fetchAccessHistory(from, to);
    }
    
    private boolean cmbChanged = true;
    private void printResult() {
        if(cmbChanged) {
            int res =  Mono.fx().alert()
                    .createConfirmation().setHeader("Not Yet Filtered")
                    .setMessage("The result to be printed are not yet filtered with the given dates above. Do you still want to print?")
                    .confirmYesNo();
            if(res==-1)
                return;
        }
        String[] colNames = new String[]{"Date and Time","PC Name","PC Username", "OS Version", "IP Address", "Result"};
        ArrayList<String[]> rowData = new ArrayList<>();
        if(preview==null) {
            Notifications.create()
                    .title("No Access History Found")
                    .text("No data to print.")
                    .showWarning();
            return;
        }
        AccountFacultyAttemptMapping ref = null;
        for (int i = 0; i < preview.size(); i++) {
            AccountFacultyAttemptMapping result = preview.get(i);
            ref = result;
            String[] row = new String[]{(i+1)+".  "+ ReportsUtility.formatter2.format(result.getTime()),
                WordUtils.capitalizeFully(result.getPc_name()), 
                (result.getPc_username()), 
                (result.getOs_version()),
                result.getIp_address(),
                WordUtils.capitalizeFully(result.getResult().replace("_", " "))};
            rowData.add(row);
        }
        PrintResult print = new PrintResult();
        print.setDocumentFormat(ReportsUtility.paperSizeChooser(this.getStage()));
        print.columnNames = colNames;
        print.ROW_DETAILS = rowData;
        String username = CollegeFaculty.instance().getUSERNAME();
        print.fileName = "log_attempts_" + username.toLowerCase();
        String fr = cmb_from.getSelectionModel().getSelectedItem();
        String to = cmb_to.getSelectionModel().getSelectedItem();
        SimpleDateFormat month = new SimpleDateFormat("MMMMM");
        print.reportTitleIntro = CollegeFaculty.instance().getBULSU_ID().toUpperCase() + " | " + WordUtils.capitalizeFully(CollegeFaculty.instance().getFirstLastName());
        if(cmbChanged) {
            print.reportTitleIntro = null;
            print.reportOtherDetail = print.reportTitleIntro;//CollegeFaculty.instance().getBULSU_ID().toUpperCase() + " | " + WordUtils.capitalizeFully(CollegeFaculty.instance().getFirstLastName());
        } else if(fr==null || to==null || ref==null || ref.getTime()==null) {
            print.reportOtherDetail = "As of " + formatter_display.format(Mono.orm().getServerTime().getDateWithFormat());
        } else if(fr.equalsIgnoreCase(to)) {
            print.reportOtherDetail = formatter_display.format(ref.getTime());
         } else
            print.reportOtherDetail = "From " + fr + " to " + to;
        
        print.reportTitleHeader = "Account Log And Attempts";
        print.whenStarted(() -> {
            btn_print.setDisable(true);
            btn_filter.setDisable(true);
            super.cursorWait();
        });
        print.whenCancelled(() -> {
            Notifications.create()
                    .title("Request Cancelled")
                    .text("Sorry for the inconviniece.")
                    .showWarning();
        });
        print.whenFailed(() -> {
            Notifications.create()
                    .title("Request Failed")
                    .text("Something went wrong. Sorry for the inconviniece.")
                    .showInformation();
        });
        print.whenSuccess(() -> {
            Notifications.create()
                    .title("Printing Results")
                    .text("Please wait a moment.")
                    .showInformation();
        });
        print.whenFinished(() -> {
            btn_print.setDisable(false);
            btn_filter.setDisable(false);
            super.cursorDefault();
        });
        //----------------------------------------------------------------------
        print.transact();
    }
    
    //------------------------------------------------------
    
    
    /**
     * Change Security Question and answer.
     */
    private void changeSecurity() {
        String selectedQuestion = cmb_cs_security_question.getSelectionModel().getSelectedItem();
        if (selectedQuestion == null) {
            Mono.fx().snackbar().showError(application_root, "Select Security Question");
            return;
        }

        String answer = txt_cs_answer.getText();
        String confirmAnswer = txt_cs_confirm_answer.getText();
        String password = txt_cs_currnet_password.getText();

        if (tgrp_change_question.hasEmptyFieldsTrimmed()) {
            Mono.fx().snackbar().showError(application_root, "Some Fields Are Empty");
            return;
        }

        if (!answer.equals(confirmAnswer)) {
            Mono.fx().snackbar().showError(application_root, "Security Answer does not match");
            return;
        }

        if (answer.equals(password)) {
            Mono.fx().snackbar().showError(application_root, "Security Answer and password must not be the same.");
            return;
        }

        ChangeSecurityQuestion changeQuestionTx = new ChangeSecurityQuestion();
        changeQuestionTx.setCurrentPassword(password);
        changeQuestionTx.setSecurityQuestion(selectedQuestion);
        changeQuestionTx.setSecurityAnswer(confirmAnswer);

        changeQuestionTx.whenStarted(() -> {
            super.cursorWait();
            btn_cs_change.setDisable(true);
        });
        changeQuestionTx.whenFailed(() -> {
            // cannot update
            Mono.fx().snackbar().showError(application_root, "Connection problem encountered.");
        });
        changeQuestionTx.whenCancelled(() -> {
            // wrong old password
            Mono.fx().snackbar().showError(application_root, "Authentication Failed! Invalid current password.");
        });
        changeQuestionTx.whenSuccess(() -> {
            // successfully updated
            Mono.fx().snackbar().showSuccess(application_root, "Successfully Updated");
            tgrp_change_question.clearGroup();
        });
        changeQuestionTx.whenFinished(() -> {
            super.cursorDefault();
            btn_cs_change.setDisable(false);
        });

        changeQuestionTx.transact();

    }

    /**
     * Change User Password.
     */
    private void changePassword() {
        String old = txt_cp_current.getText();
        String update = txt_cp_new.getText();
        String confirm = txt_cp_confirm.getText();

        if (tgrp_change_pass.hasEmptyFieldsTrimmed()) {
            Mono.fx().snackbar().showError(application_root, "Some Fields Are Empty");
            return;
        }

        if (!update.equals(confirm)) {
            Mono.fx().snackbar().showError(application_root, "Password Mismatch");
            return;
        }

        // check for min characters
        try {
            tgrp_change_pass.getMembers().forEach(member -> {
                if (member.getText().trim().length() < MIN_PASSWORD_LENGTH) {
                    Mono.fx().snackbar().showError(application_root, member.getAccessibleHelp() + " is below minimum characters.");
                    throw new RuntimeException();
                }
            });
        } catch (RuntimeException e) {
            return;
        }

        if (old.equals(confirm)) {
            Mono.fx().snackbar().showError(application_root, "Current password and new password are the same.");
            return;
        }

        ChangePassword changePassTx = new ChangePassword();
        changePassTx.setOldPassword(old);
        changePassTx.setNewPassword(update);

        changePassTx.whenStarted(() -> {
            super.cursorWait();
            btn_cp_change.setDisable(true);
        });
        changePassTx.whenFailed(() -> {
            // cannot update
            Mono.fx().snackbar().showError(application_root, "Connection problem encountered.");
        });
        changePassTx.whenCancelled(() -> {
            // wrong old password
            Mono.fx().snackbar().showError(application_root, "Authentication Failed! Invalid current password.");
        });
        changePassTx.whenSuccess(() -> {
            // successfully updated
            Mono.fx().snackbar().showSuccess(application_root, "Successfully Updated");
            tgrp_change_pass.clearGroup();
        });
        changePassTx.whenFinished(() -> {
            super.cursorDefault();
            btn_cp_change.setDisable(false);
        });

        changePassTx.transact();

    }

    private void changePin() {
        String newPin = txt_ct_new.getText();
        String confirmPin = txt_ct_confirm.getText();
        String password = txt_ct_password.getText();

        if (tgrp_change_pin.hasEmptyFieldsTrimmed()) {
            Mono.fx().snackbar().showError(application_root, "Some Fields Are Empty");
            return;
        }

        if (newPin.length() != 6) {
            Mono.fx().snackbar().showError(application_root, "PIN must be exactly six (6) digits.");
            return;
        }

        if (!newPin.equals(confirmPin)) {
            Mono.fx().snackbar().showError(application_root, "PIN Mismatch");
            return;
        }

        if (newPin.equals(password)) {
            Mono.fx().snackbar().showError(application_root, "PIN must not be the same as password.");
            return;
        }

        ChangeTransactionPin changePinTx = new ChangeTransactionPin();
        changePinTx.setNewPin(newPin);
        changePinTx.setOldPassword(password);

        changePinTx.whenStarted(() -> {
            super.cursorWait();
            btn_ct_change_pin.setDisable(true);
        });
        changePinTx.whenFailed(() -> {
            // cannot update
            Mono.fx().snackbar().showError(application_root, "Connection problem encountered.");
        });
        changePinTx.whenCancelled(() -> {
            // wrong old password
            Mono.fx().snackbar().showError(application_root, "Authentication Failed! Invalid current password.");
        });
        changePinTx.whenSuccess(() -> {
            // successfully updated
            Mono.fx().snackbar().showSuccess(application_root, "Successfully Updated");
            tgrp_change_pin.clearGroup();
        });
        changePinTx.whenFinished(() -> {
            super.cursorDefault();
            btn_ct_change_pin.setDisable(false);
        });

        changePinTx.transact();
    }

    //--------------------------------------------------------------------------
    private void showAccessHistory() {
        displayCurrentSession();
    }

    private void displayCurrentSession() {
        CollegeComputer cc = CollegeComputer.instance();

        String displayIP = "";
        String displayMAC = "";
        try {
            boolean firstOnly = true;
            String[] ipSet = cc.getIP_ADDRESS().split("%");
            for (String ipString : ipSet) {
                if (ipString.isEmpty()) {
                    continue;
                }

                if (!firstOnly) {
                    break;
                }
                String[] ipInfo = ipString.split("@");

                String ipAddr = ipInfo[0];
                String macAddr = ipInfo[1];

                displayIP += (ipAddr + "  ");
                displayMAC += (macAddr + "  ");

                firstOnly = false; // only the first set of ip
            }
        } catch (Exception e) {
            displayIP = "Unknown Host";
            displayMAC = "Unknown Host";
        }

        lbl_history_ip.setText(displayIP);
        lbl_history_mac.setText(displayMAC);
        lbl_history_os.setText(cc.getOS());
        lbl_history_terminal.setText(cc.getPC_NAME());
        lbl_history_user.setText(cc.getPC_USERNAME());

        this.fetchAccessHistory(null, null);

        //--
    }
    
    private void fetchAccessHistory(Date FROM, Date TO) {
        FetchAccessHistory accessTx = new FetchAccessHistory();
        accessTx.setFiltereDates(FROM, TO);
        accessTx.whenStarted(() -> {
            this.btn_print.setDisable(true);
            this.vbox_access.setVisible(true);
            this.detachAll();
            this.loaderView.setMessage("Loading History");
            this.loaderView.attach();
            
            this.btn_view_change_password.setDisable(true);
            this.btn_view_change_pin.setDisable(true);
            this.btn_view_change_question.setDisable(true);
            this.btn_view_update_profile.setDisable(true);
            this.btn_voew_access_history.setDisable(true);
            this.btn_update_info.setDisable(true);
            this.cmb_from.setDisable(true);
            this.cmb_to.setDisable(true);
        });
        accessTx.whenFailed(() -> {
            this.vbox_access_history.getChildren().clear();
            this.failView.setMessage("Failed to Load History");
            this.failView.attach();

        });
        accessTx.whenCancelled(() -> {
            this.vbox_access_history.getChildren().clear();
            System.out.println(this.vbox_access_history.isVisible());
            this.emptyView.setMessage("No History");
            this.emptyView.getButton().setVisible(false);
            this.emptyView.attach();
        });
        accessTx.whenSuccess(() -> {
            renderHistory(accessTx);
            this.btn_print.setDisable(false);
            
            this.btn_view_change_password.setDisable(false);
            this.btn_view_change_pin.setDisable(false);
            this.btn_view_change_question.setDisable(false);
            this.btn_view_update_profile.setDisable(false);
            this.btn_voew_access_history.setDisable(false);
            this.btn_update_info.setDisable(false);
            
            this.cmb_from.setDisable(false);
            this.cmb_to.setDisable(false);
        });
        accessTx.whenFinished(() -> {
            this.loaderView.detach();
        });

        accessTx.transact();
    }

    /**
     * Reduces Lag Renders the FXML outside the main thread.
     *
     * @param accessTx
     */
    private ArrayList<AccountFacultyAttemptMapping> preview;
    private void renderHistory(FetchAccessHistory accessTx) {
        SimpleTask renderTask = new SimpleTask("render-history");
        renderTask.setTask(() -> {
            preview = accessTx.getAttempts();
            displayAccessTable(preview);
        });
        renderTask.whenStarted(() -> {
            this.loaderView.setMessage("Preparing History");
            this.loaderView.attach();
        });
        renderTask.whenFailed(() -> {
        });
        renderTask.whenCancelled(() -> {
        });
        renderTask.whenSuccess(() -> {
//            displayAccessTable(accessTx.getAttempts());
        });
        renderTask.whenFinished(() -> {
            this.loaderView.detach();
        });

        renderTask.start();
    }

    /**
     * Display access history row.
     *
     * @param attempts
     */
    private void displayAccessTable(ArrayList<AccountFacultyAttemptMapping> attempts) {
        /**
         * Create Table
         */
        SimpleTable tblSections = new SimpleTable();

        for (AccountFacultyAttemptMapping attempt : attempts) {
            SimpleTableRow row = new SimpleTableRow();
            row.setRowHeight(50.0);
            /**
             * Create FXML ROW.
             */
            RowAccessHistory accessRow = new RowAccessHistory();

            accessRow.lbl_time.setText(dateFormat.format(attempt.getTime()));
            accessRow.lbl_state.setText(attempt.getResult().replace("_", " "));

            //
            String displayIP = "";
            String displayMAC = "";
            try {
                boolean firstOnly = true;
                String[] ipSet = attempt.getIp_address().split("%");
                for (String ipString : ipSet) {
                    if (ipString.isEmpty()) {
                        continue;
                    }

                    if (!firstOnly) {
                        break;
                    }
                    String[] ipInfo = ipString.split("@");

                    String ipAddr = ipInfo[0];
                    String macAddr = ipInfo[1];

                    displayIP += (ipAddr + "");
                    displayMAC += (macAddr + "");

                    firstOnly = false; // only the first set of ip
                }
            } catch (Exception e) {
                displayIP = "Unknown Host";
                displayMAC = "Unknown Host";
            }

            //
            accessRow.lbl_ip.setText(displayIP + "@" + displayMAC);
            accessRow.lbl_os.setText(attempt.getOs_version());
            accessRow.lbl_terminal.setText(attempt.getPc_username() + "@" + attempt.getPc_name());
            //
            SimpleTableCell cellParent = new SimpleTableCell();
            cellParent.setResizePriority(Priority.ALWAYS);
            cellParent.setContentAsPane(accessRow.rowHistory);

            row.addCell(cellParent);
            tblSections.addRow(row);
        }

        //
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(tblSections);
        simpleTableView.setFixedWidth(true);
        Mono.fx().thread().wrap(() -> {
            simpleTableView.setParentOnScene(vbox_access_history);
        });

    }

    /**
     * Class that controls the access row.
     */
    private class RowAccessHistory extends SceneFX {

        public HBox rowHistory;
        public Label lbl_time, lbl_ip, lbl_os, lbl_terminal, lbl_state;

        public RowAccessHistory() {

            rowHistory = (HBox) Mono.fx().create()
                    .setPackageName("update3.org.cict.my_account")
                    .setFxmlDocument("row-access")
                    .makeFX()
                    .pullOutLayout();

            lbl_state = super.searchAccessibilityText(rowHistory, "lbl_state");
            this.lbl_time = super.searchAccessibilityText(rowHistory, "lbl_time");
            this.lbl_ip = super.searchAccessibilityText(rowHistory, "lbl_ip");
            this.lbl_os = super.searchAccessibilityText(rowHistory, "lbl_os");
            this.lbl_terminal = super.searchAccessibilityText(rowHistory, "lbl_terminal");
        }
    }

    /**
     * Class that fetch the access history.
     */
    private class FetchAccessHistory extends Transaction {

        private ArrayList<AccountFacultyAttemptMapping> attempts;

        //---------------------
        private Date from = null, to=null;
        public void setFiltereDates(Date FROM, Date TO) {
            from = FROM;
            to = TO;
        }
        
        public ArrayList<AccountFacultyAttemptMapping> getAttempts() {
            return attempts;
        }

        @Override
        protected boolean transaction() {
            Integer logged_account = CollegeFaculty.instance().getACCOUNT_ID();

            if(from != null && to != null) {
                attempts = Mono.orm().newSearch(Database.connect().account_faculty_attempt())
        //                .between(DB.system_override_logs().executed_date, from, to)
                        .gte(DB.account_faculty_attempt().time, from)
                        .lte(DB.account_faculty_attempt().time, to)
                        .eq(DB.account_faculty_attempt().account_id, logged_account)
                        .active(Order.asc(DB.account_faculty_attempt().time)).all();
                if(attempts==null) {
                    return false;
                }
                return true;
            }
            
            attempts = Mono.orm()
                    .newSearch(Database.connect().account_faculty_attempt())
                    .eq(DB.account_faculty_attempt().account_id, logged_account)
                    .active(Order.desc(DB.account_faculty_attempt().try_id))
                    .take(50);

            if (attempts == null) {
                return false; // no history
            }

            return true;
        }

        @Override
        protected void after() {

        }

    }

    /**
     * This transaction changes the password of the user.
     */
    private class ChangePassword extends Transaction {

        private String oldPassword;
        private String newPassword;

        public void setOldPassword(String oldPassword) {
            this.oldPassword = oldPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }

        @Override
        protected boolean transaction() {
            Integer account_id = CollegeFaculty.instance().getACCOUNT_ID();

            AccountFacultyMapping account = Database
                    .connect()
                    .account_faculty()
                    .getPrimary(account_id);

            // if not equal
            if (!Mono.security().hashFactory().hash_sha512(oldPassword).equals(account.getPassword())) {
                return false;
            }

            // update password
            account.setPassword(Mono.security().hashFactory().hash_sha512(newPassword));

            boolean passwordUpdated = Database.connect().account_faculty().update(account);
            // if failed to update
            if (!passwordUpdated) {
                throw new TransactionException("update-failed");
            }

            return true;
        }

        @Override
        protected void after() {

        }
    }

    /**
     * Change Pin.
     */
    private class ChangeTransactionPin extends Transaction {

        private String oldPassword;
        private String newPin;

        public void setOldPassword(String oldPassword) {
            this.oldPassword = oldPassword;
        }

        public void setNewPin(String newPin) {
            this.newPin = newPin;
        }

        @Override
        protected boolean transaction() {
            Integer account_id = CollegeFaculty.instance().getACCOUNT_ID();

            AccountFacultyMapping account = Database
                    .connect()
                    .account_faculty()
                    .getPrimary(account_id);

            // if not equal
            if (!Mono.security().hashFactory().hash_sha512(oldPassword).equals(account.getPassword())) {
                return false;
            }

            // update pin
            account.setTransaction_pin(Mono.security().hashFactory().hash_sha512(newPin));

            boolean pinUpdated = Database.connect().account_faculty().update(account);
            // if failed to update
            if (!pinUpdated) {
                throw new TransactionException("update-failed");
            }

            return true;
        }

        @Override
        protected void after() {

        }
    }

    /**
     * Update Security Question.
     */
    private class ChangeSecurityQuestion extends Transaction {

        private String currentPassword;
        private String securityQuestion;
        private String securityAnswer;

        public void setCurrentPassword(String currentPassword) {
            this.currentPassword = currentPassword;
        }

        public void setSecurityQuestion(String securityQuestion) {
            this.securityQuestion = securityQuestion;
        }

        public void setSecurityAnswer(String securityAnswer) {
            this.securityAnswer = securityAnswer;
        }

        @Override
        protected boolean transaction() {
            Integer account_id = CollegeFaculty.instance().getACCOUNT_ID();

            AccountFacultyMapping account = Database
                    .connect()
                    .account_faculty()
                    .getPrimary(account_id);

            // if not equal
            if (!Mono.security().hashFactory().hash_sha512(currentPassword).equals(account.getPassword())) {
                return false;
            }

            // update password
            account.setRecovery_question(securityQuestion);
            account.setRecovery_answer(Mono.security().hashFactory().hash_sha512(securityAnswer));

            boolean questionUpdated = Database.connect().account_faculty().update(account);
            // if failed to update
            if (!questionUpdated) {
                throw new TransactionException("update-failed");
            }

            return true;
        }

        @Override
        protected void after() {

        }
    }
    
    private FacultyMapping faculty;
    private void showProfile() {
        faculty = FacultyUtility.getFaculty(CollegeFaculty.instance().getFACULTY_ID());
        txt_first.setText(faculty.getFirst_name());
        txt_last.setText(faculty.getLast_name());
        txt_middle.setText(faculty.getMiddle_name()==null? "" : faculty.getMiddle_name());
        txt_mobile.setText(faculty.getMobile_number()==null? "" : "09"+faculty.getMobile_number().substring(4, 13));
        
        if ((faculty.getGender()==null? "" : faculty.getGender()).equalsIgnoreCase("MALE")) {
            rbtn_male.setSelected(true);
        } else if ((faculty.getGender()==null? "" : faculty.getGender()).equalsIgnoreCase("FEMALE")) {
            rbtn_female.setSelected(true);
        } else {
            // if none was set in the database
            rbtn_male.setSelected(true);
        }
    }
    
    private void updateProfile() {
        if(txt_last.getText()==null || txt_last.getText().isEmpty()) {
            showWarning("Last Name", "Last name cannot be empty.");
            return;
        } 
        if(txt_first.getText()==null || txt_first.getText().isEmpty()) {
            showWarning("First Name", "First name cannot be empty.");
            return;
        } 
        String contactNumber = txt_mobile.getText();
        if(contactNumber==null || contactNumber.isEmpty()) {
            showWarning("Invalid Contact Number", "Contact number must have a value.");
            return;
        } else if(contactNumber.length()<11 || !contactNumber.substring(0, 2).equalsIgnoreCase("09")) {
            showWarning("Invalid Contact Number", "Must start with \"09\" with a length\n"
                    + "of 11 digits.");
            return;
        } else if(contactNumber.length() != 11) {
            showWarning("Invalid Contact Number", "Must have a length of 11 digits\n"
                    + "and starts with \"09\"");
            return;
        }
        String gender = rbtn_female.isSelected()? "FEMALE" : "MALE";
        UpdateProfile updateProfile = new UpdateProfile();
        updateProfile.setFirstName(MonoString.removeExtraSpace(txt_first.getText()).toUpperCase());
        updateProfile.setLastName(MonoString.removeExtraSpace(txt_last.getText()).toUpperCase());
        updateProfile.setMiddleName(txt_middle.getText()==null? "" : MonoString.removeExtraSpace(txt_middle.getText()).toUpperCase());
        updateProfile.setMobileNumber("+639"+contactNumber.substring(2, 11));
        updateProfile.setUser(faculty);
        updateProfile.setGender(gender);
        updateProfile.whenStarted(() -> {
            super.cursorWait();
            btn_ct_change_pin.setDisable(true);
        });
        updateProfile.whenFailed(() -> {
            // cannot update
            Mono.fx().snackbar().showError(application_root, "Connection problem encountered.");
        });
        updateProfile.whenCancelled(() -> {
            Mono.fx().snackbar().showError(application_root, "Authentication Failed!");
        });
        updateProfile.whenSuccess(() -> {
            // successfully updated
            Mono.fx().snackbar().showSuccess(application_root, "Successfully Updated");
        });
        updateProfile.whenFinished(() -> {
            super.cursorDefault();
        });

        updateProfile.transact();
    }

    private void showWarning(String title, String text) {
        Notifications.create().darkStyle()
                .title(title)
                .text(text)
                .showWarning();
    }
    
    private class UpdateProfile extends Transaction {
        private FacultyMapping user;
        private String lastName;
        private String firstName;
        private String middleName;
        private String mobileNumber;
        private String gender;

        public void setGender(String gender) {
            this.gender = gender;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        public void setUser(FacultyMapping user) {
            this.user = user;
        }
        
        
        @Override
        protected boolean transaction() {
            user.setFirst_name(firstName);
            user.setLast_name(lastName);
            user.setMiddle_name(middleName);
            user.setMobile_number(mobileNumber);
            user.setGender(gender);
            boolean result = Database.connect().faculty().update(user);
            if(result) {
                CollegeFaculty.instance().setMIDDLE_NAME(middleName);
                CollegeFaculty.instance().setLAST_NAME(lastName);
                CollegeFaculty.instance().setFIRST_NAME(firstName);
                CollegeFaculty.instance().setGENDER(gender);
                return true;
            } else
                return false;
        }
        
    }
}

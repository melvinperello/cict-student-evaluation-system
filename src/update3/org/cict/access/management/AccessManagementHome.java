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
 * JOEMAR N. DEvLA CRUZ
 * GRETHEL EINSTEIN BERNARDINO
 *
 * OTHER LIBRARIES THAT ARE USED BELONGS TO THEIR RESPECTFUL OWNERS AND AUTHORS.
 * NO COPYRIGHT ARE INTENTIONAL OR INTENDED.
 * THIS PROJECT IS NOT PROFITABLE HENCE FOR EDUCATIONAL PURPOSES ONLY.
 * THIS PROJECT IS ONLY FOR COMPLIANCE TO OUR REQUIREMENTS.
 * THIS PROJECT DOES NOT INCLUDE DISTRIBUTION FOR OTHER PURPOSES.
 *
 */
package update3.org.cict.access.management;

import app.lazy.models.AccountFacultyMapping;
import app.lazy.models.BackupScheduleMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.FacultyMapping;
import app.lazy.models.LinkedSettingsMapping;
import app.lazy.models.utils.FacultyUtility;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import com.jhmvin.transitions.Animate;
import com.melvin.mono.fx.bootstrap.M;
import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.bsu.cict.alerts.MessageBox;
import org.bsu.cict.tools.BackUpAndRestore;
import org.cict.GenericLoadingShow;
import org.cict.MainApplication;
import org.cict.PublicConstants;
import org.cict.ThreadMill;
import org.cict.accountmanager.AccountManager;
import org.cict.accountmanager.Logout;
import org.cict.accountmanager.faculty.FacultyInformation;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.controlsfx.control.Notifications;
import org.hibernate.criterion.Order;
import sys.org.cict.layout.home.system_variables.SystemValues;
import update.org.cict.controller.home.Home;
import update3.org.cict.access.Access;
import update3.org.facultychooser.FacultyChooser;

/**
 *
 * @author Jhon Melvin
 */
public class AccessManagementHome extends SceneFX implements ControllerFX {

    @FXML
    private VBox application_root;

    @FXML
    private JFXButton btn_home;

    @FXML
    private JFXButton btn_view_system;

    @FXML
    private JFXButton btn_view_admin;

    @FXML
    private JFXButton btn_view_assistant_admin;

    @FXML
    private JFXButton btn_view_local_registrar;

    @FXML
    private JFXButton btn_view_co_registrar;

    @FXML
    private JFXButton btn_view_evaluator;

    @FXML
    private VBox vbox_system;

    @FXML
    private JFXButton btn_create_system_admin;

    @FXML
    private JFXButton btn_reset_system_admin_pass;

    @FXML
    private JFXButton btn_reclaim_system_admin;

    @FXML
    private VBox vbox_admin;

    @FXML
    private JFXButton btn_assign_new_sys_admin;

    @FXML
    private JFXButton btn_add_new_asst_admin;

    @FXML
    private JFXButton btn_assign_local_registrar;

    @FXML
    private VBox vbox_assistant_admin;

    @FXML
    private JFXButton btn_add_new_asst_admin1;

    @FXML
    private VBox vbox_asst_admin_table;

    @FXML
    private VBox vbox_asst_admin_no_found;

    @FXML
    private VBox vbox_local_registrar;

    @FXML
    private JFXButton btn_add_asst_registrar;

    @FXML
    private JFXButton btn_add_asst_registrar1;

    @FXML
    private JFXButton btn_add_evaluators;

    @FXML
    private JFXButton btn_add_evaluators1;

    @FXML
    private VBox vbox_co_registrar;

    @FXML
    private VBox vbox_asst_registrar_table;

    @FXML
    private VBox vbox_asst_registrar_no_found;

    @FXML
    private VBox vbox_evaluators;

    @FXML
    private VBox vbox_evaluator_table;

    @FXML
    private VBox vbox_evaluator_no_found;

    @FXML
    private JFXButton btn_system_values;

    @FXML
    private JFXButton btn_show_override_logs;

    @FXML
    private JFXButton btn_change_cluster_admin;

    @FXML
    private JFXButton btn_change_cluster_local_reg;

    @FXML
    private Label lbl_cluster_local_reg;

    @FXML
    private Label lbl_cluster_admin;

    //-----------------------
    @FXML
    private JFXButton btn_view_backup_restore;

    @FXML
    private JFXButton btn_back_up;

    @FXML
    private JFXButton btn_restore;
    
    @FXML
    private VBox vbox_backup_restore;
    
    @FXML
    private CheckBox chkbx_enable_autobackup;
    
    @FXML
    private VBox vbox_autobackup_feature;
    
    @FXML
    private ComboBox<String> cmb_autobackup_hr;
    
    @FXML
    private ComboBox<String> cmb_autobackup_min;
    
    @FXML
    private ComboBox<String> cmb_autobackup_a;
    
    @FXML
    private JFXButton btn_apply_autobackup;
    
    @FXML
    private HBox hbox_autobackup_info_user;
    
    @FXML
    private Label lbl_time_auto;
    
    @FXML
    private Label lbl_create_by_auto;
    
    @FXML
    private Label lbl_created_date_auto;
    


    public AccessManagementHome() {
        //
    }

    private LinkedSettingsMapping currentLinkedSettings;
    private AccountFacultyMapping currentAccount;

    @Override
    public void onInitialization() {
        super.bindScene(application_root);

        this.changeView(vbox_system);

        currentLinkedSettings = Mono.orm().newSearch(Database.connect().linked_settings())
                .eq(DB.linked_settings().mark, "ALIVE").active(Order.desc(DB.linked_settings().id)).first();

        AccountFacultyMapping admin = Mono.orm().newSearch(Database.connect().account_faculty())
                .eq(DB.account_faculty().access_level, Access.ACCESS_ADMIN)
                .active(Order.asc(DB.account_faculty().id)).first();
        if (admin != null) {
            lbl_cluster_admin.setText(admin.getAssigned_cluster() == null ? "No Cluster Assigned" : (admin.getAssigned_cluster().equals(3) ? "C1: " + currentLinkedSettings.getFloor_3_name() : "C2: " + currentLinkedSettings.getFloor_4_name()));
        } else {
            lbl_cluster_admin.setText("NO ADMIN FOUND");
        }

        AccountFacultyMapping localRegistrar = Mono.orm().newSearch(Database.connect().account_faculty())
                .eq(DB.account_faculty().access_level, Access.ACCESS_LOCAL_REGISTRAR)
                .active(Order.asc(DB.account_faculty().id)).first();
        if (localRegistrar != null) {
            lbl_cluster_local_reg.setText(localRegistrar.getAssigned_cluster() == null ? "No Cluster Assigned" : (localRegistrar.getAssigned_cluster().equals(3) ? "C1: " + currentLinkedSettings.getFloor_3_name() : "C2: " + currentLinkedSettings.getFloor_4_name()));
        } else {
            lbl_cluster_local_reg.setText("NO LOCAL REGISTRAR FOUND");
        }
        
        this.autoBackUp();
    }

    /**
     * The Following are disabled when using system.
     */
    public void whenSystem() {
        btn_home.setDisable(true);
        btn_view_admin.setDisable(true);
        btn_view_assistant_admin.setDisable(true);
        btn_view_local_registrar.setDisable(true);
        btn_view_co_registrar.setDisable(true);
        btn_view_evaluator.setDisable(true);
    }

    private void changeView(Node whatView) {
        Animate.fade(whatView, 150, () -> {
            vbox_system.setVisible(false);
            vbox_admin.setVisible(false);
            vbox_assistant_admin.setVisible(false);
            vbox_local_registrar.setVisible(false);
            vbox_co_registrar.setVisible(false);
            vbox_evaluators.setVisible(false);
            vbox_backup_restore.setVisible(false);
            whatView.setVisible(true);
        }, vbox_system, vbox_admin, vbox_assistant_admin, vbox_local_registrar,
                vbox_co_registrar, vbox_evaluators);
    }

    @Override
    public void onEventHandling() {
        super.addClickEvent(btn_home, () -> {
            Home.callHome(this);
        });
        super.addClickEvent(btn_view_system, () -> {
            this.changeView(vbox_system);
        });
        super.addClickEvent(btn_view_admin, () -> {
            this.changeView(vbox_admin);
        });
        super.addClickEvent(btn_view_assistant_admin, () -> {
            this.changeView(vbox_assistant_admin);
            fetchFaculty(Access.ACCESS_ASST_ADMIN, vbox_asst_admin_table, true);
        });
        super.addClickEvent(btn_view_local_registrar, () -> {
            this.changeView(vbox_local_registrar);
        });
        super.addClickEvent(btn_view_co_registrar, () -> {
            this.changeView(vbox_co_registrar);
            fetchFaculty(Access.ACCESS_CO_REGISTRAR, vbox_asst_registrar_table, true);
        });
        super.addClickEvent(btn_view_evaluator, () -> {
            this.changeView(vbox_evaluators);
            fetchFaculty(Access.ACCESS_EVALUATOR, vbox_evaluator_table, true);
        });

        this.systemAccountEvents();

        this.systemAdminEvents();

        this.localRegistrarEvents();

        //---------------
        // added back up and restore
        super.addClickEvent(btn_view_backup_restore, () -> {
            this.changeView(vbox_backup_restore);
        });
        this.backUpRestoreEvents();
        //

    }

    private void backUpRestoreEvents() {
        super.addClickEvent(btn_back_up, () -> {
            this.database("BACK_UP");
        });
        super.addClickEvent(btn_restore, () -> {
            this.database("RESTORE");
        });
        this.chkbx_enable_autobackup.selectedProperty().addListener((a)->{
            boolean isSelected = chkbx_enable_autobackup.isSelected();
            if(!isSelected) {
                // set all into inactive in backup schedule table
                this.setAllBackUpSchedInactive();
                this.setLatestBackUpSchedule();
            }
            this.vbox_autobackup_feature.setDisable(!isSelected);
        });
        super.addClickEvent(btn_apply_autobackup, ()->{
            this.applyNewBackUpSchedule();
        });
    }
    
    private void setAllBackUpSchedInactive() {
        ArrayList<BackupScheduleMapping> allActive = Mono.orm().newSearch(Database.connect().backup_schedule())
                .active(Order.desc(DB.backup_schedule().id)).all();
        if(allActive != null) {
            for(BackupScheduleMapping each : allActive) {
                each.setActive(0);
                Database.connect().backup_schedule().update(each);
            }
        }
    }
    
    private void applyNewBackUpSchedule() {
        this.setAllBackUpSchedInactive();
        BackupScheduleMapping newSchedule = new BackupScheduleMapping();
        newSchedule.setActive(1);
        newSchedule.setCreated_by(CollegeFaculty.instance().getFACULTY_ID());
        newSchedule.setCreated_date(Mono.orm().getServerTime().getDateWithFormat());
        
        int hr = (Integer.parseInt(cmb_autobackup_hr.getSelectionModel().getSelectedItem()));
        int min = (Integer.parseInt(cmb_autobackup_min.getSelectionModel().getSelectedItem()));
        String a = cmb_autobackup_a.getSelectionModel().getSelectedItem();
        
        String time = null;
        try {
            time = this.convert12hrTo24hr(hr, min, a);
        } catch (ParseException ex) {
            Logger.getLogger(AccessManagementHome.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(time == null) {
            Mono.fx().snackbar().showInfo(application_root, "Please try again later.");
            return;
        }
        newSchedule.setTime(time);
        
        int res = Database.connect().backup_schedule().insert(newSchedule);
        if(res != -1 || res != 0) {
//            PublicConstants.BACKUP_TIME = time;
            this.setLatestBackUpSchedule();
            Mono.fx().snackbar().showSuccess(application_root, "Applied new back up time schedule.");
        } else {
            Mono.fx().snackbar().showError(application_root, "Failed to apply. Try again later.");
        }
    }

    private void database(String request) {
        int res = 1;
        String title = "";
        String path = null;
        boolean isBackUp = false;
        if (request.equalsIgnoreCase("BACK_UP")) {
            isBackUp = true;
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(this.getStage());
            if (selectedDirectory == null) {
                // no directory selected
                return;
            }
            SimpleDateFormat formatter = new SimpleDateFormat("MM_dd_yyyy_hh_mm_s_a");
            String filename = formatter.format(Mono.orm().getServerTime().getDateWithFormat());
            path = selectedDirectory.getAbsolutePath() + "\\" + filename + ".monosync";
            res = BackUpAndRestore.backup(
                    PublicConstants.getServerIP(),
                    PublicConstants.getDATABASE_USERNAME(),
                    PublicConstants.getDATABASE_PASSWORD(),
                    PublicConstants.getDATABASE_NAME(),
                    path);
            title = "Back Up Database";
        } else if (request.equalsIgnoreCase("RESTORE")) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Monosync File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Monosync File", "*.monosync"));
            File file = fileChooser.showOpenDialog(this.getStage());
            if (file == null) {
                // no file selected
                return;
            }
            res = BackUpAndRestore.restore(
                    PublicConstants.getServerIP(),
                    PublicConstants.getDATABASE_USERNAME(),
                    PublicConstants.getDATABASE_PASSWORD(),
                    file.getAbsolutePath());
            title = "Restore Database";
        }
        if (res == 0) {
            // success 
            if (isBackUp) {
                // when back up, double check if file size is not 0
                // before concluding it is successful
                boolean notSaved = true;
                File fileBackUp = new File(path);
                if (fileBackUp.exists()) {
                    if ((fileBackUp.length() / 1024) == 0) {
                    } else {
                        notSaved = false;
                    }
                }
                if (notSaved) {
                    PublicConstants.addBackupLog("MANUAL", "FAILED", new Date());
                    Notifications.create().title(title)
                            .text("Please try again later.")
                            .showWarning();
                    return;
                }
                PublicConstants.addBackupLog("MANUAL", "SUCCESS", new Date());
            }
            Mono.fx().alert().createInfo()
                    .setHeader(title)
                    .setMessage("Successful Transaction!").show();
        } else if (res == 1) {
            // path error
            Mono.fx().alert().createError()
                    .setHeader(title)
                    .setMessage("Something is wrong with the path.").show();
            if(isBackUp) {
                PublicConstants.addBackupLog("MANUAL", "FAILED", new Date());
            }
        } else if (res == 2) {
            /**
             * This may failed when there is a space in the directory.
             */
            MessageBox.showError("Failed", "Failed to execute operation. This may be caused by the Operating System requiring administrative rights, or there is an invalid path character.");
            if(isBackUp) {
                PublicConstants.addBackupLog("MANUAL", "FAILED", new Date());
            }
        } else {
            // unknown error
            Mono.fx().alert().createError()
                    .setHeader(title)
                    .setMessage("Unknown error occured. Please try again later.").show();
            if(isBackUp) {
                PublicConstants.addBackupLog("MANUAL", "FAILED", new Date());
            }
        }
    }
    
    private void autoBackUp() {
        // insert time from 00:00 to 23:59
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 0);
//        calendar.set(Calendar.MINUTE, 0);
//
//        Calendar end = Calendar.getInstance();
//        end.set(Calendar.HOUR_OF_DAY, 23);
//        end.set(Calendar.MINUTE, 60);
//        cmb_autobackup_hr.getItems().clear();
//        cmb_autobackup_min.getItems().clear();
//        
//        SimpleDateFormat format = new SimpleDateFormat("hh");
//        SimpleDateFormat format2 = new SimpleDateFormat("mm");
        int ctrHr = 01;
        Integer ctrMin = 00;    
        do {
            cmb_autobackup_hr.getItems().add(ctrHr + "");
            ctrHr++;
        } while (ctrHr != 13);
        
        DecimalFormat formatter = new DecimalFormat("00");
        do {
            String strMin = formatter.format(ctrMin);
            cmb_autobackup_min.getItems().add(strMin);
            ctrMin++;
        } while (!ctrMin.equals(60));
        cmb_autobackup_hr.getSelectionModel().selectFirst();
        cmb_autobackup_min.getSelectionModel().selectFirst();
        
        
        cmb_autobackup_a.getItems().clear();
        cmb_autobackup_a.getItems().add("AM");
        cmb_autobackup_a.getItems().add("PM");
        cmb_autobackup_a.getSelectionModel().selectFirst();
        
        this.setLatestBackUpSchedule();
    }
    
    private void setLatestBackUpSchedule() {
        BackupScheduleMapping current = Mono.orm().newSearch(Database.connect().backup_schedule())
                .active(Order.desc(DB.backup_schedule().id)).first();
        
        if(current == null) {
            // set invisible when no active schedule of back up
            this.hbox_autobackup_info_user.setVisible(false);
            this.chkbx_enable_autobackup.setSelected(false);
            this.vbox_autobackup_feature.setDisable(true);
            return;
        }
        
        // set details and set selected chkbx if there is existing then set visible
        this.chkbx_enable_autobackup.setSelected(true);
        String[] split = current.getTime().split(":");
        String time = this.convert24hrTo12hr(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        this.lbl_time_auto.setText(time);
        
        this.lbl_create_by_auto.setText(FacultyUtility.getFacultyName(FacultyUtility.getFaculty(current.getCreated_by())));
        SimpleDateFormat format = new SimpleDateFormat("MMMMM dd, yyyy hh:mm:ss a");
        this.lbl_created_date_auto.setText(format.format(current.getCreated_date()) + ".");
        this.hbox_autobackup_info_user.setVisible(true);
    }

    private void systemAccountEvents() {
        super.addClickEvent(btn_create_system_admin, () -> {
            if (this.isGranted("Access Denied. Not A System Account.", Access.ACCESS_SYSTEM)) {
                this.createNewAdmin();
            }
        });
        super.addClickEvent(btn_reset_system_admin_pass, () -> {
            if (this.isGranted("Access Denied. Not A System Account.", Access.ACCESS_SYSTEM)) {
                this.resetAdminPassword();
            }
        });
        super.addClickEvent(btn_reclaim_system_admin, () -> {
            if (this.isGranted("Access Denied. Not A System Account.", Access.ACCESS_SYSTEM)) {
                this.reclaimAdminRights();
            }
        });
    }

    private void systemAdminEvents() {
        super.addClickEvent(btn_assign_new_sys_admin, () -> {
            if (this.isGranted("Access Denied. You Are Not Authorized.", Access.ACCESS_ADMIN)) {
                assignSystemAdmin();
            }
        });
        super.addClickEvent(btn_add_new_asst_admin, () -> {
            if (this.isGranted("Access Denied. You Are Not Authorized.", Access.ACCESS_ADMIN)) {
                addNewAsstAdmin();
            }
        });
        super.addClickEvent(btn_assign_local_registrar, () -> {
            if (this.isGranted("Access Denied. You Are Not Authorized.", Access.ACCESS_ADMIN, Access.ACCESS_ASST_ADMIN)) {
                assignLocalRegistrar();
            }
        });

        super.addClickEvent(btn_add_new_asst_admin1, () -> {
            if (this.isGranted("Access Denied. You Are Not Authorized.", Access.ACCESS_ADMIN)) {
                if (addNewAsstAdmin()) {
                    fetchFaculty(Access.ACCESS_ASST_ADMIN, vbox_asst_admin_table, true);
                }
            }
        });

        super.addClickEvent(btn_system_values, () -> {
            this.onShowSystemVariables();
        });

        super.addClickEvent(btn_change_cluster_admin, () -> {
            this.onChangeClusterOf(Access.ACCESS_ADMIN, lbl_cluster_admin);
        });
    }

    private void localRegistrarEvents() {
        super.addClickEvent(btn_add_asst_registrar, () -> {
            if (this.isGranted("Access Denied. You Are Not Authorized.", Access.ACCESS_LOCAL_REGISTRAR, Access.ACCESS_ADMIN, Access.ACCESS_ASST_ADMIN)) {
                this.assignAsstRegistrar();
            }
        });
        super.addClickEvent(btn_add_evaluators, () -> {
            if (this.isGranted("Access Denied. You Are Not Authorized.", Access.ACCESS_LOCAL_REGISTRAR, Access.ACCESS_ADMIN, Access.ACCESS_ASST_ADMIN, Access.ACCESS_CO_REGISTRAR)) {
                this.addEvaluator();
            }
        });

        super.addClickEvent(btn_add_asst_registrar1, () -> {
            if (this.isGranted("Access Denied. You Are Not Authorized.", Access.ACCESS_LOCAL_REGISTRAR, Access.ACCESS_ADMIN, Access.ACCESS_ASST_ADMIN)) {
                if (this.assignAsstRegistrar()) {
                    fetchFaculty(Access.ACCESS_CO_REGISTRAR, vbox_asst_registrar_table, true);
                }
            }
        });
        super.addClickEvent(btn_add_evaluators1, () -> {
            if (this.isGranted("Access Denied. You Are Not Authorized.", Access.ACCESS_LOCAL_REGISTRAR, Access.ACCESS_ADMIN, Access.ACCESS_ASST_ADMIN, Access.ACCESS_CO_REGISTRAR)) {
                if (this.addEvaluator()) {
                    fetchFaculty(Access.ACCESS_EVALUATOR, vbox_evaluator_table, true);
                }
            }
        });

        super.addClickEvent(btn_show_override_logs, () -> {
            if (this.isGranted("Access Denied. You Are Not Authorized.", Access.ACCESS_LOCAL_REGISTRAR, Access.ACCESS_ADMIN, Access.ACCESS_ASST_ADMIN, Access.ACCESS_CO_REGISTRAR)) {
                this.onShowOverrideLogs();
            }
        });

        super.addClickEvent(btn_change_cluster_local_reg, () -> {
            this.onChangeClusterOf(Access.ACCESS_LOCAL_REGISTRAR, lbl_cluster_local_reg);
        });
    }

    private void onShowOverrideLogs() {
        OverrideLogs overrideLogs = M.load(OverrideLogs.class);
        overrideLogs.onDelayedStart(); // do not put database transactions on startUp
        try {
            overrideLogs.getCurrentStage().show();
        } catch (NullPointerException e) {
            Stage a = overrideLogs.createChildStage(super.getStage());
            a.initStyle(StageStyle.UNDECORATED);
            a.show();
        }
    }

    private boolean isSameAccount(Integer facultyID) {
        if (facultyID.equals(CollegeFaculty.instance().getFACULTY_ID())) {

            int res = 0;
            if (isGranted(null, Access.ACCESS_ADMIN, Access.ACCESS_ASST_ADMIN)) {
                res = Mono.fx().alert().createConfirmation()
                        .setMessage("Managing your account is prohibited."
                                + " Do you still want to continue?")
                        .confirmYesNo();
            }
            if (res == 1) {
                return false;
            } else {
                Notifications.create().darkStyle()
                        .title("Request Cancelled")
                        .text("Managing your account is prohibited."
                                + "\nPlease ask your System Administrator"
                                + "\nabout this matter.")
                        .showWarning();
                return true;
            }
        }
        return false;
    }

    private AccountFacultyMapping getAccountFaculty(Integer facultyID, String accessLvl) {
        AccountFacultyMapping afMap = Mono.orm().newSearch(Database.connect().account_faculty())
                .eq(DB.account_faculty().FACULTY_id, facultyID)
                .active().first();
        if (afMap == null) {
            Notifications.create().darkStyle()
                    .title("Request Failed")
                    .text("The faculty you selected has no account yet.")
                    .showWarning();
        } else {
            if (Access.getAccessLevel(accessLvl) > Access.getAccessLevel(afMap.getAccess_level())) {
                if (Access.isGrantedIf(Access.ACCESS_ADMIN, Access.ACCESS_ASST_ADMIN)) {
                    int res = Mono.fx().alert().createConfirmation()
                            .setMessage("This will exhibit demotion of access level. "
                                    + "It is prohibited to change a high admission into lower. "
                                    + "Do you still want to continue?")
                            .confirmYesNo();
                    if (res == 1) {
                        return afMap;
                    }
                }
                Notifications.create().darkStyle()
                        .title("Request Denied")
                        .text("Demotion of Access Level.")
                        .showWarning();
                return null;
            }
        }
        return afMap;
    }

    // -------------------------------------------------------------------------
    // ACCESS LEVEL CHECKER
    //-------------------------------------
    private boolean isGranted(Object errorSnackBarMessage, String... grantedAccessLvl) {
        boolean granted = false;
        for (String current : grantedAccessLvl) {
            if (Access.isGranted(current, CollegeFaculty.instance().getACCESS_LEVEL())) {
                return true;
            }
        }

        if (errorSnackBarMessage == null) {
            return false;
        }
        Mono.fx().snackbar().showError(application_root, errorSnackBarMessage.toString());
        return false;
    }

    // --------------------------------------------------------------
    // TABLE LOADER
    // ------------------------------------------
    private void fetchFaculty(String access_level, VBox holder, boolean fade) {
        holder.setVisible(false);
        FetchFaculty fetch = new FetchFaculty();
        fetch.accessLevel = access_level;
        fetch.whenSuccess(() -> {
            ArrayList<FacultyInformation> results = fetch.getResults();
            createTable(results, holder, access_level);
            if (!fade) {
                return;
            }
            if (access_level.equalsIgnoreCase(Access.ACCESS_ASST_ADMIN)) {
                Animate.fade(vbox_asst_admin_no_found, 200, () -> {
                    vbox_asst_admin_no_found.setVisible(false);
                    vbox_asst_admin_table.setVisible(true);
                }, vbox_asst_admin_table);
            } else if (access_level.equalsIgnoreCase(Access.ACCESS_CO_REGISTRAR)) {
                Animate.fade(vbox_asst_registrar_no_found, 200, () -> {
                    vbox_asst_registrar_no_found.setVisible(false);
                    vbox_asst_registrar_table.setVisible(true);
                }, vbox_asst_registrar_table);
            } else if (access_level.equalsIgnoreCase(Access.ACCESS_EVALUATOR)) {
                Animate.fade(vbox_evaluator_no_found, 200, () -> {
                    vbox_evaluator_no_found.setVisible(false);
                    vbox_evaluator_table.setVisible(true);
                }, vbox_evaluator_table);
            }
        });
        fetch.whenCancelled(() -> {
            System.out.println("CANCELLED");
            if (access_level.equalsIgnoreCase(Access.ACCESS_ASST_ADMIN)) {
                Animate.fade(vbox_asst_admin_table, 150, () -> {
                    vbox_asst_admin_table.setVisible(false);
                    vbox_asst_admin_no_found.setVisible(true);
                }, vbox_asst_admin_no_found);
            } else if (access_level.equalsIgnoreCase(Access.ACCESS_CO_REGISTRAR)) {
                Animate.fade(vbox_asst_registrar_table, 150, () -> {
                    vbox_asst_registrar_table.setVisible(false);
                    vbox_asst_registrar_no_found.setVisible(true);
                }, vbox_asst_registrar_no_found);
            } else if (access_level.equalsIgnoreCase(Access.ACCESS_EVALUATOR)) {
                Animate.fade(vbox_evaluator_table, 150, () -> {
                    vbox_evaluator_table.setVisible(false);
                    vbox_evaluator_no_found.setVisible(true);
                }, vbox_evaluator_no_found);
            }
        });
        fetch.whenFailed(() -> {
            System.out.println("FAILED");
        });
        fetch.transact();
    }

    private final String KEY_MORE_INFO = "MORE_INFO";

    private void createTable(ArrayList<FacultyInformation> list, VBox parent, String accessLevelShown) {
        SimpleTable tblFaculty = new SimpleTable();
        for (FacultyInformation each : list) {
            SimpleTableRow row = new SimpleTableRow();
            row.setRowHeight(70.0);
            FacultyRow rowFX = M.load(FacultyRow.class);
            Label lbl_bulsu_id = rowFX.getLbl_bulsu_id();
            Label lbl_dept = rowFX.getLbl_department();
            Label lbl_name = rowFX.getLbl_name();
            JFXButton btn_remove = rowFX.getBtn_remove();

            lbl_bulsu_id.setText(each.getBulsuID());
            lbl_dept.setText((each.getDepartment().isEmpty() ? "NOT SET" : each.getDepartment()));
            lbl_name.setText(each.getFullName());

            super.addClickEvent(rowFX.getBtn_change_cluster(), () -> {
                FacultyInformation info = (FacultyInformation) row.getRowMetaData().get(KEY_MORE_INFO);
                FacultyRow fx = (FacultyRow) row.getRowMetaData().get("FX");
                Integer res = this.getCluster(true, info.getAccountFacultyMapping());
                if (res == null) {
                    Notifications.create().title("No Current Session")
                            .text("You can create a session in Linked System.\n"
                                    + " then click New Session.").showWarning();
                    return;
                }
                if (res.equals(1)) {
                    fx.getLbl_cluster_name().setText(info.getAccountFacultyMapping().getAssigned_cluster() == null ? "No Cluster Assigned" : (info.getAccountFacultyMapping().getAssigned_cluster().equals(3) ? "C1: " + currentLinkedSettings.getFloor_3_name() : "C2: " + currentLinkedSettings.getFloor_4_name()));
                    Notifications.create().darkStyle()
                            .title("Successfully Updated")
                            .text("Cluster of the faculty is updated\n"
                                    + "successfully.").showInformation();
                }
            });

            AccountFacultyMapping afMap = each.getAccountFacultyMapping();
            rowFX.getLbl_cluster_name().setText(afMap.getAssigned_cluster() == null ? "No Cluster Assigned" : (afMap.getAssigned_cluster().equals(1) ? "C1: " + currentLinkedSettings.getFloor_3_name() : "C2: " + currentLinkedSettings.getFloor_4_name()));

            super.addClickEvent(btn_remove, () -> {
                this.onRemove(row, accessLevelShown, tblFaculty);
            });

            row.getRowMetaData().put(KEY_MORE_INFO, each);
            row.getRowMetaData().put("FX", rowFX);
            SimpleTableCell cellParent = new SimpleTableCell();
            cellParent.setResizePriority(Priority.ALWAYS);
            cellParent.setContentAsPane(rowFX.getApplicationRoot());

            row.addCell(cellParent);

            /**
             * Add to table.
             */
            tblFaculty.addRow(row);
        }

        // table view
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(tblFaculty);
        simpleTableView.setFixedWidth(true);
        // attach to parent variable name in scene builder
        simpleTableView.setParentOnScene(parent);
    }

    // -----------------------------------------------------------
    // ADMIN CREATOR
    // -----------------------------------------
    private void createNewAdmin() {
        ArrayList<AccountFacultyMapping> afMaps = this.getCurrentAdmin();
        if (afMaps != null) {
            int res = Mono.fx().alert().createConfirmation()
                    .setMessage("There is an existing active Administrator. This will remove his/her administrative rights.")
                    .confirmCustom("Continue", "Cancel");
            if (res != 1) {
                return;
            }
        }

        CreateSystemAdmin create = M.load(CreateSystemAdmin.class);
        try {
            create.getCurrentStage().showAndWait();
            create.onDelayedStart();
        } catch (NullPointerException e) {
            Stage a = create.createChildStage(super.getStage());
            a.initStyle(StageStyle.UNDECORATED);
            a.showAndWait();
        }
    }

    // -----------------------------------------------------------------
    // RESET ADMIN PASSWORD
    // --------------------------------------
    private void resetAdminPassword() {
        AccountFacultyMapping afMap = Mono.orm().newSearch(Database.connect().account_faculty())
                .eq(DB.account_faculty().access_level, Access.ACCESS_ADMIN)
                .active().first();
        if (afMap == null) {
            Mono.fx().alert().createWarning()
                    .setMessage("No System Administrator Account found. Create a new one first by clicking Create System Administrator.")
                    .show();
            return;
        }
        ResetAdminPassword create = M.load(ResetAdminPassword.class);
        try {
            create.getCurrentStage().show();
        } catch (NullPointerException e) {
            Stage a = create.createChildStage(super.getStage());
            a.initStyle(StageStyle.UNDECORATED);
            a.showAndWait();
        }
    }

    // -----------------------------------------------------------
    // FACULTY SELECTOR
    // -----------------------------------------------
    private FacultyMapping selectFaculty(String message) {
        FacultyChooser facultyChooser = M.load(FacultyChooser.class);
        facultyChooser.onDelayedStart(); // do not put database transactions on startUp
        try {
            System.out.println("Stage Recycled. ^^v");
            facultyChooser.getCurrentStage().showAndWait();
        } catch (NullPointerException e) {
            Stage a = facultyChooser.createChildStage(super.getStage());
            a.initStyle(StageStyle.UNDECORATED);
            a.showAndWait();
        }

        FacultyMapping selectedFaculty = facultyChooser.getSelectedFaculty();
        if (selectedFaculty != null && message != null) {
            int res = Mono.fx().alert().createConfirmation()
                    .setMessage(message).confirmYesNo();
            if (res == -1) {
                return null;
            }
        }
        return selectedFaculty;
    }

    class FetchFaculty extends Transaction {

        public String accessLevel;

        private ArrayList<FacultyInformation> results = new ArrayList<>();

        public ArrayList<FacultyInformation> getResults() {
            return results;
        }

        @Override
        protected boolean transaction() {
            ArrayList<FacultyMapping> faculty = Mono.orm().newSearch(Database.connect().faculty())
                    .eq(DB.faculty().designation, accessLevel)
                    .active(Order.asc(DB.faculty().last_name))
                    .all();
            if (faculty == null) {
                System.out.println("No " + accessLevel + " found.");
                return false;
            }
            for (FacultyMapping result : faculty) {
                FacultyInformation info = new FacultyInformation(result);
                results.add(info);
            }
            return true;
        }

        @Override
        protected void after() {
        }
    }

    // --------------------------------------------------
    // ACTION
    // -------------------------------------------------
    private void reclaimAdminRights() {
        FacultyMapping selectedFaculty = selectFaculty("This will reclaim the rights of the previous admin and transfer the authority to the selected faculty. Continue?");
        if (selectedFaculty == null) {
            return;
        }

        ArrayList<AccountFacultyMapping> afMaps = this.getCurrentAdmin();
        if (afMaps != null) {
            for (AccountFacultyMapping afMap : afMaps) {
                FacultyInformation info = new FacultyInformation(afMap);
                info.getFacultyMapping().setDesignation(Access.ACCESS_FACULTY);
                afMap.setAccess_level(Access.ACCESS_FACULTY);

                if (!Database.connect().account_faculty().update(afMap)
                        || !Database.connect().faculty().update(info.getFacultyMapping())) {
                    Notifications.create().darkStyle()
                            .title("Request Failed")
                            .text("Something went wrong. Try again later")
                            .showError();
                    return;
                }
            }
        }
        FacultyInformation infoNewAdmin = new FacultyInformation(selectedFaculty);
        AccountFacultyMapping afMapNewAdmin = infoNewAdmin.getAccountFacultyMapping();

        if (afMapNewAdmin.getAccess_level().equalsIgnoreCase(Access.ACCESS_ADMIN)) {
            Notifications.create().darkStyle()
                    .title("No Changes Made")
                    .text("Faculty selected is already"
                            + "\n a Administrator.")
                    .showError();
            return;
        }

        afMapNewAdmin.setAccess_level(Access.ACCESS_ADMIN);
        selectedFaculty.setDesignation(Access.ACCESS_ADMIN);
        if (!Database.connect().account_faculty().update(afMapNewAdmin)
                || !Database.connect().faculty().update(selectedFaculty)) {
            Notifications.create().darkStyle()
                    .title("Request Failed")
                    .text("Something went wrong. Try again later")
                    .showError();
            return;
        } else {
            Notifications.create().darkStyle()
                    .title("Assigned Successfully")
                    .text("New System Administrator is assigned.")
                    .showInformation();
        }

    }

    private ArrayList<AccountFacultyMapping> getCurrentAdmin() {
        ArrayList<AccountFacultyMapping> afMaps = Mono.orm().newSearch(Database.connect().account_faculty())
                .eq(DB.account_faculty().access_level, Access.ACCESS_ADMIN)
                .active().all();
        return afMaps;
    }

    private void assignSystemAdmin() {
        FacultyMapping selectedFaculty = selectFaculty("This will remove your authority of being a System Administrator and change it into Faculty. Account will automatically logout after. Do you still want to continue?");
        if (selectedFaculty == null) {
            return;
        }
        if (isSameAccount(selectedFaculty.getId())) {
            return;
        }
        AccountFacultyMapping afMap = this.getAccountFaculty(selectedFaculty.getId(), Access.ACCESS_ADMIN);
        if (afMap == null) {
            return;
        }

        if (afMap.getAccess_level().equalsIgnoreCase(Access.ACCESS_ADMIN)) {
            Notifications.create().darkStyle()
                    .title("No Changes Made")
                    .text("Faculty selected is already"
                            + "\n a Administrator.")
                    .showError();
            return;
        }

        afMap.setAccess_level(Access.ACCESS_ADMIN);
        selectedFaculty.setDesignation(Access.ACCESS_ADMIN);
        if (Database.connect().account_faculty().update(afMap)
                && Database.connect().faculty().update(selectedFaculty)) {
            Notifications.create().darkStyle()
                    .title("Assigned Successfully")
                    .text("New System Administrator is assigned.")
                    .showInformation();
            AccountFacultyMapping userAFMap = Database.connect().account_faculty().getPrimary(CollegeFaculty.instance().getACCOUNT_ID());
            FacultyMapping userFMap = Database.connect().faculty().getPrimary(CollegeFaculty.instance().getFACULTY_ID());
            if (userAFMap == null) {
                System.out.println("No Account found for user.");
                return;
            }
            userAFMap.setAccess_level(Access.ACCESS_FACULTY);
            userFMap.setDesignation(Access.ACCESS_FACULTY);
            if (!Database.connect().account_faculty().update(userAFMap)) {
                System.out.println("User not set into Faculty.");
            } else {
//                this.onLogout();
                MainApplication.die(0);
            }
        } else {
            Notifications.create().darkStyle()
                    .title("Request Failed")
                    .text("Something went wrong. Try again later")
                    .showError();
        }
    }

    private boolean addNewAsstAdmin() {
        FacultyMapping selectedFaculty = selectFaculty(null);
        if (selectedFaculty == null) {
            return false;
        }
        if (this.isSameAccount(selectedFaculty.getId())) {
            return false;
        }
        AccountFacultyMapping afMap = this.getAccountFaculty(selectedFaculty.getId(), Access.ACCESS_ASST_ADMIN);
        if (afMap == null) {
            return false;
        }
        if (afMap.getAccess_level().equalsIgnoreCase(Access.ACCESS_ASST_ADMIN)) {
            Notifications.create().darkStyle()
                    .title("No Changes Made")
                    .text("Faculty selected is already"
                            + "\n a Assistant Administrator.")
                    .showError();
            return false;
        }
        afMap.setAccess_level(Access.ACCESS_ASST_ADMIN);
        selectedFaculty.setDesignation(Access.ACCESS_ASST_ADMIN);
        if (Database.connect().account_faculty().update(afMap)
                && Database.connect().faculty().update(selectedFaculty)) {
            Notifications.create().darkStyle()
                    .title("Added Successfully")
                    .text("New Assistant System Administrator"
                            + "\nis added.")
                    .showInformation();
            return true;
        } else {
            Notifications.create().darkStyle()
                    .title("Request Failed")
                    .text("Something went wrong. Try again later")
                    .showError();
            return false;
        }
    }

    private void assignLocalRegistrar() {
        FacultyMapping selectedFaculty = selectFaculty(null);
        if (selectedFaculty == null) {
            return;
        }
        if (isSameAccount(selectedFaculty.getId())) {
            return;
        }
        AccountFacultyMapping afMap = this.getAccountFaculty(selectedFaculty.getId(), Access.ACCESS_LOCAL_REGISTRAR);
        if (afMap == null) {
            return;
        }
//        if (!selectedFaculty.getDesignation().equalsIgnoreCase(Access.ACCESS_LOCAL_REGISTRAR)) {
//            Notifications.create().darkStyle()
//                    .title("Request Cancelled")
//                    .text("The faculty is not designated"
//                            + "\n as a Local Registrar.")
//                    .showWarning();
//            return;
//        }

        if (afMap.getAccess_level().equalsIgnoreCase(Access.ACCESS_LOCAL_REGISTRAR)) {
            Notifications.create().darkStyle()
                    .title("No Changes Made")
                    .text("Faculty selected is already"
                            + "\n a Local Registrar.")
                    .showWarning();
            return;
        }
        boolean updatedDetails = true;
        AccountFacultyMapping currentLocalRegistrar = Mono.orm().newSearch(Database.connect().account_faculty())
                .eq(DB.account_faculty().access_level, Access.ACCESS_LOCAL_REGISTRAR)
                .active(Order.asc(DB.account_faculty().id)).first();
        if (currentLocalRegistrar != null) {
            int res = Mono.fx().alert().createConfirmation()
                    .setMessage("There is an existing Local Registrar account. This action will make the said account into Faculty access level. Continue?")
                    .confirmYesNo();
            if (res == -1) {
                return;
            }
            currentLocalRegistrar.setAccess_level(Access.ACCESS_FACULTY);
            updatedDetails = Database.connect().account_faculty().update(currentLocalRegistrar);
        }

        if (updatedDetails) {
            afMap.setAccess_level(Access.ACCESS_LOCAL_REGISTRAR);
            updatedDetails = Database.connect().account_faculty().update(afMap);
        }

        if (updatedDetails) {
            Notifications.create().darkStyle()
                    .title("Assigned Successfully")
                    .text("New Local Registrar is assigned.")
                    .showInformation();
        } else {
            Notifications.create().darkStyle()
                    .title("Request Failed")
                    .text("Something went wrong. Try again later")
                    .showError();
        }
    }

    private Integer getCluster(boolean save, AccountFacultyMapping afMap) {
        if (currentLinkedSettings == null) {
            return null;
        }
        boolean cluster2Closed = currentLinkedSettings.getFloor_4_name() == null;
        boolean invalid = true;
        Integer choosen = null;
        while (invalid) {
            int res = Mono.fx().alert().createConfirmation()
                    .setHeader("Assign Cluster")
                    .setMessage("Please choose a cluster for the faculty selected.")
                    .confirmCustom("Cluster 1: " + currentLinkedSettings.getFloor_3_name(), "Cluster 2: " + (cluster2Closed ? "CLOSED" : currentLinkedSettings.getFloor_4_name()));
            if (cluster2Closed && res == -1) {
            } else {
                invalid = false;
            }
            choosen = res == -1 ? 4 : 3;
        }
        if (save) {
            afMap.setAssigned_cluster(choosen);
            boolean res = Database.connect().account_faculty().update(afMap);
            return (res ? 1 : -1);
        }
        return choosen;
    }

    private boolean assignAsstRegistrar() {
        FacultyMapping selectedFaculty = selectFaculty(null);
        if (selectedFaculty == null) {
            return false;
        }
        if (isSameAccount(selectedFaculty.getId())) {
            return false;
        }
        AccountFacultyMapping afMap = this.getAccountFaculty(selectedFaculty.getId(), Access.ACCESS_CO_REGISTRAR);
        if (afMap == null) {
            return false;
        }

        if (afMap.getAccess_level().equalsIgnoreCase(Access.ACCESS_CO_REGISTRAR)) {
            Notifications.create().darkStyle()
                    .title("No Changes Made")
                    .text("Faculty selected is already"
                            + "\n a Assistant Registrar.")
                    .showError();
            return false;
        }

        afMap.setAccess_level(Access.ACCESS_CO_REGISTRAR);
        selectedFaculty.setDesignation(Access.ACCESS_CO_REGISTRAR);
        if (Database.connect().account_faculty().update(afMap)
                && Database.connect().faculty().update(selectedFaculty)) {
            Notifications.create().darkStyle()
                    .title("Assigned Successfully")
                    .text("New Assistant Registrar is assigned.")
                    .showInformation();
            return true;
        } else {
            Notifications.create().darkStyle()
                    .title("Request Failed")
                    .text("Something went wrong. Try again later")
                    .showWarning();
            return false;
        }
    }

    private boolean addEvaluator() {
        FacultyMapping selectedFaculty = selectFaculty(null);
        if (selectedFaculty == null) {
            return false;
        }
        if (isSameAccount(selectedFaculty.getId())) {
            return false;
        }
        AccountFacultyMapping afMap = this.getAccountFaculty(selectedFaculty.getId(), Access.ACCESS_EVALUATOR);
        if (afMap == null) {
            return false;
        }

        Integer clusterNumber = this.getCluster(false, afMap);
        if (afMap.getAccess_level().equalsIgnoreCase(Access.ACCESS_EVALUATOR) && afMap.getAssigned_cluster().equals(clusterNumber)) {
            Notifications.create().darkStyle()
                    .title("No Changes Made")
                    .text("Faculty selected is already"
                            + "\n a Evaluator.")
                    .showError();
            return false;
        }

        afMap.setAccess_level(Access.ACCESS_EVALUATOR);
        afMap.setAssigned_cluster(clusterNumber);
        selectedFaculty.setDesignation(Access.ACCESS_EVALUATOR);
        if (Database.connect().account_faculty().update(afMap)
                && Database.connect().faculty().update(selectedFaculty)) {
            Notifications.create().darkStyle()
                    .title("Added Successfully")
                    .text("New Evaluator is added.")
                    .showInformation();
            return true;
        } else {
            Notifications.create().darkStyle()
                    .title("Request Failed")
                    .text("Something went wrong. Try again later")
                    .showWarning();
            return false;
        }
    }

    private void onRemove(SimpleTableRow row, String accessLevelShown, SimpleTable tblFaculty) {
        FacultyInformation info = (FacultyInformation) row.getRowMetaData().get(KEY_MORE_INFO);
        if (isSameAccount(info.getFacultyMapping().getId())) {
            return;
        }
        if (accessLevelShown.equalsIgnoreCase(Access.ACCESS_ASST_ADMIN)) {
            if (this.isGranted("Access Denied. Not A System Administrator.", Access.ACCESS_ADMIN)) {
                this.setToDefaultAccessLevel(info, tblFaculty, row);
            }
        } else if (accessLevelShown.equalsIgnoreCase(Access.ACCESS_CO_REGISTRAR)) {
            if (this.isGranted("Access Denied. Not a Local Registrar.", Access.ACCESS_LOCAL_REGISTRAR, Access.ACCESS_ADMIN, Access.ACCESS_ASST_ADMIN)) {
                this.setToDefaultAccessLevel(info, tblFaculty, row);
            }
        } else if (accessLevelShown.equalsIgnoreCase(Access.ACCESS_EVALUATOR)) {
            boolean accessDenied = false;
            if (this.isGranted("Access Denied. Not a Local Registrar.", Access.ACCESS_LOCAL_REGISTRAR, Access.ACCESS_CO_REGISTRAR, Access.ACCESS_ADMIN, Access.ACCESS_ASST_ADMIN)) {
                this.setToDefaultAccessLevel(info, tblFaculty, row);
            }
        }
    }

    private void setToDefaultAccessLevel(FacultyInformation info, SimpleTable table, SimpleTableRow row) {
        AccountFacultyMapping afMap = info.getAccountFacultyMapping();
        FacultyMapping fMap = info.getFacultyMapping();
        afMap.setAccess_level(Access.ACCESS_FACULTY);
        fMap.setDesignation(Access.ACCESS_FACULTY);
        if (Database.connect().account_faculty().update(afMap)
                && Database.connect().faculty().update(fMap)) {
            Notifications.create().darkStyle()
                    .title("Removed Successfully")
                    .text("Account selected is now in"
                            + "\n Faculty Access Level.")
                    .showInformation();
            table.getChildren().remove(row);
        } else {
            Notifications.create().darkStyle()
                    .title("Request Failed")
                    .text("Something went wrong. Try again later")
                    .showWarning();
        }
    }

    /**
     * There is no user access for logout everyone can logout ofcourse.
     */
    public void onLogout() {
        Logout logout = AccountManager.instance().createLogout();
        logout.whenStarted(() -> {
            GenericLoadingShow.instance().show();
        });
        logout.whenCancelled(() -> {

        });
        logout.whenFailed(() -> {
            // sometimes it fails to logout
        });
        logout.whenSuccess(() -> {

        });
        logout.whenFinished(() -> {
            GenericLoadingShow.instance().hide();
            /**
             * Only destroy the threads of this session.
             */
            ThreadMill.threads().shutdown();
            // close the stage.
            Mono.fx().getParentStage(application_root).close();
            // relaunch the login screen.
            relaunchLogin();

        });
        logout.setRestTime(300);
        logout.transact();
    }

    private void relaunchLogin() {
        MainApplication.launchLogin();
    }

    //-----------------------------------
    private void onShowSystemVariables() {
        SystemValues systemValues = M.load(SystemValues.class);
        systemValues.onDelayedStart(); // do not put database transactions on startUp
        try {
            systemValues.getCurrentStage().showAndWait();
        } catch (NullPointerException e) {
            Stage a = systemValues.createChildStage(this.getStage());
            a.initStyle(StageStyle.UNDECORATED);
            a.showAndWait();
        }
    }

    private void onChangeClusterOf(String access, Label lbl) {
        currentAccount = Database.connect().account_faculty().getPrimary(CollegeFaculty.instance().getACCOUNT_ID());
        if (!currentAccount.getAccess_level().equalsIgnoreCase(access)) {
            Mono.fx().snackbar().showError(application_root, "Not Authorized To Change This Cluster");
            return;
        }
        Integer res = this.getCluster(true, currentAccount);
        if (res == null) {
            Notifications.create().title("No Current Session")
                    .text("You can create a session in Linked System.\n"
                            + " then click New Session.").showWarning();
            return;
        }
        if (res.equals(1)) {
            lbl.setText(currentAccount.getAssigned_cluster() == null ? "No Cluster Assigned" : (currentAccount.getAssigned_cluster().equals(3) ? "C1: " + currentLinkedSettings.getFloor_3_name() : "C2: " + currentLinkedSettings.getFloor_4_name()));
            Notifications.create().darkStyle()
                    .title("Successfully Updated")
                    .text("Cluster of the faculty is updated\n"
                            + "successfully.").showInformation();
        }
    }
    
    public static void main(String[] args) throws ParseException, ParseException {
        // from 24 hr into 12 hr
//        Date date = new Date();
//        SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
//        date.setHours(24);
//        date.setMinutes(00);
//        System.out.println(format.format(date));

        // 12 hr into 24 hr
        String a = "PM";
        int hr = 4;
        int min = 00;
        String time = hr +":" + min + " " + a; 
        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
        Date date = parseFormat.parse(time);
        System.out.println(parseFormat.format(date) + " = " + displayFormat.format(date));
        
    }
    
    private String convert12hrTo24hr(int hr, int min, String a) throws ParseException {
        String time = hr +":" + min + " " + a; 
        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
        Date date = parseFormat.parse(time);
        System.out.println(parseFormat.format(date) + " = " + displayFormat.format(date));
        return displayFormat.format(date);
    }
    
    private String convert24hrTo12hr(int hr, int min) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
        date.setHours(hr);
        date.setMinutes(min);
        System.out.println(format.format(date));
        return format.format(date);
    }

}

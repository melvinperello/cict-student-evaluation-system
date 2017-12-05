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

import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import com.jhmvin.transitions.Animate;
import com.melvin.mono.fx.MonoLauncher;
import com.melvin.mono.fx.bootstrap.M;
import com.melvin.mono.fx.events.MonoClick;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.apache.commons.lang3.text.WordUtils;
import org.cict.MainApplication;
import org.cict.ThreadMill;
import org.cict.accountmanager.AccountManager;
import org.cict.accountmanager.Logout;
import org.cict.accountmanager.faculty.FacultyMainController;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.cict.authentication.authenticator.SystemProperties;
import org.cict.evaluation.EvaluateController;
import org.cict.reports.ReportsMain;
import org.controlsfx.control.Notifications;
import update.org.cict.controller.adding.AddingHome;
import update2.org.cict.controller.academicprogram.AcademicHome;
import update3.org.cict.access.Access;
import update3.org.cict.access.management.AccessManagementHome;
import update3.org.cict.controller.sectionmain.SectionHomeController;
import update3.org.cict.my_account.MyAccountHome;
import update3.org.cict.termcalendar.AcademicTermHome;
import update4.org.cict.linked_manager.LinkedHome;
import update5.org.cict.facultyhub.FacultyHub;
import update5.org.cict.student.controller.StudentHomeController;

/**
 *
 * @author Jhon Melvin
 */
public class SystemHome extends MonoLauncher {

    @FXML
    private Label lbl_hi;

    @FXML
    private Label lbl_system_id;

    @FXML
    private Label lbl_bsu_id;

    @FXML
    private Label lbl_lastname;

    @FXML
    private Label lbl_frstname;

    @FXML
    private Label lbl_middlename;

    @FXML
    private Label lbl_gender;

    @FXML
    private Label lbl_access;

    @FXML
    private Label lbl_desgination;

    @FXML
    private JFXButton btn_evaluation;

    @FXML
    private JFXButton btn_adding;

    @FXML
    private JFXButton btn_faculty_center;

    @FXML
    private JFXButton btn_my_account;

    @FXML
    private JFXButton btn_logout;

    @FXML
    private JFXButton btn_reports;

//    @FXML
//    private JFXButton btn_system_values;
    @FXML
    private JFXButton btn_academic_term;

    @FXML
    private JFXButton btn_academic_programs;

    @FXML
    private JFXButton btn_student;

    @FXML
    private JFXButton btn_section;

    @FXML
    private JFXButton btn_faculty;

    @FXML
    private JFXButton btn_access_controls;

    @FXML
    private JFXButton btn_linked;

    @Override
    public void onStartUp() {
        // call upon loading automatically.
    }

    @Override
    public void onDelayedStart() {
        this.application_root = this.getApplicationRoot();
        //----------------------------------------------------------------------
        MonoClick.addClickEvent(btn_evaluation, () -> {
            checkStatus("evaluation", () -> {
                onShowEvaluation();
            });
        });

        MonoClick.addClickEvent(btn_adding, () -> {
            checkStatus("adding", () -> {
                onShowAddingAndChanging();
            });
        });

        MonoClick.addClickEvent(btn_academic_programs, () -> {
            this.onShowAcademicPrograms();
        });

        MonoClick.addClickEvent(btn_section, () -> {
            if(this.isCurrentAcadTermNotSet()) {
                Notifications.create()
                        .title("No Academic Term Found")
                        .text("Academic term must be set first\n"
                                + "to proceed section management.").showWarning();
                return;
            }
            this.onShowSectionManagement();
        });

        MonoClick.addClickEvent(btn_faculty, () -> {
            this.onShowFacultyManagement();
        });

        MonoClick.addClickEvent(btn_logout, () -> {
            onLogout();
        });

        MonoClick.addClickEvent(btn_academic_term, () -> {
            onShowAcademicTerm();
        });

        MonoClick.addClickEvent(btn_access_controls, () -> {
            onShowAccessControls();
        });

        MonoClick.addClickEvent(btn_my_account, () -> {
            onShowMyAccount();
        });

        MonoClick.addClickEvent(btn_linked, () -> {
            onShowLinkedManagement();
        });

        MonoClick.addClickEvent(btn_student, () -> {
            onShowStudentHome();
        });

        MonoClick.addClickEvent(btn_faculty_center, () -> {
            this.onShowFacultyHub();
//            Mono.fx().snackbar().showInfo(application_root, "Sorry this feature is under constructions.");
        });

//        MonoClick.addClickEvent(btn_system_values, () -> {
//            this.onShowSystemVariables();
//        });
        //----------------------------------------------------------------------
        this.displayLabels();

        //----------------------------
        MonoClick.addClickEvent(btn_reports, () -> {
            this.onShowReports();
        });
    }
//    
//    private void onShowSystemVariables() {
//        SystemValues systemValues = M.load(SystemValues.class);
//        systemValues.onDelayedStart(); // do not put database transactions on startUp
//        try {
//            systemValues.getCurrentStage().showAndWait();
//        } catch (NullPointerException e) {
//            Stage a = systemValues.createChildStage(this.getCurrentStage());
//            a.initStyle(StageStyle.UNDECORATED);
//            a.showAndWait();
//        }
//    }

    /**
     * Display Information labels on the left side.
     */
    private void displayLabels() {
        CollegeFaculty loggedUser = CollegeFaculty.instance();
        this.setLabelText(lbl_hi, "Hi " + loggedUser.getFIRST_NAME() + "!");
        this.lbl_system_id.setText("EMS-" + loggedUser.getFACULTY_ID());
        try {
            this.lbl_bsu_id.setText(loggedUser.getBULSU_ID().toUpperCase());
        } catch (Exception e) {
            this.lbl_bsu_id.setText("No ID");
        }
        //
        this.setLabelText(this.lbl_frstname, loggedUser.getFIRST_NAME());
        this.setLabelText(this.lbl_lastname, loggedUser.getLAST_NAME());
        this.setLabelText(this.lbl_middlename, loggedUser.getMIDDLE_NAME());
        this.setLabelText(lbl_gender, loggedUser.getGENDER());
        this.setLabelText(lbl_access, loggedUser.getACCESS_LEVEL());
        this.setLabelText(lbl_desgination, loggedUser.getDESIGNATION());
    }

    /**
     * Null safe label setting text.
     *
     * @param label
     * @param value
     */
    private void setLabelText(Label label, String value) {
        if (value == null) {
            label.setText("");
            return;
        }

        //----------------------------------------------------------------------
        if (value.equalsIgnoreCase(Access.ACCESS_CO_REGISTRAR)) {
            value = "Asst. Registrar";
        }
        //----------------------------------------------------------------------
        value = value.trim();
        if (value.isEmpty()) {
            label.setText("No Data");
        }
        value = value.replace('_', ' ');
        label.setText(WordUtils.capitalizeFully(value));
    }

    //--------------------------------------------------------------------------
    private Pane application_root;
    private static Stage APPLICATION_STAGE;
    public final static String SCENE_TRANSITION_COLOR = "#414852";

    /**
     * Launch the main menu from the very beginning.
     *
     * @param systemLaunch
     */
    public static void launchHome(boolean systemLaunch) {
        SystemHome homeFx = M.load(SystemHome.class);
        Stage mainStage = homeFx.createStage();
        mainStage.setMinWidth(1024);
        mainStage.setMinHeight(700);
        mainStage.setMaximized(true);
        mainStage.setTitle("CICT Enrollment Evaluation Management System 2017 | Monosync Studio PH");
        mainStage.show();
        homeFx.onDelayedStart();
        homeFx.onStageClosing();

        /**
         * Run application as system.
         */
        if (systemLaunch) {
            AccessManagementHome controller = homeFx.showAccessControls();
            // disable buttons for system account.
            controller.whenSystem();
        }
    }

    /**
     * Closing event for this stage.
     */
    private void onStageClosing() {
        APPLICATION_STAGE = this.getCurrentStage();
        APPLICATION_STAGE.setOnCloseRequest(onClose -> {
            this.onLogout();
            onClose.consume();
        });
    }

    //--------------------------------------------------------------------------
    /**
     * Incase of a system glitch and the system account was able to access the
     * main menu.
     *
     * @return
     */
    private boolean sysWarning() {
        boolean restricted = restrictSystem();
        if (restricted) {
            Mono.fx().alert()
                    .createWarning().setTitle("System")
                    .setHeader("System Restriction")
                    .setMessage("System Account is restricted to use this feature.")
                    .show();
        }
        return restricted;
    }

    /**
     * To ensure that a system account will not have authority to touch any
     * modules.
     *
     * @return
     */
    private boolean restrictSystem() {
        CollegeFaculty cf_instance = CollegeFaculty.instance();
        if (cf_instance == null) {
            return true;
        } else {
            // if not null
            if (cf_instance.getACCESS_LEVEL() == null
                    || cf_instance.getACCESS_LEVEL().equals(Access.ACCESS_SYSTEM)) {
                // if system or access was null
                return true;
            }
        }

        return false;
    }

    //--------------------------------------------------------------------------
    /**
     * Logout the current user and terminate the program. this method is not
     * called directly it is called as a stage closing event. please do not
     * invoke this method directly.
     */
    private void onLogout() {
        int res = Mono.fx().alert()
                .createConfirmation()
                .setHeader("Logout Account")
                .setTitle("Confirmation")
                .setMessage("Are you sure you want to logout this account?")
                .confirmYesNo();
        if (res != 1) {
            return;
        }
        //------------------------------------------------------------------
        if (this.restrictSystem()) {
            MainApplication.die(0);
            return;
        }
        //------------------------------------------------------------------
        Logout logout = AccountManager.instance().createLogout();
        logout.whenStarted(() -> {
            //
        });
        logout.whenCancelled(() -> {
            //
        });
        logout.whenFailed(() -> {
            // sometimes it fails to logout
        });
        logout.whenSuccess(() -> {
            //
        });
        logout.whenFinished(() -> {
            // destroy threads.
            ThreadMill.threads().shutdown();
            // close the stage.
            APPLICATION_STAGE.close();
            // kill the app to be sure that values will be refreshed
            MainApplication.die(0);
        });
        logout.transact();
    }

    @Deprecated
    private void relaunchLogin() {
        // relaunching the app is dangerous as it does not resets singletons
        MainApplication.launchLogin();
    }

    //--------------------------------------------------------------------------
    /**
     * Replaces the current root that is being displayed.
     *
     * @param controller
     * @param packer
     * @param fxml
     */
    private void changeRoot(ControllerFX controller, String packer, String fxml) {

        Pane fxRoot = Mono.fx().create()
                .setPackageName(packer)
                .setFxmlDocument(fxml)
                .makeFX()
                .setController(controller)
                .pullOutLayout();

        super.setSceneColor(SCENE_TRANSITION_COLOR);
        Animate.fade(this.getApplicationRoot(), 150, () -> {
            this.changeRoot(fxRoot);
        }, fxRoot);
    }

    /**
     * Call Home menu from other modules. this is used as a callback when
     * calling home from other menu.
     *
     * @param scene
     */
    public static void callHome(SceneFX scene) {
        SystemHome homeFx = M.load(SystemHome.class);
        homeFx.onDelayedStart();
        // revert back this color will be the background of the scene
        // when replacing a root this color will be visible upon transitions
        scene.setSceneColor(SCENE_TRANSITION_COLOR);
        Animate.fade(scene.getApplicationRoot(), 150, () -> {
            scene.replaceRoot(homeFx.getApplicationRoot());
        }, homeFx.getApplicationRoot());
    }

    //--------------------------------------------------------------------------
    /**
     * Checks the status whether if the evaluation and adding services are
     * online.
     *
     * @param operation
     * @param next
     */
    private void checkStatus(String operation, Runnable next) {
        //----------------------------------------------------------------------
        if (this.sysWarning()) {
            return;
        }
        //----------------------------------------------------------------------

        AcademicTermHome.ServiceStatusChecker ssc = new AcademicTermHome().new ServiceStatusChecker();
        ssc.whenStarted(() -> {
            if(operation.equalsIgnoreCase("evaluation"))
                btn_evaluation.setDisable(true);
            else
                btn_adding.setDisable(true);
        });

        ssc.whenCancelled(() -> {
            if(operation.equalsIgnoreCase("evaluation")) {
                Notifications.create()
                        .title("No Academic Term Found")
                        .text("Academic term must be set first\n"
                                + "to proceed evaluation.").showWarning();
            } else {
                Notifications.create()
                        .title("No Academic Term Found")
                        .text("Academic term must be set first\n"
                                + "to proceed adding.").showWarning();
            }
        });

        ssc.whenFailed(() -> {
            Mono.fx().snackbar().showInfo(application_root, "Service is Currently Not Available");
        });

        ssc.whenSuccess(() -> {
            if (operation.equalsIgnoreCase("evaluation")) {
                if (ssc.isEvaluationActive()) {
                    onShowEvaluation();
                } else {
                    // offline
                    Mono.fx().snackbar().showInfo(application_root, "Evaluation Service is Offline");
                }
            } else if (operation.equalsIgnoreCase("adding")) {
                if (ssc.isAddingActive()) {
                    onShowAddingAndChanging();
                } else {
                    // offline
                    Mono.fx().snackbar().showInfo(application_root, "Adding Service is Offline");
                }
            }
        });

        ssc.whenFinished(() -> {
            btn_evaluation.setDisable(false);
            btn_adding.setDisable(false);
        });

        ssc.transact();
    }

    //--------------------------------------------------------------------------
    /**
     * This section is covered to manage the curriculums and academic programs.
     * only the administrator limited to the assistant administrator can apply
     * changes in this section. the local registrar was given permission to view
     * this section to verify the correctness of the data entry. also the local
     * registrar is the only one that can implement curriculums and academic
     * programs upon verification.
     */
    private void onShowAcademicPrograms() {
        //----------------------------------------------------------------------
        if (this.sysWarning()) {
            return;
        }
        //----------------------------------------------------------------------
        if (Access.isDeniedIfNotFrom(Access.ACCESS_ADMIN,
                Access.ACCESS_ASST_ADMIN,
                Access.ACCESS_LOCAL_REGISTRAR)) {
            Mono.fx().snackbar().showInfo(application_root, "You are not allowed to use this feature.");
            return;
        }
        ControllerFX controller = new AcademicHome();
        this.changeRoot(controller,
                "update2.org.cict.layout.academicprogram",
                "academic-home");
    }

    /**
     * Anyone that have login into the system since the access level required is
     * EVALUATOR we assume that the lowest possible user here is EVALUATOR
     * hence, adding an access control here is not necessary. but for assurance
     * and verification we will still do so.
     *
     * This method is not called directly it is called by checks status method.
     * please do not invoke this method directly.
     */
    private void onShowEvaluation() {
        if (Access.isDeniedIfNot(Access.ACCESS_EVALUATOR, true)) {
            Mono.fx().snackbar().showInfo(application_root, "You are not allowed to use this feature.");
            return;
        }

        EvaluateController controller = new EvaluateController();
        this.changeRoot(controller,
                "org.cict.evaluation",
                "evaluation_home");

    }

    /**
     * This method is not called directly it is called by checks status method.
     * please do not invoke this method directly.
     */
    private void onShowAddingAndChanging() {
        if (Access.isDeniedIfNot(Access.ACCESS_EVALUATOR, true)) {
            Mono.fx().snackbar().showInfo(application_root, "You are not allowed to use this feature.");
            return;
        }

        AddingHome controller = new AddingHome();
        this.changeRoot(controller,
                "update.org.cict.layout.adding_changing",
                "adding-changing-home");

    }

    /**
     * Section management is only allowed for the administrator of the system,
     * including assistant administrators.
     */
    private void onShowSectionManagement() {
        //----------------------------------------------------------------------
        if (this.sysWarning()) {
            return;
        }
        //----------------------------------------------------------------------

        /**
         * Only the administrator can access this section.
         */
        if (Access.isDeniedIfNotFrom(
                Access.ACCESS_ADMIN,
                Access.ACCESS_ASST_ADMIN)) {
            Mono.fx().snackbar().showInfo(application_root, "You are not allowed to use this feature.");
            return;
        }

        SectionHomeController controller = new SectionHomeController();
        this.changeRoot(controller,
                "update3.org.cict.layout.sectionmain",
                "sectionHome");

    }

    /**
     * Faculty menu.
     */
    private void onShowFacultyManagement() {
        //----------------------------------------------------------------------
        if (this.sysWarning()) {
            return;
        }
        //----------------------------------------------------------------------
        if (Access.isDeniedIfNotFrom(
                Access.ACCESS_ADMIN,
                Access.ACCESS_ASST_ADMIN)) {
            Mono.fx().snackbar().showInfo(application_root, "You are not allowed to use this feature.");
            return;
        }
        FacultyMainController controller = new FacultyMainController();
        this.changeRoot(controller,
                "org.cict.accountmanager.faculty.layout",
                "faculty-home");
    }

    /**
     * Displays the academic term.
     */
    private void onShowAcademicTerm() {
        //----------------------------------------------------------------------
        if (this.sysWarning()) {
            return;
        }
        //----------------------------------------------------------------------
        if (Access.isDeniedIfNotFrom(
                Access.ACCESS_ADMIN,
                Access.ACCESS_ASST_ADMIN,
                Access.ACCESS_LOCAL_REGISTRAR,
                Access.ACCESS_CO_REGISTRAR)) {
            Mono.fx().snackbar().showInfo(application_root, "You are not allowed to use this feature.");
            return;
        }
        ControllerFX controller = new AcademicTermHome();
        this.changeRoot(controller,
                "update3.org.cict.termcalendar",
                "AcademicTermHome");
    }

    /**
     * When access control button was clicked.
     */
    private void onShowAccessControls() {
        //----------------------------------------------------------------------
        if (this.sysWarning()) {
            return;
        }
        //----------------------------------------------------------------------
        if (Access.isDeniedIfNotFrom(
                Access.ACCESS_ADMIN,
                Access.ACCESS_ASST_ADMIN,
                Access.ACCESS_LOCAL_REGISTRAR,
                Access.ACCESS_CO_REGISTRAR
        )) {
            Mono.fx().snackbar().showInfo(application_root, "You are not allowed to use this feature.");
            return;
        }
        this.showAccessControls();
    }

    /**
     * Display Access Controls
     */
    private AccessManagementHome showAccessControls() {
        AccessManagementHome controller = new AccessManagementHome();
        this.changeRoot(controller,
                "update3.org.cict.access.management",
                "AccessManagementHome");
        return controller;
    }

    //--------------------------------------------------------------------------
    /**
     * Show My Account Options.
     */
    private void onShowMyAccount() {

        //----------------------------------------------------------------------
        if (this.sysWarning()) {
            return;
        }
        //----------------------------------------------------------------------

        ControllerFX controller = new MyAccountHome();
        this.changeRoot(controller,
                "update3.org.cict.my_account",
                "MyAccountHome");

    }

    /**
     * Show Linked Application Settings.
     */
    private void onShowLinkedManagement() {
        //----------------------------------------------------------------------
        if (this.sysWarning()) {
            return;
        }
        //----------------------------------------------------------------------

        if (Access.isDeniedIfNotFrom(Access.ACCESS_ADMIN, Access.ACCESS_ASST_ADMIN, Access.ACCESS_CO_REGISTRAR, Access.ACCESS_LOCAL_REGISTRAR)) {
            Mono.fx().snackbar().showInfo(application_root, "You are not allowed to use this feature.");
            return;
        }

        ControllerFX controller = new LinkedHome();
        this.changeRoot(controller,
                "update4.org.cict.linked_manager",
                "LinkedHome");
    }

    /**
     * Show student menu.
     */
    private void onShowStudentHome() {
        //----------------------------------------------------------------------
        if (this.sysWarning()) {
            return;
        }
        //----------------------------------------------------------------------

        if (Access.isDeniedIfNot(Access.ACCESS_LOCAL_REGISTRAR)) {
            Mono.fx().snackbar().showInfo(application_root, "You are not allowed to use this feature.");
            return;
        }

        ControllerFX controller = new StudentHomeController();
        this.changeRoot(controller,
                "update5.org.cict.student.layout",
                "student-home");

    }

    //--------------------------------------------------------------------------
    /**
     * Show Reports Options.
     */
    private void onShowReports() {

        //----------------------------------------------------------------------
        if (this.sysWarning()) {
            return;
        }
        //----------------------------------------------------------------------

        ControllerFX controller = new ReportsMain();
        this.changeRoot(controller,
                "org.cict.reports",
                "ReportsMain");

    }

    //--------------------------------------------------------------------------
    /**
     * Show Faculty Hub.
     */
    private void onShowFacultyHub() {

        //----------------------------------------------------------------------
        if (this.sysWarning()) {
            return;
        }
        //----------------------------------------------------------------------

        ControllerFX controller = new FacultyHub();
        this.changeRoot(controller,
                "update5.org.cict.facultyhub",
                "FacultyHub");

    }
    
    private boolean isCurrentAcadTermNotSet() {
        return SystemProperties.instance().getCurrentAcademicTerm()==null;
    }
}

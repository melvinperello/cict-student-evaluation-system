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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.cict.GenericLoadingShow;
import org.cict.MainApplication;
import org.cict.ThreadMill;
import org.cict.accountmanager.AccountManager;
import org.cict.accountmanager.Logout;
import org.cict.accountmanager.faculty.FacultyMainController;
import org.cict.evaluation.EvaluateController;
import update.org.cict.controller.adding.AddingHome;
import update2.org.cict.controller.academicprogram.AcademicHome;
import update3.org.cict.access.Access;
import update3.org.cict.access.management.AccessManagementHome;
import update3.org.cict.controller.sectionmain.SectionHomeController;
import update3.org.cict.my_account.MyAccountHome;
import update3.org.cict.termcalendar.AcademicTermHome;
import update4.org.cict.linked_manager.LinkedHome;
import update5.org.cict.student.controller.StudentHomeController;

/**
 *
 * @author Jhon Melvin
 */
public class SystemHome extends MonoLauncher {

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
        //----------------------------------------------------------------------

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
    }

    @Override
    public void onDelayedStart() {
        this.application_root = this.getApplicationRoot();
    }

    //--------------------------------------------------------------------------
    private Pane application_root;
    private static Stage APPLICATION_STAGE;
    public final static String SCENE_TRANSITION_COLOR = "#414852";

    public static void launchHome() {
        SystemHome homeFx = M.load(SystemHome.class);
        Stage mainStage = homeFx.createStageApplication();
        mainStage.setMinWidth(1024);
        mainStage.setMinHeight(700);
        mainStage.setMaximized(true);
        mainStage.show();
        homeFx.onDelayedStart();
        homeFx.onStageClosing();
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

    private void onLogout() {
        int res = Mono.fx().alert()
                .createConfirmation()
                .setHeader("Logout Account")
                .setTitle("Confirmation")
                .setMessage("Are you sure you want to logout this account?")
                .confirmYesNo();
        if (res == 1) {
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
                APPLICATION_STAGE.close();
                // relaunch the login screen.
                relaunchLogin();

            });
            logout.setRestTime(300);
            logout.transact();
        }
    }

    private void relaunchLogin() {
        MainApplication.launchLogin();
    }

    //--------------------------------------------------------------------------
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
        AcademicTermHome.ServiceStatusChecker ssc = new AcademicTermHome().new ServiceStatusChecker();
        ssc.whenStarted(() -> {
            btn_evaluation.setDisable(true);
            btn_adding.setDisable(true);
        });

        ssc.whenCancelled(() -> {
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

            } else if (operation.equalsIgnoreCase("encoding")) {
                // 
            }
        });

        ssc.whenFinished(() -> {
            btn_evaluation.setDisable(false);
            btn_adding.setDisable(false);
        });

        ssc.transact();
    }

    //--------------------------------------------------------------------------
    // events
    /**
     * This section is covered to manage the curriculums and academic programs.
     * only the administrator limited to the assistant administrator can apply
     * changes in this section. the local registrar was given permission to view
     * this section to verify the correctness of the data entry. also the local
     * registrar is the only one that can implement curriculums and academic
     * programs upon verification.
     */
    private void onShowAcademicPrograms() {

        if (Access.isDeniedIfNotFrom(Access.ACCESS_ADMIN,
                Access.ACCESS_ASST_ADMIN,
                Access.ACCESS_LOCAL_REGISTRAR)) {
            Mono.fx().snackbar().showInfo(application_root, "You are not allowed to use this feature.");
            return;
        }

        /**
         * Since AcademicProgramHome implements ControllerFX it is said that it
         * will inherit it as an interface and not as an direct child. therefore
         * it will also be classified under the ControllerFX interface. also it
         * is a Direct Child of SceneFX therefore you can also use:
         * <pre>
         *      SceneFX fx = new AcademicProgramHome();
         *      or the most common you can declare iself as the instance of its own class:
         *      AcademicProgramHome controller = new AcademicProgramHome();
         *      In this case it both reflects and uses the traits of both
         *      SceneFX and ControllerFX
         * </pre>
         *
         */
//        ControllerFX controller = new AcademicProgramHome();
//        this.changeRoot(controller,
//                "update2.org.cict.layout.academicprogram",
//                "academic-program-home");
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
     * Same as adding and changing.
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
        /**
         * Only the administrator can access this section.
         */
        if (Access.isDeniedIfNotFrom(Access.ACCESS_ADMIN, Access.ACCESS_ASST_ADMIN)) {
            Mono.fx().snackbar().showInfo(application_root, "You are not allowed to use this feature.");
            return;
        }

        SectionHomeController controller = new SectionHomeController();
        this.changeRoot(controller,
                "update3.org.cict.layout.sectionmain",
                "sectionHome");

    }

    private void onShowFacultyManagement() {
        /**
         * Only the administrator can access this section. including the
         * assistant administrator.
         */
        if (Access.isDeniedIfNotFrom(Access.ACCESS_ADMIN, Access.ACCESS_ASST_ADMIN)) {
            Mono.fx().snackbar().showInfo(application_root, "You are not allowed to use this feature.");
            return;
        }

        FacultyMainController controller = new FacultyMainController();
        this.changeRoot(controller,
                "org.cict.accountmanager.faculty.layout",
                "faculty-home");

    }

    private void onShowAcademicTerm() {
        if (Access.isDeniedIfNotFrom(Access.ACCESS_ADMIN, Access.ACCESS_ASST_ADMIN, Access.ACCESS_LOCAL_REGISTRAR)) {
            Mono.fx().snackbar().showInfo(application_root, "You are not allowed to use this feature.");
            return;
        }

        ControllerFX controller = new AcademicTermHome();
        this.changeRoot(controller,
                "update3.org.cict.termcalendar",
                "AcademicTermHome");

    }

    private void onShowAccessControls() {
        if (Access.isDeniedIfNotFrom(Access.ACCESS_ADMIN, Access.ACCESS_ASST_ADMIN, Access.ACCESS_LOCAL_REGISTRAR)) {
            Mono.fx().snackbar().showInfo(application_root, "You are not allowed to use this feature.");
            return;
        }

        ControllerFX controller = new AccessManagementHome();
        this.changeRoot(controller,
                "update3.org.cict.access.management",
                "AccessManagementHome");

    }

    private void onShowMyAccount() {

        ControllerFX controller = new MyAccountHome();
        this.changeRoot(controller,
                "update3.org.cict.my_account",
                "MyAccountHome");

    }

    private void onShowLinkedManagement() {
        ControllerFX controller = new LinkedHome();
        this.changeRoot(controller,
                "update4.org.cict.linked_manager",
                "LinkedHome");
    }

    private void onShowStudentHome() {
        ControllerFX controller = new StudentHomeController();
        this.changeRoot(controller,
                "update5.org.cict.student.layout",
                "student-home");

    }

}

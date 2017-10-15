package update.org.cict.controller.home;

import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import com.jhmvin.transitions.Animate;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.cict.MainApplication;
import org.cict.GenericLoadingShow;
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

public class Home extends SceneFX implements ControllerFX {

    @FXML
    private AnchorPane application_root;

    @FXML
    private Button btn_evaluation;

    @FXML
    private Button btn_adding;

    @FXML
    private Button btn_academic_programs;

    @FXML
    private Button btn_section;

    @FXML
    private Button btn_faculty;

    @FXML
    private JFXButton btn_student;

    @FXML
    private JFXButton btn_access_controls;

    @FXML
    private JFXButton btn_academic_term;

    @FXML
    private JFXButton btn_logout;

    @FXML
    private JFXButton btn_linked;

    @FXML
    private JFXButton btn_my_account;

    public Home() {
        //
    }
    
    public final static String SCENE_TRANSITION_COLOR = "#414852";
 
    @Override
    public void onInitialization() {
        /**
         * Never forget this it is the most important part of SceneFX.
         */
        this.bindScene(application_root);
    }

    /**
     * Launches the main stage for the first time. this will be the stage of the
     * entire application upon its whole life cycle. it will also use the same
     * Scene across all windows.
     */
    public static void launchApp() {
        Home controller = new Home();
        Mono.fx().create()
                .setPackageName("update.org.cict.layout.home")
                .setFxmlDocument("home")
                .makeFX()
                .setController(controller)
                .makeScene()
                .makeStageApplication()
                .stageMinDimension(1024, 700)
                .stageMaximized(true)
                .stageShow();

        /**
         * Adds stage close request.
         */
        controller.onStageClosing();
    }

    private static Stage APPLICATION_STAGE;

    /**
     * Closing event for this stage.
     */
    private void onStageClosing() {
        APPLICATION_STAGE = super.getStage();
        APPLICATION_STAGE.setOnCloseRequest(onClose -> {
            this.onLogout();
            onClose.consume();
        });
    }

    /**
     * Allows all stages outside home to call this method and return to Home
     * Root. this will re-create the home root.
     *
     * @param scene
     */
    public static void callHome(SceneFX scene) {
        Home controller = new Home();
        Pane home_root = Mono.fx().create()
                .setPackageName("update.org.cict.layout.home")
                .setFxmlDocument("home")
                .makeFX()
                .setController(controller)
                .pullOutLayout();
        // revert back this color will be the background of the scene
        // when replacing a root this color will be visible upon transitions
        scene.setSceneColor(SCENE_TRANSITION_COLOR);
        Animate.fade(scene.getApplicationRoot(), 150, () -> {
            scene.replaceRoot(home_root);
        }, home_root);
    }

    @Override
    public void onEventHandling() {

        super.addClickEvent(btn_evaluation, () -> {
            checkStatus("evaluation", () -> {
                onShowEvaluation();
            });
        });

        super.addClickEvent(btn_adding, () -> {
            checkStatus("adding", () -> {
                onShowAddingAndChanging();
            });
        });

        super.addClickEvent(btn_academic_programs, () -> {
            this.onShowAcademicPrograms();
        });

        super.addClickEvent(btn_section, () -> {
            this.onShowSectionManagement();
        });

        super.addClickEvent(btn_faculty, () -> {
            this.onShowFacultyManagement();
        });

        super.addClickEvent(btn_logout, () -> {
            onLogout();
        });

        super.addClickEvent(btn_academic_term, () -> {
            onShowAcademicTerm();
        });

        super.addClickEvent(btn_access_controls, () -> {
            onShowAccessControls();
        });

        super.addClickEvent(btn_my_account, () -> {
            onShowMyAccount();
        });

        super.addClickEvent(btn_linked, () -> {
            onShowLinkedManagement();
        });

    }

    private void changeRoot(ControllerFX controller, String packer, String fxml) {

        Pane fxRoot = Mono.fx().create()
                .setPackageName(packer)
                .setFxmlDocument(fxml)
                .makeFX()
                .setController(controller)
                .pullOutLayout();

        super.setSceneColor(SCENE_TRANSITION_COLOR);
        Animate.fade(application_root, 150, () -> {
            super.replaceRoot(fxRoot);
        }, fxRoot);
    }

    /**
     * There is no user access for logout everyone can logout ofcourse.
     */
    public void onLogout() {
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

    /**
     * This is just a sample function to show the different uses of isDenied
     * methods.
     */
    private void accessSamples() {
        /**
         * If the user don't have system level of access it will return true.
         */
        Access.isDeniedIfNot(Access.ACCESS_SYSTEM);

        /**
         * This will return true if the user is not from any access level given
         * below. this can accept multiple parameters.
         */
        Access.isDeniedIfNotFrom(Access.ACCESS_ADMIN,
                Access.ACCESS_EVALUATOR,
                Access.ACCESS_SYSTEM);

        /**
         * Any user with lower access level than the prescribed level. this will
         * return true. take note that the higher the access level the less
         * integer value it possess. any access level above evaluator this will
         * return false hence executing the preceeding codes.
         *
         */
        Access.isDeniedIfNot(Access.ACCESS_EVALUATOR, true);

        /**
         * You can also use the isGranted method.
         */
        Access.isGranted(Access.ACCESS_EVALUATOR);
    }

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

}

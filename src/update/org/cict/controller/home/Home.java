package update.org.cict.controller.home;

import com.jhmvin.Mono;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import org.cict.GenericLoadingShow;
import org.cict.accountmanager.AccountManager;
import org.cict.accountmanager.Logout;
import org.cict.accountmanager.faculty.FacultyMainController;
import update3.org.cict.access.Access;
import update3.org.cict.controller.sectionmain.SectionHomeController;

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

    @Override
    public void onInitialization() {
        this.bindScene(application_root);
    }

    /**
     * This is a static method to call home in other stages.
     */
    public static void callHome() {
        Mono.fx().create()
                .setPackageName("update.org.cict.layout.home")
                .setFxmlDocument("home")
                .makeFX()
                .makeScene()
                .makeStageApplication()
                .stageMaximized(false)
                .stageShow();
    }

    @Override
    public void onEventHandling() {

        super.addClickEvent(btn_evaluation, () -> {
            this.onShowEvaluation();
        });

        super.addClickEvent(btn_adding, () -> {
            this.onShowAddingAndChanging();
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
    }

    public void onLogout() {
        int res = Mono.fx().alert()
                .createConfirmation()
                .setHeader("Logout Account")
                .setTitle("Confirmation")
                .setMessage("Are you sure you want to logout this account?")
                .confirmYesNo();
        if (res == 1) {
            Logout logout = AccountManager.instance().createLogout();
            logout.setOnStart(onStart -> {
                GenericLoadingShow.instance().show();
            });
            logout.setOnSuccess(onSuccess -> {
                GenericLoadingShow.instance().hide();
                //close session
                Mono.orm().shutdown();
                //system exit
                Runtime.getRuntime().halt(0);
            });
            logout.setRestTime(300);
            logout.transact();
        }
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

        super.finish();
        Mono.fx().create()
                .setPackageName("update2.org.cict.layout.academicprogram")
                .setFxmlDocument("academic-program-home")
                .makeFX()
                .makeScene()
                .makeStageApplication()
                .stageMinDimension(1024, 700)
                .stageMaximized(true)
                .stageShow();
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

        super.finish();
        Mono.fx().create()
                .setPackageName("org.cict.evaluation")
                .setFxmlDocument("evaluation_home")
                .makeFX()
                .makeScene()
                .makeStageApplication()
                .stageMinDimension(1024, 700)
                .stageMaximized(true)
                .stageShow();
    }

    /**
     * Same as adding and changing.
     */
    private void onShowAddingAndChanging() {
        super.finish();
        Mono.fx().create()
                .setPackageName("update.org.cict.layout.adding_changing")
                .setFxmlDocument("adding-changing-home")
                .makeFX()
                .makeScene()
                .makeStageApplication()
                .stageMinDimension(1024, 700)
                .stageMaximized(true)
                .stageShow();
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

        super.finish(); // close the stage
        SectionHomeController controller = new SectionHomeController();
        Mono.fx().create()
                .setPackageName("update3.org.cict.layout.sectionmain")
                .setFxmlDocument("sectionHome")
                .makeFX()
                .setController(controller)
                .makeScene()
                .makeStageApplication()
                .stageMinDimension(1024, 700)
                .stageMaximized(true)
                .stageShow();
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

        super.finish();
        FacultyMainController controller = new FacultyMainController();
        Mono.fx().create()
                .setPackageName("org.cict.accountmanager.faculty")
                .setFxmlDocument("faculty-main")
                .makeFX()
                .setController(controller)
                .makeScene()
                .makeStageApplication()
                .stageMinDimension(1024, 700)
                .stageMaximized(true)
                .stageShow();
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
}

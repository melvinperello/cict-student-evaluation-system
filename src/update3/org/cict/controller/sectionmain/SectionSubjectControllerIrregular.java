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
package update3.org.cict.controller.sectionmain;

import app.lazy.models.Database;
import app.lazy.models.LoadGroupMapping;
import app.lazy.models.LoadSectionMapping;
import app.lazy.models.SubjectMapping;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.LayoutDataFX;
import com.jhmvin.fx.display.SceneFX;
import com.jhmvin.transitions.Animate;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.cict.authentication.authenticator.SystemProperties;
import update3.org.cict.SectionConstants;
import update3.org.cict.controller.sectionmain.SectionSubjectsController.FetchSchedule;
import update3.org.cict.layout.default_loader.LoaderView;
import update3.org.cict.window_prompts.empty_prompt.EmptyView;

/**
 *
 * @author Jhon Melvin
 */
public class SectionSubjectControllerIrregular extends SceneFX implements ControllerFX {

    private void sout(Object messaage) {
        System.out.println(this.getClass().getSimpleName() + ": " + messaage.toString());
    }

    @FXML
    private VBox application_root;

    @FXML
    private Label lbl_header_class;

    @FXML
    private JFXButton btn_back;

    @FXML
    private Label lbl_semester;

    @FXML
    private Label lbl_college_owner;

    @FXML
    private VBox vbox_single;

    @FXML
    private Label lbl_section_name;

    @FXML
    private TextField txt_section_name;

    @FXML
    private TextField txt_instructor;

    @FXML
    private JFXButton btn_save_changes;

    @FXML
    private VBox vbox_conjunction_tip;

    @FXML
    private Label lbl_section_name1;

    @FXML
    private Label lbl_section_name11;

    @FXML
    private Label lbl_section_name111;

    @FXML
    private JFXButton btn_save_changes1;

    @FXML
    private VBox vbox_view_schedule;

    @FXML
    private Label lbl_subject_code_top;

    @FXML
    private StackPane stack_schedules;

    @FXML
    private VBox vbox_schedule;

    @FXML
    private JFXButton lbl_add_schedule;

    @FXML
    private JFXButton btn_export;

    @FXML
    private Label lbl_student_count;

    @FXML
    private Label lbl_instructor_big;

    public SectionSubjectControllerIrregular() {
        //
    }

    /**
     * Variables from WinIrregularSectionController.
     */
    private LayoutDataFX winIrregularSectionFx;
    private LoadGroupMapping loadGroup;
    private LoadSectionMapping loadSection;
    private String instructorName;
    private String studentCount;

    public void setWinIrregularSectionFx(LayoutDataFX winIrregularSectionFx) {
        this.winIrregularSectionFx = winIrregularSectionFx;
    }

    public void setLoadGroup(LoadGroupMapping loadGroup) {
        this.loadGroup = loadGroup;
    }

    public void setLoadSection(LoadSectionMapping loadSection) {
        this.loadSection = loadSection;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public void setStudentCount(String studentCount) {
        this.studentCount = studentCount;
    }

    /**
     * Attachments
     */
    private EmptyView emptySchedules;

    @Override
    public void onInitialization() {
        super.bindScene(application_root);
        /**
         * Empty View.
         */

        this.emptySchedules = new EmptyView(stack_schedules);
        this.emptySchedules.getButton().setText("Refresh");
        super.addClickEvent(this.emptySchedules.getButton(), () -> {
            this.emptySchedules.detach();
            this.fetchSchedules();
        });

        if (!this.loadSection.getType().equalsIgnoreCase(SectionConstants.CONJUNCTION)) {
            vbox_conjunction_tip.setVisible(false);
            lbl_college_owner.setVisible(false);
        }

        this.reinitialize();
    }

    private SubjectMapping loadGroupSubject;

    public void reinitialize() {
        loadGroupSubject = Database.connect()
                .subject()
                .getPrimary(this.loadGroup.getSUBJECT_id());
        /**
         * Set Section Name.
         */
        this.lbl_section_name.setText(loadSection.getSection_name());
        this.lbl_subject_code_top.setText(loadGroupSubject.getCode());
        this.lbl_instructor_big.setText(this.instructorName);
        this.lbl_student_count.setText(this.studentCount);
        this.lbl_semester.setText(SystemProperties.instance().getCurrentTermString());
        /**
         * Set College Information.
         */
        String college = "";
        for (Character c : this.loadSection.getCollege().toCharArray()) {
            college += c.toString() + "  ";
        }
        this.lbl_college_owner.setText(college);
        this.lbl_header_class.setText("Special Class");

        /**
         * Fetch Schedules.
         */
        this.fetchSchedules();
    }

    @Override
    public void onEventHandling() {
        super.addClickEvent(btn_back, () -> {
            Animate.fade(this.application_root, SectionConstants.FADE_SPEED, () -> {
                super.replaceRoot(winIrregularSectionFx.getApplicationRoot());
            }, winIrregularSectionFx.getApplicationRoot());
            /**
             *
             */
            this.emptySchedules.detach();
        });

    }

    //--------------------------------------------------------------------------
    /**
     * Uses inner-class of SectionSubjectsController to fetch schedules.
     */
    private void fetchSchedules() {
        SectionSubjectsController regularSections = new SectionSubjectsController();
        SectionSubjectsController.FetchSchedule scheduleTx;
        scheduleTx = regularSections.new FetchSchedule();

        scheduleTx.loadGroup = this.loadGroup;
        scheduleTx.whenCancelled(() -> {
            this.emptySchedules.attach();
            sout("No Schedule");
        });
        scheduleTx.whenSuccess(() -> {
            sout("Finished Fetching Schedules");
        });
        scheduleTx.whenFailed(() -> {
            sout("Schedule Failed.");
        });
        scheduleTx.whenFinished(() -> {
            sout("Fetching Schedules Finished");
        });

        scheduleTx.transact();
    }
}

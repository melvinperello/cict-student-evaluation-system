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

import app.lazy.models.AcademicProgramMapping;
import app.lazy.models.CurriculumMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.FacultyMapping;
import app.lazy.models.LoadGroupMapping;
import app.lazy.models.LoadGroupScheduleMapping;
import app.lazy.models.LoadSectionMapping;
import app.lazy.models.MapFactory;
import app.lazy.models.SubjectMapping;
import com.izum.fx.textinputfilters.StringFilter;
import com.izum.fx.textinputfilters.TextInputFilters;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.SimpleTask;
import com.jhmvin.fx.async.Transaction;
import com.jhmvin.fx.controls.MonoText;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.LayoutDataFX;
import com.jhmvin.fx.display.SceneFX;
import com.jhmvin.orm.Searcher;
import com.jhmvin.transitions.Animate;
import com.melvin.mono.fx.bootstrap.M;
import com.melvin.mono.fx.events.MonoClick;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.lang3.text.WordUtils;
import org.cict.authentication.authenticator.CollegeFaculty;
import update3.org.cict.ChoiceRange;
import update3.org.cict.scheduling.ScheduleConstants;
import update3.org.cict.SectionConstants;
import update3.org.cict.controller.sectionmain.deltesection.DeleteSectionTransaction;
import update3.org.cict.scheduling.OpenScheduleViewer;
import update3.org.cict.scheduling.ScheduleChecker;
import update3.org.cict.window_prompts.empty_prompt.EmptyView;
import update3.org.excelprinter.StudentMasterListPrinter;
import update3.org.facultychooser.FacultyChooser;

/**
 *
 * This class is used in both regular and irregular sections.
 *
 * @author Jhon Melvin
 */
public class SectionSubjectsController extends SceneFX implements ControllerFX {

    @FXML
    private VBox application_root;

    @FXML
    private Label lbl_window_header;

    @FXML
    private JFXButton btn_back;

    @FXML
    private HBox hbox_reg_sub;

    @FXML
    private Label lbl_semester;

    @FXML
    private Label lbl_curriculum;

    @FXML
    private Label lbl_curriculum_type;

    @FXML
    private HBox hbox_irregu_sub;

    @FXML
    private Label lbl_irregular_semester;

    @FXML
    private Label lbl_college_owner;

    @FXML
    private VBox vbox_regular_menu;

    @FXML
    private Label lbl_section_name;

    @FXML
    private TextField txt_year_level;

    @FXML
    private TextField txt_section_name;

    @FXML
    private TextField txt_section_group;

    @FXML
    private TextField txt_adviser;

    @FXML
    private JFXButton btn_delete_section;

    @FXML
    private JFXButton btn_save_changes;

    @FXML
    private VBox vbox_irregular_menu;

    @FXML
    private Label lbl_irregular_section_name;

    @FXML
    private TextField txt_irregular_section_name;

    @FXML
    private Label lbl_irregular_instructor;

    @FXML
    private JFXButton btn_irregular_delete;

    @FXML
    private JFXButton btn_irregular_save;

    @FXML
    private VBox vbox_conjunction_tip;

    @FXML
    private JFXButton btn_i_change_college;

    @FXML
    private VBox vbox_special_tip;

    @FXML
    private VBox vbox_view_subjects;

    @FXML
    private VBox vbox_subjects;

    @FXML
    private VBox vbox_view_schedule;

    @FXML
    private Label lbl_subject_code_top;

    @FXML
    private StackPane stack_schedules;

    @FXML
    private VBox vbox_schedule;

    @FXML
    private JFXButton btn_view_add_schedule;

    @FXML
    private JFXButton btn_view_class_sched;

    @FXML
    private JFXButton btn_export;

    @FXML
    private Label lbl_student_count;

    @FXML
    private Label lbl_instructor_big;

    @FXML
    private Label btn_change_instructor;

    @FXML
    private JFXButton btn_back_to_subject;

    @FXML
    private VBox vbox_add_schedule;

    @FXML
    private Label lbl_subject_code_top_addsched;

    @FXML
    private ComboBox<String> cmb_sched_day;

    @FXML
    private ComboBox<String> cmb_sched_start;

    @FXML
    private ComboBox<String> cmb_sched_end;

    @FXML
    private TextField txt_sched_room;

    @FXML
    private JFXButton btn_add_sched;

    @FXML
    private JFXButton btn_cancel_sched;

    public SectionSubjectsController() {
        /**
         * Every code below this constructor was passed from another controller.
         */
    }

    private LayoutDataFX dataFx;

    public void setDataFx(LayoutDataFX winRegularSectionControllerFx) {
        this.dataFx = winRegularSectionControllerFx;
    }

    /**
     * Use the modify the window header, since this will be use in every type of
     * sections.
     *
     * @param header
     */
    public void setWindowHeader(String header) {
        lbl_window_header.setText(header + " Class");
        // display college

        if (header.equalsIgnoreCase(SectionConstants.CONJUNCTION)) {
            lbl_college_owner.setVisible(true);
            vbox_conjunction_tip.setVisible(true);
        } else {
            // if not conjunction and not regular
            // display special tip
            if (!header.equalsIgnoreCase(SectionConstants.REGULAR)) {
                vbox_special_tip.setVisible(true);
            }
        }

    }

    /**
     * Passed Variables from WinRegularSectionController.class
     */
    private AcademicProgramMapping academicProgramMap;
    private CurriculumMapping curriculumMap;
    private String currentTermString;
    private String curriculumType;
    private String sectionName;
    private LoadSectionMapping sectionMap;

    public void setSectionMap(LoadSectionMapping sectionMap) {
        this.sectionMap = sectionMap;
    }

    public void setAcademicProgramMap(AcademicProgramMapping academicProgramMap) {
        this.academicProgramMap = academicProgramMap;
    }

    public void setCurriculumMap(CurriculumMapping curriculumMap) {
        this.curriculumMap = curriculumMap;
    }

    public void setCurrentTermString(String currentTermString) {
        this.currentTermString = currentTermString;
    }

    public void setCurriculumType(String curriculumType) {
        this.curriculumType = curriculumType;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    /**
     * Attachments
     */
    private EmptyView scheduleEmpty;

    /**
     * Initialize everything here.
     *
     *
     *
     */
    @Override
    public void onInitialization() {
        super.bindScene(application_root);
        /**
         * Attachments.
         */
        this.scheduleEmpty = new EmptyView(stack_schedules);

        //
        lbl_college_owner.setVisible(false);
        vbox_conjunction_tip.setVisible(false);
        vbox_special_tip.setVisible(false);
        // hide dependent components
        vbox_irregular_menu.setVisible(false);
        vbox_conjunction_tip.setVisible(false);
        vbox_regular_menu.setVisible(false);
        hbox_irregu_sub.setVisible(false);
        hbox_reg_sub.setVisible(false);

        // semester display
        this.lbl_semester.setText(currentTermString);
        this.lbl_irregular_semester.setText(currentTermString);
        // if there are assigned academic program and curriculum
        // this is a regular section
        if (this.academicProgramMap != null && this.curriculumMap != null) {
            vbox_regular_menu.setVisible(true);
            hbox_reg_sub.setVisible(true);
            regularSectionInit();
        } else {
            vbox_irregular_menu.setVisible(true);
            hbox_irregu_sub.setVisible(true);
            irregularSectionInit();
        }

        /**
         * Default view.
         */
        this.vbox_add_schedule.setVisible(false);
        this.vbox_view_schedule.setVisible(false);
        this.vbox_view_subjects.setVisible(true);

        fetchSectionSubject();
        //loadSchedule();
        this.initScheduler();

    }

    /**
     * initialization routine if there are academic program and curriculum.
     */
    private void regularSectionInit() {

        // add filters in text fields
        SectionCreateWizard.addTextFilters(curriculumMap,
                txt_year_level,
                txt_section_name,
                txt_section_group);
        // set values in displays

        this.lbl_curriculum.setText(curriculumMap.getName());
        this.lbl_curriculum_type.setText(curriculumType);
        //
        this.lbl_section_name.setText(sectionName);

        // initial text field values
        txt_year_level.setText(sectionMap.getYear_level().toString());
        txt_section_name.setText(sectionMap.getSection_name());
        txt_section_group.setText(sectionMap.get_group().toString());
    }

    /**
     * Only the conjunction classes will display college owner.
     */
    private void displayCollege() {
        try {
            String college = "";
            for (Character c : this.sectionMap.getCollege().toCharArray()) {
                college += c.toString() + "  ";
            }
            lbl_college_owner.setText(college);
        } catch (Exception e) {
            System.err.println("CANNOT DISPLAY COLLEGE OWNER");
            lbl_college_owner.setVisible(false);
        }
    }

    /**
     * Initialize irregular sections.
     */
    private void irregularSectionInit() {
        // set labels

        lbl_irregular_section_name.setText(sectionName);
        this.displayCollege();
        // default values
        txt_irregular_section_name.setText(sectionName);
        TextInputFilters.string()
                .setMaxCharacters(50)
                .setNoLeadingTrailingSpaces(false)
                .setFilterMode(StringFilter.LETTER_DIGIT_SPACE)
                .setTextSource(txt_irregular_section_name)
                .applyFilter();

    }

    private ArrayList<String> timeTable12;

    private void initScheduler() {
        this.cmb_sched_day.getItems().clear();
        this.cmb_sched_day.getItems().addAll(ScheduleConstants.getDayList());
        this.cmb_sched_day.getSelectionModel().selectFirst();
        // call once.
        this.timeTable12 = ScheduleConstants.getTimeLapsePretty();
        // add limits to time combo box.
        ChoiceRange.setComboBoxLimitTime(timeTable12, cmb_sched_start, cmb_sched_end);
    }

    private void fetchSectionSubject() {
        FetchSubjects subjectsTx = new FetchSubjects();
        subjectsTx.sectionMap = sectionMap;

        subjectsTx.whenSuccess(() -> {
            loadSubjects(subjectsTx.sectionSubjects);
        });
        subjectsTx.transact();
    }

    @Override
    public void onEventHandling() {
        // back to sections
        super.addClickEvent(btn_back, () -> {
            onBackPressed();
        });
        // back to subjects
        super.addClickEvent(btn_back_to_subject, () -> {
            onBackToSubjectListFromSchedule();
        });
        // switch to schedule view.
        super.addClickEvent(btn_view_add_schedule, () -> {
            showScheduleAdding(false);
        });
        // back grom schedule view
        super.addClickEvent(btn_cancel_sched, () -> {
            onBackFromScheduleCreation();
        });
        // switch to add schedule view.
        super.addClickEvent(btn_add_sched, () -> {
            if (!btn_add_sched.getText().equalsIgnoreCase("SAVE")) {
                onAddSchedule();
            } else {
                onEditSchedule();
            }
        });
        // open schedule viewer
        super.addClickEvent(btn_view_class_sched, () -> {
            /**
             * Open Schedule Viewer.
             */
            openScheduleViewer();
        });

        // save changes from section information
        super.addClickEvent(btn_save_changes, () -> {

        });

        super.addClickEvent(btn_irregular_save, () -> {

        });

        // delete section
        super.addClickEvent(btn_delete_section, () -> {
            onDeleteSection();
        });
        // delete irregular section
        super.addClickEvent(btn_irregular_delete, () -> {
            onDeleteSection();
        });

        // change college
        super.addClickEvent(btn_i_change_college, () -> {
            this.onChangeCollege();
        });
        // export student master list
        super.addClickEvent(btn_export, () -> {
            exportToExcel();
        });

        super.addClickEvent(txt_adviser, () -> {
            FacultyMapping selected_faculty = selectFaculty();
            if (selected_faculty != null) {
                txt_adviser.setText(selected_faculty.getLast_name() + ", " + selected_faculty.getFirst_name());
            } else {
                txt_adviser.setText("");
            }
        });

    }

    private FacultyMapping selectFaculty() {
        FacultyChooser facultyChooser = M.app().restore(FacultyChooser.class);
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
        return selectedFaculty;
    }

    private void openScheduleViewer() {

        String sectionString = "";
        AcademicProgramMapping coursecode = null;
        try {
            if (sectionMap.getACADPROG_id() != null) {
                coursecode = Database.connect().academic_program()
                        .getPrimary(sectionMap.getACADPROG_id());
            }
        } catch (Exception e) {
            coursecode = null;
        }

        if (coursecode != null) {
            try {
                sectionString = coursecode.getCode() + " " + sectionMap.getYear_level()
                        + sectionMap.getSection_name() + "-G" + sectionMap.get_group();
            } catch (Exception e) {
                sectionString = sectionMap.getSection_name();
            }
        } else {
            sectionString = sectionMap.getSection_name();
        }

        OpenScheduleViewer.openScheduleViewer(sectionMap, lbl_semester.getText(), sectionString);
    }

    private void exportToExcel() {
        SubjectMasterListTransaction exportTx = new SubjectMasterListTransaction();
        exportTx.setId(this.selected_load_group_in_sections.getId());

        exportTx.whenStarted(() -> {
            super.cursorWait();
            this.btn_export.setDisable(true);
        });

        exportTx.whenSuccess(() -> {
            SimpleTask openTask = new SimpleTask("open-excel");
            openTask.setTask(() -> {
                String fileName = this.currentTermString + " "
                        + this.sectionName
                        + " "
                        + this.lbl_subject_code_top.getText();
                boolean printed = StudentMasterListPrinter.export(fileName, exportTx.getStudentData());
                if (!printed) {
                    Mono.fx().snackbar().showError(application_root, "Unable to open Excel File");
                }
            });
            openTask.start();

        });

        exportTx.whenCancelled(() -> {
            // no results
            Mono.fx().snackbar().showError(application_root, "No Data to Export");
        });

        exportTx.whenFailed(() -> {
            // no callback
        });

        exportTx.whenFinished(() -> {
            super.cursorDefault();
            this.btn_export.setDisable(false);
        });
        exportTx.transact();
    }

    /**
     * Change College.
     */
    private void onChangeCollege() {
        String selected = update3.org.collegechooser.ChooserHome.open();
        if (selected.equalsIgnoreCase("CANCEL")) {
            return;
        }

        try {
            LoadSectionMapping loadSection = Database.connect().load_section()
                    .getPrimary(this.sectionMap.getId());
            loadSection.setCollege(selected);
            boolean college_updated = Database.connect().load_section().update(loadSection);
            if (college_updated) {
                Mono.fx().snackbar().showSuccess(application_root, "Successfully Updated");
                this.sectionMap = loadSection;
                this.displayCollege();
            } else {
                Mono.fx().snackbar().showError(application_root, "Failed to Update");
            }
        } catch (Exception e) {
            Mono.fx().snackbar().showError(application_root, "Failed to Update");
        }
    }

    private void showScheduleAdding(boolean edit) {

        Animate.fade(vbox_view_schedule, SectionConstants.FADE_SPEED, () -> {
            this.vbox_add_schedule.setVisible(true);
            this.vbox_view_schedule.setVisible(false);
            this.vbox_view_subjects.setVisible(false);
        }, vbox_add_schedule);

        if (edit) {
            btn_add_sched.setText("Save");

            cmb_sched_day.getSelectionModel().select(WordUtils.capitalizeFully(this.selectedSchedForEditting.getClass_day()));
            int start_index = ScheduleConstants.getTimeLapse().indexOf(this.selectedSchedForEditting.getClass_start());
            int end_index = ScheduleConstants.getTimeLapse().indexOf(this.selectedSchedForEditting.getClass_end());
            cmb_sched_start.getSelectionModel().select(start_index);
            cmb_sched_end.getSelectionModel().select(end_index - 4);
            txt_sched_room.setText(this.selectedSchedForEditting.getClass_room());
        } else {
            // not editting
            cmb_sched_day.getSelectionModel().selectFirst();
            cmb_sched_start.getSelectionModel().selectFirst();
            cmb_sched_end.getSelectionModel().selectFirst();
            txt_sched_room.setText("");
            btn_add_sched.setText("Add Schedule");
        }

    }

    private void onBackFromScheduleCreation() {
        Animate.fade(vbox_add_schedule, SectionConstants.FADE_SPEED, () -> {
            this.vbox_add_schedule.setVisible(false);
            this.vbox_view_schedule.setVisible(true);
            this.vbox_view_subjects.setVisible(false);
        }, vbox_view_schedule);
        /**
         * Canceling the creation of schedule will refresh the schedules.
         */
        fetchSchedules(selected_load_group_in_sections);
    }

    /**
     * Back to Section List.
     */
    private void onBackPressed() {
        Animate.fade(this.application_root, SectionConstants.FADE_SPEED, () -> {
            super.replaceRoot(dataFx.getApplicationRoot());
        }, dataFx.getApplicationRoot());
        /**
         * Back To Section List. Detach empty if ever the section was empty
         */
        this.scheduleEmpty.detach();

        // on back pressed callbacks
        if (this.academicProgramMap != null && this.curriculumMap != null) {
            dataFx.<WinRegularSectionsController>getController()
                    .fetchSections();
        } else {
            dataFx.<WinIrregularSectionsController>getController()
                    .fetchSections();
        }

    }

    private void onDeleteSection() {
        int confirm = Mono.fx()
                .alert()
                .createConfirmation()
                .setTitle("Confirmation")
                .setHeader("Delete This Section ?")
                .setMessage("Are you sure to delete this section. it will also subject group and its schedules, do you want to continue ?")
                .confirmYesNo();

        if (confirm != 1) {
            return;
        }

        DeleteSectionTransaction dst = new DeleteSectionTransaction();
        dst.setLoadSectionID(this.sectionMap.getId());
        dst.whenStarted(() -> {
            btn_delete_section.setDisable(true);
        });

        dst.whenSuccess(() -> {
            onBackPressed();
            Mono.fx().snackbar().showSuccess(dataFx.getApplicationRoot(), "Section Successfully Deleted.");
        });

        dst.whenCancelled(() -> {
            Mono.fx().snackbar().showError(application_root, "Cannot Delete Section.");
        });

        dst.whenFailed(() -> {
            Mono.fx().snackbar().showError(application_root, "Cannot Delete Section.");
        });

        dst.whenFinished(() -> {
            btn_delete_section.setDisable(false);
        });
        dst.transact();
    }

    private void onEditSchedule() {
        String schedDay = cmb_sched_day.getSelectionModel().getSelectedItem().toUpperCase();
        int start_selected_index = cmb_sched_start.getSelectionModel().getSelectedIndex();
        String schedStart = ScheduleConstants.getTimeLapse().get(start_selected_index);

        String schedEnd = ScheduleConstants.getTimeLapse()
                .get(cmb_sched_end.getSelectionModel().getSelectedIndex() + 4 + start_selected_index); // because starts from 8 am

        /**
         * Before testing the conflicting schedule temporarily delete the
         * schedule
         */
        if (this.selectedSchedForEditting == null) {
            // if there was a glitch and there is no selected schedule selected
            Mono.fx().snackbar().showError(application_root, "Please Try Again.");
            onBackFromScheduleCreation();
            return;
        }

        LoadGroupScheduleMapping currentSched = Database.
                connect()
                .load_group_schedule()
                .getPrimary(this.selectedSchedForEditting.getId());

        currentSched.setActive(0);
        boolean schedDeactivated = Database.connect().load_group_schedule().update(currentSched);

        if (!schedDeactivated) {
            // error in negative values
            System.out.println("Cannot deactivate schedule");
            Mono.fx().snackbar().showError(application_root, "Conflict Verification Failed.");
            return;
        }

        System.out.println("schedule deactivated checking conflict");

        // now the schedule is deleted conflict checking will only  verify all sched except this one
        /**
         * conflict checker only checks schedule conflicts within the section
         * subjects itself. it does not verify faculty schedule conflict and
         * room conflict schedules.
         */
        boolean conflict = ScheduleChecker.checkIfConflict(this.sectionMap.getId(), schedDay, schedStart, schedEnd);
        currentSched = Database.
                connect()
                .load_group_schedule()
                .getPrimary(this.selectedSchedForEditting.getId());
        // if conflict reactivate the schedule
        if (conflict) {

            currentSched.setActive(1);
            boolean schedReactivated = Database.connect().load_group_schedule().update(currentSched);
            if (schedReactivated) {
                Mono.fx().snackbar().showError(application_root, "Update Failed, Schedule Conflict was found.");
            } else {
                Mono.fx().snackbar().showError(application_root, "Update Error, Please recreate the schedule.");
            }

        } else {
            // if no conflict
            currentSched.setClass_start(schedStart);
            currentSched.setClass_end(schedEnd);
            currentSched.setClass_day(schedDay);
            currentSched.setClass_room(MonoText.getFormatted(txt_sched_room));
            currentSched.setUpdated_by(CollegeFaculty.instance().getFACULTY_ID());
            currentSched.setUpdated_date(Mono.orm().getServerTime().getDateWithFormat());
            currentSched.setActive(1);
            boolean schedUpdated = Database.connect().load_group_schedule().update(currentSched);
            if (schedUpdated) {
                Mono.fx().snackbar().showSuccess(application_root, "Schedule successfully updated.");
            } else {
                Mono.fx().snackbar().showError(application_root, "Update Error, Please recreate the schedule.");
            }
        }
        onBackFromScheduleCreation();
    }

    /**
     * Adds a schedule in this load group and checks for conflict
     */
    private void onAddSchedule() {
        String schedDay = cmb_sched_day.getSelectionModel().getSelectedItem().toUpperCase();
        int start_selected_index = cmb_sched_start.getSelectionModel().getSelectedIndex();
        String schedStart = ScheduleConstants.getTimeLapse().get(start_selected_index);

        String schedEnd = ScheduleConstants.getTimeLapse()
                .get(cmb_sched_end.getSelectionModel().getSelectedIndex() + 4 + start_selected_index); // because starts from 8 am

        /**
         * conflict checker only checks schedule conflicts within the section
         * subjects itself. it does not verify faculty schedule conflict and
         * room conflict schedules.
         */
        boolean conflict = ScheduleChecker.checkIfConflict(this.sectionMap.getId(), schedDay, schedStart, schedEnd);
        if (conflict) {
            Mono.fx().snackbar().showError(application_root, "Selected schedule is in conflict with this section's other schedules.");
        } else {
            LoadGroupScheduleMapping lg_sched = MapFactory.map().load_group_schedule();
            lg_sched.setClass_start(schedStart);
            lg_sched.setClass_end(schedEnd);
            lg_sched.setClass_day(schedDay);
            lg_sched.setClass_room(MonoText.getFormatted(txt_sched_room));
            lg_sched.setCreated_by(CollegeFaculty.instance().getFACULTY_ID());
            lg_sched.setCreated_date(Mono.orm().getServerTime().getDateWithFormat());
            lg_sched.setLoad_group_id(this.selected_load_group_in_sections.getId());
            int res = Database.connect().load_group_schedule().insert(lg_sched);

            if (res <= 0) {
                // failed
                Mono.fx().snackbar().showError(application_root, "Failed to Add Schedule.");
            } else {
                // success

                Animate.fade(vbox_add_schedule, SectionConstants.FADE_SPEED, () -> {
                    this.vbox_add_schedule.setVisible(false);
                    this.vbox_view_schedule.setVisible(true);
                    this.vbox_view_subjects.setVisible(false);
                }, vbox_view_schedule);

                fetchSchedules(selected_load_group_in_sections);

                Mono.fx().snackbar().showSuccess(application_root, "Successfully Added.");
            }
        }
    }

    /**
     * From schedule viewing to subject view.
     */
    private void onBackToSubjectListFromSchedule() {
        // set null
        selected_load_group_in_sections = null;
        //
        Animate.fade(vbox_view_schedule, SectionConstants.FADE_SPEED, () -> {
            vbox_view_subjects.setVisible(true);
            vbox_view_schedule.setVisible(false);
            vbox_add_schedule.setVisible(false);
        }, vbox_view_subjects);
        /**
         * detach empty schedule if ever schedule was empty.
         */
        this.scheduleEmpty.detach();
    }

    private LoadGroupScheduleMapping selectedSchedForEditting;

    /**
     * Load Schedules of the selected subject.
     *
     * @param scheds
     */
    private void loadSchedule(ArrayList<LoadGroupScheduleMapping> scheds) {
        // create table
        SimpleTable tblSectionType = new SimpleTable();

        for (LoadGroupScheduleMapping sched : scheds) {
            // create row
            SimpleTableRow row = new SimpleTableRow();
            row.setRowHeight(50.0);

            // get contents
            HBox rowSection = (HBox) Mono.fx().create()
                    .setPackageName("update3.org.cict.layout.sectionmain")
                    .setFxmlDocument("row-section-subject-sched")
                    .makeFX()
                    .pullOutLayout();

            Label lbl_day = super.searchAccessibilityText(rowSection, "lbl_day");
            Label lbl_time = super.searchAccessibilityText(rowSection, "lbl_time");
            Label lbl_room = super.searchAccessibilityText(rowSection, "lbl_room");

            lbl_day.setText(WordUtils.capitalizeFully(sched.getClass_day()));
            String startTime = ScheduleConstants.toPrettyFormat(sched.getClass_start());
            String endTime = ScheduleConstants.toPrettyFormat(sched.getClass_end());
            lbl_time.setText(startTime + " - " + endTime);
            lbl_room.setText(sched.getClass_room().toUpperCase());

            /**
             * Button Actions
             */
            ImageView img_edit = super.searchAccessibilityText(rowSection, "img_edit");
            JFXButton btn_remove = super.searchAccessibilityText(rowSection, "btn_remove");

            super.addClickEvent(img_edit, () -> {
                this.selectedSchedForEditting = sched;
                showScheduleAdding(true);
            });
            super.addClickEvent(btn_remove, () -> {

                /**
                 * Remove Schedule.
                 */
                int c = Mono.fx().alert().createConfirmation().setTitle("Remove")
                        .setHeader("Remove Schedule")
                        .setMessage("Are you sure to remove this schedule ?")
                        .confirmYesNo();
                if (c != 1) {
                    return;
                }

                try {
                    LoadGroupScheduleMapping lgsm = Database.connect().load_group_schedule().getPrimary(sched.getId());
                    lgsm.setUpdated_by(CollegeFaculty.instance().getFACULTY_ID());
                    lgsm.setUpdated_date(Mono.orm().getServerTime().getDateWithFormat());
                    lgsm.setActive(0);
                    boolean updated = Database.connect().load_group_schedule().update(lgsm);
                    if (updated) {
                        Mono.fx().snackbar().showSuccess(application_root, "Schedule was removed");
                        fetchSchedules(selected_load_group_in_sections);
                    } else {
                        Mono.fx().snackbar().showError(application_root, "Remove Failed, Please Try Again.");
                    }
                } catch (Exception e) {
                    Mono.fx().snackbar().showError(application_root, "Remove Failed, Please Try Again.");
                }
            });

            SimpleTableCell cellParent = new SimpleTableCell();
            cellParent.setResizePriority(Priority.ALWAYS);
            cellParent.setContentAsPane(rowSection);

            row.addCell(cellParent);

            tblSectionType.addRow(row);
        }

        /**
         * Load More Rows.
         */
        // table view
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(tblSectionType);
        simpleTableView.setFixedWidth(true);
        // attach to parent variable name in scene builder
        simpleTableView.setParentOnScene(vbox_schedule);
    }

    /**
     * Load Section subjects.
     *
     * @param sectionSubjects
     */
    private void loadSubjects(ArrayList<SubjectData> sectionSubjects) {
        // create table
        SimpleTable tblSectionType = new SimpleTable();

        /**
         * Create row.
         */
        for (SubjectData sectionSubject : sectionSubjects) {
            SimpleTableRow row = new SimpleTableRow();
            row.setRowHeight(50.0);

            // get contents
            HBox rowSectionSubject = (HBox) Mono.fx().create()
                    .setPackageName("update3.org.cict.layout.sectionmain")
                    .setFxmlDocument("row-section-subject")
                    .makeFX()
                    .pullOutLayout();

            Label lbl_subject_code = super.searchAccessibilityText(rowSectionSubject, "lbl_subject_code");
            Label lbl_count = super.searchAccessibilityText(rowSectionSubject, "lbl_count");
            Label lbl_instructor = super.searchAccessibilityText(rowSectionSubject, "lbl_instructor");

            lbl_subject_code.setText(sectionSubject.subject.getCode());
            lbl_count.setText(sectionSubject.studentCount);
            lbl_instructor.setText(sectionSubject.instructorName);

            JFXButton btn_information = super.searchAccessibilityText(rowSectionSubject, "btn_information");
            /**
             *
             */

            LoadGroupMapping selected_load_group = sectionSubject.loadGroup;
            super.addClickEvent(btn_information, () -> {
                viewSchedule(selected_load_group, sectionSubject);
            });

            SimpleTableCell cellParent = new SimpleTableCell();
            cellParent.setResizePriority(Priority.ALWAYS);
            cellParent.setContentAsPane(rowSectionSubject);

            row.addCell(cellParent);

            /**
             * Add to table.
             */
            tblSectionType.addRow(row);
        }

        // table view
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(tblSectionType);
        simpleTableView.setFixedWidth(true);
        // attach to parent variable name in scene builder
        simpleTableView.setParentOnScene(vbox_subjects);
    }

    // selected load_group when viewSchedule function was triggered
    private LoadGroupMapping selected_load_group_in_sections;

    /**
     * View Schedule an annexed function. when a subject information was
     * clicked.
     *
     * @param selected_load_group
     * @param sectionSubject
     */
    private void viewSchedule(LoadGroupMapping selected_load_group, SubjectData sectionSubject) {
        // assign
        selected_load_group_in_sections = selected_load_group;
        Animate.fade(vbox_view_subjects, SectionConstants.FADE_SPEED, () -> {
            vbox_view_subjects.setVisible(false);
            vbox_view_schedule.setVisible(true);
        }, vbox_view_schedule);
        /**
         * Start Fetching Schedule.
         */
        this.fetchSchedules(selected_load_group);
        /**
         * Display Count and instructor.
         */
        this.lbl_student_count.setText(sectionSubject.studentCount);
        this.lbl_instructor_big.setText(sectionSubject.instructorName);
        this.lbl_subject_code_top.setText(sectionSubject.subject.getCode());
        this.lbl_subject_code_top_addsched.textProperty().bind(this.lbl_subject_code_top.textProperty());
    }

    /**
     * Get schedules of a particular load group.
     *
     * @param load_group
     */
    private void fetchSchedules(LoadGroupMapping load_group) {
        this.scheduleEmpty.detach();
        FetchSchedule scheduleTx = new FetchSchedule();
        scheduleTx.loadGroup = load_group;

        scheduleTx.whenFailed(() -> {
            System.out.println("Emtpy Schedule");
        });
        scheduleTx.whenCancelled(() -> {
            this.scheduleEmpty.setMessage("No Schedules");
            this.scheduleEmpty.attach();
        });

        scheduleTx.whenSuccess(() -> {
            ArrayList<LoadGroupScheduleMapping> schedule = scheduleTx.loadSchedules;

            // arrange by time
            // sorted schedule based on time start.
            schedule.sort((LoadGroupScheduleMapping o1, LoadGroupScheduleMapping o2) -> {
                Double o1_val = ScheduleChecker.doubleConverter(o1.getClass_start());
                Double o2_val = ScheduleChecker.doubleConverter(o2.getClass_start());

                return o1_val.compareTo(o2_val);
            });

            schedule.sort((LoadGroupScheduleMapping o1, LoadGroupScheduleMapping o2) -> {
                Integer o1_val = ScheduleConstants.getDayInteger(o1.getClass_day());
                Integer o2_val = ScheduleConstants.getDayInteger(o2.getClass_day());

                return o1_val.compareTo(o2_val);
            });

            loadSchedule(schedule);
        });
        scheduleTx.whenFinished(() -> {
            //System.out.println("done");
        });

        scheduleTx.transact();
    }

    //--------------------------------------------------------------------------
    /**
     * Get all subjects from this sections.
     */
    private class FetchSubjects extends Transaction {

        private LoadSectionMapping sectionMap;

        /**
         * Results
         */
        private ArrayList<SubjectData> sectionSubjects;

        @Override
        protected boolean transaction() {
            sectionSubjects = new ArrayList<>();
            /**
             * Fetch all load Groups.
             */

            ArrayList<LoadGroupMapping> loadGroups = Mono.orm()
                    .newSearch(Database.connect().load_group())
                    .eq(DB.load_group().LOADSEC_id, sectionMap.getId())
                    .active()
                    .all();

            /**
             * Get Subject Name.
             */
            for (LoadGroupMapping loadGroup : loadGroups) {
                SubjectData subjectData = new SubjectData();
                /**
                 * save load group data.
                 */
                subjectData.loadGroup = loadGroup;
                /**
                 * save subject data.
                 */
                subjectData.subject = Database.connect()
                        .subject()
                        .getPrimary(loadGroup.getSUBJECT_id());

                /**
                 * Get Student Count.
                 */
                Searcher studentCountSearch = Mono.orm()
                        .newSearch(Database.connect().load_subject())
                        .eq(DB.load_subject().LOADGRP_id, loadGroup.getId())
                        .eq(DB.load_subject().active, 1)
                        .pull();

                subjectData.studentCount = Mono.orm()
                        .projection(studentCountSearch)
                        .count(DB.load_subject().id);

                /**
                 * Get Instructor;
                 */
                if (loadGroup.getFaculty() == null) {
                    subjectData.instructorName = "No Data";
                } else {
                    FacultyMapping intructor = Database.connect().faculty().getPrimary(loadGroup);
                    try {
                        String instructorName = intructor.getLast_name() + ", ";
                        if (intructor.getMiddle_name() != null) {
                            instructorName += intructor.getMiddle_name() + " ";
                        }
                        instructorName += intructor.getLast_name();
                        subjectData.instructorName = instructorName;
                    } catch (NullPointerException e) {
                        subjectData.instructorName = "Unreadable Data";
                    }
                }

                sectionSubjects.add(subjectData);
            }

            return true;
        }

        @Override
        protected void after() {

        }
    }

    private class SubjectData {

        private LoadGroupMapping loadGroup;
        private SubjectMapping subject;
        private String studentCount;
        private String instructorName;
    }

    //--------------------------------------------------------------------------
    public class FetchSchedule extends Transaction {

        public LoadGroupMapping loadGroup;
        /**
         * Results
         */
        ArrayList<LoadGroupScheduleMapping> loadSchedules;

        @Override
        protected boolean transaction() {
            /**
             * Get All schedules.
             */
            this.loadSchedules = Mono.orm()
                    .newSearch(Database.connect().load_group_schedule())
                    .eq(DB.load_group_schedule().load_group_id, loadGroup.getId())
                    .active()
                    .all();

            if (loadSchedules == null) {
                // cancel will mean empty schedules.
                return false;
            }

            return true;
        }

        @Override
        protected void after() {

        }
    }
}

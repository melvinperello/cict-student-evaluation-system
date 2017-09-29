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
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
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
import java.util.ArrayList;
import java.util.Comparator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.text.WordUtils;
import org.cict.authentication.authenticator.CollegeFaculty;
import update3.org.cict.ChoiceRange;
import update3.org.cict.scheduling.ScheduleConstants;
import update3.org.cict.SectionConstants;
import update3.org.cict.scheduling.OpenScheduleViewer;
import update3.org.cict.scheduling.ScheduleChecker;
import update3.org.cict.window_prompts.empty_prompt.EmptyView;

/**
 *
 * @author Jhon Melvin
 */
public class SectionSubjectsController extends SceneFX implements ControllerFX {

    @FXML
    private VBox application_root;

    @FXML
    private JFXButton btn_back;

    @FXML
    private Label lbl_semester;

    @FXML
    private Label lbl_curriculum;

    @FXML
    private Label lbl_curriculum_type;

    @FXML
    private VBox vbox_single;

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
    private JFXButton btn_save_changes;

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

    @FXML
    private JFXButton btn_view_class_sched;

    public SectionSubjectsController() {
        /**
         * Every code below this constructor was passed from another controller.
         */
    }

    private LayoutDataFX winRegularSectionControllerFx;
    private LoadSectionMapping sectionMap;

    public void setSectionMap(LoadSectionMapping sectionMap) {
        this.sectionMap = sectionMap;
    }

    public void setWinRegularSectionControllerFx(LayoutDataFX winRegularSectionControllerFx) {
        this.winRegularSectionControllerFx = winRegularSectionControllerFx;
    }

    /**
     * Passed Variables from WinRegularSectionController.class
     */
    private AcademicProgramMapping academicProgramMap;
    private CurriculumMapping curriculumMap;
    private String currentTermString;
    private String curriculumType;
    private String sectionName;

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

        /**
         * Set Labels.
         */
        this.lbl_semester.setText(currentTermString);
        this.lbl_curriculum.setText(curriculumMap.getName());
        this.lbl_curriculum_type.setText(curriculumType);

        /**
         * Default view.
         */
        this.vbox_add_schedule.setVisible(false);
        this.vbox_view_schedule.setVisible(false);
        this.vbox_view_subjects.setVisible(true);
        //
        this.lbl_section_name.setText(sectionName);

        fetchSectionSubject();
        //loadSchedule();
        this.initScheduler();

//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        super.addClickEvent(btn_back, () -> {
            Animate.fade(this.application_root, SectionConstants.FADE_SPEED, () -> {
                super.replaceRoot(winRegularSectionControllerFx.getApplicationRoot());
            }, winRegularSectionControllerFx.getApplicationRoot());
            /**
             * Back To Section List. Detach empty if ever the section was empty
             */
            this.scheduleEmpty.detach();
        });
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

        /**
         * Back to subject list
         */
        super.addClickEvent(btn_back_to_subject, () -> {
            onBackToSubjectListFromSchedule();
        });

        super.addClickEvent(btn_view_add_schedule, () -> {
            Animate.fade(vbox_view_schedule, SectionConstants.FADE_SPEED, () -> {
                this.vbox_add_schedule.setVisible(true);
                this.vbox_view_schedule.setVisible(false);
                this.vbox_view_subjects.setVisible(false);
            }, vbox_add_schedule);

        });

        super.addClickEvent(btn_cancel_sched, () -> {
            Animate.fade(vbox_add_schedule, SectionConstants.FADE_SPEED, () -> {
                this.vbox_add_schedule.setVisible(false);
                this.vbox_view_schedule.setVisible(true);
                this.vbox_view_subjects.setVisible(false);
            }, vbox_view_schedule);
            /**
             * Canceling the creation of schedule will refresh the schedules.
             */
            fetchSchedules(selected_load_group_in_sections);
        });

        //--------------
        super.addClickEvent(btn_add_sched, () -> {
            onAddSchedule();
        });

        super.addClickEvent(btn_view_class_sched, () -> {
            /**
             * Open Schedule Viewer.
             */
            OpenScheduleViewer.openScheduleViewer(sectionMap);
        });

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

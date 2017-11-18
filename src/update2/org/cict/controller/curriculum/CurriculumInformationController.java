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
 * JOEMAR N. DE LA CRUZ
 * GRETHEL EINSTEIN BERNARDINO
 *
 * OTHER LIBRARIES THAT ARE USED BELONGS TO THEIR RESPECTFUL OWNERS AND AUTHORS.
 * NO COPYRIGHT ARE INTENTIONAL OR INTENDED.
 * THIS PROJECT IS NOT PROFITABLE HENCE FOR EDUCATIONAL PURPOSES ONLY.
 * THIS PROJECT IS ONLY FOR COMPLIANCE TO OUR REQUIREMENTS.
 * THIS PROJECT DOES NOT INCLUDE DISTRIBUTION FOR OTHER PURPOSES.
 *
 */
package update2.org.cict.controller.curriculum;

import app.lazy.models.AcademicProgramMapping;
import app.lazy.models.AcademicTermMapping;
import app.lazy.models.CurriculumHistoryMapping;
import app.lazy.models.CurriculumHistorySummaryMapping;
import app.lazy.models.CurriculumMapping;
import app.lazy.models.CurriculumPreMapping;
import app.lazy.models.CurriculumRequisiteExtMapping;
import app.lazy.models.CurriculumRequisiteLineMapping;
import app.lazy.models.CurriculumSubjectMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.SubjectMapping;
import artifacts.MonoString;
import com.izum.fx.textinputfilters.StringFilter;
import com.izum.fx.textinputfilters.TextInputFilters;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jhmvin.Mono;
import com.jhmvin.fx.controls.SimpleImage;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.LayoutDataFX;
import com.jhmvin.fx.display.SceneFX;
import com.jhmvin.transitions.Animate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.cict.SubjectClassification;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.Notifications;
import org.hibernate.criterion.Order;
import sys.org.cict.enumerations.CurriculumValues;
import update2.org.cict.controller.academicprogram.AcademicProgramAccessManager;
import update2.org.cict.controller.academicprogram.AcademicProgramHome;
import update2.org.cict.controller.subjects.SearchSubjectController;
import update2.org.cict.controller.subjects.SubjectPrerequisiteController;
import update3.org.cict.SectionConstants;

/**
 *
 * @author Joemar
 */
public class CurriculumInformationController extends SceneFX implements ControllerFX {

    @FXML
    private TextField txt_name;

    @FXML
    private TextField txt_description;

    @FXML
    private TextField txt_major;

    @FXML
    private RadioButton rbtn_yes;

    @FXML
    private RadioButton rbtn_no;

    @FXML
    private ComboBox<String> cmb_type;

    @FXML
    private CheckComboBox<String> cmb_prereq;

    @FXML
    private ComboBox<String> cmb_preparatory;

    @FXML
    private JFXCheckBox chkbx_obsolete;

    @FXML
    private Label lbl_id;

    @FXML
    private JFXButton btn_edit;

    @FXML
    private JFXButton btn_implement;

    @FXML
    private JFXButton btn_history;

    @FXML
    private Label lbl_created_by;

    @FXML
    private Label lbl_created_date;

    @FXML
    private Label lbl_status;

    @FXML
    private Label lbl_implemented_by;

    @FXML
    private Label lbl_implemented_date;

    @FXML
    private VBox vbox_semesters;

    @FXML
    private VBox vbox_no_result;

    @FXML
    private VBox application_root;

    @FXML
    private JFXButton btn_back;

    private CurriculumMapping CURRICULUM;
    private final ToggleGroup group = new ToggleGroup();
    private ObservableList<String> ladderTypes = FXCollections.observableArrayList();
    private ArrayList<CurriculumMapping> allCurriculums = new ArrayList<>();

    private String NONE = CurriculumValues.LadderType.NONE.toString();
    private String PREPARATORY = CurriculumValues.LadderType.PREPARATORY.toString();
    private String CONSEQUENT = CurriculumValues.LadderType.CONSEQUENT.toString();

    private Integer CREATED_BY = CollegeFaculty.instance().getFACULTY_ID();
    private Date CREATED_DATE = Mono.orm().getServerTime().getDateWithFormat();

    private Integer IMPLEMENTED_BY = CollegeFaculty.instance().getFACULTY_ID();
    private Date IMPLEMENTED_DATE = Mono.orm().getServerTime().getDateWithFormat();

    private void logs(String str) {
        boolean logging = true;
        if (logging) {
            System.out.println("@CurriculumInformationController: " + str);
        }
    }

    public CurriculumInformationController(CurriculumMapping curriculum) {
        this.CURRICULUM = curriculum;
    }

    @Override
    public void onInitialization() {
        super.bindScene(application_root);

        rbtn_yes.setToggleGroup(group);
        rbtn_no.setToggleGroup(group);

        ladderTypes.add(PREPARATORY);
        ladderTypes.add(CONSEQUENT);
        cmb_type.getItems().addAll(ladderTypes);

        allCurriculums = Mono.orm()
                .newSearch(Database.connect().curriculum())
                .eq(DB.curriculum().ladderization_type, "PREPARATORY")
                .ne(DB.curriculum().id, this.CURRICULUM.getId())
                .active()
                .all();
        this.refreshCmbCurriculums(true);

        /**
         * by default
         */
        cmb_type.getItems().add(NONE);
        cmb_type.getSelectionModel().select(NONE);
//        cmb_type.setDisable(true);
//        setViewCheckCmbBoxPreReq("NO");
        setDetails();

        if (AcademicProgramAccessManager.denyIfNotRegistrar()) {
            btn_implement.setDisable(true);
        }

        if (AcademicProgramAccessManager.denyIfNotAdmin()) {
            btn_edit.setDisable(true);
            txt_description.setDisable(true);
            txt_major.setDisable(true);
            txt_name.setDisable(true);
        }

        addTextFieldFilters();
    }

    private void refreshCmbCurriculums(boolean all) {
        if (allCurriculums != null) {
            cmb_prereq.getItems().clear();
            cmb_prereq.getItems().add(NONE);
            for (CurriculumMapping currentCurriculum : allCurriculums) {
                cmb_prereq.getItems().add(currentCurriculum.getName());
                if(all)
                    cmb_preparatory.getItems().add(currentCurriculum.getName());
            }
        }
    }
    
    private void addTextFieldFilters() {
        StringFilter textField = TextInputFilters.string()
                .setMaxCharacters(50)
                .setNoLeadingTrailingSpaces(false)
                .setFilterManager(filterManager -> {
                    if (!filterManager.isValid()) {
                        Mono.fx().alert().createWarning().setHeader("Warning")
                                .setMessage(filterManager.getMessage())
                                .show();
                    }
                });
        textField.clone().setTextSource(txt_name).applyFilter();

        StringFilter textField1 = TextInputFilters.string()
                .setFilterMode(StringFilter.LETTER_DIGIT_SPACE)
                .setMaxCharacters(50)
                .setNoLeadingTrailingSpaces(false)
                .setFilterManager(filterManager -> {
                    if (!filterManager.isValid()) {
                        Mono.fx().alert().createWarning().setHeader("Warning")
                                .setMessage(filterManager.getMessage())
                                .show();
                    }
                });
        textField1.clone().setTextSource(txt_major).applyFilter();
        textField1.clone().setTextSource(txt_description).applyFilter();

    }

    @Override
    public void onEventHandling() {
        addClickEvent(btn_edit, () -> {
            if (isImplemented) {
                Mono.fx().alert()
                        .createWarning()
                        .setHeader("Curriculum Cannot Be Modified")
                        .setMessage("It seems like this curriculum is implemented, therefore modifying is prohibited.")
                        .showAndWait();
            } else {
                validate();
            }
        });

        addClickEvent(btn_implement, () -> {
            this.onImplement();
        });

        this.addClickEvent(btn_history, () -> {
            showHistory();
        });

        this.addClickEvent(btn_back, () -> {
            onBack();
        });

        chkbx_obsolete.selectedProperty().addListener((a) -> {
            this.onObsolete();
        });

        super.addClickEvent(rbtn_no, () -> {
            isLadderized(false);
        });
        super.addClickEvent(rbtn_yes, () -> {
            isLadderized(true);
        });
        cmb_type.valueProperty().addListener((a) -> {
            ladderType = cmb_type.getSelectionModel().getSelectedItem();
            setViewCheckCmbBoxPreReq(ladderType);
        });

        cmb_preparatory.valueProperty().addListener((a)->{
            this.refreshCmbCurriculums(false);
            cmb_prereq.getItems().remove(cmb_preparatory.getSelectionModel().getSelectedItem());
        });
    }
    
    private void onObsolete() {
        if (run) {
            run = false;
            return;
        }
        CURRICULUM.setObsolete_term((chkbx_obsolete.isSelected() ? 1 : 0));
        if (Database.connect().curriculum().update(CURRICULUM)) {
            Notifications.create().title("Updated Successfully")
                    .text("Obsolete of the curriculum is updated.")
                    .showInformation();
        } else {
            Notifications.create().title("Process Failed")
                    .text("Something went wrong in removing\n"
                            + "the term.")
                    .showInformation();
        }
    }
    
    private void onImplement() {
        btn_implement.setDisable(isImplemented);
        boolean isComplete = true;
        for (int yrCtr = (CURRICULUM.getLadderization_type().equalsIgnoreCase("CONSEQUENT")? 3 : 1); yrCtr <= CURRICULUM.getStudy_years(); yrCtr++) {
            for (int semCtr = 1; semCtr <= 2; semCtr++) {
                Object exist = Mono.orm().newSearch(Database.connect().curriculum_subject())
                        .eq(DB.curriculum_subject().CURRICULUM_id, CURRICULUM.getId())
                        .eq(DB.curriculum_subject().year, yrCtr)
                        .eq(DB.curriculum_subject().semester, semCtr)
                        .active()
                        .first();
                if (exist == null) {
                    isComplete = false;
                }
            }
        }
        if (!isComplete) {
            Mono.fx().alert()
                    .createInfo()
                    .setHeader("Incomplete Curriculum Subject")
                    .setMessage("Please provide the complete subject of this curriculum before the implementation process.")
                    .show();
            return;
        }
        if (CURRICULUM.getImplemented() == 1) {
            btn_implement.setDisable(true);
        }
        int res = Mono.fx().alert().createConfirmation()
                .setHeader("Implement Curriculum")
                .setMessage("Implementing this curriculum will prohibit modifying any form, even its subjects. Do you still want to implement this curriculum?")
                .confirmYesNo();
        if (res == 1) {
            CURRICULUM.setImplementation_date(IMPLEMENTED_DATE);
            CURRICULUM.setImplemented(1);
            CURRICULUM.setImplemented_by(IMPLEMENTED_BY);
            if (!Database.connect().curriculum().update(CURRICULUM)) {
                logs("NOT IMPLEMENTED, CURRICULUM NAME: " + CURRICULUM.getName());
            } else {
                isUpdated = true;
                AcademicProgramMapping apMap = Mono.orm().newSearch(Database.connect().academic_program())
                        .eq(DB.academic_program().id, CURRICULUM.getACADPROG_id())
                        .active()
                        .first();
                if (apMap == null) {
                    logs("NO ACADEMIC PROGRAM FOUND");
                } else {
                    if (apMap.getImplemented() != 1) {
                        apMap.setImplementation_date(IMPLEMENTED_DATE);
                        apMap.setImplemented(1);
                        apMap.setImplemented_by(IMPLEMENTED_BY);
                        if (Database.connect().academic_program().update(apMap)) {
                            isUpdated = true;
                        } else {
                            logs("NOT SAVED, ACADPROG CODE: " + apMap.getCode());
                        }
                    } else {
                        logs("ACADEMIC PROGRAM ALREADY SAVED");
                    }
                }
                if (isUpdated) {
                    Mono.fx().alert()
                            .createInfo()
                            .setHeader("Implemented Successfully")
                            .setMessage("This curriculum is successfully implemented.")
                            .showAndWait();
                    onBack();
                }
            }
        }
    }

    private Boolean run = true;

    private void isLadderized(boolean ladderized) {
        if (ladderized) {
            laderized = "YES";
            setViewCheckCmbBoxPreReq("YES");
            cmb_type.setDisable(false);
            cmb_type.getItems().remove(NONE);
        } else {
            laderized = "NO";
            setViewCheckCmbBoxPreReq("NO");
            cmb_type.setDisable(true);
            if (!cmb_type.getItems().contains(NONE)) {
                cmb_type.getItems().add(NONE);
            }
            cmb_type.getSelectionModel().select(NONE);
        }

    }

    private final String SECTION_BASE_COLOR = "#414852";

    private void onBack() {
        AcademicProgramHome controller = new AcademicProgramHome();
        Pane pane = Mono.fx().create()
                .setPackageName("update2.org.cict.layout.academicprogram")
                .setFxmlDocument("academic-program-home")
                .makeFX()
                .setController(controller)
                .pullOutLayout();

        super.setSceneColor(SECTION_BASE_COLOR); // call once on entire scene lifecycle

        Animate.fade(this.application_root, SectionConstants.FADE_SPEED, () -> {
            super.replaceRoot(pane);
        }, pane);
    }

    private void showHistory() {
        HistoryController controller = new HistoryController(CURRICULUM);
        Pane pane = Mono.fx().create()
                .setPackageName("update2.org.cict.layout.curriculum")
                .setFxmlDocument("history")
                .makeFX()
                .setController(controller)
                .pullOutLayout();

        super.setSceneColor(SECTION_BASE_COLOR); // call once on entire scene lifecycle

        Animate.fade(this.application_root, SectionConstants.FADE_SPEED, () -> {
            super.replaceRoot(pane);
        }, pane);
    }

    
    private void setViewCheckCmbBoxPreReq(String mode) {
        if (mode.equalsIgnoreCase(PREPARATORY)
                || mode.equalsIgnoreCase("NO")) {
            cmb_prereq.getCheckModel().clearChecks();
            cmb_prereq.getItems().clear();
//            cmb_prereq.getItems().remove(NONE);
            cmb_prereq.getItems().add(NONE);
            cmb_prereq.getCheckModel().check(NONE);
            cmb_prereq.setDisable(true);
            cmb_preparatory.getItems().remove(NONE);
            cmb_preparatory.getItems().add(NONE);
            cmb_preparatory.getSelectionModel().select(NONE);
            cmb_preparatory.setDisable(true);
        } else if (mode.equalsIgnoreCase(CONSEQUENT)
                || mode.equalsIgnoreCase("YES")) {
            cmb_prereq.getCheckModel().clearChecks();
//            cmb_prereq.getItems().remove(NONE);
            cmb_prereq.setDisable(false);
            cmb_prereq.getCheckModel().check(NONE);
            cmb_preparatory.getItems().remove(NONE);
            cmb_preparatory.getSelectionModel().selectFirst();
            cmb_preparatory.setDisable(false);
        }
    }

    private boolean isImplemented;

    private void setDetails() {
        FetchCurriculumInfo fetch = new FetchCurriculumInfo();
        fetch.curriculum = this.CURRICULUM;
        fetch.setOnSuccess(onSuccess -> {
            lbl_id.setText(this.CURRICULUM.getId().toString());
            txt_name.setText(this.CURRICULUM.getName());
            try {
                if (!this.CURRICULUM.getDescription().equalsIgnoreCase("NO DESCRIPTION")) {
                    txt_description.setText(this.CURRICULUM.getDescription());
                }
            } catch (NullPointerException a) {
            }
            try {
                if (!this.CURRICULUM.getMajor().equalsIgnoreCase(NONE)) {
                    txt_major.setText(this.CURRICULUM.getMajor());
                }
            } catch (NullPointerException b) {
            }
            lbl_created_by.setText(fetch.getCreatedByFirstLastName());
            lbl_created_date.setText(fetch.getCreatedDateWithFormat());
            lbl_implemented_by.setText(fetch.getImplementedByFirstLastName());
            lbl_implemented_date.setText(fetch.getImplementedDateWithFormat());
            lbl_status.setText(fetch.getStatus());
            isImplemented = fetch.isImplemented();
            chkbx_obsolete.setSelected(CURRICULUM.getObsolete_term() == null || CURRICULUM.getObsolete_term() == 0 ? false : true);

            isLadderized(fetch.isLadderized());
            if (fetch.isLadderized()) {
                rbtn_yes.setSelected(true);
                ladderType = this.CURRICULUM.getLadderization_type();
                cmb_type.getSelectionModel().select(ladderType);
                ArrayList<CurriculumMapping> preReqs = fetch.getPreReqCurriculums();
                if (preReqs != null) {
                    cmb_prereq.getCheckModel().clearChecks();
                    cmb_prereq.getItems().remove(NONE);
                    for (CurriculumMapping preReq : preReqs) {
                        cmb_prereq.getCheckModel().check(preReq.getName());
                    }
                } else {
                    if (!cmb_prereq.getItems().contains(NONE)) {
                        cmb_prereq.getItems().add(NONE);
                    }
                    cmb_prereq.getCheckModel().clearChecks();
                    cmb_prereq.getCheckModel().check(NONE);
                }
                if (fetch.getPreparatory() != null) {
                    cmb_preparatory.getSelectionModel().select(fetch.getPreparatory());
                }
            } else {
                rbtn_no.setSelected(true);
            }
            if (isImplemented) {
                txt_description.setDisable(isImplemented);
                txt_major.setDisable(isImplemented);
                txt_name.setDisable(isImplemented);
                rbtn_no.setDisable(isImplemented);
                rbtn_yes.setDisable(isImplemented);
                cmb_type.setDisable(isImplemented);
                cmb_prereq.setDisable(isImplemented);
                cmb_preparatory.setDisable(isImplemented);
                btn_edit.setDisable(isImplemented);
                btn_implement.setDisable(isImplemented);
            }
        });
        fetch.transact();
        setYearAndSemesterView();
    }

    private void setYearAndSemesterView() {
        createTable();
        int exptdSem = 2;
        int studyYears = this.CURRICULUM.getStudy_years();
        boolean isConsequent = false;
        if (CURRICULUM.getLadderization_type().equalsIgnoreCase(CONSEQUENT)) {
            isConsequent = true;
        }
        for (int ctrYr = 1; ctrYr <= studyYears; ctrYr++) {
            Double totalSubjects = 0.0;
            Double totalUnits = 0.0;
            if (isConsequent) {
                ctrYr += 2;
            }
            ArrayList<CurriculumSubjectMapping> years = Mono.orm().newSearch(Database.connect().curriculum_subject())
                    .eq(DB.curriculum_subject().CURRICULUM_id, this.CURRICULUM.getId())
                    .eq(DB.curriculum_subject().year, ctrYr)
                    .active()
                    .all();

            for (int ctrSem = 1; ctrSem <= exptdSem; ctrSem++) {
                totalSubjects = 0.0;
                totalUnits = 0.0;
                ArrayList<CurriculumSubjectMapping> semesters = new ArrayList<>();
                try {
                    for (CurriculumSubjectMapping csMap : years) {
                        if (csMap.getSemester() == ctrSem) {
                            semesters.add(csMap);
                            SubjectMapping subject = Mono.orm().newSearch(Database.connect().subject())
                                    .eq(DB.subject().id, csMap.getSUBJECT_id())
                                    .active()
                                    .first();
                            totalUnits += (subject.getLab_units() + subject.getLec_units());
                            totalSubjects++;
                        }
                    }
                } catch (NullPointerException s) {
                }
                createRow(ctrYr, ctrSem, totalSubjects, totalUnits, semesters);
            }
            if (isConsequent) {
                ctrYr -= 2;
            }
        }
    }

    private SimpleTable semesterTable = new SimpleTable();

    private void createTable() {
        semesterTable.getChildren().clear();
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(semesterTable);
        simpleTableView.setFixedWidth(true);

        simpleTableView.setParentOnScene(vbox_semesters);
    }

    private Integer curriculumID;

    private void createRow(Integer year, Integer sem, Double subjects, Double units, ArrayList<CurriculumSubjectMapping> semesters) {
        curriculumID = this.CURRICULUM.getId();
        SimpleTableRow row = new SimpleTableRow();
        row.setRowHeight(80.0);

        HBox semesterRow = (HBox) Mono.fx().create()
                .setPackageName("update2.org.cict.layout.curriculum")
                .setFxmlDocument("row-semester")
                .makeFX()
                .pullOutLayout();

        ImageView btn_extension = searchAccessibilityText(semesterRow, "btn_extension");
        Label lbl_year_level = searchAccessibilityText(semesterRow, "lbl_year_level");
        Label lbl_semester = searchAccessibilityText(semesterRow, "lbl_semester");
        Label lbl_subjects = searchAccessibilityText(semesterRow, "lbl_subjects");
        Label lbl_units = searchAccessibilityText(semesterRow, "lbl_units");
        Button btn_add_subject = searchAccessibilityText(semesterRow, "btn_add_subject");

        lbl_year_level.setText(getYearLevel(year));
        lbl_semester.setText(getSemester(sem));
        lbl_subjects.setText(subjects.toString());
        lbl_units.setText(units.toString());
        btn_add_subject.setDisable(isImplemented);

        if (AcademicProgramAccessManager.denyIfNotAdmin()) {
            btn_add_subject.setDisable(true);
        }

        addClickEvent(btn_add_subject, () -> {
            if (!isImplemented) {
                this.showSubjectSearch(curriculumID, year, sem);
            } else {
                Mono.fx().alert().createWarning()
                        .setHeader("Curriculum Cannot Be Modified")
                        .setMessage("It seems like this curriculum is implemented, therefore modifying is prohibited.")
                        .showAndWait();
            }
        });

        SimpleTableCell cellParent = new SimpleTableCell();
        cellParent.setResizePriority(Priority.ALWAYS);
        cellParent.setContent(semesterRow);

        row.addCell(cellParent);

        createSubjectExtension(row, btn_extension, semesterRow, semesters, lbl_subjects, lbl_units);

        semesterTable.addRow(row);
    }

    private void showSubjectSearch(Integer curriculumId, Integer yr, Integer sem) {
        if (curriculumId == -1) {
            System.out.println("No curriculum found");
        } else {
            SearchSubjectController controller = new SearchSubjectController(CURRICULUM);
            controller.setYearAndSemester(yr, sem);
            controller.setModeSetting("CURRICULUM");
            controller.title = getYearLevel(yr) + "-" + getSemester(sem);

            LayoutDataFX layoutFx = new LayoutDataFX(application_root, this);
            controller.setHomeFx(layoutFx);
            Pane pane = Mono.fx().create()
                    .setPackageName("update2.org.cict.layout.subjects")
                    .setFxmlDocument("search-subject")
                    .makeFX()
                    .setController(controller)
                    .pullOutLayout();

            super.setSceneColor(SECTION_BASE_COLOR); // call once on entire scene lifecycle

            Animate.fade(this.application_root, SectionConstants.FADE_SPEED, () -> {
                super.replaceRoot(pane);
            }, pane);

            if (controller.isAdded()) {
                setYearAndSemesterView();
            }
            System.out.println("DONE");
        }
    }

    private void createSubjectExtension(SimpleTableRow row, ImageView img_extension, HBox programRow, ArrayList<CurriculumSubjectMapping> semesters, Label subjects, Label units) {

        VBox subjectExtension = (VBox) Mono.fx().create()
                .setPackageName("update2.org.cict.layout.curriculum")
                .setFxmlDocument("subject-extension")
                .makeFX()
                .pullOutLayout();

        addClickEvent(img_extension, () -> {
            if (row.isExtensionShown()) {
                img_extension.setImage(SimpleImage.make("update2.org.cict.layout.academicprogram.images", "show_extension.png"));
                row.hideExtension();
            } else {
                // close all row extension
                for (Node tableRows : semesterTable.getRows()) {
                    SimpleTableRow simplerow
                            = (SimpleTableRow) tableRows;
                    SimpleTableCell simplecell = simplerow.getCell(0);
                    HBox simplerowcontent = simplecell.getContent();
                    ImageView simplerowimage = findByAccessibilityText(simplerowcontent, "btn_extension");

                    simplerowimage.setImage(SimpleImage.make("update2.org.cict.layout.academicprogram.images", "show_extension.png"));
                    simplerow.hideExtension();
                }

                // show row extension
                img_extension.setImage(SimpleImage.make("update2.org.cict.layout.academicprogram.images", "hide_extension.png"));
                row.setRowExtension(subjectExtension);
                row.showExtension();
            }
        });

        super.addDoubleClickEvent(row, () -> {
            if (row.isExtensionShown()) {
                img_extension.setImage(SimpleImage.make("update2.org.cict.layout.academicprogram.images", "show_extension.png"));
                row.hideExtension();
            } else {
                // close all row extension
                for (Node tableRows : semesterTable.getRows()) {
                    SimpleTableRow simplerow = (SimpleTableRow) tableRows;
                    SimpleTableCell simplecell = simplerow.getCell(0);
                    HBox simplerowcontent = simplecell.getContent();
                    ImageView simplerowimage = findByAccessibilityText(simplerowcontent, "btn_extension");

                    simplerowimage.setImage(SimpleImage.make("update2.org.cict.layout.academicprogram.images", "show_extension.png"));
                    simplerow.hideExtension();
                }

                // show row extension
                img_extension.setImage(SimpleImage.make("update2.org.cict.layout.academicprogram.images", "hide_extension.png"));
                row.setRowExtension(subjectExtension);
                row.showExtension();
            }
        });

        VBox vbox_curriculum_table_holder = searchAccessibilityText(subjectExtension, "vbox_subjects");
        Label lbl_no_found = searchAccessibilityText(subjectExtension, "lbl_no_found");
        if (semesters.isEmpty()) {
            lbl_no_found.setVisible(true);
        } else {
            createSubjectRows(vbox_curriculum_table_holder, semesters, subjects, units);
        }
    }

    private void createSubjectRows(VBox holder, ArrayList<CurriculumSubjectMapping> semesters, Label subjects, Label units) {
        SimpleTable subjectTable = new SimpleTable();
        try {
            for (CurriculumSubjectMapping csMap : semesters) {
                SubjectMapping subject = Mono.orm().newSearch(Database.connect().subject())
                        .eq(DB.subject().id, csMap.getSUBJECT_id())
                        .active()
                        .first();
                if (subject == null) {
                    continue;
                }

                SimpleTableRow row = new SimpleTableRow();
                row.setRowHeight(70.0);

                // load pre defined row layout
                HBox curriculumRow = (HBox) Mono.fx().create()
                        .setPackageName("update2.org.cict.layout.curriculum")
                        .setFxmlDocument("row-subject")
                        .makeFX()
                        .pullOutLayout();

                Label lbl_subject_code = searchAccessibilityText(curriculumRow, "subject_code");
                Label lbl_descriptive_title = searchAccessibilityText(curriculumRow, "descriptive_title");
                Label lbl_lec = searchAccessibilityText(curriculumRow, "lec");
                Label lbl_lab = searchAccessibilityText(curriculumRow, "lab");
                Button btn_remove = searchAccessibilityText(curriculumRow, "btn_remove");
                Button btn_more_info = searchAccessibilityText(curriculumRow, "btn_more_info");

                addClickEvent(btn_more_info, () -> {
                    SubjectPrerequisiteController controller = new SubjectPrerequisiteController(this.CURRICULUM, subject, isImplemented);
                    Pane pane = Mono.fx().create()
                            .setPackageName("update2.org.cict.layout.subjects")
                            .setFxmlDocument("subject-prereq")
                            .makeFX()
                            .setController(controller)
                            .pullOutLayout();

                    super.setSceneColor(SECTION_BASE_COLOR); // call once on entire scene lifecycle

                    Animate.fade(this.application_root, SectionConstants.FADE_SPEED, () -> {
                        super.replaceRoot(pane);
                    }, pane);
                });
                addClickEvent(btn_remove, () -> {
                    onRemove(csMap, subjectTable, subjects, units, subject, row);
                });

                if (AcademicProgramAccessManager.denyIfNotAdmin()) {
                    btn_remove.setDisable(true);
                }

                lbl_subject_code.setText(subject.getCode());
                lbl_descriptive_title.setText(subject.getDescriptive_title());
                lbl_lec.setText(subject.getLec_units().toString());
                lbl_lab.setText(subject.getLab_units().toString());

                SimpleTableCell cellParent = new SimpleTableCell();
                cellParent.setResizePriority(Priority.ALWAYS);
                cellParent.setContent(curriculumRow);

                // add cell to row
                row.addCell(cellParent);

                // add row
                subjectTable.addRow(row);
            }
        } catch (NullPointerException a) {
            System.out.println("Null is here");
        }
        // when all details are added in the table;
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(subjectTable);
        simpleTableView.setFixedWidth(true);
        // attach to parent variable name in scene builder
        simpleTableView.setParentOnScene(holder);
    }

    private void onRemove(CurriculumSubjectMapping csMap, SimpleTable subjectTable, Label lbl_subjects, Label lbl_units,
            SubjectMapping subject, SimpleTableRow row) {

        boolean isAPrerequisite, isACoRequisite = false;
        CurriculumRequisiteLineMapping crlMap = Mono.orm().newSearch(Database.connect().curriculum_requisite_line())
                .eq(DB.curriculum_requisite_line().CURRICULUM_id, this.curriculumID)
                .eq(DB.curriculum_requisite_line().SUBJECT_id_req, subject.getId())
                .active(Order.desc(DB.curriculum_requisite_line().id))
                .first();
        String code = "", yearSem = "";
        if (crlMap != null) {
            isAPrerequisite = true;
            SubjectMapping subjectGet = Mono.orm().newSearch(Database.connect().subject())
                    .eq(DB.subject().id, crlMap.getSUBJECT_id_get())
                    .active()
                    .first();
            code = subjectGet.getCode();
            CurriculumSubjectMapping csubjectMap = Mono.orm().newSearch(Database.connect().curriculum_subject())
                    .eq(DB.curriculum_subject().CURRICULUM_id, curriculumID)
                    .eq(DB.curriculum_subject().SUBJECT_id, crlMap.getSUBJECT_id_get())
                    .execute()
                    .first();
            if (csubjectMap != null) {
                if (csubjectMap.getActive() == 1) {
                    yearSem = getYearLevel(csubjectMap.getYear()) + "-" + getSemester(csubjectMap.getSemester());
                } else {
                    isAPrerequisite = false;
                    crlMap.setActive(0);
                    crlMap.setRemoved_by(CREATED_BY);
                    crlMap.setRemoved_date(CREATED_DATE);
                    if (!Database.connect().curriculum_requisite_line().update(crlMap)) {
                        logs("C_REQUISITE_LINE MAP NOT UPDATED TO ACTIVE = 0");
                    }
                }
            }
        } else {
            isAPrerequisite = false;

            CurriculumRequisiteExtMapping creMap = Mono.orm().newSearch(Database.connect().curriculum_requisite_ext())
                    .eq(DB.curriculum_requisite_ext().CURRICULUM_id, this.curriculumID)
                    .eq(DB.curriculum_requisite_ext().SUBJECT_id_req, subject.getId())
                    .eq(DB.curriculum_requisite_ext().type, SubjectClassification.REQUITE_TYPE_CO)
                    .active(Order.desc(DB.curriculum_requisite_ext().id))
                    .first();
            if (creMap != null) {
                isACoRequisite = true;
                SubjectMapping subjectGet = Mono.orm().newSearch(Database.connect().subject())
                        .eq(DB.subject().id, creMap.getSUBJECT_id_get())
                        .active()
                        .first();
                code = subjectGet.getCode();
                CurriculumSubjectMapping csubjectMap = Mono.orm().newSearch(Database.connect().curriculum_subject())
                        .eq(DB.curriculum_subject().CURRICULUM_id, curriculumID)
                        .eq(DB.curriculum_subject().SUBJECT_id, creMap.getSUBJECT_id_get())
                        .execute()
                        .first();
                if (csubjectMap != null) {
                    if (csubjectMap.getActive() == 1) {
                        yearSem = getYearLevel(csubjectMap.getYear()) + "-" + getSemester(csubjectMap.getSemester());
                    } else {
                        isACoRequisite = false;
                        creMap.setActive(0);
                        creMap.setRemoved_by(CREATED_BY);
                        creMap.setRemoved_date(CREATED_DATE);
                        if (!Database.connect().curriculum_requisite_ext().update(creMap)) {
                            logs("C_REQUISITE_EXT MAP NOT UPDATED TO ACTIVE = 0");
                        }
                    }
                }
            } else {
                isACoRequisite = false;
            }
        }

        if (isImplemented) {
            Mono.fx().alert()
                    .createWarning()
                    .setHeader("Subject Cannot Be Remove")
                    .setMessage("It seems like this subject has an implemented curriculum, therefore removing is prohibited.")
                    .showAndWait();
            return;
        } else if (isAPrerequisite) {
            Mono.fx().alert()
                    .createWarning()
                    .setHeader("Subject Cannot Be Remove")
                    .setMessage("This subject is a pre-requisite of " + code + " from " + yearSem
                            + ", therefore removing is not allowed.")
                    .showAndWait();
        } else if (isACoRequisite) {
            Mono.fx().alert()
                    .createWarning()
                    .setHeader("Subject Cannot Be Remove")
                    .setMessage("This subject is a co-requisite of " + code + " from " + yearSem
                            + ", therefore removing is not allowed.")
                    .showAndWait();
        } else {
            int res = Mono.fx().alert()
                    .createConfirmation()
                    .setHeader("Remove Subject")
                    .setMessage("Are you sure you want to remove this subject?")
                    .confirmYesNo();
            if (res == 1) {
                if (!insertRemoveSubjectHistory(subject)) {
                    return;
                }
                csMap.setActive(0);
                csMap.setRemoved_by(CREATED_BY);
                csMap.setRemoved_date(CREATED_DATE);
                if (Database.connect().curriculum_subject().update(csMap)) {
                    subjectTable.getChildren().remove(row);
                    Double ttlSubjects = Double.valueOf(lbl_subjects.getText());
                    Double ttlUnits = Double.valueOf(lbl_units.getText());
                    lbl_subjects.setText((ttlSubjects - 1) + "");
                    lbl_units.setText((ttlUnits - (subject.getLab_units() + subject.getLec_units())) + "");
                    Notifications.create()
                            .title("Removed Successfully")
                            .text(subject.getCode() + ": " + subject.getDescriptive_title())
                            .showInformation();
                    isUpdated = true;
                } else {
                    Notifications.create()
                            .title("Remove Failed")
                            .text(subject.getCode() + ": " + subject.getDescriptive_title())
                            .showError();
                }
            }
        }
    }

    private boolean insertRemoveSubjectHistory(SubjectMapping subject) {
        CurriculumSubjectMapping csubjectMap = Mono.orm().newSearch(Database.connect().curriculum_subject())
                .eq(DB.curriculum_subject().CURRICULUM_id, curriculumID)
                .eq(DB.curriculum_subject().SUBJECT_id, subject.getId())
                .active()
                .first();
        String yearSemester = getYearLevel(csubjectMap.getYear()) + "-" + getSemester(csubjectMap.getSemester());

        CurriculumHistorySummaryMapping chsMap = new CurriculumHistorySummaryMapping();
        chsMap.setActive(1);
        chsMap.setCreated_by(CREATED_BY);
        chsMap.setCreated_date(CREATED_DATE);
        chsMap.setCurriculum_id(curriculumID);
        chsMap.setDescription("REMOVED SUBJECT [ID:" + subject.getId() + "] " + subject.getCode()
                + " FROM " + yearSemester.toUpperCase());
        if (Database.connect().curriculum_history_summary().insert(chsMap) == -1) {
            logs("HISTORY NOT SAVED");
            return false;
        }
        return true;
    }

    private String getYearLevel(Integer year) {
        String yearLevel = "";
        switch (year) {
            case 1:
                yearLevel = "First Year";
                break;
            case 2:
                yearLevel = "Second Year";
                break;
            case 3:
                yearLevel = "Third Year";
                break;
            case 4:
                yearLevel = "Fourth Year";
                break;
        }
        return yearLevel;
    }

    private String getSemester(Integer sem) {
        String semester = "";
        switch (sem) {
            case 1:
                semester = "First Semester";
                break;
            case 2:
                semester = "Second Semester";
                break;
        }
        return semester;
    }

    private String name, description = "", major = "", laderized = "", ladderType = "";

    private void validate() {
        name = MonoString.removeExtraSpace(txt_name.getText()).toUpperCase();
        try {
            description = MonoString.removeExtraSpace(txt_description.getText()).toUpperCase();
            major = MonoString.removeExtraSpace(txt_major.getText()).toUpperCase();
        } catch (NullPointerException a) {
        }
        if (name.isEmpty()) {
            Mono.fx().alert()
                    .createWarning()
                    .setHeader("Invalid Name")
                    .setMessage("Please provide a name for this Curriculum.")
                    .showAndWait();
            return;
        }

        CurriculumMapping nameExist = Mono.orm()
                .newSearch(Database.connect().curriculum())
                .eq(DB.curriculum().name, name)
                .eq(DB.curriculum().ACADPROG_id, CURRICULUM.getACADPROG_id())
                .execute()
                .first();
        if (nameExist != null) {
            if (!Objects.equals(nameExist.getId(), CURRICULUM.getId()) && nameExist.getActive() == 1) {
                Mono.fx().alert()
                        .createWarning()
                        .setHeader("Curriculum Name Exist")
                        .setMessage("Please provide a a unique name for this Curriculum.")
                        .showAndWait();
                return;
            } else if (nameExist.getActive() != 1) {
                Mono.fx().alert()
                        .createWarning()
                        .setHeader("Curriculum Content Restricted")
                        .setMessage("Curriculum name exist but inactive. Please provide a a unique name for this curriculum.")
                        .showAndWait();
                return;
            }
        }

        if (description == null || description.isEmpty()) {
            description = "NO DESCRIPTION";
        }

        if (major == null || major.isEmpty()) {
            major = NONE;
        }

        if (isAnyChangeMade()) {
            insertCurriculumHistory();
            if (didUpdateDB()) {
                insertSummary();
            }
        }
    }

    private boolean insertCurriculumHistory() {
        CurriculumHistoryMapping chMap = new CurriculumHistoryMapping();
        chMap.setActive(1);
        chMap.setCURRICULUM_id(curriculumID);
        chMap.setCreated_by(CURRICULUM.getCreated_by());
        chMap.setCreated_date(CURRICULUM.getCreated_date());
        chMap.setDescription(CURRICULUM.getDescription());
        chMap.setImplementation_date(CURRICULUM.getImplementation_date());
        chMap.setImplemented(CURRICULUM.getImplemented());
        chMap.setImplemented_by(CURRICULUM.getImplemented_by());
        chMap.setLadderization(CURRICULUM.getLadderization());
        chMap.setLadderization_type(CURRICULUM.getLadderization_type());
        chMap.setMajor(CURRICULUM.getMajor());
        chMap.setName(CURRICULUM.getName());
        chMap.setRemoved_by(CURRICULUM.getRemoved_by());
        chMap.setRemoved_date(CURRICULUM.getRemoved_date());
        chMap.setStudy_years(CURRICULUM.getStudy_years());
        chMap.setUpdated_by(CURRICULUM.getUpdated_by());
        chMap.setUpdated_date(CURRICULUM.getUpdated_date());
        return (Database.connect().curriculum_history().insert(chMap) != -1);
    }

    private ArrayList<String> changedCol = new ArrayList<>();

    private Integer insertSummary() {
        CurriculumHistorySummaryMapping chsMap = new CurriculumHistorySummaryMapping();
        chsMap.setActive(1);
        chsMap.setCreated_by(CREATED_BY);
        chsMap.setCreated_date(CREATED_DATE);
        chsMap.setCurriculum_id(curriculumID);
        String summaryDescription = "";
        if (changedCol.isEmpty()) {
            return -1;
        }
        for (String changeCol : changedCol) {
            summaryDescription += (summaryDescription.isEmpty() ? changeCol : (", " + changeCol));
        }
        chsMap.setDescription(summaryDescription + (changedCol.size() > 1 ? " OF THE CURRICULUM ARE UPDATED" : " OF THE CURRICULUM IS UPDATED."));
        return Database.connect().curriculum_history_summary().insert(chsMap);
    }

    private boolean didUpdateDB() {
        changedCol.clear();
        if (!CURRICULUM.getDescription().equalsIgnoreCase(description)) {
            CURRICULUM.setDescription(description);
            changedCol.add("DESCRIPTION");
        }
        if (!CURRICULUM.getMajor().equalsIgnoreCase(major)) {
            CURRICULUM.setMajor(major);
            changedCol.add("MAJOR");
        }
        if (!CURRICULUM.getName().equalsIgnoreCase(name)) {
            CURRICULUM.setName(name);
            changedCol.add("CURRICULUM NAME");
        }

        //
        if (!CURRICULUM.getLadderization().equalsIgnoreCase(laderized)) {
            CURRICULUM.setLadderization(laderized);
            if (laderized.equalsIgnoreCase("YES")) {
                CURRICULUM.setStudy_years(2);
            } else {
                CURRICULUM.setStudy_years(4);
                this.clearAllPrimaryExcept(null, false);
                this.clearAllEquivalent();
            }
            changedCol.add("LADDERIZATION");
            changedCol.add("STUDY YEARS");
        }
        if (!CURRICULUM.getLadderization_type().equalsIgnoreCase(ladderType)) {
            CURRICULUM.setLadderization_type(ladderType);
            changedCol.add("LADDERIZATION TYPE");
        }

        if (ladderType.equalsIgnoreCase(CONSEQUENT)) {
            String name = cmb_preparatory.getSelectionModel().getSelectedItem();
            CurriculumMapping prep = null;
            if (allCurriculums != null) {
                for (CurriculumMapping currentCurriculum : allCurriculums) {
                    if (currentCurriculum.getName().equalsIgnoreCase(name)) {
                        prep = currentCurriculum;
                        break;
                    }
                }

                // PRIMARY
                if (prep != null) {
                    boolean isPrimaryChanged = clearAllPrimaryExcept(prep.getId(), false);
                    if (isPrimaryChanged) {
                        CurriculumPreMapping new_pre = new CurriculumPreMapping();
                        new_pre.setActive(1);
                        new_pre.setCreated_by(CREATED_BY);
                        new_pre.setCreated_date(CREATED_DATE);
                        new_pre.setCur_type("PRIMARY");
                        new_pre.setCurriculum_id_get(CURRICULUM.getId());
                        new_pre.setCurriculum_id_req(prep.getId());
                        Integer res = Database.connect().curriculum_pre().insert(new_pre);
                        if (res.equals(-1)) {
                            System.out.println("NEW PRIMARY FOR CURRICULUM " + CURRICULUM.getName() + " IS NOT INSERTED.");
                        } else {
                            changedCol.add("CURRICULUM PRIMARY");
                        }
                    }
                }
            }

            // EQUIVALENT
            clearAllEquivalent();
            ArrayList<CurriculumMapping> equivalents = new ArrayList<>();
            ObservableList<String> checked = cmb_prereq.getCheckModel().getCheckedItems();
            if (checked != null) {
                if (allCurriculums != null) {
                    for (String check : checked) {
                        for (CurriculumMapping currentCurriculum : allCurriculums) {
                            if (currentCurriculum.getName().equalsIgnoreCase(check)) {
                                equivalents.add(currentCurriculum);
                            }
                        }
                    }
                }

                if (!equivalents.isEmpty()) {
                    for (CurriculumMapping equivalent : equivalents) {
                        CurriculumPreMapping new_eq = new CurriculumPreMapping();
                        new_eq.setActive(1);
                        new_eq.setCreated_by(CREATED_BY);
                        new_eq.setCreated_date(CREATED_DATE);
                        new_eq.setCur_type("EQUIVALENT");
                        new_eq.setCurriculum_id_get(CURRICULUM.getId());
                        new_eq.setCurriculum_id_req(equivalent.getId());
                        Integer res = Database.connect().curriculum_pre().insert(new_eq);
                        if (res.equals(-1)) {
                            System.out.println("NEW EQUIVALENT FOR CURRICULUM " + CURRICULUM.getName() + " IS NOT INSERTED.");
                        } else {
                            changedCol.add("CURRICULUM EQUIVALENT");
                        }
                    }
                }
            }
        }
        CURRICULUM.setUpdated_by(CREATED_BY);
        CURRICULUM.setUpdated_date(CREATED_DATE);
        if (Database.connect().curriculum().update(CURRICULUM)) {
            Notifications.create()
                    .title("Updated Successfully")
                    .text("Curriculum successfully updated in the database.")
                    .showInformation();
            if (!description.equalsIgnoreCase("NO DESCRIPTION")) {
                txt_description.setText(description);
            } else {
                txt_description.setText("");
            }
            if (!major.equalsIgnoreCase(NONE)) {
                txt_major.setText(major);
            }
            txt_name.setText(name);
            isUpdated = true;
        } else {
            Notifications.create()
                    .title("Update Failed")
                    .text("Database connection might have a problem.")
                    .showError();
        }
        return (!changedCol.isEmpty());
    }

    private boolean isAnyChangeMade() {
        if (!CURRICULUM.getDescription().equalsIgnoreCase(description)) {
            changedCol.add("DESCRIPTION");
        }
        if (!CURRICULUM.getMajor().equalsIgnoreCase(major)) {
            changedCol.add("MAJOR");
        }
        if (!CURRICULUM.getName().equalsIgnoreCase(name)) {
            changedCol.add("CURRICULUM NAME");
        }
        if (!CURRICULUM.getLadderization().equalsIgnoreCase(laderized)) {
            changedCol.add("LADDERIZATION");
            changedCol.add("STUDY YEARS");
        }
        if (!CURRICULUM.getLadderization_type().equalsIgnoreCase(ladderType)) {
            changedCol.add("LADDERIZATION TYPE");
        }
        if (allCurriculums != null) {
            CurriculumMapping prep = null;
            for (CurriculumMapping currentCurriculum : allCurriculums) {
                if (currentCurriculum.getName().equalsIgnoreCase(name)) {
                    prep = currentCurriculum;
                    break;
                }
            }
            if (prep !=null && clearAllPrimaryExcept(prep.getId(), true)) {
                changedCol.add("CURRICULUM PRIMARY");
            }

            // EQUIVALENTT
            ObservableList<String> checked = cmb_prereq.getCheckModel().getCheckedItems();
            ArrayList<CurriculumMapping> equivalents = new ArrayList<>();
            for (String check : checked) {
                for (CurriculumMapping currentCurriculum : allCurriculums) {
                    if (currentCurriculum.getName().equalsIgnoreCase(check)) {
                        equivalents.add(currentCurriculum);
                    }
                }
            }
            if (!equivalents.isEmpty()) {
                changedCol.add("CURRICULUM EQUIVALENT");
            }
        }
        return (!changedCol.isEmpty());
    }

    private boolean clearAllPrimaryExcept(Integer id, boolean forChecking) {
        boolean isPrimaryChanged = true;
        ArrayList<CurriculumPreMapping> prevs = Mono.orm().newSearch(Database.connect().curriculum_pre())
                .eq(DB.curriculum_pre().curriculum_id_get, CURRICULUM.getId())
                .eq(DB.curriculum_pre().cur_type, "PRIMARY")
                .active().all();
        if (prevs != null) {
            System.out.println(prevs.size());
            for (CurriculumPreMapping prev : prevs) {
                if (id != null) {
                    if (id.equals(prev.getCurriculum_id_req())) {
                        isPrimaryChanged = false;
                        System.out.println("SKIP");
                        continue;
                    }
                }
                System.out.println("REMOVED");
                if (!forChecking) {
                    prev.setActive(0);
                    prev.setRemoved_by(CREATED_BY);
                    prev.setRemoved_date(CREATED_DATE);
                    if (!Database.connect().curriculum_pre().update(prev)) {
                        System.out.println("CURRICULUM_pre with an ID of " + prev.getId() + " is not removed.");
                    }
                }
            }
        }
        return isPrimaryChanged;
    }

    private void clearAllEquivalent() {
        ArrayList<CurriculumPreMapping> prevs_eq = Mono.orm().newSearch(Database.connect().curriculum_pre())
                .eq(DB.curriculum_pre().curriculum_id_get, CURRICULUM.getId())
                .eq(DB.curriculum_pre().cur_type, "EQUIVALENT")
                .active().all();
        if (prevs_eq != null) {
            for (CurriculumPreMapping prev : prevs_eq) {
                prev.setActive(0);
                prev.setRemoved_by(CREATED_BY);
                prev.setRemoved_date(CREATED_DATE);
                if (!Database.connect().curriculum_pre().update(prev)) {
                    System.out.println("CURRICULUM_pre with an ID of " + prev.getId() + " is not removed.");
                }
            }
        }
    }

    private boolean isUpdated = false;

    public boolean isUpdated() {
        return isUpdated;
    }
}

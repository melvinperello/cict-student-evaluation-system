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
import app.lazy.models.CurriculumHistorySummaryMapping;
import app.lazy.models.CurriculumMapping;
import app.lazy.models.CurriculumPreMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import artifacts.MonoString;
import com.izum.fx.textinputfilters.StringFilter;
import com.izum.fx.textinputfilters.TextInputFilters;
import com.jhmvin.Mono;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import java.util.ArrayList;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.controlsfx.control.CheckComboBox;
import org.hibernate.criterion.Order;
import sys.org.cict.enumerations.CurriculumValues;

/**
 *
 * @author Joemar
 */
public class AddNewCurriculumController extends SceneFX implements ControllerFX {

    @FXML
    private AnchorPane application_root;
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
    private Button btn_add;
    @FXML
    private Button btn_cancel;
    private final ToggleGroup group = new ToggleGroup();
    private CurriculumMapping newCurriculum = new CurriculumMapping();
    private ArrayList<Integer> validStudyYears = new ArrayList<>();
    private ObservableList<String> ladderTypes = FXCollections.observableArrayList();
    private ArrayList<CurriculumMapping> allCurriculums = new ArrayList<>();

    private String NONE = CurriculumValues.LadderType.NONE.toString();
    private String PREPARATORY = CurriculumValues.LadderType.PREPARATORY.toString();
    private String CONSEQUENT = CurriculumValues.LadderType.CONSEQUENT.toString();

    private String TYPE_PRIMARY = CurriculumValues.RequisiteType.PRIMARY.toString();
    private String TYPE_EQUIVALENT = CurriculumValues.RequisiteType.EQUIVALENT.toString();

    private Integer CREATED_BY = CollegeFaculty.instance().getFACULTY_ID();
    private Date CREATED_DATE = Mono.orm().getServerTime().getDateWithFormat();

    public AcademicProgramMapping academicProgram;

    @Override
    public void onInitialization() {
        /**
         * group the radio button
         */
        rbtn_yes.setToggleGroup(group);
        rbtn_no.setToggleGroup(group);
        /**
         * add the valid study years for checking
         */
        validStudyYears.add(2);
        validStudyYears.add(4);
        /**
         * add values in the type
         */
        ladderTypes.add(PREPARATORY);
        ladderTypes.add(CONSEQUENT);
        cmb_type.getItems().addAll(ladderTypes);

        /**
         * get all the curriculum for pre req
         */
        allCurriculums = Mono.orm()
                .newSearch(Database.connect().curriculum())
                .active()
                .all();
        if (allCurriculums != null) {
            cmb_prereq.getItems().clear();
            for (CurriculumMapping currentCurriculum : allCurriculums) {
                cmb_prereq.getItems().add(currentCurriculum.getName());
                cmb_preparatory.getItems().add(currentCurriculum.getName());
            }
        }

        /**
         * by default
         */
        cmb_type.getItems().add(NONE);
        cmb_type.getSelectionModel().select(NONE);
        cmb_type.setDisable(true);
        setViewCheckCmbBoxPreReq("NO");

        addTextFieldFilters();
    }

    private void addTextFieldFilters() {
        StringFilter textField = TextInputFilters.string()
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
        textField.clone().setTextSource(txt_name).applyFilter();
        textField.clone().setTextSource(txt_major).applyFilter();
        textField.clone().setTextSource(txt_description).applyFilter();

    }

    @Override
    public void onEventHandling() {
        addClickEvent(btn_add, () -> {
            addNew();
        });

        addClickEvent(btn_cancel, () -> {
            Mono.fx().getParentStage(btn_add).close();
        });

        addClickEvent(rbtn_yes, () -> {
            cmb_type.setDisable(false);
            cmb_type.getItems().remove(NONE);
//            cmb_prereq.getItems().remove(NONE);

            setViewCheckCmbBoxPreReq("YES");
        });

        addClickEvent(rbtn_no, () -> {
            cmb_type.getItems().add(NONE);
            cmb_type.getSelectionModel().select(NONE);
            cmb_type.setDisable(true);
            setViewCheckCmbBoxPreReq("NO");
        });

        cmb_type.valueProperty().addListener((e) -> {
            String selected = cmb_type.getSelectionModel().getSelectedItem();
            setViewCheckCmbBoxPreReq(selected);
        });

        Mono.fx().key(KeyCode.ENTER).release(application_root, () -> {
            addNew();
        });
    }

    private void setViewCheckCmbBoxPreReq(String mode) {
        if (mode.equalsIgnoreCase(PREPARATORY)
                || mode.equalsIgnoreCase("NO")) {
            cmb_prereq.getCheckModel().clearChecks();
            cmb_prereq.getItems().remove(NONE);
            cmb_prereq.getItems().add(NONE);
            cmb_prereq.getCheckModel().check(NONE);
            cmb_prereq.setDisable(true);
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

    public CurriculumMapping getNewCurriculum() {
        return newCurriculum;
    }

    private String name, description, major, laderized, ladderType;
    private Integer studyYears;

    private void addNew() {
        name = MonoString.removeExtraSpace(txt_name.getText()).toUpperCase();
        description = MonoString.removeExtraSpace(txt_description.getText()).toUpperCase();
        major = MonoString.removeExtraSpace(txt_major.getText()).toUpperCase();
        ladderType = cmb_type.getSelectionModel().getSelectedItem();
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
                .eq(DB.curriculum().ACADPROG_id, academicProgram.getId())
                .execute()
                .first();
        if (nameExist != null) {
            if (nameExist.getActive() == 1) {
                Mono.fx().alert()
                        .createWarning()
                        .setHeader("Curriculum Name Exist")
                        .setMessage("Please provide a a unique name for this Curriculum.")
                        .showAndWait();
            } else {
                int res = Mono.fx().alert()
                        .createConfirmation()
                        .setHeader("Restore Curriculum")
                        .setMessage("Curriculum with a name of " + name
                                + " is existing but inactive. Do you want to restore it?")
                        .confirmYesNo();
                if (res == 1) {
                    nameExist.setActive(1);
                    if (Database.connect().curriculum().update(nameExist)) {
                        Mono.fx().alert()
                                .createInfo()
                                .setHeader("Restored Successfully")
                                .setMessage("Curriculum successfully restored.")
                                .showAndWait();
                        curriculums = Mono.orm().newSearch(Database.connect().curriculum())
                                .eq(DB.curriculum().ACADPROG_id, academicProgram.getId())
                                .active(Order.asc(DB.curriculum().id))
                                .all();
                        insertSummary(nameExist.getId(), "RESTORED CURRICULUM [ID:" + nameExist.getId() + "] "
                                + nameExist.getName());
                        Mono.fx().getParentStage(btn_add).close();
                    }
                }
            }
            return;
        }

        if (description.isEmpty()) {
            description = null;
        }

        if (major.isEmpty()) {
            major = "NONE";
        }

        if (rbtn_yes.isSelected()) {
            laderized = "YES";
            studyYears = 2;
        } else if (rbtn_no.isSelected()) {
            laderized = "NO";
            studyYears = 4;
        }

        /**
         * set values to mapping check if preparatory, just add to curriculum
         * table if consequent, add to curriculum table and curriculum_pre
         */
        Integer newCurriculumID = insertNewCurriculum();
        if (ladderType.equalsIgnoreCase(CONSEQUENT)) {
            String primary = cmb_preparatory.getSelectionModel().getSelectedItem();
            if (allCurriculums != null) {
                for (CurriculumMapping currentCurriculum : allCurriculums) {
                    if (primary.equalsIgnoreCase(currentCurriculum.getName())) {
                        insertNewCurriculumPre(newCurriculumID, currentCurriculum, TYPE_PRIMARY);
                        System.out.println("INSERTED " + TYPE_PRIMARY);
                    }
                }
            }
            String description = "";
            if (CURRICULUM_pre == null) {
                description = "ADDED A NEW CURRICULUM [ID:" + newCurriculumID
                        + "] " + newCurriculum.getName();
            } else {
                CurriculumMapping curriculumGet = Mono.orm().newSearch(Database.connect().curriculum())
                        .eq(DB.curriculum().id, CURRICULUM_pre.getCurriculum_id_req())
                        .active()
                        .first();

                description = "ADDED A NEW CURRICULUM [ID:" + newCurriculumID
                        + "] " + newCurriculum.getName() + "WITH A PREPARATORY CURRICULUM [ID:"
                        + CURRICULUM_pre.getCurriculum_id_req() + "] " + curriculumGet.getName();
            }
            insertSummary(newCurriculumID, description);
            ObservableList<String> checkedItems = cmb_prereq.getCheckModel().getCheckedItems();
            if (checkedItems.contains(NONE)) {
                return;
            }
            if (allCurriculums != null) {
                for (String currentPreReq : checkedItems) {
                    for (CurriculumMapping currentCurriculum : allCurriculums) {
                        if (currentCurriculum.getName().equalsIgnoreCase(currentPreReq)) {
                            System.out.println("INSERTED " + TYPE_EQUIVALENT);
                            insertNewCurriculumPre(newCurriculumID, currentCurriculum, TYPE_EQUIVALENT);
                            break;
                        }
                    }
                }
            }
        }
    }

    private Integer insertNewCurriculum() {
        newCurriculum = new CurriculumMapping();
        newCurriculum.setACADPROG_id(academicProgram.getId());
        newCurriculum.setActive(1);
        newCurriculum.setCreated_by(CREATED_BY);
        newCurriculum.setCreated_date(CREATED_DATE);
        newCurriculum.setDescription(description);
        newCurriculum.setLadderization(laderized);
        newCurriculum.setLadderization_type(ladderType);
        newCurriculum.setMajor(major);
        newCurriculum.setName(name);
        newCurriculum.setStudy_years(studyYears);
        Integer res = Database.connect().curriculum().insert(newCurriculum);
        if (res != -1) {
            Mono.fx().alert()
                    .createInfo()
                    .setHeader("Successfully Added")
                    .setMessage("The curriculum is successfully added to this academic program.")
                    .showAndWait();
            curriculums = Mono.orm().newSearch(Database.connect().curriculum())
                    .eq(DB.curriculum().ACADPROG_id, academicProgram.getId())
                    .active(Order.asc(DB.curriculum().id))
                    .all();
            Mono.fx().getParentStage(btn_add).close();
        } else {
            Mono.fx().alert()
                    .createInfo()
                    .setHeader("Adding Failed")
                    .setMessage("Something went wrong in the adding process. Please try again later.")
                    .showAndWait();
        }
        return res;
    }

    private ArrayList<CurriculumMapping> curriculums;

    public ArrayList<CurriculumMapping> getCurriculums() {
        return curriculums;
    }

    private CurriculumPreMapping CURRICULUM_pre;

    private void insertNewCurriculumPre(Integer newCurriculumID, CurriculumMapping pre_req, String type) {
        CURRICULUM_pre = new CurriculumPreMapping();
        CURRICULUM_pre.setActive(1);
        CURRICULUM_pre.setCreated_by(CREATED_BY);
        CURRICULUM_pre.setCreated_date(CREATED_DATE);
        CURRICULUM_pre.setCurriculum_id_get(newCurriculumID);
        CURRICULUM_pre.setCurriculum_id_req(pre_req.getId());
        CURRICULUM_pre.setCur_type(type);
        Integer res = Database.connect().curriculum_pre().insert(CURRICULUM_pre);
        if (res == -1) {
            System.err.println("CURRICULUM_PRE NOT SAVED");
        }
    }

    private void insertSummary(Integer CURRICULUM_id, String description) {
        CurriculumHistorySummaryMapping chsMap = new CurriculumHistorySummaryMapping();
        chsMap.setActive(1);
        chsMap.setCreated_by(CREATED_BY);
        chsMap.setCreated_date(CREATED_DATE);
        chsMap.setCurriculum_id(CURRICULUM_id);

        chsMap.setDescription(description);
        if (Database.connect().curriculum_history_summary().insert(chsMap) == -1) {
            System.out.println("CURRICULUM HISTORY NOT SAVED");
        }
    }
}

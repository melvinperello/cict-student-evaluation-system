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
package org.cict.accountmanager.faculty;

import app.lazy.models.AccountFacultyMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.FacultyMapping;
import artifacts.MonoString;
import com.izum.fx.textinputfilters.StringFilter;
import com.izum.fx.textinputfilters.TextInputFilters;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jhmvin.Mono;
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
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.text.WordUtils;
import org.controlsfx.control.Notifications;
import update.org.cict.controller.home.Home;
import update3.org.cict.SectionConstants;

/**
 *
 * @author Joemar
 */
public class FacultyMainController extends SceneFX implements ControllerFX {

    @FXML
    private AnchorPane application_root;

    @FXML
    private JFXButton btn_home;

    @FXML
    private VBox vbox_info;

    @FXML
    private VBox vbox_list;

    @FXML
    private JFXCheckBox chkbx_show_disabled;

    @FXML
    private ComboBox<String> cmb_sort;

    @FXML
    private JFXButton btn_new_faculty;

    @FXML
    private TextField txt_bulsu_id;

    @FXML
    private TextField txt_lastname;

    @FXML
    private TextField txt_firstname;

    @FXML
    private TextField txt_middlename;

    @FXML
    private RadioButton rbtn_male_new;

    @FXML
    private RadioButton rbtn_female_new;

    @FXML
    private TextField txt_rank_new;

    @FXML
    private TextField txt_designation_new;

    @FXML
    private TextField txt_dept;

    @FXML
    private JFXButton btn_save_new;

    @FXML
    private JFXButton btn_back_new;

    @FXML
    private AnchorPane anchor_new_faculty;

    @FXML
    private JFXButton btn_search;

    @FXML
    private TextField txt_search;

    @FXML
    private HBox hbox_tools;

    public FacultyMainController() {

    }

    @Override
    public void onInitialization() {
        super.bindScene(application_root);
        this.fetchFaculty();
        this.cmb_sort.getItems().clear();
        this.cmb_sort.getItems().add("Active");
        this.cmb_sort.getItems().add("Inactive");
        this.cmb_sort.getSelectionModel().selectFirst();

        ToggleGroup group2 = new ToggleGroup();
        rbtn_male_new.setToggleGroup(group2);
        rbtn_female_new.setToggleGroup(group2);
        addTextFieldFilters();
    }

    private void addTextFieldFilters() {
        StringFilter textId = TextInputFilters.string()
                //                .setFilterMode(StringFilter.LETTER_DIGIT)
                .setMaxCharacters(50)
                .setNoLeadingTrailingSpaces(true)
                .setFilterManager(filterManager -> {
                    if (!filterManager.isValid()) {
                        Mono.fx().alert().createWarning().setHeader("Warning")
                                .setMessage(filterManager.getMessage())
                                .show();
                    }
                });
        textId.clone().setTextSource(txt_bulsu_id).applyFilter();

        StringFilter text50Max = TextInputFilters.string()
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
        text50Max.clone().setTextSource(txt_dept).applyFilter();
        text50Max.clone().setTextSource(txt_designation_new).applyFilter();
        text50Max.clone().setTextSource(txt_rank_new).applyFilter();

        StringFilter textName = TextInputFilters.string()
                .setFilterMode(StringFilter.LETTER_SPACE)
                .setMaxCharacters(100)
                .setNoLeadingTrailingSpaces(false)
                .setFilterManager(filterManager -> {
                    if (!filterManager.isValid()) {
                        Mono.fx().alert().createWarning().setHeader("Warning")
                                .setMessage(filterManager.getMessage())
                                .show();
                    }
                });
        textName.clone().setTextSource(txt_lastname).applyFilter();
        textName.clone().setTextSource(txt_firstname).applyFilter();
        textName.clone().setTextSource(txt_middlename).applyFilter();
    }

    @Override
    public void onEventHandling() {
        // back to home
        super.addClickEvent(btn_home, () -> {
            this.onBackToHome();
        });

        this.cmb_sort.valueProperty().addListener((observable) -> {
            refreshInfo = true;
            if (cmb_sort.getSelectionModel().getSelectedIndex() == 1) {
                createFacultyTable(deactivatedFaculty);
            } else {
                createFacultyTable(activeFaculty);
            }
        });

        /**
         * New Faculty
         */
        this.addClickEvent(btn_new_faculty, () -> {
            txt_bulsu_id.setText("");
            txt_designation_new.setText("");
            txt_firstname.setText("");
            txt_lastname.setText("");
            txt_middlename.setText("");
            txt_rank_new.setText("");
            txt_dept.setText("");
            anchor_new_faculty.setVisible(true);
            this.hbox_tools.setDisable(true);
        });

        /**
         * Add
         */
        this.addClickEvent(btn_save_new, () -> {
            addNewFaculty();
        });

        /**
         * Cancel creation.
         */
        this.addClickEvent(btn_back_new, () -> {
            anchor_new_faculty.setVisible(false);
            //
            this.hbox_tools.setDisable(false);
        });

        this.addClickEvent(btn_search, () -> {
            onSearch();
        });

        Mono.fx().key(KeyCode.ENTER).release(application_root, () -> {
            onSearch();
        });
    }

    private void onSearch() {
        String key = MonoString.removeExtraSpace(txt_search.getText().toUpperCase());
        if (key == null) {
            key = "";
        }
        if (key.isEmpty()) {
            if (cmb_sort.getSelectionModel().getSelectedIndex() == 0) {
                createFacultyTable(activeFaculty);
            } else {
                createFacultyTable(deactivatedFaculty);
            }
            return;
        }
        ArrayList<FacultyInformation> temp_search = new ArrayList<>();
        if (cmb_sort.getSelectionModel().getSelectedIndex() == 0) {
            for (FacultyInformation active : activeFaculty) {
                if (active.getFullName().contains(key)) {
                    temp_search.add(active);
                } else if (active.getBulsuID().contains(key)) {
                    temp_search.add(active);
                }
            }
        } else {
            for (FacultyInformation inactive : deactivatedFaculty) {
                if (inactive.getFullName().contains(key)) {
                    temp_search.add(inactive);
                } else if (inactive.getBulsuID().contains(key)) {
                    temp_search.add(inactive);
                }
            }
        }
        createFacultyTable(temp_search);
    }

    private void onBackToHome() {
        Home.callHome(this);
    }

    private ArrayList<FacultyInformation> activeFaculty = new ArrayList<>();
    private ArrayList<FacultyInformation> deactivatedFaculty = new ArrayList<>();

    public void fetchFaculty() {
        FetchFacultyInfo ffInfo = new FetchFacultyInfo();
        ffInfo.setOnSuccess(onSuccess -> {
            activeFaculty = ffInfo.getAllFaculty();
            deactivatedFaculty = ffInfo.getDeactivatedFaculty();
            this.createFacultyTable(activeFaculty);
        });
        ffInfo.setOnFailure(onFailure -> {
            System.out.println("FAILED");
        });
        ffInfo.transact();
    }

    private final static String KEY_MORE_INFO = "MORE_INFO";
    private SimpleTable tableFaculty = new SimpleTable();

    private void createFacultyTable(ArrayList<FacultyInformation> facultyToDisplay) {
        try {
            tableFaculty.getChildren().clear();
            if (facultyToDisplay.isEmpty()) {
                System.out.println("EMPTY");
                return;
            }
            for (FacultyInformation currentFacultyInfo : facultyToDisplay) {
                createRow(currentFacultyInfo, tableFaculty);
            }

            SimpleTableView simpleTableView = new SimpleTableView();
            simpleTableView.setTable(tableFaculty);
            simpleTableView.setFixedWidth(true);
            simpleTableView.setParentOnScene(vbox_list);
        } catch (NullPointerException a) {

        }
    }

    private boolean refreshInfo = true;

    private void createRow(FacultyInformation currentFacultyInfo, SimpleTable table) {

        FacultyMapping faculty = currentFacultyInfo.getFacultyMapping();
        SimpleTableRow row = new SimpleTableRow();
        row.setRowHeight(70.0);

        HBox facultyRow = (HBox) Mono.fx().create()
                .setPackageName("org.cict.accountmanager.faculty.layout")
                .setFxmlDocument("faculty-row")
                .makeFX()
                .pullOutLayout();

        VBox bg = searchAccessibilityText(facultyRow, "bg");
        Label lbl_id = searchAccessibilityText(facultyRow, "id");
        Label lbl_name = searchAccessibilityText(facultyRow, "name");
        Label lbl_access = searchAccessibilityText(facultyRow, "access");
        lbl_id.setText(faculty.getBulsu_id().toUpperCase());
        lbl_name.setText(WordUtils.capitalizeFully(faculty.getFirst_name() + " " + faculty.getLast_name()));
        lbl_access.setText(faculty.getDesignation());

        SimpleTableCell cellParent = new SimpleTableCell();
        cellParent.setResizePriority(Priority.ALWAYS);
        cellParent.setContent(facultyRow);

        row.addCell(cellParent);

//        if (refreshInfo) {
//            createInfo(currentFacultyInfo, lbl_id, lbl_name, row);
//            refreshInfo = false;
//        }
        row.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            createInfo(currentFacultyInfo, lbl_id, lbl_name, row);
            anchor_new_faculty.setVisible(false);
        });
        row.getRowMetaData().put(KEY_MORE_INFO, faculty);
        table.addRow(row);
    }

    private Date DATE_TODAY = Mono.orm().getServerTime().getDateWithFormat();
    private final String SECTION_BASE_COLOR = "#414852";

    private void createInfo(FacultyInformation currentFacultyInfo, Label lbl_bulsu_id,
            Label lbl_full_name,
            SimpleTableRow rowFaculty) {

        FacultyInfoController controller = new FacultyInfoController(currentFacultyInfo);

        LayoutDataFX homeFX = new LayoutDataFX(application_root, this);
        controller.setHomeFx(homeFX);

        Pane pane = Mono.fx().create()
                .setPackageName("org.cict.accountmanager.faculty.layout")
                .setFxmlDocument("faculty-info")
                .makeFX()
                .setController(controller)
                .pullOutLayout();

        super.setSceneColor(SECTION_BASE_COLOR); // call once on entire scene lifecycle

        Animate.fade(this.application_root, SectionConstants.FADE_SPEED, () -> {
            super.replaceRoot(pane);
        }, pane);

        if (true) {
            return;
        }
        //-------------------------------------------------------------------------
        AnchorPane infoRow = (AnchorPane) Mono.fx().create()
                .setPackageName("org.cict.accountmanager.faculty")
                .setFxmlDocument("info")
                .makeFX()
                .pullOutLayout();

        FacultyMapping faculty = currentFacultyInfo.getFacultyMapping();
        SimpleTable tableInfo = new SimpleTable();
        SimpleTableRow row = new SimpleTableRow();
        row.setRowHeight(770.0);

        AccountFacultyMapping afMap = currentFacultyInfo.getAccountFacultyMapping();

        /**
         * ANCHOR
         */
        AnchorPane anchor_basic = searchAccessibilityText(infoRow, "anchor_basic");
        AnchorPane anchor_account = searchAccessibilityText(infoRow, "anchor_account");

        /**
         * WINDOW 1 - INFORMATIONS
         */
        Label lbl_name = searchAccessibilityText(infoRow, "name");
        Label lbl_id = searchAccessibilityText(infoRow, "bulsu_id");
        Label lbl_gender = searchAccessibilityText(infoRow, "gender");
        Label lbl_rank = searchAccessibilityText(infoRow, "rank");
        Label lbl_designation = searchAccessibilityText(infoRow, "designation");
        Label lbl_initial_letter = searchAccessibilityText(infoRow, "initial_letter");
        VBox bg = searchAccessibilityText(infoRow, "bg");

        JFXButton btn_basic = searchAccessibilityText(infoRow, "btn_basic");
        this.addClickEvent(btn_basic, () -> {
            anchor_basic.setVisible(true);
        });

        this.setBasicInfo(lbl_id, lbl_name, lbl_gender, lbl_rank, lbl_designation, lbl_initial_letter, bg, faculty);

        Label lbl_username = searchAccessibilityText(infoRow, "username");
        Label lbl_access_level = searchAccessibilityText(infoRow, "access_level");

        //DISABLE
//        Label lbl_since = searchAccessibilityText(infoRow, "since");
//        JFXCheckBox chkbx_disable = searchAccessibilityText(infoRow, "chkbx_disable");
//        VBox vbox_disable = searchAccessibilityText(infoRow, "vbox_disable");
        //BLOCK
        Label lbl_until = searchAccessibilityText(infoRow, "until");
        JFXCheckBox chkbx_block = searchAccessibilityText(infoRow, "chkbx_block");

        if (afMap != null) {
            lbl_username.setText(WordUtils.capitalizeFully(afMap.getUsername()));
            lbl_access_level.setText(WordUtils.capitalizeFully(afMap.getAccess_level()));

            if (afMap.getBlocked_until() != null) {
                if (Mono.orm().getServerTime().isPastServerTime(afMap.getBlocked_until())) {
                    lbl_until.setText("ACTIVE");
                    chkbx_block.setVisible(false);
                } else {
                    lbl_until.setText("BLOCKED until " + afMap.getBlocked_until() + "");
                    chkbx_block.setVisible(true);
                }
            } else {
                lbl_until.setText("ACTIVE");
                chkbx_block.setVisible(false);
            }
            chkbx_block.selectedProperty().addListener((a) -> {
                if (chkbx_block.isSelected()) {
                    System.out.println("HEY");
                    afMap.setBlocked_until(null);
                    System.out.println("PASSED");
                    if (Database.connect().account_faculty().update(afMap)) {
                        Notifications.create().darkStyle()
                                .title("Unblocked Successfully")
                                .text("Account: " + afMap.getUsername().toUpperCase())
                                .showInformation();
                        lbl_until.setText("ACTIVE");
                        chkbx_block.setVisible(false);
                    }
                }
            });

//            if(afMap.getDisabled_since() != null) {
//                chkbx_disable.selectedProperty().set(true);
//                lbl_since.setText(afMap.getDisabled_since()+"");
//            }
//            chkbx_disable.selectedProperty().addListener((b)->{
//                if(chkbx_disable.isSelected()) {
//                    afMap.setActive(0);
//                    afMap.setDisabled_since(DATE_TODAY);
//                    if(Database.connect().account_faculty().update(afMap)) {
//                        Notifications.create().darkStyle()
//                                .title("Disabled Successfully")
//                                .text("Account: " + afMap.getUsername().toUpperCase())
//                                .showInformation();
//                        lbl_since.setText(afMap.getDisabled_since()+"");
//                    }
//                } else {
//                    afMap.setActive(1);
//                    afMap.setDisabled_since(null);
//                    if(Database.connect().account_faculty().update(afMap)) {
//                        Notifications.create().darkStyle()
//                                .title("Enabled Successfully")
//                                .text("Account: " + afMap.getUsername().toUpperCase())
//                                .showInformation();
//                        lbl_since.setText("N / A");
//                    }
//                }
//            });
        } else {
            chkbx_block.setDisable(true);
//            chkbx_disable.setDisable(true);
        }

        /**
         * WINDOW 2 - UPDATE BASIC INFORMATION
         */
        TextField txt_id = searchAccessibilityText(infoRow, "id");
        TextField txt_lastname_info = searchAccessibilityText(infoRow, "lastname");
        TextField txt_firstname_info = searchAccessibilityText(infoRow, "firstname");
        TextField txt_middlename_info = searchAccessibilityText(infoRow, "middlename");
        RadioButton rbtn_male = searchAccessibilityText(infoRow, "rbtn_male");
        RadioButton rbtn_female = searchAccessibilityText(infoRow, "rbtn_female");
        TextField txt_rank = searchAccessibilityText(infoRow, "txt_rank");
        TextField txt_designation = searchAccessibilityText(infoRow, "txt_designation");
        JFXButton btn_save_changes = searchAccessibilityText(infoRow, "btn_save_changes");
        JFXButton btn_back = searchAccessibilityText(infoRow, "back");
        JFXButton btn_remove = searchAccessibilityText(infoRow, "btn_remove");
        JFXButton btn_restore = searchAccessibilityText(infoRow, "btn_restore");

        ToggleGroup group = new ToggleGroup();
        rbtn_male.setToggleGroup(group);
        rbtn_female.setToggleGroup(group);

        this.setValues(txt_id, txt_lastname_info, txt_firstname_info, txt_middlename_info, rbtn_male, rbtn_female, txt_rank, txt_designation, faculty);

        if (faculty.getActive() == 0) {
            btn_remove.setVisible(false);
            btn_restore.setVisible(true);
        } else {
            btn_remove.setVisible(true);
            btn_restore.setVisible(false);
        }

        this.addClickEvent(btn_remove, () -> {
            faculty.setActive(0);
            if (Database.connect().faculty().update(faculty)) {
                Notifications.create().darkStyle()
                        .title("Removed Successfully")
                        .text("Faculty: " + faculty.getBulsu_id().toUpperCase())
                        .showInformation();
                tableFaculty.getChildren().remove(rowFaculty);
                btn_restore.setVisible(true);
                btn_remove.setVisible(false);
                activeFaculty.remove(currentFacultyInfo);
                deactivatedFaculty.add(currentFacultyInfo);
            }
        });

        this.addClickEvent(btn_restore, () -> {
            faculty.setActive(1);
            if (Database.connect().faculty().update(faculty)) {
                Notifications.create().darkStyle()
                        .title("Restored Successfully")
                        .text("Faculty: " + faculty.getBulsu_id().toUpperCase())
                        .showInformation();
                tableFaculty.getChildren().remove(rowFaculty);
                btn_restore.setVisible(false);
                btn_remove.setVisible(true);
                activeFaculty.add(currentFacultyInfo);
                deactivatedFaculty.remove(currentFacultyInfo);
            }
        });

        this.addClickEvent(btn_save_changes, () -> {
            String gender = "";
            if (rbtn_male.isSelected()) {
                gender = "MALE";
            } else if (rbtn_female.isSelected()) {
                gender = "FEMALE";
            }
            String bulsu_id = "", lastname = "", firstname = "", middlename = "", rank = "", designation = "";

            try {
                bulsu_id = MonoString.removeExtraSpace(txt_id.getText().toUpperCase());
                lastname = MonoString.removeExtraSpace(txt_lastname_info.getText().toUpperCase());
                firstname = MonoString.removeExtraSpace(txt_firstname_info.getText().toUpperCase());
            } catch (Exception e) {
                showWarning("Incomplete Data", "Please fill the required fields*.");
                return;
            }

            if (bulsu_id.isEmpty() || lastname.isEmpty() || firstname.isEmpty()) {
                showWarning("Incomplete Data", "Please fill the required fields*.");
                return;
            }
            ArrayList<FacultyMapping> exist = Mono.orm().newSearch(Database.connect().faculty())
                    .eq(DB.faculty().bulsu_id, bulsu_id)
                    .execute().all();
            if (exist != null) {
                for (FacultyMapping eachExist : exist) {
                    if (!Objects.equals(eachExist.getId(), faculty.getId())) {
                        showWarning("BulSU ID Exist", "A faculty is already using\n"
                                + "this BulSU ID.");
                        return;
                    }
                }
            }

            if (MonoString.removeExtraSpace(txt_middlename_info.getText().toUpperCase()) != null) {
                middlename = MonoString.removeExtraSpace(txt_middlename_info.getText().toUpperCase());
            }
            if (MonoString.removeExtraSpace(txt_rank.getText().toUpperCase()) != null) {
                rank = MonoString.removeExtraSpace(txt_rank.getText().toUpperCase());
            }
            if (MonoString.removeExtraSpace(txt_designation.getText().toUpperCase()) != null) {
                designation = MonoString.removeExtraSpace(txt_designation.getText().toUpperCase());
            }
            faculty.setBulsu_id(bulsu_id);
            faculty.setLast_name(lastname);
            faculty.setFirst_name(firstname);
            faculty.setMiddle_name(middlename);
            faculty.setRank(rank);
            faculty.setDesignation(designation);
            faculty.setGender(gender);
            if (Database.connect().faculty().update(faculty)) {
                Notifications.create().darkStyle()
                        .title("Updated Successfully")
                        .text("Faculty: " + faculty.getBulsu_id().toUpperCase())
                        .showInformation();
            }
            lbl_bulsu_id.setText(faculty.getBulsu_id());
            lbl_full_name.setText(WordUtils.capitalizeFully(faculty.getFirst_name() + " " + faculty.getLast_name()));
            this.setValues(txt_id, txt_lastname_info, txt_firstname_info, txt_middlename_info, rbtn_male, rbtn_female, txt_rank, txt_designation, faculty);

        });

        this.addClickEvent(btn_back, () -> {
            this.setBasicInfo(lbl_id, lbl_name, lbl_gender, lbl_rank, lbl_designation, lbl_initial_letter, bg, faculty);
            anchor_basic.setVisible(false);
        });

        /**
         * WINDOW 3 - UPDATE ACCOUNT
         */
        TextField txt_username = searchAccessibilityText(infoRow, "txt_username");
        ComboBox cmb_access_level = searchAccessibilityText(infoRow, "cmb_access_level");
        PasswordField txt_old_pass = searchAccessibilityText(infoRow, "txt_old_pass");
        PasswordField txt_new_pass = searchAccessibilityText(infoRow, "txt_new_pass");
        PasswordField txt_confirm_new = searchAccessibilityText(infoRow, "txt_confirm_new");
        Label lbl_old_q = searchAccessibilityText(infoRow, "cmb_old_q");
        PasswordField txt_old_ans = searchAccessibilityText(infoRow, "txt_old_ans");
        ComboBox cmb_new_q = searchAccessibilityText(infoRow, "cmb_new_q");
        PasswordField txt_new_ans = searchAccessibilityText(infoRow, "txt_new_ans");
        PasswordField txt_update_pass = searchAccessibilityText(infoRow, "txt_update_pass");
        JFXButton btn_submit = searchAccessibilityText(infoRow, "btn_submit");
        PasswordField txt_confirm_ans = searchAccessibilityText(infoRow, "txt_confirm_ans");
        this.setComboBoxQuestions(cmb_new_q);
        this.setAccessLevels(cmb_access_level);

        JFXButton btn_account = searchAccessibilityText(infoRow, "btn_account");
        JFXButton btn_back2 = searchAccessibilityText(infoRow, "btn_back2");
        this.addClickEvent(btn_account, () -> {
            if (afMap == null) {
                Notifications.create().darkStyle()
                        .title("No Account Found")
                        .text("You can create one by only\n"
                                + " following the registration process.")
                        .showInformation();
                return;
            }
            anchor_account.setVisible(true);
            txt_username.setText(afMap.getUsername().toUpperCase());
            cmb_access_level.getSelectionModel().select(afMap.getAccess_level());
            String ques = afMap.getRecovery_question();
            if (ques != null) {
                lbl_old_q.setText(ques);
            }

        });
        this.addClickEvent(btn_back2, () -> {
            anchor_account.setVisible(false);
        });

        if (afMap != null) {
            this.addClickEvent(btn_submit, () -> {
                String username = MonoString.removeSpaces(txt_username.getText().toUpperCase()), entered_new_pass = txt_new_pass.getText(), new_pass = null;
                boolean okToSave = false;
                if (!username.isEmpty()) {
                    okToSave = checkExist(username, afMap);
                    if (!okToSave) {
                        return;
                    }
                } else {
                    System.out.println("NO USERNAME");
                    return;
                }

                if (!entered_new_pass.isEmpty()) {
                    okToSave = validateNewPass(afMap, txt_confirm_new, entered_new_pass, txt_old_pass);
                    if (okToSave) {
                        new_pass = Mono.security().hashFactory().hash_sha512(entered_new_pass);
                    } else {
                        return;
                    }
                }

                String entered_old_ans = null, ques = null, ans = null, entered_new_ans = MonoString.removeExtraSpace(txt_new_ans.getText().toUpperCase());
                if (!entered_new_ans.isEmpty()) {
                    entered_old_ans = Mono.security().hashFactory().hash_sha512(entered_old_ans);
                    okToSave = validateNewRecovery(afMap, entered_old_ans, txt_new_ans, txt_confirm_ans);
                    if (okToSave) {
                        ques = cmb_new_q.getSelectionModel().getSelectedItem().toString().toUpperCase();
                        ans = Mono.security().hashFactory().hash_sha512(MonoString.removeExtraSpace(txt_new_ans.getText()));
                    } else {
                        return;
                    }
                }

                okToSave = confirmCurrentPassword(txt_update_pass, afMap);
                if (!okToSave) {
                    Notifications.create().darkStyle()
                            .title("Updated Failed: Wrong Password")
                            .text("Please provide your password to save changes.")
                            .showError();
                    return;
                }

                if (okToSave) {
                    if (!username.isEmpty()) {
                        afMap.setUsername(username);
                    }
                    afMap.setAccess_level(MonoString.removeExtraSpace(cmb_access_level.getSelectionModel().getSelectedItem().toString().toUpperCase()));
                    if (new_pass != null) {
                        afMap.setPassword(new_pass);
                    }
                    if (ques != null) {
                        afMap.setRecovery_answer(ans);
                        afMap.setRecovery_question(ques);
                        lbl_old_q.setText(ques);
                        cmb_new_q.getSelectionModel().selectFirst();
                        txt_confirm_ans.setText("");
                        txt_new_ans.setText("");
                        txt_old_ans.setText("");
                    }
                }

                if (Database.connect().account_faculty().update(afMap)) {
                    Notifications.create().darkStyle()
                            .title("Updated Successfully")
                            .text("Account: " + afMap.getUsername().toUpperCase())
                            .showInformation();
                    lbl_username.setText(WordUtils.capitalizeFully(afMap.getUsername()));
                    lbl_access_level.setText(WordUtils.capitalizeFully(afMap.getAccess_level()));
                    txt_update_pass.setText("");
                    txt_username.setText(afMap.getUsername());
                } else {
                    Notifications.create().darkStyle()
                            .title("Updated Failed")
                            .text("Something went wrong.")
                            .showError();
                }
            });
        }

        SimpleTableCell cellParent = new SimpleTableCell();
        cellParent.setResizePriority(Priority.ALWAYS);
        cellParent.setContent(infoRow);

        row.addCell(cellParent);
        tableInfo.addRow(row);

        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(tableInfo);
        simpleTableView.setFixedWidth(true);
        simpleTableView.setParentOnScene(vbox_info);
    }

    private void addNewFaculty() {
        String gender = "";
        if (rbtn_male_new.isSelected()) {
            gender = "MALE";
        } else if (rbtn_female_new.isSelected()) {
            gender = "FEMALE";
        }

        String bulsu_id = MonoString.removeExtraSpace(txt_bulsu_id.getText().toUpperCase()), lastname = MonoString.removeExtraSpace(txt_lastname.getText().toUpperCase()), firstname = MonoString.removeExtraSpace(txt_firstname.getText().toUpperCase()), middlename = MonoString.removeExtraSpace(txt_middlename.getText().toUpperCase()), rank = MonoString.removeExtraSpace(txt_rank_new.getText().toUpperCase()), designation = MonoString.removeExtraSpace(txt_designation_new.getText().toUpperCase()), dept = MonoString.removeExtraSpace(txt_dept.getText().toUpperCase());

        if (bulsu_id.isEmpty() || lastname.isEmpty() || firstname.isEmpty()) {
            showWarning("Incomplete Data", "Please fill the required fields*.");
            return;
        }
        if (middlename == null) {
            middlename = "";
        }
        if (rank == null) {
            rank = "";
        }
        if (designation == null) {
            designation = "";
        }
        if (dept == null) {
            dept = "";
        }

        ArrayList<FacultyMapping> exist = Mono.orm().newSearch(Database.connect().faculty())
                .eq(DB.faculty().bulsu_id, bulsu_id)
                .execute().all();
        if (exist != null) {
            showWarning("BulSU ID Exist", "A faculty is already using\n"
                    + "this BulSU ID.");
            return;
        }
        FacultyMapping new_faculty = new FacultyMapping();
        new_faculty.setBulsu_id(bulsu_id);
        new_faculty.setLast_name(lastname);
        new_faculty.setFirst_name(firstname);
        new_faculty.setMiddle_name(middlename);
        new_faculty.setRank(rank);
        new_faculty.setDesignation(designation);
        new_faculty.setGender(gender);
        new_faculty.setDepartment(dept);
        int id = Database.connect().faculty().insert(new_faculty);
        if (id != -1) {
            Notifications.create().darkStyle()
                    .title("Added Successfully")
                    .text("Faculty: " + new_faculty.getBulsu_id().toUpperCase())
                    .showInformation();
            new_faculty = Mono.orm().newSearch(Database.connect().faculty())
                    .eq(DB.faculty().bulsu_id, bulsu_id)
                    .active().first();
            FacultyInformation fInfo = new FacultyInformation(new_faculty);
            activeFaculty.add(fInfo);
            createRow(fInfo, tableFaculty);
        }
    }

    private void showWarning(String title, String text) {
        Notifications.create().darkStyle()
                .title(title)
                .text(text)
                .showWarning();
    }

    private boolean confirmCurrentPassword(PasswordField txt_update_pass, AccountFacultyMapping afMap) {
        String entered_pass = Mono.security().hashFactory().hash_sha512(txt_update_pass.getText());
        return (afMap.getPassword() == null ? entered_pass == null : afMap.getPassword().equals(entered_pass));
    }

    private boolean validateNewRecovery(AccountFacultyMapping afMap, String entered_old_ans, TextField txt_new_ans, TextField txt_confirm_ans) {
        boolean okToSave = false;
        String old_ans = afMap.getRecovery_answer();

        if (old_ans == null || old_ans.equals(entered_old_ans)) {
            String new_ans = MonoString.removeExtraSpace(txt_new_ans.getText().toUpperCase());
            String confirm_ans = MonoString.removeExtraSpace(txt_confirm_ans.getText().toUpperCase());
            if (new_ans == null ? confirm_ans == null : new_ans.equals(confirm_ans)) {
                okToSave = true;
            } else {
                showWarning("Change Recovery Information:", "New answer not matched to its confirmation.");
                System.out.println("NEW ANS NOT MATCHED");
            }
        } else {
            showWarning("Change Recovery Information:", "Please provide the old answer correctly.");
            System.out.println("OLD ANS NOT MATCHED");
        }
        return okToSave;
    }

    private boolean validateNewPass(AccountFacultyMapping afMap, TextField txt_confirm_new, String entered_new_pass, TextField txt_old_pass) {
        boolean okToSave = false;
        String old_pass = afMap.getPassword();
        String entered_old__pass = Mono.security().hashFactory().hash_sha512(txt_old_pass.getText());
        if (entered_old__pass == null ? old_pass == null : entered_old__pass.equals(old_pass)) {
            if (entered_new_pass.length() < 6) {
                showWarning("Change Password: Invalid Length", "Valid password length is at least six (6)\n"
                        + "characters.");
                System.out.println("LENGTH MUST BE AT LEAST 6");
            } else if (txt_confirm_new.getText().equals(entered_new_pass)) {
                okToSave = true;
            } else {
                showWarning("Change Password: New Password Not Matched", "New password not matched to its confirmation.");
                System.out.println("NEW PASS NOT MATCHED");
            }
        } else {
            showWarning("Change Password: Old Password Not Matched", "Please provide your correct password to change it.");
            System.out.println("OLD PASS NOT MATCHED");
        }
        return okToSave;
    }

    private boolean checkExist(String username, AccountFacultyMapping afMap) {
        boolean okToSave = false;
        ArrayList<AccountFacultyMapping> exist = Mono.orm().newSearch(Database.connect().account_faculty())
                .eq(DB.account_faculty().username, username)
                .execute().all();
        if (exist != null) {
            boolean reallyExist = false;
            for (AccountFacultyMapping af : exist) {
                if (!Objects.equals(af.getId(), afMap.getId())) {
                    System.out.println("USERNAME EXIST");
                    showWarning("Username Exist", "Please provide a unique username or\n"
                            + " just use your old one.");
                    reallyExist = true;
                    break;
                }
            }
            okToSave = !reallyExist;
        } else {
            okToSave = true;
        }
        return okToSave;
    }

    private void setAccessLevels(ComboBox cmb_access_level) {
        cmb_access_level.getItems().add("NONE");
        cmb_access_level.getItems().add("EVALUATOR");
        cmb_access_level.getItems().add("ADMINISTRATOR");
    }

    private void setComboBoxQuestions(ComboBox cmb_questions) {
        ArrayList<String> questions = new ArrayList<>();
        questions.add("What was your favorite place to visit as a child?");
        questions.add("Who is your favorite actor, musician, or artist?");
        questions.add("What is the name of your favorite pet?");
        questions.add("In what city were you born?");
        questions.add("Which is your favorite web browser?");
        questions.add("What is the name of your first school?");
        questions.add("What is your favorite movie?");
        questions.add("What is your mother's maiden name?");
        questions.add("What is your favorite color?");
        questions.add("What is your father's middle name?");
        cmb_questions.getItems().addAll(questions);
        cmb_questions.getSelectionModel().selectFirst();
    }

    private void setBasicInfo(Label lbl_id,
            Label lbl_name,
            Label lbl_gender,
            Label lbl_rank,
            Label lbl_designation,
            Label lbl_initial_letter,
            VBox bg,
            FacultyMapping faculty) {
        lbl_id.setText(faculty.getBulsu_id().toUpperCase());
        lbl_name.setText(WordUtils.capitalizeFully(faculty.getFirst_name() + " " + faculty.getLast_name()));
        lbl_gender.setText(WordUtils.capitalizeFully(faculty.getGender()));
        lbl_rank.setText(faculty.getRank().toUpperCase());
        lbl_designation.setText(WordUtils.capitalizeFully(faculty.getDesignation()));
        lbl_initial_letter.setText(faculty.getFirst_name().toUpperCase().charAt(0) + "");

        if (faculty.getGender().equalsIgnoreCase("FEMALE")) {
            bg.setStyle(bg.getStyle() + " -fx-background-color: #D64651;");
        }

    }

    private void setValues(TextField txt_id,
            TextField txt_lastname,
            TextField txt_firstname,
            TextField txt_middlename,
            RadioButton rbtn_male,
            RadioButton rbtn_female,
            TextField txt_rank,
            TextField txt_designation,
            FacultyMapping faculty) {
        txt_id.setText(faculty.getBulsu_id());
        txt_lastname.setText(faculty.getLast_name());
        txt_firstname.setText(faculty.getFirst_name());
        txt_middlename.setText(faculty.getMiddle_name());
        if (faculty.getGender().equalsIgnoreCase("MALE")) {
            rbtn_male.setSelected(true);
        } else if (faculty.getGender().equalsIgnoreCase("FEMALE")) {
            rbtn_female.setSelected(true);
        }
        txt_rank.setText(faculty.getRank());
        txt_designation.setText(faculty.getDesignation());
    }
}

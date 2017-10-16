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
package org.cict.accountmanager.faculty;

import app.lazy.models.AcademicProgramMapping;
import app.lazy.models.AccountFacultyMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.FacultyMapping;
import artifacts.MonoString;
import com.izum.fx.textinputfilters.StringFilter;
import com.izum.fx.textinputfilters.TextInputFilters;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.LayoutDataFX;
import com.jhmvin.fx.display.SceneFX;
import com.jhmvin.transitions.Animate;
import java.util.ArrayList;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.text.WordUtils;
import org.controlsfx.control.Notifications;
import org.hibernate.criterion.Order;
import update3.org.cict.SectionConstants;

/**
 * Faculty Information Controller Class.
 * <fxml>faculty-info.fxml</fxml>
 *
 * @author Joemar
 */
public class FacultyInfoController extends SceneFX implements ControllerFX {

    @FXML
    private VBox application_root;

    @FXML
    private JFXButton btn_back;

    @FXML
    private Label lbl_name;

    @FXML
    private Label lbl_name1;
    
    @FXML
    private Label lbl_name11;
    
    @FXML
    private Label lbl_bulsu_id;

    @FXML
    private Label lbl_gender;

    @FXML
    private Label lbl_rank;

    @FXML
    private Label lbl_designation;

    @FXML
    private Label lbl_department;

    @FXML
    private Label lbl_username;

    @FXML
    private Label lbl_access;

    @FXML
    private Label lbl_status;

    @FXML
    private JFXButton btn_nav;

    @FXML
    private TextField txt_id;

    @FXML
    private TextField txt_lastname;

    @FXML
    private TextField txt_fname;

    @FXML
    private TextField txtmname;

    @FXML
    private RadioButton rbtn_male;

    @FXML
    private RadioButton rbtn_female;

    @FXML
    private ComboBox cmb_rank;

    @FXML
    private ComboBox cmb_des;

    @FXML
    private ComboBox cmb_dept;

    @FXML
    private JFXButton btn_save;

    @FXML
    private VBox vbox_disabled;

    @FXML
    private JFXButton btn_enable;

    @FXML
    private JFXButton btn_reset_enable;

    @FXML
    private VBox vbox_faculty_tip;

    public FacultyInfoController(FacultyInformation currentFacultyInfo) {
        this.currentFacultyInfo = currentFacultyInfo;
    }

    private FacultyInformation currentFacultyInfo;
    private LayoutDataFX homeFx;

    public void setHomeFx(LayoutDataFX homeFx) {
        this.homeFx = homeFx;
    }

    @Override
    public void onInitialization() {
        bindScene(application_root);
        setComboBox();
        // disabled box
        this.vbox_faculty_tip.setVisible(true); // display tip
        this.vbox_disabled.setVisible(false); // hid disable box

        setData();
        addTextFieldFilters();
    }
    
    private void setComboBox() {
        cmb_rank.getItems().clear();
        cmb_rank.getItems().add("Part Time");
        cmb_rank.getItems().add("Associate Professor I");
        cmb_rank.getItems().add("Associate Professor II");
        cmb_rank.getItems().add("Associate Professor III");
        cmb_rank.getItems().add("Associate Professor III");
        cmb_rank.getItems().add("Professor I");
        cmb_rank.getItems().add("Professor II");
        cmb_rank.getItems().add("Professor III");
        cmb_rank.getSelectionModel().selectFirst();
        
        cmb_des.getItems().clear();
        cmb_des.getItems().add("Instructor");
        cmb_des.getItems().add("Faculty");
        cmb_des.getItems().add("System Administrator");
        cmb_des.getItems().add("Local Registrar");
        cmb_des.getItems().add("Department Head");
        cmb_des.getItems().add("Associate Dean");
        cmb_des.getItems().add("Dean");
        cmb_des.getSelectionModel().selectFirst();
        
        cmb_dept.getItems().clear();
        ArrayList<AcademicProgramMapping> acads = Mono.orm().newSearch(Database.connect().academic_program())
//                .eq(DB.academic_program().implemented, 1)
                .active(Order.asc(DB.academic_program().code))
                .all();
        for(AcademicProgramMapping acad: acads) {
            cmb_dept.getItems().add(acad.getCode());
        }
        cmb_dept.getSelectionModel().selectFirst();
    }

    /**
     * Add Text Filters for input.
     */
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
        textId.clone().setTextSource(txt_id).applyFilter();

//        StringFilter text50Max = TextInputFilters.string()
//                .setFilterMode(StringFilter.LETTER_DIGIT_SPACE)
//                .setMaxCharacters(50)
//                .setNoLeadingTrailingSpaces(false)
//                .setFilterManager(filterManager -> {
//                    if (!filterManager.isValid()) {
//                        Mono.fx().alert().createWarning().setHeader("Warning")
//                                .setMessage(filterManager.getMessage())
//                                .show();
//                    }
//                });
//        text50Max.clone().setTextSource(cmb_dept).applyFilter();
//        text50Max.clone().setTextSource(txt_des).applyFilter();
//        text50Max.clone().setTextSource(cmb_rank).applyFilter();

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
        textName.clone().setTextSource(txt_fname).applyFilter();
        textName.clone().setTextSource(txtmname).applyFilter();
    }

    @Override
    public void onEventHandling() {

        this.addClickEvent(btn_back, () -> {

            Animate.fade(this.application_root, SectionConstants.FADE_SPEED, () -> {
                super.replaceRoot(homeFx.getApplicationRoot());
            }, homeFx.getApplicationRoot());

            homeFx.getController().onInitialization();
        });

        this.addClickEvent(btn_save, () -> {
            onSave();
        });

        this.addClickEvent(btn_nav, () -> {
            String nav = btn_nav.getText();
            if (nav.equalsIgnoreCase("activate account")) {
                setAcc(true);
            } else {
                setAcc(false);
            }
        });

        this.addClickEvent(btn_reset_enable, () -> {
            this.resetAndEnable();
        });

        this.addClickEvent(btn_enable, () -> {
            this.reenableOnly();
        });
    }

    /**
     * Re-enables a disabled account.
     */
    private void reenableOnly() {
        int res = Mono.fx().alert().createConfirmation()
                .setHeader("Enable Account")
                .setMessage("This will only enable the disabled account."
                        + " Continue?")
                .confirmYesNo();
        if (res == -1) {
            return;
        }

        afMap.setDisabled_since(null);
        afMap.setBlocked_count(0);

        if (Database.connect().account_faculty().update(afMap)) {
            Notifications.create().darkStyle()
                    .title("Enabled Successfully")
                    .text("Faculty: " + faculty.getBulsu_id().toUpperCase())
                    .showInformation();
            this.vbox_disabled.setVisible(false);
            this.vbox_faculty_tip.setVisible(true); //show tip
        }
    }

    /**
     * Resets and re-enable an account.
     */
    private void resetAndEnable() {
        int res = Mono.fx().alert().createConfirmation()
                .setHeader("Reset And Enable Account")
                .setMessage("This will result into an update to the user's password into default."
                        + " Do you really want to reset this account?")
                .confirmYesNo();
        if (res == -1) {
            return;
        }

        String defaultPass = "123456";
        String hash = Mono.security().hashFactory().hash_sha512(defaultPass);
        afMap.setPassword(hash);
        afMap.setBlocked_count(0);
        afMap.setDisabled_since(null);

        if (Database.connect().account_faculty().update(afMap)) {
            Notifications.create().darkStyle()
                    .title("Reset And Enabled Successfully")
                    .text("Faculty: " + faculty.getBulsu_id().toUpperCase())
                    .showInformation();
            this.vbox_disabled.setVisible(false);
            this.vbox_faculty_tip.setVisible(true); //show tip
        }
    }

    private void setAcc(boolean active) {
        String title = "", btn = "", status = "";
        if (active) {
            afMap.setActive(1);
            title = "Activated Successfully";
            btn = "Deactivate Account";
            status = "Active";
        } else {
            afMap.setActive(0);
            title = "Deactivated Successfully";
            btn = "Activate Account";
            status = "Inactive";
        }
        if (Database.connect().account_faculty().update(afMap)) {
            Notifications.create().darkStyle()
                    .title(title)
                    .text("Faculty: " + faculty.getBulsu_id().toUpperCase())
                    .showInformation();
            btn_nav.setText(btn);
            lbl_status.setText(status);
        }
    }

    /**
     * Save new Values.
     */
    private void onSave() {
        String gender = "";
        if (rbtn_male.isSelected()) {
            gender = "MALE";
        } else if (rbtn_female.isSelected()) {
            gender = "FEMALE";
        }
        String bulsu_id = "", lastname = "", firstname = "", middlename = "", rank = "", designation = "", dept = "";

        try {
            bulsu_id = MonoString.removeExtraSpace(txt_id.getText().toUpperCase());
            lastname = MonoString.removeExtraSpace(txt_lastname.getText().toUpperCase());
            firstname = MonoString.removeExtraSpace(txt_fname.getText().toUpperCase());
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

        if (MonoString.removeExtraSpace(txtmname.getText().toUpperCase()) != null) {
            middlename = MonoString.removeExtraSpace(txtmname.getText().toUpperCase());
        }
        if (MonoString.removeExtraSpace(cmb_rank.getSelectionModel().getSelectedItem().toString().toUpperCase()) != null) {
            rank = MonoString.removeExtraSpace(cmb_rank.getSelectionModel().getSelectedItem().toString().toUpperCase());
        }
        if (MonoString.removeExtraSpace(cmb_des.getSelectionModel().getSelectedItem().toString().toUpperCase()) != null) {
            designation = MonoString.removeExtraSpace(cmb_des.getSelectionModel().getSelectedItem().toString().toUpperCase());
        }
        if (MonoString.removeExtraSpace(cmb_dept.getSelectionModel().getSelectedItem().toString().toUpperCase()) != null) {
            dept = MonoString.removeExtraSpace(cmb_dept.getSelectionModel().getSelectedItem().toString().toUpperCase());
        }
        faculty.setBulsu_id(bulsu_id);
        faculty.setLast_name(lastname);
        faculty.setFirst_name(firstname);
        faculty.setMiddle_name(middlename);
        faculty.setRank(rank);
        faculty.setDesignation(designation);
        faculty.setGender(gender);
        faculty.setDepartment(dept);
        if (Database.connect().faculty().update(faculty)) {
            Notifications.create().darkStyle()
                    .title("Updated Successfully")
                    .text("Faculty: " + faculty.getBulsu_id().toUpperCase())
                    .showInformation();
        }
        // update values after update
        lbl_bulsu_id.setText(faculty.getBulsu_id());
        this.setBasicInfo();
        this.setValues();
    }

    private void showWarning(String title, String text) {
        Notifications.create().darkStyle()
                .title(title)
                .text(text)
                .showWarning();
    }

    private FacultyMapping faculty;
    private AccountFacultyMapping afMap;

    /**
     * Set Values in labels.
     */
    private void setData() {
        // group toggle
        ToggleGroup group2 = new ToggleGroup();
        rbtn_male.setToggleGroup(group2);
        rbtn_female.setToggleGroup(group2);

        // get values if selected faculty
        faculty = currentFacultyInfo.getFacultyMapping();
        // account mapping
        afMap = currentFacultyInfo.getAccountFacultyMapping();

        this.setBasicInfo(); // set values in the label for basic  info
        this.setValues(); // set values in the editting fields
        // if no account
        if (afMap == null) {
            System.out.println("NO ACCOUNT");
            return;
        }
        // set username
        lbl_username.setText(afMap.getUsername().toUpperCase());
        // set account access level
        String access = afMap.getAccess_level().replace("_", " ");
        lbl_access.setText(access);

        // set status and button text
        lbl_status.setText(afMap.getActive().equals(0) ? "Inactive" : "Active");
        btn_nav.setText(afMap.getActive().equals(0) ? "Activate Account" : "Deactivate Account");

        // check if the account is blocked
        try {
            if (afMap.getBlocked_count() >= 3 && afMap.getDisabled_since() != null) {
                vbox_disabled.setVisible(true);
                this.vbox_faculty_tip.setVisible(false); // hide tip
            }
        } catch (Exception e) {
        }
    }

    /**
     * Load Values on Label.
     */
    private void setBasicInfo() {
        lbl_bulsu_id.setText(faculty.getBulsu_id().toUpperCase());
        lbl_name.setText(WordUtils.capitalizeFully(faculty.getFirst_name()));
        lbl_name1.setText(faculty.getMiddle_name()==null || faculty.getMiddle_name().isEmpty() ? "---" : WordUtils.capitalizeFully(faculty.getMiddle_name()));
        lbl_name11.setText(faculty.getLast_name()==null ? "---" : WordUtils.capitalizeFully(faculty.getLast_name()));
        lbl_gender.setText(faculty.getGender()==null || faculty.getGender().isEmpty()  ? "NOT SPECIFIED" : WordUtils.capitalizeFully(faculty.getGender()));
        lbl_rank.setText(faculty.getRank()==null || faculty.getRank().isEmpty()  ? "NOT SPECIFIED" : faculty.getRank());
        lbl_department.setText(faculty.getDepartment()==null || faculty.getDepartment().isEmpty()  ? "NONE" : (faculty.getDepartment()));
        lbl_designation.setText(faculty.getDesignation()==null || faculty.getDesignation().isEmpty()  ? "NONE" : WordUtils.capitalizeFully(faculty.getDesignation()));
    }

    /**
     * Set Current Values on text fields.
     */
    private void setValues() {
        txt_id.setText(faculty.getBulsu_id());
        txt_lastname.setText(faculty.getLast_name());
        txt_fname.setText(faculty.getFirst_name());
        txtmname.setText(faculty.getMiddle_name() == null ? "" : faculty.getMiddle_name());

        if (faculty.getGender().equalsIgnoreCase("MALE")) {
            rbtn_male.setSelected(true);
        } else if (faculty.getGender().equalsIgnoreCase("FEMALE")) {
            rbtn_female.setSelected(true);
        } else {
            // if none was set in the database
            rbtn_male.setSelected(true);
        }
        if(faculty.getRank() == null)
            cmb_rank.getSelectionModel().selectFirst();
        else
            cmb_rank.getSelectionModel().select(faculty.getRank());
        
        if(faculty.getDesignation()== null)
            cmb_des.getSelectionModel().selectFirst();
        else
            cmb_des.getSelectionModel().select(faculty.getDesignation());
        
        if(faculty.getDepartment()== null)
            cmb_dept.getSelectionModel().selectFirst();
        else
            cmb_dept.getSelectionModel().select(faculty.getDepartment());
    }

}

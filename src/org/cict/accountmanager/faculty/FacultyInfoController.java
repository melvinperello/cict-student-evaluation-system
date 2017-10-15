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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.text.WordUtils;
import org.controlsfx.control.Notifications;
import update3.org.cict.SectionConstants;

/**
 *
 * @author Joemar
 */
public class FacultyInfoController extends SceneFX implements ControllerFX {

    @FXML
    private AnchorPane application_root;

    @FXML
    private Label lbl_name;

    @FXML
    private Label lbl_bulsu_id;

    @FXML
    private Label lbl_gender;

    @FXML
    private Label lbl_rank;

    @FXML
    private JFXButton btn_basic;

    @FXML
    private Label lbl_username;

    @FXML
    private Label lbl_access;

    @FXML
    private Label lbl_status;

    @FXML
    private JFXButton btn_nav;
    
    @FXML
    private JFXButton btn_reset_enable;
    
    @FXML
    private JFXButton btn_enable;

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
    private TextField txt_rank;

    @FXML
    private TextField txt_des;

    @FXML
    private TextField txt_dept;
            
    @FXML
    private JFXButton btn_save;

    @FXML
    private JFXButton btn_back;
    
    @FXML
    private AnchorPane anchor_basic;
            
    @FXML
    private AnchorPane anchor_disabled;

    private FacultyInformation currentFacultyInfo;
    public FacultyInfoController(FacultyInformation currentFacultyInfo) {
        this.currentFacultyInfo = currentFacultyInfo;
    }
    
    private LayoutDataFX homeFx;
    public void setHomeFx(LayoutDataFX homeFx) {
        this.homeFx = homeFx;
    }
    
    @Override
    public void onInitialization() {
        bindScene(application_root);
        setData();
        addTextFieldFilters();
    }
     
    private void addTextFieldFilters() {
        StringFilter textId = TextInputFilters.string()
//                .setFilterMode(StringFilter.LETTER_DIGIT)
                .setMaxCharacters(50)
                .setNoLeadingTrailingSpaces(true)
                .setFilterManager(filterManager->{
                    if(!filterManager.isValid()) {
                        Mono.fx().alert().createWarning().setHeader("Warning")
                                .setMessage(filterManager.getMessage())
                                .show();
                    }
                });
        textId.clone().setTextSource(txt_id).applyFilter();
        
        StringFilter text50Max = TextInputFilters.string()
                .setFilterMode(StringFilter.LETTER_DIGIT_SPACE)
                .setMaxCharacters(50)
                .setNoLeadingTrailingSpaces(false)
                .setFilterManager(filterManager->{
                    if(!filterManager.isValid()) {
                        Mono.fx().alert().createWarning().setHeader("Warning")
                                .setMessage(filterManager.getMessage())
                                .show();
                    }
                });
        text50Max.clone().setTextSource(txt_dept).applyFilter();
        text50Max.clone().setTextSource(txt_des).applyFilter();
        text50Max.clone().setTextSource(txt_rank).applyFilter();
        
        StringFilter textName = TextInputFilters.string()
                .setFilterMode(StringFilter.LETTER_SPACE)
                .setMaxCharacters(100)
                .setNoLeadingTrailingSpaces(false)
                .setFilterManager(filterManager->{
                    if(!filterManager.isValid()) {
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
        
        this.addClickEvent(btn_back, ()->{
            homeFx.getController().onInitialization();
            Animate.fade(this.application_root, SectionConstants.FADE_SPEED, () -> {
                super.replaceRoot(homeFx.getApplicationRoot());
            }, homeFx.getApplicationRoot());
        });
        
        this.addClickEvent(btn_basic, () -> {
            anchor_basic.setVisible(true);
            btn_basic.setDisable(true);
        });

        this.addClickEvent(btn_save, () -> {
            onSave();
        });
        
        this.addClickEvent(btn_nav, () -> {
            String nav = btn_nav.getText();
            if(nav.equalsIgnoreCase("activate account")) {
                setAcc(true);
            } else {
                setAcc(false);
            }
        });
        
        this.addClickEvent(btn_reset_enable, () -> {
            int res = Mono.fx().alert().createConfirmation()
                    .setHeader("Reset And Enable Account")
                    .setMessage("This will result into an update to the user's password into default."
                            + " Do you really want to reset this account?")
                    .confirmYesNo();
            if(res == -1)
                return;
            
            String defaultPass = "123456";
            String hash = Mono.security().hashFactory().hash_sha512(defaultPass);
            afMap.setPassword(hash);
            afMap.setBlocked_count(0);
            afMap.setDisabled_since(null);
            
            if(Database.connect().account_faculty().update(afMap)) {
                Notifications.create().darkStyle()
                        .title("Reset And Enabled Successfully")
                        .text("Faculty: " + faculty.getBulsu_id().toUpperCase())
                        .showInformation();
                anchor_disabled.setVisible(false);
            }
        });
        
        this.addClickEvent(btn_enable, () -> {
            int res = Mono.fx().alert().createConfirmation()
                    .setHeader("Enable Account")
                    .setMessage("This will only enable the disabled account."
                            + " Continue?")
                    .confirmYesNo();
            if(res == -1)
                return;
            
            afMap.setDisabled_since(null);
            afMap.setBlocked_count(0);
            
            if(Database.connect().account_faculty().update(afMap)) {
                Notifications.create().darkStyle()
                        .title("Enabled Successfully")
                        .text("Faculty: " + faculty.getBulsu_id().toUpperCase())
                        .showInformation();
                anchor_disabled.setVisible(false);
            }
        });
    }
    
    private void setAcc(boolean active) {
        String title = "", btn = "", status = "";
        if(active) {
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
        if(Database.connect().account_faculty().update(afMap)) {
            Notifications.create().darkStyle()
                    .title(title)
                    .text("Faculty: " + faculty.getBulsu_id().toUpperCase())
                    .showInformation();
            btn_nav.setText(btn);
            lbl_status.setText(status);
        }
    }
    
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
            if (MonoString.removeExtraSpace(txt_rank.getText().toUpperCase()) != null) {
                rank = MonoString.removeExtraSpace(txt_rank.getText().toUpperCase());
            }
            if (MonoString.removeExtraSpace(txt_des.getText().toUpperCase()) != null) {
                designation = MonoString.removeExtraSpace(txt_des.getText().toUpperCase());
            }
            try {
                if (MonoString.removeExtraSpace(txt_dept.getText().toUpperCase()) != null) {
                    dept = MonoString.removeExtraSpace(txt_dept.getText().toUpperCase());
                }
            } catch (Exception e) {
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
            lbl_bulsu_id.setText(faculty.getBulsu_id());
            this.setValues();
            this.setBasicInfo();
    }

    private void showWarning(String title, String text) {
        Notifications.create().darkStyle()
                .title(title)
                .text(text)
                .showWarning();
    }
    
    private FacultyMapping faculty;
    private AccountFacultyMapping afMap;
    private void setData() {
        ToggleGroup group2 = new ToggleGroup();
        rbtn_male.setToggleGroup(group2);
        rbtn_female.setToggleGroup(group2);
        
        faculty = currentFacultyInfo.getFacultyMapping();
        afMap = currentFacultyInfo.getAccountFacultyMapping();
        this.setBasicInfo();
        if(afMap == null) {
            System.out.println("NO ACCOUNT");
            return;
        }
        lbl_username.setText(afMap.getUsername());
        String access = afMap.getAccess_level().replace("_", " ");
        lbl_access.setText(access);
        if(afMap.getActive() == 0)
            lbl_status.setText("Inactive");
        else
            lbl_status.setText("Active");
            
        if(afMap.getActive() == 0) {
            btn_nav.setText("Activate Account");
        } else {
            btn_nav.setText("Deactivate Account");
        }
        try {
            if(afMap.getDisabled_since() != null) {
                if(!Mono.orm().getServerTime().isPastServerTime(afMap.getDisabled_since()))
                    anchor_disabled.setVisible(true);
            }
        } catch (Exception e) {
        }
    }
    
    private void setBasicInfo() {
        lbl_bulsu_id.setText(faculty.getBulsu_id().toUpperCase());
        lbl_name.setText(WordUtils.capitalizeFully(faculty.getFirst_name() + " " + faculty.getLast_name()));
        lbl_gender.setText(WordUtils.capitalizeFully(faculty.getGender()));
        lbl_rank.setText(faculty.getRank().toUpperCase());
        setValues();
        anchor_basic.setVisible(false);
        btn_basic.setDisable(false);
    }

    private void setValues() {
        txt_id.setText(faculty.getBulsu_id());
        txt_lastname.setText(faculty.getLast_name());
        txt_fname.setText(faculty.getFirst_name());
        txtmname.setText(faculty.getMiddle_name());
        if (faculty.getGender().equalsIgnoreCase("MALE")) {
            rbtn_male.setSelected(true);
        } else if (faculty.getGender().equalsIgnoreCase("FEMALE")) {
            rbtn_female.setSelected(true);
        }
        txt_rank.setText(faculty.getRank());
        txt_des.setText(faculty.getDesignation());
        txt_dept.setText(faculty.getDepartment());
    }

}

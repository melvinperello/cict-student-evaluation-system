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
package update5.org.cict.student.controller;

import app.lazy.models.Database;
import app.lazy.models.StudentMapping;
import app.lazy.models.utils.DateString;
import app.lazy.models.utils.FacultyUtility;
import artifacts.MonoString;
import com.izum.fx.textinputfilters.StringFilter;
import com.izum.fx.textinputfilters.TextInputFilters;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.LayoutDataFX;
import com.jhmvin.fx.display.SceneFX;
import com.jhmvin.transitions.Animate;
import java.util.Objects;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.cict.evaluation.student.StudentValues;
import org.cict.evaluation.student.credit.CreditController;
import org.cict.reports.deficiency.PrintDeficiency;
import org.cict.reports.profile.student.PrintStudentProfile;
import org.controlsfx.control.Notifications;
import update3.org.collegechooser.ChooserHome;

/**
 *
 * @author Joemar
 */
public class InfoStudentController extends SceneFX implements ControllerFX {

    @FXML
    private VBox application_root;

    @FXML
    private VBox vbox_verify;

    @FXML
    private JFXButton btn_back;

    @FXML
    private Label lbl_lastname;

    @FXML
    private Label lbl_firstname;

    @FXML
    private Label lbl_middlename;

    @FXML
    private JFXButton btn_edit_grades;

    @FXML
    private JFXButton btn_verify;

    @FXML
    private JFXButton btn_change_college;

    @FXML
    private JFXButton btn_change_residency;

    @FXML
    private JFXButton btn_view_profile;

    @FXML
    private JFXButton btn_view_deficiency;

    @FXML
    private TextField txt_studnum;

    @FXML
    private TextField txt_lastname;

    @FXML
    private TextField txt_firstname;

    @FXML
    private TextField txt_middlename;

    @FXML
    private ComboBox cmb_yrlvl;

    @FXML
    private Label lbl_cictid;

    @FXML
    private ComboBox cmb_campus;

    @FXML
    private TextField txt_section;

    @FXML
    private TextField txt_group;

    @FXML
    private JFXButton btn_editsave;

    @FXML
    private JFXButton btn_shift_course;

    @FXML
    private RadioButton rbtn_male;

    @FXML
    private RadioButton rbtn_female;

    @FXML
    private Label lbl_verified_by;

    @FXML
    private Label lbl_verified_date;

    private StudentValues studentValues = new StudentValues();

    public InfoStudentController(StudentMapping currentStudent) {
        this.CURRENT_STUDENT = currentStudent;
    }

    private LayoutDataFX homeFX;

    public void setHomeFX(LayoutDataFX home) {
        this.homeFX = home;
    }

    @Override
    public void onInitialization() {
        super.bindScene(application_root);
        ToggleGroup group2 = new ToggleGroup();
        rbtn_male.setToggleGroup(group2);
        rbtn_female.setToggleGroup(group2);

        this.isVerified(false);
        this.setCmbYearLevel();
        this.setCmbCampus();
        this.setValues();
        addTextFieldFilters();
    }

    private void addTextFieldFilters() {
        StringFilter textFilter = TextInputFilters.string()
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
        textFilter.clone().setTextSource(txt_firstname).applyFilter();
        textFilter.clone().setTextSource(txt_lastname).applyFilter();
        textFilter.clone().setTextSource(txt_middlename).applyFilter();
        textFilter.clone().setFilterMode(StringFilter.LETTER_DIGIT).setTextSource(txt_section).applyFilter();

        textFilter.clone().setFilterMode(StringFilter.LETTER_DIGIT)
                .setMaxCharacters(50)
                .setNoLeadingTrailingSpaces(true)
                .setTextSource(txt_studnum).applyFilter();

        textFilter.clone().setFilterMode(StringFilter.DIGIT)
                .setMaxCharacters(11)
                .setNoLeadingTrailingSpaces(true)
                .setTextSource(txt_group).applyFilter();

    }

    @Override
    public void onEventHandling() {
        btn_editsave.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            onSave();
        });

        super.addClickEvent(btn_back, () -> {
            back();
        });

        Mono.fx().key(KeyCode.ENTER).release(application_root, () -> {
            this.onSave();
        });

        super.addClickEvent(btn_edit_grades, () -> {
            this.onShowCredit();
        });

        super.addClickEvent(btn_verify, () -> {
            this.onVerify();
        });

        super.addClickEvent(btn_view_deficiency, () -> {
            btn_view_deficiency.setDisable(true);
            this.printDeficiency();
        });

        super.addClickEvent(btn_view_profile, () -> {
            this.printProfile();
        });

        // Code to change student college
        super.addClickEvent(btn_change_college, () -> {
            String selectedCollege = ChooserHome.open();
            if (selectedCollege == null || selectedCollege.equalsIgnoreCase("cancel")) {
                // operation was cancelled
                return;
            }

            if (this.CURRENT_STUDENT.getCollege().equalsIgnoreCase(selectedCollege)) {
                Mono.fx().snackbar().showInfo(application_root, "No Changes Were Made.");
                return;
            }

            // save changes
            this.CURRENT_STUDENT.setCollege(selectedCollege);
            boolean updated = Database.connect().student().update(CURRENT_STUDENT);
            if (updated) {
                Mono.fx().snackbar().showSuccess(application_root, "Information was updated.");
            } else {
                Mono.fx().snackbar().showInfo(application_root, "Cannot Save Changes, Please Try Again.");
            }
        });

    }

    private void printProfile() {
        PrintStudentProfile profile = new PrintStudentProfile();
        profile.CICT_id = CURRENT_STUDENT.getCict_id();
        //----------------------------------------------------------------------
        // proper call back do not put disabling before this method call
        // always disable a button inside whenStarted to assure that the task had started.
        profile.whenStarted(() -> {
            // disable + wait cursor (Optional)
            btn_view_profile.setDisable(true);
            super.cursorWait();
        });
        // Cancel
        profile.whenCancelled(() -> {
            //------------------------------------------------------------------
            if (profile.hasNoProfile()) {
                Notifications.create()
                        .title("No Profile")
                        .text("The student has not yet submitted a profile")
                        .showInformation();
                return;
            }
            //------------------------------------------------------------------

            Notifications.create()
                    .title("Something went wrong.")
                    .text("Student not found.")
                    .showInformation();
        });
        // Failed
        profile.whenFailed(() -> {
            Notifications.create()
                    .title("Request Failed")
                    .text("Please try again later.")
                    .showInformation();
        });
        // If everything seems right
        // Success
        profile.whenSuccess(() -> {
            Notifications.create()
                    .title("Printing Student Profile")
                    .text("Please wait a moment.")
                    .showInformation();
        });
        // What Ever Happens
        profile.whenFinished(() -> {
            btn_view_profile.setDisable(false);
            super.cursorDefault();
        });
        profile.transact();
    }

    private void printDeficiency() {
        PrintDeficiency print = new PrintDeficiency();
        print.CICT_id = CURRENT_STUDENT.getCict_id();
        print.whenSuccess(() -> {
            btn_view_deficiency.setDisable(false);
            Notifications.create()
                    .title("Printing the Deficiency Report.")
                    .text("Please wait a moment.")
                    .showInformation();
        });
        print.whenCancelled(() -> {
            Notifications.create()
                    .title("Request Cancelled")
                    .text(print.getMessage()
                            + "\nSorry for the inconviniece.")
                    .showWarning();
        });
        print.whenFailed(() -> {
            Notifications.create()
                    .title("Request Failed")
                    .text("Something went wrong. Sorry for the inconviniece.")
                    .showInformation();
        });
        print.transact();
    }

    private void onVerify() {
        this.CURRENT_STUDENT.setVerified(1);
        this.CURRENT_STUDENT.setVerification_date(Mono.orm().getServerTime().getDateWithFormat());
        this.CURRENT_STUDENT.setVerfied_by(CollegeFaculty.instance().getFACULTY_ID());
        if (Database.connect().student().update(this.CURRENT_STUDENT)) {
            Notifications.create().title("Verified Successfully")
                    .text("Student Number: " + this.CURRENT_STUDENT.getId())
                    .showInformation();
            isVerified(true);
        }
    }

    private void onShowCredit() {
        String title = "Edit Grades";
        CreditController controller = new CreditController(this.CURRENT_STUDENT.getCict_id(), "CREDIT");
        Mono.fx().create()
                .setPackageName("org.cict.evaluation.student.credit")
                .setFxmlDocument("Credit")
                .makeFX()
                .setController(controller)
                .makeScene()
                .makeStageApplication()
                .stageResizeable(true)
                .stageMaximized(true)
                .stageShow();
    }

    private void isVerified(Boolean answer) {
        vbox_verify.setVisible(!answer);
        txt_firstname.setDisable(!answer);
        txt_group.setDisable(!answer);
        txt_lastname.setDisable(!answer);
        txt_middlename.setDisable(!answer);
        txt_section.setDisable(!answer);
        txt_studnum.setDisable(!answer);

        cmb_campus.setDisable(!answer);
        rbtn_female.setDisable(!answer);
        rbtn_male.setDisable(!answer);
        cmb_yrlvl.setDisable(!answer);

        btn_change_college.setDisable(!answer);
        btn_change_residency.setDisable(!answer);
        btn_edit_grades.setDisable(!answer);
        btn_editsave.setDisable(!answer);
        btn_shift_course.setDisable(!answer);
        btn_view_deficiency.setDisable(!answer);
        btn_view_profile.setDisable(!answer);

        if (answer) {
            lbl_verified_by.setText(FacultyUtility.getFacultyName(FacultyUtility.getFaculty(this.CURRENT_STUDENT.getVerfied_by()), FacultyUtility.NameFormat.SURNAME_FIRST));
            lbl_verified_date.setText(DateString.formatDate(this.CURRENT_STUDENT.getVerification_date(), DateString.TIME_FORMAT_2));
            btn_verify.setDisable(true);
        }
    }

    private void setValues() {
        try {
            lbl_firstname.setText(CURRENT_STUDENT.getFirst_name());
            lbl_lastname.setText(CURRENT_STUDENT.getLast_name());
            lbl_middlename.setText(CURRENT_STUDENT.getMiddle_name() == null ? "" : CURRENT_STUDENT.getMiddle_name());

            lbl_cictid.setText(CURRENT_STUDENT.getCict_id().toString());
            txt_firstname.setText(CURRENT_STUDENT.getFirst_name());
            txt_group.setText(CURRENT_STUDENT.get_group() == null ? "" : CURRENT_STUDENT.get_group().toString());
            txt_lastname.setText(CURRENT_STUDENT.getLast_name());
            txt_middlename.setText(CURRENT_STUDENT.getMiddle_name() == null ? "" : CURRENT_STUDENT.getMiddle_name());
            txt_section.setText(CURRENT_STUDENT.getSection() == null ? "" : CURRENT_STUDENT.getSection());
            txt_studnum.setText(CURRENT_STUDENT.getId());
            setComboBoxValue(CURRENT_STUDENT.getCampus() == null ? "" : CURRENT_STUDENT.getCampus(), cmb_campus);
            this.cmb_yrlvl.getSelectionModel().select(CURRENT_STUDENT.getYear_level() == null || CURRENT_STUDENT.getYear_level() == 1 ? 0 : CURRENT_STUDENT.getYear_level() - 1);

            Boolean verified = (this.CURRENT_STUDENT.getVerified() == null ? 0 : this.CURRENT_STUDENT.getVerified()) == 1;
            this.isVerified(verified);
            if (verified) {
                lbl_verified_by.setText(FacultyUtility.getFacultyName(FacultyUtility.getFaculty(this.CURRENT_STUDENT.getVerfied_by()), FacultyUtility.NameFormat.SURNAME_FIRST));
                lbl_verified_date.setText(DateString.formatDate(this.CURRENT_STUDENT.getVerification_date(), DateString.TIME_FORMAT_2));
            }

            if ((CURRENT_STUDENT.getGender() == null ? "" : CURRENT_STUDENT.getGender()).equalsIgnoreCase("MALE")) {
                rbtn_male.setSelected(true);
            } else if ((CURRENT_STUDENT.getGender() == null ? "" : CURRENT_STUDENT.getGender()).equalsIgnoreCase("FEMALE")) {
                rbtn_female.setSelected(true);
            } else {
                // if none was set in the database
                rbtn_male.setSelected(true);
            }
        } catch (NullPointerException f) {
        }
    }

    public String removeExtraSpace(String str) {
        return MonoString.removeExtraSpace(str);
    }

    public void setComboBoxValue(String value, ComboBox cmb) {
        ObservableList cmb_items = cmb.getItems();
        for (int i = 0; i < cmb_items.size(); i++) {
            if (value.equalsIgnoreCase(cmb_items.get(i).toString())) {
                cmb.getSelectionModel().select(i);
                break;
            }
        }
    }

    private void onRemove() {
        int res = Mono.fx().alert()
                .createConfirmation()
                .setHeader("Remove Student")
                .setMessage("Are you sure you want to remove "
                        + CURRENT_STUDENT.getId() + " from the list?")
                .confirmYesNo();

        if (res == 1) {
            this.CURRENT_STUDENT.setActive(0);
            if (Database.connect().student().update(this.CURRENT_STUDENT)) {
                Notifications.create()
                        .title("Removed Successfully")
                        .text("Student number " + this.CURRENT_STUDENT.getId()
                                + " is successfully removed.")
                        .showInformation();
            }
        }
    }

    public void back() {
        StudentHomeController controller = homeFX.getController();
        controller.onSearch();
        Animate.fade(application_root, 150, () -> {
            super.replaceRoot(homeFX.getApplicationRoot());
        }, homeFX.getApplicationRoot());
    }

    private void onSave() {
        update();
    }

    private void update() {
        if (getValues()) {
            int res = this.validateStudent(this.CURRENT_STUDENT.getCict_id(),
                    txt_studnum.getText());
            switch (res) {
                case 0:
                    if (this.updateStudentData()) {
                        Notifications.create()
                                .title("Updated Successfully")
                                .text("Student successfully updated.")
                                .showInformation();
                        this.setValues();
                    }
                    break;
            }
        }
    }

    private String studnum = "", lastname = "", firstname = "", middlename = "", campus = "", section = "", gender;
    private Integer yearLevel, group;

    private boolean getValues() {
        studnum = removeExtraSpace(txt_studnum.getText().toUpperCase());
        if (studnum == null ? false : studnum.isEmpty()) {
            Mono.fx().alert()
                    .createError()
                    .setHeader("Student Number")
                    .setMessage("Please enter the student number to proceed.")
                    .showAndWait();
            return false;
        }

        lastname = removeExtraSpace(txt_lastname.getText().toUpperCase());
        if (lastname == null ? false : lastname.isEmpty()) {
            Mono.fx().alert()
                    .createError()
                    .setHeader("Last Name")
                    .setMessage("Please enter the student last name to proceed.")
                    .showAndWait();
            return false;
        }

        firstname = removeExtraSpace(txt_firstname.getText().toUpperCase());
        if (firstname == null ? true : firstname.isEmpty()) {
            Mono.fx().alert()
                    .createError()
                    .setHeader("First Name")
                    .setMessage("Please enter the student first name to proceed.")
                    .showAndWait();
            return false;
        }

        middlename = removeExtraSpace(txt_middlename.getText().toUpperCase()) == null || removeExtraSpace(txt_middlename.getText().toUpperCase()).isEmpty() ? "" : removeExtraSpace(txt_middlename.getText().toUpperCase());

        gender = "";
        if (rbtn_male.isSelected()) {
            gender = "MALE";
        } else if (rbtn_female.isSelected()) {
            gender = "FEMALE";
        }

        try {
            campus = (cmb_campus.getSelectionModel().getSelectedItem().toString());
        } catch (NullPointerException a) {
            Mono.fx().alert()
                    .createError()
                    .setHeader("Campus")
                    .setMessage("Please select a campus to proceed.")
                    .showAndWait();
            return false;
        }
        try {
            String yrlvl = MonoString.removeExtraSpace(this.cmb_yrlvl.getSelectionModel().getSelectedItem().toString());
            yearLevel = (studentValues.getYearLevel(yrlvl)); // year lvl
        } catch (NullPointerException s) {
            Mono.fx().alert()
                    .createError()
                    .setHeader("Year Level")
                    .setMessage("Please select a student year level to proceed.")
                    .showAndWait();
            return false;
        }

        section = removeExtraSpace(txt_section.getText().toUpperCase()) == null || removeExtraSpace(txt_section.getText().toUpperCase()).isEmpty() ? "" : removeExtraSpace(txt_section.getText().toUpperCase());
        boolean invalidSection = false;
        try {
            Integer value = Integer.valueOf(section);
            if (value > 99 || value < 1) {
                invalidSection = true;
            }
        } catch (NumberFormatException e) {
            if (section.length() > 1) {
                invalidSection = true;
            }
        }
        if (invalidSection) {
            Mono.fx().alert()
                    .createError()
                    .setHeader("Section")
                    .setMessage("Please enter a valid section to proceed. "
                            + "Valid values are only 1-99 or A-Z.")
                    .showAndWait();
            return false;
        }

        group = (removeExtraSpace(txt_group.getText()) == null || removeExtraSpace(txt_group.getText()).isEmpty() ? null : 1);
        if (group.equals(1)) {
            boolean invalidGroup = false;
            try {
                group = (Integer.valueOf(removeExtraSpace(txt_group.getText())));
                if (!group.equals(1) || !group.equals(2)) {
                    invalidGroup = true;
                }
            } catch (NumberFormatException d) {
                invalidGroup = true;
            }
            if (invalidGroup) {
                Mono.fx().alert()
                        .createError()
                        .setHeader("Group")
                        .setMessage("Please enter a valid student group to proceed. "
                                + "Either one(1) or two(2) only.")
                        .showAndWait();
                return false;
            }
        }
        return true;
    }

    //------------------------------------------------
    //------------------------------------------------------------
    private final StudentValues sv = new StudentValues();

    public void setCmbYearLevel() {
        cmb_yrlvl.getItems().addAll(sv.getYearLevels());
        cmb_yrlvl.getSelectionModel().selectFirst();
        setSelectedYearLevel(cmb_yrlvl.getSelectionModel().getSelectedItem().toString());
    }

    public Integer selectedYearLevel;

    public void setSelectedYearLevel(String yrlvl) {
        selectedYearLevel = null;
        if (!yrlvl.equalsIgnoreCase(StudentValues.ALL)) {
            this.selectedYearLevel = sv.getYearLevel(yrlvl);
        }
    }

    public void setCmbCampus() {
        cmb_campus.getItems().add("Main");
        cmb_campus.getItems().add("Bustos");
        cmb_campus.getItems().add("Meneses");
        cmb_campus.getItems().add("Sarmiento");
        cmb_campus.getItems().add("Hagonoy");
        cmb_campus.getSelectionModel().selectFirst();;
    }

    public int validateStudent(Integer cict_id, String id) {
        if (findInactive(id)) {
            Mono.fx().alert()
                    .createWarning()
                    .setHeader("Content Restricted")
                    .setMessage("The student number you entered is already in the database but inactive.")
                    .show();
            return -1; //inactive
        } else if (checkExist(cict_id, id) != 0) {
            Mono.fx().alert()
                    .createWarning()
                    .setHeader("Student Exist")
                    .setMessage("The student with a student number of " + id
                            + " already exist.")
                    .show();
            System.out.println("@InfoStudent: Student exist");
            return -1; //exist
        } else {
            //update
            return 0;
        }
    }

    public int checkExist(Integer cict_id, String id) {
        StudentMapping student = Mono.orm().newSearch(Database.connect().student())
                .eq("id", id)
                .active()
                .first();
        if (student == null) {
            return 0;
        }
        if (Objects.equals(student.getCict_id(), cict_id)) {
            return 0;
        } else {
            return 1;
        }

    }

    public boolean findInactive(String id) {
        return !Mono.orm().newSearch(Database.connect().student())
                .eq("id", id)
                .inactive()
                .isEmpty();
    }

    public StudentMapping CURRENT_STUDENT = new StudentMapping();

    public boolean updateStudentData() {
        CURRENT_STUDENT.setId(studnum);
        CURRENT_STUDENT.setLast_name(lastname);
        CURRENT_STUDENT.setFirst_name(firstname);
        CURRENT_STUDENT.setMiddle_name(middlename);
        CURRENT_STUDENT.setGender(gender);
        CURRENT_STUDENT.setCampus(campus);
        CURRENT_STUDENT.setYear_level(yearLevel);
        CURRENT_STUDENT.setSection(section);
        CURRENT_STUDENT.set_group(group);
        Boolean res = Database.connect().student()
                .update(CURRENT_STUDENT);
        if (res) {
            System.out.println("@InfoStudent: UPDATE SUCCESS");
            return true;
        } else if (Objects.equals(res, Boolean.FALSE)) {
            Notifications.create()
                    .title("Nothing Happened")
                    .text("There is a referenced value violated "
                            + "in the values. Update is not yet possible.")
                    .showError();
        } else {
            Notifications.create()
                    .title("Connection Error")
                    .text("Please check your connectivity to the server.")
                    .showError();
        }
        return false;
    }

    public void setCmbGender(ComboBox cmb_gender) {
        cmb_gender.getItems().addAll(sv.getGenders());
        cmb_gender.getSelectionModel().selectFirst();
    }

}

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
package org.cict.evaluation.student.info;

import app.lazy.models.Database;
import app.lazy.models.StudentMapping;
import artifacts.MonoString;
import com.izum.fx.textinputfilters.StringFilter;
import com.izum.fx.textinputfilters.TextInputFilters;
import com.jhmvin.Mono;
import com.jhmvin.fx.display.ControllerFX;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.cict.evaluation.student.StudentValues;
import org.controlsfx.control.Notifications;

/**
 *
 * @author Joemar
 */
public class InfoStudentController implements ControllerFX{

    @FXML
    private Label btn_infoHistory;
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
    private Button btn_editsave;
    @FXML
    private ComboBox cmb_gender;
    @FXML
    private ComboBox cmb_curriculum;
    @FXML
    private Label btn_printCheckList;
    @FXML
    private TextField txt_section;
    @FXML
    private TextField txt_group;
    @FXML
    private ComboBox cmb_etype;
    @FXML
    private TextField txt_admissionYear;
    @FXML
    private Label btn_printStudentProfile;
    @FXML
    private Button btn_creditUnits;
    @FXML
    private Button btn_delete;
    
    private StudentMapping CURRENT_STUDENT;
    private InfoStudent infoStudent = new InfoStudent();
    private StudentValues studentValues = new StudentValues();
    
    public InfoStudentController(StudentMapping currentStudent) {
        this.CURRENT_STUDENT = currentStudent;
    }
    
    @Override
    public void onInitialization() {
        infoStudent.setCmbEnrollmentType(cmb_etype);
        infoStudent.setCmbGender(cmb_gender);
        infoStudent.setCmbYearLevel(cmb_yrlvl);
        infoStudent.setCmbCurriculum(cmb_curriculum);
        this.setValues();
        this.setAllEditable(Boolean.FALSE);
        addTextFieldFilters();
    }
    
    private void addTextFieldFilters() {
        StringFilter textId = TextInputFilters.string()
                .setFilterMode(StringFilter.LETTER_DIGIT)
                .setMaxCharacters(50)
                .setNoLeadingTrailingSpaces(true)
                .setFilterManager(filterManager->{
                    if(!filterManager.isValid()) {
                        Mono.fx().alert().createWarning().setHeader("Warning")
                                .setMessage(filterManager.getMessage())
                                .show();
                    }
                });
        textId.clone().setTextSource(txt_studnum).applyFilter();
        
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
        textName.clone().setTextSource(txt_firstname).applyFilter();
        textName.clone().setTextSource(txt_lastname).applyFilter();
        textName.clone().setTextSource(txt_middlename).applyFilter();
        textName.clone().setTextSource(txt_section).applyFilter();
        
        StringFilter textGroup = TextInputFilters.string()
                .setFilterMode(StringFilter.DIGIT)
                .setMaxCharacters(11)
                .setNoLeadingTrailingSpaces(true)
                .setFilterManager(filterManager->{
                    if(!filterManager.isValid()) {
                        Mono.fx().alert().createWarning().setHeader("Warning")
                                .setMessage(filterManager.getMessage())
                                .show();
                    }
                });
        textGroup.clone().setTextSource(txt_group).applyFilter();
        
        StringFilter textAdYear = TextInputFilters.string()
                .setFilterMode(StringFilter.DIGIT)
                .setMaxCharacters(50)
                .setNoLeadingTrailingSpaces(true)
                .setFilterManager(filterManager->{
                    if(!filterManager.isValid()) {
                        Mono.fx().alert().createWarning().setHeader("Warning")
                                .setMessage(filterManager.getMessage())
                                .show();
                    }
                });
        textAdYear.clone().setTextSource(txt_admissionYear).applyFilter();
    }

    @Override
    public void onEventHandling() {
        this.buttonEvents();
    }
    
    private void buttonEvents() {
        this.editSaveEvent();
        btn_delete.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent e) -> {
            if (close) {
                this.onClose();
            }
            this.onRemove();
        });
    }
    
    private void setAllEditable(Boolean answer) {
        txt_firstname.setEditable(answer);
        txt_group.setEditable(answer);
        txt_lastname.setEditable(answer);
        txt_middlename.setEditable(answer);
        txt_section.setEditable(answer);
        txt_studnum.setEditable(answer);
        txt_admissionYear.setEditable(answer);

        cmb_curriculum.setDisable(!answer);
        cmb_etype.setDisable(!answer);
        cmb_gender.setDisable(!answer);
        cmb_yrlvl.setDisable(!answer);
    }
    
    boolean close;
    private void setValues() {
        try {
            lbl_cictid.setText(CURRENT_STUDENT.getCict_id().toString());
            try {
                txt_admissionYear.setText(CURRENT_STUDENT.getAdmission_year());
            } catch (NullPointerException e) {
            }
            txt_firstname.setText(CURRENT_STUDENT.getFirst_name());
            try {
                txt_group.setText(CURRENT_STUDENT.get_group().toString());
            } catch (NullPointerException s) {
            }
            txt_lastname.setText(CURRENT_STUDENT.getLast_name());
            txt_middlename.setText(CURRENT_STUDENT.getMiddle_name().toString());
            try {
                txt_section.setText(CURRENT_STUDENT.getSection());
            } catch (NullPointerException f) {
            }
            txt_studnum.setText(CURRENT_STUDENT.getId().toString());
            try {
                setComboBoxValue(infoStudent.getCurriculumName(CURRENT_STUDENT.getCURRICULUM_id()), cmb_curriculum);
            } catch (NullPointerException d) {
            }
            try {
                setComboBoxValue(CURRENT_STUDENT.getEnrollment_type(), cmb_etype);
                if (cmb_etype.getSelectionModel().getSelectedItem().toString().equalsIgnoreCase(studentValues.CROSS_ENROLLEE)) {
                    btn_creditUnits.setDisable(true);
                }
            } catch (NullPointerException g) {
            }
            try {
                setComboBoxValue(CURRENT_STUDENT.getGender(), cmb_gender);
            } catch (NullPointerException r) {
            }
            try {
                this.cmb_yrlvl.getSelectionModel().select(CURRENT_STUDENT.getYear_level()-1);
            } catch (NullPointerException l) {
            }
            close = false;
        } catch (NullPointerException f) {
            close = true;
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
                .setMessage("Are you sure you want to remove " +
                        CURRENT_STUDENT.getId() + " from the list?")
                .confirmYesNo();
        
        if (res == 1) {
            this.CURRENT_STUDENT.setActive(0);
            if(Database.connect().student().update(this.CURRENT_STUDENT)) {
                Notifications.create()
                        .title("Removed Successfully")
                        .text("Student number " + this.CURRENT_STUDENT.getId() + 
                                " is successfully removed.")
                        .showInformation();
            }
        }
    }

    public void onClose() {
        Mono.fx().getParentStage(btn_delete).close();
    }
    
    private void editSaveEvent() {
        this.setAllEditable(false);
        btn_editsave.setText("Edit");
        btn_editsave.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            if (close) {
                this.onClose();
            }
            btn_editsave.setText("Save changes");
            this.setAllEditable(Boolean.TRUE);
            changeEditButtonEvent();
        });
    }
    
    private void changeEditButtonEvent() {
        btn_editsave.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent d) -> {
            updateStudent();
        });
    }

    private void updateStudent() {
        if (getValues()) {
            int res = infoStudent.validateStudent(this.CURRENT_STUDENT.getCict_id(),
                    txt_studnum.getText());
            switch (res) {
                case 0:
                    infoStudent.student = this.CURRENT_STUDENT;
                    if (infoStudent.updateStudent(values)) {
                        Notifications.create()
                                .title("Updated Successfully")
                                .text("Student successfully updated.")
                                .showInformation();
//                        this.editSaveEvent();
                    }
                    break;
            }
        }
    }

    private ArrayList values = new ArrayList();

    private boolean getValues() {
        ArrayList temp_values = new ArrayList();
        String studnum = "", lastname = "", firstname = "", middlename = "";
        try {
            studnum = removeExtraSpace(txt_studnum.getText().toUpperCase());
            if (studnum.isEmpty()) {
                Mono.fx().alert()
                        .createError()
                        .setHeader("Student Number")
                        .setMessage("Please enter the student number to proceed.")
                        .showAndWait();
                return false;
            }
        } catch (NullPointerException d) {
        }
        try {
            lastname = removeExtraSpace(txt_lastname.getText().toUpperCase());
            if (lastname.isEmpty()) {
                Mono.fx().alert()
                        .createError()
                        .setHeader("Last Name")
                        .setMessage("Please enter the student last name to proceed.")
                        .showAndWait();
                return false;
            }
        } catch (NullPointerException a) {
        }
        try {
            firstname = removeExtraSpace(txt_firstname.getText().toUpperCase());
            if (firstname.isEmpty()) {
                Mono.fx().alert()
                        .createError()
                        .setHeader("First Name")
                        .setMessage("Please enter the student first name to proceed.")
                        .showAndWait();
                return false;
            }
        } catch (NullPointerException r) {
        }
        try {
            middlename = removeExtraSpace(txt_middlename.getText().toUpperCase());
            if (middlename.isEmpty()) {
                Mono.fx().alert()
                        .createError()
                        .setHeader("Middle Name")
                        .setMessage("Please enter the student middle name to proceed.")
                        .showAndWait();
                return false;
            }
        } catch (NullPointerException v) {
        }
        temp_values.add(studnum);
        temp_values.add(lastname);
        temp_values.add(firstname);
        temp_values.add(middlename);
        try {
            temp_values.add(cmb_gender.getSelectionModel().getSelectedItem().toString()); // gender
        } catch (NullPointerException s) {
            Mono.fx().alert()
                    .createError()
                    .setHeader("Gender")
                    .setMessage("Please select a gender to proceed.")
                    .showAndWait();
            return false;
        }
        try {
            temp_values.add(infoStudent.getCurriculumId(cmb_curriculum.getSelectionModel().getSelectedItem().toString()));
        } catch (NullPointerException a) {
            Mono.fx().alert()
                    .createError()
                    .setHeader("Curriculum")
                    .setMessage("Please select a student curriculum to proceed.")
                    .showAndWait();
            return false;
        }
        try {
            String yrlvl = MonoString.removeExtraSpace(this.cmb_yrlvl.getSelectionModel().getSelectedItem().toString());
            temp_values.add(studentValues.getYearLevel(yrlvl)); // year lvl
        } catch (NullPointerException s) {
            Mono.fx().alert()
                    .createError()
                    .setHeader("Year Level")
                    .setMessage("Please select a student year level to proceed.")
                    .showAndWait();
            return false;
        }
        try {
            String section = removeExtraSpace(txt_section.getText().toUpperCase());
            if (section.isEmpty()) {
                Mono.fx().alert()
                        .createError()
                        .setHeader("Section")
                        .setMessage("Please enter the student section to proceed.")
                        .showAndWait();
                return false;
            }
            temp_values.add(section);
        } catch (NullPointerException s) {
            temp_values.add("");
        }
        String group = removeExtraSpace(txt_group.getText());
        if (group.isEmpty()) {
            Mono.fx().alert()
                    .createError()
                    .setHeader("Group")
                    .setMessage("Please enter the student group to proceed.")
                    .showAndWait();
            return false;
        }
        try {
            temp_values.add(Integer.valueOf(group));
        } catch (NumberFormatException d) {
            Mono.fx().alert()
                    .createError()
                    .setHeader("Group")
                    .setMessage("Please enter a valid student group to proceed. "
                            + "Single numeric value is required.")
                    .showAndWait();
            return false;
        }
        try {
            temp_values.add(cmb_etype.getSelectionModel().getSelectedItem().toString());
        } catch (NullPointerException a) {
            Mono.fx().alert()
                    .createError()
                    .setHeader("Enrollment Type")
                    .setMessage("Please select a student enrollment type to proceed.")
                    .showAndWait();
            return false;
        }
        this.values = temp_values;
        return true;
    }
}

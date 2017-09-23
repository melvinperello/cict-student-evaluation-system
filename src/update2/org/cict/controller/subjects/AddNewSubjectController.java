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
package update2.org.cict.controller.subjects;

import app.lazy.models.CurriculumMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.SubjectMapping;
import artifacts.MonoString;
import com.jfoenix.controls.JFXComboBox;
import com.jhmvin.Mono;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import org.cict.SubjectClassification;

/**
 *
 * @author Joemar
 */
public class AddNewSubjectController extends SceneFX implements ControllerFX{

    @FXML
    private VBox vbox_main;
    
    @FXML
    private TextField text_subjectCode;

    @FXML
    private TextField txt_descriptiveTitle;

    @FXML
    private TextField txt_lecUnits;

    @FXML
    private TextField txt_labUnits;

    @FXML
    private JFXComboBox<String> cmbb_type;
    
    @FXML
    private JFXComboBox<String> cmb_subtype;

    @FXML
    private Button btn_add;
    
    private ArrayList<CurriculumMapping> curriculums;
    
    private String searchKeyword;
    public void setTextFieldSubjectCode(String str) {
        searchKeyword = str;
    }
    
    @Override
    public void onInitialization() {
        if(searchKeyword != null)
            text_subjectCode.setText(searchKeyword);
        this.loadComboBox();
    }

    @Override
    public void onEventHandling() {
        addClickEvent(btn_add, () -> {
            this.addNewSubject();
        });
        Mono.fx().key(KeyCode.ENTER).release(vbox_main, ()->{
            this.addNewSubject();
        });
    }
    
    private String acadProgram, subjectCode, descriptiveTitle, type, subType;
    private Double lecDbl, labDbl;
    private void addNewSubject() {
//        acadProgram = cmb_academicProgram.getSelectionModel().getSelectedItem();
        subjectCode = MonoString.removeExtraSpace(text_subjectCode.getText()).toUpperCase();
        descriptiveTitle = MonoString.removeExtraSpace(txt_descriptiveTitle.getText()).toUpperCase();
        String lec = MonoString.removeExtraSpace(txt_lecUnits.getText());
        String lab = MonoString.removeExtraSpace(txt_labUnits.getText());
        type = cmbb_type.getSelectionModel().getSelectedItem().toUpperCase();
        subType = cmb_subtype.getSelectionModel().getSelectedItem().toUpperCase();
        
//        if(acadProgram == null) {
//            Mono.fx().alert()
//                    .createWarning()
//                    .setHeader("Academic Program")
//                    .setMessage("Please select a program to add this subject")
//                    .showAndWait();
//            return;
//        }
        
        if (subjectCode.equals("")) {
            Mono.fx().alert()
                    .createWarning()
                    .setHeader("Subject Code")
                    .setMessage("Please fill up the value for Subject Code.")
                    .showAndWait();
//            MonoDialog.showAlert("Subject Code", "Please fill up the value for Subject Code", "error");
            return;
        }
        
        if (descriptiveTitle.equals("")) {
            Mono.fx().alert()
                    .createWarning()
                    .setHeader("Descriptive Title")
                    .setMessage("Please fill up the value for Descriptive Title.")
                    .showAndWait();
//            MonoDialog.showAlert("Descriptive Title", "Please fill up the value for Descriptive Title", "error");
            return;
        }
        
        if(lec.equals("")) {
            Mono.fx().alert()
                    .createWarning()
                    .setHeader("Lecture Units")
                    .setMessage("Please fill up the value for Lecture Units.")
                    .showAndWait();
            return;
        }
        
        lecDbl = 0.0;
        try{
            lecDbl = Double.valueOf(lec);
        } catch(NumberFormatException a) {
            Mono.fx().alert()
                    .createWarning()
                    .setHeader("Invalid Lecture Units")
                    .setMessage("Please fill up a valid value for Lecture Units.")
                    .showAndWait();
            return;
        }
        
        if(lab.equals("")) {
            Mono.fx().alert()
                    .createWarning()
                    .setHeader("Laboratory Units")
                    .setMessage("Please fill up the value for Laboratory Units.")
                    .showAndWait();
            return;
        }
        
        labDbl = 0.0;
        try{
            labDbl = Double.valueOf(lab);
        } catch(NumberFormatException a) {
            Mono.fx().alert()
                    .createWarning()
                    .setHeader("Invalid Laboratory Units")
                    .setMessage("Please fill up a valid value for Laboratory Units.")
                    .showAndWait();
            return;
        }
        
        Boolean res = isSubjectExisting();
        try {
            if(res) {
                Mono.fx().alert()
                        .createWarning()
                        .setHeader("Subject Exist")
                        .setMessage("The subject details you are trying to add is already in the list of subjects. Try changing any detail, and click Add again.")
                        .showAndWait();
            } else if(res == false){
                insertSubject();
            }
        } catch(NullPointerException a) {
            System.out.println("isSubjectExisting : " + res);
        }
    }
    
    private Boolean isSubjectExisting() {
        SubjectMapping exist = Mono.orm().newSearch(Database.connect().subject())
                .eq(DB.subject().code, subjectCode)
                .eq(DB.subject().descriptive_title, descriptiveTitle)
                .eq(DB.subject().type, type)
                .eq(DB.subject().subtype, subType)
                .eq(DB.subject().lec_units, lecDbl)
                .eq(DB.subject().lab_units, labDbl)
                .execute()
                .first();
        
        if(exist != null) {
            if(exist.getActive() != 1) {
                int res = Mono.fx().alert()
                        .createConfirmation()
                        .setHeader("Restore Subject")
                        .setMessage("Subject is already existing but inactive. Do you want to restore it?")
                        .confirmYesNo();
                if(res == 1) {
                    exist.setActive(1);
                    if(Database.connect().subject().update(exist)) {
                        Mono.fx().alert()
                                .createInfo()
                                .setHeader("Restored Successfully")
                                .setMessage("Subject is restored successfully.")
                                .showAndWait();
                        newSubject = exist;
                        Mono.fx().getParentStage(btn_add).close();
                    }
                }
                return null;
            } else
                return true;
        } else 
            return false;
    }
    
    private SubjectMapping newSubject;
    public SubjectMapping getNewSubject() {
        return newSubject;
    }
    
    private void insertSubject() {
        newSubject = new SubjectMapping();
        newSubject.setCode(subjectCode);
        newSubject.setDescriptive_title(descriptiveTitle);
        newSubject.setLec_units(lecDbl);
        newSubject.setLab_units(labDbl);
        newSubject.setSubtype(subType);
        newSubject.setType(type);
        int res = Database.connect().subject().insert(newSubject);
        if(res != -1) {
            Mono.fx().alert()
                    .createInfo()
                    .setHeader("Successfully Added")
                    .setMessage("Subject successfully added to the list.")
                    .showAndWait();
            Mono.fx().getParentStage(btn_add).close();
        } else {
            Mono.fx().alert()
                    .createError()
                    .setHeader("Adding Failed")
                    .setMessage("Database connection might have a problem.")
                    .showAndWait();
        }
    }
    
    private void loadComboBox() {
        
        /**
         * Types
         */
        cmbb_type.getItems().clear();
        cmbb_type.getItems().add(SubjectClassification.TYPE_MAJOR);
        cmbb_type.getItems().add(SubjectClassification.TYPE_MINOR);
        cmbb_type.getItems().add(SubjectClassification.TYPE_ELECTIVE);
        cmbb_type.getItems().add(SubjectClassification.TYPE_PE);
        cmbb_type.getItems().add(SubjectClassification.TYPE_NSTP);
        cmbb_type.getItems().add(SubjectClassification.TYPE_CAPSTONE);
        cmbb_type.getItems().add(SubjectClassification.TYPE_INTERNSHIP);
        cmbb_type.getSelectionModel().selectFirst();
        
        /**
         * Subtypes
         */
        cmb_subtype.getItems().clear();
        cmb_subtype.getItems().add(SubjectClassification.SUBTYPE_NONE);
        cmb_subtype.getItems().add(SubjectClassification.SUBTYPE_PROGRAMMING);
        cmb_subtype.getItems().add(SubjectClassification.SUBTYPE_HARDWARE);
        cmb_subtype.getItems().add(SubjectClassification.SUBTYPE_GRAPHICS);
        cmb_subtype.getSelectionModel().selectFirst();
    }
}

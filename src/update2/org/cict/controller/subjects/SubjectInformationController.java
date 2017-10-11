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
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.cict.GenericLoadingShow;
import org.cict.SubjectClassification;
import org.controlsfx.control.Notifications;
import update2.org.cict.controller.academicprogram.AcademicProgramAccessManager;
import update3.org.cict.SectionConstants;

/**
 *
 * @author Joemar
 */
public class SubjectInformationController extends SceneFX implements ControllerFX {

    @FXML
    private VBox application_root;
    
    @FXML
    private JFXButton btn_edit;

    @FXML
    private TextField txt_subjectCode;

    @FXML
    private TextField txt_descriptiveTitle;

    @FXML
    private TextField txt_lecUnits;

    @FXML
    private TextField txt_labUnits;

    @FXML
    private ComboBox<String> cmb_type;

    @FXML
    private ComboBox<String> cmb_subtype;

    @FXML
    private Label lbl_subject_id;
    
    @FXML
    private VBox vbox_no_result;

    @FXML
    private VBox vbox_curriculums;
    
    @FXML
    private JFXButton btn_back;

    private SubjectMapping SUBJECT;
    private ArrayList<CurriculumMapping> curriculums;
    
    public SubjectInformationController(SubjectMapping subject) {
        SUBJECT = subject;
    }
    
    @Override
    public void onInitialization() {
        bindScene(application_root);
        this.loadComboBox();
        this.setPreview(true);
        this.setSubjectDetails();
        
        if (AcademicProgramAccessManager.denyIfNotAdmin()) {
            btn_edit.setDisable(true);
        }
    }

    private final String SECTION_BASE_COLOR = "#E85764";
    @Override
    public void onEventHandling() {
        addClickEvent(btn_edit, () -> {
            onEdit();
        });
        
        this.addClickEvent(btn_back, ()->{
            onBack();
        });
        
        cmb_type.valueProperty().addListener((e) -> {
            String selected = cmb_type.getSelectionModel().getSelectedItem();
            cmb_subtype.setDisable(!SubjectClassification.isMajor(selected));
            if(cmb_subtype.isDisable())
                cmb_subtype.getSelectionModel().select("NONE");
        });
    }
    
    private void onEdit() {
        if(canEdit) {
            if(btn_edit.getText().equalsIgnoreCase("Save Changes")) {
                // update here
                validateInputs();
            } else
                setPreview(false);
        } else {
            Mono.fx().alert()
                    .createWarning()
                    .setHeader("Subject Cannot Be Modified")
                    .setMessage("It seems like the subject has an implemented curriculum, therefore modifying is prohibited. Add new subject if needed.")
                    .showAndWait();
        }
    }
    
    private void onBack() {
        if(!isEdited) {
            Animate.fade(this.application_root, SectionConstants.FADE_SPEED, () -> {
                super.replaceRoot(homeFx.getApplicationRoot());
            }, homeFx.getApplicationRoot());
            return;
        }
        SubjectRepositoryController controller = new SubjectRepositoryController();
        Pane pane = Mono.fx().create()
                .setPackageName("update2.org.cict.layout.subjects")
                .setFxmlDocument("subject-bank")
                .makeFX()
                .setController(controller)
                .pullOutLayout();

        super.setSceneColor(SECTION_BASE_COLOR); // call once on entire scene lifecycle

        Animate.fade(this.application_root, SectionConstants.FADE_SPEED, () -> {
            super.replaceRoot(pane);
        }, pane);
    }
    
    private void setPreview(boolean view) {
        if(view)
            btn_edit.setText("Edit");
        else
            btn_edit.setText("Save Changes");
        
        txt_descriptiveTitle.setDisable(view);
        txt_labUnits.setDisable(view);
        txt_lecUnits.setDisable(view);
        txt_subjectCode.setDisable(view);
        
        cmb_subtype.setDisable(view);
        cmb_type.setDisable(view);
        if(!view) {
            if(!SubjectClassification.isMajor(cmb_type.getSelectionModel().getSelectedItem()))
                cmb_subtype.setDisable(true);
        }
            
    }
    
    private void setSubjectDetails() {
        lbl_subject_id.setText(SUBJECT.getId().toString());
        txt_subjectCode.setText(SUBJECT.getCode());
        txt_descriptiveTitle.setText(SUBJECT.getDescriptive_title());
        txt_lecUnits.setText(SUBJECT.getLec_units().toString());
        txt_labUnits.setText(SUBJECT.getLab_units().toString());
        
        cmb_subtype.getSelectionModel().select(SUBJECT.getSubtype().toUpperCase());
        cmb_type.getSelectionModel().select(SUBJECT.getType().toUpperCase());
        
        FetchSubjectInformation fetch = new FetchSubjectInformation();
        fetch.subjectID = this.SUBJECT.getId();
        
        fetch.setOnSuccess(onSuccess -> {
            curriculums = fetch.getCurriculums();
            createCurriculumTable();
        });
        
        fetch.setOnCancel(onCancel -> {
            vbox_no_result.setVisible(true);
        });
        
        fetch.transact();
        
    }
    
    private SimpleTable curriculumTable = new SimpleTable();
    private void createCurriculumTable() {
        for (CurriculumMapping curriculum : curriculums) {
            createRow(curriculum);
        }
        
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(curriculumTable);
        simpleTableView.setFixedWidth(true);
        simpleTableView.setParentOnScene(vbox_curriculums);
        this.vbox_curriculums.setVisible(true);
    }
    
    private Boolean canEdit = true;
    private void createRow(CurriculumMapping curriculum) {
        SimpleTableRow row = new SimpleTableRow();
        row.setRowHeight(71.0);

        HBox curriculumRow = (HBox) Mono.fx().create()
                .setPackageName("update2.org.cict.layout.subjects")
                .setFxmlDocument("subject-info-curriculum-row")
                .makeFX()
                .pullOutLayout();

        Label lbl_name = searchAccessibilityText(curriculumRow, "name");
        Label lbl_major = searchAccessibilityText(curriculumRow, "major");
        Label lbl_status = searchAccessibilityText(curriculumRow, "status");
        
        try {
            lbl_name.setText(curriculum.getName());
        } catch (Exception e) {
        }
        try {
            lbl_major.setText(curriculum.getMajor());
        } catch (Exception e) {
        }
        String status = "UNIMPLEMENTED";
        try {
            if(curriculum.getImplemented() == 1) {
                status = "IMPLEMENTED";
                canEdit = false;
            }
        } catch (Exception e) {
        }
        lbl_status.setText(status);
         
        SimpleTableCell cellParent = new SimpleTableCell();
        cellParent.setResizePriority(Priority.ALWAYS);
        cellParent.setContent(curriculumRow);

        row.addCell(cellParent);

        curriculumTable.addRow(row);
    }
    
    private void loadComboBox() {
        /**
         * Types
         */
        cmb_type.getItems().clear();
        cmb_type.getItems().add(SubjectClassification.TYPE_MAJOR);
        cmb_type.getItems().add(SubjectClassification.TYPE_MINOR);
        cmb_type.getItems().add(SubjectClassification.TYPE_ELECTIVE);
        cmb_type.getItems().add(SubjectClassification.TYPE_PE);
        cmb_type.getItems().add(SubjectClassification.TYPE_NSTP);
        cmb_type.getItems().add(SubjectClassification.TYPE_CAPSTONE);
        cmb_type.getItems().add(SubjectClassification.TYPE_INTERNSHIP);
        cmb_type.getSelectionModel().selectFirst();
        
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
    
       
    private String subjectCode, descriptiveTitle, type, subType;
    private Double lecDbl, labDbl;
    private void validateInputs() {
        subjectCode = MonoString.removeExtraSpace(txt_subjectCode.getText()).toUpperCase();
        descriptiveTitle = MonoString.removeExtraSpace(txt_descriptiveTitle.getText()).toUpperCase();
        String lec = MonoString.removeExtraSpace(txt_lecUnits.getText());
        String lab = MonoString.removeExtraSpace(txt_labUnits.getText());
        type = cmb_type.getSelectionModel().getSelectedItem().toUpperCase();
        subType = cmb_subtype.getSelectionModel().getSelectedItem().toUpperCase();
     
        if (subjectCode.equals("")) {
            Mono.fx().alert()
                    .createWarning()
                    .setHeader("Subject Code")
                    .setMessage("Please fill up the value for Subject Code.")
                    .showAndWait();
            return;
        }
        
        if (descriptiveTitle.equals("")) {
            Mono.fx().alert()
                    .createWarning()
                    .setHeader("Descriptive Title")
                    .setMessage("Please fill up the value for Descriptive Title.")
                    .showAndWait();
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
        
        try {
            if(!isSubjectExisting())
                updateSubject();
        } catch(NullPointerException a) {}
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
            if(!Objects.equals(exist.getId(), SUBJECT.getId())) {
                if(exist.getActive() != 1) {
                    //restricted
                    Mono.fx().alert()
                            .createError()
                            .setHeader("Content Restricted")
                            .setMessage("The subject details are existing but inactive.")
                            .showAndWait();
                    return true;
                } else {
                    Mono.fx().alert()
                            .createWarning()
                            .setHeader("Subject Exist")
                            .setMessage("The subject is already in the list of subjects.")
                            .showAndWait();
                    return true;
                }
            } else 
                return false;
        } else 
            return false;
    }
     
    public Boolean isEdited = false;
    public SubjectMapping editedSubject;
    private void updateSubject() {
        SUBJECT.setCode(subjectCode);
        SUBJECT.setDescriptive_title(descriptiveTitle);
        SUBJECT.setLec_units(lecDbl);
        SUBJECT.setLab_units(labDbl);
        SUBJECT.setSubtype(subType);
        SUBJECT.setType(type);
        
        if(Database.connect().subject().update(SUBJECT)) {
            Notifications.create()
                    .title("Successfully Updated")
                    .text(subjectCode + " : " + descriptiveTitle)
                    .showInformation();
            txt_descriptiveTitle.setText(descriptiveTitle);
            txt_subjectCode.setText(subjectCode);
            isEdited = true;
            editedSubject = SUBJECT;
        } else {
            Notifications.create()
                    .title("Update Failed")
                    .text("Database connection might have a problem.")
                    .showInformation();
        }
    }
      
    private LayoutDataFX homeFx;
    public void setHomeFx(LayoutDataFX homeFx) {
        this.homeFx = homeFx;
    }

}

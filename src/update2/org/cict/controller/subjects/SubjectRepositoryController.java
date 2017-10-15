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
package update2.org.cict.controller.subjects;

import app.lazy.models.CurriculumMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.SubjectMapping;
import artifacts.MonoString;
import com.izum.fx.textinputfilters.DoubleFilter;
import com.izum.fx.textinputfilters.StringFilter;
import com.izum.fx.textinputfilters.TextInputFilters;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.SimpleTask;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.LayoutDataFX;
import com.jhmvin.fx.display.SceneFX;
import com.jhmvin.transitions.Animate;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.text.WordUtils;
import org.cict.SubjectClassification;
import org.controlsfx.control.Notifications;
import update2.org.cict.controller.academicprogram.AcademicHome;
import update2.org.cict.controller.academicprogram.AcademicProgramAccessManager;
import update3.org.cict.SectionConstants;

/**
 *
 * @author Jhon Melvin
 */
public class SubjectRepositoryController extends SceneFX implements ControllerFX {

    @FXML
    private AnchorPane anchor_main;

    @FXML
    private JFXButton btn_home;

    @FXML
    private VBox vbox_subjects;

    @FXML
    private HBox hbox_searching;

    @FXML
    private JFXButton btn_new_subject;

    @FXML
    private HBox hbox_none;

    @FXML
    private AnchorPane anchor_new_subject;

    @FXML
    private VBox vbox_main;

    @FXML
    private JFXButton btn_back;

    @FXML
    private TextField text_subjectCode;

    @FXML
    private TextField txt_descriptiveTitle;

    @FXML
    private TextField txt_lecUnits;

    @FXML
    private TextField txt_labUnits;

    @FXML
    private ComboBox<String> cmbb_type;

    @FXML
    private ComboBox<String> cmb_subtype;

    @FXML
    private Button btn_add;


    private ArrayList<SubjectMapping> lst_subject;
    private SimpleTable subjectTable = new SimpleTable();
    
    @Override
    public void onInitialization() {
        bindScene(anchor_main);
        
        FetchSubjects fetch = new FetchSubjects();
        fetch.setOnStart(onStart -> {
            this.hbox_searching.setVisible(true);
            btn_new_subject.setDisable(true);
        });
        fetch.setOnSuccess(onSuccess -> {
            lst_subject = fetch.getSubjectResult();
            
//            createTable();
//            this.hbox_searching.setVisible(false);
            renderTable();
            if (AcademicProgramAccessManager.denyIfNotAdmin()) {
                btn_new_subject.setDisable(true);
            } else 
                btn_new_subject.setDisable(false);
        });
        fetch.setOnCancel(onCancel -> {
            this.hbox_none.setVisible(true);
            this.hbox_searching.setVisible(false);
        });
        fetch.setRestTime(2000);
        fetch.transact();
        
        this.loadComboBox();
        
        addTextFieldFilters();
    }
    
    private void addTextFieldFilters() {
        StringFilter textField = TextInputFilters.string()
                .setFilterMode(StringFilter.LETTER_DIGIT_SPACE)
                .setMaxCharacters(100)
                .setNoLeadingTrailingSpaces(false)
                .setFilterManager(filterManager->{
                    if(!filterManager.isValid()) {
                        Mono.fx().alert().createWarning().setHeader("Warning")
                                .setMessage(filterManager.getMessage())
                                .show();
                    }
                });
        textField.clone().setTextSource(text_subjectCode).applyFilter();
        textField.clone().setTextSource(txt_descriptiveTitle).applyFilter();
     
        DoubleFilter textNumber = TextInputFilters.doubleFloating()
//                .setFilterMode(StringFilter.DIGIT)
//                .setMaxCharacters(100)
//                .setNoLeadingTrailingSpaces(true)
                .setFilterManager(filterManager->{
                    if(!filterManager.isValid()) {
                        Mono.fx().alert().createWarning().setHeader("Warning")
                                .setMessage(filterManager.getMessage())
                                .show();
                    }
                });
        textNumber.clone().setTextSource(txt_labUnits).applyFilter();
        textNumber.clone().setTextSource(txt_lecUnits).applyFilter();
    }

    private void renderTable() {
        SimpleTask rendering_task = new SimpleTask("rendering_task");
        rendering_task.setTask(()->{
            createTable();
        });
        rendering_task.setOnSuccess((a)->{
            this.hbox_searching.setVisible(false);
        });
        rendering_task.start();
        
    }
    
    private LayoutDataFX homeFX;
    public void setHomeFX(LayoutDataFX homeFX) {
        this.homeFX = homeFX;
    }
    
    @Override
    public void onEventHandling() {
        
        addClickEvent(btn_new_subject, () -> {
            anchor_new_subject.setVisible(true);
            btn_new_subject.setDisable(true);
        });
        
        addClickEvent(btn_home, () -> {
            AcademicHome controller = new AcademicHome();
            Pane pane = Mono.fx().create()
                    .setPackageName("update2.org.cict.layout.academicprogram")
                    .setFxmlDocument("academic-home")
                    .makeFX()
                    .setController(controller)
                    .pullOutLayout();

            super.setSceneColor(SECTION_BASE_COLOR); // call once on entire scene lifecycle

            Animate.fade(this.anchor_main, SectionConstants.FADE_SPEED, () -> {
                super.replaceRoot(pane);
            }, pane);
        });
        
        addNewSubjectEvents();
    }
    
//    private SimpleTableView simpleTableView;
    private void createTable() {
        for(SubjectMapping subject: lst_subject) {
            createRow(subject);
        }
        
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(subjectTable);
        simpleTableView.setFixedWidth(true);
        
        Mono.fx().thread().wrap(() -> {
            simpleTableView.setParentOnScene(vbox_subjects);
        });
    }
    
    private ArrayList<CurriculumMapping> curriculums;
    private boolean canBeRemove;
    private void createRow(SubjectMapping subject) {
        
        SimpleTableRow row = new SimpleTableRow();
        row.setRowHeight(70.0);

        HBox programRow = (HBox) Mono.fx().create()
                .setPackageName("update2.org.cict.layout.subjects")
                .setFxmlDocument("row-subject")
                .makeFX()
                .pullOutLayout();

        Label lbl_code = searchAccessibilityText(programRow, "subject_code");
        Label lbl_descriptive_title = searchAccessibilityText(programRow, "descriptive_title");
        Label lbl_lec = searchAccessibilityText(programRow, "lec");
        Label lbl_lab = searchAccessibilityText(programRow, "lab");
        Label lbl_subtype = searchAccessibilityText(programRow, "subtype");
        Label lbl_type = searchAccessibilityText(programRow, "type");
        
        JFXButton btn_more_info = searchAccessibilityText(programRow, "btn_more_info");
        btn_more_info.addEventHandler(MouseEvent.MOUSE_CLICKED, (e)->{
            this.onOpenSubjectInfoWindow(subject);
       });
        
        JFXButton btn_remove = searchAccessibilityText(programRow, "btn_remove");
        
        if (AcademicProgramAccessManager.denyIfNotAdmin()) {
            btn_remove.setDisable(true);
        }
        
        addClickEvent(btn_remove, () -> {
            canBeRemove = true;
            FetchSubjectInformation fetch = new FetchSubjectInformation();
            fetch.subjectID = subject.getId();

            fetch.setOnSuccess(onSuccess -> {
                curriculums = fetch.getCurriculums();
                for(CurriculumMapping curriculum: curriculums) {
                    try {
                        if(curriculum.getImplemented() == 1) 
                            canBeRemove = false;
                    } catch (Exception e) {
                    }
                }
                askIfRemove(subject, row);
            });
            
            fetch.setOnCancel(onCancel -> {
                askIfRemove(subject, row);
            });

            fetch.transact();
            
        });
        
        lbl_code.setText(subject.getCode());
        lbl_descriptive_title.setText(subject.getDescriptive_title());
        lbl_lec.setText(subject.getLec_units().toString());
        lbl_lab.setText(subject.getLab_units().toString());
        lbl_subtype.setText(WordUtils.capitalizeFully(subject.getSubtype()));
        lbl_type.setText(subject.getType());
        
        SimpleTableCell cellParent = new SimpleTableCell();
        cellParent.setResizePriority(Priority.ALWAYS);
        cellParent.setContent(programRow);

        row.addCell(cellParent);

        subjectTable.addRow(row);
    }
        
    private void askIfRemove(SubjectMapping subject, SimpleTableRow row) {
        if(canBeRemove) {
            int res = Mono.fx().alert()
                    .createConfirmation()
                    .setHeader("Remove Subject")
                    .setMessage("Are you sure you want to remove " + subject.getCode() 
                            + " from the list of subjects?")
                    .confirmYesNo();
            if(res == 1) {
                if(removeSubject(subject)) {
                    row.getChildren().clear();
                }
            }
        } else {
            Mono.fx().alert()
                    .createWarning()
                    .setHeader("Subject Cannot Be Removed")
                    .setMessage("It seems like the subject has an implemented curriculum, therefore removing is prohibited.")
                    .showAndWait();
        }
    }
    
    
    private boolean removeSubject(SubjectMapping subject) {
        subject.setActive(0);
        if(Database.connect().subject().update(subject)) {
            Notifications.create()
                    .title("Removed Successfully")
                    .text("Subject successfully removed from the list.")
                    .showInformation();
            return true;
        } else {
            Mono.fx().alert()
                    .createError()
                    .setHeader("Remove Failed")
                    .setMessage("Subject cannot be removed at this moment.")
                    .show();
            return false;
        }
    }
    
    /**
     * ADD NEW SUBJECT
     */
    
    private void addNewSubjectEvents() {
        addClickEvent(btn_add, () -> {
            this.addNewSubject();
        });
        Mono.fx().key(KeyCode.ENTER).release(vbox_main, ()->{
            this.addNewSubject();
        });
        cmbb_type.valueProperty().addListener((e) -> {
            String selected = cmbb_type.getSelectionModel().getSelectedItem();
            cmb_subtype.setDisable(!SubjectClassification.isMajor(selected));
            if(cmb_subtype.isDisable())
                cmb_subtype.getSelectionModel().select("NONE");
        });
        this.addClickEvent(btn_back, ()->{
            anchor_new_subject.setVisible(false);
            btn_new_subject.setDisable(false);
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
                if(newSubject != null) {
                   createRow(newSubject);
                   newSubject = null;
               }
                anchor_new_subject.setVisible(false);
            }
        } catch(NullPointerException a) {
            System.out.println("isSubjectExisting : " + res);
        }
    }
    
    private SubjectMapping newSubject;
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
                    .show();
            txt_descriptiveTitle.setText("");
            txt_labUnits.setText("");
            text_subjectCode.setText("");
            txt_lecUnits.setText("");
            cmb_subtype.getSelectionModel().selectFirst();
            cmbb_type.getSelectionModel().selectFirst();
            btn_new_subject.setDisable(false);
        } else {
            Mono.fx().alert()
                    .createError()
                    .setHeader("Adding Failed")
                    .setMessage("Database connection might have a problem.")
                    .show();
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

    /**
     * SUBJECT INFORMATION
     */

    private final String SECTION_BASE_COLOR = "#414852";
    private void onOpenSubjectInfoWindow(SubjectMapping subject) {
        SubjectInformationController controller = new SubjectInformationController(subject);
        
        
        LayoutDataFX layoutFx = new LayoutDataFX(anchor_main, this);
        controller.setHomeFx(layoutFx);
            
        
        Pane pane = Mono.fx().create()
                .setPackageName("update2.org.cict.layout.subjects")
                .setFxmlDocument("subject-info")
                .makeFX()
                .setController(controller)
                .pullOutLayout();

        super.setSceneColor(SECTION_BASE_COLOR); // call once on entire scene lifecycle

        Animate.fade(this.anchor_main, SectionConstants.FADE_SPEED, () -> {
            super.replaceRoot(pane);
        }, pane);
    }
}

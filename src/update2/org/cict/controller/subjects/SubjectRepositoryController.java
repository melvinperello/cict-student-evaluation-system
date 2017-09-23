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
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.text.WordUtils;
import org.controlsfx.control.Notifications;

/**
 *
 * @author Jhon Melvin
 */
public class SubjectRepositoryController extends SceneFX implements ControllerFX {

    @FXML
    private HBox hbox_none;
    
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

    private ArrayList<SubjectMapping> lst_subject;
    private SimpleTable subjectTable = new SimpleTable();
    
    @Override
    public void onInitialization() {
        bindScene(anchor_main);
        
        FetchSubjects fetch = new FetchSubjects();
        fetch.setOnStart(onStart -> {
            this.hbox_searching.setVisible(true);
        });
        fetch.setOnSuccess(onSuccess -> {
            lst_subject = fetch.getSubjectResult();
            createTable();
            this.hbox_searching.setVisible(false);
        });
        fetch.setOnCancel(onCancel -> {
            this.hbox_none.setVisible(true);
            this.hbox_searching.setVisible(false);
        });
        fetch.setRestTime(1000);
        fetch.transact();
    }

    @Override
    public void onEventHandling() {
        
        addClickEvent(btn_new_subject, () -> {
            this.hbox_none.setVisible(false);
            this.hbox_searching.setVisible(false);
            AddNewSubjectController controller = new AddNewSubjectController();
            Mono.fx().create()
                    .setPackageName("update2.org.cict.layout.subjects")
                    .setFxmlDocument("add-new-subject")
                    .makeFX()
                    .setController(controller)
                    .makeScene()
                    .makeStageApplication()
                    .stageResizeable(false)
                    .stageShowAndWait();
            SubjectMapping newSubject = controller.getNewSubject();
            if(newSubject != null)
                createRow(newSubject);
        });
        
        addClickEvent(btn_home, () -> {
            finish();
        });
    }
    
    private void createTable() {
        for(SubjectMapping subject: lst_subject) {
            createRow(subject);
        }
        
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(subjectTable);
        simpleTableView.setFixedWidth(true);
        
        simpleTableView.setParentOnScene(vbox_subjects);
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
        
        Button btn_more_info = searchAccessibilityText(programRow, "btn_more_info");
        btn_more_info.addEventHandler(MouseEvent.MOUSE_CLICKED, (e)->{
            this.showSubjectInfo(subject, programRow);
        });
        
        Button btn_remove = searchAccessibilityText(programRow, "btn_remove");
        addClickEvent(btn_remove, () -> {
            canBeRemove = true;
            FetchSubjectInformation fetch = new FetchSubjectInformation();
            fetch.subjectID = subject.getId();

            fetch.setOnSuccess(onSuccess -> {
                curriculums = fetch.getCurriculums();
                for(CurriculumMapping curriculum: curriculums) {
                    if(curriculum.getImplemented() == 1) 
                        canBeRemove = false;
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
    
    private void showSubjectInfo(SubjectMapping subject, HBox programRow) {
        SubjectInformationController controller = new SubjectInformationController(subject);
        Mono.fx().create()
                .setPackageName("update2.org.cict.layout.subjects")
                .setFxmlDocument("subject-info")
                .makeFX()
                .setController(controller)
                .makeScene()
                .makeStageApplication()
                .stageResizeable(false)
                .stageShowAndWait();
        if(controller.isEdited) {
            if(controller.editedSubject != null) {
                SubjectMapping updatedSubject = controller.editedSubject;
                Label lbl_code = searchAccessibilityText(programRow, "subject_code");
                Label lbl_descriptive_title = searchAccessibilityText(programRow, "descriptive_title");
                Label lbl_lec = searchAccessibilityText(programRow, "lec");
                Label lbl_lab = searchAccessibilityText(programRow, "lab");
                Label lbl_subtype = searchAccessibilityText(programRow, "subtype");
                Label lbl_type = searchAccessibilityText(programRow, "type");
                
                lbl_code.setText(updatedSubject.getCode());
                lbl_descriptive_title.setText(updatedSubject.getDescriptive_title());
                lbl_lab.setText(updatedSubject.getLab_units() + "");
                lbl_lec.setText(updatedSubject.getLec_units() + "");
                lbl_subtype.setText(updatedSubject.getSubtype());
                lbl_type.setText(updatedSubject.getType());
            }
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
}

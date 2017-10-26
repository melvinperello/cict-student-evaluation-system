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
import com.jhmvin.orm.SQL;
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
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
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
    private TextField txt_search;

    @FXML
    private JFXButton btnFind;

    @FXML
    private JFXButton btn_home;

    @FXML
    private VBox vbox_subjects;

    @FXML
    private HBox hbox_searching;

    @FXML
    private HBox hbox_none;


    private ArrayList<SubjectMapping> lst_subject;
//    private SimpleTable subjectTable = new SimpleTable();
    
    private AnchorPane application_root;
    
    @Override
    public void onInitialization() {
        application_root = anchor_main;
        bindScene(application_root);
        
        FetchSubjects fetch = new FetchSubjects();
        fetch.setOnStart(onStart -> {
            this.vbox_subjects.getChildren().clear();
            this.hbox_searching.setVisible(true);
            this.txt_search.setDisable(true);
            this.btnFind.setDisable(true);
        });
        fetch.setOnSuccess(onSuccess -> {
            lst_subject = fetch.getSubjectResult();
            this.renderTable(lst_subject);
        });
        fetch.setOnCancel(onCancel -> {
            this.vbox_subjects.getChildren().clear();
            this.hbox_none.setVisible(true);
            this.hbox_searching.setVisible(false);
        });
        fetch.transact();
    }
    
    private void renderTable(ArrayList<SubjectMapping> lst_subject) {
        SimpleTask rendering_task = new SimpleTask("rendering_task");
        SimpleTable subjectTable = new SimpleTable();
        rendering_task.setTask(()->{
            createTable(lst_subject, subjectTable);
        });
        rendering_task.setOnStart((a)->{
            this.vbox_subjects.getChildren().clear();
            this.hbox_searching.setVisible(true);
            this.txt_search.setDisable(true);
            this.btnFind.setDisable(true);
        });
        rendering_task.setOnSuccess((a)->{
            this.hbox_searching.setVisible(false);
            this.txt_search.setDisable(false);
            this.btnFind.setDisable(false);
        });
        rendering_task.start();
    }
    
    private LayoutDataFX homeFX;
    public void setHomeFX(LayoutDataFX homeFX) {
        this.homeFX = homeFX;
    }
    
    @Override
    public void onEventHandling() {
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
        
        Mono.fx().key(KeyCode.ENTER).release(application_root, ()->{
            this.onSearch();
        });
        
        super.addClickEvent(btnFind, ()->{
            this.onSearch();
        });
    }
    
    private void onSearch() {
        String searchKey = MonoString.removeExtraSpace(txt_search.getText());
        if(searchKey.isEmpty()) {
            this.renderTable(lst_subject);
            return;
        }
        ArrayList<SubjectMapping> results = Mono.orm().newSearch(Database.connect().subject())
                .put(SQL.or(Restrictions.ilike(DB.subject().code, (searchKey), MatchMode.ANYWHERE)))
                .active(Order.asc(DB.subject().code)).all();
        if(results==null) {
            Animate.fade(hbox_searching, 150, ()->{
                hbox_searching.setVisible(false);
                vbox_subjects.getChildren().clear();
                hbox_none.setVisible(true);
            }, hbox_none);
            return;
        }
        this.renderTable(results);
    }
    
    private void createTable(ArrayList<SubjectMapping> lst_subject, SimpleTable subjectTable) {
        subjectTable.getChildren().clear();
        for(SubjectMapping subject: lst_subject) {
            createRow(subject, subjectTable);
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
    private void createRow(SubjectMapping subject, SimpleTable subjectTable) {
        
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
//        Mono.fx().thread().wrap(()->{
            subjectTable.addRow(row);
//        });
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

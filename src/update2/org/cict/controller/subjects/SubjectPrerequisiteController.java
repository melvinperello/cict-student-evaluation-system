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

import app.lazy.models.CurriculumHistorySummaryMapping;
import app.lazy.models.CurriculumRequisiteExtMapping;
import app.lazy.models.CurriculumRequisiteLineMapping;
import app.lazy.models.CurriculumSubjectMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.SubjectMapping;
import com.jhmvin.Mono;
import com.jhmvin.fx.controls.SimpleImage;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import java.util.ArrayList;
import java.util.Date;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.cict.SubjectClassification;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.controlsfx.control.Notifications;
import org.hibernate.criterion.Order;

/**
 *
 * @author Joemar
 */
public class SubjectPrerequisiteController extends SceneFX implements ControllerFX {

    @FXML
    private VBox vbox_main;

    @FXML
    private Label lbl_lec;

    @FXML
    private Label lbl_lab;

    @FXML
    private Label lbl_type;

    @FXML
    private Label lbl_subtype;

    @FXML
    private Label lbl_subject_id;

    @FXML
    private Label lbl_subject_code;

    @FXML
    private Label lbl_description;

    @FXML
    private Button btn_new_prereq;

    @FXML
    private Button btn_new_coreq;
    
    @FXML
    private VBox vbox_subjects;

    @FXML
    private VBox vbox_no_result;
    
    @FXML
    private Label lbl_title;
    
    private SubjectMapping SUBJECT;
    private Integer CURRICULUM_id;
    private ArrayList<SubjectMapping> prereqs;
    private ArrayList<SubjectMapping> coreqs;
    private Integer REMOVED_BY = CollegeFaculty.instance().getFACULTY_ID();
    private Date REMOVED_DATE = Mono.orm().getServerTime().getDateWithFormat();
    private boolean isImplemented = false;
    
    public SubjectPrerequisiteController(Integer curriculumID, SubjectMapping subject, boolean isImplemented) {
        SUBJECT = subject;
        CURRICULUM_id = curriculumID;
        this.isImplemented = isImplemented;
    }
    
    @Override
    public void onInitialization() {
        bindScene(vbox_main);
        this.setSubjectDetails();
    }

    @Override
    public void onEventHandling() {
        addClickEvent(btn_new_prereq, ()-> {
            if(!isImplemented)
                showSubjectSearch("pre-req");
            else {
                Mono.fx().alert().createWarning()
                        .setHeader("Subject Cannot Be Modified")
                        .setMessage("It seems like this subject belongs to an implemented curriculum, therefore modifying is prohibited.")
                        .showAndWait();
            }
        });
        
        this.addClickEvent(btn_new_coreq, ()->{
            if(!isImplemented)
                showSubjectSearch("co-req");
            else {
                Mono.fx().alert().createWarning()
                        .setHeader("Subject Cannot Be Modified")
                        .setMessage("It seems like this subject belongs to an implemented curriculum, therefore modifying is prohibited.")
                        .showAndWait();
            }
        });
    }
    
    private boolean isUpdated = false;
    public boolean isUpdated() {
        return isUpdated;
    }
     
    private void showSubjectSearch(String mode) {
        if(CURRICULUM_id == -1) {
            System.out.println("NO CURRICULUM ID RECEIVED");
        } else {
            SearchSubjectController controller = new SearchSubjectController(CURRICULUM_id);
            controller.setModeSetting("SUBJECT_" + mode);
            controller.setSubjectIdGet(SUBJECT.getId());
            String title = "";
            if(mode.equals("pre-req")) {
                title = "New Pre-requisite";
                controller.title = "Pre-requisite for " + SUBJECT.getCode();
            } else {
                title = "New Co-requisite";
                controller.title = "Co-requisite for " + SUBJECT.getCode();
            }
            Mono.fx().create()
                    .setPackageName("update2.org.cict.layout.subjects")
                    .setFxmlDocument("search-subject")
                    .makeFX()
                    .setController(controller)
                    .makeScene()
                    .makeStageWithOwner(Mono.fx().getParentStage(btn_new_prereq))
                    .stageResizeable(false)
                    .stageTitle(title)
                    .stageShowAndWait();
            if(controller.isAdded()) {
                this.showRequisites();
                isUpdated = true;
            }
        }
    }
     
    private void setSubjectDetails() {
        lbl_subject_id.setText(SUBJECT.getId().toString());
        lbl_subject_code.setText(SUBJECT.getCode());
        lbl_description.setText(SUBJECT.getDescriptive_title());
        lbl_lec.setText(SUBJECT.getLec_units().toString());
        lbl_lab.setText(SUBJECT.getLab_units().toString());
        
        lbl_subtype.setText(SUBJECT.getSubtype().toUpperCase());
        lbl_type.setText(SUBJECT.getType().toUpperCase());
        showRequisites();
    }
    
    private void showRequisites() {
        FetchSubjectInformation fetch = new FetchSubjectInformation();
        fetch.subjectID = this.SUBJECT.getId();
        fetch.setCurriculumID(this.CURRICULUM_id);
        
        fetch.setOnSuccess(onSuccess -> {
            prereqs = fetch.getSubjectPrerequisite();
            coreqs = fetch.getSubjectCorequisite();
            if(prereqs.isEmpty() && coreqs.isEmpty()) {
                vbox_no_result.setVisible(true);
            } else {
                subjectTable.getChildren().clear();
                createSubjectTable();
            }
        });
        fetch.transact();
    }
     
    private SimpleTable subjectTable = new SimpleTable();
    private void createSubjectTable() {
        for (SubjectMapping subject : prereqs) {
            createRow(subject, "P");
        }
        for (SubjectMapping subject : coreqs) {
            createRow(subject, "C");
        }
        
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(subjectTable);
        simpleTableView.setFixedWidth(true);
        simpleTableView.setParentOnScene(vbox_subjects);
        this.vbox_subjects.setVisible(true);
    }
    
    private void createRow(SubjectMapping subject, String mode) {
        SimpleTableRow row = new SimpleTableRow();
        row.setRowHeight(70.0);

        HBox prereqRow = (HBox) Mono.fx().create()
                .setPackageName("update2.org.cict.layout.subjects")
                .setFxmlDocument("subject-prereq-row")
                .makeFX()
                .pullOutLayout();
        ImageView img = searchAccessibilityText(prereqRow, "img");   
        Label lbl_code = searchAccessibilityText(prereqRow, "subject_code");
        Label lbl_descriptive_title = searchAccessibilityText(prereqRow, "descriptive_title");
        Label lbl_units = searchAccessibilityText(prereqRow, "units");
        Label lbl_typee = searchAccessibilityText(prereqRow, "type");
        Label lbl_subtypee = searchAccessibilityText(prereqRow, "subtype");
        Button btn_remove = searchAccessibilityText(prereqRow, "btn_remove");
        
        if(mode.equalsIgnoreCase("P")) {
            img.setImage(SimpleImage.make("update2.org.cict.layout.subjects.img", "p.png"));       
        } else {
            img.setImage(SimpleImage.make("update2.org.cict.layout.subjects.img", "c.png"));       
        }
        lbl_code.setText(subject.getCode());
        lbl_descriptive_title.setText(subject.getDescriptive_title());
        Double totalUnits = (subject.getLab_units() + subject.getLec_units());  
        lbl_units.setText(totalUnits + "");
        lbl_typee.setText(subject.getType());
        lbl_subtypee.setText(subject.getSubtype());
        addClickEvent(btn_remove, ()-> {
            if(!isImplemented) {
                if(mode.equalsIgnoreCase("P"))
                    removePrerequisite(subject, row);
                else if(mode.equalsIgnoreCase("C"))
                    removeCorequisite(subject, row);
            } else {
                Mono.fx().alert().createWarning()
                        .setHeader("Subject Cannot Be Modified")
                        .setMessage("It seems like this subject belongs to an implemented curriculum, therefore modifying is prohibited.")
                        .showAndWait();
            }
        });
        
        SimpleTableCell cellParent = new SimpleTableCell();
        cellParent.setResizePriority(Priority.ALWAYS);
        cellParent.setContent(prereqRow);

        row.addCell(cellParent);

        subjectTable.addRow(row);
    }
    
    private void removePrerequisite(SubjectMapping subject, SimpleTableRow row) {
        int res = Mono.fx().alert()
                .createConfirmation()
                .setHeader("Remove Pre-requiste Subject")
                .setMessage("Are you sure you want to remove the subject as its pre-requisite?")
                .confirmYesNo();
        if(res == 1) {
            CurriculumRequisiteLineMapping crlMap = Mono.orm().newSearch(Database.connect().curriculum_requisite_line())
                    .eq(DB.curriculum_requisite_line().SUBJECT_id_req, subject.getId())
                    .eq(DB.curriculum_requisite_line().SUBJECT_id_get, this.SUBJECT.getId())
                    .active(Order.desc(DB.curriculum_requisite_line().id))
                    .first();
            if(crlMap != null) {
                crlMap.setActive(0);
                crlMap.setRemoved_date(REMOVED_DATE);
                crlMap.setRemoved_by(REMOVED_BY);
                if(Database.connect().curriculum_requisite_line().update(crlMap)) {
                    insertSubjectHistory(this.SUBJECT, subject, "PRE-REQUISITE");
                    Notifications.create()
                            .title("Removed Successfully")
                            .text(subject.getCode() + ": " + subject.getDescriptive_title())
                            .showInformation();
                    subjectTable.getChildren().remove(row);
                } else {
                    Notifications.create()
                            .title("Remove Failed")
                            .text(subject.getCode() + ": " + subject.getDescriptive_title())
                            .showError();
                }
            } else {
                System.out.println("NO CURRICULUM REQUISITE LINE FOUND TO BE REMOVED");
            }
        }
    }
    
     
    private void removeCorequisite(SubjectMapping subject, SimpleTableRow row) {
        int res = Mono.fx().alert()
                .createConfirmation()
                .setHeader("Remove Co-requiste Subject")
                .setMessage("Are you sure you want to remove the subject as its co-requisite?")
                .confirmYesNo();
        if(res == 1) {
            CurriculumRequisiteExtMapping creMap = Mono.orm().newSearch(Database.connect().curriculum_requisite_ext())
                    .eq(DB.curriculum_requisite_ext().SUBJECT_id_req, subject.getId())
                    .eq(DB.curriculum_requisite_ext().SUBJECT_id_get, this.SUBJECT.getId())
                    .eq(DB.curriculum_requisite_ext().type, SubjectClassification.REQUITE_TYPE_CO)
                    .active(Order.desc(DB.curriculum_requisite_ext().id))
                    .first();
            if(creMap != null) {
                creMap.setActive(0);
                creMap.setRemoved_date(REMOVED_DATE);
                creMap.setRemoved_by(REMOVED_BY);
                if(Database.connect().curriculum_requisite_ext().update(creMap)) {
                    insertSubjectHistory(this.SUBJECT, subject, "CO-REQUISITE");
                    Notifications.create()
                            .title("Removed Successfully")
                            .text(subject.getCode() + ": " + subject.getDescriptive_title())
                            .showInformation();
                    subjectTable.getChildren().remove(row);
                } else {
                    Notifications.create()
                            .title("Remove Failed")
                            .text(subject.getCode() + ": " + subject.getDescriptive_title())
                            .showError();
                }
            } else {
                System.out.println("NO CURRICULUM REQUISITE LINE FOUND TO BE REMOVED");
            }
        }
    }
     
    private boolean insertSubjectHistory(SubjectMapping subject, SubjectMapping subjectReq, String mode) {
        CurriculumSubjectMapping csubjectMap = Mono.orm().newSearch(Database.connect().curriculum_subject())
                .eq(DB.curriculum_subject().CURRICULUM_id, this.CURRICULUM_id)
                .eq(DB.curriculum_subject().SUBJECT_id, subject.getId())
                .active()
                .first();
        String yearSemester = getYearLevel(csubjectMap.getYear()) + "-" + getSemester(csubjectMap.getSemester());
        String description = "REMOVED " + mode + " [ID:" + subjectReq.getId()+ "] " + subjectReq.getCode()
                    +" FOR [ID:" + subject.getId()+ "] " + subject.getCode() + " FROM " + yearSemester.toUpperCase();
        
        CurriculumHistorySummaryMapping chsMap = new CurriculumHistorySummaryMapping();
        chsMap.setActive(1);
        chsMap.setCreated_by(REMOVED_BY);
        chsMap.setCreated_date(REMOVED_DATE);
        chsMap.setCurriculum_id(this.CURRICULUM_id);
        chsMap.setDescription(description);
        if(Database.connect().curriculum_history_summary().insert(chsMap) == -1) {
            System.out.println("HISTORY NOT SAVED");
            return false;
        }
        return true;
    }
     
    private String getYearLevel(Integer year) {
        String yearLevel = "";
        switch(year){
                case 1:
                    yearLevel = "First Year";
                    break;
                case 2:
                    yearLevel = "Second Year";
                    break;
                case 3:
                    yearLevel = "Third Year";
                    break;
                case 4:
                    yearLevel = "Fourth Year";
                    break;
        }
        return yearLevel;           
    }

    private String getSemester(Integer sem) {
        String semester = "";
        switch(sem) {
            case 1:
                semester = "First Semester";
                break;
            case 2:
                semester = "Second Semester";
                break;
        }
        return semester;
    }
}

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
package org.cict.evaluation;

import app.lazy.models.AcademicTermMapping;
import app.lazy.models.CurriculumRequisiteLineMapping;
import app.lazy.models.CurriculumSubjectMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.GradeMapping;
import app.lazy.models.StudentMapping;
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
import java.util.Objects;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.text.WordUtils;
import org.cict.evaluation.assessment.AssessmentResults;
import org.cict.evaluation.assessment.CurricularLevelAssesor;
import org.cict.evaluation.evaluator.Evaluator;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.hibernate.criterion.Order;

/**
 *
 * @author Joemar
 */
public class AssistantController extends SceneFX implements ControllerFX {

    @FXML
    private AnchorPane anchor_main;

    @FXML
    private Label btn_close;
    
    @FXML
    private AnchorPane anchor_start;

    @FXML
    private Label lbl_message;

    @FXML
    private Label label_student_id;

    @FXML
    private Label lbl_name;

    @FXML
    private Label lbl_year_level;

    @FXML
    private JFXButton btn_change_yr;

    @FXML
    private JFXButton btn_next;

    @FXML
    private AnchorPane anchor_change_yr;

    @FXML
    private ComboBox<String> cmb_year_level;

    @FXML
    private JFXButton btn_save_changes;

    @FXML
    private AnchorPane anchor_subjects;

    @FXML
    private Label lbl_message2;

    @FXML
    private VBox vbox_list;

    @FXML
    private Label lbl_title;

    @FXML
    private JFXButton btn_previous1;

    @FXML
    private JFXButton btn_next1;
    
    @FXML
    private JFXButton btn_closee;

    @FXML
    private JFXButton btn_previous;
    
    private StudentMapping STUDENT;
    
    public AssistantController(StudentMapping student) {
        this.STUDENT = student;
    }
    
    @Override
    public void onInitialization() {
        ArrayList<SubjectMapping> subjects = getSubjects(false);
        if(subjects == null || subjects.isEmpty()) {
            lbl_message2.setText("Click Next.");
            lbl_title.setText("You're doin it right!");
            vbox_list.setVisible(false);
        } else {
            vbox_list.setVisible(true);
            createTable(subjects, false);
        }
        String fullName = WordUtils.capitalizeFully(STUDENT.getFirst_name() + " " + STUDENT.getLast_name());
        label_student_id.setText(STUDENT.getId());
        lbl_name.setText(fullName);
        
        cmb_year_level.getItems().add("First Year");
        cmb_year_level.getItems().add("Second Year");
        cmb_year_level.getItems().add("Third Year");
        cmb_year_level.getItems().add("Fourth Year");
        cmb_year_level.getSelectionModel().select((STUDENT.getYear_level()-1));
    
        lbl_year_level.setText(cmb_year_level.getSelectionModel().getSelectedItem());
        
    }

    private Integer index;
    private Boolean isSaved = false;
    @Override
    public void onEventHandling() {
        this.addClickEvent(btn_change_yr, ()-> {
            anchor_change_yr.setVisible(true);
        });
        
        this.addClickEvent(btn_save_changes, ()->{
            index = cmb_year_level.getSelectionModel().getSelectedIndex();
            Integer studentYrLvl = STUDENT.getYear_level();
            String message = "";
            boolean invalid = false;
            index+=1;
            if(index < studentYrLvl) {
                invalid = true;
                message = "We prohibit changing the year level of the student backward. Please ask your local registrar for assistance.";
            } else if(index.equals(studentYrLvl+1)) {
                // valid
            }else if(index > studentYrLvl) {
                invalid = true;
                message = "Changing the year level twice or more is not allowed, just one step at a time. If that so, please ask your local registrar for assistance.";
            }
            if(invalid) {
                Mono.fx().alert()
                        .createWarning()
                        .setHeader("Ops, Invalid Move")
                        .setMessage(message)
                        .show();
                return;
            }
            String yearLevel = cmb_year_level.getSelectionModel().getSelectedItem();
            anchor_change_yr.setVisible(false);
            
            if(Objects.equals(index, STUDENT.getYear_level())) {
                return;
            }
            btn_change_yr.setDisable(true);
            lbl_year_level.setText(yearLevel);
            STUDENT.setYear_level(index);
            if(!Database.connect().student().update(STUDENT)) {
                System.out.println("NOT SAVED");
            } else 
                isSaved = true;
            lbl_message.setText("If we're done with it, lets proceed! Click next.");
        });
        
        this.addClickEvent(btn_next, ()->{
            if(isSaved) {
                ArrayList<SubjectMapping> subjects = getSubjects(false);
                if(subjects == null || subjects.isEmpty()) {
                    lbl_message2.setText("Click Next.");
                    lbl_title.setText("You're doin it right!");
                    vbox_list.setVisible(false);
                } else {
                    lbl_message2.setText("Based on my findings, the student can't take these subjects in this semester.");
                    lbl_title.setText("Subjects Cannot Be Taken In This Semester");
                    vbox_list.setVisible(true);
                    createTable(subjects, false);
                }
            }
            anchor_change_yr.setVisible(false);
            btn_previous.setVisible(true);
            anchor_start.setVisible(false);
            anchor_subjects.setVisible(true);
            
        });
        
        this.addClickEvent(btn_previous, ()->{
            btn_previous.setVisible(false);
            anchor_start.setVisible(true);
            anchor_subjects.setVisible(false);
        });
        
        this.addClickEvent(btn_close, ()->{
            if(index == null)
                index = STUDENT.getYear_level();
            Mono.fx().getParentStage(btn_close).close();
        });
        
        this.addClickEvent(btn_previous1, ()->{
            btn_closee.setVisible(false);
            btn_previous.setVisible(true);
            //change table values
            ArrayList<SubjectMapping> subjects = getSubjects(false);
            if(subjects == null || subjects.isEmpty()) {
                lbl_message2.setText("Click Next.");
                lbl_title.setText("You're doin it right!");
                vbox_list.setVisible(false);
            } else {vbox_list.setVisible(true);
                lbl_message2.setText("Based on my findings, the student can't take these subjects in this semester.");
                lbl_title.setText("Subjects Cannot Be Taken In This Semester");
                createTable(subjects, false);
            }
            btn_next1.setDisable(false);
        });
        
        this.addClickEvent(btn_next1, ()->{
            btn_closee.setVisible(true);
            ArrayList<SubjectMapping> subjects = getSubjects(true);
            btn_previous.setVisible(false);
            //change table values
            if(subjects == null || subjects.isEmpty()) {
                lbl_message2.setText("");
                lbl_title.setText("No reports for now.");
                vbox_list.setVisible(false);
            } else {
                vbox_list.setVisible(true);
                lbl_message2.setText("Just to give an advance report, these are the subjects that the student can take in this semester.");
                lbl_title.setText("Subjects Can Be Taken In This Semester");
                createTable(subjects, true);
            }
            btn_next1.setDisable(true);
        });
        
        this.addClickEvent(btn_closee, ()->{
            if(index == null)
                index = STUDENT.getYear_level();
            Mono.fx().getParentStage(btn_close).close();
        });
    }
    
    private void createTable(ArrayList<SubjectMapping> subjects, boolean canTake) {
        recordTable.getChildren().clear();
        for (SubjectMapping subject: subjects) {
            createRow(subject, canTake);
        }
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(recordTable);
        simpleTableView.setFixedWidth(true);
        simpleTableView.setParentOnScene(vbox_list);
        this.vbox_list.setVisible(true);
    }
    
    private SimpleTable recordTable = new SimpleTable();
    private String colorCannotTake = "#D64651";
    private void createRow(SubjectMapping subject, boolean canTake) {
        SimpleTableRow row = new SimpleTableRow();
        row.setRowHeight(40.0);

        HBox subjectRow = (HBox) Mono.fx().create()
                .setPackageName("org.cict.evaluation")
                .setFxmlDocument("assistant-row")
                .makeFX()
                .pullOutLayout();
        if(!canTake)
            subjectRow.setStyle(subjectRow.getStyle() + "-fx-background-color: " + colorCannotTake + ";");
        
        Label lbl_code = searchAccessibilityText(subjectRow, "code");
        Label lbl_descr = searchAccessibilityText(subjectRow, "description");
        
        lbl_code.setText(subject.getCode());
        lbl_descr.setText(subject.getDescriptive_title());
        
        SimpleTableCell cellParent = new SimpleTableCell();
        cellParent.setResizePriority(Priority.ALWAYS);
        cellParent.setContent(subjectRow);
        row.addCell(cellParent);
        recordTable.addRow(row);
    }
    
    private ArrayList<SubjectMapping> allSubjects;
    private ArrayList<SubjectMapping> getSubjects(boolean canTake) {
        AcademicTermMapping atMap = Evaluator.instance().getCurrentAcademicTerm();
        Integer sem = atMap.getSemester_regular();
        if(sem != null) {
            if(!sem.equals(0)) {
                if(index == null)
                    index = STUDENT.getYear_level();
                ArrayList<CurriculumSubjectMapping> csMaps = Mono.orm().newSearch(Database.connect().curriculum_subject())
                        .eq(DB.curriculum_subject().CURRICULUM_id, STUDENT.getCURRICULUM_id())
                        .eq(DB.curriculum_subject().semester, sem)
                        .eq(DB.curriculum_subject().year, index)
                        .active()
                        .all();
                if(csMaps != null) {
                    boolean canBeTaken = true;
                    allSubjects = new ArrayList<>();
                    CurricularLevelAssesor assessor = new CurricularLevelAssesor(STUDENT);
                    assessor.assess();
                    AssessmentResults annualAsses = assessor.getAnnualAssessment(STUDENT.getYear_level());
                    for(CurriculumSubjectMapping csMap: csMaps) {
                        SubjectMapping subjectToBeAdded = Mono.orm().newSearch(Database.connect().subject())
                                .eq(DB.subject().id, csMap.getSUBJECT_id())
                                .active()
                                .first();
                         checkForPrerequisteRequired(subjectToBeAdded, sem, canTake, annualAsses);
                    }
                } else
                    System.out.println("CSMAP IS NULL");
            } else
                System.out.println("SEM IS ZERO");
        } else
            System.out.println("SEM IS NULL");
        return allSubjects;
    }
    
    
    private void checkForPrerequisteRequired(SubjectMapping subject, Integer semester, Boolean canTake, AssessmentResults annualAsses) {
        
        annualAsses.getSemestralResults(semester).forEach(sem_details -> {
            boolean canBeTaken = true;
            if(Objects.equals(subject.getId(), sem_details.getSubjectID())) {
                ArrayList<Integer> pre_ids = new ArrayList<>();
                Integer[] preqid = new Integer[0];
                if (sem_details.getSubjectRequisites() == null) {
                    // do nothing no preq
                    canBeTaken = true;
                } else {
                    sem_details.getSubjectRequisites().forEach(pre_requisite -> {
                            pre_ids.add(pre_requisite.getSUBJECT_id_req());
                    });
                    preqid = pre_ids.toArray(new Integer[pre_ids.size()]);
                
                    // if this assessment contains a grade
                    if (sem_details.getGradeDetails() != null) {
                        System.out.println("GRADES NOT NULL");
                    }

                    for (int q = 0; q < preqid.length; q++) {
                        GradeMapping grade = Mono.orm().newSearch(Database.connect().grade())
                                .eq(DB.grade().STUDENT_id, STUDENT.getCict_id())
                                .eq(DB.grade().SUBJECT_id, preqid[q])
                                .active(Order.desc(DB.grade().id))
                                .first();
                        if(grade == null) {
                            System.out.println("NO GRADE OF PRE REQ");
                            canBeTaken = false;
                        } else {
                            try {
                                switch(grade.getRemarks()) {
                                    case "PASSED":
                                    case "INCOMPLETE":
                                        break;
                                    default:
                                        canBeTaken = false;
                                        break;
                                }
                            } catch (NullPointerException a) {
                                canBeTaken = false;
                            }
                        }
                    }
                }

                if(canTake) {
                    if(canBeTaken) {
                        if(!isAlreadyTaken(subject))
                            allSubjects.add(subject);
                    }
                } else {
                    if(!canBeTaken) {
                        if(!isAlreadyTaken(subject))
                            allSubjects.add(subject);
                    }
                }
            }
        });
    }
    
    
    private boolean isAlreadyTaken(SubjectMapping subject) {
        GradeMapping grade = Mono.orm().newSearch(Database.connect().grade())
                .eq(DB.grade().STUDENT_id, STUDENT.getCict_id())
                .eq(DB.grade().SUBJECT_id, subject.getId())
                .active(Order.desc(DB.grade().id))
                .first();
        if(grade != null) {
            String remarks = grade.getRemarks();
            switch(remarks) {
                case "PASSED":
                case "INCOMPLETE":
                    return true;
                default:
                    return false;
            }
        } else
            return false;
    }
    
    public Integer getNewYearLevel() {
        return index;
    }
    
}

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
package update.org.cict.controller.adding.subjectviewer;

import app.lazy.models.AcademicTermMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.EvaluationMapping;
import app.lazy.models.LoadSubjectMapping;
import app.lazy.models.StudentMapping;
import app.lazy.models.SubjectMapping;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jhmvin.Mono;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import java.util.ArrayList;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.text.WordUtils;
import org.cict.PublicConstants;
import org.cict.evaluation.assessment.AssessmentResults;
import org.cict.evaluation.assessment.CurricularLevelAssesor;
import org.cict.evaluation.assessment.SubjectAssessmentDetials;
import org.cict.evaluation.evaluator.Evaluator;

/**
 *
 * @author Joemar
 */
public class AssistantController2 extends SceneFX implements ControllerFX {

    @FXML
    private AnchorPane anchor_main;

    @FXML
    private Label btn_close;

    @FXML
    private AnchorPane anchor_1;

    @FXML
    private Label lbl_message;

    @FXML
    private Label lbl_total_units;

    @FXML
    private JFXButton btn_next;

    @FXML
    private AnchorPane anchor_2;

    @FXML
    private Label lbl_message2;

    @FXML
    private VBox vbox_list;

    @FXML
    private Label lbl_title;

    @FXML
    private JFXButton btn_previous;

    @FXML
    private JFXButton btn_next1;
    
    @FXML
    private JFXCheckBox chkbx_disable_ai;


    private EvaluationMapping EVALUATION;
    private String totalUnits = "0";
    private ArrayList<Integer> eval_SUBJECT_ids;
    
    public AssistantController2(EvaluationMapping e_map) {
        this.EVALUATION = e_map;
        Double temp_total = 0.0;
        ArrayList<LoadSubjectMapping> lsMaps = Mono.orm().newSearch(Database.connect().load_subject())
                .eq(DB.load_subject().EVALUATION_id, this.EVALUATION.getId())
                .eq(DB.load_subject().STUDENT_id, this.EVALUATION.getSTUDENT_id())
                .active()
                .all();
        if(lsMaps != null) {
            eval_SUBJECT_ids = new ArrayList<>();
            for(LoadSubjectMapping lsMap: lsMaps) {
                SubjectMapping subject = Mono.orm().newSearch(Database.connect().subject())
                        .eq(DB.subject().id, (lsMap.getSUBJECT_id()))
                        .active()
                        .first();
                if(subject != null) {
                    eval_SUBJECT_ids.add(subject.getId());
                    temp_total += (subject.getLab_units() + subject.getLec_units());
                }
            }
        }
        totalUnits = String.valueOf(temp_total);
    }
    
    @Override
    public void onInitialization() {
        lbl_total_units.setText(totalUnits);
        getSubjects();
        if(allSubjects.isEmpty()) {
            vbox_list.setVisible(false);
            if(STUDENT.getYear_level()>1) {
                lbl_message.setText("Let's see if I can find some suggestion of subjects for " + WordUtils.capitalizeFully(STUDENT.getFirst_name()) + ", Click Next.");
                lbl_title.setText("Great! No unacquired subject.");
                lbl_message2.setText("It seems like " + WordUtils.capitalizeFully(STUDENT.getFirst_name()) + " did study well.");
            } else {
                lbl_message.setText("Since " + WordUtils.capitalizeFully(STUDENT.getFirst_name()) + " is a freshman, no need for my subject suggestion yet.");
                btn_next.setText("Close");
                btn_next.setStyle(btn_next.getStyle() + " -fx-background-color: #F44336;");
            }
        } else 
            createTable(allSubjects);
    }

    @Override
    public void onEventHandling() {
        this.addClickEvent(btn_close, ()->{
            Mono.fx().getParentStage(btn_close).close();
        });
    
        this.addClickEvent(btn_next, ()->{
            if(btn_next.getText().equalsIgnoreCase("close")) {
                Mono.fx().getParentStage(btn_close).close();
                return;
            }
            this.anchor_1.setVisible(false);
            this.anchor_2.setVisible(true);
        });
    
        this.addClickEvent(btn_previous, ()->{
            this.anchor_1.setVisible(true);
            this.anchor_2.setVisible(false);
        });
        
        this.addClickEvent(btn_next1, ()->{
            Mono.fx().getParentStage(btn_close).close();
        });
        
        Mono.fx().key(KeyCode.ENTER).release(anchor_main, ()->{
            Mono.fx().getParentStage(btn_close).close();
        });
        
        //----------------------------------------
        // disable sitti
        chkbx_disable_ai.selectedProperty().addListener((a)->{
            PublicConstants.DISABLE_ASSISTANCE = chkbx_disable_ai.isSelected();
        });
    }
    
    private SimpleTable recordTable = new SimpleTable();
    private void createTable(ArrayList<SubjectMapping> subjects) {
        recordTable.getChildren().clear();
        for (SubjectMapping subject: subjects) {
            createRow(subject);
        }
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(recordTable);
        simpleTableView.setFixedWidth(true);
        simpleTableView.setParentOnScene(vbox_list);
        this.vbox_list.setVisible(true);
    }
    
    private void createRow(SubjectMapping subject) {
        SimpleTableRow row = new SimpleTableRow();
        row.setRowHeight(40.0);

        HBox subjectRow = (HBox) Mono.fx().create()
                .setPackageName("org.cict.evaluation")
                .setFxmlDocument("assistant-row")
                .makeFX()
                .pullOutLayout();
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
    private StudentMapping STUDENT;
    private void getSubjects() {
        allSubjects = new ArrayList<>();
        STUDENT = Mono.orm().newSearch(Database.connect().student())
                .eq(DB.student().cict_id, EVALUATION.getSTUDENT_id())
                .active()
                .first();
        if(STUDENT != null) {
            CurricularLevelAssesor assessor = new CurricularLevelAssesor(STUDENT);
            assessor.assess();
            ArrayList<SubjectAssessmentDetials> saDetails = new ArrayList<>();
            for (int yrCtr = 1; yrCtr <= STUDENT.getYear_level(); yrCtr++) {
                AssessmentResults annualAsses = assessor.getAnnualAssessment(yrCtr);
                ArrayList<SubjectAssessmentDetials> temp_array = annualAsses.getUnacquiredSubjects();
                if(temp_array != null) {
                    for(SubjectAssessmentDetials temp_value: temp_array)
                        saDetails.add(temp_value);
                } else
                    System.out.println("It's NULL");
            }
            if(!saDetails.isEmpty()) {
                AcademicTermMapping atMap = Evaluator.instance().getCurrentAcademicTerm();
                Integer sem = atMap.getSemester_regular();
                for(SubjectAssessmentDetials saDetail: saDetails) {
                    if(Objects.equals(saDetail.getSemester(), sem)) {
                        if(!eval_SUBJECT_ids.contains(saDetail.getSubjectDetails().getId()))
                            allSubjects.add(saDetail.getSubjectDetails());
                    }
                }
            }
        }
    }
}

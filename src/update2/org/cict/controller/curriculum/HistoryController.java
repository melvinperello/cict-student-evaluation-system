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
package update2.org.cict.controller.curriculum;

import app.lazy.models.CurriculumHistorySummaryMapping;
import app.lazy.models.CurriculumMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.FacultyMapping;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import com.jhmvin.transitions.Animate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.text.WordUtils;
import org.hibernate.criterion.Order;
import update3.org.cict.SectionConstants;

/**
 *
 * @author Joemar
 */
public class HistoryController extends SceneFX implements ControllerFX {

    @FXML
    private VBox application_root;
    
    @FXML
    private Label lbl_name;

    @FXML
    private Label lbl_description;

    @FXML
    private VBox vbox_list;

    @FXML
    private Button btn_view;

    @FXML
    private Button btn_back;
    
    private CurriculumMapping CURRICULUM;
    public HistoryController(CurriculumMapping CURRICULUM) {
        this.CURRICULUM = CURRICULUM;
    }
    
    @Override
    public void onInitialization() {
        
        super.bindScene(application_root);
        
        lbl_name.setText(CURRICULUM.getName());
        String major = "";
        if(CURRICULUM.getMajor() != null) {
            if(!CURRICULUM.getMajor().equalsIgnoreCase("NONE"))
                major = " MAJOR IN " + CURRICULUM.getMajor();
        }
        lbl_description.setText(CURRICULUM.getDescription() + major);
        createTable();
        ArrayList<CurriculumHistorySummaryMapping> chsMaps = Mono.orm().newSearch(Database.connect().curriculum_history_summary())
                .eq(DB.curriculum_history_summary().curriculum_id, this.CURRICULUM.getId())
                .active(Order.desc(DB.curriculum_history_summary().history_id))
                .all();
        if(chsMaps == null)
            return;
        for(CurriculumHistorySummaryMapping chsMap: chsMaps) {
            createRow(chsMap);
        }
    }

    private final String SECTION_BASE_COLOR = "#E85764";
    @Override
    public void onEventHandling() {
        this.addClickEvent(btn_view, ()->{
            showActualData();
        });
        
        this.addClickEvent(btn_back ,()->{
            CurriculumInformationController controller = new CurriculumInformationController(CURRICULUM);
            Pane pane = Mono.fx().create()
                    .setPackageName("update2.org.cict.layout.curriculum")
                    .setFxmlDocument("curriculum-info")
                    .makeFX()
                    .setController(controller)
                    .pullOutLayout();

            super.setSceneColor(SECTION_BASE_COLOR); // call once on entire scene lifecycle

            Animate.fade(this.application_root, SectionConstants.FADE_SPEED, () -> {
                super.replaceRoot(pane);
            }, pane);
        });
    }
    
     
    private SimpleTable historyTable = new SimpleTable();
    private void createTable() {
        historyTable.getChildren().clear();
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(historyTable);
        simpleTableView.setFixedWidth(true);
        
        simpleTableView.setParentOnScene(vbox_list);
    }
    
    private void createRow(CurriculumHistorySummaryMapping chsMap) {
        SimpleTableRow row = new SimpleTableRow();
        row.setRowHeight(90.0);
        
        HBox semesterRow = (HBox) Mono.fx().create()
                .setPackageName("update2.org.cict.layout.curriculum")
                .setFxmlDocument("history-row")
                .makeFX()
                .pullOutLayout();

        Label lbl_description = searchAccessibilityText(semesterRow, "description");
        Label lbl_by = searchAccessibilityText(semesterRow, "by");
        Label lbl_date = searchAccessibilityText(semesterRow, "date");
        
        lbl_description.setText(chsMap.getDescription());
        FacultyMapping faculty = Mono.orm().newSearch(Database.connect().faculty())
                .eq(DB.faculty().id, chsMap.getCreated_by())
                .execute()
                .first();
        if(faculty != null)
            lbl_by.setText(WordUtils.capitalizeFully(faculty.getFirst_name()) + " " + WordUtils.capitalizeFully(faculty.getLast_name()));
        else
            lbl_by.setText("NONE");
        SimpleDateFormat formatter = new SimpleDateFormat("EEEEE MMMMM dd, yyyy HH:mm:ss aa");
        lbl_date.setText(formatter.format(chsMap.getCreated_date()));
        
        SimpleTableCell cellParent = new SimpleTableCell();
        cellParent.setResizePriority(Priority.ALWAYS);
        cellParent.setContent(semesterRow);

        row.addCell(cellParent);
        
        historyTable.addRow(row);
    }
    
    private void showActualData() {
//        ActualDataHistory controller = new ActualDataHistory(CURRICULUM);
//        Mono.fx().create()
//                .setPackageName("update2.org.cict.layout.curriculum")
//                .setFxmlDocument("actual-data-history")
//                .makeFX()
//                .setController(controller)
//                .makeScene()
//                .makeStageWithOwner(Mono.fx().getParentStage(btn_view))
//                .stageResizeable(false)
//                .stageMaximized(true)
//                .stageShow();
        ActualDataHistory controller = new ActualDataHistory(CURRICULUM);
        Pane pane = Mono.fx().create()
                .setPackageName("update2.org.cict.layout.curriculum")
                .setFxmlDocument("actual-data-history")
                .makeFX()
                .setController(controller)
                .pullOutLayout();

        super.setSceneColor(SECTION_BASE_COLOR); // call once on entire scene lifecycle

        Animate.fade(this.application_root, SectionConstants.FADE_SPEED, () -> {
            super.replaceRoot(pane);
        }, pane);
    }
}

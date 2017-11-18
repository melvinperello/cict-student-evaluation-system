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
package update2.org.cict.controller.curriculum;

import app.lazy.models.CurriculumHistoryMapping;
import app.lazy.models.CurriculumMapping;
import app.lazy.models.CurriculumPreMapping;
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
public class ActualDataHistory extends SceneFX implements ControllerFX {
    
    @FXML
    private VBox application_root;
    
    @FXML
    private Label lbl_title;

    @FXML
    private Label lbl_name;

    @FXML
    private Label lbl_description;

    @FXML
    private VBox vbox_list;
    
    @FXML
    private JFXButton btn_back;
    
    private CurriculumMapping CURRICULUM;
    public ActualDataHistory(CurriculumMapping CURRICULUM) {
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
        ArrayList<CurriculumHistoryMapping> chsMaps = Mono.orm().newSearch(Database.connect().curriculum_history())
                .eq(DB.curriculum_history().CURRICULUM_id, this.CURRICULUM.getId())
                .active(Order.desc(DB.curriculum_history().history_id))
                .all();
        if(chsMaps == null)
            return;
        createTable();
        for(CurriculumHistoryMapping chsMap: chsMaps) {
            createRow(chsMap);
        }
    }

    @Override
    public void onEventHandling() {
        this.addClickEvent(btn_back, ()->{
            onBack();
        });
    }
       
    private final String SECTION_BASE_COLOR = "#414852";
    private void onBack() {
        HistoryController controller = new HistoryController(CURRICULUM);
        Pane pane = Mono.fx().create()
                .setPackageName("update2.org.cict.layout.curriculum")
                .setFxmlDocument("history")
                .makeFX()
                .setController(controller)
                .pullOutLayout();

        super.setSceneColor(SECTION_BASE_COLOR); // call once on entire scene lifecycle

        Animate.fade(this.application_root, SectionConstants.FADE_SPEED, () -> {
            super.replaceRoot(pane);
        }, pane);
    }
     
    private SimpleTable historyTable = new SimpleTable();
    private void createTable() {
        historyTable.getChildren().clear();
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(historyTable);
        simpleTableView.setFixedWidth(true);
        
        simpleTableView.setParentOnScene(vbox_list);
    }
    
    private void createRow(CurriculumHistoryMapping chsMap) {
        SimpleTableRow row = new SimpleTableRow();
        row.setRowHeight(255.0);
        
        HBox historyRow = (HBox) Mono.fx().create()
                .setPackageName("update2.org.cict.layout.curriculum")
                .setFxmlDocument("actual-data")
                .makeFX()
                .pullOutLayout();

        //CREATED
        Label lbl_created_by = searchAccessibilityText(historyRow, "created_by");
        Label lbl_created_date = searchAccessibilityText(historyRow, "created_date");
        
        //CURRICULUM INFO
        Label lbl_name = searchAccessibilityText(historyRow, "name");
        Label lbl_description = searchAccessibilityText(historyRow, "description");
        Label lbl_major = searchAccessibilityText(historyRow, "major");
        Label lbl_ladder = searchAccessibilityText(historyRow, "ladder");
        Label lbl_type = searchAccessibilityText(historyRow, "type");
        
        //UPDATED BY
        Label lbl_updated_by = searchAccessibilityText(historyRow, "updated_by");
        Label lbl_updated_date = searchAccessibilityText(historyRow, "updated_date");
        
        //REMOVED BY
        Label lbl_removed_by = searchAccessibilityText(historyRow, "removed_by");
        Label lbl_removed_date = searchAccessibilityText(historyRow, "removed_date");
        
        //SET CURRICULUM INFO
        lbl_ladder.setText(chsMap.getLadderization());
        if(chsMap.getMajor() != null)
            lbl_major.setText(chsMap.getMajor());
        else
            lbl_major.setText("NONE");
        lbl_name.setText(chsMap.getName());
        if(chsMap.getDescription() != null)
            lbl_description.setText(chsMap.getDescription());
        else
            lbl_description.setText("NONE");
        lbl_type.setText(chsMap.getLadderization_type());
        
        SimpleDateFormat formatter = new SimpleDateFormat("EEEEE MMMMM dd, yyyy HH:mm:ss aa");
        
        // SET CREATED
        FacultyMapping facultyCreated = Mono.orm().newSearch(Database.connect().faculty())
                .eq(DB.faculty().id, chsMap.getCreated_by())
                .execute()
                .first();
        if(facultyCreated != null) {
            lbl_created_by.setText(WordUtils.capitalizeFully(facultyCreated.getFirst_name()) + " " + WordUtils.capitalizeFully(facultyCreated.getLast_name()));
            lbl_created_date.setText(formatter.format(chsMap.getCreated_date()));
        } else {
            lbl_created_by.setText("NONE");
            lbl_created_date.setText("N / A");
        }
        
        
        // SET UPDATED
        FacultyMapping facultyUpdated = Mono.orm().newSearch(Database.connect().faculty())
                .eq(DB.faculty().id, chsMap.getUpdated_by())
                .execute()
                .first();
        if(facultyUpdated != null) {
            lbl_updated_by.setText(WordUtils.capitalizeFully(facultyUpdated.getFirst_name()) + " " + WordUtils.capitalizeFully(facultyUpdated.getLast_name()));
            lbl_updated_date.setText(formatter.format(chsMap.getUpdated_date()));
        }
          
        // SET REMOVED
        FacultyMapping facultyRemoved = Mono.orm().newSearch(Database.connect().faculty())
                .eq(DB.faculty().id, chsMap.getRemoved_by())
                .execute()
                .first();
        if(facultyRemoved != null) {
            lbl_removed_by.setText(WordUtils.capitalizeFully(facultyRemoved.getFirst_name()) + " " + WordUtils.capitalizeFully(facultyRemoved.getLast_name()));
            lbl_removed_date.setText(formatter.format(chsMap.getRemoved_date()));
        }
        
        SimpleTableCell cellParent = new SimpleTableCell();
        cellParent.setResizePriority(Priority.ALWAYS);
        cellParent.setContent(historyRow);

        row.addCell(cellParent);
        
        historyTable.addRow(row);
    }
}

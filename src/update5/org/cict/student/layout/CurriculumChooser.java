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
package update5.org.cict.student.layout;

import app.lazy.models.CurriculumMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import com.jhmvin.transitions.Animate;
import com.melvin.mono.fx.MonoLauncher;
import com.melvin.mono.fx.bootstrap.M;
import com.melvin.mono.fx.events.MonoClick;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.cict.evaluation.moving_up.CurriculumRow;
import org.controlsfx.control.Notifications;
import update3.org.cict.CurriculumConstants;

/**
 *
 * @author Joemar
 */
public class CurriculumChooser extends MonoLauncher {

    @FXML
    private VBox application_root;

    @FXML
    private VBox vbox_list;

    @FXML
    private VBox vbox_no_found;

    @FXML
    private JFXButton btn_cancel;

    @Override
    public void onStartUp() {
        MonoClick.addClickEvent(btn_cancel, () -> {
            this.close();
        });
    }

    private CurriculumMapping selected;

    @Override
    public void onDelayedStart() {
        super.onDelayedStart(); //To change body of generated methods, choose Tools | Templates.
        this.selected = null;
        this.loadTable();
    }

    private void loadTable() {
        vbox_no_found.setVisible(false);
        vbox_list.setVisible(false);
        FetchCurriculum fetch = new FetchCurriculum();
        fetch.whenSuccess(() -> {
            Animate.fade(vbox_no_found, 150, () -> {
                vbox_no_found.setVisible(false);
                vbox_list.setVisible(true);
            }, vbox_list);
            createTable(fetch.getResults());
        });
        fetch.whenCancelled(() -> {
            Animate.fade(vbox_list, 150, () -> {
                vbox_list.setVisible(false);
                vbox_no_found.setVisible(true);
            }, vbox_no_found);
        });
        fetch.whenFailed(() -> {
            Animate.fade(vbox_list, 150, () -> {
                vbox_list.setVisible(false);
                vbox_no_found.setVisible(true);
            }, vbox_no_found);
            Notifications.create().darkStyle()
                    .title("Process Failed")
                    .text("Something went wrong. Please"
                            + "\ntry again later.")
                    .showError();
        });
        fetch.transact();
    }

    class FetchCurriculum extends Transaction {

        private ArrayList<CurriculumMapping> results;

        public ArrayList<CurriculumMapping> getResults() {
            return results;
        }
        private String log;

        @Override
        protected boolean transaction() {
            //------------------------------------------------------------------
            ArrayList<CurriculumMapping> curriculums = Mono.orm()
                    .newSearch(Database.connect().curriculum())
                    //                    .eq(DB.curriculum().implemented, 1)
                    // must not be 1 or must not be null
                    .neOrNn(DB.curriculum().obsolete_term, 1)
                    // must not be consequent
                    // shift to the preparatory then moving up
                    .ne(DB.curriculum().ladderization_type, CurriculumConstants.TYPE_CONSEQUENT)
                    .active()
                    .all();
            //------------------------------------------------------------------
            if (curriculums == null) {
                log = "No account found.";
                return false;
            }
            results = new ArrayList<>();
            results.addAll(curriculums);
            return true;
        }

        @Override
        protected void after() {
        }

    }

    private void createTable(ArrayList<CurriculumMapping> lst_info) {
        SimpleTable curriculumTable = new SimpleTable();
        curriculumTable.getChildren().clear();
        for (CurriculumMapping info : lst_info) {
            createRow(curriculumTable, info);
        }

        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(curriculumTable);
        simpleTableView.setFixedWidth(true);

        simpleTableView.setParentOnScene(vbox_list);

    }

    private void createRow(SimpleTable marshallTable, CurriculumMapping each) {
        SimpleTableRow row = new SimpleTableRow();
        row.setRowHeight(70.0);
        CurriculumRow rowFX = M.load(CurriculumRow.class);

        Label lbl_name = rowFX.getLbl_curriculum_name();
        Label lbl_major = rowFX.getLbl_major();
        JFXButton btn_select = rowFX.getBtn_select();

        lbl_name.setText(each.getName());
        lbl_major.setText((each.getMajor() == null ? "NONE" : each.getMajor()));
        MonoClick.addClickEvent(btn_select, () -> {
            selected = (CurriculumMapping) row.getRowMetaData().get("MORE_INFO");
            Mono.fx().getParentStage(application_root).close();
        });

        row.getRowMetaData().put("MORE_INFO", each);

        SimpleTableCell cellParent = new SimpleTableCell();
        cellParent.setResizePriority(Priority.ALWAYS);
        cellParent.setContent(rowFX.getApplicationRoot());

        row.addCell(cellParent);
        marshallTable.addRow(row);
    }

    public CurriculumMapping getSelected() {
        return selected;
    }
}

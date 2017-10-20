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
package update3.org.facultychooser;

import app.lazy.models.Database;
import app.lazy.models.FacultyMapping;
import com.izumi.mono.fx.MonoLauncher;
import com.izumi.mono.fx.Stageable;
import com.izumi.mono.fx.events.MonoClick;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author Jhon Melvin
 */
public class FacultyChooser extends MonoLauncher implements Stageable {

    @FXML
    private VBox application_root;

    @FXML
    private TextField txt_search;

    @FXML
    private JFXButton btn_search;

    @FXML
    private JFXButton btn_cancel;

    @FXML
    private VBox vbox_table;

    @Override
    public void onStartUp() {
        FetchFaculty fetchAllTx = new FetchFaculty();
        fetchAllTx.whenStarted(() -> {
        });
        fetchAllTx.whenCancelled(() -> {
        });
        fetchAllTx.whenFailed(() -> {
        });
        fetchAllTx.whenSuccess(() -> {
            this.createTable(fetchAllTx.getFacultyList());
        });
        fetchAllTx.whenFinished(() -> {
        });

        fetchAllTx.transact();

        MonoClick.addClickEvent(btn_cancel, () -> {

        });
        MonoClick.addClickEvent(btn_search, () -> {

        });
    }

    private void createTable(ArrayList<FacultyMapping> list) {
        SimpleTable tbl_faculty = new SimpleTable();
        for (FacultyMapping map : list) {
            SimpleTableRow row = new SimpleTableRow();
            row.setRowHeight(100.0);

            RowFaculty rowController = new RowFaculty();
            Pane rowFaculty = rowController.open();
            rowController.getLbl_name().setText(map.getFirst_name());
            rowController.getLbl_bulsu_id().setText(map.getBulsu_id());
            rowController.getLbl_department().setText(map.getDepartment());

            SimpleTableCell cellParent = new SimpleTableCell();
            cellParent.setResizePriority(Priority.ALWAYS);
            cellParent.setContentAsPane(rowFaculty);

            row.addCell(cellParent);
            // add to table
            tbl_faculty.addRow(row);

        }
        // table view
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(tbl_faculty);
        simpleTableView.setFixedWidth(true);
        // attach to parent variable name in scene builder
        simpleTableView.setParentOnScene(vbox_table);
    }

    class FetchFaculty extends Transaction {

        private ArrayList<FacultyMapping> facultyList;

        public ArrayList<FacultyMapping> getFacultyList() {
            return facultyList;
        }

        @Override
        protected boolean transaction() {
            facultyList = Mono.orm()
                    .newSearch(Database.connect().faculty())
                    .active()
                    .all();
            return true;
        }

        @Override
        protected void after() {

        }
    }

}

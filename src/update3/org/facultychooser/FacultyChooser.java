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

import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.FacultyMapping;
import com.melvin.mono.fx.MonoLauncher;
import com.melvin.mono.fx.bootstrap.M;
import com.melvin.mono.fx.events.MonoClick;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.orm.SQL;
import com.jhmvin.orm.Searcher;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Jhon Melvin
 */
public class FacultyChooser extends MonoLauncher {

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
        MonoClick.addClickEvent(btn_cancel, () -> {
            this.close();
        });
        MonoClick.addClickEvent(btn_search, () -> {
            if (this.txt_search.getText().isEmpty()) {
                this.fetchAll();
            } else {
                onSearchFaculty();
            }
        });
    }

    private FacultyMapping selectedFaculty;

    public FacultyMapping getSelectedFaculty() {
        return selectedFaculty;
    }

    private void onSearchFaculty() {
        SearchFaculty searchTx = new SearchFaculty();
        searchTx.setSearchValue(txt_search.getText().trim());
        searchTx.whenStarted(() -> {
            this.btn_search.setDisable(true);
            this.setCursor(Cursor.WAIT);
        });
        searchTx.whenCancelled(() -> {
        });
        searchTx.whenFailed(() -> {
        });
        searchTx.whenSuccess(() -> {
            this.createTable(searchTx.getFacultyList());
        });
        searchTx.whenFinished(() -> {
            this.btn_search.setDisable(false);
            this.setCursor(Cursor.DEFAULT);
        });

        searchTx.transact();
    }

    private void fetchAll() {
        FetchFaculty fetchAllTx = new FetchFaculty();
        fetchAllTx.whenStarted(() -> {
            this.btn_search.setDisable(true);
            this.setCursor(Cursor.WAIT);
        });
        fetchAllTx.whenCancelled(() -> {
        });
        fetchAllTx.whenFailed(() -> {
        });
        fetchAllTx.whenSuccess(() -> {
            this.createTable(fetchAllTx.getFacultyList());
        });
        fetchAllTx.whenFinished(() -> {
            this.btn_search.setDisable(false);
            this.setCursor(Cursor.DEFAULT);
        });

        fetchAllTx.transact();
    }

    @Override
    public void onDelayedStart() {
        super.onDelayedStart(); //To change body of generated methods, choose Tools | Templates.
        this.selectedFaculty = null;
        this.txt_search.setText("");
        this.fetchAll();
    }

    private void createTable(ArrayList<FacultyMapping> list) {
        if (list == null) {
            list = new ArrayList<FacultyMapping>();
        }
        SimpleTable tbl_faculty = new SimpleTable();
        for (FacultyMapping map : list) {

            RowFaculty rowController = M.app().reload(RowFaculty.class);
            Pane rowFaculty = rowController.getApplicationRoot();
            String facultyName = "";
            facultyName += (map.getLast_name() + ", ");
            facultyName += map.getFirst_name();
            if (map.getMiddle_name() != null) {
                facultyName += (" " + map.getMiddle_name());
            }
            rowController.getLbl_name().setText(facultyName);
            rowController.getLbl_bulsu_id().setText(map.getBulsu_id());
            rowController.getLbl_department().setText(map.getDepartment() == null || map.getDepartment().isEmpty() ? "No Department" : map.getDepartment());

            MonoClick.addClickEvent(rowFaculty, () -> {
                this.selectedFaculty = map;
                this.close();
                //this.close();
            });
            // add to table
            tbl_faculty.addRow(SimpleTable.fxRow(rowFaculty, 100.0));

        }
        SimpleTable.fxTable(tbl_faculty, vbox_table);

    }

    class SearchFaculty extends FetchFaculty {

        private String searchValue;

        public void setSearchValue(String searchValue) {
            this.searchValue = searchValue;
        }

        @Override
        protected boolean transaction() {
            Searcher searchFaculty = Mono.orm()
                    .newSearch(Database.connect().faculty())
                    .pull();
            // pull values to add recursive query
            // add recursive query searcher
            facultyList = this.recursiveQuery(searchFaculty)
                    .active()
                    .all();
            return true;
        }

        /**
         * Add multiple restriction for each part of the text separated by
         * spaces.
         *
         * @param searchQuery
         * @return
         */
        private Searcher recursiveQuery(Searcher searchQuery) {
            for (String textPart : this.searchValue.split(" ")) {
                if (textPart.isEmpty()) {
                    continue;
                }
                searchQuery.put(forAllNames(textPart));
            }
            return searchQuery.pull();
        }

        /**
         * Apply restriction to every part of the text not as a whole.
         *
         * @param textPart
         * @return
         */
        private Criterion forAllNames(String textPart) {
            return SQL.or(
                    Restrictions.ilike(DB.faculty().first_name, textPart, MatchMode.ANYWHERE),
                    Restrictions.ilike(DB.faculty().middle_name, textPart, MatchMode.ANYWHERE),
                    Restrictions.ilike(DB.faculty().last_name, textPart, MatchMode.ANYWHERE)
            );
        }

    }

    /**
     *
     */
    class FetchFaculty extends Transaction {

        protected ArrayList<FacultyMapping> facultyList = new ArrayList<>();

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

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
package update4.org.cict.linked_manager;

import app.lazy.models.AccountStudentMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.StudentMapping;
import artifacts.MonoString;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import com.jhmvin.orm.SQL;
import com.jhmvin.orm.Searcher;
import com.jhmvin.transitions.Animate;
import com.melvin.mono.fx.MonoLauncher;
import com.melvin.mono.fx.bootstrap.M;
import com.melvin.mono.fx.events.MonoClick;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.controlsfx.control.Notifications;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Joemar
 */
public class StudentChooser extends MonoLauncher {

    @FXML
    private VBox application_root;

    @FXML
    private TextField txt_search;

    @FXML
    private JFXButton btn_search;

    @FXML
    private VBox vbox_list;

    @FXML
    private VBox vbox_no_found;

    @FXML
    private JFXButton btn_cancel;

    private boolean isShown = false;
    public void showChangeAffiliates(boolean show) {
        this.isShown = show;
    }
    
    @Override
    public void onStartUp() {
        MonoClick.addClickEvent(btn_cancel, ()->{
            Mono.fx().getParentStage(application_root).close();
        });
    }
     
    @Override
    public void onDelayedStart() {
        super.onDelayedStart(); //To change body of generated methods, choose Tools | Templates.
        this.account = null;
        this.txt_search.setText("");
        this.loadMarshalls();
        
        Mono.fx().key(KeyCode.ENTER).release(application_root, ()->{
            this.onSearch();
        });
        
        MonoClick.addClickEvent(btn_search, ()->{
            this.onSearch();
        });
    }
    
    private void onSearch() {
        String value = MonoString.removeExtraSpace(txt_search.getText());
        if(value.isEmpty()) {
            this.loadMarshalls();
            return;
        }
        FindStudent searchTx = new FindStudent();
        searchTx.setSearchValue(value);
        searchTx.whenStarted(() -> {
            this.btn_search.setDisable(true);
            this.setCursor(Cursor.WAIT);
        });
        searchTx.whenCancelled(() -> {
            Notifications.create().darkStyle()
                    .title("No Result")
                    .text("No student found.").showWarning();
            this.loadMarshalls();
        });
        searchTx.whenFailed(() -> {
            Notifications.create().darkStyle()
                    .title("Search Failed")
                    .text("Sorry for the inconvinince. ").showError();
        });
        searchTx.whenSuccess(() -> {
            this.createTable(searchTx.getStudentList());
        });
        searchTx.whenFinished(() -> {
            this.btn_search.setDisable(false);
            this.setCursor(Cursor.DEFAULT);
        });
        
        searchTx.transact();
    }
    
    class FindStudent extends Transaction {

        private String searchValue;
        public void setSearchValue(String searchValue) {
            this.searchValue = searchValue;
        }
        
        private ArrayList<StudentAccountInfo> list = new ArrayList<>();
        public ArrayList<StudentAccountInfo> getStudentList() {
            return list;
        }
        @Override
        protected boolean transaction() {
            Searcher searchStudent = Mono.orm()
                    .newSearch(Database.connect().student())
                    .pull();
            ArrayList<StudentMapping> studentList = this.recursiveQuery(searchStudent)
                    .active()
                    .all();
            if(studentList==null)
                return false;
            for(StudentMapping each: studentList) {
                StudentAccountInfo info = new StudentAccountInfo(each);
                if(info.getAccountMapping()==null)
                    continue;
                list.add(info);
            }
            return true;
        }
        
        private Searcher recursiveQuery(Searcher searchQuery) {
            for (String textPart : this.searchValue.split(" ")) {
                if (textPart.isEmpty()) {
                    continue;
                }
                searchQuery.put(forAllNames(textPart));
            }
            return searchQuery.pull();
        }
        
        private Criterion forAllNames(String textPart) {
            return SQL.or(
                    Restrictions.ilike(DB.student().first_name, textPart, MatchMode.ANYWHERE),
                    Restrictions.ilike(DB.student().middle_name, textPart, MatchMode.ANYWHERE),
                    Restrictions.ilike(DB.student().last_name, textPart, MatchMode.ANYWHERE)
            );
        }
    }
    
    private void loadMarshalls() {
        vbox_no_found.setVisible(false);
        vbox_list.setVisible(false);
        FetchStudent fetch = new FetchStudent();
        fetch.whenSuccess(()->{
            Animate.fade(vbox_no_found, 150, ()->{
                vbox_no_found.setVisible(false);
                vbox_list.setVisible(true);
            }, vbox_list);
            createTable(fetch.getResults());
        });
        fetch.whenCancelled(()->{
            Animate.fade(vbox_list, 150, ()->{
                vbox_list.setVisible(false);
                vbox_no_found.setVisible(true);
            }, vbox_no_found);
        });
        fetch.whenFailed(()->{
            Animate.fade(vbox_list, 150, ()->{
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
    class FetchStudent extends Transaction {

        private ArrayList<StudentAccountInfo> results;
        public ArrayList<StudentAccountInfo> getResults() {
            return results;
        }
        private String log;
        @Override
        protected boolean transaction() {
            ArrayList<AccountStudentMapping> accounts = Mono.orm().newSearch(Database.connect().account_student())
                    .eq(DB.account_student().access_level, "STUDENT")
                    .active().all();
            if(accounts==null) {
                log = "No account found.";
                return false;
            }
            results = new ArrayList<>();
            for(AccountStudentMapping account: accounts) {
                StudentAccountInfo info = new StudentAccountInfo(account);
                results.add(info);
            }
            return true;
        }

        @Override
        protected void after() {
        }
        
    }
    
    private void createTable(ArrayList<StudentAccountInfo> lst_info) {
        SimpleTable marshallTable = new SimpleTable();
        marshallTable.getChildren().clear();
        for(StudentAccountInfo info: lst_info) {
            createRow(marshallTable, info);
        }
        
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(marshallTable);
        simpleTableView.setFixedWidth(true);
        
        simpleTableView.setParentOnScene(vbox_list);
            
    }
    
    
    
    private void createRow(SimpleTable marshallTable, StudentAccountInfo accountInfo) {
        SimpleTableRow row = new SimpleTableRow();
        row.setRowHeight(80.0);
        MarshallRow rowFX = M.load(MarshallRow.class);
        
        Label lbl_name = rowFX.getLbl_name(); 
        Label lbl_org = rowFX.getLbl_org(); 
        Label lbl_num = rowFX.getLbl_student_num(); 
        JFXButton btn_remove = rowFX.getBtn_remove(); //searchAccessibilityText(programRow, "btn_remove");
        JFXButton btn_change_org = rowFX.getBtn_change_org();
        JFXButton btn_save = rowFX.getBtn_save_changes();
        HBox change_org = rowFX.getHbox_change_org();
        HBox view = rowFX.getHbox_view_org();
        ComboBox<String> cmb_org = rowFX.getCmb_org();
        
        if(isShown) {
            cmb_org.getItems().clear();
            cmb_org.getItems().add("SWITS");
            cmb_org.getItems().add("LSC");
            cmb_org.getItems().add("ACT");
            cmb_org.getItems().add("NONE");
            cmb_org.getSelectionModel().select(accountInfo.getAffiliates());
            MonoClick.addClickEvent(btn_change_org, ()->{
                Animate.fade(view, 150, ()->{
                    view.setVisible(false);
                    change_org.setVisible(true);
                }, change_org);
            });

            MonoClick.addClickEvent(btn_save, ()->{
                StudentAccountInfo info = (StudentAccountInfo) row.getRowMetaData().get("MORE_INFO");
                if(info.changeAffiliates(cmb_org.getSelectionModel().getSelectedItem())) {
                    Notifications.create().darkStyle()
                            .title("Updated Successfully")
                            .text("Affiliates of the student is updated"
                                    + "\nto its account.")
                            .showInformation();
                }
                lbl_org.setText(info.getAffiliates());
                Animate.fade(change_org, 150, ()->{
                    change_org.setVisible(false);
                    view.setVisible(true);
                }, view);
            });
        } else
            btn_change_org.setVisible(false);
        
        lbl_num.setText((accountInfo.getStudentMapping().getId()==null? "NO STUDENT NUMBER" : accountInfo.getStudentMapping().getId()));
        lbl_name.setText(accountInfo.getFullName());
        lbl_org.setText(accountInfo.getAffiliates());
        btn_remove.setVisible(false);
        btn_remove.setText("Select");
        MonoClick.addClickEvent(row, ()->{
            StudentAccountInfo info = (StudentAccountInfo) row.getRowMetaData().get("MORE_INFO");
            account = info.getAccountMapping();
            Mono.fx().getParentStage(application_root).close();
        });
        
        row.getRowMetaData().put("MORE_INFO", accountInfo);
        
        SimpleTableCell cellParent = new SimpleTableCell();
        cellParent.setResizePriority(Priority.ALWAYS);
        cellParent.setContent(rowFX.getApplicationRoot());
        
        row.addCell(cellParent);
        marshallTable.addRow(row);
    }
    
    private AccountStudentMapping account;
    public AccountStudentMapping getSelected(){
        return account;
    }
    
    class StudentAccountInfo {
        private StudentMapping student;
        public StudentMapping getStudentMapping() {
            return student;
        }
        
        private AccountStudentMapping account;
        public AccountStudentMapping getAccountMapping() {
            return account;
        }
        
        public String getFullName() {
            return (student.getFirst_name() + " "
                    + (student.getMiddle_name()==null? "" : (student.getMiddle_name() + " "))
                    + student.getLast_name());
        }
        
        public String getAffiliates() {
            return (account.getAffiliates()==null? "" : account.getAffiliates());
        }
        
        public boolean remove() {
            account.setAccess_level("STUDENT");
            return Database.connect().account_student().update(account);
        }
        
        public boolean changeAffiliates(String newAffiliate) {
            account.setAffiliates(newAffiliate);
            return Database.connect().account_student().update(account);
        }
        
        public StudentAccountInfo(AccountStudentMapping account) {
            this.account = account;
            this.run();
        }
        
        public StudentAccountInfo(StudentMapping student) {
            this.student = student;
            if(student==null) {
                this.account = null;
                return;
            }
            this.run2();
        }
        
        private void run() {
            student = Database.connect().student().getPrimary(this.account.getSTUDENT_id());
        }
        
        private void run2() {
            account = Mono.orm().newSearch(Database.connect().account_student())
                    .eq(DB.account_student().STUDENT_id, student.getCict_id())
                    .active().first();
        }
    }
}

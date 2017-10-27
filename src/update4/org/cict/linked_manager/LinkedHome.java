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
package update4.org.cict.linked_manager;

import app.lazy.models.AccountStudentMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.StudentMapping;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import com.jhmvin.transitions.Animate;
import com.melvin.mono.fx.bootstrap.M;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.Notifications;
import update.org.cict.controller.home.Home;

/**
 *
 * @author Jhon Melvin
 */
public class LinkedHome extends SceneFX implements ControllerFX {

    @FXML
    private VBox application_root;

    @FXML
    private JFXButton btn_home;

    @FXML
    private JFXButton btn_linked_session;

    @FXML
    private JFXButton btn_new_session;

    @FXML
    private JFXButton btn_marshalls;

    @FXML
    private VBox vbox_home;

    @FXML
    private VBox vbox_new_session;

    @FXML
    private VBox vbox_marshalls;

    @FXML
    private VBox vbox_no_marshall_found;

    @FXML
    private VBox vbox_list;
    
    @FXML
    private JFXButton btn_new_marshall;
            
    @Override
    public void onInitialization() {
        super.bindScene(application_root);
        // default view.
        this.changeView(vbox_home);
    }

    /**
     * Changes the layout view.
     *
     * @param whatView
     */
    private void changeView(Node whatView) {
        Animate.fade(whatView, 150, () -> {
            vbox_home.setVisible(false);
            vbox_new_session.setVisible(false);
            vbox_marshalls.setVisible(false);
            whatView.setVisible(true);
        }, vbox_home, vbox_new_session, vbox_marshalls);
    }

    @Override
    public void onEventHandling() {
        super.addClickEvent(btn_home, () -> {
            Home.callHome(this);
        });

        super.addClickEvent(btn_linked_session, () -> {
            this.changeView(vbox_home);
        });
        super.addClickEvent(btn_new_session, () -> {
            this.changeView(vbox_new_session);
        });
        super.addClickEvent(btn_marshalls, () -> {
            this.changeView(vbox_marshalls);
            this.loadMarshalls();
        });

        super.addClickEvent(btn_new_marshall, ()->{
            AccountStudentMapping selected = this.selectStudent("Are you sure you want to add the student as a marshall?");
            if(selected==null)
                return;
            selected.setAccess_level("ORGANIZATIONAL");
            if(Database.connect().account_student().update(selected)) {
                this.loadMarshalls();
                Notifications.create().darkStyle()
                        .title("New Marshall is Added")
                        .text("Successfully added a new marshall.")
                        .showInformation();
            }
        });
    }
    
    private void loadMarshalls() {
        vbox_list.setVisible(false);
        vbox_no_marshall_found.setVisible(false);
        FetchMarshall fetch = new FetchMarshall();
        fetch.whenSuccess(()->{
            Animate.fade(vbox_no_marshall_found, 150, ()->{
                vbox_no_marshall_found.setVisible(false);
                vbox_list.setVisible(true);
            }, vbox_list);
            createTable(fetch.getResults());
        });
        fetch.whenCancelled(()->{
            Animate.fade(vbox_list, 150, ()->{
                vbox_list.setVisible(false);
                vbox_no_marshall_found.setVisible(true);
            }, vbox_no_marshall_found);
        });
        fetch.whenFailed(()->{
            Animate.fade(vbox_list, 150, ()->{
                vbox_list.setVisible(false);
                vbox_no_marshall_found.setVisible(true);
            }, vbox_no_marshall_found);
            Notifications.create().darkStyle()
                    .title("Process Failed")
                    .text("Something went wrong. Please"
                            + "\ntry again later.")
                    .showError();
        });
        fetch.transact();
    }
    private AccountStudentMapping selectStudent(String message) {
        StudentChooser studentChooser = M.load(StudentChooser.class);
        studentChooser.onDelayedStart(); // do not put database transactions on startUp
        try {
            System.out.println("Stage Recycled. ^^v");
            studentChooser.getCurrentStage().showAndWait();
        } catch (NullPointerException e) {
            Stage a = studentChooser.createChildStage(super.getStage());
            a.initStyle(StageStyle.UNDECORATED);
            a.showAndWait();
        }

        AccountStudentMapping selected = studentChooser.getSelected();
        if(selected!=null && message!=null) {
            int res = Mono.fx().alert().createConfirmation()
                    .setMessage(message).confirmYesNo();
            if(res==-1)
                return null;
        }
        return selected;
    }
    
    private SimpleTable marshallTable = new SimpleTable();
    private void createTable(ArrayList<StudentAccountInfo> lst_info) {
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
        JFXButton btn_remove = rowFX.getBtn_remove();
        JFXButton btn_change_org = rowFX.getBtn_change_org();
        JFXButton btn_save = rowFX.getBtn_save_changes();
        HBox change_org = rowFX.getHbox_change_org();
        HBox view = rowFX.getHbox_view_org();
        ComboBox<String> cmb_org = rowFX.getCmb_org();
        
        cmb_org.getItems().clear();
        cmb_org.getItems().add("SWITS");
        cmb_org.getItems().add("LSC");
        cmb_org.getItems().add("ACT");
        cmb_org.getItems().add("NONE");
        cmb_org.getSelectionModel().select(accountInfo.getAffiliates());
        
        super.addClickEvent(btn_change_org, ()->{
            Animate.fade(view, 150, ()->{
                view.setVisible(false);
                change_org.setVisible(true);
            }, change_org);
        });
        
        super.addClickEvent(btn_save, ()->{
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
        
        lbl_num.setText((accountInfo.getStudentMapping().getId()==null? "NO STUDENT NUMBER" : accountInfo.getStudentMapping().getId()));
        lbl_name.setText(accountInfo.getFullName());
        lbl_org.setText(accountInfo.getAffiliates());
        super.addClickEvent(btn_remove, ()->{
            StudentAccountInfo info = (StudentAccountInfo) row.getRowMetaData().get("MORE_INFO");
            if(info.remove()) {
                this.loadMarshalls();
                Notifications.create().darkStyle()
                        .title("Removed Successfully")
                        .text("Student is removed from"
                                + "\nbeing a marshall.")
                        .showInformation();
            }
        });
        
        row.getRowMetaData().put("MORE_INFO", accountInfo);
        
        SimpleTableCell cellParent = new SimpleTableCell();
        cellParent.setResizePriority(Priority.ALWAYS);
        cellParent.setContent(rowFX.getApplicationRoot());
        
        row.addCell(cellParent);
        marshallTable.addRow(row);
    }
    
    class FetchMarshall extends Transaction {

        private ArrayList<StudentAccountInfo> results;
        public ArrayList<StudentAccountInfo> getResults() {
            return results;
        }
        private String log;
        @Override
        protected boolean transaction() {
            ArrayList<AccountStudentMapping> accounts = Mono.orm().newSearch(Database.connect().account_student())
                    .eq(DB.account_student().access_level, "ORGANIZATIONAL")
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
        private void run() {
            student = Database.connect().student().getPrimary(this.account.getSTUDENT_id());
        }
    }
}

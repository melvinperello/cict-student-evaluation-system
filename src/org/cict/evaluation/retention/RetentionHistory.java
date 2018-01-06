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
package org.cict.evaluation.retention;

import app.lazy.models.CurriculumMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.RetentionPolicyMapping;
import app.lazy.models.RetentionSubjectMapping;
import app.lazy.models.StudentMapping;
import com.jhmvin.Mono;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import com.melvin.mono.fx.MonoLauncher;
import com.melvin.mono.fx.bootstrap.M;
import com.melvin.mono.fx.events.MonoClick;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.hibernate.criterion.Order;
import update3.org.cict.layout.default_loader.LoaderView;
import update3.org.cict.window_prompts.empty_prompt.EmptyView;
import update3.org.cict.window_prompts.fail_prompt.FailView;

/**
 *
 * @author Joemar
 */
public class RetentionHistory extends MonoLauncher{

    @FXML
    private VBox application_root;

    @FXML
    private Label lbl_student_num;

    @FXML
    private Label lbl_student_name;

    @FXML
    private VBox vbox_table;
    
    @FXML
    private StackPane stack_retention;
    
    private StudentMapping student;
    public void setStudent(StudentMapping student) {
        this.student = student;
    }
    
    private LoaderView loaderView;
    private FailView failView;
    private EmptyView emptyView;
    @Override
    public void onStartUp() {
        this.loaderView = new LoaderView(stack_retention);
        this.failView = new FailView(stack_retention);
        this.emptyView = new EmptyView(stack_retention);
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onDelayedStart() {
        lbl_student_num.setText(student.getId());
        lbl_student_name.setText(student.getLast_name() + ", " + student.getFirst_name() + (student.getMiddle_name()!=null? " " + student.getMiddle_name() : ""));
        this.fetchDetails();
        super.onDelayedStart(); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void fetchDetails() {
        ArrayList<RetentionPolicyMapping> retentionPolicies = Mono.orm().newSearch(Database.connect().retention_policy())
                .eq(DB.retention_policy().STUDENT_id, this.student.getCict_id())
                .active(Order.asc(DB.retention_policy().id)).all();
        this.detachAll();
        if(retentionPolicies == null || retentionPolicies.isEmpty()) {
            System.out.println("NO RETENTION FOUND");
            this.emptyView.getLabelMessage().setStyle("fx-text-fill: black;");
            this.emptyView.setMessage("No Result Found");
            this.emptyView.getButton().setVisible(false);
            this.emptyView.attach();
            return;
        } 
        
        System.out.println(retentionPolicies.size() + "FOUND");
        this.createTable(retentionPolicies);
    }
    
    private void createTable(ArrayList<RetentionPolicyMapping> retentionPolicies) {
        
        SimpleTable tblRetentionPolicy = new SimpleTable();
        for(RetentionPolicyMapping each : retentionPolicies) {
            SimpleTableRow row = new SimpleTableRow();
            row.setRowHeight(77.0);
            
            RetentionRow rowFX = M.load(RetentionRow.class);
            System.out.println("************************");
            CurriculumMapping curriculum = Database.connect().curriculum().getPrimary(each.getCURRICULUM_id());
            if(curriculum!=null) {
                System.out.println(curriculum.getName());
                rowFX.getLbl_curriculum_code().setText(curriculum.getName());
            } else {
                rowFX.getLbl_curriculum_code().setText("NONE");
            }
            
            MonoClick.addClickEvent(rowFX.getBtn_view_printing_details(), ()->{
                Object[] map = (Object[]) row.getRowMetaData().get("MAP");
                RetentionRowDetails r = M.load(RetentionRowDetails.class);
                r.setCurriculum((CurriculumMapping) map[1]);
                r.setRetentionPolicy((RetentionPolicyMapping) map[0]);
                r.setStudent(student);
                r.onDelayedStart();
                try {
                    r.getCurrentStage().show();
                } catch (NullPointerException e) {
                    Stage a = r.createChildStage(Mono.fx().getParentStage(application_root));
                    a.initStyle(StageStyle.UNDECORATED);
                    a.show();
                }
            });
            
            rowFX.getLbl_yearlvl().setText(each.getYear_level());
            rowFX.getLbl_semester().setText(each.getSemester());
            System.out.println(each.getYear_level() + " | " + each.getSemester());

            row.getRowMetaData().put("MAP", new Object[] {each, curriculum});
            SimpleTableCell cellParent = new SimpleTableCell();
            cellParent.setResizePriority(Priority.ALWAYS);
            cellParent.setContentAsPane(rowFX.getApplicationRoot());

            row.addCell(cellParent);
            tblRetentionPolicy.addRow(row);
        }
        
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(tblRetentionPolicy);
        simpleTableView.setFixedWidth(true);
        simpleTableView.setParentOnScene(vbox_table);
    }
    
    private void detachAll() {
        this.emptyView.detach();
        this.failView.detach();
        this.loaderView.detach();
    }
}

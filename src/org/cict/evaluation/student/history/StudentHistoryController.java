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
package org.cict.evaluation.student.history;

import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.StudentDataHistoryMapping;
import app.lazy.models.StudentMapping;
import artifacts.MonoString;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.transitions.Animate;
import com.melvin.mono.fx.bootstrap.M;
import com.melvin.mono.fx.events.MonoClick;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.text.WordUtils;
import org.cict.reports.ReportsUtility;
import org.cict.reports.result.PrintResult;
import org.controlsfx.control.Notifications;
import org.hibernate.criterion.Order;

/**
 *
 * @author Joemar
 */
public class StudentHistoryController implements ControllerFX{

    @FXML
    private VBox application_root;
            
    @FXML
    private Label lbl_title;
            
    @FXML
    private Label lblName;

    @FXML
    private Label lblCourse;

    @FXML
    private JFXTreeTableView<History> tblEvaluation;
    
    @FXML
    private JFXButton btnBack;
    
    @FXML
    private JFXButton btn_print;
    
    @FXML
    private JFXButton btn_info_history;
    
    @FXML
    private VBox vbox_table_history;
    
    @FXML
    private StackPane stack_view;
    
            
    private StudentMapping STUDENT;
    private String COURSE, MODULE;
    
    public StudentHistoryController(StudentMapping student, String course, String module) {
        this.STUDENT = student;
        this.COURSE = course;
        this.MODULE = module;
    }
    
    @Override
    public void onInitialization() {
        this.setValues();
        load.setStudent(this.STUDENT);
        load.createTableView(this.tblEvaluation);
        this.changeView(this.tblEvaluation);
    }

    private LoadHistory load = new LoadHistory();
    @Override
    public void onEventHandling() {
        btnBack.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent e) -> {
            Mono.fx().getParentStage(lblName).close();
        });
        MonoClick.addClickEvent(btn_print, ()->{
            this.printResult();
        });
        MonoClick.addClickEvent(btn_info_history, ()->{
            lbl_title.setText((tblEvaluation.isVisible()? "Student Information History".toUpperCase() : "Student EVALUATION HISTORY".toUpperCase()));
            btn_info_history.setText((tblEvaluation.isVisible()? "Show Evaluation History" : "Show Information History"));
            if(tblEvaluation.isVisible()) {
                this.fetchInfoHistory();
                this.changeView(this.vbox_table_history);
            } else {
                this.changeView(this.tblEvaluation);
            }
        });
    }
    
    private void setValues() {
        String fullName = this.STUDENT.getLast_name() + ", " +
                this.STUDENT.getFirst_name() + " " + (this.STUDENT.getMiddle_name()==null? "" :  this.STUDENT.getMiddle_name());
        this.lblName.setText(fullName);
        this.lblCourse.setText(this.COURSE);
    }
    
    private ObservableList<History> results;
    private void printResult() {
        String[] colNames = null;
        ArrayList<String[]> rowData = new ArrayList<>();
        if(tblEvaluation.isVisible()) {
            results = load.getResults();
            if(results==null || results.isEmpty()) {
                Notifications.create()
                        .title("No Result")
                        .text("No result found to print.")
                        .showWarning();
                return;
            }
            colNames = new String[]{"S.Y. & Semester", "Year Level", "Evaluator", "Evaluated Date", "Cancelled By", "Cancelled Date", "Remarks"};

            for (int i = 0; i < results.size(); i++) {
                History result = results.get(i);
                String[] row = new String[]{(i + 1) + ".  " + result.schoolYear.get() + " " + result.semester.get(),
                    result.year.get(), result.evaluatedBy.get(), result.evaluationDate.get(),
                result.canceledBy.get(), result.canceledDate.get(), result.remarks.get()};
                rowData.add(row);
            }
        } else {
            if(previewHistory==null || previewHistory.isEmpty()) {
                Notifications.create()
                        .title("No Result")
                        .text("No result found to print.")
                        .showWarning();
                return;
            }
            colNames = new String[]{"Update Information", "Student No.", "Full Name", "Yr.Level  |  Section  |  Group", "Gender  |  Campus"};
            for (int i = 0; i < previewHistory.size(); i++) {
                StudentDataHistoryMapping result = previewHistory.get(i);
                String[] row = new String[]{(i + 1) + ".  " + result.getUpdated_date() + " | " + result.getUpdated_by(), 
                    result.getStudent_number(), WordUtils.capitalizeFully(result.getLast_name() + ", " + result.getFirst_name() + (result.getMiddle_name()==null || result.getMiddle_name().equalsIgnoreCase("null")? "" : " " + result.getMiddle_name())),
                    (result.getYear_level()==null || result.getYear_level().isEmpty() || result.getYear_level().equalsIgnoreCase("null")? "NONE" : result.getYear_level()) + " | " + 
                        (result.getSection()==null || result.getSection().isEmpty() || result.getSection().equalsIgnoreCase("null")? "NONE" : result.getSection())  + " | " + 
                        (result.get_group()==null || result.get_group().isEmpty() || result.get_group().equalsIgnoreCase("null")? "NONE" : result.get_group()),
                        (result.getGender()==null || result.getGender().isEmpty()? "NONE" : result.getGender()) + " | " + result.getCampus()};
                rowData.add(row);
            }
        }
        PrintResult print = new PrintResult();
        print.columnNames = colNames;
        print.ROW_DETAILS = rowData;
        print.fileName = lbl_title.getText() + " " + MonoString.removeAll(this.STUDENT.getId(), " ").toLowerCase();
        print.reportTitleIntro = this.lblName.getText();
        print.reportTitleHeader = lbl_title.getText();
        print.reportOtherDetail = this.lblCourse.getText();
        print.whenStarted(() -> {
            btn_print.setDisable(true);
        });
        print.whenCancelled(() -> {
            Notifications.create()
                    .title("Request Cancelled")
                    .text("Sorry for the inconviniece.")
                    .showWarning();
        });
        print.whenFailed(() -> {
            Notifications.create()
                    .title("Request Failed")
                    .text("Something went wrong. Sorry for the inconviniece.")
                    .showInformation();
        });
        print.whenSuccess(() -> {
            Notifications.create()
                    .title("Printing Results")
                    .text("Please wait a moment.")
                    .showInformation();
        });
        print.whenFinished(() -> {
            btn_print.setDisable(false);
        });
        //----------------------------------------------------------------------
        print.setDocumentFormat(ReportsUtility.paperSizeChooser(Mono.fx().getParentStage(lblName)));
        if(ReportsUtility.savePrintLogs(this.STUDENT.getCict_id(), lbl_title.getText().toUpperCase(), MODULE, "INITIAL"))
            print.transact();
    }
    
    
    private void changeView(Node node) {
        Animate.fade(node, 150, ()->{
            vbox_table_history.setVisible(false);
            tblEvaluation.setVisible(false);
            node.setVisible(true);
        }, vbox_table_history, tblEvaluation);
    }
    
    private void createTable(ArrayList<StudentDataHistoryMapping> lst_info) {
        SimpleTable infoHistoryTable = new SimpleTable();
        infoHistoryTable.getChildren().clear();
        for (StudentDataHistoryMapping info : lst_info) {
            createRow(infoHistoryTable, info);
        }

        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(infoHistoryTable);
        simpleTableView.setFixedWidth(true);

        simpleTableView.setParentOnScene(vbox_table_history);

    }
    private ArrayList<StudentDataHistoryMapping> previewHistory;
    private void fetchInfoHistory() {
        FetchStudentInfoHistory fetch = new FetchStudentInfoHistory();
        fetch.CICT_id = this.STUDENT.getCict_id();
        fetch.whenStarted(()->{});
        fetch.whenSuccess(()->{
            previewHistory = fetch.getHistoryResult();
            if(previewHistory!=null) {
                this.createTable(previewHistory);
            } else {
                Mono.fx().snackbar().showInfo(application_root, "No Student Information History Found");
            }
        });
        fetch.whenFinished(()->{});
        fetch.whenFailed(()->{});
        fetch.transact();
    }

    private void createRow(SimpleTable table, StudentDataHistoryMapping each) {
        SimpleTableRow row = new SimpleTableRow();
        row.setRowHeight(80.0);
        StudentInfoHistoryRow rowFX = M.load(StudentInfoHistoryRow.class);

        rowFX.getLbl_campus().setText(each.getCampus());
        rowFX.getLbl_gender().setText(each.getGender());
        rowFX.getLbl_section().setText(each.getSection()==null || each.getSection().isEmpty() || each.getSection().equalsIgnoreCase("null")? "NONE" : "Section: " + each.getSection());
        rowFX.getLbl_grp().setText((each.get_group()==null || each.get_group().isEmpty() || each.get_group().equalsIgnoreCase("null")? "NONE" : "Group: " + each.get_group()));
        rowFX.getLbl_yr_lvl().setText(each.getYear_level()==null || each.getYear_level().isEmpty() || each.getYear_level().equalsIgnoreCase("null")? "NONE" : "Year Level: " + each.getYear_level());
        rowFX.getLbl_student_name().setText(each.getLast_name() + ", " + each.getFirst_name() + (each.getMiddle_name()==null || each.getMiddle_name().equalsIgnoreCase("null")? "" : " " + each.getMiddle_name()));
        rowFX.getLbl_student_number().setText(each.getStudent_number());
        rowFX.getLbl_updated_by().setText(each.getUpdated_by());//FacultyUtility.getFacultyName(FacultyUtility.getFaculty(each.getUpdated_by())));
        rowFX.getLbl_updated_date().setText(each.getUpdated_date().toString());
        
        SimpleTableCell cellParent = new SimpleTableCell();
        cellParent.setResizePriority(Priority.ALWAYS);
        cellParent.setContent(rowFX.getApplicationRoot());

        row.addCell(cellParent);
        table.addRow(row);
    }
    
    private class FetchStudentInfoHistory extends Transaction{
        
        public Integer CICT_id;
        
        private ArrayList<StudentDataHistoryMapping> historyResult;
        public ArrayList<StudentDataHistoryMapping> getHistoryResult() {
            return historyResult;
        }
        
        @Override
        protected boolean transaction() {
            historyResult = Mono.orm().newSearch(Database.connect().student_data_history())
                    .eq(DB.student_data_history().cict_id, CICT_id).active(Order.asc(DB.student_data_history().updated_date)).all();
            return true;
        }
        
    }
}

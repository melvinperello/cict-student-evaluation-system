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

import app.lazy.models.StudentMapping;
import artifacts.MonoString;
import com.jfoenix.controls.JFXTreeTableView;
import com.jhmvin.Mono;
import com.jhmvin.fx.display.ControllerFX;
import com.melvin.mono.fx.events.MonoClick;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.cict.reports.ReportsUtility;
import org.cict.reports.result.PrintResult;
import org.controlsfx.control.Notifications;

/**
 *
 * @author Joemar
 */
public class StudentHistoryController implements ControllerFX{

    @FXML
    private Label lblName;

    @FXML
    private Label lblCourse;

    @FXML
    private JFXTreeTableView<History> tblEvaluation;
    
    @FXML
    private Button btnBack;
    
    @FXML
    private Button btn_print;
            
    private StudentMapping STUDENT;
    private String COURSE;
    
    public StudentHistoryController(StudentMapping student, String course) {
        this.STUDENT = student;
        this.COURSE = course;
    }
    
    @Override
    public void onInitialization() {
        this.setValues();
        load.setStudent(this.STUDENT);
        load.createTableView(this.tblEvaluation);
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
    }
    
    private void setValues() {
        String fullName = this.STUDENT.getLast_name() + ", " +
                this.STUDENT.getFirst_name() + " " + (this.STUDENT.getMiddle_name()==null? "" :  this.STUDENT.getMiddle_name());
        this.lblName.setText(fullName);
        this.lblCourse.setText(this.COURSE);
    }
    
    private ObservableList<History> results;
    private void printResult() {
        results = load.getResults();
        if(results==null || results.isEmpty()) {
            Notifications.create()
                    .title("No Result")
                    .text("No result found to print.")
                    .showWarning();
            return;
        }
        String[] colNames = new String[]{"S.Y. & Semester", "Year Level", "Evaluator", "Evaluated Date", "Cancelled By", "Cancelled Date", "Remarks"};
        ArrayList<String[]> rowData = new ArrayList<>();
        for (int i = 0; i < results.size(); i++) {
            History result = results.get(i);
            String[] row = new String[]{(i + 1) + ".  " + result.schoolYear.get() + " " + result.semester.get(),
                result.year.get(), result.evaluatedBy.get(), result.evaluationDate.get(),
            result.canceledBy.get(), result.canceledDate.get(), result.remarks.get()};
            rowData.add(row);
        }
        
        PrintResult print = new PrintResult();
        print.columnNames = colNames;
        print.ROW_DETAILS = rowData;
        print.fileName = "student_history_" + MonoString.removeAll(this.STUDENT.getId(), " ").toLowerCase();
        print.reportTitleIntro = this.lblName.getText();
        print.reportTitleHeader = "Student Evalutaion History";
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
        if(ReportsUtility.savePrintLogs(this.STUDENT.getCict_id(), "Student Evalutaion History".toUpperCase(), "EVALUATION", "INITIAL"))
            print.transact();
    }
    
}

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

import app.lazy.models.AcademicTermMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.EvaluationMapping;
import app.lazy.models.FacultyMapping;
import app.lazy.models.StudentMapping;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jhmvin.Mono;
import java.text.SimpleDateFormat;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import org.cict.evaluation.student.StudentValues;

/**
 *
 * @author Joemar
 */
public class LoadHistory {
    
    private JFXTreeTableView<History> treeTableView;
    private ObservableList<History> lstData;
    private ObservableList<History> holder;
    private StudentMapping STUDENT;
    private StudentValues studentValues = new StudentValues();
    
    public void setStudent(StudentMapping student) {
        this.STUDENT = student;
    }
    
    public void createTableView(JFXTreeTableView<History> tblSubjects) {
        JFXTreeTableColumn<History, String> col_sy = new JFXTreeTableColumn<>("School Year");
        col_sy.setPrefWidth(100);
        col_sy.setCellValueFactory((TreeTableColumn.CellDataFeatures<History, String> param) -> param.getValue().getValue().schoolYear);

        JFXTreeTableColumn<History, String> col_yearlvl = new JFXTreeTableColumn<>("Year Level");
        col_yearlvl.setPrefWidth(130);
        col_yearlvl.setCellValueFactory((TreeTableColumn.CellDataFeatures<History, String> param) -> param.getValue().getValue().year);

        JFXTreeTableColumn<History, String> col_semester = new JFXTreeTableColumn<>("Semester");
        col_semester.setPrefWidth(130);
        col_semester.setCellValueFactory((TreeTableColumn.CellDataFeatures<History, String> param) -> param.getValue().getValue().semester);

        JFXTreeTableColumn<History, String> col_type = new JFXTreeTableColumn<>("Type");
        col_type.setPrefWidth(120);
        col_type.setCellValueFactory((TreeTableColumn.CellDataFeatures<History, String> param) -> param.getValue().getValue().type);

        JFXTreeTableColumn<History, String> col_evalBy = new JFXTreeTableColumn<>("Evaluator");
        col_evalBy.setPrefWidth(200);
        col_evalBy.setCellValueFactory((TreeTableColumn.CellDataFeatures<History, String> param) -> param.getValue().getValue().evaluatedBy);

        JFXTreeTableColumn<History, String> col_evaldate = new JFXTreeTableColumn<>("Evaluation Date");
        col_evaldate.setPrefWidth(150);
        col_evaldate.setCellValueFactory((TreeTableColumn.CellDataFeatures<History, String> param) -> param.getValue().getValue().evaluationDate);

        JFXTreeTableColumn<History, String> col_canceledBy = new JFXTreeTableColumn<>("Canceled By");
        col_canceledBy.setPrefWidth(200);
        col_canceledBy.setCellValueFactory((TreeTableColumn.CellDataFeatures<History, String> param) -> param.getValue().getValue().canceledBy);

        JFXTreeTableColumn<History, String> col_canceledDate = new JFXTreeTableColumn<>("Canceled Date");
        col_canceledDate.setPrefWidth(150);
        col_canceledDate.setCellValueFactory((TreeTableColumn.CellDataFeatures<History, String> param) -> param.getValue().getValue().canceledDate);

        JFXTreeTableColumn<History, String> col_remarks = new JFXTreeTableColumn<>("Remarks");
        col_remarks.setPrefWidth(150);
        col_remarks.setCellValueFactory((TreeTableColumn.CellDataFeatures<History, String> param) -> param.getValue().getValue().remarks);

        lstData = FXCollections.observableArrayList();
        //store data on list
        this.loadData();
        final TreeItem<History> root = new RecursiveTreeItem<>(lstData, RecursiveTreeObject::getChildren);
        tblSubjects.getColumns().setAll(col_sy, col_yearlvl, col_semester, 
                col_type, col_evalBy, col_evaldate, col_remarks, 
                col_canceledBy, col_canceledDate);
        
        tblSubjects.setRoot(root);
        tblSubjects.setShowRoot(false);

        //select the first entry
        tblSubjects.requestFocus();
        tblSubjects.getSelectionModel().select(0);
        tblSubjects.getFocusModel().focus(0);
        this.treeTableView = tblSubjects;
    }
    
    private void loadData() {
        holder = FXCollections.observableArrayList();
        List evaluationList = this.searchResult();
        try{
            for (int x = 0; x < evaluationList.size(); x++) {
                EvaluationMapping currentEvaluation = (EvaluationMapping) evaluationList.get(x);
                this.setAcademicTerm(currentEvaluation.getACADTERM_id());
                String canceledBy = "", canceledDate = "";
                SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
                try{
                    canceledBy = (currentEvaluation.getCancelled_by()==null? "NAME NOT FOUND":this.getFacultyName(currentEvaluation.getCancelled_by()));
                    canceledDate = (currentEvaluation.getCancelled_date()==null? "DATE NOT FOUND": formatter.format(currentEvaluation.getCancelled_date()).toString());
                }catch(NullPointerException a) {
                    canceledBy = "NAME NOT FOUND";
                    canceledDate = "DATE NOT FOUND";
                }
                holder.add(new History((this.ACADEMIC_TERM.getSchool_year()==null? "" : this.ACADEMIC_TERM.getSchool_year()),
                        (currentEvaluation.getYear_level()==null? "" : StudentValues.getYearLevel(currentEvaluation.getYear_level())),
                        (this.ACADEMIC_TERM.getSemester()==null? "" : this.ACADEMIC_TERM.getSemester()), 
                        (currentEvaluation.getType()==null?"":currentEvaluation.getType()),
                        (currentEvaluation.getFACULTY_id()==null? "" : this.getFacultyName(currentEvaluation.getFACULTY_id())),
                        (currentEvaluation.getEvaluation_date()==null? "" : formatter.format(currentEvaluation.getEvaluation_date()).toString()),
                        canceledBy,
                        canceledDate,
                        (currentEvaluation.getRemarks()==null? "":currentEvaluation.getRemarks())));
            }
        }catch(NullPointerException a){}
        lstData.removeAll(lstData);
        lstData.addAll(holder);
    }
    
    private List searchResult() {
        return Mono.orm()
                .newSearch(Database.connect().evaluation())
                .eq(DB.evaluation().STUDENT_id, this.STUDENT.getCict_id())
                .execute()
                .all();
    }
    
    private AcademicTermMapping ACADEMIC_TERM;
    private void setAcademicTerm(Integer acadTermId) {
        this.ACADEMIC_TERM = Database.connect().academic_term().getPrimary(acadTermId);
        /*Mono.orm()
                .newSearch(Database.connect().academic_term())
                .eq(DB.academic_term().id, acadTermId)
                .execute()
                .first();*/
    }
    
    private String getFacultyName(Integer faculty_id) {
        FacultyMapping faculty = Database.connect().faculty().getPrimary(faculty_id);
                /*Mono.orm()
                .newSearch(Database.connect().faculty())
                .eq("id", faculty_id)
                .active()
                .first();*/
        if(faculty==null)
            return "";
        return faculty.getLast_name() + ", " +
                faculty.getFirst_name() + " " +
                (faculty.getMiddle_name()==null? "" : faculty.getMiddle_name());
    }
}

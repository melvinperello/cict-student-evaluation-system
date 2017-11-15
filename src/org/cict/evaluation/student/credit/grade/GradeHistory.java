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
package org.cict.evaluation.student.credit.grade;

import app.lazy.models.AcademicTermMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.FacultyMapping;
import app.lazy.models.GradeMapping;
import app.lazy.models.SubjectMapping;
import com.jfoenix.controls.JFXCheckBox;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.apache.commons.lang3.text.WordUtils;
import org.controlsfx.control.Notifications;
import org.hibernate.criterion.Order;

/**
 *
 * @author Jhon Melvin
 */
public class GradeHistory extends SceneFX implements ControllerFX {

    @FXML
    private VBox application_root;

    @FXML
    private Label lbl_subject_code;

    @FXML
    private Label lbl_subject_title;

    @FXML
    private Label lbl_created_by;

    @FXML
    private Label lbl_created_date;

    @FXML
    private Label lbl_posted_by;

    @FXML
    private Label lbl_posted_date;

    @FXML
    private ComboBox<AcademicTermMapping> cmb_term;

    @FXML
    private VBox vbox_grade_table;

    public GradeHistory() {
        //
    }

    private Integer subjectID;

    public void setSubjectID(Integer subjectID) {
        this.subjectID = subjectID;
    }

    @Override
    public void onInitialization() {
        super.bindScene(application_root);
        fetchData();
    }

    private void fetchData() {
        FetchGradeHistory fetchTx = new FetchGradeHistory();
        fetchTx.subjectID = this.subjectID;

        fetchTx.whenStarted(() -> {
            this.cmb_term.setDisable(true);
        });
        fetchTx.whenCancelled(() -> {
            Mono.fx().alert().createInfo().setTitle("No History")
                    .setHeader("No History")
                    .setMessage("This Grade has no history.")
                    .showAndWait();
            super.close();
        });
        fetchTx.whenFailed(() -> {
            Mono.fx().alert().createInfo().setTitle("History")
                    .setHeader("Failed to Load")
                    .setMessage("Cannot retrieve grade history at the moment.")
                    .showAndWait();
            super.close();
        });
        fetchTx.whenSuccess(() -> {
            this.onSuccess(fetchTx);
        });

        fetchTx.whenFinished(() -> {

        });
        fetchTx.transact();
    }

    private void onSuccess(FetchGradeHistory fetchTx) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:ss a");
        SubjectMapping subject = fetchTx.subjectInfo;
        //----------------------------------------------------------------------
        this.lbl_subject_code.setText(subject.getCode());
        this.lbl_subject_title.setText(WordUtils.capitalizeFully(subject.getDescriptive_title()));
        //----------------------------------------------------------------------
        GradeMapping latestGrade = fetchTx.gradeHistory.get(0);
        if (latestGrade.getCreated_by() != null) {
            this.lbl_created_by.setText(getFacultyName(latestGrade.getCreated_by()));
            this.lbl_created_date.setText(dateFormat.format(latestGrade.getCreated_date()));
        }

        if (latestGrade.getPosted().equals(1)) {
            this.lbl_posted_by.setText(getFacultyName(latestGrade.getPosted_by()));
            this.lbl_posted_date.setText(dateFormat.format(latestGrade.getPosting_date()));
        } else {
            this.lbl_posted_by.setText("UNPOSTED");
            this.lbl_posted_date.setText("UNPOSTED");
        }

        ArrayList<AcademicTermMapping> termList = fetchTx.academicTerm;
        if (termList == null) {
            this.cmb_term.setDisable(true);
        } else {
            //------------------------------------------------------------------
            // create cell value factory
            Callback<ListView<AcademicTermMapping>, ListCell<AcademicTermMapping>> factory = lv -> {
                return new ListCell<AcademicTermMapping>() {
                    @Override
                    protected void updateItem(AcademicTermMapping item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(empty ? "" : (item.getSchool_year() + " " + item.getSemester()));
                    }
                };
            };
            // add items
            this.cmb_term.getItems().clear();
            this.cmb_term.getItems().addAll(termList);
            this.cmb_term.setCellFactory(factory);
            this.cmb_term.setButtonCell(factory.call(null));
            // select default
            this.cmb_term.setPromptText("When this was taken ?");
            if (latestGrade.getACADTERM_id() != null) {
                for (AcademicTermMapping term : termList) {
                    if (term.getId().equals(latestGrade.getACADTERM_id())) {
                        this.cmb_term.getSelectionModel().select(term);
                        break;
                    }
                }
            }
            //------------------------------------------------------------------
            // add value listener
            this.cmb_term.valueProperty().addListener((ObservableValue<? extends AcademicTermMapping> observable, AcademicTermMapping oldValue, AcademicTermMapping newValue) -> {
                try {
                    GradeMapping grade = Database.connect().grade().getPrimary(latestGrade.getId());
                    grade.setACADTERM_id(newValue.getId());
                    boolean isUpdated = Database.connect().grade().update(grade);
                    if (isUpdated) {
                        Mono.fx().snackbar().showSuccess(application_root, "Success");
                    } else {
                        Mono.fx().snackbar().showError(application_root, "Failed");
                        this.cmb_term.getSelectionModel().select(oldValue);
                    }
                } catch (Exception e) {
                    Mono.fx().snackbar().showError(application_root, "Failed");
                    this.cmb_term.getSelectionModel().select(oldValue);
                }
            });

            //------------------------------------------------------------------
            this.createTable(fetchTx.gradeHistory);
            this.cmb_term.setDisable(false);
        }
    }

    private void createTable(ArrayList<GradeMapping> gradeList) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:ss a");
        SimpleTable tblHistory = new SimpleTable();

        for (GradeMapping grade : gradeList) {
            SimpleTableRow row = new SimpleTableRow();
            row.setRowHeight(55.0);
            row.getRowMetaData().put("MAP", grade);
            HBox fxRow = (HBox) Mono.fx().create()
                    .setPackageName("org.cict.evaluation.student.credit.grade")
                    .setFxmlDocument("row-grade-history")
                    .makeFX()
                    .pullOutLayout();
            
            Label lbl_rating = searchAccessibilityText(fxRow, "lbl_rating");
            Label lbl_remarks = searchAccessibilityText(fxRow, "lbl_remarks");
            Label lbl_reason = searchAccessibilityText(fxRow, "lbl_reason");
            Label lbl_editor = searchAccessibilityText(fxRow, "lbl_editor");
            Label lbl_date = searchAccessibilityText(fxRow, "lbl_date");
            
            //--------------------------
            JFXCheckBox chkbx_correction = searchAccessibilityText(fxRow, "chkbx_correction");
            chkbx_correction.setTooltip(new Tooltip("This will mark the grade inputted as correction."));
            chkbx_correction.setSelected(grade.getGrade_state().equalsIgnoreCase("CORRECTION"));
            super.addClickEvent(chkbx_correction, ()->{
                    GradeMapping map = (GradeMapping) row.getRowMetaData().get("MAP");
                if(chkbx_correction.isSelected()) {
                    map.setGrade_state("CORRECTION");
                    if(Database.connect().grade().update(map)) {
                        Notifications.create().text("Grade is marked as correction.")
                                .title("Successfully Marked").showInformation();
                    } else {
                        Notifications.create().text("Please check your connection to server.")
                                .title("Failed").showError();
                    }
                } else {
                    map.setGrade_state("ACCEPTED");
                    if(Database.connect().grade().update(map)) {
                        Notifications.create().text("Grade is unmarked as correction.")
                                .title("Successfully Unmarked").showInformation();
                    } else {
                        Notifications.create().text("Please check your connection to server.")
                                .title("Failed").showError();
                    }
                }
            });

            //-----------------------------
            
            //------------------------------------------------------------------
            lbl_rating.setText(grade.getRating());
            lbl_remarks.setText(grade.getRemarks());
            //reason
            String description = "No Description";
            if (grade.getReason_for_update() != null) {
                if (!grade.getReason_for_update().isEmpty()) {
                    description = grade.getReason_for_update();
                }
            }

            lbl_reason.setText(description);
            //// updater
            String updater = "No Data";
            if (grade.getUpdated_by() != null) {
                updater = getFacultyName(grade.getUpdated_by());
            }
            lbl_editor.setText(updater);
            // update date
            String update_date = "No Data";
            if (grade.getUpdated_date() != null) {
                update_date = dateFormat.format(grade.getUpdated_date());
            }
            lbl_date.setText(update_date);
            //------------------------------------------------------------------

            SimpleTableCell cellParent = new SimpleTableCell();
            cellParent.setResizePriority(Priority.ALWAYS);
            cellParent.setContentAsPane(fxRow);

            row.addCell(cellParent);
            tblHistory.addRow(row);
        }

// table view
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(tblHistory);
        simpleTableView.setFixedWidth(true);
        // attach to parent variable name in scene builder
        simpleTableView.setParentOnScene(vbox_grade_table);
    }

    private String getFacultyName(Integer facultyID) {
        FacultyMapping faculty = Database.connect().faculty().getPrimary(facultyID);
        if (faculty == null) {
            return "";
        }
        try {
            String firstName = faculty.getFirst_name();
            String middleName = faculty.getMiddle_name() == null ? "" : faculty.getMiddle_name();
            String lastName = faculty.getLast_name();
            return WordUtils.capitalizeFully(lastName + ", " + firstName + " " + middleName);
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public void onEventHandling() {

    }

    class FetchGradeHistory extends Transaction {

        public Integer subjectID;
        //----------------------------------------------------------------------
        public SubjectMapping subjectInfo;
        public ArrayList<GradeMapping> gradeHistory;
        public ArrayList<AcademicTermMapping> academicTerm;

        @Override
        protected boolean transaction() {
            // check subject
            this.subjectInfo = Database.connect().subject().getPrimary(subjectID);

            if (subjectInfo == null) {
                return false;
            }

            gradeHistory = Mono.orm()
                    .newSearch(Database.connect().grade())
                    .eq(DB.grade().SUBJECT_id, subjectID)
                    .execute(Order.desc(DB.grade().id))
                    .all();

            if (gradeHistory == null) {
                return false;
            }

            this.academicTerm = Mono.orm()
                    .newSearch(Database.connect().academic_term())
                    .execute(Order.desc(DB.academic_term().id))
                    .all();

            return true;
        }

        @Override
        protected void after() {

        }
    }

}

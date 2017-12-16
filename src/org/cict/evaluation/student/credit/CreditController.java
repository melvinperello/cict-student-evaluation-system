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
package org.cict.evaluation.student.credit;

import app.lazy.models.CurriculumMapping;
import app.lazy.models.CurriculumSubjectMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.GradeMapping;
import app.lazy.models.StudentMapping;
import app.lazy.models.SubjectMapping;
import app.lazy.models.utils.FacultyUtility;
import com.itextpdf.text.Document;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.FXThread;
import com.jhmvin.fx.async.SimpleTask;
import com.jhmvin.fx.display.ControllerFX;
import com.melvin.mono.fx.events.MonoClick;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.apache.commons.lang3.text.WordUtils;
import org.cict.evaluation.assessment.AssessmentResults;
import org.cict.evaluation.assessment.CurricularLevelAssesor;
import org.cict.evaluation.student.credit.credittree.CreditTree;
import org.cict.evaluation.student.credit.credittree.CreditTreeColumn;
import org.cict.evaluation.student.credit.credittree.CreditTreeRow;
import org.cict.evaluation.student.credit.credittree.CreditTreeView;
import org.cict.reports.ReportsUtility;
import org.cict.reports.result.PrintResult;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.StatusBar;

/**
 *
 * @author Joemar
 */
public class CreditController implements ControllerFX {

    // ROW COLOR
    private final String defaultColor = "#FFFFFF";

    // STATIC VALUES
    public final static String MODE_CREDIT = "CREDIT";
    public final static String MODE_ENCODE = "ENCODE";
    public final static String MODE_READ = "READ";

    @FXML
    private Label lbl_title;
    @FXML
    private Label lbl_STUDENT_id;

    @FXML
    private Label lbl_STUDENT_name;

    @FXML
    private Label lbl_CURRICULUM_name;

    @FXML
    private HBox hbox_main;

    @FXML
    private JFXButton btn_save;
    
    @FXML
    private JFXButton btn_print_history;

    @FXML
    private StatusBar status_bar;

    private String MODULE;
    public CreditController(Integer studentCictID, String mode, String module) {
        this.CICT_ID = studentCictID;
        this.creditMode = mode;
        this.TITLE = mode.equals(CreditController.MODE_READ) ? "Viewing Mode"
                : mode.equals(CreditController.MODE_CREDIT) ? "Editing Mode"
                : "Credit Tree";
        /**
         * Create credit tree;
         */
        creditview = new CreditTreeView();
        this.creditTree = creditview.getTree();
        // set pre defined mode

        /**
         * Mode is credit
         */
        if (creditMode.equalsIgnoreCase(MODE_CREDIT)) {
            this.creditTree.setCreditMode(true);
        }

        // set line colors
        this.creditTree.setLineStartColor("#9ED36B");
        this.creditTree.setLineEndColor("#9ED36B");
        this.creditTree.setLineColor("#9ED36B");
        // row colors
        this.creditTree.setRowDefaultColor(defaultColor);
        this.creditTree.setRowDisallowColor("#F76C83");

        //--------------------------
        this.MODULE = module;
    }

    //--------------------------------------------------------------------------
    private final String TITLE;
    private String creditMode = "";
    private final CreditTreeView creditview;
    private final CreditTree creditTree;
    //--------------------------------------------------------------------------
    private final Integer CICT_ID;
    private StudentMapping STUDENT_MAP;
    private final ArrayList<Object[]> studentGrades = new ArrayList<>();
    private final ArrayList<Object[]> retrievedGrades = new ArrayList<>();
    //--------------------------------------------------------------------------

    @Override
    public void onInitialization() {
        //----------------------------------------------------------------------
        lbl_title.setText(TITLE);
        this.createView();
        this.setStudentInformation();
        //----------------------------------------------------------------------
        if (creditMode.equalsIgnoreCase(MODE_READ)) {
            // disable all text
            this.creditTree.disableAllText();
            this.creditTree.setReadOnly(true);
            this.btn_save.setDisable(true);
        } else if (creditMode.equalsIgnoreCase(MODE_CREDIT)) {
            // re enable text fields.
            this.creditTree.enableAllText();
        }
    }

    /**
     * Set Student Information.
     */
    private void setStudentInformation() {

        this.lbl_STUDENT_id.setText(this.STUDENT_MAP.getId());
        this.lbl_STUDENT_name.setText(this.STUDENT_MAP.getLast_name() + ", "
                + this.STUDENT_MAP.getFirst_name() + " " + (this.STUDENT_MAP.getMiddle_name() == null ? "" : this.STUDENT_MAP.getMiddle_name()));
        //----------------------------------------------------------------------
        // Display Student Curriculum
        CurriculumMapping curriculum = null;
        if (this.STUDENT_MAP.getCURRICULUM_id() != null) {
            curriculum = Database.connect().curriculum()
                    .getPrimary(this.STUDENT_MAP.getCURRICULUM_id());
        }
        CurriculumMapping prepCur = null;
        if (this.STUDENT_MAP.getPREP_id() != null) {
            prepCur = Database.connect().curriculum()
                    .getPrimary(this.STUDENT_MAP.getPREP_id());
        }
        String displayCur = "Cannot Retrieve Data";
        if (curriculum != null) {
            displayCur = curriculum.getName();
        }

        if (prepCur != null) {
            displayCur += (" - " + prepCur.getName());
        }
        this.lbl_CURRICULUM_name.setText(displayCur);
    }

    /**
     * Creates the credit view
     */
    private void createView() {
        STUDENT_MAP = (StudentMapping) Database.connect().student().getPrimary(this.CICT_ID);
        //----------------------------------------------------------------------
        CurricularLevelAssesor assessor = new CurricularLevelAssesor(STUDENT_MAP);
        assessor.assess();
        for (int yrctr = 1; yrctr <= 4; yrctr++) {
            AssessmentResults annualAsses = assessor.getAnnualAssessment(yrctr);

            if (annualAsses == null) {
                continue;
            }
            String yearString = (yrctr == 1) ? "First" : (yrctr == 2) ? "Second"
                    : (yrctr == 3) ? "Third" : "Fourth";
            String header = String.valueOf(yearString + " Year");
            // semesters
            for (int semctr = 1; semctr <= 2; semctr++) {
                // get assessment details of this year
                CreditTreeColumn creditColumn = creditview.createNewColumn();
                creditColumn.setHeader(header);

                /**
                 * Semester String.
                 */
                String semString = (semctr == 1) ? "First " : "Second";
                String subheader = String.valueOf(semString + " Semester");
                creditColumn.setSubHeader(subheader);
                //
                annualAsses.getSemestralResults(semctr).forEach(sem_details -> {
                    CreditTreeRow row = creditview.createNewRow();
                    row.setColor(this.defaultColor);
                    row.setID(sem_details.getSubjectID());

                    // set pre requistes
                    ArrayList<Integer> pre_ids = new ArrayList<>();
                    Integer[] preqid = new Integer[0];
                    if (sem_details.getSubjectRequisites() == null) {
                        // do nothing no preq
                    } else {
                        sem_details.getSubjectRequisites().forEach(pre_requisite -> {
                            pre_ids.add(pre_requisite.getSUBJECT_id_req());
                        });
                        preqid = pre_ids.toArray(new Integer[pre_ids.size()]);
                    }

                    row.setPreRequisites(preqid);

                    //set value
                    row.lblSubjectCode.setText(sem_details.getSubjectDetails().getCode());
                    // if this assessment contains a grade
                    if (sem_details.getGradeDetails() != null) {
                        row.setColor(this.defaultColor);
                        row.getGradeField().setText(sem_details.getGradeDetails().getRating());
                        row.getGradeField().setEditable(false);
                    }

                    creditColumn.getChildRows().add(row);
                });
                creditview.getTree().getChildColumns().add(creditColumn);
            }
        }

        /**
         * Add mouse event upon creation
         */
        creditview.getTree().mouseEvent();
        // extend to parent        
        HBox.setHgrow(creditview, Priority.ALWAYS);
        // bind to parent
        creditview.prefHeightProperty().bind(this.hbox_main.prefHeightProperty());
        // add this view
        this.hbox_main.getChildren().add(creditview);
    }

    @Override
    public void onEventHandling() {
        //btn_save.setDisable(this.readOnly);
        btn_save.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            // if (readOnly == false) {
            readContents();
            // }
        });
        MonoClick.addClickEvent(btn_print_history, ()->{
            this.fetchData();
        });
    }
    private ArrayList<GradeHistory> first_1 = new ArrayList<>();
    private ArrayList<GradeHistory> first_2 = new ArrayList<>();
    private ArrayList<GradeHistory> second_1 = new ArrayList<>();
    private ArrayList<GradeHistory> second_2 = new ArrayList<>();
    private ArrayList<GradeHistory> third_1 = new ArrayList<>();
    private ArrayList<GradeHistory> third_2 = new ArrayList<>();
    private ArrayList<GradeHistory> fourth_1 = new ArrayList<>();
    private ArrayList<GradeHistory> fourth_2 = new ArrayList<>();
    private SimpleDateFormat formatter_filename = new SimpleDateFormat("MMddyyyhhmmss");
    private SimpleDateFormat formatter_display = new SimpleDateFormat("MMMM dd, yyyy");
    
    private void print(boolean is4Yrs) {
        
        ArrayList<String[]> rowData = new ArrayList<>();
        this.addToRow(first_1, rowData, "FIRST YEAR - First Semester");
        this.addToRow(first_2, rowData, "FIRST YEAR - Second Semester");
        this.addToRow(second_1, rowData, "SECOND YEAR - First Semester");
        this.addToRow(second_2, rowData, "SECOND YEAR - Second Semester");
        if(is4Yrs) {
            this.addToRow(third_1, rowData, "THIRD YEAR - First Semester");
            this.addToRow(third_2, rowData, "THIRD YEAR - Second Semester");
            this.addToRow(fourth_1, rowData, "FOURTH YEAR - First Semester");
            this.addToRow(fourth_2, rowData, "FOURTH YEAR - Second Semester");
        }
        
        if(rowData.size()<1) {
            Notifications.create().title("No Result")
                    .text("No result to print.").showWarning();
            return;
        }
        
        String[] colNames = new String[]{"Date and Time","Subject Code", "Rating", "Updated By", "Reason For\nUpdate", "State"};
        String[] colDescprtion = new String[]{"Date and time encoded.","Subject code.", "Rating encoded.", "Faculty who updated the grade.", "Reason for update.", "State of the grade."};
        //--------------
        ArrayList<Object> colDetails = ReportsUtility.paperSizeChooserwithCustomize(Mono.fx().getParentStage(btn_save), colNames, colDescprtion);
        Document doc = (Document) colDetails.get(0);
        if(doc==null) {
            return;
        }
        HashMap<Integer, Object[]> customized = (HashMap<Integer, Object[]>) colDetails.get(1);
        ArrayList<String> newColNames = new ArrayList<>();
        for (int i = 0; i < customized.size(); i++) {
            Object[] details = customized.get(i);
            if(details != null) {
                Boolean isChecked = (Boolean) details[0];
                if(isChecked) {
                    newColNames.add((String) details[1]);
                }
            }
        }
        //
        
        PrintResult print = new PrintResult();
        print.SHOW_EXTRA_HEADER = true;
        print.columnNames = newColNames;
        print.ROW_DETAILS = rowData;
        String dateToday = formatter_filename.format(Mono.orm().getServerTime().getDateWithFormat());
        print.fileName = "Student Grade History " + this.STUDENT_MAP.getId() + "_" + dateToday;
        print.reportTitleIntro = lbl_CURRICULUM_name.getText();
        print.reportTitleHeader = "Student Grade History";
        print.reportOtherDetail = WordUtils.capitalizeFully(lbl_STUDENT_name.getText()) + "\n"
                        + "As of " + formatter_display.format(Mono.orm().getServerTime().getDateWithFormat());
        print.whenStarted(() -> {
            btn_print_history.setDisable(true);
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
            btn_print_history.setDisable(false);
            Notifications.create()
                    .title("Printing Results")
                    .text("Please wait a moment.")
                    .showInformation();
        });
        print.whenFinished(() -> {
            btn_print_history.setDisable(false);
        });
        print.setDocumentFormat(doc, customized);
        if(ReportsUtility.savePrintLogs(this.CICT_ID, "Student Grade History".toUpperCase(), this.MODULE, "INITIAL")) {
            print.transact();
        }
    }
    
    private void addToRow(ArrayList<GradeHistory> storage, ArrayList<String[]> rowData, String title){
        for (int i = 0; i < storage.size(); i++) {
            GradeHistory history = storage.get(i);
            String lastCol = null;
            if(i == 0){
                lastCol = title;
                System.out.println(title);
            }
            String[] row = new String[]{(i+1)+".  "+ history.getCreated(),
                (history.getSubjectCode()), 
                history.getRating(),
                WordUtils.capitalizeFully(history.getUpdatedBy()), 
                WordUtils.capitalizeFully(history.getReason().replace("_", " ")), 
                WordUtils.capitalizeFully(history.getState()), lastCol};
            rowData.add(row);
        }
    }
    
    private boolean is4Yrs = false;
    private void fetchData() {
        first_1.clear();
        first_2.clear();
        second_1.clear();
        second_2.clear();
        third_1.clear();
        third_2.clear();
        fourth_1.clear();
        fourth_2.clear();
        SimpleTask fetch_history = new SimpleTask("fetch_history");
        fetch_history.setTask(()->{
            Integer CICT_id = this.STUDENT_MAP.getCict_id();
            Integer CURRICULUM_id = this.STUDENT_MAP.getCURRICULUM_id();
            Integer PREP_id = this.STUDENT_MAP.getPREP_id();
            CurriculumMapping curriculum = Database.connect().curriculum().getPrimary(CURRICULUM_id);
            is4Yrs = curriculum != null? (curriculum.getStudy_years().equals(4)) : false;
            ArrayList<GradeMapping> grades = Mono.orm().newSearch(Database.connect().grade())
                    .eq(DB.grade().STUDENT_id, CICT_id).execute().all();
            if(grades != null) {
                for(GradeMapping grade: grades) {
                    CurriculumSubjectMapping csMap = Mono.orm().newSearch(Database.connect().curriculum_subject())
                            .eq(DB.curriculum_subject().CURRICULUM_id, CURRICULUM_id)
                            .eq(DB.curriculum_subject().SUBJECT_id, grade.getSUBJECT_id()).active().first();
                    CurriculumSubjectMapping csMap_prep = null;
                    if(PREP_id != null) {
                        csMap_prep = Mono.orm().newSearch(Database.connect().curriculum_subject())
                                .eq(DB.curriculum_subject().CURRICULUM_id, PREP_id)
                                .eq(DB.curriculum_subject().SUBJECT_id, grade.getSUBJECT_id()).active().first();
                     }
                    GradeHistory history;
                    if(csMap != null) {
                        SubjectMapping subject = Database.connect().subject().getPrimary(csMap.getSUBJECT_id());
                        history = new GradeHistory(grade.getCreated_date(), subject==null? "NONE" : subject.getCode(), grade.getRating(), grade.getRemarks(), grade.getUpdated_by()==null? "NONE" : FacultyUtility.getFacultyName(FacultyUtility.getFaculty(grade.getUpdated_by())), grade.getReason_for_update()==null? "NONE" : grade.getReason_for_update(), grade.getActive().equals(1)? "Active": "Inactive");
                        this.store(csMap, history);
                    } else if(csMap_prep != null) {
                        SubjectMapping subject = Database.connect().subject().getPrimary(csMap_prep.getSUBJECT_id());
                        history = new GradeHistory(grade.getCreated_date(), subject==null? "NONE" : subject.getCode(), grade.getRating(), grade.getRemarks(), grade.getUpdated_by()==null? "NONE" : FacultyUtility.getFacultyName(FacultyUtility.getFaculty(grade.getUpdated_by())), grade.getReason_for_update()==null? "NONE" : grade.getReason_for_update(), grade.getActive().equals(1)? "Active": "Inactive");
                        this.store(csMap_prep, history);
                    }
                }
            }
        });
        fetch_history.whenStarted(()->{
            btn_print_history.setDisable(true);
        });
        fetch_history.whenCancelled(()->{
            System.out.println("CANCELLED");
        });
        fetch_history.whenFailed(()->{
            fetch_history.getTaskException().printStackTrace();
            System.out.println("FAILED");
            Notifications.create().darkStyle()
                    .title("Failed")
                    .text("No result found.").showError();
        });
        fetch_history.whenSuccess(()->{
            this.print(is4Yrs);
            btn_print_history.setDisable(false);
        });
        
        if(this.STUDENT_MAP.getCURRICULUM_id()==null) {
            Notifications.create().darkStyle()
                    .title("Cancelled")
                    .text("No curriculum found.").showError();
            return;
        }
        fetch_history.start();
    }
    
    private void store(CurriculumSubjectMapping csMap, GradeHistory history) {
        switch(csMap.getYear()) {
                    case 1:
                        if(csMap.getSemester().equals(1))
                            first_1.add(history);
                        else if(csMap.getSemester().equals(2))
                            first_2.add(history);
                        break;
                    case 2:
                        if(csMap.getSemester().equals(1))
                            second_1.add(history);
                        else if(csMap.getSemester().equals(2))
                            second_2.add(history);
                        break;
                    case 3:
                        if(csMap.getSemester().equals(1))
                            third_1.add(history);
                        else if(csMap.getSemester().equals(2))
                            third_2.add(history);
                        break;
                    case 4:
                        if(csMap.getSemester().equals(1))
                            fourth_1.add(history);
                        else if(csMap.getSemester().equals(2))
                            fourth_2.add(history);
                        break;
                }
    }
    
    class GradeHistory {
        private String created;
        private String subjectCode, rating, remarks, updatedBy, reason, state;
        private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
        GradeHistory(Date created, String subjectCode, String rating, String remarks, String updatedBy, String reason, String state) {
            this.created = format.format(created);
            this.reason = reason;
            this.rating = rating;
            this.updatedBy = updatedBy;
            this.subjectCode = subjectCode;
            this.remarks = remarks;
            this.state = state;
        }

        public String getState() {
            return state;
        }

        public String getCreated() {
            return created;
        }

        public String getSubjectCode() {
            return subjectCode;
        }

        public String getRating() {
            return rating;
        }

        public String getUpdatedBy() {
            return updatedBy;
        }

        public String getReason() {
            return reason==null || reason.isEmpty()? "NOT SPECIFIED" : reason;
        }

        public String getRemarks() {
            return remarks;
        }

        public SimpleDateFormat getFormat() {
            return format;
        }
        
    }

    /**
     * Read All Text Fields.
     */
    private void readContents() {
        // refresh
        this.studentGrades.clear();
        //----------------------------------------------------------------------
        SimpleTask checkFields = new SimpleTask("check-credit-fields");
        checkFields.setTask(() -> {
            // focus on every row.
            readRows();
        });
        checkFields.setOnStart(start -> {
            btn_save.setDisable(true);
            creditTree.disableAllText();
            status_bar.setText("Starting Verification.");
        });
        checkFields.setOnSuccess(success -> {
            // ask to save new values
            saveValues();
            status_bar.setText("Verification Complete.");
            btn_save.setDisable(false);
            creditTree.enableAllText();
        });
        checkFields.setOnCancelled(event -> {
            status_bar.setText("Verification has been cancelled by system.");
            btn_save.setDisable(false);
            creditTree.enableAllText();
        });
        checkFields.setOnFailed(event -> {
            status_bar.setText("Verification Failed.");
            btn_save.setDisable(false);
            creditTree.enableAllText();
        });
        checkFields.start();
    }

    /**
     * Save values to db.
     */
    private void saveValues() {
        CreditEncode ce = new CreditEncode();
        ce.cict_id = this.CICT_ID;
        ce.grades = this.studentGrades;
        ce.setOnStart(onStart -> {
            status_bar.setText("Saving To Database.");
        });
        ce.setOnSuccess(event -> {
            status_bar.setText("Successfully Saved.");
        });

        int choice = Mono.fx()
                .alert()
                .createConfirmation()
                .setTitle("Confirmation")
                .setHeader("Save Your Changes ?")
                .setMessage("This will save the following changes to the database.")
                .confirmYesNo();

        if (choice == 1) {
            ce.transact();
            Notifications.create()
                    .title("Saved Successfully")
                    .text("Grades are successfully saved in the database.")
                    .showInformation();
        } else {

        }

    }

    /**
     * Focuses on every row.
     */
    private void readRows() {
        for (int x = 0; x < creditTree.getChildColumns().size(); x++) {
            for (int y = 0; y < creditTree.getChildColumns().get(x).getChildRows().size(); y++) {
                readRows(x, y);
                FXThread.pause(200);
            }
        }
    }

    /**
     * Method to focus on a specific row.
     *
     * @param x
     * @param y
     */
    private void readRows(int x, int y) {
        Platform.runLater(() -> {
            creditTree.setFocusedCell(x, y);
            CreditTreeRow cr = creditTree.getRowAt(x, y);

            /**
             * Center Scroll pane.
             */
            CreditTree.centerNodeInScrollPane(creditview, cr);
            CreditTree.centerH(creditview, creditTree.getColumn(x));
            Integer id = cr.getID();
            creditTree.showLines(id);

            status_bar.setText("Verifying " + cr.lblSubjectCode.getText() + "...");

            // subject id;
            Integer subject_id = cr.getID();
            // grade
            String grade = cr.txtGrade.getText();

            System.out.println("ID: " + subject_id + " -- " + "GRADE: " + grade);

            if (!grade.isEmpty()) {
                // object array
                Object[] holder = {subject_id, grade};
                studentGrades.add(holder);
            } else {
                // if grade is empty
                // marked this grade to be removed if there is a grade.
                Object[] holder = {subject_id, ""};
                studentGrades.add(holder);
            }

        });
    }

}

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
import app.lazy.models.Database;
import app.lazy.models.StudentMapping;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.FXThread;
import com.jhmvin.fx.async.SimpleTask;
import com.jhmvin.fx.display.ControllerFX;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.cict.evaluation.assessment.AssessmentResults;
import org.cict.evaluation.assessment.CurricularLevelAssesor;
import org.cict.evaluation.student.credit.credittree.CreditTree;
import org.cict.evaluation.student.credit.credittree.CreditTreeColumn;
import org.cict.evaluation.student.credit.credittree.CreditTreeRow;
import org.cict.evaluation.student.credit.credittree.CreditTreeView;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.StatusBar;

/**
 *
 * @author Joemar
 */
public class CreditController implements ControllerFX {

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
    private StatusBar status_bar;

    private final Integer CICT_ID;
    //private Integer CURRICULUM_ID;
    private final CreditTreeView creditview;
    private final CreditTree creditTree;
    private StudentMapping STUDENT_MAP;

    private final ArrayList<Object[]> studentGrades = new ArrayList<>();
    private final ArrayList<Object[]> retrievedGrades = new ArrayList<>();
    private String creditMode = "";
    private final String TITLE;

    public CreditController(Integer studentCictID, String mode, String title) {
        this.CICT_ID = studentCictID;
        this.creditMode = mode;
        this.TITLE = title;
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

    }

    /**
     * Colors of the row.
     */
    private final String defaultColor = "#FFFFFF";

    //static values
    public final static String MODE_CREDIT = "CREDIT";
    public final static String MODE_ENCODE = "ENCODE";
    public final static String MODE_READ = "READ";

    @Override
    public void onInitialization() {
        lbl_title.setText(TITLE);
        this.createView();
        this.setValues();
        /**
         * If read only mode.
         */
        if (creditMode.equalsIgnoreCase(MODE_READ)) {
            /**
             * may disable all text credit tree
             */
            this.creditTree.disableAllText();
            this.creditTree.setReadOnly(true);
            this.btn_save.setDisable(true);
        } else if (creditMode.equalsIgnoreCase(MODE_CREDIT)) {
            // to re enable rows with grades already
            this.creditTree.enableAllText();
        }
    }

    @Override
    public void onEventHandling() {
        //btn_save.setDisable(this.readOnly);
        btn_save.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            // if (readOnly == false) {
            readContents();
            // }
        });
    }

    //-------------------------------------------------------------------------
    //-------------------------------------------------------------------------
    /**
     * Read the contents of the credit tree in a separate thread.
     */
    private void readContents() {
        /**
         * refresh grade list.
         */
        this.studentGrades.clear();

        // check all fields
        SimpleTask checkFields = new SimpleTask("check-credit-fields");

        checkFields.setTask(() -> {
            /**
             * check
             */
            readRows();
        });
        checkFields.setOnStart(start -> {
            btn_save.setDisable(true);
            creditTree.disableAllText();
            status_bar.setText("Starting Verification.");
        });
        checkFields.setOnSuccess(success -> {
            /**
             * Ask to save to database.
             */
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
     * Read all rows in the view.
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
     * UI change must be on FX Thread.
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
            }

        });
    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    /**
     * Get the Student's Curriculum.
     *
     * @author melvin
     * @return
     */
//    private Integer getStudentCurriculum() {
//        List lst = Mono.orm()
//                .newSearch(Database.connect().student())
//                .eq("cict_id", this.CICT_ID)
//                .active();
//        if (!lst.isEmpty()) {
//            this.STUDENT_MAP = (StudentMapping) lst.get(0);
//            return STUDENT_MAP.getCURRICULUM_id();
//        }
//        return null;
//    }
    /**
     * Creates the credit view
     */
    private void createView() {
        /**
         * @melvin
         */
//        this.CURRICULUM_ID = getStudentCurriculum();
//        // if null return
//        if (Objects.isNull(this.CURRICULUM_ID)) {
//            return;
//        }
//        // continue
//        ArrayList<CurriculumSubjectMapping> subList = Mono.orm()
//                .newSearch(Database.connect().curriculum_subject())
//                .eq("CURRICULUM_id", this.CURRICULUM_ID)
//                .active()
//                .all();
//        // if empty return
//        if (subList == null) {
//            return;
//        }

        STUDENT_MAP = (StudentMapping) Database.connect().student().getPrimary(this.CICT_ID);

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
         * @melvin
         */
//        CreditTreeColumn c_11 = creditview.createNewColumn();
//        c_11.setHeader("FIRST YEAR");
//        c_11.setSubHeader("First Semester");
//        CreditTreeColumn c_12 = creditview.createNewColumn();
//        c_12.setHeader("FIRST YEAR");
//        c_12.setSubHeader("Second Semester");
//        CreditTreeColumn c_21 = creditview.createNewColumn();
//        c_21.setHeader("SECOND YEAR");
//        c_21.setSubHeader("First Semester");
//        CreditTreeColumn c_22 = creditview.createNewColumn();
//        c_22.setHeader("SECOND YEAR");
//        c_22.setSubHeader("Second Semester");
//        CreditTreeColumn c_31 = creditview.createNewColumn();
//        c_31.setHeader("THIRD YEAR");
//        c_31.setSubHeader("First Semester");
//        CreditTreeColumn c_32 = creditview.createNewColumn();
//        c_32.setHeader("THIRD YEAR");
//        c_32.setSubHeader("Second Semester");
//        CreditTreeColumn c_41 = creditview.createNewColumn();
//        c_41.setHeader("FOURTH YEAR");
//        c_41.setSubHeader("First Semester");
//        CreditTreeColumn c_42 = creditview.createNewColumn();
//        c_42.setHeader("FOURTH YEAR");
//        c_42.setSubHeader("Second Semester");
//
//        subList.forEach(subjects -> {
//            CurriculumSubjectMapping sub = subjects;
//            //create row
//            CreditTreeRow row = creditview.createNewRow();
//            row.setBackground(new Background(new BackgroundFill(Paint.valueOf("#2B89E0"), new CornerRadii(10), new Insets(5, 5, 5, 5))));
//            // assign id
//            row.setID(sub.getSUBJECT_id());
//            // set code
//            List subnames = Mono.orm()
//                    .newSearch(Database.connect().subject())
//                    .eq("id", sub.getSUBJECT_id())
//                    .active();
//            row.setSubjectCode(((SubjectMapping) subnames.get(0)).getCode());
//            // add preq
//            List reqlist = Mono.orm()
//                    .newSearch(Database.connect().curriculum_requisite_line())
//                    .eq("SUBJECT_id_get", sub.getSUBJECT_id())
//                    .active();
//            // get pre requisite ids
//            Integer[] preqid = new Integer[0];
//            if (!reqlist.isEmpty()) {
//                ArrayList<Integer> ids = new ArrayList<>();
//                for (Object object : reqlist) {
//                    ids.add(((CurriculumRequisiteLineMapping) object).getSUBJECT_id_req());
//                }
//                preqid = ids.toArray(new Integer[ids.size()]);
//            }
//            row.setPreRequisites(preqid);
//
//            switch (sub.getYear()) {
//                case 1:
//                    if (sub.getSemester().equals(1)) {
//                        c_11.getChildRows().add(row);
//                    } else if (sub.getSemester().equals(2)) {
//                        c_12.getChildRows().add(row);
//                    }
//                    break;
//                case 2:
//                    if (sub.getSemester().equals(1)) {
//                        c_21.getChildRows().add(row);
//                    } else if (sub.getSemester().equals(2)) {
//                        c_22.getChildRows().add(row);
//                    }
//                    break;
//                case 3:
//                    if (sub.getSemester().equals(1)) {
//                        c_31.getChildRows().add(row);
//                    } else if (sub.getSemester().equals(2)) {
//                        c_32.getChildRows().add(row);
//                    }
//                    break;
//                case 4:
//                    if (sub.getSemester().equals(1)) {
//                        c_41.getChildRows().add(row);
//                    } else if (sub.getSemester().equals(2)) {
//                        c_42.getChildRows().add(row);
//                    }
//                    break;
//                default:
//                    //none
//                    break;
//            }
//        });
//
//        creditview.getTree().getChildColumns().addAll(c_11, c_12, c_21, c_22, c_31, c_32, c_41, c_42);
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

        /**
         * Retrieving values.
         */
        //this.setRetrievedGrades();
    }

    /**
     * Set Student Information.
     */
    private void setValues() {
        this.lbl_STUDENT_id.setText(this.STUDENT_MAP.getId());
        this.lbl_STUDENT_name.setText(this.STUDENT_MAP.getLast_name() + ", "
                + this.STUDENT_MAP.getFirst_name() + " " + this.STUDENT_MAP.getMiddle_name());
        CurriculumMapping curriculum = Mono.orm()
                .newSearch(Database.connect().curriculum())
                .eq("id", this.STUDENT_MAP.getCURRICULUM_id())
                .execute()
                .first();
        this.lbl_CURRICULUM_name.setText(curriculum.getName());
    }

    /**
     * @joemar
     */
//    private void setRetrievedGrades() {
//        CreditRetrieve retrieve = new CreditRetrieve();
//        retrieve.cict_id = this.STUDENT_MAP.getCict_id();
//        retrieve.setOnStart(onStart -> {
//            this.status_bar.setText("Checking the posted grades...");
//        });
//        retrieve.setOnSuccess(onSuccess -> {
//            this.status_bar.setText("Status: Done");
//            this.retrievedGrades = retrieve.getRetrievedGrades();
//            if (!this.retrievedGrades.isEmpty()) {
//                this.checkContents();
//            }
//        });
//        retrieve.transact();
//    }
//    private void checkContents() {
//        // check all fields
//        SimpleTask checkFields = new SimpleTask("retrieve-posted-credit-fields");
//        
//        checkFields.setTask(() -> {
//            /**
//             * check
//             */
//            retrieveRows();
//        });
//        checkFields.setOnStart(start -> {
//            btn_save.setDisable(true);
////            creditTree.disableAllText();
//            status_bar.setText("Starting the retrieve process...");
//        });
//        checkFields.setOnSuccess(success -> {
//            status_bar.setText("Status: Completed.");
//            btn_save.setDisable(false);
////            creditTree.enableAllText();
//        });
//        checkFields.setOnCancelled(event -> {
//            status_bar.setText("Verification has been cancelled by system.");
//            btn_save.setDisable(false);
//            creditTree.enableAllText();
//        });
//        checkFields.setOnFailed(event -> {
//            status_bar.setText("Verification Failed.");
//            btn_save.setDisable(false);
//            creditTree.enableAllText();
//        });
//        checkFields.start();
//        
//    }
//    
//    private void retrieveRows() {
//        for (int x = 0; x < creditTree.getChildColumns().size(); x++) {
//            for (int y = 0; y < creditTree.getChildColumns().get(x).getChildRows().size(); y++) {
//                retrieveRows(x, y);
////                FXThread.pause(200);
//            }
//        }
//    }
//    
//    private void retrieveRows(int x, int y) {
//        Platform.runLater(() -> {
//            creditTree.setFocusedCell(x, y);
//            CreditTreeRow cr = creditTree.getRowAt(x, y);
//
//            /**
//             * Center Scroll pane.
//             */
//            CreditTree.centerNodeInScrollPane(creditview, cr);
//            CreditTree.centerH(creditview, creditTree.getColumn(x));
//            Integer id = cr.getID();
//            creditTree.showLines(id);
//            
//            status_bar.setText("Verifying " + cr.lblSubjectCode.getText() + "...");
//
//            // subject id;
//            Integer subject_id = cr.getID();
//            
//            for (int i = 0; i < this.retrievedGrades.size(); i++) {
//                if (Objects.equals(subject_id, this.retrievedGrades.get(i)[0])) {
//                    String grade = this.retrievedGrades.get(i)[1].toString();
//                    cr.txtGrade.setText(grade);
//                    cr.txtGrade.setDisable(true);
//                    break;
//                }
//            }
//            if (cr.txtGrade.getText().isEmpty()) {
//                cr.txtGrade.setDisable(false);
//                cr.txtGrade.setEditable(true);
//            }
//        });
//    }
}

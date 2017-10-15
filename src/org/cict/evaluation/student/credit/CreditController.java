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
            } else {
                // if grade is empty
                // marked this grade to be removed if there is a grade.
                Object[] holder = {subject_id, ""};
                studentGrades.add(holder);
            }

        });
    }

    /**
     * Creates the credit view
     */
    private void createView() {

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

}

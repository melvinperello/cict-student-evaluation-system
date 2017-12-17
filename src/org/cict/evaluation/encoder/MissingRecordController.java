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
package org.cict.evaluation.encoder;

import app.lazy.models.AcademicTermMapping;
import app.lazy.models.CurriculumMapping;
import app.lazy.models.CurriculumSubjectMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.GradeMapping;
import app.lazy.models.StudentMapping;
import app.lazy.models.SubjectMapping;
import artifacts.CurriculumFix;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import java.util.ArrayList;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.cict.authentication.authenticator.SystemProperties;
import org.cict.evaluation.assessment.AssessmentResults;
import org.cict.evaluation.assessment.CurricularLevelAssesor;
import org.cict.evaluation.assessment.SubjectAssessmentDetials;
import org.cict.evaluation.evaluator.Evaluator;
import org.hibernate.criterion.Order;

/**
 *
 * @author Joemar
 */
public class MissingRecordController extends SceneFX implements ControllerFX {

    @FXML
    private VBox vbox_main;

    @FXML
    private ListView<String> lstvwAcadTerm;

    @FXML
    private VBox vbox_list;

    @FXML
    private Button btnSkip;

    @FXML
    private Button btnDone;

    private StudentMapping CURRENT_STUDENT;
    private ArrayList<ArrayList<SubjectMapping>> FILTERED_SUBJECTS;
    private ArrayList<String> TITLES;
    private Boolean isCompleted = false;
    private Integer year_level;
    private Integer current_SEMESTER;
//    private Integer current_YEAR;

    public MissingRecordController(StudentMapping currentStudent,
            ArrayList<ArrayList<SubjectMapping>> subjectsPerSem,
            ArrayList<String> yearAndSem, Integer yearLevel) {
        this.FILTERED_SUBJECTS = subjectsPerSem;
        this.TITLES = yearAndSem;
        this.CURRENT_STUDENT = currentStudent;
        this.year_level = yearLevel;
    }

    private void logs(String str) {
        if (true) {
            System.out.println("@MissingRecordController: " + str);
        }
    }

    @Override
    public void onInitialization() {
        this.init();
    }

    @Override
    public void onEventHandling() {
        this.buttonEvents();
    }

    private void buttonEvents() {
        this.btnSkip.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            this.startAssessment("SKIP");
        });

        this.btnDone.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            this.startAssessment("DONE");
        });
    }
    private SimpleTable recordTable = new SimpleTable();

    private void init() {
        //----------------------------------------------------------------------
        AcademicTermMapping acadTermMap = SystemProperties.instance().getCurrentAcademicTerm();
        current_SEMESTER = acadTermMap.getSemester_regular();
        //----------------------------------------------------------------------
//        String[] sy = acadTermMap.getSchool_year().split("-");
//        try {
//            current_YEAR = Integer.valueOf(sy[0]);
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//        }
        //----------------------------------------------------------------------
        for (int i = 0; i < 2; i++) {
            String title = TITLES.get(i);
            createRow(title, i);
        }

        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(recordTable);
        simpleTableView.setFixedWidth(true);
        simpleTableView.setParentOnScene(vbox_list);
        this.vbox_list.setVisible(true);
    }

    private Integer unacquiredCount(Integer sem) {
        //----------------------------------------------------------------------
        Integer count = 0;
        //----------------------------------------------------------------------
        Integer correctCurriculum = CurriculumFix
                .getCorrectCurriculum(CURRENT_STUDENT, this.year_level);
        //----------------------------------------------------------------------
        ArrayList<CurriculumSubjectMapping> csMaps = Mono.orm()
                .newSearch(Database.connect().curriculum_subject())
                .eq(DB.curriculum_subject().CURRICULUM_id, correctCurriculum)
                .eq(DB.curriculum_subject().semester, (sem + 1))
                .eq(DB.curriculum_subject().year, this.year_level)
                .active()
                .all();
        //----------------------------------------------------------------------
        if (csMaps == null) {
            logs("NO CS MAPS Found");
            return count;
        }
        //----------------------------------------------------------------------
        System.out.println("SEMESTER " + (sem + 1));
        for (CurriculumSubjectMapping csMap : csMaps) {
            GradeMapping grade = Mono.orm().newSearch(Database.connect().grade())
                    .eq(DB.grade().STUDENT_id, this.CURRENT_STUDENT.getCict_id())
                    .eq(DB.grade().SUBJECT_id, csMap.getSUBJECT_id())
                    .active(Order.desc(DB.grade().id))
                    .first();

            if (grade == null) {
                count++;
                System.out.print("NO GRADE FOR: ");
                System.out.println(csMap.getSUBJECT_id());
                continue; // skip code below
            }

            //------------------------------------------------------------------
            // counted as not defficiency
            boolean isPassed = grade.getRemarks().equalsIgnoreCase("PASSED");
            //boolean isIncomplete = grade.getRemarks().equalsIgnoreCase("INCOMPLETE");
            boolean isEitherOfTwo = (isPassed || false); // only passed is counted
            // counted as defficiency
            if (!isEitherOfTwo) {
                count++;
                System.out.println(csMap.getSUBJECT_id());
            }

        }
        return count;
    }

    private Boolean canEdit = true;

    /**
     * Create a row represented as a semester.
     *
     * @param title
     * @param index
     */
    private void createRow(String title, Integer index) {
        //----------------------------------------------------------------------
        SimpleTableRow row = new SimpleTableRow();
        row.setRowHeight(55.0);
        //----------------------------------------------------------------------
        HBox titleRow = (HBox) Mono.fx().create()
                .setPackageName("org.cict.evaluation.encoder")
                .setFxmlDocument("missing-record-row")
                .makeFX()
                .pullOutLayout();
        //----------------------------------------------------------------------
        Label lbl_title = searchAccessibilityText(titleRow, "title");
        Label lbl_subject = searchAccessibilityText(titleRow, "number");
        Button btn_encode = searchAccessibilityText(titleRow, "encode");
        //----------------------------------------------------------------------
        lbl_title.setText(title);
        lbl_subject.setText(unacquiredCount(index).toString());
        //----------------------------------------------------------------------
        addClickEvent(btn_encode, () -> {
            this.onShowGradeEncoder(index, title);
        });

        addDoubleClickEvent(row, () -> {
            this.onShowGradeEncoder(index, title);
        });
        //----------------------------------------------------------------------
        // Create Row and add to table.
        SimpleTableCell cellParent = new SimpleTableCell();
        cellParent.setResizePriority(Priority.ALWAYS);
        cellParent.setContent(titleRow);
        row.addCell(cellParent);
        recordTable.addRow(row);
    }

    private GradeEncoderController controller;

    //--------------------------------------------------------------------------
    /**
     * Open Grade Encoder.
     *
     * @param index
     * @param selected
     */
    private void onShowGradeEncoder(Integer index, String selected) {
        //----------------------------------------------------------------------
        if (!isOkayToView(index + 1)) {
            return;
        }
        //----------------------------------------------------------------------
        // disable all
        vbox_main.setDisable(true);
        //----------------------------------------------------------------------
        try {
            if (this.FILTERED_SUBJECTS.get(index) == null) {
                Mono.fx().alert().createWarning()
                        .setHeader("No Subject Found")
                        .setMessage("No subject to encode.")
                        .show();
                return;
            }
            //------------------------------------------------------------------
            /**
             * @Request: Please describe what does the following segment does.
             * @Description:
             * @Date: 10/31/2017
             */
            Transaction loadEncoder = new Transaction() {
                @Override
                protected boolean transaction() {
                    controller = new GradeEncoderController(CURRENT_STUDENT,
                            FILTERED_SUBJECTS.get(index),
                            selected);
                    controller.setYearAndSem(year_level, index + 1);
                    return true;
                }
            };
            //------------------------------------------------------------------
            loadEncoder.whenStarted(() -> {
                vbox_main.getScene().setCursor(Cursor.WAIT);
            });
            loadEncoder.whenCancelled(() -> {
            });
            loadEncoder.whenFailed(() -> {
                System.err.println("ERROROROROR");
            });
            loadEncoder.whenSuccess(() -> {
                //--------------------------------------------------------------
                // Launch Encoder
                Mono.fx().create()
                        .setPackageName("org.cict.evaluation.encoder")
                        .setFxmlDocument("GradeEncoder")
                        .makeFX()
                        .setController(controller)
                        .makeScene()
                        .makeStageApplication()
                        .stageMaximized(true)
                        .stageShowAndWait();
                //--------------------------------------------------------------
                // The above code was changed to show and wait
                // to make sure that the encoder is closed before this will be closed
                // and an event to refresh cla will be called upon closing this window
                Mono.fx().getParentStage(btnDone).close();
            });
            loadEncoder.whenFinished(() -> {
                vbox_main.getScene().setCursor(Cursor.DEFAULT);
            });

            loadEncoder.transact();

        } catch (IndexOutOfBoundsException a) {
            Mono.fx().alert()
                    .createWarning()
                    .setHeader("No Selected")
                    .setMessage("Please select one, and click encode.")
                    .showAndWait();
        }
    }

    //--------------------------------------------------------------------------
    /**
     * Checks if its okay to encode in this semester
     *
     * @param semester the selected semester to encode.
     * @return
     */
    private boolean isOkayToView(Integer semester) {
        Integer studentYearLevel = CURRENT_STUDENT.getYear_level();
        Integer currentSemester = current_SEMESTER;
        //----------------------------------------------------------------------
        // if the student is on the same year level as the year that will be encoded
        // e.g. student is first year and encoding in first year.
        if (studentYearLevel.equals(year_level)) {
            if (currentSemester.equals(0)) {
                // this is a non regular term which means
                // first sem and second sem have already passed.
                return true; // allow
            } else if (currentSemester.equals(semester)) {
                // if the student is on the same year level as will be encoded
                // and the current semester is = to the semester that wanted to be encoded.
                // dis allow this as per the evaluation process will insert unposted grades
                // this semester must be clean
                Mono.fx().alert()
                        .createWarning()
                        .setTitle("Restricted")
                        .setHeader("Currently For Evaluation")
                        .setMessage("This semester is reserved for evaluation, encoding for this semester will be allowed in the next academic term. If student already have grades for this semester, you can change the year level and proceed.")
                        .showAndWait();
                return false;
            } else if (semester > currentSemester) {
                Mono.fx().alert()
                        .createWarning()
                        .setTitle("Restricted")
                        .setHeader("Scheduled For Evaluation")
                        .setMessage("This semester is schedule for evaluation for this student in the next semester.")
                        .showAndWait();
                return false;
            } else {
                // return true
                return true;
            }
        } else if (studentYearLevel > year_level) {
            // ok
            return true;
        } else {
            //
            Mono.fx().alert()
                    .createWarning()
                    .setTitle("Restricted")
                    .setHeader("Below Required Year Level")
                    .setMessage("The student is currently below the required year level to encode grades in this semester.")
                    .showAndWait();
            return false;
        }

        //----------------------------------------------------------------------
    }
    //--------------------------------------------------------------------------

    private void onError() {
        Mono.fx().alert()
                .createWarning()
                .setHeader("Something went wrong")
                .setMessage("We cannot process your request this moment. "
                        + "Sorry for the inconvinience.")
                .showAndWait();
    }

    private void startAssessment(String mode) {
        // Student Mapping as constructor parameter.
        CurricularLevelAssesor cla = new CurricularLevelAssesor(CURRENT_STUDENT);
        // call this once only or refresh values from the database for changes.
        // in line Transaction 
        Transaction assessTx = new Transaction() {
            @Override
            protected boolean transaction() {
                /**
                 * since the assess function contains database operations. or if
                 * you think assess function will be loaded fast enough and will
                 * not cause any lags you may not use transaction. transaction
                 * class is intended to remove lags during database activities.
                 */
                cla.assess();
                return true;
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            protected void after() {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };

        assessTx.setOnSuccess(onSuccess -> {
            showValues(cla, mode);
        });
        assessTx.setOnFailure(onFailure -> {
            /**
             * Since there are not return false in the transaction on cancel
             * event will not be invoke only errors may occur.
             */
            System.err.println("ERROROROROR");
        });

        assessTx.transact();
        /**
         * Without transaction just use
         * @code cla.assess(); as linear approach this will be executed in the main thread.
         */

    }

    private ArrayList<SubjectAssessmentDetials> subjectAssessment_UNACQUIRED;

    private void showValues(CurricularLevelAssesor cla, String mode) {
        /**
         * DESCRIPTION
         */
        // checks whether the current curriculum of the student is a consequent
        // when the curriculum is consequent the system assumes that the student
        // have completed a preparatory course
        // hence called prep data
        // this will return an error if the student has no prepdata but enrolled in a consequent curriculum
        boolean prepData = cla.hasPrepData();

        // primary curriculum
        // if the curriculum is not ladderized that curriculum will be here
        // if the curriculum is ladderized but type is prep it will be here
        // if the curriculum is ladderized and type consequent it will be here and the prep curriclum wiil be pushed as prepData
        CurriculumMapping primary = cla.getConsCurriculum();
        // this is always null
        // unless a preparatory curriculum was completed.
        CurriculumMapping secondary = cla.getPrepCurriculum();
        // end description

        // code starts here
        if (cla.hasPrepData()) {
            String text = cla.getPrepCurriculum().getName() + " -> " + cla.getConsCurriculum().getName();
            logs(text);
        } else {
            logs(cla.getConsCurriculum().getName());
        }
        /**
         * You can call getAnnual assessment as many times after calling
         * assess(); if values are changed in the database call assess();
         * function to refresh values before getting annual assessment again
         *
         */
        try {
            AssessmentResults results = cla.getAnnualAssessment(year_level);
            subjectAssessment_UNACQUIRED = results.getUnacquiredSubjects();
            if (subjectAssessment_UNACQUIRED.isEmpty()) {
                isCompleted = true;
            }
        } catch (NullPointerException ne) {
            logs("NO DATA");
        }
        if (mode.equalsIgnoreCase("SKIP")) {
            if (isCompleted) {
                Mono.fx()
                        .alert()
                        .createInfo()
                        .setHeader("Missing Record Completed")
                        .setMessage("All the needed information for this level are encoded.")
                        .showAndWait();
                Mono.fx().getParentStage(btnDone).close();
                return;
            }
            int choice = Mono.fx()
                    .alert()
                    .createConfirmation()
                    .setTitle("Encode Grade")
                    .setHeader("Skip Posting")
                    .setMessage("Are you sure you want to skip posting grade? "
                            + "This can result the evaluation subjects of the student into irregular type.")
                    .confirmCustom("Yes", "No, I'll encode");

            if (choice == 1) {
                Mono.fx().getParentStage(btnDone).close();
            }
        } else if (mode.equalsIgnoreCase("DONE")) {
            if (!isCompleted) {
                Mono.fx()
                        .alert()
                        .createWarning()
                        .setHeader("Incomplete Record")
                        .setMessage("Please supply all the needed grades to proceed."
                                + " Click Skip, if you want to end the process.")
                        .showAndWait();
            } else {
                Mono.fx()
                        .alert()
                        .createInfo()
                        .setHeader("Missing Record Completed")
                        .setMessage("All the needed information for this level are encoded.")
                        .showAndWait();
                Mono.fx().getParentStage(btnDone).close();
            }
        }
    }
}

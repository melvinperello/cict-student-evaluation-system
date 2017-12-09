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
package org.cict.evaluation;

import app.lazy.models.CurriculumMapping;
import app.lazy.models.Database;
import app.lazy.models.MapFactory;
import org.cict.evaluation.assessment.CurricularLevelAssesor;
import app.lazy.models.StudentMapping;
import app.lazy.models.SubjectMapping;
import app.lazy.models.SystemOverrideLogsMapping;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import com.jhmvin.transitions.Animate;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.apache.commons.lang3.text.WordUtils;
import org.cict.PublicConstants;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.cict.authentication.authenticator.SystemProperties;
import org.cict.evaluation.assessment.AssessmentResults;
import org.cict.evaluation.assessment.SubjectAssessmentDetials;
import org.cict.evaluation.encoder.MissingRecordController;
import org.controlsfx.control.Notifications;
import update3.org.cict.access.Access;
import update3.org.cict.access.SystemOverriding;

/**
 *
 * @author Jhon Melvin
 */
public class CurricularLevelController extends SceneFX implements ControllerFX {

    @FXML
    private AnchorPane application_pane;

    @FXML
    private HBox hbox_main_update;

    @FXML
    private Label lbl_year_level;

    @FXML
    private JFXButton btn_change_yr;

    @FXML
    private HBox hbox_update;

    @FXML
    private ComboBox<String> cmb_year_level;

    @FXML
    private JFXButton btn_save_changes;

    @FXML
    private Label lbl_curriculum;

    @FXML
    private HBox hbox_1progress;

    @FXML
    private Label lbl_1_percent;

    @FXML
    private Label lbl_1_subjects;

    @FXML
    private HBox hbox_2_progess;

    @FXML
    private Label lbl_2_percent;

    @FXML
    private Label lbl_2_subjects;

    @FXML
    private HBox hbox_3_progress;

    @FXML
    private Label lbl_3_percent;

    @FXML
    private Label lbl_3_subjects;

    @FXML
    private HBox hbox_4_progress;

    @FXML
    private Label lbl_4_percent;

    @FXML
    private Label lbl_4_subjects;

    @FXML
    private JFXCheckBox chkbx_disable_ai;

    @FXML
    private JFXButton btn_1st;

    @FXML
    private JFXButton btn_2nd;

    @FXML
    private JFXButton btn_3rd;

    @FXML
    private JFXButton btn_4th;

    public CurricularLevelController(StudentMapping studentMap) {
        STUDENT_current = studentMap;
    }

    private final StudentMapping STUDENT_current;
    private final Double BAR_MAX = 780.0;

    @Override
    public void onInitialization() {
        bindScene(application_pane);
        chkbx_disable_ai.setSelected(!PublicConstants.DISABLE_ASSISTANCE);
        this.showAssessment();
        
        //---------------------------------------
        cmb_year_level.getItems().add("First Year");
        cmb_year_level.getItems().add("Second Year");
        cmb_year_level.getItems().add("Third Year");
        cmb_year_level.getItems().add("Fourth Year");
        cmb_year_level.getSelectionModel().select((STUDENT_current.getYear_level()-1));
    
        lbl_year_level.setText(cmb_year_level.getSelectionModel().getSelectedItem());
        
        hbox_main_update.setVisible(true);
        hbox_update.setVisible(false);
    }

    private CurricularLevelAssesor assessmentResults;

    private void showAssessment() {
        // Student Mapping as constructor parameter.
        CurricularLevelAssesor cla = new CurricularLevelAssesor(STUDENT_current);
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
        assessTx.setOnStart(onStart->{
            btn_1st.setDisable(true);
            btn_2nd.setDisable(true);
            btn_3rd.setDisable(true);
            btn_4th.setDisable(true);
        });

        assessTx.setOnSuccess(onSuccess -> {
            assessmentResults = cla;
            showValues(assessmentResults);
            btn_1st.setDisable(false);
            btn_2nd.setDisable(false);
            btn_3rd.setDisable(false);
            btn_4th.setDisable(false);
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

    private ArrayList<SubjectAssessmentDetials> subjectAssessment_UNACQUIRED_1;
    private ArrayList<SubjectAssessmentDetials> subjectAssessment_UNACQUIRED_2;
    private ArrayList<SubjectAssessmentDetials> subjectAssessment_UNACQUIRED_3;
    private ArrayList<SubjectAssessmentDetials> subjectAssessment_UNACQUIRED_4;
    private Boolean is1stYrCompleted = false,
            is2ndYrCompleted = false,
            is3rdYrCompleted = false,
            is4thYrCompleted = false;

    /**
     * Load initial values.
     *
     * @param cla
     */
    private void showValues(CurricularLevelAssesor cla) {
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
            lbl_curriculum.setText(text);
        } else {
            lbl_curriculum.setText(cla.getConsCurriculum().getName());
        }
        /**
         * You can call getAnnual assessment as many times after calling
         * assess(); if values are changed in the database call assess();
         * function to refresh values before getting annual assessment again
         *
         */
        try {
            AssessmentResults firstYr = cla.getAnnualAssessment(1);
            hbox_1progress.setPrefWidth(firstYr.getAcquiredPercentage() * BAR_MAX);
            if ((firstYr.getAcquiredPercentage() * BAR_MAX) >= 100.0) {
                is1stYrCompleted = true;
            }
            String percent_1 = format2Decimal(firstYr.getAcquiredPercentage()) + " ( "
                    + firstYr.getAcquiredUnits().toString() + " / "
                    + firstYr.getTotalUnits().toString() + " unit(s) )";
            lbl_1_percent.setText(percent_1);
            lbl_1_subjects.setText(firstYr.getSubUnacquireCount().toString() + " subjec(s) deficiency.");

            //get the subjects with no grade
            subjectAssessment_UNACQUIRED_1 = firstYr.getAllSubjects();
        } catch (NullPointerException ne) {
            lbl_1_percent.setText("NO DATA");
            lbl_1_subjects.setText("NO DATA");
        }

        try {
            AssessmentResults secYear = cla.getAnnualAssessment(2);
            hbox_2_progess.setPrefWidth(secYear.getAcquiredPercentage() * BAR_MAX);
            if ((secYear.getAcquiredPercentage() * BAR_MAX) >= 100.0) {
                is2ndYrCompleted = true;
            }
            String percent_2 = format2Decimal(secYear.getAcquiredPercentage()) + " % ( "
                    + secYear.getAcquiredUnits().toString() + " / "
                    + secYear.getTotalUnits().toString() + " unit(s) )";
            lbl_2_percent.setText(percent_2);
            lbl_2_subjects.setText(secYear.getSubUnacquireCount().toString() + " subjec(s) deficiency.");

            //get the subjects with no grade
            subjectAssessment_UNACQUIRED_2 = secYear.getAllSubjects();
        } catch (NullPointerException ne) {
            lbl_2_percent.setText("NO DATA");
            lbl_2_subjects.setText("NO DATA");
        }

        try {
            AssessmentResults thirdYr = cla.getAnnualAssessment(3);
            hbox_3_progress.setPrefWidth(thirdYr.getAcquiredPercentage() * BAR_MAX);
            if ((thirdYr.getAcquiredPercentage() * BAR_MAX) >= 100.0) {
                is3rdYrCompleted = true;
            }
            String percent_3 = format2Decimal(thirdYr.getAcquiredPercentage()) + " % ( "
                    + thirdYr.getAcquiredUnits().toString() + " / "
                    + thirdYr.getTotalUnits().toString() + " unit(s) )";
            lbl_3_percent.setText(percent_3);
            lbl_3_subjects.setText(thirdYr.getSubUnacquireCount().toString() + " subjec(s) deficiency.");

            //get the subjects with no grade
            subjectAssessment_UNACQUIRED_3 = thirdYr.getAllSubjects();
        } catch (NullPointerException ne) {
            lbl_3_percent.setText("NO DATA");
            lbl_3_subjects.setText("NO DATA");

        }

        try {
            AssessmentResults fourthYr = cla.getAnnualAssessment(4);
            hbox_4_progress.setPrefWidth(fourthYr.getAcquiredPercentage() * BAR_MAX);
            if ((fourthYr.getAcquiredPercentage() * BAR_MAX) >= 100.0) {
                is4thYrCompleted = true;
            }
            String percent_4 = format2Decimal(fourthYr.getAcquiredPercentage()) + "  ( "
                    + fourthYr.getAcquiredUnits().toString() + " / "
                    + fourthYr.getTotalUnits().toString() + " unit(s) )";
            lbl_4_percent.setText(percent_4);
            lbl_4_subjects.setText(fourthYr.getSubUnacquireCount().toString() + " subjec(s) deficiency.");

            //get the subjects with no grade
            subjectAssessment_UNACQUIRED_4 = fourthYr.getAllSubjects();
        } catch (NullPointerException ne) {
            lbl_4_percent.setText("NO DATA");
            lbl_4_subjects.setText("NO DATA");

        }
    }

    /**
     * Formats string to decimal
     *
     * @param percent
     * @return
     */
    private String format2Decimal(Double percent) {
        percent = percent * 100.0;
        DecimalFormat df = new DecimalFormat("0.00");
        String fomratted = df.format(percent) + " %";
        return fomratted;
    }

    /**
     * On Click Event.
     *
     * @param percent
     * @param level
     */
    private void onClickLevel(Double percent, int level) {
        //------------------------------------------------------------------
        // percentage
        logs(String.valueOf(level) + " YR PERCENT: " + percent);
        //------------------------------------------------------------------
        // check year level
        if (level > STUDENT_current.getYear_level()) {
            Mono.fx().alert()
                    .createWarning()
                    .setTitle("Restricted")
                    .setHeader("Below Required Year Level")
                    .setMessage("The student is currently below the required year level to encode grades.")
                    .showAndWait();
            return;
        }
        //------------------------------------------------------------------
        // completion
        if (percent >= BAR_MAX) {
            logs("COMPLETED");
            Mono.fx().alert()
                    .createInfo()
                    .setHeader("Completed")
                    .setMessage("You already completed this year level.")
                    .showAndWait();
            return;
        }
        //------------------------------------------------------------------
        ArrayList<ArrayList<SubjectMapping>> allSubject = this.getFilteredUnacquiredSubjects(level);
        if (allSubject == null) {
            Mono.fx().alert()
                    .createError()
                    .setHeader("No Subject Found")
                    .setMessage("There are no subjects found for the year level of this curriculum.")
                    .show();
            return;
        }
        ArrayList<String> titles = this.getTitles(level);
        //show missing
        onShowMissingRecord(allSubject, titles, level);
    }

    @Override
    public void onEventHandling() {
        //----------------------------------------------------------------------
        addClickEvent(btn_1st, () -> {
            this.onClickLevel(hbox_1progress.getPrefWidth(), 1);
        });
        //----------------------------------------------------------------------
        addClickEvent(btn_2nd, () -> {
            this.onClickLevel(hbox_2_progess.getPrefWidth(), 2);
        });
        //----------------------------------------------------------------------
        addClickEvent(btn_3rd, () -> {
            this.onClickLevel(hbox_3_progress.getPrefWidth(), 3);
        });
        //----------------------------------------------------------------------
        addClickEvent(btn_4th, () -> {
            this.onClickLevel(hbox_4_progress.getPrefWidth(), 4);
        });
        //----------------------------------------------------------------------
//        Mono.fx().key(KeyCode.ENTER).release(application_pane, () -> {
//            Mono.fx().getParentStage(application_pane).close();
//        });
        chkbx_disable_ai.selectedProperty().addListener((a)->{
            PublicConstants.DISABLE_ASSISTANCE = !chkbx_disable_ai.isSelected();
        });
        
        this.changeYearLevelEvents();
    }
    
    //--------------------------------------
    private Integer index;
    private void changeYearLevelEvents() {
        
        this.addClickEvent(btn_change_yr, ()-> {
            PublicConstants.DISABLE_ASSISTANCE = chkbx_disable_ai.isSelected();
            Animate.fade(hbox_main_update, 150, ()->{
                hbox_main_update.setVisible(false);
                hbox_update.setVisible(true);
            }, hbox_update);
        });
        
        this.addClickEvent(btn_save_changes, ()->{
            PublicConstants.DISABLE_ASSISTANCE = chkbx_disable_ai.isSelected();
            index = cmb_year_level.getSelectionModel().getSelectedIndex();
            Integer studentYrLvl = STUDENT_current.getYear_level();
            String message = "";
            boolean invalid = false;
            index+=1;
            if(index < studentYrLvl) {
                invalid = true;
                message = "We prohibit changing the year level of\n"
                        + "the student backward. Please ask your local\n"
                        + "registrar for assistance.";
            } else if(index.equals(studentYrLvl+1)) {
                // valid
            }else if(index > studentYrLvl) {
                invalid = true;
                message = "Changing the year level twice or more\n"
                        + "is not allowed, just one step at a time.\n"
                        + "If that so, please ask your local registrar for assistance.";
            }
            if(invalid) {
                Notifications.create().title("Warning")
                        .position(Pos.BOTTOM_RIGHT)
                        .text(message
                                + "\nClick This notification for more details.")
                        .onAction(onAction -> {
                            this.systemOverride(SystemOverriding.EVAL_CHANGED_YEAR_LEVEL_BACKWARD);
                        })
                        .showWarning();
//                Mono.fx().alert()
//                        .createWarning()
//                        .setHeader("Ops, Invalid Move")
//                        .setMessage(message)
//                        .show();
                return;
            }
            updatedYearLevel();
        });
    }

    private boolean allowOverride = false;
    private void systemOverride(String type) {
        Object[] result = Access.isEvaluationOverride(allowOverride, SystemOverriding.getACRONYM(15, type));
        boolean ok = (boolean) result[0];
        String fileName = (String) result[1];
        if (ok) {
            SystemOverrideLogsMapping map = MapFactory.map().system_override_logs();
            map.setCategory(SystemOverriding.CATEGORY_EVALUATION);
            map.setDescription(type);
            map.setExecuted_by(CollegeFaculty.instance().getFACULTY_ID());
            map.setExecuted_date(Mono.orm().getServerTime().getDateWithFormat());
            map.setAcademic_term(SystemProperties.instance().getCurrentAcademicTerm().getId());
            String conforme = STUDENT_current.getLast_name() + ", ";

            conforme += STUDENT_current.getFirst_name();
            if (STUDENT_current.getMiddle_name() != null) {
                conforme += " ";
                conforme += STUDENT_current.getMiddle_name();
            }
            map.setConforme(conforme);
            map.setConforme_type("STUDENT");
            map.setConforme_id(STUDENT_current.getCict_id());

            //-----------------
            map.setAttachment_file(fileName);
            //------------
                
            int id = Database.connect().system_override_logs().insert(map);
            if (id <= 0) {
                Mono.fx().snackbar().showError(application_pane, "Something went wrong please try again.");
            } else {
                this.updatedYearLevel();
            }
        }
    }
    
    private boolean updatedYearLevel() {
        String yearLevel = cmb_year_level.getSelectionModel().getSelectedItem();
        Animate.fade(hbox_update, 150, ()->{
            hbox_update.setVisible(false);
            hbox_main_update.setVisible(true);
        }, hbox_main_update);

        if(Objects.equals(index, STUDENT_current.getYear_level())) {
            return false;
        }
        lbl_year_level.setText(yearLevel);
        STUDENT_current.setYear_level(index);
        if(!Database.connect().student().update(STUDENT_current)) {
            System.out.println("NOT SAVED");
            Notifications.create().title("Failed")
                    .text("Please check your connection to the server.").showError();
            return false;
        }
        return true;
    }
    
    
    //------------------------------------------------
    private void logs(String str) {
        if (false) {
            System.out.println("@CurricularLevelController: " + str);
        }
    }

    private ArrayList<String> getTitles(Integer yearLevel) {
        ArrayList<String> titles = new ArrayList<>();
        switch (yearLevel) {
            case 1:
                titles.add("FIRST YEAR - First Semester");
                titles.add("FIRST YEAR - Second Semester");
                return titles;
            case 2:
                titles.add("SECOND YEAR - First Semester");
                titles.add("SECOND YEAR - Second Semester");
                return titles;
            case 3:
                titles.add("THIRD YEAR - First Semester");
                titles.add("THIRD YEAR - Second Semester");
                return titles;
            case 4:
                titles.add("FOURTH YEAR - First Semester");
                titles.add("FOURTH YEAR - Second Semester");
                return titles;
        }
        return null;
    }

    private ArrayList<ArrayList<SubjectMapping>> getFilteredUnacquiredSubjects(Integer yearLevel) {
        ArrayList<ArrayList<SubjectMapping>> allSubject = new ArrayList<>();
        ArrayList<SubjectMapping> temp_list;
        ArrayList<SubjectAssessmentDetials> selectedYearAssessemnt = null;
        switch (yearLevel) {
            case 1:
                selectedYearAssessemnt = subjectAssessment_UNACQUIRED_1;
                break;
            case 2:
                selectedYearAssessemnt = subjectAssessment_UNACQUIRED_2;
                break;
            case 3:
                selectedYearAssessemnt = subjectAssessment_UNACQUIRED_3;
                break;
            case 4:
                selectedYearAssessemnt = subjectAssessment_UNACQUIRED_4;
                break;
        }
        for (int ctrSem = 1; ctrSem <= 2; ctrSem++) {
            temp_list = new ArrayList<>();
            if (selectedYearAssessemnt == null) {
                return null;
            }
            for (SubjectAssessmentDetials subAssessmentDetail : selectedYearAssessemnt) {
                if (subAssessmentDetail.getSemester() == 1) {
                    if (ctrSem == 1) {
                        temp_list.add(subAssessmentDetail.getSubjectDetails());
                        logs("ADDED: " + subAssessmentDetail.getSubjectDetails().getCode());
                    }
                }
                if (subAssessmentDetail.getSemester() == 2) {
                    if (ctrSem == 2) {
                        temp_list.add(subAssessmentDetail.getSubjectDetails());
                        logs("ADDED: " + subAssessmentDetail.getSubjectDetails().getCode());
                    }
                }
            }
            allSubject.add(temp_list);
            logs("**************************************");
        }
        return allSubject;
    }

    /**
     * Shows the missing records of the student.
     *
     * @param subjects
     * @param titles
     * @param yearLevel
     */
    private void onShowMissingRecord(ArrayList<ArrayList<SubjectMapping>> subjects, ArrayList<String> titles, Integer yearLevel) {
        try {
            //------------------------------------------------------------------
            // create Missing Records Instance
            MissingRecordController controller = new MissingRecordController(this.STUDENT_current,
                    subjects, titles, yearLevel);
            //------------------------------------------------------------------
            // show GUI and wait until it closes
            Mono.fx().create()
                    .setPackageName("org.cict.evaluation.encoder")
                    .setFxmlDocument("missing_record")
                    .makeFX()
                    .setController(controller)
                    .makeScene()
                    .makeStageWithOwner(Mono.fx().getParentStage(application_pane))
                    .stageResizeable(false)
                    .stageShowAndWait();
            //------------------------------------------------------------------
            // refresh values
            // the above code was replaced to show and wait
            // when the encoder was closed the missing record will also be closed
            // and then will refresh this assessors values.
            this.showAssessment();
        } catch (Exception e) {
            System.err.println("09.13.2017 This error already occured");
            e.printStackTrace();
        }

    }

    @Deprecated
    private void setYearLevel(Integer year) {
        STUDENT_current.setYear_level(year);
        if (!Database.connect().student().update(STUDENT_current)) {
            System.out.println("STUDENT NOT UPDATED");
        }
    }
}

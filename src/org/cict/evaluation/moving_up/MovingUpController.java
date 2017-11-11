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
package org.cict.evaluation.moving_up;

import app.lazy.models.CurriculumMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.GradeMapping;
import app.lazy.models.MapFactory;
import app.lazy.models.StudentMapping;
import app.lazy.models.SystemOverrideLogsMapping;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import com.melvin.mono.fx.bootstrap.M;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.cict.GenericLoadingShow;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.cict.authentication.authenticator.SystemProperties;
import org.cict.evaluation.assessment.AssessmentResults;
import org.cict.evaluation.assessment.CurricularLevelAssesor;
import org.cict.evaluation.assessment.SubjectAssessmentDetials;
import org.controlsfx.control.Notifications;
import org.hibernate.Session;
import update3.org.cict.access.Access;
import update3.org.cict.access.SystemOverriding;

/**
 *
 * @author Joemar
 */
public class MovingUpController extends SceneFX implements ControllerFX {

    @FXML
    private VBox application_root;

    @FXML
    private VBox vbox_list;

    private StudentMapping student;
    private ArrayList<CurriculumMapping> results;
    public MovingUpController(StudentMapping student, ArrayList<CurriculumMapping> results) {
        this.student = student;
        this.results = results;
    }
    
    @Override
    public void onInitialization() {
        this.createTable();
    }

    @Override
    public void onEventHandling() {
    
    }
    
    private final String KEY_MORE_INFO = "MORE_INFO";
    private void createTable() {
        SimpleTable tblFaculty = new SimpleTable();
        for(CurriculumMapping each: results) {
            SimpleTableRow row = new SimpleTableRow();
            row.setRowHeight(70.0);
            CurriculumRow rowFX = M.load(CurriculumRow.class);
            Label lbl_name = rowFX.getLbl_curriculum_name();
            Label lbl_major = rowFX.getLbl_major();
            JFXButton btn_select = rowFX.getBtn_select();
            
            lbl_name.setText(each.getName());
            lbl_major.setText((each.getMajor().isEmpty()? "NONE": each.getMajor()));
            
            super.addClickEvent(btn_select, ()->{
                this.showAssessment(row);
            });
            
            row.getRowMetaData().put(KEY_MORE_INFO, each);
            SimpleTableCell cellParent = new SimpleTableCell();
            cellParent.setResizePriority(Priority.ALWAYS);
            cellParent.setContentAsPane(rowFX.getApplicationRoot());

            row.addCell(cellParent);

            /**
             * Add to table.
             */
            tblFaculty.addRow(row);
        }
        
        // table view
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(tblFaculty);
        simpleTableView.setFixedWidth(true);
        // attach to parent variable name in scene builder
        simpleTableView.setParentOnScene(vbox_list);
    }
    
    private CurricularLevelAssesor assessmentResults;

    private void showAssessment(SimpleTableRow row) {
        // Student Mapping as constructor parameter.
        CurricularLevelAssesor cla = new CurricularLevelAssesor(student);
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
            assessmentResults = cla;
            showValues(assessmentResults, row);
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
    /**
     * Load initial values.
     *
     * @param cla
     */
    private void showValues(CurricularLevelAssesor cla, SimpleTableRow row) {
        // if local registrar allow override.
        allowOverride = Access.isGrantedIf(Access.ACCESS_LOCAL_REGISTRAR);

        /**
         * You can call getAnnual assessment as many times after calling
         * assess(); if values are changed in the database call assess();
         * function to refresh values before getting annual assessment again
         *
         */
        try {
            AssessmentResults firstYr = cla.getAnnualAssessment(1);
            //get the subjects with no grade
            subjectAssessment_UNACQUIRED_1 = firstYr.getUnacquiredSubjects();
        } catch (NullPointerException ne) {
        }

        try {
            AssessmentResults secYear = cla.getAnnualAssessment(2);
            //get the subjects with no grade
            subjectAssessment_UNACQUIRED_2 = secYear.getUnacquiredSubjects();
        } catch (NullPointerException ne) {
        }
        
        if(subjectAssessment_UNACQUIRED_1.isEmpty() && subjectAssessment_UNACQUIRED_2.isEmpty()) {
            System.out.println("GRADES ARE ALL COMPLETED");
            this.save(row);
        } else {
            this.checkUser(row);
        }
    }
    
    /**
     * Override filters.
     */
    private boolean allowOverride = false;
    private final String GRADE_REQUIREMENT = SystemOverriding.EVAL_GRADE_REQUIREMENT_FOR_MOVING_UP;

    private void checkUser(SimpleTableRow row) {
        Notifications.create()
                .title("Warning")
                .text("Moving up requires all the subjects"
                    + "\nof the current curriculum into passed"
                        + "\nremarks. Click here for more info.")
                .onAction(a -> {
                    this.systemOverride(this.GRADE_REQUIREMENT, row);
                })
                .position(Pos.BOTTOM_RIGHT).showWarning();
    }
    
    
    private void systemOverride(String type, SimpleTableRow row) {
        Object[] res = Access.isEvaluationOverride(allowOverride);
        boolean ok = (boolean) res[0];
        String fileName = (String) res[1];
        if (ok) {
            SystemOverrideLogsMapping map = MapFactory.map().system_override_logs();
            map.setCategory(SystemOverriding.CATEGORY_EVALUATION);
            map.setDescription(type);
            map.setExecuted_by(CollegeFaculty.instance().getFACULTY_ID());
            map.setExecuted_date(Mono.orm().getServerTime().getDateWithFormat());
            map.setAcademic_term(SystemProperties.instance().getCurrentAcademicTerm().getId());
            String conforme = student.getLast_name() + ", ";

            conforme += student.getFirst_name();
            if (student.getMiddle_name() != null) {
                conforme += " ";
                conforme += student.getMiddle_name();
            }
            map.setConforme(conforme);
            map.setConforme_type("STUDENT");
            map.setConforme_id(student.getCict_id());

            map.setAttachment_file(fileName);
            
            int id = Database.connect().system_override_logs().insert(map);
            if (id <= 0) {
                Mono.fx().snackbar().showError(application_root, "Something went wrong please try again.");
            } else {
                System.out.println("SAVE HERE!!");
                this.save(row);
            }
            
        }
    }
    
    private boolean isSaved = false;
    public boolean isSaved() {
        return isSaved;
    }
    
    private void save(SimpleTableRow row) {
        int res = Mono.fx().alert().createConfirmation()
                .setHeader("Save Selected")
                .setMessage("This will no longer be altered or edited. Are you sure you want to save the selected curriculum?")
                .confirmCustom("Yes, please.", "No, I'll check it again.");
        if(res!=1) {
            return;
        }
        CurriculumMapping selected_cur = (CurriculumMapping) row.getRowMetaData().get(KEY_MORE_INFO);
        SaveMovingUp save = new SaveMovingUp();
        save.student = this.student;
        save.old_curriculum_id = student.getCURRICULUM_id();
        save.new_selected = selected_cur.getId();
        save.whenStarted(()->{
            GenericLoadingShow.instance().show();
        });
        save.whenCancelled(()->{
            GenericLoadingShow.instance().hide();
            Notifications.create()
                    .title("Something went wrong.")
                    .text(save.getLog())
                    .showError();
        });
        save.whenSuccess(()->{
            GenericLoadingShow.instance().hide();
            Mono.fx().alert().createInfo()
                .setHeader("Success!")
                .setMessage("Student and its curriculum is successfully updated.")
                    .show();
            isSaved = true;
            Mono.fx().getParentStage(application_root).close();
        });
        save.transact();
    }
    
    public class SaveMovingUp extends Transaction {

        public StudentMapping student;
        public Integer old_curriculum_id;
        public Integer new_selected;
        
        private String log;
        public String getLog() {
            return log;
        }
        
        @Override
        protected boolean transaction() {
            // create local session
            Session currentSession = Mono.orm().session();
            // start your transaction
            org.hibernate.Transaction dataTransaction = currentSession.beginTransaction();
            ArrayList<GradeMapping> grades = Mono.orm().newSearch(Database.connect().grade())
                    .eq(DB.grade().STUDENT_id, this.student.getCict_id())
                    .active().all();
            if(grades!=null) {
                for(GradeMapping grade: grades) {
                    grade.setReferrence_curriculum(old_curriculum_id);
                    boolean updated = Database.connect().grade().transactionalSingleUpdate(currentSession, grade);
                    
                    if (!updated) {
                        // if errors occured during temporary insert
                        dataTransaction.rollback();
                        log("grade update failed");
                        log = "Curriculum update failed.";
                        // cannot insert transaction failed
                        return false;
                    }
                }
            } else
                log("No grade is updated.");
            student.setPREP_id(old_curriculum_id);
            student.setCURRICULUM_id(new_selected);
            boolean updated = Database.connect().student().transactionalSingleUpdate(currentSession, this.student);

            if (!updated) {
                // if errors occured during temporary insert
                dataTransaction.rollback();
                log("student update failed");
                log = "Student update failed.";
                // cannot insert transaction failed
                return false;
            }

            log("student successfully updated");
            dataTransaction.commit();
            return true;
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        protected void after() {
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        private void log(String log) {
            if(true)
                System.out.println("@MovingUpController: " + log);
        }
    }
}

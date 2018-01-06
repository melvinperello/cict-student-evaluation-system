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
package org.cict.evaluation.retention;

import app.lazy.models.CurriculumMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.RetentionPolicyMapping;
import app.lazy.models.RetentionSubjectMapping;
import app.lazy.models.StudentMapping;
import app.lazy.models.utils.FacultyUtility;
import com.itextpdf.text.Document;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import com.melvin.mono.fx.MonoLauncher;
import com.melvin.mono.fx.bootstrap.M;
import com.melvin.mono.fx.events.MonoClick;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.text.WordUtils;
import org.cict.PublicConstants;
import org.cict.evaluation.FailedGradeChecker;
import org.cict.reports.ReportsDirectory;
import org.cict.reports.ReportsUtility;
import org.cict.reports.retention.RetentionLetter;
import org.hibernate.criterion.Order;

/**
 *
 * @author Joemar
 */
public class RetentionRowDetails extends MonoLauncher{

    @FXML
    private VBox application_root;

    @FXML
    private JFXButton btn_close;

    @FXML
    private Label lbl_section;

    @FXML
    private Label lbl_faculty;

    @FXML
    private Label lbl_date;

    @FXML
    private Label lbl_acad_year;

    @FXML
    private Label lbl_acad_semester;

    @FXML
    private Label lbl_curriculum_id;

    @FXML
    private Label lbl_name;

    @FXML
    private Label lbl_major;

    @FXML
    private Label lbl_ladder_type;

    @FXML
    private VBox vbox_table_subjects;
    
    @FXML
    private JFXButton btn_reprint;
    
    @Override
    public void onStartUp() {
        MonoClick.addClickEvent(btn_close, ()->{
            Mono.fx().getParentStage(btn_close).close();
        });
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private CurriculumMapping curriculum;
    private RetentionPolicyMapping retentionPolicy;
    private StudentMapping student;

    public void setStudent(StudentMapping student) {
        this.student = student;
    }

    public void setCurriculum(CurriculumMapping curriculum) {
        this.curriculum = curriculum;
    }

    public void setRetentionPolicy(RetentionPolicyMapping retentionPolicy) {
        this.retentionPolicy = retentionPolicy;
    }
    
    @Override
    public void onDelayedStart() {
        this.lbl_acad_semester.setText(retentionPolicy.getAcademic_semester());
        this.lbl_acad_year.setText(retentionPolicy.getAcademic_year());
        this.lbl_curriculum_id.setText(curriculum.getId().toString());
        this.lbl_date.setText(retentionPolicy.getVerification_date().toString());
        this.lbl_faculty.setText(FacultyUtility.getFacultyName(FacultyUtility.getFaculty(retentionPolicy.getVerified_by())));
        this.lbl_ladder_type.setText(curriculum.getLadderization_type());
        this.lbl_major.setText(curriculum.getMajor());
        this.lbl_name.setText(curriculum.getName());
        this.lbl_section.setText(retentionPolicy.getSection());
        
        ArrayList<RetentionSubjectMapping> retentionSubjects = Mono.orm().newSearch(Database.connect().retention_subject())
                .eq(DB.retention_subject().retention_id, retentionPolicy.getId())
                .active(Order.asc(DB.retention_subject().id)).all();
        if(retentionSubjects!=null || !retentionSubjects.isEmpty()) {
            for(RetentionSubjectMapping eachSubject: retentionSubjects){
                System.out.println(eachSubject.getSubject_code() + " | " + eachSubject.getSubject_title());
            }
        }
        
        this.createSubjectRow(retentionSubjects);
        
        MonoClick.addClickEvent(btn_reprint, ()->{
            this.printeLetter(FailedGradeChecker.numNames[retentionSubjects.size()] + " (" + retentionSubjects.size() + ")");
        });
        super.onDelayedStart(); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void createSubjectRow(ArrayList<RetentionSubjectMapping> retentionSubjects) {
        SimpleTable tblRetentionSubject = new SimpleTable();
        if(retentionSubjects!=null || !retentionSubjects.isEmpty()) {
            for(RetentionSubjectMapping eachSubject: retentionSubjects){
                SimpleTableRow row = new SimpleTableRow();
                row.setRowHeight(50.0);
                
                RetentionDetailsSubjectRow rowFX = M.load(RetentionDetailsSubjectRow.class);
                rowFX.getLbl_code().setText(eachSubject.getSubject_code());
                rowFX.getLbl_title().setText(eachSubject.getSubject_title());
                rowFX.getLbl_units().setText(eachSubject.getUnits());
                
                SimpleTableCell cellParent = new SimpleTableCell();
                cellParent.setResizePriority(Priority.ALWAYS);
                cellParent.setContentAsPane(rowFX.getApplicationRoot());

                row.addCell(cellParent);
                tblRetentionSubject.addRow(row);
            }
            
            SimpleTableView simpleTableView = new SimpleTableView();
            simpleTableView.setTable(tblRetentionSubject);
            simpleTableView.setFixedWidth(true);
            simpleTableView.setParentOnScene(vbox_table_subjects);
        }
    }
    
    public final static String SAVE_DIRECTORY = "reports/retention";
    private void printeLetter(String failedGrades) {
        String doc = "Retention Letter For " + student.getId() + "_" + Mono.orm().getServerTime().getCalendar().getTimeInMillis();

        String RESULT = SAVE_DIRECTORY + "/" + doc + ".pdf";

        //------------------------------------------------------------------------------
        //------------------------------------------------------------------------------
        /**
         * Check if the report save directory is already existing and created if
         * not this will try to create the needed directories.
         */
        boolean isCreated = ReportsDirectory.check(SAVE_DIRECTORY);

        if (!isCreated) {
            // some error message that the directory is not created
            System.err.println("Directory is not created.");
            return;
        }
        //------------------------------------------------------------------
        //------------------------------------------------------------------
        RetentionLetter retention = new RetentionLetter(RESULT);
        retention.STUDENT_NAME = student.getFirst_name() + (student.getMiddle_name()==null? "" : " " + WordUtils.initials(student.getMiddle_name())) + " " + student.getLast_name();
        retention.STUDENT_SECTION = retentionPolicy.getSection();
        retention.DEAN = PublicConstants.getSystemVar_Noted_By().toString();
        retention.SERVER_DATE = retentionPolicy.getVerification_date();
        retention.NUMBER_OF_FAILED_SUBJECTS = failedGrades;
        retention.PREV_SCHOOL_YEAR = this.retentionPolicy.getPrev_school_year();
        retention.PREV_SEMESTER = (retentionPolicy.getPrev_semester().equals("FIRST SEMESTER")? "1st" : (retentionPolicy.getPrev_semester().equalsIgnoreCase("SECOND SEMESTER")? "2nd" : retentionPolicy.getPrev_semester()));
        
        String localReg1 = PublicConstants.getSystemVar_LocalRegistrar1().toString();
        String localReg2 = PublicConstants.getSystemVar_LocalRegistrar2().toString();
        if(!localReg1.isEmpty() && !localReg2.isEmpty()) {
            retention.SENDER_NAMES = new String[] {localReg1, localReg2};
        } else if(!localReg1.isEmpty()) {
            retention.SENDER_NAMES = new String[] {localReg1};
        } else if(!localReg2.isEmpty()) {
            retention.SENDER_NAMES = new String[] {localReg2};
        } else {
            retention.SENDER_NAMES = new String[] {};
        }
        Document document = ReportsUtility.paperSizeChooser(this.getCurrentStage());
        if(document==null) {
            return;
        }
        retention.setDocumentFormat(document);
        retention.print();
    }
}

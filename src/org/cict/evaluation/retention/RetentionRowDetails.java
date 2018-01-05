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
import app.lazy.models.utils.FacultyUtility;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.melvin.mono.fx.MonoLauncher;
import com.melvin.mono.fx.events.MonoClick;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
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
    
    @Override
    public void onStartUp() {
        MonoClick.addClickEvent(btn_close, ()->{
            Mono.fx().getParentStage(btn_close).close();
        });
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private CurriculumMapping curriculum;
    private RetentionPolicyMapping retentionPolicy;

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
        super.onDelayedStart(); //To change body of generated methods, choose Tools | Templates.
    }
    
}

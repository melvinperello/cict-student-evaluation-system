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
package org.cict.evaluation;

import app.lazy.models.Database;
import app.lazy.models.StudentMapping;
import app.lazy.models.SubjectMapping;
import com.jhmvin.Mono;
import com.jhmvin.fx.display.ControllerFX;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import org.cict.evaluation.encoder.MissingRecordController;
import org.cict.evaluation.evaluator.CheckGrade;
import org.cict.evaluation.evaluator.Evaluator;

/**
 *
 * @author Joemar
 */
public class EnrollmentTypePickerController implements ControllerFX {

    @FXML
    private ComboBox cmbEtype;

    @FXML
    private Button btnSave;
    
    private final StudentMapping CURRENT_STUDENT;
//    private final String OLD = "OLD";
//    private final String NEW = "NEW";
    private final String REGULAR = "REGULAR";
    private final String IRREGULAR = "IRREGULAR";
    private String choseEnrollmentType;
    private ArrayList<ArrayList<SubjectMapping>> subjectsWithNoGrade;
    private ArrayList<String> yearAndSemWithMissingRecord;
    
    public EnrollmentTypePickerController(StudentMapping student, ArrayList<String> yearAndSemWithMissingRecord){
        this.yearAndSemWithMissingRecord = yearAndSemWithMissingRecord;
        this.CURRENT_STUDENT = student;
    }
    
    @Override
    public void onInitialization() {
        this.setComboBox(cmbEtype);
    }

    @Override
    public void onEventHandling() {
        this.btnSave.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
//            this.onSave();
        });
    }
    
//    private void onSave(){
//        //store the selected enrollmemt type
//        this.choseEnrollmentType = this.cmbEtype.getSelectionModel()
//                                                .getSelectedItem()
//                                                .toString()
//                                                .toUpperCase();
//        //check if grades are complete up to the current semester
//        CheckGrade checkGrade = Evaluator.instance().createGradeChecker();
//        checkGrade.curriculumId = this.CURRENT_STUDENT.getCURRICULUM_id();
//        checkGrade.studentId = this.CURRENT_STUDENT.getCict_id();
//        checkGrade.studentYearLevel = this.CURRENT_STUDENT.getYear_level();
//
//        checkGrade.setOnStart(onStart -> {
//            this.btnSave.setDisable(true);
//        });
//        checkGrade.setOnSuccess(success -> {
//            //if not, show grade encoder
//            this.subjectsWithNoGrade = checkGrade.getSubjectsWithNoGrade();
//            System.out.println("@EnrollmentTypePickerController: subjectsWithNoGrade.size() " + subjectsWithNoGrade.size());
//            if(this.choseEnrollmentType.equalsIgnoreCase(this.REGULAR)) {
//                //regular
//                if(subjectsWithNoGrade.size() > 0){
//                    Mono.fx().alert()
//                            .createInfo()
//                            .setHeader("Missing Grade")
//                            .setMessage("There are some missing record(s) of the student."
//                                    + " Let's now supply them.")
//                            .showAndWait();
////                    this.onShowMissingRecord();
//                } else {
//                    this.CURRENT_STUDENT.setEnrollment_type(this.choseEnrollmentType);
//                    if(Database.connect().student().update(this.CURRENT_STUDENT))
//                        System.out.println("@EnrollmentTypePickerController: Student updated process success");
//                }
//            } else {
//                //irregular
//                this.CURRENT_STUDENT.setEnrollment_type(this.IRREGULAR);
//                if(Database.connect().student().update(this.CURRENT_STUDENT))
//                    System.out.println("@EnrollmentTypePickerController: Student updated process success");
//                
//            }
//            Mono.fx().getParentStage(btnSave).close();
//        });
//        
//        checkGrade.setOnCancel(onCancel -> {
//            this.onError();
//        });
//        checkGrade.setOnFailure(failure -> {
//            this.onError();
//        });
//        checkGrade.transact();
//    }
     
    private void setComboBox(ComboBox cmb_questions){
        ArrayList<String> questions = new ArrayList<>();
//        questions.add("Old");
//        questions.add("New");
        questions.add("Regular");
        questions.add("Irregular");
        cmb_questions.getItems().addAll(questions);
        cmb_questions.getSelectionModel().selectFirst();
    }
    
    private void onError(){
        Mono.fx().alert()
                .createWarning()
                .setHeader("Enrollment Type Picker")
                .setMessage("We cannot process your request this moment. "
                        + "Sorry for the inconvinience.")
                .showAndWait();
    }
    
//    private void onShowMissingRecord() {
//        System.out.println("@EvaluateController: Show Missing Records Controller");
//        MissingRecordController controller = new MissingRecordController(this.CURRENT_STUDENT, this.subjectsWithNoGrade, this.yearAndSemWithMissingRecord);
//        //onShowMissingRecords
//        Mono.fx().create()
//                .setPackageName("org.cict.evaluation.encoder")
//                .setFxmlDocument("missing_record")
//                .makeFX()
//                .setController(controller)
//                .makeScene()
//                .makeStageWithOwner(Mono.fx().getParentStage(btnSave))
//                .stageResizeable(false)
//                .stageShowAndWait();
//    }
}

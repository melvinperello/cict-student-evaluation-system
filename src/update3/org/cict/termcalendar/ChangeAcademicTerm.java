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
package update3.org.cict.termcalendar;

import app.lazy.models.AcademicTermMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import artifacts.MonoString;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.melvin.mono.fx.MonoLauncher;
import com.melvin.mono.fx.events.MonoClick;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 *
 * @author Joemar
 */
public class ChangeAcademicTerm extends MonoLauncher {

    @FXML
    private VBox application_root;

    @FXML
    private TextField txt_school_year;

    @FXML
    private ComboBox<String> cmb_semester;

    @FXML
    private JFXButton btn_send;

    @FXML
    private JFXButton btn_cancel;
    
    @Override
    public void onStartUp() {
        cmb_semester.getItems().clear();
        cmb_semester.getItems().add("First Semester");
        cmb_semester.getItems().add("Second Semester");
        cmb_semester.getItems().add("Midyear");
        cmb_semester.getSelectionModel().selectFirst();
        
        MonoClick.addClickEvent(btn_cancel, ()->{
            int res = Mono.fx().alert().createConfirmation()
                    .setMessage("Are you sure you want to cancel?")
                    .confirmYesNo();
            if(res==1) {
                Mono.fx().getParentStage(btn_send).close();
            }
        });
    }
    

    @Override
    public void onDelayedStart() {
        
        MonoClick.addClickEvent(btn_send, ()->{
            this.sendRequest();
        });
        
        super.onDelayedStart(); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void sendRequest() {
        String schoolYear = MonoString.removeExtraSpace(txt_school_year.getText());
        if(schoolYear.isEmpty()) {
            Mono.fx().alert().createWarning()
                    .setMessage("Academic Year must have a value.")
                    .show();
            return;
        }
        String semester = MonoString.removeExtraSpace(cmb_semester.getSelectionModel().getSelectedItem());
        if(semester.isEmpty()) {
            Mono.fx().alert().createWarning()
                    .setMessage("Semester must have a value.")
                    .show();
            return;
        }
        String type = "REGULAR";
        Integer semesterRegular = cmb_semester.getSelectionModel().getSelectedIndex()+1;
        if(semester.equalsIgnoreCase("MIDYEAR")) {
            type = "MIDYEAR";
            semesterRegular = 0;
        }
        
//        AcademicTermMapping exist = Mono.orm().newSearch(Database.connect().academic_term())
//                .eq(DB.academic_term().school_year, schoolYear)
//                .eq(DB.academic_term().semester, semester)
//                .active().first();
//        int res;
//        if(exist!=null) {
//            res = Mono.fx().alert().createConfirmation()
//                    .setMessage("The details entered are already existing. Do you still want to send the request?")
//                    .confirmYesNo();
//            if(res==-1)
//                return;
//        } else {
//            res = Mono.fx().alert().createConfirmation()
//                    .setMessage("This request may take time to be approved and implemented. Do you still want to continue?")
//                    .confirmYesNo();
//            if(res==-1)
//                return;
//        }
        
        AcademicTermMapping atMap = new AcademicTermMapping();
        atMap.setActive(1);
        atMap.setApproval_state("PENDING");
        atMap.setSchool_year(schoolYear.toUpperCase());
        atMap.setSemester(semester.toUpperCase());
        atMap.setType(type);
        atMap.setSemester_regular(semesterRegular);
        Integer id = Database.connect().academic_term().insert(atMap);
        if(id.equals(-1)) {
            Mono.fx().alert().createError()
                    .setHeader("Request Failed")
                    .setMessage("Please try again later.")
                    .show();
        } /*else {
            Mono.fx().alert().createInfo()
                    .setHeader("Request Sent")
                    .setMessage("Wait until the Local Registrar take action on your request.")
                    .show();
        }*/
        Mono.fx().getParentStage(btn_send).close();
    }
}

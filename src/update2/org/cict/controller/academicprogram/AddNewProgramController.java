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
package update2.org.cict.controller.academicprogram;

import app.lazy.models.AcademicProgramMapping;
import app.lazy.models.CurriculumMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import artifacts.MonoString;
import com.izum.fx.textinputfilters.StringFilter;
import com.izum.fx.textinputfilters.TextInputFilters;
import com.jhmvin.Mono;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.LayoutDataFX;
import com.jhmvin.fx.display.SceneFX;
import com.jhmvin.transitions.Animate;
import java.util.ArrayList;
import java.util.Date;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.hibernate.criterion.Order;

/**
 *
 * @author Joemar
 */
public class AddNewProgramController extends SceneFX implements ControllerFX{

    @FXML
    private VBox vbox_main;
    
    @FXML
    private TextField txt_progcode;

    @FXML
    private TextField txt_progname;

    @FXML
    private TextField txt_floor;
    
    @FXML
    private Button btn_add;

    @FXML
    private Button btn_cancel;
    
    private ArrayList<String> validFloorAssignment = new ArrayList<>();
    private Integer CREATED_BY = CollegeFaculty.instance().getFACULTY_ID();
    private Date CREATED_DATE = Mono.orm().getServerTime().getDateWithFormat();
    
    @Override
    public void onInitialization() {
        this.bindScene(vbox_main);
        validFloorAssignment.add("3");
        validFloorAssignment.add("4");
        addTextFieldFilters();
    }
    
    private void addTextFieldFilters() {
        StringFilter textField = TextInputFilters.string()
//                .setFilterMode(StringFilter.LETTER_DIGIT_SPACE)
                .setMaxCharacters(50)
                .setNoLeadingTrailingSpaces(false)
                .setFilterManager(filterManager->{
                    if(!filterManager.isValid()) {
                        Mono.fx().alert().createWarning().setHeader("Warning")
                                .setMessage(filterManager.getMessage())
                                .show();
                    }
                });
        textField.clone().setTextSource(txt_progcode).applyFilter();
        textField.clone().setTextSource(txt_progname).applyFilter();
        
        StringFilter textFloor = TextInputFilters.string()
//                .setFilterMode(StringFilter.DIGIT)
                .setMaxCharacters(11)
                .setNoLeadingTrailingSpaces(true)
                .setFilterManager(filterManager->{
                    if(!filterManager.isValid()) {
                        Mono.fx().alert().createWarning().setHeader("Warning")
                                .setMessage(filterManager.getMessage())
                                .show();
                    }
                });
        textFloor.clone().setTextSource(txt_floor).applyFilter();
    }
    
    private LayoutDataFX homeFX;
    public void setHomeFX(LayoutDataFX homeFX) {
        this.homeFX = homeFX;
    }

    @Override
    public void onEventHandling() {
        addClickEvent(btn_add, () -> {
            validateValues();
        });
        addClickEvent(btn_cancel, () -> {
//            Mono.fx().getParentStage(btn_add).close();
//            ControllerFX controller = new AcademicHome();
//            Pane fxRoot = Mono.fx().create()
//                    .setPackageName("update2.org.cict.layout.academicprogram")
//                    .setFxmlDocument("academic-home")
//                    .makeFX()
//                    .setController(controller)
//                    .pullOutLayout();
            back();
        });
        
        Mono.fx().key(KeyCode.ENTER).release(vbox_main, ()->{
            validateValues();
        });
    }
    
    private void back() {
        super.setSceneColor("#414852");
        Animate.fade(vbox_main, 150, () -> {
            super.replaceRoot(homeFX.getApplicationRoot());
        }, homeFX.getApplicationRoot());
    }
    
    public void validateValues(){
        String code = MonoString.removeExtraSpace(txt_progcode.getText()).toUpperCase();
        String programName = MonoString.removeExtraSpace(txt_progname.getText()).toUpperCase();
        String floorAssignment = MonoString.removeExtraSpace(txt_floor.getText()).toUpperCase();
        if(code.isEmpty()) {
            Mono.fx().alert()
                    .createWarning()
                    .setHeader("Program Code")
                    .setMessage("Please provide a program code for this Academic Program.")
                    .showAndWait();
            return;
        }

        /**
         * check if code exists
         */
        Object codeExist = Mono.orm().newSearch(Database.connect().academic_program())
                .eq(DB.academic_program().code, code)
                .active()
                .first();
        if(codeExist != null) {
            Mono.fx().alert()
                    .createWarning()
                    .setHeader("Program Code Exist")
                    .setMessage("Academic code is already in the list of programs. Try another one.")
                    .showAndWait();
            return;
        }
        
        if(programName.isEmpty()) {
            Mono.fx().alert()
                    .createWarning()
                    .setHeader("Program Name")
                    .setMessage("Please provide a program name for this Academic Program.")
                    .showAndWait();
            return;
        }
        
        /**
         * check if name exists
         */
        Object programNameExist = Mono.orm()
                .newSearch(Database.connect().academic_program())
                .eq(DB.academic_program().name, programName)
                .active()
                .first();
        if(programNameExist != null) {
            Mono.fx().alert()
                    .createWarning()
                    .setHeader("Program Name Exist")
                    .setMessage("Academic name is already in the list of programs. Try another one.")
                    .showAndWait();
            return;
        }
        
        /**
         * check if the code and name are existing but inactive.
         * if true, ask to restore
         */
        Object inActiveCodeExist = Mono.orm().newSearch(Database.connect().academic_program())
                .eq(DB.academic_program().code, code)
                .eq(DB.academic_program().name, programName)
                .inactive()
                .first();
        if(inActiveCodeExist != null) {
            int res = Mono.fx().alert()
                    .createConfirmation()
                    .setHeader("Restore Academic Program")
                    .setMessage("Academic Program is already in the list of programs but inactive. Do you want to restore it?")
                    .confirmCustom("Yes, Please", "No");
            if(res == 1) {
                this.restore(code);
//                Mono.fx().getParentStage(btn_add).close();
            }
            return;
        }
        
//        if(floorAssignment.isEmpty()) {
//            Mono.fx().alert()
//                    .createWarning()
//                    .setHeader("Floor Assignment")
//                    .setMessage("Please provide a floor assignment to proceed. "
//                            + "Use digit or number to represent floors.")
//                    .showAndWait();
//            return;
//        }
        
//        boolean validFloor = false;
//        for (String currentFloor: validFloorAssignment) {
//            if(floorAssignment.equalsIgnoreCase(currentFloor)) {
//                validFloor = true;
//            }
//        }
//        if(validFloor)
            insert(code, programName, floorAssignment);
//        else {
//            Mono.fx().alert()
//                    .createWarning()
//                    .setHeader("Invalid Floor Assignment")
//                    .setMessage("Please provide a valid floor assignment to proceed. "
//                            + "Use digit or number to represent floors.")
//                    .showAndWait();
//        }
    }
    
    private AcademicProgramInfo acadProgInfo;
    public AcademicProgramInfo getNewAcademicProgramInfo() {
        return acadProgInfo;
    }
    
    public void insert(String code, String name, String floorAssignment){
        AcademicProgramMapping academicProgram = new AcademicProgramMapping();
        academicProgram.setCode(code);
        academicProgram.setName(name);
        academicProgram.setCreated_by(CREATED_BY);
        academicProgram.setCreated_date(CREATED_DATE);
//        academicProgram.setFloor_assignment(Integer.valueOf(floorAssignment));
        academicProgram.setActive(1);
        academicProgram.setImplemented(0);
        
        Integer res = Database.connect().academic_program().insert(academicProgram);
        switch(res){
            case -1:
                Mono.fx().alert()
                        .createError()
                        .setHeader("Adding Failed")
                        .setMessage("Something went wrong in the insertion process. "
                                + "Try again later.")
                        .showAndWait();
                break;
            default:
                Mono.fx().alert()
                        .createInfo()
                        .setHeader("Successfully Added")
                        .setMessage("The academic program is successfully added in the list.")
                        .showAndWait();
                acadProgInfo = new AcademicProgramInfo();
                acadProgInfo.setAcademicProgram(academicProgram);
                try {
                    acadProgInfo.setCurriculums(getCurriculums(academicProgram.getId()));
                } catch(NullPointerException a) {}
                acadProgInfo.setApCreatedBy(CollegeFaculty.instance().getFirstLastName());
                back();
                break;
        }
//        Mono.fx().getParentStage(btn_add).close();
    }
    
    private ArrayList<CurriculumMapping> getCurriculums(Integer id) {
        ArrayList<CurriculumMapping> curriculums = Mono.orm().newSearch(Database.connect().curriculum())
                .eq(DB.curriculum().ACADPROG_id, id)
                .active(Order.desc(DB.curriculum().id))
                .all();
        return curriculums;
    }
     
    public void restore(String code){
        AcademicProgramMapping academicProgram = (AcademicProgramMapping) Database.connect()
                .academic_program()
                .getBy(DB.academic_program().code, code);
        academicProgram.setActive(1);
        if(Database.connect().academic_program().update(academicProgram)) {
            Mono.fx().alert()
                    .createInfo()
                    .setHeader("Successfully Restored")
                    .setMessage("The academic program is successfully restored in the list.")
                    .showAndWait();
            acadProgInfo = new AcademicProgramInfo();
            acadProgInfo.setAcademicProgram(academicProgram);
            acadProgInfo.setCurriculums(getCurriculums(academicProgram.getId()));
            back();
        } else {
            Mono.fx().alert()
                    .createError()
                    .setHeader("Restore Failed")
                    .setMessage("Something went wrong in the restoration process. "
                            + "Try again later.")
                    .showAndWait();
        }
//        Mono.fx().getParentStage(btn_add).close();
    }
}

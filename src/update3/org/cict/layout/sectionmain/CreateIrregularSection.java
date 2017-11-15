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
package update3.org.cict.layout.sectionmain;

import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.FacultyMapping;
import app.lazy.models.LoadGroupMapping;
import app.lazy.models.LoadSectionMapping;
import app.lazy.models.SubjectMapping;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.LayoutDataFX;
import com.jhmvin.fx.display.SceneFX;
import com.jhmvin.propertymanager.FormFormat;
import com.jhmvin.transitions.Animate;
import com.melvin.mono.fx.bootstrap.M;
import java.util.Date;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.cict.authentication.authenticator.SystemProperties;
import org.controlsfx.control.Notifications;
import org.hibernate.criterion.Order;
import update3.org.cict.SectionConstants;
import update3.org.cict.controller.sectionmain.WinIrregularSectionsController;
import update3.org.collegechooser.ChooserHome;
import update3.org.facultychooser.FacultyChooser;
import update3.org.subjectselector.SubjectSelector;

public class CreateIrregularSection extends SceneFX implements ControllerFX {
    
    @FXML
    private VBox application_root;

    @FXML
    private JFXButton btn_back;

    @FXML
    private VBox vbox_special_class;

    @FXML
    private Label lbl_section_type;

    @FXML
    private Label lbl_single_term;

    @FXML
    private HBox hbox_conjunction_college;

    @FXML
    private TextField txt_conjuction;

    @FXML
    private TextField txt_section_name;

    @FXML
    private TextField txt_subject;

    @FXML
    private TextField txt_instructor;

    @FXML
    private JFXButton btn_single_create;
    

    private LayoutDataFX homeFx;

    public void setHomeFx(LayoutDataFX homeFx) {
        this.homeFx = homeFx;
    }
    
    private String sectionType;
    public void setSectionType(String sectionType) {
        this.sectionType = WordUtils.capitalizeFully(sectionType + " CLASS");
    }
    
    private String term;
    public void setTerm(String term) {
        this.term = term;
    }
    
    private void setDisplay() {
        hbox_conjunction_college.setVisible(false);
            vbox_special_class.setVisible(true);
        if(sectionType==null)
            return;
        if(sectionType.equalsIgnoreCase("conjunction class")) {
            hbox_conjunction_college.setVisible(true);
        }
//        Animate.fade(vbox_main, 150, ()->{
//            vbox_main.setVisible(false);
//            vbox_special_class.setVisible(false);
//        }, vbox_special_class);
    }

    @Override
    public void onInitialization() {
        super.bindScene(application_root);
        lbl_section_type.setText(sectionType);
        lbl_single_term.setText(term);
//        this.filters();
        this.setDisplay();
    }
    
    private void filters() {
        
        FormFormat formFormatter = new FormFormat();
        FormFormat.CustomFormat sectionNameFilter = formFormatter.new CustomFormat();
        sectionNameFilter.setMaxCharacters(1);
        sectionNameFilter.setStringFilter(text -> {
            // custom filter
            if (StringUtils.isAlpha(text)) {
                sectionNameFilter.setMaxCharacters(1);
                return true;
            }
            if (StringUtils.isNumeric(text)) {
                if (text.equals("0")) {
                    return false;
                }
                sectionNameFilter.setMaxCharacters(2);
                return true;
            }
            return false;
        });
        sectionNameFilter.setFilterAction(filterAction -> {
            //System.out.println(filterAction.getFilterMessage());
            // do something when filter fails.
        });
        sectionNameFilter.filter(txt_section_name.textProperty());
    }

    private SubjectMapping subject;
    private FacultyMapping instructor;
    @Override
    public void onEventHandling() {
        super.addClickEvent(btn_back, ()->{
            WinIrregularSectionsController controller = homeFx.getController();
            controller.fetchSections();
            Animate.fade(this.application_root, SectionConstants.FADE_SPEED, () -> {
                super.replaceRoot(homeFx.getApplicationRoot());
            }, homeFx.getApplicationRoot());
        });
        
        super.addClickEvent(txt_subject, ()->{
            SubjectSelector ss = M.load(SubjectSelector.class);
            ss.onDelayedStart();
            ss.createStageApplication().showAndWait();
            SubjectMapping temp = ss.getSubjectSelected();
            if(temp!=null) {
                subject = temp;
                txt_subject.setText(subject.getCode());
            }
        });
        
        super.addClickEvent(txt_instructor, () -> {
            FacultyMapping selected_faculty = selectFaculty();
            if (selected_faculty != null) {
                this.instructor = selected_faculty;
                txt_instructor.setText(selected_faculty.getLast_name() + ", " + selected_faculty.getFirst_name());
            }
        });

        super.addClickEvent(btn_single_create, ()->{
            this.createIrregularSection();
        });
        
        super.addClickEvent(txt_conjuction, ()->{
            this.showCollegeChanger();
        });
    }
    
    private void createIrregularSection() {
        String sectionName = txt_section_name.getText().toUpperCase();
        if(sectionName==null || sectionName.isEmpty()) {
            Mono.fx().alert()
                    .createWarning()
                    .setMessage("Please enter a section name to continue.")
                    .show();
            return;
        }
        if(subject==null) {
            Mono.fx().alert()
                    .createWarning()
                    .setMessage("Must have a subject before creating the section.")
                    .show();
            return;
        }
        if(sectionType.equalsIgnoreCase("CONJUNCTION CLASS")) {
            this.createSection(sectionName, txt_conjuction.getText(), "REGULAR", "CONJUNCTION");
        } else if(sectionType.equalsIgnoreCase("TUTORIAL CLASS")) {
            this.createSection(sectionName, "CICT", "TUTORIAL", "TUTORIAL");
        } else if(sectionType.equalsIgnoreCase("MIDYEAR CLASS")) {
            this.createSection(sectionName, "CICT", "REGULAR", "MIDYEAR");
        } else {
            this.createSection(sectionName, "CICT", "REGULAR", "SPECIAL");
        }
        
    }
    
    private void createSection(String SECTION_NAME, String COLLEGE, String GROUP_TYPE, String SECTION_TYPE) {
        Integer FACULTY_ID = CollegeFaculty.instance().getFACULTY_ID();
        Date DATE_CREATED = Mono.orm().getServerTime().getDateWithFormat()
                ;
        System.out.println("SECTION NAME: " + SECTION_NAME);
        System.out.println("SUBJECT: " + subject.getCode());
        LoadSectionMapping a = null;
        if(SECTION_TYPE.equalsIgnoreCase("CONJUNCTION")) {
            a = Mono.orm().newSearch(Database.connect().load_section())
                    .eq(DB.load_section().section_name, SECTION_NAME)
                    .eq(DB.load_section().type, SECTION_TYPE)
                    .eq(DB.load_section().college, COLLEGE)
                    .eq(DB.load_section().ACADTERM_id, SystemProperties.instance().getCurrentAcademicTerm().getId())
                    .isNull(DB.load_section().ACADPROG_id)
                    .isNull(DB.load_section().CURRICULUM_id)
                    .isNull(DB.load_section().adviser)
                    .active(Order.desc(DB.load_section().id))
                    .first();
        } else {
            a = Mono.orm().newSearch(Database.connect().load_section())
                    .eq(DB.load_section().section_name, SECTION_NAME)
                    .eq(DB.load_section().type, SECTION_TYPE)
                    .eq(DB.load_section().ACADTERM_id, SystemProperties.instance().getCurrentAcademicTerm().getId())
                    .isNull(DB.load_section().ACADPROG_id)
                    .isNull(DB.load_section().CURRICULUM_id)
                    .isNull(DB.load_section().adviser)
                    .active(Order.desc(DB.load_section().id))
                    .first();
        }

        if (a != null) {
            Mono.fx().snackbar().showError(application_root, "Another section has the same information.");
            return;
        }
        
        LoadSectionMapping lsMap = new LoadSectionMapping();
        lsMap.setACADTERM_id(SystemProperties.instance().getCurrentAcademicTerm().getId());
        lsMap.setActive(1);
        lsMap.setCollege(COLLEGE);
        lsMap.setCreated_by(FACULTY_ID);
        lsMap.setCreated_date(DATE_CREATED);
        lsMap.setSection_name(SECTION_NAME);
        lsMap.setType(SECTION_TYPE);
        Integer lsID = Database.connect().load_section().insert(lsMap);
        if(lsID.equals(-1)) {
            Notifications.create().darkStyle()
                    .title("Creation Failed")
                    .text("Please check your connection to the server.").showError();
            return;
        }
        LoadGroupMapping lgMap = new LoadGroupMapping();
        lgMap.setActive(1);
        lgMap.setAdded_by(FACULTY_ID);
        lgMap.setAdded_date(DATE_CREATED);
        lgMap.setFaculty(instructor==null? null : instructor.getId());
        lgMap.setGroup_type(GROUP_TYPE);
        lgMap.setLOADSEC_id(lsID);
        lgMap.setSUBJECT_id(subject.getId());
        Integer lgID = Database.connect().load_group().insert(lgMap);
        if(lgID.equals(-1)) {
            Notifications.create().darkStyle()
                    .title("Creation Failed")
                    .text("Please check your connection to the server.").showError();
        } else {
            Notifications.create().darkStyle()
                    .title("Successfully Created")
                    .text(sectionType + " successfully created.").showInformation();
        }
    }
    
    private FacultyMapping selectFaculty() {
        FacultyChooser facultyChooser = M.load(FacultyChooser.class);
        facultyChooser.onDelayedStart(); // do not put database transactions on startUp
        try {
            facultyChooser.getCurrentStage().showAndWait();
        } catch (NullPointerException e) {
            Stage a = facultyChooser.createChildStage(super.getStage());
            a.initStyle(StageStyle.UNDECORATED);
            a.showAndWait();
        }
        return facultyChooser.getSelectedFaculty();
    }
    
    private void showCollegeChanger() {
        ChooserHome.showCICT(false);
        String selectedCollege = ChooserHome.open();
        if (selectedCollege == null || selectedCollege.equalsIgnoreCase("cancel")) {
            // operation was cancelled
            return;
        }
        txt_conjuction.setText(selectedCollege);
    }
}

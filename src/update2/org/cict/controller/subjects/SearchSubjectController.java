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
package update2.org.cict.controller.subjects;

import app.lazy.models.CurriculumHistorySummaryMapping;
import app.lazy.models.CurriculumRequisiteExtMapping;
import app.lazy.models.CurriculumRequisiteLineMapping;
import app.lazy.models.CurriculumSubjectMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.SubjectMapping;
import artifacts.MonoString;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.text.WordUtils;
import org.cict.SubjectClassification;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.controlsfx.control.Notifications;
import org.hibernate.criterion.Order;

/**
 *
 * @author Joemar
 */
public class SearchSubjectController extends SceneFX implements ControllerFX {

    @FXML
    private AnchorPane anchor_main;

    @FXML
    private TextField txtSearch;

    @FXML
    private JFXButton btnSearch;

    @FXML
    private AnchorPane anchor_result;

    @FXML
    private HBox hbox_greet;

    @FXML
    private HBox hbox_no_result;

    @FXML
    private HBox hbox_search;

    @FXML
    private AnchorPane anchor_view;

    @FXML
    private VBox vbox_subjects;
    
    @FXML
    private Button btn_add_new_subject;

    @FXML
    private Button btn_cancel;
    
    private Integer CURRICULUM_id, YEAR, SEMESTER, SUBJECT_id_get;
    private ArrayList<SubjectMapping> lst_subject;
    private ArrayList<SubjectMapping> temp_subject_list = new ArrayList<>();
    private SimpleTable subjectTable = new SimpleTable();
    private String MODE_setting;
    private Integer CREATED_BY = CollegeFaculty.instance().getFACULTY_ID();
    private Date CREATED_DATE = Mono.orm().getServerTime().getDateWithFormat();
    private CurriculumSubjectMapping SUBJECT_csMap;
    
    private void logs(String str) {
        if(true)
            System.out.println("@SearchSubjectController: " + str);
    }
    
    public void setSubjectIdGet(Integer SUBJECT_id) {
        this.SUBJECT_id_get = SUBJECT_id;
    }
    
    public void setModeSetting(String mode) {
        this.MODE_setting = mode;
    }
    
    public void setYearAndSemester(Integer year, Integer sem) {
        this.YEAR = year;
        this.SEMESTER = sem;
    }
    
    
    public SearchSubjectController(Integer curriculumID) {
        this.CURRICULUM_id = curriculumID;
    }
    
    @Override
    public void onInitialization() {
    
        bindScene(anchor_main);
        
        FetchSubjects fetch = new FetchSubjects();
        fetch.setOnStart(onStart -> {
            this.hbox_search.setVisible(true);
        });
        fetch.setOnSuccess(onSuccess -> {
            lst_subject = fetch.getSubjectResult();
            createTable(lst_subject);
            this.hbox_search.setVisible(false);
        });
        fetch.setOnCancel(onCancel -> {
            this.hbox_no_result.setVisible(true);
            this.hbox_search.setVisible(false);
        });
        fetch.setRestTime(1000);
        fetch.transact();
        
        if(MODE_setting.contains("SUBJECT")) {
            /**
             * get the CurriculumSubjectMapping of the subject that seeks for pre-req
             * this will be used in validation
             */
            SUBJECT_csMap = Mono.orm().newSearch(Database.connect().curriculum_subject())
                    .eq(DB.curriculum_subject().CURRICULUM_id, this.CURRICULUM_id)
                    .eq(DB.curriculum_subject().SUBJECT_id, SUBJECT_id_get)
                    .active(Order.desc(DB.curriculum_subject().id))
                    .first();
            if(SUBJECT_csMap == null) {
                System.out.println("NO CurriculumSubjectMapping FOUND FOR SUBJECT ID: " + SUBJECT_id_get);
            }
        }
    }

    @Override
    public void onEventHandling() {
        txtSearch.textProperty().addListener(listener -> {
            if (txtSearch.getText().isEmpty()) {
                if (this.lst_subject != null) {
                    anchor_view.setVisible(true);
                    hbox_no_result.setVisible(false);
                    subjectTable.getChildren().clear();
                    this.createTable(this.lst_subject);
                } else {
                    subjectTable.getChildren().clear();
                    anchor_view.setVisible(false);
                    hbox_no_result.setVisible(true);
                }
            }
        });
        
        addClickEvent(btnSearch, ()-> {
            onSearch();
        });
        
        addClickEvent(btn_add_new_subject, ()-> {
            showAddNewSubject();
        });
        
        addClickEvent(btn_cancel, ()-> {
            anchor_view.setVisible(true);
            hbox_no_result.setVisible(false);
            subjectTable.getChildren().clear();
            createTable(lst_subject);
        });
         
        
        Mono.fx().key(KeyCode.ENTER).release(anchor_main, () -> {
            onSearch();
        });
    }
    
    private void onSearch() {
        String keyword = MonoString.removeExtraSpace(txtSearch.getText()).toUpperCase();
        if(keyword.isEmpty()) {
            return;
        }
        temp_subject_list.clear();
        for(SubjectMapping currentSubject: lst_subject) {
            if(currentSubject.getCode().equalsIgnoreCase(keyword)
                    || currentSubject.getCode().contains(keyword)) {
                temp_subject_list.add(currentSubject);
            }
        }

        if(temp_subject_list.isEmpty()) {
            vbox_subjects.getChildren().clear();
            hbox_no_result.setVisible(true);
            anchor_view.setVisible(false);
        } else {
            anchor_view.setVisible(true);
            hbox_no_result.setVisible(false);
            subjectTable.getChildren().clear();
            createTable(temp_subject_list);
        }
    }
    
    private void showAddNewSubject() {
            AddNewSubjectController controller = new AddNewSubjectController();
            controller.setTextFieldSubjectCode(txtSearch.getText());
            Mono.fx().create()
                    .setPackageName("update2.org.cict.layout.subjects")
                    .setFxmlDocument("add-new-subject")
                    .makeFX()
                    .setController(controller)
                    .makeScene()
                    .makeStageApplication()
                    .stageResizeable(false)
                    .stageShowAndWait();
            SubjectMapping newSubject = controller.getNewSubject();
            if(newSubject != null) {
                subjectTable.getChildren().clear();
                hbox_no_result.setVisible(false);
                anchor_view.setVisible(true);
                temp_subject_list.clear();
                temp_subject_list.add(newSubject);
                createTable(temp_subject_list);
            }
    }
    
    private void createTable(ArrayList<SubjectMapping> displaySubjects) {
        for(SubjectMapping subject: displaySubjects) {
            createRow(subject);
        }
        
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(subjectTable);
        simpleTableView.setFixedWidth(true);
        
        simpleTableView.setParentOnScene(vbox_subjects);
    }
    
    private void createRow(SubjectMapping subject) {
        
        SimpleTableRow row = new SimpleTableRow();
        row.setRowHeight(70.0);

        HBox programRow = (HBox) Mono.fx().create()
                .setPackageName("update2.org.cict.layout.subjects")
                .setFxmlDocument("search-subject-row")
                .makeFX()
                .pullOutLayout();

        Label lbl_code = searchAccessibilityText(programRow, "subject_code");
        Label lbl_descriptive_title = searchAccessibilityText(programRow, "descriptive_title");
        Label lbl_lec = searchAccessibilityText(programRow, "lec");
        Label lbl_lab = searchAccessibilityText(programRow, "lab");
        Label lbl_subtype = searchAccessibilityText(programRow, "subtype");
        Label lbl_type = searchAccessibilityText(programRow, "type");
        Button btn_select = searchAccessibilityText(programRow, "btn_select");

        addClickEvent(btn_select, ()-> {
            if(MODE_setting.equalsIgnoreCase("CURRICULUM"))
                addToCurriculum(subject);
            else if(MODE_setting.equalsIgnoreCase("SUBJECT_pre-req")) 
                addAsPreReq(subject);
            else if(MODE_setting.equalsIgnoreCase("SUBJECT_co-req")) {
                addAsCoReq(subject);
            }
        });
        
        lbl_code.setText(subject.getCode());
        lbl_descriptive_title.setText(subject.getDescriptive_title());
        lbl_lec.setText(subject.getLec_units().toString());
        lbl_lab.setText(subject.getLab_units().toString());
        lbl_subtype.setText(WordUtils.capitalizeFully(subject.getSubtype()));
        lbl_type.setText(subject.getType());
        
        SimpleTableCell cellParent = new SimpleTableCell();
        cellParent.setResizePriority(Priority.ALWAYS);
        cellParent.setContent(programRow);

        row.addCell(cellParent);

        subjectTable.addRow(row);
    }
    
    private void addAsPreReq(SubjectMapping subjectPreReq) {
        boolean isInvalid = false;
        String message = "";
        if(Objects.equals(subjectPreReq.getId(), SUBJECT_id_get)) {
            isInvalid = true;
            message = "A subject cannot be referenced as a pre-requisite of itself.";
        } else {
            CurriculumSubjectMapping csMap_prereq = Mono.orm().newSearch(Database.connect().curriculum_subject())
                    .eq(DB.curriculum_subject().CURRICULUM_id, this.CURRICULUM_id)
                    .eq(DB.curriculum_subject().SUBJECT_id, subjectPreReq.getId())
                    .active(Order.desc(DB.curriculum_subject().id))
                    .first();
            if(csMap_prereq != null) {
                Integer year = SUBJECT_csMap.getYear();
                Integer sem = SUBJECT_csMap.getSemester();
                if(csMap_prereq.getYear() > year) {
                    //not valid
                    isInvalid = true;
                    message = "A pre-requisite with a higher year level than the subject is not allowed.";
                } else {
                    if(Objects.equals(csMap_prereq.getYear(), year)) {
                        if(csMap_prereq.getSemester() >= sem) {
                            //not valid
                            isInvalid = true;
                            message = "A pre-requisite with an equal or higher semester level than the subject is not allowed.";
                        } else {
                            //valid
                        }
                    } else {
                        //valid
                    }
                }
            } else {
                //subject is not yet added to the curriculum
                isInvalid = true;
                message = "The subject is not yet added to this curriculum. Please add it first to proceed.";
            }
        }
        
        if(isInvalid) {
            Mono.fx().alert()
                    .createWarning()
                    .setHeader("Invalid Pre-requisite Subject")
                    .setMessage(message)
                    .showAndWait();
            return;
        }
        
        CurriculumRequisiteLineMapping crlMap = Mono.orm().newSearch(Database.connect().curriculum_requisite_line())
                .eq(DB.curriculum_requisite_line().SUBJECT_id_get, this.SUBJECT_id_get)
                .eq(DB.curriculum_requisite_line().SUBJECT_id_req, subjectPreReq.getId())
                .eq(DB.curriculum_requisite_line().CURRICULUM_id, this.CURRICULUM_id)
                .execute(Order.desc(DB.curriculum_requisite_line().id))
                .first();
        boolean isSuccessfull = false;
        if(crlMap == null) {
            //add new crlMap
            CurriculumRequisiteLineMapping new_crl_Map = new CurriculumRequisiteLineMapping();
            new_crl_Map.setActive(1);
            new_crl_Map.setCreated_by(CREATED_BY);
            new_crl_Map.setCreated_date(CREATED_DATE);
            new_crl_Map.setSUBJECT_id_get(SUBJECT_id_get);
            new_crl_Map.setSUBJECT_id_req(subjectPreReq.getId());
            new_crl_Map.setCURRICULUM_id(CURRICULUM_id);
            int res = Database.connect().curriculum_requisite_line().insert(new_crl_Map);
            if(res != -1) {
                isAdded = true;
                isSuccessfull = true;
            }
        } else {
            if(crlMap.getActive() == 1) {
                Mono.fx().alert()
                        .createWarning()
                        .setHeader("Pre-requisite Already Exist")
                        .setMessage("This subject is already in the pre-requisite list.")
                        .showAndWait();
                return;
            }
            //set active
            crlMap.setActive(1);
            crlMap.setCURRICULUM_id(CURRICULUM_id);
            crlMap.setCreated_by(CREATED_BY);
            crlMap.setCreated_date(CREATED_DATE);
            crlMap.setRemoved_by(null);
            crlMap.setRemoved_date(null);
            if(Database.connect().curriculum_requisite_line().update(crlMap)) {
                isAdded = true;
                isSuccessfull = true;
            }
        }
        
        if(isSuccessfull) {
            isAdded = true;
            SubjectMapping subject = Mono.orm().newSearch(Database.connect().subject())
                    .eq(DB.subject().id, SUBJECT_id_get)
                    .execute()
                    .first();
            insertSubjectHistory(subject, "PREREQ", subjectPreReq);
            Notifications.create()
                    .title("Successfully Added")
                    .text(subjectPreReq.getCode() + ": " + subjectPreReq.getDescriptive_title())
                    .showInformation();
        } else {
            Notifications.create()
                    .title("Adding Failed")
                    .text(subjectPreReq.getCode() + ": " + subjectPreReq.getDescriptive_title())
                    .showError();
        }
    }
    
    
    private void addAsCoReq(SubjectMapping subjectCoReq) {
        boolean isInvalid = false;
        String message = "";
        if(Objects.equals(subjectCoReq.getId(), SUBJECT_id_get)) {
            isInvalid = true;
            message = "A subject cannot be referenced as a co-requisite of itself.";
        } else {
            CurriculumSubjectMapping csMap_coreq = Mono.orm().newSearch(Database.connect().curriculum_subject())
                    .eq(DB.curriculum_subject().CURRICULUM_id, this.CURRICULUM_id)
                    .eq(DB.curriculum_subject().SUBJECT_id, subjectCoReq.getId())
                    .active(Order.desc(DB.curriculum_subject().id))
                    .first();
            if(csMap_coreq != null) {
                Integer year = SUBJECT_csMap.getYear();
                Integer sem = SUBJECT_csMap.getSemester();
                if(csMap_coreq.getYear() > year) {
                    //not valid
                    isInvalid = true;
                    message = "A co-requisite with a higher year level than the subject is not allowed.";
                } else {
                    if(Objects.equals(csMap_coreq.getYear(), year)) {
                        if(Objects.equals(csMap_coreq.getSemester(), sem)) {
                            // valid
                        } else {
                            // not valid
                            isInvalid = true;
                            message = "A co-requisite must be with the same semester level.";
                        }
                    } else {
                        // not valid
                        isInvalid = true;
                        message = "A co-requisite must be with the same year level.";
                    }
                }
            } else {
                //subject is not yet added to the curriculum
                isInvalid = true;
                message = "The subject is not yet added to this curriculum. Please add it first to proceed.";
            }
        }
        
        if(isInvalid) {
            Mono.fx().alert()
                    .createWarning()
                    .setHeader("Invalid Co-requisite Subject")
                    .setMessage(message)
                    .showAndWait();
            return;
        }
        
        CurriculumRequisiteExtMapping creMap = Mono.orm().newSearch(Database.connect().curriculum_requisite_ext())
                .eq(DB.curriculum_requisite_ext().SUBJECT_id_get, this.SUBJECT_id_get)
                .eq(DB.curriculum_requisite_ext().SUBJECT_id_req, subjectCoReq.getId())
                .eq(DB.curriculum_requisite_ext().CURRICULUM_id, this.CURRICULUM_id)
                .execute(Order.desc(DB.curriculum_requisite_ext().id))
                .first();
        boolean isSuccessfull = false;
        if(creMap == null) {
            //add new crlMap
            CurriculumRequisiteExtMapping new_cre_Map = new CurriculumRequisiteExtMapping();
            new_cre_Map.setActive(1);
            new_cre_Map.setAdded_by(CREATED_BY);
            new_cre_Map.setAdded_date(CREATED_DATE);
            new_cre_Map.setSUBJECT_id_get(SUBJECT_id_get);
            new_cre_Map.setSUBJECT_id_req(subjectCoReq.getId());
            new_cre_Map.setCURRICULUM_id(CURRICULUM_id);
            new_cre_Map.setType(SubjectClassification.REQUITE_TYPE_CO);
            int res = Database.connect().curriculum_requisite_ext().insert(new_cre_Map);
            if(res != -1) {
                isAdded = true;
                isSuccessfull = true;
            }
        } else {
            if(creMap.getActive() == 1) {
                Mono.fx().alert()
                        .createWarning()
                        .setHeader("Co-requisite Already Exist")
                        .setMessage("This subject is already in the co-requisite list.")
                        .showAndWait();
                return;
            }
            //set active
            creMap.setActive(1);
            creMap.setCURRICULUM_id(CURRICULUM_id);
            creMap.setAdded_by(CREATED_BY);
            creMap.setAdded_date(CREATED_DATE);
            creMap.setRemoved_by(null);
            creMap.setRemoved_date(null);
            if(Database.connect().curriculum_requisite_ext().update(creMap)) {
                isAdded = true;
                isSuccessfull = true;
            }
        }
        
        if(isSuccessfull) {
            isAdded = true;
            SubjectMapping subject = Mono.orm().newSearch(Database.connect().subject())
                    .eq(DB.subject().id, SUBJECT_id_get)
                    .execute()
                    .first();
            insertSubjectHistory(subject, "COREQ", subjectCoReq);
            Notifications.create()
                    .title("Successfully Added")
                    .text(subjectCoReq.getCode() + ": " + subjectCoReq.getDescriptive_title())
                    .showInformation();
        } else {
            Notifications.create()
                    .title("Adding Failed")
                    .text(subjectCoReq.getCode() + ": " + subjectCoReq.getDescriptive_title())
                    .showError();
        }
    }
    
    private void addToCurriculum(SubjectMapping SUBJECT) {
        CurriculumSubjectMapping exist = Mono.orm().newSearch(Database.connect().curriculum_subject())
                .eq(DB.curriculum_subject().SUBJECT_id, SUBJECT.getId())
                .eq(DB.curriculum_subject().CURRICULUM_id, this.CURRICULUM_id)
                .execute(Order.desc(DB.curriculum_subject().id))
                .first();
        
        if(exist != null) {
            if(exist.getActive() == 1) {
                //exist
                Mono.fx().alert()
                        .createWarning()
                        .setHeader("Subject Exist")
                        .setMessage("Subject is already in the curriculum's list of subject.")
                        .showAndWait();
                return;
            } else {
                if(Objects.equals(exist.getSemester(), SEMESTER) && Objects.equals(exist.getYear(), YEAR)) {
                    exist.setActive(1);
                    this.showResultAlert(Database.connect().curriculum_subject().update(exist), SUBJECT, "ADD");
                    return;
                } else {
                    int res = Mono.fx().alert()
                            .createConfirmation()
                            .setHeader("Subject Exist But Inactive")
                            .setMessage("This subject is existing in this curriculum, " 
                                    + getYearLevel(exist.getYear()) + " " 
                                    + getSemester(exist.getSemester()) + " but inactive. Do you still want to add it?")
                            .confirmYesNo();
                    if(res != 1) {
                        return;
                    } else {
                        //update the values to avoid unused data 
                        exist.setSemester(SEMESTER);
                        exist.setYear(YEAR);
                        exist.setActive(1);
                        this.showResultAlert(Database.connect().curriculum_subject().update(exist), SUBJECT, "RESTORE");
                        return;
                    }
                }
            }
        }
        
        CurriculumSubjectMapping newCsMap = new CurriculumSubjectMapping();
        newCsMap.setActive(1);
        newCsMap.setCURRICULUM_id(CURRICULUM_id);
        newCsMap.setSUBJECT_id(SUBJECT.getId());
        newCsMap.setSemester(SEMESTER);
        newCsMap.setYear(YEAR);
        Integer id = Database.connect().curriculum_subject().insert(newCsMap);
        if(id != -1) {
            CurriculumSubjectMapping csMap = Mono.orm().newSearch(Database.connect().curriculum_subject())
                    .eq(DB.curriculum_subject().id, id)
                    .active()
                    .first();
            if(csMap != null) {
                csMap.setSequence(id);
                this.showResultAlert(Database.connect().curriculum_subject().update(csMap), SUBJECT, "ADD");
            }
        }
    }
    
     
    private boolean insertSubjectHistory(SubjectMapping subject, String mode, SubjectMapping subjectReq) {
        CurriculumSubjectMapping csubjectMap = Mono.orm().newSearch(Database.connect().curriculum_subject())
                .eq(DB.curriculum_subject().CURRICULUM_id, this.CURRICULUM_id)
                .eq(DB.curriculum_subject().SUBJECT_id, subject.getId())
                .active()
                .first();
        String yearSemester = getYearLevel(csubjectMap.getYear()) + "-" + getSemester(csubjectMap.getSemester());
        String description = "";
        if(mode.equalsIgnoreCase("ADD") || mode.equalsIgnoreCase("RESTORE")) {
            description = mode + "ED SUBJECT [ID:" + subject.getId()+ "] " + subject.getCode() 
                + " TO " + yearSemester.toUpperCase();
        } else if(mode.equalsIgnoreCase("PREREQ")) {
            description = "ADDED PRE-REQUISITE [ID:" + subjectReq.getId()+ "] " + subjectReq.getCode()
                    +" FOR [ID:" + subject.getId()+ "] " + subject.getCode() + " FROM " + yearSemester.toUpperCase();
        } else if(mode.equalsIgnoreCase("COREQ")) {
            description = "ADDED CO-REQUISITE [ID:" + subjectReq.getId()+ "] " + subjectReq.getCode()
                    +" FOR [ID:" + subject.getId()+ "] " + subject.getCode() + " FROM " + yearSemester.toUpperCase();
        }
        CurriculumHistorySummaryMapping chsMap = new CurriculumHistorySummaryMapping();
        chsMap.setActive(1);
        chsMap.setCreated_by(CREATED_BY);
        chsMap.setCreated_date(CREATED_DATE);
        chsMap.setCurriculum_id(this.CURRICULUM_id);
        chsMap.setDescription(description);
        if(Database.connect().curriculum_history_summary().insert(chsMap) == -1) {
            logs("HISTORY NOT SAVED");
            return false;
        }
        return true;
    }
    
    private void showResultAlert(Boolean res, SubjectMapping SUBJECT, String mode) {
        if(res) {
            isAdded = true;
            insertSubjectHistory(SUBJECT, mode, null);
            Notifications.create()
                    .title("Successfully Added")
                    .text(SUBJECT.getCode() + ": " + SUBJECT.getDescriptive_title())
                    .showInformation();
        } else {
            Notifications.create()
                    .title("Adding Failed")
                    .text(SUBJECT.getCode() + ": " + SUBJECT.getDescriptive_title())
                    .showError();
        }
    }
        
    private boolean isAdded = false;
    public boolean isAdded() {
        return isAdded;
    }
     
    private String getYearLevel(Integer year) {
        String yearLevel = "";
        switch(year){
                case 1:
                    yearLevel = "First Year";
                    break;
                case 2:
                    yearLevel = "Second Year";
                    break;
                case 3:
                    yearLevel = "Third Year";
                    break;
                case 4:
                    yearLevel = "Fourth Year";
                    break;
        }
        return yearLevel;           
    }

    private String getSemester(Integer sem) {
        String semester = "";
        switch(sem) {
            case 1:
                semester = "First Semester";
                break;
            case 2:
                semester = "Second Semester";
                break;
        }
        return semester;
    }
}

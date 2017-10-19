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
package update5.org.cict.student.controller;

import app.lazy.models.AcademicProgramMapping;
import app.lazy.models.AcademicTermMapping;
import app.lazy.models.CurriculumMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.StudentMapping;
import artifacts.MonoString;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.LayoutDataFX;
import com.jhmvin.fx.display.SceneFX;
import com.jhmvin.orm.SQL;
import com.jhmvin.transitions.Animate;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.cict.authentication.authenticator.SystemProperties;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import update.org.cict.controller.home.Home;
import static update.org.cict.controller.home.Home.SCENE_TRANSITION_COLOR;

/**
 *
 * @author Joemar
 */
public class StudentHomeController extends SceneFX implements ControllerFX {

    @FXML
    private StackPane application_root;

    @FXML
    private JFXButton btn_home;

    @FXML
    private ComboBox<String> cmb_category_main;

    @FXML
    private TextField txt_search_key_main;

    @FXML
    private JFXButton btn_search1_main;

    @FXML
    private CheckBox chkbx_onlyEnrolled_main;

    @FXML
    private ComboBox<String> cmb_category;

    @FXML
    private JFXButton btn_home1;

    @FXML
    private TextField txt_search_key;

    @FXML
    private JFXButton btn_search;

    @FXML
    private CheckBox chkbx_onlyEnrolled;

    @FXML
    private VBox vbox_result;

    @FXML
    private VBox vbox_list;
    
    @FXML
    private VBox vbox_no_result;
    
    @FXML
    private Label lbl_status;
    
    @FXML
    private Label lbl_status1;
    
    @Override
    public void onInitialization() {
        super.bindScene(application_root);
        this.setComboBox();
        txt_search_key_main.requestFocus();
    }
    private final String NAME = "NAME"
            , STUDENT_NUM = "STUDENT NUMBER"
            , ACAD_PROG = "ACADEMIC PROGRAM";
    private String SELECTED_mode = this.NAME;
    private Boolean isEnrolledSelected = false;
    @Override
    public void onEventHandling() {
        chkbx_onlyEnrolled_main.selectedProperty().addListener((g)->{
            if(chkbx_onlyEnrolled_main.isSelected()) {
                isEnrolledSelected = true;
            } else
                isEnrolledSelected = false;
            chkbx_onlyEnrolled.selectedProperty().setValue(isEnrolledSelected);
        });
        chkbx_onlyEnrolled.selectedProperty().addListener((g)->{
            if(chkbx_onlyEnrolled.isSelected()) {
                isEnrolledSelected = true;
            } else
                isEnrolledSelected = false;
            chkbx_onlyEnrolled_main.selectedProperty().setValue(isEnrolledSelected);
        });
        super.addClickEvent(btn_home, ()->{
            Home.callHome(this);
        });
        super.addClickEvent(btn_home1, ()->{
            vbox_result.setVisible(false);
            vbox_no_result.setVisible(false);
        });
        cmb_category_main.valueProperty().addListener((a)->{
            SELECTED_mode = cmb_category_main.getSelectionModel().getSelectedItem();
            cmb_category.getSelectionModel().select(SELECTED_mode);
        });
        cmb_category.valueProperty().addListener((a)->{
            SELECTED_mode = cmb_category.getSelectionModel().getSelectedItem();
        });
        super.addClickEvent(btn_search1_main, ()->{
            onSearch();
        });
        super.addClickEvent(btn_search, ()->{
            onSearch();
        });
        Mono.fx().key(KeyCode.ENTER).release(application_root, ()->{
            onSearch();
        });
    }
    
    public void onSearch() {
        String searchWord = "";
        if(vbox_result.isVisible())
            searchWord = MonoString.removeExtraSpace(txt_search_key.getText()).toUpperCase();
        else {
            searchWord = MonoString.removeExtraSpace(txt_search_key_main.getText()).toUpperCase();
            txt_search_key.setText(txt_search_key_main.getText());
        }
        if(searchWord.isEmpty()) {
            lbl_status.setText("Nothing to search. Please enter a " + SELECTED_mode.toLowerCase() + ". You can change the selected category.");
            lbl_status1.setText("Nothing to search. Please enter a " + SELECTED_mode.toLowerCase() + ". You can change the selected category.");
            return;
        } else {
            lbl_status.setText("Searching please wait...");
            lbl_status1.setText("Searching please wait...");
        }
        
        if(SELECTED_mode.equalsIgnoreCase(NAME)) {
            Criterion search_for_name = SQL.or(
                    Restrictions.ilike(DB.student().last_name, searchWord, MatchMode.ANYWHERE),
                    Restrictions.ilike(DB.student().first_name, searchWord, MatchMode.ANYWHERE),
                    Restrictions.ilike(DB.student().middle_name, searchWord, MatchMode.ANYWHERE));
            fetchStudent(search_for_name, false, null, null);
        } else if(SELECTED_mode.equalsIgnoreCase(STUDENT_NUM)) {
            Criterion search_for_stud_num = SQL.or(
                    Restrictions.ilike(DB.student().id, searchWord, MatchMode.ANYWHERE));
            fetchStudent(search_for_stud_num, false, null, null);
        } else if(SELECTED_mode.equalsIgnoreCase(ACAD_PROG)) {
            String[] keys = searchWord.split(" ");
            if(keys.length == 1) {
                // assuming that it is a academic code
                Criterion search_for_academic_program = SQL.or(
                        Restrictions.ilike(DB.academic_program().code, searchWord, MatchMode.ANYWHERE));
                fetchStudent(search_for_academic_program, true, null, null);
            } else if(keys.length>=2) {
                String[] section = keys[1].split("-");
                if(section.length==1) {
                    // assuming that the inputted text is a program code and a section name
                    Criterion search_for_academic_program = SQL.or(
                            Restrictions.ilike(DB.academic_program().code, keys[0], MatchMode.ANYWHERE));
                    fetchStudent(search_for_academic_program, true, keys[1], null);
                } else if(section.length==2) {
                    // assuming that the inputted text is a program code, a section name and a group
                    Criterion search_for_academic_program = SQL.or(
                            Restrictions.ilike(DB.academic_program().code, keys[0], MatchMode.ANYWHERE));
                    Integer group;
                    try {
                        group = Integer.valueOf(section[1].substring(1));
                    } catch (Exception e) {
                        group = null;
                    }
                    fetchStudent(search_for_academic_program, true, section[0], group);
                }
            }
        }
    }
    
    private ArrayList<StudentMapping> students = new ArrayList<>();
    private void fetchStudent(Criterion criterion, Boolean isSearchingForAcadProgCode, String section, Integer group) {
        students.clear();
        FetchStudents fetch = new FetchStudents();
        fetch.setCriterion(criterion);
        fetch.setEnrolledSelected(isEnrolledSelected);
        fetch.isSearchingForAcadProgCode = isSearchingForAcadProgCode;
        fetch.setSectionKey(section);
        fetch.setGroupKey(group);
        fetch.whenStarted(()->{
            vbox_no_result.setVisible(false);
            tableStudent.getChildren().clear();
        });
        fetch.whenRunning(()->{
            lbl_status.setText("Loading...");
        });
        fetch.whenSuccess(()->{
            students = fetch.getResult();
            if(students.isEmpty()) {
                lbl_status1.setText("No result. " + fetch.getMessage());
                vbox_no_result.setVisible(true);
            } else {
                createStudentTable(students);
                lbl_status1.setText("Done. Total Result Found: " + students.size());
            }
            vbox_result.setVisible(true);
        });
        fetch.whenFailed(()->{
            lbl_status.setText("Process failed. Something went wrong. " + fetch.getMessage());
            lbl_status1.setText("Process failed. Something went wrong. " + fetch.getMessage());
            vbox_result.setVisible(true);
            tableStudent.getChildren().clear();
            vbox_no_result.setVisible(true);
        });
        fetch.whenCancelled(()->{
            lbl_status.setText(fetch.getMessage());
            lbl_status1.setText(fetch.getMessage());
            vbox_result.setVisible(true);
            tableStudent.getChildren().clear();
            vbox_no_result.setVisible(true);
        });
        fetch.whenFinished(()->{
            if(fetch.getResult()==null) {
                vbox_no_result.setVisible(true);
                tableStudent.getChildren().clear();
                lbl_status1.setText("No result. " + fetch.getMessage());
                lbl_status1.setText("No result. " + fetch.getMessage());
            }
        });
        fetch.setRestTime(1000);
        fetch.transact();
    }
    
    private SimpleTable tableStudent = new SimpleTable();
    private void createStudentTable(ArrayList<StudentMapping> studentToDisplay) {
        try {
//            Mono.fx().thread().wrap(()->{
            tableStudent.getChildren().clear();
            
            if (studentToDisplay.isEmpty()) {
                System.out.println("NO STUDENT FOUND");
                return;
            }
            for (StudentMapping student : studentToDisplay) {
                createRow(student, tableStudent);
            }

            SimpleTableView simpleTableView = new SimpleTableView();
            simpleTableView.setTable(tableStudent);
            simpleTableView.setFixedWidth(true);
                simpleTableView.setParentOnScene(vbox_list);
//            });
        } catch (NullPointerException a) {
            a.printStackTrace();
        }
    }
    
    private final static String KEY_MORE_INFO = "MORE_INFO";
    private void createRow(StudentMapping student, SimpleTable table) {
        SimpleTableRow row = new SimpleTableRow();
        row.setRowHeight(80.0);
        StudentInformation studInfo = new StudentInformation(student);
        HBox studentRow = (HBox) Mono.fx().create()
                .setPackageName("update5.org.cict.student.layout")
                .setFxmlDocument("student-row")
                .makeFX()
                .pullOutLayout();

        Label lbl_id = searchAccessibilityText(studentRow, "id");
        Label lbl_name = searchAccessibilityText(studentRow, "name");
        Label lbl_section = searchAccessibilityText(studentRow, "section");
//        Label lbl_college = searchAccessibilityText(studentRow, "college");
//        Label lbl_campus = searchAccessibilityText(studentRow, "campus");

        lbl_id.setText(student.getId());
        String middleName = student.getMiddle_name();
        String mName = middleName==null || middleName.isEmpty()? "" : (" " + middleName);
        lbl_name.setText(student.getLast_name() + ", " + student.getFirst_name() + mName);
//        lbl_campus.setText(student.getCampus()==null? "NOT SET" : student.getCampus());
//        lbl_college.setText(student.getCollege()==null? "NOT SET" : student.getCollege());
        lbl_section.setText(studInfo.getSection());
        
        SimpleTableCell cellParent = new SimpleTableCell();
        cellParent.setResizePriority(Priority.ALWAYS);
        cellParent.setContent(studentRow);

        row.addCell(cellParent);
        
        row.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            showStudentInfo(student);
        });
        
        row.getRowMetaData().put(KEY_MORE_INFO, studInfo);
        table.addRow(row);
    }
    
    private void showStudentInfo(StudentMapping currentStudent) {
        InfoStudentController controller = new InfoStudentController(currentStudent);
//        Mono.fx().create()
//                .setPackageName("update5.org.cict.student.layout")
//                .setFxmlDocument("InfoStudent")
//                .makeFX()
//                .setController(controller)
//                .makeScene()
//                .makeStageWithOwner(Mono.fx().getParentStage(application_root))
//                .stageResizeable(false)
//                .stageShowAndWait();
        LayoutDataFX home = new LayoutDataFX(application_root, this);
        controller.setHomeFX(home);
        Pane fxRoot = Mono.fx().create()
                .setPackageName("update5.org.cict.student.layout")
                .setFxmlDocument("InfoStudent")
                .makeFX()
                .setController(controller)
                .pullOutLayout();

        Animate.fade(application_root, 150, () -> {
            super.replaceRoot(fxRoot);
        }, fxRoot);
    }
    
    private void setComboBox() {
        cmb_category_main.getItems().clear();
        cmb_category_main.getItems().add("Name");
        cmb_category_main.getItems().add("Student Number");
        cmb_category_main.getItems().add("Academic Program");
        cmb_category_main.getSelectionModel().selectFirst();
        
        cmb_category.getItems().clear();
        cmb_category.getItems().add("Name");
        cmb_category.getItems().add("Student Number");
        cmb_category.getItems().add("Academic Program");
        cmb_category.getSelectionModel().selectFirst();
    }

    private class FetchStudents extends Transaction {
        private Criterion search_key;
        public void setCriterion(Criterion crtrn) {
            this.search_key = crtrn;
        }
        
        private String section_key;
        public void setSectionKey(String str) {
            this.section_key = str;
        }
        
        private Integer group_key;
        public void setGroupKey(Integer str) {
            this.group_key = str;
        }
        
        private Boolean isEnrolledSelected = false;
        private void setEnrolledSelected(Boolean res) {
            this.isEnrolledSelected = res;
        }
        
        private ArrayList<StudentMapping> students = new ArrayList<>();
        private ArrayList<StudentMapping> enrolledStudents = new ArrayList<>();
        public ArrayList<StudentMapping> getResult() {
            return isEnrolledSelected? enrolledStudents : students;
        }
        
        public ArrayList<StudentMapping> getEnrolledStudents() {
            return enrolledStudents;
        }
        
        private String msg = "";
        public String getMessage() {
            return msg;
        }
        
        private Boolean isSearchingForAcadProgCode = false;
        private void isSearchingForAcadProgCode(Boolean is_it) {
            this.isSearchingForAcadProgCode = is_it;
        }
        
        @Override
        protected boolean transaction() {
            if(!isSearchingForAcadProgCode) {
                students = Mono.orm().newSearch(Database.connect().student())
                        .put(search_key)
                        .active(Order.asc(DB.student().last_name)).all();
                
                if(students==null? true: students.isEmpty()) {
                    msg = "No student found with the given key.";
                    return false;
                }
            } else {
                ArrayList<AcademicProgramMapping> acads = Mono.orm().newSearch(Database.connect().academic_program())
                        .put(search_key).active().all();
                if(acads==null) {
                    msg = "No academic program found with the given key.";
                    return false;
                }
                for(AcademicProgramMapping acad:acads){
                    ArrayList<CurriculumMapping> curriculums = Mono.orm().newSearch(Database.connect().curriculum())
                            .eq(DB.curriculum().ACADPROG_id, acad.getId()).active().all();
                    if(curriculums==null)
                        continue;
                    for(CurriculumMapping curriculum: curriculums) {
                        ArrayList<StudentMapping> students_temp = Mono.orm().newSearch(Database.connect().student())
                                .eq(DB.student().CURRICULUM_id, curriculum.getId())
                                .active(Order.asc(DB.student().last_name)).all();
                        if(students_temp==null? true: students_temp.isEmpty()) {
                            continue;
                        }
                        if(section_key!=null) {
                            int count = 0;
                            for(StudentMapping student_temp: students_temp){
                                if(student_temp.getSection()!=null && student_temp.getYear_level()!=null) {
                                    if(section_key.equalsIgnoreCase(student_temp.getYear_level() + student_temp.getSection())) {
                                        if(group_key!=null) {
                                            if(student_temp.get_group()!=null) {
                                                if(group_key.equals(student_temp.get_group())) {
                                                    students.add(student_temp);
                                                    count++;
                                                }
                                            }
                                        } else {
                                            students.add(student_temp);
                                            count++;
                                        }
                                    }
                                }
                            }
                            System.out.println("FETCH FOUND: " + count);
                        } else {
                            students.addAll(students_temp);
                            System.out.println("FETCH FOUND: " + students_temp.size());
                        }
                    }
                }
            }
            System.out.println("FETCH TOTAL SIZE: " + students.size());
            if(!isEnrolledSelected) {
                return true;
            }
            AcademicTermMapping acadTerm = SystemProperties.instance().getCurrentAcademicTerm();
            for(StudentMapping student:students) {
                if(student.getLast_evaluation_term() != null){
                    if(acadTerm==null) {
                        enrolledStudents.add(student);
                        continue;
                    }
                    if(student.getLast_evaluation_term().equals(acadTerm.getId()))
                        enrolledStudents.add(student);
                }
            }
            return true;
        }

        @Override
        protected void after() {
        
        }
        
    }
}

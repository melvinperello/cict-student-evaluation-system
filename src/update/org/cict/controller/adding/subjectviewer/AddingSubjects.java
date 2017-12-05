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
package update.org.cict.controller.adding.subjectviewer;

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
import com.jhmvin.transitions.Animate;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.cict.PublicConstants;
import org.controlsfx.control.Notifications;
import update.org.cict.controller.adding.AddSubjectSuggestion;
import update.org.cict.controller.adding.SearchSection;
import update.org.cict.controller.adding.SubjectInformationHolder;
import update3.org.cict.access.SystemOverriding;

/**
 *
 * @author Joemar
 */
public class AddingSubjects extends SceneFX implements ControllerFX{

    @FXML
    private AnchorPane anchor_main;

    @FXML
    private TextField txtSearch;

    @FXML
    private JFXButton btnSearch;

    @FXML
    private JFXButton btnShowAll;

    @FXML
    private AnchorPane anchor_result;

    @FXML
    private HBox hbox_no_result;

    @FXML
    private HBox hbox_search;

    @FXML
    private AnchorPane anchor_view;

    @FXML
    private VBox vbox_subjectList;
    
    private String studentNumber;
    private ArrayList<SubjectInformationHolder> suggestedSubject;
    private ArrayList<ArrayList<Object>> sectionSearched;
    private final static String KEY_MORE_INFO = "MORE_INFO";
    private Integer CURRICULUM_id;
    
    public void setStudentNumber(String studNum, Integer curriculum_id) {
        this.studentNumber = studNum;
        CURRICULUM_id = curriculum_id;
    }
    
    @Override
    public void onInitialization() {
        
    }
    
    public void callBack() {
        this.onSearchStart();
    }

    @Override
    public void onEventHandling() {
        txtSearch.textProperty().addListener(listener -> {
            if (txtSearch.getText().isEmpty()) {
                if (this.suggestedSubject != null) {
                    this.createSubjectTable(this.suggestedSubject);
                }
            }
        });

        btnSearch.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            searchClicked();
        });

        btnShowAll.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            this.createSubjectTable(this.suggestedSubject);
            this.btnShowAll.setDisable(true);
        });
        
        Mono.fx().key(KeyCode.ENTER).release(anchor_main, () -> {
            searchClicked();
        });
    }
    
    private void searchClicked() {
        if (!txtSearch.getText().equals("")) {
            this.btnShowAll.setDisable(false);
//            this.hbox_search.setVisible(true);
//            this.hbox_no_result.setVisible(false);
//            this.vbox_subjectList.setVisible(true);
            changeView(vbox_subjectList);
            this.vbox_subjectList.getChildren().clear();

            ArrayList<SubjectInformationHolder> subjectSearched = this.searchSubject(MonoString.removeExtraSpace(txtSearch.getText()).toUpperCase());
            this.createSubjectTable(subjectSearched);
        } else {
            this.btnShowAll.setDisable(true);
            if (this.suggestedSubject != null) {
                this.createSubjectTable(this.suggestedSubject);
            }
        }
    }
    
    private void onSearchStart() {
        AddSubjectSuggestion search = new AddSubjectSuggestion();
        search.studentNumber = this.studentNumber;
//        this.hbox_search.setVisible(true);
//        this.hbox_no_result.setVisible(false);
        
        search.setOnSuccess(onSuccess -> {
            this.suggestedSubject = search.getSubjectSuggestion();
            if(suggestedSubject.isEmpty()) {
//                this.hbox_search.setVisible(false);
//                this.hbox_no_result.setVisible(true);
                changeView(hbox_no_result);
            } else 
                this.createSubjectTable(this.suggestedSubject);
        });

        search.setOnFailure(onFailure -> {
//            this.hbox_search.setVisible(false);
//            this.hbox_no_result.setVisible(true);
            changeView(hbox_no_result);
        });

        search.setOnCancel(onCancel -> {
//            this.hbox_no_result.setVisible(true);
//            this.hbox_search.setVisible(false);
            changeView(hbox_no_result);
        });
        search.setRestTime(1000);
        search.transact();
    }
    
    /**
     * Creates the subject choices table.
     *
     * @param subjectsToDisplay Array list of subject mapping.
     */
    private void createSubjectTable(ArrayList<SubjectInformationHolder> subjectsToDisplay) {
        try {
//            this.hbox_search.setVisible(false);
            SimpleTable tableSubject = new SimpleTable();
            for (int i = 0; i < subjectsToDisplay.size(); i++) {
                SubjectMapping currentSubject = subjectsToDisplay.get(i).getSubjectMap();
                createRow(currentSubject, tableSubject);
            }
            
            changeView(vbox_subjectList);
//            vbox_subjectList.setVisible(true);
            SimpleTableView simpleTableView = new SimpleTableView();
            simpleTableView.setTable(tableSubject);
            simpleTableView.setFixedWidth(true);
            simpleTableView.setParentOnScene(vbox_subjectList);
        } catch (NullPointerException a) {
//            this.hbox_no_result.setVisible(true);
//            this.hbox_search.setVisible(false);
            this.btnShowAll.setDisable(false);
            changeView(hbox_no_result);
        }
    }
    
    private void createRow(SubjectMapping subject, SimpleTable table) {
        
        SimpleTableRow row = new SimpleTableRow();
        row.setRowHeight(70.0);

        HBox subjectRow = (HBox) Mono.fx().create()
                .setPackageName("update.org.cict.layout.adding_changing.adding")
                .setFxmlDocument("adding-row")
                .makeFX()
                .pullOutLayout();
        
        Label lbl_code = searchAccessibilityText(subjectRow, "code");
        Label lbl_descriptive_title = searchAccessibilityText(subjectRow, "descript");
        Label lbl_lec = searchAccessibilityText(subjectRow, "lec");
        Label lbl_lab = searchAccessibilityText(subjectRow, "lab");
        
        lbl_code.setText(subject.getCode());
        lbl_descriptive_title.setText(subject.getDescriptive_title());
        lbl_lec.setText(subject.getLec_units().toString());
        lbl_lab.setText(subject.getLab_units().toString());
        
        SimpleTableCell cellParent = new SimpleTableCell();
        cellParent.setResizePriority(Priority.ALWAYS);
        cellParent.setContent(subjectRow);

        row.addCell(cellParent);
        
        /**
         * Method to view available sections of the selected subject.
         */
        row.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            vbox_subjectList.getChildren().clear();
            btnShowAll.setDisable(false);
            this.searchSection(subject);
        });
        row.getRowMetaData().put(KEY_MORE_INFO, subject);
        table.addRow(row);
    }
    
    /**
     * fetch the data of the available sections.
     *
     * @param subject the subject mapping.
     */
    private void searchSection(SubjectMapping subject) {
        SearchSection search = new SearchSection();
        search.subjectCode = subject.getId();
        search.CURRICULUM_id = CURRICULUM_id;
        changeView(hbox_search);
        search.setOnSuccess(onSuccess -> {
            this.sectionSearched = search.getSearchResults();
            /**
             * Call create section table.
             */
            if(sectionSearched.isEmpty()) {
//                this.vbox_subjectList.setVisible(false);
//                this.hbox_no_result.setVisible(true);
                changeView(hbox_no_result);
            } else
                this.createSectionTable(this.sectionSearched, subject, search.getSubInfo());
        });

        search.setOnFailure(onFailure -> {
//            this.vbox_subjectList.setVisible(false);
//            this.hbox_no_result.setVisible(true);
            changeView(hbox_no_result);
        });

        search.setOnCancel(onCancel -> {
//            this.vbox_subjectList.setVisible(false);
//            this.hbox_no_result.setVisible(true);
            changeView(hbox_no_result);
        });
        search.setRestTime(1000);
        search.transact();
    }
    
    /**
     * Displays the actual table of available sections of a particular subject.
     *
     * @param sectionsToDisplay
     * @param searched
     */
    private void createSectionTable(ArrayList<ArrayList<Object>> sectionsToDisplay, SubjectMapping searched, SubjectInformationHolder subInfo) {
        try {
            changeView(vbox_subjectList);
//            this.hbox_search.setVisible(false);
            SimpleTable tableSection = new SimpleTable();
            for (int i = 0; i < sectionsToDisplay.size(); i++) {
                ArrayList<Object> currentSection = sectionsToDisplay.get(i);
                String studentCount = (String) currentSection.get(0);
                SubjectInformationHolder subInfos = (SubjectInformationHolder) currentSection.get(1);
                try {
                    subInfos.setStudentCount(Integer.valueOf(studentCount));
                } catch (Exception e) {
                }
                SimpleTableRow row = new SimpleTableRow();
                row.setRowHeight(70.0);

                HBox sectionRow = (HBox) Mono.fx().create()
                        .setPackageName("update.org.cict.layout.adding_changing.adding")
                        .setFxmlDocument("section-row")
                        .makeFX()
                        .pullOutLayout();
                
                Label lbl_section = searchAccessibilityText(sectionRow, "section");
                Label lbl_code = searchAccessibilityText(sectionRow, "code");
                Label lbl_descriptive_title = searchAccessibilityText(sectionRow, "descript");
                Label lbl_count = searchAccessibilityText(sectionRow, "count");
                Label lbl_curriculum = searchAccessibilityText(sectionRow, "curriculum");
        
        
                lbl_section.setText(subInfos.getFullSectionName());
                lbl_code.setText(subInfos.getSubjectMap().getCode());
                lbl_descriptive_title.setText(subInfos.getSubjectMap().getDescriptive_title());
                lbl_count.setText(studentCount);
                lbl_curriculum.setText(subInfos.getCurriculum()==null? "No Curriculum": subInfos.getCurriculum().getName());
                
                SimpleTableCell cellParent = new SimpleTableCell();
                cellParent.setResizePriority(Priority.ALWAYS);
                cellParent.setContent(sectionRow);
                row.addCell(cellParent);
                
                row.getRowMetaData().put(KEY_MORE_INFO, subInfos);

                // meta data is not usable with this scope of code.
                // this snippet does not required use of any extended information
                // now we have a complete subject information holder here
                // we can now add an event that will pass this value to the parent window.
                row.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    // check if max population reached before adding
                    SubjectInformationHolder info = (SubjectInformationHolder) row.getRowMetaData().get(KEY_MORE_INFO);
                    
                    
                    
                    System.out.println("STUDENT COUNT: " + (info.getStudentCount()==null? 0: info.getStudentCount()));
                    System.out.println(PublicConstants.getSystemVar_MAX_POPULATION());
                    
                    Integer MAX_VALUE = 1;
                    try {
                        MAX_VALUE = Integer.valueOf(PublicConstants.getSystemVar_MAX_POPULATION().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if((info.getStudentCount()==null? 0: info.getStudentCount()) >= MAX_VALUE) {
                        AddingDataPipe.instance().isMaxPopulationReached = true;
                    }
                    
                    // check if this window was open thru change request
                    // use the adding data pipe

                    int res = Mono.fx().alert()
                            .createConfirmation()
                            .setHeader("Add Subject")
                            .setMessage("Are you sure you want to add "
                                    + subInfos.getSubjectMap().getCode() + " to the list?")
                            .confirmYesNo();
                    if(res == 1) {
                        if (AddingDataPipe.instance().isChanged) {
                            AddingDataPipe.instance().isChanged = false; // make it false again
                            AddingDataPipe.instance().isChangedValueRecieved = true; // mark value as received
                            AddingDataPipe.instance().isChangedValue = subInfos; // attached the value

                            // now close this window
                            Mono.fx().getParentStage(row).close();
                        }
                    }
                });

                tableSection.addRow(row);
            }

            SimpleTableView simpleTableView = new SimpleTableView();
            simpleTableView.setTable(tableSection);
            simpleTableView.setFixedWidth(true);
            simpleTableView.setParentOnScene(vbox_subjectList);
        } catch (NullPointerException a) {
//            this.hbox_no_result.setVisible(true);
//            this.hbox_search.setVisible(false);
            changeView(hbox_no_result);
            a.printStackTrace();
        }
    }
    
    public ArrayList<SubjectInformationHolder> searchSubject(String code) {
        try {
            ArrayList<SubjectInformationHolder> temp = new ArrayList<>();
            for (int i = 0; i < this.suggestedSubject.size(); i++) {
                SubjectMapping subject = this.suggestedSubject.get(i).getSubjectMap();
                if (subject.getCode().equalsIgnoreCase(code)
                        || subject.getCode().contains(code)) {
                    temp.add(this.suggestedSubject.get(i));
                }
            }
            if(temp.isEmpty()) {
//                this.hbox_no_result.setVisible(true);
//                this.vbox_subjectList.setVisible(false);
                changeView(hbox_no_result);
                return null;
            }
            return temp;
        }catch(NullPointerException a) {
//            this.hbox_no_result.setVisible(true);
//            this.vbox_subjectList.setVisible(false);
            changeView(hbox_no_result);
        }
        return null;
    }
    
    private void changeView(Node showMe) {
        Animate.fade(showMe, 150, ()->{
            this.hbox_no_result.setVisible(false);
            this.vbox_subjectList.setVisible(false);
            this.hbox_search.setVisible(false);
            showMe.setVisible(true);
        }, hbox_no_result, hbox_search, vbox_subjectList);
    }
}

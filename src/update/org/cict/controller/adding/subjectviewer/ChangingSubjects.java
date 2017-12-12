package update.org.cict.controller.adding.subjectviewer;

import app.lazy.models.SubjectMapping;
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
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import update.org.cict.controller.adding.SearchSection;
import update.org.cict.controller.adding.ChangeSubjectSuggestion;
import update.org.cict.controller.adding.SubjectInformationHolder;

public class ChangingSubjects extends SceneFX implements ControllerFX {

    @FXML
    private AnchorPane anchor_main;

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

    private SubjectInformationHolder subjectInfo;
    private Integer studentNumber;
    private ArrayList<SubjectInformationHolder> suggestedSubject;
    private Integer CURRICULUM_id;
    private double totalUnits;
    /**
     * Changed to type Object to give way for the load section mapping as third
     * index.
     */
    private ArrayList<ArrayList<Object>> sectionSearched;

    /**
     * Isolates the string from the code logic.
     */
    private final static String KEY_MORE_INFO = "MORE_INFO";

    public void setSubjectInfo(SubjectInformationHolder subjectInfo) {
        this.subjectInfo = subjectInfo;
    }

    public void setStudentNumber(Integer studNum) {
        this.studentNumber = studNum;
    }
    
    public ChangingSubjects(Integer CURRICULUM_id, double totalUnits) {
        this.CURRICULUM_id = CURRICULUM_id;
        this.totalUnits = totalUnits;
    }
    
    @Override
    public void onInitialization() {

    }

    @Override
    public void onEventHandling() {
        
    }

    public void callBack() {
        this.onSearchStart();
    }

    private void onSearchStart() {
        ChangeSubjectSuggestion search = new ChangeSubjectSuggestion();
        search.studentNumber = this.studentNumber;
        search.subjectInfoHolder = this.subjectInfo;
        search.totalUnits = totalUnits;
        search.setOnSuccess(onSuccess -> {
            this.suggestedSubject = search.getSubjectSuggestion();
            System.out.println("RES: " + suggestedSubject.size());
            this.createSubjectTable(this.suggestedSubject);
        });

        search.setOnFailure(onFailure -> {
//            this.vbox_subjectList.setVisible(false);
//            this.hbox_no_result.setVisible(true);
            changeView(hbox_no_result);
        });

        search.setOnCancel(onCancel -> {
//            this.hbox_no_result.setVisible(true);
//            this.hbox_search.setVisible(false);
            changeView(hbox_no_result);
        });

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
                System.out.println("ADD TO ROW SUBJECT CODE: " + currentSubject.getCode());
                createRow(currentSubject, tableSubject);
            }
            System.out.println("TABLE: " + tableSubject.getChildren().size());
            changeView(vbox_subjectList);
            SimpleTableView simpleTableView = new SimpleTableView();
            simpleTableView.setTable(tableSubject);
            simpleTableView.setFixedWidth(true);
            simpleTableView.setParentOnScene(vbox_subjectList);
            System.out.println("VBOX: "+vbox_subjectList.getChildren().size());
            System.out.println("vbox_subjectList is visible? " + vbox_subjectList.isVisible());
        } catch (NullPointerException a) {
//            this.hbox_no_result.setVisible(true);
//            this.hbox_search.setVisible(false);
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
            this.searchSection((SubjectMapping) row.getRowMetaData().get(KEY_MORE_INFO));
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
        changeView(hbox_search);
        SearchSection search = new SearchSection();
        search.subjectCode = subject.getId();
        search.CURRICULUM_id = CURRICULUM_id;
        
        search.setOnSuccess(onSuccess -> {
            this.sectionSearched = search.getSearchResults();
            /**
             * Call create section table.
             */
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
//            this.hbox_search.setVisible(false);
            changeView(vbox_subjectList);
            SimpleTable tableSubject = new SimpleTable();
            for (int i = 0; i < sectionsToDisplay.size(); i++) {
                ArrayList<Object> currentSection = sectionsToDisplay.get(i);
                String studentCount = (String) currentSection.get(0);
                SubjectInformationHolder subInfos = (SubjectInformationHolder) currentSection.get(1);

                /**
                 * Load section mapping as third index.
                 */
//                LoadSectionMapping load_sec_map = (LoadSectionMapping) currentSection.get(2);
                // add this mapping to our sub info
//                subInfo.setSectionMap(load_sec_map);
                // since this is a search for a section with the same subject mapping
                // same academic program
                // the difference only is the section mapping
                // every time the loop iterates
                // only the section mapping will be changed.

                // subject info holder has a feature to get the section name
                // thus replacing this line with
                //
                // String subject_code = (String) currentSection.get(0);
                //
                // this one
//                String subject_code = subInfo.getFullSectionName();
                // the above code is also plausible it will give the exact output
                // but we need to return a subject information holder to the parent window
                // thus making this approach more approriate

                // only the population is outside of scope that only needs separate transaction.
                // this transaction of adding subject has a different approach than
                // suggestions of subject from evaluation.

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
                    // check if this window was open thru change request
                    // use the adding data pipe
                    if (AddingDataPipe.instance().isChanged) {
                        AddingDataPipe.instance().isChanged = false; // make it false again
                        AddingDataPipe.instance().isChangedValueRecieved = true; // mark value as received
                        AddingDataPipe.instance().isChangedValue = subInfos; // attached the value

                        // now close this window
                        Mono.fx().getParentStage(row).close();
                    } else {
                        // do nothing.
                    }
                });

                tableSubject.addRow(row);
            }

            SimpleTableView simpleTableView = new SimpleTableView();
            simpleTableView.setTable(tableSubject);
            simpleTableView.setFixedWidth(true);
            simpleTableView.setParentOnScene(vbox_subjectList);
        } catch (NullPointerException a) {
//            this.hbox_no_result.setVisible(true);
//            this.hbox_search.setVisible(false);
            changeView(hbox_no_result);
            a.printStackTrace();
        }
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

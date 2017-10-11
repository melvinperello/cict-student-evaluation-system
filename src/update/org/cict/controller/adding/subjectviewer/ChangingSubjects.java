package update.org.cict.controller.adding.subjectviewer;

import app.lazy.models.SubjectMapping;
import com.jhmvin.Mono;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
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
    private String studentNumber;
    private ArrayList<SubjectInformationHolder> suggestedSubject;
    private Integer CURRICULUM_id;
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

    public void setStudentNumber(String studNum) {
        this.studentNumber = studNum;
    }
    
    public ChangingSubjects(Integer CURRICULUM_id) {
        this.CURRICULUM_id = CURRICULUM_id;
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
        search.setOnSuccess(onSuccess -> {
            this.suggestedSubject = search.getSubjectSuggestion();
            this.createSubjectTable(this.suggestedSubject);
        });

        search.setOnFailure(onFailure -> {
            this.vbox_subjectList.setVisible(false);
            this.hbox_no_result.setVisible(true);
        });

        search.setOnCancel(onCancel -> {
            this.hbox_no_result.setVisible(true);
            this.hbox_search.setVisible(false);
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
            SimpleTable tableSubject = new SimpleTable();
            for (int i = 0; i < subjectsToDisplay.size(); i++) {
                SubjectMapping currentSubject = subjectsToDisplay.get(i).getSubjectMap();

//                SimpleTableRow row = new SimpleTableRow();
//                row.setRowHeight(50.0);
//
//                SimpleTableCell cellIcon = new SimpleTableCell();
//                cellIcon.setPrefWidth(50.0);
//                cellIcon.setCellPadding(0.0, 30.0);
//
//                ImageView img = new ImageView();
//                img.setFitHeight(50.0);
//                img.setFitWidth(50.0);
//                img.setImage(new Image("update/org/cict/layout/adding_changing/img/book.png"));
//                Label lbl = new Label();
//                lbl.setGraphic(img);
//                cellIcon.setContent(lbl);
//                row.addCell(cellIcon);
//
//                /**
//                 * SUBJECT CODE
//                 */
//                SimpleTableCell cellCode = new SimpleTableCell();
//                cellCode.setPrefWidth(180.0);
//                cellCode.setCellPadding(0.0, 10.0);
//                cellCode.setContent(new Label(currentSubject.getCode()));
//                cellCode.addContentCssClass("table-cell-content-code");
//                row.addCell(cellCode);
//
//                /**
//                 * DESCRIPTION
//                 */
//                SimpleTableCell cellDescription = new SimpleTableCell();
//                cellDescription.setPrefWidth(350.0);
////                cellDescription.setResizePriority(Priority.ALWAYS);
//                cellDescription.setContent(new Label(currentSubject.getDescriptive_title()));
//                cellDescription.addContentCssClass("table-cell-content-description");
//                row.addCell(cellDescription);
//
//
//            /**
//             * Lec Label
//             */
//            //lec icon
//            ImageView lecIcon = new ImageView();
//            lecIcon.setFitWidth(15.0);
//            lecIcon.setFitHeight(15.0);
//            lecIcon.setImage(new Image("res/img/lec.png"));
//            // icon column
//            SimpleTableCell cellLecLabel = new SimpleTableCell();
//            cellLecLabel.setPrefWidth(30.0);
//            //
//            Label content_lec = new Label();
//            content_lec.setText("LEC");
//            content_lec.setGraphic(lecIcon);
//            content_lec.setContentDisplay(ContentDisplay.TOP);
//            //
//            cellLecLabel.setContent(content_lec);
//            cellLecLabel.addContentCssClass("table-cell-content-image-label");
//            // add cell to row
//            row.addCell(cellLecLabel);
//
//            // lec units
//            SimpleTableCell cellLecUnits = new SimpleTableCell();
//            cellLecUnits.setPrefWidth(50.0);
//            //
//            Label content_lect_units = new Label();
//            content_lect_units.setText(currentSubject.getLec_units().toString());
//            //
//            cellLecUnits.setContent(content_lect_units);
//            cellLecUnits.addContentCssClass("table-cell-content-units");
//            // add cell to row
//            row.addCell(cellLecUnits);
//
//            /**
//             * Lab Label
//             */
//            //lab icon
//            ImageView labIcon = new ImageView();
//            labIcon.setFitWidth(15.0);
//            labIcon.setFitHeight(15.0);
//            labIcon.setImage(new Image("res/img/lab.png"));
//            // icon column
//            SimpleTableCell cellLabLabel = new SimpleTableCell();
//            cellLabLabel.setPrefWidth(40.0);
//            cellLabLabel.setCellPadding(0.0, 10.0);
//            // node
//            Label content_lab = new Label();
//            content_lab.setText("LAB");
//            content_lab.setGraphic(labIcon);
//            content_lab.setContentDisplay(ContentDisplay.TOP);
//            //
//            cellLabLabel.setContent(content_lab);
//            cellLabLabel.addContentCssClass("table-cell-content-image-label");
//            // add cell to row
//            row.addCell(cellLabLabel);
//
//            // lab units
//            SimpleTableCell cellLabUnits = new SimpleTableCell();
//            cellLabUnits.setPrefWidth(50.0);
//            //
//            Label content_lab_units = new Label();
//            content_lab_units.setText(currentSubject.getLab_units().toString());
//            //
//            cellLabUnits.setContent(content_lab_units);
//            cellLabUnits.addContentCssClass("table-cell-content-units");
//            // add cell to row
//            row.addCell(cellLabUnits);
//
//                /**
//                 * View Section
//                 */
//                SimpleTableCell cellSection = new SimpleTableCell();
//                cellSection.setPrefWidth(200.0);
//                Label content_section = new Label();
//                content_section.setText("Click to view sections");
//                content_section.setContentDisplay(ContentDisplay.CENTER);
//
//                /**
//                 * Method to view available sections of the selected subject.
//                 */
//                row.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
//                    vbox_subjectList.getChildren().clear();
//                    this.searchSection(currentSubject);
//                });
//
//                cellSection.setContent(content_section);
//                cellSection.setCellPadding(20.0, 10.0);
//                cellSection.addContentCssClass("table-cell-content-view");
//                row.addCell(cellSection);
//
//                row.getRowMetaData().put(KEY_MORE_INFO, currentSubject);
//                tableSubject.addRow(row);
                createRow(currentSubject, tableSubject);
            }

            SimpleTableView simpleTableView = new SimpleTableView();
            simpleTableView.setTable(tableSubject);
            simpleTableView.setFixedWidth(true);
            simpleTableView.setParentOnScene(vbox_subjectList);
        } catch (NullPointerException a) {
            this.hbox_no_result.setVisible(true);
            this.hbox_search.setVisible(false);
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
        search.subjectCode = subject.getCode();
        search.CURRICULUM_id = CURRICULUM_id;
        
        search.setOnSuccess(onSuccess -> {
            this.sectionSearched = search.getSearchResults();
            /**
             * Call create section table.
             */
            this.createSectionTable(this.sectionSearched, subject, search.getSubInfo());
        });

        search.setOnFailure(onFailure -> {
            this.vbox_subjectList.setVisible(false);
            this.hbox_no_result.setVisible(true);
        });

        search.setOnCancel(onCancel -> {
            this.vbox_subjectList.setVisible(false);
            this.hbox_no_result.setVisible(true);
        });

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

                /**
                 * SUBJECT CODE
                 */
//                SimpleTableCell cellIcon = new SimpleTableCell();
//                cellIcon.setPrefWidth(50.0);
//                cellIcon.setCellPadding(0.0, 30.0);
//
//                ImageView img = new ImageView();
//                img.setFitHeight(50.0);
//                img.setFitWidth(50.0);
//                img.setImage(new Image("res/img/Google Classroom_96px.png"));
//                Label lbl = new Label();
//                lbl.setGraphic(img);
//                cellIcon.setContent(lbl);
//                row.addCell(cellIcon);
//
//                SimpleTableCell cellCode = new SimpleTableCell();
//                cellCode.setPrefWidth(200.0);
//                cellCode.setCellPadding(10.0, 5.0);
//                cellCode.setContent(new Label(subInfos.getFullSectionName()));
//                cellCode.addContentCssClass("table-cell-content-section");
//                row.addCell(cellCode);
//
//                /**
//                 * DESCRIPTION
//                 */
//                SimpleTableCell cellDescription = new SimpleTableCell();
//                cellDescription.setResizePriority(Priority.ALWAYS);
//
//                Label lblCode = new Label(subInfos.getSubjectMap().getCode());
//                lblCode.setTextFill(Paint.valueOf("#15315C"));
//
//                Label lblDescp = new Label(subInfos.getSubjectMap().getDescriptive_title());
//                lblDescp.setTextFill(Paint.valueOf("#15315C"));
//
//                VBox vb = new VBox();
//                vb.setAlignment(Pos.CENTER_LEFT);
//                vb.getChildren().add(lblCode);
//                vb.getChildren().add(lblDescp);
//
//                cellDescription.setContent(vb);
//                cellDescription.addContentCssClass("table-cell-content-subject");
//                row.addCell(cellDescription);
//
//                /**
//                 * Student
//                 */
//                ImageView studentImg = new ImageView();
//                studentImg.setFitWidth(25.0);
//                studentImg.setFitHeight(25.0);
//                studentImg.setImage(new Image("res/img/Graduation Cap_104px.png"));
//
//                SimpleTableCell cellCountImage = new SimpleTableCell();
//                cellCountImage.setPrefWidth(80.0);
//                cellCountImage.setCellPadding(0.0, 5.0);
//
//                Label lblStudent = new Label();
//                lblStudent.setText("Student(s)");
//                lblStudent.setGraphic(studentImg);
//                lblStudent.setContentDisplay(ContentDisplay.TOP);
//
//                cellCountImage.setContent(lblStudent);
//                cellCountImage.addContentCssClass("table-cell-content-image-label");
//                row.addCell(cellCountImage);
//
//                /**
//                 * Count
//                 */
//                Label content_lec = new Label();
//                content_lec.setText(studentCount);
//                content_lec.setContentDisplay(ContentDisplay.LEFT);
//
//                SimpleTableCell cellCount = new SimpleTableCell();
//                cellCount.setPrefWidth(90.0);
//                cellCount.setCellPadding(10.0, 5.0);
//                cellCount.setContent(content_lec);
//                cellCount.addContentCssClass("table-cell-content-count");
//                row.addCell(cellCount);

                HBox sectionRow = (HBox) Mono.fx().create()
                        .setPackageName("update.org.cict.layout.adding_changing.adding")
                        .setFxmlDocument("section-row")
                        .makeFX()
                        .pullOutLayout();
                
                Label lbl_section = searchAccessibilityText(sectionRow, "section");
                Label lbl_code = searchAccessibilityText(sectionRow, "code");
                Label lbl_descriptive_title = searchAccessibilityText(sectionRow, "descript");
                Label lbl_count = searchAccessibilityText(sectionRow, "count");
        
                lbl_section.setText(subInfos.getFullSectionName());
                lbl_code.setText(subInfos.getSubjectMap().getCode());
                lbl_descriptive_title.setText(subInfos.getSubjectMap().getDescriptive_title());
                lbl_count.setText(studentCount);
                
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
            this.hbox_no_result.setVisible(true);
            this.hbox_search.setVisible(false);
            a.printStackTrace();
        }
    }
}

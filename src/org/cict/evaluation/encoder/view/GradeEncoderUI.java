package org.cict.evaluation.encoder.view;

import app.lazy.models.AccountFacultyMapping;
import app.lazy.models.CurriculumSubjectMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.GradeMapping;
import app.lazy.models.StudentMapping;
import app.lazy.models.SubjectMapping;
import static com.itextpdf.text.SpecialSymbol.index;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jhmvin.Mono;

import java.util.ArrayList;
import java.util.Objects;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.bsu.cict.alerts.MessageBox;
import org.cict.evaluation.assessment.AssessmentResults;
import org.cict.evaluation.assessment.CurricularLevelAssesor;
import org.cict.evaluation.encoder.SpreadSheetGradeEncoder;
import org.cict.evaluation.evaluator.Evaluator;
import org.controlsfx.control.Notifications;

import org.controlsfx.control.spreadsheet.GridBase;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetCellType;
import org.controlsfx.control.spreadsheet.SpreadsheetView;
import org.hibernate.criterion.Order;
import update.org.cict.controller.adding.ValidateAddingSubject;
import update3.org.cict.access.Access;

public class GradeEncoderUI {

    private void logs(String str) {
        if (true) {
            System.out.println("@GradeEncoderUI: " + str);
        }
    }

    //--------------------------------------------------------------------------
//    private final Integer FACULTY_id;
//    private Integer ACAD_TERM_id;
//    private Date POSTED_DATE;
//    private boolean POSTED;
//    private String MODE = "";
    private Integer CURRICULUM_id, yearLevel, semester;

//    private HBox pnl_spreadsheet;
//
//    public void setPnl_spreadsheet(HBox pnl_spreadsheet) {
//        this.pnl_spreadsheet = pnl_spreadsheet;
//    }
    public GradeEncoderUI() {
//        this.POSTED = false;
        /**
         * @improper_date: 09.02.2017
         */
//        this.FACULTY_id = CollegeFaculty.instance().getFACULTY_ID();
//        this.POSTED_DATE = Mono.orm().getServerTime().getDateWithFormat();
        initialize();
    }

//    public void setMode(String mode) {
//        this.MODE = mode;
//    }
//    public void setAcadTermId(Integer id) {
//        this.ACAD_TERM_id = id;
//    }
    /**
     * Student Mapping.
     */
    private StudentMapping studentMap;

    public void setStudentMap(StudentMapping student) {
        this.studentMap = student;
        this.CURRICULUM_id = student.getCURRICULUM_id();
        this.CICT_id = student.getCict_id();
    }

    public void setYearAndSem(Integer yr, Integer sem) {
        //this.CURRICULUM_id = id;
        this.yearLevel = yr;
        this.semester = sem;
    }

    //----------------------------------------------------
//    public void setCictId(Integer cict_id) {
//        // get student curriculum
//        this.CICT_id = cict_id;
//        studentMap = (StudentMapping) Database.connect().student().getPrimary(cict_id);
//    }
    //-----------------------------------------------------
    private boolean valueFiltering = true;

    /**
     * Sets if the spreadsheet will filter values or not.
     *
     * @param valueFiltering
     */
    public void setValueFiltering(boolean valueFiltering) {
        this.valueFiltering = valueFiltering;
    }

    public void setSubjectsToBePrinted(ArrayList<SubjectMapping> subjects) {
        this.subjectsToBePrinted = subjects;
    }

    private void initialize() {

    }

    /**
     * <@Table Rating JavaFX table>
     * this codes are meant to store data to rating table invokes the class
     * RatingTableClass as object handler
     */
    /**
     * create columns for the table
     *
     * @return return the table columns for Rating Table
     */
    private ArrayList<JFXTreeTableColumn<RatingTableClass, String>> createGradeColumn() {
        try {
            JFXTreeTableColumn<RatingTableClass, String> col_rating = tableColumnFactory("Rating", 120, false, false);
            col_rating.setCellValueFactory((TreeTableColumn.CellDataFeatures<RatingTableClass, String> param) -> param.getValue().getValue().rating);

            JFXTreeTableColumn<RatingTableClass, String> col_eqv = tableColumnFactory("Equivalent", 130, false, false);
            col_eqv.setCellValueFactory((TreeTableColumn.CellDataFeatures<RatingTableClass, String> param) -> param.getValue().getValue().equivalent);

            JFXTreeTableColumn<RatingTableClass, String> col_remarks = tableColumnFactory("Remarks", 130, false, false);
            col_remarks.setCellValueFactory((TreeTableColumn.CellDataFeatures<RatingTableClass, String> param) -> param.getValue().getValue().remarks);

            ArrayList<JFXTreeTableColumn<RatingTableClass, String>> table_columns = new ArrayList<>();

            table_columns.add(col_rating);
            table_columns.add(col_eqv);
            table_columns.add(col_remarks);

            return table_columns;
        } catch (Exception e) {
            System.err.println("createGradeColumn");
            return null;
        }

    }

    /**
     * Creates a table column
     *
     * @param colHeader Column Header
     * @param prefWidth width of the column
     * @param editable is it editable ?
     * @param resizable is it resizable ?
     * @return returns a JFXTreeTableColumn<RatingTableClass, String>
     */
    private JFXTreeTableColumn<RatingTableClass, String> tableColumnFactory(String colHeader, int prefWidth, boolean editable, boolean resizable) {
        try {
            JFXTreeTableColumn<RatingTableClass, String> column = new JFXTreeTableColumn<>(colHeader);
            column.setStyle("-fx-alignment: BASELINE-CENTER;");
            column.setPrefWidth(prefWidth);
            column.setEditable(editable);
            column.setResizable(resizable);
            return column;
        } catch (Exception e) {
            System.err.println("tableColumnFactory");
            return null;
        }

    }

    /**
     *
     * @return returns the rating data for BULSU
     */
    private ObservableList<RatingTableClass> createTableData() {
        ObservableList<RatingTableClass> holder = FXCollections.observableArrayList();
        holder.add(new RatingTableClass("1.00", "97-100%", "Passed"));
        holder.add(new RatingTableClass("1.25", "94-96%", "Passed"));
        holder.add(new RatingTableClass("1.50", "91-93%", "Passed"));
        holder.add(new RatingTableClass("1.75", "88-90%", "Passed"));
        holder.add(new RatingTableClass("2.00", "85-87%", "Passed"));
        holder.add(new RatingTableClass("2.25", "82-84%", "Passed"));
        holder.add(new RatingTableClass("2.50", "79-81%", "Passed"));
        holder.add(new RatingTableClass("2.75", "76-78%", "Passed"));
        holder.add(new RatingTableClass("3.00", "75%", "Passed"));
        holder.add(new RatingTableClass("4.00", "N/A", "Conditionally Passed"));
        holder.add(new RatingTableClass("INC", "N/A", "Incomplete"));
        holder.add(new RatingTableClass("DRP", "N/A", "Dropped"));
        holder.add(new RatingTableClass("UD", "N/A", "Unofficially Dropped"));
        holder.add(new RatingTableClass("5.00", "N/A", "Failed"));

        return holder;
    }

    private JFXTreeTableView<RatingTableClass> treeTableView;

    /**
     * Table created for the ratings
     *
     * @param table JFX table to store data
     * @return returns the table with data
     */
    public JFXTreeTableView<RatingTableClass> createGradeTable(JFXTreeTableView<RatingTableClass> table) {
        try {
            /* create tree item for table */
            final TreeItem<RatingTableClass> root = new RecursiveTreeItem<>(this.createTableData(), RecursiveTreeObject::getChildren);
            /* get created columns */
            ArrayList<JFXTreeTableColumn<RatingTableClass, String>> tbl_columns = this.createGradeColumn();
            /* Assign this columns */
            table.getColumns().addAll(tbl_columns.get(0), tbl_columns.get(1), tbl_columns.get(2));
            /* set the table */
            table.setRoot(root);
            table.setShowRoot(false);
            this.treeTableView = table;
            this.treeTableView.setOnMousePressed(this.tableRatingMouseClickAction());
            return this.treeTableView;
        } catch (Exception e) {
            System.err.println("createGradeTable");
            return null;
        }

    }

    private EventHandler<MouseEvent> tableRatingMouseClickAction() {
        return (MouseEvent event) -> {
            try {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    String rating = this.treeTableView.getSelectionModel().getSelectedItem().getValue().rating.get();

                    this.enterGrade(rating);
                }
            } catch (Exception e) {
                System.err.println("tableRatingMouseClickAction");
            }
        };
    }

    //--------------------------------------------------------------------------
    /**
     * <@Spreadsheetview JavaFX ControlsFX dependencies>
     * setups you spreadsheet view ^^v
     */
    private SpreadsheetView spreadSheet;
    private Integer CICT_id;
    private ArrayList<SubjectMapping> subjectsToBePrinted;
    private GridBase spreadSheetGrid;
    private final int gridColumnCount = 5;
    //--------------------------------------------------------------------------

    /**
     * Create Spread sheet.
     *
     * @return
     */
    public SpreadsheetView createSpreadsheet() {
        SpreadsheetView spv = new SpreadsheetView(this.getSpreadSheetGrid());
        try {
            /* Adjust the width of spreadsheet columns */
            spv.getColumns().get(this.codeCol).setPrefWidth(140.0);
            spv.getColumns().get(this.titleCol).setPrefWidth(452.0);
            spv.getColumns().get(this.unitCol).setPrefWidth(90.0);
            spv.getColumns().get(this.finalCol).setPrefWidth(90.0);
            spv.getColumns().get(this.remarkCol).setPrefWidth(150.0);
            spv.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            spv.setShowRowHeader(true);
        } catch (java.lang.IndexOutOfBoundsException d) {
            logs("IndexOutOfBoundsException @  Create Spreadsheet");
        }
        this.spreadSheet = spv;
        return this.spreadSheet;
    }

    /**
     * Generate the grid.
     *
     * @return
     */
    private GridBase getSpreadSheetGrid() {
        this.spreadSheetGrid = this.addGridRows();
        ObservableList<ObservableList<SpreadsheetCell>> cells = spreadSheetGrid.getRows();
        int row = 0;
        for (ObservableList<SpreadsheetCell> cell : cells) {
            try {
                if (!cell.get(3).isEditable()) {
                    this.spreadSheetGrid.getRows().get(row).get(this.remarkCol).setEditable(true);
                    this.spreadSheetGrid.setCellValue(row, this.finalCol, "NP");
                    this.spreadSheetGrid.setCellValue(row, this.remarkCol, "NOT FOR ENCODING");
                    this.spreadSheetGrid.getRows().get(row).get(this.finalCol).setEditable(false);
                    this.spreadSheetGrid.getRows().get(row).get(this.remarkCol).setEditable(false);
                    this.rowPainter(row, "#E74C3C");
                } else {
                    this.spreadSheetGrid.getRows().get(row).get(this.finalCol).setEditable(true);
                }
                row++;
            } catch (NullPointerException a) {
                System.err.println("Error at getSpreadSheetGrid");
                a.printStackTrace();
            }
        }
        return this.spreadSheetGrid;
    }

    /**
     * initialize at addGridRows creates a GridBase Variable
     *
     * @param rows how many rows to create
     * @return returns the grid with column headers
     */
    private GridBase createSpreadSheetGrid(int rows) {
        try {
            GridBase grid = new GridBase(rows, this.gridColumnCount);
            /* Add Header to grid */
            grid.getColumnHeaders().add("Code");
            grid.getColumnHeaders().add("Descriptive Title");
            grid.getColumnHeaders().add("Units");
            grid.getColumnHeaders().add("Final");
            grid.getColumnHeaders().add("Remarks");
            return grid;
        } catch (Exception e) {
            logs("Create Spredsheet Grid Error");
            return null;
        }
    }

    /**
     * Add Rows.
     *
     * @return
     */
    private GridBase addGridRows() {
        try {

            GridBase grid = createSpreadSheetGrid(10);
            /* Grid Rows */
            ObservableList<ObservableList<SpreadsheetCell>> gridRows = FXCollections.observableArrayList();
            for (int i = 0; i < this.subjectsToBePrinted.size(); i++) {
//            boolean editable = true;
                SubjectMapping subject = (SubjectMapping) subjectsToBePrinted.get(i);
                checkForPrerequisteRequired(subject, gridRows, i);
            }
            //----------------------------------------------------------------------
            grid.setRows(gridRows);
            grid.setRowHeightCallback((param) -> {
                // row height
                return 25.0;
            });
            return grid;
        } catch (Exception e) {
            System.err.println("error @ AddGridRows");
            return null;
        }
    }

    /**
     * Checks for pre requisite before adding a row. disables the field if no
     * pre requisite was detected.
     *
     * @param subject
     * @param gridRows
     * @param i
     */
    private void checkForPrerequisteRequired(SubjectMapping subject, ObservableList<ObservableList<SpreadsheetCell>> gridRows, int i) {
        try {
            CurricularLevelAssesor assessor = new CurricularLevelAssesor(studentMap);
            assessor.assess();
            AssessmentResults annualAsses = assessor.getAnnualAssessment(yearLevel);
            if (annualAsses == null) {
                return;
            }
            //------------------------------------------------------------------
            annualAsses.getSemestralResults(semester).forEach(sem_details -> {
                if (Objects.equals(subject.getId(), sem_details.getSubjectID())) {
                    ArrayList<Integer> pre_ids = new ArrayList<>();

                    if (sem_details.getSubjectRequisites() == null || sem_details.getSubjectRequisites().isEmpty()) {
                        // do nothing no preq
                        gridRows.add(gridRowFactory(true, // editable
                                i,
                                subject.getCode(),
                                subject.getDescriptive_title(),
                                String.valueOf(subject.getLec_units() + subject.getLab_units()),
                                "",
                                ""));
                        System.out.println("ADDED HERE");
                        return;
                    }
                    //----------------------------------------------------------
                    // if this assessment contains a grade
                    if (sem_details.getGradeDetails() != null) {
                        logs("GRADES NOT NULL");
                    }
                    //----------------------------------------------------------
                    boolean editable = false;
                    // check for pre requisites.
                    sem_details.getSubjectRequisites().forEach(pre_requisite -> {
                        pre_ids.add(pre_requisite.getSUBJECT_id_req());
                    });
                    Integer[] preqid = pre_ids.toArray(new Integer[pre_ids.size()]);
                    //----------------------------------------------------------
                    // loop all pre requisite ID and check for grades.
                    for (Integer requisitedID : preqid) {
                        GradeMapping grade = Mono.orm().newSearch(Database.connect().grade())
                                .eq(DB.grade().STUDENT_id, studentMap.getCict_id())
                                .eq(DB.grade().SUBJECT_id, requisitedID)
                                .active(Order.desc(DB.grade().id))
                                .first();

                        if (grade == null) {
                            logs("NO GRADE OF PRE REQ FOR SUBJECT ID " + requisitedID);
                            editable = false;
                            break;
                        }

                        //------------------------------------------------------
                        boolean isPassed;
                        boolean isIncomplete;
                        try {
                            isPassed = grade.getRemarks().equalsIgnoreCase("passed");
                            isIncomplete = grade.getRemarks().equalsIgnoreCase("incomplete");
                        } catch (NullPointerException a) {
                            editable = false;
                            a.printStackTrace();
                            break;
                        }

                        if (isPassed || isIncomplete) {
                            editable = true;
                        } else {
                            editable = false;
                            break;
                        }
                        //------------------------------------------------------
                    } // END LOOP

                    //----------------------------------------------------------
                    // add row
                    gridRows.add(gridRowFactory(editable,
                            i,
                            subject.getCode(),
                            subject.getDescriptive_title(),
                            String.valueOf(subject.getLec_units() + subject.getLab_units()),
                            "",
                            ""));
                    //----------------------------------------------------------
                }
            });
        } catch (Exception e) {
            System.out.println("@GRADE ENCODER UI CHECK PRE REQUISITE");
        }

    }

    private final String cellTextAlignmentCenter = "-fx-alignment: BASELINE-CENTER;";

    /**
     * returns a SpreadSheetCell
     *
     * @param row
     * @param col
     * @param cellText
     * @param editable
     * @return SpreadSheetCell
     */
    private SpreadsheetCell rowCellFactory(int row, int col, String cellText, boolean editable) {
        SpreadsheetCell my_cell = SpreadsheetCellType.STRING.createCell(row, col, 1, 1, cellText);
        my_cell.setEditable(editable);
        my_cell.setStyle(this.cellTextAlignmentCenter);
        /* Enables Text Wrapping by default */
        my_cell.setWrapText(true);
        return my_cell;
    }

    private final int codeCol = 0;
    private final int titleCol = 1;
    private final int unitCol = 2;
    private final int finalCol = 3;
    private final int remarkCol = 4;

    /**
     * creates a row using aided function of rowCellFactory
     *
     * @param row index to insert this row
     * @param code subject code
     * @param title subject title
     * @param units units credited
     * @param _final grade
     * @param remarks remarks
     * @return returns a row for grid
     */
    private ObservableList<SpreadsheetCell> gridRowFactory(boolean editable, int row, String code, String title, String units, String _final, String remarks) {

        ObservableList<SpreadsheetCell> gridRow = FXCollections.observableArrayList();
        gridRow.add(this.rowCellFactory(row, this.codeCol, code, false));
        /* Descriptive Title Column */
        SpreadsheetCell titleCell = this.rowCellFactory(row, this.titleCol, title, false);
        titleCell.setStyle("-fx-padding: 0 0 0 15;");
        gridRow.add(titleCell);

        gridRow.add(this.rowCellFactory(row, this.unitCol, units, false));
        // Final Rating Cell
        SpreadsheetCell ratingCell = this.rowCellFactory(row, this.finalCol, _final, editable);
        ratingCell.itemProperty().addListener(this.ratingCellValueChangeListenerAction(ratingCell.getRow(), editable));
        //----------------------------------------------------------------------
        System.out.println("TRY");
        //----------------------------------------------------------------------
        gridRow.add(ratingCell); // the column that is editable
        gridRow.add(this.rowCellFactory(row, this.remarkCol, remarks, false));
        return gridRow;
    }

    /**
     *
     * @param row for applying value changes
     * @return value change listener for the cell
     */
    private ChangeListener<Object> ratingCellValueChangeListenerAction(int row, boolean editable) {
        return (ObservableValue<? extends Object> observable, Object oldValue, Object newValue) -> {
            //ObjectProperty op = (ObjectProperty) observable;

            try {
                if (editable) {
                    //----------------------------------------------------------
                    if (newValue == null) {
                        // Stack Overflow exception
                        // GridCell Error
                        // Resolved @ 10/31/2017
                    } else if (newValue.toString().equals("")) {
                        // do nothing if empty
                    } else if (newValue.equals(oldValue)) {
                        // do nothing if the same
                    } else {
                        // allow editting of the remarks column
                        System.out.println("1");
                        this.spreadSheetGrid.getRows().get(row).get(this.remarkCol).setEditable(true);
                        System.out.println("2");
                        String cellValue[] = validateGrade(newValue, row);
                        System.out.println("4");
                        this.spreadSheetGrid.setCellValue(row, this.remarkCol, cellValue[1]);
                        System.out.println("5");
                        // disallow editting of the remarks column
                        this.spreadSheetGrid.getRows().get(row).get(this.remarkCol).setEditable(false);
                        System.out.println("3");
                        this.spreadSheetGrid.setCellValue(row, this.finalCol, cellValue[0]);
                        System.out.println("6");
                        //------------------------------------------------------
                        // Error appears in this part
                        //------------------------------------------------------
                    }
                    //----------------------------------------------------------
                } else {
                    System.out.println("Im called");
                    System.out.println(editable);
                    System.out.println(row);
                }
            } catch (Exception e) {
                System.err.println("@Error in ratingCellValueChangeListener");
                System.err.println("This error was already catched and won't cause any problems @GradeEncoderUi.ratingCellValueChangeListenerAction()");
            }

        };
    }

    /**
     *
     * @param grade grade from spreadsheet
     * @param row for row painter only
     * @return returns string array [GRADE,REMARKS]
     */
    private String[] validateGrade(Object grade, int row) {
        openRestrictedAccess = false;
        String[] cellValue = {"", ""};
        try {
            if (grade.equals("")) {
                System.out.println("IT's EMPTY");
                this.rowPainter(row, this.colorBlanked);
                return cellValue;
            }
            if (grade instanceof String) {
                String rating = (String) grade;

                if (rating.equalsIgnoreCase("INC")) {
                    cellValue[0] = "INC";
                    cellValue[1] = "Incomplete";
                    this.rowPainter(row, this.colorINC);
                } else if (rating.equalsIgnoreCase("DRP")) {
                    cellValue[0] = "DRP";
                    cellValue[1] = "Dropped";
                    this.rowPainter(row, this.colorDropped);
                } else if (rating.equalsIgnoreCase("UD")) {
                    cellValue[0] = "UD";
                    cellValue[1] = "Unofficially Dropped";
                    this.rowPainter(row, this.colorUD);
                } else {
                    try {
                        cellValue = this.checkGradeRange(Double.parseDouble(rating), row);
                    } catch (NumberFormatException e) {
                        if (!this.valueFiltering) {
                            if (rating.equalsIgnoreCase("EXP")) {
                                cellValue[0] = "EXP";
                                cellValue[1] = "EXPIRED";
                                this.rowPainter(row, this.colorBlanked);
                                return cellValue;
                            }
                        }

                        this.rowPainter(row, this.colorBlanked);

                        Mono.fx().alert().createWarning()
                                .setHeader("Invalid Grade")
                                .setMessage("Please refer from the table on your left for valid range.")
                                .showAndWait();
                        //------------------------------------------------------
                        System.out.println("RETURN");
                        return cellValue;
                        //------------------------------------------------------
                    }
                }
            }
        } catch (NullPointerException s) {
            this.rowPainter(row, this.colorBlanked);
            return cellValue;
        }
        return cellValue;
    }

//    private boolean willShow = false;
//    private void checkForPreReq(Object grade, String cellValue[], int row) {
//        TablePosition cellFocusPosition = this.spreadSheet.getSelectionModel().getFocusedCell();
//        try {
//            // validate pre req here
//            String subjectCode = this.spreadSheetGrid.getRows().get(cellFocusPosition.getRow()).get(cellFocusPosition.getColumn()-3).getText();
//            logs("SELECTED " + subjectCode);
//            ArrayList<SubjectMapping> subjects = Mono.orm().newSearch(Database.connect().subject())
//                    .eq(DB.subject().code, subjectCode)
//                    .active()
//                    .all();
//            
//            for(SubjectMapping temp: subjects) {
//                CurriculumSubjectMapping csMap = Mono.orm().newSearch(Database.connect().curriculum_subject())
//                        .eq(DB.curriculum_subject().SUBJECT_id, temp.getId())
//                        .eq(DB.curriculum_subject().CURRICULUM_id, CURRICULUM_id)
//                        .active()
//                        .first();
//                if(csMap != null) {
//                    subject = temp;
//                    break;
//                }
//            }
//            if(subject != null) {
////                ValidateAddingSubject validate = new ValidateAddingSubject();
////                validate.studentCICT_id = studentMap.getCict_id();
////                validate.subjectID = subject.getId();
////                validate.setOnSuccess(onSuccess->{
//////                    this.spreadSheetGrid.setCellValue(row, this.finalCol, cellValue[0]);
//////                    this.spreadSheetGrid.setCellValue(row, this.remarkCol, cellValue[1]);
//////                    this.spreadSheetGrid.getRows().get(row).get(this.remarkCol).setEditable(false);
////                });
////                validate.setOnCancel(onCancel-> {
////                    if (validate.isAlreadyTaken()) {
////                        setToDefaultCell(row);
////                        logs("SUBJECT ALREADY TAKEN ");
////                        showWarningNotification("Subject Already Taken", subject.getCode() + " is already taken.\n"
////                                        + "Verified For S/N: " + studentMap.getId() + ", " + studentMap.getLast_name() + ".");
////                    } else if (validate.isPreReqNotAllTaken()) {
////                        logs("INCOMPLETE PRE-REQUISITE SUBJECT/S REQUIREMENT");
////                        ArrayList<Integer> preReqIds = validate.getPreReqRequiredIds();
////                        String prereqs = "";
////                        for (Integer id : preReqIds) {
////                            SubjectMapping subject = Mono.orm().newSearch(Database.connect().subject())
////                                    .eq(DB.subject().id, id)
////                                    .execute()
////                                    .first();
////                            prereqs += subject.getCode() + " | ";
////                        }
////                        int len = prereqs.length();
////                        prereqs = prereqs.substring(0, len - 3);
////                        setToDefaultCell(row);
////                        showWarningNotification("Pre-Requisites Required.", subject.getCode() + "\n"
////                                + "Verified For S/N: " + studentMap.getId() + ", " + studentMap.getLast_name() + ".\n"
////                                + "Requires: " + prereqs + " " + row);
////                    } else {
////                        System.out.println("HERE !");
//////                        this.spreadSheetGrid.setCellValue(row, this.finalCol, cellValue[0]);
//////                        this.spreadSheetGrid.setCellValue(row, this.remarkCol, cellValue[1]);
//////                        this.spreadSheetGrid.getRows().get(row).get(this.remarkCol).setEditable(false);
////                    }
////                });
////                validate.transact();
//            } else {
//                logs("SUBJECT IS NULL");
//            }
//        } catch (Exception e) {}
//    }
    private void setToDefaultCell(int row) {
        this.spreadSheetGrid.getRows().get(row).get(this.finalCol).setEditable(true);
        this.spreadSheetGrid.setCellValue(row, this.finalCol, "");
        this.spreadSheetGrid.setCellValue(row, this.remarkCol, "");
        this.spreadSheetGrid.getRows().get(row).get(this.remarkCol).setEditable(false);
        this.rowPainter(row, this.colorBlanked);
    }

    /**
     * checks the grade for its range 1.00 - 3.00 , 4.00 and 5.00
     *
     * @param rating grade value
     * @param row row index for painter only
     * @return returns string array [GRADE,REMARKS]
     */
    private String[] checkGradeRange(Double rating, int row) {
        String[] cellValue = {"", ""};
        if (rating % 0.25 != 0
                || ((rating < 1.00 || rating > 3.00) && (rating != 5.00) && (rating != 4.00))) {
            this.rowPaint(row, this.colorBlanked);
            Mono.fx().alert().createWarning()
                    .setHeader("Invalid Range")
                    .setMessage("Please refer from the table on your left for valid grades.")
                    .show();
            return cellValue;
        }

        boolean isPassing = (rating >= 1.00 && rating <= 3.00);
        String gradeFormat = String.format("%.02f", rating);
        /*
        ternary operator
        String s = (condition) ? "true" : "false";
         */
        cellValue = (isPassing) ? new String[]{gradeFormat, "Passed"}
                : (rating == 5.00) ? new String[]{"5.00", "Failed"}
                : new String[]{"4.00", "Conditionally Passed"};

        String rowColor = (isPassing) ? this.colorPassed
                : (rating == 5.00) ? this.colorFailed
                        : this.colorConditionalPass;

        this.rowPainter(row, rowColor);
        return cellValue;
    }

    /**
     * <@color list>
     */
    private final String colorPassed = "#22EE5B";
    private final String colorBlanked = "#FFFFFF";
    private final String colorDropped = "#FFEE75";
    private final String colorUD = "#FFA578";
    private final String colorConditionalPass = "#FF00FF";
    private final String colorFailed = "#FE3939";
    private final String colorINC = "#00B0F0";

    /**
     * paints the row for any color
     *
     * @param row row to paint
     */
    private void rowPainter(int row) {
        this.rowPainterMaster(row, "#c8ffad");
    }

    private void rowPainter(int row, String hexColor) {
        this.rowPainterMaster(row, hexColor);
    }

    /**
     * Polymorphism function overloading
     *
     * @param row
     * @param hexColor
     */
    private void rowPainterMaster(int row, String hexColor) {
        try {
            this.rowPaint(row, hexColor);

            if (!this.isKeyEvent) {
                this.rowFocus(row);
                this.isKeyEvent = true;
            }
        } catch (Exception e) {
            System.err.println("rowPainter");
        }
    }

    private void rowPaint(int row, String hexColor) {
        for (int x = 0; x < 5; x++) {
            String cell_style = this.spreadSheetGrid.getRows().get(row).get(x).getStyle();
            this.spreadSheetGrid.getRows().get(row).get(x).setStyle(cell_style += "-fx-background-color: " + hexColor + ";");
        }
    }

    /**
     * Moves the focus to the next row
     *
     * @throws index out of bound exception
     * @param row
     */
    private void rowFocus(int row) {
        try {
            row += 1; // get the next row
            //this.spreadSheet.getSelectionModel().focus(row, this.spreadSheet.getColumns().get(this.finalCol)); 
            this.spreadSheet.getSelectionModel().select(row, this.spreadSheet.getColumns().get(this.finalCol));
            //this.rowPaint(row, "#8FB1E8");

        } catch (Exception e) {
            System.err.println("rowFocus");
            e.printStackTrace();
        }
    }

    /* Origin of the event */
    private boolean isKeyEvent = true;

    /**
     * enter a grade to the spread sheet using the mouse pressed event of table
     * rating
     *
     * @param rating
     */
    private SubjectMapping subject = null;

    //-----------------------------
    private boolean openRestrictedAccess = false;
    //---------------------

    private void enterGrade(String rating) {
        this.isKeyEvent = false;
        subject = null;
        TablePosition cellFocusPosition = this.spreadSheet.getSelectionModel().getFocusedCell();
        if (cellFocusPosition.getRow() == -1) {
            MessageBox.showWarning("Invalid Selection", "Please select the [FINAL] column.");
            return;
        }
        if (isTheSame(rating, cellFocusPosition)) {
            this.spreadSheet.getSelectionModel().select(cellFocusPosition.getRow() + 1, this.spreadSheet.getColumns().get(this.finalCol));
            return;
        }
        try {
            int focusRow = (cellFocusPosition.getRow());
            int focusCol = (cellFocusPosition.getColumn());
            if (focusCol != this.finalCol) {
                MessageBox.showWarning("Invalid Selection", "Please select the [FINAL] column.");
                return;
            }
            //------------------------------------------------------------------
            // Replacement block for the unsed code block
            // @date 12/17/2017
            // @author Jhon Melvin
            SpreadsheetCell testCell = this.spreadSheetGrid.getRows()
                    .get(focusRow).get(this.finalCol);
            if (testCell.isEditable()) {
                this.setToDefaultCell(focusRow);
                this.spreadSheetGrid.setCellValue(focusRow, focusCol, rating);
            } else {
                MessageBox.showWarning("Not Editable", "The current selection is not for editting please skip this.");
            }
            // End of replacement code
            //------------------------------------------------------------------
            //------------------------------------------------------------------
            if (true) {
                return;
            }
            // Disregard below code.
            //------------------------------------------------------------------
            //------------------------------------------------------------------
            //------------------------------------------------------------------
            //------------------------------------------------------------------
            /**
             * UNUSED COdE BLOCK
             */
            // validate pre req here
            String subjectCode = this.spreadSheetGrid.getRows().get(cellFocusPosition.getRow()).get(cellFocusPosition.getColumn() - 3).getText();
            logs("SELECTED " + subjectCode);
            ArrayList<SubjectMapping> subjects = Mono.orm().newSearch(Database.connect().subject())
                    .eq(DB.subject().code, subjectCode)
                    .active()
                    .all();

            //------------------------------------------------------------------
            for (SubjectMapping temp : subjects) {
                CurriculumSubjectMapping csMap = Mono.orm().newSearch(Database.connect().curriculum_subject())
                        .eq(DB.curriculum_subject().SUBJECT_id, temp.getId())
                        .eq(DB.curriculum_subject().CURRICULUM_id, CURRICULUM_id)
                        .active()
                        .first();
                if (csMap != null) {
                    subject = temp;
                    break;
                }
            }
            if (subject != null) {
                ValidateAddingSubject validate = new ValidateAddingSubject();
                validate.studentCICT_id = studentMap.getCict_id();
                validate.subjectID = subject.getId();
                validate.setOnSuccess(onSuccess -> {
                    setToDefaultCell(focusRow); // do not look
                    this.spreadSheetGrid.setCellValue(focusRow, focusCol, rating);
                });
                validate.setOnCancel(onCancel -> {
                    if (validate.isAlreadyTaken()) {
                        logs("SUBJECT ALREADY TAKEN ");
                        showWarningNotification("Subject Already Taken", subject.getCode() + " is already taken.\n"
                                + "Verified For S/N: " + studentMap.getId() + ", " + studentMap.getLast_name() + ".");
                    } else if (validate.isPreReqNotAllTaken()) {
                        logs("INCOMPLETE PRE-REQUISITE SUBJECT/S REQUIREMENT");
                        ArrayList<Integer> preReqIds = validate.getPreReqRequiredIds();
                        String prereqs = "";
                        for (Integer id : preReqIds) {
                            SubjectMapping subject = Mono.orm().newSearch(Database.connect().subject())
                                    .eq(DB.subject().id, id)
                                    .execute()
                                    .first();
                            prereqs += subject.getCode() + " | ";
                        }
                        int len = prereqs.length();
                        prereqs = prereqs.substring(0, len - 3);
                        showWarningNotification("Pre-Requisites Required.", subject.getCode() + "\n"
                                + "Verified For S/N: " + studentMap.getId() + ", " + studentMap.getLast_name() + ".\n"
                                + "Requires: " + prereqs);
                    } else if (validate.isNoSectionOffered()) {
                        setToDefaultCell(focusRow); // do not look
                        this.spreadSheetGrid.setCellValue(focusRow, focusCol, rating);
                    } else {
                        setToDefaultCell(focusRow); // do not look
                        this.spreadSheetGrid.setCellValue(focusRow, focusCol, rating);
                    }
                });
                validate.transact();
            } else {
                logs("SUBJECT IS NULL");
            }
            //------------------------------------------------------------------
            // END OF UNUSED COdE BLOcK
        } catch (Exception e) {
            MessageBox.showError("Failed", "Cannot encode this grade.");
        }
    }

    private void showWarningNotification(String title, String message) {
        Notifications.create()
                .title(title)
                .text(message)
                .showWarning();
    }

    /**
     * checks if it's the same the focus will not move down if it's the same
     * because it will not trigger itemProperty change listener because there is
     * no change occured
     *
     * @param rating grade from table rating mouse click
     * @param cellFocusPosition current focus position
     * @return
     */
    private boolean isTheSame(String rating, TablePosition cellFocusPosition) {
        String oldValue = this.spreadSheetGrid.getRows().get(cellFocusPosition.getRow()).get(cellFocusPosition.getColumn()).getText();
        return oldValue.equalsIgnoreCase(rating);
    }

    /**
     * <@Posting>
     * all methods that concerns the posting of the grade and verification
     */
    private static int UNPASSED_COUNT;

    /**
     * requires the button control that will invoke the post action, verify the
     * sheet.
     *
     * @param btnPost
     */
    public void verifySheet(Button btnPost, int unPassed) {
        this.UNPASSED_COUNT = unPassed;
        int row = this.spreadSheet.getGrid().getRowCount();
        Task<Void> task = verificationProcess(row);
        task.setOnScheduled(event -> {

        });
        task.setOnSucceeded(event -> {
            btnPost.setDisable(false);
            this.spreadSheet.setEditable(true);
            //------------------------------------------------------------------
            /**
             * If an unpassed subject was changed to passed open restrcited
             * access.
             */
            System.out.println("CHECKED UNPASSED COUNT: " + CHECK_UNPASSED_COUNT);
            System.out.println("UNPASSED COUNT: " + UNPASSED_COUNT);
            if (UNPASSED_COUNT != 0) {
                System.out.println("TEST TO OPEN RESTRCTION");
                if (this.CHECK_UNPASSED_COUNT != this.UNPASSED_COUNT) {
                    System.out.println("OPEN RESTRICTION");
                    this.openRestrictedAccess = true;
                }
            }
            //------------------------------------------------------------------

            this.notifyUser(this.verficationResult, btnPost);
        });
        task.setOnRunning(event -> {
            btnPost.setDisable(true);
            this.spreadSheet.setEditable(false);
        });
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    /**
     * notifies the user for the result of the posting
     *
     * @param res value from verification result number of empty rows.
     */
    private void notifyUser(int res, Button btn_post) {
        if (res != 0) {
            int result = Mono.fx().alert().createConfirmation()
                    .setHeader("Empty Row Found")
                    .setMessage("There are " + res + " subjects with no grade found. Do you still want to post it?")
                    .confirmYesNo();
            if (result != 1) {
                return;
            }
        }

        if (this.openRestrictedAccess) {
            boolean isAllowed = false;
            AccountFacultyMapping allowedUser = null;
            if (Access.isDeniedIfNotFrom(
                    Access.ACCESS_LOCAL_REGISTRAR,
                    Access.ACCESS_CO_REGISTRAR)) {
                /**
                 * Check if the user was granted permission by the registrar.
                 */
                //--------------------------------------------------------------
                allowedUser = Access.isAllowedToRevoke();
                //--------------------------------------------------------------
                if (allowedUser != null) {
                    isAllowed = true;
                }
            } else {
                // allowed user
                isAllowed = true;
            }
            //------------------------------------------------------------------
            if (!isAllowed) {
                this.showWarningNotification("Access Denied", "Cannot update grade with INC and Failed.");
                return; // do not execute below code
            }
            //------------------------------------------------------------------
        }

        SpreadSheetGradeEncoder encodeGrade = Evaluator.instance().createGradeEncoder();
        encodeGrade.CICT_id = this.CICT_id;
        encodeGrade.spreadSheet = this.spreadSheet;
        //----------------------------------------------------------------------
        encodeGrade.studentMapping = studentMap;
        encodeGrade.encodingYear = this.yearLevel;
        //----------------------------------------------------------------------
//        if (ACAD_TERM_id != null) {
//            encodeGrade.ACAD_TERM_id = this.ACAD_TERM_id;
//        }
//        encodeGrade.MODE = this.MODE;

        final boolean FLAG_DISABLED_POST = btn_post.isDisabled();

        encodeGrade.whenStarted(() -> {
            btn_post.setDisable(true);
        });
        encodeGrade.whenCancelled(() -> {
            // two reasons this will be cancelled
            System.out.println("SUBJECTS NOT FOUND");
            System.out.println("FAILED TO FIND SUBJECT IN CURRICULUM");
            MessageBox.showError("Failed", "Please try again.");
        });

        encodeGrade.whenFailed(() -> {
            // when insertion failed
            MessageBox.showError("Failed", "Cannot insert or update new values from the database.");
//            Mono.fx().getParentStage(this.notificationPane).close();
        });

        encodeGrade.whenSuccess(() -> {
            if (encodeGrade.isAlreadyPosted()) {
                
                MessageBox.showInformation("Done", "No Changes Where Made.");
                Mono.fx().getParentStage(this.notificationPane).close();
            } else {
                MessageBox.showInformation("Done", "Grades successfully posted.");
                Mono.fx().getParentStage(this.notificationPane).close();
            }

//            else {
//                Notifications.create()
//                        .text("No new grade and subject to be posted.")
//                        .title("No Changes Made")
//                        .showWarning();
//
//                    Mono.fx().snackbar().showSuccess(this.notificationPane, "No grade and subject to be posted.");
//                    btn_post.setDisable(false);
//            }
        });
        encodeGrade.whenFinished(() -> {
            // finished
            btn_post.setDisable(FLAG_DISABLED_POST);
        });

        encodeGrade.transact();
//        }
    }

    /**
     * if a grade that is not passed is updated.
     *
     * @param x
     * @param cellRemark
     * @deprecated
     */
    @Deprecated
    private void joemarOpenRestricted(int x, String cellRemark) {
        // verify if grade is already posted with failed/inc
        String cellCode = spreadSheetGrid.getRows().get(x).get(codeCol).getText();
        System.out.print("VERIFYING IF ALREADY POSTED: ");
        System.out.println(cellCode);
        //----------------------------------------------------------
        ArrayList<SubjectMapping> subjects = Mono.orm().newSearch(Database.connect().subject())
                .eq(DB.subject().code, cellCode)
                .active()
                .all();
        //----------------------------------------------------------
        SubjectMapping temp_subject = null;
        //----------------------------------------------------------
        for (SubjectMapping temp : subjects) {
            CurriculumSubjectMapping csMap = Mono.orm().newSearch(Database.connect().curriculum_subject())
                    .eq(DB.curriculum_subject().SUBJECT_id, temp.getId())
                    .eq(DB.curriculum_subject().CURRICULUM_id, CURRICULUM_id)
                    .active()
                    .first();
            if (csMap != null) {
                temp_subject = temp;
                break;
            }
        }
        //----------------------------------------------------------
        if (temp_subject != null) {
            GradeMapping grade = Mono.orm().newSearch(Database.connect().grade())
                    .eq(DB.grade().STUDENT_id, CICT_id)
                    .eq(DB.grade().SUBJECT_id, temp_subject.getId())
                    .active(Order.desc(DB.grade().id)).first();
            if (grade != null) {
                if (grade.getRemarks().equalsIgnoreCase("FAILED") || grade.getRemarks().equalsIgnoreCase("INCOMPLETE")) {
                    if (!cellRemark.equalsIgnoreCase(grade.getRemarks())) {
                        openRestrictedAccess = true;
                    }
                }
            } else {
                System.out.println("GRADE IS NULL");
            }
        } else {
            System.out.println("TEMP SUBJECT IS NULL");
        }
    }

    /**
     *
     * @param rowCount
     * @return returns the task created
     */
    private Task<Void> verificationProcess(int rowCount) {
        return new Task<Void>() {
            // inner class variables
            int x;
            int errorCount = 0;

            //------------------------------------------------------------------
            // there is already a defined unpassed count when the grade was wrtten
            // if this unpassed count and the latter do  not match
            // it means that the user has updated it to passing.
            int unpassedCount = 0;
            //------------------------------------------------------------------

            @Override
            public Void call() throws Exception {
                for (x = 0; x < rowCount; x++) {
                    Platform.runLater(() -> {
                        rowPaint(x, "#315A8E"); // blue highlight
                    });
                    //sleep for a while
                    Thread.sleep(200);
                    String cellText = spreadSheetGrid.getRows().get(x).get(finalCol).getText();
                    String cellRemark = spreadSheetGrid.getRows().get(x).get(remarkCol).getText();
                    //----------------------------------------------------------
                    if ((!cellRemark.equalsIgnoreCase("PASSED")) && (!cellRemark.isEmpty())) {
                        System.out.println("UNPASSED SUBJECt");
                        this.unpassedCount++;
                    }
                    //----------------------------------------------------------
                    // MUST BE REMOVED
                    //----------------------------------------------------------
                    // joemarOpenRestricted(x, cellRemark);
                    //----------------------------------------------------------
                    // <MUST BE REMOVED>
                    //----------------------------------------------------------
                    if (cellText.isEmpty()) {
                        // if remarks is NOT FOR ENCODING, retain color
                        if (cellRemark.equalsIgnoreCase("NOT FOR ENCODING")) {
                            rowPaint(x, "#E74C3C");
                        } else {
                            // if empty paint it white
                            rowPaint(x, "#FFFFFF");
                        }
                        errorCount++;
                    } else {
                        if (cellText.equalsIgnoreCase("EXP")) {
                            Platform.runLater(() -> {
                                spreadSheetGrid.getRows().get(x).get(finalCol).setItem("");
                            });

                            rowPaint(x, "#FFFFFF");
                            errorCount++;
                        } else {
                            rowPaint(x, getRemarkColor(cellRemark));
                        }

                    }

                }
                // sends the result outside this anonymous class
                verficationResult(this.errorCount, this.unpassedCount);
                return null;
            }
        };
    }

    /**
     * get the corresponding color for the remark
     *
     * @param remarks
     * @return
     */
    private String getRemarkColor(String remarks) {

        String hexColor = this.colorBlanked;
        if (remarks.equalsIgnoreCase("PASSED")) {
            hexColor = this.colorPassed;
        } else if (remarks.equalsIgnoreCase("Conditionally Passed")) {
            hexColor = this.colorConditionalPass;
        } else if (remarks.equalsIgnoreCase("Incomplete")) {
            hexColor = this.colorINC;
        } else if (remarks.equalsIgnoreCase("Dropped")) {
            hexColor = this.colorDropped;
        } else if (remarks.equalsIgnoreCase("Unofficially Dropped")) {
            hexColor = this.colorUD;
        } else if (remarks.equalsIgnoreCase("Failed")) {
            hexColor = this.colorFailed;
        }
        return hexColor;
    }

    private Pane notificationPane;

    /**
     * sets to where the notification will appear
     *
     * @param pane any container
     */
    public void setNotificationPane(Pane pane) {
        this.notificationPane = pane;
    }

    private int verficationResult = 0;
    private int CHECK_UNPASSED_COUNT = 0;

    /**
     * gets the value of the result from the task
     *
     * @param count
     */
    private void verficationResult(int count, int unpass) {
        this.verficationResult = count;
        this.CHECK_UNPASSED_COUNT = unpass;
    }

    //-------------------------------------------
//    private ArrayList<Hashtable<String, String>> restrictedSubjects = new ArrayList<>();
//
//    public void addRestrictedSubject(Hashtable<String, String> detail) {
//        restrictedSubjects.add(detail);
//    }
}

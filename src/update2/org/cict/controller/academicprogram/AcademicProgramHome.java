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
import app.lazy.models.CurriculumHistorySummaryMapping;
import app.lazy.models.CurriculumMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import artifacts.MonoString;
import com.izum.fx.textinputfilters.StringFilter;
import com.izum.fx.textinputfilters.TextInputFilters;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.controls.SimpleImage;
import com.jhmvin.fx.display.SceneFX;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.LayoutDataFX;
import com.jhmvin.transitions.Animate;
import com.melvin.mono.fx.bootstrap.M;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.text.WordUtils;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.controlsfx.control.Notifications;
import update2.org.cict.controller.curriculum.AddNewCurriculumController;
import update2.org.cict.controller.curriculum.CurriculumInformationController;
import update2.org.cict.controller.subjects.SubjectRepositoryController;
import update2.org.cict.layout.academicprogram.ProgramRowExtension;
import update3.org.cict.SectionConstants;
import update3.org.cict.access.management.FacultyRow;

/**
 *
 * @author Jhon Melvin
 */
public class AcademicProgramHome extends SceneFX implements ControllerFX {
    
    @FXML
    private VBox vbox_table_holder;
    
    @FXML
    private AnchorPane application_root;
    
    @FXML
    private JFXButton btn_home;
    
    @FXML
    private JFXButton btn_home1;
    
    @FXML
    private JFXButton btnNewProgram;
    
    @FXML
    private VBox vbox_search;
    
    @FXML
    private JFXButton btn_view_subjects;
    
    @FXML
    private VBox vbox_no_found;
    
    @FXML
    private Label lbl_title_no_found;
    
    @FXML
    private Label lbl_subtitle_no_found;
    
    private Integer IMPLEMENTED_BY = CollegeFaculty.instance().getFACULTY_ID();
    private Date IMPLEMENTED_DATE = Mono.orm().getServerTime().getDateWithFormat();
    private ArrayList<Integer> validFloorAssignment = new ArrayList<>();
    
    @Override
    public void onInitialization() {
        // all destructive operations should be disabled if not admin
        if (AcademicProgramAccessManager.denyIfNotAdmin()) {
            btnNewProgram.setDisable(true);
        }

        /**
         * Required when using SceneFX.
         */
        bindScene(application_root);
        
        this.refreshAcademicProgramTable(2000);
        /**
         * insert valid floors values must be Integer
         */
        validFloorAssignment.add(3);
        validFloorAssignment.add(4);
        
    }
    
    private LayoutDataFX homeFX;
    public void setHomeFX(LayoutDataFX homeFX) {
        this.homeFX = homeFX;
    }
    
    private final String SECTION_BASE_COLOR = "#414852";
    @Override
    public void onEventHandling() {
        this.addClickEvent(btn_home, () -> {
            AcademicHome controller = new AcademicHome();
            Pane pane = Mono.fx().create()
                    .setPackageName("update2.org.cict.layout.academicprogram")
                    .setFxmlDocument("academic-home")
                    .makeFX()
                    .setController(controller)
                    .pullOutLayout();
            super.setSceneColor(SECTION_BASE_COLOR); // call once on entire scene lifecycle
            Animate.fade(this.application_root, SectionConstants.FADE_SPEED, () -> {
                super.replaceRoot(pane);
            }, pane);
        });
        this.addClickEvent(btnNewProgram, () -> {
            this.showAddNewProgram();
        });
        
        this.addClickEvent(btn_view_subjects, () -> {
            SubjectRepositoryController controller = new SubjectRepositoryController();
            Pane pane = Mono.fx().create()
                    .setPackageName("update2.org.cict.layout.subjects")
                    .setFxmlDocument("subject-bank")
                    .makeFX()
                    .setController(controller)
                    .pullOutLayout();

            super.setSceneColor(SECTION_BASE_COLOR); // call once on entire scene lifecycle

            Animate.fade(this.application_root, SectionConstants.FADE_SPEED, () -> {
                super.replaceRoot(pane);
            }, pane);
        });
        
    this.addClickEvent(btn_home1, ()->{
        changeView();
    });
    }
    
    private void changeView() {
        ArrayList<AcademicProgramInfo> implementedAcadPrograms = new ArrayList<>();
        ArrayList<AcademicProgramInfo> unImplementedAcadPrograms = new ArrayList<>();
        if (acadProgramInfo != null) {
            for (AcademicProgramInfo apInfo : acadProgramInfo) {
                if (apInfo.getAcademicProgram().getImplemented() == 1) {
                    implementedAcadPrograms.add(apInfo);
                } else {
                    unImplementedAcadPrograms.add(apInfo);
                }
            }
        }
        String selected = btn_home1.getText();//cmb_sort_.getSelectionModel().getSelectedIndex();
        vbox_table_holder.getChildren().clear();
        if (selected.equalsIgnoreCase("Show Saved")) {
            btn_home1.setText("Show Not Saved");
            if (implementedAcadPrograms.isEmpty()) {
//                vbox_no_found.setVisible(true);
//                vbox_search.setVisible(false);
                changeView(vbox_no_found);
            } else {
//                vbox_no_found.setVisible(false);
//                changeView(vbox_search);
                createProgramsTable(implementedAcadPrograms);
            }
        } else {
            btn_home1.setText("Show Saved");
            if (unImplementedAcadPrograms.isEmpty()) {
//                vbox_no_found.setVisible(true);
//                vbox_search.setVisible(false);
                changeView(vbox_no_found);
            } else {
//                vbox_no_found.setVisible(false);
//                changeView(vbox_search);
                createProgramsTable(unImplementedAcadPrograms);
            }
        }
    }
    
    private void showAddNewProgram() {
        
        AddNewProgramController controller = new AddNewProgramController();
        Mono.fx().create()
                .setPackageName("update2.org.cict.layout.academicprogram")
                .setFxmlDocument("add-acad-program")
                .makeFX()
                .setController(controller)
                .makeScene()
                .makeStageWithOwner(Mono.fx().getParentStage(btn_home))
                .stageTitle("New Program")
                .stageResizeable(false)
                .stageShowAndWait();
        AcademicProgramInfo acadProg = controller.getNewAcademicProgramInfo();
        if (acadProg != null) {
            createProgramsView(acadProg);
        }
    }
    
    private SimpleTable programsTable = new SimpleTable();
    
    private void createProgramsTable(ArrayList<AcademicProgramInfo> collection) {
        programsTable.getChildren().clear();
        for (AcademicProgramInfo academicPrograms : collection) {
            createRow(academicPrograms);
        }

        // when all details are added in the table;
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(programsTable);
        simpleTableView.setFixedWidth(true);
        // attach to parent variable name in scene builder
        Mono.fx().thread().wrap(()->{
            changeView(vbox_table_holder);
            simpleTableView.setParentOnScene(vbox_table_holder);
        });
//        this.vbox_search.setVisible(false);
//        this.vbox_table_holder.setVisible(true);
    }
    
    private void changeView(Node showThis) {
        Animate.fade(showThis, 150, ()->{
            vbox_no_found.setVisible(false);
            vbox_search.setVisible(false);
            vbox_table_holder.setVisible(false);
            showThis.setVisible(true);
        }, vbox_no_found, vbox_search, vbox_table_holder);
    }
    
    private void createRow(AcademicProgramInfo academicPrograms) {
        AcademicProgramMapping apMap = academicPrograms.getAcademicProgram();
        ArrayList<CurriculumMapping> curriculums = academicPrograms.getCurriculums();

        // create table row
        SimpleTableRow row = new SimpleTableRow();
        row.setRowHeight(100.0);

        // load pre defined row layout
        HBox programRow = (HBox) Mono.fx().create()
                .setPackageName("update2.org.cict.layout.academicprogram")
                .setFxmlDocument("program-row")
                .makeFX()
                .pullOutLayout();
        
        ImageView img_extension = searchAccessibilityText(programRow, "img_row_extension");
        Label lbl_code = searchAccessibilityText(programRow, "lbl_code");
        Label lbl_name = searchAccessibilityText(programRow, "lbl_name");
        Label lbl_count = searchAccessibilityText(programRow, "lbl_count");
        JFXButton btn_implement = searchAccessibilityText(programRow, "btn_information");
        
        if (AcademicProgramAccessManager.denyIfNotRegistrar()) {
            btn_implement.setDisable(true);
        }
        
        lbl_code.setText(apMap.getCode());
        lbl_name.setText(apMap.getName());
        try {
            lbl_count.setText(String.valueOf(curriculums.size()));
        } catch (NullPointerException b) {
            lbl_count.setText("0");
        }
        boolean isImplemented;
        if (apMap.getImplemented().equals(1)) {
            btn_implement.setText("SAVED");
            isImplemented = true;
            btn_implement.setDisable(true);
        } else {
            isImplemented = false;
            addClickEvent(btn_implement, () -> {
                implementAcademicProgram(apMap, curriculums);
            });
        }

        // create cell container
        SimpleTableCell cellParent = new SimpleTableCell();
        cellParent.setResizePriority(Priority.ALWAYS);
        cellParent.setContent(programRow);

        // add cell to row
        row.addCell(cellParent);

        // add row extension
        createProgramRowExtension(programsTable, row, img_extension, academicPrograms, programRow, lbl_count, isImplemented);
        // add row
        programsTable.addRow(row);
    }

//    private VBox vbox_curriculum_table_holder;
    private void createProgramRowExtension(SimpleTable programsTable, SimpleTableRow row, ImageView img_extension, AcademicProgramInfo info, HBox programRow, Label lbl_count, boolean isImplemented) {
        AcademicProgramMapping apMap = info.getAcademicProgram();
        ArrayList<CurriculumMapping> curriculums = info.getCurriculums();

        /**
         * Create row extension from fxml template.
         */
//        VBox programRowExtension = (VBox) Mono.fx().create()
//                .setPackageName("update2.org.cict.layout.academicprogram")
//                .setFxmlDocument("program-row-extension")
//                .makeFX()
//                .pullOutLayout();
        ProgramRowExtension rowFX = M.load(ProgramRowExtension.class);

        /**
         * Get Controls
         */
        TextField txt_code = rowFX.getTxt_program_code();//searchAccessibilityText(programRowExtension, "txt_program_code");
        TextField txt_floor_assignment = rowFX.getTxt_floor_assignment(); //searchAccessibilityText(programRowExtension, "txt_floor_assignment");
        Label lbl_created_date = rowFX.getLbl_created_date();//searchAccessibilityText(programRowExtension, "lbl_created_date");
        Label lbl_created_by = rowFX.getLbl_created_by(); //searchAccessibilityText(programRowExtension, "lbl_created_by");
        TextField txt_program_name = rowFX.getTxt_program_name(); //searchAccessibilityText(programRowExtension, "txt_program_name");
        Label lbl_status = rowFX.getLbl_status(); //searchAccessibilityText(programRowExtension, "lbl_status");
        Label lbl_implementation_date = rowFX.getLbl_implementation_date(); //searchAccessibilityText(programRowExtension, "lbl_implementation_date");
        Label lbl_implemented_by = rowFX.getLbl_implemented_by(); //searchAccessibilityText(programRowExtension, "lbl_implemented_by");
        JFXButton btn_save_changes = rowFX.getBtn_save_changes(); //searchAccessibilityText(programRowExtension, "btn_save_changes");
        JFXButton btn_newCurriculum = rowFX.getBtn_newCurriculum(); //searchAccessibilityText(programRowExtension, "btn_newCurriculum");
        JFXButton btn_delete = rowFX.getBtn_delete(); //searchAccessibilityText(programRowExtension, "btn_delete");
        VBox vbox_curriculum_table_holder = rowFX.getVbox_curriculum_table_holder(); //searchAccessibilityText(programRowExtension, "vbox_curriculum_table_holder");
        JFXButton btn_show = rowFX.getBtn_show(); //searchAccessibilityText(programRowExtension, "btn_show");
        VBox vbox_no_found_curriculum = rowFX.getVbox_no_found_curriculum(); //searchAccessibilityText(programRowExtension, "vbox_no_found_curriculum");
        Label lbl_no_found_curriculum = rowFX.getLbl_no_found_curriculum(); //searchAccessibilityText(programRowExtension, "lbl_no_found_curriculum");
        
        // filter
        addTextFieldFilters(txt_code, txt_program_name, txt_floor_assignment);
        
        /**
         * Disable if not administrator.
         */
        if (AcademicProgramAccessManager.denyIfNotAdmin()) {
            btn_save_changes.setDisable(true);
            btn_delete.setDisable(true);
            btn_newCurriculum.setDisable(true);
            
        }
        
        addClickEvent(btn_save_changes, () -> {
            this.updateAcademicProgram(programRow, txt_code, txt_program_name, txt_floor_assignment, apMap);
        });
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        // set values
        txt_code.setText(apMap.getCode().toUpperCase());
        try {
            String floor = apMap.getFloor_assignment().toString();
            txt_floor_assignment.setText(floor);
        } catch (NullPointerException a) {
        }
        
        txt_program_name.setText(apMap.getName().toUpperCase());
        // creation values
        lbl_created_date.setText(dateFormat.format(apMap.getCreated_date()));
        lbl_created_by.setText(info.getApCreatedBy());
        // status values
        if (apMap.getImplemented().equals(1)) {
            lbl_status.setText("SAVED");
            isImplemented = true;
            lbl_implementation_date.setText((apMap.getImplementation_date()==null? "" : dateFormat.format(apMap.getImplementation_date())));
            lbl_implemented_by.setText((info.getApImplementedBy()==null? "" : info.getApImplementedBy()));
            txt_code.setEditable(false);
            txt_program_name.setEditable(false);
            btn_delete.setDisable(true);
        } else {
            isImplemented = false;
            lbl_status.setText("N / A");
            lbl_implementation_date.setText("N / A");
            lbl_implemented_by.setText("N / A");
            addClickEvent(btn_delete, () -> {
                int count = 0;
                try {
                    curriculums.size();
                } catch (Exception e) {
                }
                String message = "Are you sure you want to remove this academic program?";
                if (count != 0) {
                    message = count + " curriculum exist. Do you still want to remove this academic program?";
                }
                int res = Mono.fx().alert()
                        .createConfirmation()
                        .setHeader("Delete A Program")
                        .setMessage(message)
                        .confirmYesNo();
                if (res == 1) {
                    apMap.setActive(0);
                    apMap.setRemoved_by(IMPLEMENTED_BY);
                    apMap.setRemoved_date(IMPLEMENTED_DATE);
                    if (Database.connect().academic_program().update(apMap)) {
                        try {
                            for(CurriculumMapping c: curriculums){
                                c.setActive(0);
                                c.setRemoved_by(IMPLEMENTED_BY);
                                c.setRemoved_date(IMPLEMENTED_DATE);
                                if(!Database.connect().curriculum().update(c))
                                    System.out.println("CURRICULUM " + c.getName() +" IS NOT REMOVED");
                            }
                        } catch (Exception e) {
                        }
                        Notifications.create()
                                .title("Deleted Successfully")
                                .text("The academic program and its curriculums is/are successfully removed from the list.")
                                .showInformation();
                        row.getChildren().clear();
                    }
                }
            });
        }

        /**
         * Row Extension Image Event.
         */
        super.addClickEvent(img_extension, () -> {
            if (row.isExtensionShown()) {
                img_extension.setImage(SimpleImage.make("update2.org.cict.layout.academicprogram.images", "show_extension.png"));
                row.hideExtension();
            } else {
                // close all row extension
                for (Node tableRows : programsTable.getRows()) {
                    SimpleTableRow simplerow = (SimpleTableRow) tableRows;
                    SimpleTableCell simplecell = simplerow.getCell(0);
                    HBox simplerowcontent = simplecell.getContent();
                    ImageView simplerowimage = findByAccessibilityText(simplerowcontent, "img_row_extension");
                    
                    simplerowimage.setImage(SimpleImage.make("update2.org.cict.layout.academicprogram.images", "show_extension.png"));
                    simplerow.hideExtension();
                }

                // show row extension
                img_extension.setImage(SimpleImage.make("update2.org.cict.layout.academicprogram.images", "hide_extension.png"));
                row.setRowExtension(rowFX.getApplicationRoot());
                row.showExtension();
            }
        });
        
        super.addDoubleClickEvent(row, () -> {
            if (row.isExtensionShown()) {
                img_extension.setImage(SimpleImage.make("update2.org.cict.layout.academicprogram.images", "show_extension.png"));
                row.hideExtension();
            } else {
                // close all row extension
                for (Node tableRows : programsTable.getRows()) {
                    SimpleTableRow simplerow = (SimpleTableRow) tableRows;
                    SimpleTableCell simplecell = simplerow.getCell(0);
                    HBox simplerowcontent = simplecell.getContent();
                    ImageView simplerowimage = findByAccessibilityText(simplerowcontent, "img_row_extension");
                    
                    simplerowimage.setImage(SimpleImage.make("update2.org.cict.layout.academicprogram.images", "show_extension.png"));
                    simplerow.hideExtension();
                }

                // show row extension
                img_extension.setImage(SimpleImage.make("update2.org.cict.layout.academicprogram.images", "hide_extension.png"));
                row.setRowExtension(rowFX.getApplicationRoot());
                row.showExtension();
            }
        });
        
        addClickEvent(btn_newCurriculum, () -> {
            labelCount = lbl_count;
            currentProgram = vbox_curriculum_table_holder;
            this.showAddNewCurriculum(apMap);
        });
        
        ArrayList<CurriculumMapping> implementedCurriculums = new ArrayList<>();
        ArrayList<CurriculumMapping> unImplementedCurriculums = new ArrayList<>();
        if (curriculums != null) {
            for (CurriculumMapping curriculum : curriculums) {
                if (curriculum.getImplemented() == 1) {
                    implementedCurriculums.add(curriculum);
                } else {
                    unImplementedCurriculums.add(curriculum);
                }
            }
        }
        
        if(implementedCurriculums.isEmpty()) {
            Animate.fade(vbox_curriculum_table_holder, 150, ()->{
                vbox_curriculum_table_holder.setVisible(false);
                vbox_no_found_curriculum.setVisible(true);
            }, vbox_no_found_curriculum); 
            lbl_no_found_curriculum.setText("No implemented curriculum found. Click " + btn_show.getText());
        } else {
            Animate.fade(vbox_no_found_curriculum, 150, ()->{
                vbox_no_found_curriculum.setVisible(false);
                vbox_curriculum_table_holder.setVisible(true);
            }, vbox_curriculum_table_holder);
            createCurriculumRows(vbox_curriculum_table_holder, implementedCurriculums, lbl_count);
        }
                
        this.addClickEvent(btn_show, ()->{
            String selected = btn_show.getText(); //cmb_sort.getSelectionModel().getSelectedIndex();
            vbox_curriculum_table_holder.getChildren().clear();
            
            if (selected.equalsIgnoreCase("Show Implemented")) {
                btn_show.setText("Show Unimplemented");
                if(implementedCurriculums.isEmpty()) {
                    Animate.fade(vbox_curriculum_table_holder, 150, ()->{
                        vbox_curriculum_table_holder.setVisible(false);
                        vbox_no_found_curriculum.setVisible(true);
                    }, vbox_no_found_curriculum);   
                    lbl_no_found_curriculum.setText("No implemented curriculum found. Click " + btn_show.getText());
                } else {
                    Animate.fade(vbox_no_found_curriculum, 150, ()->{
                        vbox_no_found_curriculum.setVisible(false);
                        vbox_curriculum_table_holder.setVisible(true);
                    }, vbox_curriculum_table_holder);
                    createCurriculumRows(vbox_curriculum_table_holder, implementedCurriculums, lbl_count);
                }
            } else {
                btn_show.setText("Show Implemented");
                if(unImplementedCurriculums.isEmpty()) {
                    Animate.fade(vbox_curriculum_table_holder, 150, ()->{
                        vbox_curriculum_table_holder.setVisible(false);
                        vbox_no_found_curriculum.setVisible(true);
                    }, vbox_no_found_curriculum);
                    lbl_no_found_curriculum.setText("No unimplemented curriculum found. Click " + btn_show.getText());
                } else {
                    Animate.fade(vbox_no_found_curriculum, 150, ()->{
                        vbox_no_found_curriculum.setVisible(false);
                        vbox_curriculum_table_holder.setVisible(true);
                    }, vbox_curriculum_table_holder);
                    createCurriculumRows(vbox_curriculum_table_holder, unImplementedCurriculums, lbl_count);
                 }
            }
        });
    }
    
    private void createCurriculumRows(VBox holder, ArrayList<CurriculumMapping> curriculum, Label lbl_count) {
        SimpleTable curriculumTable = new SimpleTable();
        if (curriculum == null || curriculum.isEmpty()) {
            return;
        }
        
        for (CurriculumMapping cur : curriculum) {
            SimpleTableRow row = new SimpleTableRow();
            row.setRowHeight(70.0);
            boolean isImplemented = false;
            if (cur.getImplemented() == 1) {
                isImplemented = true;
            }
            // load pre defined row layout
            HBox curriculumRow = (HBox) Mono.fx().create()
                    .setPackageName("update2.org.cict.layout.academicprogram")
                    .setFxmlDocument("curriculum-row")
                    .makeFX()
                    .pullOutLayout();
            
            Label lbl_major = searchAccessibilityText(curriculumRow, "lbl_major");
            Label lbl_name = searchAccessibilityText(curriculumRow, "lbl_name");
            Label lbl_ladderized = searchAccessibilityText(curriculumRow, "lbl_ladderized");
            Label lbl_years = searchAccessibilityText(curriculumRow, "lbl_years");
            JFXButton btn_details = searchAccessibilityText(curriculumRow, "btn_details");
            VBox btn_deleteCurriculum = searchAccessibilityText(curriculumRow, "btn_deleteCurriculum");
            // set values
            lbl_major.setText(WordUtils.capitalizeFully(cur.getMajor()));
            lbl_name.setText(cur.getName().toUpperCase());
            lbl_ladderized.setText(WordUtils.capitalizeFully(cur.getLadderization()));
            lbl_years.setText(cur.getStudy_years().toString());
            
            if (AcademicProgramAccessManager.denyIfNotAdmin()) {
                btn_deleteCurriculum.setDisable(true);
            }
            
            addClickEvent(btn_deleteCurriculum, () -> {
                AcademicProgramMapping apMap = Mono.orm().newSearch(Database.connect().academic_program())
                        .eq(DB.academic_program().id, cur.getACADPROG_id())
                        .active()
                        .first();
                
                if (apMap.getImplemented() != 1) {
                    int res = Mono.fx().alert()
                            .createConfirmation()
                            .setHeader("Remove Curriculum")
                            .setMessage("Are you sure you want to remove " + cur.getName()
                                    + " from the curriculum list?")
                            .confirmYesNo();
                    if (res == 1) {
                        cur.setActive(0);
                        cur.setRemoved_by(IMPLEMENTED_BY);
                        cur.setRemoved_date(IMPLEMENTED_DATE);
                        if (Database.connect().curriculum().update(cur)) {
                            row.getChildren().clear();
                            insertRemoveHistory(cur);
                            Notifications.create()
                                    .title("Removed Successfully")
                                    .text("Curriculum " + cur.getName() + " is removed successfully.")
                                    .showInformation();
                            Integer currentCount = Integer.valueOf(lbl_count.getText());
                            lbl_count.setText(String.valueOf(currentCount -= 1));
                        }
                    }
                } else {
                    Mono.fx().alert()
                            .createWarning()
                            .setHeader("Curriculum Cannot Be Removed")
                            .setMessage("It seems like this academic program is already saved, therefore removing is prohibited.")
                            .showAndWait();
                }
            });
            
            addClickEvent(btn_details, () -> {
                this.showCurriculumInfo(cur);
            });
            
            SimpleTableCell cellParent = new SimpleTableCell();
            cellParent.setResizePriority(Priority.ALWAYS);
            cellParent.setContent(curriculumRow);

            // add cell to row
            row.addCell(cellParent);

            // add row
            curriculumTable.addRow(row);
        }
        lbl_count.setText(String.valueOf(curriculum.size()));
        // when all details are added in the table;
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(curriculumTable);
        simpleTableView.setFixedWidth(true);
        // attach to parent variable name in scene builder
        simpleTableView.setParentOnScene(holder);
    }
    
    private void insertRemoveHistory(CurriculumMapping cur) {
        CurriculumHistorySummaryMapping chsMap = new CurriculumHistorySummaryMapping();
        chsMap.setActive(1);
        chsMap.setCreated_by(IMPLEMENTED_BY);
        chsMap.setCreated_date(IMPLEMENTED_DATE);
        chsMap.setCurriculum_id(cur.getId());
        chsMap.setDescription("REMOVED CURRICULUM [ID:" + cur.getId() + "] " + cur.getName());
        if (Database.connect().curriculum_history_summary().insert(chsMap) == -1) {
            System.out.println("REMOVED HISTORY NOT SAVED");
        }
    }
    
    private void showCurriculumInfo(CurriculumMapping curriculum) {
        CurriculumInformationController controller = new CurriculumInformationController(curriculum);
        Pane pane = Mono.fx().create()
                .setPackageName("update2.org.cict.layout.curriculum")
                .setFxmlDocument("curriculum-info")
                .makeFX()
                .setController(controller)
                .pullOutLayout();

        super.setSceneColor(SECTION_BASE_COLOR); // call once on entire scene lifecycle

        Animate.fade(this.application_root, SectionConstants.FADE_SPEED, () -> {
            super.replaceRoot(pane);
        }, pane);
    }
    
    private ArrayList<AcademicProgramInfo> acadProgramInfo;
    
    public void refreshAcademicProgramTable(long restTime) {
        if (restTime != 0) {
            this.vbox_table_holder.setVisible(false);
            this.vbox_search.setVisible(true);
        }
        
        FetchAcademicPrograms fetchProgramsTx = new FetchAcademicPrograms();
        fetchProgramsTx.setOnStart(onStart->{
            programsTable.getChildren().clear();
            btn_home1.setDisable(true);
        });
        fetchProgramsTx.setOnSuccess(onSuccess -> {
            acadProgramInfo = fetchProgramsTx.getAcademicProgramsCollection();
            createProgramsView(null);
            lbl_subtitle_no_found.setText("There " + (fetchProgramsTx.getImplementedCount()>1? "are ":"is ") + (fetchProgramsTx.getImplementedCount().equals(0)? "no" : fetchProgramsTx.getImplementedCount()) + " saved and " + (fetchProgramsTx.getUnimplementedCount().equals(0)? "no" : fetchProgramsTx.getUnimplementedCount()) + " not saved academic programs. Click " + btn_home1.getText() + ".");
            btn_home1.setDisable(false);
        });
        
        fetchProgramsTx.setRestTime(restTime);
        fetchProgramsTx.transact();
    }
    
    private void createProgramsView(AcademicProgramInfo newapInfo) {
        boolean isSaved = false;
        if (btn_home1.getText().equalsIgnoreCase("Show Not Saved")) {
            isSaved = true;
        }
        ArrayList<AcademicProgramInfo> viewedAcadPrograms = new ArrayList<>();
        if (newapInfo != null) {
            acadProgramInfo.add(newapInfo);
            if (newapInfo.getAcademicProgram().getImplemented() == 1) {
                if (isSaved) {
                    viewedAcadPrograms.add(newapInfo);
                }
            }
        }
        
        if (acadProgramInfo == null) {
            vbox_no_found.setVisible(true);
            if(isSaved) {
                lbl_title_no_found.setText("No Saved Academic Program Found");
                lbl_subtitle_no_found.setText("There are no saved or no saved Academic Program in the database.");
            }
            vbox_search.setVisible(false);
        }
        for (AcademicProgramInfo apInfo : acadProgramInfo) {
            if (apInfo.getAcademicProgram().getImplemented() == 1) {
                if (isSaved) {
                    viewedAcadPrograms.add(apInfo);
                }
            } else {
                if (!isSaved) {
                    viewedAcadPrograms.add(apInfo);
                }
            }
        }
        if (viewedAcadPrograms.isEmpty()) {
            vbox_no_found.setVisible(true);
            Mono.fx().thread().wrap(()->{
                if(btn_home1.getText().equalsIgnoreCase("Show Not Saved")) {
                    lbl_title_no_found.setText("No Saved Academic Program Found");
                } else {
                    lbl_title_no_found.setText("No Not Saved Academic Program Found");
                }
            });
            vbox_search.setVisible(false);
        } else {
            createProgramsTable(viewedAcadPrograms);
        }
    }
    
    private void implementAcademicProgram(AcademicProgramMapping apMap, ArrayList<CurriculumMapping> curriculums) {
//        if (curriculums.isEmpty()) {
//            Mono.fx().alert()
//                    .createInfo()
//                    .setHeader("No Curriculum Found")
//                    .setMessage("Saving a program requires "
//                            + "at least one (1) curriculum to continue.")
//                    .showAndWait();
//            return;
//        }
        int res = Mono.fx()
                .alert()
                .createConfirmation()
                .setHeader("Save Program")
                .setMessage("Saving this program will prohibit deleting its curriculum/s. Do you still want to save this program?")
                .confirmCustom("Yes, sure", "No");
        if (res == 1) {
            apMap.setImplementation_date(IMPLEMENTED_DATE);
            apMap.setImplemented(1);
            apMap.setImplemented_by(IMPLEMENTED_BY);
            if (Database.connect().academic_program().update(apMap)) {
                Notifications.create()
                        .title("Successfully Implemented")
                        .text(apMap.getCode() + ": " + apMap.getName()
                                + " successfully implemented.")
                        .showInformation();
                this.refreshAcademicProgramTable(500);
            } else {
                Notifications.create()
                        .title("Implementation Failed")
                        .text("Database connection might have a problem.")
                        .showInformation();
            }
        }
    }
    
    private void updateAcademicProgram(HBox programRow, TextField txt_code,
            TextField txt_program_name,
            TextField txt_floor_assignment,
            AcademicProgramMapping apMap) {
        
        String code = MonoString.removeExtraSpace(txt_code.getText()).toUpperCase();
        String programName = MonoString.removeExtraSpace(txt_program_name.getText()).toUpperCase();
        Integer floorAssignment = null;
        try {
            floorAssignment = Integer.valueOf(MonoString.removeExtraSpace(txt_floor_assignment.getText()));
        } catch (NumberFormatException a) {
        }
        if (code.isEmpty()) {
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
        AcademicProgramMapping codeExist = Mono.orm().newSearch(Database.connect().academic_program())
                .eq(DB.academic_program().code, code)
                .active()
                .first();
        if (codeExist != null
                && (!Objects.equals(codeExist.getId(), apMap.getId()))) {
            Mono.fx().alert()
                    .createWarning()
                    .setHeader("Program Code Exist")
                    .setMessage("Academic code is already in the list of programs. Try another one.")
                    .showAndWait();
            return;
        }
        
        if (programName.isEmpty()) {
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
        AcademicProgramMapping programNameExist = Mono.orm()
                .newSearch(Database.connect().academic_program())
                .eq(DB.academic_program().name, programName)
                .active()
                .first();
        if (programNameExist != null
                && (!Objects.equals(programNameExist.getId(), apMap.getId()))) {
            Mono.fx().alert()
                    .createWarning()
                    .setHeader("Program Name Exist")
                    .setMessage("Academic name is already in the list of programs. Try another one.")
                    .showAndWait();
            return;
        }
        if (floorAssignment == null) {
            Mono.fx().alert()
                    .createWarning()
                    .setHeader("Floor Assignment")
                    .setMessage("Please provide a floor assignment to proceed. "
                            + "Use digit or number to represent floors.")
                    .showAndWait();
            return;
        }
        
        boolean validFloor = false;
        for (Integer currentFloor : validFloorAssignment) {
            if (Objects.equals(floorAssignment, currentFloor)) {
                validFloor = true;
            }
        }
        if (validFloor) {
            //update here
            apMap.setCode(code);
            apMap.setName(programName);
            apMap.setFloor_assignment(floorAssignment);
            if (Database.connect().academic_program().update(apMap)) {
                Label lbl_code = searchAccessibilityText(programRow, "lbl_code");
                Label lbl_name = searchAccessibilityText(programRow, "lbl_name");
                /**
                 * set the values manually to avoid refresh of tables
                 */
                lbl_code.setText(code);
                lbl_name.setText(programName);
                txt_code.setText(code);
                txt_program_name.setText(programName);
                Notifications.create()
                        .title("Updated Successfully")
                        .text(code + " : " + programName)
                        .showInformation();
            } else {
                Notifications.create()
                        .title("Update Failed")
                        .text("Something went wrong in the update process.")
                        .showInformation();
            }
        } else {
            Mono.fx().alert()
                    .createWarning()
                    .setHeader("Invalid Floor Assignment")
                    .setMessage("Please provide a valid floor assignment to proceed. "
                            + "Use digit or number to represent floors.")
                    .showAndWait();
        }
    }
    
    private VBox currentProgram;
    private Label labelCount;
    
    private void showAddNewCurriculum(AcademicProgramMapping apMap) {
        AddNewCurriculumController controller = new AddNewCurriculumController();
        controller.academicProgram = apMap;
        Mono.fx().create()
                .setPackageName("update2.org.cict.layout.curriculum")
                .setFxmlDocument("add-curriculum")
                .makeFX()
                .setController(controller)
                .makeScene()
                .makeStageWithOwner(Mono.fx().getParentStage(btn_home))
                .stageTitle("New Curriculum")
                .stageResizeable(false)
                .stageShowAndWait();
        ArrayList<CurriculumMapping> curriculums = controller.getCurriculums();
        createCurriculumRows(currentProgram, curriculums, labelCount);
    }
    
    private void addTextFieldFilters(TextField txt_progcode, TextField txt_progname, TextField txt_floor) {
        StringFilter textField = TextInputFilters.string()
                .setFilterMode(StringFilter.LETTER_DIGIT_SPACE)
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
                .setFilterMode(StringFilter.DIGIT)
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
}

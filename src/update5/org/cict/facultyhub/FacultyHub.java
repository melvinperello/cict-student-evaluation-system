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
package update5.org.cict.facultyhub;

import app.lazy.models.AcademicProgramMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.FacultyMapping;
import app.lazy.models.LoadGroupMapping;
import app.lazy.models.LoadSectionMapping;
import app.lazy.models.LoadSubjectMapping;
import app.lazy.models.StudentMapping;
import app.lazy.models.SubjectMapping;
import com.itextpdf.text.Document;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.SimpleTask;
import com.jhmvin.fx.async.Transaction;
import com.jhmvin.fx.controls.SimpleImage;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import com.jhmvin.orm.Searcher;
import com.jhmvin.transitions.Animate;
import com.melvin.mono.fx.bootstrap.M;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import org.apache.commons.lang3.text.WordUtils;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.cict.authentication.authenticator.SystemProperties;
import org.cict.reports.ReportsUtility;
import org.cict.reports.result.PrintResult;
import org.controlsfx.control.Notifications;
import update.org.cict.controller.home.Home;
import update3.org.cict.controller.sectionmain.SubjectMasterListTransaction;
import update3.org.cict.layout.default_loader.LoaderView;
import update3.org.cict.scheduling.OpenScheduleViewer;
import update3.org.cict.window_prompts.empty_prompt.EmptyView;
import update3.org.cict.window_prompts.fail_prompt.FailView;
import update3.org.excelprinter.StudentMasterListPrinter;
import update3.org.excelreader.EncodeGradeFromExcel;
import update3.org.excelreader.ReadData;
import update3.org.excelreader.StudentMasterListReader;

/**
 *
 * @author Joemar
 */
public class FacultyHub extends SceneFX implements ControllerFX {

    @FXML
    private VBox application_root;

    @FXML
    private JFXButton btn_home;

    @FXML
    private JFXButton btn_view_sections;

    @FXML
    private JFXButton btn_view_subjects;

    @FXML
    private VBox vbox_section;

    @FXML
    private StackPane stack_section;

    @FXML
    private VBox vbox_section_table;

//    @FXML
//    private VBox vbox_section_no_found;
    @FXML
    private VBox vbox_subject;

    @FXML
    private StackPane stack_subject;

    @FXML
    private VBox vbox_subject_table;

//    @FXML
//    private VBox vbox_subject_no_found;
    private LoaderView loaderView;
    private FailView failView;
    private EmptyView emptyView;

    private LoaderView loaderView2;
    private FailView failView2;
    private EmptyView emptyView2;

    @Override
    public void onInitialization() {
        super.bindScene(application_root);

        this.loaderView = new LoaderView(stack_section);
        this.failView = new FailView(stack_section);
        this.emptyView = new EmptyView(stack_section);

        this.loaderView2 = new LoaderView(stack_subject);
        this.failView2 = new FailView(stack_subject);
        this.emptyView2 = new EmptyView(stack_subject);

        this.changeMainView(vbox_section);
        this.showLoadSection();
        this.showSubjectLoad();
    }

    private void detachAll() {
        this.loaderView.detach();
        this.failView.detach();
        this.emptyView.detach();

        this.loaderView2.detach();
        this.failView2.detach();
        this.emptyView2.detach();
    }

    @Override
    public void onEventHandling() {
        super.addClickEvent(btn_home, () -> {
            Home.callHome(this);
        });
        super.addClickEvent(btn_view_sections, () -> {
            this.changeMainView(vbox_section);
        });
        super.addClickEvent(btn_view_subjects, () -> {
            this.changeMainView(vbox_subject);
        });
    }

    private void changeMainView(Node node) {
        Animate.fade(node, 150, () -> {
            vbox_section.setVisible(false);
            vbox_subject.setVisible(false);
            node.setVisible(true);
        }, vbox_section, vbox_subject);
    }

    private void showSubjectLoad() {
        FetchSubjects subjectsTx = new FetchSubjects();
        subjectsTx.sectionMap = null;
        subjectsTx.facultyID = CollegeFaculty.instance().getFACULTY_ID();
        subjectsTx.whenStarted(() -> {
            this.detachAll();
            this.loaderView2.setMessage("Loading Subject");
            this.loaderView2.attach();
        });
        subjectsTx.whenCancelled(() -> {
            this.emptyView2.setMessage("No Subject");
            this.emptyView2.getButton().setVisible(false);
            this.emptyView2.attach();
        });
        subjectsTx.whenFailed(() -> {
            this.failView2.setMessage("Failed to Load Subject");
            this.failView2.attach();
        });
        subjectsTx.whenSuccess(() -> {
            this.createSubjectTable(subjectsTx.sectionSubjects);
        });
        subjectsTx.whenFinished(() -> {
            this.loaderView2.detach();
        });
        subjectsTx.transact();
    }

    private EmptyView scheduleEmpty;

    private void createSubjectTable(ArrayList<SubjectData> subjectData) {
        SimpleTable subjectTable = new SimpleTable();

        if (subjectData == null) {
//            this.changeViewSubject(vbox_subject_no_found);
            return;
        }

//        this.changeViewSubject(vbox_subject_table);
        for (SubjectData data : subjectData) {
            SimpleTableRow row = new SimpleTableRow();
            row.setRowHeight(65.0);
            SubjectRowFH rowFX = M.load(SubjectRowFH.class);
            rowFX.getLbl_code().setText(data.subject.getCode());
            rowFX.getLbl_description().setText(data.subject.getDescriptive_title());
            rowFX.getLbl_student_count().setText(data.studentCount);

            rowFX.getLbl_section().setText(data.getSectionName() + " - ");

            row.getRowMetaData().put("MORE_INFO", data);

            super.addClickEvent(rowFX.getPrint_export(), () -> {
                SubjectData info = (SubjectData) row.getRowMetaData().get("MORE_INFO");
                this.exportToExcel(info.loadGroup, rowFX.getPrint_export(), "", info.subject.getCode());
            });

            super.addClickEvent(rowFX.getBtn_schedule(), () -> {
                SubjectData info = (SubjectData) row.getRowMetaData().get("MORE_INFO");
                openScheduleViewer(info.sectionDetails, info.subject);
            });

            super.addClickEvent(rowFX.getBtn_print_pdf(), () -> {
                SubjectData info = (SubjectData) row.getRowMetaData().get("MORE_INFO");
                this.printMasterListInPDF(info.loadGroup, rowFX.getBtn_print_pdf(), info.getSectionName());
            });

            super.addClickEvent(rowFX.getPrint_import(), () -> {
                this.checkEncodingService(()->{
                    SubjectData info = (SubjectData) row.getRowMetaData().get("MORE_INFO");
                    this.readExcel(info.loadGroup, rowFX.getPrint_import(), info.getSectionName());
                });
            });

            SimpleTableCell cellParent = new SimpleTableCell();
            cellParent.setResizePriority(Priority.ALWAYS);
            cellParent.setContent(rowFX.getApplicationRoot());

            row.addCell(cellParent);
            subjectTable.addRow(row);
        }
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(subjectTable);
        simpleTableView.setFixedWidth(true);

        simpleTableView.setParentOnScene(vbox_subject_table);
    }
    
    private void showLoadSection() {
        FetchSection fetch = new FetchSection();
        fetch.whenStarted(() -> {
            this.detachAll();
            this.loaderView.setMessage("Loading Section");
            this.loaderView.attach();
        });
        fetch.whenCancelled(() -> {
            this.emptyView.setMessage("No Section");
            this.emptyView.getButton().setVisible(false);
            this.emptyView.attach();
        });
        fetch.whenFailed(() -> {
            this.failView.setMessage("Failed to Load Section");
            this.failView.attach();
        });
        fetch.whenSuccess(() -> {
            ArrayList<SectionData> loads = fetch.getSectionInformation();
            if (loads == null) {
                return;
            }
            this.createSectionTable(loads);
        });
        fetch.whenFinished(() -> {
            this.loaderView.detach();
        });
        fetch.transact();
    }

    private SimpleTable marshallTable = new SimpleTable();

    private void createSectionTable(ArrayList<SectionData> sectionInformation) {
        marshallTable.getChildren().clear();
        for (SectionData load : sectionInformation) {
            SimpleTableRow row = new SimpleTableRow();
            row.setRowHeight(70.0);
            SectionRow rowFX = M.load(SectionRow.class);
            rowFX.getLbl_section_name().setText(load.getSectionName());
            rowFX.getLbl_student_count().setText(load.studentCount);
            row.getRowMetaData().put("FX", rowFX);

            // Extension button was send to this function
            fetchSectionSubject(marshallTable, load.sectionDetails, load.getSectionName(), rowFX.getImg_extension(), row);

            SimpleTableCell cellParent = new SimpleTableCell();
            cellParent.setResizePriority(Priority.ALWAYS);
            cellParent.setContent(rowFX.getApplicationRoot());

            row.addCell(cellParent);
            marshallTable.addRow(row);
        }
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(marshallTable);
        simpleTableView.setFixedWidth(true);

        simpleTableView.setParentOnScene(vbox_section_table);
    }

    private void createLoadTable(SimpleTable tableHolder, ArrayList<SubjectData> subjectData, String sectionName, ImageView img_extension, SimpleTableRow row_) {
        // img_extension was passed in this method
        // functionality of that button will be added here

        ExtensionHolder holder = M.load(ExtensionHolder.class);
        SimpleTable subjectTable = new SimpleTable();

        if (subjectData == null) {
            return;
        }

        for (SubjectData data : subjectData) {
            SimpleTableRow row = new SimpleTableRow();
            row.setRowHeight(55.0);
            SubjectRowFH rowFX = M.load(SubjectRowFH.class);
            rowFX.getLbl_code().setText(data.subject.getCode());
            rowFX.getLbl_description().setText(data.subject.getDescriptive_title());
            rowFX.getLbl_student_count().setText(data.studentCount);

            row.getRowMetaData().put("MORE_INFO", data);

            super.addClickEvent(rowFX.getPrint_export(), () -> {
                SubjectData info = (SubjectData) row.getRowMetaData().get("MORE_INFO");
                this.exportToExcel(info.loadGroup, rowFX.getPrint_export(), sectionName, info.subject.getCode());
            });

            super.addClickEvent(rowFX.getBtn_schedule(), () -> {
                SubjectData info = (SubjectData) row.getRowMetaData().get("MORE_INFO");
                this.openScheduleViewer(info.sectionDetails, info.subject);
            });

            super.addClickEvent(rowFX.getBtn_print_pdf(), () -> {
                SubjectData info = (SubjectData) row.getRowMetaData().get("MORE_INFO");
                this.printMasterListInPDF(info.loadGroup, rowFX.getBtn_print_pdf(), info.getSectionName());
            });

            rowFX.getPrint_import().setDisable(true);
            super.addClickEvent(rowFX.getPrint_import(), () -> {
                this.checkEncodingService(()->{
                    SubjectData info = (SubjectData) row.getRowMetaData().get("MORE_INFO");
                    this.readExcel(info.loadGroup, rowFX.getPrint_import(), info.getSectionName());
                });
            });

            SimpleTableCell cellParent = new SimpleTableCell();
            cellParent.setResizePriority(Priority.ALWAYS);
            cellParent.setContent(rowFX.getApplicationRoot());

            row.addCell(cellParent);
            subjectTable.addRow(row);
        } // end loop for inner contents

        //----------------------------------------------------------------------
        /**
         * Row Extension Image Event.
         */
        super.addClickEvent(img_extension, () -> {
            System.out.println("IM CLICKED");

            if (row_.isExtensionShown()) {
                img_extension.setImage(SimpleImage.make("update2.org.cict.layout.academicprogram.images", "show_extension.png"));
                row_.hideExtension();
            } else {
                // close all row extension
                for (Node tableRows : tableHolder.getRows()) {
                    SimpleTableRow simplerow = (SimpleTableRow) tableRows;
                    SectionRow rowFX_ = (SectionRow) simplerow.getRowMetaData().get("FX");

                    ImageView simplerowimage = rowFX_.getImg_extension();//findByAccessibilityText(simplerowcontent, "img_row_extension");

                    simplerowimage.setImage(SimpleImage.make("update2.org.cict.layout.academicprogram.images", "show_extension.png"));
                    simplerow.hideExtension();
                }

                // show row extension
                img_extension.setImage(SimpleImage.make("update2.org.cict.layout.academicprogram.images", "hide_extension.png"));
                row_.setRowExtension(holder.getApplicationRoot());
                row_.showExtension();
            }
        });

        super.addDoubleClickEvent(row_, () -> {
            if (row_.isExtensionShown()) {
                img_extension.setImage(SimpleImage.make("update2.org.cict.layout.academicprogram.images", "show_extension.png"));
                row_.hideExtension();
            } else {
                // close all row extension
                for (Node tableRows : tableHolder.getRows()) {
                    SimpleTableRow simplerow = (SimpleTableRow) tableRows;
                    SectionRow rowFX_ = (SectionRow) simplerow.getRowMetaData().get("FX");

                    ImageView simplerowimage = rowFX_.getImg_extension();//findByAccessibilityText(simplerowcontent, "img_row_extension");

                    simplerowimage.setImage(SimpleImage.make("update2.org.cict.layout.academicprogram.images", "show_extension.png"));
                    simplerow.hideExtension();
                }

                // show row extension
                img_extension.setImage(SimpleImage.make("update2.org.cict.layout.academicprogram.images", "hide_extension.png"));
                row_.setRowExtension(holder.getApplicationRoot());
                row_.showExtension();
            }
        });
        
        //----------------------------------------------------------------------
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(subjectTable);
        simpleTableView.setFixedWidth(true);

        simpleTableView.setParentOnScene(holder.getVbox_subject_table());
    }

    private SubjectMapping getSubject(Integer id) {
        return (SubjectMapping) Database.connect().subject().getPrimary(id);
    }

    private void fetchSectionSubject(SimpleTable holder, LoadSectionMapping sectionMap, String sectionName, ImageView img_extension, SimpleTableRow row) {
        FetchSubjects subjectsTx = new FetchSubjects();
        subjectsTx.sectionMap = sectionMap;
        subjectsTx.facultyID = null;

        subjectsTx.whenSuccess(() -> {
            createLoadTable(holder, subjectsTx.sectionSubjects, sectionName, img_extension, row);
        });
        subjectsTx.transact();
    }

    //--------------------------------------------------------------------------
    /**
     * Get all subjects from this sections.
     */
    private class FetchSubjects extends Transaction {

        private LoadSectionMapping sectionMap;
        private Integer facultyID;

        /**
         * Results
         */
        private ArrayList<SubjectData> sectionSubjects;

        @Override
        protected boolean transaction() {
            sectionSubjects = new ArrayList<>();
            /**
             * Fetch all load Groups.
             */

            ArrayList<LoadGroupMapping> loadGroups = null;
            if (sectionMap != null) {
                loadGroups = Mono.orm()
                        .newSearch(Database.connect().load_group())
                        .eq(DB.load_group().LOADSEC_id, sectionMap.getId())
                        .active()
                        .all();
            } else if (facultyID != null) {
                loadGroups = Mono.orm()
                        .newSearch(Database.connect().load_group())
                        .eq(DB.load_group().faculty, facultyID)
                        .active()
                        .all();
            }
            if (loadGroups == null || loadGroups.isEmpty()) {
                return false;
            }
            /**
             * Get Subject Name.
             */
            for (LoadGroupMapping loadGroup : loadGroups) {
                SubjectData subjectData = new SubjectData();
                /**
                 * save load group data.
                 */
                subjectData.loadGroup = loadGroup;
                /**
                 * save subject data.
                 */
                subjectData.subject = Database.connect()
                        .subject()
                        .getPrimary(loadGroup.getSUBJECT_id());

                /**
                 * Get Student Count.
                 */
                Searcher studentCountSearch = Mono.orm()
                        .newSearch(Database.connect().load_subject())
                        .eq(DB.load_subject().LOADGRP_id, loadGroup.getId())
                        .eq(DB.load_subject().active, 1)
                        .pull();

                subjectData.studentCount = Mono.orm()
                        .projection(studentCountSearch)
                        .count(DB.load_subject().id);

                /**
                 * Get Instructor;
                 */
                if (loadGroup.getFaculty() == null) {
                    subjectData.instructorName = "No Data";
                } else {
                    FacultyMapping intructor = Database.connect().faculty().getPrimary(loadGroup.getFaculty());
                    try {
                        String instructorName = intructor.getLast_name() + ", ";

                        instructorName += intructor.getFirst_name();
                        if (intructor.getMiddle_name() != null) {
                            instructorName += (" " + intructor.getMiddle_name());
                        }
                        subjectData.instructorName = instructorName;
                    } catch (NullPointerException e) {
                        subjectData.instructorName = "Unreadable Data";
                    }
                }

                subjectData.sectionDetails = sectionMap == null ? Database.connect().load_section().getPrimary(loadGroup.getLOADSEC_id()) : sectionMap;

                sectionSubjects.add(subjectData);
            }

            return true;
        }

        @Override
        protected void after() {

        }
    }

    private class SubjectData {

        private LoadGroupMapping loadGroup;
        private SubjectMapping subject;
        private String studentCount;
        private String instructorName;
        private LoadSectionMapping sectionDetails;

        public String getSectionName() {
            String sectionName = "";
            if (sectionDetails.getACADPROG_id() != null) {
                AcademicProgramMapping programDetails = Database.connect().academic_program().getPrimary(sectionDetails.getACADPROG_id());
                if (programDetails != null) {
                    sectionName += programDetails.getCode() + " ";
                }
            }

            if (sectionDetails.getYear_level() != null) {
                sectionName += sectionDetails.getYear_level();
            }
            sectionName += sectionDetails.getSection_name();
            if (sectionDetails.get_group() != null) {
                sectionName += "-G" + sectionDetails.get_group();
            }
            return sectionName;
        }
    }

    private class FetchSection extends Transaction {

        private ArrayList<SectionData> sectionInformation;

        public ArrayList<SectionData> getSectionInformation() {
            return sectionInformation;
        }

        @Override
        protected boolean transaction() {
            ArrayList<LoadSectionMapping> loads = Mono.orm().newSearch(Database.connect().load_section())
                    .eq(DB.load_section().adviser, CollegeFaculty.instance().getFACULTY_ID())
                    .eq(DB.load_section().ACADTERM_id, SystemProperties.instance().getCurrentAcademicTerm().getId()).active().all();
            if (loads == null || loads.isEmpty()) {
                return false;
            }
            sectionInformation = new ArrayList<>();
            for (LoadSectionMapping sectionMap : loads) {
                SectionData sectionWithAdviser = new SectionData();
                sectionWithAdviser.sectionDetails = sectionMap;

                Integer facultyID = sectionMap.getAdviser();
                // if no adviser
                if (facultyID == null) {
                    //FacultyMapping adviserDetails = Database.connect().faculty().getPrimary(facultyID);
                    sectionWithAdviser.adviserName = "NOT SET";
                } else {

                    FacultyMapping adviserDetails = Database.connect().faculty().getPrimary(facultyID);
                    sectionWithAdviser.adviserName = getAdviserFullname(adviserDetails);
                } // end if

                /**
                 * Required for student count.
                 */
                String sectionName = sectionMap.getSection_name();
                Integer sectionYear = sectionMap.getYear_level();
                Integer sectionGroup = sectionMap.get_group();
                Integer currentTerm = SystemProperties.instance().getCurrentAcademicTerm().getId();
                Integer curID = sectionMap.getCURRICULUM_id();

                Searcher studentCountSearch = Mono.orm()
                        .newSearch(Database.connect().student())
                        .eq(DB.student().section, sectionName)
                        .eq(DB.student().year_level, sectionYear)
                        .eq(DB.student()._group, sectionGroup)
                        .eq(DB.student().last_evaluation_term, currentTerm)
                        .eq(DB.student().CURRICULUM_id, curID)
                        .pull();

                String studentCount = Mono.orm()
                        .projection(studentCountSearch)
                        .count(DB.student().cict_id);
                sectionWithAdviser.studentCount = studentCount;

                sectionWithAdviser.loadGroups = Mono.orm().newSearch(Database.connect().load_group())
                        .eq(DB.load_group().LOADSEC_id, sectionMap.getId()).active().all();

                sectionWithAdviser.programDetails = sectionMap.getACADPROG_id() == null ? null : Database.connect().academic_program().getPrimary(sectionMap.getACADPROG_id());

                sectionInformation.add(sectionWithAdviser);
            }
            return true;
        }

    }

    private String getAdviserFullname(FacultyMapping adviserDetails) {
        String advsierName = "";
        String lName = adviserDetails.getLast_name();
        if (lName != null) {
            advsierName = lName;
        }
        String fName = adviserDetails.getFirst_name();
        if (fName != null) {
            advsierName += ", " + fName;
        }
        String mName = adviserDetails.getMiddle_name();
        if (mName != null) {
            advsierName += " " + mName;
        }
        return advsierName;
    }

    private class SectionData {

        private AcademicProgramMapping programDetails;
        private LoadSectionMapping sectionDetails;
        private String adviserName;
        private String studentCount;
        private ArrayList<LoadGroupMapping> loadGroups;

        public String getSectionName() {
            String sectionName = "";
            if (programDetails != null) {
                sectionName += programDetails.getCode() + " ";
            }

            if (sectionDetails.getYear_level() != null) {
                sectionName += sectionDetails.getYear_level();
            }
            sectionName += sectionDetails.getSection_name();
            if (sectionDetails.get_group() != null) {
                sectionName += "-G" + sectionDetails.get_group();
            }
            return sectionName;
        }
    }

//    private void changeSectionView(Node node) {
//        Animate.fade(node, 150, ()->{
//            vbox_section_table.setVisible(false);
//            vbox_section_no_found.setVisible(false);
//            node.setVisible(true);
//        }, vbox_section_table, vbox_section_no_found);
//    }
    //-------------------------------------------------
    // EXPORT EXCEL
    private void exportToExcel(LoadGroupMapping selected_load_group_in_sections, JFXButton btn_export, String sectionName, String subjectCode) {
        SubjectMasterListTransaction exportTx = new SubjectMasterListTransaction();
        exportTx.setId(selected_load_group_in_sections.getId());

        exportTx.whenStarted(() -> {
            super.getScene().setCursor(Cursor.WAIT);
            btn_export.setDisable(true);
        });

        exportTx.whenSuccess(() -> {
            SimpleTask openTask = new SimpleTask("open-excel");
            openTask.setTask(() -> {
                String fileName = SystemProperties.instance().getCurrentTermString() + " "
                        + sectionName
                        + " "
                        + subjectCode;
                boolean printed = StudentMasterListPrinter.export(fileName, exportTx.getStudentData());
                if (!printed) {
                    Mono.fx().thread().wrap(()->{
                        Mono.fx().snackbar().showError(application_root, "Unable to open Excel File");
                    });
                } else {
                    Mono.fx().thread().wrap(()->{
                        Mono.fx().snackbar().showSuccess(application_root, "Excel File Exported");
                    });
                }
            });
//            openTask.whenStarted(()->{
//                super.getScene().setCursor(Cursor.WAIT);
//                btn_export.setDisable(true);
//            });
//            openTask.whenFinished(()->{
//                super.getScene().setCursor(Cursor.DEFAULT);
//                btn_export.setDisable(false);
//            });
            openTask.start();

        });

        exportTx.whenCancelled(() -> {
            // no results
            super.getScene().setCursor(Cursor.DEFAULT);
            btn_export.setDisable(false);
            Mono.fx().snackbar().showError(application_root, "No Data to Export");
        });

        exportTx.whenFailed(() -> {
            // no callback
        });

        exportTx.whenFinished(() -> {
            super.getScene().setCursor(Cursor.DEFAULT);
            btn_export.setDisable(false);
        });

        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(this.getStage());
        if (selectedDirectory == null) {
            return;
        }
        StudentMasterListPrinter.setExcelPath(selectedDirectory.getAbsolutePath());
        exportTx.transact();
    }

    //-------------------------------------------
    // SCHEDULE
    private void openScheduleViewer(LoadSectionMapping sectionMap, SubjectMapping subject) {
        
        String sectionString = "";
        AcademicProgramMapping coursecode = null;
        try {
            if (sectionMap.getACADPROG_id() != null) {
                coursecode = Database.connect().academic_program()
                        .getPrimary(sectionMap.getACADPROG_id());
            }
        } catch (Exception e) {
            coursecode = null;
        }

        if (coursecode != null) {
            try {
                sectionString = coursecode.getCode() + " " + sectionMap.getYear_level()
                        + sectionMap.getSection_name() + "-G" + sectionMap.get_group();
            } catch (Exception e) {
                sectionString = sectionMap.getSection_name();
            }
        } else {
            sectionString = sectionMap.getSection_name();
        }
        int res = Mono.fx().alert().createConfirmation()
                        .setHeader("Schedule Format")
                        .setMessage("Please choose if show the section's schedule or the specified subject's schedule only.")
                        .confirmCustom("Section Schedule", "Subject Schedule");
        if(res==-1) {
            sectionString = sectionString + " - " + subject.getCode().toUpperCase();
            OpenScheduleViewer.setSpecificSubject(subject.getId());
        }
        OpenScheduleViewer.openScheduleViewer(sectionMap, SystemProperties.instance().getCurrentTermString(), sectionString);
    }

    //-----------------------------------------
    // Print PDF
    private void printMasterListInPDF(LoadGroupMapping loadGroup, JFXButton btn_print, String sectionName) {
        FetchStudents fetch = new FetchStudents();
        fetch.loadGroup = loadGroup;
        fetch.whenSuccess(() -> {
            ArrayList<MasterListPdfStudent> preview = fetch.getResults();
            if (preview == null || preview.isEmpty()) {
                Notifications.create()
                        .title("No Student Found")
                        .text("No data to print.")
                        .showWarning();
                return;
            }
            String[] colNames = new String[]{"Student Number", "Last Name", "First Name", "Middle Name"};
            String[] colDescprtion = new String[]{"Student number of the student.", "Last name.", "First name.", "Middle name."};
            //--------------
            ArrayList<Object> colDetails = ReportsUtility.paperSizeChooserwithCustomize(Mono.fx().getParentStage(application_root), colNames, colDescprtion);
            Document doc = (Document) colDetails.get(0);
            if(doc==null) {
                return;
            }
            HashMap<Integer, Object[]> customized = (HashMap<Integer, Object[]>) colDetails.get(1);
            ArrayList<String> newColNames = new ArrayList<>();
            for (int i = 0; i < customized.size(); i++) {
                Object[] details = customized.get(i);
                if(details != null) {
                    Boolean isChecked = (Boolean) details[0];
                    if(isChecked) {
                        newColNames.add((String) details[1]);
                    }
                }
            }
            //
        
            ArrayList<String[]> rowData = new ArrayList<>();
            PrintResult print = new PrintResult();
            print.setDocumentFormat(doc, customized);

            for (int i = 0; i < preview.size(); i++) {
                MasterListPdfStudent result = preview.get(i);
                String[] row = new String[]{(i + 1) + ".  " + result.studentNumber,
                    WordUtils.capitalizeFully(result.lastName),
                WordUtils.capitalizeFully(result.firstName),
                WordUtils.capitalizeFully(result.middleName)};
                rowData.add(row);
            }
            print.columnNames = newColNames;
            print.ROW_DETAILS = rowData;
            SubjectMapping subject = this.getSubject(loadGroup.getSUBJECT_id());
            print.fileName = SystemProperties.instance().getCurrentTermString() + " " + subject.getCode() + " Master List " + String.valueOf(Calendar.getInstance().getTimeInMillis());
            print.reportTitleIntro = SystemProperties.instance().getCurrentTermString();

            print.reportTitleHeader = sectionName + " - " + subject.getCode() + " Master List";
            print.reportOtherDetail = (subject.getDescriptive_title());
            print.whenStarted(() -> {
                btn_print.setDisable(true);
                super.cursorWait();
            });
            print.whenCancelled(() -> {
                Notifications.create()
                        .title("Request Cancelled")
                        .text("Sorry for the inconviniece.")
                        .showWarning();
            });
            print.whenFailed(() -> {
                Notifications.create()
                        .title("Request Failed")
                        .text("Something went wrong. Sorry for the inconviniece.")
                        .showInformation();
            });
            print.whenSuccess(() -> {
                btn_print.setDisable(false);
                Notifications.create()
                        .title("Printing Results")
                        .text("Please wait a moment.")
                        .showInformation();
            });
            print.whenFinished(() -> {
                btn_print.setDisable(false);
                super.cursorDefault();
            });
//            print.transact();
            if(ReportsUtility.savePrintLogs(null, (sectionName + " - " + subject.getCode() + " Master List").toUpperCase(), "FACULTY HUB", "INITIAL"))
                print.transact();
        });
        fetch.transact();
    }

    private class FetchStudents extends Transaction {

        private LoadGroupMapping loadGroup;
        private ArrayList<MasterListPdfStudent> results;

        public ArrayList<MasterListPdfStudent> getResults() {
            return results;
        }

        @Override
        protected boolean transaction() {
            ArrayList<LoadSubjectMapping> loadSubjects = Mono.orm().newSearch(Database.connect().load_subject())
                    .eq(DB.load_subject().LOADGRP_id, loadGroup.getId()).active().all();
            if (loadSubjects == null) {
                return true;
            }
            results = new ArrayList<>();
            for (LoadSubjectMapping loadSubject : loadSubjects) {
                MasterListPdfStudent info = new MasterListPdfStudent();
                StudentMapping student = Database.connect().student().getPrimary(loadSubject.getSTUDENT_id());
                info.studentNumber = student.getId();
                info.lastName = student.getLast_name();
                info.firstName = student.getFirst_name();
                info.middleName = (student.getMiddle_name()==null? "": student.getMiddle_name());
                results.add(info);
            }
            return true;
        }

    }

    private class MasterListPdfStudent {

        private String studentNumber;
        private String lastName;
        private String firstName;
        private String middleName;
    }

    //-------------------------------------------
    // IMPORT EXCEL
    private void readExcel(LoadGroupMapping loadGroup, JFXButton button, String sectionName) {
        button.setDisable(true);
        ArrayList<ReadData> readData = StudentMasterListReader.readStudentGrade(this.getStage(), loadGroup.getSUBJECT_id());
        if (readData == null) {
            Mono.fx().alert().createWarning()
                    .setMessage(StudentMasterListReader.LOG).show();
            button.setDisable(false);
            return;
        }
        EncodeGradeFromExcel encode = new EncodeGradeFromExcel();
        encode.setLDGRP_id(loadGroup.getId());
        encode.setFACULTY_id(CollegeFaculty.instance().getFACULTY_ID());
        encode.setSUBJECT_id(loadGroup.getSUBJECT_id());
        encode.setImportedData(readData);
        encode.whenCancelled(() -> {
            Mono.fx().alert().createWarning()
                    .setMessage("Please check your connection to the server. Try again later.").show();
        });
        encode.whenSuccess(() -> {
            button.setDisable(false);
            Notifications.create().darkStyle()
                    .title("Successful")
                    .text("A status report will be printed.\n"
                            + "Please wait just a moment.").showInformation();
            // print here.
            this.printStatusReport(loadGroup, readData, button, sectionName);
        });
        encode.whenFailed(() -> {
            Mono.fx().alert().createWarning()
                    .setMessage("Please check your connection to the server. Try again later.").show();
        });
        encode.transact();
    }

    private void printStatusReport(LoadGroupMapping loadGroup, ArrayList<ReadData> preview, JFXButton btn_print, String sectionName) {
        if (preview == null || preview.isEmpty()) {
            Notifications.create()
                    .title("No Student Found")
                    .text("No data to print.")
                    .showWarning();
            return;
        }
        String[] colNames = new String[]{"Student Number", "Full Name", "Grade", "Status"};
        String[] colDescprtion = new String[]{"Student number.", "Full name of the student.", "Grade encoded to the student.", "Status of the encoded grade."};
        //--------------
        ArrayList<Object> colDetails = ReportsUtility.paperSizeChooserwithCustomize(Mono.fx().getParentStage(application_root), colNames, colDescprtion);
        Document doc = (Document) colDetails.get(0);
        if(doc==null) {
            return;
        }
        HashMap<Integer, Object[]> customized = (HashMap<Integer, Object[]>) colDetails.get(1);
        ArrayList<String> newColNames = new ArrayList<>();
        for (int i = 0; i < customized.size(); i++) {
            Object[] details = customized.get(i);
            if(details != null) {
                Boolean isChecked = (Boolean) details[0];
                if(isChecked) {
                    newColNames.add((String) details[1]);
                }
            }
        }
        //
        
        ArrayList<String[]> rowData = new ArrayList<>();
        PrintResult print = new PrintResult();
        print.setDocumentFormat(doc, customized);
        for (int i = 0; i < preview.size(); i++) {
            ReadData result = preview.get(i);
            String[] row = new String[]{(i + 1) + ".  " + result.getSTUDENT_NUMBER().toUpperCase(),
                WordUtils.capitalizeFully(result.getSTUDENT_NAME()),
                result.getSTUDENT_GRADE().isEmpty() ? "NONE" : result.getSTUDENT_GRADE()/*, result.getSTUDENT_CLEARANCE().equalsIgnoreCase("YES") || result.getSTUDENT_CLEARANCE().equals("1") ? "YES" : "NO"*/,
                result.getSTATUS()};
            rowData.add(row);
        }
        print.columnNames = newColNames;
        print.ROW_DETAILS = rowData;
        SubjectMapping subject = this.getSubject(loadGroup.getSUBJECT_id());
        print.fileName = SystemProperties.instance().getCurrentTermString() + " " + subject.getCode() + " Encode Status Report " + String.valueOf(Calendar.getInstance().getTimeInMillis());
        print.reportOtherDetail = sectionName + " - " + subject.getCode() + " | " + WordUtils.capitalizeFully(CollegeFaculty.instance().getFirstLastName());
        print.reportTitleIntro = SystemProperties.instance().getCurrentTermString();
        print.reportTitleHeader = "Encoding Status Report";
        print.whenStarted(() -> {
            btn_print.setDisable(true);
            super.cursorWait();
        });
        print.whenCancelled(() -> {
            Notifications.create()
                    .title("Request Cancelled")
                    .text("Sorry for the inconviniece.")
                    .showWarning();
        });
        print.whenFailed(() -> {
            Notifications.create()
                    .title("Request Failed")
                    .text("Something went wrong. Sorry for the inconviniece.")
                    .showInformation();
        });
        print.whenSuccess(() -> {
            btn_print.setDisable(false);
            Notifications.create()
                    .title("Printing Results")
                    .text("Please wait a moment.")
                    .showInformation();
        });
        print.whenFinished(() -> {
            btn_print.setDisable(false);
            super.cursorDefault();
        });
        if(ReportsUtility.savePrintLogs(null, "ENCODING STATUS REPORT", "FACULTY HUB", "INITIAL")) {
            print.transact();
        }
    }
    
    private void checkEncodingService(Runnable runMe) {
        Integer encoding_service = SystemProperties.instance().getCurrentAcademicTerm().getEncoding_service();
        if (encoding_service == null) {
            Mono.fx().alert().createWarning().setTitle("Checking Service")
                    .setHeader("Grade Encoding Service")
                    .setMessage("Cannot check the encoding service. Please Try Again.")
                    .show();
        } else {
            switch (encoding_service) {
                case 1:
                    runMe.run();
                    break;
                default:
                    Mono.fx().alert().createWarning().setTitle("Grade Encoding Service")
                            .setHeader("Grade Encoding is OFFLINE")
                            .setMessage("Grade Encoding Service is currently offline, no grade can be save at the moment")
                            .show();
                    break;
            }
        }
    }
}

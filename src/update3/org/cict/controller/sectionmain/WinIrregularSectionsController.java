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
package update3.org.cict.controller.sectionmain;

import app.lazy.models.AcademicTermMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.FacultyMapping;
import app.lazy.models.LoadGroupMapping;
import app.lazy.models.LoadSectionMapping;
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
import com.jhmvin.orm.Searcher;
import com.jhmvin.transitions.Animate;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.text.WordUtils;
import org.cict.authentication.authenticator.SystemProperties;
import org.hibernate.criterion.Order;
import update3.org.cict.SectionConstants;
import update3.org.cict.layout.default_loader.LoaderView;
import update3.org.cict.window_prompts.empty_prompt.EmptyView;

/**
 *
 * @author Jhon Melvin
 */
public class WinIrregularSectionsController extends SceneFX implements ControllerFX {

    private void sout(Object message) {
        System.out.println(this.getClass().getSimpleName() + ": " + message.toString()
        );
    }

    @FXML
    private VBox application_root;

    @FXML
    private Label lbl_header_section_type;

    @FXML
    private JFXButton btn_create_class;

    @FXML
    private JFXButton btn_back;

    @FXML
    private Label lbl_current_term;

    @FXML
    private Label lbl_curriculum_code;

    @FXML
    private Label lbl_curriculum_type;

    @FXML
    private StackPane stack_main;

    @FXML
    private VBox vbox_section;

    public WinIrregularSectionsController() {
        //
    }

    private LayoutDataFX homeFx;
    private String sectionType;

    public void setHomeFx(LayoutDataFX homeFx) {
        this.homeFx = homeFx;
    }

    public void setSectionType(String sectionType) {
        this.sectionType = sectionType;
    }

    /**
     * Attachments
     */
    private EmptyView emptySections;
    private LoaderView loaderView;

    @Override
    public void onInitialization() {
        super.bindScene(application_root);
        /**
         * Empty View.
         */
        this.emptySections = new EmptyView(stack_main);
        this.emptySections.getButton().setText("Refresh");
        super.addClickEvent(this.emptySections.getButton(), () -> {
            this.emptySections.detach();
            this.fetchSections();
        });

        this.lbl_current_term.setText(SystemProperties.instance().getCurrentTermString());
        /**
         * Triggered events.
         */

        this.lbl_header_section_type.setText(WordUtils.capitalizeFully(sectionType + " " + "Class"));
        this.fetchSections();

    }

    @Override
    public void onEventHandling() {
        super.addClickEvent(btn_back, () -> {
            Animate.fade(this.application_root, SectionConstants.FADE_SPEED, () -> {
                super.replaceRoot(homeFx.getApplicationRoot());
            }, homeFx.getApplicationRoot());
            /**
             * Detach empty view on back.
             */
            this.emptySections.detach();

            /**
             * Reload Home.
             */
            homeFx.<SectionHomeController>getController().reloadInformation();
        });
    }

    public void fetchSections() {
        FetchSections getSectionsTx = new FetchSections();
        getSectionsTx.sectionType = this.sectionType;

        getSectionsTx.whenCancelled(() -> {
            sout("No Special Classes");
            this.emptySections.attach();

        });
        getSectionsTx.whenFailed(() -> {
            sout("Failed to load Irregular Sections");
        });
        getSectionsTx.whenSuccess(() -> {
            sout("Sections Found.");
            createTable(getSectionsTx.sectionData);
        });
        getSectionsTx.whenFinished(() -> {
            sout("Done.");
        });

        getSectionsTx.transact();
    }

    private void createTable(ArrayList<IrregularSectionData> sectionData) {
        /**
         * Create Table
         */
        SimpleTable tblSections = new SimpleTable();

        /**
         * Create Divider.
         */
        SimpleTableRow divRow = new SimpleTableRow();
        divRow.setRowHeight(50.0);

        HBox divFx = (HBox) Mono.fx().create()
                .setPackageName("update3.org.cict.layout.sectionmain")
                .setFxmlDocument("row-div-yearlevel")
                .makeFX()
                .pullOutLayout();

        Label lbl_year_level = super.searchAccessibilityText(divFx, "lbl_year_level");
        Label lbl_section_count = super.searchAccessibilityText(divFx, "lbl_section_count");

        lbl_year_level.setText(WordUtils.capitalizeFully(this.sectionType + " Class"));

        SimpleTableCell divCell = new SimpleTableCell();
        divCell.setResizePriority(Priority.ALWAYS);
        divCell.setContentAsPane(divFx);
        divRow.addCell(divCell);
        tblSections.addRow(divRow);

        int sectionCount = 0;
        for (IrregularSectionData isd : sectionData) {
            SimpleTableRow row = new SimpleTableRow();
            row.setRowHeight(50.0);

            /**
             * Create FXML ROW.
             */
            RowIrregularSection ris = new RowIrregularSection();
            ris.lbl_section.setText(isd.loadSection.getSection_name());
            ris.lbl_instructor.setText(isd.getFacultyName());
            ris.lbl_count.setText(isd.studentCount);

            super.addClickEvent(ris.btn_section_information, () -> {
//                LayoutDataFX layoutFx = new LayoutDataFX(application_root, this);
//                SectionSubjectControllerIrregular controller = new SectionSubjectControllerIrregular();
//                controller.setWinIrregularSectionFx(layoutFx);
//                controller.setLoadGroup(isd.loadGroup);
//                controller.setLoadSection(isd.loadSection);
//                controller.setInstructorName(isd.getFacultyName());
//                controller.setStudentCount(isd.studentCount);
//
//                Pane irregularSectionRoot = Mono.fx()
//                        .create()
//                        .setPackageName("update3.org.cict.layout.sectionmain")
//                        .setFxmlDocument("win-irreg-section-view")
//                        .makeFX()
//                        .setController(controller)
//                        .pullOutLayout();
//                Animate.fade(this.application_root, SectionConstants.FADE_SPEED, () -> {
//                    super.replaceRoot(irregularSectionRoot);
//                }, irregularSectionRoot);

                /**
                 * Shared With Regular Section View.
                 */
                // current layout data
                LayoutDataFX fxData = new LayoutDataFX(application_root, this);
                SectionSubjectsController controller = new SectionSubjectsController();
                controller.setDataFx(fxData);
                // not included data in irregular sections
                controller.setAcademicProgramMap(null);/*?*/
                controller.setCurriculumMap(null);/*?*/
                // data for irregular sections
                controller.setSectionMap(isd.loadSection);
                controller.setCurrentTermString(SystemProperties.instance().getCurrentTermString());
                controller.setCurriculumType(lbl_curriculum_type.getText());/*?*/
                controller.setSectionName(isd.loadSection.getSection_name());

                Pane pane = Mono.fx()
                        .create()
                        .setPackageName("update3.org.cict.layout.sectionmain")
                        .setFxmlDocument("win-section-view")
                        .makeFX()
                        .setController(controller)
                        .pullOutLayout();

                Animate.fade(this.application_root, SectionConstants.FADE_SPEED, () -> {
                    super.replaceRoot(pane);
                }, pane);

                // post initialization when the fxml is loaded change the header
                controller.setWindowHeader(WordUtils.capitalizeFully(this.sectionType));

                // end interface
            });

            //
            SimpleTableCell cellParent = new SimpleTableCell();
            cellParent.setResizePriority(Priority.ALWAYS);
            cellParent.setContentAsPane(ris.rowSection);

            row.addCell(cellParent);

            tblSections.addRow(row);
            sectionCount++;
        }
        /**
         * Set Divider section count.
         */
        lbl_section_count.setText(String.valueOf(sectionCount));

        //
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(tblSections);
        simpleTableView.setFixedWidth(true);
        simpleTableView.setParentOnScene(vbox_section);
    }

    //--------------------------------------------------------------------------
    /**
     * Irregular Section Row.
     */
    private class RowIrregularSection extends SceneFX {

        private final Label lbl_section;
        private final Label lbl_count;
        private final Label lbl_instructor;
        private final JFXButton btn_section_information;

        private final HBox rowSection;

        public RowIrregularSection() {
            rowSection = (HBox) Mono.fx().create()
                    .setPackageName("update3.org.cict.layout.sectionmain")
                    .setFxmlDocument("row-irreg-section")
                    .makeFX()
                    .pullOutLayout();

            this.lbl_section = super.searchAccessibilityText(rowSection, "lbl_section");
            this.lbl_count = super.searchAccessibilityText(rowSection, "lbl_count");
            this.lbl_instructor = super.searchAccessibilityText(rowSection, "lbl_instructor");
            this.btn_section_information = super.searchAccessibilityText(rowSection, "btn_section_information");
        }
    }

    private class FetchSections extends Transaction {

        private String sectionType;
        private ArrayList<IrregularSectionData> sectionData;

        @Override
        protected boolean transaction() {
            sectionData = new ArrayList<>();
            AcademicTermMapping currentTerm = SystemProperties.instance().getCurrentAcademicTerm();

            ArrayList<LoadSectionMapping> irregularSections = Mono.orm()
                    .newSearch(Database.connect().load_section())
                    .eq(DB.load_section().ACADTERM_id, currentTerm.getId())
                    .isNull(DB.load_section().ACADPROG_id)
                    .isNull(DB.load_section().CURRICULUM_id)
                    .isNull(DB.load_section().adviser)
                    .eq(DB.load_section().college, "CICT")
                    .eq(DB.load_section().type, sectionType)
                    .active()
                    .all();

            if (irregularSections == null) {
                // no secton found.

                return false;
            }

            for (LoadSectionMapping results : irregularSections) {
                IrregularSectionData isd = new IrregularSectionData();

                /**
                 * Irregular Section has only one subject.
                 */
                LoadGroupMapping loadGroup = Mono.orm()
                        .newSearch(Database.connect().load_group())
                        .eq(DB.load_group().LOADSEC_id, results.getId())
                        .eq(DB.load_group().group_type, "REGULAR")
                        .active(Order.desc(DB.load_group().id))
                        .first();

                isd.loadSection = results;
                isd.loadGroup = loadGroup;

                if (loadGroup == null) {
                    System.err.println("No Load Group Found !!!!!!!!");
                    continue;
                }

                /**
                 * Get Instructor.
                 */
                if (loadGroup.getFaculty() != null) {
                    isd.instructor = Database.connect()
                            .faculty()
                            .getPrimary(loadGroup.getFaculty());
                }

                /**
                 * Get Count.
                 */
                Searcher studentCount = Mono.orm()
                        .newSearch(Database.connect().load_subject())
                        .eq(DB.load_subject().LOADGRP_id, loadGroup.getId())
                        .eq(DB.load_subject().active, 1)
                        .pull();

                isd.studentCount = Mono.orm().projection(studentCount).count(DB.load_subject().id);

                sectionData.add(isd);
            }

            return true;
        }

        @Override
        protected void after() {

        }
    }

    private class IrregularSectionData {

        private LoadSectionMapping loadSection;
        private LoadGroupMapping loadGroup;
        private FacultyMapping instructor;
        private String studentCount;

        public String getFacultyName() {
            if (this.instructor == null) {
                /**
                 * No Assigned Instructor;
                 */
                return "No Data";
            }

            String fullName = "";

            if (this.instructor.getLast_name() != null) {
                fullName += this.instructor.getLast_name();
                fullName += ", ";
            }

            if (this.instructor.getFirst_name() != null) {
                fullName += this.instructor.getFirst_name();
                fullName += " ";
            }

            if (this.instructor.getMiddle_name() != null) {
                fullName += this.instructor.getMiddle_name();
            }

            return fullName;

        }
    }

}

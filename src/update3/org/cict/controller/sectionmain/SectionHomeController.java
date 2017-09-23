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

import app.lazy.models.AcademicProgramMapping;
import app.lazy.models.AcademicTermMapping;
import app.lazy.models.CurriculumMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import update3.org.cict.SectionConstants;
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
import java.util.HashMap;
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
import update.org.cict.controller.home.Home;
import update3.org.cict.layout.default_loader.LoaderView;

/**
 *
 * @author Jhon Melvin
 */
public class SectionHomeController extends SceneFX implements ControllerFX {

    @FXML
    private VBox application_root;

    @FXML
    private JFXButton btn_home;

    @FXML
    private VBox vbox_section_type;

    @FXML
    private Label lbl_stat_regular;

    @FXML
    private Label lbl_stat_special;

    @FXML
    private Label lbl_stat_midyear;

    @FXML
    private Label lbl_stat_tutorial;

    @FXML
    private Label lbl_current_term;

    @FXML
    private StackPane stack_statistics;

    @FXML
    private StackPane stack_section_type;

    public SectionHomeController() {
        // constructor
    }
    private final String SECTION_BASE_COLOR = "#5E9CE9";

    private LoaderView statisticsLoader;
    private LoaderView sectionLoader;

    private String currentTermString;

    @Override
    public void onInitialization() {
        super.bindScene(application_root);
        this.statisticsLoader = new LoaderView(stack_statistics);
        this.sectionLoader = new LoaderView(stack_section_type);
        /**
         * Attachment
         */
        /**
         * Display current academic terms
         */
        this.currentTermString = SystemProperties.instance().getCurrentTermString();
        lbl_current_term.setText(this.currentTermString);

        /**
         * Get Section Statistics
         */
        FetchSectionStatistics sectionStatData = new FetchSectionStatistics();
        sectionStatData.whenStarted(() -> {
            this.statisticsLoader.setMessage("Loading Statistics");
            this.statisticsLoader.attach();
        });
        sectionStatData.whenSuccess(() -> {
            lbl_stat_regular.setText(sectionStatData.regularSectionCount);
            lbl_stat_special.setText(sectionStatData.specialSectionCount);
            lbl_stat_midyear.setText(sectionStatData.midyearSectionCount);
            lbl_stat_tutorial.setText(sectionStatData.tutorialSectionCount);
        });
        sectionStatData.whenFinished(() -> {
            this.statisticsLoader.detach();
        });

        sectionStatData.transact();

        /**
         * Load Section
         */
        loadSections();

    }

    @Override
    public void onEventHandling() {
        super.addClickEvent(btn_home, () -> {
            this.onBackToHome();
        });
    }

    /**
     * Back To Main Menu
     */
    private void onBackToHome() {
        super.finish();
        Home.callHome();
    }

    /**
     * Load All Sections. Display table.
     */
    private void loadSections() {
        SimpleTable tblSectionType = new SimpleTable();
        /**
         * Load Regular Sections.
         */
        onShowRegularSections(tblSectionType);

        // table view
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(tblSectionType);
        simpleTableView.setFixedWidth(true);
        // attach to parent variable name in scene builder
        simpleTableView.setParentOnScene(vbox_section_type);
    }

    /**
     * View Regular Sections. when section from regular flag was clicked this
     * will display all regular sections related to that curriculum.
     */
    private void onOpenSectionWindow(WinRegularSectionsController controller) {
        Pane pane = Mono.fx().create()
                .setPackageName("update3.org.cict.layout.sectionmain")
                .setFxmlDocument("win-regular-sections")
                .makeFX()
                .setController(controller)
                .pullOutLayout();

        super.setSceneColor(SECTION_BASE_COLOR); // call once on entire scene lifecycle

        Animate.fade(this.application_root, SectionConstants.FADE_SPEED, () -> {
            super.replaceRoot(pane);
        }, pane);
    }

    /**
     * Show Regular Sections.
     *
     * @param table
     */
    private void onShowRegularSections(SimpleTable table) {

        /**
         * Create Divider
         */
        SimpleTableRow divRow = new SimpleTableRow();
        divRow.setRowHeight(50.0);
        /**
         * Load Divider FX.
         */
        DividerRow dividerRow = new DividerRow();
        dividerRow.lbl_types.setText("Regular Sections");
        /**
         * Add divider to cell.
         */
        SimpleTableCell divCell = new SimpleTableCell();
        divCell.setResizePriority(Priority.ALWAYS);
        divCell.setContentAsPane(dividerRow.divTypes);
        // add cell to row
        divRow.addCell(divCell);
        // add row to table
        table.addRow(divRow);

        /**
         * Load Regular Sections.
         */
        FetchCurriculums curriculumTx = new FetchCurriculums();
        curriculumTx.whenStarted(() -> {
            this.sectionLoader.setMessage("Retrieving Section List");
            this.sectionLoader.attach();
        });

        curriculumTx.whenSuccess(() -> {
            /**
             * Load regular section data
             */
            onLoadRegularSectionData(curriculumTx, table);
            /**
             * Load Irregular Section data
             */
            onShowIrregularSections(table);
        });

        curriculumTx.whenFinished(() -> {
            this.sectionLoader.detach();
        });

        // start transaction
        curriculumTx.transact();

    }

    /**
     * Add Curriculum Rows of regular section to the table
     *
     * @param curriculumTx
     * @param table
     */
    private void onLoadRegularSectionData(FetchCurriculums curriculumTx, SimpleTable table) {
        for (StoreCurriculumInfo curriculum : curriculumTx.storeCurriculums) {
            AcademicProgramMapping academicProgram = curriculum.course;
            CurriculumMapping curriculumMap = curriculum.curriculum;
            /**
             * Create Sections
             */
            SimpleTableRow row = new SimpleTableRow();
            row.setRowHeight(50.0);

            /**
             * Load Regular Section FX.
             */
            SectionRow regularRow = new SectionRow();
            /**
             * Change row components
             */
            regularRow.lbl_code.setText(academicProgram.getCode());
            regularRow.lbl_name.setText(curriculumMap.getName());

            /**
             * Add Events to row components.
             */
            this.addClickEvent(regularRow.btn_viewsections, () -> {
                /**
                 * Create Section window controller
                 */
                WinRegularSectionsController controller = new WinRegularSectionsController();
                controller.setAcademicProgramMap(academicProgram);
                controller.setCurriculumMap(curriculumMap);
                controller.setCurrentTermString(this.currentTermString);
                /**
                 * open the FXML along with the controller
                 */
                onOpenSectionWindow(controller);
            });

            /**
             * Add FX to cell.
             */
            SimpleTableCell cellParent = new SimpleTableCell();
            cellParent.setResizePriority(Priority.ALWAYS);
            cellParent.setContentAsPane(regularRow.rowSection);

            /**
             * Add to row
             */
            row.addCell(cellParent);

            /**
             * Add to table.
             */
            table.addRow(row);
            // end loop
        }
    }

    /**
     * Display Irregular Sections.
     *
     * @param table
     */
    private void onShowIrregularSections(SimpleTable table) {
        /**
         * Create Divider
         */
        // create row
        SimpleTableRow divRow = new SimpleTableRow();
        divRow.setRowHeight(50.0);
        // create divider
        DividerRow dividerRow = new DividerRow();
        dividerRow.lbl_types.setText("Irregular Sections");
        // create cell
        SimpleTableCell divCell = new SimpleTableCell();
        divCell.setResizePriority(Priority.ALWAYS);
        divCell.setContentAsPane(dividerRow.divTypes);
        // add cell to row
        divRow.addCell(divCell);
        // add row to table
        table.addRow(divRow);

        /**
         * Get Irregular Section Types.
         */
        HashMap<String, String> types = SectionConstants.SECTION_TYPES();
        for (String string : types.keySet()) {
            String code = string;
            String name = types.get(code);
            //

            if (name.equalsIgnoreCase(SectionConstants.REGULAR)) {
                // skip regular sections.
                continue;
            }

            // create row
            SimpleTableRow row = new SimpleTableRow();
            row.setRowHeight(50.0);

            SectionRow irregularRows = new SectionRow();

            String classString = " CLASS";
            irregularRows.lbl_code.setText(code.toUpperCase());
            irregularRows.lbl_name.setText(WordUtils.capitalizeFully(name + classString));
            super.addClickEvent(irregularRows.btn_viewsections, () -> {
                LayoutDataFX homeFx = new LayoutDataFX(application_root, this);
                WinIrregularSectionsController controller = new WinIrregularSectionsController();
                controller.setHomeFx(homeFx);
                controller.setSectionType(name);

                Pane next_root = Mono.fx().create()
                        .setPackageName("update3.org.cict.layout.sectionmain")
                        .setFxmlDocument("win-irregular-sections")
                        .makeFX()
                        .setController(controller)
                        .pullOutLayout();

                super.setSceneColor(SECTION_BASE_COLOR); // call once on entire scene lifecyclez

                Animate.fade(this.application_root, SectionConstants.FADE_SPEED, () -> {
                    super.replaceRoot(next_root);
                }, next_root);
            });

            // create cell
            SimpleTableCell cellParent = new SimpleTableCell();
            cellParent.setResizePriority(Priority.ALWAYS);
            cellParent.setContentAsPane(irregularRows.rowSection);
            // add to row
            row.addCell(cellParent);
            // add to table
            table.addRow(row);

        }

    }

    /**
     * Sub Class for Section Row FX.
     */
    private class SectionRow {

        public HBox rowSection = (HBox) Mono.fx().create()
                .setPackageName("update3.org.cict.layout.sectionmain")
                .setFxmlDocument("row-section-types")
                .makeFX()
                .pullOutLayout();

        public JFXButton btn_viewsections = searchAccessibilityText(rowSection, "btn_viewsections");
        public Label lbl_code = searchAccessibilityText(rowSection, "lbl_code");
        public Label lbl_name = searchAccessibilityText(rowSection, "lbl_name");

    }

    /**
     * Sub Class for a divider row FX.
     */
    private class DividerRow {

        public HBox divTypes = (HBox) Mono.fx().create()
                .setPackageName("update3.org.cict.layout.sectionmain")
                .setFxmlDocument("row-div-types")
                .makeFX()
                .pullOutLayout();

        public Label lbl_types = searchAccessibilityText(divTypes, "lbl_types");
    }

    //--------------------------------------------------------------------------
    /**
     * Get Section Statistics Data.
     */
    private class FetchSectionStatistics extends Transaction {

        public String regularSectionCount = "0";
        public String specialSectionCount = "0";
        public String midyearSectionCount = "0";
        public String tutorialSectionCount = "0";

        @Override
        protected boolean transaction() {

            regularSectionCount = getSectionStat(SectionConstants.REGULAR);
            specialSectionCount = getSectionStat(SectionConstants.SPECIAL);
            midyearSectionCount = getSectionStat(SectionConstants.MIDYEAR);
            tutorialSectionCount = getSectionStat(SectionConstants.TUTORIAL);
            return true;
        }

        @Override
        protected void after() {

        }

        private String getSectionStat(String type) {
            AcademicTermMapping current = SystemProperties.instance().getCurrentAcademicTerm();
            Searcher regularSections = Mono.orm()
                    .newSearch(Database.connect().load_section())
                    .eq(DB.load_section().ACADTERM_id, current.getId())
                    .eq(DB.load_section().type, type)
                    .eq(DB.load_section().active, 1)
                    .pull();

            // any errors will be recieved by failed callback
            return Mono.orm()
                    .projection(regularSections)
                    .count(DB.load_section().id);
        }
    }

    /**
     * Get Curriculums to display section that are related to the respective
     * curriculum.
     */
    private class FetchCurriculums extends Transaction {

        public ArrayList<StoreCurriculumInfo> storeCurriculums = new ArrayList<>();
        private final Integer implemented = 0;

        @Override
        protected boolean transaction() {

            ArrayList<CurriculumMapping> curriculums
                    = Mono.orm().newSearch(Database.connect().curriculum())
                            .eq(DB.curriculum().implemented, implemented)
                            .active(Order.desc(DB.curriculum().id))
                            .all();

            for (CurriculumMapping curriculum : curriculums) {

                AcademicProgramMapping course = Database.connect().academic_program()
                        .getPrimary(curriculum.getACADPROG_id());

                StoreCurriculumInfo curriculum_info = new StoreCurriculumInfo();
                curriculum_info.curriculum = curriculum;
                curriculum_info.course = course;

                storeCurriculums.add(curriculum_info);
            }

            return true;
        }

        @Override
        protected void after() {

        }
    }

    private class StoreCurriculumInfo {

        public CurriculumMapping curriculum;
        public AcademicProgramMapping course;

    }

}

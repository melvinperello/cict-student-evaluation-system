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
import app.lazy.models.CurriculumMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.FacultyMapping;
import app.lazy.models.LoadSectionMapping;
import update3.org.cict.SectionConstants;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.SimpleTask;
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
import com.melvin.mono.fx.bootstrap.M;
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
import update3.org.cict.layout.default_loader.LoaderView;
import update3.org.cict.layout.sectionmain.RowSection;
import update3.org.cict.window_prompts.empty_prompt.EmptyView;

/**
 *
 * @author Jhon Melvin
 */
public class WinRegularSectionsController extends SceneFX implements ControllerFX {

    @FXML
    private VBox vbox_section;

    @FXML
    private VBox application_root;

    @FXML
    private JFXButton btn_create_sections;

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

    /**
     * Class Logger.
     *
     * @param message
     */
    private static void sout(Object message) {
        System.out.println(message.toString());
    }

    public WinRegularSectionsController() {

    }

    /**
     * View Attachments.
     */
    private LoaderView loaderView;
    private EmptyView emptyView;

    /**
     * Class Variables.
     */
    private AcademicProgramMapping academicProgramMap;
    private CurriculumMapping curriculumMap;
    private String currentTermString;

    public void setAcademicProgramMap(AcademicProgramMapping academicProgramMap) {
        this.academicProgramMap = academicProgramMap;
    }

    public void setCurriculumMap(CurriculumMapping curriculumMap) {
        this.curriculumMap = curriculumMap;
    }

    public void setCurrentTermString(String currentTermString) {
        this.currentTermString = currentTermString;
    }

    @Override
    public void onInitialization() {
        /**
         * bind to scene.
         */
        super.bindScene(application_root);
        /**
         * attachments.
         */
        this.loaderView = new LoaderView(stack_main);
        //
        this.emptyView = new EmptyView(stack_main);
        this.emptyView.getButton().setText("Refresh");
        super.addClickEvent(this.emptyView.getButton(), () -> {
            this.emptyView.detach();
            this.fetchSections();
        });

        lbl_current_term.setText(currentTermString);
        lbl_curriculum_code.setText(curriculumMap.getName());
        // curriculum type
        String curriculum_type = "";
        if (curriculumMap.getLadderization().equals("NO")) {
            curriculum_type = "Continous";
        } else {
            curriculum_type = curriculumMap.getLadderization_type();
        }
        curriculum_type = WordUtils.capitalizeFully(curriculum_type + " Curriculum");
        lbl_curriculum_type.setText(curriculum_type);

        // end curriculum type
        /**
         * Load Sections.
         */
        this.fetchSections();
    }

    /**
     * Load Sections.
     */
    public void fetchSections() {
        /**
         *
         */
        // create transactions
        FetchSections sectionsTx = new FetchSections();
        // section paramters
        sectionsTx.acadTermID = SystemProperties.instance().getCurrentAcademicTerm().getId();
        sectionsTx.acadProgID = academicProgramMap.getId();
        sectionsTx.curriculumID = curriculumMap.getId();

        sectionsTx.whenStarted(() -> {
            this.emptyView.detach();
            /**
             * During task start up, attach loader view.
             */
            this.loaderView.setMessage("Retrieving Sections");
            this.loaderView.attach();
        });

        sectionsTx.whenSuccess(() -> {
            /**
             * Continue to next task.
             */
            loadSectionTask(sectionsTx.sectionInformation);
        });

        sectionsTx.whenCancelled(() -> {
            /**
             * When there is no sections, canceled call back was triggered.
             */
            // remove loading
            this.loaderView.detach();
            /**
             * Show Empty
             */
            emptyView.attach();
        });

        sectionsTx.whenFinished(() -> {
            /**
             * When finished do not detached another task will run to execute
             * FXML LOADING.
             */
        });

        sectionsTx.transact();
    }

    /**
     * Load Section UI. to remove lag. another task to append each row the
     * table.
     *
     * @param sectionInformation
     */
    private void loadSectionTask(ArrayList<SectionData> sectionInformation) {
        SimpleTask loadSectionWrk = new SimpleTask("delay-load-sections");
        loadSectionWrk.setTask(() -> {
            loadSections(sectionInformation);
        });

        loadSectionWrk.whenSuccess(() -> {
            // Task Executed.
        });

        loadSectionWrk.whenFailed(() -> {
            // UI Loading Failed.s
        });

        loadSectionWrk.whenFinished(() -> {
            /**
             * Remove loading screen.
             */
            this.loaderView.detach();
        });
        loadSectionWrk.start();
    }

    @Override
    public void onEventHandling() {
        super.addClickEvent(btn_create_sections, () -> {

            LayoutDataFX layoutFx = new LayoutDataFX(application_root, this);
            SectionCreateWizard controller = new SectionCreateWizard();
            controller.setWinRegularSectionControllerFx(layoutFx);
            controller.setProgramMap(academicProgramMap);
            controller.setCurriculumMap(curriculumMap);
            controller.setCurriculumType(lbl_curriculum_type.getText());

            Pane rootSectionWizard = Mono.fx()
                    .create()
                    .setPackageName("update3.org.cict.layout.sectionmain")
                    .setFxmlDocument("win-section-wizard")
                    .makeFX()
                    .setController(controller)
                    .pullOutLayout();

            Animate.fade(this.application_root, SectionConstants.FADE_SPEED, () -> {
                super.replaceRoot(rootSectionWizard);
            }, rootSectionWizard);

        });

        super.addClickEvent(btn_back, () -> {
            SectionHomeController controller = new SectionHomeController();
            Pane pane = Mono.fx()
                    .create()
                    .setPackageName("update3.org.cict.layout.sectionmain")
                    .setFxmlDocument("sectionHome")
                    .makeFX()
                    .setController(controller)
                    .pullOutLayout();

            Animate.fade(this.application_root, SectionConstants.FADE_SPEED, () -> {
                super.replaceRoot(pane);
            }, pane);

            /**
             * detach empty view incase that there are no sections.
             */
            this.emptyView.detach();
        });
    }

    /**
     * Load Sections sorted by year level.
     */
    private void loadSections(ArrayList<SectionData> sectionInformation) {
        /**
         * Create Table
         */
        SimpleTable tblSections = new SimpleTable();

        /**
         * Loop year levels.
         */
        for (int yearlevel = 1; yearlevel <= 4; yearlevel++) {
            if (this.curriculumMap.getLadderization().equalsIgnoreCase("NO")) {
                // if this curriculum is not ladderized
                // continue all iterations.
            } else {
                // check if preparatory or consequent.
                if (curriculumMap.getLadderization_type().equalsIgnoreCase("PREPARATORY")) {
                    // if prep curriculum
                    if (yearlevel >= 3) {
                        continue;
                    }
                } else {
                    // if consequent curriculum
                    if (yearlevel <= 2) {
                        continue;
                    }
                }
            }
            /**
             * Load section divider.
             */
            Label lbl_section_count = loadSectionDivider(tblSections, yearlevel);
            /**
             * Load each sections.
             */
            Integer section_count = loadEachSections(tblSections, yearlevel, sectionInformation);

            lbl_section_count.setText(section_count.toString());
        }

        // table view
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(tblSections);
        simpleTableView.setFixedWidth(true);
        // attach to parent variable name in scene builder

        /**
         * run in FX thread. when interacting with the scene.
         */
        Mono.fx().thread().wrap(() -> {
            simpleTableView.setParentOnScene(vbox_section);
        });

    }

    /**
     * Create Sections divider.
     *
     * @param table
     * @param year
     */
    private Label loadSectionDivider(SimpleTable table, int year) {
        /**
         * Create Divider row
         */
        SimpleTableRow row = new SimpleTableRow();
        row.setRowHeight(50.0);

        HBox rowSection = (HBox) Mono.fx().create()
                .setPackageName("update3.org.cict.layout.sectionmain")
                .setFxmlDocument("row-div-yearlevel")
                .makeFX()
                .pullOutLayout();

        Label lbl_year_level = super.searchAccessibilityText(rowSection, "lbl_year_level");
        Label lbl_section_count = super.searchAccessibilityText(rowSection, "lbl_section_count");
        /**
         * Set values.
         */
        String yearString = (year == 1) ? "First"
                : (year == 2) ? "Second"
                        : (year == 3) ? "Third"
                                : "Fourth";
        yearString = yearString + " " + "Year";
        lbl_year_level.setText(yearString);

        SimpleTableCell cellParent = new SimpleTableCell();
        cellParent.setResizePriority(Priority.ALWAYS);
        cellParent.setContentAsPane(rowSection);

        row.addCell(cellParent);
        row.setRowIdentifier("heading");

        table.addRow(row);

        return lbl_section_count;
    }

    private Integer loadEachSections(SimpleTable table,
            Integer yearLevel,
            ArrayList<SectionData> sectionInformation) {
        /**
         * Load all sections with data
         */

        Integer sectionCount = 0;
        for (SectionData sectionData : sectionInformation) {
            LoadSectionMapping sectionMap = sectionData.sectionDetails;
            String adviserName = sectionData.adviserName;
            String studentCount = sectionData.studentCount;

            if (!sectionMap.getYear_level().equals(yearLevel)) {
                // if not equal
                continue;
            }

            /**
             * if equal show in table
             */
            SimpleTableRow row = new SimpleTableRow();
            row.setRowHeight(50.0);

            RowSection rowFx = M.load(RowSection.class);
//            HBox rowSection = (HBox) Mono.fx().create()
//                    .setPackageName("update3.org.cict.layout.sectionmain")
//                    .setFxmlDocument("row-section")
//                    .makeFX()
//                    .pullOutLayout();

//            JFXButton btn_section_information = super.searchAccessibilityText(rowSection, "btn_section_information");
//            Label lbl_section = super.searchAccessibilityText(rowSection, "lbl_section");
//            Label lbl_adviser = super.searchAccessibilityText(rowSection, "lbl_adviser");
//            Label lbl_count = super.searchAccessibilityText(rowSection, "lbl_count");
            JFXButton btn_section_information = rowFx.getBtn_information();
            Label lbl_section = rowFx.getLbl_section();
            Label lbl_adviser = rowFx.getLbl_adviser();
            Label lbl_count = rowFx.getLbl_count();

            String sectionName = sectionMap.getYear_level()
                    + sectionMap.getSection_name() + "-G"
                    + sectionMap.get_group();

            lbl_section.setText(sectionName);
            lbl_adviser.setText(adviserName);
            //System.out.println(sectionName + " : " + studentCount);
            lbl_count.setText(studentCount);

            super.addClickEvent(btn_section_information, () -> {
                // current layout data
                LayoutDataFX fxData = new LayoutDataFX(application_root, this);
                SectionSubjectsController controller = new SectionSubjectsController();
                controller.setDataFx(fxData);
                controller.setSectionMap(sectionMap);
                controller.setAcademicProgramMap(academicProgramMap);/*?*/
                controller.setCurriculumMap(curriculumMap);
                /*?*/
                controller.setCurrentTermString(currentTermString);
                controller.setCurriculumType(lbl_curriculum_type.getText());/*?*/
                controller.setSectionName(sectionName);
                controller.setSectionType("REGULAR");
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
            });

            SimpleTableCell cellParent = new SimpleTableCell();
            cellParent.setResizePriority(Priority.ALWAYS);
            cellParent.setContentAsPane(rowFx.getApplicationRoot());

            row.addCell(cellParent);
            row.setRowIdentifier("data");

            table.addRow(row);
            // increate section counter
            sectionCount++;
        } // end loop

        return sectionCount;

    }

    //--------------------------------------------------------------------------
    private class FetchSections extends Transaction {

        private Integer acadTermID;
        private Integer acadProgID;
        private Integer curriculumID;

        /**
         * Final variables.
         */
        private final String collegeName = "CICT";
        /**
         * Result Variables.
         */
        private ArrayList<SectionData> sectionInformation;

        @Override
        protected boolean transaction() {
            sectionInformation = new ArrayList<SectionData>();
            ArrayList<LoadSectionMapping> sectionsData = Mono.orm()
                    .newSearch(Database.connect().load_section())
                    .eq(DB.load_section().ACADTERM_id, this.acadTermID)
                    .eq(DB.load_section().ACADPROG_id, this.acadProgID)
                    .eq(DB.load_section().CURRICULUM_id, this.curriculumID)
                    //.eq(DB.load_section().year_level, this.yearLevel)
                    .eq(DB.load_section().type, SectionConstants.REGULAR)
                    .eq(DB.load_section().college, collegeName)
                    .active()
                    .all();

            if (sectionsData == null) {
                return false;
            }

            for (LoadSectionMapping sectionMap : sectionsData) {

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
                Integer currentTerm = this.acadTermID;
                Integer curID = this.curriculumID;

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
                sectionInformation.add(sectionWithAdviser);
            }

            return true;
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

        @Override
        protected void after() {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }

    /**
     *
     */
    private class SectionData {

        private AcademicProgramMapping programDetails;
        private LoadSectionMapping sectionDetails;
        private String adviserName;
        private String studentCount;
    }

}

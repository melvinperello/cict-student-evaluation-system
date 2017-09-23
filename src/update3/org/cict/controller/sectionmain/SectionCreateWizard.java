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
import app.lazy.models.CurriculumSubjectMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.LoadGroupMapping;
import app.lazy.models.LoadSectionMapping;
import app.lazy.models.MapFactory;
import update3.org.cict.SectionConstants;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import com.jhmvin.fx.async.TransactionException;
import com.jhmvin.fx.controls.MonoText;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.LayoutDataFX;
import com.jhmvin.fx.display.SceneFX;
import com.jhmvin.propertymanager.FormFormat;
import com.jhmvin.transitions.Animate;
import java.util.ArrayList;
import java.util.Locale;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.cict.authentication.authenticator.SystemProperties;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

/**
 *
 * @author Jhon Melvin
 */
public class SectionCreateWizard extends SceneFX implements ControllerFX {

    public void sout(Object message) {
        System.out.println(this.getClass().getSimpleName() + ": " + message.toString());
    }

    @FXML
    private VBox application_root;

    @FXML
    private JFXButton btn_back;

    @FXML
    private Label lbl_program_name;

    @FXML
    private Label lbl_curriculum_name;

    @FXML
    private Label lbl_curriculum_type;

    @FXML
    private VBox vbox_single;

    @FXML
    private Label lbl_single_term;

    @FXML
    private TextField txt_single_year_level;

    @FXML
    private TextField txt_single_section_name;

    @FXML
    private TextField txt_single_group_name;

    @FXML
    private TextField txt_single_adviser;

    @FXML
    private JFXButton btn_single_create;

    @FXML
    private JFXButton btn_single_back_main;

    @FXML
    private VBox vbox_main;

    @FXML
    private JFXButton btn_single_creation;

    @FXML
    private JFXButton btn_multi_creation;

    @FXML
    private VBox vbox_multi;

    @FXML
    private Label lbl_auto_term;

    @FXML
    private JFXButton btn_back121;

    @FXML
    private JFXButton btn_multi_back;

    public SectionCreateWizard() {
        //
    }

    private LayoutDataFX winRegularSectionControllerFx;
    private AcademicProgramMapping programMap;
    private CurriculumMapping curriculumMap;
    private String curriculumType;

    public void setWinRegularSectionControllerFx(LayoutDataFX winRegularSectionControllerFx) {
        this.winRegularSectionControllerFx = winRegularSectionControllerFx;
    }

    public void setProgramMap(AcademicProgramMapping programMap) {
        this.programMap = programMap;
    }

    public void setCurriculumMap(CurriculumMapping curriculumMap) {
        this.curriculumMap = curriculumMap;
    }

    public void setCurriculumType(String curriculumType) {
        this.curriculumType = curriculumType;
    }

    @Override
    public void onInitialization() {
        super.bindScene(application_root);
        /**
         * Default visibility.
         */
        this.vbox_main.setVisible(true);
        this.vbox_multi.setVisible(false);
        this.vbox_single.setVisible(false);

        /**
         * set labels.
         */
        this.lbl_program_name.setText(programMap.getName());
        this.lbl_curriculum_name.setText(curriculumMap.getName());
        this.lbl_curriculum_type.setText(curriculumType);

        this.lbl_single_term.setText(SystemProperties.instance().getCurrentTermString());
        this.lbl_auto_term.textProperty().bind(this.lbl_single_term.textProperty());

        this.addTextFilters();
    }

    private void addTextFilters() {
        /**
         * Filter Year Level.
         */
        FormFormat.IntegerFormat yearFilter = new FormFormat().new IntegerFormat();
        yearFilter.setMinLimit(1);
        yearFilter.setMaxLimit(4);
        yearFilter.setFilterAction(filter -> {
            //System.out.println(filter.getFilterMessage());
            // do something when filter fails.
        });
        yearFilter.filter(this.txt_single_year_level.textProperty());

        /**
         * Filter Section Name.
         */
        FormFormat.CustomFormat sectionNameFilter = new FormFormat().new CustomFormat();
        sectionNameFilter.setMaxCharacters(1);
        sectionNameFilter.setStringFilter(text -> {
            /**
             * Custom Filter.
             */
            return StringUtils.isAlpha(text);
        });
        sectionNameFilter.setFilterAction(filterAction -> {
            //System.out.println(filterAction.getFilterMessage());
            // do something when filter fails.
        });
        sectionNameFilter.filter(this.txt_single_section_name.textProperty());

        /**
         * Filter Group.
         */
        FormFormat.IntegerFormat groupFilter = new FormFormat().new IntegerFormat();
        groupFilter.setMinLimit(1);
        groupFilter.setMaxLimit(2);
        groupFilter.setFilterAction(filter -> {
            //System.out.println(filter.getFilterMessage());
            // do something when filter fails.
        });
        groupFilter.filter(this.txt_single_group_name.textProperty());
    }

    @Override
    public void onEventHandling() {
        /**
         * Back to Section List.
         */
        super.addClickEvent(btn_back, () -> {
            Animate.fade(this.application_root, SectionConstants.FADE_SPEED, () -> {
                super.replaceRoot(winRegularSectionControllerFx.getApplicationRoot());

            }, winRegularSectionControllerFx.getApplicationRoot());
            // refresh section list.
            winRegularSectionControllerFx.
                    <WinRegularSectionsController>getController()
                    .fetchSections();

        });

        /**
         * Single creation.
         */
        super.addClickEvent(btn_single_creation, () -> {
            Animate.fade(vbox_main, SectionConstants.FADE_SPEED, () -> {
                vbox_main.setVisible(false);
                vbox_single.setVisible(true);
            }, vbox_single);
        });
        /**
         * single creation back to options.
         */
        super.addClickEvent(btn_single_back_main, () -> {
            Animate.fade(vbox_main, SectionConstants.FADE_SPEED, () -> {
                vbox_main.setVisible(true);
                vbox_single.setVisible(false);
            }, vbox_main);
        });

        /**
         * Automatic creation.
         */
        super.addClickEvent(btn_multi_creation, () -> {
            Animate.fade(vbox_main, SectionConstants.FADE_SPEED, () -> {
                vbox_multi.setVisible(true);
                vbox_main.setVisible(false);
            }, vbox_multi);
        });

        /**
         * Automatic Creation back to menu.
         */
        super.addClickEvent(btn_multi_back, () -> {
            Animate.fade(vbox_main, SectionConstants.FADE_SPEED, () -> {
                vbox_main.setVisible(true);
                vbox_multi.setVisible(false);

            }, vbox_main);
        });

        /**
         * Create New Single Section.
         */
        super.addClickEvent(btn_single_create, () -> {
            this.createRegularSingle();
        });
    }

    /**
     * Inserts new single section in the database.
     */
    private void createRegularSingle() {
        if (this.txt_single_year_level.getText().trim().isEmpty()) {
            Mono.fx().snackbar().showError(this.application_root, "Please Assign a Year Level.");
            return;
        }
        if (this.txt_single_section_name.getText().trim().isEmpty()) {
            Mono.fx().snackbar().showError(this.application_root, "Please Assign a Section Name.");
            return;
        }
        if (this.txt_single_group_name.getText().trim().isEmpty()) {
            Mono.fx().snackbar().showError(this.application_root, "Please Assign a Section Group.");
            return;
        }

        int selected = Mono.fx().alert().createConfirmation()
                .setTitle("Confirm")
                .setHeader("Create New Section ?")
                .setMessage("Are you sure you want to add this section ?")
                .confirmYesNo();

        if (selected == 1) {
            CreateRegularSection createSingleTx = new CreateRegularSection();
            createSingleTx.secionGroup = MonoText.getInt(txt_single_group_name); //Integer.valueOf(this.txt_single_group_name.getText().trim().toUpperCase(Locale.ENGLISH));
            createSingleTx.yearLevel = MonoText.getInt(txt_single_year_level); //Integer.valueOf(this.txt_single_year_level.getText().trim().toUpperCase(Locale.ENGLISH));
            createSingleTx.sectionName = MonoText.getFormatted(txt_single_section_name); //this.txt_single_section_name.getText().trim().toUpperCase(Locale.ENGLISH);
            createSingleTx.curriculumMapping = this.curriculumMap;

            createSingleTx.whenStarted(() -> {
                btn_single_create.setDisable(true);
            });
            createSingleTx.whenCancelled(() -> {
                Mono.fx().snackbar().showError(this.application_root, "Section Already Exist.");
            });
            createSingleTx.whenFailed(() -> {
                Mono.fx().snackbar().showError(this.application_root, "Connection Error.");
            });
            createSingleTx.whenSuccess(() -> {
                Mono.fx().snackbar().showSuccess(this.application_root, "Section Successfully Added.");
                MonoText.clear(txt_single_group_name, txt_single_year_level, txt_single_section_name);
            });
            createSingleTx.whenFinished(() -> {
                btn_single_create.setDisable(false);
            });
            createSingleTx.transact();
        }
    }

    //--------------------------------------------------------------------------
    private class CreateRegularSection extends Transaction {

        public String sectionName;
        public Integer yearLevel;
        public Integer secionGroup;
        public CurriculumMapping curriculumMapping;

        @Override
        protected boolean transaction() {
            AcademicTermMapping currentTerm = SystemProperties.instance().getCurrentAcademicTerm();
            /**
             * search if exist.
             */
            LoadSectionMapping exist = Mono.orm().newSearch(Database.connect().load_section())
                    .eq(DB.load_section().section_name, sectionName)
                    .eq((DB.load_section().year_level), yearLevel)
                    .eq((DB.load_section()._group), secionGroup)
                    .eq(DB.load_section().CURRICULUM_id, curriculumMapping.getId())
                    .eq(DB.load_section().ACADTERM_id, currentTerm.getId())
                    .active()
                    .first();

            if (exist != null) {
                System.out.println(exist.getSection_name());
                sout("Section Existing");
                return false;
            }

            /**
             * When not existing proceed to creation.
             */
            LoadSectionMapping newLoadSection = MapFactory.map().load_section();
            newLoadSection.setACADTERM_id(currentTerm.getId());
            newLoadSection.setACADPROG_id(this.curriculumMapping.getACADPROG_id());
            newLoadSection.setCURRICULUM_id(this.curriculumMapping.getId());
            newLoadSection.setSection_name(sectionName);
            newLoadSection.setYear_level(yearLevel);
            newLoadSection.set_group(secionGroup);
            newLoadSection.setType(SectionConstants.REGULAR);
            newLoadSection.setCollege("CICT");
            newLoadSection.setCreated_date(Mono.orm().getServerTime().getDateWithFormat());
            newLoadSection.setCreated_by(CollegeFaculty.instance().getFACULTY_ID());

            Session localSession = Mono.orm().openSession();
            org.hibernate.Transaction dataTx = localSession.beginTransaction();

            /**
             * Insert section temporarily.
             */
            int temp_section_id = Database.connect()
                    .load_section()
                    .transactionalInsert(localSession, newLoadSection);

            if (temp_section_id <= 0) {
                /**
                 * Throw exception.
                 */
                dataTx.rollback();
                throw new TransactionException("load_section insertion error");
            }

            ArrayList<CurriculumSubjectMapping> subjects = Mono.orm().newSearch(Database.connect().curriculum_subject())
                    .eq(DB.curriculum_subject().CURRICULUM_id, curriculumMapping.getId())
                    .eq(DB.curriculum_subject().year, yearLevel)
                    .eq(DB.curriculum_subject().semester, currentTerm.getSemester_regular())
                    .active(Order.asc(DB.curriculum_subject().id))
                    .all();

            for (CurriculumSubjectMapping subject : subjects) {
                LoadGroupMapping loadGroup = MapFactory.map().load_group();
                loadGroup.setSUBJECT_id(subject.getSUBJECT_id());
                loadGroup.setLOADSEC_id(temp_section_id);
                loadGroup.setGroup_type("REGULAR");
                loadGroup.setAdded_date(Mono.orm().getServerTime().getDateWithFormat());
                loadGroup.setAdded_by(CollegeFaculty.instance().getFACULTY_ID());

                int temp_load_group = Database.connect().load_section()
                        .transactionalInsert(localSession, loadGroup);

                if (temp_load_group <= 0) {
                    /**
                     * Throw exception.
                     */
                    dataTx.rollback();
                    throw new TransactionException("load_group insertion error");
                }
            }

            /**
             * If ended properly.
             */
            dataTx.commit();
            return true;
        }

        @Override
        protected void after() {

        }
    }
}
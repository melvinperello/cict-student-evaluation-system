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
package org.cict.evaluation;

import app.lazy.models.CurriculumMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.StudentMapping;
import artifacts.MonoString;
import com.izum.fx.textinputfilters.StringFilter;
import com.izum.fx.textinputfilters.TextInputFilters;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jhmvin.Mono;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import java.util.ArrayList;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import sys.org.cict.enumerations.CurriculumValues;
import update3.org.cict.controller.sectionmain.SectionCreateWizard;

/**
 *
 * @author Joemar
 */
public class MissingInfoController extends SceneFX implements ControllerFX {

    @FXML
    private VBox vbox_main;

    @FXML
    private ComboBox<String> cmb_curriculum;

    @FXML
    private ComboBox<String> cmb_year_level;

    @FXML
    private TextField txt_section;

    @FXML
    private TextField txt_type;
            
    @FXML
    private TextField txt_group;

    @FXML
    private JFXButton btn_proceed;

    public MissingInfoController(StudentMapping student) {
        STUDENT = student;
    }

    private ArrayList<Integer> curriculumIDs = new ArrayList<>();
    private StudentMapping STUDENT;
    // general name
    private Pane application_root;

    @Override
    public void onInitialization() {
        this.application_root = vbox_main;
        super.bindScene(application_root);
        this.loadComboBoxValues();
        this.loadDefaultValues();
        this.checkEvent();

        /**
         * Add Filters in group and section same with create wizard.
         */
        SectionCreateWizard.sectionGroupFilter(txt_section, txt_group);
        txt_type.setText(type);
    }

    private void checkEvent() {
        this.txt_type.textProperty().addListener((a) -> {
            if(type==null)
                return;
            cmb_curriculum.setDisable(type.equalsIgnoreCase(StudentType.CROSS_ENROLLEE.replace("_", "-")));
            cmb_year_level.setDisable(type.equalsIgnoreCase(StudentType.CROSS_ENROLLEE.replace("_", "-")));
            txt_section.setDisable(type.equalsIgnoreCase(StudentType.CROSS_ENROLLEE.replace("_", "-")));
            txt_group.setDisable(type.equalsIgnoreCase(StudentType.CROSS_ENROLLEE.replace("_", "-")));
        });
    }

    private void loadComboBoxValues() {
        //----------------------------------------------------------------------
        ArrayList<CurriculumMapping> allCurriculums = Mono.orm()
                .newSearch(Database.connect().curriculum())
                // must not be obsolete
                .eq(DB.curriculum().obsolete_term, 0)
                // must not be consequent
                .put(Restrictions.ne(DB.curriculum().ladderization_type, CurriculumValues.LadderType.CONSEQUENT.toString()))
                // must be implemented
                .eq(DB.curriculum().implemented, 1)
                .active(Order.desc(DB.curriculum().id))
                .all();
        //----------------------------------------------------------------------

        if (allCurriculums == null) {
            System.out.println("NO CURRICULUM FOUND");
        } else {
            for (CurriculumMapping curriculum : allCurriculums) {
                if (curriculum.getLadderization().equalsIgnoreCase("YES")) {
                    if (curriculum.getLadderization_type().equalsIgnoreCase("PREPARATORY")) {
                        cmb_curriculum.getItems().add(curriculum.getName());
                        curriculumIDs.add(curriculum.getId());
                    }
                } else {
                    cmb_curriculum.getItems().add(curriculum.getName());
                    curriculumIDs.add(curriculum.getId());
                }

                try {
                    //get the curriculum if not null
                    if (STUDENT.getCURRICULUM_id() != null) {
                        if (Objects.equals(STUDENT.getCURRICULUM_id(), curriculum.getId())) {
                            cmb_curriculum.getSelectionModel().select(curriculum.getName());
                        }
                    }
                } catch (NullPointerException e) {
                    System.out.println("@MissingInfoController: NULL CURRICULUM ID");
                }
            }
        }

        cmb_year_level.getItems().add("First Year");
        cmb_year_level.getItems().add("Second Year");
        cmb_year_level.getItems().add("Third Year");
        cmb_year_level.getItems().add("Fourth Year");
        cmb_year_level.getSelectionModel().selectFirst();
    }

    private void loadDefaultValues() {
        try {
            if (STUDENT.getCURRICULUM_id() == null) {
                cmb_curriculum.getSelectionModel().selectFirst();
            }

            if (STUDENT.getYear_level() != null) {
                cmb_year_level.getSelectionModel().select((STUDENT.getYear_level() - 1));
            }

            if (STUDENT.getSection() != null) {
                txt_section.setText(STUDENT.getSection());
            }

            if (STUDENT.get_group() != null) {
                txt_group.setText(STUDENT.get_group() + "");
            }

//            if (STUDENT.getAdmission_year() != null || STUDENT.getAdmission_year().equalsIgnoreCase("not_set")) {
//                txt_adyear.setText(STUDENT.getAdmission_year());
//            }
        } catch (NullPointerException e) {
        }
    }
    
    private String type = "Regular";
    @Override
    public void onEventHandling() {
        addClickEvent(btn_proceed, () -> {
            validateInput();
        });
        addClickEvent(txt_type, ()->{
            type = StudentType.showStudentTypeChooser(this.getStage());
            txt_type.setText(type==null? txt_type.getText() : type);
        });

    }

    private void validateInput() {
        type = type==null? txt_type.getText() : type;
        if (type.equalsIgnoreCase(StudentType.CROSS_ENROLLEE.replace("_", "-"))) {
            this.crossEnrollee();
            return;
        }
        String section = MonoString.removeExtraSpace(txt_section.getText());
        String group = MonoString.removeExtraSpace(txt_group.getText());
//        String adyear = MonoString.removeExtraSpace(txt_adyear.getText());
        if (section == null) {
            Mono.fx().alert().createWarning()
                    .setHeader("No Section Found")
                    .setMessage("Please enter a section.")
                    .show();
            return;
        }
        Integer groupInt = 0;
        try {
            if (group == null) {
                Mono.fx().alert().createWarning()
                        .setHeader("No Group Found")
                        .setMessage("Please enter a group.")
                        .show();
                return;
            }
            try {
                groupInt = Integer.valueOf(group);
                if (groupInt > 2) {
                    Mono.fx().alert().createWarning()
                            .setHeader("Invalid Group")
                            .setMessage("Please enter a valid group.")
                            .show();
                    return;
                }
            } catch (NumberFormatException a) {
                Mono.fx().alert().createWarning()
                        .setHeader("Invalid Group")
                        .setMessage("Please enter a valid group.")
                        .show();
                return;
            }
        } catch (NullPointerException a) {
            Mono.fx().alert().createWarning()
                    .setHeader("Invalid Group")
                    .setMessage("Please enter a valid group.")
                    .show();
            return;
        }
        //----------------------------------------------------------------------
        try {
            Integer CURRICULUM_id = curriculumIDs.get(cmb_curriculum.getSelectionModel().getSelectedIndex());
            STUDENT.setCURRICULUM_id(CURRICULUM_id);
        } catch (Exception e) {
            // null pointer or index out of bound
            Mono.fx().alert().createWarning()
                    .setHeader("No Curriculum")
                    .setMessage("Please select a curriculum for this student.")
                    .showAndWait();
            return;
        }
        //----------------------------------------------------------------------
        STUDENT.setYear_level((cmb_year_level.getSelectionModel().getSelectedIndex() + 1));
        STUDENT.setSection(section.toUpperCase());
        STUDENT.set_group(groupInt);
        STUDENT.setClass_type(type.replace("-", "_").toUpperCase());

        if (Database.connect().student().update(STUDENT)) {
            isSaved = true;
            Mono.fx().alert().createInfo()
                    .setHeader("Successfully Saved")
                    .setMessage("Proceed to evaluation.")
                    .show();
            Mono.fx().getParentStage(vbox_main).close();
        } else {
            System.out.println("NOT SAVED");
            Mono.fx().alert().createInfo()
                    .setHeader("Save Failed")
                    .setMessage("Database connection might have a problem.")
                    .show();
        }
    }

    private Boolean isSaved = false;

    public StudentMapping getStudent() {
        if (isSaved) {
            return STUDENT;
        } else {
            return null;
        }
    }

    //--------------------------------------------------------------------------
    private void crossEnrollee() {
        STUDENT.setCURRICULUM_id(null);
        STUDENT.setYear_level(null);
        STUDENT.setSection(null);
        STUDENT.set_group(null);
        STUDENT.setResidency(StudentType.CROSS_ENROLLEE);
        STUDENT.setClass_type(StudentType.CROSS_ENROLLEE);
        if (Database.connect().student().update(STUDENT)) {
            isSaved = true;
            Mono.fx().alert().createInfo()
                    .setHeader("Successfully Saved")
                    .setMessage("Proceed to evaluation.")
                    .show();
            super.finish();
        } else {
            System.out.println("NOT SAVED");
            Mono.fx().alert().createInfo()
                    .setHeader("Save Failed")
                    .setMessage("Database connection might have a problem.")
                    .show();
        }
    }
}

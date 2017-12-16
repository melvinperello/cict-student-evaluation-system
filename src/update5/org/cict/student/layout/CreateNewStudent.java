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
package update5.org.cict.student.layout;

import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.StudentMapping;
import app.lazy.models.StudentProfileMapping;
import artifacts.MonoString;
import com.izum.fx.textinputfilters.StringFilter;
import com.izum.fx.textinputfilters.TextInputFilters;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jhmvin.Mono;
import com.melvin.mono.fx.MonoLauncher;
import com.melvin.mono.fx.events.MonoClick;
import java.util.Date;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.controlsfx.control.Notifications;

/**
 *
 * @author Joemar
 */
public class CreateNewStudent extends MonoLauncher {

    @FXML
    private VBox application_root;

    @FXML
    private JFXTextField txt_id;

    @FXML
    private JFXTextField txt_last_name;

    @FXML
    private JFXTextField txt_first_name;

    @FXML
    private JFXTextField txt_middle_name;

    @FXML
    private RadioButton rbtn_cluster1;

    @FXML
    private RadioButton rbtn_cluster2;

    @FXML
    private JFXButton btn_add;

    @FXML
    private JFXButton btn_cancel;

    @Override
    public void onStartUp() {
        ToggleGroup group = new ToggleGroup();
        rbtn_cluster1.setToggleGroup(group);
        rbtn_cluster2.setToggleGroup(group);

        MonoClick.addClickEvent(btn_cancel, () -> {
            Mono.fx().getParentStage(btn_add).close();
        });

        this.textFieldFilters();
    }

    private void textFieldFilters() {
        StringFilter textId = TextInputFilters.string()
                //                .setFilterMode(StringFilter.LETTER_DIGIT)
                .setMaxCharacters(50)
                .setNoLeadingTrailingSpaces(true)
                .setFilterManager(filterManager -> {
                    if (!filterManager.isValid()) {
                        Mono.fx().alert().createWarning().setHeader("Warning")
                                .setMessage(filterManager.getMessage())
                                .show();
                    }
                });
        textId.clone().setTextSource(txt_id).applyFilter();

        StringFilter textName = TextInputFilters.string()
                .setFilterMode(StringFilter.LETTER_SPACE)
                .setMaxCharacters(100)
                .setNoLeadingTrailingSpaces(false)
                .setFilterManager(filterManager -> {
                    if (!filterManager.isValid()) {
                        Mono.fx().alert().createWarning().setHeader("Warning")
                                .setMessage(filterManager.getMessage())
                                .show();
                    }
                });
        textName.clone().setTextSource(txt_first_name).applyFilter();
        textName.clone().setTextSource(txt_last_name).applyFilter();
        textName.clone().setTextSource(txt_middle_name).applyFilter();
    }

    @Override
    public void onDelayedStart() {
        MonoClick.addClickEvent(btn_add, () -> {
            this.saveStudent();
        });
        super.onDelayedStart(); //To change body of generated methods, choose Tools | Templates.
    }

    private void saveStudent() {
        String id = MonoString.removeExtraSpace(txt_id.getText().toUpperCase());
        if (id == null || id.isEmpty()) {
            Mono.fx().alert().createWarning()
                    .setHeader("Student Number")
                    .setMessage("Student Number must have a value.").show();
            return;
        }
        StudentMapping exist = Mono.orm().newSearch(Database.connect().student())
                .eq(DB.student().id, id).active().first();
        if (exist != null) {
            Mono.fx().alert().createWarning()
                    .setHeader("Student Exist!")
                    .setMessage("Student Number already existing.").show();
            return;
        }
        String lastName = MonoString.removeExtraSpace(txt_last_name.getText().toUpperCase());
        if (lastName == null || lastName.isEmpty()) {
            Mono.fx().alert().createWarning()
                    .setHeader("Last Name")
                    .setMessage("Last Name must have a value.").show();
            return;
        }

        String firstName = MonoString.removeExtraSpace(txt_first_name.getText().toUpperCase());
        if (firstName == null || firstName.isEmpty()) {
            Mono.fx().alert().createWarning()
                    .setHeader("First Name")
                    .setMessage("First Name must have a value.").show();
            return;
        }

        String middleName = txt_middle_name.getText() == null ? "" : MonoString.removeExtraSpace(txt_middle_name.getText().toUpperCase());

        Integer cluster = null;
        try {
            cluster = rbtn_cluster1.isSelected() ? 3 : (rbtn_cluster2.isSelected() ? 4 : null);
            System.out.println("CLUSTER: " + cluster);
        } catch (NullPointerException e) {
            Mono.fx().alert().createWarning()
                    .setHeader("Cluster Assignment")
                    .setMessage("Select a Cluster Assignment for the student.").show();
            return;
        }

        Date DATE = Mono.orm().getServerTime().getDateWithFormat();
        StudentMapping newStudent = new StudentMapping();
        newStudent.setCreated_by("FACULTY");
        newStudent.setCreated_date(DATE);
        newStudent.setFirst_name(firstName);
        newStudent.setId(id);
        newStudent.setLast_name(lastName);
        newStudent.setMiddle_name(middleName);
        newStudent.setVerfied_by(CollegeFaculty.instance().getFACULTY_ID());
        newStudent.setVerification_date(DATE);
        newStudent.setVerified(1);
        Integer newStudentCICT_id = Database.connect().student().insert(newStudent);
        if (newStudentCICT_id.equals(-1)) {
            Notifications.create().darkStyle()
                    .title("Failed")
                    .text("Please check your connectivity with\nthe server.").showError();
            return;
        }
        StudentProfileMapping profile = new StudentProfileMapping();
        profile.setActive(1);
        profile.setCreated_date(DATE);
        profile.setEmail("");
        profile.setFloor_assignment(cluster);
        profile.setIce_address("");
        profile.setIce_contact("");
        profile.setIce_name("");
        profile.setMobile("");
        //----------------------------------------------------------------------
        profile.setProfile_picture("NONE");
        //----------------------------------------------------------------------
        profile.setSTUDENT_id(newStudentCICT_id);
        profile.setZipcode("");
        Integer profileID = Database.connect().student_profile().insert(profile);
        if (!profileID.equals(-1)) {
            Notifications.create().darkStyle()
                    .title("Successfully Created!")
                    .text("New Student is created.").showInformation();
        } else {
            Notifications.create().darkStyle()
                    .title("Profile Not Created")
                    .text("Please check your connectivity with\nthe server.").showError();
        }
        Mono.fx().getParentStage(btn_add).close();
    }

}

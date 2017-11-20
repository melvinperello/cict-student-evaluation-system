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
package update4.org.cict.linked_manager;

import app.lazy.models.AnnouncementsMapping;
import app.lazy.models.Database;
import artifacts.MonoString;
import com.izum.fx.textinputfilters.StringFilter;
import com.izum.fx.textinputfilters.TextInputFilters;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.melvin.mono.fx.MonoLauncher;
import com.melvin.mono.fx.events.MonoClick;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.controlsfx.control.Notifications;

/**
 *
 * @author Joemar
 */
public class AddAnnouncement extends MonoLauncher {

    @FXML
    private VBox application_root;

    @FXML
    private TextField txt_title;

    @FXML
    private TextArea txtarea_message;

    @FXML
    private Label lbl_count;

    @FXML
    private JFXButton btn_save;

    @FXML
    private JFXButton btn_cancel;
    
    @Override
    public void onStartUp() {
        this.textFilter();
        
        txtarea_message.textProperty().addListener((a)->{
            lbl_count.setText(String.valueOf(300-(txtarea_message.getText().length())));
        });
        
        MonoClick.addClickEvent(btn_cancel, ()->{
            int res = Mono.fx().alert().createConfirmation()
                    .setMessage("Are you sure you want to cancel?")
                    .confirmYesNo();
            if(res==1)
                Mono.fx().getParentStage(btn_save).close();
        });
    
    }
    
    private void textFilter() {
        StringFilter textField = TextInputFilters.string()
                .setMaxCharacters(50)
                .setNoLeadingTrailingSpaces(false);
        textField.clone().setTextSource(txt_title).applyFilter();
        StringFilter textField2 = TextInputFilters.string()
                .setMaxCharacters(300)
                .setNoLeadingTrailingSpaces(false);
        textField2.clone().setTextSource(txtarea_message).applyFilter();
    }

    @Override
    public void onDelayedStart() {
        MonoClick.addClickEvent(btn_save, ()->{
            this.save();
        });
        super.onDelayedStart(); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void save() {
        String title = MonoString.removeExtraSpace(txt_title.getText());
        if(title.isEmpty()) {
            Mono.fx().alert().createWarning()
                    .setMessage("Title cannot be empty.")
                    .show();
            return;
        }
        String message = MonoString.removeExtraSpace(txtarea_message.getText());
        if(message.isEmpty()) {
            Mono.fx().alert().createWarning()
                    .setMessage("Message cannot be empty.")
                    .show();
            return;
        }
        AnnouncementsMapping announce = new AnnouncementsMapping();
        announce.setActive(1);
        announce.setAnnounced_by(CollegeFaculty.instance().getFACULTY_ID());
        announce.setDate(Mono.orm().getServerTime().getDateWithFormat());
        announce.setMessage(message);
        announce.setTitle(title);
        Integer id = Database.connect().announcements().insert(announce);
        if(id.equals(-1)){
            Notifications.create().darkStyle()
                    .text("Something went wrong."
                            + "\nSorry for the inconvinince.")
                    .showError();
            btn_save.setDisable(true);
        } else {
            Notifications.create().darkStyle()
                    .text("Successfully Saved!")
                    .showInformation();
            Mono.fx().getParentStage(btn_save).close();
        }
    }
    
}

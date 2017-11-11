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
package sys.org.cict.layout.home;

import artifacts.MonoString;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.melvin.mono.fx.MonoLauncher;
import com.melvin.mono.fx.events.MonoClick;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.cict.MainApplication;

/**
 *
 * @author Joemar
 */
public class Settings extends MonoLauncher {

    @FXML
    private VBox application_root;

    @FXML
    private TextField txt_ip;

    @FXML
    private JFXButton btn_save;

    @FXML
    private JFXButton btn_cancel;

    @Override
    public void onStartUp() {
        txt_ip.setText(com.melvin.java.properties.PropertyFile.getPropertyFile("src/evaluation.properties").getProperty("hostIP"));
        
        MonoClick.addClickEvent(btn_cancel, ()->{
            Mono.fx().getParentStage(txt_ip).close();
        });
        
        MonoClick.addClickEvent(btn_save, ()->{
            String newIP = MonoString.removeAll(txt_ip.getText(), " ");
            if(newIP.isEmpty()) {
                Mono.fx().alert().createWarning()
                        .setMessage("Field must not be empty.")
                        .show();
                return;
            }
            int res = Mono.fx().alert().createConfirmation()
                    .setMessage("This transaction will automatically close the application, do you still want to continue?")
                    .confirmOkCancel();
            if(res==-1)
                return;
            
            com.melvin.java.properties.PropertyFile.writePropertyFile("src/evaluation.properties", "hostIP", newIP, "-----------------------");
            System.out.println("NEW IP SET " + com.melvin.java.properties.PropertyFile.getPropertyFile("src/evaluation.properties").getProperty("hostIP"));
            MainApplication.die(0);
        });
    }
    
}

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
package org.cict.reports;

import com.itextpdf.text.Document;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.melvin.mono.fx.MonoLauncher;
import com.melvin.mono.fx.events.MonoClick;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

/**
 *
 * @author Joemar
 */
public class PageSizeChooser extends MonoLauncher {
    
    @FXML
    private VBox application_root;

    @FXML
    private JFXButton btn_short;

    @FXML
    private JFXButton btn_long;

    @FXML
    private JFXButton btn_a4;
    
    @FXML
    private VBox vbox_table_col;
    
    @FXML
    private JFXButton btn_cancel;
    
    private Document choosenDetails;
    public Document getChoosenSize() {
        return choosenDetails;
    }

    @Override
    public void onStartUp() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onDelayedStart() {
        MonoClick.addClickEvent(btn_a4, ()->{
            this.choosenDetails = ReportsUtility.createA4Document();
            Mono.fx().getParentStage(application_root).close();
        });
        MonoClick.addClickEvent(btn_long, ()->{
            this.choosenDetails = ReportsUtility.createLongDocument();
            Mono.fx().getParentStage(application_root).close();
        });
        MonoClick.addClickEvent(btn_short, ()->{
            this.choosenDetails = ReportsUtility.createShortDocument();
            Mono.fx().getParentStage(application_root).close();
        });
        MonoClick.addClickEvent(btn_cancel, ()->{
            this.choosenDetails = null;
            Mono.fx().getParentStage(application_root).close();
        });
        super.onDelayedStart(); //To change body of generated methods, choose Tools | Templates.
    }
    
}

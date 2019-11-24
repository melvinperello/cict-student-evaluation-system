/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cict.linemanager.caller;

import com.melvin.mono.fx.MonoLauncher;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 *
 * @author Jhon Melvin
 */
public class RowQue extends MonoLauncher {

    @FXML
    private Label lbl_number;

    @FXML
    private Label lbl_student_number;

    @Override
    public void onStartUp() {

    }

    public Label getLbl_number() {
        return lbl_number;
    }

    public Label getLbl_student_number() {
        return lbl_student_number;
    }

}

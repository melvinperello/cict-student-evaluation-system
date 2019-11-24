/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melvin.mono.fx;

import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * @author Jhon Melvin
 */
public interface MonoController extends javafx.fxml.Initializable {

    @Override
    public default void initialize(URL location, ResourceBundle resources) {
        this.onStartUp();
    }
    
    public void onStartUp();

}

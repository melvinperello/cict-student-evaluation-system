/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jhmvin.messenger;

import com.melvin.mono.fx.MonoApplication;
import com.melvin.mono.fx.bootstrap.M;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Jhon Melvin
 */
public class MainApplication extends Application {

    public static void main(String... args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Configuration configurationController = M.load(Configuration.class);
            configurationController.createScene();
            Stage stage = configurationController.createStage();
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (Exception e) {
        }
    }
}

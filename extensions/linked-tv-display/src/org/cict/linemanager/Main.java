/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cict.linemanager;

import com.jhmvin.Mono;
import com.jhmvin.fx.display.WindowFX;
import com.melvin.mono.fx.bootstrap.M;
import javafx.application.Application;
import javafx.stage.Stage;
import org.cict.configuration.ip.SetupIP;
import org.cict.linemanager.caller.CallerUI;

/**
 *
 * @author Jhon Melvin
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        SetupIP ipController = M.load(SetupIP.class);

        Stage ipStage = ipController.createStage();
        ipStage.setTitle("Configuration");
        ipController.onDelayedStart();
        ipStage.showAndWait();

        if (!ipController.isProceed()) {
            Runtime.getRuntime().halt(0);
        }

        //----------------------------------------------------------------------
        CallerUI callerUI = new CallerUI();
        WindowFX main = Mono.fx().create()
                .setPackageName("org.cict.linemanager.caller")
                .setFxmlDocument("caller-ui-both")
                .makeFX()
                .setController(callerUI)
                .makeScene()
                .makeStage();
        callerUI.onDelayedStart();
        main.stageTitle("Linked TV Queue Display build 11202017");
        main.stageShow();
    }

    public static void main(String[] args) {
        launch(args);
    }

}

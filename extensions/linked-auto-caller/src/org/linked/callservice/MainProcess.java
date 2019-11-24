/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linked.callservice;

import com.jhmvin.Mono;
import com.jhmvin.fx.display.WindowFX;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author Jhon Melvin
 */
public class MainProcess extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        CallServiceMain controller = new CallServiceMain();
        WindowFX callStage = Mono.fx().create()
                .setPackageName("org.linked.callservice")
                .setFxmlDocument("CallServiceMain")
                .makeFX()
                .setController(controller)
                .makeScene()
                .makeStage();

        controller.onDelayedStart();
        callStage.stageResizeable(false);
        callStage.stageCenter();
        callStage.stageTitle("Linked Auto Caller build 11202017");
        callStage.stageShowAndWait();

    }

    public static void main(String[] args) {
        launch(args);
    }

}

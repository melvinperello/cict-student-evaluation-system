/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cict;

import com.jhmvin.Mono;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Jhon Melvin
 */
public class EvaluationMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Mono.version();
        //
        Scene mainScene = Mono.fx().create()
                .setPackageName("org.cict.authentication")
                .setFxmlDocument("Login")
                .makeFX()
                .makeScene()
                .pullOutScene();

        stage.setScene(mainScene);
        stage.setResizable(false);
        stage.setTitle("Login");
        stage.show();

    }

}

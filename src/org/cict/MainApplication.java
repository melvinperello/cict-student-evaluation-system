/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cict;

import app.lazy.models.Database;
import com.jhmvin.Mono;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.cict.authentication.LoginController;
import update3.org.cict.controller.sectionmain.deltesection.DeleteSectionTransaction;

/**
 *
 * @author Jhon Melvin
 */
public class MainApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Mono.version();
        // Destroy the default stage.
        stage = null;
        launchLogin();
    }

    public static void launchLogin() {
        LoginController controller = new LoginController();
        Mono.fx().create()
                .setPackageName("org.cict.authentication")
                .setFxmlDocument("Login")
                .makeFX()
                .setController(controller)
                .makeScene()
                .makeStageApplication()
                .stageResizeable(false)
                .stageTitle("Login")
                .stageShow();
        /**
         * Add a closing event.
         */
        controller.onStageClosing();

    }

    /**
     * Kills the application without proper termination.
     *
     * @param exitStatus 0 for normal exit.
     */
    public static void die(int exitStatus) {
        try {
            Platform.exit();
            Runtime.getRuntime().halt(exitStatus);
        } catch (SecurityException e) {
            // not allowerd to kill the process.
            System.err.println(e.getLocalizedMessage());
        }
    }

}

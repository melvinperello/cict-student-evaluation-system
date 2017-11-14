/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cict;

import app.lazy.models.Database;
import artifacts.StackTraceDialog;
import com.jhmvin.Mono;
import com.melvin.java.properties.PropertyFile;
import com.melvin.mono.fx.bootstrap.M;
import com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory;
import com.sun.javafx.application.HostServicesDelegate;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.cict.authentication.LoginController;
import sys.org.cict.layout.home.SystemLogin;
import update3.org.cict.controller.sectionmain.deltesection.DeleteSectionTransaction;
import update4.org.cict.linked_manager.CreateNewSessionTransaction;

/**
 *
 * @author Jhon Melvin
 */
public class MainApplication extends Application {

    public static HostServicesDelegate HOST_SERVICE;

    public static void main(String[] args) {
//        Thread.setDefaultUncaughtExceptionHandler((Thread t, Throwable e)->{
//            StackTraceDialog.show(e);
//        });
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.HOST_SERVICE = HostServicesFactory.getInstance(this);
        Mono.version();
        // Destroy the default stage.
        stage = null;
        launchLogin();
    }

    public static void launchLogin() {
//        LoginController controller = new LoginController();
//        Mono.fx().create()
//                .setPackageName("org.cict.authentication")
//                .setFxmlDocument("Login")
//                .makeFX()
//                .setController(controller)
//                .makeScene()
//                .makeStageApplication()
//                .stageResizeable(false)
//                .stageTitle("Login")
//                .stageShow();
//
//        /**
//         * Add a closing event.
//         */
//        controller.onStageClosing();

        SystemLogin loginFx = M.load(SystemLogin.class);
        Stage loginStage = loginFx.createStageApplication();
        loginStage.setResizable(false);
        loginStage.setTitle("CICT | Evaluation System");

        loginFx.onDelayedStart();
        loginStage.show();

    }

    /**
     * Kills the application without proper termination.
     *
     * @param exitStatus 0 for normal exit.
     */
    public static void die(int exitStatus) {
        try {
            Mono.orm().shutdown();
            Platform.exit();
            Runtime.getRuntime().halt(exitStatus);
        } catch (Exception e) {
            // not allowerd to kill the process.
            Runtime.getRuntime().halt(exitStatus);
        }
    }

}

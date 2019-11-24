/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cict;

import artifacts.ErrorLogger;
import com.jhmvin.Mono;
import com.melvin.mono.fx.bootstrap.M;
import com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory;
import com.sun.javafx.application.HostServicesDelegate;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import sys.org.cict.layout.home.SystemLogin;

/**
 *
 * @author Jhon Melvin
 */
public class MainApplication extends Application {

    public static HostServicesDelegate HOST_SERVICE;

    public static void main(String[] args) {
        //----------------------------------------------------------------------
        // saves error in "error_logs" directory
        MainApplication.integrateExceptionCatcher();
        com.jhmvin.fx.async.Transaction.setErrorApprehension(true);
        com.jhmvin.fx.async.SimpleTask.setErrorApprehension(true);
        //----------------------------------------------------------------------
        launch(args);
    }

    /**
     * Catches uncaught errors.
     */
    public static void integrateExceptionCatcher() {
        Thread.setDefaultUncaughtExceptionHandler((Thread t, Throwable e) -> {
            try {
                StackTraceElement se = e.getStackTrace()[0];
                String spreadsheet_error = se.toString();
                // Ignorable error
                if (spreadsheet_error.contains("impl.org.controlsfx.spreadsheet.GridCellEditor.endEdit(GridCellEditor.java:132)")) {
                    // do nothing this is a known error
                    System.out.println(e.getCause());
                } else {
                    //StackTraceDialog.show(e);
                    ErrorLogger.record(e, t);
                }
            } catch (Exception haha) {
                //StackTraceDialog.show(haha);
                ErrorLogger.record(haha, t);
            }
        });
    }

    @Override
    public void start(Stage stage) throws Exception {
        //TODO: JDK-11-FIX
        System.out.println("//TODO: JDK-11-FIX -> FUNCTION DISABLED = HostServicesFactory");
        //this.HOST_SERVICE = HostServicesFactory.getInstance(this);
        
    	
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
        loginStage.setTitle("CICT | Student Evaluation System");

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

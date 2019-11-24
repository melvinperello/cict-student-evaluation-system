/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linked.callservice;

import app.lazy.models.Database;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.CronTimer;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 *
 * @author Jhon Melvin
 */
public class CallServiceMain extends SceneFX implements ControllerFX {

    @FXML
    private VBox application_root;

    @FXML
    private VBox vbox_main;

    @FXML
    private VBox vbox_loading;

    @FXML
    private Label lbl_loading_message;

    public CallServiceMain() {
        this.caller_service = new CronTimer("caller_service");
    }

    private CronTimer caller_service;

    @Override
    public void onInitialization() {
        super.bindScene(application_root);
        //
        vbox_main.setVisible(false);
        vbox_loading.setVisible(true);

    }

    @Override
    public void onEventHandling() {

    }

    public void onDelayedStart() {
        vbox_main.setVisible(false);
        vbox_loading.setVisible(true);

        Thread bootHib = new Thread(() -> {
            
            Database.connect();
            if (Mono.orm().getSessionFactory() == null) {
                // failed to connects
                Platform.runLater(() -> {
                    Mono.fx().alert().createError()
                            .setTitle("Error")
                            .setHeader("Cannot Start Service")
                            .setMessage("Cannot start the service right now, please try again later.")
                            .showAndWait();
                    Runtime.getRuntime().halt(0);
                });
            }
            Platform.runLater(() -> {
                vbox_main.setVisible(true);
                vbox_loading.setVisible(false);
                this.boot();
            });
        });
        bootHib.setName("Thread:bootHib");
        bootHib.setDaemon(true);
        bootHib.start();

        super.getStage().setOnCloseRequest(value -> {
            shutdown();
            value.consume();
        });
    }

    private void boot() {
        this.caller_service = new CronTimer("caller_service");
        caller_service.setInterval(1111);
        caller_service.setTask(() -> {
            try {
                //------------------------------------------------------------------
                // CALLER SERVICE
                Caller.autoCall(3);
                Caller.autoCall(4);
                //------------------------------------------------------------------
                // EXPIRE WATCHER
                Caller.expireWatcher(3);
                Caller.expireWatcher(4);
                System.out.println("RUNNING . . .");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        caller_service.start();

    }

    private void shutdown() {
        int c = Mono.fx().alert()
                .createConfirmation()
                .setTitle("Exit")
                .setHeader("Stop Services?")
                .setMessage("Are you sure you want to stop Linked System Services ?")
                .confirmYesNo();

        if (c != 1) {
            return;
        }

        try {
            this.caller_service.stop();
            Mono.orm().shutdown();
            Platform.exit();
            Runtime.getRuntime().halt(0);
        } catch (Exception e) {
            Runtime.getRuntime().halt(0);
        }

    }

}

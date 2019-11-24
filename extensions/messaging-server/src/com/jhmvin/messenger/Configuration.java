/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jhmvin.messenger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jhmvin.Mono;
import com.melvin.java.properties.PropertyFile;
import com.melvin.mono.fx.MonoLauncher;
import com.melvin.mono.fx.bootstrap.M;
import com.melvin.mono.fx.events.MonoClick;
import java.util.Properties;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.stage.Stage;

/**
 *
 * @author Jhon Melvin
 */
public class Configuration extends MonoLauncher {

    @FXML
    private JFXTextField txt_modem_id;

    @FXML
    private JFXTextField txt_com_port;

    @FXML
    private JFXTextField txt_bitrate;

    @FXML
    private JFXTextField txt_message_center;

    @FXML
    private JFXButton btn_ok;

    @FXML
    private JFXButton btn_cancel;

    //--------------------------------------------------------------------------
    public final static String CONFIG_PROPERTY = "configuration/config.properties";

    @Override
    public void onStartUp() {
        //----------------------------------------------------------------------
        // Check if exists
        if (PropertyFile.exists(CONFIG_PROPERTY)) {
            Properties config = PropertyFile.getPropertyFile(CONFIG_PROPERTY);
            txt_modem_id.setText(config.getOrDefault("id", "").toString());
            txt_com_port.setText(config.getOrDefault("port", "").toString());
            txt_bitrate.setText(config.getOrDefault("bitrate", "").toString());
            txt_message_center.setText(config.getOrDefault("message_center", "").toString());
        }
        // COM PORT ALL UPPER CASE.
        txt_com_port.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            txt_com_port.setText(newValue.toUpperCase());
        });
        //----------------------------------------------------------------------
        MonoClick.addClickEvent(btn_ok, () -> {
            onSaveConfiguration();
        });

        MonoClick.addClickEvent(btn_cancel, () -> {
            Runtime.getRuntime().halt(0);
        });
    }

    private void writeToProperty(String key, String value) {
        PropertyFile.writePropertyFile(CONFIG_PROPERTY, key, value);
    }

    private void onSaveConfiguration() {
        try {
            writeToProperty("id", txt_modem_id.getText().trim());
            writeToProperty("port", txt_com_port.getText().trim());
            writeToProperty("bitrate", txt_bitrate.getText().trim());
            writeToProperty("message_center", txt_message_center.getText().trim());
            //------------------------------------------------------------------
            this.getCurrentStage().close();
            // open new stage
            MessagingServer serverController = M.load(MessagingServer.class);
            serverController.createScene();
            Stage stage = serverController.createStage();
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.setTitle("SMS Server");
            serverController.onDelayedStart();
            stage.show();

        } catch (Exception e) {
            Mono.fx().alert().createError().setTitle("Error")
                    .setHeader("Configuration Error")
                    .setMessage("Cannot Store Configuration Details")
                    .show();
        }
    }
}

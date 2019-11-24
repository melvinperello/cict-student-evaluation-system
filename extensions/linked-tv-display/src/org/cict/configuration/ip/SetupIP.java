/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cict.configuration.ip;

import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.melvin.java.properties.PropertyFile;
import com.melvin.mono.fx.MonoLauncher;
import com.melvin.mono.fx.events.MonoClick;
import java.util.Properties;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.cict.configuration.ConfigurationManager;

/**
 *
 * @author Jhon Melvin
 */
public class SetupIP extends MonoLauncher {

    @FXML
    private TextField txt_ip;

    @FXML
    private JFXButton btn_start;

    @Override
    public void onStartUp() {

    }

    private boolean proceed = false;

    public boolean isProceed() {
        return proceed;
    }

    @Override
    public void onDelayedStart() {
        // Get current IP
        ConfigurationManager.checkConfiguration();
        Properties prop = PropertyFile.getPropertyFile(ConfigurationManager.CONNECTION_PROP.getAbsolutePath());
        txt_ip.setText(prop.getOrDefault(ConfigurationManager.PROP_HOST_IP, "127.0.0.1").toString());
        // Add Click Event
        MonoClick.addClickEvent(btn_start, () -> {
            this.onChangedIp();
        });
    }

    private void onChangedIp() {
        String ip = txt_ip.getText().trim();

        if (ip.isEmpty()) {
            Mono.fx().alert().createWarning().setTitle("Configuration")
                    .setHeader("IP Address is Empty")
                    .setMessage("Please type the IP Address of the server")
                    .show();
            return;
        }

        //----------------------------------------------------------------------
        boolean res = PropertyFile.writePropertyFile(ConfigurationManager.CONNECTION_PROP.getAbsolutePath(),
                ConfigurationManager.PROP_HOST_IP,
                ip);

        if (res) {
            this.proceed = true; // allow next stage to open
            this.getCurrentStage().close();
        } else {
            Mono.fx().alert().createWarning().setTitle("Configuration")
                    .setHeader("Configuration Problem")
                    .setMessage("The application was unable to load the configuration.")
                    .show();
        }
    }

}

package com.jhmvin.fx.display;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

public interface ControllerFX extends Initializable {

    @Override
    public default void initialize(URL location, ResourceBundle resources) {
        this.onInitialization();
        this.onEventHandling();
    }

    /**
     * Initialization processes here
     */
    public void onInitialization();

    /**
     * Add events to controls
     */
    public void onEventHandling();

}

package com.jhmvin.fx.display;

import javafx.scene.layout.Pane;

/**
 *
 * @author Jhon Melvin
 */
public class LayoutDataFX {

    private final Pane applicationRoot;
    private final ControllerFX controller;

    /**
     * Stores the current layout data of a FXML Root.
     *
     * @param applicationRoot the application_root
     * @param controller the controller that implements ControllerFX
     */
    public LayoutDataFX(Pane applicationRoot, ControllerFX controller) {
        this.applicationRoot = applicationRoot;
        this.controller = controller;
    }

    /**
     * Get the application root that extends the Pane Class.
     *
     * @param <T> HBOX VBOX ANCHOR PANE or anything
     * @return the stored application root.
     */
    public <T extends Pane> T getApplicationRoot() {
        return (T) applicationRoot;
    }

    /**
     * The controller of the application root.
     *
     * @param <T>
     * @return
     */
    public <T extends ControllerFX> T getController() {
        return (T) controller;
    }

}

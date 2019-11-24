/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jhmvin.fx;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import com.jhmvin.fx.async.FXThread;
import com.jhmvin.fx.display.WindowFX;
import com.jhmvin.fx.keys.KeyAssignment;
import com.jhmvin.fx.notify.AlertMessage;
import com.jhmvin.fx.notify.snackbars.Snackbar;

/**
 *
 * @author Jhon Melvin
 */
public class FxLibrary {

    private static FxLibrary FX_INSTANCE;

    private FxLibrary() {
        //
    }

    public static FxLibrary instance() {
        if (FX_INSTANCE == null) {
            FX_INSTANCE = new FxLibrary();
        }
        return FX_INSTANCE;
    }

    public WindowFX create() {
        return WindowFX.create();
    }

    public Snackbar snackbar() {
        return new Snackbar();
    }

    public AlertMessage alert() {
        return new AlertMessage();
    }

    public KeyAssignment key(KeyCode key) {
        return new KeyAssignment(key);
    }

    //--------------------------------------------------------------------------
    /**
     * Gets the parent stage of a child node.
     *
     * @param node The child node.
     * @return the parent stage of this node.
     */
    public Stage getParentStage(Node node) {
        Stage stage = (Stage) node.getScene().getWindow();
        return stage;
    }

    public FXThread thread() {
        return FXThread.instance();
    }

}

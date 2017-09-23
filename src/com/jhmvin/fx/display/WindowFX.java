/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jhmvin.fx.display;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Previously MonoWindow
 *
 * @author Jhon Melvin
 */
public class WindowFX {

    private WindowFX() {
        //
        this.packageName = "";
        this.fxmlDocument = "";
    }

    public static WindowFX create() {
        return new WindowFX();
    }

    private String packageName;
    private String fxmlDocument;
    private LoaderFX fx;
    private Scene myScene;
    private Stage myStage;

    private boolean FX_LOADED = false;
    private boolean SCENE_LOADED = false;

    public WindowFX setPackageName(String packageName) {
        packageName = packageName.replaceAll("\\.", "/");
        packageName = "/" + packageName;
        this.packageName = packageName;
        return this;
    }

    public WindowFX setFxmlDocument(String fxmlDocument) {
        this.fxmlDocument = "/" + fxmlDocument + ".fxml";
        return this;
    }

    /**
     * Called before make scene
     *
     * @return
     */
    public WindowFX makeFX() {
        this.fx = new LoaderFX(getClass().getResource(this.packageName + this.fxmlDocument));
        this.FX_LOADED = true;
        return this;
    }

    public WindowFX setController(ControllerFX ctrl) {
        this.fx.setController(ctrl);
        return this;
    }

    /**
     * Loads a layout from a FXML File.
     *
     * @return Container Node.
     * @since 08.24.2017
     * @author jhmvin
     */
    public <T extends Pane> T pullOutLayout() {
        if (!this.FX_LOADED) {
            System.err.println(" . <wrong_order>");
            System.err.println(" . ");
            System.err.println(" . makeFX() must be called first before pullOutLayout()");
            System.err.println(" . ");
            System.err.println(" . </wrong_order>");
            return null;
        }
        Pane layout = this.fx.load();
        return (T) layout;
    }

    public WindowFX makeScene() {
        if (!this.FX_LOADED) {
            System.err.println(" . <wrong_order>");
            System.err.println(" . ");
            System.err.println(" . makeFX() must be called first before makeScene()");
            System.err.println(" . ");
            System.err.println(" . </wrong_order>");
            return null;
        }

        Pane layout = this.fx.load();
        //this.fx.getController();
        this.myScene = new Scene(layout);
        this.SCENE_LOADED = true;
        return this;
    }

    //--------------------------------------------------------------------------
    /**
     * <strong> Call Only After makeScene()</strong>
     *
     * @return
     */
    public Scene pullOutScene() {
        return this.myScene;
    }

    /**
     * <strong> Call Only After makeScene()</strong>
     * <br>
     * typecast to the desired controller
     *
     * @return
     */
    public ControllerFX pullOutController() {
        return this.fx.getController();
    }

    //--------------------------------------------------------------------------
    private boolean stageMaker() {
        if (!this.SCENE_LOADED) {
            System.err.println(" . <wrong_order>");
            System.err.println(" . ");
            System.err.println(" . makeScene() must be called first before making a stage.");
            System.err.println(" . ");
            System.err.println(" . </wrong_order>");
            return false;
        }
        this.myStage = new Stage();
        this.myStage.setScene(myScene);
        return true;
    }

    /**
     * Makes a stage that does not interfere with other windows.
     *
     * @return
     */
    public WindowFX makeStage() {
        if (!this.stageMaker()) {
            return null;
        }
        // non modal
        this.myStage.initModality(Modality.NONE);
        return this;
    }

    /**
     * If you want the stage to have an owner
     *
     * @param parentStage owner
     * @return
     */
    public WindowFX makeStageWithOwner(Stage parentStage) {
        if (!this.stageMaker()) {
            return null;
        }
        this.myStage.initOwner(parentStage);
        this.myStage.initModality(Modality.WINDOW_MODAL);
        return this;
    }

    /**
     * Makes a stage that blocks events from entering another window.
     *
     * @return
     */
    public WindowFX makeStageApplication() {
        if (!this.stageMaker()) {
            return null;
        }
        this.myStage.initModality(Modality.APPLICATION_MODAL);
        return this;
    }

    public WindowFX stageUndecorated(boolean undecorate) {
        if (undecorate) {
            this.myStage.initStyle(StageStyle.UNDECORATED);
        } else {
            this.myStage.initStyle(StageStyle.DECORATED);
        }
        return this;
    }

    public WindowFX stageResizeable(boolean resize) {
        this.myStage.setResizable(resize);
        return this;
    }

    public WindowFX stageMaximized(boolean max) {
        this.myStage.setMaximized(max);
        return this;
    }

    public WindowFX stageFullScreen(boolean full) {
        this.myStage.setFullScreen(full);
        return this;
    }

    public WindowFX stageTitle(String title) {
        this.myStage.setTitle(title);
        return this;
    }

    public WindowFX stageCenter() {
        this.myStage.centerOnScreen();
        return this;
    }

    /**
     * Added 07/30/2017. Stage will not be allowed to resized below the
     * restricted values to disable layout distortion.
     *
     * @param width
     * @param height
     * @return
     */
    public WindowFX stageMinDimension(double width, double height) {
        this.myStage.setMinWidth(width);
        this.myStage.setMinHeight(height);
        return this;
    }

    //------------------------------------------------------------------------
    /**
     * If you want to pull out the stage and make additional changes
     *
     * @return The created stage
     */
    public Stage pullOutStage() {
        return this.myStage;
    }

    //-------------------------------------------------------------------------
    public void stageShow() {
        this.myStage.show();
    }

    public void stageShowAndWait() {
        this.myStage.showAndWait();
    }

}

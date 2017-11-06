/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melvin.mono.fx;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.melvin.mono.classmeta.Informable;

/**
 *
 * @author Jhon Melvin
 */
public abstract class MonoLauncher implements Informable, MonoController {

    private String fxmlPackagePath;
    private String fxmlDocumentPath;
    private Pane applicationRoot;

    //--------------------------------------------------------------------------
    /**
     * Launcher method. starting point of this class method call.
     *
     * @param launchData
     * @param controller
     * @return
     */
    public Pane launch(String[] launchData, MonoController controller) {
        this.fxmlPackagePath = this.formatPackage(launchData[0]);
        this.fxmlDocumentPath = this.formatDocument(launchData[1]);

        MonoLoaderFX fx = new MonoLoaderFX(getClass().getResource(this.fxmlPackagePath + this.fxmlDocumentPath
        ));
        if (controller != null) {
            fx.setController(controller);
        }
        this.applicationRoot = fx.load();
        return this.applicationRoot;
    }

    /**
     * Formats the package name in a file format.
     *
     * @param packageName
     * @return
     */
    private static String formatPackage(String packageName) {
        packageName = packageName.replaceAll("\\.", "/");
        packageName = "/" + packageName;
        return packageName;
    }

    /**
     * Formats the document name.
     *
     * @param fxmlDocument
     * @return
     */
    private String formatDocument(String fxmlDocument) {
        fxmlDocument = "/" + fxmlDocument + ".fxml";
        return fxmlDocument;
    }

    //--------------------------------------------------------------------------
    /**
     * Start method for heavy Threads. this is to avoid consuming to much
     * processes during the framework boot.
     */
    public void onDelayedStart() {
        /**
         * Your Code Logic Here.
         */
    }

    /**
     * Open this FXML document.
     *
     * @param <T>
     * @return
     */
    public <T extends Pane> T open() {
        return (T) launch(getLaunchData(), this);
    }

    /**
     * Returns this class application root.
     *
     * @return
     */
    public <T extends Pane> T getApplicationRoot() {
        return (T) this.applicationRoot;
    }

    /**
     * Gets the current scene of this root.
     *
     * @return
     */
    public Scene getScene() {
        Scene currentScene = getApplicationRoot().getScene();
        return currentScene;
    }

    /**
     * Creates a new Scene if there is no scene create for this application
     * root.
     *
     * @return
     */
    public Scene createScene() {
        if (getScene() == null) {
            return new Scene(this.getApplicationRoot());
        }
        return getScene();
    }

    /**
     * Changes the scene default color.
     *
     * @param hexColor
     */
    public void setSceneColor(String hexColor) {
        this.getScene().setFill(Color.web(hexColor));
    }

    /**
     * Changes this scene's application root.
     *
     * @param newRoot
     */
    public void changeRoot(Pane newRoot) {
        this.getApplicationRoot().setPrefWidth(this.getCurrentStage().getWidth());
        this.getApplicationRoot().setPrefHeight(this.getCurrentStage().getHeight());
        this.getScene().setRoot(newRoot);
    }

    /**
     * Change this scene's cursor.
     *
     * @param value
     */
    public void setCursor(Cursor value) {
        this.getScene().setCursor(value);
    }

    //--------------------------------------------------------------------------
    /**
     * Get the current Stage.
     *
     * @return
     */
    public Stage getCurrentStage() {
        Stage currentStage = (Stage) getScene().getWindow();
        return currentStage;
    }

    /**
     * Creates a New stage for this Scene.
     *
     * @return
     */
    private Stage createStage(Modality modality, Stage owner) {
        Stage stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(modality);
        if (getScene() == null) {
            createScene();
        }
        stage.setScene(getScene());
        return stage;
    }

    /**
     * Create Stage Application.
     *
     * @return
     */
    public Stage createStageApplication() {
        return createStage(Modality.APPLICATION_MODAL, null);
    }

    /**
     * Create Child Stage.
     *
     * @param owner
     * @return
     */
    public Stage createChildStage(Stage owner) {
        return createStage(Modality.WINDOW_MODAL, owner);
    }

    /**
     * Create Window Stage.
     *
     * @return
     */
    public Stage createStage() {
        return createStage(Modality.NONE, null);
    }

    /**
     * Closes the current stage.
     */
    public void close() {
        this.getCurrentStage().close();
    }

}

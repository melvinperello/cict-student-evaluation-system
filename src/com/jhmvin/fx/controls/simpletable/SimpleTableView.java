package com.jhmvin.fx.controls.simpletable;

import com.jhmvin.fx.controls.Container;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class SimpleTableView extends ScrollPane {
    
    private VBox mainContainer;
    private HBox tableHeader;
    private SimpleTable table;
    
    private boolean fixedWith = false;
    
    public SimpleTableView() {
        mainContainer = new VBox();
        tableHeader = new HBox();
        table = new SimpleTable();
        
        this.setContent(mainContainer);
        
    }
    
    private void reInstate() {
        mainContainer.getChildren().clear();
        
        mainContainer.getChildren().add(tableHeader);
        
        table.prefWidthProperty().bind(mainContainer.prefWidthProperty());
        table.minWidthProperty().bind(mainContainer.minWidthProperty());
        table.maxWidthProperty().bind(mainContainer.maxWidthProperty());
        mainContainer.getChildren().add(table);
    }
    
    public void setTable(SimpleTable table) {
        this.table = table;
        this.reInstate();
    }
    
    public void setFixedWidth(boolean isFixedWith) {
        fixedWith = isFixedWith;
    }

    /**
     * Must be last called.
     *
     * @param parent
     */
    public void setParentOnScene(VBox parent) {
        parent.getChildren().clear();
        //Container.bindDimensionsToParent(this, parent);
        VBox.setVgrow(this, Priority.ALWAYS);
        parent.getChildren().add(this);
        
        mainContainer.prefWidthProperty().bind(this.prefWidthProperty());
        mainContainer.minWidthProperty().bind(this.minWidthProperty());
        mainContainer.maxWidthProperty().bind(this.maxWidthProperty());
        this.maxWidth(Double.MAX_VALUE);
        this.maxHeight(Double.MAX_VALUE);
        
        if (this.fixedWith) {
            this.prefWidthProperty().bind(parent.widthProperty());
            this.maxWidthProperty().bind(parent.widthProperty());
            this.setFitToWidth(true);
            this.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        }
        
    }

    /**
     * Returns the main container of this Simple Table View.
     *
     * @return
     */
    public VBox getContentPane() {
        return this.mainContainer;
    }
}

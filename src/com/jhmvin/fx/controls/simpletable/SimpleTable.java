package com.jhmvin.fx.controls.simpletable;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public class SimpleTable extends VBox {


    private final String tableCss = "simple-table";

    public SimpleTable() {
        this.getStyleClass().add(tableCss);
    }

    public void addRow(SimpleTableRow row) {
        row.prefWidthProperty().bind(this.prefWidthProperty());
        row.minWidthProperty().bind(this.minWidthProperty());
        row.maxWidthProperty().bind(this.maxWidthProperty());


        this.getChildren().add(row);
    }

    public void hideAllExtensions() {
        this.getChildren().forEach(row -> {
            ((SimpleTableRow) row).hideExtension();
        });
    }

    public void showAllExtensions() {
        this.getChildren().forEach(row -> {
            ((SimpleTableRow) row).showExtension();
        });
    }

    public ObservableList<Node> getRows() {
        return this.getChildren();
    }
}

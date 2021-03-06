package com.jhmvin.fx.controls.simpletable;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

/**
 * <p>
 * Simple Table Cell</p>
 * <p>
 * Here are some important information about this class</p>
 * <ul>
 * <li>CSS Style Class: "simple-table-cell"</li>
 * <li>Styling may be done using the css styling global class</li>
 * </ul>
 */
public class SimpleTableCell extends HBox {

    /**
     * CSS Styling Class names.
     */
    private final String cellCssClass = "simple-table-cell";
    private final String contentCssClass = "simple-table-cell-content";

    /**
     * Constructor.
     */
    public SimpleTableCell() {
        // css styling class
        this.getStyleClass().add(this.cellCssClass);
    }

    /**
     * Class Variables
     */
    private Node control;

    /**
     * Sets what type of control should be added.
     *
     * @param control
     */
    public void setContent(Node control) {

        this.control = control;
        this.control.getStyleClass().add(this.contentCssClass);
        this.control.getStyleClass().add(this.cellCssClass);
        //------------------------------------------------------------------
        // control position default
        this.setAlignment(Pos.CENTER_LEFT);
        // same with as the cell
        HBox.setHgrow(this.control, Priority.ALWAYS);
        this.control.maxWidth(Double.MAX_VALUE);

        //this.control.setMaxWidth();
        // add the control
        this.getChildren().add(this.control);
    }

    public void setContentAsPane(Pane pane) {
        this.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(pane, Priority.ALWAYS);
        pane.prefWidthProperty().bind(this.prefWidthProperty());
        pane.maxHeightProperty().bind(this.maxHeightProperty());
        pane.minHeightProperty().bind(this.minHeightProperty());

        // revert back to node
        this.control = (Node) pane;
        this.control.getStyleClass().add(this.contentCssClass);
        this.control.getStyleClass().add(this.cellCssClass);
        //
        this.getChildren().add(this.control);
    }

    /**
     * Sets the control's position within the container.
     *
     * @param position
     */
    public void setContentPosition(Pos position) {
        this.setAlignment(position);
    }

    /**
     * If there are empty spaces in the row this priority will determine on how
     * the cell will react in filling up the space.
     *
     * @param priority priority type.
     */
    public void setResizePriority(Priority priority) {
        HBox.setHgrow(this, priority);
        this.setMaxWidth(Double.MAX_VALUE);
    }

    //-----------------------------------------------------------------
    public void setCellPadding(double top, double right, double bottom, double left) {
        this.setPadding(new Insets(top, right, bottom, left));
    }

    public void setCellPadding(double right, double left) {
        this.setPadding(new Insets(0.0, right, 0.0, left));
    }

    //-----------------------------------------------------------------
    /**
     * @return
     */
    public <T> T getContent() {
        if (this.getChildren().isEmpty()) {
            return null;
        } else if (this.getChildren().size() == 1) {
            return (T) this.getChildren().get(0);
        } else {
            return null;
        }

    }

    /**
     * Assign a new CSS Styling Class in this cell.
     *
     * @param className String class name.
     */
    public void addCssClass(String className) {
        this.getStyleClass().add(className);
    }

    /**
     * Assign a new CSS Styling Class in this cell's content.
     *
     * @param className String class name.
     */
    public void addContentCssClass(String className) {
        this.control.getStyleClass().add(className);
    }
}

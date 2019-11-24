package com.jhmvin.fx.controls;

import javafx.scene.control.Control;
import javafx.scene.layout.Pane;

public class Container {
    public static void bindDimensionsToParent(Control childControl, Pane parent) {
        /**
         * Preferred.
         */
        childControl.prefHeightProperty().bind(parent.heightProperty());
        childControl.prefWidthProperty().bind(parent.widthProperty());

        /**
         * Min.
         */
        childControl.minHeightProperty().bind(parent.minHeightProperty());
        childControl.minWidthProperty().bind(parent.minWidthProperty());

        /**
         * Max.
         */
        childControl.maxHeightProperty().bind(parent.maxHeightProperty());
        childControl.maxWidthProperty().bind(parent.maxWidthProperty());
    }
}

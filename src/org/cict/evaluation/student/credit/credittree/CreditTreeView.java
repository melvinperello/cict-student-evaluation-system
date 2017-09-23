/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cict.evaluation.student.credit.credittree;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * Do Not modify.
 *
 * @author Jhon Melvin
 */
public class CreditTreeView extends ScrollPane {

    public CreditTreeView() {
        super();
        this.initialize();
    }

    /**
     * Unused
     *
     * @param content
     */
    public CreditTreeView(Node content) {
        super(content);
    }

    private CreditTree creditTree;
    private Pane mainBoard;
    private Pane lineDrawBoard;
    Line line;

    private void initialize() {
        //

        // initialize line draw board
        this.mainBoard = new Pane();

        // sample
        this.lineDrawBoard = new Pane();
        this.lineDrawBoard.prefWidthProperty().bind(this.mainBoard.prefWidthProperty());
        this.lineDrawBoard.prefHeightProperty().bind(this.mainBoard.prefHeightProperty());

        // initialize the credit tree
        this.creditTree = new CreditTree(lineDrawBoard);
        this.creditTree.setScrollpane(this);
        this.creditTree.setSpacing(100);
        this.creditTree.setPrefHeight(500);
        this.creditTree.prefHeightProperty().bind(this.prefHeightProperty());

        // add tree and draw board to main
        this.mainBoard.getChildren().addAll(this.lineDrawBoard, this.creditTree);

        // set the content
        this.setContent(this.mainBoard);
        //this.showBounds();
    }

    public void showBounds() {
        //this.mainBoard.setBackground(new Background(new BackgroundFill(Paint.valueOf("#0000FF"), CornerRadii.EMPTY, new Insets(0, 0, 0, 0))));
        this.creditTree.setBackground(new Background(new BackgroundFill(Color.web("#00FF00"), CornerRadii.EMPTY, Insets.EMPTY)));
        this.lineDrawBoard.setBackground(new Background(new BackgroundFill(Color.web("#FF0000"), CornerRadii.EMPTY, Insets.EMPTY)));

    }

    public CreditTree getTree() {
        return this.creditTree;
    }

    public CreditTreeColumn createNewColumn() {
        return new CreditTreeColumn();
    }

    public CreditTreeRow createNewRow() {
        CreditTreeRow row = new CreditTreeRow();
        row.setLinePanel(mainBoard);
        return row;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cict.evaluation.student.credit.credittree;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 *
 * @author Jhon Melvin
 */
public class CreditTreeColumn extends VBox {

    private Label Header;
    private Label SubHeader;
    private ObservableList<CreditTreeRow> ChildRows;

    public CreditTreeColumn() {
        this.Header = new Label();
        this.SubHeader = new Label();
        this.Header.setFont(new Font(35.0));
        this.SubHeader.setFont(new Font(20.0));

        this.ChildRows = FXCollections.observableArrayList();
        this.ChildRows.addListener((ListChangeListener.Change<? extends CreditTreeRow> c) -> {
            if (c.next()) {
                if (c.wasAdded()) {
                    for (CreditTreeRow creditTreeRow : c.getAddedSubList()) {
                        if (!this.getChildren().contains(creditTreeRow)) {
                            this.getChildren().add(creditTreeRow);
                        }
                    }
                } else if (c.wasRemoved()) {
                    this.getChildren().removeAll(c.getRemoved());
                }
            }
        });
        //this.setBackground(new Background(new BackgroundFill(Color.web("#000000"), CornerRadii.EMPTY, Insets.EMPTY)));
        this.setAlignment(Pos.CENTER);
        this.setSpacing(3);
        this.getChildren().addAll(this.Header, this.SubHeader);
        this.setPrefWidth(300.0);
    }

    public void setHeader(String header) {
        this.Header.setText(header);
    }

    public void setSubHeader(String sub) {
        this.SubHeader.setText(sub);
    }

    public ObservableList<CreditTreeRow> getChildRows() {
        return this.ChildRows;
    }

    public boolean hasFocus() {
        boolean hasFocus = false;
        for (Node node : this.getChildren()) {
            if (node instanceof CreditTreeRow) {
                if (((CreditTreeRow) node).hasFocus()) {
                    hasFocus = true;
                    break;
                }
            }
        }
        return hasFocus;
    }

    public void setFocusedRow(int index) {
        Node node = this.getChildRows().get(index);
        node.requestFocus();
        CreditTreeRow row = (CreditTreeRow) node;
        row.getChildren().forEach(n -> {
            if (n instanceof TextField) {
                n.requestFocus();
            }
        });
    }

    public int getFocusedRowIndex() {
        if (this.hasFocus()) {
            for (int x = 0; x < this.getChildRows().size(); x++) {
                Node node = this.getChildRows().get(x);
                if (node instanceof CreditTreeRow) {
                    if (((CreditTreeRow) node).hasFocus()) {
                        return x;
                    }
                }
            }
        }
        return -1;
    }

    public CreditTreeRow getFocusedRow() {
        int index = this.getFocusedRowIndex();
        if (index != -1) {
            return (CreditTreeRow) this.getChildRows().get(this.getFocusedRowIndex());
        }
        return null;
    }

}

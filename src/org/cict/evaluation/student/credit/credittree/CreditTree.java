/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cict.evaluation.student.credit.credittree;

import java.util.Objects;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 *
 * @author Jhon Melvin
 */
public class CreditTree extends HBox {

    private CreditTree() {
        //
        creditMode = false;
    }

    private boolean creditMode;
    private boolean readOnly;

    public CreditTree(Pane drawboard) {
        this.initialize();
        this.lineDrawBoard = drawboard;
        // default values
        this.creditMode = false;
        this.readOnly = false;
    }

    public void setCreditMode(boolean creditMode) {
        this.creditMode = creditMode;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    // preset color values.
    private String lineStartColor = "#ff9151";
    private String lineEndColor = "#d13636";
    private String lineColor = "#b9ff99";

    public void setLineStartColor(String lineStartColor) {
        this.lineStartColor = lineStartColor;
    }

    public void setLineEndColor(String lineEndColor) {
        this.lineEndColor = lineEndColor;
    }

    public void setLineColor(String lineColor) {
        this.lineColor = lineColor;
    }

    //
    /**
     * Row Colors
     */
    private String rowDefaultColor = "";
    private String rowDisallowColor = "";

    public void setRowDefaultColor(String rowDefaultColor) {
        this.rowDefaultColor = rowDefaultColor;
    }

    public void setRowDisallowColor(String rowDisallowColor) {
        this.rowDisallowColor = rowDisallowColor;
    }

    //
    private ObservableList<CreditTreeColumn> ChildColumns;
    private Pane lineDrawBoard;

    //-----------------
    private ScrollPane scrollpane;

    public void setScrollpane(ScrollPane scrollpane) {
        this.scrollpane = scrollpane;
    }
    //-----------

    private void initialize() {
        this.setAlignment(Pos.BASELINE_LEFT);
        this.setSpacing(0);
        this.ChildColumns = FXCollections.observableArrayList();
        this.ChildColumns.addListener((ListChangeListener.Change<? extends CreditTreeColumn> c) -> {
            if (c.next()) {
                if (c.wasAdded()) {
                    for (CreditTreeColumn creditTreeColumns : c.getAddedSubList()) {
                        if (!this.getChildren().contains(creditTreeColumns)) {
                            this.getChildren().add(creditTreeColumns);
                        }
                    }
                } else if (c.wasRemoved()) {
                    this.getChildren().removeAll(c.getRemoved());
                }
            }
        });
        this.setFocusTraversable(true);
        this.keyEvents();
    }

    public void mouseEvent() {
        this.getChildColumns().forEach(cols -> {
            cols.getChildRows().forEach(rows -> {

                rows.getGradeField().addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
                    //rows.getGradeField().requestFocus();
                    this.focusedCol = this.getFocusedColumnIndex();
                    this.focusedRow = this.getFocusedColumn().getFocusedRowIndex();
                    showLines(rows.getID());
                    commitFocus();
                });
            });
        });
    }

    public ObservableList<CreditTreeColumn> getChildColumns() {
        return this.ChildColumns;
    }

    private Integer focusedCol = -1;
    private Integer focusedRow = -1;

    public void keyEvents() {
        this.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
            if (this.focusedCol != -1 && this.focusedRow != -1) {
                this.focusedCol = this.getFocusedColumnIndex();
                try {
                    this.focusedRow = this.getFocusedColumn().getFocusedRowIndex();
                } catch (Exception e) {
                    System.err.println("No selected row.");
                }

                // left and right
                if (event.getCode().equals(KeyCode.RIGHT)) {
                    this.focusedCol++;
                    if (this.focusedCol > this.getHorizontalLimit()) {
                        this.focusedCol = this.getHorizontalLimit();
                    }
                    this.commitFocus();
                } else if (event.getCode().equals(KeyCode.LEFT)) {
                    this.focusedCol--;
                    if (this.focusedCol < 0) {
                        this.focusedCol = 0;
                    }
                    this.commitFocus();
                } // up and down
                else if (event.getCode().equals(KeyCode.DOWN) || event.getCode().equals(KeyCode.ENTER)) {
                    this.focusedRow++;
                    if (this.focusedRow > this.getVerticalLimit()) {
                        this.focusedRow = this.getVerticalLimit();
                    }
                    this.commitFocus();
                } else if (event.getCode().equals(KeyCode.UP)) {
                    this.focusedRow--;
                    if (this.focusedRow < 0) {
                        this.focusedRow = 0;
                    }
                    this.commitFocus();
                }

            } else {
                this.setFocusedCell(0, 0);
                this.focusedCol = 0;
                this.focusedRow = 0;
            }
        });
    }

    public void commitFocus() {
        try {
            this.setFocusedCell(this.focusedCol, this.focusedRow);
            CreditTreeRow cr = this.getRowAt(this.focusedCol, this.focusedRow);
            Integer id = cr.getID();
            this.showLines(id);

            /**
             * Center in SCrollpane
             */
            centerNodeInScrollPane(this.scrollpane, cr);
            centerH(this.scrollpane, this.getColumn(this.focusedCol));
        } catch (IndexOutOfBoundsException e) {
            this.setFocusedCell(this.getFocusedColumnIndex(), this.getFocusedColumn().getFocusedRowIndex());
        } catch (NullPointerException e) {
            System.err.println("@commitFocus: null id");
        }
    }

    //--------------------------------------------------------------------------
    public static void centerNodeInScrollPane(ScrollPane scrollPane, Node node) {
        // y 
        double content_height = scrollPane.getContent().getBoundsInLocal().getHeight();
        double node_height = (node.getBoundsInParent().getMaxY()
                + node.getBoundsInParent().getMinY()) / 2.0;
        double viewport_height = scrollPane.getViewportBounds().getHeight();
        scrollPane.setVvalue(scrollPane.getVmax() * ((node_height - 0.5 * viewport_height) / (content_height - viewport_height)));

    }

    public static void centerH(ScrollPane scrollPane, CreditTreeColumn node) {
        // x
        double content_width = scrollPane.getContent().getBoundsInLocal().getWidth();
        double node_width = (node.getBoundsInParent().getMaxX()
                + node.getBoundsInParent().getMinX()) / 2.0;
        double viewport_width = scrollPane.getViewportBounds().getWidth();
        double hval = scrollPane.getHmax() * ((node_width - 0.5 * viewport_width) / (content_width - viewport_width));

        scrollPane.setHvalue(hval);
    }

    //--------------------------------------------------------------------------
    public CreditTreeRow getRowAt(int col, int row) {
        return ((CreditTreeColumn) this.getChildColumns().get(col)).getChildRows().get(row);
    }

    public int getHorizontalLimit() {
        return (this.getChildColumns().size() - 1);
    }

    public int getVerticalLimit() {
        return (this.getFocusedColumn().getChildren().size() - 1);
    }

    /**
     * Sets the focused cell.
     *
     * @param column
     * @param row
     */
    public void setFocusedCell(int column, int row) {
        this.getColumn(column).setFocusedRow(row);
    }

    public CreditTreeColumn getColumn(int index) {
        Node node = this.getChildColumns().get(index);
        if (node instanceof CreditTreeColumn) {
            CreditTreeColumn col = (CreditTreeColumn) node;
            return col;
        }
        return null;
    }

    public int getFocusedColumnIndex() {
        for (int x = 0; x < this.getChildColumns().size(); x++) {
            Node node = this.getChildColumns().get(x);
            if (node instanceof CreditTreeColumn) {
                if (((CreditTreeColumn) node).hasFocus()) {
                    return x;
                }
            }
        }
        return -1;
    }

    public CreditTreeColumn getFocusedColumn() {
        int index = this.getFocusedColumnIndex();
        if (index != -1) {
            return (CreditTreeColumn) this.getChildColumns().get(this.getFocusedColumnIndex());
        }
        return null;
    }

    //---------------- LINES
    private Background defaultBack;

    /**
     * Checks for pre-requisite.
     *
     * @param id
     */
    public void showLines(Integer id) {
        this.lineDrawBoard.getChildren().clear();
        CreditTreeRow row = this.getRowById(id);
        int preqCount = 0;
        try {
            preqCount = row.getPreRequisites().length;
        } catch (NullPointerException e) {
            System.err.println("@showLines: row == null");
        }

        if (preqCount != 0) {
            // the end point is this row
            SuperPoint endPoint = row.getLeftPoint();
            int emptyPre = 0;
            for (int x = 0; x < preqCount; x++) {
                CreditTreeRow preRequisite = this.getRowById(row.getPreRequisites()[x]);

                if (preRequisite.getGradeField().getText().trim().isEmpty()) {
                    emptyPre++;
                }
                // the start point is it's pre-requisite.
                SuperPoint startPoint = preRequisite.getRightPoint();
                // set lines
                Line line = new Line();
                line.setStrokeWidth(3);
                line.setStroke(Color.web(lineColor));
                line.startXProperty().bind(startPoint.getX());
                line.startYProperty().bind(startPoint.getY());
                line.endXProperty().bind(endPoint.getX());
                line.endYProperty().bind(endPoint.getY());

                //set circles
                Circle start = new Circle();
                start.setFill(Color.web(lineStartColor));
                start.centerXProperty().bind(startPoint.getX());
                start.centerYProperty().bind(startPoint.getY());
                start.setRadius(7.0f);

                Circle end = new Circle();
                //end.setStroke(Color.BLACK);
                end.setFill(Color.web(lineEndColor));
                end.setStrokeWidth(2);
                end.centerXProperty().bind(endPoint.getX());
                end.centerYProperty().bind(endPoint.getY());
                end.setRadius(7.0f);
                this.lineDrawBoard.getChildren().addAll(line, start, end);
            }

            if (emptyPre != 0) {
                /**
                 * If Missing pre-requisite.
                 */
                if (!creditMode) {
                    /**
                     * If credit mode is enabled this portion will be skipped.
                     */
                    row.getGradeField().setText("");
                    row.getGradeField().setEditable(false);
                    //this.defaultBack = this.getChildColumns().get(0).getChildRows().get(0).getGradeField().getBackground();
                    //row.getGradeField().setBackground(new Background(new BackgroundFill(Color.web("#ff7575"), CornerRadii.EMPTY, Insets.EMPTY)));
                }
                //System.out.println("hey");
                row.setColor(this.rowDisallowColor);
            } else {
                // if complete pre-requisite
                row.getGradeField().setEditable(true);
                row.setColor(this.rowDefaultColor);
                //System.out.println("hoy");
            }

            /**
             * Focus Leave.
             */
            row.getGradeField().focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                if (newValue == false) {
                    if (row.getGradeField().getText().isEmpty()) {
                        //row.getGradeField().setBackground(this.defaultBack);
                        row.setColor(this.rowDefaultColor);
                    }
                }
            });

            /**
             * If read only
             */
            if (readOnly) {
                row.getGradeField().setEditable(false);
            }
        }

    }

    public CreditTreeRow getRowById(Integer ID) {
        for (CreditTreeColumn childColumn : this.getChildColumns()) {
            for (CreditTreeRow childRow : childColumn.getChildRows()) {
                if (Objects.equals(childRow.getID(), ID)) {
                    return childRow;
                }
            }
        }
        return null;
    }

    //--------------------------------------------------------------------------
    int _____count;

    public int countAll() {
        _____count = 0;
        readAll((x, y) -> {
            _____count++;
        });
        return _____count;
    }

    public void disableAllText() {
        readAll((x, y) -> {
            CreditTreeRow cr = this.getRowAt(x, y);
            cr.txtGrade.setEditable(false);
        });
    }

    public void enableAllText() {
        readAll((x, y) -> {
            CreditTreeRow cr = this.getRowAt(x, y);
            cr.txtGrade.setEditable(true);
        });
    }

    private void readAll(LocationEvent le) {
        for (int x = 0; x < this.getChildColumns().size(); x++) {
            for (int y = 0; y < this.getChildColumns().get(x).getChildRows().size(); y++) {
                le.location(x, y);
            }
        }
    }

    interface LocationEvent {

        void location(int x, int y);
    }

}

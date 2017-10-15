/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cict.evaluation.student.credit.credittree;

import com.jhmvin.fx.controls.SimpleImage;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

/**
 *
 * @author Jhon Melvin
 */
public class CreditTreeRow extends HBox {

    public CreditTreeRow() {
        this.initialize();
    }

    // really needs to be private for the scene graph id
    private Integer SUBJECT_ID;
    // add style sheets to this row
    private String stylesheet;
    // add pre requisites
    private Integer[] PRE_REQUISITES;

    //Controls
    public Label lblSubjectCode = new Label();
    public TextField txtGrade = new TextField();
    public Button btnDelete = new Button();
    private Pane linePanel;

    /**
     * Create Row.
     */
    private void initialize() {
        this.PRE_REQUISITES = new Integer[0];
        /**
         * Row Size
         */
        // pref
        this.setPrefHeight(Control.USE_COMPUTED_SIZE);
        this.setPrefWidth(Control.USE_COMPUTED_SIZE);
        // min
        this.setMinWidth(Control.USE_COMPUTED_SIZE);
        this.setMinHeight(Control.USE_PREF_SIZE);
        // max
        this.setMaxWidth(Double.MAX_VALUE);
        this.setMaxHeight(Control.USE_PREF_SIZE);

        /**
         * Alignment
         */
        this.setAlignment(Pos.CENTER_LEFT);
        // spacing
        this.setSpacing(5); // default spacing
        this.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));

        /**
         * Any changes you want to this label must be applied to credit-tree.css
         */
        this.lblSubjectCode.getStyleClass().add("credit-row-label");
        this.txtGrade.getStyleClass().add("credit-row-text");
        this.btnDelete.getStyleClass().add("credit-row-delete");
        this.getStyleClass().add("credit-row");

        //this.setBackground(new Background(new BackgroundFill(Paint.valueOf("#ffccb5"), new CornerRadii(10), new Insets(5, 5, 5, 5))));
        //HBox.setMargin(txtGrade, new Insets(20));
        //this.txtGrade.setStyle("-fx-faint-focus-color: transparent;-fx-focus-color:rgba(255,0,0,1.0);");
        this.events();

        /**
         * Button setup
         */
        Image deleteIcon
                = SimpleImage.make("org.cict.evaluation.student.credit.credittree",
                        "Trash_64px.png");
        ImageView deleteImage = new ImageView(deleteIcon);
        deleteImage.setFitHeight(20.0);
        deleteImage.setFitWidth(20.0);
        this.btnDelete.setText("");
        this.btnDelete.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        this.btnDelete.setGraphic(deleteImage);
        this.btnDelete.setGraphicTextGap(0.0);
        this.btnDelete.setMaxHeight(20.0);
        this.btnDelete.setMaxWidth(20.0);
        this.btnDelete.setPrefHeight(20.0);
        this.btnDelete.setPrefHeight(20.0);

        this.getChildren().addAll(lblSubjectCode, txtGrade/*, this.btnDelete*/);

    }

    private void initializeFx() {
//        this.PRE_REQUISITES = new Integer[0];
//
//        TreeRowFx row = new TreeRowFx();
//        this.lblSubjectCode = row.lbl_code;
//        this.txtGrade = row.txt_grade;
//        HBox.setHgrow(row.row, Priority.ALWAYS);
//        this.events();
//
//        this.getChildren().add(row.row);

    }

//    private class TreeRowFx extends SceneFX {
//
//        private HBox row;
//        private Label lbl_code;
//        private TextField txt_grade;
//
//        public TreeRowFx() {
//            row = Mono.fx().create()
//                    .setPackageName("org.cict.evaluation.student.credit.credittree")
//                    .setFxmlDocument("CreditTreeRow")
//                    .makeFX()
//                    .pullOutLayout();
//
//            lbl_code = super.searchAccessibilityText(row, "lbl_code");
//            txt_grade = super.searchAccessibilityText(row, "txt_grade");
//        }
//    }
    /**
     * Sets the color of this row.
     *
     * @param colorCode
     */
    public void setColor(String colorCode) {
        this.setBackground(new Background(new BackgroundFill(Paint.valueOf(colorCode), new CornerRadii(10), new Insets(5, 5, 5, 5))));
    }

    public TextField getGradeField() {
        return this.txtGrade;
    }

    public String getCode() {
        return this.lblSubjectCode.getText();
    }

    public void setSubjectCode(String code) {
        this.lblSubjectCode.setText(code);
    }

    //--------------------------------------------------------------------------
    public void addStyle(String css) {
        this.setStyle(this.stylesheet += css);
    }

    public void setPreRequisites(Integer... ids) {
        this.PRE_REQUISITES = ids;
    }

    public Integer[] getPreRequisites() {
        return this.PRE_REQUISITES;
    }

    //--------------------------------------------------------------------------
    /**
     *
     * @return
     */
    public Integer getID() {
        return this.SUBJECT_ID;
    }

    /**
     *
     * @param subjectID
     */
    public void setID(Integer subjectID) {
        super.setId("row_" + subjectID);
        this.SUBJECT_ID = subjectID;
    }

    /**
     * this will set as the main board for drawing lines.
     *
     * @param panel the line panel that contains the line
     */
    public void setLinePanel(Pane panel) {
        this.linePanel = panel;
    }

    //--------------------------------------------------------------------------
    // control methods
    /**
     * Gets the focus state of this row
     *
     * @return
     */
    public boolean hasFocus() {
        boolean hasFocus = false;

        for (Node node : this.getChildren()) {

            if (node.isFocused()) {
                hasFocus = true;
                break;
            }

        }
        return hasFocus;
    }

    public ObjectBinding<Bounds> getBounds() {
        ObjectBinding<Bounds> rowBounds = Bindings.createObjectBinding(() -> {
            Bounds label1InScene = this.localToScene(this.getBoundsInLocal());
            return this.linePanel.sceneToLocal(label1InScene);
        }, this.boundsInLocalProperty(), this.localToSceneTransformProperty(), this.linePanel.localToSceneTransformProperty());
        return rowBounds;
    }

    public SuperPoint getRightPoint() {
        ObjectBinding<Bounds> rowBounds = this.getBounds();
        DoubleBinding startX = Bindings.createDoubleBinding(() -> rowBounds.get().getMaxX(), rowBounds);
        DoubleBinding startY = Bindings.createDoubleBinding(() -> {
            Bounds b = rowBounds.get();
            return b.getMinY() + b.getHeight() / 2;
        }, rowBounds);
        SuperPoint sp = new SuperPoint(startX, startY);
        return sp;
    }

    public SuperPoint getLeftPoint() {
        ObjectBinding<Bounds> rowBounds = this.getBounds();
        DoubleBinding endX = Bindings.createDoubleBinding(() -> rowBounds.get().getMinX(), rowBounds);
        DoubleBinding endY = Bindings.createDoubleBinding(() -> {
            Bounds b = rowBounds.get();
            return b.getMinY() + b.getHeight() / 2;
        }, rowBounds);
        SuperPoint sp = new SuperPoint(endX, endY);
        return sp;
    }

    //
    private void events() {
        // key
        this.txtGrade.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
            if (event.getCode().equals(KeyCode.BACK_SPACE)) {
                if (this.txtGrade.isEditable()) {
                    this.txtGrade.setText("");
                }
            }
        });

        // mouse
        this.txtGrade.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            this.txtGrade.selectRange(0, this.txtGrade.getText().length());
        });

        // Focused Event
        this.txtGrade.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                String textValue = this.txtGrade.getText().trim();
                if (!textValue.isEmpty()) {
                    if (textValue.length() == 1) {
                        textValue += ".00";
                    } else if (textValue.length() == 2) {
                        textValue += "00";
                    }
                    this.txtGrade.setText(textValue);
                }
            } else {
                this.txtGrade.selectRange(0, this.txtGrade.getText().length());
            }
        });

        // change event
        this.txtGrade.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            this.txtGrade.setText(newValue.trim());
            if (!newValue.isEmpty()) {

                if (Character.toString(newValue.charAt(0)).equalsIgnoreCase("i")) {
                    this.txtGrade.setText("INC");
                    return;
                }

                if (Character.toString(newValue.charAt(0)).equalsIgnoreCase("e")) {
                    this.txtGrade.setText("EXP");
                    return;
                }

                try {
                    Double val = Double.parseDouble(newValue);
                    if (val > 3.00) {
                        this.txtGrade.setText(oldValue);
                    }
                } catch (NumberFormatException e) {
                    this.txtGrade.setText(oldValue);
                }

                if (newValue.charAt(0) == '0') {
                    this.txtGrade.setText(oldValue);
                }

                if (newValue.length() == 3) {
                    switch (newValue.charAt(2)) {
                        case '0':
                            newValue += "0";
                            break;
                        case '2':
                            newValue += "5";
                            break;
                        case '5':
                            newValue += "0";
                            break;
                        case '7':
                            newValue += "5";
                            break;
                        default:
                            newValue = oldValue;
                            break;
                    }
                    this.txtGrade.setText(newValue);
                }

                if (newValue.length() > 4) {
                    this.txtGrade.setText(oldValue);
                }
            }
        });
    }

}

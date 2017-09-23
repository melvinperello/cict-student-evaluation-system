/* 
 * Copyright (C) Jhon Melvin N. Perello - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * PROPRIETARY and CONFIDENTIAL
 *
 * Written by Jhon Melvin N. Perello <jhmvinperello@gmail.com>, 2017.
 *
 */
package org.cict.evaluation.views;

import org.cict.evaluation.evaluator.Evaluator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 *
 * @author Jhon Melvin
 */
public class SectionSearchView extends HBox {

    public Label labelCode;
    public Label labelTitle;
    public Label labelMax;
    public Label labelExpected;
    public Label labelCurrent;
    public Label labelSection;
    //
    public Integer loadGroupID;
    public Integer subjectID;
    public Integer sectionID;

    public SectionSearchView() {
        this.construct();
    }

    private void construct() {

        HBox hbox_subject = new HBox(30);
        hbox_subject.setPadding(new Insets(10.0, 0.0, 10.0, 10.0));
        hbox_subject.setAlignment(Pos.CENTER_LEFT);
        //AnchorPane.setLeftAnchor(hbox_subject, 0.0);
        HBox.setHgrow(hbox_subject, Priority.ALWAYS);

        // subject icon
        ImageView img_icon = new ImageView();
        img_icon.setFitWidth(40.0);
        img_icon.setFitHeight(40.0);
        img_icon.setImage(new Image("res/img/Books_96px.png"));
        hbox_subject.getChildren().add(img_icon);

        VBox subjectContent = new VBox();

        // subject code
        labelCode = new Label("Math 113");
        labelCode.setFont(new Font(25.0));
        subjectContent.getChildren().add(labelCode);
        labelTitle = new Label("Lorem Ipsum Dolor Algebra Computer");
        labelTitle.setFont(new Font(15.0));
        subjectContent.getChildren().add(labelTitle);

        hbox_subject.getChildren().add(subjectContent);

        HBox hbox_stats = new HBox(15);
        hbox_stats.setAlignment(Pos.CENTER_LEFT);

        // stats
        ImageView img_max = new ImageView();
        img_max.setFitWidth(30.0);
        img_max.setFitHeight(30.0);
        img_max.setImage(new Image("res/img/High Priority_96px.png"));

        labelMax = new Label("30");
        labelMax.setFont(new Font(15.0));
        labelMax.setGraphic(img_max);
        labelMax.setGraphicTextGap(10.0);

        hbox_stats.getChildren().add(labelMax);

        //
        ImageView img_expected = new ImageView();
        img_expected.setFitWidth(30.0);
        img_expected.setFitHeight(30.0);
        img_expected.setImage(new Image("res/img/Goal_96px.png"));

        labelExpected = new Label("> 25");
        labelExpected.setFont(new Font(15.0));
        labelExpected.setGraphic(img_expected);
        labelExpected.setGraphicTextGap(10.0);

        hbox_stats.getChildren().add(labelExpected);

        //
        ImageView img_current = new ImageView();
        img_current.setFitWidth(30.0);
        img_current.setFitHeight(30.0);
        img_current.setImage(new Image("res/img/Graduation Cap_104px.png"));

        labelCurrent = new Label("18");
        labelCurrent.setFont(new Font(15.0));
        labelCurrent.setGraphic(img_current);
        labelCurrent.setGraphicTextGap(10.0);

        hbox_stats.getChildren().add(labelCurrent);

        // statss
        // secion
        HBox hbox_controls = new HBox(10);
        hbox_controls.setPadding(new Insets(10.0, 20.0, 10.0, 30.0));
        hbox_controls.setAlignment(Pos.CENTER_LEFT);
        // classroom icon
        ImageView img_class = new ImageView();
        img_class.setFitWidth(40.0);
        img_class.setFitHeight(40.0);
        img_class.setImage(new Image("res/img/Google Classroom_96px.png"));
        hbox_controls.getChildren().add(img_class);
        // section
        labelSection = new Label("---");
        labelSection.setStyle("-fx-font-weight: bold;");
        labelSection.setFont(new Font(15.0));
        labelSection.setPadding(new Insets(0.0, 30.0, 0.0, 0.0));
        hbox_controls.getChildren().add(labelSection);

        this.getChildren().add(hbox_subject);
        this.getChildren().add(hbox_stats);
        this.getChildren().add(hbox_controls);

        //---------------------------
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            this.getScene().setCursor(Cursor.HAND);
            System.out.println("@SectionSearchView: Mouse Pressed Recieved.");
            Evaluator.instance().pressedLoadGroupID = this.loadGroupID;
            Evaluator.instance().pressedSubjectID = this.subjectID;
            Evaluator.instance().pressedSectionID = this.sectionID;

        });

        this.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            this.getScene().setCursor(Cursor.DEFAULT);
            System.out.println("@SectionSearchView: Mouse Realeased Recieved.");
            Evaluator.instance().sectionViewReleased = true;
        });
        //main.getChildren().add(hbox_controls);

        this.setStyle("-fx-border-color: #000000");
    }

}

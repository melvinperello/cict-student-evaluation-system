/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cict.evaluation.views;

import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;

/**
 *
 * @author Jhon Melvin
 */
public class SubjectView extends HBox {

    /**
     * For displaying purpose
     */
    public Label code;
    public Label title;
    public Label section;
    public JFXButton actionFind;
    /**
     * Verifications (not visible)
     */
    public Double lec_units;
    public Double lab_units;
    public Double units;
    public Integer subjectID;
    public Integer loadGroupID;
    public Integer loadSecID;

    public SubjectView() {
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

        // subject code
        code = new Label("Math 113");
        code.setFont(new Font(30.0));
        hbox_subject.getChildren().add(code);

        // subject title
        title = new Label("FUNDAMENTALS OF INFORMATION TECHNOLOGY");
        title.setFont(new Font(15.0));
        hbox_subject.getChildren().add(title);

        HBox hbox_controls = new HBox(10);
        hbox_controls.setPadding(new Insets(10.0, 20.0, 10.0, 0.0));
        hbox_controls.setAlignment(Pos.CENTER_RIGHT);
        //AnchorPane.setRightAnchor(hbox_controls, 0.0);

        // classroom icon
        ImageView img_class = new ImageView();
        img_class.setFitWidth(40.0);
        img_class.setFitHeight(40.0);
        img_class.setImage(new Image("res/img/Google Classroom_96px.png"));
        hbox_controls.getChildren().add(img_class);
        // section
        section = new Label("BSIT 3AG1");
        section.setStyle("-fx-font-weight: bold;");
        section.setFont(new Font(15.0));
        section.setPadding(new Insets(0.0, 50.0, 0.0, 0.0));
        hbox_controls.getChildren().add(section);
        //
        actionFind = new JFXButton();
        actionFind.setStyle("-fx-text-fill: #FFFFFF; -fx-background-color: #D9534F;");
        actionFind.setText("Remove");
        actionFind.setFont(new Font(15.0));
        hbox_controls.getChildren().add(actionFind);

        this.getChildren().add(hbox_subject);
        this.getChildren().add(hbox_controls);

        this.setStyle("-fx-border-color: #000000");
    }

}

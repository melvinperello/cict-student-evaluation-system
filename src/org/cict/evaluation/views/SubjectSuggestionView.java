/**
 * CAPSTONE PROJECT.
 * BSIT 4A-G1.
 * MONOSYNC TECHNOLOGIES.
 * MONOSYNC FRAMEWORK VERSION 1.0.0 TEACUP RICE ROLL.
 * THIS PROJECT IS PROPRIETARY AND CONFIDENTIAL ANY PART THEREOF.
 * COPYING AND DISTRIBUTION WITHOUT PERMISSION ARE NOT ALLOWED.
 *
 * COLLEGE OF INFORMATION AND COMMUNICATIONS TECHNOLOGY.
 * LINKED SYSTEM.
 *
 * PROJECT MANAGER: JHON MELVIN N. PERELLO
 * DEVELOPERS:
 * JOEMAR N. DELA CRUZ
 * GRETHEL EINSTEIN BERNARDINO
 *
 * OTHER LIBRARIES THAT ARE USED BELONGS TO THEIR RESPECTFUL OWNERS AND AUTHORS.
 * NO COPYRIGHT ARE INTENTIONAL OR INTENDED.
 * THIS PROJECT IS NOT PROFITABLE HENCE FOR EDUCATIONAL PURPOSES ONLY.
 * THIS PROJECT IS ONLY FOR COMPLIANCE TO OUR REQUIREMENTS.
 * THIS PROJECT DOES NOT INCLUDE DISTRIBUTION FOR OTHER PURPOSES.
 *
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
public class SubjectSuggestionView extends HBox {

    /**
     * For displaying purpose
     */
    public Label code;
    public Label title;
    public Label section;
    public JFXButton actionFind;

    public SubjectSuggestionView() {
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
        section = new Label("View Available Sections");
        section.setStyle("-fx-font-weight: bold;");
        section.setFont(new Font(15.0));
        section.setPadding(new Insets(0.0, 50.0, 0.0, 0.0));
        hbox_controls.getChildren().add(section);
        //
        actionFind = new JFXButton();
        actionFind.setStyle("-fx-text-fill: #FFFFFF; -fx-background-color: #D9534F;");
        actionFind.setText("Remove");
        actionFind.setFont(new Font(15.0));
        //hbox_controls.getChildren().add(actionFind);

        this.getChildren().add(hbox_subject);
        this.getChildren().add(hbox_controls);

        this.setStyle("-fx-border-color: #000000");
    }

}

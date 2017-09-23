/**
 * CAPSTONE PROJECT.
 * BSIT 4A-G1.
 * MONOSYNC TECHNOLOGIES.
 * MONOSYNC FRAMEWORK VERSION 1.0.0 TEACUP RICE ROLL.
 * THIS PROJECT IS PROPRIETARY AND CONFIDENTIAL ANY PART OF THEREOF.
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
 * NO COPYRIGHT ARE INTENTIONALLY OR INTENDED.
 * THIS PROJECT IS NOT PROFITABLE HENCE FOR EDUCATIONAL PURPOSES ONLY.
 * THIS PROJECT IS ONLY FOR COMPLIANCE TO OUR REQUIREMENTS.
 * THIS PROJECT DOES NOT INCLUDE DISTRIBUTION FOR OTHER PURPOSES.
 *
 */
package com.jhmvin.fx.notify;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

/**
 *
 * @author Jhon Melvin
 */
public class AlertMessage {

    public AlertMessage() {

    }
    private Alert alertMessage;

    public AlertMessage createError() {
        this.alertMessage = new Alert(Alert.AlertType.ERROR);
        return this;
    }

    public AlertMessage createInfo() {
        this.alertMessage = new Alert(Alert.AlertType.INFORMATION);
        return this;
    }

    public AlertMessage createWarning() {
        this.alertMessage = new Alert(Alert.AlertType.WARNING);
        return this;
    }

    public AlertMessage create() {
        this.alertMessage = new Alert(Alert.AlertType.NONE);
        return this;
    }

    public AlertMessage setTitle(String title) {
        this.alertMessage.setTitle(title);
        return this;
    }

    public AlertMessage setHeader(String header) {
        this.alertMessage.setHeaderText(header);
        return this;
    }

    public AlertMessage setMessage(String message) {
        this.alertMessage.setContentText(message);
        return this;
    }

    //-------------------------------------------------------------------------
    public void show() {
        this.alertMessage.show();
    }

    public void showAndWait() {
        this.alertMessage.showAndWait();
    }

    //-------------------------------------------------------------------------
    /**
     * For Confirmation purposes.
     *
     * @return
     */
    public AlertMessage createConfirmation() {
        this.alertMessage = new Alert(Alert.AlertType.CONFIRMATION);
        return this;
    }

    /**
     *
     * @return 1 for ok -1 for cancel
     */
    public int confirmOkCancel() {
        Optional<ButtonType> choice = this.alertMessage.showAndWait();
        return (choice.get() == ButtonType.OK) ? 1 : -1;
    }

    public int confirmYesNo() {
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        this.alertMessage.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> choice = this.alertMessage.showAndWait();
        return (choice.get().equals(yesButton)) ? 1 : -1;
    }

    public int confirmCustom(String positive, String negative) {
        ButtonType yesButton = new ButtonType(positive);
        ButtonType noButton = new ButtonType(negative);
        this.alertMessage.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> choice = this.alertMessage.showAndWait();
        return (choice.get().equals(yesButton)) ? 1 : -1;
    }

}

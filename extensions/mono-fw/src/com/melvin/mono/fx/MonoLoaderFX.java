/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melvin.mono.fx;

import com.melvin.mono.classmeta.ConsoleWriter;
import java.net.URL;
import javafx.fxml.LoadException;

/**
 *
 * @author Jhon Melvin
 */
public class MonoLoaderFX extends javafx.fxml.FXMLLoader {

    public MonoLoaderFX(URL location) {
        super(location);
    }

    @Override
    public <T> T load() {
        try {
            return super.load();
        } catch (Exception ex) {
            this.analyzeException(ex);
            return null;
        }
    }

    public void analyzeException(Exception ex) {
        if (ex instanceof LoadException) {
            displayException(ex);
            displayLoadeExceptionTip();
        } else if (ex instanceof IllegalStateException) {
            displayException(ex);
            displayIllegalStateExceptionTip();
        } else {
            displayException(ex);
        }

        ConsoleWriter.monoStackTrace(ex);
    }

    public void displayException(Exception ex) {
        ConsoleWriter.monoException(ex);
    }

    public void displayLoadeExceptionTip() {
        ConsoleWriter.monoError("");

        ConsoleWriter.monoTip("This exception occurs whenever you have an exception inside your controller.",
                "Try to put a try catch inside your Controller Initilization method.",
                "Multiple Controller has been detected",
                "If you want to use a controller using your code",
                "Remove the controller in the FXML File");

    }

    public void displayIllegalStateExceptionTip() {
        ConsoleWriter.monoError("");
        ConsoleWriter.monoTip("Wrong Package of the FXML file",
                "Wrong name of the FXML document",
                "Wrong syntax inside the fxml file");
    }

}

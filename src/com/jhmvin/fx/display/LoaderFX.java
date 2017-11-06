/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jhmvin.fx.display;

import com.melvin.mono.fx.MonoLoaderFX;
import java.net.URL;
import javafx.fxml.LoadException;

/**
 * This class will not receive anymore updates please use.
 *
 * @see MonoLoaderFX
 * @author Jhon Melvin
 */
@Deprecated
public class LoaderFX extends javafx.fxml.FXMLLoader {

    public LoaderFX(URL location) {
        super(location);
    }

    @Override
    public <T> T load() {
        try {
            return super.load();
        } catch (Exception ex) {
            if (ex instanceof LoadException) {
                this.loadExceptionHandler(ex);
            } else if (ex instanceof IllegalStateException) {
                this.loadFXMLHandler(ex);
            } else {
                this.generalException(ex);
            }

            return null;
        }
    }

    private void generalException(Exception ex) {
        System.err.println("-----------------------------");
        System.err.println(" * Unknown Error *");
        System.err.println("-----------------------------");
        System.err.println(" * Monosync Framework was not able load your Document.");
        System.err.println(" * Error Summary.");
        System.err.println(" *    Error Name: " + ex.getClass().getName());
        System.err.println(" *    Localized Message: " + ex.getLocalizedMessage());
        System.err.println(" *    Message: " + ex.getMessage());
        System.err.println(" * This error is generated to help you locate the possible problem.");
        System.err.println(" * Here's a detailed Information about the error:");
        ex.printStackTrace();
        System.err.println("-----------------------------");
        System.err.println(" * Note: ");
        System.err.println(" * Since the Document is not loaded this will return a null ROOT");
        System.err.println("-----------------------------");
        System.err.println(" * End of Unknown Error * ");
        System.err.println("-----------------------------");
    }

    private void loadExceptionHandler(Exception ex) {
        String error_name = ex.getMessage();

        System.err.println("-----------------------------");
        System.err.println(" * Controller Error *");
        System.err.println("-----------------------------");

        if (error_name.contains("Controller value already specified.")) {
            System.err.println("♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥");
            System.err.println("  ☺  CONTROLLER VALUE ALREADY SPECIFIED  ☺");
            System.err.println("♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥♥");
            System.err.println(" - Please remove the controller attribute in the Scene Builder.");
            System.err.println(" - You have used HashMaps to pass.");
            System.err.println("   values in another controller. and ended up with an error");
            System.err.println("   ,becuase there is already a controller speicified for this fxml");
            System.err.println("---------------------------------------------");
        }

        System.err.println(" * Monosync Framework was not able load your selected controller.");
        System.err.println(" * It may be caused by missing libraries for the UI.");
        System.err.println(" * Null values inside the controller or something more.");
        System.err.println(" * This error is generated to help you locate the possible problem.");
        System.err.println(" * Here's a detailed Information about the error:");
        ex.printStackTrace();
        System.err.println("-----------------------------");
        System.err.println(" * Note: ");
        System.err.println(" * Since the controller is not loaded this will return a null ROOT");
        System.err.println("-----------------------------");
        System.err.println(" * End of Controller Error * ");
        System.err.println("-----------------------------");
    }

    private void loadFXMLHandler(Exception ex) {
        System.err.println("-----------------------------");
        System.err.println(" * FXML Error *");
        System.err.println("-----------------------------");
        System.err.println(" * Monosync Framework was not able load your selected FXML Document.");
        System.err.println(" * It may be caused by wrong syntax of the FXML");
        System.err.println(" * Wrong location of the fxml file.");
        System.err.println(" * This error is generated to help you locate the possible problem.");
        System.err.println(" * Here's a detailed Information about the error:");
        ex.printStackTrace();
        System.err.println("-----------------------------");
        System.err.println(" * Note: ");
        System.err.println(" * Since the FXML is not loaded this will return a null ROOT");
        System.err.println("-----------------------------");
        System.err.println(" * End of FXML Error * ");
        System.err.println("-----------------------------");
    }

}

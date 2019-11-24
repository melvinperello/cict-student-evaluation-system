package com.jhmvin.fx.notify.snackbars;

import com.jfoenix.controls.JFXSnackbar;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;

/**
 * DEPENDENCIES: /LIBRARIES/LIBMATERIAL
 *
 * @author Jhon Melvin
 */
public class Snackbar {

    private final static Integer lapse = 3000;
    private final static String INFOMRATION = Snackbar.class.getResource("snackbar.css").toExternalForm();
    private final static String SUCCESS = Snackbar.class.getResource("snackbarsuccess.css").toExternalForm();
    private final static String ERROR = Snackbar.class.getResource("snackbarerror.css").toExternalForm();
    
    public static void snackBarInfo(Pane container, String message) {
        snackBarMaster(container, message, INFOMRATION);
    }

    public static void snackBarSuccess(Pane container, String message) {
        snackBarMaster(container, message, SUCCESS);
    }

    public static void snackBarError(Pane container, String message) {
        snackBarMaster(container, message, ERROR);
    }

    /**
     * used only as interface for the methods above
     *
     * @param container
     * @param message
     * @param popType
     */
    private static void snackBarMaster(Pane container, String message, String popType) {
        try {
            //TODO: JDK-11-FIX
            container.getStylesheets().removeAll(INFOMRATION, SUCCESS, ERROR);
            JFXSnackbar popup = new JFXSnackbar(container);
            EventHandler event = (EventHandler) (Event event1) -> {
                popup.unregisterSnackbarContainer(container);
            };

            popup.show(message, "Close", lapse, event);
            popup.prefWidthProperty().bind(container.widthProperty());
            container.getStylesheets().add(popType);
        } catch (Exception e) {
            System.err.println("@snackbar: Just Another Null Pointer, I'm Harmeless anyway.");
        }
    }

    //
    public Snackbar() {
        //
    }

    public void showInfo(Pane parent, String message) {
        snackBarInfo(parent, message);
    }

    public void showSuccess(Pane parent, String message) {
        snackBarSuccess(parent, message);
    }

    public void showError(Pane parent, String message) {
        snackBarError(parent, message);
    }

}

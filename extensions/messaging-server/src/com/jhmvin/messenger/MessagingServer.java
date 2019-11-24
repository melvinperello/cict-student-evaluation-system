/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jhmvin.messenger;

import app.sms.models.DB;
import app.sms.models.Database;
import app.sms.models.ExceptionLogMapping;
import app.sms.models.MapFactory;
import app.sms.models.SmsLogMapping;
import app.sms.models.SmsQueryMapping;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.SimpleTask;
import com.jhmvin.sms.MonoSMS;
import com.melvin.java.properties.PropertyFile;
import com.melvin.mono.fx.MonoLauncher;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.hibernate.criterion.Order;
import org.smslib.OutboundMessage;

/**
 *
 * @author Jhon Melvin
 */
public class MessagingServer extends MonoLauncher {

    @FXML
    private VBox vbox_home;

    @FXML
    private Label lbl_modem;

    @FXML
    private Label lbl_model;

    @FXML
    private Label lbl_serial;

    @FXML
    private Label lbl_message_center;

    @FXML
    private VBox vbox_loading;

    @FXML
    private Label lbl_loading_message;

    @Override
    public void onStartUp() {

    }

    //--------------------------------------------------------------------------
    private MonoSMS smsManager;

    @Override
    public void onDelayedStart() {
        //----------------------------------------------------------------------
        onStartServices();
        //----------------------------------------------------------------------
        this.getCurrentStage().setOnCloseRequest(event -> {
            //------------------------------------------------------------------
            this.onStageClosing();
            event.consume();
        });
    }

    /**
     * Start Services.
     */
    private void onStartServices() {
        SimpleTask startServices = new SimpleTask("start-service-thread");
        startServices.setTask(() -> {
            //------------------------------------------------------------------
            // Start Database Service
            Database.connect();
            //------------------------------------------------------------------
            // Check connectivity
            if (Mono.orm().getSessionFactory() == null) {
                Platform.runLater(() -> {
                    Mono.fx().alert().createError()
                            .setTitle("Connectivity")
                            .setHeader("Database Problem")
                            .setMessage("The system was unable to initialize database connectivity. Please Try Again.")
                            .showAndWait();
                    Runtime.getRuntime().halt(0);
                });
                // Make this task fail
                throw new RuntimeException("Cannot start services");
            }
            //------------------------------------------------------------------
            // Change Loading Text.
            Platform.runLater(() -> {
                this.lbl_loading_message.setText("Starting SMS Service");
            });
            //------------------------------------------------------------------
            // Retrieve Properties
            try {
                this.smsManager = new MonoSMS();
                Properties config = PropertyFile.getPropertyFile(Configuration.CONFIG_PROPERTY);
                String id = config.getOrDefault("id", "").toString();
                String port = config.getOrDefault("port", "").toString();
                String msc = config.getOrDefault("message_center", "").toString();
                int bitrate = Integer.parseInt(config.getProperty("bitrate"));
                // set properties
                this.smsManager.setModemID(id); // optional
                this.smsManager.setComPort(port);
                this.smsManager.setBitRate(bitrate);
                this.smsManager.setModemName(id); // optional
                this.smsManager.setModemModel(id);
                this.smsManager.setModemPin(null); // SIM Card PIN
                this.smsManager.setMessageCenterNumber(msc);
            } catch (final Exception e) {
                Platform.runLater(() -> {
                    String message = "The server configuration may contain invalid parmeters: " + e.getMessage();
                    if (e instanceof NumberFormatException) {
                        message = "The bitrate values is invalid.";
                    }
                    Mono.fx().alert().createError()
                            .setTitle("Error")
                            .setHeader("Invalid Server Configuration")
                            .setMessage(message)
                            .showAndWait();
                    Runtime.getRuntime().halt(0);
                });
                throw new RuntimeException("Cannot start services");
            }
            //------------------------------------------------------------------
            // Start SMS Service
            try {
                // start SMS Service
                this.smsManager.startService();
            } catch (Exception e) {
                //--------------------------------------------------------------
                Platform.runLater(() -> {
                    Mono.fx().alert().createError()
                            .setTitle("Error")
                            .setHeader("Cannot Start Services")
                            .setMessage("The server cannot start properly: " + e.getMessage())
                            .showAndWait();
                    Runtime.getRuntime().halt(0);
                });
                // make this task fail
                throw new RuntimeException("Cannot start services");
                //--------------------------------------------------------------
            }
        });
        //----------------------------------------------------------------------
        // Service Callback
        startServices.whenStarted(() -> {
            this.lbl_loading_message.setText("Starting Database Server");
            vbox_loading.setVisible(true);
            vbox_home.setVisible(false);
        });
        startServices.whenCancelled(() -> {
            // NO CALLBACK
        });
        startServices.whenFailed(() -> {
            // NO CALLBACK
        });
        //----------------------------------------------------------------------
        // On Task Success
        startServices.whenSuccess(() -> {
            // call service start success
            this.onServiceStartSuccess();
        });
        startServices.whenFinished(() -> {
            // no call back
        });

        startServices.start();
    }

    //--------------------------------------------------------------------------
    private void onServiceStartSuccess() {
        vbox_loading.setVisible(false);
        vbox_home.setVisible(true);
        //----------------------------------------------------------------------
        // Get Modem Information
        try {
            // Display Modem Information
            this.lbl_modem.setText(this.smsManager.getSmsGateWay().getManufacturer());
            this.lbl_model.setText(this.smsManager.getSmsGateWay().getModel() + " v" + this.smsManager.getSmsGateWay().getSwVersion());
            this.lbl_serial.setText(this.smsManager.getSmsGateWay().getSerialNo());
            this.lbl_message_center.setText(this.smsManager.getSmsGateWay().getSmscNumber());
            //------------------------------------------------------------------
            /**
             * Listen to incoming SMS Request.
             */
            this.messangingService();
            //------------------------------------------------------------------
        } catch (Exception e) {
            Platform.runLater(() -> {
                Mono.fx().alert().createError()
                        .setTitle("Error")
                        .setHeader("No Modem Informaion")
                        .setMessage("The server was unable to read modem information: " + e.getMessage())
                        .showAndWait();
                Runtime.getRuntime().halt(0);
            });
        }
    }

    /**
     * Error Tolerance Counter.
     */
    private static int ERROR_COUNTER = 0;
    private final static int ERROR_TOLLERANCE = 5;
    /**
     * Messaging Flag.
     */
    private boolean isRunning;

    /**
     * Messaging Service.
     */
    private void messangingService() {
        System.out.println("Messaging Service Started");
        isRunning = true;
        Thread th = new Thread(() -> {
            while (isRunning) {
                try {
                    //----------------------------------------------------------
                    // check database for query
                    SmsQueryMapping sms_map = Mono.orm().newSearch(Database.connect().sms_query())
                            .eq(DB.sms_query().status, 0)
                            .active(Order.asc(DB.sms_query().id))
                            .first();
                    //----------------------------------------------------------
                    // no sms for sending
                    if (sms_map == null) {
                        System.out.println("Nothing to send");
                    } else {
                        // if there is an sms get the details
                        String number = sms_map.getReciepients_number();
                        String message = sms_map.getMessage_body();
                        // update first the record before processing
                        sms_map.setActive(0);
                        sms_map.setStatus(1);
                        boolean updated = Database.connect().sms_query().update(sms_map);
                        //------------------------------------------------------
                        // if the update was failed do no proceed
                        if (!updated) {
                            System.out.println("Failed to update retrieving again");
                            continue; // continue
                        }
                        //------------------------------------------------------
                        // if the record was updated send the message
                        System.out.println("Sending Message");
                        /**
                         * Send Message.
                         */
                        OutboundMessage msg = this.smsManager.sendSMS(number, message);
                        //------------------------------------------------------
                        // After sending the message try to save it.
                        SmsLogMapping sms_log = MapFactory.map().sms_log();
                        sms_log.setGateway_id(msg.getGatewayId());
                        sms_log.setMessage_uuid(msg.getUuid());
                        sms_log.setDate(msg.getDate());
                        sms_log.setRecipient(msg.getRecipient());
                        sms_log.setDispatch_date(msg.getDispatchDate());
                        sms_log.setMessage_status(msg.getMessageStatus().name());
                        sms_log.setFailure_cause(msg.getFailureCause().name());
                        sms_log.setMessage_body(msg.getText());
                        sms_log.setPdu_data(msg.getPduUserData());
                        int res = Database.connect().sms_log().insert(sms_log);
                        if (res >= 1) {
                            System.out.println("Message Was Logged");
                        }
                        //------------------------------------------------------
                        System.out.println("Message Sent");
                    }
                } catch (Exception e) {
                    //----------------------------------------------------------
                    // Exception Handling
                    System.err.println("There Was An Exception: " + e.getMessage());
                    // Add Error Count
                    ERROR_COUNTER++;
                    // Try To Log this error for future reference.
                    ExceptionLogMapping logs = MapFactory.map().exception_log();
                    logs.setCount(ERROR_COUNTER);
                    logs.setMessage(e.getMessage());
                    int id = Database.connect().exception_log().insert(logs);
                    if (id >= 1) {
                        System.out.println("Exception Was Captured.");
                    }
                    //----------------------------------------------------------
                    // check if this error exceed the maximum tollerance if yes exit the application.
                    // this means that the application will not be able to recover.
                    if (ERROR_COUNTER >= ERROR_TOLLERANCE) {
                        // make the thread flag false
                        isRunning = false;
                        Platform.runLater(() -> {
                            Mono.fx().alert().createError()
                                    .setTitle("Error")
                                    .setHeader("Server Error")
                                    .setMessage("The server was not able to recover from a problem, please restart the application. (ERR: " + e.getMessage() + " )")
                                    .showAndWait();
                            // exit the application
                            Runtime.getRuntime().halt(0);
                        });
                    }
                    //----------------------------------------------------------
                    // Make The Thread Sleep for 3 seconds
                    // To give time for the thread to recover and see if it will have some changes.
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                        //
                    }
                    //----------------------------------------------------------
                    // After sleeping continue to next iteration
                    continue;
                } // end catch // end catch

                //--------------------------------------------------------------
                // add delay
                // this will not be executed when the thread caught up with an error.
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {

                }
                // this means that the thread was executed successfully and restart the counter
                ERROR_COUNTER = 0;
                //--------------------------------------------------------------
            }
        });
        th.setDaemon(false);
        th.setName("Messenger-Thread");
        th.setUncaughtExceptionHandler((Thread t, Throwable e) -> {
            //------------------------------------------------------------------
            Platform.runLater(() -> {
                Mono.fx().alert().createError()
                        .setTitle("Error")
                        .setHeader("Server Error")
                        .setMessage("The server encountered a problem. (ERR: " + e.getMessage() + " )")
                        .showAndWait();

                // close the program
                if (this.smsManager != null) {
                    try {
                        this.smsManager.stopService();
                    } catch (Exception ex) {
                        System.err.println("SMS Service was not shutdown properly.");
                    }
                }
                try {
                    Mono.orm().shutdown();
                } catch (Exception ex) {

                }
                Runtime.getRuntime().halt(0);
            });
            //------------------------------------------------------------------
        });
        th.start();
    }

    /**
     * When trying to close the stage.
     */
    private void onStageClosing() {
        int choice = Mono.fx()
                .alert().createConfirmation()
                .setTitle("Info")
                .setHeader("Shutdown Server ?")
                .setMessage("The server would not be able to send messages when closed. Do you want to continue ?")
                .confirmYesNo();

        if (choice == 1) {
            // close the program
            if (this.smsManager != null) {
                try {
                    this.smsManager.stopService();
                } catch (Exception e) {
                    System.err.println("SMS Service was not shutdown properly.");
                }
            }
            try {
                Mono.orm().shutdown();
            } catch (Exception e) {

            }
            Runtime.getRuntime().halt(0);
        }

    }

}

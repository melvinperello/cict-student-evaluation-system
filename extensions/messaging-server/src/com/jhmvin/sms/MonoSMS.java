package com.jhmvin.sms;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import org.smslib.AGateway;
import org.smslib.GatewayException;
import org.smslib.OutboundMessage;
import org.smslib.SMSLibException;
import org.smslib.Service;
import org.smslib.TimeoutException;
import org.smslib.modem.SerialModemGateway;

/**
 *
 * @author Jhon Melvin
 */
public class MonoSMS {

    public final static String WRONG_PORT_OR_NOT_RECOGNIZED = "Comm library exception: java.lang.RuntimeException: javax.comm.NoSuchPortException";
    /**
     * Sometimes appear when there is no SIM Card in the USB Modem.
     */
    public final static String SMS_EXCEPTION_NO_PDU_PROC = "The GSM modem does not support the PDU protocol.";
    /**
     * Sometimes appear when the device is not yet ready or just plugged in.
     */
    public final static String SMS_EXCEPTION_DEVICE_NOT_READY = "Comm library exception: java.lang.RuntimeException: javax.comm.UnsupportedCommOperationException: Win32 Comm Driver: Error 5";

    /**
     * Logger.
     *
     * @param message
     */
    private static void log(Object message) {
        System.out.println(String.valueOf(message));
    }

    // Modem Configuration
    private String modemID;
    private String comPort;
    private int bitRate;
    private String modemName;
    private String modemModel;
    private String modemPin;
    private String messageCenterNumber;

    public void setModemID(String modemID) {
        this.modemID = modemID;
    }

    public void setComPort(String comPort) {
        this.comPort = comPort;
    }

    public void setBitRate(int bitRate) {
        this.bitRate = bitRate;
    }

    public void setModemName(String modemName) {
        this.modemName = modemName;
    }

    public void setModemPin(String modemPin) {
        this.modemPin = modemPin;
    }

    public void setMessageCenterNumber(String messageCenterNumber) {
        this.messageCenterNumber = messageCenterNumber;
    }

    public void setModemModel(String modemModel) {
        this.modemModel = modemModel;
    }

    //--------------------------------------------------------------------------
    public MonoSMS() {
        // Constructor
    }
    //--------------------------------------------------------------------------
    private SerialModemGateway smsGateWay;

    /**
     * Returns the Modem Gateway.
     *
     * @return
     */
    public SerialModemGateway getSmsGateWay() {
        return smsGateWay;
    }

    /**
     * Return Service.
     *
     * @return
     */
    public Service getService() {
        return Service.getInstance();
    }

    //--------------------------------------------------------------------------
    /**
     * Starts the SMS Service before sending a message.
     *
     * @throws SMSLibException
     * @throws IOException
     * @throws TimeoutException
     * @throws GatewayException
     * @throws InterruptedException
     */
    public void startService() throws SMSLibException, IOException, TimeoutException, GatewayException, InterruptedException {
        this.smsGateWay = this.createGateWay();
        this.createSMSService(this.smsGateWay);
    }

    /**
     * Stopping the SMS Service thread when done finishing sending message. or
     * the program will be terminated.
     *
     * @throws SMSLibException
     * @throws IOException
     * @throws TimeoutException
     * @throws GatewayException
     * @throws InterruptedException
     */
    public void stopService() throws SMSLibException, IOException, TimeoutException, GatewayException, InterruptedException {
        Service.getInstance().stopService();
    }

    //--------------------------------------------------------------------------
    /**
     * Creates a SMS Gateway for the Modem.
     *
     * @return SerialModemGateway
     */
    public SerialModemGateway createGateWay() {
        SerialModemGateway gateway
                = new SerialModemGateway(this.modemID,
                        this.comPort,
                        this.bitRate,
                        this.modemName, this.modemModel);
        gateway.setInbound(true);
        gateway.setOutbound(true);
        gateway.setSimPin(this.modemPin);
        gateway.setSmscNumber(this.messageCenterNumber);
        return gateway;
    }

    /**
     * Creates the SMS Service.
     *
     * @param gateway
     * @throws SMSLibException
     * @throws IOException
     * @throws TimeoutException
     * @throws GatewayException
     * @throws InterruptedException
     */
    public void createSMSService(SerialModemGateway gateway) throws SMSLibException, IOException, TimeoutException, GatewayException, InterruptedException {
        // Set Outbound Notification
        Service.getInstance().setOutboundMessageNotification((AGateway ag, OutboundMessage om) -> {
            log("Outbound Notification Was Called !!!");
        });
        Service.getInstance().addGateway(gateway);
        Service.getInstance().startService();
    }

    /**
     * Sends the message.
     *
     * @param phoneNumber (+prefix) and the number
     * @param messageBody
     * @return
     * @throws TimeoutException
     * @throws GatewayException
     * @throws IOException
     * @throws InterruptedException
     */
    public OutboundMessage sendSMS(String phoneNumber, String messageBody) throws TimeoutException, GatewayException, IOException, InterruptedException {
        OutboundMessage msg = new OutboundMessage(phoneNumber, messageBody);
        Service.getInstance().sendMessage(msg);
        System.out.println(msg);
        return msg;
    }

}

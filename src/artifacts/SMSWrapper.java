/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artifacts;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.cict.PublicConstants;

/**
 * Wrapper Class for sending an SMS using MONOSYNC SMS SERVER.
 *
 * @author Jhon Melvin
 */
public class SMSWrapper {

    /**
     * SMS Server IP.
     */
    private final static String SMS_SERVER_IP = PublicConstants.getServerIP();
    /**
     * API Link.
     */
    private final static String SMS_API = "http://" + SMS_SERVER_IP + "/laravel/smsserver/public/sms/send";

    /**
     * Create a request for an SMS. NON-BLOCKING.
     *
     * @param to number starts with +63
     * @param message maximum of 160 characters
     * @param from (Optional just for logging)
     * @param response SENDING, FAILED, NO_REPLY
     */
    public static void send(String to, String message, String from, OnRequestSubmitted response) {
        Thread smsThread = new Thread(() -> {
            try {
                HttpResponse<JsonNode> jsonResponse = Unirest.post(SMS_API)
                        .field("number", to)
                        .field("message", message)
                        .field("sender", from)
                        .asJson();
                String reply = jsonResponse.getBody().getObject().getString("status");
                response.onGetResults(reply);
            } catch (UnirestException e) {
                response.onGetResults("NO_REPLY");
            }
        });
        smsThread.setName("SMS-Thread");
        smsThread.setDaemon(false);
        smsThread.start();
    }

    /**
     * Create a request for an SMS. BLOCKING.
     *
     * @param to number starts with +63
     * @param message maximum of 160 characters
     * @param from (Optional just for logging)
     * @return SENDING, FAILED, NO_REPLY
     */
    public static String send(String to, String message, String from) {
        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.post(SMS_API)
                    .field("number", to)
                    .field("message", message)
                    .field("sender", from)
                    .asJson();
            return jsonResponse.getBody().getObject().getString("status");
        } catch (UnirestException e) {
            return "NO_REPLY";
        }
    }

    /**
     * On submit Interface request handler.
     */
    @FunctionalInterface
    public interface OnRequestSubmitted {

        /**
         * Function caller.
         *
         * @param response
         */
        void onGetResults(String response);
    }
}

package com.jhmvin.system;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 *
 * @author Jhon Melvin
 */
public class SystemEnvironment {

    /**
     * GET the IP ADDRESSES and its MAC ADDRESSES
     *
     * @return
     */
    public String getIP() {
        try {
            String hostname = Inet4Address.getLocalHost().getHostName();
            InetAddress[] ips = Inet4Address.getAllByName(hostname); // all addresses
            String ip_string = "";
            for (InetAddress ip_add : ips) {
                if (ip_add.getHostAddress().contains(":")) {
                    // if ipv6 or default
                } else {
                    String ip = ip_add.getHostAddress().trim();
                    String mac = getMacAddress(ip_add);
                    ip_string += ("%" + ip + "@" + mac);
                }
            }
            return ip_string;

        } catch (Exception ex) {
            return "Unknown Address";
        }

    }

    private String getMacAddress(InetAddress ip_add) {
        try {
            NetworkInterface network = NetworkInterface.getByInetAddress(ip_add);
            byte[] mac = network.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            return sb.toString();
        } catch (Exception ex) {
            return "Unknown Mac Address";
        }

    }

    /**
     *
     * @return The Computer Name
     */
    public String getTerminal() {
        try {
            return Inet4Address.getLocalHost().getHostName();
        } catch (Exception ex) {

        }
        return "Unknown Host";
    }

    /**
     *
     * @return The Logged In User Many PC can have many users
     */
    public String getLoggedUser() {
        return System.getProperty("user.name", "Unknown User"); //platform independent 
    }

    /**
     * Returns the operating system version and architecture
     *
     * @return
     */
    public String getOS() {
        return System.getProperty("os.name", "Unknown OS") + "@" + System.getProperty("os.arch", "Unknown Architecture");
    }

    /**
     * Lists all available system properties
     */
    public void listEnvironment() {
        System.getProperties().list(System.out);
    }
}

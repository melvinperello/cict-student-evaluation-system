/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cict.configuration;

import com.melvin.java.properties.PropertyFile;
import java.util.Properties;

/**
 *
 * @author Jhon Melvin
 */
public class PublicConstants {

    public static String getServerIP() {
        ConfigurationManager.checkConfiguration();
        Properties propertyFile = PropertyFile.getPropertyFile(ConfigurationManager.CONNECTION_PROP.getAbsolutePath());
        return propertyFile.getOrDefault(ConfigurationManager.PROP_HOST_IP, "127.0.0.1").toString();

    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melvin.java.properties;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jhon Melvin
 */
public class PropertyManager {

    public static Properties gerPropertyFile(String propertyPath) {
        File propertyFile = new File(propertyPath);
        try {
            FileReader reader = new FileReader(propertyFile);
            Properties props = new Properties();
            props.load(reader);
            return props;
        } catch (FileNotFoundException ex) {
            // file was not found
            return null;
        } catch (IOException ex) {
            // cannot load property file
            return null;
        }
    }
}

/**
 * Just use if you want.
 */
package com.melvin.java.properties;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/**
 * A Wrapper class for easy usage of Java Properties.
 *
 * @author Jhon Melvin
 */
public class PropertyFile {

    /**
     * Checks if a property file is existing.
     *
     * @param propertyPath
     * @return
     */
    public static boolean exists(String propertyPath) {
        File propertyFile = new File(propertyPath);
        return java.nio.file.Files.exists(java.nio.file.Paths.get(propertyFile.getAbsolutePath()));
    }

    /**
     * Reads a property File.
     *
     * @param propertyPath
     * @return
     */
    public static Properties getPropertyFile(String propertyPath) {
        File propertyFile = new File(propertyPath);
        FileReader reader = null;
        try {
            reader = new FileReader(propertyFile);
            Properties props = new Properties();
            props.load(reader);
            reader.close();
            return props;
        } catch (FileNotFoundException ex) {
            // file was not found
            System.err.println("File Was Not Found: " + ex.getMessage());
            return null;
        } catch (IOException ex) {
            // cannot load property file
            System.err.println("Cannot Load Property File: " + ex.getMessage());
            return null;
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
//              
            }
        }
    }

    /**
     *
     * @param propertyPath
     * @param key
     * @param value
     * @param comment
     * @return
     */
    public static boolean writePropertyFile(String propertyPath, String key, String value, String comment) {
        File configFile = new File(propertyPath);
        Properties props = getPropertyFile(propertyPath);
        FileWriter writer = null;
        try {
            props.setProperty(key, value);
            writer = new FileWriter(configFile);
            props.store(writer, comment);
            writer.close();
            return true;
        } catch (IOException e) {
            System.err.println("Cannot Write to Property File: " + e.getMessage());
            return false;
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * Overloaded write property method with default comment.
     *
     * @param propertyPath
     * @param key
     * @param value
     * @return
     */
    public static boolean writePropertyFile(String propertyPath, String key, String value) {
        return PropertyFile.writePropertyFile(propertyPath, key, value, "Property File");
    }
}

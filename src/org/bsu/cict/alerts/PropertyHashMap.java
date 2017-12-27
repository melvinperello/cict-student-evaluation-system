package org.bsu.cict.alerts;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

/**
 * This class is intended to extend the capability of a hash map to store its
 * data in a java property file. This class has a load and save method.
 *
 * @author Jhon Melvin
 */
public class PropertyHashMap extends HashMap<String, String> {

    /**
     * loads a property file into the hash map.
     *
     * @param path
     * @throws IOException
     */
    public void load(String path) throws IOException {
        FileInputStream reader = null;
        try {
            // fail fast throw exception if file was not loaded
            reader = new FileInputStream(path);
            Properties properties = new Properties();
            properties.load(reader);
            // clear contents if there are any
            this.clear();
            // load data in hashmap.
            properties.keySet().forEach(key -> {
                String keyName = String.valueOf(key);
                this.put(keyName, properties.getProperty(keyName));
            });
        } finally {
            // close the reader
            if (reader != null) {
                reader.close();
            }
        }
    }

    public void save(String path) {
        
    }
}

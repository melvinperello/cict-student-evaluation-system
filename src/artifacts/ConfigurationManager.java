/**
 * CAPSTONE PROJECT.
 * BSIT 4A-G1.
 * MONOSYNC TECHNOLOGIES.
 * MONOSYNC FRAMEWORK VERSION 1.0.0 TEACUP RICE ROLL.
 * THIS PROJECT IS PROPRIETARY AND CONFIDENTIAL ANY PART THEREOF.
 * COPYING AND DISTRIBUTION WITHOUT PERMISSION ARE NOT ALLOWED.
 *
 * COLLEGE OF INFORMATION AND COMMUNICATIONS TECHNOLOGY.
 * LINKED SYSTEM.
 *
 * PROJECT MANAGER: JHON MELVIN N. PERELLO
 * DEVELOPERS:
 * JOEMAR N. DELA CRUZ
 * GRETHEL EINSTEIN BERNARDINO
 *
 * OTHER LIBRARIES THAT ARE USED BELONGS TO THEIR RESPECTFUL OWNERS AND AUTHORS.
 * NO COPYRIGHT ARE INTENTIONAL OR INTENDED.
 * THIS PROJECT IS NOT PROFITABLE HENCE FOR EDUCATIONAL PURPOSES ONLY.
 * THIS PROJECT IS ONLY FOR COMPLIANCE TO OUR REQUIREMENTS.
 * THIS PROJECT DOES NOT INCLUDE DISTRIBUTION FOR OTHER PURPOSES.
 *
 */
package artifacts;

import com.jhmvin.propertymanager.PropertyManager;
import com.melvin.java.properties.PropertyFile;
import java.io.File;
import java.util.Properties;

/**
 * This class is intended in creating the configuration folder and the
 * properties especially for IP Address
 *
 * @author Jhon Melvin
 */
public class ConfigurationManager {

    public final static File CONFIGURATION_DIR = new File("configuration");
    public final static File EVALUATION_PROP = new File("configuration/evaluation.properties");

    //--------------------------------------------------------------------------
    public final static String PROP_HOST_IP = "hostIP";

    private static boolean checkConfigurationFile() {
        if (!CONFIGURATION_DIR.exists()) {
            try {
                boolean folder_created = CONFIGURATION_DIR.mkdirs();
                if (folder_created) {
                    boolean file_created = EVALUATION_PROP.createNewFile();
                    return file_created;
                }
            } catch (Exception e) {
                return false;
            }
        } else {
            // already exist
            if (!EVALUATION_PROP.exists()) {
                try {
                    boolean file_created = EVALUATION_PROP.createNewFile();
                    return file_created;
                } catch (Exception e) {
                   
                    return false;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if configuration file is existing then create and add default
     * values if not existing.
     */
    public static void checkConfiguration() {
        boolean config_ready = ConfigurationManager.checkConfigurationFile();
        if (config_ready) {
            Properties property = PropertyFile.getPropertyFile(EVALUATION_PROP.getAbsolutePath());
            String val = property.getProperty(PROP_HOST_IP);
            if (val == null) {
                PropertyFile.writePropertyFile(EVALUATION_PROP.getAbsolutePath(), PROP_HOST_IP, "127.0.0.1");
            }
        }
    }
}

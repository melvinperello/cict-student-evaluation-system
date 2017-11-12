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

import java.io.InputStream;

/**
 * Class dedicated for managing resource files. any methods or functionality
 * that is related in managing and any operations that are related in the SRC
 * Management must be added here.
 *
 * @author Jhon Melvin
 */
public class ResourceManager {

    /**
     * Fetch A File from the SRC Folder. the SRC Folder is not visible anymore
     * upon build. this is the proper way to access the SRC Folder during build
     * time.
     *
     * @param classloader the
     * @param relativePath relative path home Path is SRC therefore you do not
     * need to include src/dir/dir.
     * @return
     */
    public static java.net.URL fetchFromResource(Class classloader, String relativePath) {
        return classloader.getClassLoader().getResource(relativePath);
    }

    /**
     * Fetching the resource as stream.
     *
     * @param classloader
     * @param relativePath
     * @return
     */
    public static InputStream fetchFromResourceAsStream(Class classloader, String relativePath) {
        return classloader.getClassLoader().getResourceAsStream(relativePath);
    }

}

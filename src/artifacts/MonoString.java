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

import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Jhon Melvin
 */
public class MonoString extends StringUtils {

    public final static String DELIMITERS = "!@#%&*/";

    /**
     * Removes spaces between strings
     *
     * @param str The string to clean
     * @return returns filtered string
     */
    public static String removeExtraSpace(String str) {
        String[] words = str.split(" ");
        String temp = "";
        for (String word : words) {
            if (!word.isEmpty()) {
                temp += word.concat(" ");
            }
        }
        return temp.trim();
    }

    /**
     * ADDED: 9/27/2017 For username purposes, etc. Removes all the spaces on
     * the string.
     *
     * @param str the string to be filtered
     * @return space free string
     */
    public static String removeSpaces(String str) {
        String[] words = str.split(" ");
        String temp = "";
        for (String word : words) {
            if (!word.isEmpty()) {
                temp += word;
            }
        }
        return temp.trim();
    }

    /**
     * Creates an array of words from a string separated by commas.
     *
     * @param str
     * @return
     */
    public static ArrayList<String> commaSeparator(String str) {
        ArrayList<String> wordlist = new ArrayList<>();
        String[] words = str.split(",");
        for (String word : words) {
            wordlist.add(removeExtraSpace(word));
        }
        return wordlist;
    }
}

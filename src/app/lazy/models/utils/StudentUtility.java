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
package app.lazy.models.utils;

import app.lazy.models.StudentProfileMapping;
import java.util.Locale;

/**
 *
 * @author Jhon Melvin
 */
public class StudentUtility {

    /**
     * using the new field as address reference.
     *
     * @param profile
     * @return
     */
    public static String getStudentAddress(StudentProfileMapping profile) {
        String address = "";
        //----------------------------------------------------------------------
        address += concat(profile.getStudent_address());
        //----------------------------------------------------------------------
        address += concat(" " + profile.getZipcode());
        //----------------------------------------------------------------------
        return address;
    }

    public static String concat(String str) {
        if (str == null) {
            return "";
        } else {
            return str.trim().toUpperCase(Locale.ENGLISH);
        }
    }

    private static String retrieveAddressUsingOldFormat(StudentProfileMapping profile) {
        //----------------------------------------------------------------------
        // Get Values
        String hNum = profile.getHouse_no(),
                brgy = profile.getBrgy(),
                city = profile.getCity(),
                province = profile.getProvince();
        String postalCode = profile.getZipcode();
        //----------------------------------------------------------------------
        String address = "";
        //----------------------------------------------------------------------
        address += concat(hNum);
        //----------------------------------------------------------------------
        address += concat(" " + brgy);
        //----------------------------------------------------------------------
        address += concat(" " + city);
        //----------------------------------------------------------------------
        address += concat(" " + province);
        //----------------------------------------------------------------------
        address += concat(" " + postalCode);
        //----------------------------------------------------------------------
        return address;
    }
}

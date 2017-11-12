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
package org.cict.reports;

import java.io.File;
import static org.cict.reports.advisingslip.AdvisingSlip.SAVE_DIRECTORY;

/**
 *
 * @author Jhon Melvin
 */
public class ReportsDirectory {

    //------------------------
    public static String REPORTS_DIR_IMAGES = "org/cict/reports/images/";
    public static String REPORTS_DIR_MAIN = "org/cict/reports/";
    public static String DEFAULT_IMAGE2x2 = REPORTS_DIR_IMAGES + "checklist/default2x2.png";
    public static String DEFAULT_IMAGE1x1 = REPORTS_DIR_IMAGES + "profile/default1x1.png";
    
    
    public static boolean check(String reportPath) {
        try {
            File directory = new File(reportPath);
            if (!directory.exists()) {
                System.out.println("Directory Not Exist for " + reportPath);
                boolean dir_created = directory.mkdirs();
                if (dir_created) {
                    System.out.println("Created Directory " + reportPath);
                    return true; // directory was created.
                } else {
                    System.err.println("Cannot Create Directory.");
                    return false; // cannot create the directory
                }
            } else {
                // existing already
                return true; // the directory is already created.
            }
        } catch (Exception e) {
            System.out.println(reportPath + " DIRECTORY CREATION FAILED.");
            return false; // error in creating directory
        }
    }
}

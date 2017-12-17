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
package org.bsu.cict.tools;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.CodeSource;
import javax.swing.JOptionPane;

/**
 *
 * @author Jhon Melvin
 */
public class BackUpAndRestore {

    public static void main(String[] args) {
        Backupdbtosql();
    }

    public static void Backupdbtosql() {

        try {

            /*NOTE: Getting path to the Jar file being executed*/
 /*NOTE: YourImplementingClass-> replace with the class executing the code*/
            CodeSource codeSource = BackUpAndRestore.class.getProtectionDomain().getCodeSource();
            File jarFile = new File(codeSource.getLocation().toURI().getPath());
            String jarDir = jarFile.getParentFile().getPath();


            /*NOTE: Creating Database Constraints*/
            String dbName = "cictems";
            String dbUser = "root";
            String dbPass = "root";

            /*NOTE: Creating Path Constraints for folder saving*/
 /*NOTE: Here the backup folder is created for saving inside it*/
            String folderPath = jarDir + "\\backup";

            /*NOTE: Creating Folder if it does not exist*/
            File f1 = new File(folderPath);
            f1.mkdir();

            /*NOTE: Creating Path Constraints for backup saving*/
 /*NOTE: Here the backup is saved in a folder called backup with the name backup.sql*/
            String savePath = "\"" + jarDir + "\\backup\\" + "backup.sql\"";
            savePath = "C:\\Users\\Jhon Melvin\\Desktop\\asd\\backup.sql";
            /*NOTE: Used to create a cmd command*/
            String executeCmd = "mysqldump -u" + dbUser + " -p" + dbPass + " --database " + dbName + " -r " + savePath;

            /*NOTE: Executing the command here*/
            Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();

            /*NOTE: processComplete=0 if correctly executed, will contain other values if not*/
            if (processComplete == 0) {
                System.out.println("Backup Complete");
            } else {
                System.out.println("Backup Failure");
            }

        } catch (URISyntaxException | IOException | InterruptedException ex) {
            ex.printStackTrace();
           // JOptionPane.showMessageDialog(null, "Error at Backuprestore" + ex.getMessage());
        }
    }
}

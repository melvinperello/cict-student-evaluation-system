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

import com.jhmvin.commandline.CommandLine;
import java.io.File;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.ArrayList;

/**
 *
 * @author Jhon Melvin
 */
public class BackUpAndRestore {

    public static void main(String[] args) {
//        int res = BackUpAndRestore.backup(
//                "127.0.0.1",
//                "root",
//                "root",
//                "cictems",
//                "D:\\12_17_2017_04_51_21_PM.monosync"
//        );
//        // 0 for success
//        // 1 for path error
//        // 2 for SQL error
//        System.out.println("DATABASE BACKUP RESULT: " + res);

        int restoreRes = BackUpAndRestore.restore(
                "127.0.0.1",
                "root",
                "root",
                "D:\\12_17_2017_04_51_21_PM.monosync"
        );

        System.out.println("REStOrE RESULT: " + restoreRes);

    }

    /**
     * Get the Jar absolute location
     *
     * @return
     * @throws URISyntaxException
     */
    private static String getJarPath() throws URISyntaxException {
        CodeSource codeSource = BackUpAndRestore.class.getProtectionDomain().getCodeSource();
        File jarFile = new File(codeSource.getLocation().toURI().getPath());
        String jarDir = jarFile.getParentFile().getParentFile().getPath();
        System.out.println("JAR LOCATION: " + jarDir);
        return jarDir;
    }

    /**
     * Backup the current data.
     *
     * @param host the database server can be local host or remote host.
     * @param user
     * @param password
     * @param database database name
     * @param savePath where to save the file
     * @return 0 for success 1 for invalid path 2 for SQL error.
     */
    public static int backup(
            String host,
            String user,
            String password,
            String database,
            String savePath
    ) {
        try {
            String jarDir = BackUpAndRestore.getJarPath();

            ArrayList<String> commands = new ArrayList<>();
            commands.add("pushd " + jarDir);
            commands.add("cd " + "maria-tools");
            commands.add("mysqldump.exe --host=" + host + " --user=" + user + " --password=" + password + " --add-drop-database  --databases " + database + " > " + savePath);

            String a = CommandLine.multipleCommand(commands);
            ArrayList<String> result = CommandLine.run(a);
            if (result.size() == 1) {
                if (result.get(0).equalsIgnoreCase("Exit Code: 0")) {
                    return 0; // success
                }
            }
        } catch (URISyntaxException e) {
            return 1; // uri syntax
        }
        return 2; // others
    }

    /**
     *
     * @param host Host on where to restore the data.
     * @param user
     * @param pass
     * @param backUpLocation location of the backup file.
     * @return
     */
    public static int restore(
            String host,
            String user,
            String pass,
            String backUpLocation
    ) {
        try {
            String jarDir = BackUpAndRestore.getJarPath();

            ArrayList<String> commands = new ArrayList<>();
            commands.add("pushd " + jarDir);
            commands.add("cd " + "maria-tools");
            commands.add("mysql.exe --host " + host + " --user=" + user + " --password=" + pass + " < " + backUpLocation);

            String a = CommandLine.multipleCommand(commands);
            ArrayList<String> result = CommandLine.run(a);
            if (result.size() == 1) {
                if (result.get(0).equalsIgnoreCase("Exit Code: 0")) {
                    return 0; // success
                }
            }
        } catch (URISyntaxException e) {
            return 1; // uri syntax
        }
        return 2; // others
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.monosync.sms.install;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 *
 * @author Jhon Melvin
 */
public class RequiredFiles {

    /**
     * The following are required extensions and should be placed in the JRE
     * Home directory. The following extensions are fully compatible with 32-bit
     * Java, but having issues with 64-bit Java. the following paths are
     * relative to the location of the JRE Folder.
     */
    // Java Communication Files
    public final static String COMM_JAR = "/lib/ext/comm.jar";
    public final static String JAVAX_COMM_PROPERTIES = "/lib/javax.comm.properties";
    public final static String WIN_32_COM_DLL = "/bin/win32com.dll";
    // RXTX Extensions
    public final static String RXTX_COMM_JAR = "/lib/ext/RXTXcomm.jar";
    public final static String RXTX_SERIAL_DLL = "/bin/rxtxSerial.dll";
    public final static String RXTX_PARALLEL_DLL = "/bin/rxtxParallel.dll";

    /**
     * Source File where the files where be taken form and be installed in the
     * JRE folder.
     */
    // Library Directory
    public final static String SRC_COMM_JAR = "extensions/comm/comm.jar";
    public final static String SRC_JAVAX_COMM_PROPERTIES = "extensions/comm/javax.comm.properties";
    public final static String SRC_WIN_32_COM_DLL = "extensions/comm/win32com.dll";
    public final static String SRC_RXTX_COMM_JAR = "extensions/rxtx/RXTXcomm.jar";
    public final static String SRC_RXTX_SERIAL_DLL = "extensions/rxtx/rxtxSerial.dll";
    public final static String SRC_RXTX_PARALLEL_DLL = "extensions/rxtx/rxtxParallel.dll";

    /**
     *
     * @param filePath
     * @return
     */
    public static String deleteFile(String filePath) {
        File tempFile = new File(filePath);
        if (!checkExistence(filePath)) {
            return "File Not Existing";
        }
        if (!Files.isRegularFile(Paths.get(tempFile.getAbsolutePath()))) {
            System.out.println("NOT FILE");
            return "Not A File";
        }
        try {
            Files.delete(Paths.get(tempFile.getAbsolutePath()));
            return "Deleted";
        } catch (Exception e) {
            return "No Permission";
        }
    }

    /**
     *
     * @param filePath
     * @return
     */
    public static boolean checkExistence(String filePath) {
        File tempFile = new File(filePath);
        if (!Files.exists(Paths.get(tempFile.getAbsolutePath()))) {
            return false;
        }
        return true;
    }

    public static String copyFile(String source, String destination) {
        File sourceFile = new File(source);
        File targetFile = new File(destination);
        Path sourceFilePath = Paths.get(sourceFile.getAbsolutePath());
        Path targetFilePath = Paths.get(targetFile.getAbsolutePath());
        try {
            Files.copy(sourceFilePath, targetFilePath,
                    StandardCopyOption.REPLACE_EXISTING);
            return "OK";
        } catch (IOException ex) {
            return "No Permission";
        }
    }

}

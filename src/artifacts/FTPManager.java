/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artifacts;

import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.SystemVariablesMapping;
import com.jhmvin.Mono;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

/**
 *
 * @author Jhon Melvin
 */
public class FTPManager {

    /**
     * Server Configuration.
     */
    private final static String FTP_SERVER = "127.0.0.1";
    private final static int FTP_PORT = 21;
    private final static String FTP_USER = "ftp-cict";
    private final static String FTP_PASS = "123456";

    /**
     * Creates a FTP Connection.
     *
     * @return
     * @throws IOException
     */
    private static FTPClient establishConnection() throws IOException {
        //---------------------------------
        SystemVariablesMapping ftpPort = Mono.orm().newSearch(Database.connect().system_variables())
                .eq(DB.system_variables().name, "FTP_PORT").active().first();
        SystemVariablesMapping ftpUser = Mono.orm().newSearch(Database.connect().system_variables())
                .eq(DB.system_variables().name, "FTP_USERNAME").active().first();
        SystemVariablesMapping ftpPass = Mono.orm().newSearch(Database.connect().system_variables())
                .eq(DB.system_variables().name, "FTP_PASSWORD").active().first();
        SystemVariablesMapping ftpServer = Mono.orm().newSearch(Database.connect().system_variables())
                .eq(DB.system_variables().name, "FTP_SERVER").active().first();
        //-----------------------------------

        FTPClient ftpClient = new FTPClient();
        ftpClient.connect((ftpServer.getValue() == null ? FTP_SERVER : ftpServer.getValue()), (ftpPort.getValue() == null ? FTP_PORT : Integer.parseInt(ftpPort.getValue())));
        ftpClient.login((ftpUser.getValue() == null ? FTP_USER : ftpUser.getValue()), (ftpPass.getValue() == null ? FTP_PASS : ftpPass.getValue()));
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        return ftpClient;
    }

    //--------------------------------------------------------------------------
    public interface OnTransfer {

        void transferResult(boolean result, Exception exception);
    };

    /**
     * NON-BLOCKING Upload. Runs the upload method on a separate thread so that
     * when the file is large and network is slow the program won't hang.
     *
     * @param uploadAbsolutePath
     * @param ftpFolder
     * @param ftpFileName
     * @param transferResults
     */
    public final static void upload(String uploadAbsolutePath, String ftpFolder, String ftpFileName, OnTransfer transferResults) {
        Thread uploadThread = new Thread(() -> {
            try {
                boolean result = FTPManager.upload(uploadAbsolutePath, ftpFolder, ftpFileName);
                transferResults.transferResult(result, null);
            } catch (Exception e) {
                transferResults.transferResult(false, e);
            }
        });
        uploadThread.setName("FTP-Upload-Thread");
        uploadThread.setDaemon(true);
        uploadThread.start();
    }

    /**
     * NON-BLOCKING Download.Runs the download method on a separate thread so
     * that when the file is large and network is slow the program won't hang.
     *
     * @param ftpFolder
     * @param ftpFileName
     * @param downloadPath
     * @param transferResults
     */
    public final static void download(String ftpFolder, String ftpFileName, String downloadPath, OnTransfer transferResults) {
        Thread downloadThread = new Thread(() -> {
            try {
                boolean results = FTPManager.download(ftpFolder, ftpFileName, downloadPath);
                transferResults.transferResult(results, null);
            } catch (Exception e) {
                transferResults.transferResult(false, e);
            }
        });

        downloadThread.setName("FTP-Download-Thread");
        downloadThread.setDaemon(true);
        downloadThread.start();
    }

    /**
     * This is a sample code ignore this one.
     */
    public void sampleUsage() {
        //----------------------------------------------------------------------
        // Do Some Preparations here
        // add loading or disable buttons + loading cursor
        //----------------------------------------------------------------------
        // start the transfer on another thread
        FTPManager.download("FTP FOLDER", "FILE NAME", "DOWNLOAD PATH", (boolean result, Exception e) -> {
            // do something after the operation
            // result is the result of the transfer true or false
            if (result) {
                // success
                // when success exception is null
            } else {
                // when failed
                // there may be an exception or not
            }
        });
        // do not add any codes after because the above method is non blocking
    }

    //--------------------------------------------------------------------------
    /**
     * Upload File to the FTP Server. (BLOCKING)
     *
     * @param uploadAbsolutePath The location of the file in the user's
     * computer.
     * @param ftpFolder The Folder from the server
     * @param ftpFileName The file name that will be save in the server
     * @return
     * @throws IOException
     */
    public final static boolean upload(String uploadAbsolutePath, String ftpFolder, String ftpFileName) throws IOException {
        // Connection Entry.
        FTPClient ftpClient = FTPManager.establishConnection();
        // Get The File to be uploaded.
        File fileToUpload = new File(uploadAbsolutePath);
        // Save file information in the FTP Server
        String fileInFTP = ftpFolder + "/" + ftpFileName;
        // Convert the file into an Input Stream.
        InputStream fileToUploadStream = new FileInputStream(fileToUpload);
        // Start Upload Process
        System.out.println("FTP MAN: Starting to upload file . . .");
        boolean done = ftpClient.storeFile(fileInFTP, fileToUploadStream);
        fileToUploadStream.close();
        return done;
    }

    /**
     * Downloads a file from the server (BLOCKING)
     *
     * @param ftpFolder FTP server Folder
     * @param ftpFileName The File Name from FTP
     * @param downloadPath Where to save the file in the user's Computer
     * @return
     * @throws IOException
     */
    public final static boolean download(String ftpFolder, String ftpFileName, String downloadPath) throws IOException {
        FTPClient ftpClient = FTPManager.establishConnection();
        // Location and file name of the file from the Server
        String remoteFile = "/" + ftpFolder + "/" + ftpFileName;
        // create a file on where to save the file
        File whereToDownload = new File(downloadPath);
        // create download stream
        OutputStream downloadStream = new BufferedOutputStream(new FileOutputStream(whereToDownload));
        // Start Download Process
        System.out.println("FTP MAN: Starting to download file . . .");
        boolean downloaded = ftpClient.retrieveFile(remoteFile, downloadStream);
        downloadStream.close();
        return downloaded;
    }

}

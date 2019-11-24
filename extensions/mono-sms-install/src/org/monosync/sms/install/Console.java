/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.monosync.sms.install;

import java.io.File;
import java.util.Scanner;

/**
 *
 * @author Jhon Melvin
 */
public class Console {

    private void write(Object message) {
        System.out.println(String.valueOf(message));
    }

    private String read() {
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    public Console() {
        header();
        System.out.print("Directory: ");
        String directory = read();
        File checkDir = new File(directory);
        if (!checkDir.exists()) {
            write("Directory Not Existing !");
            return;
        }

        if (!checkDir.isDirectory()) {
            write("Not A Directory !");
            return;
        }

        write("----------------------------------------------------------------------");
        deleteFiles(directory);
        write("----------------------------------------------------------------------");
        copyFiles(directory);
        write("----------------------------------------------------------------------");
        checkExistence(directory);
    }

    private void header() {
        write("----------------------------------------------------------------------");
        write("|      JAVA SMS EXTENSIONS INSTALLER | jhmvinperello@gmail.com       |");
        write("----------------------------------------------------------------------");
        write("| This script copies all the required extension for sending an sms   |");
        write("| using USB GSM Modem with Java.                                     |");
        write("----------------------------------------------------------------------");
    }

    private void deleteFiles(String directory) {
        write("Deleting Existing Files");
        write("----------------------------------------------------------------------");
        writeDeleteResults("1", directory, RequiredFiles.COMM_JAR);
        writeDeleteResults("2", directory, RequiredFiles.JAVAX_COMM_PROPERTIES);
        writeDeleteResults("3", directory, RequiredFiles.WIN_32_COM_DLL);
        writeDeleteResults("4", directory, RequiredFiles.RXTX_COMM_JAR);
        writeDeleteResults("5", directory, RequiredFiles.RXTX_SERIAL_DLL);
        writeDeleteResults("6", directory, RequiredFiles.RXTX_PARALLEL_DLL);
    }

    private void copyFiles(String directory) {
        writeCopyResults("1", RequiredFiles.SRC_COMM_JAR, directory, RequiredFiles.COMM_JAR);
        writeCopyResults("2", RequiredFiles.SRC_JAVAX_COMM_PROPERTIES, directory, RequiredFiles.JAVAX_COMM_PROPERTIES);
        writeCopyResults("3", RequiredFiles.SRC_WIN_32_COM_DLL, directory, RequiredFiles.WIN_32_COM_DLL);
        writeCopyResults("4", RequiredFiles.SRC_RXTX_COMM_JAR, directory, RequiredFiles.RXTX_COMM_JAR);
        writeCopyResults("5", RequiredFiles.SRC_RXTX_SERIAL_DLL, directory, RequiredFiles.RXTX_SERIAL_DLL);
        writeCopyResults("6", RequiredFiles.SRC_RXTX_PARALLEL_DLL, directory, RequiredFiles.RXTX_PARALLEL_DLL);
    }

    private void checkExistence(String directory) {
        write("Checking File Extensions");
        write("----------------------------------------------------------------------");
        writeExistResults("1", directory, RequiredFiles.COMM_JAR);
        writeExistResults("2", directory, RequiredFiles.JAVAX_COMM_PROPERTIES);
        writeExistResults("3", directory, RequiredFiles.WIN_32_COM_DLL);
        writeExistResults("4", directory, RequiredFiles.RXTX_COMM_JAR);
        writeExistResults("5", directory, RequiredFiles.RXTX_SERIAL_DLL);
        writeExistResults("6", directory, RequiredFiles.RXTX_PARALLEL_DLL);

    }

    private void writeCopyResults(String number, String src, String directory, String file) {
        write("[ " + number + " / 6 ] Copying\t" + file + "\t\t->\t" + RequiredFiles.copyFile(src, directory + file));
    }

    private void writeExistResults(String number, String directory, String file) {
        write("[ " + number + " / 6 ] Checking\t" + file + "\t\t->\t" + (RequiredFiles.checkExistence(directory + file) ? "OK" : "MISSING"));
    }

    private void writeDeleteResults(String number, String directory, String file) {
        write("[ " + number + " / 6 ] Deleting\t" + file + "\t\t->\t" + RequiredFiles.deleteFile(directory + file));
    }

    public static void main(String[] args) {
        new Console();
    }
}

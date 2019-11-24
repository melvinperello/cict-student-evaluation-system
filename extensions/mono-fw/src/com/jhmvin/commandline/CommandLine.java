/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jhmvin.commandline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jhon Melvin
 */
public class CommandLine {

    /**
     * Runs a Windows Command Line Command.
     *
     * @param cli sing line command.
     * @return ArrayList of results.
     */
    public static ArrayList<String> run(String cli) {
        // ResultString Holder
        ArrayList<String> cmdOutput = new ArrayList<>();
        // Creates the process
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", cli);
        // redirect the error to standard input stream
        processBuilder.redirectErrorStream(true);
        // start the process
        Process cmdProcess;
        try {
            cmdProcess = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(cmdProcess.getInputStream()));

            reader.lines().forEach(resLine -> {
                cmdOutput.add(resLine);
            });

            cmdOutput.add("Exit Code: " + cmdProcess.exitValue());
        } catch (IOException ex) {
            // no results
        }
        return cmdOutput;
    }

    public static String multipleCommand(ArrayList<String> commands) {
        Iterator<String> commandIterate = commands.iterator();
        String command = "";
        while (commandIterate.hasNext()) {
            String currentLine = commandIterate.next();
            command += currentLine;

            if (commandIterate.hasNext()) {
                command += " & ";
            }
        }
        return command;
    }

    public static void startCLI(String command) {

        try {
            Runtime.getRuntime().exec("cmd /c start " + command);

        } catch (IOException ex) {

        }

    }

}

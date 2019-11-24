/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jhmvin.orm.lazy;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Jhon Melvin
 */
public class TextWriter {

    private void log(Object message) {
        System.out.println("@TextWriter: " + message.toString());
    }

    private final String filename;
    private FileWriter fileWriter;
    private BufferedWriter bufferWriter;

    public TextWriter(String filename) {
        this.filename = filename;
    }

    public TextWriter forAppend() {
        try {
            fileWriter = new FileWriter(filename, true);
            bufferWriter = new BufferedWriter(fileWriter);
        } catch (IOException ex) {
            log("@forAppend: " + ex.getMessage());
        }
        return this;
    }

    public TextWriter forRewrite() {
        try {
            fileWriter = new FileWriter(filename, false);
            bufferWriter = new BufferedWriter(fileWriter);
        } catch (IOException ex) {
            log("@forRewrite: " + ex.getMessage());
        }
        return this;
    }

    public TextWriter write(String text) {
        try {
            bufferWriter.write(text);
        } catch (IOException ex) {
            log("@write: " + ex.getMessage());
        }
        return this;
    }

    public TextWriter writeln(String text) {
        return this.write("\n" + text);
    }

    public TextWriter writern(String text) {
        return this.write(text + "\n");
    }

    public void close() {
        try {
            bufferWriter.close();
            fileWriter.close();
        } catch (IOException ex) {
            log("@close: " + ex.getLocalizedMessage());
        }
    }

    public TextWriter flush() {
        try {
            bufferWriter.flush();
            fileWriter.flush();
        } catch (IOException ex) {
            log("@flush: " + ex.getLocalizedMessage());
        }

        return this;
    }

}

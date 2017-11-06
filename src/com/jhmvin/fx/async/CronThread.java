/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jhmvin.fx.async;

import javafx.application.Platform;

/**
 *
 * @author Jhon Melvin
 */
public class CronThread {

    private final String name;
    private Thread cron;
    private boolean running;
    private long interval;

    //
    private boolean isStarted;

    public CronThread(String name) {
        if (name == null) {
            this.name = "";
        } else {
            this.name = name;
        }
        running = true;

        //
        isStarted = false;
    }

    /**
     * Set the event to do.
     *
     * @param forever
     */
    public void setTask(CronEvent forever) {

        this.cron = new Thread(() -> {
            while (running) {
                try {
                    //
                    forever.forever();
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    //
                }
            }
        });

        this.cron.setName("Cron-Thread: " + this.name);
        this.cron.setDaemon(true);
    }

    /**
     * Interval per execution
     *
     * @param interval
     */
    public void setInterval(long interval) {
        this.interval = interval;
    }

    /**
     * Starts the task.
     */
    public void start() {
        if (this.isStarted) {
            System.err.println("Cron-Thread: " + this.name + " >> Cannot start Thread, Thread is already running.");
            return;
        }

        this.running = true;
        this.cron.start();
        // mark as started.
        this.isStarted = true;
    }

    /**
     * Stops the task.
     */
    public void stop() {
        this.running = false;
    }

}

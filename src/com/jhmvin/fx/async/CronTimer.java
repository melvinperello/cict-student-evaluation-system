/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jhmvin.fx.async;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Jhon Melvin
 */
public class CronTimer {

    private final Timer cronScheduler;
    private long interval;
    private CronEvent event;
    private boolean running;
    private final String name;

    private boolean isStarted;

    public CronTimer(String name) {
        if (name == null) {
            this.name = "";
        } else {
            this.name = name;
        }
        running = true;
        cronScheduler = new Timer("Cron-Timer: " + this.name, true);

        //
        isStarted = false;
    }

    public void setTask(CronEvent event) {
        this.event = event;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public void start() {
        if (isStarted) {
            System.err.println("Cron-Timer: " + this.name + " >> Cannot start timer, timer is already running.");
            return;
        }

        this.cronScheduler.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (running) {

                    event.forever();

                }

            }
        }, 0, interval);
        isStarted = true;
    }

    /**
     * Stops the task.
     */
    public void stop() {
        this.cronScheduler.cancel();
    }

    /**
     * Pause the task.
     */
    public void pause() {
        this.running = false;
    }

    /**
     * Resumes the timer task.
     */
    public void resume() {
        this.running = true;
    }

}

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
package com.jhmvin.fx.async;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

/**
 *
 * @author Jhon Melvin
 */
public class SimpleTask {

    private Task<Void> task;
    private final String taskName;

    /**
     * Worker events.
     */
    private EventHandler<WorkerStateEvent> onCancel;
    private EventHandler<WorkerStateEvent> onFailure;
    private EventHandler<WorkerStateEvent> onRunning;
    private EventHandler<WorkerStateEvent> onSchedule;
    private EventHandler<WorkerStateEvent> onSuccess;
    // extended event
    private SimpleEvent onFinished = () -> {
        // default event;
    };

    private Exception taskException;

    public Exception getTaskException() {
        return taskException;
    }

    public SimpleTask(String name) {
        this.taskName = name;
    }

    public void setTask(Runnable whatToDo) {
        this.task = new Task<Void>() {
            @Override
            protected Void call() {
                // task to do
                try {
                    whatToDo.run();
                } catch (Exception e) {
                    taskException = e;
                    System.err.println("SimpleTask-Call-Error");
                    //e.printStackTrace();
                    throw new RuntimeException("SimpleTask-Call-Error");
                }
                return null;
            }
        };
    }

    public void setOnStart(EventHandler<WorkerStateEvent> start) {
        this.onSchedule = start;
    }

    public void setOnCancelled(EventHandler<WorkerStateEvent> cancel) {
        this.onCancel = cancel;
    }

    public void setOnFailed(EventHandler<WorkerStateEvent> fail) {
        this.onFailure = fail;
    }

    public void setOnRunning(EventHandler<WorkerStateEvent> running) {
        this.onRunning = running;
    }

    public void setOnSuccess(EventHandler<WorkerStateEvent> success) {
        this.onSuccess = success;
    }

    /**
     * When events
     */
    public void whenStarted(SimpleEvent whenStarted) {
        this.onSchedule = (WorkerStateEvent wsEvent) -> {
            whenStarted.call();
        };
    }

    public void whenRunning(SimpleEvent whenRunning) {
        this.onRunning = (WorkerStateEvent wsEvent) -> {
            whenRunning.call();
        };
    }

    public void whenCancelled(SimpleEvent whenCancelled) {
        this.onCancel = (WorkerStateEvent wsEvent) -> {
            whenCancelled.call();
            this.onFinished.call();
        };
    }

    public void whenFailed(SimpleEvent whenFailed) {
        this.onFailure = (WorkerStateEvent wsEvent) -> {
            whenFailed.call();
            this.onFinished.call();
        };
    }

    public void whenSuccess(SimpleEvent whenSuccess) {
        this.onSuccess = (WorkerStateEvent wsEvent) -> {
            whenSuccess.call();
            this.onFinished.call();
        };
    }

    /**
     * This will be called whether the transaction was canceled, had failed, or
     * have been succeeded. only applicable to WHEN CALLBACKS.
     *
     * @param whenFinished
     */
    public void whenFinished(SimpleEvent whenFinished) {
        this.onFinished = whenFinished;
    }

    /**
     * Starts the task.
     */
    private Thread th;

    public void start() {
        try {
            /**
             * Set Events.
             */
            this.task.setOnCancelled(onCancel);
            this.task.setOnFailed(onFailure);
            this.task.setOnRunning(onRunning);
            this.task.setOnScheduled(onSchedule);
            this.task.setOnSucceeded(onSuccess);

            /**
             * Start Task.
             */
            th = new Thread(this.task);
            th.setDaemon(true);
            th.setName(taskName);
            th.start();
        } catch (Exception e) {
            System.err.println("SimpleTask-Thread-Error");
            e.printStackTrace();
            throw new RuntimeException("SimpleTask-Thread-Error");
        }

    }

    /**
     * Cancels the task.
     */
    public void cancel() {
        this.task.cancel();
    }

    /**
     * Forcibly cancels the task.
     */
    public void abort() {
        this.task.cancel(true);
    }

    /**
     * Get Task Thread.
     *
     * @return
     */
    public Thread getThread() {
        return th;
    }

}

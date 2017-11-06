/* 
 * Copyright (C) Jhon Melvin N. Perello - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * PROPRIETARY and CONFIDENTIAL
 *
 * Written by Jhon Melvin N. Perello <jhmvinperello@gmail.com>, 2017.
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
public abstract class Transaction {

    private Task<Void> transactionTask;
    private Thread supportThread;
    private long restTime = 0;

    private EventHandler<WorkerStateEvent> onCancel = (WorkerStateEvent wsEvent) -> {
        this.onFinished.call();
    };
    private EventHandler<WorkerStateEvent> onFailure = (WorkerStateEvent wsEvent) -> {
        this.onFinished.call();
    };
    private EventHandler<WorkerStateEvent> onSuccess = (WorkerStateEvent wsEvent) -> {
        this.onFinished.call();
    };

    private EventHandler<WorkerStateEvent> onRunning;
    private EventHandler<WorkerStateEvent> onSchedule;

    // extended event
    private SimpleEvent onFinished = () -> {
        // default event;
    };
    //

    /**
     * <strong> RUN ONLY IN FX-THREAD </strong>
     *
     * @return
     */
    protected abstract boolean transaction();

    /**
     * Continuation of transaction event. this callback will only be called if
     * the return value was true. This was intended for preparation of data.
     */
    protected void after() {
        //
    }

    public final void transact() {
        this.transactionTask = new Task<Void>() {
            @Override
            protected Void call() {
                try {
                    Thread.sleep(restTime);
                    boolean complete = transaction();
                    // cancel is not called
                    if (complete) {
                        after();
                    } else {
                        // if transaction returns false
                        // mark this task as cancelled
                        this.cancel(true);
                    }
                } catch (Exception e) {
                    //this.cancel(true);
                    exceptionHandling(e);
                    throw new java.lang.RuntimeException("Transaction Error -> FIX YOUR TRANSACTION FLOW !");
                }
                return null;
            }
        };
        this.transactionTask.setOnCancelled(onCancel);
        this.transactionTask.setOnFailed(onFailure);
        this.transactionTask.setOnRunning(onRunning);
        this.transactionTask.setOnScheduled(onSchedule);
        this.transactionTask.setOnSucceeded(onSuccess);

        this.supportThread = new Thread(this.transactionTask);
        this.supportThread.setName("@Transaction-Thread");
        this.supportThread.setDaemon(true);
        this.supportThread.start();
    }

    /*
    Task Flags Here.
     */
    protected boolean isCancelled() {
        return this.transactionTask.isCancelled();
    }

    protected boolean isTransacting() {
        return this.transactionTask.isRunning();
    }

    protected boolean isCompleted() {
        return this.transactionTask.isDone();
    }

    /*
    Callback Events.
     */
    public void setOnCancel(EventHandler<WorkerStateEvent> onCancel) {
        this.onCancel = onCancel;
    }

    public void setOnFailure(EventHandler<WorkerStateEvent> onFailure) {
        this.onFailure = onFailure;
    }

    public void setOnRunning(EventHandler<WorkerStateEvent> onRunning) {
        this.onRunning = onRunning;
    }

    public void setOnStart(EventHandler<WorkerStateEvent> onStart) {
        this.onSchedule = onStart;
    }

    public void setOnSuccess(EventHandler<WorkerStateEvent> onSuccess) {
        this.onSuccess = onSuccess;
    }

    /*
    When events.
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

    /*
    Misc Configuration.
     */
    public void setRestTime(long restTime) {
        this.restTime = restTime;
    }

    /**
     * Error Handler Notifier.
     *
     * @param e Task Exception
     */
    final void exceptionHandling(Exception e) {
        System.err.println("<transaction>");
        System.err.println(" - ");
        System.err.println(" - ");
        System.err.println(" - An error occured on a transaction.");
        System.err.println(" - This error was catched and won't cause any system crash.");
        System.err.println(" - Data loss may occur or integrity may be compromsied.");
        System.err.println(" - ");
        System.err.println(" - Please Resolve the following:");
        System.err.println(" - ");
        System.err.println("<error>");
        System.err.println(" - ");
        e.printStackTrace();
        System.err.println(" - ");
        System.err.println("</error>");
        System.err.println(" - setOnFailure() Callback will now be invoked.");
        System.err.println(" - add some error routines using this callback.");
        System.err.println("<transaction>");

    }

    /**
     * Returns the Thread that holds this Transaction Task.
     *
     * @return
     */
    public Thread getSupportThread() {
        return supportThread;
    }

    /**
     * Returns the Task that executes the transaction. Please be careful in
     * modifying the properties of this task.
     *
     * @return
     */
    public Task<Void> getTransactionTask() {
        return transactionTask;
    }

}

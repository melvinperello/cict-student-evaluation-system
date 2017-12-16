package org.bsu.cict.threads;

/**
 * Updated and more efficient Data Transaction abstract class for managing non
 * blocking database operation.
 *
 * @author Jhon Melvin
 */
public abstract class DataTransaction {

    /**
     * Allow access to child classes.
     */
    protected String threadName = "Data-Transaction-Thread";

    /**
     * Set this transaction thread name.
     *
     * @param threadName
     */
    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    /**
     * Transaction.
     */
    public abstract void transaction();

    /**
     * Start the transaction.
     *
     * @param callback
     */
    public void start(DataTransactionCallback callback) {
        Thread transactionThread = new Thread(() -> {
            try {
                //--------------------------------------------------------------
                // execute the transaction.
                transaction();
                //--------------------------------------------------------------
                callback.postExecution(null);
            } catch (Exception e) {
                callback.postExecution(e);
            }
        });
        transactionThread.setDaemon(false);
        transactionThread.setName(this.threadName);
        transactionThread.start();
    }

    /**
     * Post execution call back for transaction.
     */
    @FunctionalInterface
    public interface DataTransactionCallback {

        void postExecution(Exception e);
    }

}

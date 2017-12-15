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

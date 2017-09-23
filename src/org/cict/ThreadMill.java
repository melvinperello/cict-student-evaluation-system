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
package org.cict;

import com.jhmvin.fx.async.CronTimer;

/**
 *
 * @author Jhon Melvin
 */
public class ThreadMill {

    private static ThreadMill THREAD_INSTANCE;

    public static ThreadMill threads() {
        if (THREAD_INSTANCE == null) {
            THREAD_INSTANCE = new ThreadMill();
        }
        return THREAD_INSTANCE;
    }

    /**
     * List your threads here.
     */
    public CronTimer KEEP_ALIVE_THREAD;

    private ThreadMill() {
        // Create Threads here
        this.threadFactory();
    }

    private void threadFactory() {
        this.KEEP_ALIVE_THREAD = new CronTimer("loginkeep-alive");
        this.KEEP_ALIVE_THREAD.setInterval(30000); // 30 secs
        // add more threads here
    }

    /**
     * Add All thread entries here.
     */
    public void shutdown() {
        KEEP_ALIVE_THREAD.pause();
        KEEP_ALIVE_THREAD.stop();
    }
}

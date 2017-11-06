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
package com.jhmvin.flow;

/**
 *
 * @author Jhon Melvin
 */
public class LoopData {

    private final Integer value, from, to;
    private boolean exitting = false;
    private boolean skipping = false;

    public LoopData(Integer value, Integer from, Integer to) {
        this.value = value;
        this.from = from;
        this.to = to;
    }

    public Integer getValue() {
        return value;
    }

    public void end() {
        exitting = true;
    }

    public void skip() {
        this.skipping = true;
    }

    public boolean isExitting() {
        return exitting;
    }

    public boolean isSkipping() {
        return skipping;
    }

    public boolean isLast() {
        return this.to.equals(this.value);
    }

    public boolean isFirst() {
        return this.from.equals(this.value);
    }

}

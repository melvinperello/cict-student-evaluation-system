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
package org.cict.authentication.authenticator;

import java.util.Date;

/**
 *
 * @author Jhon Melvin
 */
public class CollegeComputer {

    private static CollegeComputer COMPUTER_INSTANCE;

    private CollegeComputer() {
        //
    }

    public static CollegeComputer instance() {
        if (COMPUTER_INSTANCE == null) {
            COMPUTER_INSTANCE = new CollegeComputer();
        }
        return COMPUTER_INSTANCE;
    }

    /**
     * Terminal Details <br>
     * All details related to the used computer.
     */
    private Integer SESSION_ID;
    private Date SESSION_START;
    private String IP_ADDRESS;
    private String PC_NAME;
    private String PC_USERNAME;
    private String OS;
    private String PLATFORM;

    public Integer getSESSION_ID() {
        return SESSION_ID;
    }

    public void setSESSION_ID(Integer SESSION_ID) {
        this.SESSION_ID = SESSION_ID;
    }

    public Date getSESSION_START() {
        return SESSION_START;
    }

    public void setSESSION_START(Date SESSION_START) {
        this.SESSION_START = SESSION_START;
    }

    public String getIP_ADDRESS() {
        return IP_ADDRESS;
    }

    public void setIP_ADDRESS(String IP_ADDRESS) {
        this.IP_ADDRESS = IP_ADDRESS;
    }

    public String getPC_NAME() {
        return PC_NAME;
    }

    public void setPC_NAME(String PC_NAME) {
        this.PC_NAME = PC_NAME;
    }

    public String getPC_USERNAME() {
        return PC_USERNAME;
    }

    public void setPC_USERNAME(String PC_USERNAME) {
        this.PC_USERNAME = PC_USERNAME;
    }

    public String getOS() {
        return OS;
    }

    public void setOS(String OS) {
        this.OS = OS;
    }

    public String getPLATFORM() {
        return PLATFORM;
    }

    public void setPLATFORM(String PLATFORM) {
        this.PLATFORM = PLATFORM;
    }

}

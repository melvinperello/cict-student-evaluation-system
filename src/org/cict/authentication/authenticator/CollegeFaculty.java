/**
 * CAPSTONE PROJECT.
 * BSIT 4A-G1.
 * MONOSYNC TECHNOLOGIES.
 * MONOSYNC FRAMEWORK VERSION 1.0.0 TEACUP RICE ROLL.
 * THIS PROJECT IS PROPRIETARY AND CONFIDENTIAL ANY PART THEREOF.
 * COPYING AND DISTRIBUTION WITHOUT PERMISSION ARE NOT ALLOWED.
 * <p>
 * COLLEGE OF INFORMATION AND COMMUNICATIONS TECHNOLOGY. LINKED SYSTEM.
 * <p>
 * PROJECT MANAGER: JHON MELVIN N. PERELLO DEVELOPERS: JOEMAR N. DELA CRUZ
 * GRETHEL EINSTEIN BERNARDINO
 * <p>
 * OTHER LIBRARIES THAT ARE USED BELONGS TO THEIR RESPECTFUL OWNERS AND AUTHORS.
 * NO COPYRIGHT ARE INTENTIONAL OR INTENDED. THIS PROJECT IS NOT PROFITABLE
 * HENCE FOR EDUCATIONAL PURPOSES ONLY. THIS PROJECT IS ONLY FOR COMPLIANCE TO
 * OUR REQUIREMENTS. THIS PROJECT DOES NOT INCLUDE DISTRIBUTION FOR OTHER
 * PURPOSES.
 */
package org.cict.authentication.authenticator;

import app.lazy.models.AcademicTermMapping;
import com.jhmvin.fx.async.CronTimer;

/**
 * @author Jhon Melvin
 */
public class CollegeFaculty {

    private static CollegeFaculty FACULTY_INSTANCE;

    public static CollegeFaculty instance() {
        if (FACULTY_INSTANCE == null) {
            FACULTY_INSTANCE = new CollegeFaculty();
        }
        return FACULTY_INSTANCE;
    }

    private CollegeFaculty() {
        // construct
    }

    /**
     * Faculty Information <br>
     * All the details represented by this faculty
     */
    private Integer FACULTY_ID;
    private String BULSU_ID;
    private String LAST_NAME;
    private String FIRST_NAME;
    private String MIDDLE_NAME;
    private String GENDER;
    private String RANK;
    private String DESIGNATION;
    private String TRANSACTION_PIN;

    public String getBULSU_ID() {
        return BULSU_ID;
    }

    public void setBULSU_ID(String BULSU_ID) {
        this.BULSU_ID = BULSU_ID;
    }

    public Integer getFACULTY_ID() {
        return FACULTY_ID;
    }

    public void setFACULTY_ID(Integer FACULTY_ID) {
        this.FACULTY_ID = FACULTY_ID;
    }

    public String getLAST_NAME() {
        return LAST_NAME;
    }

    public void setLAST_NAME(String LAST_NAME) {
        this.LAST_NAME = LAST_NAME;
    }

    public String getFIRST_NAME() {
        return FIRST_NAME;
    }

    public void setFIRST_NAME(String FIRST_NAME) {
        this.FIRST_NAME = FIRST_NAME;
    }

    public String getMIDDLE_NAME() {
        return MIDDLE_NAME;
    }

    public void setMIDDLE_NAME(String MIDDLE_NAME) {
        this.MIDDLE_NAME = MIDDLE_NAME;
    }

    public String getGENDER() {
        return GENDER;
    }

    public void setGENDER(String GENDER) {
        this.GENDER = GENDER;
    }

    public String getRANK() {
        return RANK;
    }

    public void setRANK(String RANK) {
        this.RANK = RANK;
    }

    public String getTRANSACTION_PIN() {
        return TRANSACTION_PIN;
    }

    public void setTRANSACTION_PIN(String TRANSACTION_PIN) {
        this.TRANSACTION_PIN = TRANSACTION_PIN;
    }

    public String getDESIGNATION() {
        return DESIGNATION;
    }

    public void setDESIGNATION(String DESIGNATION) {
        this.DESIGNATION = DESIGNATION;
    }

    //---
    public String getFirstLastName() {
        return this.FIRST_NAME + " " + this.LAST_NAME;
    }

    /**
     * Account Information <br>
     * All details represented by this account
     */
    private Integer ACCOUNT_ID;
    private String USERNAME;
    // Some Fields are private
    private String ACCESS_LEVEL;

    public Integer getACCOUNT_ID() {
        return ACCOUNT_ID;
    }

    public void setACCOUNT_ID(Integer ACCOUNT_ID) {
        this.ACCOUNT_ID = ACCOUNT_ID;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getACCESS_LEVEL() {
        return ACCESS_LEVEL;
    }

    public void setACCESS_LEVEL(String ACCESS_LEVEL) {
        this.ACCESS_LEVEL = ACCESS_LEVEL;
    }

}

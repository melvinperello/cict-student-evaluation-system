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
package org.cict.accountmanager;

import app.lazy.models.AccountFacultyMapping;
import app.lazy.models.Database;
import app.lazy.models.FacultyMapping;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;

/**
 *
 * @author Joemar
 */
public class ValidateRegister extends Transaction{
    
    public String bulsuId;
    public String username;
    public String password;
    public String question;
    public String answer;
    public boolean complete = false;
    
    private AccountFacultyMapping accountFaculty;
    private FacultyMapping faculty;
    private String authenticatorMessage = "";
    private boolean stage1Verified = false;
    private boolean saved = false;

    public AccountFacultyMapping getAccountFacultyMap(){
        return this.accountFaculty;
    }
    
    public boolean isStage1Verified() {
        return stage1Verified;
    }
    
    public boolean isSaved() {
        return saved;
    }
    
    public String getAuthenticatorMessage() {
        return authenticatorMessage;
    }
    
    @Override
    protected boolean transaction() {
        //check if bulsu_id exist
        this.faculty = Mono.orm()
                .newSearch(Database.connect().faculty())
                .eq("bulsu_id", this.bulsuId)
                .execute()
                .first();
        if(this.faculty == null){
            authenticatorMessage = "No faculty found with a BulSU ID of '" + this.bulsuId + "'."
                    + " Please try another.";
            return true;
        }
        //check username if exist
        this.accountFaculty = Mono.orm()
                .newSearch(Database.connect().account_faculty())
                .eq("username", this.username)
                .execute()
                .first();
        
        if(this.accountFaculty != null){
            authenticatorMessage = "Username already used by another account."
                    + " Please provide a new one. Thank You!";
            return true;
        }
        //check if faculty already have an account
        this.accountFaculty = Mono.orm()
                .newSearch(Database.connect().account_faculty())
                .eq("FACULTY_id", this.faculty.getId())
                .execute()
                .first();
        if(this.accountFaculty != null){
            authenticatorMessage = "Faculty with a BulSU ID of " + this.bulsuId + ","
                    + " already have an account."
                    + " Single account per user only.";
            return true;
        }
        this.stage1Verified = true;
        this.accountFaculty = new AccountFacultyMapping();
        this.accountFaculty.setFACULTY_id(this.faculty.getId());
        this.accountFaculty.setUsername(this.username.toUpperCase());
        this.accountFaculty.setPassword(this.password);
        this.accountFaculty.setAccess_level(this.faculty.getDesignation());
        if(this.complete){
            this.accountFaculty.setRecovery_question(this.question);
            this.accountFaculty.setRecovery_answer(this.answer);
            Integer i = Database
                    .connect()
                    .account_faculty()
                    .insert(this.accountFaculty);
            if( i != -1) {
                authenticatorMessage = "Congratulations! "
                        + "Your account is now available for use.";
                this.saved = true;
            } else {
                authenticatorMessage = "Something went wrong in the registration process. "
                        + "Please try again later. Sorry for the inconvinience.";
            }
        }
        return true;
    }

    @Override
    protected void after() {
    
    }
    
}

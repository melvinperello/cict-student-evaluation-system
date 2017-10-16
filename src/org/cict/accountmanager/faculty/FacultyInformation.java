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
package org.cict.accountmanager.faculty;

import app.lazy.models.AccountFacultyMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.FacultyMapping;
import artifacts.MonoString;
import com.jhmvin.Mono;

/**
 *
 * @author Joemar
 */
public class FacultyInformation {

    public FacultyInformation(FacultyMapping faculty) {
        this.faculty = faculty;
        afMap = Mono.orm().newSearch(Database.connect().account_faculty())
                .eq(DB.account_faculty().FACULTY_id, faculty.getId())
                .execute().first();
        if (afMap == null) {
            System.out.println("NO ACCOUNT: " + faculty.getBulsu_id());
        }
    }

    private FacultyMapping faculty;
    private AccountFacultyMapping afMap;

//    public void setFacultyMapping(FacultyMapping faculty) {
//        this.faculty = faculty;
//    }
//    public void setAccountFacultyMapping(AccountFacultyMapping afMap) {
//        this.afMap = afMap;
//    }
    public FacultyMapping getFacultyMapping() {
        return this.faculty;
    }

    public AccountFacultyMapping getAccountFacultyMapping() {
        return this.afMap;
    }

    public String getFullName() {
        String middleName = " ";
        String temp = MonoString.removeExtraSpace(faculty.getMiddle_name()).trim();
        if(temp != null && !temp.isEmpty())
            middleName = " " + temp + " ";
            
        return  faculty.getFirst_name() 
                + middleName + faculty.getLast_name();
    }

    public String getFirtsName() {
        return  faculty.getFirst_name();
    }

    public String getMiddleName() {
        return faculty.getMiddle_name();
    }

    public String getLastName() {
        return faculty.getLast_name();
    }

    public String getDepartment() {
        return faculty.getDepartment();
    }
    
    public String getBulsuID() {
        return faculty.getBulsu_id();
    }
    
    public String getAccessLevel() {
        return afMap.getAccess_level();
    }
}

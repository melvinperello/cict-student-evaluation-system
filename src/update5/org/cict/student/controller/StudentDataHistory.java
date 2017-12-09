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
package update5.org.cict.student.controller;

import app.lazy.models.Database;
import app.lazy.models.StudentDataHistoryMapping;
import java.util.Date;

/**
 *
 * @author Jhon Melvin
 */
public class StudentDataHistory {

    private Integer cictID;
    private String studentNumber;
    private String lastName;
    private String firstName;
    private String middleName;
    private String gender;
    private String campus;
    private String yearLevel;
    private String section;
    private String group;
    private String updatedby;
    private Date updatedDate;

    public void setCictID(Integer cictID) {
        this.cictID = cictID;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public void setYearLevel(String yearLevel) {
        this.yearLevel = yearLevel;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setUpdatedby(String updatedby) {
        this.updatedby = updatedby;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    /**
     * Create a history of the previous state of the record.
     */
    public void makeHistory() {
        StudentDataHistoryMapping historyMap = new StudentDataHistoryMapping();
        historyMap.setCict_id(cictID);
        historyMap.setStudent_number(studentNumber);
        historyMap.setLast_name(lastName);
        historyMap.setFirst_name(firstName);
        historyMap.setMiddle_name(middleName);
        historyMap.setGender(gender);
        historyMap.setCampus(campus);
        historyMap.setYear_level(yearLevel);
        historyMap.setSection(section);
        historyMap.set_group(group);
        historyMap.setUpdated_by(updatedby);
        historyMap.setUpdated_date(updatedDate);
        // no check if inserted
        Integer a = Database.connect().student_data_history().insert(historyMap);
        System.out.println("STUDENT DATA HISTORY INSERT: " + a);
    }

}

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
 * JOEMAR N. DE LA CRUZ
 * GRETHEL EINSTEIN BERNARDINO
 *
 * OTHER LIBRARIES THAT ARE USED BELONGS TO THEIR RESPECTFUL OWNERS AND AUTHORS.
 * NO COPYRIGHT ARE INTENTIONAL OR INTENDED.
 * THIS PROJECT IS NOT PROFITABLE HENCE FOR EDUCATIONAL PURPOSES ONLY.
 * THIS PROJECT IS ONLY FOR COMPLIANCE TO OUR REQUIREMENTS.
 * THIS PROJECT DOES NOT INCLUDE DISTRIBUTION FOR OTHER PURPOSES.
 *
 */
package org.cict.reports.profile.student;

import app.lazy.models.AcademicTermMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.StudentMapping;
import app.lazy.models.StudentProfileMapping;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import org.apache.commons.lang3.text.WordUtils;
import org.cict.authentication.authenticator.SystemProperties;
import org.cict.reports.ReportsDirectory;
import org.hibernate.criterion.Order;

/**
 *
 * @author Joemar
 */
public class PrintStudentProfile extends Transaction {

    public Integer CICT_id;
    
    private StudentMapping student;
    private String address = "", studentContact = "", emailAdd = ""
            , guardianName = "", guardianAddr = "", guardianContact = ""
            , imageLoc = "";
    
    @Override
    protected boolean transaction() {
        student = Database.connect().student().getPrimary(CICT_id);
        if(student==null) {
            System.out.println("NO STUDENT FOUND");
            return false;
        }
        
        if (student.getHas_profile() == 1) {
            StudentProfileMapping spMap = Mono.orm().newSearch(Database.connect().student_profile())
                    .eq(DB.student_profile().STUDENT_id, student.getCict_id())
                    .active(Order.desc(DB.student_profile().id)).first();
            String hNum = spMap.getHouse_no(),
                    brgy = spMap.getBrgy(),
                    city = spMap.getCity(),
                    province = spMap.getProvince();
            if (hNum != null) {
                address = hNum;
            }
            if (brgy != null) {
                if (!address.isEmpty()) {
                    address += " " + spMap.getBrgy();
                } else {
                    address = brgy;
                }
            }
            if (city != null) {
                if (!address.isEmpty()) {
                    address += " " + city;
                } else {
                    address = city;
                }
            }
            if (province != null) {
                if (!address.isEmpty()) {
                    address += ", " + province;
                } else {
                    address = province;
                }
            }
            studentContact = (spMap.getMobile()==null? "" : spMap.getMobile());
            emailAdd = (spMap.getEmail()==null? "" : spMap.getEmail());
            guardianName = (spMap.getIce_name()==null? "" : spMap.getIce_name());
            guardianAddr = (spMap.getIce_address()==null? "" : spMap.getIce_address());
            guardianContact = (spMap.getIce_contact()==null? "" : spMap.getIce_contact());
            imageLoc = (spMap.getProfile_picture()==null? "" : spMap.getProfile_picture());
        }
        return true;
    }

    private String SAVE_DIRECTORY = "reports/profile/student";
    @Override
    protected void after() {
        String doc = student.getId() + "_"
                + Mono.orm().getServerTime().getCalendar().getTimeInMillis();
        String RESULT = SAVE_DIRECTORY + "/" + doc + ".pdf";

        //------------------------------------------------------------------------------
        //------------------------------------------------------------------------------
        /**
         * Check if the report save directory is already existing and created if
         * not this will try to create the needed directories.
         */
        boolean isCreated = ReportsDirectory.check(SAVE_DIRECTORY);

        if (!isCreated) {
            // some error message that the directory is not created
            System.err.println("Directory is not created.");
            return;
        }
        //------------------------------------------------------------------
        //------------------------------------------------------------------

        String fullName = student.getLast_name() + ", " + student.getFirst_name() 
                + (student.getMiddle_name()==null? "": (" " +student.getMiddle_name()));
        
        StudentProfile studentProfile = new StudentProfile(RESULT);
        AcademicTermMapping acadTerm = SystemProperties.instance().getCurrentAcademicTerm();
        studentProfile.SEMESTER = acadTerm.getSemester_regular()==1? "1st" : "2nd";
        studentProfile.SCHOOL_YEAR = acadTerm.getSchool_year();
        studentProfile.IMAGE_LOCATION = imageLoc;
        studentProfile.STUDENT_NAME = WordUtils.capitalizeFully(fullName);
        studentProfile.STUDENT_ADDRESS = WordUtils.capitalizeFully(address);
        studentProfile.STUDENT_CONTACT_NO = WordUtils.capitalizeFully(studentContact);
        studentProfile.STUDENT_EMAIL_ADD = WordUtils.capitalizeFully(emailAdd);
        studentProfile.GUARDIAN_NAME = WordUtils.capitalizeFully(guardianName);
        studentProfile.GUARDIAN_ADDRESS = WordUtils.capitalizeFully(guardianAddr);
        studentProfile.GUARDIAN_CONTACT_NO = WordUtils.capitalizeFully(guardianContact);
        
        int val = studentProfile.print();
    }
    
}

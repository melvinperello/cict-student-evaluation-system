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
import app.lazy.models.utils.StudentUtility;
import artifacts.FTPManager;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import java.io.File;
import org.apache.commons.io.FileUtils;
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
    private String address = "", studentContact = "", emailAdd = "", guardianName = "", guardianAddr = "", guardianContact = "";

    //--------------------------------------------------------------------------
    // store image here
    private String studentImage;
    //--------------------------------------------------------------------------
    private boolean noProfile;

    public boolean hasNoProfile() {
        return noProfile;
    }
    //--------------------------------------------------------------------------

    @Override
    protected boolean transaction() {
        // assume that the student has a profile
        this.noProfile = false;
        //----------------------------------------------------------------------
        student = Database.connect().student().getPrimary(CICT_id);
        if (student == null) {
            System.out.println("NO STUDENT FOUND");
            return false;
        }

        if (student.getHas_profile() == 1) {
            StudentProfileMapping spMap = Mono.orm().newSearch(Database.connect().student_profile())
                    .eq(DB.student_profile().STUDENT_id, student.getCict_id())
                    .active(Order.desc(DB.student_profile().id))
                    .first();

            //------------------------------------------------------------------
            if (spMap == null) {
                // the student has no profile
                this.noProfile = true;
                return false; // cancel this transaction
            }
            //------------------------------------------------------------------

            //------------------------------------------------------------------
            // Get student address
            this.address = StudentUtility.getStudentAddress(spMap);
            //------------------------------------------------------------------
            studentContact = (spMap.getMobile() == null ? "" : spMap.getMobile());
            emailAdd = (spMap.getEmail() == null ? "" : spMap.getEmail());
            guardianName = (spMap.getIce_name() == null ? "" : spMap.getIce_name());
            guardianAddr = (spMap.getIce_address() == null ? "" : spMap.getIce_address());
            guardianContact = (spMap.getIce_contact() == null ? "" : spMap.getIce_contact());

            //------------------------------------------------------------------
            // assign picture
            studentImage = spMap.getProfile_picture();
            //------------------------------------------------------------------
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
                + (student.getMiddle_name() == null ? "" : (" " + student.getMiddle_name()));

        StudentProfile studentProfile = new StudentProfile(RESULT);
        AcademicTermMapping acadTerm = SystemProperties.instance().getCurrentAcademicTerm();
        //----------------------------------------------------------------------
        String semester = "";
        String schoolYear = "";
        if (acadTerm == null) {
            semester = "";
        } else {
            //
            schoolYear = acadTerm.getSchool_year();
            switch (acadTerm.getSemester_regular()) {
                case 0:
                    semester = "Midyear";
                    break;
                case 1:
                    semester = "1st";
                    break;
                case 2:
                    semester = "2nd";
                    break;
                default:
                    semester = "UNREGISTERED";
            }
        }
        //----------------------------------------------------------------------
        studentProfile.SEMESTER = semester;
        //----------------------------------------------------------------------
        studentProfile.SCHOOL_YEAR = schoolYear;
        //----------------------------------------------------------------------
        studentProfile.STUDENT_NAME = WordUtils.capitalizeFully(fullName);
        studentProfile.STUDENT_ADDRESS = WordUtils.capitalizeFully(address);
        studentProfile.STUDENT_CONTACT_NO = WordUtils.capitalizeFully(studentContact);
        studentProfile.STUDENT_EMAIL_ADD = WordUtils.capitalizeFully(emailAdd);
        studentProfile.GUARDIAN_NAME = WordUtils.capitalizeFully(guardianName);
        studentProfile.GUARDIAN_ADDRESS = WordUtils.capitalizeFully(guardianAddr);
        studentProfile.GUARDIAN_CONTACT_NO = WordUtils.capitalizeFully(guardianContact);

        //----------------------------------------------------------------------
        if (studentImage == null
                || studentImage.isEmpty()
                || studentImage.equalsIgnoreCase("NONE")) {
            // do not run downloader
            // print the profile
            // in this case the image setter was not called but we have assigned default value
            int val = studentProfile.print();
        } else {
            String tempProfilePath = "temp/images/profile";
            String tempProfileImagePath = tempProfilePath + "/" + studentImage;
            File tempProfileDir = new File(tempProfilePath);
            File tempProfileImage = new File(tempProfileImagePath);
            try {
                FileUtils.forceMkdir(tempProfileDir);
                FTPManager.download("student_avatar", studentImage, tempProfileImage.getAbsolutePath());
                studentProfile.setProfileImage(tempProfileImage.getAbsolutePath());
                int val = studentProfile.print();
            } catch (Exception e) {
                studentProfile.setProfileImage(null);
                int val = studentProfile.print();
            }

//            // this is a downloader thread please put next actions inside
//            ProfileImage.download(studentImage, path -> {
//                // now we have the path
//                // this will return null if not fetched from the server
//                // we can call the setter to set the image
//                studentProfile.setProfileImage(path);
//                // and then print
//                int val = studentProfile.print();
//            });
        }
        //----------------------------------------------------------------------
    }

}

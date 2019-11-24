/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cict.profilereader;

import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.MapFactory;
import app.lazy.models.StudentMapping;
import app.lazy.models.StudentProfileMapping;
import com.jhmvin.Mono;
import com.reeses.docs.excel.InvalidSpreadSheetHeaderException;
import com.reeses.docs.excel.SpreadSheetHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;

/**
 *
 * @author Jhon Melvin
 */
public class ProfileReader {

    //--------------------------------------------------------------------------
    // Define Columns
    public final static int COL_STUDENT_NUMBER = 0,
            COL_LAST_NAME = 1,
            COL_FIRST_NAME = 2,
            COL_MIDDLE_NAME = 3,
            COL_YEAR = 4,
            COL_SECTION = 5,
            COL_GROUP = 6,
            COL_ADDRESS = 7,
            COL_MOBILE = 8,
            COL_EMAIL = 9,
            COL_GUARDIAN = 10,
            COL_GUARDIAN_MOBILE = 11,
            COL_GUARDIAN_ADDRESS = 12;

    //--------------------------------------------------------------------------
    public final static String[] HEADERS = new String[]{
        "STUDENT NUMBER",
        "LAST NAME",
        "FIRST NAME",
        "MIDDLE NAME",
        "YEAR",
        "SECTION",
        "GROUP",
        "ADDRESS",
        "MOBILE",
        "EMAIL",
        "GUARDIAN",
        "GUARDIAN MOBILE",
        "GUARDIAN ADDRESS"
    };

    public ArrayList<ProfileObject> read(String file) throws InvalidSpreadSheetHeaderException, IOException {
        final ArrayList<ProfileObject> profileList = new ArrayList<>();
        // open the workbook
        XSSFWorkbook excelDocument = SpreadSheetHelper.open(file);
        // select the sheet
        XSSFSheet excelSheet = excelDocument.getSheetAt(0);

        SpreadSheetHelper.checkHeaders(excelSheet, HEADERS);

        SpreadSheetHelper.readSpreadSheet(excelSheet, (i, row) -> {
            if (i == 0) {
                return;
            }
            // check for HEADERS
            ProfileObject profile = new ProfileObject();
            SpreadSheetHelper.readSpreadSheetRow(row, (index, cell, cellString) -> {
                switch (index) {
                    case COL_STUDENT_NUMBER:
                        profile.setStudentNumber(cellString);
                        break;
                    case COL_LAST_NAME:
                        profile.setLastName(cellString);
                        break;
                    case COL_FIRST_NAME:
                        profile.setFirstName(cellString);
                        break;
                    case COL_MIDDLE_NAME:
                        profile.setMiddleName(cellString);
                        break;
                    case COL_YEAR:
                        profile.setYear(cellString);
                        break;
                    case COL_SECTION:
                        profile.setSection(cellString);
                        break;
                    case COL_GROUP:
                        profile.setGroup(cellString);
                        break;
                    case COL_ADDRESS:
                        profile.setAddress(cellString);
                        break;
                    case COL_MOBILE:
                        profile.setMobile(cellString);
                        break;
                    case COL_EMAIL:
                        profile.setEmail(cellString);
                        break;
                    case COL_GUARDIAN:
                        profile.setGuardian(cellString);
                        break;
                    case COL_GUARDIAN_MOBILE:
                        profile.setGuardianMobile(cellString);
                        break;
                    case COL_GUARDIAN_ADDRESS:
                        profile.setGuardianAddress(cellString);
                        break;
                }
            });
            profileList.add(profile);
        });

        // return the list
        return profileList;
    }

    private final static String file = "C:\\Users\\Jhon Melvin\\Desktop\\EMS-CICT\\BSIT 4\\4H-G2.xlsx";
    private final static Integer curriculum_id = 9; // curriculum
    private final static Integer prep_id = null; // preparatory

    //--------------------------------------------------------------------------
    public static void main(String[] args) {

        try {
            // Create reader object
            ProfileReader reader = new ProfileReader();
            // assign the file
            ArrayList<ProfileObject> profileList = reader.read(file);
            //------------------------------------------------------------------
            // if not connected then connect
            if (Mono.orm().getSessionFactory() == null) {
                Database.connect();
            }
            // create local transaction
            //------------------------------------------------------------------
            Session localSession = Mono.orm().openSession();
            org.hibernate.Transaction dataTx = localSession.beginTransaction();
            //------------------------------------------------------------------s
            boolean success = true;
            // read data
            for (int x = 0; x < profileList.size(); x++) {
                ProfileObject profileObject = profileList.get(x);
                System.out.println("Reading: [ " + (x + 1) + " / " + profileList.size() + " ]\t" + profileObject.getStudentNumber() + "\t" + profileObject.getLastName());
                System.out.println("Uploading . . .");
                boolean res = reader.uploadData(profileObject, localSession);
                if (!res) {
                    success = false;
                    break;
                }
            }

            if (success) {
                dataTx.commit();
                localSession.close();
            } else {
                dataTx.rollback();
                localSession.close();
            }
            Mono.orm().shutdown();

        } catch (InvalidSpreadSheetHeaderException ex) {
            System.out.println("Invalid header");
        } catch (IOException ex) {
            System.out.println("cannot open");
        }

    }
    //--------------------------------------------------------------------------

    public boolean uploadData(ProfileObject profileObject, Session localSession) {
        // if not connected then connect
        if (Mono.orm().getSessionFactory() == null) {
            Database.connect();
        }

        //----------------------------------------------------------------------
        StudentMapping student = MapFactory.map().student();
        //----------------------------------------------------------------------
        // Student Number
        String studentNumber = profileObject.getStudentNumber();
        studentNumber = removeSpaceOrDash(studentNumber);
        student.setId(studentNumber);
        //----------------------------------------------------------------------
        student.setLast_name(profileObject.getLastName().toUpperCase());
        student.setFirst_name(profileObject.getFirstName().toUpperCase());
        student.setMiddle_name(profileObject.getMiddleName().toUpperCase());
        student.setYear_level(Integer.parseInt(profileObject.getYear()));
        student.set_group(Integer.parseInt(profileObject.getGroup()));
        student.setSection(profileObject.getSection().toUpperCase());
        //----------------------------------------------------------------------
        // Assign Curriculum
        if (curriculum_id != null) {
            student.setCURRICULUM_id(curriculum_id);
            student.setCurriculum_assignment(new Date());
        }
        if (prep_id != null) {
            student.setPREP_id(prep_id);
            student.setPrep_assignment(new Date());
        }
        //----------------------------------------------------------------------
        StudentProfileMapping studentProfile = MapFactory.map().student_profile();
        /**
         * Mali yung set city dapat set student address
         */
        //studentProfile.setCity(profileObject.getAddress().toUpperCase());
        studentProfile.setStudent_address(profileObject.getAddress().toUpperCase());

        String mobileNumber = ProfileReader.formatMobile(profileObject.getMobile());
        studentProfile.setMobile(mobileNumber);

        studentProfile.setEmail(profileObject.getEmail());
        studentProfile.setIce_name(profileObject.getGuardian());
        studentProfile.setIce_contact(ProfileReader.formatMobile(profileObject.getGuardianMobile()));
        studentProfile.setIce_address(profileObject.getGuardianAddress());
        //----------------------------------------------------------------------
        StudentMapping checkStudent = Mono.orm().newSearch(Database.connect().student())
                .eq(DB.student().id, student.getId())
                .active()
                .first();
        //----------------------------------------------------------------------
        // insert or update student table
        if (checkStudent == null) {
            // not exist insert
            int id = Database.connect().student().transactionalInsert(localSession, student);
            if (id >= 1) {
                //--------------------------------------------------------------
                // insert profile
                boolean res = uploadProfile(id, studentProfile, localSession);
                //--------------------------------------------------------------
                if (res) {
                    return true;
                }
            }

            System.out.println("FAILED TO INSERT STUDENT");
            return false;
        }

        //----------------------------------------------------------------------
        // exist update
        //----------------------------------------------------------------------
        boolean updated = Database.connect()
                .student().transactionalSingleUpdate(localSession, checkStudent);
        if (updated) {
            //--------------------------------------------------------------
            // insert profile
            boolean res = uploadProfile(checkStudent.getCict_id(), studentProfile, localSession);
            //--------------------------------------------------------------
            if (res) {
                return true;
            }
        }
        System.out.println("FAILED TO INSERT STUDENT");
        return false;
    }

    /**
     *
     * @param CICT_ID
     * @param studentProfile
     * @param localSession
     * @return
     */
    public boolean uploadProfile(
            int CICT_ID,
            StudentProfileMapping studentProfile,
            Session localSession) {

        StudentProfileMapping studentProfileCheck = Mono.orm().newSearch(Database.connect().student_profile())
                .eq(DB.student_profile().STUDENT_id, CICT_ID)
                .active()
                .first();

        // insert or update student table
        if (studentProfileCheck == null) {
            // not exist insert
            studentProfile.setSTUDENT_id(CICT_ID);
            int id = Database.connect().student_profile().transactionalInsert(localSession, studentProfile);
            if (id >= 1) {
                // success
                return true;
            } else {
                System.out.println("FAILED TO INSERT STUDENT");
                return false;
            }
        } else {
            // exist update
            studentProfileCheck.setSTUDENT_id(CICT_ID);
            boolean updated = Database.connect().student_profile().transactionalSingleUpdate(localSession, studentProfileCheck);
            if (updated) {
                // success
                return true;
            } else {
                System.out.println("FAILED TO INSERT STUDENT");
                return false;
            }
        }

    }

    public static String formatMobile(String mobile) {
        String mobileNumber = mobile;
        if (mobileNumber.length() == 10 && mobileNumber.charAt(0) == '9') {
            mobileNumber = "0" + mobileNumber;
        }

        mobile = removeSpaceOrDash(mobileNumber);
        return mobileNumber;
    }

    public static String removeSpaceOrDash(String studentNumber) {
        // Student Number
        String tempStudentNumber = "";
        //----------------------------------------------------------------------
        for (String str : studentNumber.split("-")) {
            if (str.isEmpty()) {
                continue;
            }
            tempStudentNumber += str.trim().toUpperCase();
        }
        studentNumber = tempStudentNumber;
        tempStudentNumber = "";
        //----------------------------------------------------------------------
        for (String str : studentNumber.split(" ")) {
            if (str.isEmpty()) {
                continue;
            }
            tempStudentNumber += str.trim().toUpperCase();
        }
        studentNumber = tempStudentNumber;

        //----------------------------------------------------------------------
        return studentNumber;
    }

}

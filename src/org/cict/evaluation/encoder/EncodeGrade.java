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
package org.cict.evaluation.encoder;

import app.lazy.models.CurriculumSubjectMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.GradeMapping;
import app.lazy.models.StudentMapping;
import app.lazy.models.SubjectMapping;
import artifacts.CurriculumFix;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import com.jhmvin.fx.async.TransactionException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javafx.collections.ObservableList;
import org.cict.PublicConstants;
import org.cict.SubjectClassification;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.controlsfx.control.spreadsheet.Grid;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetView;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

/**
 * Encodes grade from the spreadsheet view.
 *
 * @author Joemar
 *
 * Code Legibility Level (0/10), Extremely messy and incomprehensible.
 * exceptions are not well handled. errors are thrown sometimes and sometimes
 * don't. Does not follow a logical flow.
 *
 * @reviewed 10/17/2017
 * @editor Melvin.
 */
public class EncodeGrade extends Transaction {

    public SpreadsheetView spreadSheet;
    public Integer CICT_id;
    public String MODE = "";
    //--------------------------------------------------------------------------
    public StudentMapping studentMapping;
    public Integer encodingYear;
    //--------------------------------------------------------------------------

    private final Integer FACULTY_id = CollegeFaculty.instance().getFACULTY_ID();
    public Integer ACAD_TERM_id;

//    private boolean POSTED = false;
    private boolean beenModified = false;

    public boolean isAlreadyPosted() {
        return this.beenModified;
    }

//    public boolean isPosted() {
//        return this.POSTED;
//    }
    @Override
    protected boolean transaction() {
        final Calendar serverCalendar = Mono.orm().getServerTime().getCalendar();
        final Date serverDate = Mono.orm().getServerTime().getDateWithFormat();
        final Calendar incExpireTime = Calendar.getInstance();
        incExpireTime.setTime(serverDate);
        incExpireTime.add(Calendar.MONTH, PublicConstants.INC_EXPIRE);

        // Start Transaction
        Session localSession = Mono.orm().openSession();
        org.hibernate.Transaction dataTx = localSession.beginTransaction();
        //----------------------------------------------------------------------
        // spreadsheet grid
        Grid grid = this.spreadSheet.getGrid();
        // get gridRows
        ObservableList<ObservableList<SpreadsheetCell>> gridRows = grid.getRows();
        // counters
        int alreadyPostedGradeCount = 0, postedGradeCount = 0;
        // loop
        for (int rowIndex = 0; rowIndex < grid.getRowCount(); rowIndex++) {
            //------------------------------------------------------------------
            ObservableList<SpreadsheetCell> individualRow = gridRows.get(rowIndex);
            String subjectCode = individualRow.get(0).getItem().toString();
            String gradeRating = individualRow.get(3).getItem()==null? null : individualRow.get(3).getItem().toString();
            String gradeRemarks = individualRow.get(4).getItem()==null? null : individualRow.get(4).getItem().toString().toUpperCase();
            //------------------
            if(gradeRating==null || gradeRemarks==null || gradeRating.isEmpty()) {
                System.out.println("GRADE IS NULL " + subjectCode);
                continue;
            }
            //----------------------

            // check if not for encoding then skip
            if (gradeRemarks.equalsIgnoreCase("not for encoding")) {
                continue;
            }
            

            //------------------------------------------------------------------
            // search subjects from the repository
            ArrayList<SubjectMapping> subjects = Mono.orm()
                    .newSearch(Database.connect().subject())
                    .eq(DB.subject().code, subjectCode)
                    .active()
                    .all();

            if (subjects == null) {
                System.out.println("SUBJECTS NOT FOUND");
                return false;
            }
            //------------------------------------------------------------------
            // identify the subject from the curriculum list
            SubjectMapping subject = null;
            for (SubjectMapping temp_subject : subjects) {
                //--------------------------------------------------------------
                //--------------------------------------------------------------
                Integer correctCurriculum = CurriculumFix
                        .getCorrectCurriculum(this.studentMapping, this.encodingYear);
                //--------------------------------------------------------------
                CurriculumSubjectMapping csMap = Mono.orm()
                        .newSearch(Database.connect().curriculum_subject())
                        .eq(DB.curriculum_subject().CURRICULUM_id, correctCurriculum)
                        .eq(DB.curriculum_subject().SUBJECT_id, temp_subject.getId())
                        .active()
                        .first();
                //--------------------------------------------------------------
                if (csMap != null) {
                    subject = temp_subject;
                    break;
                }
            }

            if (subject == null) {
                System.out.println("FAILED TO FIND SUBJECT IN CURRICULUM");
                return false;
            }
            //------------------------------------------------------------------
            // check grades for this subject
            // this can be posted or unposted as long as there is an existing grade
            GradeMapping existingGrade = Mono.orm()
                    .newSearch(Database.connect().grade())
                    .eq("STUDENT_id", this.CICT_id)
                    .eq("SUBJECT_id", subject.getId())
                    .active(Order.desc(DB.grade().id))
                    .first();

            // no existing grade
            if (existingGrade == null) {
                // insert new
                //--------------------------------------------------------------
                GradeMapping grades = new GradeMapping();
                grades.setSUBJECT_id(subject.getId());
                grades.setSTUDENT_id(this.CICT_id);
                grades.setACADTERM_id(null);
                grades.setRating(gradeRating);
                grades.setRemarks(gradeRemarks);
                //--------------------------------------------------------------
                grades.setCreated_by(FACULTY_id);
                grades.setCreated_date(serverDate);
                grades.setCredit_method("REGULAR");
                grades.setPosted_by(FACULTY_id);
                grades.setPosting_date(serverDate);
                grades.setPosted(1);
                grades.setActive(1);
                //--------------------------------------------------------------
                if (SubjectClassification.isCreditted(subject.getType())) {
                    // if creditted set credit value
                    grades.setCredit(Double.parseDouble(individualRow.get(2).getItem().toString()));
                }
                //--------------------------------------------------------------
                if (grades.getRating().equalsIgnoreCase("INC")) {
                    grades.setInc_expire(incExpireTime.getTime());
                }
                //--------------------------------------------------------------
                int newGradeID = Database.connect().grade().transactionalInsert(localSession, grades);
                if (newGradeID <= 0) {
                    // failed
                    dataTx.rollback();
                    throw new TransactionException("Cannot insert " + grades.getSUBJECT_id());
                    // this transaction will end
                    // whenFailed callback will be invoked
                }
                postedGradeCount++;

            } else {
                // update old and insert new
                // since editting of passed subjects are not allowed skip them
                if (existingGrade.getRemarks().equalsIgnoreCase("PASSED")) {
                    continue;
                }
                // if the values are the same skip update
                if (existingGrade.getRating().equalsIgnoreCase(gradeRating)) {
                    // same grade rating skip this record
                    continue;
                }
                // if not update the record
                GradeMapping newGrade = new GradeMapping();
                newGrade.setSUBJECT_id(existingGrade.getSUBJECT_id());
                newGrade.setSTUDENT_id(existingGrade.getSTUDENT_id());
                newGrade.setACADTERM_id(existingGrade.getACADTERM_id());
                newGrade.setRating(gradeRating); // new rating
                newGrade.setRemarks(gradeRemarks); // new remarks
                newGrade.setReason_for_update("UPDATED FAILED OR INCOMPLETE GRADE");
                //--------------------------------------------------------------
                // created meta
                newGrade.setCreated_by(existingGrade.getCreated_by() == null ? FACULTY_id : existingGrade.getCreated_by());
                newGrade.setCreated_date(existingGrade.getCreated_date() == null ? serverDate : existingGrade.getCreated_date());
                //--------------------------------------------------------------
                newGrade.setCredit_method(existingGrade.getCredit_method() == null ? "REGULAR" : existingGrade.getCredit_method());
                //--------------------------------------------------------------
                if (existingGrade.getPosted().equals(0)
                        || existingGrade.getPosted_by() == null
                        || existingGrade.getPosting_date() == null) {
                    newGrade.setReason_for_update("GRADE POSTING");
                    newGrade.setPosted_by(FACULTY_id);
                    newGrade.setPosting_date(serverDate);
                } else {
                    newGrade.setPosted_by(existingGrade.getPosted_by());
                    newGrade.setPosting_date(existingGrade.getPosting_date());
                }
                newGrade.setPosted(1);
                newGrade.setActive(1);
                //--------------------------------------------------------------
                newGrade.setUpdated_by(FACULTY_id);
                newGrade.setUpdated_date(serverDate);
                //--------------------------------------------------------------
                if (SubjectClassification.isCreditted(subject.getType())) {
                    // if creditted set credit value
                    newGrade.setCredit(Double.parseDouble(individualRow.get(2).getItem().toString()));
                }
                //--------------------------------------------------------------
                if (newGrade.getRating().equalsIgnoreCase("INC")) {
                    newGrade.setInc_expire(incExpireTime.getTime());
                }
                //--------------------------------------------------------------
                int updatedGradeID = Database.connect().grade().transactionalInsert(localSession, newGrade);
                if (updatedGradeID <= 0) {
                    // failed
                    dataTx.rollback();
                    throw new TransactionException("Cannot update " + newGrade.getSUBJECT_id());
                    // this transaction will end
                    // whenFailed callback will be invoked
                }

                // if the updated grade was inserted deactivate the old grade
                existingGrade.setActive(0);
                boolean deactivateOld = Database.connect().grade().transactionalSingleUpdate(localSession, existingGrade);
                if (!deactivateOld) {
                    dataTx.rollback();
                    throw new TransactionException("");
                }
                // if finished gracefully
                alreadyPostedGradeCount++;
            }

//            try {
//
//                try {
//
//                    if ("unposted".equalsIgnoreCase(this.MODE)) {
//                        existingGrade.setSUBJECT_id(subject.getId());
//                        existingGrade.setSTUDENT_id(this.CICT_id);
//                        if (ACAD_TERM_id != null) {
//                            existingGrade.setACADTERM_id(this.ACAD_TERM_id);
//                        }
//
//                        existingGrade.setRating(gradeRating);
//                        existingGrade.setRemarks(gradeRemarks);
//                        existingGrade.setPosted_by(FACULTY_id);
//                        existingGrade.setPosting_date(POSTED_DATE);
//                        existingGrade.setPosted(1);
//                        /**
//                         * Replaced is credited by this line of code
//                         */
//                        if (SubjectClassification.isCreditted(subject.getType())) {
//                            existingGrade.setCredit(Double.parseDouble(individualRow.get(2).getItem().toString()));
//                        }
//                        if (existingGrade.getRating().equalsIgnoreCase("INC")) {
//                            Calendar cal = Calendar.getInstance();
//                            cal.add(Calendar.YEAR, 1);
//                            existingGrade.setInc_expire(cal.getTime());
//                        }
//                        if (Database.connect().grade().update(existingGrade)) {
//                            log("UNPOSTED GRADE UPDATED");
//                        }
//                        this.POSTED = true;
//                    } else {
//                        Integer id = existingGrade.getSUBJECT_id();
//                        log("SUBJECT ID " + id + " ALREADY POSTED");
//                        alreadyPostedGradeCount++;
//                    }
//                } catch (NullPointerException es) {
//                    if (!gradeRemarks.equalsIgnoreCase("")) {
//
//                        this.POSTED = true;
//                    }
//                }
//
//                if (alreadyPostedGradeCount >= grid.getRowCount()) {
//                    this.beenModified = true;
//                }
//            } catch (NullPointerException ef) {
//                this.POSTED = false;
//                ef.printStackTrace();
//            }
        } // end of loop
//        if (this.POSTED) {
//            return true;
//        }
        if ((postedGradeCount + alreadyPostedGradeCount) == 0) {
            this.beenModified = true;
        }

        log("NUMBER OF POSTED GRADE: " + postedGradeCount);
        log("NUMBER OF ALREADY POSTED GRADE: " + alreadyPostedGradeCount);
//        this.POSTED = true;
        dataTx.commit();
        localSession.close();
        return true;
    }

    @Override
    protected void after() {

    }

    private void log(String str) {
        System.out.println("@EncodeGrade: " + str);
    }
}

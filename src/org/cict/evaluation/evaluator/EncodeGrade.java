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
package org.cict.evaluation.evaluator;

import app.lazy.models.CurriculumSubjectMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.GradeMapping;
import app.lazy.models.SubjectMapping;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javafx.collections.ObservableList;
import org.cict.SubjectClassification;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.controlsfx.control.spreadsheet.Grid;
import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetView;

/**
 *
 * @author Joemar
 */
public class EncodeGrade extends Transaction {

    public SpreadsheetView spreadSheet;
    public Integer CICT_id;
    public String MODE = "";
    public Integer CURRICULUM_id;

    private final Integer FACULTY_id = CollegeFaculty.instance().getFACULTY_ID();
    public Integer ACAD_TERM_id;
    private final Date POSTED_DATE = new Date();
    private boolean POSTED = false;
    private boolean ALREADY_POSTED = false;

    public boolean isAlreadyPosted() {
        return this.ALREADY_POSTED;
    }

    public boolean isPosted() {
        return this.POSTED;
    }

    @Override
    protected boolean transaction() {
        Grid grid = this.spreadSheet.getGrid();
        ObservableList<ObservableList<SpreadsheetCell>> cells = grid.getRows();
        int alreadyPostedGradeCount = 0, postedGradeCount = 0;
        for (int i = 0; i < grid.getRowCount(); i++) { //gets the info needed from the spreadsheet view and stores in the database
            GradeMapping grades = new GradeMapping();
            ObservableList<SpreadsheetCell> cell = cells.get(i);
            try {
                String remarks = cell.get(4).getItem().toString().toUpperCase();
                if(remarks.equalsIgnoreCase("not for encoding")) {
                    continue;
                }
                ArrayList<SubjectMapping> subjects = Mono.orm()
                        .newSearch(Database.connect().subject())
                        .eq("code", cell.get(0).getItem().toString())
                        .active()
                        .all();
                SubjectMapping subject = null;
                for(SubjectMapping temp_subject: subjects) {
                    CurriculumSubjectMapping csMap = Mono.orm().newSearch(Database.connect().curriculum_subject())
                            .eq(DB.curriculum_subject().CURRICULUM_id, this.CURRICULUM_id)
                            .eq(DB.curriculum_subject().SUBJECT_id, temp_subject.getId())
                            .active()
                            .first();
                    if(csMap != null) {
                        subject = temp_subject;
                        break;
                    }
                }
                try {
                    GradeMapping grade_posted = Mono.orm()
                            .newSearch(Database.connect().grade())
                            .eq("STUDENT_id", this.CICT_id)
                            .eq("SUBJECT_id", subject.getId())
                            .active()
                            .first();
                    if ("unposted".equalsIgnoreCase(this.MODE)) {
                        grade_posted.setSUBJECT_id(subject.getId());
                        grade_posted.setSTUDENT_id(this.CICT_id);
                        if(ACAD_TERM_id != null)
                            grade_posted.setACADTERM_id(this.ACAD_TERM_id);
                        String rtng_val = cell.get(3).getItem().toString();
                        grade_posted.setRating(rtng_val);
                        grade_posted.setRemarks(remarks);
                        grade_posted.setPosted_by(FACULTY_id);
                        grade_posted.setPosting_date(POSTED_DATE);
                        grade_posted.setPosted(1);
                        /**
                         * Replaced is credited by this line of code
                         */
                        if (SubjectClassification.isCreditted(subject.getType())) {
                            grade_posted.setCredit(Double.parseDouble(cell.get(2).getItem().toString()));
                        }
                        if (grade_posted.getRating().equalsIgnoreCase("INC")) {
                            Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.YEAR, 1);
                            grade_posted.setInc_expire(cal.getTime());
                        }
                        if (Database.connect().grade().update(grade_posted)) {
                            log("UNPOSTED GRADE UPDATED");
                        }
                        this.POSTED = true;
                    } else {
                        Integer id = grade_posted.getSUBJECT_id();
                        log("SUBJECT ID " + id + " ALREADY POSTED");
                        alreadyPostedGradeCount++;
                    }
                } catch (NullPointerException es) {
                    if(!remarks.equalsIgnoreCase("")) {
                        grades.setSUBJECT_id(subject.getId());
                        grades.setSTUDENT_id(this.CICT_id);
                        if(ACAD_TERM_id != null)
                            grades.setACADTERM_id(this.ACAD_TERM_id);
                        String rtng_val = cell.get(3).getItem().toString();
                        grades.setRating(rtng_val);
                        grades.setRemarks(remarks);
                        grades.setPosted_by(FACULTY_id);
                        grades.setPosting_date(POSTED_DATE);
                        grades.setPosted(1);
                        /**
                         * Replaced is credited by this line of code
                         */
                        if (SubjectClassification.isCreditted(subject.getType())) {
                            grades.setCredit(Double.parseDouble(cell.get(2).getItem().toString()));
                        }
                        if (grades.getRating().equalsIgnoreCase("INC")) {
                            Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.YEAR, 1);
                            grades.setInc_expire(cal.getTime());
                        }
                        if (Database.connect().grade().insert(grades) != -1) {
                            postedGradeCount++;
                        }
                        this.POSTED = true;
                    }
                }

                if (alreadyPostedGradeCount >= grid.getRowCount()) {
                    this.ALREADY_POSTED = true;
                }
            } catch (NullPointerException ef) {
                this.POSTED = false;
                ef.printStackTrace();
            }

        }
        if (this.POSTED) {
            return true;
        }
        log("NUMBER OF POSTED GRADE: " + postedGradeCount);
        log("NUMBER OF ALREADY POSTED GRADE: " + alreadyPostedGradeCount);
        return false;
    }

    @Override
    protected void after() {

    }

    private void log(String str) {
        System.out.println("@EncodeGrade: " + str);
    }
}

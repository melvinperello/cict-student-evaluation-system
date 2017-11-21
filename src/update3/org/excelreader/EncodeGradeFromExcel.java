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
package update3.org.excelreader;

import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.GradeMapping;
import app.lazy.models.LoadSubjectMapping;
import app.lazy.models.StudentMapping;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import java.util.ArrayList;
import java.util.Date;
import org.cict.authentication.authenticator.SystemProperties;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

/**
 *
 * @author Joemar
 */
public class EncodeGradeFromExcel extends Transaction{
    private Integer LDGRP_id;
    private Integer SUBJECT_id;
    private Integer FACULTY_id;
    private ArrayList<ReadData> importedData;

    public void setLDGRP_id(Integer LDGRP_id) {
        this.LDGRP_id = LDGRP_id;
    }

    public void setSUBJECT_id(Integer SUBJECT_id) {
        this.SUBJECT_id = SUBJECT_id;
    }

    public void setFACULTY_id(Integer FACULTY_id) {
        this.FACULTY_id = FACULTY_id;
    }

    public void setImportedData(ArrayList<ReadData> importedData) {
        this.importedData = importedData;
    }

    public ArrayList<ReadData> getImportedData() {
        return importedData;
    }
    
    private void log(String str) {
        System.out.println("@EncodeGradeFromExcel: " + str);
    }

    @Override
    protected boolean transaction() {
        // create local session
        Session currentSession = Mono.orm().session();
        // start your transaction
        org.hibernate.Transaction dataTransaction = currentSession.beginTransaction();
        log("session started...");
        Integer ACADTERM_id = SystemProperties.instance().getCurrentAcademicTerm().getId();
        for(ReadData data: importedData) {
            StudentMapping studentExist = Mono.orm().newSearch(Database.connect().student())
                    .eq(DB.student().id, data.getSTUDENT_NUMBER())
                    .active(Order.desc(DB.student().cict_id)).first();
            if(studentExist==null) {
                data.setSTATUS("STUDENT NOT FOUND");
                log("student not found");
                continue;
            }
            
            LoadSubjectMapping loadSubject = Mono.orm().newSearch(Database.connect().load_subject())
                    .eq(DB.load_subject().LOADGRP_id, LDGRP_id)
                    .eq(DB.load_subject().STUDENT_id, studentExist.getCict_id())
                    .eq(DB.load_subject().SUBJECT_id, SUBJECT_id).active(Order.desc(DB.load_subject().id)).first();
            if(loadSubject==null) {
                data.setSTATUS("NOT ENROLLED");
                log("student not enrolled ...");
                continue;
            }
            
            String remarks = this.getRemarks(data.getSTUDENT_GRADE());
            if(remarks.equalsIgnoreCase("Unrecognized Grade")) {
                data.setSTATUS(remarks.toUpperCase());
                log(remarks);
                continue;
            } else if(remarks.equalsIgnoreCase("No Grade Found")) {
                data.setSTATUS(remarks.toUpperCase());
                log(remarks);
                continue;
            }
            Date CURRENT_DATE = Mono.orm().getServerTime().getDateWithFormat();
            GradeMapping gradeExist = Mono.orm().newSearch(Database.connect().grade())
                    .eq(DB.grade().ACADTERM_id, ACADTERM_id)
                    .eq(DB.grade().STUDENT_id, studentExist.getCict_id())
                    .eq(DB.grade().SUBJECT_id, SUBJECT_id).active(Order.desc(DB.grade().id)).first();
            if(gradeExist!=null) {
                if(gradeExist.getPosted()!=null && gradeExist.getPosted().equals(1)) {
                    data.setSTATUS("Already posted".toUpperCase());
                    log("already posted...");
                    continue;
                }
                gradeExist.setRating(data.getSTUDENT_GRADE());
                gradeExist.setRemarks(remarks);
                gradeExist.setReason_for_update("Posted by Faculty-in-charge");
                gradeExist.setUpdated_by(FACULTY_id);
                gradeExist.setUpdated_date(CURRENT_DATE);
                gradeExist.setPosted(1);
                gradeExist.setPosted_by(FACULTY_id);
                gradeExist.setPosting_date(CURRENT_DATE);
                // update here
                boolean res = Database.connect().grade().transactionalSingleUpdate(currentSession, gradeExist);
                if(!res) {
                    dataTransaction.rollback();
                    log("ROLLBACK ...");
                    return false;
                }
                data.setSTATUS("POSTED");
                log("POSTED...");
                continue;
            }
            GradeMapping encodeGrade = new GradeMapping();
            encodeGrade.setACADTERM_id(ACADTERM_id);
            encodeGrade.setSTUDENT_id(studentExist.getCict_id());
            encodeGrade.setSUBJECT_id(SUBJECT_id);
            encodeGrade.setActive(1);
            encodeGrade.setPosted(1);
            encodeGrade.setPosted_by(FACULTY_id);
            encodeGrade.setPosting_date(CURRENT_DATE);
            encodeGrade.setRating(data.getSTUDENT_GRADE());
            encodeGrade.setRemarks(remarks);
            encodeGrade.setCreated_by(FACULTY_id);
            encodeGrade.setCreated_date(CURRENT_DATE);
            // insert here
            Integer res = Database.connect().grade().transactionalInsert(currentSession, encodeGrade);
            if(res.equals(-1)) {
                dataTransaction.rollback();
                log("ROLLBACK 2 ...");
                return false;
            }
            data.setSTATUS("POSTED");
            log("POSTED 2...");
        }
        dataTransaction.commit();
        log("COMMIT ...");
        return true;
    }
    
    private String getRemarks(String grade) {
        if(grade==null || grade.isEmpty()) {
            return "NO GRADE FOUND";
        }
        if (grade instanceof String) {
            String rating = (String) grade;
            if (rating.equalsIgnoreCase("INC") || rating.equalsIgnoreCase("Incomplete")) {
                return "Incomplete";
            } else if (rating.equalsIgnoreCase("DRP") || rating.equalsIgnoreCase("Dropped")) {
                return "Dropped";
            } else if (rating.equalsIgnoreCase("UD") || rating.equalsIgnoreCase("Unofficially Dropped")) {
                return "Unofficially Dropped";
            }
        }
        try {
            double g = Double.parseDouble(grade);
            if(g == 1.00 || g == 1.25 || g == 1.50 || g == 1.75 
                    || g == 2.00 || g == 2.25 || g == 2.50 || g == 2.75 || g == 3.00) {
                return "Passed";
            }
        } catch (NumberFormatException e) {
        }
        
        return "Unrecognized Grade";
    }
}

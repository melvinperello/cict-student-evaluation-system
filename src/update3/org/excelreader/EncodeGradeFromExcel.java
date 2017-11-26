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
import java.text.DecimalFormat;
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
        if(true)
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
//            Integer cleared = data.getSTUDENT_CLEARANCE().equals("1") || data.getSTUDENT_CLEARANCE().equalsIgnoreCase("YES")? 1 : 0;

//            if(!loadSubject.getCleared().equals(cleared)) {
//                loadSubject.setCleared(cleared);
//                boolean res = Database.connect().load_subject().transactionalSingleUpdate(currentSession, loadSubject);
//                if(!res) {
//                    dataTransaction.rollback();
//                    log("ROLLBACK ...");
//                    return false;
//                } else 
//                    log("load subject updated ...");
//            }
            String[] gradeInfo = this.getRemarks(data.getSTUDENT_GRADE());
            if(gradeInfo[0].equalsIgnoreCase("Unrecognized Grade")) {
                data.setSTATUS(gradeInfo[0].toUpperCase());
                log(gradeInfo[0]);
                continue;
            } else if(gradeInfo[0].equalsIgnoreCase("No Grade Found")) {
                data.setSTATUS(gradeInfo[0].toUpperCase());
                log(gradeInfo[0]);
                continue;
            }
            data.setSTUDENT_GRADE(gradeInfo[0]);
            Date CURRENT_DATE = Mono.orm().getServerTime().getDateWithFormat();
            GradeMapping gradeExist = Mono.orm().newSearch(Database.connect().grade())
                    .eq(DB.grade().ACADTERM_id, ACADTERM_id)
                    .eq(DB.grade().STUDENT_id, studentExist.getCict_id())
                    .eq(DB.grade().SUBJECT_id, SUBJECT_id).active(Order.desc(DB.grade().id)).first();
            if(gradeExist!=null) {
                if(gradeExist.getPosted()!=null && gradeExist.getPosted().equals(1)) {
                    data.setSTUDENT_GRADE(gradeExist.getRating());
                    data.setSTATUS("Already posted".toUpperCase());
                    log("already posted...");
                    continue;
                }
                gradeExist.setRating(gradeInfo[0]);
                gradeExist.setRemarks(gradeInfo[1].toUpperCase());
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
            encodeGrade.setRating(gradeInfo[0]);
            encodeGrade.setRemarks(gradeInfo[1].toUpperCase());
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
    
    private String[] getRemarks(String grade) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        if(grade==null || grade.isEmpty()) {
            return new String[]{"NO GRADE FOUND", ""};
        }
        if (grade instanceof String) {
            String rating = (String) grade;
            if (rating.equalsIgnoreCase("INC") || rating.equalsIgnoreCase("Incomplete")) {
                return new String[]{"INC", "Incomplete"};
            } else if (rating.equalsIgnoreCase("DRP") || rating.equalsIgnoreCase("Dropped")) {
                return new String[]{"DRP", "Dropped"};
            } else if (rating.equalsIgnoreCase("UD") || rating.equalsIgnoreCase("Unofficially Dropped")) {
                return new String[]{"UD", "Unofficially Dropped"};
            }
        }
        try {
            Double g = Double.valueOf(grade);
            if(g.equals(1.00) || g.equals(1.25) || g.equals(1.50) || g.equals(1.75) 
                    || g.equals(2.00) || g.equals(2.25) || g.equals(2.50) || g.equals(2.75) || g.equals(3.00)) {
                return new String[]{df.format(g), "Passed"};
            } else if(g.equals(4.00)) {
                return new String[]{df.format(g), "Conditionally Passed"};
            }else if(g.equals(5.00)) {
                return new String[]{df.format(g), "Failed"};
            }
        } catch (NumberFormatException e) {
        }
        return new String[]{"Unrecognized Grade", ""};
    }
}

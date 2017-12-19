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
package update2.org.cict.controller.subjects;

import app.lazy.models.CurriculumMapping;
import app.lazy.models.CurriculumRequisiteExtMapping;
import app.lazy.models.CurriculumRequisiteLineMapping;
import app.lazy.models.CurriculumSubjectMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.SubjectMapping;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import org.cict.SubjectClassification;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.hibernate.criterion.Order;

/**
 *
 * @author Joemar
 */
public class FetchSubjectInformation extends Transaction{

    public Integer subjectID;
    private CurriculumSubjectMapping SUBJECT_csMap;
    private ArrayList<SubjectMapping> prereqs = new ArrayList<>();
    private ArrayList<SubjectMapping> coreqs = new ArrayList<>();
    private ArrayList<CurriculumMapping> curriculums;
    private Integer CURRICULUM_id;
    private Date REMOVED_DATE;// = Mono.orm().getServerTime().getDateWithFormat();
    
    public void setCurriculumID(Integer id) {
        this.CURRICULUM_id = id;
    }
    
    public ArrayList<CurriculumMapping> getCurriculums() {
        return curriculums;
    }
    
    public ArrayList<SubjectMapping> getSubjectPrerequisite() {
        return prereqs;
    }
    
    public ArrayList<SubjectMapping> getSubjectCorequisite() {
        return coreqs;
    }
    
    @Override
    protected boolean transaction() {
        /**
         * CURRICULUM **************************************************************
         */
        ArrayList<CurriculumSubjectMapping> curriculumSubjects = Mono.orm().newSearch(Database.connect().curriculum_subject())
                .eq(DB.curriculum_subject().SUBJECT_id, subjectID)
                .active(Order.desc(DB.curriculum_subject().id))
                .all();
        if(curriculumSubjects == null) {
            logs("NO CURRICULUM SUBJECT FOUND, SUBJECT ID: " + subjectID);
            return false;
        }
        
        curriculums = new ArrayList<>();
        for(CurriculumSubjectMapping curriculumSubject: curriculumSubjects) {
            
            CurriculumMapping curriculum = Mono.orm().newSearch(Database.connect().curriculum())
                    .eq(DB.curriculum().id, curriculumSubject.getCURRICULUM_id())
                    .active(Order.desc(DB.curriculum().id))
                    .first();
            curriculums.add(curriculum);
        }
        
        if(this.CURRICULUM_id == null)
            return true;
        
        /**
         * PRE-REQUISITE ******************************************************
         */
        ArrayList<CurriculumRequisiteLineMapping> crlMaps = Mono.orm().newSearch(Database.connect().curriculum_requisite_line())
                .eq(DB.curriculum_requisite_line().SUBJECT_id_get, this.subjectID)
                .eq(DB.curriculum_requisite_line().CURRICULUM_id, this.CURRICULUM_id)
                .active(Order.asc(DB.curriculum_requisite_line().id))
                .all();
        if(crlMaps != null) {
            for(CurriculumRequisiteLineMapping crlMap: crlMaps){
                    SubjectMapping subjectPreReq = Mono.orm().newSearch(Database.connect().subject())
                            .eq(DB.subject().id, crlMap.getSUBJECT_id_req())
                            .active()
                            .first();
                    if(subjectPreReq != null) {
                        prereqs.add(subjectPreReq);
                    } else {
                        // if no active subject found, set inactive
                        crlMap.setActive(0);
                        crlMap.setRemoved_by(null); // when removed by is null, means done by the system itself
                        REMOVED_DATE = Mono.orm().getServerTime().getDateWithFormat();
                        crlMap.setRemoved_date(REMOVED_DATE);
                        Database.connect().curriculum_requisite_line().update(crlMap);
                    }
            }
        } else {
            logs("NO PRE-REQUISITE FOUND FOR SUBJECT ID: " + this.subjectID);
        }
        
        /**
         * CO-REQUISITE ******************************************************
         */
        ArrayList<CurriculumRequisiteExtMapping> creMaps = Mono.orm().newSearch(Database.connect().curriculum_requisite_ext())
                .eq(DB.curriculum_requisite_ext().SUBJECT_id_get, this.subjectID)
                .eq(DB.curriculum_requisite_ext().CURRICULUM_id, this.CURRICULUM_id)
                .eq(DB.curriculum_requisite_ext().type, SubjectClassification.REQUITE_TYPE_CO)
                .active(Order.asc(DB.curriculum_requisite_ext().id))
                .all();
        if(creMaps != null) {
            for(CurriculumRequisiteExtMapping creMap: creMaps){
                    SubjectMapping subjectCoReq = Mono.orm().newSearch(Database.connect().subject())
                            .eq(DB.subject().id, creMap.getSUBJECT_id_req())
                            .active()
                            .first();
                    if(subjectCoReq != null) {
                        coreqs.add(subjectCoReq);
                    } else {
                        // if no active subject found, set inactive
                        creMap.setActive(0);
                        creMap.setRemoved_by(null); // when removed by is null, means done by the system itself
                        REMOVED_DATE = Mono.orm().getServerTime().getDateWithFormat();
                        creMap.setRemoved_date(REMOVED_DATE);
                        Database.connect().curriculum_requisite_line().update(creMap);
                    }
            }
        } else
            logs("NO CO-REQUISITE FOUND FOR SUBJECT ID: " + this.subjectID);
        
        return true;
    }

    @Override
    protected void after() {
    
    }
    
    private void logs(String str) {
        boolean logging = true;
        if(logging) {
            System.out.println("@FetchSubjectInformation: " + str);
        }
    }
}

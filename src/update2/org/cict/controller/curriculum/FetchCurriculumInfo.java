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
package update2.org.cict.controller.curriculum;

import app.lazy.models.CurriculumMapping;
import app.lazy.models.CurriculumPreMapping;
import app.lazy.models.CurriculumSubjectMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.FacultyMapping;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import com.jhmvin.fx.async.TransactionException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Distinct;
import org.hibernate.criterion.Order;

/**
 *
 * @author Joemar
 */
public class FetchCurriculumInfo extends Transaction{

    public CurriculumMapping curriculum;
    
    private String createdBy;
    public String getCreatedByFirstLastName() {
        
        return createdBy;
    }
    
    private String implementedBy;
    public String getImplementedByFirstLastName() {
        return implementedBy;
    }
    
    private ArrayList<CurriculumMapping> curriculumPreReqs;
    public ArrayList<CurriculumMapping> getPreReqCurriculums() {
        return curriculumPreReqs;
    }
    
    private Boolean isLadderized;
    public Boolean isLadderized() {
        return isLadderized;
    }
    
    private String status;
    public String getStatus() {
        return status;
    }
    
    private boolean isImplemented;
    public boolean isImplemented() {
        return isImplemented;
    }
    
    private SimpleDateFormat formatter = new SimpleDateFormat("MMMMM dd, yyyy HH:mm:ss aaa");
    private Date createdDate;
    public String getCreatedDateWithFormat() {
        if(createdDate != null)
            return formatter.format(createdDate);
        else
            return "N / A";
    }
    
    private Date implementedDate;
    public String getImplementedDateWithFormat() {
        if(implementedDate != null)
            return formatter.format(implementedDate);
        else 
            return "N / A";
    }
    
    private String PREPARATORY;
    public String getPreparatory() {
        return PREPARATORY;
    }
    
    @Override
    protected boolean transaction() {
        /**
         * created by and date
         */
        FacultyMapping createdByFaculty = (curriculum.getCreated_by()==null? null: Database.connect().faculty().getPrimary(curriculum.getCreated_by()));
                /*Mono.orm().newSearch(Database.connect().faculty())
                .eq(DB.faculty().id, curriculum.getCreated_by())
                .active()
                .first();*/
        if(createdByFaculty == null) {
            this.createdBy = "N / A";
        } else 
            this.createdBy = createdByFaculty.getFirst_name() + " " + createdByFaculty.getLast_name();
        try {
            this.createdDate = curriculum.getCreated_date();
        } catch(NullPointerException a) {
            this.createdDate = null;
        }
        
        /**
         * status, implemented by and date
         */
        if(curriculum.getImplemented() == 1) {
            isImplemented = true;
            status = "IMPLEMENTED";
            try {
                FacultyMapping implementedByFaculty= (curriculum.getImplemented_by()==null? null: Database.connect().faculty().getPrimary(curriculum.getImplemented_by()));
                /*Mono.orm().newSearch(Database.connect().faculty())
                        .eq(DB.faculty().id, curriculum.getImplemented_by())
                        .active()
                        .first();*/
                if(implementedByFaculty == null) {
                    this.implementedBy = "N / A";
                } else 
                    this.implementedBy = implementedByFaculty.getFirst_name() + " " + implementedByFaculty.getLast_name();
                try {
                    this.implementedDate = curriculum.getImplementation_date();
                } catch(NullPointerException a) {
                    this.implementedDate = null;
                }
            }catch(NullPointerException a) {
                this.implementedBy = "N / A";
                this.implementedDate = null;
            } 
        } else {
            isImplemented = false;
            status = "UNIMPLEMENTED";
            this.implementedBy = "N / A";
            this.implementedDate = null;
        }
        
        /**
         * ladderized and pre req curriculums
         */
        if(curriculum.getLadderization().equalsIgnoreCase("YES")) {
            isLadderized = true;
            ArrayList<CurriculumPreMapping> curriculumPres = Mono.orm().newSearch(Database.connect().curriculum_pre())
                    .eq(DB.curriculum_pre().curriculum_id_get, curriculum.getId())
                    .active()
                    .all();
            if(curriculumPres != null) {
                curriculumPreReqs = new ArrayList<>();
                for(CurriculumPreMapping curriculumPre: curriculumPres) {
                    if(curriculumPre.getCur_type().equalsIgnoreCase("EQUIVALENT")) {
                        CurriculumMapping curriculumPreReq = Mono.orm().newSearch(Database.connect().curriculum())
                                .eq(DB.curriculum().id, curriculumPre.getCurriculum_id_req())
                                .active(Order.desc(DB.curriculum().id))
                                .first();
                        curriculumPreReqs.add(curriculumPreReq);
                    }
                    
                    if(curriculumPre.getCur_type().equalsIgnoreCase("PRIMARY")) {
                        CurriculumMapping curriculumPreReq = Mono.orm().newSearch(Database.connect().curriculum())
                                .eq(DB.curriculum().id, curriculumPre.getCurriculum_id_req())
                                .active(Order.desc(DB.curriculum().id))
                                .first();
                        PREPARATORY = curriculumPreReq.getName();
                    }
                }
            }
        } else {
            isLadderized = false;
        }
        
        return true;
    }

    @Override
    protected void after() {
    
    }
    
}

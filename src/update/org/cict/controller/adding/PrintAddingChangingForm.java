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
package update.org.cict.controller.adding;

import app.lazy.models.AcademicProgramMapping;
import app.lazy.models.CurriculumMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.EvaluationMapping;
import app.lazy.models.LoadGroupMapping;
import app.lazy.models.LoadSectionMapping;
import app.lazy.models.LoadSubjectMapping;
import app.lazy.models.StudentMapping;
import app.lazy.models.SubjectMapping;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import org.hibernate.criterion.Order;
import update.org.cict.reports.add_change_form.AddChangeForm;

/**
 *
 * @author Joemar
 */
public class PrintAddingChangingForm extends Transaction{

    public Integer evaluationID;
    public Integer studentID;
    public ArrayList<Object[]> details;
    
    private EvaluationMapping evaluation;
    private StudentMapping student;
    private ArrayList<SubjectInformationHolder> alteredSubjects = new ArrayList<>();
    private AcademicProgramMapping academicProgram;
    private EvaluationMapping evaluationReference;
    private Boolean isAdded = false, 
            isChanged = false;
    
    @Override
    protected boolean transaction() {
        student = Mono.orm().newSearch(Database.connect().student())
                .eq(DB.student().cict_id, this.studentID)
                .execute()
                .first();
        
        CurriculumMapping curriculum = Mono.orm().newSearch(Database.connect().curriculum())
                .eq(DB.curriculum().id, this.student.getCURRICULUM_id())
                .execute()
                .first();
        
        academicProgram = Mono.orm().newSearch(Database.connect().academic_program())
                .eq(DB.academic_program().id, curriculum.getACADPROG_id())
                .execute()
                .first();
        
        evaluation = Mono.orm()
                .newSearch(Database.connect().evaluation())
                .eq(DB.evaluation().id, this.evaluationID)
                .active()
                .first();
        
        Integer referenceID = evaluation.getAdding_reference_id();
        evaluationReference = Mono.orm()
                .newSearch(Database.connect().evaluation())
                .eq(DB.evaluation().id, referenceID)
                .execute()
                .first();
        if(evaluationReference == null) {
            System.out.println("No evaluation reference found. ID: " + referenceID);
            return false;
        }
        /**
         * get the evaluated subjects 
         */
        ArrayList<LoadSubjectMapping> evaluatedSubjects = Mono.orm()
                .newSearch(Database.connect().load_subject())
                .eq(DB.load_subject().EVALUATION_id, this.evaluationID)
                .eq(DB.load_subject().remarks, "ACCEPTED")
                .active()
                .all();
        
        /**
         * get the previous subjects
         */
        ArrayList<LoadSubjectMapping> referenceSubjects = Mono.orm()
                .newSearch(Database.connect().load_subject())
                .eq(DB.load_subject().EVALUATION_id, this.evaluationReference.getId())
//                .eq(DB.load_subject().remarks, "ADDING_CHANGING")
                .execute()
                .all();
        
        for(LoadSubjectMapping currentEvaluatedSubject: evaluatedSubjects) {
            boolean canAdd = true;
            for(LoadSubjectMapping currentReferenceSubject: referenceSubjects) {
                if(Objects.equals(currentEvaluatedSubject.getSUBJECT_id(), currentReferenceSubject.getSUBJECT_id())) {
                    canAdd = false;
                } else {
                    
                }
            }
            
            if(canAdd)
                isAdded = true;
            
            SubjectMapping subjectMap_changed = null;
            LoadSectionMapping lsMap_changed = null;
            LoadGroupMapping lgMap_changed = null;
            if(currentEvaluatedSubject.getChanging_reference() != null) {
                System.out.println("SUBJECT IS CHANGED");
                isChanged = true;
                lgMap_changed = Mono.orm()
                    .newSearch(Database.connect().load_group())
                    .eq(DB.load_group().id, currentEvaluatedSubject.getChanging_reference())
                    .execute()
                    .first();
                 subjectMap_changed = Mono.orm()
                    .newSearch(Database.connect().subject())
                    .eq(DB.subject().id, lgMap_changed.getSUBJECT_id())
                    .execute()
                    .first();
                 lsMap_changed = Mono.orm().newSearch(Database.connect().load_section())
                    .eq(DB.load_section().id, lgMap_changed.getLOADSEC_id())
                    .execute(Order.desc(DB.load_section().id))
                    .first();
                 
                 if(canAdd)
                    isAdded = false;
                 
                 canAdd = true;
            }
            
            
            if(canAdd) {
                SubjectMapping subjectMap = Mono.orm()
                        .newSearch(Database.connect().subject())
                        .eq(DB.subject().id, currentEvaluatedSubject.getSUBJECT_id())
                        .execute()
                        .first();
                if(subjectMap == null) {
                    System.out.println("No subject found. ID: " + currentEvaluatedSubject.getSUBJECT_id());
                    return false;
                }
                LoadSubjectMapping loadSubject = Mono.orm()
                        .newSearch(Database.connect().load_subject())
                        .eq(DB.load_subject().EVALUATION_id, this.evaluationID)
                        .eq(DB.load_subject().SUBJECT_id, subjectMap.getId())
                        .eq(DB.load_subject().STUDENT_id, this.studentID)
                        .eq(DB.load_subject().remarks, "ACCEPTED")
                        .active(Order.desc(DB.load_subject().id))
                        .first();
                if(loadSubject == null) {
                    System.out.println("Load subject is null");
                    System.out.println("EVALUATION_id: " + this.evaluationID);
                    System.out.println("SUBJECT_id: " + subjectMap.getId());
                    System.out.println("STUDENT_id: " + this.studentID);
                    System.out.println("remarks: ACCEPTED");
                    return false;
                }
                LoadGroupMapping loadGroup = Mono.orm().newSearch(Database.connect().load_group())
                        .eq(DB.load_group().id, loadSubject.getLOADGRP_id())
                        .execute(Order.desc(DB.load_group().id))
                        .first();
                LoadSectionMapping loadSection = Mono.orm().newSearch(Database.connect().load_section())
                        .eq(DB.load_section().id, loadGroup.getLOADSEC_id())
                        .execute(Order.desc(DB.load_section().id))
                        .first();
                
                
                
                AcademicProgramMapping acadProgram = Mono.orm().newSearch(Database.connect().academic_program())
                        .eq(DB.academic_program().id, loadSection.getACADPROG_id())
                        .execute()
                        .first();
                SubjectInformationHolder subInfo = new SubjectInformationHolder();
                subInfo.setSubjectMap(subjectMap);
                subInfo.setLoadGroup(loadGroup);
                subInfo.setSectionMap(loadSection);
                subInfo.setAcademicProgramMapping(acadProgram);
                subInfo.setLoadGroup_changed(lgMap_changed);
                //add subject to list
                alteredSubjects.add(subInfo);
            }
        }
        
        
        if(alteredSubjects.isEmpty()) {
            willPrint = false;
            return false;
        } else 
            willPrint = true;
        return true;
    }
    
    private boolean willPrint;
    public boolean isPrinting() {
        return willPrint;
    }

    @Override
    protected void after() {
        Date date_now = Mono.orm().getServerTime().getDateWithFormat();
        
        String studentFullName = student.getLast_name() + ", " + student.getFirst_name() + " " + student.getMiddle_name();
        String section = academicProgram.getCode() + " " + student.getYear_level() + student.getSection() 
                + "-G" + student.get_group();
        String filename = student.getId() + "_" + Mono.orm()
                        .getServerTime()
                        .getCalendar()
                        .getTimeInMillis();
        AddChangeForm addChangeForm = new AddChangeForm(filename);
        addChangeForm.STUDENT_NUMBER = student.getId();
        addChangeForm.STUDENT_NAME = studentFullName;
        addChangeForm.STUDENT_SECTION = section;
        addChangeForm.COLLEGE_DEAN = "ENGR. NOEMI REYES";
        addChangeForm.DATE = date_now;
        addChangeForm.STUDENT_CURRENT_UNITS = String.valueOf(getCurrentUnits());
        boolean canPrint = false;
        for(SubjectInformationHolder currentSubjectHolder: alteredSubjects) {
            SubjectMapping subjectMapp = currentSubjectHolder.getSubjectMap();
            String subjectCode = subjectMapp.getCode();
            String subjectSection = currentSubjectHolder.getFullSectionName();
            LoadGroupMapping lgMap_changed = currentSubjectHolder.getLoadGroup_changed();
            
            String subjectCode_from = "";
            String subjectSection_from = ""; 
            String units_from = "";
            if(lgMap_changed != null) {
                SubjectMapping subjectMap_changed = Mono.orm()
                        .newSearch(Database.connect().subject())
                        .eq(DB.subject().id, lgMap_changed.getSUBJECT_id())
                        .execute()
                        .first();
                subjectCode_from = subjectMap_changed.getCode();
                LoadSectionMapping lsMap_changed = Mono.orm().newSearch(Database.connect().load_section())
                        .eq(DB.load_section().id, lgMap_changed.getLOADSEC_id())
                        .execute(Order.desc(DB.load_section().id))
                        .first();
                subjectSection_from = currentSubjectHolder.getFullSectionNameOfChanged();
                units_from = String.valueOf(subjectMap_changed.getLab_units() + subjectMap_changed.getLec_units());
            }
            String totalUnits = String.valueOf((subjectMapp.getLec_units() + subjectMapp.getLab_units()));
            String[] subjectData = {subjectCode, subjectSection, totalUnits, subjectCode_from, subjectSection_from, units_from};
            addChangeForm.STUDENT_SUBJECTS.add(subjectData);
            addChangeForm.IS_ADDED = isAdded;
            addChangeForm.IS_CHANGED = isChanged;
            addChangeForm.REGISTRAR = "LEILANI M. LIZARDO";
            canPrint = true;
        }
        if(canPrint)
            addChangeForm.print();
        else
            System.out.println("Nothing to print");
    }
    
    private double getCurrentUnits() {
        double totalCurrentUnits = 0.0;
        ArrayList<LoadSubjectMapping> loadSubjects = Mono.orm()
                .newSearch(Database.connect().load_subject())
                .eq(DB.load_subject().EVALUATION_id, this.evaluationReference.getId())
                .eq(DB.load_subject().STUDENT_id, this.studentID)
                .eq(DB.load_subject().remarks, "ADDING_CHANGING")
                .execute(Order.desc(DB.load_subject().id))
                .all();
        
        if(loadSubjects == null) {
            System.out.println("EVALUATION_id: " +  this.evaluationReference.getId());
            System.out.println("STUDENT_id: " + studentID);
            return totalCurrentUnits;
        }
        
        for(LoadSubjectMapping loadSubject: loadSubjects) {
                SubjectMapping subjectMap = Mono.orm()
                        .newSearch(Database.connect().subject())
                        .eq(DB.subject().id, loadSubject.getSUBJECT_id())
                        .execute()
                        .first();
                totalCurrentUnits += (subjectMap.getLec_units() + subjectMap.getLab_units());
        }
        return totalCurrentUnits;
    }
}

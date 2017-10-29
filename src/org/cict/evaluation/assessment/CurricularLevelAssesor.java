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
 * JOEMAR N. DELA CRUZ
 * GRETHEL EINSTEIN BERNARDINO
 *
 * OTHER LIBRARIES THAT ARE USED BELONGS TO THEIR RESPECTFUL OWNERS AND AUTHORS.
 * NO COPYRIGHT ARE INTENTIONAL OR INTENDED.
 * THIS PROJECT IS NOT PROFITABLE HENCE FOR EDUCATIONAL PURPOSES ONLY.
 * THIS PROJECT IS ONLY FOR COMPLIANCE TO OUR REQUIREMENTS.
 * THIS PROJECT DOES NOT INCLUDE DISTRIBUTION FOR OTHER PURPOSES.
 *
 */
package org.cict.evaluation.assessment;

import app.lazy.models.AcademicTermMapping;
import app.lazy.models.CurriculumMapping;
import app.lazy.models.CurriculumRequisiteLineMapping;
import app.lazy.models.CurriculumSubjectMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.GradeMapping;
import app.lazy.models.MapFactory;
import app.lazy.models.StudentMapping;
import app.lazy.models.SubjectMapping;
import com.jhmvin.Mono;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javafx.application.Platform;
import org.cict.PublicConstants;
import org.hibernate.criterion.Order;
import sys.org.cict.enumerations.CurriculumValues;

/**
 *
 * @author Jhon Melvin
 */
public class CurricularLevelAssesor {

    private final StudentMapping studentMap;
    private final ArrayList<SubjectAssessmentDetials> assessmentDetails;
    /**
     * For errors
     */
    private boolean assessmentComplete = true;
    private String assessmentMessage = "";

    /**
     * Curriculum
     */
    private CurriculumMapping prepCurriculum;
    private CurriculumMapping consCurriculum;
    private boolean hasPrepData;

    //misc
    private boolean includeAcadTerm = false;

    public CurriculumMapping getPrepCurriculum() {
        return prepCurriculum;
    }

    public CurriculumMapping getConsCurriculum() {
        return consCurriculum;
    }

    public boolean hasPrepData() {
        return hasPrepData;
    }

    public CurricularLevelAssesor(StudentMapping studentMap) {
        this.assessmentDetails = new ArrayList<>();
        this.studentMap = studentMap;
    }

    public void setIncludeAcademicTermTable(boolean include) {
        this.includeAcadTerm = include;
    }

    private Date serverTime;

    /**
     * Assess the student's standing.
     *
     * @return true if success and false if error was encoutered.
     */
    public boolean assess() {
        this.serverTime = Mono.orm().getServerTime().getDateWithFormat();
        hasPrepData = false;
        this.assessmentDetails.clear();
        // check expired grades before assessing.

        // asses grades
        try {
            this.checkExpired();
            // if check of expired was failed will not proceed here
            this.assessGrades();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the assessment values for a specific year.
     *
     * @param year
     * @return
     */
    public AssessmentResults getAnnualAssessment(Integer year) {
        // create a sublist for a specific year
        ArrayList<SubjectAssessmentDetials> subList = new ArrayList<>();
        // filter the annual values
        for (SubjectAssessmentDetials coreDetails : assessmentDetails) {
            if (coreDetails.getYearLevel().equals(year)) {
                // the specific year only
                subList.add(coreDetails);
            }
        }

        if (subList.isEmpty()) {
            // no results for this year level
            return null;
        }

        // sublist now contains only a specific year value.
        Double totalUnits = 0.0;
        Double totalAcquiredUnits = 0.0;
        Double percentageAcquiredUnits = 0.0;
        Integer subjectDefficiency = 0;

        // sublist for unacquired subjects in this specific year
        ArrayList<SubjectAssessmentDetials> unacquiredSubjects = new ArrayList<>();
        ArrayList<SubjectAssessmentDetials> acquiredSubjects = new ArrayList<>();

        for (SubjectAssessmentDetials assessment : subList) {
            if (assessment.isAcquired()) {
                // total acquired
                totalAcquiredUnits += assessment.getTotalUnits();
                acquiredSubjects.add(assessment);
            } else {
                // add subject defficiency
                subjectDefficiency++;
                // add this subject as not acquried
                unacquiredSubjects.add(assessment);
            }
            // compute total units
            totalUnits += assessment.getTotalUnits();
        }
        // level progress in percent whole number
        percentageAcquiredUnits = (double) (totalAcquiredUnits / totalUnits);

        AssessmentResults results = new AssessmentResults();
        results.setTotalUnits(totalUnits);
        results.setAcquiredUnits(totalAcquiredUnits);
        results.setAcquiredPercentage(percentageAcquiredUnits);
        results.setSubUnacquireCount(subjectDefficiency);
        results.setUnacquiredSubjects(unacquiredSubjects);
        results.setAcquiredSubjects(acquiredSubjects);
        results.setAllSubjects(subList);

        return results;
    }

    /**
     * Expire checker. 1 year expiration.
     */
    private void checkExpired() {
//        AcademicTermMapping currentTerm = SystemProperties.instance().getCurrentAcademicTerm();

        ArrayList<GradeMapping> studentGrades = Mono.orm()
                .newSearch(Database.connect().grade())
                .eq(DB.grade().STUDENT_id, this.studentMap.getCict_id())
                .eq(DB.grade().rating, "INC")
                .active(Order.desc(DB.grade().id))
                .all();

        if (studentGrades == null) {
            // no inc grades.
            return;
        }

        Date currentDate = Mono.orm().getServerTime().getDateWithFormat();
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(currentDate);

        for (GradeMapping studentGrade : studentGrades) {
            Date incExpire = studentGrade.getInc_expire();
            Date postingDate = studentGrade.getPosting_date();
            if (incExpire != null) {
                Calendar expireCalendar = Calendar.getInstance();
                expireCalendar.setTime(incExpire);
                if (currentCalendar.getTimeInMillis() > expireCalendar.getTimeInMillis()) {
                    // grade is expired
                    makeGradeExpired(studentGrade);
                    // notify user notifications.
                }
            } else if (postingDate != null) {
                // expire time is null but still remarks is inc.
                Calendar postingCalendar = Calendar.getInstance();
                postingCalendar.setTime(postingDate);
                postingCalendar.add(Calendar.MONTH, PublicConstants.INC_EXPIRE);
                if (currentCalendar.getTimeInMillis() > postingCalendar.getTimeInMillis()) {
                    // expired Grade
                    makeGradeExpired(studentGrade);
                    // notify user notifications.
                }
            } else {
                // no method to detect if grade was expired.
            }
        }

//        for (GradeMapping grade : studentGrades) {
//            if (grade.getACADTERM_id() != null) {
//                int difference = currentTerm.getId() - grade.getACADTERM_id();
//                if (difference > 2) {
//                    // this grade is expired
//                    makeGradeExpired(grade);
//                    continue;
//                }
//            } else {
//                // not null
//                if (grade.getInc_expire() != null) {
//                    // check expire time
//                    if (Mono.orm().getServerTime().isPastServerTime(grade.getInc_expire())) {
//                        // expired
//                        makeGradeExpired(grade);
//                    }
//                } else {
//                    // if null check the creation time
//                    Calendar creationDate = Calendar.getInstance();
//                    creationDate.setTime(grade.getCreated_date());
//                    creationDate.add(Calendar.YEAR, 1);
//
//                    if (Mono.orm().getServerTime().isPastServerTime(creationDate.getTime())) {
//                        // expired
//                        makeGradeExpired(grade);
//                    }
//                }
//            }
//
//        }
    }

    private void makeGradeExpired(GradeMapping grade) {
        GradeMapping newGrade = MapFactory.map().grade();
        //----------------------------------------------------------------------
        newGrade.setACADTERM_id(grade.getACADTERM_id());
        newGrade.setCreated_by(grade.getCreated_by());
        newGrade.setCreated_date(grade.getCreated_date());
        newGrade.setCredit(grade.getCredit());
        newGrade.setCredit_method(grade.getCredit_method());
        //----------------------------------------------------------------------
        newGrade.setInc_expire(grade.getInc_expire());
        newGrade.setPosted(grade.getPosted());
        newGrade.setPosted_by(grade.getPosted_by());
        newGrade.setPosting_date(grade.getPosting_date());
        newGrade.setRemarks(grade.getRemarks());
        newGrade.setRating(grade.getRating());
        //----------------------------------------------------------------------
        newGrade.setReferrence_curriculum(grade.getReferrence_curriculum());
        newGrade.setSTUDENT_id(grade.getSTUDENT_id());
        newGrade.setSUBJECT_id(grade.getSUBJECT_id());
        //----------------------------------------------------------------------
        // update remarks and ratings
        newGrade.setRemarks("EXPIRED");
        newGrade.setRating("EXP");

        newGrade.setActive(1);
        newGrade.setReason_for_update(PublicConstants.EXPIRE_DESCRIPTION);
        newGrade.setUpdated_by(null);
        newGrade.setUpdated_date(this.serverTime);

        //
        grade.setActive(0);
        boolean oldDeactivated = Database.connect().grade().update(grade);

        //
        Integer newInserted = Database.connect().grade().insert(newGrade);

    }

    /**
     *
     */
    private void assessGrades() {

        /**
         * Get the curriculum details to check if type
         */
        CurriculumMapping curInfo = Mono.orm()
                .newSearch(Database.connect().curriculum())
                .eq(DB.curriculum().id, this.studentMap.getCURRICULUM_id())
                .execute()
                .first();

        // consequent curriculum info
        this.consCurriculum = curInfo;

        // prep
        ArrayList<CurriculumSubjectMapping> prep_curriculum_subjects = new ArrayList<>();
        // assertion there should always a curriculum result fk of student
        if (curInfo.getLadderization().equalsIgnoreCase(CurriculumValues.Ladderization.YES.toString())) {
            // curriculum is ladderized
            if (curInfo.getLadderization_type().equalsIgnoreCase(CurriculumValues.LadderType.CONSEQUENT.toString())) {

                // get pre curriculum info
                CurriculumMapping preCur = (CurriculumMapping) Database.connect()
                        .curriculum()
                        .getPrimary(this.studentMap.getPREP_id());
                //
                this.prepCurriculum = preCur;

                // this curriculum has a pre requisite.
                // if the primary curriculum of the student is consequent
                // it means that he completed the preparotary curriculum
                prep_curriculum_subjects = Mono.orm()
                        .newSearch(Database.connect().curriculum_subject())
                        .eq(DB.curriculum_subject().CURRICULUM_id, this.studentMap.getPREP_id())
                        .active()
                        .all();

                this.hasPrepData = true;
            }
        }

        /**
         * Subject from the current curriculum assigned.
         */
        ArrayList<CurriculumSubjectMapping> curriculum_subjects
                = Mono.orm()
                        .newSearch(Database.connect().curriculum_subject())
                        .eq(DB.curriculum_subject().CURRICULUM_id, this.studentMap.getCURRICULUM_id())
                        .active()
                        .all();

        try {
            // combine the present curriculum and the prep cur
            curriculum_subjects.addAll(prep_curriculum_subjects);
        } catch (NullPointerException ne) {
            assessmentComplete = false;
            System.err.println("Null merge");
            return;
        }

        for (CurriculumSubjectMapping curriculum_subject : curriculum_subjects) {

            SubjectMapping subjectDetails = Mono.orm()
                    .newSearch(Database.connect().subject())
                    .eq(DB.subject().id, curriculum_subject.getSUBJECT_id())
                    .execute()
                    .first();

            SubjectAssessmentDetials details = new SubjectAssessmentDetials();
            // add the first 2 values required.
            details.setCurriculumSubject(curriculum_subject);
            details.setSubjectDetails(subjectDetails);

            /**
             * get pre requisites
             */
            ArrayList<CurriculumRequisiteLineMapping> preqs = Mono.orm()
                    .newSearch(Database.connect().curriculum_requisite_line())
                    .eq(DB.curriculum_requisite_line().SUBJECT_id_get, subjectDetails.getId())
                    .put(PublicConstants.getCurriculumRequisite(studentMap))
                    .active()
                    .all();

            details.setSubjectRequisites(preqs);
            assessmentDetails.add(details);

        }

        /**
         * Check With Grades.
         */
        for (SubjectAssessmentDetials details : assessmentDetails) {
            GradeMapping grade = Mono.orm()
                    .newSearch(Database.connect().grade())
                    .eq(DB.grade().SUBJECT_id, details.getSubjectID())
                    // passed or inc remarks
                    .put(PublicConstants.OK_SUBJECT_REMARKS)
                    // must be posted
                    .eq(DB.grade().posted, 1)
                    // from this student
                    .eq(DB.grade().STUDENT_id, this.studentMap.getCict_id())
                    .active(Order.desc(DB.grade().id))
                    .first();
            // add grade value.
            details.setGradeDetails(grade);

            /**
             * If the settings of the CLA to include academic term was set to
             * true the query below will be executed.
             */
            if (this.includeAcadTerm) {
                AcademicTermMapping acadTerm = Mono.orm()
                        .newSearch(Database.connect().academic_term())
                        .eq(DB.academic_term().id, grade.getACADTERM_id())
                        .execute()
                        .first();

                details.setAcadTerm(acadTerm);
            }
        }

        /**
         * Reach End.
         */
        assessmentComplete = true;

    }
}

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
import app.lazy.models.CurriculumRequisiteLineMapping;
import app.lazy.models.CurriculumSubjectMapping;
import app.lazy.models.GradeMapping;
import app.lazy.models.SubjectMapping;
import java.util.ArrayList;

/**
 *
 * @author Jhon Melvin
 */
public class SubjectAssessmentDetials {

    private CurriculumSubjectMapping curriculumSubject;
    private SubjectMapping subjectDetails;
    private GradeMapping gradeDetails;
    private ArrayList<CurriculumRequisiteLineMapping> subjectRequisites;
    private AcademicTermMapping acadTerm;

    public ArrayList<CurriculumRequisiteLineMapping> getSubjectRequisites() {
        return subjectRequisites;
    }

    public void setSubjectRequisites(ArrayList<CurriculumRequisiteLineMapping> subjectRequisites) {
        this.subjectRequisites = subjectRequisites;
    }

    public GradeMapping getGradeDetails() {
        return gradeDetails;
    }

    public void setGradeDetails(GradeMapping gradeDetails) {
        this.gradeDetails = gradeDetails;
    }

    public CurriculumSubjectMapping getCurriculumSubject() {
        return curriculumSubject;
    }

    public void setCurriculumSubject(CurriculumSubjectMapping curriculumSubject) {
        this.curriculumSubject = curriculumSubject;
    }

    public SubjectMapping getSubjectDetails() {
        return subjectDetails;
    }

    public void setSubjectDetails(SubjectMapping subjectDetails) {
        this.subjectDetails = subjectDetails;
    }

    public AcademicTermMapping getAcadTerm() {
        return acadTerm;
    }

    public void setAcadTerm(AcademicTermMapping acadTerm) {
        this.acadTerm = acadTerm;
    }

    //--------------------------------------------------------------------------
    // class methods
    /**
     * get the id of the subject
     *
     * @return
     */
    public Integer getSubjectID() {
        return this.subjectDetails.getId();
    }

    /**
     * Return the total units of this subject.
     *
     * @return
     */
    public Double getTotalUnits() {
        return (this.subjectDetails.getLab_units() + this.subjectDetails.getLec_units());
    }

    /**
     * Get Year assignment of this subject.
     *
     * @return
     */
    public Integer getYearLevel() {
        return this.curriculumSubject.getYear();
    }

    /**
     * If this subject has a passing grade already.
     *
     * @return
     */
    public boolean isAcquired() {
        if (this.gradeDetails == null) {
            return false;
        }
        return true;
    }

    public Integer getSemester() {
        return this.curriculumSubject.getSemester();
    }

    public boolean hasAcadTerm() {
        if (this.acadTerm == null) {
            return false;
        }
        return true;
    }

}

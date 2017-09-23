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
package org.cict.evaluation.assessment;

import app.lazy.models.CurriculumMapping;
import java.util.ArrayList;

/**
 * this class has values for an annual assessment results.
 *
 * @author Jhon Melvin
 */
public class AssessmentResults {

    private Double totalUnits;
    private Double acquiredUnits;
    private Double acquiredPercentage;
    private Integer subUnacquireCount;

    /**
     * Total subject count of that are not taken.
     *
     * @return
     */
    public Integer getSubUnacquireCount() {
        return subUnacquireCount;
    }

    public void setSubUnacquireCount(Integer subUnacquireCount) {
        this.subUnacquireCount = subUnacquireCount;
    }

    /**
     * Total units this year.
     *
     * @return
     */
    public Double getTotalUnits() {
        return totalUnits;
    }

    public void setTotalUnits(Double totalUnits) {
        this.totalUnits = totalUnits;
    }

    /**
     * Total acquired units this year.
     *
     * @return
     */
    public Double getAcquiredUnits() {
        return acquiredUnits;
    }

    public void setAcquiredUnits(Double acquiredUnits) {
        this.acquiredUnits = acquiredUnits;
    }

    /**
     * Acquired percentage this year.
     *
     * @return
     */
    public Double getAcquiredPercentage() {
        return acquiredPercentage;
    }

    public void setAcquiredPercentage(Double acquiredPercentage) {
        this.acquiredPercentage = acquiredPercentage;
    }

    //-------------------------------------------------------------------------
    private ArrayList<SubjectAssessmentDetials> unacquiredSubjects;
    private ArrayList<SubjectAssessmentDetials> acquiredSubjects;
    private ArrayList<SubjectAssessmentDetials> allSubjects;

    /**
     * All subjects whether taken or not.
     *
     * @return
     */
    public ArrayList<SubjectAssessmentDetials> getAllSubjects() {
        return allSubjects;
    }

    public void setAllSubjects(ArrayList<SubjectAssessmentDetials> allSubjects) {
        this.allSubjects = allSubjects;
    }

    /**
     * Information about the acquired subjects.
     *
     * @return
     */
    public ArrayList<SubjectAssessmentDetials> getAcquiredSubjects() {
        return acquiredSubjects;
    }

    public void setAcquiredSubjects(ArrayList<SubjectAssessmentDetials> acquiredSubjects) {
        this.acquiredSubjects = acquiredSubjects;
    }

    /**
     * Information about the subjects that are not yet taken.
     *
     * @return
     */
    public ArrayList<SubjectAssessmentDetials> getUnacquiredSubjects() {
        return unacquiredSubjects;
    }

    public void setUnacquiredSubjects(ArrayList<SubjectAssessmentDetials> unacquiredSubjects) {
        this.unacquiredSubjects = unacquiredSubjects;
    }

    /**
     * Get a subject from a specific semester from this year's assessment.
     *
     * @param semester
     * @return
     */
    public ArrayList<SubjectAssessmentDetials> getSemestralResults(Integer semester) {
        ArrayList<SubjectAssessmentDetials> semSublist = new ArrayList<>();
        for (SubjectAssessmentDetials subject : this.allSubjects) {
            if (subject.getSemester().equals(semester)) {
                semSublist.add(subject);
            }
        }
        return semSublist;
    }

    

}

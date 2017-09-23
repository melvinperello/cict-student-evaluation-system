/* 
 * Copyright (C) Jhon Melvin N. Perello - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * PROPRIETARY and CONFIDENTIAL
 *
 * Written by Jhon Melvin N. Perello <jhmvinperello@gmail.com>, 2017.
 *
 */
package org.cict.evaluation.evaluator;

import app.lazy.models.Database;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import java.util.ArrayList;
import java.util.Objects;
import app.lazy.models.AcademicProgramMapping;
import app.lazy.models.CurriculumRequisiteLineMapping;
import app.lazy.models.DB;
import app.lazy.models.GradeMapping;
import app.lazy.models.LoadSectionMapping;
import app.lazy.models.StudentMapping;
import app.lazy.models.SubjectMapping;
import org.cict.PublicConstants;
import org.hibernate.criterion.Order;

/**
 *
 * @author Jhon Melvin
 */
public class ValidateAddedSubject extends Transaction {

    /**
     * for debugging purposes enable Logging for this class.
     */
    private void log(Object message) {
        boolean logging = true;
        if (logging) {
            System.out.println(message);
        }
    }

    public Integer loadSecID;
    public Integer subjectID;
    public Integer loadGroupID;
    public Integer studentCictID;

    /**
     * Search Cached Values
     */
    private ArrayList<CurriculumRequisiteLineMapping> preRequisiteID;
    // missing pre-requisites.
    private ArrayList<Integer> subjectsNeeded;
    private ArrayList<SubjectMapping> subjectNeededInfo;
    //
    private SubjectMapping subjectToTakeInfo;
    private AcademicProgramMapping acadProgram;
    private StudentMapping studentMap;

    /**
     * Flag values
     */
    private boolean hasPreRequisites = false;

    private boolean isAlreadyTaken = false;

    /**
     * Result values
     */
    private boolean isEligibleToTake = false;
    // list of pre requisites code
    private ArrayList<String> subjectNeededCode;
    // Subject to add information
    private String subjectCode;
    private String subjectTitle;
    private Double subjectUnits;
    private Double subjectLecUnits;
    private Double subjectLabUnits;

    public Double getSubjectLecUnits() {
        return subjectLecUnits;
    }

    public Double getSubjectLabUnits() {
        return subjectLabUnits;
    }

    //
    private String sectionWithFormat;
    //
    private boolean isAllPreRequisiteTaken = false;

    public boolean isAlreadyTaken() {
        return isAlreadyTaken;
    }

    public String getSectionWithFormat() {
        return sectionWithFormat;
    }

    public ArrayList<String> getSubjectNeededCode() {
        return subjectNeededCode;
    }

    public boolean isEligibleToTake() {
        return this.isEligibleToTake;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public String getSubjectTitle() {
        return subjectTitle;
    }

    public Double getSubjectUnits() {
        return subjectUnits;
    }

    @Override
    protected boolean transaction() {
        log("@ValidateAddedSubjects: Transaction started . . .");
        if (isSomethingNull(loadSecID, subjectID, loadGroupID, studentCictID)) {
            // if one is null
            log("@ValidateAddedSubjects: Missing Values");
            return false;
        } else {

            /**
             * Get Student Curriculum.
             *
             * @date: 09.02.2017
             */
            this.studentMap = (StudentMapping) Database.connect().student()
                    .getPrimary(studentCictID);
            /**
             * Double Checking the existence of the subject
             */
            subjectToTakeInfo = Mono.orm()
                    .newSearch(Database.connect().subject())
                    .eq("id", subjectID)
                    .active()
                    .first();

            if (subjectToTakeInfo == null) {
                return false;
            } else {
                subjectCode = subjectToTakeInfo.getCode();
                subjectTitle = subjectToTakeInfo.getDescriptive_title();
                subjectUnits = subjectToTakeInfo.getLab_units() + subjectToTakeInfo.getLec_units();
                subjectLabUnits = subjectToTakeInfo.getLab_units();
                subjectLecUnits = subjectToTakeInfo.getLec_units();
                log("@ValidateAddedSubjects: Checking if already taken with PASSED or INC REMARKS");

                GradeMapping grade = Mono.orm()
                        .newSearch(Database.connect().grade())
                        .eq("STUDENT_id", studentCictID)
                        .eq("SUBJECT_id", subjectToTakeInfo.getId())
                        .put(PublicConstants.OK_SUBJECT_REMARKS)
                        .active(Order.desc("id"))
                        .first();

                if (grade != null) {
                    log("@ValidateAddedSubjects: Already Taken");
                    this.isAlreadyTaken = true;
                    return true;
                }
            }
            // proceed to transaction
            /**
             * Check if the subject has a pre-requisite
             */
            log("@ValidateAddedSubjects: Checking for pre-requisites");
            preRequisiteID = Mono.orm()
                    .newSearch(Database.connect().curriculum_requisite_line())
                    .eq("SUBJECT_id_get", subjectID)
                    .put(PublicConstants.getCurriculumRequisite(studentMap))
                    .active()
                    .all();

            if (Objects.isNull(preRequisiteID)) {
                // the subject has no pre requisite
                this.hasPreRequisites = false;
                log("@ValidateAddedSubjects: No Pre-Requisites Found.");
            } else {
                this.hasPreRequisites = true;
                // the subject has pre-requisites
                subjectsNeeded = new ArrayList<>();
                /**
                 * Check for the pre-requisites
                 */

                preRequisiteID.forEach(pre_requisite -> {
                    int pre_requisite_id = pre_requisite.getSUBJECT_id_req();
                    Object preq = Mono.orm().newSearch(Database.connect().grade())
                            .eq("STUDENT_id", studentCictID)
                            .eq("SUBJECT_id", pre_requisite_id)
                            .put(PublicConstants.OK_SUBJECT_REMARKS)
                            .active(Order.desc("id")).first();
                    if (Objects.isNull(preq)) {
                        // if the pre requisite is not found
                        subjectsNeeded.add(pre_requisite_id);
                        log("@ValidateAddedSubjects: " + pre_requisite_id + " Has not yet taken.");
                    }
                });

                /**
                 * Check if all the pre-requisites are taken
                 */
                if (subjectsNeeded.isEmpty()) {
                    // all pre-requisites are taken
                    /**
                     * When all pre requisites are taken. must check if the
                     * pre-requisite of the pre-requisite are taken also.
                     *
                     * @date: 08/11/2017
                     * @update: added CURRICULUM_id
                     * @date: 09.02.2017
                     */
                    //----------------------------------------------------------
                    ArrayList<Integer> res = RequisitesTracker.viewRequisites(preRequisiteID, this.studentMap);

                    if (!res.isEmpty()) {
                        subjectsNeeded.addAll(res);
                        untakenInfo();
                    } else {
                        this.isAllPreRequisiteTaken = true;
                        log("@ValidateAddedSubjects: All Pre-Requisites are taken.");
                    }

                    //----------------------------------------------------------
                } else {
                    untakenInfo();
                }
            }// end of pre requiste check

            //------------------------------------------------------------------
            /**
             * Section Check
             */
            LoadSectionMapping sectionMap = Mono.orm()
                    .newSearch(Database.connect().load_section())
                    .eq("id", loadSecID)
                    .active()
                    .first();

            if (sectionMap == null) {
                // section was not found
                return false;
            } else {
                /**
                 * Get the academic program for program code.
                 */
                acadProgram = Mono.orm()
                        .newSearch(Database.connect().academic_program())
                        .eq("id", sectionMap.getACADPROG_id())
                        .active()
                        .first();

                /**
                 * code added 8/28/17 by: joemar
                 */
                if (acadProgram == null) {
//                    return false;
                    sectionWithFormat = sectionMap.getSection_name();
                } else {
                    sectionWithFormat = acadProgram.getCode() + " "
                            + sectionMap.getYear_level()
                            + " "
                            + sectionMap.getSection_name()
                            + " - G"
                            + sectionMap.get_group();
                }

            }
        }
        return true;
    }

    private void untakenInfo() {
        // gather info about the missing subject
        subjectNeededInfo = new ArrayList<>();
        subjectNeededCode = new ArrayList<>();
        subjectsNeeded.forEach(subject_id -> {
            SubjectMapping sMap = Mono.orm()
                    .newSearch(Database.connect().subject())
                    .eq("id", subject_id)
                    .active()
                    .first();
            subjectNeededInfo.add(sMap);
            subjectNeededCode.add(sMap.getCode());
        });

        // some pre-requistes are not yet taken
        this.isAllPreRequisiteTaken = false;
        log("@ValidateAddedSubjects: Some Pre-requisites are NOT taken");
    }

    /**
     * Checks every object if there is null.
     *
     * @param obj
     * @return
     */
    public boolean isSomethingNull(Object... obj) {
        boolean somethingIsNull = false;
        if (obj.length != 0) {
            for (Object object : obj) {
                if (Objects.isNull(object) || Objects.equals(object, 0)) {
                    somethingIsNull = true;

                    break;
                }

            }
        }
        return somethingIsNull;
    }

    @Override
    protected void after() {

        if (this.isAlreadyTaken) {
            this.isEligibleToTake = false;
            return;
        }
        //
        if (this.hasPreRequisites) {
            //
            if (this.isAllPreRequisiteTaken) {
                this.isEligibleToTake = true;
                log("@ValidateAddedSubjects: Student is Eligible");
            } else {
                this.isEligibleToTake = false;
                log("@ValidateAddedSubjects: Cannot Take the subject");
            }
        } else {
            this.isEligibleToTake = true;
            log("@ValidateAddedSubjects: Student is Eligible");
        }

    }

}

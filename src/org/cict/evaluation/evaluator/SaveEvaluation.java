/* 
 * Copyright (C) Jhon Melvin N. Perello - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * PROPRIETARY and CONFIDENTIAL
 *
 * Written by Jhon Melvin N. Perello <jhmvinperello@gmail.com>, 2017.
 *
 */
package org.cict.evaluation.evaluator;

import org.cict.evaluation.views.SubjectView;
import app.lazy.models.Database;
import app.lazy.models.MapFactory;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import java.util.ArrayList;
import app.lazy.models.EvaluationMapping;
import app.lazy.models.GradeMapping;
import app.lazy.models.LoadSubjectMapping;
import app.lazy.models.StudentMapping;
import java.util.Locale;
import org.cict.reports.advisingslip.AdvisingSlip;
import org.cict.reports.advisingslip.AdvisingSlipData;

import org.hibernate.Session;

/**
 *
 * @author Jhon Melvin
 */
public class SaveEvaluation extends Transaction {
    
    private void log(Object message) {
        boolean enableLoggin = true;
        if (enableLoggin) {
            System.out.println("@SaveEvaluation: " + message.toString());
        }
    }
    
    public Integer studentID;
    public Integer acadTermID;
    public Integer facultyID;
    public ArrayList<SubjectView> subjects;

    /**
     * Search cached values
     */
    private EvaluationMapping currentlyEvaluating;
    private StudentMapping student;

    /**
     * Flag Values
     */
    private boolean isAlreadyEvaluated;
    private int evaluationID;
    
    public int getEvaluationID() {
        return evaluationID;
    }
    
    @Override
    protected boolean transaction() {

        // check if student is already evaluated
        log("Checking if already Evaluated");
        currentlyEvaluating = Mono.orm()
                .newSearch(Database.connect().evaluation())
                .eq("STUDENT_id", studentID)
                .eq("ACADTERM_id", acadTermID)
                .active()
                .first();

        /**
         * If already evaluated
         */
        if (currentlyEvaluating != null) {
            isAlreadyEvaluated = true;
            evaluationID = currentlyEvaluating.getId();
            log("Already Evaluated");
            return true;
        }
        
        log("Starting . . .");
        /**
         * If not proceed to evaluation
         */

        /**
         * Search for the student.
         */
        student = Mono.orm()
                .newSearch(Database.connect().student())
                .eq("cict_id", studentID)
                .execute()
                .first();

        // create local session
        Session currentSession = Mono.orm().session();
        // start your transaction
        org.hibernate.Transaction dataTransaction = currentSession.beginTransaction();

        /**
         * Create evaluation entry.
         */
        EvaluationMapping eval = MapFactory.map().evaluation();
        eval.setACADTERM_id(acadTermID);
        eval.setSTUDENT_id(studentID);
        eval.setFACULTY_id(facultyID);
        eval.setEvaluation_date(Mono.orm().getServerTime().getDateWithFormat());
        eval.setYear_level(student.getYear_level());
        eval.setType("REGULAR");

        // get temporary evaluation.
        int temp_eval_id = Database.connect().grade().transactionalInsert(currentSession, eval);
        
        if (temp_eval_id < 0) {
            // if errors occured during temporary insert
            dataTransaction.rollback();
            log("evaluation_id failed to insert");
            // cannot insert transaction failed
            return false;
        }

        /**
         * For reference.
         */
        evaluationID = temp_eval_id;

        // if no error proceed to temporary insert load_subject table
        log("temporary evaluation id inserted");
        boolean isInserted = true;
        // insert in subject load
        for (SubjectView subject : subjects) {
            LoadSubjectMapping load_subject = MapFactory.map().load_subject();
            load_subject.setSUBJECT_id(subject.subjectID);
            load_subject.setEVALUATION_id(temp_eval_id);
            load_subject.setLOADGRP_id(subject.loadGroupID);
            load_subject.setSTUDENT_id(studentID);
            load_subject.setAdded_by(facultyID);
            
            int insert_load_subject = Database.connect()
                    .load_subject()
                    .transactionalInsert(currentSession, load_subject);

            /**
             * If Failed to insert.
             */
            if (insert_load_subject < 0) {
                isInserted = false;
                break;
            }
            
            GradeMapping grades = MapFactory.map().grade();
            grades.setSTUDENT_id(studentID);
            grades.setSUBJECT_id(subject.subjectID);
            grades.setACADTERM_id(acadTermID);
            grades.setRating("UNPOSTED");
            grades.setRemarks("UNPOSTED");
            grades.setCredit(subject.units);
            grades.setCredit_method("REGULAR");
            grades.setCreated_by(facultyID);
            grades.setCreated_date(Mono.orm().getServerTime().getDateWithFormat());
            grades.setPosted(0);
            grades.setReason_for_update("Created by Evaluation");
            
            int insert_grade = Database
                    .connect()
                    .grade()
                    .transactionalInsert(currentSession, grades);

            /**
             * If Failed to insert.
             */
            if (insert_grade < 0) {
                isInserted = false;
                break;
            }
            
        }

        /**
         * if error in inserting.
         */
        if (!isInserted) {
            dataTransaction.rollback();
            return false;
        }

        /**
         * If inserted success fully. commit changes.
         */
        if (isInserted) {
            log("subject successfully inserted");
            dataTransaction.commit();
        }
        
        return true;
    }
    
    @Override
    protected void after() {
//        PrintAdvising slip = Evaluator.instance().printAdvising();
//        slip.studentNumber = student.getId();
//        slip.academicTerm = acadTermID;
//        slip.transact();
    }
    
}

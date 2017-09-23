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
package org.cict.evaluation;

import java.util.ArrayList;
import org.cict.evaluation.evaluator.Evaluator;
import org.cict.evaluation.evaluator.ValidateAddedSubject;

/**
 *
 * @author Jhon Melvin
 */
public class SampleOfValidate {

    public SampleOfValidate() {
        ValidateAddedSubject validate = Evaluator.instance().createValidateAddedSubject();
        /**
         * Required Variables.
         */
        // these are sample values only
        validate.subjectID = 4; // the subject that you want to check
        validate.loadSecID = 2; // section belonging to that subject
        validate.loadGroupID = 1; // the group belonging to the section
        validate.studentCictID = 3; // the student to take

        validate.setOnCancel(onCancel -> {
            /**
             * This event will be only triggered when one of the required
             * variables were missing. based on my coding structure but print
             * something in here to be sure.
             *
             * @when the subject was deleted during the instance of this
             * operation. e.g. when the registrar have deleted this subject
             * during this transaction.
             * @when there is no section found for this subject. or the section
             * was suddenly closed.
             */
        });

        validate.setOnFailure(onFailure -> {
            /**
             * If a database connection failure occur.
             */
        });

        validate.setOnSuccess(onSuccess -> {
            // validation success values
            /**
             * ( 1 ) when subject is already taken you can use isAlreadyTaken
             * method to check
             */
            boolean already_taken = validate.isAlreadyTaken();

            /**
             * Checking if the student is eligible to take the subject.
             */
            boolean student_can_take = validate.isEligibleToTake();
            /**
             * If the student is NOT ELIGIBLE.
             *
             * @list of subject codes that are required which is missing.
             */
            ArrayList<String> why_cant_i_take = validate.getSubjectNeededCode();

            /**
             * When success method is invoked there is an extended feature with
             * this validation. the following values below are available. if you
             * only have the subject_id you do not need to query it again to
             * find it's values.
             */
            validate.getSubjectCode(); // full section name
            validate.getSubjectCode(); // code of the subject that is being tested.
            // these are values from the subject that is being tested.
            validate.getSubjectLabUnits();
            validate.getSubjectLecUnits();
            validate.getSubjectTitle();
            validate.getSubjectUnits();  // combination of lab lec no need to add
            /**
             * Do not use anything that is not included in this sample.
             */

        });

        validate.transact(); // start validating
        /**
         * @REMEMBER this is a transaction based validation. all codes after
         *
         * @function validate.transact();
         *
         * @continue to execute.
         */
    }

}

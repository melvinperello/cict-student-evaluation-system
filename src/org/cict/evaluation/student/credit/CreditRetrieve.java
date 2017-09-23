/**
 * CAPSTONE PROJECT.
 * BSIT 4A-G1.
 * MONOSYNC TECHNOLOGIES.
 * MONOSYNC FRAMEWORK VERSION 1.0.0 TEACUP RICE ROLL.
 * THIS PROJECT IS PROPRIETARY AND CONFIDENTIAL ANY PART THEREOF.
 * COPYING AND DISTRIBUTION WITHOUT PERMISSION ARE NOT ALLOWED.
 * <p>
 * COLLEGE OF INFORMATION AND COMMUNICATIONS TECHNOLOGY.
 * LINKED SYSTEM.
 * <p>
 * PROJECT MANAGER: JHON MELVIN N. PERELLO
 * DEVELOPERS:
 * JOEMAR N. DE LA CRUZ
 * GRETHEL EINSTEIN BERNARDINO
 * <p>
 * OTHER LIBRARIES THAT ARE USED BELONGS TO THEIR RESPECTFUL OWNERS AND AUTHORS.
 * NO COPYRIGHT ARE INTENTIONAL OR INTENDED.
 * THIS PROJECT IS NOT PROFITABLE HENCE FOR EDUCATIONAL PURPOSES ONLY.
 * THIS PROJECT IS ONLY FOR COMPLIANCE TO OUR REQUIREMENTS.
 * THIS PROJECT DOES NOT INCLUDE DISTRIBUTION FOR OTHER PURPOSES.
 */
package org.cict.evaluation.student.credit;

import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.GradeMapping;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;

import java.util.ArrayList;

import org.hibernate.criterion.Order;

/**
 * @author Joemar
 */
@Deprecated
public class CreditRetrieve extends Transaction {

    private void logs(String message) {
        System.out.println("@CreditRetrieve: " + message);
    }

    public Integer cict_id;

    private ArrayList<Object[]> retrievedGrades = new ArrayList<>();
    ;

    public ArrayList<Object[]> getRetrievedGrades() {
        return retrievedGrades;
    }

    @Override
    protected boolean transaction() {
        /**
         * TRANSACTION PROCESS MARKED AS WRONG
         * @date 08/17/2017
         * @note grades must be retrieved individually.
         * @correctionStatus NOT UPDATED.
         */
        try {
            ArrayList<GradeMapping> studentGrades = Mono.orm().newSearch(Database.connect().grade())
                    .eq(DB.grade().STUDENT_id, this.cict_id)
                    .active(Order.desc(DB.grade().id))
                    .all();
            for (int i = 0; i < studentGrades.size(); i++) {
                Integer subject_id = studentGrades.get(i).getSUBJECT_id();
                String grade = studentGrades.get(i).getRating();
                Object[] holder = {subject_id, grade};
                retrievedGrades.add(holder);
            }
        } catch (NullPointerException a) {
        }
        return true;
    }

    @Override
    protected void after() {

    }

}

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
package artifacts;

import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.EvaluationMapping;
import app.lazy.models.StudentMapping;
import com.jhmvin.Mono;
import java.util.Calendar;
import org.hibernate.criterion.Order;

/**
 * This class is used to check is a student is over staying. the maximum amount
 * of time a student can be admitted is 6 years from the first day of the first
 * evaluation.
 *
 * @author Jhon Melvin
 */
public class StudentOverStay {

    // Max years a student is allowed to stay
    private final static int MAX_YEARS = 6;

    public static boolean check(StudentMapping student) {
        //----------------------------------------------------------------------
        // Get the first Evaluation Entry
        EvaluationMapping firstEvaluation = Mono.orm()
                .newSearch(Database.connect().evaluation())
                .eq(DB.evaluation().STUDENT_id, student.getCict_id())
                .active(Order.asc(DB.evaluation().id))
                .first();
        //----------------------------------------------------------------------
        if (firstEvaluation == null) {
            return false;
        }
        //----------------------------------------------------------------------
        // Check the date of the first evaluation
        Calendar firstEvaluationCalendar = Calendar.getInstance();
        firstEvaluationCalendar.setTime(firstEvaluation.getEvaluation_date());
        // Add years
        firstEvaluationCalendar.add(Calendar.YEAR, MAX_YEARS);
        //----------------------------------------------------------------------
        // Now check if past server time
        if (Mono.orm().getServerTime().isPastServerTime(firstEvaluationCalendar.getTime())) {
            return true;
        }
        //----------------------------------------------------------------------

        //----------------------------------------------------------------------
        // Default return values is false
        return false;
    }
}

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
package update3.org.cict.scheduling;

import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.LoadGroupMapping;
import app.lazy.models.LoadGroupScheduleMapping;
import com.jhmvin.Mono;
import java.util.ArrayList;

/**
 *
 * @author Jhon Melvin
 */
public class ScheduleChecker {

    public static boolean checkIfConflict(Integer load_sec, String classDay, String start, String end) {

        /**
         * Get all load groups from that section.
         */
        ArrayList<LoadGroupMapping> loadGroups = Mono.orm()
                .newSearch(Database.connect().load_group())
                .eq(DB.load_group().LOADSEC_id, load_sec)
                .active()
                .all();

        if (loadGroups == null) {
            return false;
        }

        /**
         * All load group from this section.
         */
        for (LoadGroupMapping load_group : loadGroups) {
            /**
             * Get schedules from this day. of this load group.
             */
            ArrayList<LoadGroupScheduleMapping> scheds = Mono.orm().
                    newSearch(Database.connect().load_group_schedule())
                    .eq(DB.load_group_schedule().load_group_id, load_group.getId())
                    .eq(DB.load_group_schedule().class_day, classDay)
                    .active()
                    .all();

            if (scheds == null) {
                continue;
            }

            //---------------------
            for (LoadGroupScheduleMapping sched : scheds) {

                double x_start = doubleConverter(sched.getClass_start());
                double x_end = doubleConverter(sched.getClass_end());

                double y_start = doubleConverter(start);
                double y_end = doubleConverter(end);

//                System.out.println(x_start);
//                System.out.println(x_end);
//                System.out.println(y_start);
//                System.out.println(y_end);
                /**
                 * Check if overlapping.
                 */
                if (checkOverlap(x_start, x_end, y_start, y_end)) {
                    System.out.println("OVERLAP");
                    return true;
                }
            }

        }

        /**
         * No Overlapping found.
         */
        return false;

    }

    /**
     * Null Safe.
     *
     * @param time
     * @return
     */
    public static double doubleConverter(String time) {
        try {
            return Double.parseDouble(time.replace(':', '.'));
        } catch (NumberFormatException | NullPointerException e) {
            return 0.00;
        }
    }

    /**
     * Check if a line contains or intersect with another line.
     *
     * @param x_start
     * @param x_end
     * @param y_start
     * @param y_end
     * @return
     */
    public static boolean checkOverlap(double x_start, double x_end, double y_start, double y_end) {

        if (x_start <= y_start && x_end >= y_start) {
            if (x_end == y_start) {
                return false;
            }
            /**
             * Check y_start contain in x
             */
            return true;
        }
        if (x_start <= y_end && x_end >= y_end) {

            if (y_end == x_start) {
                return false;
            }
            /**
             * Check y_end contains in x
             */
            return true;
        }

        return false;
    }

}

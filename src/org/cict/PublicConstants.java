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
package org.cict;

import app.lazy.models.DB;
import app.lazy.models.StudentMapping;
import com.jhmvin.orm.SQL;
import java.util.HashMap;
import org.hibernate.criterion.Criterion;

/**
 *
 * @author Jhon Melvin
 */
public class PublicConstants {

    public final static String getServer() {
        return "127.0.0.1";
    }

    /**
     * INC expiration in months.
     */
    public final static int INC_EXPIRE = 12;
    public final static String EXPIRE_DESCRIPTION = "INC GRADE HAS EXPIRED.";

    public final static String[] LEGACY_CURRICULUM = new String[]{"ACT (15-16)", "BSIT (11-12)", "BITCT (11-12)", "BSIT (15-16)"};

    /**
     * System.
     */
    public final static String PLATFORM_NAME = "EVALUATION_SYSTEM";

    /**
     * Evaluation constants.
     */
    public final static Double MAX_UNITS = 26.0;
    public final static Double MIN_UNITS = 12.0;

    /**
     * Authentication constants.
     */
    public final int MAX_ATTEMPTS = 3; // max wrong attempts for temp block

    /**
     * Internship subjects.
     */
    public final String[] SUBJECT_INTERNSHIP = {
        "INTERNSHIP",
        "INTERNSHIP",};

    /**
     * Remarks required for pre-requisites.
     */
    public final static Criterion OK_SUBJECT_REMARKS = SQL
            .or(SQL.where("remarks").equalTo("PASSED"),
                    SQL.where("remarks").equalTo("INCOMPLETE"));

    public static Criterion getCurriculumRequisite(StudentMapping studentMap) {
        //return SQL.where("active").equalTo(1);
        return SQL.where(DB.curriculum_requisite_line().CURRICULUM_id).equalTo(studentMap.getCURRICULUM_id());
//        return SQL.or(
//                SQL.where(DB.curriculum_requisite_line().CURRICULUM_id).equalTo(studentMap.getCURRICULUM_id()),
//                SQL.where(DB.curriculum_requisite_line().CURRICULUM_id).equalTo(studentMap.getPREP_id()));
    }

    /**
     *
     * @param rating
     * @return
     */
    public static String getRemarks(String rating) {
        HashMap<String, String> table = new HashMap<>();
        table.put("1.00", "PASSED");
        table.put("1.25", "PASSED");
        table.put("1.50", "PASSED");
        table.put("2.00", "PASSED");
        table.put("2.25", "PASSED");
        table.put("2.50", "PASSED");
        table.put("3.00", "PASSED");
        table.put("INC", "INCOMPLETE");
        table.put("EXP", "EXPIRED");

        return table.get(rating);
    }

}

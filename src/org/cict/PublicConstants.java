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
import app.lazy.models.Database;
import app.lazy.models.StudentMapping;
import app.lazy.models.SystemVariablesMapping;
import com.jhmvin.Mono;
import com.jhmvin.orm.SQL;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import sys.org.cict.enumerations.GradeValues;

/**
 *
 * @author Jhon Melvin
 */
public class PublicConstants {

    //--------------------------------------------------------------------------
    /**
     * System Platform Name
     */
    public final static String PLATFORM_NAME = "EVALUATION_SYSTEM";

    //--------------------------------------------------------------------------
    /**
     * Server IP
     *
     * @return
     */
    public final static String getServer() {
        return com.melvin.java.properties.PropertyManager.gerPropertyFile("evaluation.properties").getProperty("hostIP");
    }

    public final static String FACULTY_REGISTRATION_LINK = "http://" + getServer() + "/laravel/cictwebportal/public/home/hello";
    public final static String FACULTY_FORGOT_LINK = "http://" + getServer() + "/laravel/cictwebportal/public/home/hello";

    //--------------------------------------------------------------------------
    // INC expiration in months
    public final static int INC_EXPIRE = 12;
    public final static String EXPIRE_DESCRIPTION = "INC GRADE HAS EXPIRED.";
    //--------------------------------------------------------------------------
    // Legacy Curriculums for Checklist
    public final static String[] LEGACY_CURRICULUM = new String[]{
        "ACT (15-16)",
        "BSIT OLD (11-12)",
        "BITCT (11-12)"
    };
    //--------------------------------------------------------------------------
    /**
     * Evaluation constants.
     */
    public final static Double MAX_UNITS = 26.0;
    public final static Double MIN_UNITS = 12.0;
    public final static Integer MAX_POPULATION = 1;
    //--------------------------------------------------------------------------
    /**
     * Authentication constants.
     */
    public final int MAX_ATTEMPTS = 3; // max wrong attempts for temp block
    //--------------------------------------------------------------------------
    /**
     * Remarks required for pre-requisites.
     */
    public final static Criterion OK_SUBJECT_REMARKS = SQL
            .or(SQL.where(DB.grade().remarks).equalTo("PASSED"),
                    SQL.where(DB.grade().remarks).equalTo("INCOMPLETE"));

    //--------------------------------------------------------------------------
    /**
     * Forgotten !!! but required.
     *
     * @param studentMap
     * @return
     */
    public static Criterion getCurriculumRequisite(StudentMapping studentMap) {
        return SQL.where(DB.curriculum_requisite_line().CURRICULUM_id).equalTo(studentMap.getCURRICULUM_id());
//        return SQL.or(
//                SQL.where(DB.curriculum_requisite_line().CURRICULUM_id).equalTo(studentMap.getCURRICULUM_id()),
//                SQL.where(DB.curriculum_requisite_line().CURRICULUM_id).equalTo(studentMap.getPREP_id()));
    }
    //--------------------------------------------------------------------------

    /**
     * Please use the code at GradeValues Enumeration
     *
     * @param rating
     * @return
     * @deprecated
     */
    @Deprecated
    public static String getRemarks(String rating) {
        return GradeValues.RATING_REMAKRS.get(rating);
    }

    //--------------------------------------------------------------------------
    public final String KEY_REGISTRAR = "REGISTRAR";
    public final String KEY_RECOMMENDING_APPROVAL = "RECOMMENDING_APPROVAL";

    /**
     * Gets values from the system variables from the server using the keys
     * provided.
     *
     * @param key
     * @return
     */
    public final static String getServerValues(String key) {
        try {
            SystemVariablesMapping map = Mono.orm()
                    .newSearch(Database.connect().system_variables())
                    .eq(DB.system_variables().name, key)
                    .active(Order.desc(DB.system_variables().id))
                    .first();
            if (map == null) {
                return ""; // to avoid null pointer
            }
            return map.getValue();
        } catch (Exception e) {
            return "";
        }
    }
}

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
import artifacts.ConfigurationManager;
import com.jhmvin.Mono;
import com.jhmvin.orm.SQL;
import com.melvin.java.properties.PropertyFile;
import java.util.Properties;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import sys.org.cict.enumerations.GradeValues;

/**
 *
 * @author Jhon Melvin
 */
public class PublicConstants {

    public final static String FTP_STUDENT_AVATAR = "student_avatar";
    //--------------------------------------------------------------------------
    /**
     * System Platform Name
     */
    public final static String PLATFORM_NAME = "EVALUATION_SYSTEM";

    //--------------------------------------------------------------------------
    /**
     * Get IP From Configuration File.
     *
     * @return
     */
    public final static String getServerIP() {
        ConfigurationManager.checkConfiguration();
        Properties evaluation_property = PropertyFile.getPropertyFile(ConfigurationManager.EVALUATION_PROP.getAbsolutePath());
        return evaluation_property.getProperty(ConfigurationManager.PROP_HOST_IP);
    }

    public final static String FACULTY_REGISTRATION_LINK = "http://" + getServerIP() + "/laravel/cictwebportal/public/home/hello";
    public final static String FACULTY_FORGOT_LINK = "http://" + getServerIP() + "/laravel/cictwebportal/public/home/hello";

    //--------------------------------------------------------------------------
    // INC expiration in months
    public final static int INC_EXPIRE = 12;
    public final static String EXPIRE_DESCRIPTION = "INC GRADE HAS EXPIRED.";
    //--------------------------------------------------------------------------
    // Legacy Curriculums for Checklist
    public final static String[] LEGACY_CURRICULUM = new String[]{
        "ACT (15-16)",
        "BSIT (11-12) REVISED",
        "BITCT (11-12)",
        "BSIT (15-16)"
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
        //return SQL.where(DB.curriculum_requisite_line().CURRICULUM_id).equalTo(studentMap.getCURRICULUM_id());
        return SQL.or(
                SQL.where(DB.curriculum_requisite_line().CURRICULUM_id).equalTo(studentMap.getCURRICULUM_id()),
                SQL.where(DB.curriculum_requisite_line().CURRICULUM_id).equalTo(studentMap.getPREP_id())
        );
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
    /**
     * Gets values from the system variables from the server using the keys
     * provided.
     *
     * @param key
     * @return
     */
    public final static Object getServerValues(String key) {
        try {
            SystemVariablesMapping map = Mono.orm()
                    .newSearch(Database.connect().system_variables())
                    .eq(DB.system_variables().name, key)
                    .active(Order.desc(DB.system_variables().id))
                    .first();
            if (map == null) {
                return null;
            }
            return map.getValue();
        } catch (Exception e) {
            return null;
        }
    }

    //--------------------------------------
    // SYSTEM VARIABLES NAME
    //----------------------------------
    public static final String KEY_REGISTRAR = "REGISTRAR";
    public static final String KEY_REGISTRAR_DEFAULT = "LEILANIE M. LIZARDO";

    public static final String KEY_RECOMMENDING_APPROVAL = "RECOMMENDING_APPROVAL";
    public static final String KEY_RECOMMENDING_APPROVAL_DEFAULT = "ENGR. NOEMI P. REYES";

    public final static String KEY_BULSU_TEL = "BULSU_TELEPHONE_NO";
    public final static String KEY_BULSU_TEL_DEFAULT = "(044) 9197800 LOCAL 1101";

    public final static String KEY_MAX_POPULATION_NAME = "MAXIMUM_POPULATION";
    public final static String KEY_MAX_POPULATION_DEFAULT = "20";

    public final static String KEY_FTP_PORT = "FTP_PORT";
    public final static String KEY_FTP_PORT_DEFAULT = "21";

    public final static String KEY_FTP_USERNAME = "FTP_USERNAME";
    public final static String KEY_FTP_USERNAME_DEFAULT = "ftp-cict";

    public final static String KEY_FTP_PASSWORD = "FTP_PASSWORD";
    public final static String KEY_FTP_PASSWORD_DEFAULT = "123456";

    public final static String KEY_FTP_SERVER = "FTP_SERVER";
    public final static String KEY_FTP_SERVER_DEFAULT = "127.0.0.1";

    public final static String KEY_SMS_SERVER = "SMS_SERVER";
    public final static String KEY_SMS_SERVER_DEFAULT = "127.0.0.1";

    public static final String KEY_NOTED_BY = "NOTED_BY";
    public static final String KEY_NOTED_BY_DEFAULT = "Engr. Noemi P. Reyes, Dean-CICT";

    public static final String KEY_RETENTION_LETTER_SENDER1 = "LOCAL_REGISTRAR1";
    public static final String KEY_RETENTION_LETTER_SENDER_DEFAULT1 = "Engr. Alex P. Caparas, Local Registrar-BSIT";

    public static final String KEY_RETENTION_LETTER_SENDER2 = "LOCAL_REGISTRAR2";
    public static final String KEY_RETENTION_LETTER_SENDER_DEFAULT2 = "Engr. Evelyn Samson, Local Registrar-BSIT";

    //----------------------------------------------
    public static String SQL_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    //-----------------------------------------------
    public static boolean DISABLE_ASSISTANCE = false;

    //------------------------
    public static Object getSystemVar_BULSU_TEL() {
        return getSystemValue(KEY_BULSU_TEL, KEY_BULSU_TEL_DEFAULT);
    }

    public static Object getSystemVar_REGISTRAR() {
        return getSystemValue(KEY_REGISTRAR, KEY_REGISTRAR_DEFAULT);
    }

    public static Object getSystemVar_RECOMMENDNG_APPRVL() {
        return getSystemValue(KEY_RECOMMENDING_APPROVAL, KEY_RECOMMENDING_APPROVAL_DEFAULT);
    }

    public static Object getSystemVar_MAX_POPULATION() {
        return getSystemValue(KEY_MAX_POPULATION_NAME, KEY_MAX_POPULATION_DEFAULT);
    }

    public static Object getSystemVar_FTP_PORT() {
        return getSystemValue(KEY_FTP_PORT, KEY_FTP_PORT_DEFAULT);
    }

    public static Object getSystemVar_FTP_USERNAME() {
        return getSystemValue(KEY_FTP_USERNAME, KEY_FTP_USERNAME_DEFAULT);
    }

    public static Object getSystemVar_FTP_PASSWORD() {
        return getSystemValue(KEY_FTP_PASSWORD, KEY_FTP_PASSWORD_DEFAULT);
    }

    public static Object getSystemVar_FTP_SERVER() {
        return getSystemValue(KEY_FTP_SERVER, KEY_FTP_SERVER_DEFAULT);
    }

    public static Object getSystemVar_SMS_SERVER() {
        return getSystemValue(KEY_SMS_SERVER, KEY_SMS_SERVER_DEFAULT);
    }

    public static Object getSystemVar_Noted_By() {
        return getSystemValue(KEY_NOTED_BY, KEY_NOTED_BY_DEFAULT);
    }

    public static Object getSystemVar_LocalRegistrar1() {
        return getSystemValue(KEY_RETENTION_LETTER_SENDER1, KEY_RETENTION_LETTER_SENDER_DEFAULT1);
    }

    public static Object getSystemVar_LocalRegistrar2() {
        return getSystemValue(KEY_RETENTION_LETTER_SENDER2, KEY_RETENTION_LETTER_SENDER_DEFAULT2);
    }

    private static Object getSystemValue(String name, String defaultValue) {
        Object value = getServerValues(name);
        if (value == null) {
            createDefault(name, defaultValue);
            return defaultValue;
        }
        return value;
    }

    private static boolean createDefault(String name, String value) {
        SystemVariablesMapping defaultValue = new SystemVariablesMapping();
        defaultValue.setActive(1);
        defaultValue.setCreated_by(CollegeFaculty.instance().getFACULTY_ID());
        defaultValue.setCreated_date(Mono.orm().getServerTime().getDateWithFormat());
        defaultValue.setName(name);
        defaultValue.setValue(value);
        Integer res = Database.connect().system_variables().insert(defaultValue);
        return (res.equals(-1));
    }
}

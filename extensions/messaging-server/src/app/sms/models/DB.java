package app.sms.models;

/**
 *
 * @author Jhon Melvin
 */
public class DB {private final static Tbl_exception_log exception_log = new Tbl_exception_log();
    public static Tbl_exception_log exception_log() {
        return exception_log;
    }private final static Tbl_sms_log sms_log = new Tbl_sms_log();
    public static Tbl_sms_log sms_log() {
        return sms_log;
    }private final static Tbl_sms_query sms_query = new Tbl_sms_query();
    public static Tbl_sms_query sms_query() {
        return sms_query;
    }
}
package app.sms.models;

/**
 *
 * @author Jhon Melvin
 */
public class MapFactory {

    public MapFactory() {

    }
    private static MapFactory MAP_FACTORY_INSTANCE;

    public static MapFactory map() {
        if (MAP_FACTORY_INSTANCE == null) {
            MAP_FACTORY_INSTANCE = new MapFactory();
        }
        return MAP_FACTORY_INSTANCE;
    }
public ExceptionLogMapping exception_log() {
return new ExceptionLogMapping();
}
public SmsLogMapping sms_log() {
return new SmsLogMapping();
}
public SmsQueryMapping sms_query() {
return new SmsQueryMapping();
}
}

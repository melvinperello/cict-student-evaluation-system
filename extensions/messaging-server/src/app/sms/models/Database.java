
package app.sms.models;

import com.jhmvin.Mono;
import com.jhmvin.orm.MonoModels;


public class Database {
// construct
private Database() {setup();}
private static Database DATABASE_INSTANCE;
    public static Database connect() {
        if (DATABASE_INSTANCE == null) {
            DATABASE_INSTANCE = new Database();
        }
        return DATABASE_INSTANCE;
    }
private MonoModels tbl_exception_log;
private MonoModels tbl_sms_log;
private MonoModels tbl_sms_query;
private void setup() {
Mono.orm().setConnectionDriverClass("org.mariadb.jdbc.Driver");
Mono.orm().setConnectionProvider("jdbc:mariadb");
Mono.orm().setHost("127.0.0.1");
Mono.orm().setPort(3306);
Mono.orm().setDatabaseName("sms_server");
Mono.orm().setDatabaseUser("root");
Mono.orm().setDatabasePassword("root");
Mono.orm().setShowSQL(false);
Mono.orm().setShowLogs(false);
// c3p0 settings
Mono.orm().setC3p0_max(5);
Mono.orm().setC3p0_min(3);
Mono.orm().setC3p0_timeout(300);
Mono.orm().setC3p0_max_statements(0);
Mono.orm().setC3p0_idle_period(0);
// mapping information
Mono.orm().setMappingsLocation("app/sms/models/");
Mono.orm().addMappings("ExceptionLogMapping");
this.tbl_exception_log = Mono.orm().createModel(ExceptionLogMapping.class);
Mono.orm().addMappings("SmsLogMapping");
this.tbl_sms_log = Mono.orm().createModel(SmsLogMapping.class);
Mono.orm().addMappings("SmsQueryMapping");
this.tbl_sms_query = Mono.orm().createModel(SmsQueryMapping.class);
Mono.orm().connect();
}
public MonoModels exception_log() {
return tbl_exception_log;
}
public MonoModels sms_log() {
return tbl_sms_log;
}
public MonoModels sms_query() {
return tbl_sms_query;
}
}

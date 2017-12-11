
package app.lazy.models;

import com.jhmvin.Mono;
import com.jhmvin.orm.MonoModels;
import org.cict.PublicConstants;


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
private MonoModels tbl_academic_program;
private MonoModels tbl_academic_term;
private MonoModels tbl_account_faculty;
private MonoModels tbl_account_faculty_attempt;
private MonoModels tbl_account_faculty_session;
private MonoModels tbl_account_student;
private MonoModels tbl_announcements;
private MonoModels tbl_curriculum;
private MonoModels tbl_curriculum_history;
private MonoModels tbl_curriculum_history_summary;
private MonoModels tbl_curriculum_pre;
private MonoModels tbl_curriculum_requisite_ext;
private MonoModels tbl_curriculum_requisite_line;
private MonoModels tbl_curriculum_subject;
private MonoModels tbl_evaluation;
private MonoModels tbl_faculty;
private MonoModels tbl_faculty_data_history;
private MonoModels tbl_faculty_profile;
private MonoModels tbl_grade;
private MonoModels tbl_linked_entrance;
private MonoModels tbl_linked_marshall_session;
private MonoModels tbl_linked_pila;
private MonoModels tbl_linked_pila_3f;
private MonoModels tbl_linked_pila_4f;
private MonoModels tbl_linked_settings;
private MonoModels tbl_linked_telemetry;
private MonoModels tbl_load_group;
private MonoModels tbl_load_group_schedule;
private MonoModels tbl_load_section;
private MonoModels tbl_load_subject;
private MonoModels tbl_otp_generator;
private MonoModels tbl_print_logs;
private MonoModels tbl_student;
private MonoModels tbl_student_course_history;
private MonoModels tbl_student_data_history;
private MonoModels tbl_student_profile;
private MonoModels tbl_subject;
private MonoModels tbl_system_override_logs;
private MonoModels tbl_system_variables;
private void setup() {
Mono.orm().setConnectionDriverClass("org.mariadb.jdbc.Driver");
Mono.orm().setConnectionProvider("jdbc:mariadb");
Mono.orm().setHost(PublicConstants.getServerIP());
Mono.orm().setPort(3306);
Mono.orm().setDatabaseName("cictems");
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
Mono.orm().setMappingsLocation("app/lazy/models/");
Mono.orm().addMappings("AcademicProgramMapping");
this.tbl_academic_program = Mono.orm().createModel(AcademicProgramMapping.class);
Mono.orm().addMappings("AcademicTermMapping");
this.tbl_academic_term = Mono.orm().createModel(AcademicTermMapping.class);
Mono.orm().addMappings("AccountFacultyMapping");
this.tbl_account_faculty = Mono.orm().createModel(AccountFacultyMapping.class);
Mono.orm().addMappings("AccountFacultyAttemptMapping");
this.tbl_account_faculty_attempt = Mono.orm().createModel(AccountFacultyAttemptMapping.class);
Mono.orm().addMappings("AccountFacultySessionMapping");
this.tbl_account_faculty_session = Mono.orm().createModel(AccountFacultySessionMapping.class);
Mono.orm().addMappings("AccountStudentMapping");
this.tbl_account_student = Mono.orm().createModel(AccountStudentMapping.class);
Mono.orm().addMappings("AnnouncementsMapping");
this.tbl_announcements = Mono.orm().createModel(AnnouncementsMapping.class);
Mono.orm().addMappings("CurriculumMapping");
this.tbl_curriculum = Mono.orm().createModel(CurriculumMapping.class);
Mono.orm().addMappings("CurriculumHistoryMapping");
this.tbl_curriculum_history = Mono.orm().createModel(CurriculumHistoryMapping.class);
Mono.orm().addMappings("CurriculumHistorySummaryMapping");
this.tbl_curriculum_history_summary = Mono.orm().createModel(CurriculumHistorySummaryMapping.class);
Mono.orm().addMappings("CurriculumPreMapping");
this.tbl_curriculum_pre = Mono.orm().createModel(CurriculumPreMapping.class);
Mono.orm().addMappings("CurriculumRequisiteExtMapping");
this.tbl_curriculum_requisite_ext = Mono.orm().createModel(CurriculumRequisiteExtMapping.class);
Mono.orm().addMappings("CurriculumRequisiteLineMapping");
this.tbl_curriculum_requisite_line = Mono.orm().createModel(CurriculumRequisiteLineMapping.class);
Mono.orm().addMappings("CurriculumSubjectMapping");
this.tbl_curriculum_subject = Mono.orm().createModel(CurriculumSubjectMapping.class);
Mono.orm().addMappings("EvaluationMapping");
this.tbl_evaluation = Mono.orm().createModel(EvaluationMapping.class);
Mono.orm().addMappings("FacultyMapping");
this.tbl_faculty = Mono.orm().createModel(FacultyMapping.class);
Mono.orm().addMappings("FacultyDataHistoryMapping");
this.tbl_faculty_data_history = Mono.orm().createModel(FacultyDataHistoryMapping.class);
Mono.orm().addMappings("FacultyProfileMapping");
this.tbl_faculty_profile = Mono.orm().createModel(FacultyProfileMapping.class);
Mono.orm().addMappings("GradeMapping");
this.tbl_grade = Mono.orm().createModel(GradeMapping.class);
Mono.orm().addMappings("LinkedEntranceMapping");
this.tbl_linked_entrance = Mono.orm().createModel(LinkedEntranceMapping.class);
Mono.orm().addMappings("LinkedMarshallSessionMapping");
this.tbl_linked_marshall_session = Mono.orm().createModel(LinkedMarshallSessionMapping.class);
Mono.orm().addMappings("LinkedPilaMapping");
this.tbl_linked_pila = Mono.orm().createModel(LinkedPilaMapping.class);
Mono.orm().addMappings("LinkedPila3fMapping");
this.tbl_linked_pila_3f = Mono.orm().createModel(LinkedPila3fMapping.class);
Mono.orm().addMappings("LinkedPila4fMapping");
this.tbl_linked_pila_4f = Mono.orm().createModel(LinkedPila4fMapping.class);
Mono.orm().addMappings("LinkedSettingsMapping");
this.tbl_linked_settings = Mono.orm().createModel(LinkedSettingsMapping.class);
Mono.orm().addMappings("LinkedTelemetryMapping");
this.tbl_linked_telemetry = Mono.orm().createModel(LinkedTelemetryMapping.class);
Mono.orm().addMappings("LoadGroupMapping");
this.tbl_load_group = Mono.orm().createModel(LoadGroupMapping.class);
Mono.orm().addMappings("LoadGroupScheduleMapping");
this.tbl_load_group_schedule = Mono.orm().createModel(LoadGroupScheduleMapping.class);
Mono.orm().addMappings("LoadSectionMapping");
this.tbl_load_section = Mono.orm().createModel(LoadSectionMapping.class);
Mono.orm().addMappings("LoadSubjectMapping");
this.tbl_load_subject = Mono.orm().createModel(LoadSubjectMapping.class);
Mono.orm().addMappings("OtpGeneratorMapping");
this.tbl_otp_generator = Mono.orm().createModel(OtpGeneratorMapping.class);
Mono.orm().addMappings("PrintLogsMapping");
this.tbl_print_logs = Mono.orm().createModel(PrintLogsMapping.class);
Mono.orm().addMappings("StudentMapping");
this.tbl_student = Mono.orm().createModel(StudentMapping.class);
Mono.orm().addMappings("StudentCourseHistoryMapping");
this.tbl_student_course_history = Mono.orm().createModel(StudentCourseHistoryMapping.class);
Mono.orm().addMappings("StudentDataHistoryMapping");
this.tbl_student_data_history = Mono.orm().createModel(StudentDataHistoryMapping.class);
Mono.orm().addMappings("StudentProfileMapping");
this.tbl_student_profile = Mono.orm().createModel(StudentProfileMapping.class);
Mono.orm().addMappings("SubjectMapping");
this.tbl_subject = Mono.orm().createModel(SubjectMapping.class);
Mono.orm().addMappings("SystemOverrideLogsMapping");
this.tbl_system_override_logs = Mono.orm().createModel(SystemOverrideLogsMapping.class);
Mono.orm().addMappings("SystemVariablesMapping");
this.tbl_system_variables = Mono.orm().createModel(SystemVariablesMapping.class);
Mono.orm().connect();
}
public MonoModels academic_program() {
return tbl_academic_program;
}
public MonoModels academic_term() {
return tbl_academic_term;
}
public MonoModels account_faculty() {
return tbl_account_faculty;
}
public MonoModels account_faculty_attempt() {
return tbl_account_faculty_attempt;
}
public MonoModels account_faculty_session() {
return tbl_account_faculty_session;
}
public MonoModels account_student() {
return tbl_account_student;
}
public MonoModels announcements() {
return tbl_announcements;
}
public MonoModels curriculum() {
return tbl_curriculum;
}
public MonoModels curriculum_history() {
return tbl_curriculum_history;
}
public MonoModels curriculum_history_summary() {
return tbl_curriculum_history_summary;
}
public MonoModels curriculum_pre() {
return tbl_curriculum_pre;
}
public MonoModels curriculum_requisite_ext() {
return tbl_curriculum_requisite_ext;
}
public MonoModels curriculum_requisite_line() {
return tbl_curriculum_requisite_line;
}
public MonoModels curriculum_subject() {
return tbl_curriculum_subject;
}
public MonoModels evaluation() {
return tbl_evaluation;
}
public MonoModels faculty() {
return tbl_faculty;
}
public MonoModels faculty_data_history() {
return tbl_faculty_data_history;
}
public MonoModels faculty_profile() {
return tbl_faculty_profile;
}
public MonoModels grade() {
return tbl_grade;
}
public MonoModels linked_entrance() {
return tbl_linked_entrance;
}
public MonoModels linked_marshall_session() {
return tbl_linked_marshall_session;
}
public MonoModels linked_pila() {
return tbl_linked_pila;
}
public MonoModels linked_pila_3f() {
return tbl_linked_pila_3f;
}
public MonoModels linked_pila_4f() {
return tbl_linked_pila_4f;
}
public MonoModels linked_settings() {
return tbl_linked_settings;
}
public MonoModels linked_telemetry() {
return tbl_linked_telemetry;
}
public MonoModels load_group() {
return tbl_load_group;
}
public MonoModels load_group_schedule() {
return tbl_load_group_schedule;
}
public MonoModels load_section() {
return tbl_load_section;
}
public MonoModels load_subject() {
return tbl_load_subject;
}
public MonoModels otp_generator() {
return tbl_otp_generator;
}
public MonoModels print_logs() {
return tbl_print_logs;
}
public MonoModels student() {
return tbl_student;
}
public MonoModels student_course_history() {
return tbl_student_course_history;
}
public MonoModels student_data_history() {
return tbl_student_data_history;
}
public MonoModels student_profile() {
return tbl_student_profile;
}
public MonoModels subject() {
return tbl_subject;
}
public MonoModels system_override_logs() {
return tbl_system_override_logs;
}
public MonoModels system_variables() {
return tbl_system_variables;
}
}

package app.lazy.models;

/**
 *
 * @author Jhon Melvin
 */
public class DB {private final static Tbl_academic_program academic_program = new Tbl_academic_program();
    public static Tbl_academic_program academic_program() {
        return academic_program;
    }private final static Tbl_academic_term academic_term = new Tbl_academic_term();
    public static Tbl_academic_term academic_term() {
        return academic_term;
    }private final static Tbl_account_faculty account_faculty = new Tbl_account_faculty();
    public static Tbl_account_faculty account_faculty() {
        return account_faculty;
    }private final static Tbl_account_faculty_attempt account_faculty_attempt = new Tbl_account_faculty_attempt();
    public static Tbl_account_faculty_attempt account_faculty_attempt() {
        return account_faculty_attempt;
    }private final static Tbl_account_faculty_session account_faculty_session = new Tbl_account_faculty_session();
    public static Tbl_account_faculty_session account_faculty_session() {
        return account_faculty_session;
    }private final static Tbl_account_student account_student = new Tbl_account_student();
    public static Tbl_account_student account_student() {
        return account_student;
    }private final static Tbl_announcements announcements = new Tbl_announcements();
    public static Tbl_announcements announcements() {
        return announcements;
    }private final static Tbl_backup_logs backup_logs = new Tbl_backup_logs();
    public static Tbl_backup_logs backup_logs() {
        return backup_logs;
    }private final static Tbl_backup_schedule backup_schedule = new Tbl_backup_schedule();
    public static Tbl_backup_schedule backup_schedule() {
        return backup_schedule;
    }private final static Tbl_curriculum curriculum = new Tbl_curriculum();
    public static Tbl_curriculum curriculum() {
        return curriculum;
    }private final static Tbl_curriculum_history curriculum_history = new Tbl_curriculum_history();
    public static Tbl_curriculum_history curriculum_history() {
        return curriculum_history;
    }private final static Tbl_curriculum_history_summary curriculum_history_summary = new Tbl_curriculum_history_summary();
    public static Tbl_curriculum_history_summary curriculum_history_summary() {
        return curriculum_history_summary;
    }private final static Tbl_curriculum_pre curriculum_pre = new Tbl_curriculum_pre();
    public static Tbl_curriculum_pre curriculum_pre() {
        return curriculum_pre;
    }private final static Tbl_curriculum_requisite_ext curriculum_requisite_ext = new Tbl_curriculum_requisite_ext();
    public static Tbl_curriculum_requisite_ext curriculum_requisite_ext() {
        return curriculum_requisite_ext;
    }private final static Tbl_curriculum_requisite_line curriculum_requisite_line = new Tbl_curriculum_requisite_line();
    public static Tbl_curriculum_requisite_line curriculum_requisite_line() {
        return curriculum_requisite_line;
    }private final static Tbl_curriculum_subject curriculum_subject = new Tbl_curriculum_subject();
    public static Tbl_curriculum_subject curriculum_subject() {
        return curriculum_subject;
    }private final static Tbl_evaluation evaluation = new Tbl_evaluation();
    public static Tbl_evaluation evaluation() {
        return evaluation;
    }private final static Tbl_faculty faculty = new Tbl_faculty();
    public static Tbl_faculty faculty() {
        return faculty;
    }private final static Tbl_faculty_data_history faculty_data_history = new Tbl_faculty_data_history();
    public static Tbl_faculty_data_history faculty_data_history() {
        return faculty_data_history;
    }private final static Tbl_faculty_profile faculty_profile = new Tbl_faculty_profile();
    public static Tbl_faculty_profile faculty_profile() {
        return faculty_profile;
    }private final static Tbl_grade grade = new Tbl_grade();
    public static Tbl_grade grade() {
        return grade;
    }private final static Tbl_linked_entrance linked_entrance = new Tbl_linked_entrance();
    public static Tbl_linked_entrance linked_entrance() {
        return linked_entrance;
    }private final static Tbl_linked_marshall_session linked_marshall_session = new Tbl_linked_marshall_session();
    public static Tbl_linked_marshall_session linked_marshall_session() {
        return linked_marshall_session;
    }private final static Tbl_linked_pila linked_pila = new Tbl_linked_pila();
    public static Tbl_linked_pila linked_pila() {
        return linked_pila;
    }private final static Tbl_linked_pila_3f linked_pila_3f = new Tbl_linked_pila_3f();
    public static Tbl_linked_pila_3f linked_pila_3f() {
        return linked_pila_3f;
    }private final static Tbl_linked_pila_4f linked_pila_4f = new Tbl_linked_pila_4f();
    public static Tbl_linked_pila_4f linked_pila_4f() {
        return linked_pila_4f;
    }private final static Tbl_linked_settings linked_settings = new Tbl_linked_settings();
    public static Tbl_linked_settings linked_settings() {
        return linked_settings;
    }private final static Tbl_linked_telemetry linked_telemetry = new Tbl_linked_telemetry();
    public static Tbl_linked_telemetry linked_telemetry() {
        return linked_telemetry;
    }private final static Tbl_load_group load_group = new Tbl_load_group();
    public static Tbl_load_group load_group() {
        return load_group;
    }private final static Tbl_load_group_schedule load_group_schedule = new Tbl_load_group_schedule();
    public static Tbl_load_group_schedule load_group_schedule() {
        return load_group_schedule;
    }private final static Tbl_load_section load_section = new Tbl_load_section();
    public static Tbl_load_section load_section() {
        return load_section;
    }private final static Tbl_load_subject load_subject = new Tbl_load_subject();
    public static Tbl_load_subject load_subject() {
        return load_subject;
    }private final static Tbl_otp_generator otp_generator = new Tbl_otp_generator();
    public static Tbl_otp_generator otp_generator() {
        return otp_generator;
    }private final static Tbl_print_logs print_logs = new Tbl_print_logs();
    public static Tbl_print_logs print_logs() {
        return print_logs;
    }private final static Tbl_student student = new Tbl_student();
    public static Tbl_student student() {
        return student;
    }private final static Tbl_student_course_history student_course_history = new Tbl_student_course_history();
    public static Tbl_student_course_history student_course_history() {
        return student_course_history;
    }private final static Tbl_student_data_history student_data_history = new Tbl_student_data_history();
    public static Tbl_student_data_history student_data_history() {
        return student_data_history;
    }private final static Tbl_student_profile student_profile = new Tbl_student_profile();
    public static Tbl_student_profile student_profile() {
        return student_profile;
    }private final static Tbl_subject subject = new Tbl_subject();
    public static Tbl_subject subject() {
        return subject;
    }private final static Tbl_system_override_logs system_override_logs = new Tbl_system_override_logs();
    public static Tbl_system_override_logs system_override_logs() {
        return system_override_logs;
    }private final static Tbl_system_variables system_variables = new Tbl_system_variables();
    public static Tbl_system_variables system_variables() {
        return system_variables;
    }
}
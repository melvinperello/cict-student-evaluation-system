package app.lazy.models;

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
public AcademicProgramMapping academic_program() {
return new AcademicProgramMapping();
}
public AcademicTermMapping academic_term() {
return new AcademicTermMapping();
}
public AccountFacultyMapping account_faculty() {
return new AccountFacultyMapping();
}
public AccountFacultyAttemptMapping account_faculty_attempt() {
return new AccountFacultyAttemptMapping();
}
public AccountFacultySessionMapping account_faculty_session() {
return new AccountFacultySessionMapping();
}
public AccountStudentMapping account_student() {
return new AccountStudentMapping();
}
public CurriculumMapping curriculum() {
return new CurriculumMapping();
}
public CurriculumHistoryMapping curriculum_history() {
return new CurriculumHistoryMapping();
}
public CurriculumHistorySummaryMapping curriculum_history_summary() {
return new CurriculumHistorySummaryMapping();
}
public CurriculumPreMapping curriculum_pre() {
return new CurriculumPreMapping();
}
public CurriculumRequisiteExtMapping curriculum_requisite_ext() {
return new CurriculumRequisiteExtMapping();
}
public CurriculumRequisiteLineMapping curriculum_requisite_line() {
return new CurriculumRequisiteLineMapping();
}
public CurriculumSubjectMapping curriculum_subject() {
return new CurriculumSubjectMapping();
}
public EvaluationMapping evaluation() {
return new EvaluationMapping();
}
public FacultyMapping faculty() {
return new FacultyMapping();
}
public FacultyProfileMapping faculty_profile() {
return new FacultyProfileMapping();
}
public GradeMapping grade() {
return new GradeMapping();
}
public LinkedEntranceMapping linked_entrance() {
return new LinkedEntranceMapping();
}
public LinkedMarshallSessionMapping linked_marshall_session() {
return new LinkedMarshallSessionMapping();
}
public LinkedPilaMapping linked_pila() {
return new LinkedPilaMapping();
}
public LinkedPila3fMapping linked_pila_3f() {
return new LinkedPila3fMapping();
}
public LinkedPila4fMapping linked_pila_4f() {
return new LinkedPila4fMapping();
}
public LinkedSettingsMapping linked_settings() {
return new LinkedSettingsMapping();
}
public LinkedTelemetryMapping linked_telemetry() {
return new LinkedTelemetryMapping();
}
public LoadGroupMapping load_group() {
return new LoadGroupMapping();
}
public LoadGroupScheduleMapping load_group_schedule() {
return new LoadGroupScheduleMapping();
}
public LoadSectionMapping load_section() {
return new LoadSectionMapping();
}
public LoadSubjectMapping load_subject() {
return new LoadSubjectMapping();
}
public StudentMapping student() {
return new StudentMapping();
}
public StudentProfileMapping student_profile() {
return new StudentProfileMapping();
}
public SubjectMapping subject() {
return new SubjectMapping();
}
}

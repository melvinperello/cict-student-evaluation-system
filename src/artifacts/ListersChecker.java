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
package artifacts;

import app.lazy.models.AcademicTermMapping;
import app.lazy.models.CurriculumMapping;
import app.lazy.models.CurriculumSubjectMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.GradeMapping;
import app.lazy.models.StudentMapping;
import com.jhmvin.Mono;
import com.jhmvin.orm.SQL;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import sys.org.cict.enumerations.CurriculumValues;

/**
 *
 * @author Jhon Melvin
 */
public class ListersChecker {

    private final static double DEAN_CAP = 1.75;
    private final static double PRES_CAP = 1.20;
    private final static DecimalFormat df = new DecimalFormat("#.###");

    public enum ListerMode {
        DEANS_LIST,
        PRESIDENTS_LIST
    }

    private AcademicTermMapping acadTermMapping;
    private ListerMode listerMode;
    private Integer CURRICULUM_id;

    public void setCurrentTerm(AcademicTermMapping currentTerm) {
        this.acadTermMapping = currentTerm;
    }

    public void setListerMode(ListerMode listerMode) {
        this.listerMode = listerMode;
    }

    public void setCURRICULUM_id(Integer CURRICULUM_id) {
        this.CURRICULUM_id = CURRICULUM_id;
    }

    public ArrayList<ListerData> check() {
        //----------------------------------------------------------------------
        // Check current term if regular and second semester
        if (!isCurrentTermRegular(acadTermMapping)) {
            return null;
        }
        //----------------------------------------------------------------------
        // Get Qualified Students
        ArrayList<StudentMapping> qualifiedStudents = this.listValidStudents(CURRICULUM_id);
        //----------------------------------------------------------------------
        // Check Grades of Students
        return this.checkGrades(qualifiedStudents);
    }

    /**
     * Get all qualified students for listers.
     *
     * @return
     */
    private ArrayList<StudentMapping> listValidStudents(Integer CURRICULUM_id) {
        ArrayList<StudentMapping> qualifiedStudents = Mono.orm()
                .newSearch(Database.connect().student())
                .eq(DB.student().college, "CICT")
                .eq(DB.student().campus, "MAIN")
                .eq(DB.student().residency, "REGULAR")
                .eq(DB.student().university, "HOME")
                .eq(DB.student().verified, 1)
                .put(Restrictions.isNotNull(DB.student().year_level))
                .ne(DB.student().year_level, 1)
                .eq(DB.student().last_evaluation_term, this.acadTermMapping.getId())
                //-----------------------
                // added curriculum_id
                .eq(DB.student().CURRICULUM_id, CURRICULUM_id)
                //-----
                .active(Order.desc(DB.student().cict_id))
                .all();
        return qualifiedStudents;
    }

    /**
     * Check Current term. it must be second semester.
     *
     * @param currentTerm
     * @return
     */
    private boolean isCurrentTermRegular(AcademicTermMapping currentTerm) {
        if (currentTerm.getSemester_regular().equals(2)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check student grades of qualified students.
     *
     * @param qualifiedStudents
     * @return
     */
    private ArrayList<ListerData> checkGrades(ArrayList<StudentMapping> qualifiedStudents) {
        ArrayList<ListerData> listerList = new ArrayList<>();
        /**
         * So this is a fucking loop and it requires a lot of data in the
         * database it is advisable to use a cache to prevent the database from
         * exhaustion. this is a one time process it means data can be cached
         * without conflict for external changes during the operation.
         */
        //----------------------------------------------------------------------
        // Curriculum Cache (Temporary)
        this.curriculumCache = new HashMap<>();
        this.curriculumSubjectCache = new HashMap<>();
        //----------------------------------------------------------------------
        // Iterate all the qualified students for verification
        if(qualifiedStudents==null) {
            return listerList;
        }
        for (StudentMapping student : qualifiedStudents) { // Start Loop
            Integer yearLevel = student.getYear_level();
            //------------------------------------------------------------------
            // Get Student Current Curriculum from cache if applicable.
            CurriculumMapping studentCurriculum = getCurriculum(student.getCURRICULUM_id());
            //------------------------------------------------------------------
            if (studentCurriculum == null) {
                // this is nearly imposible but but always use best practices
                // skip this student if curriculum was null
                continue;
            }
            //------------------------------------------------------------------
            if (studentCurriculum.getLadderization_type().equals(CurriculumValues.LadderType.CONSEQUENT.toString())) {
                // This is complicated
                if (yearLevel.equals(3)) {
                    // do stuff before returning
                    //----------------------------------------------------------
                    // (Code Here)
                    // (Code Here)
                    // (Code Here)
                    // (Code Here)
                    // (Code Here)
                    // (Code Here)
                    // (Code Here)
                    // (Code Here)
                    // (Code Here)
                    // (Code Here)
                    //----------------------------------------------------------
                    continue; // to skip code below for regulars
                }
            }
            //------------------------------------------------------------------
            // This is somehow easy because the course is not ladderzied
            // Get subjects from curriclum
            // Prepare curriculumID
            // Prepare Year Level
            // Prepare Semester
            //------------------------------------------------------------------
            // Phase 1 Subjects
            ArrayList<CurriculumSubjectMapping> subjectsPhase1
                    = getSubjectsFromCurriculum(studentCurriculum.getId(),
                            (yearLevel - 1), 2);
            //------------------------------------------------------------------
            // Phase 2 Subjects
            ArrayList<CurriculumSubjectMapping> subjectsPhase2
                    = getSubjectsFromCurriculum(studentCurriculum.getId(),
                            (yearLevel), 1);
            //------------------------------------------------------------------
            double phase1Grade = validateGrade(subjectsPhase1);
            if (phase1Grade == 0.00) {
                continue;
            }
            double phase2Grade = validateGrade(subjectsPhase2);
            if (phase2Grade == 0.00) {
                continue;
            }
            //------------------------------------------------------------------
            double overAllgwa = (phase1Grade + phase2Grade) / 2.00;
            if (listerMode.equals(ListerMode.DEANS_LIST)) {
                if (overAllgwa <= DEAN_CAP) {
                    // deans list
                    df.setRoundingMode(RoundingMode.CEILING);
                    listerList.add(new ListerData(student, df.format(overAllgwa)));
                }
            } else {
                if (overAllgwa <= PRES_CAP) {
                    // pres list
                    df.setRoundingMode(RoundingMode.CEILING);
                    listerList.add(new ListerData(student, df.format(overAllgwa)));
                }
            }
            //------------------------------------------------------------------
        }// End Loop
        return listerList;
    }

    /**
     * Validate grade of students.
     *
     * @param phase
     * @return
     */
    private double validateGrade(ArrayList<CurriculumSubjectMapping> phase) {
        // subjects in that phase
        double totalUnits = 0.00;
        double totalGrade = 0.00;
        for (CurriculumSubjectMapping sub : phase) {
            GradeMapping studentGradeInThisSubject = Mono.orm().newSearch(Database.connect().grade())
                    // Grade for the subject
                    .eq(DB.grade().SUBJECT_id, sub.getSUBJECT_id())
                    // Must be posted
                    .eq(DB.grade().posted, 1)
                    // Must Be Accepted
                    .eq(DB.grade().grade_state, "ACCEPTED")
                    // Must NOT be cancelled or unposted
                    .put(SQL.or(
                            Restrictions.ne(DB.grade().rating, "CANCELLED"),
                            Restrictions.ne(DB.grade().rating, "UNPOSTED")
                    ))
                    // Acitve or Inactive
                    .active(Order.desc(DB.grade().id))
                    // get all
                    .first();
            if (studentGradeInThisSubject == null) {
                // if no grade this student is disqualified
                // as prescribe by the curriculum
                return 0.00;
            }
            //------------------------------------------------------------------
            try {
                double grade = Double.parseDouble(studentGradeInThisSubject.getRating());
                if (grade > 2.00) {
                    // Disqualified lower than 2
                    return 0.00;
                } else {
                    // can be computed
                    double subjectUnits = studentGradeInThisSubject.getCredit();
                    //----------------------------------------------------------
                    totalUnits += subjectUnits;
                    totalGrade += (grade * subjectUnits);
                    //----------------------------------------------------------
                }
            } catch (NumberFormatException e) {
                // Disqualified cannot compute
                return 0.00;
            }
        }
        double gwa = totalGrade / totalUnits;
        return gwa;
    }

    //--------------------------------------------------------------------------
    private HashMap<String, ArrayList<CurriculumSubjectMapping>> curriculumSubjectCache;
    private HashMap<Integer, CurriculumMapping> curriculumCache;

    /**
     * Get subjects from curriculum with cache application
     *
     * @param curriculumID
     * @param year
     * @param sem
     * @return
     */
    private ArrayList<CurriculumSubjectMapping> getSubjectsFromCurriculum(Integer curriculumID, Integer year, Integer sem) {
        //----------------------------------------------------------------------
        // Create key for these set of subjects
        String cacheKey = String.valueOf(curriculumID)
                + "-"
                + String.valueOf(year)
                + "-"
                + String.valueOf(sem);
        //----------------------------------------------------------------------
        /**
         * The probability of a subject being changed during this transaction is
         * low enough. e.g. Subject Math 113 was altered during the operation
         * and the data in the cache was not updated. it is OK to cache this
         * process because this process is one time usage straight process. even
         * a change in the data in database for a short moment will not affect
         * this process.
         */
        ArrayList<CurriculumSubjectMapping> subjectList = this.curriculumSubjectCache.get(cacheKey);
        if (subjectList == null) {
            subjectList = Mono.orm().newSearch(Database.connect().curriculum_subject())
                    .eq(DB.curriculum_subject().CURRICULUM_id, curriculumID)
                    .eq(DB.curriculum_subject().year, year)
                    .eq(DB.curriculum_subject().semester, sem)
                    .active(Order.asc(DB.curriculum_subject().id))
                    .all();
            //------------------------------------------------------------------
            // If there was a result add it to cache
            if (subjectList != null) {
                this.curriculumSubjectCache.put(cacheKey, subjectList);
            }
        } else {
            System.out.println("Curriculum Subjects Recycled");
        }
        return subjectList;
    }

    /**
     * Get Student Curriculum from cache or DB.
     *
     * @param curriculumId
     * @return
     */
    private CurriculumMapping getCurriculum(Integer curriculumId) {
        CurriculumMapping cachedCurriculum = this.curriculumCache.get(curriculumId);
        if (cachedCurriculum == null) {
            // if not existing get it from DB
            cachedCurriculum = Database.connect().curriculum()
                    .getPrimary(curriculumId);
            if (cachedCurriculum != null) {
                // add to cache
                this.curriculumCache.put(cachedCurriculum.getId(), cachedCurriculum);
            }

        } else {
            // check if the curriculum is being fetched from cache
            System.out.println("Curriculum Recycled");
        }
        return cachedCurriculum;
    }

    //--------------------------------------------------------------------------
}

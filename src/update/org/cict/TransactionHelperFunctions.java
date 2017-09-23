package update.org.cict;

import app.lazy.models.*;
import com.jhmvin.Mono;
import org.apache.commons.lang3.ObjectUtils;

import javax.xml.crypto.Data;

/**
 * Commonly Needed Transaction Functions. Please used with caution.
 * Use only inside Trnasaction classes.
 */
public class TransactionHelperFunctions {

    /**
     * Since the curriculum, academic program and student relation are binded
     * with high level of table normalization. this function was created to ease
     * this connection. using the [CURRICULUM_id] from the student. this
     * function will return the related AcademicProgramMapping.
     *
     * @param curriculum_id from the student table
     * @return the AcademicProgramMapping
     */
    public static AcademicProgramMapping getAcademicProgram(Integer curriculum_id) {
        AcademicProgramMapping studentProgram;
        try {
            CurriculumMapping cmap = Mono.orm().newSearch(Database.connect().curriculum())
                    .eq(DB.curriculum().id, curriculum_id)
                    .execute()
                    .first();

            studentProgram = Mono.orm().newSearch(Database.connect().academic_program())
                    .eq(DB.academic_program().id, cmap.getACADPROG_id())
                    .execute()
                    .first();

            return studentProgram;
        } catch (NullPointerException ne) {
            return null;
        }
    }

    /**
     * @param loadGroupID loadGroup id from load_subject table
     * @return the LoadSectionMapping
     */
    public static LoadSectionMapping getSectionFromLoadGroup(Integer loadGroupID) {
        LoadSectionMapping section;
        try {
            LoadGroupMapping loadGroupMap = Mono.orm()
                    .newSearch(Database.connect().load_group())
                    .eq(DB.load_group().id, loadGroupID)
                    .execute()
                    .first();

            section = Mono.orm().newSearch(Database.connect().load_section())
                    .eq(DB.load_section().id, loadGroupMap.getLOADSEC_id())
                    .execute()
                    .first();

        } catch (NullPointerException ne) {
            return null;
        }

        return section;
    }
}

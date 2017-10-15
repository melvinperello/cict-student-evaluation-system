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
package update2.org.cict.controller.academicprogram;

import app.lazy.models.AcademicProgramMapping;
import app.lazy.models.CurriculumMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.FacultyMapping;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import com.jhmvin.orm.Searcher;
import java.util.ArrayList;
import org.hibernate.criterion.Order;

/**
 *
 * @author Jhon Melvin
 */
public class FetchAcademicPrograms extends Transaction {

    private ArrayList<AcademicProgramInfo> academicProgramsCollection;

    public ArrayList<AcademicProgramInfo> getAcademicProgramsCollection() {
        return academicProgramsCollection;
    }

    @Override
    protected boolean transaction() {
        academicProgramsCollection = new ArrayList<>();

        ArrayList<AcademicProgramMapping> academicPrograms = Mono.orm()
                .newSearch(Database.connect().academic_program())
                .active(Order.asc(DB.academic_program().code))
                .all();

        for (AcademicProgramMapping academicProgram : academicPrograms) {
            AcademicProgramInfo info = new AcademicProgramInfo();
            
            //academic program extended details
            FacultyMapping creator
                    = Mono.orm().newSearch(Database.connect().faculty())
                            .eq(DB.faculty().id, academicProgram.getCreated_by())
                            .execute()
                            .first();
            String created_by = creator.getFirst_name() + " " + creator.getLast_name();
            info.setApCreatedBy(created_by);
            
            // implementor
            FacultyMapping implementor = Mono.orm().newSearch(Database.connect().faculty())
                    .eq(DB.faculty().id, academicProgram.getImplemented_by())
                    .execute()
                    .first();

            if (implementor != null) {
                String implemented_by = implementor.getFirst_name() + " " + implementor.getLast_name();
                info.setApImplementedBy(implemented_by);
            }
            //
            ArrayList<CurriculumMapping> curriculums = Mono.orm()
                    .newSearch(Database.connect().curriculum())
                    .eq(DB.curriculum().ACADPROG_id, academicProgram.getId())
                    .active()
                    .all();

            info.setAcademicProgram(academicProgram);
            info.setCurriculums(curriculums);
            this.academicProgramsCollection.add(info);
        }

        return true;
    }

    @Override
    protected void after() {

    }

}

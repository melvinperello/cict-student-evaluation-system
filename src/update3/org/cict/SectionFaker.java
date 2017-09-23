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
package update3.org.cict;

import app.lazy.models.CurriculumSubjectMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.LoadGroupMapping;
import app.lazy.models.LoadSectionMapping;
import app.lazy.models.MapFactory;
import com.jhmvin.Mono;
import java.util.ArrayList;
import java.util.Locale;
import org.hibernate.criterion.Order;

/**
 *
 * @author Jhon Melvin
 */
public class SectionFaker {

    public SectionFaker() {
        generate();
    }

    private void generate() {
        String sectionNames = "abcdefghijklmnopqrstuvwxyz";
        Integer study_years = 2;

        for (char c : sectionNames.toCharArray()) {
            for (int year = 1; year <= study_years; year++) {
                for (int group = 1; group <= 2; group++) {
                    createSection(c, year, group);
                }
            }
        }

        Mono.orm().shutdown();
        System.out.println("ORM OFF");

    }

    private void createSection(char c, Integer year, Integer _group) {
        Integer semester = 1;
        Integer acadTerm = 11;
        Integer curriculum = 6;
        Integer acadProg = 4;
        //
        String name = Character.toString(c).toUpperCase(Locale.ENGLISH);
        LoadSectionMapping section = MapFactory.map().load_section();
        section.setACADTERM_id(acadTerm);
        section.setCURRICULUM_id(curriculum);
        section.setACADPROG_id(acadProg);
        section.setSection_name(name);
        section.setYear_level(year);
        section.set_group(_group);

        int res = Database.connect().load_section().insert(section);

        if (res <= 0) {
            System.out.println("FAIL");
        } else {
            ArrayList<CurriculumSubjectMapping> subjects = Mono.orm().newSearch(Database.connect().curriculum_subject())
                    .eq(DB.curriculum_subject().CURRICULUM_id, curriculum)
                    .eq(DB.curriculum_subject().year, year)
                    .eq(DB.curriculum_subject().semester, semester)
                    .active(Order.asc(DB.curriculum_subject().id))
                    .all();

            for (CurriculumSubjectMapping subject : subjects) {
                LoadGroupMapping loadGroup = MapFactory.map().load_group();
                loadGroup.setSUBJECT_id(subject.getSUBJECT_id());
                loadGroup.setLOADSEC_id(res);
                loadGroup.setGroup_type("REGULAR");

                int gres = Database.connect().load_section().insert(loadGroup);

                if (gres <= 0) {
                    System.out.println("\tFAIL LG");
                } else {
                    System.out.println("\t - INSERTED load_group");
                }
            }

            System.out.println("INSERTED load_section");
        }
    }
}

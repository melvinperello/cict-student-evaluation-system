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
 * JOEMAR N. DE LA CRUZ
 * GRETHEL EINSTEIN BERNARDINO
 *
 * OTHER LIBRARIES THAT ARE USED BELONGS TO THEIR RESPECTFUL OWNERS AND AUTHORS.
 * NO COPYRIGHT ARE INTENTIONAL OR INTENDED.
 * THIS PROJECT IS NOT PROFITABLE HENCE FOR EDUCATIONAL PURPOSES ONLY.
 * THIS PROJECT IS ONLY FOR COMPLIANCE TO OUR REQUIREMENTS.
 * THIS PROJECT DOES NOT INCLUDE DISTRIBUTION FOR OTHER PURPOSES.
 *
 */
package update.org.cict.controller.adding;

import app.lazy.models.AcademicProgramMapping;
import app.lazy.models.CurriculumSubjectMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.LoadGroupMapping;
import app.lazy.models.LoadSectionMapping;
import app.lazy.models.SubjectMapping;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.collections.ObservableList;
import org.cict.evaluation.views.SectionSearchView;

/**
 *
 * @author Joemar
 */
public class SearchSection extends Transaction {

    public String subjectCode;
    public Integer CURRICULUM_id;

    private SubjectMapping searchedSubject;
    private ArrayList<LoadGroupMapping> loadGroups;
    private ArrayList<LoadSectionMapping> loadSections;
    private ArrayList<Integer> loadGroupPopulation;

    /**
     * Changed to type object to give way for load section mapping as third
     * index.
     */
    private ArrayList<ArrayList<Object>> searchResults = new ArrayList<>();

    /**
     * Values for subject info
     *
     * @date 08222017
     */
    private SubjectInformationHolder subInfo = new SubjectInformationHolder();

    /**
     * @date 08222017 this sub info still does not have load section data. load
     * section data will be added upon clicking the row.
     *
     * @return
     */
    public SubjectInformationHolder getSubInfo() {
        return subInfo;
    }

    public ArrayList<ArrayList<Object>> getSearchResults() {
        return searchResults;
    }

    @Override
    protected boolean transaction() {
        /**
         * Get the subject mapping
         */
//        searchedSubject = Mono.orm()
//                .newSearch(Database.connect().subject())
//                .eq(DB.subject().code, subjectCode)
//                .active()
//                .first();
        ArrayList<SubjectMapping> subjects = Mono.orm()
                .newSearch(Database.connect().subject())
                .eq(DB.subject().code, subjectCode)
                .active()
                .all();
        for(SubjectMapping subject: subjects) {
            CurriculumSubjectMapping csMap = Mono.orm().newSearch(Database.connect().curriculum_subject())
                    .eq(DB.curriculum_subject().SUBJECT_id, subject.getId())
                    .eq(DB.curriculum_subject().CURRICULUM_id, CURRICULUM_id)
                    .active()
                    .first();
            if(csMap != null) {
                searchedSubject = subject;
                break;
            }
        }
        
        if (Objects.isNull(searchedSubject)) {
            return false;
        }

        /**
         * Values for subject info
         *
         * @date 08222017
         */
        subInfo.setSubjectMap(searchedSubject);

        /**
         * If subject is existing get all load_groups with this subject ID.
         */
        int subject_id = searchedSubject.getId();
        loadGroups = Mono.orm()
                .newSearch(Database.connect().load_group())
                .eq(DB.load_group().SUBJECT_id, subject_id)
                .active()
                .all();

        if (Objects.isNull(loadGroups)) {
            return false;
        }

        /**
         * If load groups where found get its section and population.
         *
         * @date 08222017
         * @note since load section is required in subject information holder it
         * must be added also, but this is an array list and the load section is
         * still not selected. this data will be added upon clicking the
         * selected section in the window. leave it blank for the moment.
         */
        loadGroupPopulation = new ArrayList<>();
        loadSections = new ArrayList<>();
        loadGroups.forEach(load_group -> {
            // search section
            LoadSectionMapping section = Mono.orm()
                    .newSearch(Database.connect().load_section())
                    .eq(DB.load_section().id, load_group.getLOADSEC_id())
                    .active()
                    .first();
            loadSections.add(section);

            // get population
            int load_group_ids = load_group.getId(); // for load subject
            int population = 0;
            List res = Mono.orm()
                    .newSearch(Database.connect().load_subject())
                    .eq(DB.load_subject().LOADGRP_id, load_group_ids)
                    .active();
            if (Objects.isNull(res)) {
                population = 0;
            } else {
                population = res.size();
            }

            loadGroupPopulation.add(population);
        });

        return true;
    }

    @Override
    protected void after() {
        if (!this.checkResults()) {
            return;
        }

        for (int x = 0; x < this.loadGroups.size(); x++) {
//            ArrayList<Object> subList = new ArrayList<>();
            AcademicProgramMapping acadProg = Mono.orm()
                    .newSearch(Database.connect().academic_program())
                    .eq(DB.academic_program().id, loadSections.get(x).getACADPROG_id())
                    .execute()
                    .first();

            String course_code = "---";
            if (acadProg != null) {
                course_code = acadProg.getCode();
            }
            /**
             * Required for subinfo.
             *
             * @date 08222017
             */
            subInfo.setAcademicProgramMapping(acadProg);

//            String sectionWithFormat = course_code + " " + loadSections.get(x).getYear_level()
//                    + " "
//                    + loadSections.get(x).getSection_name()
//                    + " - G"
//                    + loadSections.get(x).get_group();
            String currentCount = this.loadGroupPopulation.get(x).toString();
//            subList.add(sectionWithFormat);
//            subList.add(currentCount);
            //
            /**
             * Load Section as Third Index.
             */
//            subList.add(this.loadSections.get(x));
            //
            ArrayList<Object> temp = new ArrayList<>();
            temp.add(currentCount);
            /**
             * @added parameter LoadGroupMapping this.loadGroups.get(x)
             */
            temp.add(this.addNewSubjectInformationHolder(searchedSubject, loadSections.get(x), this.loadGroups.get(x)));
            searchResults.add(temp);
        }

    }

    private SubjectInformationHolder addNewSubjectInformationHolder(SubjectMapping subject,
            LoadSectionMapping loadSection, LoadGroupMapping lgMap) {
        SubjectInformationHolder suggested = new SubjectInformationHolder();
        suggested.setSubjectMap(subject);
        suggested.setSectionMap(loadSection);

        AcademicProgramMapping acadProg = Mono.orm()
                .newSearch(Database.connect().academic_program())
                /**
                 * ACADPROG_id will be truncated to subject table.
                 *
                 * @revision 001
                 * @date: 8282017
                 */
                .eq(DB.academic_program().id, loadSection.getACADPROG_id())
                //.eq(DB.academic_program().id, subject.getACADPROG_id())
                .execute()
                .first();

        suggested.setAcademicProgramMapping(acadProg);
        // added 08.24.2017
        suggested.setLoadGroup(lgMap);
        return suggested;
    }

    private boolean checkResults() {

        /**
         * Checks if there is no null values
         */
        if (this.loadGroups == null || this.loadSections == null || this.loadGroupPopulation == null) {
            System.out.println("@something is null");
            return false;
        }

        if (this.loadGroups.size() == this.loadSections.size()) {
            if (this.loadSections.size() == this.loadGroupPopulation.size()) {
                return true;
            }
        }
        return false; // size mismatch
    }

}

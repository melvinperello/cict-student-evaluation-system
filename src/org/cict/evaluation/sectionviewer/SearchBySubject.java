/* 
 * Copyright (C) Jhon Melvin N. Perello - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * PROPRIETARY and CONFIDENTIAL
 *
 * Written by Jhon Melvin N. Perello <jhmvinperello@gmail.com>, 2017.
 *
 */
package org.cict.evaluation.sectionviewer;

import app.lazy.models.Database;
import org.cict.evaluation.views.SectionSearchView;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import java.util.ArrayList;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import app.lazy.models.AcademicProgramMapping;
import app.lazy.models.CurriculumMapping;
import app.lazy.models.DB;
import app.lazy.models.LoadGroupMapping;
import app.lazy.models.LoadSectionMapping;
import app.lazy.models.SubjectMapping;
import java.util.HashMap;

/**
 *
 * @author Jhon Melvin
 */
public class SearchBySubject extends Transaction {

    /**
     * Preparing public values.
     */
    public String subjectCode;

    /**
     * Search Cached Values
     */
    private ArrayList<SubjectMapping> searchedSubject;
    private ArrayList<LoadGroupMapping> loadGroups;
    private ArrayList<LoadSectionMapping> loadSections;
    private ArrayList<String> loadGroupPopulation;

    public void log(Object message) {
        System.out.println(this.getClass().getSimpleName() + ": " + message);
    }

    /**
     *
     *
     */
    private final ObservableList<SectionSearchView> searchResults = FXCollections.observableArrayList();

    public ObservableList<SectionSearchView> getSearchResults() {
        return searchResults;
    }

    @Override
    protected boolean transaction() {
        System.out.println("Search By Subject Started.");

        //----------------------------------------------------------------------
        /**
         * Get the subject mapping. since there will be many subjects with same
         * code we should get all of them.
         */
        searchedSubject = Mono.orm()
                .newSearch(Database.connect().subject())
                .eq(DB.subject().code, subjectCode)
                .active()
                .all();

        //----------------------------------------------------------------------
        if (Objects.isNull(searchedSubject)) {
            System.out.println("Subject not found");
            return false;
        }
        //----------------------------------------------------------------------
        /**
         * If subject is existing get all load_groups with this subject ID.
         */
        loadGroups = new ArrayList<>();
        for (SubjectMapping subjectMap : searchedSubject) {
            int subject_id = subjectMap.getId();
            ArrayList<LoadGroupMapping> lg = Mono.orm()
                    .newSearch(Database.connect().load_group())
                    .eq("SUBJECT_id", subject_id)
                    .active()
                    .all();

            if (lg == null) {
                System.out.println("Subject ID: " + subject_id);
                System.out.println("No section found");
                continue;
            }

            // if not empty add to collection
            loadGroups.addAll(lg);
        }
        //----------------------------------------------------------------------
        if (loadGroups.isEmpty()) {
            // no sections for that subject.
            return false;
        }
        //----------------------------------------------------------------------

        /**
         * If load groups where found get its section and population.
         */
        loadGroupPopulation = new ArrayList<>();
        loadSections = new ArrayList<>();
        loadGroups.forEach(load_group -> {
            // search section
            LoadSectionMapping section = Mono.orm()
                    .newSearch(Database.connect().load_section())
                    .eq("id", load_group.getLOADSEC_id())
                    .active()
                    .first();
            loadSections.add(section);

            // get population
            int load_group_ids = load_group.getId(); // for load subject
            String populationString = SearchBySection.getLoadGroupPopulation(load_group_ids);
            loadGroupPopulation.add(populationString);
        });
        log("Success Search by Subject");
        return true;
    }

    private SubjectMapping getSubjectByID(Integer id) {
        for (SubjectMapping subjectMapping : this.searchedSubject) {
            if (subjectMapping.getId().equals(id)) {
                return subjectMapping;
            }
        }
        return null;
    }

    //--------------------------------------------------------------------------
    // stores the curriculum locally to avoid unecessary redundant fetches
    private HashMap<Integer, CurriculumMapping> curDataStore;

    // fetch only from the database if not existing from current collection
    private CurriculumMapping getStoredCurriculum(Integer id) {
        CurriculumMapping cur = curDataStore.getOrDefault(id, null);
        if (cur == null) {
            cur = Database.connect().curriculum().getPrimary(id);
            curDataStore.put(id, cur);
            System.out.println("@SearchBySubject.getStoredCurriculum: FETCHED");
            return cur;
        } else {
            System.out.println("@SearchBySubject.getStoredCurriculum: RECYCLED");
            return cur;
        }
    }
    //--------------------------------------------------------------------------

    @Override
    protected void after() {
        if (!this.checkResults()) {
            return;
        }
        curDataStore = new HashMap<>();

        for (int x = 0; x < this.loadGroups.size(); x++) {
            // join tables
            SubjectMapping subject = getSubjectByID(loadGroups.get(x).getSUBJECT_id());

            SectionSearchView ssv = new SectionSearchView();
            String code = subject.getCode();
            ssv.labelCode.setText(code);
            String title = subject.getDescriptive_title();
            ssv.labelTitle.setText(title);
//            ssv.labelMax.setText("30");
//            ssv.labelExpected.setText("~ 20");
            String current = this.loadGroupPopulation.get(x);
            ssv.labelCurrent.setText(current);

            //
            AcademicProgramMapping acadProg = Mono.orm()
                    .newSearch(Database.connect().academic_program())
                    .eq("id", loadSections.get(x).getACADPROG_id())
                    .execute()
                    .first();

            /**
             * code added 8/28/17 by: joemar
             */
            String course_code = "";
            if (acadProg != null) {
                course_code = acadProg.getCode();
            }
            String sectionName = "";
            if (loadSections.get(x).getYear_level() != null
                    && loadSections.get(x).getYear_level() != 0) {
                sectionName = loadSections.get(x).getYear_level() + ""
                        + loadSections.get(x).getSection_name()
                        + " - G"
                        + loadSections.get(x).get_group();
            } else {
                sectionName = loadSections.get(x).getSection_name();
            }

            String sectionWithFormat = course_code + " " + sectionName;

            ssv.labelSection.setText(sectionWithFormat);

            ssv.loadGroupID = this.loadGroups.get(x).getId();
            ssv.subjectID = subject.getId();
            ssv.sectionID = this.loadSections.get(x).getId();

            //------------------------------------------------------------------
            ssv.lbl_curname.setText(getStoredCurriculum(this.loadSections.get(x).getCURRICULUM_id()).getName());
            //------------------------------------------------------------------

            searchResults.add(ssv);
        }
    }

    private boolean checkResults() {

        /**
         * Checks if there is no null values
         */
        if (this.loadGroups == null || this.loadSections == null || this.loadGroupPopulation == null) {
            System.out.println("@something is null");
            return false;
        }
        /**
         * Check if the three have same result count. these represents (THREE)
         * tables from the database as a table join so their sizes should be the
         * same, before executing a join method using a for loop you can access
         * rows of this three tables at the same time.
         */
        if (this.loadGroups.size() == this.loadSections.size()) {
            if (this.loadSections.size() == this.loadGroupPopulation.size()) {
                return true;
            }
        }
        return false; // size mismatch
    }

}

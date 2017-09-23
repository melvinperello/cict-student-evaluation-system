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
import java.util.List;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import app.lazy.models.AcademicProgramMapping;
import app.lazy.models.DB;
import app.lazy.models.LoadGroupMapping;
import app.lazy.models.LoadSectionMapping;
import app.lazy.models.LoadSubjectMapping;
import app.lazy.models.SubjectMapping;

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
    private SubjectMapping searchedSubject;
    private ArrayList<LoadGroupMapping> loadGroups;
    private ArrayList<LoadSectionMapping> loadSections;
    private ArrayList<Integer> loadGroupPopulation;

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

        /**
         * Get the subject mapping
         */
        searchedSubject = Mono.orm()
                .newSearch(Database.connect().subject())
                .eq(DB.subject().code, subjectCode)
                .active()
                .first();

        if (Objects.isNull(searchedSubject)) {
            return false;
        }

        /**
         * If subject is existing get all load_groups with this subject ID.
         */
        int subject_id = searchedSubject.getId();
        loadGroups = Mono.orm()
                .newSearch(Database.connect().load_group())
                .eq("SUBJECT_id", subject_id)
                .active()
                .all();

        if (Objects.isNull(loadGroups)) {
            return false;
        }

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
            int population = 0;
            List res = Mono.orm()
                    .newSearch(Database.connect().load_subject())
                    .eq("LOADGRP_id", load_group_ids)
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
            // join tables

            SectionSearchView ssv = new SectionSearchView();
            String code = this.searchedSubject.getCode();
            ssv.labelCode.setText(code);
            String title = this.searchedSubject.getDescriptive_title();
            ssv.labelTitle.setText(title);
            ssv.labelMax.setText("30");
            ssv.labelExpected.setText("~ 20");
            String current = this.loadGroupPopulation.get(x).toString();
            ssv.labelCurrent.setText(current);
            //

            AcademicProgramMapping acadProg = Mono.orm()
                    .newSearch(Database.connect().academic_program())
                    .eq("id", loadSections.get(x).getACADPROG_id())
                    .execute()
                    .first();
            
            /**
             * code added 8/28/17
             * by: joemar
             */
            String course_code = "";
            if (acadProg != null) {
                course_code = acadProg.getCode();
            }
            String sectionName = "";
            if(loadSections.get(x).getYear_level() != null
                    && loadSections.get(x).getYear_level() != 0) {
                sectionName = loadSections.get(x).getYear_level()+ " "
                    + loadSections.get(x).getSection_name()
                    + " - G"
                    + loadSections.get(x).get_group();
            } else {
                sectionName = loadSections.get(x).getSection_name();
            }

            String sectionWithFormat = course_code + " " + sectionName;

            ssv.labelSection.setText(sectionWithFormat);

            ssv.loadGroupID = this.loadGroups.get(x).getId();
            ssv.subjectID = this.searchedSubject.getId();
            ssv.sectionID = this.loadSections.get(x).getId();

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
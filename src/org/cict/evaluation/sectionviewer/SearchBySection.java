/* 
 * Copyright (C) Jhon Melvin N. Perello - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * PROPRIETARY and CONFIDENTIAL
 *
 * Written by Jhon Melvin N. Perello <jhmvinperello@gmail.com>, 2017.
 *
 */
package org.cict.evaluation.sectionviewer;

import com.jhmvin.fx.async.Transaction;
import app.lazy.models.Database;
import org.cict.evaluation.views.SectionSearchView;
import com.jhmvin.Mono;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import app.lazy.models.AcademicProgramMapping;
import app.lazy.models.LoadGroupMapping;
import app.lazy.models.LoadSectionMapping;
import app.lazy.models.SubjectMapping;
import org.cict.evaluation.evaluator.Evaluator;

/**
 *
 * @author Jhon Melvin
 */
public class SearchBySection extends Transaction {

    /**
     * Why these variables public ? We don't care about them being modified or
     * being read anyway. this are the required values to start this
     * transaction. what wrong can it bring being modified or read ?
     */
    public String course_code;
    public String year;
    public String section;
    public String group;

    /**
     * So why these variables are private? we are declaring variables private
     * because we wanted to restrict access to it. with this example this
     * transaction. produces results that will reflect in the variables below.
     * these results are not to be modified outside this transaction.
     */
    private AcademicProgramMapping academicProgram;
    private LoadSectionMapping loadSection;
    private ArrayList<LoadGroupMapping> loadGroups;
    private ArrayList<SubjectMapping> subjectInfo;
    private ArrayList<Integer> loadGroupPopulation;

    /**
     * And why this variable has the only one with a getter ? because it is
     * needed to be passed on others classes that needs this transaction's
     * results. but again it is only for reading and other classes cannot modify
     * this result, that's why it is private.
     */
    private final ObservableList<SectionSearchView> searchResults = FXCollections.observableArrayList();

    /**
     *
     * @return a list of SectionSearchView
     */
    public ObservableList<SectionSearchView> getSearchResults() {
        return searchResults;
    }

    /**
     * <strong>transaction()</strong> is automatically called outside using
     * <strong>transact()</strong> and needs to have a return value.
     * <ul>
     * <li>Returns "true" - if completed</li>
     * <li>Returns "false" - if not</li>
     *
     * </ul>
     *
     * @return
     */
    @Override
    protected boolean transaction() {
        // start task
        academicProgram = Mono.orm()
                .newSearch(Database.connect().academic_program())
                .eq("code", course_code)
                .execute()
                .first();

        if (Objects.isNull(academicProgram)) {
            // cancel task if no results found
            return false;
        }
        // set values for section search
        int program_id = academicProgram.getId();
        int acad_term_id = Evaluator.instance().getCurrentAcademicTerm().getId(); // first semester of 2017-2018

        try {
            loadSection = Mono.orm()
                    .newSearch(Database.connect().load_section())
                    .eq("ACADTERM_id", acad_term_id)
                    .eq("ACADPROG_id", program_id)
                    .eq("section_name", section)
                    .eq("year_level", Integer.valueOf(year))
                    .eq("_group", Integer.valueOf(group))
                    .active()
                    .first();
        } catch (NumberFormatException e) {
            return false;
        }
        // search section

        // set values for section search
        if (Objects.isNull(loadSection)) {
            // cancel task if no results found
            return false;
        }

        // prepare for load group search
        int loadsec_id = loadSection.getId();

        // search load_group
        loadGroups = Mono.orm()
                .newSearch(Database.connect().load_group())
                .eq("LOADSEC_id", loadsec_id)
                .active()
                .all();

        // if no subjects are assigned to that section
        if (Objects.isNull(loadGroups)) {
            System.out.println(loadsec_id);
            return false;
        }

        subjectInfo = new ArrayList<>();
        loadGroupPopulation = new ArrayList<>();

        loadGroups.forEach(rows -> {
            // prepare subject search
            SubjectMapping subject = (SubjectMapping) Database.connect().subject().getPrimary(rows.getSUBJECT_id());
            /*Mono.orm()
                    .newSearch(Database.connect().subject())
                    .eq("id", rows.getSUBJECT_id())
                    .eq("ACADPROG_id", program_id)
                    .execute()
                    .first();*/
            subjectInfo.add(subject);

            // population count
            int load_group_ids = rows.getId(); // for load subject
            int population = 0;
            List res = Mono.orm()
                    .newSearch(Database.connect().load_subject())
                    .eq("LOADGRP_id", load_group_ids)
                    .execute();
            if (Objects.isNull(res)) {
                population = 0;
            } else {
                population = res.size();
            }

            loadGroupPopulation.add(population);

        });

        // if the transaction is completed
        return true;
    }

    /**
     * this method is only called if transaction() returns true, which means it
     * has completed normally. this method will not be called if transaction
     * returns false.
     */
    @Override
    protected void after() {
        if (!this.checkResults()) {
            return;
        }
        // since the three should have the same size
        for (int x = 0; x < this.subjectInfo.size(); x++) {
            // join tables
            if ((Objects.equals(this.subjectInfo.get(x).getId(), this.loadGroups.get(x).getSUBJECT_id()))) {

                SectionSearchView ssv = new SectionSearchView();
                String code = this.subjectInfo.get(x).getCode();
                ssv.labelCode.setText(code);
                String title = this.subjectInfo.get(x).getDescriptive_title();
                ssv.labelTitle.setText(title);
//                ssv.labelMax.setText("30");
//                ssv.labelExpected.setText("~ 20");
                String current = this.loadGroupPopulation.get(x).toString();
                ssv.labelCurrent.setText(current);

                String sectionWithFormat = this.academicProgram.getCode()
                        + " "
                        + loadSection.getYear_level()
                        + " "
                        + loadSection.getSection_name()
                        + " - G"
                        + loadSection.get_group();//

                ssv.labelSection.setText(sectionWithFormat);
                ssv.loadGroupID = this.loadGroups.get(x).getId();
                ssv.subjectID = this.subjectInfo.get(x).getId();
                ssv.sectionID = this.loadSection.getId();

                searchResults.add(ssv);
            }
        }
    }

    private boolean checkResults() {

        /**
         * Checks if there is no null values
         */
        if (this.loadGroups == null || this.subjectInfo == null || this.loadGroupPopulation == null) {
            System.out.println("@something is null");
            return false;
        }
        /**
         * Check if the three have same result count. these represents (THREE)
         * tables from the database as a table join so their sizes should be the
         * same, before executing a join method using a for loop you can access
         * rows of this three tables at the same time.
         */
        if (this.loadGroups.size() == this.subjectInfo.size()) {
            if (this.subjectInfo.size() == this.loadGroupPopulation.size()) {
                return true;
            }
        }
        return false; // size mismatch
    }

}

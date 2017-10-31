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
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import app.lazy.models.AcademicProgramMapping;
import app.lazy.models.CurriculumMapping;
import app.lazy.models.DB;
import app.lazy.models.LoadGroupMapping;
import app.lazy.models.LoadSectionMapping;
import app.lazy.models.SubjectMapping;
import com.jhmvin.orm.Searcher;
import org.cict.evaluation.evaluator.Evaluator;
import org.hibernate.criterion.Order;

/**
 *
 * @author Jhon Melvin
 */
public class SearchBySection extends Transaction {

    private final static void log(Object message) {
        System.err.println("@SearchBySection: " + String.valueOf(message));
    }

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
    private ArrayList<LoadSectionMapping> loadSection;
    private ArrayList<LoadGroupMapping> loadGroups;
    /*
    will not be used because of subject information holder
    private ArrayList<SubjectMapping> subjectInfo;
    private ArrayList<Integer> loadGroupPopulation;
     */

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
    //--------------------------------------------------------------------------

    private ArrayList<SectionCurriculumPair> sectionPairList;
    private ArrayList<SectionInfo> sectionInfoList;

    private class SectionCurriculumPair {

        private LoadSectionMapping section;
        private CurriculumMapping ownerCurriculum;

    }

    /**
     * To avoid redundant connection to the database to fetch section and
     * curriculum. this code segment saves it locally.
     *
     * @param sectionID
     * @return
     */
    private SectionCurriculumPair getSectionPair(Integer sectionID) {
        for (SectionCurriculumPair sectionCurriculumPair : sectionPairList) {
            boolean same = sectionID.equals(sectionCurriculumPair.section.getId());
            if (same) {
                return sectionCurriculumPair;
            }
        }
        return null;
    }

    //--------------------------------------------------------------------------
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
        //----------------------------------------------------------------------
        sectionPairList = new ArrayList<>();
        sectionInfoList = new ArrayList<>();
        //----------------------------------------------------------------------
        log("Searching Academic Program Code");
        academicProgram = Mono.orm()
                .newSearch(Database.connect().academic_program())
                .eq(DB.academic_program().code, course_code)
                .execute(Order.desc(DB.academic_program().id))
                .first();
        //----------------------------------------------------------------------
        if (Objects.isNull(academicProgram)) {
            // cancel task if no results found
            log("No Program Code");
            return false;
        }
        //----------------------------------------------------------------------
        // set values for section search
        log("Preparing Values");
        int program_id = academicProgram.getId();
        int acad_term_id = Evaluator.instance().getCurrentAcademicTerm().getId();
        //----------------------------------------------------------------------
        try {
            // LoadSection was revised to an ArrayList.
            // @10312017
            loadSection = Mono.orm()
                    .newSearch(Database.connect().load_section())
                    .eq(DB.load_section().ACADTERM_id, acad_term_id)
                    .eq(DB.load_section().ACADPROG_id, program_id)
                    .eq(DB.load_section().section_name, section)
                    .eq(DB.load_section().year_level, Integer.valueOf(year))
                    .eq(DB.load_section()._group, Integer.valueOf(group))
                    .active(Order.desc(DB.load_section().id))
                    .all();
        } catch (NumberFormatException e) {
            log("Number Format exception in loadSection");
            return false;
        }
        //----------------------------------------------------------------------
        // set values for section search
        if (Objects.isNull(loadSection)) {
            // cancel task if no results found
            log("No Section Found");
            return false;
        }
        //----------------------------------------------------------------------
        // Multiple sections are associated in a single prefix since an
        // academic program can contain multiple curriculums.
        //----------------------------------------------------------------------
        // initialize load groups
        loadGroups = new ArrayList<>();
        for (LoadSectionMapping lsMap : loadSection) {
            int loadsec_id = lsMap.getId();
            // search load groups of each sections
            ArrayList<LoadGroupMapping> searchLoadGroups = Mono.orm()
                    .newSearch(Database.connect().load_group())
                    .eq(DB.load_group().LOADSEC_id, loadsec_id)
                    .active()
                    .all();
            //------------------------------------------------------------------
            // if not null add it to the collection
            if (searchLoadGroups != null) {
                this.loadGroups.addAll(searchLoadGroups);
                //--------------------------------------------------------------
                try {
                    SectionCurriculumPair secCurPair = new SectionCurriculumPair();
                    secCurPair.section = lsMap;
                    secCurPair.ownerCurriculum = Database.connect().curriculum()
                            .getPrimary(lsMap.getCURRICULUM_id());
                    this.sectionPairList.add(secCurPair);
                } catch (Exception e) {
                    log("Error in creating secCurPair");
                    return false;
                }
                //--------------------------------------------------------------
            }
        }
        //----------------------------------------------------------------------
        // if no subjects are assigned to that section
        // means its load group is null
        if (Objects.isNull(loadGroups)) {
            log("No Loadgroup");
            return false;
        }
        // has no matching values
        if (loadGroups.isEmpty()) {
            log("Empty Loadgroup");
            return false;
        }
        //----------------------------------------------------------------------
        // if values we're found.
//        subjectInfo = new ArrayList<>();
//        loadGroupPopulation = new ArrayList<>();

        // iterate all over the load groups
        for (LoadGroupMapping loadGroup : loadGroups) {
            //------------------------------------------------------------------
            // create holder
            SectionInfo info = new SectionInfo();
            info.curriculum = getSectionPair(loadGroup.getLOADSEC_id()).ownerCurriculum;
            info.loadSection = getSectionPair(loadGroup.getLOADSEC_id()).section;
            info.loadGroup = loadGroup;
            info.subjectInfo = Database.connect().subject().getPrimary(loadGroup.getSUBJECT_id());

            info.population = SearchBySection.getLoadGroupPopulation(loadGroup.getId());
            sectionInfoList.add(info);
        }

        //----------------------------------------------------------------------
//        loadGroups.forEach(rows -> {
//            // prepare subject search
//            SubjectMapping subject = (SubjectMapping) Database.connect().subject().getPrimary(rows.getSUBJECT_id());
//            /*Mono.orm()
//                    .newSearch(Database.connect().subject())
//                    .eq("id", rows.getSUBJECT_id())
//                    .eq("ACADPROG_id", program_id)
//                    .execute()
//                    .first();*/
//            subjectInfo.add(subject);
//
//            // population count
//            int load_group_ids = rows.getId(); // for load subject
//            int population = 0;
//            List res = Mono.orm()
//                    .newSearch(Database.connect().load_subject())
//                    .eq("LOADGRP_id", load_group_ids)
//                    .execute();
//            if (Objects.isNull(res)) {
//                population = 0;
//            } else {
//                population = res.size();
//            }
//
//            loadGroupPopulation.add(population);
//
//        });
        //----------------------------------------------------------------------
        // if the transaction is completed
        return true;
    }

    /**
     * Returns the population of a loadGroup
     *
     * @param loadGroupID
     * @return
     */
    public final static String getLoadGroupPopulation(Integer loadGroupID) {
        Searcher search = Mono.orm()
                .newSearch(Database.connect().load_subject())
                .eq(DB.load_subject().LOADGRP_id, loadGroupID)
                .eq(DB.load_subject().active, 1);
        return Mono.orm().projection(search).count(DB.load_group().id);
    }

    /**
     * this method is only called if transaction() returns true, which means it
     * has completed normally. this method will not be called if transaction
     * returns false.
     */
    @Override
    protected void after() {
        for (SectionInfo si : sectionInfoList) {
            //------------------------------------------------------------------
            SubjectMapping subjectInfo = si.subjectInfo;
            LoadSectionMapping ls = si.loadSection;
            LoadGroupMapping lGroup = si.loadGroup;
            //------------------------------------------------------------------
            SectionSearchView ssv = new SectionSearchView();
            String code = subjectInfo.getCode();
            ssv.labelCode.setText(code);
            String title = subjectInfo.getDescriptive_title();
            ssv.labelTitle.setText(title);
//                ssv.labelMax.setText("30");
//                ssv.labelExpected.setText("~ 20");
            String current = si.population;
            ssv.labelCurrent.setText(current);
            String sectionWithFormat = "";
            try {
                sectionWithFormat = this.academicProgram.getCode()
                        + " "
                        + ls.getYear_level()
                        + " "
                        + ls.getSection_name()
                        + " - G"
                        + ls.get_group();
            } catch (Exception e) {
                sectionWithFormat = ls.getSection_name();
            }

            ssv.labelSection.setText(sectionWithFormat);
            ssv.loadGroupID = lGroup.getId();
            ssv.subjectID = subjectInfo.getId();
            ssv.sectionID = ls.getId();
            ssv.lbl_curname.setText(si.curriculum.getName());

            searchResults.add(ssv);
        }

//        if (!this.checkResults()) {
//            return;
//        }
//        // since the three should have the same size
//        for (int x = 0; x < this.subjectInfo.size(); x++) {
//            // join tables
//            if ((Objects.equals(this.subjectInfo.get(x).getId(), this.loadGroups.get(x).getSUBJECT_id()))) {
//
//                SectionSearchView ssv = new SectionSearchView();
//                String code = this.subjectInfo.get(x).getCode();
//                ssv.labelCode.setText(code);
//                String title = this.subjectInfo.get(x).getDescriptive_title();
//                ssv.labelTitle.setText(title);
////                ssv.labelMax.setText("30");
////                ssv.labelExpected.setText("~ 20");
//                String current = this.loadGroupPopulation.get(x).toString();
//                ssv.labelCurrent.setText(current);
//
//                String sectionWithFormat = this.academicProgram.getCode()
//                        + " "
//                        + loadSection.getYear_level()
//                        + " "
//                        + loadSection.getSection_name()
//                        + " - G"
//                        + loadSection.get_group();//
//
//                ssv.labelSection.setText(sectionWithFormat);
//                ssv.loadGroupID = this.loadGroups.get(x).getId();
//                ssv.subjectID = this.subjectInfo.get(x).getId();
//                ssv.sectionID = this.loadSection.getId();
//
//                searchResults.add(ssv);
//            }
//        }
    }

    private boolean checkResults() {

//        /**
//         * Checks if there is no null values
//         */
//        if (this.loadGroups == null || this.subjectInfo == null || this.loadGroupPopulation == null) {
//            System.out.println("@something is null");
//            return false;
//        }
//        /**
//         * Check if the three have same result count. these represents (THREE)
//         * tables from the database as a table join so their sizes should be the
//         * same, before executing a join method using a for loop you can access
//         * rows of this three tables at the same time.
//         */
//        if (this.loadGroups.size() == this.subjectInfo.size()) {
//            if (this.subjectInfo.size() == this.loadGroupPopulation.size()) {
//                return true;
//            }
//        }
//        return false; // size mismatch
        return false;
    }

    //--------------------------------------------------------------------------
    /**
     * Section Information Holder.
     */
    private class SectionInfo {

        private LoadSectionMapping loadSection;
        private LoadGroupMapping loadGroup;
        private SubjectMapping subjectInfo;
        private String population;
        private CurriculumMapping curriculum;

    }

}

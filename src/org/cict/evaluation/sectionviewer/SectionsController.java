/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cict.evaluation.sectionviewer;

import artifacts.MonoString;
import org.cict.evaluation.evaluator.Evaluator;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.display.ControllerFX;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.cict.evaluation.views.SubjectSuggestionView;

/**
 * FXML Controller class
 *
 * @author Jhon Melvin
 */
public class SectionsController implements ControllerFX {

    @FXML
    private AnchorPane anchor_main;

    @FXML
    private TextField txtSearch;

    @FXML
    private JFXButton btnSearch;

    @FXML
    private AnchorPane anchor_result;

    @FXML
    private HBox hbox_greet;

    @FXML
    private HBox hbox_no_result;

    @FXML
    private HBox hbox_search;

    @FXML
    private AnchorPane anchor_view;

    @FXML
    private ScrollPane scroll_view;

    @Override
    public void onInitialization() {
        setView("greet");

        vbox_subjects.prefWidthProperty().bind(scroll_view.widthProperty());
        scroll_view.setContent(vbox_subjects);
    }
    VBox vbox_subjects = new VBox(4);

    @Override
    public void onEventHandling() {
        this.btnSearch.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {

            this.search();

        });

        this.anchor_main.addEventHandler(KeyEvent.KEY_RELEASED, key_event -> {
            if (key_event.getCode().equals(KeyCode.ENTER)) {
                if (!this.btnSearch.isDisabled()) {
                    this.search();
                }
            }
        });
    }

    /**
     * Call back function on start.
     *
     * @param searchFor
     */
    public void searchCallBack(String searchFor) {
        this.txtSearch.setText(searchFor);
        this.search();
    }

    /**
     * Just a shortcut in display stacked view.
     *
     * @param view
     */
    public void setView(String view) {
        anchor_view.setVisible(false);
        anchor_result.setVisible(false);
        hbox_search.setVisible(false);
        hbox_greet.setVisible(false);
        hbox_no_result.setVisible(false);

        switch (view.toLowerCase().trim()) {
            case "result":
                anchor_view.setVisible(true);
                break;
            case "search":
                anchor_result.setVisible(true);
                hbox_search.setVisible(true);
                break;
            case "greet":
                anchor_result.setVisible(true);
                hbox_greet.setVisible(true);
                break;
            case "no_result":
                anchor_result.setVisible(true);
                hbox_no_result.setVisible(true);
                break;
        }
    }

    private void search() {

        String keyword = this.txtSearch.getText().trim().toUpperCase();
        HashMap<String, String> course_hint = new HashMap<>();

        /**
         * Checks for suggestions
         */
        boolean isLookingForSuggestions = false;
        String suggestionForStudent = "";
        ArrayList<String> suggestHints = new ArrayList<>();
        suggestHints.add("suggested");
        suggestHints.add("suggested for");
        suggestHints.add("suggestions for");
        suggestHints.add("suggestions");
        suggestHints.add("subjects for");
        suggestHints.add("what can i give to");
        suggestHints.add("what subjects can i give to");
        suggestHints.add("what subjects i can give to");
        suggestHints.add("list of subjects for");
        suggestHints.add("help me find subjects for");
        suggestHints.add("pwedeng kunin ni");
        suggestHints.add("maaaring kunin ni");
        suggestHints.add("listahan ng maaring kunin ni");
        for (String suggestHint : suggestHints) {
            if (MonoString.startsWithIgnoreCase(keyword, suggestHint)) {
                isLookingForSuggestions = true;
                suggestionForStudent = MonoString.removeStartIgnoreCase(keyword, suggestHint);
                break;
            }
        }

        if (isLookingForSuggestions) {
            searchSuggestions(suggestionForStudent.trim());
            return;
        }

        /**
         * Sections
         */
        // it
        course_hint.put("bs it", "BSIT");
        course_hint.put("bsit", "BSIT");
        // comtech
        course_hint.put("bit ct", "BITCT");
        course_hint.put("bit comtech", "BITCT");
        course_hint.put("bitct", "BITCT");
        // act
        course_hint.put("bs act", "BSACT");
        course_hint.put("bsact", "BSACT");

        /**
         * Checks if its looking for a section
         */
        boolean isCourse = false;
        String whatCourse = "";
        String whatSection = "";
        for (String string : course_hint.keySet()) {
            if (MonoString.startsWithIgnoreCase(keyword, string)) {
                isCourse = true;
                whatCourse = course_hint.get(string);
                // get the remaining string as section keyword
                whatSection = MonoString.removeStartIgnoreCase(keyword, string).trim();
                break;
            }
        }

        /**
         * If the keyword determines a possible section search
         */
        if (isCourse) {
            String[] sec_group = whatSection.split("-");
            ArrayList<String> filtered_sec_group = new ArrayList<>();
            for (String string : sec_group) {
                if (!string.isEmpty()) {
                    filtered_sec_group.add(string);
                }
            }
            // prepare the additional section information
            String year_level;
            String section_name;
            String group_number;
            // if correct search parameters
            if (filtered_sec_group.size() == 2) {
                String year_sec = filtered_sec_group.get(0);
                char year = year_sec.charAt(0);
                String section = year_sec.substring(1, (year_sec.length()));
                char group = filtered_sec_group.get(1).charAt(1);

                // submit values
                year_level = Character.toString(year);
                section_name = section;
                group_number = Character.toString(group);

                // call thread
                searchBySectionTask(whatCourse, year_level, section_name, group_number);

            } else {
                // if invalid search parameters
                // no results none execition
                setView("no_result");
            }

        } else {
            /**
             * by default if it's not a section search it will proceed to search
             * using subject code.
             */
            // let's assume the keyword is a subject code and remove spaces in between
            String possibleSubjectCode = MonoString.removeExtraSpace(keyword);
            this.searchBySubjectCodeTask(possibleSubjectCode);
        }
    }

    //--------------------------------------------------------------------------
    private void searchSuggestions(String studentNumber) {
        SearchSuggestions suggest = Evaluator
                .instance()
                .createSuggestionSearcher();

        suggest.studentNumber = studentNumber;

        suggest.setOnStart(start -> {
            this.btnSearch.setDisable(true);
            setView("search");
            this.btnSearch.getScene().setCursor(Cursor.WAIT);
            this.vbox_subjects
                    .getChildren()
                    .clear();
        });

        suggest.setOnSuccess((WorkerStateEvent success) -> {
            setView("result");
            this.btnSearch.setDisable(false);
            this.btnSearch.getScene().setCursor(Cursor.DEFAULT);
            for (SubjectSuggestionView suggestion : suggest.getSuggestions()) {
                suggestion.section.addEventHandler(MouseEvent.MOUSE_ENTERED, hover -> {
                    this.btnSearch.getScene().setCursor(Cursor.HAND);
                });
                suggestion.section.addEventHandler(MouseEvent.MOUSE_EXITED, exit -> {
                    this.btnSearch.getScene().setCursor(Cursor.DEFAULT);
                });

                suggestion.section.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent click) -> {
                    if (click.isControlDown()) {

                        SectionsController sectionController = new SectionsController();

                        Mono.fx().create()
                                .setPackageName("org.cict.evaluation.sectionviewer")
                                .setFxmlDocument("section_viewer")
                                .makeFX()
                                .setController(sectionController)
                                .makeScene()
                                .makeStage()
                                .stageShow();
                        sectionController.searchCallBack(suggestion.code.getText());

                    } else {
                        txtSearch.setText(suggestion.code.getText());
                        this.search();
                    }

                });

                this.vbox_subjects
                        .getChildren()
                        .add(suggestion);
            }

        });

        suggest.setOnCancel(cancel -> {
            this.btnSearch.setDisable(false);
            this.btnSearch.getScene().setCursor(Cursor.DEFAULT);
            setView("no_result");
        });

        suggest.setRestTime(500);
        suggest.transact();

    }

    //--------------------------------------------------------------------------
    /**
     * Worker Thread for Section Search
     *
     * @param course_code
     * @param year
     * @param section
     * @param group
     */
    private void searchBySectionTask(
            String course_code,
            String year,
            String section,
            String group) {

        SearchBySection searchSectionTransaction = Evaluator
                .instance()
                .createSectionSearcher();
        // prepare required values
        searchSectionTransaction.course_code = course_code;
        searchSectionTransaction.year = year;
        searchSectionTransaction.section = section;
        searchSectionTransaction.group = group;

        searchSectionTransaction.setOnStart(event -> {
            this.btnSearch.setDisable(true);
            setView("search");
            this.btnSearch.getScene().setCursor(Cursor.WAIT);
            this.vbox_subjects
                    .getChildren()
                    .clear();
        });

        // set events
        searchSectionTransaction.setOnSuccess(event -> {
            // we need the results if it's successfull
            // search cache variables
            setView("result");
            this.btnSearch.setDisable(false);
            this.btnSearch.getScene().setCursor(Cursor.DEFAULT);
            this.vbox_subjects
                    .getChildren()
                    .addAll(searchSectionTransaction.getSearchResults());
            //
        });
        searchSectionTransaction.setOnCancel(event -> {
            this.btnSearch.setDisable(false);
            this.btnSearch.getScene().setCursor(Cursor.DEFAULT);
            setView("no_result");
        });

        // set pre execution wait time
        searchSectionTransaction.setRestTime(500);
        // begin
        searchSectionTransaction.transact();

    }

    /**
     * Search section using subject code Subject Worker Thread
     *
     * @param subject_code
     */
    private void searchBySubjectCodeTask(String subject_code) {
        SearchBySubject bySubjectTask = Evaluator
                .instance()
                .createSectionSearcherBySubject();

        // prepare required values
        bySubjectTask.subjectCode = subject_code;

        /**
         * task is on schedule and ready to start
         */
        bySubjectTask.setOnStart(event -> {
            setView("search");
            this.btnSearch.setDisable(true);
            this.btnSearch.getScene().setCursor(Cursor.WAIT);
            this.vbox_subjects
                    .getChildren()
                    .clear();
        });

        bySubjectTask.setOnSuccess(event -> {
            this.btnSearch.setDisable(false);
            setView("result");
            this.btnSearch.getScene().setCursor(Cursor.DEFAULT);
            this.vbox_subjects
                    .getChildren()
                    .addAll(bySubjectTask.getSearchResults());

        });

        bySubjectTask.setOnCancel(event -> {
            this.btnSearch.setDisable(false);
            this.btnSearch.getScene().setCursor(Cursor.DEFAULT);
            setView("no_result");
        });

        /**
         * Delay before execution
         */
        bySubjectTask.setRestTime(500);
        bySubjectTask.transact();
    }

}

/* 
 * Copyright (C) Jhon Melvin N. Perello - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * PROPRIETARY and CONFIDENTIAL
 *
 * Written by Jhon Melvin N. Perello <jhmvinperello@gmail.com>, 2017.
 *
 */
package org.cict.evaluation.evaluator;

import app.lazy.models.AcademicTermMapping;
import app.lazy.models.Database;
import app.lazy.models.StudentMapping;
import app.lazy.models.SubjectMapping;
import com.jhmvin.Mono;
import org.cict.authentication.authenticator.SystemProperties;
import org.cict.evaluation.sectionviewer.SearchBySection;
import org.cict.evaluation.sectionviewer.SearchBySubject;
import com.jhmvin.fx.async.Process;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.cict.SubjectClassification;
import org.cict.evaluation.sectionviewer.SearchSuggestions;
import org.cict.evaluation.views.SubjectView;
import org.controlsfx.control.Notifications;

/**
 * @author Jhon Melvin
 */
public class Evaluator implements Process {

    @Override
    public void restart() {

    }

    // <Singleton>
    private static Evaluator EVALUATION_INSTANCE;

    public static Evaluator instance() {
        if (EVALUATION_INSTANCE == null) {
            EVALUATION_INSTANCE = new Evaluator();
        }
        return EVALUATION_INSTANCE;
    }

    private Evaluator() {
        //
//        currentAcademicTerm = Mono.orm()
//                .newSearch(Database.connect().academic_term())
//                .eq("current", 1)
//                .active()
//                .first();
        currentAcademicTerm = SystemProperties.instance().getCurrentAcademicTerm();
    }

    private AcademicTermMapping currentAcademicTerm;

    public AcademicTermMapping getCurrentAcademicTerm() {
        return currentAcademicTerm;
    }

    public SearchBySection createSectionSearcher() {
        return new SearchBySection();
    }

    public SearchBySubject createSectionSearcherBySubject() {
        return new SearchBySubject();
    }

    public SearchSuggestions createSuggestionSearcher() {
        return new SearchSuggestions();
    }

    public ValidateAddedSubject createValidateAddedSubject() {
        return new ValidateAddedSubject();
    }

    public SaveEvaluation createSaveEvaluation() {
        return new SaveEvaluation();
    }

    public PrintAdvising printAdvising() {
        return new PrintAdvising();
    }

    public CheckGrade createGradeChecker() {
        return new CheckGrade();
    }

    public EncodeGrade createGradeEncoder() {
        return new EncodeGrade();
    }

    /**
     * Define Process Persistent Values
     */
    // cursor draggables <SECTION VIEWER> add to <EVALUATION>
    public Integer pressedLoadGroupID;
    public Integer pressedSubjectID;
    public Integer pressedSectionID;
    public boolean sectionViewReleased;

    //
    //-------------------------------------------------------------------------
    /**
     * The event when a subject was dropped in the evaluation subject list
     *
     * @param currentStudent
     * @param max_units max allowed units
     * @param unit_count current count
     * @param vbox_subjects the container of the subjects
     * @param anchor_right
     */
    public static void mouseDropSubject(StudentMapping currentStudent, double max_units, double unit_count, VBox vbox_subjects, AnchorPane anchor_right) {
        System.out.println("@EvaluateController: Mouse Entered Drop Zone.");
        /**
         * check if the event is from the class viewer
         */
        if (Evaluator.instance().sectionViewReleased) {

            System.out.println("@EvaluateController: Object Recieved.");
            Evaluator.instance().sectionViewReleased = false;

            /**
             * Validation of subjects server side. this won't run unless
             * transact is called
             */
            ValidateAddedSubject validationTask = Evaluator
                    .instance()
                    .createValidateAddedSubject();
            validationTask.studentCictID = currentStudent.getCict_id();
            validationTask.loadGroupID = Evaluator.instance().pressedLoadGroupID;
            validationTask.loadSecID = Evaluator.instance().pressedSectionID;
            validationTask.subjectID = Evaluator.instance().pressedSubjectID;

            validationTask.setOnSuccess(e -> {
                if (validationTask.isEligibleToTake()) {

                    /**
                     * Check Max Units
                     */
                    if ((unit_count + validationTask.getSubjectUnits()) > max_units) {
                        String text = validationTask.getSubjectCode() + ", Requires special permission to continue.\nClick here for more information.";
                        Notifications.create()
                                .title("Max Units Reached")
                                .text(text)
                                .onAction(pop -> {
                                    onPopRequest();

                                })
                                .position(Pos.BOTTOM_RIGHT).showInformation();
                        e.consume();
                        return;
                    }
                    /**
                     * Display notifications
                     */
                    String text = validationTask.getSubjectCode()
                            + "\nVerified For S/N: "
                            + currentStudent.getId() + " ," + currentStudent.getLast_name() + ".";
                    Notifications.create()
                            .title("Verified")
                            .text(text)
                            .position(Pos.BOTTOM_RIGHT).showInformation();

                    /**
                     * Add subject to List
                     */
                    SubjectView addedSubject = new SubjectView();
                    addedSubject.code.setText(validationTask.getSubjectCode());
                    addedSubject.title.setText(validationTask.getSubjectTitle());
                    addedSubject.section.setText(validationTask.getSectionWithFormat());
                    //
                    addedSubject.units = validationTask.getSubjectUnits();
                    addedSubject.lab_units = validationTask.getSubjectLabUnits();
                    addedSubject.lec_units = validationTask.getSubjectLecUnits();
                    addedSubject.subjectID = Evaluator.instance().pressedSubjectID;
                    addedSubject.loadGroupID = Evaluator.instance().pressedLoadGroupID;
                    addedSubject.loadSecID = Evaluator.instance().pressedSectionID;
                    //
                    addedSubject.actionFind.addEventHandler(MouseEvent.MOUSE_RELEASED, onRemove -> {
                        vbox_subjects.getChildren().remove(addedSubject);
                        Mono.fx().snackbar().showInfo(anchor_right, addedSubject.code.getText() + " Has Been Removed.");
                    });

                    vbox_subjects.getChildren().add(addedSubject);
                } else {

                    /**
                     * If subject is already Taken
                     */
                    if (validationTask.isAlreadyTaken()) {
                        String text = validationTask.getSubjectCode() + " is already taken."
                                + "\nVerified For S/N: "
                                + currentStudent.getId() + " ," + currentStudent.getLast_name() + ".";
                        Notifications.create()
                                .title("Already Taken")
                                .text(text)
                                .position(Pos.BOTTOM_RIGHT).showWarning();
                        e.consume();
                        return;
                    }

                    String text = validationTask.getSubjectCode()
                            + "\nVerified For S/N: "
                            + currentStudent.getId() + " ," + currentStudent.getLast_name() + ".\n"
                            + "Requires: ";
                    for (String string : validationTask.getSubjectNeededCode()) {
                        text += (string + " | ");
                    }
                    Notifications.create()
                            .title("Pre-Requisites Required")
                            .text(text)
                            .position(Pos.BOTTOM_RIGHT).showWarning();

                }

            });
            validationTask.setOnCancel(e -> {
                Notifications.create()
                        .title("Verification Failed")
                        .text("Please try again.")
                        .position(Pos.BOTTOM_RIGHT).showError();
            });

            //--------------------------------------------------------------
            /**
             * Validate if it's already in the list.
             */
            boolean isOnList = false;
            String existingCode = ""; // for pop ups
            /**
             * Loop that checks if the subject that being add is in the list.
             */
            //------------------------------------------------------------------
            for (Node node : vbox_subjects.getChildren()) {
                if (node instanceof SubjectView) {
                    SubjectView view = (SubjectView) node;
                    if (view.subjectID.equals(Evaluator.instance().pressedSubjectID)) {
                        isOnList = true;
                        existingCode = view.code.getText();
                        break;
                    }
                }
            }
            //------------------------------------------------------------------

            if (isOnList) {
                // already on the list
                Notifications.create()
                        .title("Already Added")
                        .text(existingCode + " is already in the list.")
                        .position(Pos.BOTTOM_RIGHT)
                        .showWarning();
            } else {
                /**
                 * Check for unit count
                 */
                //--------------------------------------------------------------
                //--------------------------------------------------------------
                /**
                 * transaction and ojt verification
                 *
                 * @date 08/31/2017
                 */

                String ojtVerification = ojtVerify(vbox_subjects, Evaluator.instance().pressedSubjectID).toLowerCase();
                if (ojtVerification.equals("allow")) {
                    // allow add
                    validationTask.transact();
                } else {
                    String warningtext = "";

                    if (ojtVerification.equals("limit_one")) {
                        warningtext = "Internship can only be taken with \n"
                                + "1 Minor/Elective Subject.";
                    } else if (ojtVerification.equals("not_allowed_with_major")) {
                        warningtext = "Cannot take Internship with a Major Subject";
                    }

                    Notifications.create().title("Warning")
                            .position(Pos.BOTTOM_RIGHT)
                            .text(warningtext)
                            .showWarning();
                }

                //--------------------------------------------------------------
            }
        } else {
            System.out.println("@EvaluateController: No Object Found.");
        }
    }

    private static void onPopRequest() {
    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    /**
     * verifies if there is an existing internship subject in the list.
     *
     * @date 08/31/2017
     *
     */
    private static String ojtVerify(VBox subjectBox, Integer subjectID) {
        // get info about the added subject.
        SubjectMapping addedSubject = (SubjectMapping) Database.connect()
                .subject()
                .getPrimary(subjectID);

        Integer subjectcnt = 0;
        subjectcnt = subjectBox.getChildren().stream().filter((node) -> (node instanceof SubjectView)).map((_item) -> 1).reduce(subjectcnt, Integer::sum);

        boolean listHasOjt = false;
        // check if the added subject is an internship subject.
        if (addedSubject.getType().equalsIgnoreCase(SubjectClassification.TYPE_INTERNSHIP)) {
            //------------------------------------------------------------------
            if (subjectcnt == 1) {
                SubjectView inTheList = (SubjectView) subjectBox.getChildren().get(0);
                SubjectMapping subInList = (SubjectMapping) Database.connect()
                        .subject()
                        .getPrimary(inTheList.subjectID);

                if (SubjectClassification.isMajor(subInList.getType())) {
                    // if the subject on the list is a major subject
                    return "NOT_ALLOWED_WITH_MAJOR";
                } else {
                    return "ALLOW";
                }
                //--------------------------------------------------------------
            } else if (subjectcnt == 0) {
                // if list is empty
                return "ALLOW";
            } else {
                // only one subject is allowed
                return "LIMIT_ONE";
            }
        } else {
            /**
             * If the user is trying to add a minor subject or regular one. we
             * will check if an internship subject is existing within the list.
             */
            for (Node node : subjectBox.getChildren()) {
                if (node instanceof SubjectView) {
                    SubjectView view = (SubjectView) node;
                    SubjectMapping subjectMap = (SubjectMapping) Database.connect()
                            .subject()
                            .getPrimary(view.subjectID);

                    if (subjectMap.getType().equalsIgnoreCase(SubjectClassification.TYPE_INTERNSHIP)) {
                        listHasOjt = true;
                        break;
                    }

                }
            }
            //------------------------------------------------------------------
        }
        /**
         * If the list has OJT.
         */
        if (listHasOjt) {
            if (subjectcnt > 1) {
                // if this subject was the third subject if added
                return "LIMIT_ONE";
            } else {
                if (SubjectClassification.isMajor(addedSubject.getType())) {
                    // if the subject on the list is a major subject
                    return "NOT_ALLOWED_WITH_MAJOR";
                } else {
                    return "ALLOW";
                }
            }
        }

        /**
         * If no ojt subject is on the list.
         */
        return "ALLOW";
    }

}

/* 
 * Copyright (C) Jhon Melvin N. Perello - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * PROPRIETARY and CONFIDENTIAL
 *
 * Written by Jhon Melvin N. Perello <jhmvinperello@gmail.com>, 2017.
 *
 */
package org.cict.evaluation.evaluator;

import app.lazy.models.AcademicProgramMapping;
import app.lazy.models.AcademicTermMapping;
import app.lazy.models.Database;
import app.lazy.models.LoadSectionMapping;
import app.lazy.models.MapFactory;
import app.lazy.models.StudentMapping;
import app.lazy.models.SubjectMapping;
import app.lazy.models.SystemOverrideLogsMapping;
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
import org.cict.authentication.authenticator.CollegeFaculty;
import org.cict.evaluation.sectionviewer.SearchSuggestions;
import org.cict.evaluation.views.SubjectView;
import org.controlsfx.control.Notifications;
import update.org.cict.controller.adding.ValidateOJT;
import update3.org.cict.access.Access;
import update3.org.cict.access.SystemOverriding;

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
        SubjectWatcher sw = Evaluator.instance().new SubjectWatcher();
        // add values.
        sw.currentStudent = currentStudent;
        sw.max_units = max_units;
        sw.unit_count = unit_count;
        sw.vbox_subjects = vbox_subjects;
        sw.anchor_right = anchor_right;
        // run analysis.
        sw.analyze();

    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    private class SubjectWatcher {

        private StudentMapping currentStudent;
        private double max_units;
        private double unit_count;
        private VBox vbox_subjects;
        private AnchorPane anchor_right;

        //local
        private ValidateAddedSubject validationTask;
        private final String accessLevel = CollegeFaculty.instance().getACCESS_LEVEL();

        //----------------------------------------------------------------------
        //----------------------------------------------------------------------
        //----------------------------------------------------------------------
        //----------------------------------------------------------------------
        //----------------------------------------------------------------------
        //----------------------------------------------------------------------
        /**
         * Override filters.
         */
        private boolean allowOverride = false;

        /**
         * Override Operations
         */
        private final String EXCEED_MAX_UNITS = SystemOverriding.EVAL_EXCEED_MAX_UNITS;
        private final String INTERNSHIP_WITH_OTHERS = SystemOverriding.EVAL_INTERNSHIP_WITH_OTHERS;
        private final String BYPASSED_PRE_REQUISITES = SystemOverriding.EVAL_BYPASSED_PRE_REQUISITES;
        private final String INTERN_GRADE_REQUIREMENT = SystemOverriding.EVAL_INTERN_GRADE_REQUIREMENT;

        /**
         * Retrieves section information of subjects that are forcibly added.
         *
         * @param sectionID
         * @return
         */
        private String getSection(Integer sectionID) {
            LoadSectionMapping sectionMap = Database.connect().load_section()
                    .getPrimary(sectionID);

            String sectionWithFormat = "";
            AcademicProgramMapping acadProgram = Mono.orm()
                    .newSearch(Database.connect().academic_program())
                    .eq("id", sectionMap.getACADPROG_id())
                    .active()
                    .first();

            if (acadProgram == null) {
//                    return false;
                sectionWithFormat = sectionMap.getSection_name();
            } else {
                sectionWithFormat = acadProgram.getCode() + " "
                        + sectionMap.getYear_level()
                        + ""
                        + sectionMap.getSection_name()
                        + " - G"
                        + sectionMap.get_group();
            }

            return sectionWithFormat;
        }

        /**
         * Overloaded method default for force adding of subject.
         */
        private void forceAdd() {
            this.forceAdd(false);
        }

        /**
         * Forcibly adds the subject without filters.
         */
        private void forceAdd(boolean crossEnrollee) {

            Integer subjectID = Evaluator.instance().pressedSubjectID;
            Integer sectionID = Evaluator.instance().pressedSectionID;

            SubjectMapping subjectMap = Database.connect().subject()
                    .getPrimary(subjectID);

            /**
             * Display notifications
             */
            String text = subjectMap.getCode()
                    + "\nOverrided For S/N: "
                    + currentStudent.getId() + " ," + currentStudent.getLast_name() + "."
                    + "\nThis action was captured and logged.";
            String title = "System Rules Overriden";

            if (crossEnrollee) {
                text = subjectMap.getCode()
                        + "\nAdded For S/N: "
                        + currentStudent.getId() + " ," + currentStudent.getLast_name() + "."
                        + "\nNo Validation was Called.";
                title = "Cross-Enrollment";
            }
            Notifications.create()
                    .title(title)
                    .text(text)
                    .position(Pos.BOTTOM_RIGHT).showWarning();

            //------------------------------------------------------------------
            //
            SubjectView addedSubject = new SubjectView();
            addedSubject.code.setText(subjectMap.getCode());
            addedSubject.title.setText(subjectMap.getDescriptive_title());
            addedSubject.section.setText(getSection(sectionID));
            //
            addedSubject.units = subjectMap.getLab_units() + subjectMap.getLec_units();
            addedSubject.lab_units = subjectMap.getLab_units();
            addedSubject.lec_units = subjectMap.getLec_units();
            addedSubject.subjectID = subjectID;
            addedSubject.loadGroupID = Evaluator.instance().pressedLoadGroupID;
            addedSubject.loadSecID = sectionID;
            //
            addedSubject.actionRemove.addEventHandler(MouseEvent.MOUSE_RELEASED, onRemove -> {

                int choice = Mono.fx().alert()
                        .createConfirmation()
                        .setHeader("Remove Subject")
                        .setMessage("Are you sure you want to remove the subject?")
                        .confirmYesNo();
                if (choice == 1) {
                    vbox_subjects.getChildren().remove(addedSubject);
                    Mono.fx()
                            .snackbar()
                            .showInfo(anchor_right, addedSubject.code.getText() + " Has Been Removed.");
                }

            });

            vbox_subjects.getChildren().add(addedSubject);
        }

        private void goLang(String type) {
            boolean ok = Access.isEvaluationOverride(allowOverride);
            if (ok) {
                SystemOverrideLogsMapping map = MapFactory.map().system_override_logs();
                map.setCategory(SystemOverriding.CATEGORY_EVALUATION);
                map.setDescription(type);
                map.setExecuted_by(CollegeFaculty.instance().getFACULTY_ID());
                map.setExecuted_date(Mono.orm().getServerTime().getDateWithFormat());
                map.setAcademic_term(SystemProperties.instance().getCurrentAcademicTerm().getId());
                String conforme = currentStudent.getLast_name() + ", ";

                conforme += currentStudent.getFirst_name();
                if (currentStudent.getMiddle_name() != null) {
                    conforme += " ";
                    conforme += currentStudent.getMiddle_name();
                }
                map.setConforme(conforme);
                map.setConforme_type("STUDENT");
                map.setConforme_id(currentStudent.getCict_id());

                int id = Database.connect().system_override_logs().insert(map);
                if (id <= 0) {
                    Mono.fx().snackbar().showError(anchor_right, "Something went wrong please try again.");
                } else {
                    forceAdd();
                }

            }
        }

        /**
         * (1)IS OVERRIDABLE BY SYSTEM. checks whether if the student is
         * overloaded.
         *
         * @param validationTask the validation task
         * @param max_units defined in public constant
         * @param unit_count current number of units.
         * @return
         */
        private boolean isOverMaxUnits() {
            if ((unit_count + validationTask.getSubjectUnits()) > max_units) {
                String text = validationTask.getSubjectCode() + ", Cannot be added."
                        + "\nThis will exceed the maximum units allowed."
                        + "\nClick This notification for more details.";
                Notifications.create()
                        .title("Max Units Reached")
                        .text(text)
                        .onAction(pop -> {
                            this.goLang(EXCEED_MAX_UNITS);
                        })
                        .position(Pos.BOTTOM_RIGHT).showWarning();
                return true;
            } else {
                return false;
            }
        }

        /**
         * (2)IS OVERRIDABLE BY SYSTEM. Tests whether adding of an Internship
         * subject is allowed. taking it with other subjects on the same
         * semester.
         *
         * @param validationTask
         * @param vbox_subjects
         * @return
         */
        private boolean isOJTVerified() {
            String ojtVerification = verifyOJT(vbox_subjects, Evaluator.instance().pressedSubjectID).toLowerCase();
            if (ojtVerification.equals("allow")) {
                return true;
            } else {
                String warningtext = "";

                if (ojtVerification.equals("limit_one")) {
                    warningtext = "Internship can only be taken with \n"
                            + "1 Minor/Elective Subject.";
                } else if (ojtVerification.equals("not_allowed_with_major")) {
                    warningtext = "Cannot take Internship with a Major Subject";
                }

                warningtext += "\nClick This notification for more details.";

                Notifications.create().title("Warning")
                        .position(Pos.BOTTOM_RIGHT)
                        .text(warningtext)
                        .onAction(onAction -> {
                            this.goLang(INTERNSHIP_WITH_OTHERS);
                        })
                        .showWarning();

                return false;
            }
        }

        /**
         * (3)IS OVERRIDABLE BY SYSTEM. checks whether the subject to take has
         * complete pre requisites.
         */
        private void hasIncompletePreRequisite() {
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
                    .onAction(a -> {
                        this.goLang(this.BYPASSED_PRE_REQUISITES);
                    })
                    .position(Pos.BOTTOM_RIGHT).showWarning();
        }

        /**
         * (4) IS OVERRIDABLE BY SYSTEM. Checks whether the student has
         * completed all the subjects and only missing with 1 Elective or 1
         * Minor and not both.
         *
         * @return
         */
        private boolean isInternshipAllowed() {
            return ValidateOJT.isValidForOJT(currentStudent);
        }

        //----------------------------------------------------------------------
        //----------------------------------------------------------------------
        //----------------------------------------------------------------------
        //----------------------------------------------------------------------
        //----------------------------------------------------------------------
        //----------------------------------------------------------------------
        // main function
        public void analyze() {
            // if local registrar allow override.
            allowOverride = Access.isGrantedIf(Access.ACCESS_LOCAL_REGISTRAR);

            System.out.println("@EvaluateController: Mouse Entered Drop Zone.");
            // check input
            if (!Evaluator.instance().sectionViewReleased) {
                // no events from the class viewer
                System.out.println("@EvaluateController: No Object Found.");
                return;
            }
            // events was from the section viewer
            System.out.println("@EvaluateController: Object Recieved.");
            Evaluator.instance().sectionViewReleased = false; //refresh when object was recieved.

            // creates the task but noy yet started.
            createTask();
            // these are methods to test before executing the transaction.
            if (this.isAlreadyOnTheList()) {
                // checks if already in the list.
                return;
            }
            //------------------------------------------------------------------
            // CROSS-ENROLLEE FILTER SHOULD BE ADDED HERE
            if (currentStudent.getResidency().equalsIgnoreCase("CROSS_ENROLLEE")) {
                // this is a cross enrollment force add
                forceAdd(true);
                return;
            }
            //------------------------------------------------------------------
            // check for ojt if can take with other subject on the same time
            if (!this.isOJTVerified()) {
                // if ojt not verified
                return;
            }

            // get the subject information if no subject info return
            Integer subjectID = Evaluator.instance().pressedSubjectID;
            if (subjectID == null) {
                Mono.fx().snackbar().showError(anchor_right, "Cannot Recieved Subject Information");
                return;
            }

            // get details from database.
            SubjectMapping sub = Database.connect().subject().getPrimary(subjectID);
            if (sub == null) {
                Mono.fx().snackbar().showError(anchor_right, "Cannot Connect to Database");
                return;
            }

            // if the subject is an internship subject
            if (sub.getType().equalsIgnoreCase(SubjectClassification.TYPE_INTERNSHIP)) {
                // if internship
                if (!this.isInternshipAllowed()) {
                    Notifications.create()
                            .title("Missing Grades")
                            .text("Grades Defficiency.")
                            .onAction(a -> {
                                this.goLang(INTERN_GRADE_REQUIREMENT);
                            })
                            .position(Pos.BOTTOM_RIGHT).showWarning();
                    return;
                }
            }

            // if passed the filter continue
            // starts transaction.
            validationTask.transact();
        }

        /**
         * Create Task.
         */
        private void createTask() {
            // validator
            validationTask = Evaluator
                    .instance()
                    .createValidateAddedSubject();
            validationTask.studentCictID = currentStudent.getCict_id();
            validationTask.loadGroupID = Evaluator.instance().pressedLoadGroupID;
            validationTask.loadSecID = Evaluator.instance().pressedSectionID;
            validationTask.subjectID = Evaluator.instance().pressedSubjectID;

            validationTask.setOnSuccess(e -> {
                onValidateSuccess();
                e.consume();
            });

            validationTask.setOnCancel(e -> {
                Notifications.create()
                        .title("Verification Failed")
                        .text("Please try again.")
                        .position(Pos.BOTTOM_RIGHT).showError();
            });
        }

        /**
         * When the validation result is success.
         */
        private void onValidateSuccess() {
            // check eligibility
            if (validationTask.isEligibleToTake()) {
                if (this.isOverMaxUnits()) {
                    return;
                }
                // add subject to the list if eligible and not max units
                this.addToList();
            } else {
                // subject already taken
                if (this.isAlreadyTaken()) {
                    return;
                }
                // has incomplete pre requisites
                this.hasIncompletePreRequisite();
            }
        }

        //----------------------------------------------------------------------
        /**
         * Add to list this is dependent on the Validation Task.
         */
        private void addToList() {
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
            addedSubject.actionRemove.addEventHandler(MouseEvent.MOUSE_RELEASED, onRemove -> {

                int choice = Mono.fx().alert()
                        .createConfirmation()
                        .setHeader("Remove Subject")
                        .setMessage("Are you sure you want to remove the subject?")
                        .confirmYesNo();
                if (choice == 1) {
                    vbox_subjects.getChildren().remove(addedSubject);
                    Mono.fx()
                            .snackbar()
                            .showInfo(anchor_right, addedSubject.code.getText() + " Has Been Removed.");
                }

            });

            vbox_subjects.getChildren().add(addedSubject);
        }

        private boolean isAlreadyTaken() {
            if (validationTask.isAlreadyTaken()) {
                String text = validationTask.getSubjectCode() + " is already taken."
                        + "\nVerified For S/N: "
                        + currentStudent.getId() + " ," + currentStudent.getLast_name() + ".";
                Notifications.create()
                        .title("Already Taken")
                        .text(text)
                        .position(Pos.BOTTOM_RIGHT).showWarning();
                return true;
            }
            return false;
        }

        /**
         * checks whether the subject that is trying to add is already on the
         * list. same subject in different section will still return true.
         *
         * @param vbox_subjects
         * @return
         */
        private boolean isAlreadyOnTheList() {
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

            if (isOnList) {
                // already on the list
                Notifications.create()
                        .title("Already Added")
                        .text(existingCode + " is already in the list.")
                        .position(Pos.BOTTOM_RIGHT)
                        .showWarning();
            }

            return isOnList;
        }

        /**
         * Helper function for verification of ojt.
         */
        private String verifyOJT(VBox subjectBox, Integer subjectID) {
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
                 * If the user is trying to add a minor subject or regular one.
                 * we will check if an internship subject is existing within the
                 * list.
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

    } // inner clas end

}

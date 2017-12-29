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
package org.bsu.cict.queue;

import app.lazy.models.AccountFacultyMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.LinkedPilaMapping;
import app.lazy.models.LinkedSettingsMapping;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javax.inject.Inject;
import org.bsu.cict.alerts.MessageBox;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.hibernate.criterion.Order;

/**
 * A Class that controls calling next student.
 *
 * @author Jhon Melvin
 */
public class CallerObject {

    // All Dependencies must be set.
    @Inject
    private Label lbl_que_cluster;
    @Inject
    private Label lbl_que_cluster_name;
    @Inject
    private Label lbl_que_terminal;
    @Inject
    private VBox vbox_que_call_next;
    @Inject
    private JFXButton btn_que_call_next;
    @Inject
    private VBox vbox_que_finish;
    @Inject
    private JFXButton btn_que_finish;
    @Inject
    private VBox vbox_que_load;

    public void setLbl_que_cluster(Label lbl_que_cluster) {
        this.lbl_que_cluster = lbl_que_cluster;
    }

    public void setLbl_que_cluster_name(Label lbl_que_cluster_name) {
        this.lbl_que_cluster_name = lbl_que_cluster_name;
    }

    public void setLbl_que_terminal(Label lbl_que_terminal) {
        this.lbl_que_terminal = lbl_que_terminal;
    }

    public void setVbox_que_call_next(VBox vbox_que_call_next) {
        this.vbox_que_call_next = vbox_que_call_next;
    }

    public void setBtn_que_call_next(JFXButton btn_que_call_next) {
        this.btn_que_call_next = btn_que_call_next;
    }

    public void setVbox_que_finish(VBox vbox_que_finish) {
        this.vbox_que_finish = vbox_que_finish;
    }

    public void setBtn_que_finish(JFXButton btn_que_finish) {
        this.btn_que_finish = btn_que_finish;
    }

    public void setVbox_que_load(VBox vbox_que_load) {
        this.vbox_que_load = vbox_que_load;
    }

    //--------------------------------------------------------------------------
    @Inject
    private JFXButton btnFind;
    @Inject
    private TextField txtStudentNumber;
    @Inject
    private boolean allowSearch;
    private Runnable searchEvent;

    /**
     * What to do when there is a next student to be called.
     *
     * @param searchEvent
     */
    public void setSearchEvent(Runnable searchEvent) {
        this.searchEvent = searchEvent;
    }

    /**
     * Set whether the user can search.
     *
     * @param allowSearch
     */
    public void setAllowSearch(boolean allowSearch) {
        this.allowSearch = allowSearch;
    }

    /**
     *
     * search button.
     *
     * @param btnFind
     */
    public void setBtnFind(JFXButton btnFind) {
        this.btnFind = btnFind;
    }

    /**
     * The Search Textfield.
     */
    public void setTxtStudentNumber(TextField txtStudentNumber) {
        this.txtStudentNumber = txtStudentNumber;
    }

    //--------------------------------------------------------------------------
    private final String terminalName;
    private final String callerName;

    public CallerObject() {
        //
        this.terminalName = Mono.sys().getTerminal();
        this.callerName = CollegeFaculty.instance().getFirstLastName();
        this.allowSearch = true;
        this.searchEvent = () -> {
            // do nothing
        };
    }

    private LinkedPilaMapping currentPila;

    /**
     * This is to initialize the controls of the queue system. this method
     * should be called once in the initialization block.
     */
    public void queEvents() {
        lbl_que_terminal.setText(this.terminalName);
        // if search is not allowed disable search button
        if (!this.allowSearch) {
            this.btnFind.setDisable(true);
        }

        // default is call next.
        this.queView(this.vbox_que_call_next);

        /**
         * Invoke Call Next Button.
         */
        this.btn_que_call_next.setOnMouseClicked(click -> {
            this.callNextOnQueue();
            click.consume(); // end event
        });

        /**
         * Invoke Finish Button.
         */
        this.btn_que_finish.setOnMouseClicked(click -> {
            this.onFinishCurrentQueue();
            click.consume(); // end event
        });
    }

    /**
     * Switch between queue views.
     *
     * @param queView
     */
    private void queView(VBox queView) {
        vbox_que_call_next.setVisible(false);
        vbox_que_finish.setVisible(false);
        vbox_que_load.setVisible(false);

        queView.setVisible(true);
    }

    /**
     * Get the current active linked settings and marked as alive.
     *
     * @return
     */
    private LinkedSettingsMapping getCurrentLinkedSettings() {
        LinkedSettingsMapping linkSettingsMap = Mono.orm()
                .newSearch(Database.connect().linked_settings())
                .eq(DB.linked_settings().mark, "ALIVE")
                .active(Order.desc(DB.linked_settings().id))
                .first();
        return linkSettingsMap;
    }

    /**
     * Get the faculty current assigned cluster.
     */
    private Integer getFacultyFloorAssignment() {
        AccountFacultyMapping accounfFacultyMap = Database.connect().account_faculty().getPrimary(CollegeFaculty.instance().getACCOUNT_ID());
        Integer clusterNumber = accounfFacultyMap.getAssigned_cluster();
        return clusterNumber;
    }

    /**
     * Invoke the event for calling the next student in line.
     *
     * this must refresh the faculty floor assignment and linked settings so
     * that it can monitor changes in the following tables.
     */
    private void callNextOnQueue() {
        //--------------------------------------------------------------
        Integer facultyFloorAssignment = this.getFacultyFloorAssignment();
        //--------------------------------------------------------------
        if (facultyFloorAssignment == null) {
            // no cluster assignment display
            this.lbl_que_cluster.setText("n / a");
            // since there is no floor assignment the cluster name cannot be 
            this.lbl_que_cluster_name.setText("n / a");

            Mono.fx().alert().createWarning()
                    .setTitle("System")
                    .setHeader("Unassigned")
                    .setMessage("You are not yet assigned to any cluster.")
                    .showAndWait();

            return;
        } else {
            // if there is an assigned floor
            if (facultyFloorAssignment == 3 || facultyFloorAssignment == 4) {
                // it mus only be between this 2 number
                String clusterLabel = (facultyFloorAssignment == 3 ? "1" : "2");
                /**
                 * Cluster Assignment now set.
                 */
                this.lbl_que_cluster.setText(clusterLabel);
                //--------------------------------------------------------------
                /**
                 * Get its name.
                 */
                LinkedSettingsMapping currentLinked = this.getCurrentLinkedSettings();
                //--------------------------------------------------------------
                if (currentLinked == null) {
                    MessageBox.showWarning("Offline", "The Queue System is offline.");
                } else {
                    String clusterNameLabel = (facultyFloorAssignment == 3
                            ? currentLinked.getFloor_3_name()
                            : currentLinked.getFloor_4_name());

                    if (clusterNameLabel == null) {
                        clusterNameLabel = "n / a";
                    }
                    /**
                     * Name now set.
                     */
                    this.lbl_que_cluster_name.setText(clusterNameLabel);

                    //----------------------------------------------------------
                    // All Details Ready, can now execute call next.
                    CallNextTransaction cnt = new CallNextTransaction();
                    // setters are now properly placed
                    cnt.setFloorAssignment(facultyFloorAssignment);
                    cnt.setLinkedSessionID(currentLinked.getId());
                    cnt.setTerminalName(this.terminalName);
                    cnt.setCallerName(this.callerName);
                    // 2 more needs to be set terminal name and caller name
                    //----------------------------------------------------------
                    // show loading

                    this.queView(vbox_que_load);
                    // disable button before calling
                    this.btnFind.setDisable(true);
                    cnt.start((Exception e) -> {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                        //------------------------------------------------------
                        this.onCalledTransaction(e, cnt);
                        //------------------------------------------------------

                        if (this.allowSearch) {
                            // if search is allowed re enable
                            Platform.runLater(() -> {
                                this.btnFind.setDisable(false);
                            });
                        }
                    });

                    //----------------------------------------------------------
                }

            } else {
                // no cluster assignment display
                this.lbl_que_cluster.setText("n / a");
                // since there is no floor assignment the cluster name cannot be 
                this.lbl_que_cluster_name.setText("n / a");
                Mono.fx().alert().createError()
                        .setTitle("System")
                        .setHeader("ERROR")
                        .setMessage("You are assigned to an invalid cluster.")
                        .showAndWait();
                return;
            }
        }

    }

    /**
     * After executing the transaction.
     *
     * @param e
     * @param cnt
     */
    private void onCalledTransaction(Exception e, CallNextTransaction cnt) {
        if (e != null) {
            e.printStackTrace();
            MessageBox.showError("Failed", "Cannot retrieve queue, please try again.");
            Platform.runLater(() -> {
                this.queView(vbox_que_call_next);
            });

            return;
        }

        if (!cnt.isWithNext()) {
            Platform.runLater(() -> {
                Mono.fx().alert().createInfo()
                        .setTitle("System")
                        .setHeader("Empty")
                        .setMessage("There are no students in the queue")
                        .showAndWait();
            });
            Platform.runLater(() -> {
                this.queView(vbox_que_call_next);
            });

            return;
        }
        //------------------------------------------------------
        /**
         * Call Next Student Block.
         */
        this.currentPila = cnt.getNextCalled();
        Platform.runLater(() -> {
            this.txtStudentNumber.setText(this.currentPila.getConforme());
            //------------------------------------------------------------------
            // change view
            this.queView(vbox_que_finish);
            //------------------------------------------------------------------
            // run search
            this.searchEvent.run();
        });
        //------------------------------------------------------
    }

    /**
     * Mark the current transaction as done.
     */
    private void onFinishCurrentQueue() {
        FinishCurrentTransaction fct = new FinishCurrentTransaction();
        fct.setLinkedPilaID(this.currentPila.getId());
        this.queView(vbox_que_load); // show loading
        // disable button before calling
        this.btnFind.setDisable(true);
        fct.start((Exception e) -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            //------------------------------------------------------------------
            //------------------------------------------------------------------
            // change view
            this.queView(vbox_que_call_next);
            //------------------------------------------------------------------
            //------------------------------------------------------------------
            if (this.allowSearch) {
                // if search is allowed re enable
                Platform.runLater(() -> {
                    this.btnFind.setDisable(false);
                });
            }
        });

    }

}

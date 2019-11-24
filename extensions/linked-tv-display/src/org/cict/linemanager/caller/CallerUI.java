/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and create the template in the editor.
 */
package org.cict.linemanager.caller;

import app.lazy.models.AnnouncementsMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.LinkedPilaMapping;
import app.lazy.models.LinkedSettingsMapping;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.CronTimer;
import com.jhmvin.fx.async.SimpleTask;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import com.melvin.mono.fx.bootstrap.M;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.hibernate.criterion.Order;

/**
 *
 * @author Jhon Melvin
 */
public class CallerUI extends SceneFX implements ControllerFX {

    @FXML
    private AnchorPane application_root;

    @FXML
    private VBox vbox_main;

    @FXML
    private Label cluster_1_name;

    @FXML
    private Label lbl_3;

    @FXML
    private VBox vbox_table_left;

    @FXML
    private Label cluster_2_name;

    @FXML
    private Label lbl_4;

    @FXML
    private VBox vbox_table_right;

    @FXML
    private VBox vbox_announce_body;

    @FXML
    private Label lbl_announcement_title;

    @FXML
    private Label lbl_announcement_body;

    @FXML
    private Label lbl_announcement_time;

    @FXML
    private VBox vbox_loading;

    public CallerUI() {
        //
    }

    public final static int FLOOR_THREE = 3;
    public final static int FLOOR_FOUR = 4;
    public final static int SHOW_NEXT = 10;

    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a MM/dd/yyyy");
    private CronTimer refreshList;

    @Override
    public void onInitialization() {

        SimpleTask launch_hib = new SimpleTask("boot-hib");
        launch_hib.setTask(() -> {
            Database.connect();
        });
//------------------------------------------------------------------------------
//        this.vbox_loading.setVisible(true);
//        this.vbox_main.setVisible(false);
//        Thread bootRevHib = new Thread(() -> {
//            try {
//                Database.connect();
//            } catch (RuntimeException e) {
//                // test failed
//                System.out.println("CONNECTION TEST FAILED");
//            }
//
//            if (Mono.orm().getSessionFactory() == null) {
//                Platform.runLater(() -> {
//                    Mono.fx().alert().createWarning()
//                            .setTitle("Unreachable")
//                            .setHeader("Connection Failed")
//                            .setMessage("Cannot established connection with the host server.")
//                            .showAndWait();
//
//                    Runtime.getRuntime().halt(0);
//                });
//            }
//
//            Platform.runLater(() -> {
//                super.bindScene(application_root);
//                refreshList = new CronTimer("refresh-list");
//                refreshList.setInterval(1000);
//                refreshList.setTask(() -> {
//                    displayNext();
//                    displayNow();
//                    fetchAnnouncement();
//                });
//                refreshList.start();
//
//                this.vbox_loading.setVisible(false);
//                this.vbox_main.setVisible(true);
//            });
//
//        });
//
//        bootRevHib.start();
//        System.out.println("STARTED");
//------------------------------------------------------------------------------

        launch_hib.whenStarted(() -> {
            System.out.println("Start");
            this.vbox_loading.setVisible(true);
            this.vbox_main.setVisible(false);
        });
        launch_hib.whenCancelled(() -> {
            System.out.println("cancel");

        });
        launch_hib.whenFailed(() -> {
            System.out.println("fail");

        });
        launch_hib.whenSuccess(() -> {
            System.out.println("success");
        });
        launch_hib.whenFinished(() -> {
            System.out.println("finish");

            if (Mono.orm().getSessionFactory() == null) {
                Mono.fx().alert().createWarning()
                        .setTitle("Unreachable")
                        .setHeader("Connection Failed")
                        .setMessage("Cannot established connection with the host server.")
                        .showAndWait();
                Runtime.getRuntime().halt(0);
                // no connection
                return;
            }

            super.bindScene(application_root);
            refreshList = new CronTimer("refresh-list");
            refreshList.setInterval(1000);
            refreshList.setTask(() -> {
                displayNext();
                displayNow();
                fetchAnnouncement();
            });
            refreshList.start();

            this.vbox_loading.setVisible(false);
            this.vbox_main.setVisible(true);

        });

        launch_hib.start();
    }

    @Override
    public void onEventHandling() {

    }

    public void onDelayedStart() {
        Stage current = Mono.fx().getParentStage(this.application_root);
        current.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, k -> {
            if (k.getCode().equals(KeyCode.F11)) {
                current.setFullScreen(!current.isFullScreen());
            }
        });
        current.setOnCloseRequest(close -> {
            int ans = Mono.fx().alert().createConfirmation()
                    .setTitle("Exit")
                    .setHeader("Close Application ?")
                    .setMessage("Are you sure you want to close the application")
                    .confirmYesNo();
            if (ans == 1) {
                try {
                    this.refreshList.stop();
                    Mono.orm().shutdown();
                    Runtime.getRuntime().halt(0);
                } catch (Exception e) {
                    Runtime.getRuntime().halt(0);
                }
            }
            close.consume();
        });
    }

    /**
     * Displays the current latest called number.
     */
    private void displayNow() {
        LinkedPilaMapping call_3 = getLatestCalled(FLOOR_THREE);
        LinkedPilaMapping call_4 = getLatestCalled(FLOOR_FOUR);

        // update labels
        Platform.runLater(() -> {
            // fx thread
            String called_3 = "# ";
            String called_4 = "# ";

            if (call_3 == null) {
                called_3 = "NONE";
                lbl_3.setVisible(false);
            } else {
                called_3 += getFloorNumber(call_3);
                lbl_3.setVisible(true);
            }

            if (call_4 == null) {
                called_4 = "NONE";
                lbl_4.setVisible(false);
            } else {
                called_4 += getFloorNumber(call_4);
                lbl_4.setVisible(true);
            }
            System.out.println(called_3);
            lbl_3.setText(called_3);
            lbl_4.setText(called_4);
        });

    }

    /**
     * Gets the latest number that is called in a specific floor.
     *
     * @param floor
     * @return
     */
    private LinkedPilaMapping getLatestCalled(Integer floor) {
        LinkedPilaMapping latest_called = Mono.orm()
                .newSearch(Database.connect().linked_pila())
                .eq(DB.linked_pila().status, "CALLED")
                .eq(DB.linked_pila().floor_assignment, floor)
                .active(Order.desc(DB.linked_pila().id))
                .first();
        return latest_called;
    }

    /**
     * Displays the next to be called.
     */
    private void displayNext() {
//        // Get the current active settings
//        // requires id floor names.
//        String latestSettingQuery = "SELECT id, floor_3_name, floor_4_name "
//                + "FROM linked_settings "
//                + "WHERE active = 1 "
//                + "ORDER BY id DESC "
//                + "LIMIT 1";
//
//        ConnectionManager cm = NekoManager.create();
//        ResultTable latestSettings = cm.executePreparedQuery(latestSettingQuery);
//        cm.closeQuietly();
//        
//        if (latestSettings.isEmpty()) {
//            return;
//        }
//
//        ResultRow latestSettingData = latestSettings.getRow(0);
//        Integer current_id = latestSettingData.getValue("id");
//        String floor_3_name = latestSettingData.getValue("floor_3_name");
//        String floor_4_name = latestSettingData.getValue("floor_4_name");

        LinkedSettingsMapping currentActive = Mono.orm()
                .newSearch(Database.connect().linked_settings())
                .active(Order.desc(DB.linked_settings().id))
                .first();

        if (currentActive == null) {
            return; // do nothing
        }

        ArrayList<LinkedPilaMapping> pila_3 = Mono.orm()
                .newSearch(Database.connect().linked_pila())
                .eq(DB.linked_pila().SETTINGS_id, currentActive.getId())
                .eq(DB.linked_pila().status, "INLINE")
                .eq(DB.linked_pila().floor_assignment, FLOOR_THREE)
                .active(Order.asc("id"))
                .take(SHOW_NEXT);

        ArrayList<LinkedPilaMapping> pila_4 = Mono.orm()
                .newSearch(Database.connect().linked_pila())
                .eq(DB.linked_pila().SETTINGS_id, currentActive.getId())
                .eq(DB.linked_pila().status, "INLINE")
                .eq(DB.linked_pila().floor_assignment, FLOOR_FOUR)
                .active(Order.asc("id"))
                .take(SHOW_NEXT);

        // UI Manipulation in FX Thread.
        Platform.runLater(() -> {
            if (pila_3 != null) {
                createTable(pila_3, vbox_table_left);
            } else {
                vbox_table_left.getChildren().clear();
            }

            if (pila_4 != null) {
                createTable(pila_4, vbox_table_right);
            } else {
                vbox_table_right.getChildren().clear();
            }

            this.cluster_1_name.setText(currentActive.getFloor_3_name());
            this.cluster_2_name.setText(currentActive.getFloor_4_name());

        });

    }

    /**
     * Creates a table for the next numbers that will be called.
     *
     * @param collection
     * @param tableHolder
     */
    private void createTable(ArrayList<LinkedPilaMapping> collection, VBox tableHolder) {
        SimpleTable callerTable = new SimpleTable();
        //
        for (LinkedPilaMapping pmap : collection) {
            SimpleTableRow row = new SimpleTableRow();
            row.setRowHeight(50.0);

            RowQue rowController = M.load(RowQue.class);
            HBox programRow = rowController.getApplicationRoot();

            Label lbl_number = rowController.getLbl_number();
            Label lbl_name = rowController.getLbl_student_number(); // conforme

            lbl_number.setText("# " + getFloorNumber(pmap));
            lbl_name.setText(pmap.getConforme());

            // create cell container
            SimpleTableCell cellParent = new SimpleTableCell();
            cellParent.setResizePriority(Priority.ALWAYS);
            cellParent.setContent(programRow);

            // add cell to row
            row.addCell(cellParent);

            callerTable.addRow(row);
        }

        // when all details are added in the table;
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(callerTable);
        simpleTableView.setFixedWidth(true);
        // attach to parent variable name in scene builder
        simpleTableView.setParentOnScene(tableHolder);

    }

    /**
     * Gets the floor number directly to the same table.
     *
     * @param map
     * @return
     */
    private String getFloorNumber(LinkedPilaMapping map) {
        try {
            return map.getFloor_number().toString();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Get The latest announcement for the sytem
     */
    private void fetchAnnouncement() {
//        String fetchQuery = "SELECT title,message,date FROM announcements "
//                + "WHERE active = 1 "
//                + "ORDER BY id DESC "
//                + "LIMIT 1";
//        ConnectionManager cm = NekoManager.create();
//        ResultTable announcementData = cm.executePreparedQuery(fetchQuery);
//        cm.closeQuietly();
//
//        if (announcementData.isEmpty()) {
//            Platform.runLater(() -> {
//                // hide the announcement table
//                vbox_announce_body.setVisible(false);
//            });
//        } else {
//            ResultRow row = announcementData.getRow(0);
//            String title = row.getValue("title");
//            String message = row.getValue("message");
//            Date date = row.getValue("date");
//
//            Platform.runLater(() -> {
//                vbox_announce_body.setVisible(true);
//                try {
//                    lbl_announcement_title.setText(title);
//                    lbl_announcement_body.setText(message);
//                    lbl_announcement_time.setText(dateFormat.format(date));
//                } catch (Exception e) {
//                    vbox_announce_body.setVisible(false);
//                }
//
//            });
//        }

        AnnouncementsMapping latestAnnouncement = Mono.orm().newSearch(Database.connect().announcements())
                .active(Order.desc(DB.announcements().id))
                .first();

        if (latestAnnouncement == null) {
            Platform.runLater(() -> {
                // hide the announcement table
                vbox_announce_body.setVisible(false);
            });
        } else {
            Platform.runLater(() -> {
                vbox_announce_body.setVisible(true);
                try {
                    lbl_announcement_title.setText(latestAnnouncement.getTitle());
                    lbl_announcement_body.setText(latestAnnouncement.getMessage());
                    lbl_announcement_time.setText(dateFormat.format(latestAnnouncement.getDate()));
                } catch (Exception e) {
                    vbox_announce_body.setVisible(false);
                }

            });
        }
    }

//
//    /**
//     * Translates the reference floor number to actual number.
//     *
//     * @param map
//     * @return
//     */
//    private String getFloorNumber(LinkedPilaMapping map) {
//
//        String actual = "ERROR";
//
//        if (map.getFloor_assignment() == 3) {
//            try {
//                LinkedPila3fMapping actual_number = Mono.orm()
//                        .newSearch(Database.connect().linked_pila_3f())
//                        .eq("pila_id", map.getId())
//                        .active()
//                        .first();
//                actual = actual_number.getId().toString();
//            } catch (Exception e) {
//                return actual;
//            }
//        } else if (map.getFloor_assignment() == 4) {
//            try {
//                LinkedPila4fMapping actual_number = Mono.orm()
//                        .newSearch(Database.connect().linked_pila_4f())
//                        .eq("pila_id", map.getId())
//                        .active()
//                        .first();
//                actual = actual_number.getId().toString();
//            } catch (Exception e) {
//                return actual;
//            }
//        }
//        return actual;
//    }
}

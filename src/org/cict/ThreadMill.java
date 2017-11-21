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
package org.cict;

import app.lazy.models.AccountFacultyMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.LinkedEntranceMapping;
import app.lazy.models.StudentMapping;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.CronTimer;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import com.melvin.mono.fx.bootstrap.M;
import com.melvin.mono.fx.events.MonoClick;
import java.util.ArrayList;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.text.WordUtils;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.cict.evaluation.QueueRow;
import org.controlsfx.control.Notifications;
import org.hibernate.criterion.Order;

/**
 *
 * @author Jhon Melvin
 */
public class ThreadMill {

    private static ThreadMill THREAD_INSTANCE;

    public static ThreadMill threads() {
        if (THREAD_INSTANCE == null) {
            THREAD_INSTANCE = new ThreadMill();
        }
        return THREAD_INSTANCE;
    }

    /**
     * List your threads here.
     */
    public CronTimer KEEP_ALIVE_THREAD;

    private ThreadMill() {
        // Create Threads here
        this.threadFactory();
    }

    private void threadFactory() {
        this.KEEP_ALIVE_THREAD = new CronTimer("loginkeep-alive");
        this.KEEP_ALIVE_THREAD.setInterval(30000); // 30 secs
        // add more threads here
    }

    /**
     * Add All thread entries here.
     */
    public void shutdown() {
        /**
         * Make this thread instance null.
         */
        THREAD_INSTANCE = null;

        /**
         * Destroy Keep Alive Thread.
         */
        KEEP_ALIVE_THREAD.pause();
        KEEP_ALIVE_THREAD.stop();
        KEEP_ALIVE_THREAD = null;
    }
    
    private static SimpleTable studentTable = null;
    public static boolean searching = false;
    public static boolean resfresh(VBox holder, TextField txtfieldSearch, Label labelTotal, Integer clusterNumber) {
        ArrayList<LinkedEntranceMapping> leMaps;
        if(clusterNumber==null) {
            leMaps = Mono.orm().newSearch(Database.connect().linked_entrance())
                    .eq(DB.linked_entrance().status, "NONE")
                    .active(Order.asc(DB.linked_entrance().reference_id)).take(5);
        } else {
            leMaps = Mono.orm().newSearch(Database.connect().linked_entrance())
                    .eq(DB.linked_entrance().status, "NONE")
                    .eq(DB.linked_entrance().floor_assignment, clusterNumber)
                    .active(Order.asc(DB.linked_entrance().reference_id)).take(5);
        }
        if(leMaps==null || leMaps.isEmpty()) {
            return false;
        }
        
        Mono.fx().thread().wrap(()->{
            studentTable = new SimpleTable();
            labelTotal.setText("Loading...");
        });
        for (int i = 0; i < leMaps.size(); i++) {
            LinkedEntranceMapping leMap = leMaps.get(i);
            createRow(leMap, txtfieldSearch, holder, labelTotal, clusterNumber);
        }
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setFixedWidth(true);

        Mono.fx().thread().wrap(()->{
            simpleTableView.setTable(studentTable);
            simpleTableView.setParentOnScene(holder);
            labelTotal.setText(leMaps.size() + "");
        });
        return true;
    }
    
    private static void createRow(LinkedEntranceMapping leMap, TextField txtStudentNumber, VBox holder, Label labelTotal, Integer clusterNumber) {
        SimpleTableRow row = new SimpleTableRow();
        row.setRowHeight(70.0);
        row.getRowMetaData().put("MAP", leMap);
        QueueRow rowFX = M.load(QueueRow.class);
        rowFX.getLbl_id().setText(leMap.getStudent_number());
        rowFX.getLbl_name().setText(WordUtils.capitalizeFully(getStudentName(leMap.getStudent_number())));
        rowFX.getLbl_number().setText(leMap.getReference_id() + "");
        SimpleTableCell cellParent = new SimpleTableCell();
        cellParent.setResizePriority(Priority.ALWAYS);
        cellParent.setContent(rowFX.getApplicationRoot());
        row.addCell(cellParent);
        Mono.fx().thread().wrap(()->{
            MonoClick.addClickEvent(row, ()->{
                LinkedEntranceMapping selected = (LinkedEntranceMapping) row.getRowMetaData().get("MAP");
                LinkedEntranceMapping selectedleMap = Database.connect().linked_entrance().getPrimary(selected.getReference_id());
                if(selectedleMap==null || selectedleMap.getStatus().equalsIgnoreCase("DONE")) {
                    Notifications.create().darkStyle().title("Done")
                            .text("Selected student is already serving.")
                            .showInformation();
                    return;
                }
                selected.setStatus("DONE");
                boolean res = Database.connect().linked_entrance().update(selected);
                if(!res) {
                    Notifications.create().darkStyle().title("Failed")
                            .text("Please check your connectivity to the server.")
                            .showError();
                    return;
                }
                txtStudentNumber.setText(selected.getStudent_number());
                searching = true;
                ThreadMill.resfresh(holder, txtStudentNumber, labelTotal, clusterNumber);
            });
            row.setDisable(!studentTable.getChildren().isEmpty());
            studentTable.addRow(row);
        });
    }
    
    private static String getStudentName(String studentNumber) {
        StudentMapping student = Mono.orm().newSearch(Database.connect().student())
                .eq(DB.student().id, studentNumber).active(Order.desc(DB.student().cict_id)).first();
        if(student==null) {
            return "";
        }
        return student.getLast_name() + ", " + student.getFirst_name() + " " + (student.getMiddle_name()==null? "" : student.getMiddle_name());
    }
}

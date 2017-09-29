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
package update3.org.cict.scheduling;

import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.LoadGroupMapping;
import app.lazy.models.LoadGroupScheduleMapping;
import app.lazy.models.LoadSectionMapping;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;
import update3.org.cict.ScheduleConstants;

/**
 *
 * @author Jhon Melvin
 */
public class OpenScheduleViewer {

    public static void openScheduleViewer(LoadSectionMapping load_sec) {

        class ScheduleViewerStartUp extends Transaction {

            HashMap<String, ArrayList<ScheduleData>> data = new HashMap<>();

            @Override
            protected boolean transaction() {
                /**
                 * Get all load groups from that section.
                 */
                ArrayList<LoadGroupMapping> loadGroups = Mono.orm()
                        .newSearch(Database.connect().load_group())
                        .eq(DB.load_group().LOADSEC_id, load_sec.getId())
                        .active()
                        .all();

                if (loadGroups == null) {
                    return true;
                }

                /**
                 * Iterate to all load groups of that section.
                 */
                // main container of all schedules.
                ArrayList<LoadGroupScheduleMapping> loadSchedules = new ArrayList<>();
                for (LoadGroupMapping loadGroup : loadGroups) {
                    /**
                     * Schedules of that load group.
                     */
                    ArrayList<LoadGroupScheduleMapping> scheds = Mono.orm().
                            newSearch(Database.connect().load_group_schedule())
                            .eq(DB.load_group_schedule().load_group_id, loadGroup.getId())
                            .active()
                            .all();

                    if (scheds == null) {
                        continue;
                    }

                    loadSchedules.addAll(scheds);
                }

                /**
                 * Iterate all over the days of the week.
                 */
                ScheduleConstants.getDayList().forEach(day -> {
                    /**
                     * Get load schedules of that day.
                     */
                    ArrayList<ScheduleData> perDay = new ArrayList<>();
                    for (LoadGroupScheduleMapping daySched : loadSchedules) {
                        /**
                         * Check this day.
                         */
                        if (daySched.getClass_day().equalsIgnoreCase(day)) {
                            /**
                             * Add Schedules of this day.
                             */
                            perDay.add(new ScheduleData("4", "IT 113")); // add to each day
                        }
                    }
                    data.put(day.toUpperCase(), perDay);
                });

                return true;
            }

            @Override
            protected void after() {

            }
        }

        /**
         * Transaction.
         */
        ScheduleViewerStartUp schedViewTx = new ScheduleViewerStartUp();
        schedViewTx.whenSuccess(() -> {
            schedViewTx.data.keySet().forEach(key -> {
                System.out.println(key);
            });
            TimeTableController controller = new TimeTableController(schedViewTx.data);
            Stage s = Mono.fx().create()
                    .setPackageName("update3.org.cict.scheduling")
                    .setFxmlDocument("TimeTableController")
                    .makeFX()
                    .setController(controller)
                    .makeScene()
                    .makeStageApplication()
                    .stageTitle("Schedule Viewer")
                    .pullOutStage();

            s.setWidth(1200.0);

            /**
             * Fixed Height.
             */
            s.heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                s.setHeight(714.0);
            });
            s.show();
        });

        schedViewTx.transact();

    }

}

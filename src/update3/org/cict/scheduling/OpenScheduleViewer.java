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
import app.lazy.models.SubjectMapping;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import org.cict.authentication.authenticator.SystemProperties;

/**
 *
 * @author Jhon Melvin
 */
public class OpenScheduleViewer {

    class ScheduleViewerStartUp extends Transaction {

        HashMap<String, ArrayList<ScheduleData>> data = new HashMap<>();
        private LoadSectionMapping load_sec;

        public void setLoad_sec(LoadSectionMapping load_sec) {
            this.load_sec = load_sec;
        }

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
                ArrayList<LoadGroupScheduleMapping> schedToProcess = new ArrayList<>();
                for (LoadGroupScheduleMapping daySched : loadSchedules) {
                    /**
                     * Check this day.
                     */
                    if (daySched.getClass_day().equalsIgnoreCase(day)) {
                        /**
                         * Add Schedules of this day.
                         */
                        schedToProcess.add(daySched);
                    }
                }
                /**
                 * Process per day to become proper format.
                 */

                data.put(day.toUpperCase(), buildSchedule(schedToProcess));
            });

            return true;
        }

        @Override
        protected void after() {

        }
    }

    public static void openScheduleViewer(LoadSectionMapping load_sec, String term, String sectionName) {

        /**
         * Transaction.
         */
        OpenScheduleViewer.ScheduleViewerStartUp schedViewTx = new OpenScheduleViewer().new ScheduleViewerStartUp();
        schedViewTx.setLoad_sec(load_sec);
        schedViewTx.whenSuccess(() -> {

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

            controller.setTerm(term);
            controller.setSection(sectionName);
            controller.addPrintEvents();

            s.setWidth(1300.0);
            double resizablePadding = 0.0; // if resizable is false
            s.setHeight(714.0 + resizablePadding);

            /**
             * Fixed Height.
             */
            s.heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                s.setHeight(714.0);
            });
            s.showAndWait();
        });

        schedViewTx.transact();

    }

    private static ArrayList<ScheduleData> buildSchedule(ArrayList<LoadGroupScheduleMapping> schedules) {
        schedules.sort((LoadGroupScheduleMapping o1, LoadGroupScheduleMapping o2) -> {
            Double o1_start = ScheduleChecker.doubleConverter(o1.getClass_start());
            Double o2_start = ScheduleChecker.doubleConverter(o2.getClass_start());
            // ascending order depending on class start time.
            return o1_start.compareTo(o2_start);
        });
        //
        ArrayList<ScheduleData> perDay = new ArrayList<>();

        for (int start = 0; start < schedules.size(); start++) {
            LoadGroupScheduleMapping current = schedules.get(start);
            if (start == 0) {
                // first schedule is not 7:00 AM
                if (ScheduleChecker.doubleConverter(current.getClass_start()) != 7.00) {
                    Integer initpad = timeArithmetic("7:00", current.getClass_start());
                    perDay.add(new ScheduleData(initpad.toString(), true));
                    //System.err.println(current.getClass_start());
                }
            }
            Integer space = timeArithmetic(current.getClass_start(), current.getClass_end());
            Integer subjectID = Database.connect()
                    .load_group()
                    .<LoadGroupMapping>getPrimary(current.getLoad_group_id())
                    .getSUBJECT_id();

            SubjectMapping sm = Database.connect().subject().getPrimary(subjectID);

            perDay.add(new ScheduleData(space.toString(), sm.getCode() + " - " + current.getClass_room()));

            try {
                LoadGroupScheduleMapping next = schedules.get(start + 1);
                Integer postPad = timeArithmetic(current.getClass_end(), next.getClass_start());
                if (postPad != 0) {
                    perDay.add(new ScheduleData(postPad.toString(), true));
                }
            } catch (IndexOutOfBoundsException e) {
                //
            }
        }

        return perDay;
    }

    /**
     * expecting from to be divisible by 0.15
     *
     * @param from
     * @param to
     * @return
     */
    private static Integer timeArithmetic(String from, String to) {
        int f = toSeconds(ScheduleChecker.doubleConverter(from));
        int t = toSeconds(ScheduleChecker.doubleConverter(to));

        int dif = t - f;

        return (dif / 15);
    }

    private static int toSeconds(double val) {

        int a = (int) val;
        double remainder = (int) ((val - ((double) a)) * 100);
        int inSeconds = (int) ((a * 60) + remainder);
        return inSeconds;
    }

}

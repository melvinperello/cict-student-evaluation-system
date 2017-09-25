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
package update3.org.cict;

import com.jhmvin.flow.MonoLoop;
import com.jhmvin.fx.controls.MonoText;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Jhon Melvin
 */
public class ScheduleConstants {

    /**
     * Days of the week.
     */
    public final static String MONDAY = "MONDAY";
    public final static String TUESDAY = "TUESDAY";
    public final static String WEDNESDAY = "WEDNESDAY";
    public final static String THURSDAY = "THURSDAY";
    public final static String FRIDAY = "FRIDAY";
    public final static String SATURDAY = "SATURDAY";
    public final static String SUNDAY = "SUNDAY";

    public static ObservableList<String> getDayList() {
        ObservableList<String> days = FXCollections.observableArrayList();
        days.add(MONDAY);
        days.add(TUESDAY);
        days.add(WEDNESDAY);
        days.add(THURSDAY);
        days.add(FRIDAY);
        days.add(SATURDAY);
        days.add(SUNDAY);
        return days;
    }

    public static ArrayList<String> getTimeLapse() {
        ArrayList<String> timeTable = new ArrayList<>();
        MonoLoop.range(7, 20, 1, hour -> {
            MonoLoop.range(0, 45, 15, minute -> {
                if (hour.isLast()) {
                    minute.end();
                }
                String hourString = (MonoText.lead(2, "0", hour.getValue().toString()));
                String minString = (MonoText.lead(2, "0", minute.getValue().toString()));
                String timeString = (hourString + ":" + minString);
                timeTable.add(timeString);
            });
        });
        return timeTable;
    }

}

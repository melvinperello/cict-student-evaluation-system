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

import com.jhmvin.flow.MonoLoop;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import update3.org.cict.ScheduleConstants;

/**
 *
 * @author Jhon Melvin
 */
public class TimeTableController extends SceneFX implements ControllerFX {
    
    @FXML
    private StackPane application_root;
    
    @FXML
    private HBox hbox_table;
    
    public TimeTableController() {
    }
    
    private final Double LINE_DISTANCE = 14.00;
    private final Double LINE_INTERVAL = 04.00;
    private Double UNIT;
    
    @Override
    public void onInitialization() {
        this.UNIT = LINE_DISTANCE * LINE_INTERVAL;
        
        hbox_table.heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            Double height = (Double) newValue;
            Double single_unit = height / this.UNIT;
            
            hbox_table.getChildren().forEach(column -> {
                if (column instanceof VBox) {
                    VBox col = (VBox) column;
                    col.getChildren().forEach(row -> {
                        HBox r = (HBox) row;
                        Integer multipler = (Integer) r.getUserData();
                        r.setPrefHeight(single_unit * multipler);
                    });
                }
            });
        });
        
        MonoLoop.range(1, 8, 1, loop -> {
            VBox column = new VBox();
            HBox.setHgrow(column, Priority.ALWAYS);
            column.getStyleClass().add("column-day");
            hbox_table.getChildren().add(column);
            
            HBox header = new HBox();
            header.getStyleClass().add("row");
            header.setMaxWidth(Double.MAX_VALUE);
            Integer s = 4;
            header.setUserData(s);
            Label head = new Label(getTableHeader().get(loop.getValue() - 1));
            header.getChildren().add(head);
            
            column.getChildren().add(header);
            
            AtomicInteger counter = new AtomicInteger(3);
            ScheduleConstants.getTimeLapsePretty().forEach(action -> {
                if (counter.get() != 3) {
                    counter.incrementAndGet();
                    return;
                }
                HBox row = new HBox();
                row.getStyleClass().add("row");
                row.setMaxWidth(Double.MAX_VALUE);
                Integer multiplier = 4;
                row.setUserData(multiplier);
                Label time = new Label(action);
                row.getChildren().add(time);
                column.getChildren().add(row);
                
                counter.set(0);
                
            });
            
        });
        
    }
    
    private ArrayList<String> getTableHeader() {
        ArrayList<String> headerText = new ArrayList<>();
        headerText.add("Time");
        headerText.addAll(ScheduleConstants.getDayList());
        return headerText;
    }
    
    @Override
    public void onEventHandling() {
        
    }
    
}

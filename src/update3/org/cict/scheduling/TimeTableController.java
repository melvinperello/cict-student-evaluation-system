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

import com.jhmvin.Mono;
import com.jhmvin.flow.MonoLoop;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import java.util.ArrayList;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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

    @FXML
    private HBox hbox_over;

    public TimeTableController() {
    }

    private final Double LINE_DISTANCE = 14.00; // the distance from  start and ending
    private final Double LINE_INTERVAL = 04.00; // multiplier 
    private Double UNIT; // individual units sizes.

    @Override
    public void onInitialization() {
        super.bindScene(application_root);
        this.UNIT = LINE_DISTANCE * LINE_INTERVAL;
        this.tableBelowListener();
        this.tableOverListener();
        
        

        /**
         * Create columns from time to Sunday.
         */
        MonoLoop.range(1, 8, 1, loop -> {
            // column
            VBox column = new VBox();
            HBox.setHgrow(column, Priority.ALWAYS);
            if (loop.getValue().equals(1)) {
                column.getStyleClass().add("column-template-lefter");
            } else {
                column.getStyleClass().add("column-template");
            }
            hbox_table.getChildren().add(column);

            // column header
            TimeTableCell cellHeader = new TimeTableCell();
            cellHeader.pane.getStyleClass().add("row-template-header");
            cellHeader.pane.setMaxWidth(Double.MAX_VALUE);
            cellHeader.pane.setUserData(Integer.valueOf("4"));
            cellHeader.content.setText(getTableHeader().get(loop.getValue() - 1));

            // add header to column
            column.getChildren().add(cellHeader.pane);

            /**
             * Create time stamps faded.
             */
            timeTemplates(column, loop.getValue());

            /**
             * create Rows over.
             */
            this.createOverRows(column, loop.getValue());

        });

    }

    private ArrayList<String> getTableHeader() {
        ArrayList<String> headerText = new ArrayList<>();
        headerText.add("Time");
        headerText.addAll(ScheduleConstants.getDayList());
        return headerText;
    }

    private void timeTemplates(Pane column, Integer loop_value) {
        ArrayList<String> timeLapse = ScheduleConstants.getTimeLapsePretty();
        for (Integer start = 0; start < timeLapse.size(); start += 4) {
            TimeTableCell cell = new TimeTableCell();
            cell.pane.getStyleClass().add("row-template");
            Integer multiplier = 4;
            cell.pane.setUserData(multiplier);
            try {
                cell.content.setText(timeLapse.get(start) + " - " + timeLapse.get(start + 4));
                if (!loop_value.equals(1)) {
                    cell.content.getStyleClass().add("row-template-text");
                }
                column.getChildren().add(cell.pane);
            } catch (Exception e) {
            }

        }
    }

    private void tableBelowListener() {
        /**
         * Add listener for vertical resizing.
         */
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
    }

    private void tableOverListener() {
        hbox_over.heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            Double height = (Double) newValue;
            Double single_unit = height / this.UNIT;

            hbox_over.getChildren().forEach(column -> {
                if (column instanceof ColumnFiller) {
                    ColumnFiller col = (ColumnFiller) column;
                    col.getChildren().forEach(row -> {
                        HBox r = (HBox) row;
                        Integer multipler = (Integer) r.getUserData();
                        r.setPrefHeight(single_unit * multipler);
                    });
                }
            });
        });
    }

    /**
     * Create rows over the time template.
     *
     * @param column
     * @param loop_val
     */
    private void createOverRows(Pane column, Integer loop_val) {
        // create column filler
        ColumnFiller cf = new ColumnFiller(column);

        switch (loop_val) {
            case 1:
                // time frames
                break;
            case 2:
                // monday schedule
                createTimeSchedules(cf,"12","IT 113 - IT 08");
                createTimeSchedules(cf,"10","IT 253 - IT 09");
                break;

            default:
                // default
                break;
        }

        // add column filler.
        this.hbox_over.getChildren().add(cf);
    }

    private void createTimeSchedules(ColumnFiller cf, String span, String text) {
        TimeTableCell sss = new TimeTableCell();
        sss.pane.setMaxWidth(Double.MAX_VALUE);
        sss.pane.setUserData(Integer.valueOf(span));
        sss.pane.getStyleClass().add("row-filler");
        sss.content.getStyleClass().add("row-filler-text");
        sss.content.setText(text);

        cf.getChildren().add(sss.pane);
    }

    @Override
    public void onEventHandling() {

    }

    //--------------------------------------------------------------------------
    /**
     * Class for individual time table cell.
     */
    private class TimeTableCell extends SceneFX {

        private HBox pane;
        private Label content;

        public TimeTableCell() {
            pane = Mono.fx().create()
                    .setPackageName("update3.org.cict.scheduling")
                    .setFxmlDocument("TableCell")
                    .makeFX()
                    .pullOutLayout();

            content = super.searchAccessibilityText(pane, "lbl_content");
        }

    }

    /**
     * class for a filler column between in the over HBOX.
     */
    private class ColumnFiller extends VBox {

        public ColumnFiller(Pane parent) {
            HBox.setHgrow(this, Priority.ALWAYS);
            //this.getStyleClass().add("col-filler");
            this.prefWidthProperty().bind(parent.widthProperty());

            // add one above
            TimeTableCell cellHeader = new TimeTableCell();
            cellHeader.pane.setMaxWidth(Double.MAX_VALUE);
            cellHeader.pane.setUserData(Integer.valueOf("4"));
            cellHeader.content.setText("");
            this.getChildren().add(cellHeader.pane);
        }

    }

}

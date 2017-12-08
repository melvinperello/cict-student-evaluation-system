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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javax.imageio.ImageIO;
import org.cict.reports.ReportsUtility;
import update3.org.cict.scheduling.printing.ImageToPdf;

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
    private ArrayList<String> colors;

    public TimeTableController(HashMap<String, ArrayList<ScheduleData>> schedule) {
        // pass schedule information in constructor
        this.schedule = schedule;
        this.colors = new ArrayList<>();
        this.colors.add("#ccff99");
        this.colors.add("#ff99ff");
        this.colors.add("#33ccff");
        this.colors.add("#33cccc");
        this.colors.add("#99ccff");
        this.colors.add("#ff99cc");
        this.colors.add("#ccff99");
        this.colors.add("#ccff99");
        this.colors.add("#00ff99");
        this.colors.add("#00ffcc");
        this.colors.add("#ffccff");
        this.colors.add("#66ccff");
        this.colors.add("#ffffcc");
        this.colors.add("#ccccff");
        this.colors.add("#ccff66");
    }

    private HashMap<String, String> colorScheme;
    private HashMap<String, ArrayList<ScheduleData>> schedule;

    private final Double LINE_DISTANCE = 14.00; // must be even // the distance from  start and ending 20 - 7 + 1 header
    private final Double LINE_INTERVAL = 04.00; // multiplier 4 = 1hr 1 = 15mins
    private Double UNIT; // individual units sizes.

    @Override
    public void onInitialization() {
        super.bindScene(application_root);
        this.UNIT = LINE_DISTANCE * LINE_INTERVAL;
        // create listeners.
        this.tableBelowListener();
        this.tableOverListener();

        colorScheme = new HashMap<>();
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
            column.getChildren().add(cellHeader.pane);

            //------------------------------------------------------------------
            /**
             * Create time stamps faded.
             */
            timeTemplates(column, loop.getValue());

            /**
             * create Rows over.
             */
            this.createOverRows(column, loop.getValue(), cellHeader.pane);

        });

    }

    /**
     * Table headers.
     *
     * @return
     */
    private ArrayList<String> getTableHeader() {
        ArrayList<String> headerText = new ArrayList<>();
        headerText.add("Time");
        headerText.addAll(ScheduleConstants.getDayList());
        return headerText;
    }

    /**
     * Fill in time values of template.
     *
     * @param column
     * @param loop_value
     */
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

    /**
     * The template table.
     */
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

    /**
     * The real table..
     */
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
                        r.setPrefHeight((single_unit * multipler));
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
    private void createOverRows(Pane column, Integer loop_val, Pane header) {
        // create column filler
        ColumnFiller cf = new ColumnFiller(column, header);

        switch (loop_val) {
            case 1:
                // time frames
                break;
            case 2:
                // sunday schedule
                displaySchedules(cf, ScheduleConstants.SUNDAY);

                break;
            case 3:
                // monday schedule
                displaySchedules(cf, ScheduleConstants.MONDAY);

                break;
            case 4:
                // tuesday schedule
                displaySchedules(cf, ScheduleConstants.TUESDAY);
                break;
            case 5:
                // wednesday schedule
                displaySchedules(cf, ScheduleConstants.WEDNESDAY);
                break;
            case 6:
                // thursday schedule
                displaySchedules(cf, ScheduleConstants.THURSDAY);
                break;
            case 7:
                // friday schedule
                displaySchedules(cf, ScheduleConstants.FRIDAY);
                break;
            case 8:
                // saturday schedule
                displaySchedules(cf, ScheduleConstants.SATURDAY);
                break;
            default:
                // default
                break;
        }

        // add column filler.
        this.hbox_over.getChildren().add(cf);
    }

    private void displaySchedules(ColumnFiller cf, String day) {
        ArrayList<ScheduleData> sched = this.schedule.getOrDefault(day, null);
        if (sched != null) {
            for (ScheduleData scheduleData : sched) {
                if (scheduleData.isFillable()) {
                    fillerTimeSchedules(cf, scheduleData.getSpace());
                } else {
                    createTimeSchedules(cf, scheduleData.getSpace(), scheduleData.getText());
                }
            }
        }
    }

    /**
     * Create Time schedules.
     *
     * @param cf
     * @param span
     * @param text
     */
    private void createTimeSchedules(ColumnFiller cf, String span, String text) {
        TimeTableCell sss = new TimeTableCell();
        sss.pane.setMaxWidth(Double.MAX_VALUE);
        sss.pane.setUserData(Integer.valueOf(span));
        sss.pane.getStyleClass().add("row-filler");
        //----------------------------------------------------------------------
        String currentStyle = sss.pane.getStyle();
        // check if exist in color
        String color = this.colorScheme.getOrDefault(text, null);
        if (color == null) {
            // there is no color associated with this value
            // select a color
            for (String hexColor : this.colors) {
                boolean taken = false;
                for (String colorValue : this.colorScheme.values()) {
                    if (hexColor.equals(colorValue)) {
                        // already taken
                        taken = true;
                        break; // proceed to next color
                    }
                }
                if (!taken) {
                    this.colorScheme.put(text, hexColor);
                    break;
                }
            }
        }
        color = this.colorScheme.getOrDefault(text, null);
        if (color == null) {
            color = "#C0C0C0";
        }
        sss.pane.setStyle(currentStyle + "-fx-background-color: " + color + ";");

        //----------------------------------------------------------------------
        sss.content.getStyleClass().add("row-filler-text");
        sss.content.setText(text);
        cf.getChildren().add(sss.pane);
    }

    /**
     * Create empty rows for filler and spaces in between of schedules.
     *
     * @param cf
     * @param span
     */
    private void fillerTimeSchedules(ColumnFiller cf, String span) {
        TimeTableCell sss = new TimeTableCell();
        sss.pane.setMaxWidth(Double.MAX_VALUE);
        sss.pane.setUserData(Integer.valueOf(span));
        sss.content.setText("");
        cf.getChildren().add(sss.pane);
    }

    private final KeyCombination printKey = new KeyCodeCombination(KeyCode.P,
            KeyCombination.CONTROL_DOWN);

    //
    @Override
    public void onEventHandling() {

    }

    @FXML
    private Label lbl_semester;

    @FXML
    private Label lbl_section;

    public void setTerm(String term) {
        this.lbl_semester.setText(term);
    }

    public void setSection(String section) {
        this.lbl_section.setText(section);
    }

    public void addPrintEvents() {
        super.getScene().addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (printKey.match(event)) {
                System.out.println("CTRL P");
                this.takeSnapShot(super.getScene());
            }
        });

        super.getScene().addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (event.isControlDown() && event.getCode().equals(KeyCode.S)) {
                System.out.println("CTRL S");
                this.takeSnapShot(super.getScene());
            }
        });

        super.getScene().addEventHandler(KeyEvent.KEY_RELEASED, key -> {
            if (key.getCode().equals(KeyCode.PRINTSCREEN)) {
                System.out.println("PRINT SCREEN");
                this.takeSnapShot(super.getScene());
            }
        });

        super.getScene().addEventHandler(KeyEvent.KEY_RELEASED, key -> {
            if (key.getCode().equals(KeyCode.F12)) {
                System.out.println("F12");
                this.takeSnapShot(super.getScene());
            }
        });
    }

    /**
     * SnapShot Event.
     *
     * @param scene
     */
    private void takeSnapShot(Scene scene) {
        System.out.println("SNAPSHOT");
        WritableImage writableImage
                = new WritableImage((int) scene.getWidth(), (int) scene.getHeight());
        scene.snapshot(writableImage);
        String time_stamp_name = this.lbl_semester.getText()
                + " "
                + this.lbl_section.getText()
                + " " + String.valueOf(Calendar.getInstance().getTimeInMillis());
        // get user directory
        String homePath = System.getProperty("user.home");
        // get desktop directory
        String desktop = "\\Desktop";
        String fullDesktopPath = homePath + desktop;
        File file = new File(fullDesktopPath + "\\" + time_stamp_name + ".png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
            Mono.fx().snackbar().showInfo(application_root, "Schedule was saved to your Desktop");
            System.out.println("snapshot saved: " + file.getAbsolutePath());

            printSchedule(file.getAbsolutePath(), time_stamp_name);
        } catch (IOException ex) {
            Mono.fx().snackbar().showError(application_root, "Cannot Generate Schedule.");
        }
    }

    /**
     * Print Schedule.
     *
     * @param imageLocation
     * @param pdfName
     */
    private void printSchedule(String imageLocation, String pdfName) {
        ImageToPdf imageViewer = new ImageToPdf(pdfName);
        imageViewer.imageLocation = imageLocation;
        ReportsUtility.rotate = true;
        imageViewer.setDocumentFormat(ReportsUtility.paperSizeChooser(this.getStage()));
        imageViewer.print();
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

        public ColumnFiller(Pane parent, Pane header) {
            //HBox.setHgrow(super, Priority.ALWAYS);
            //this.getStyleClass().add("col-filler");
            //-
            super.prefWidthProperty().bind(parent.widthProperty());

            // add one above
            TimeTableCell cellHeader = new TimeTableCell();
            cellHeader.pane.setMaxWidth(Double.MAX_VALUE);

            cellHeader.pane.setUserData(Integer.valueOf("4"));

            //cellHeader.pane.getStyleClass().add("fuck");
            cellHeader.content.setText("");
            super.getChildren().add(cellHeader.pane);
            //-
            //cellHeader.pane.prefHeightProperty().bind(header.heightProperty());
        }

    }

}

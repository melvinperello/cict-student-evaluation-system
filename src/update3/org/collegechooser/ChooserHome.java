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
package update3.org.collegechooser;

import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.controls.SimpleImage;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import update3.org.cict.controller.sectionmain.SectionHomeController;

/**
 *
 * @author Jhon Melvin
 */
public class ChooserHome extends SceneFX implements ControllerFX {

    @FXML
    private VBox application_root;

    @FXML
    private VBox vbox_college_table;

    @FXML
    private JFXButton btn_cancel;

    public ChooserHome() {
        //
    }

    private String selected;

    public String getSelected() {
        return selected;
    }

    @Override
    public void onInitialization() {
        super.bindScene(application_root);
        application_root.requestFocus();
        
        SimpleTable tblColleges = new SimpleTable();
        for (String[] strings : SectionHomeController.COLLEGE_LIST) {
            SimpleTableRow row = new SimpleTableRow();
            row.setRowHeight(100.0);

            HBox collegeRow = Mono.fx().create()
                    .setPackageName("update3.org.collegechooser")
                    .setFxmlDocument("row-college")
                    .makeFX()
                    .pullOutLayout();

            Label lbl_code = super.searchAccessibilityText(collegeRow, "lbl_code");
            Label lbl_name = super.searchAccessibilityText(collegeRow, "lbl_name");
            ImageView img_icom = super.searchAccessibilityText(collegeRow, "img_icon");

            lbl_code.setText(strings[0]);
            lbl_name.setText(strings[1]);
            img_icom.setImage(SimpleImage.make("update3.org.collegechooser.college_icons", strings[2]));

            super.addClickEvent(collegeRow, () -> {
                this.selected = strings[0];
                super.finish();
            });

            SimpleTableCell cellParent = new SimpleTableCell();
            cellParent.setResizePriority(Priority.ALWAYS);
            cellParent.setContentAsPane(collegeRow);

            row.addCell(cellParent);
            // add to table
            tblColleges.addRow(row);

        }
        // table view
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(tblColleges);
        simpleTableView.setFixedWidth(true);
        // attach to parent variable name in scene builder
        simpleTableView.setParentOnScene(vbox_college_table);

    }

    @Override
    public void onEventHandling() {
        super.addClickEvent(btn_cancel, () -> {
            this.selected = "CANCEL";
            super.finish();
        });
    }

}

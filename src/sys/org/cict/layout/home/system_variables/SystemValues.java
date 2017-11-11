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
 * JOEMAR N. DE LA CRUZ
 * GRETHEL EINSTEIN BERNARDINO
 *
 * OTHER LIBRARIES THAT ARE USED BELONGS TO THEIR RESPECTFUL OWNERS AND AUTHORS.
 * NO COPYRIGHT ARE INTENTIONAL OR INTENDED.
 * THIS PROJECT IS NOT PROFITABLE HENCE FOR EDUCATIONAL PURPOSES ONLY.
 * THIS PROJECT IS ONLY FOR COMPLIANCE TO OUR REQUIREMENTS.
 * THIS PROJECT DOES NOT INCLUDE DISTRIBUTION FOR OTHER PURPOSES.
 *
 */
package sys.org.cict.layout.home.system_variables;

import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.SystemVariablesMapping;
import artifacts.MonoString;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.controls.SimpleImage;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import com.jhmvin.transitions.Animate;
import com.melvin.mono.fx.MonoLauncher;
import com.melvin.mono.fx.bootstrap.M;
import com.melvin.mono.fx.events.MonoClick;
import java.util.ArrayList;
import java.util.Date;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.text.WordUtils;
import org.cict.PublicConstants;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.controlsfx.control.Notifications;
import org.hibernate.criterion.Order;
import update3.org.cict.access.Access;

/**
 *
 * @author Joemar
 */
public class SystemValues extends MonoLauncher {

    @FXML
    private VBox application_root;

    @FXML
    private VBox vbox_preview;

    @FXML
    private VBox vbox_table_holder;

    @FXML
    private JFXButton btn_new_field;

    @FXML
    private JFXButton btn_cancel;

    @FXML
    private VBox vbox_new;

    @FXML
    private TextField txt_name;

    @FXML
    private TextField txt_value;

    @FXML
    private JFXButton btn_add_new;

    @FXML
    private JFXButton btn_cancel1;
    
    private boolean isGranted;
    @Override
    public void onStartUp() {
        isGranted = Access.isGrantedIf(Access.ACCESS_ADMIN, Access.ACCESS_ASST_ADMIN);
        
        MonoClick.addClickEvent(btn_cancel, ()->{
            Mono.fx().getParentStage(btn_cancel).close();
        });
        MonoClick.addClickEvent(btn_cancel1, ()->{
            this.changeView(vbox_preview);
        });
        MonoClick.addClickEvent(btn_new_field, ()->{
            if(!isGranted) {
                Mono.fx().snackbar().showInfo(application_root, "Access Denied.");
                return;
            }
            this.changeView(vbox_new);
        });
    }

    @Override
    public void onDelayedStart() {
        this.fetchAndLoad();
        
        MonoClick.addClickEvent(btn_add_new, ()->{
            this.save();
        });
        
        super.onDelayedStart(); //To change body of generated methods, choose Tools | Templates.
    }
    
    private Integer FACULTY_ID = CollegeFaculty.instance().getFACULTY_ID();
    private Date SERVER_DATETIME = Mono.orm().getServerTime().getDateWithFormat();
    private void save() {
        if(this.checkIfEmpty(txt_name, txt_value))
            return;
        String name = MonoString.removeExtraSpace(txt_name.getText());
        String value = MonoString.removeExtraSpace(txt_value.getText());
        if(name.equalsIgnoreCase(PublicConstants.MAX_POPULATION_NAME.replace("_", " "))
                || name.equalsIgnoreCase(PublicConstants.FTP_PORT.replace("_", " "))) {
            try {
                Integer check = Integer.valueOf(value);
            } catch (NumberFormatException e) {
                Mono.fx().alert().createWarning()
                        .setMessage("Field Value requires only numeric characters.")
                        .show();
                return;
            }
        }
        SystemVariablesMapping addedVar = new SystemVariablesMapping();
        addedVar.setActive(1);
        addedVar.setCreated_by(FACULTY_ID);
        addedVar.setCreated_date(SERVER_DATETIME);
        addedVar.setName(name.toUpperCase().replace(" ", "_"));
        addedVar.setValue(value.toUpperCase());
        Integer newID = Database.connect().system_variables().insert(addedVar);
        if(!newID.equals(-1)) {
            Notifications.create().darkStyle()
                    .text("Added new system variable.")
                    .showInformation();
            this.fetchAndLoad();
            this.changeView(vbox_preview);
        } else {
            Notifications.create().darkStyle()
                    .text("Something went wrong."
                            + "\nSorry for the inconvinience.")
                    .showError();
        }
    }
    
    private boolean checkIfEmpty(TextField txt_name, TextField txt_value) {
        String name = MonoString.removeExtraSpace(txt_name.getText());
        if(name.isEmpty()) {
            Mono.fx().alert().createWarning()
                    .setMessage("Name cannot be empty.")
                    .show();
            return true;
        }
        String value = MonoString.removeExtraSpace(txt_value.getText());
        if(value.isEmpty()) {
            Mono.fx().alert().createWarning()
                    .setMessage("Value cannot be empty.")
                    .show();
            return true;
        }
        return false;
    }
    
    private void fetchAndLoad() {
        ArrayList<SystemVariablesMapping> variables = Mono.orm().newSearch(Database.connect().system_variables())
                .active(Order.asc(DB.system_variables().id)).all();
        if(variables==null) {
            System.out.println("NO VALUES FOUND");
            return;
        }
        this.createTable(variables);
    }
    
    private SimpleTable systemVarTable = new SimpleTable();
    private void createTable(ArrayList<SystemVariablesMapping> collection) {
        systemVarTable.getChildren().clear();
        for(SystemVariablesMapping each: collection) {
            this.createRow(each);
        }
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(systemVarTable);
        simpleTableView.setFixedWidth(true);
        simpleTableView.setParentOnScene(vbox_table_holder);
        this.changeView(vbox_preview);
    }
    
    private void createRow(SystemVariablesMapping each) {
        SimpleTableRow row = new SimpleTableRow();
        row.setRowHeight(50.0);
        SystemValuesRow rowFX = M.load(SystemValuesRow.class);
        ImageView img_extend = rowFX.getImg_extension();
        Label lbl_name = rowFX.getTxt_name();
        lbl_name.setText(WordUtils.capitalizeFully(each.getName().replace("_", " ")));
        
        row.getRowMetaData().put("MAP", each);
        row.getRowMetaData().put("FX", rowFX);
        this.createExtension(systemVarTable, row, img_extend, each);
        // create cell container
        SimpleTableCell cellParent = new SimpleTableCell();
        cellParent.setResizePriority(Priority.ALWAYS);
        cellParent.setContent(rowFX.getApplicationRoot());
        
        // add cell to row
        row.addCell(cellParent);
        systemVarTable.addRow(row);
    }
    
    private void createExtension(SimpleTable programsTable, SimpleTableRow row, ImageView img_extension, SystemVariablesMapping each) {
        SystemValuesRowExtension ext = M.load(SystemValuesRowExtension.class);
        TextField txt_name1 = ext.getTxt_name();
        TextField txt_value1 = ext.getTxt_value(); 
        JFXButton btn_add_new1 = ext.getBtn_add_new(); 
        JFXButton btn_remove = ext.getBtn_remove(); 
        
        txt_name1.setText(each.getName().replace("_", " "));
        txt_value1.setText(each.getValue());
        
        txt_name1.setDisable(true);
        txt_value1.setDisable(!isGranted);
        btn_add_new1.setDisable(!isGranted);
        
        MonoClick.addClickEvent(btn_add_new1, ()->{
            if(this.checkIfEmpty(txt_name1, txt_value1))
                return;
            String name = MonoString.removeExtraSpace(txt_name1.getText());
            String value = MonoString.removeExtraSpace(txt_value1.getText());
            this.update(row, name, value);
        });
        
        MonoClick.addClickEvent(btn_remove, ()->{
            SystemVariablesMapping sys = (SystemVariablesMapping) row.getRowMetaData().get("MAP");
            int res = Mono.fx().alert().createConfirmation()
                    .setMessage("Are you sure you want to remove " + sys.getName().replace("_", " ") +
                            " from the list?")
                    .confirmYesNo();
            if(res==1) {
                sys.setActive(0);
                if(Database.connect().system_variables().update(sys)) {
                    Notifications.create().darkStyle()
                            .text("Removed successfully.")
                            .showInformation();
                    this.systemVarTable.getChildren().remove(row);
                }
            }
        });
        
        MonoClick.addClickEvent(img_extension, ()->{
            if (row.isExtensionShown()) {
                img_extension.setImage(SimpleImage.make("sys.org.cict.layout.home.images.system_values", "hide_extension.png"));
                row.hideExtension();
            } else {
                // close all row extension
                for (Node tableRows : programsTable.getRows()) {
                    SimpleTableRow simplerow = (SimpleTableRow) tableRows;
                    SimpleTableCell simplecell = simplerow.getCell(0);
                    SystemValuesRow rowFX_ = (SystemValuesRow) simplerow.getRowMetaData().get("FX");
                    ImageView simplerowimage = rowFX_.getImg_extension(); //findByAccessibilityText(simplerowcontent, "img_row_extension");
                    
                    simplerowimage.setImage(SimpleImage.make("sys.org.cict.layout.home.images.system_values", "hide_extension.png"));
                    simplerow.hideExtension();
                }

                // show row extension
                img_extension.setImage(SimpleImage.make("sys.org.cict.layout.home.images.system_values", "show_extension.png"));
                row.setRowExtension(ext.getApplicationRoot());
                row.showExtension();
            }
        });
        
        MonoClick.addDoubleClickEvent(row, ()->{
            if (row.isExtensionShown()) {
                img_extension.setImage(SimpleImage.make("sys.org.cict.layout.home.images.system_values", "hide_extension.png"));
                row.hideExtension();
            } else {
                // close all row extension
                for (Node tableRows : programsTable.getRows()) {
                    SimpleTableRow simplerow = (SimpleTableRow) tableRows;
                    SimpleTableCell simplecell = simplerow.getCell(0);
                    SystemValuesRow rowFX_ = (SystemValuesRow) simplerow.getRowMetaData().get("FX");
                    ImageView simplerowimage = rowFX_.getImg_extension(); //findByAccessibilityText(simplerowcontent, "img_row_extension");
                    
                    simplerowimage.setImage(SimpleImage.make("sys.org.cict.layout.home.images.system_values", "hide_extension.png"));
                    simplerow.hideExtension();
                }

                // show row extension
                img_extension.setImage(SimpleImage.make("sys.org.cict.layout.home.images.system_values", "show_extension.png"));
                row.setRowExtension(ext.getApplicationRoot());
                row.showExtension();
            }
        });
        
        //----------------------------
        btn_remove.setVisible(false);
        //-----------------------
    }
    
    private void update(SimpleTableRow row, String name, String value) {
        if(name.equalsIgnoreCase(PublicConstants.MAX_POPULATION_NAME.replace("_", " "))
                || name.equalsIgnoreCase(PublicConstants.FTP_PORT.replace("_", " "))) {
            try {
                Integer check = Integer.valueOf(value);
            } catch (NumberFormatException e) {
                Mono.fx().alert().createWarning()
                        .setMessage("Field Value requires only numeric characters.")
                        .show();
                return;
            }
        }
        SystemVariablesMapping updateThis = (SystemVariablesMapping) row.getRowMetaData().get("MAP");
        updateThis.setName(name.toUpperCase().replace(" ", "_"));
        updateThis.setValue(value.toUpperCase());
        updateThis.setUpdated_by(FACULTY_ID);
        updateThis.setUpdated_date(SERVER_DATETIME);
        if(Database.connect().system_variables().update(updateThis)) {
            Notifications.create().darkStyle()
                    .text("Updated Successfully.")
                    .showInformation();
        }
        this.fetchAndLoad();
    }
    
    private void changeView(Node node) {
        Animate.fade(node, 150, ()->{
            vbox_new.setVisible(false);
            vbox_preview.setVisible(false);
            node.setVisible(true);
        }, vbox_new, vbox_preview);
    }
}

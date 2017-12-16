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
package org.cict.reports;

import com.itextpdf.text.Document;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import com.melvin.mono.fx.MonoLauncher;
import com.melvin.mono.fx.bootstrap.M;
import com.melvin.mono.fx.events.MonoClick;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author Joemar
 */
public class PaperSizeChooserWithCustomize extends MonoLauncher{

    @FXML
    private VBox application_root;

    @FXML
    private JFXButton btn_short;

    @FXML
    private JFXButton btn_long;

    @FXML
    private JFXButton btn_a4;
    
    @FXML
    private VBox vbox_table_col;
    
    @FXML
    private JFXButton btn_cancel;
     
    private String[] COLUMN_NAMES = new String[]{};
    private String[] COLUMN_Descriptions = new String[]{};

    public void setCOLUMN_NAMES(String[] COLUMN_NAMES) {
        this.COLUMN_NAMES = COLUMN_NAMES;
    }

    public void setCOLUMN_Descriptions(String[] COLUMN_Descriptions) {
        this.COLUMN_Descriptions = COLUMN_Descriptions;
    }
    
            
    @Override
    public void onStartUp() {
        MonoClick.addClickEvent(btn_short, ()->{
            this.saveValues(ReportsUtility.createShortDocument());
        });
        MonoClick.addClickEvent(btn_long, ()->{
            this.saveValues(ReportsUtility.createLongDocument());
        });
        MonoClick.addClickEvent(btn_a4, ()->{
            this.saveValues(ReportsUtility.createA4Document());
        });
        MonoClick.addClickEvent(btn_cancel, ()->{
            this.choosenDetails.add(null);
            Mono.fx().getParentStage(application_root).close();
        });
    }

    @Override
    public void onDelayedStart() {
        
        // creates table of column names 
        this.createCustomizeTable();
        
        super.onDelayedStart(); //To change body of generated methods, choose Tools | Templates.
    }
    
    private ArrayList<Object> choosenDetails = new ArrayList<>();
    public ArrayList<Object> getChoosenSize() {
        return choosenDetails;
    }
    
    private HashMap<Integer, Object[]> RESULT = new HashMap<>();

    public HashMap<Integer, Object[]> getCustomizedColDetails() {
        return RESULT;
    }
    
    private void changeSelectedValues(Integer columnIndex, Boolean isChecked, String newColName) {
        RESULT.remove(columnIndex);
        // first index if show col, second is the new column name
        RESULT.put(columnIndex, new Object[] {isChecked, newColName});
        System.out.println("CHANGED " + isChecked + " | " + newColName);
    }
    
    private void createCustomizeTable() {
        System.out.println("COLUMN_NAMES " + this.COLUMN_NAMES.length);
        SimpleTable logsTable = new SimpleTable();
        for (int i = 0; i < this.COLUMN_NAMES.length; i++) {
            String colName = COLUMN_NAMES[i];
            System.out.println(colName);
            String colDescription = COLUMN_Descriptions[i];
            SimpleTableRow row = new SimpleTableRow();
            row.setRowHeight(60.0);
            
            // set default values
            RESULT.put(i, new Object[] {true, colName});
        
            ColCustomizeRow rowFX = M.load(ColCustomizeRow.class);
            rowFX.getTxt_col_name().setText(colName);
            rowFX.getLbl_description().setText(colDescription);
            rowFX.getLbl_col_number().setText((i+1) + "");
            row.getRowMetaData().put("FX", rowFX);
            
            rowFX.getTxt_col_name().textProperty().addListener((a)->{
                ColCustomizeRow FX = (ColCustomizeRow) row.getRowMetaData().get("FX");
                String newColName = FX.getTxt_col_name().getText();
                Integer colIndex = Integer.valueOf(FX.getLbl_col_number().getText());
                this.changeSelectedValues(colIndex-1, FX.getChkbx_selected().isSelected(), newColName);
            });
            
            rowFX.getChkbx_selected().setSelected(true);
            
            rowFX.getChkbx_selected().selectedProperty().addListener((a)->{
                ColCustomizeRow FX = (ColCustomizeRow) row.getRowMetaData().get("FX");
                String newColName = FX.getTxt_col_name().getText();
                Integer colIndex = Integer.valueOf(FX.getLbl_col_number().getText());
                this.changeSelectedValues(colIndex-1, FX.getChkbx_selected().isSelected(), newColName);
            });
            
            MonoClick.addClickEvent(row, ()->{
                ColCustomizeRow FX = (ColCustomizeRow) row.getRowMetaData().get("FX");
                String newColName = FX.getTxt_col_name().getText();
                Integer colIndex = Integer.valueOf(FX.getLbl_col_number().getText());
                this.changeSelectedValues(colIndex-1, FX.getChkbx_selected().isSelected(), newColName);
            });
            
            SimpleTableCell cellParent = new SimpleTableCell();
            cellParent.setResizePriority(Priority.ALWAYS);
            cellParent.setContent(rowFX.getApplicationRoot());

            row.addCell(cellParent);
            logsTable.addRow(row);
        }
        
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(logsTable);
        simpleTableView.setFixedWidth(true);

        simpleTableView.setParentOnScene(vbox_table_col);
    }
    
    private void saveValues(Document doc) {
        this.choosenDetails.clear();
        this.choosenDetails.add(doc);
        this.choosenDetails.add(RESULT);
        Mono.fx().getParentStage(application_root).close();
    }
}

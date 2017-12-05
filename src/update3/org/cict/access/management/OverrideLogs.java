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
package update3.org.cict.access.management;

import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.SystemOverrideLogsMapping;
import app.lazy.models.utils.FacultyUtility;
import artifacts.FTPManager;
import com.jfoenix.controls.JFXButton;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.SimpleTask;
import com.jhmvin.fx.async.Transaction;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import com.jhmvin.transitions.Animate;
import com.melvin.mono.fx.MonoLauncher;
import com.melvin.mono.fx.bootstrap.M;
import com.melvin.mono.fx.events.MonoClick;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.cict.PublicConstants;
import org.cict.reports.ReportsUtility;
import org.cict.reports.result.PrintResult;
import org.controlsfx.control.Notifications;
import org.hibernate.criterion.Order;
import update3.org.cict.ChoiceRange;
import update3.org.cict.access.Access;

/**
 *
 * @author Joemar
 */
public class OverrideLogs extends MonoLauncher {

    @FXML
    private VBox applicationn_root;

    @FXML
    private JFXButton btn_close;

    @FXML
    private ComboBox<String> cmb_from;

    @FXML
    private ComboBox<String> cmb_to;

    @FXML
    private JFXButton btn_print_filtered;

    @FXML
    private JFXButton btn_filter;

    @FXML
    private VBox vbox_local_reg_override_table;

    @FXML
    private VBox vbox_no_logs_found;

    
    @Override
    public void onStartUp() {
        MonoClick.addClickEvent(btn_close, ()->{
            Mono.fx().getParentStage(btn_close).close();
        });
    }

    private boolean cmbChanged = false;
    @Override
    public void onDelayedStart() {
        //-----------------------
        this.loadOverrideLogs(null, null);
        this.setComboBoxLimit(cmb_from, cmb_to, 0);
        this.setCmbValues();
        
        //----------------------------
        // EVENTS
        //------------------------
        MonoClick.addClickEvent(btn_print_filtered, ()->{
            this.printResult();
        });
        
        MonoClick.addClickEvent(btn_filter, ()->{
            this.filterResult();
        });
        
        cmb_from.valueProperty().addListener((a)->{
            cmbChanged = true;
        });
        cmb_to.valueProperty().addListener((a)->{
            cmbChanged = true;
        });
        super.onDelayedStart(); //To change body of generated methods, choose Tools | Templates.
    
    }
    
    private ArrayList<String> dateList = new ArrayList<>();
    private void setComboBoxLimit(ComboBox<String> source, ComboBox<String> self, int extra, ComboBox<String>... padding) {
        ChoiceRange.setComboBoxLimit(dateList, source, self, 0, padding);
    }
    
    private ArrayList<HashMap<String,Date>> dateStorage = new ArrayList<>();
    private ArrayList<SystemOverrideLogsMapping> logs;
    private void loadOverrideLogs(Date from, Date to) {
        FetchOverrideLogs fetch = new FetchOverrideLogs();
        fetch.setFiltereDate(from, to);
        fetch.whenStarted(()->{
            vbox_no_logs_found.setVisible(false);
            vbox_local_reg_override_table.setVisible(false);
            cmb_from.setDisable(true);
            cmb_to.setDisable(true);
            btn_print_filtered.setDisable(true);
            btn_filter.setDisable(true);
        });
        
        fetch.whenFailed(()->{
            Notifications.create().darkStyle()
                    .text("Cannot load override logs at the moment.")
                    .title("Failed")
                    .showError();
        });
        
        fetch.whenSuccess(()->{
            SimpleTask create_table_override_logs = new SimpleTask("create_table_override_logs");
            create_table_override_logs.setTask(()->{
                logs = fetch.getResults();
                preview = logs;
                if(logs==null) {
                    Animate.fade(vbox_local_reg_override_table, 150, ()->{
                        vbox_local_reg_override_table.setVisible(false);
                        vbox_no_logs_found.setVisible(true);
                    }, vbox_no_logs_found);
                    return;
                }
                this.createTableOverrideLogs(logs);
            });
            create_table_override_logs.whenCancelled(()->{
                Notifications.create().darkStyle()
                        .text("Loading of override logs cancelled.")
                        .title("Information")
                        .showInformation();
            });
            create_table_override_logs.whenFailed(()->{
                Notifications.create().darkStyle()
                        .text("Loading of override logs failed.")
                        .title("Error")
                        .showError();
            });
            create_table_override_logs.whenSuccess(()->{
                cmb_from.setDisable(false); 
                cmb_to.setDisable(false);
                btn_print_filtered.setDisable(false);
                btn_filter.setDisable(false);
            });
            create_table_override_logs.start();
        });
        fetch.transact();
    }
    
    class FetchOverrideLogs extends Transaction {

        
        private ArrayList<SystemOverrideLogsMapping> results;
        public ArrayList<SystemOverrideLogsMapping> getResults() {
            return results;
        }
        
        private Date FROM =null, TO = null;
        public void setFiltereDate(Date from, Date to) {
            FROM = from;
            TO = to;
        }
        
        @Override
        protected boolean transaction() {
            if(FROM != null && TO != null) {
                results = Mono.orm().newSearch(Database.connect().system_override_logs())
        //                .between(DB.system_override_logs().executed_date, from, to)
                        .gte(DB.system_override_logs().executed_date, FROM)
                        .lte(DB.system_override_logs().executed_date, TO)
                        .active(Order.asc(DB.system_override_logs().executed_date)).all();
                return true;
            }
            results = Mono.orm().newSearch(Database.connect().system_override_logs())
                    .active(Order.asc(DB.system_override_logs().executed_date)).all();
            return true;
        }
        
    }
    
    private SimpleDateFormat formatter_sql = new SimpleDateFormat(PublicConstants.SQL_DATETIME_FORMAT);
    private SimpleDateFormat formatter_plain = new SimpleDateFormat("yyyy-MM-dd");
    
    private void setCmbValues(){
        List<Date> dates = new ArrayList<>();
        try {
            SystemOverrideLogsMapping start = Mono.orm().newSearch(Database.connect().system_override_logs())
                    .active(Order.asc(DB.system_override_logs().executed_date)).first();
            SystemOverrideLogsMapping end = Mono.orm().newSearch(Database.connect().system_override_logs())
                    .active(Order.desc(DB.system_override_logs().executed_date)).first();
            Date startDate = formatter_plain.parse(start.getExecuted_date().toString());
            Date endDate = formatter_plain.parse(end.getExecuted_date().toString()); //DateUtils.addDays(formatter_plain.parse(end.getExecuted_date().toString()), 1);
        
            long interval = 24*1000 * 60 * 60; // 1 hour in millis
            long endTime = endDate.getTime(); // create your endtime here, possibly using Calendar or Date
            long curTime = startDate.getTime();
            while (curTime <= endTime) {
                dates.add(new Date(curTime));
                curTime += interval;
            }
            for(int i=0;i<dates.size();i++){
                Date lDate = dates.get(i);
                String ds = formatter_display.format(lDate); 
                dateList.add(ds);
                HashMap<String,Date> nameDate = new HashMap<>();
                nameDate.put(ds, dates.get(i));
                System.out.println("'"+ds + "' - " + nameDate.get(ds));
                dateStorage.add(nameDate);
            }
            
            cmb_from.getItems().addAll(dateList); 
            cmb_to.getItems().addAll(dateList);
            cmb_from.getSelectionModel().selectFirst();
            cmb_to.getSelectionModel().selectLast();
            cmbChanged = false;
        } catch (Exception ex) {
//            ex.printStackTrace();
//            Logger.getLogger(OverrideLogs.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    //----------------------------------------------
    // OVERRIDE LOGS TABLE CREATOR
    //---------------------------------------
    private SimpleDateFormat formatter_display = new SimpleDateFormat("MMMM dd, yyyy");
    private SimpleDateFormat formatter_file_name = new SimpleDateFormat("MMddyyyyhhmm");
    private final String KEY_MORE_INFO = "MORE_INFO";
    private ArrayList<SystemOverrideLogsMapping> preview = new ArrayList<>();
    private void createTableOverrideLogs(ArrayList<SystemOverrideLogsMapping> logs) {
        
        if(logs==null || logs.isEmpty()) {
            vbox_local_reg_override_table.setVisible(false);
            vbox_no_logs_found.setVisible(false);
            Animate.fade(vbox_local_reg_override_table, 150, ()->{
                vbox_local_reg_override_table.setVisible(false);
                vbox_no_logs_found.setVisible(true);
            }, vbox_no_logs_found);
            return;
        }
        SimpleTable tblFaculty = new SimpleTable();
        for(SystemOverrideLogsMapping log: logs) {
            SimpleTableRow row = new SimpleTableRow();
            row.setRowHeight(90.0);
            OverrideLogsRow rowFX = M.load(OverrideLogsRow.class);
            rowFX.getBtn_download().setDisable(!Access.isGrantedIf(Access.ACCESS_ADMIN, Access.ACCESS_ASST_ADMIN, Access.ACCESS_LOCAL_REGISTRAR, Access.ACCESS_CO_REGISTRAR));
            rowFX.getBtn_download().setDisable(log.getAttachment_file()==null || log.getAttachment_file().isEmpty());
            MonoClick.addClickEvent(rowFX.getBtn_download(), ()->{
               this.downloadAttachment(row);
            });
            rowFX.getLbl_category().setText(WordUtils.capitalizeFully(log.getCategory().replace("_", " ")));
            rowFX.getLbl_conforme().setText(WordUtils.capitalizeFully(log.getConforme()));
            rowFX.getLbl_conforme_type().setText(WordUtils.capitalizeFully(log.getConforme_type().replace("_", " ")));
            rowFX.getLbl_date().setText(formatter_display.format(log.getExecuted_date()));
            rowFX.getLbl_description().setText(WordUtils.capitalizeFully(log.getDescription().replace("_", " ")));
            rowFX.getLbl_executed_by().setText(WordUtils.capitalizeFully(FacultyUtility.getFacultyName(FacultyUtility.getFaculty(log.getExecuted_by()))));
        
            row.getRowMetaData().put(KEY_MORE_INFO, log);
            SimpleTableCell cellParent = new SimpleTableCell();
            cellParent.setResizePriority(Priority.ALWAYS);
            cellParent.setContentAsPane(rowFX.getApplicationRoot());

            row.addCell(cellParent);
            tblFaculty.addRow(row);
        }

        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(tblFaculty);
        simpleTableView.setFixedWidth(true);
        Mono.fx().thread().wrap(()->{
            vbox_local_reg_override_table.getChildren().clear();
            simpleTableView.setParentOnScene(vbox_local_reg_override_table);
            Animate.fade(vbox_no_logs_found, 200, ()->{
                vbox_no_logs_found.setVisible(false);
                vbox_local_reg_override_table.setVisible(true);
            }, vbox_local_reg_override_table);
        });
    }
    
    private void downloadAttachment(SimpleTableRow row) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = 
                directoryChooser.showDialog(this.getCurrentStage());
        SystemOverrideLogsMapping selected = (SystemOverrideLogsMapping) row.getRowMetaData().get(KEY_MORE_INFO);
        if(selectedDirectory == null){
            System.out.println("No Directory selected");
        } else{
            String dateTimeToday = formatter_file_name.format(Mono.orm().getServerTime().getDateWithFormat());
            String fileDownloadPath = selectedDirectory.getAbsolutePath() + "\\downloaded_attachment_file_" + dateTimeToday + ".rar";
            if(selected.getAttachment_file()== null || selected.getAttachment_file().isEmpty()) {
                Notifications.create().darkStyle()
                        .title("Nothing to Download")
                        .text("No attachment found.")
                        .showError();
                return;
            }
            //-----------------------------
            // NON BLOCKING DOWNLOAD
            FTPManager.download("override_attachment", selected.getAttachment_file(), fileDownloadPath, (boolean result, Exception e) -> {
                if(result) {
                    Mono.fx().thread().wrap(()->{
                        Notifications.create().darkStyle()
                                .title("Downloaded Successfully")
                                .text("Attachment file is downloaded.")
                                .showInformation();
                    });
                } else {
                    Mono.fx().thread().wrap(()->{
                        Notifications.create().darkStyle()
                                .title("Failed to Download")
                                .text("Attachment not found.")
                                .showError();
                    });
                }
            });
//                boolean downloaded = FTPManager.download("override_attachment", selected.getAttachment_file(), fileDownloadPath);
//                if(downloaded) {
//                    Notifications.create().darkStyle()
//                            .title("Downloaded Successfully")
//                            .text("Attachment file is downloaded.")
//                            .showInformation();
//                } else {
//                    Notifications.create().darkStyle()
//                            .title("Failed to Download")
//                            .text("Attachment not found.")
//                            .showError();
//                }
        }
    }
    
    private void filterResult(){
        cmbChanged = false;
        String from_str = cmb_from.getSelectionModel().getSelectedItem();
        String to_str =cmb_to.getSelectionModel().getSelectedItem();
        vbox_local_reg_override_table.getChildren().clear();
        Date from = null, to = null;
        try {  
            int count = 0;
            for(int i=0; i<dateList.size(); i++) {
                String each = dateList.get(i);
                if(from_str.equalsIgnoreCase(each)) {
                    from = dateStorage.get(i).get(from_str);
                    count++;
                }
                if(to_str.equalsIgnoreCase(each)) {
                    to = dateStorage.get(i).get(to_str);
                    to = DateUtils.addDays(to,1);
                    count++;
                }
                if(count==2) {
                    break;
                }
            }
            
            System.out.println("FROM: " + from + " | " + from_str);
            System.out.println("TO: " + to + " | " + to_str);
        } catch (Exception e) {
        }
        
        if(from==null || to==null) {
            Animate.fade(vbox_local_reg_override_table, 150, ()->{
                vbox_local_reg_override_table.setVisible(false);
                vbox_no_logs_found.setVisible(true);
            }, vbox_no_logs_found);
            preview = null;
            return;
        }
        this.loadOverrideLogs(from, to);
    }
    
    //-------------------------------------
    // PRINTING
    //---------------------------
    private void printResult() {
        if(cmbChanged) {
            int res =  Mono.fx().alert()
                    .createConfirmation().setHeader("Not Yet Filtered")
                    .setMessage("The result to be printed are not yet filtered with the given dates above. Do you still want to print?")
                    .confirmYesNo();
            if(res==-1)
                return;
        }
        if(preview==null) {
            Notifications.create()
                    .title("No Result")
                    .text("No result found to print.")
                    .showWarning();
            return;
        }
        String[] colNames = new String[]{"Date","Faculty Name","Description","Category","Conforme"};
        ArrayList<String[]> rowData = new ArrayList<>();
        SystemOverrideLogsMapping ref = null;
        for (int i = 0; i < preview.size(); i++) {
            SystemOverrideLogsMapping result = preview.get(i);
            ref = result;
            String[] row = new String[]{(i+1)+".  "+ ReportsUtility.formatter_mmmm.format(result.getExecuted_date()),
                WordUtils.capitalizeFully(FacultyUtility.getFacultyName(FacultyUtility.getFaculty(result.getExecuted_by()))),
            WordUtils.capitalizeFully(result.getDescription().replace("_", " ")), WordUtils.capitalizeFully(result.getCategory().replace("_", " ")), WordUtils.capitalizeFully(result.getConforme())};
            rowData.add(row);
        }
        SimpleDateFormat month = new SimpleDateFormat("MMMMM");
        PrintResult print = new PrintResult();
        print.columnNames = colNames;
        print.ROW_DETAILS = rowData;
        print.fileName = "system_override_logs";
        String fr = cmb_from.getSelectionModel().getSelectedItem();
        String to = cmb_to.getSelectionModel().getSelectedItem();
        if(cmbChanged) {
            print.reportTitleIntro = null;
        } else if(fr==null || to==null || ref==null || ref.getExecuted_date()==null) {
            print.reportOtherDetail = "As of " + formatter_display.format(Mono.orm().getServerTime().getDateWithFormat());
        } else if(fr.equalsIgnoreCase(to)) {
            print.reportOtherDetail = formatter_display.format(ref.getExecuted_date());
         } else
            print.reportOtherDetail = "From " + fr + " to " + to;
        print.reportTitleHeader = "System Override Logs";
        print.whenStarted(() -> {
            btn_print_filtered.setDisable(true);
            super.setCursor(Cursor.WAIT);
        });
        print.whenCancelled(() -> {
            Notifications.create()
                    .title("Request Cancelled")
                    .text("Sorry for the inconviniece.")
                    .showWarning();
        });
        print.whenFailed(() -> {
            Notifications.create()
                    .title("Request Failed")
                    .text("Something went wrong. Sorry for the inconviniece.")
                    .showInformation();
        });
        print.whenSuccess(() -> {
            btn_print_filtered.setDisable(false);
            Notifications.create()
                    .title("Printing Results")
                    .text("Please wait a moment.")
                    .showInformation();
        });
        print.whenFinished(() -> {
            btn_print_filtered.setDisable(false);
            super.setCursor(Cursor.DEFAULT);
        });
        //----------------------------------------------------------------------
        print.transact();
    }
}

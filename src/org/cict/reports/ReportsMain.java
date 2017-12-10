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

import app.lazy.models.AcademicProgramMapping;
import app.lazy.models.AcademicTermMapping;
import app.lazy.models.CurriculumMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.EvaluationMapping;
import app.lazy.models.FacultyMapping;
import app.lazy.models.PrintLogsMapping;
import app.lazy.models.StudentMapping;
import app.lazy.models.utils.FacultyUtility;
import artifacts.ListerData;
import artifacts.ListersChecker;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.SimpleTask;
import com.jhmvin.fx.async.Transaction;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.SceneFX;
import com.jhmvin.transitions.Animate;
import com.melvin.mono.fx.bootstrap.M;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.cict.PublicConstants;
import org.cict.accountmanager.faculty.FacultyMainController;
import org.cict.authentication.authenticator.SystemProperties;
import org.cict.reports.result.PrintResult;
import org.controlsfx.control.Notifications;
import org.hibernate.criterion.Order;
import sys.org.cict.layout.home.SystemHome;
import update3.org.cict.ChoiceRange;
import update3.org.cict.layout.default_loader.LoaderView;
import update3.org.cict.window_prompts.empty_prompt.EmptyView;
import update3.org.cict.window_prompts.fail_prompt.FailView;

/**
 *
 * @author Joemar
 */
public class ReportsMain extends SceneFX implements ControllerFX {
    
    @FXML
    private VBox application_root;

    @FXML
    private JFXButton btn_home;

    @FXML
    private JFXButton btn_evaluation;

    @FXML
    private JFXButton btn_adding_changing;

    @FXML
    private JFXButton btn_pres_main;

    @FXML
    private JFXButton btn_dean_main;

    @FXML
    private VBox vbox_eval_main;

    @FXML
    private ImageView img_icon_eval_main;

    @FXML
    private Label lbl_title_eval;

    @FXML
    private ComboBox<String> cmb_sort_status_eval;

    @FXML
    private Label lbl_subtitle;

    @FXML
    private ComboBox<AcademicTermMapping> cmb_term_eval;

    @FXML
    private ComboBox<String> cmb_from_eval_main;

    @FXML
    private ComboBox<String> cmb_to_eval_main;

    @FXML
    private ComboBox<String> cmb_type_eval_main;

    @FXML
    private JFXButton btn_filter_eval_main;

    @FXML
    private JFXButton btn_print_eval_main;

    @FXML
    private Label lbl_result;

    @FXML
    private StackPane stack_eval_main;

    @FXML
    private VBox vbox_eval_main_table;

    @FXML
    private VBox vbox_pres_main;

//    @FXML
//    private ImageView img_icon_pres_main;

    @FXML
    private Label lbl_title_pres;

    @FXML
    private Label lbl_subtitle_pres;

    @FXML
    private ComboBox<CurriculumMapping> cmb_curriculum_lister;
//
//    @FXML
//    private JFXButton btn_filter_pres_main;

    @FXML
    private JFXButton btn_print_pres_main;

    @FXML
    private Label lbl_result_pres;

    @FXML
    private StackPane stack_pres_main;

    @FXML
    private VBox vbox_pres_main_table;
    
    @FXML
    private ComboBox<String> cmb_eval_main_faculty_search_vis;
          
    @FXML
    private JFXTextField txt_faculty_search;
        
    @FXML
    private VBox vbox_print_logs_main;
           
    @FXML
    private StackPane stack_print_logs; 
            
    @FXML
    private VBox vbox_print_logs_table;
         
    @FXML
    private ComboBox<String> cmb_from_print_logs;
            
    @FXML
    private ComboBox<String> cmb_to_print_logs;
            
    @FXML
    private ComboBox<String> cmb_print_logs_faculty_search_vis1;
      
    @FXML
    private JFXTextField txt_faculty_search_print_logs;
            
    @FXML
    private JFXButton btn_filter_print_logs;
            
    @FXML
    private JFXButton btn_print_print_logs;
         

    @FXML
    private Label lbl_result_print_logs;   
      
    @FXML
    private JFXButton btn_print_logs;
            
    @FXML
    private ComboBox<String> cmb_print_logs_type;
            
            
    private LoaderView loaderView;
    private FailView failView;
    private EmptyView emptyView;
    
    private LoaderView loaderView2;
    private FailView failView2;
    private EmptyView emptyView2;
    
    private LoaderView loaderView3;
    private FailView failView3;
    private EmptyView emptyView3;
    @Override
    public void onInitialization() {
        super.bindScene(application_root);
        
        this.loaderView = new LoaderView(stack_eval_main);
        this.failView = new FailView(stack_eval_main);
        this.emptyView = new EmptyView(stack_eval_main);
        
        this.loaderView2 = new LoaderView(stack_pres_main);
        this.failView2 = new FailView(stack_pres_main);
        this.emptyView2 = new EmptyView(stack_pres_main);
        
        this.loaderView3 = new LoaderView(stack_print_logs);
        this.failView3 = new FailView(stack_print_logs);
        this.emptyView3 = new EmptyView(stack_print_logs);
        
        cmb_sort_status_eval.getItems().clear();
        cmb_sort_status_eval.getItems().add("Accepted");
        cmb_sort_status_eval.getItems().add("Revoked");
        cmb_sort_status_eval.getSelectionModel().selectFirst();
        
        cmb_type_eval_main.getItems().clear();
        cmb_type_eval_main.getItems().add("All");
        cmb_type_eval_main.getItems().add("Old");
        cmb_type_eval_main.getItems().add("New");
        cmb_type_eval_main.getItems().add("Regular");
        cmb_type_eval_main.getItems().add("Irregular");
        cmb_type_eval_main.getSelectionModel().selectFirst();
        
        cmb_eval_main_faculty_search_vis.getItems().clear();
        cmb_eval_main_faculty_search_vis.getItems().add("Name");
        cmb_eval_main_faculty_search_vis.getItems().add("BulSU ID");
        cmb_eval_main_faculty_search_vis.getSelectionModel().selectFirst();
        
        lbl_result.setText("");
        lbl_result_pres.setText("");
        
        this.setViewListers(btn_pres_main);
        
        this.setViewTask(btn_evaluation, SystemProperties.instance().getCurrentAcademicTerm()==null? null : SystemProperties.instance().getCurrentAcademicTerm().getId());
        this.changeView(vbox_eval_main);
        
        cmb_print_logs_faculty_search_vis1.getItems().clear();
        cmb_print_logs_faculty_search_vis1.getItems().add("Name");
        cmb_print_logs_faculty_search_vis1.getItems().add("BulSU ID");
        cmb_print_logs_faculty_search_vis1.getSelectionModel().selectFirst();
        
        cmb_print_logs_type.getItems().clear();
        cmb_print_logs_type.getItems().add("Initial");
        cmb_print_logs_type.getItems().add("Reprint");
        cmb_print_logs_type.getSelectionModel().selectFirst();
    }

    @Override
    public void onEventHandling() {
        super.addClickEvent(btn_home, ()->{
            SystemHome.callHome(this);
            this.detachAll();
        });
        
        super.addClickEvent(btn_evaluation, ()->{
            this.setViewTask(btn_evaluation, SystemProperties.instance().getCurrentAcademicTerm().getId());
            this.changeView(vbox_eval_main);
        });
        
        super.addClickEvent(btn_adding_changing, ()->{
            this.setViewTask(btn_adding_changing, SystemProperties.instance().getCurrentAcademicTerm().getId());
            this.changeView(vbox_eval_main);
        });
        
        super.addClickEvent(btn_pres_main, ()->{
            this.changeView(vbox_pres_main);
        });
        
        super.addClickEvent(btn_dean_main, ()->{
            this.changeView(vbox_pres_main);
        });
        
        this.evaluationEvents();
        
        super.addClickEvent(btn_pres_main, ()->{
            this.setViewListers(btn_pres_main);
            this.changeView(vbox_pres_main);
        });
        
        super.addClickEvent(btn_dean_main, ()->{
            this.setViewListers(btn_dean_main);
            this.changeView(vbox_pres_main);
        });
        
//        super.addClickEvent(btn_filter_pres_main, ()->{
//            this.fetchAchievers();
//        });
        
        cmb_curriculum_lister.valueProperty().addListener((a)->{
            this.fetchAchievers();
        });
        
//        cmb_year_level_pres.valueProperty().addListener((a)->{
//            this.fetchAchievers();
//        });
        
        super.addClickEvent(btn_print_pres_main, ()->{
            this.printAchievers();
        });
        
        super.addClickEvent(btn_print_logs, ()->{
            this.changeView(vbox_print_logs_main);
        });
        
        super.addClickEvent(btn_print_logs, ()->{
            this.setViewInPrintLogs();
        });
        
        super.addClickEvent(btn_filter_print_logs, ()->{
            cmbChanged = false;
            this.onSearchFaculty(txt_faculty_search_print_logs.getText(), cmb_print_logs_faculty_search_vis1.getSelectionModel().getSelectedItem());
        });
        
        this.printLogsEvents();
    }
    
    private ArrayList<String> dateList = new ArrayList<>();
    private void setComboBoxLimit(ComboBox<String> source, ComboBox<String> self, int extra, ComboBox<String>... padding) {
        ChoiceRange.setComboBoxLimit(dateList, source, self, 0, padding);
    }
    
//    private ArrayList<HashMap<String,AcademicTermMapping>> termDetails = new ArrayList<>();
    private void setViewTask(JFXButton button, Integer ACADTERM_id) {
        txt_faculty_search.setText("");
        cmb_eval_main_faculty_search_vis.getSelectionModel().selectFirst();
        vbox_eval_main_table.getChildren().clear();
        MODE = button.getText();
        lbl_subtitle.setText(MODE.equalsIgnoreCase(EVALUATION)? "List of every evaluation transaction." : "List of every adding and changing transaction.");
        lbl_title_eval.setText(MODE.equalsIgnoreCase(EVALUATION)? "Successful Evaluation Summary" : "Successful Adding & Changing Summary");
        
        cmb_term_eval.getItems().clear();
        cmb_sort_status_eval.getSelectionModel().selectFirst();
        cmb_type_eval_main.getSelectionModel().selectFirst();
        SimpleTask set = new SimpleTask("set_reports_value_eval");
        set.setTask(()->{
            ArrayList<AcademicTermMapping> atMaps = Mono.orm().newSearch(Database.connect().academic_term())
                    .eq(DB.academic_term().approval_state, "ACCEPTED")
                    .active(Order.desc(DB.academic_term().school_year)).all();
            if(atMaps==null) {
                Mono.fx().thread().wrap(()->{
                    vbox_eval_main.setDisable(true);
                });
                return;
            }
//            termDetails.clear();
//            for(AcademicTermMapping each: atMaps) {
//                String termName = (each.getSchool_year()==null? "No School Year" : each.getSchool_year()) + " " + (each.getSemester()==null? "No Semester" : each.getSemester());
//                Mono.fx().thread().wrap(()->{
//                    cmb_term_eval.getItems().add(termName);
//                });
                
//                HashMap<String,AcademicTermMapping> hm = new HashMap<>();
//                hm.put(termName, each);
//                termDetails.add(hm);
//            }
            //SystemProperties.instance().getCurrentAcademicTerm();
//            String termName = (currentATMap.getSchool_year()==null? "No School Year" : currentATMap.getSchool_year()) + " " + (currentATMap.getSemester()==null? "No Semester" : currentATMap.getSemester());

            Mono.fx().thread().wrap(()->{
                
                Callback<ListView<AcademicTermMapping>, ListCell<AcademicTermMapping>> factory = lv -> {
                    return new ListCell<AcademicTermMapping>() {
                        @Override
                        protected void updateItem(AcademicTermMapping item, boolean empty) {
                            super.updateItem(item, empty);
                            setText(empty ? "" : (item.getSchool_year() + " " + item.getSemester()));
                        }
                    };
                };

                AcademicTermMapping currentATMap = null;

                if(ACADTERM_id==null) {
                    System.out.println("ITS NULL");
                    currentATMap = Database.connect().academic_term().getPrimary(ACADTERM_id);
                }
                this.cmb_term_eval.getItems().clear();
                this.cmb_term_eval.getItems().addAll(atMaps);
                this.cmb_term_eval.setCellFactory(factory);
                this.cmb_term_eval.setButtonCell(factory.call(null));
                if(currentATMap==null) {
                    this.cmb_term_eval.getSelectionModel().selectFirst();
                    return;
                }
                for(AcademicTermMapping atMap: atMaps) {
                    if(atMap.getId().equals(currentATMap.getId())) {
                        this.cmb_term_eval.getSelectionModel().select(atMap);
                        break;
                    }
                }
            });
        });
        set.whenCancelled(()->{
            System.out.println("CANCELLED");
        });
        set.whenFailed(()->{
            System.out.println("FAILED");
        });
        set.whenStarted(()->{
            System.out.println("STARTED");
//            if(MODE.equalsIgnoreCase("evaluation")) {
                vbox_eval_main.setDisable(true);
//            }
        });
        set.whenSuccess(()->{
            System.out.println("SUCCESS");
            vbox_eval_main.setDisable(false);
            this.setCmbValues(ACADTERM_id);
            this.setComboBoxLimit(cmb_from_eval_main, cmb_to_eval_main, 0);
            this.onSearchFaculty(txt_faculty_search.getText(), cmb_eval_main_faculty_search_vis.getSelectionModel().getSelectedItem());
        });
        set.start();
    }
    
    private SimpleDateFormat formatter_sql = new SimpleDateFormat(PublicConstants.SQL_DATETIME_FORMAT);
    private SimpleDateFormat formatter_filename = new SimpleDateFormat("MMddyyyhhmmss");
    private SimpleDateFormat formatter_display = new SimpleDateFormat("MMMM dd, yyyy");
    private SimpleDateFormat formatter_plain = new SimpleDateFormat("yyyy-MM-dd");
    public final static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

    
    private ArrayList<HashMap<String,Date>> dateStorage = new ArrayList<>();
    private void setCmbValues(Integer ACADTERM_id) {
        dateStorage.clear();
        dateList.clear();
        List<Date> dates = new ArrayList<Date>();
        EvaluationMapping start = Mono.orm().newSearch(Database.connect().evaluation())
                        .eq(DB.evaluation().ACADTERM_id, ACADTERM_id).execute(Order.asc(DB.evaluation().evaluation_date)).first();
        // Affected by the ORM Update - Changed the order -> desc to get last then use first()
        EvaluationMapping end = Mono.orm().newSearch(Database.connect().evaluation())
                        .eq(DB.evaluation().ACADTERM_id, ACADTERM_id).execute(Order.desc(DB.evaluation().evaluation_date)).first();
        try {
            if(end==null && start==null) {
                cmb_from_eval_main.setDisable(true);
                cmb_to_eval_main.setDisable(true);
                return;
            }
            cmb_from_eval_main.setDisable(false);
            cmb_to_eval_main.setDisable(false);
            Date endDate = formatter_plain.parse(end.getEvaluation_date().toString());//DateUtils.addDays(formatter_plain.parse(end.getEvaluation_date().toString()), 1);
            Date startDate = formatter_plain.parse(start.getEvaluation_date().toString());
        
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
            
            cmb_from_eval_main.getItems().clear();
            cmb_to_eval_main.getItems().clear();
            
            cmb_from_eval_main.getItems().addAll(dateList); 
            cmb_to_eval_main.getItems().addAll(dateList);
            
            cmb_from_eval_main.getSelectionModel().selectFirst();
            cmb_to_eval_main.getSelectionModel().selectLast();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private boolean cmbChanged = false;
    private String MODE;
    private AcademicTermMapping selected;
    private void evaluationEvents() {
         this.cmb_term_eval.valueProperty().addListener((ObservableValue<? extends AcademicTermMapping> observable, AcademicTermMapping oldValue, AcademicTermMapping newValue) -> {
//        cmb_term_eval.valueProperty().addListener((a)->{
//            String termName = cmb_term_eval.getSelectionModel().getSelectedItem();
//            int index = cmb_term_eval.getSelectionModel().getSelectedIndex();
            try {
                selected = newValue==null? SystemProperties.instance().getCurrentAcademicTerm() : newValue;
                System.out.println("SELECTED: " + selected.getId());
                this.setCmbValues(selected.getId());
                this.setComboBoxLimit(cmb_from_eval_main, cmb_to_eval_main, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        cmb_sort_status_eval.valueProperty().addListener((a)->{
            
            if(cmb_sort_status_eval.getSelectionModel().getSelectedIndex()==1) {
                lbl_title_eval.setText(MODE.equalsIgnoreCase(EVALUATION)? "Evaluation Misstatement Summary" : "Adding & Changing Misstatement Summary");
                // change vbox_table values
            } else {
                lbl_title_eval.setText(MODE.equalsIgnoreCase(EVALUATION)? "Successful Evaluation Summary" : "Successful Adding & Changing Summary");
                // change vbox_table values
            }
//            this.setCmbValues(selected==null? SystemProperties.instance().getCurrentAcademicTerm().getId() : selected.getId());
//            this.setComboBoxLimit(cmb_from_eval_main, cmb_to_eval_main, 0);
//            System.out.println("FILTER DETAILS********************");
//            String termName = cmb_term_eval.getSelectionModel().getSelectedItem().getSchool_year();
////            int index = cmb_term_eval.getSelectionModel().getSelectedIndex();
//            String status = cmb_sort_status_eval.getSelectionModel().getSelectedItem();
//            String from_str = cmb_from_eval_main.getSelectionModel().getSelectedItem();
//            String to_str = cmb_to_eval_main.getSelectionModel().getSelectedItem();
//            String type = cmb_type_eval_main.getSelectionModel().getSelectedItem();
//            
//            System.out.println("TERM: " + termName);
//            System.out.println("STATUS: " + status);
//            System.out.println("FROM: " + from_str);
//            System.out.println("TO: " + to_str);
//            System.out.println("TYPE: " + type);
//            System.out.println("MODE: " + MODE);
//            System.out.println("***************************");
            this.onSearchFaculty(txt_faculty_search.getText(), cmb_eval_main_faculty_search_vis.getSelectionModel().getSelectedItem());
        });
        super.addClickEvent(btn_filter_eval_main, ()->{
            cmbChanged = false;
            this.onSearchFaculty(txt_faculty_search.getText(), cmb_eval_main_faculty_search_vis.getSelectionModel().getSelectedItem());
        });
        super.addClickEvent(btn_print_eval_main, ()->{
            this.printResult(lbl_title_eval.getText());
        });
        cmb_from_eval_main.valueProperty().addListener((a)->{
            cmbChanged = true;
        });
        cmb_to_eval_main.valueProperty().addListener((a)->{
            cmbChanged = true;
        });
        cmb_term_eval.valueProperty().addListener((a)->{
            cmbChanged = true;
        });
        cmb_type_eval_main.valueProperty().addListener((a)->{
            cmbChanged = true;
        });
    }
    
    private void printResult(String title) {
        if(cmbChanged) {
            int res =  Mono.fx().alert()
                    .createConfirmation().setHeader("Not Yet Filtered")
                    .setMessage("The result to be printed are not yet filtered with the given dates above. Do you still want to print?")
                    .confirmYesNo();
            if(res==-1)
                return;
        }
        // select paper size
        String status = cmb_sort_status_eval.getSelectionModel().getSelectedItem();
        String[] colNames = new String[]{"Date and Time",status.equalsIgnoreCase("REVOKED")? "Revoked By" : "Evaluator", "Student Number", "Student Name", "Year Level"};
        ArrayList<String[]> rowData = new ArrayList<>();
        if(results==null || results.isEmpty()) {
            Mono.fx().snackbar().showError(application_root, "No Result To Print");
            btn_print_eval_main.setDisable(true);
            return;
        }
        EvaluationMapping ref = null;
        for (int i = 0; i < results.size(); i++) {
            EvaluationMapping result = results.get(i);
            ref = result;
            String[] row = new String[]{(i+1)+".  "+  (status.equalsIgnoreCase("REVOKED")? ReportsUtility.formatter_mm.format(result.getCancelled_date()) : ReportsUtility.formatter_mm.format(result.getEvaluation_date())),
                WordUtils.capitalizeFully(status.equalsIgnoreCase("REVOKED")? (FacultyUtility.getFacultyName(FacultyUtility.getFaculty(result.getCancelled_by()))) : (FacultyUtility.getFacultyName(FacultyUtility.getFaculty(result.getFACULTY_id())))), 
                this.getStudentNumber(result.getSTUDENT_id()), 
                WordUtils.capitalizeFully(this.getStudentName(result.getSTUDENT_id())), 
                (this.getYearLevel(result.getYear_level()))};
            rowData.add(row);
        }
        
        PrintResult print = new PrintResult();
        print.setDocumentFormat(ReportsUtility.paperSizeChooser(this.getStage()));
        print.columnNames = colNames;
        print.ROW_DETAILS = rowData;
        String dateToday = formatter_filename.format(Mono.orm().getServerTime().getDateWithFormat());
        print.fileName = title.replace(" ", "_").toLowerCase() + "_" + dateToday;
        String fr = cmb_from_eval_main.getSelectionModel().getSelectedItem();
        String to = cmb_to_eval_main.getSelectionModel().getSelectedItem();
        SimpleDateFormat month = new SimpleDateFormat("MMMMM");
        AcademicTermMapping selected = cmb_term_eval.getSelectionModel().getSelectedItem();
        print.reportTitleIntro = selected.getSchool_year() + " " + selected.getSemester();
        if(cmbChanged) {
//            print.reportDescription += "";
        } else if(fr==null || to==null || ref==null || ref.getEvaluation_date()==null) {
            print.reportOtherDetail = "As of " + formatter_display.format(Mono.orm().getServerTime().getDateWithFormat());
        } else if(fr.equalsIgnoreCase(to)) {
            print.reportOtherDetail = ""+ formatter_display.format(ref.getEvaluation_date());
         } else
            print.reportOtherDetail = "From " + fr + " to " + to;
        
        print.reportTitleHeader = lbl_title_eval.getText();
        print.whenStarted(() -> {
            btn_print_eval_main.setDisable(true);
            super.cursorWait();
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
            btn_print_eval_main.setDisable(false);
            Notifications.create()
                    .title("Printing Results")
                    .text("Please wait a moment.")
                    .showInformation();
        });
        print.whenFinished(() -> {
            btn_print_eval_main.setDisable(false);
            super.cursorDefault();
        });
        //----------------------------------------------------------------------
        if(ReportsUtility.savePrintLogs(null, lbl_title_eval.getText().toUpperCase(), "REPORTS", "INITIAL"))
            print.transact();
    }
    
    private String EVALUATION = "EVALUATION", ADD_CHANGE = "ADDING & CHANGING", PRES_LIST = "President's Lister", DEANS_LIST = "Dean's Lister", PRINT_LOGS = "Print Logs";
    private ArrayList<EvaluationMapping> results;
    private void fetchResult(JFXButton button, ArrayList<FacultyMapping> facultyRes) {
        System.out.println(MODE);
        btn_print_eval_main.setDisable(false);
        cmbChanged = false;
        FetchEvaluationResult fetch = new FetchEvaluationResult();
//        String termName = cmb_term_eval.getSelectionModel().getSelectedItem();
//        int index = cmb_term_eval.getSelectionModel().getSelectedIndex();
        selected = cmb_term_eval.getSelectionModel().getSelectedItem()==null? SystemProperties.instance().getCurrentAcademicTerm() : cmb_term_eval.getSelectionModel().getSelectedItem();
        fetch.ACADTERM_id = selected.getId();
        String status = cmb_sort_status_eval.getSelectionModel().getSelectedItem();
        
        String from_str = cmb_from_eval_main.getSelectionModel().getSelectedItem();
        String to_str = cmb_to_eval_main.getSelectionModel().getSelectedItem();
        Date from = null, to = null;
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
        fetch.from = from;
        fetch.mode = MODE;//button.getText();
        fetch.to = to;
        fetch.type = cmb_type_eval_main.getSelectionModel().getSelectedItem();
        fetch.status= cmb_sort_status_eval.getSelectionModel().getSelectedItem().toUpperCase();
        
        fetch.facultyResult = facultyRes;
        
        fetch.whenFailed(()->{
            System.out.println("FAILED");
            Notifications.create().darkStyle().title("Loading Failed")
                    .text("Please check your connection to the server.")
                    .showError();
        });
        fetch.whenStarted(()->{
            this.detachAll();
            this.loaderView.setMessage("Loading Evaluation Result");
            this.loaderView.attach();
            vbox_eval_main.setDisable(vbox_eval_main.isVisible());
        });
        fetch.whenSuccess(()->{
            vbox_eval_main.setDisable(false);
            System.out.println(fetch.getLog());
            results = fetch.getResults();
            if(results==null || results.isEmpty()) {
                lbl_result.setText("");
                this.emptyView.setMessage("No Result Found");
                this.emptyView.getButton().setVisible(false);
                this.emptyView.attach();
                vbox_eval_main_table.getChildren().clear();
                return;
            }
            lbl_result.setText(results==null? "" : "Total result found: " + results.size());
//            if(button.getText().equalsIgnoreCase(EVALUATION))
            this.createTable(results, vbox_eval_main_table);
        });
        fetch.whenFinished(()->{
            this.loaderView.detach();
        });
        
        fetch.transact();
    }
    
    private void createTable(ArrayList<EvaluationMapping> results, VBox holder) {
        holder.getChildren().clear();
        if(results==null)
            return;
        SimpleTable curriculumTable = new SimpleTable();
        curriculumTable.getChildren().clear();
        String status = cmb_sort_status_eval.getSelectionModel().getSelectedItem();
        for(EvaluationMapping each: results) {
            SimpleTableRow row = new SimpleTableRow();
            row.setRowHeight(70.0);
            ReportsRow rowFX = M.load(ReportsRow.class);
            rowFX.getLbl_date().setText(dateFormat.format(each.getEvaluation_date()));
            rowFX.getLbl_faculty().setText(WordUtils.capitalizeFully(FacultyUtility.getFacultyName(FacultyUtility.getFaculty(status.equalsIgnoreCase("REVOKED")? each.getCancelled_by() : each.getFACULTY_id()))));
            rowFX.getLbl_student_name().setText(WordUtils.capitalizeFully(this.getStudentName(each.getSTUDENT_id())));
            rowFX.getLbl_type().setText(each.getPrint_type().replace("_", " "));
            rowFX.getLbl_year_level().setText(this.getYearLevel(each.getYear_level()));
            
            row.getRowMetaData().put("MORE_INFO", each);

            SimpleTableCell cellParent = new SimpleTableCell();
            cellParent.setResizePriority(Priority.ALWAYS);
            cellParent.setContent(rowFX.getApplicationRoot());

            row.addCell(cellParent);
            curriculumTable.addRow(row);
        }
        
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(curriculumTable);
        simpleTableView.setFixedWidth(true);

        simpleTableView.setParentOnScene(holder);
    }
    
    private String getYearLevel(Integer a) {
        if(a==null)
            return "No Year Level";
        if(a.equals(1))
            return "First Year";
        else if(a.equals(2))
            return "Second Year";
        else if(a.equals(3))
            return "Third Year";
        else if(a.equals(4))
            return "Fourth Year";
        else 
            return "Not Specified";
    }
    
    private String getStudentName(Integer id) {
        StudentMapping student = Database.connect().student().getPrimary(id);
        return (student==null? "NO STUDENT FOUND" : student.getLast_name() + ", " + student.getFirst_name());
    }
    
    private String getStudentNumber(Integer id) {
        StudentMapping student = Database.connect().student().getPrimary(id);
        return (student==null? "NO STUDENT FOUND" : student.getId());
    }
    
    class FetchEvaluationResult extends Transaction {

        public String mode; // either eval, add&change or encoding
        public Integer ACADTERM_id;
        public String type, status;
        public Date from, to;
        public ArrayList<FacultyMapping> facultyResult;
        
        
        private ArrayList<EvaluationMapping> results;
        public ArrayList<EvaluationMapping> getResults() {
            return results;
        }
        
        private String log;
        public String getLog() {
            return log;
        }
        
        @Override
        protected boolean transaction() {
//            if(status.equalsIgnoreCase("ACCEPTED")) {
//                if(mode.equalsIgnoreCase(EVALUATION)) {
//                    if(type.equalsIgnoreCase("ALL")) {
//                        results = Mono.orm().newSearch(Database.connect().evaluation())
//                                .eq(DB.evaluation().ACADTERM_id, this.ACADTERM_id)
//                                .between(DB.evaluation().evaluation_date, from, to)
//                                .isNull(DB.evaluation().adding_reference_id)
//                                .active(Order.asc(DB.evaluation().evaluation_date)).all();
//                    } else {
//                        results = Mono.orm().newSearch(Database.connect().evaluation())
//                                .eq(DB.evaluation().ACADTERM_id, this.ACADTERM_id)
//                                .eq(DB.evaluation().print_type, type)
//                                .between(DB.evaluation().evaluation_date, from, to)
//                                .isNull(DB.evaluation().adding_reference_id)
//                                .active(Order.asc(DB.evaluation().evaluation_date)).all();
//                    }
//                }
//            } else if(status.equalsIgnoreCase("REVOKED")) {
                if(mode.equalsIgnoreCase(EVALUATION)) {
                    if(type.equalsIgnoreCase("ALL")) {
                        if(facultyResult==null || facultyResult.isEmpty()) {
                        } else {
                            System.out.println("HEREEE 2");
                            results = new ArrayList<>();
                            for(FacultyMapping faculty : facultyResult) {
                                ArrayList<EvaluationMapping> temp_results = Mono.orm().newSearch(Database.connect().evaluation())
                                        .eq(DB.evaluation().ACADTERM_id, this.ACADTERM_id)
                                        .eq(DB.evaluation().remarks, status)
                                        .between(DB.evaluation().evaluation_date, from, to)
                                        .isNull(DB.evaluation().adding_reference_id)
                                        .eq(status.equalsIgnoreCase("REVOKED")? DB.evaluation().cancelled_by : DB.evaluation().FACULTY_id, faculty.getId())
                                        .execute(Order.asc(DB.evaluation().evaluation_date)).all();
                                if(temp_results!=null) {
                                    System.out.println("temp_results : " + temp_results.size());
//                                    results.addAll(temp_results);
                                    outLoop:
                                    for(EvaluationMapping each : temp_results) {
                                        boolean exist = false;
                                        innerLoop:
                                        for(EvaluationMapping res : results) {
                                            if(each.getId().equals(res.getId())) {
                                                exist = true;
                                                break innerLoop;
                                            }
                                        }
                                        if(!exist) {
                                            results.add(each);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        System.out.println("HEREEE 3");
//                        results = Mono.orm().newSearch(Database.connect().evaluation())
//                                .eq(DB.evaluation().ACADTERM_id, this.ACADTERM_id)
//                                .eq(DB.evaluation().print_type, type)
//                                .eq(DB.evaluation().remarks, status)
//                                .between(DB.evaluation().evaluation_date, from, to)
//                                .isNull(DB.evaluation().adding_reference_id)
//                                .execute(Order.asc(DB.evaluation().evaluation_date)).all();
                    
                        if(facultyResult==null || facultyResult.isEmpty()) {
                        } else {
                            System.out.println("HEREEE 2");
                            results = new ArrayList<>();
                            for(FacultyMapping faculty : facultyResult) {
                                ArrayList<EvaluationMapping> temp_results = Mono.orm().newSearch(Database.connect().evaluation())
                                        .eq(DB.evaluation().ACADTERM_id, this.ACADTERM_id)
                                        .eq(DB.evaluation().print_type, type)
                                        .eq(DB.evaluation().remarks, status)
                                        .between(DB.evaluation().evaluation_date, from, to)
                                        .isNull(DB.evaluation().adding_reference_id)
                                        .eq(status.equalsIgnoreCase("REVOKED")? DB.evaluation().cancelled_by : DB.evaluation().FACULTY_id, faculty.getId())
                                        .execute(Order.asc(DB.evaluation().evaluation_date)).all();
                                if(temp_results!=null) {
                                    System.out.println("temp_results : " + temp_results.size());
//                                    results.addAll(temp_results);
                                    outLoop:
                                    for(EvaluationMapping each : temp_results) {
                                        boolean exist = false;
                                        innerLoop:
                                        for(EvaluationMapping res : results) {
                                            if(each.getId().equals(res.getId())) {
                                                exist = true;
                                                break innerLoop;
                                            }
                                        }
                                        if(!exist) {
                                            results.add(each);
                                        }
                                    }
                                }
                            }
                        }
                    
                    }
                } else if(mode.equalsIgnoreCase(ADD_CHANGE)) {
                    if(type.equalsIgnoreCase("ALL")) {
                        if(facultyResult==null || facultyResult.isEmpty()) {
                        } else {
                            results = new ArrayList<>();
                            for(FacultyMapping faculty : facultyResult) {
                                ArrayList<EvaluationMapping> temp_results = Mono.orm().newSearch(Database.connect().evaluation())
                                        .eq(DB.evaluation().ACADTERM_id, this.ACADTERM_id)
                                        .eq(DB.evaluation().type, "ADDING_CHANGING")
                                        .eq(DB.evaluation().remarks, (status.equalsIgnoreCase("REVOKED")? "REVOKED_ADD_CHANGE" : status))
                                        .between(DB.evaluation().evaluation_date, from, to)
                                        .notNull(DB.evaluation().adding_reference_id)
                                        .execute(Order.asc(DB.evaluation().evaluation_date)).all();
                                if(temp_results!=null) {
                                    System.out.println("temp_results : " + temp_results.size());
//                                    results.addAll(temp_results);
                                    outLoop:
                                    for(EvaluationMapping each : temp_results) {
                                        boolean exist = false;
                                        innerLoop:
                                        for(EvaluationMapping res : results) {
                                            if(each.getId().equals(res.getId())) {
                                                exist = true;
                                                break innerLoop;
                                            }
                                        }
                                        if(!exist) {
                                            results.add(each);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
//                        results = Mono.orm().newSearch(Database.connect().evaluation())
//                                .eq(DB.evaluation().ACADTERM_id, this.ACADTERM_id)
//                                .eq(DB.evaluation().print_type, type)
//                                .eq(DB.evaluation().type, "ADDING_CHANGING")
//                                .eq(DB.evaluation().remarks, (status.equalsIgnoreCase("REVOKED")? "REVOKED_ADD_CHANGE" : status))
//                                .between(status.equalsIgnoreCase("REVOKED")? DB.evaluation().cancelled_date : DB.evaluation().evaluation_date, from, to)
//                                .notNull(DB.evaluation().adding_reference_id)
//                                .execute(Order.asc(status.equalsIgnoreCase("REVOKED")? DB.evaluation().cancelled_date : DB.evaluation().evaluation_date)).all();
                    
                    
                        if(facultyResult==null || facultyResult.isEmpty()) {
                        } else {
                            results = new ArrayList<>();
                            for(FacultyMapping faculty : facultyResult) {
                                ArrayList<EvaluationMapping> temp_results = Mono.orm().newSearch(Database.connect().evaluation())
                                        .eq(DB.evaluation().ACADTERM_id, this.ACADTERM_id)
                                        .eq(DB.evaluation().print_type, type)
                                        .eq(DB.evaluation().type, "ADDING_CHANGING")
                                        .eq(DB.evaluation().remarks, (status.equalsIgnoreCase("REVOKED")? "REVOKED_ADD_CHANGE" : status))
                                        .between(DB.evaluation().evaluation_date, from, to)
                                        .notNull(DB.evaluation().adding_reference_id)
                                        .execute(Order.asc(DB.evaluation().evaluation_date)).all();
                                if(temp_results!=null) {
                                    System.out.println("temp_results : " + temp_results.size());
                                    results.addAll(temp_results);
                                }
                            }
                        }
                    }
                }
//            }
            if(results==null || results.isEmpty()) {
                log = "No result found.";
            } else {
                log = "Total Result Found: " + results.size() + "";
            }
            return true;
        }
        
    }
    
    private void detachAll() {
        this.loaderView.detach();
        this.failView.detach();
        this.emptyView.detach();
        
        this.loaderView2.detach();
        this.failView2.detach();
        this.emptyView2.detach();
        
        this.loaderView3.detach();
        this.failView3.detach();
        this.emptyView3.detach();
    }
    
    private void changeView(Node node) {
        this.detachAll();
//        cmb_type_eval_main.getSelectionModel().selectFirst();
        Animate.fade(node, 150, ()->{
            vbox_eval_main.setVisible(false);
            vbox_pres_main.setVisible(false);
            vbox_print_logs_main.setVisible(false);
            node.setVisible(true);
        }, vbox_eval_main, vbox_pres_main, vbox_print_logs_main);
    }
    
    //-----------------------------------------
    // LISTERS
    private void setViewListers(JFXButton button) {
        vbox_pres_main_table.getChildren().clear();
        this.MODE = button.getText();
        lbl_subtitle_pres.setText(MODE.equalsIgnoreCase(PRES_LIST)? "List of students who are qualified for President's lister in the current semester." : "List of students who are qualified for Dean's lister in the current semester.");
        lbl_title_pres.setText(MODE.equalsIgnoreCase(PRES_LIST)? "President's Lister" : "Dean's Lister");
        SimpleTask set = new SimpleTask("set_reports_value_listers");
        set.setTask(()->{
            ArrayList<CurriculumMapping> cMaps = Mono.orm().newSearch(Database.connect().curriculum())
                    .active(Order.asc(DB.curriculum().name)).all();
            
            Mono.fx().thread().wrap(()->{
                Callback<ListView<CurriculumMapping>, ListCell<CurriculumMapping>> factory = lv -> {
                    return new ListCell<CurriculumMapping>() {
                        @Override
                        protected void updateItem(CurriculumMapping item, boolean empty) {
                            super.updateItem(item, empty);
                            setText(empty ? "" : (item.getName()));
                        }
                    };
                };
                
                this.cmb_curriculum_lister.getItems().clear();
                if(cMaps != null) {
                    this.cmb_curriculum_lister.getItems().addAll(cMaps);
                    this.cmb_curriculum_lister.getSelectionModel().selectFirst();
                }
                this.cmb_curriculum_lister.setCellFactory(factory);
                this.cmb_curriculum_lister.setButtonCell(factory.call(null));
            });
        });
        set.whenFailed(()->{
            set.getTaskException().printStackTrace();
        });
        set.whenSuccess(()->{
            this.fetchAchievers();
        });
        set.start();
    }
    
    private void fetchAchievers() {
        this.detachAll();
        vbox_pres_main_table.getChildren().clear();
        previewAchievers.clear();
        
        if(cmb_curriculum_lister.getSelectionModel().getSelectedItem()==null) {
            return;
        }
        Integer CURRICULUM_id = cmb_curriculum_lister.getSelectionModel().getSelectedItem().getId();
        
        FetchAchievers fetch = new FetchAchievers();
        fetch.CURRICULUM_id = CURRICULUM_id;
        fetch.MODE =(MODE.equalsIgnoreCase(PRES_LIST))? ListersChecker.ListerMode.PRESIDENTS_LIST : ListersChecker.ListerMode.DEANS_LIST;
//        fetch.YEAR_LEVEL = cmb_year_level_pres.getSelectionModel().getSelectedIndex() + 2;
        fetch.whenStarted(()->{
            this.detachAll();
            this.loaderView2.setMessage("Loading Result");
            this.loaderView2.attach();
        });
        fetch.whenCancelled(()->{
            this.detachAll();
            lbl_result_pres.setText("");
            this.emptyView2.setMessage("No Result Found");
            this.emptyView2.getButton().setVisible(false);
            this.emptyView2.attach();
        });
        fetch.whenSuccess(()->{
            this.detachAll();
            previewAchievers.clear();
            System.out.println("MODE: " + MODE);
            if(MODE.equalsIgnoreCase(PRES_LIST)) {
                if(fetch.getPresListers().isEmpty()) {
                    lbl_result_pres.setText("");
                    this.emptyView2.setMessage("No Result Found");
                    this.emptyView2.getButton().setVisible(false);
                    this.emptyView2.attach();
                } else {
                    //create table here
                    int res = fetch.getPresListers().size();
                    lbl_result_pres.setText("Total result"+(res>1? "s" : "")+" found: " + res);
                    this.createAchieversTable(fetch.getPresListers());
                }
            } else if(MODE.equalsIgnoreCase(DEANS_LIST)) {
                if(fetch.getDeansListers().isEmpty()) {
                    lbl_result_pres.setText("");
                    this.emptyView2.setMessage("No Result Found");
                    this.emptyView2.getButton().setVisible(false);
                    this.emptyView2.attach();
                } else {
                    //create table here
                    int res = fetch.getDeansListers().size();
                    lbl_result_pres.setText("Total result"+(res>1? "s" : "")+" found: " + res);
                    this.createAchieversTable(fetch.getDeansListers());
                }
            }
        });
        fetch.whenFinished(()->{
            this.loaderView2.detach();
        });
        fetch.transact();
    }
    
    private ArrayList<AchieversData> previewAchievers = new ArrayList<>();
    
    private void createAchieversTable(ArrayList<AchieversData> results) {
        this.detachAll();
        previewAchievers = results;
        SimpleTable achieversTable = new SimpleTable();
        achieversTable.getChildren().clear();
        String status = cmb_sort_status_eval.getSelectionModel().getSelectedItem();
        for(AchieversData each: previewAchievers) {
            SimpleTableRow row = new SimpleTableRow();
            row.setRowHeight(70.0);
            AchieversRow rowFX = M.load(AchieversRow.class);
            rowFX.getLbl_name().setText(each.getFullName());
            rowFX.getLbl_section().setText(each.sectionName);
            rowFX.getLbl_gwa().setText(each.GWA);
            
            
            System.out.println(each.getFullName() + " " + each.GWA);
            
            
            row.getRowMetaData().put("MORE_INFO", each);

            SimpleTableCell cellParent = new SimpleTableCell();
            cellParent.setResizePriority(Priority.ALWAYS);
            cellParent.setContent(rowFX.getApplicationRoot());

            row.addCell(cellParent);
            achieversTable.addRow(row);
        }
        
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(achieversTable);
        simpleTableView.setFixedWidth(true);

        simpleTableView.setParentOnScene(vbox_pres_main_table);
    }
    
    //-----------------------------------------
    // TRANSACTION FOR GETTING LISTERS
    public class FetchAchievers extends Transaction{
        
        public ListersChecker.ListerMode MODE;
        public Integer CURRICULUM_id;
        
        private ArrayList<AchieversData> presListers;
        public ArrayList<AchieversData> getPresListers() {
            return presListers;
        }

        private ArrayList<AchieversData> deansListers;
        public ArrayList<AchieversData> getDeansListers() {
            return deansListers;
        }
        @Override
        protected boolean transaction() {
            ListersChecker checker = new ListersChecker();
            AcademicTermMapping current = SystemProperties.instance().getCurrentAcademicTerm();//Database.connect().academic_term().getPrimary(19);
            checker.setCurrentTerm(current);
            checker.setListerMode(MODE);
            checker.setCURRICULUM_id(CURRICULUM_id);
            ArrayList<ListerData> listers = checker.check();
            if(listers==null || listers.isEmpty())
                return false;
            
            presListers = new ArrayList<>();
            deansListers = new ArrayList<>();

            for(ListerData each: listers) {
                AchieversData data = new AchieversData(each.student, each.gwa);
                if(MODE.equals(ListersChecker.ListerMode.PRESIDENTS_LIST)) {
                    presListers.add(data);
                } else {
                    deansListers.add(data);
                }
            }
            return true;
        }
    }
    
    private class AchieversData {
        private String studentNumber;
        private String lastName;
        private String firstName;
        private String middleName;
        private String GWA;
        private String sectionName;
        
        public AchieversData(StudentMapping student, String GWA) {
            this.studentNumber = student.getId();
            this.lastName = student.getLast_name();
            this.firstName = student.getFirst_name();
            this.middleName = student.getMiddle_name()==null? "" : student.getMiddle_name();
            this.GWA = GWA;
            CurriculumMapping curriculum = Database.connect().curriculum().getPrimary(student.getCURRICULUM_id());
            AcademicProgramMapping acadProg = Database.connect().academic_program().getPrimary(curriculum.getACADPROG_id());
            this.sectionName = acadProg.getCode() + " " + student.getYear_level() + student.getSection() + (student.get_group()==null || student.get_group().equals(0)? "" : "-G" + student.get_group());
        }
        
        public String getFullName() {
            return this.lastName + ", " + this.firstName + (this.middleName==null? "" : " " + this.middleName);
        }
    }   
    
    private void printAchievers() {
        if(previewAchievers==null || previewAchievers.isEmpty()) {
            Mono.fx().snackbar().showError(application_root, "No Result To Print");
            btn_print_eval_main.setDisable(true);
            return;
        }
        String[] colNames = new String[]{"Student Number","Full Name", "Section", "GWA"};
        ArrayList<String[]> rowData = new ArrayList<>();
        for (int i = 0; i < previewAchievers.size(); i++) {
            AchieversData result = previewAchievers.get(i);
            String[] row = new String[]{(i+1)+".  "+  result.studentNumber,
                WordUtils.capitalizeFully(result.getFullName()), 
                result.sectionName, 
                result.GWA };
            rowData.add(row);
        }
        
        PrintResult print = new PrintResult();
        print.setDocumentFormat(ReportsUtility.paperSizeChooser(this.getStage()));
        print.columnNames = colNames;
        print.ROW_DETAILS = rowData;
        String dateToday = formatter_filename.format(Mono.orm().getServerTime().getDateWithFormat());
        print.fileName = lbl_title_pres.getText().replace(" ", "_").toLowerCase() + "_" + dateToday;
        CurriculumMapping selected = cmb_curriculum_lister.getSelectionModel().getSelectedItem();
        print.reportOtherDetail = SystemProperties.instance().getCurrentTermString();
        print.reportTitleHeader = lbl_title_pres.getText();
        print.reportTitleIntro = selected.getName() + (selected.getMajor()==null || selected.getMajor().isEmpty() || selected.getMajor().equalsIgnoreCase("NONE")? "" : " MAJOR IN " + selected.getMajor());
        print.whenStarted(() -> {
            btn_print_eval_main.setDisable(true);
            super.cursorWait();
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
            btn_print_eval_main.setDisable(false);
            Notifications.create()
                    .title("Printing Results")
                    .text("Please wait a moment.")
                    .showInformation();
        });
        print.whenFinished(() -> {
            btn_print_eval_main.setDisable(false);
            super.cursorDefault();
        });
        //----------------------------------------------------------------------
        print.transact();
    }
    
    
    //------------------------------
    private void onSearchFaculty(String key, String mode) {
        FacultyMainController.SearchFaculty searchTx = new FacultyMainController.SearchFaculty();
        searchTx.setSearchValue(key.trim());
        searchTx.setSearchMode(mode.equalsIgnoreCase("BulSU ID")? "ID" : mode);
        searchTx.whenStarted(() -> {
            this.btn_filter_eval_main.setDisable(true);
            this.btn_filter_print_logs.setDisable(true);
        });
        searchTx.whenCancelled(() -> {
//            this.fetchResult(MODE.equalsIgnoreCase(EVALUATION)? btn_evaluation : btn_adding_changing, null);
        });
        searchTx.whenFailed(() -> {
        });
        searchTx.whenSuccess(() -> {
            ArrayList<FacultyMapping> facultyResults = searchTx.getFacultyList();
            if(facultyResults!=null){
                System.out.println("FACULTY FOUND: " + facultyResults.size());
            }
            if(MODE.equalsIgnoreCase(PRINT_LOGS)) {
                this.fetchPrintLogsTable(facultyResults);
            } else {
                this.fetchResult(MODE.equalsIgnoreCase(EVALUATION)? btn_evaluation : btn_adding_changing, facultyResults);
            }
        });
        searchTx.whenFinished(() -> {
            this.btn_filter_eval_main.setDisable(false);
            this.btn_filter_print_logs.setDisable(false);
        });
        if(key!=null || !key.isEmpty()) {
            searchTx.transact();
        } else {
//            this.fetchResult(MODE.equalsIgnoreCase(EVALUATION)? btn_evaluation : btn_adding_changing, null);
        }
    }
    
    
    
    //---------------------------------------------------------------
    // PRINT LOGS
    //-------------------------------------
    private ArrayList<PrintLogsMapping> printLogsView = new ArrayList<>();
    private void setViewInPrintLogs() {
        MODE = PRINT_LOGS;
        SimpleTask setter = new SimpleTask("set_print_logs_view");
        setter.setTask(()->{
            PrintLogsMapping start = Mono.orm().newSearch(Database.connect().print_logs())
                    .active(Order.asc(DB.print_logs().id)).first();
            PrintLogsMapping end = Mono.orm().newSearch(Database.connect().print_logs())
                    .active(Order.desc(DB.print_logs().id)).first();
            
            dateStorage.clear();
            dateList.clear();
            List<Date> dates = new ArrayList<Date>();
            try {
                if(end==null && start==null) {
                    Mono.fx().thread().wrap(()->{
                        cmb_from_print_logs.setDisable(true);
                        cmb_to_print_logs.setDisable(true);
                    });
                    return;
                }
                Date endDate = formatter_plain.parse(end.getPrinted_date().toString());//DateUtils.addDays(formatter_plain.parse(end.getEvaluation_date().toString()), 1);
                Date startDate = formatter_plain.parse(start.getPrinted_date().toString());

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
                    dateStorage.add(nameDate);
                }

                Mono.fx().thread().wrap(()->{
                    cmb_from_print_logs.getItems().clear();
                    cmb_to_print_logs.getItems().clear();

                    cmb_from_print_logs.getItems().addAll(dateList); 
                    cmb_to_print_logs.getItems().addAll(dateList);

                    cmb_from_print_logs.getSelectionModel().selectFirst();
                    cmb_to_print_logs.getSelectionModel().selectLast();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        setter.whenStarted(()->{
            this.detachAll();
            this.loaderView3.setMessage("Loading Required Data");
            this.loaderView3.attach();
        });
        setter.whenFailed(()->{});
        setter.whenSuccess(()->{
            this.setComboBoxLimit(cmb_from_print_logs, cmb_to_print_logs, 0);
            this.onSearchFaculty(txt_faculty_search_print_logs.getText(), cmb_print_logs_faculty_search_vis1.getSelectionModel().getSelectedItem());
        });
        setter.whenFinished(()->{
            this.detachAll();
        });
        setter.start();
    }
    
    private void fetchPrintLogsTable(ArrayList<FacultyMapping> facultyResults) {
        if(cmb_from_print_logs.getItems().isEmpty()) {
            lbl_result_print_logs.setText("");
            this.emptyView3.setMessage("No Result Found");
            this.emptyView3.getButton().setVisible(false);
            this.emptyView3.attach();
            this.printLogsView.clear();
            return;
        }
        String from_str = cmb_from_print_logs.getSelectionModel().getSelectedItem();
        String to_str = cmb_to_print_logs.getSelectionModel().getSelectedItem();
        Date from = null, to = null;
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
        cmbChanged = false;
        typePrint = cmb_print_logs_type.getSelectionModel().getSelectedItem();
        System.out.println("FROM: " + from + " | TO: " + to);
        FetchPrintLog fetch = new FetchPrintLog();
        fetch.facultyResult = facultyResults;
        fetch.from = from;
        fetch.to = to;
        fetch.type = cmb_print_logs_type.getSelectionModel().getSelectedItem().toUpperCase();
        fetch.whenStarted(()->{
            this.detachAll();
            this.loaderView3.setMessage("Loading Print Logs Result");
            this.loaderView3.attach();
        });
        fetch.whenCancelled(()->{});
        fetch.whenSuccess(()->{
            this.detachAll();
            if(fetch.result==null || fetch.result.isEmpty()) {
                lbl_result_print_logs.setText("");
                this.emptyView3.setMessage("No Result Found");
                this.emptyView3.getButton().setVisible(false);
                this.emptyView3.attach();
                this.printLogsView.clear();
            } else {
                //create table here
                int res = fetch.result.size();
                lbl_result_print_logs.setText("Total result"+(res>1? "s" : "")+" found: " + res);
                
                this.printLogsView = fetch.result;
                this.createPrintLogsTable(printLogsView);
            }
        });
        fetch.whenFinished(()->{});
        fetch.transact();
    }
    
    public class FetchPrintLog extends Transaction{
        
        public Date from;
        public Date to;
        public ArrayList<FacultyMapping> facultyResult;
        public String type;
        
        private ArrayList<PrintLogsMapping> result;
        @Override
        protected boolean transaction() {
            if(facultyResult==null || facultyResult.isEmpty()) {
            } else {
                result = new ArrayList<>();
                for(FacultyMapping faculty : facultyResult) {
                    ArrayList<PrintLogsMapping> temp_results = Mono.orm().newSearch(Database.connect().print_logs())
                            .between(DB.print_logs().printed_date, from, to)
                            .eq(DB.print_logs().printed_by, faculty.getId())
                            .eq(DB.print_logs().type, type)
                            .active(Order.asc(DB.print_logs().printed_date)).all();
                    if(temp_results!=null) {
                        System.out.println("temp_results : " + temp_results.size());
//                      result.addAll(temp_results);
                        outLoop:
                        for(PrintLogsMapping each : temp_results) {
                            boolean exist = false;
                            innerLoop:
                            for(PrintLogsMapping res : result) {
                                if(each.getId().equals(res.getId())) {
                                    exist = true;
                                    break innerLoop;
                                }
                            }
                            if(!exist) {
                                result.add(each);
                            }
                        }
                    }
                }
            }
            return true;
        }
    }
    
    private void createPrintLogsTable(ArrayList<PrintLogsMapping> results) {
        this.detachAll();
//        previewAchievers = results;
        SimpleTable logsTable = new SimpleTable();
        logsTable.getChildren().clear();
        for(PrintLogsMapping each: results) {
            SimpleTableRow row = new SimpleTableRow();
            row.setRowHeight(70.0);
            ReportsRow rowFX = M.load(ReportsRow.class);
            rowFX.getLbl_date().setText(dateFormat.format(each.getPrinted_date()));
            rowFX.getLbl_faculty().setText(WordUtils.capitalizeFully(FacultyUtility.getFacultyName(FacultyUtility.getFaculty(each.getPrinted_by()))));
            rowFX.getLbl_student_name().setText(each.getTitle());
            rowFX.getLbl_type().setText(each.getModule());
            rowFX.getLbl_year_level().setText((each.getTerminal()));
            
//            row.getRowMetaData().put("MORE_INFO", each);

            SimpleTableCell cellParent = new SimpleTableCell();
            cellParent.setResizePriority(Priority.ALWAYS);
            cellParent.setContent(rowFX.getApplicationRoot());

            row.addCell(cellParent);
            logsTable.addRow(row);
        }
        
        SimpleTableView simpleTableView = new SimpleTableView();
        simpleTableView.setTable(logsTable);
        simpleTableView.setFixedWidth(true);

        simpleTableView.setParentOnScene(vbox_print_logs_table);
    }
    
    private String typePrint = "INITIAL";
    private void printLogsEvents() {
        cmb_from_print_logs.valueProperty().addListener((a)->{
            cmbChanged = true;
        });
        cmb_print_logs_type.valueProperty().addListener((a)->{
            cmbChanged = true;
        });
        cmb_to_print_logs.valueProperty().addListener((a)->{
            cmbChanged = true;
        });
        super.addClickEvent(btn_print_print_logs, ()->{
            this.printPrintLogsResult();
        });
    }
    
    
    private void printPrintLogsResult() {
        if(cmbChanged) {
            int res =  Mono.fx().alert()
                    .createConfirmation().setHeader("Not Yet Filtered")
                    .setMessage("The result to be printed are not yet filtered with the given dates above. Do you still want to print?")
                    .confirmYesNo();
            if(res==-1)
                return;
        }
        // select paper size
        String[] colNames = new String[]{"Printed Date","Printed By", "Module", "Title", "Terminal"};
        ArrayList<String[]> rowData = new ArrayList<>();
        if(printLogsView==null || printLogsView.isEmpty()) {
            Mono.fx().snackbar().showError(application_root, "No Result To Print");
            btn_print_print_logs.setDisable(true);
            return;
        }
        PrintLogsMapping ref = null;
        for (int i = 0; i < this.printLogsView.size(); i++) {
            PrintLogsMapping result = printLogsView.get(i);
            ref = result;
            String[] row = new String[]{(i+1)+".  "+  ReportsUtility.formatter_mm.format(result.getPrinted_date()),
                WordUtils.capitalizeFully((FacultyUtility.getFacultyName(FacultyUtility.getFaculty(result.getPrinted_by())))), 
                WordUtils.capitalizeFully(result.getModule()), 
                WordUtils.capitalizeFully(result.getTitle()), 
                (result.getTerminal())};
            rowData.add(row);
        }
        
        String title = "PRINTING LOGS - " + this.typePrint;
        PrintResult print = new PrintResult();
        print.setDocumentFormat(ReportsUtility.paperSizeChooser(this.getStage()));
        print.columnNames = colNames;
        print.ROW_DETAILS = rowData;
        String dateToday = formatter_filename.format(Mono.orm().getServerTime().getDateWithFormat());
        print.fileName = title.toUpperCase() + "_" + dateToday;
        String fr = cmb_from_eval_main.getSelectionModel().getSelectedItem();
        String to = cmb_to_eval_main.getSelectionModel().getSelectedItem();
        SimpleDateFormat month = new SimpleDateFormat("MMMMM");
        AcademicTermMapping selected = cmb_term_eval.getSelectionModel().getSelectedItem();
//        print.reportTitleIntro = selected.getSchool_year() + " " + selected.getSemester();
        if(cmbChanged) {
//            print.reportDescription += "";
        } else if(fr==null || to==null || ref==null || ref.getPrinted_date()==null) {
            print.reportOtherDetail = "As of " + formatter_display.format(Mono.orm().getServerTime().getDateWithFormat());
        } else if(fr.equalsIgnoreCase(to)) {
            print.reportOtherDetail = ""+ formatter_display.format(ref.getPrinted_date());
         } else
            print.reportOtherDetail = "From " + fr + " to " + to;
        
        print.reportTitleHeader = title;
        print.whenStarted(() -> {
            btn_print_print_logs.setDisable(true);
            super.cursorWait();
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
            btn_print_print_logs.setDisable(false);
            Notifications.create()
                    .title("Printing Results")
                    .text("Please wait a moment.")
                    .showInformation();
        });
        print.whenFinished(() -> {
            btn_print_print_logs.setDisable(false);
            super.cursorDefault();
        });
        //----------------------------------------------------------------------
        if(ReportsUtility.savePrintLogs(null, title.toUpperCase(), "REPORTS", "INITIAL")) {
            print.transact();
        } else {
            Notifications.create()
                    .title("Request Failed")
                    .text("Something went wrong. Sorry for the inconviniece.")
                    .showInformation();
        }
    }
}

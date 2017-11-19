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

import app.lazy.models.AcademicTermMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.EvaluationMapping;
import app.lazy.models.StudentMapping;
import app.lazy.models.utils.FacultyUtility;
import com.jfoenix.controls.JFXButton;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.cict.PublicConstants;
import org.cict.authentication.authenticator.CollegeFaculty;
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
    private JFXButton btn_encoding;

    @FXML
    private VBox vbox_eval_main;

    @FXML
    private Label lbl_title_eval;

    @FXML
    private ComboBox<String> cmb_sort_status_eval;

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
    private StackPane stack_eval_main;

    @FXML
    private VBox vbox_eval_main_table;
    
    @FXML
    private ComboBox<String> cmb_term_eval;
    
    @FXML
    private Label lbl_result;
            
    @FXML
    private Label lbl_subtitle;
            
    private LoaderView loaderView;
    private FailView failView;
    private EmptyView emptyView;
    @Override
    public void onInitialization() {
        super.bindScene(application_root);
        
        this.loaderView = new LoaderView(stack_eval_main);
        this.failView = new FailView(stack_eval_main);
        this.emptyView = new EmptyView(stack_eval_main);
    
        this.loaderView = new LoaderView(stack_eval_main);
        this.failView = new FailView(stack_eval_main);
        this.emptyView = new EmptyView(stack_eval_main);
        
        this.setViewTask(btn_evaluation, SystemProperties.instance().getCurrentAcademicTerm().getId());
        
        
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
        
        lbl_result.setText("");
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
        
        this.evaluationEvents();
        
    }
    
    private ArrayList<String> dateList = new ArrayList<>();
    private void setComboBoxLimit(ComboBox<String> source, ComboBox<String> self, int extra, ComboBox<String>... padding) {
        ChoiceRange.setComboBoxLimit(dateList, source, self, 0, padding);
    }
    
    private ArrayList<HashMap<String,AcademicTermMapping>> termDetails = new ArrayList<>();
    private void setViewTask(JFXButton button, Integer ACADTERM_id) {
        vbox_eval_main_table.getChildren().clear();
        MODE = button.getText();
        lbl_subtitle.setText(MODE.equalsIgnoreCase(EVALUATION)? "List of every evaluation transaction." : "List of every adding and changing transaction.");
        lbl_title_eval.setText(MODE.equalsIgnoreCase(EVALUATION)? "Successful Evaluation Summary" : "Successful Adding & Changing Summary");
        cmb_term_eval.getItems().clear();
        cmb_sort_status_eval.getSelectionModel().selectFirst();
        cmb_type_eval_main.getSelectionModel().selectFirst();
        SimpleTask set = new SimpleTask("set_reports_value");
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
            termDetails.clear();
            for(AcademicTermMapping each: atMaps) {
                String termName = (each.getSchool_year()==null? "No School Year" : each.getSchool_year()) + " " + (each.getSemester()==null? "No Semester" : each.getSemester());
                Mono.fx().thread().wrap(()->{
                    cmb_term_eval.getItems().add(termName);
                });
                
                HashMap<String,AcademicTermMapping> hm = new HashMap<>();
                hm.put(termName, each);
                termDetails.add(hm);
            }
            
            AcademicTermMapping currentATMap = SystemProperties.instance().getCurrentAcademicTerm();
            String termName = (currentATMap.getSchool_year()==null? "No School Year" : currentATMap.getSchool_year()) + " " + (currentATMap.getSemester()==null? "No Semester" : currentATMap.getSemester());

            Mono.fx().thread().wrap(()->{
                cmb_term_eval.getSelectionModel().select(termName);
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
            if(MODE.equalsIgnoreCase("evaluation")) {
                vbox_eval_main.setDisable(true);
            }
        });
        set.whenSuccess(()->{
            System.out.println("SUCCESS");
            vbox_eval_main.setDisable(false);
            this.setCmbValues(ACADTERM_id);
            this.setComboBoxLimit(cmb_from_eval_main, cmb_to_eval_main, 0);
            this.fetchResult(button);
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
                        .eq(DB.evaluation().ACADTERM_id, ACADTERM_id).active(Order.asc(DB.evaluation().evaluation_date)).first();
        // Affected by the ORM Update - Changed the order -> desc to get last then use first()
        EvaluationMapping end = Mono.orm().newSearch(Database.connect().evaluation())
                        .eq(DB.evaluation().ACADTERM_id, ACADTERM_id).active(Order.desc(DB.evaluation().evaluation_date)).first();
        try {
            Date endDate = DateUtils.addDays(formatter_plain.parse(end.getEvaluation_date().toString()), 1);
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
        }
    }
    
    private boolean cmbChanged = false;
    private String MODE;
    private void evaluationEvents() {
        cmb_term_eval.valueProperty().addListener((a)->{
            String termName = cmb_term_eval.getSelectionModel().getSelectedItem();
            int index = cmb_term_eval.getSelectionModel().getSelectedIndex();
            try {
                AcademicTermMapping selected = termDetails.get(index).get(termName);
                this.setCmbValues(selected.getId());
                this.setComboBoxLimit(cmb_from_eval_main, cmb_to_eval_main, 0);
            } catch (Exception e) {
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
            System.out.println("FILTER DETAILS********************");
            String termName = cmb_term_eval.getSelectionModel().getSelectedItem();
            int index = cmb_term_eval.getSelectionModel().getSelectedIndex();
            String status = cmb_sort_status_eval.getSelectionModel().getSelectedItem();
            String from_str = cmb_from_eval_main.getSelectionModel().getSelectedItem();
            String to_str = cmb_to_eval_main.getSelectionModel().getSelectedItem();
            String type = cmb_type_eval_main.getSelectionModel().getSelectedItem();
            
            System.out.println("TERM: " + termName);
            System.out.println("STATUS: " + status);
            System.out.println("FROM: " + from_str);
            System.out.println("TO: " + to_str);
            System.out.println("TYPE: " + type);
            System.out.println("MODE: " + MODE);
            System.out.println("***************************");
            this.fetchResult(MODE.equalsIgnoreCase(EVALUATION)? btn_evaluation : btn_adding_changing);
        });
        super.addClickEvent(btn_filter_eval_main, ()->{
            cmbChanged = false;
            this.fetchResult(MODE.equalsIgnoreCase(EVALUATION)? btn_evaluation : btn_adding_changing);
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
        
        String status = cmb_sort_status_eval.getSelectionModel().getSelectedItem();
        String[] colNames = new String[]{"Date and Time",status.equalsIgnoreCase("REVOKED")? "Revoked By" : "Evaluator", "Student Number", "Student Name", "Year Level"};
        ArrayList<String[]> rowData = new ArrayList<>();
        if(results==null) {
            Notifications.create()
                    .title("No Evaluation Result Found")
                    .text("No data to print.")
                    .showWarning();
            btn_print_eval_main.setDisable(true);
            return;
        }EvaluationMapping ref = null;
        for (int i = 0; i < results.size(); i++) {
            EvaluationMapping result = results.get(i);
            ref = result;
            String[] row = new String[]{(i+1)+".  "+  (status.equalsIgnoreCase("REVOKED")? ReportsUtility.formatter2.format(result.getCancelled_date()) : ReportsUtility.formatter2.format(result.getEvaluation_date())),
                WordUtils.capitalizeFully(status.equalsIgnoreCase("REVOKED")? (FacultyUtility.getFacultyName(FacultyUtility.getFaculty(result.getCancelled_by()))) : (FacultyUtility.getFacultyName(FacultyUtility.getFaculty(result.getFACULTY_id())))), 
                this.getStudentNumber(result.getSTUDENT_id()), 
                WordUtils.capitalizeFully(this.getStudentName(result.getSTUDENT_id())), 
                (this.getYearLevel(result.getYear_level()))};
            rowData.add(row);
        }
        
        PrintResult print = new PrintResult();
        print.columnNames = colNames;
        print.ROW_DETAILS = rowData;
        String dateToday = formatter_filename.format(Mono.orm().getServerTime().getDateWithFormat());
        print.fileName = title.replace(" ", "_").toLowerCase() + "_" + dateToday;
        String fr = cmb_from_eval_main.getSelectionModel().getSelectedItem();
        String to = cmb_to_eval_main.getSelectionModel().getSelectedItem();
        SimpleDateFormat month = new SimpleDateFormat("MMMMM");
        print.reportDescription = cmb_term_eval.getSelectionModel().getSelectedItem();
        if(cmbChanged) {
            print.reportDescription += "";
        } else if(fr==null || to==null || ref==null || ref.getEvaluation_date()==null) {
            print.reportDescription += "\nAs of " + formatter_display.format(Mono.orm().getServerTime().getDateWithFormat());
        } else if(fr.equalsIgnoreCase(to)) {
            print.reportDescription += "\n"+ formatter_display.format(ref.getEvaluation_date());
         } else
            print.reportDescription += "\nFrom " + fr + " to " + to;
        
        print.reportTitle = lbl_title_eval.getText();
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
    
    private String EVALUATION = "EVALUATION", ADD_CHANGE = "ADDING & CHANGING", ENCODING = " ENCODING";
    private ArrayList<EvaluationMapping> results;
    private void fetchResult(JFXButton button) {
        System.out.println(MODE);
        btn_print_eval_main.setDisable(false);
        cmbChanged = false;
        FetchEvaluationResult fetch = new FetchEvaluationResult();
        String termName = cmb_term_eval.getSelectionModel().getSelectedItem();
        int index = cmb_term_eval.getSelectionModel().getSelectedIndex();
        AcademicTermMapping selected = termDetails.get(index).get(termName);
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
        fetch.mode = button.getText();
        fetch.to = to;
        fetch.type = cmb_type_eval_main.getSelectionModel().getSelectedItem();
        fetch.status= cmb_sort_status_eval.getSelectionModel().getSelectedItem().toUpperCase();
        
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
            if(results==null) {
                lbl_result.setText("");
                this.emptyView.setMessage("No Result Found");
                this.emptyView.getButton().setVisible(false);
                this.emptyView.attach();
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
            rowFX.getLbl_date().setText(status.equalsIgnoreCase("REVOKED")? dateFormat.format(each.getCancelled_date()) : dateFormat.format(each.getEvaluation_date()));
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
                        results = Mono.orm().newSearch(Database.connect().evaluation())
                                .eq(DB.evaluation().ACADTERM_id, this.ACADTERM_id)
                                .eq(DB.evaluation().remarks, status)
                                .between(status.equalsIgnoreCase("REVOKED")? DB.evaluation().cancelled_date : DB.evaluation().evaluation_date, from, to)
                                .isNull(DB.evaluation().adding_reference_id)
                                .execute(Order.asc(status.equalsIgnoreCase("REVOKED")? DB.evaluation().cancelled_date : DB.evaluation().evaluation_date)).all();
                    } else {
                        results = Mono.orm().newSearch(Database.connect().evaluation())
                                .eq(DB.evaluation().ACADTERM_id, this.ACADTERM_id)
                                .eq(DB.evaluation().print_type, type)
                                .eq(DB.evaluation().remarks, status)
                                .between(status.equalsIgnoreCase("REVOKED")? DB.evaluation().cancelled_date : DB.evaluation().evaluation_date, from, to)
                                .isNull(DB.evaluation().adding_reference_id)
                                .execute(Order.asc(status.equalsIgnoreCase("REVOKED")? DB.evaluation().cancelled_date : DB.evaluation().evaluation_date)).all();
                    }
                } else if(mode.equalsIgnoreCase(ADD_CHANGE)) {
                    if(type.equalsIgnoreCase("ALL")) {
                        results = Mono.orm().newSearch(Database.connect().evaluation())
                                .eq(DB.evaluation().ACADTERM_id, this.ACADTERM_id)
                                .eq(DB.evaluation().type, "ADDING_CHANGING")
                                .eq(DB.evaluation().remarks, (status.equalsIgnoreCase("REVOKED")? "REVOKED_ADD_CHANGE" : status))
                                .between(status.equalsIgnoreCase("REVOKED")? DB.evaluation().cancelled_date : DB.evaluation().evaluation_date, from, to)
                                .notNull(DB.evaluation().adding_reference_id)
                                .execute(Order.asc(status.equalsIgnoreCase("REVOKED")? DB.evaluation().cancelled_date : DB.evaluation().evaluation_date)).all();
                    } else {
                        results = Mono.orm().newSearch(Database.connect().evaluation())
                                .eq(DB.evaluation().ACADTERM_id, this.ACADTERM_id)
                                .eq(DB.evaluation().print_type, type)
                                .eq(DB.evaluation().type, "ADDING_CHANGING")
                                .eq(DB.evaluation().remarks, (status.equalsIgnoreCase("REVOKED")? "REVOKED_ADD_CHANGE" : status))
                                .between(status.equalsIgnoreCase("REVOKED")? DB.evaluation().cancelled_date : DB.evaluation().evaluation_date, from, to)
                                .notNull(DB.evaluation().adding_reference_id)
                                .execute(Order.asc(status.equalsIgnoreCase("REVOKED")? DB.evaluation().cancelled_date : DB.evaluation().evaluation_date)).all();
                    }
                }
//            }
            if(results==null) {
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
    }
    
    private void changeView(Node node) {
        this.detachAll();
        cmb_type_eval_main.getSelectionModel().selectFirst();
        Animate.fade(node, 150, ()->{
            vbox_eval_main.setVisible(false);
            node.setVisible(true);
        }, vbox_eval_main);
    }
    
}

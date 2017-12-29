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
package org.cict.accountmanager.faculty;

import app.lazy.models.AcademicProgramMapping;
import app.lazy.models.AccountFacultyMapping;
import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.FacultyMapping;
import artifacts.MonoString;
import com.itextpdf.text.Document;
import com.izum.fx.textinputfilters.StringFilter;
import com.izum.fx.textinputfilters.TextInputFilters;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import com.jhmvin.fx.controls.simpletable.SimpleTable;
import com.jhmvin.fx.controls.simpletable.SimpleTableCell;
import com.jhmvin.fx.controls.simpletable.SimpleTableRow;
import com.jhmvin.fx.controls.simpletable.SimpleTableView;
import com.jhmvin.fx.display.ControllerFX;
import com.jhmvin.fx.display.LayoutDataFX;
import com.jhmvin.fx.display.SceneFX;
import com.jhmvin.orm.SQL;
import com.jhmvin.orm.Searcher;
import com.jhmvin.transitions.Animate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.lang3.text.WordUtils;
import org.cict.authentication.authenticator.SystemProperties;
import org.cict.reports.ReportsUtility;
import org.cict.reports.result.PrintResult;
import org.controlsfx.control.Notifications;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import update.org.cict.controller.home.Home;
import update3.org.cict.SectionConstants;
import update3.org.cict.access.Access;

/**
 *
 * @author Joemar
 */
public class FacultyMainController extends SceneFX implements ControllerFX {

    @FXML
    private AnchorPane application_root;

    @FXML
    private JFXButton btn_home;

    @FXML
    private VBox vbox_list;

    @FXML
    private VBox vbox_no_found;

    @FXML
    private VBox vbox_no_found1;

    @FXML
    private Label lbl_no_found1;

    @FXML
    private JFXCheckBox chkbx_show_disabled;

    @FXML
    private ComboBox<String> cmb_sort;

    @FXML
    private JFXButton btn_new_faculty;

    @FXML
    private TextField txt_bulsu_id;

    @FXML
    private TextField txt_lastname;

    @FXML
    private TextField txt_firstname;

    @FXML
    private TextField txt_middlename;

    @FXML
    private RadioButton rbtn_male_new;

    @FXML
    private RadioButton rbtn_female_new;

    @FXML
    private ComboBox cmb_rank;

    @FXML
    private ComboBox cmb_des;

    @FXML
    private ComboBox cmb_dept;

    @FXML
    private JFXButton btn_save_new;

    @FXML
    private JFXButton btn_back_new;

    @FXML
    private AnchorPane anchor_new_faculty;

    @FXML
    private JFXButton btn_search;

    @FXML
    private TextField txt_search;

    @FXML
    private HBox hbox_tools;

    @FXML
    private JFXButton btn_print;

    public FacultyMainController() {

    }

    @Override
    public void onInitialization() {
        super.bindScene(application_root);
        this.fetchFaculty(true);
        this.cmb_sort.getItems().clear();
        this.cmb_sort.getItems().add("Active");
        this.cmb_sort.getItems().add("Inactive");
        this.cmb_sort.getSelectionModel().selectFirst();

        ToggleGroup group2 = new ToggleGroup();
        rbtn_male_new.setToggleGroup(group2);
        rbtn_female_new.setToggleGroup(group2);
        addTextFieldFilters();
        setComboBox();
    }

    private void addTextFieldFilters() {
        StringFilter textId = TextInputFilters.string()
                //                .setFilterMode(StringFilter.LETTER_DIGIT)
                .setMaxCharacters(50)
                .setNoLeadingTrailingSpaces(true)
                .setFilterManager(filterManager -> {
                    if (!filterManager.isValid()) {
                        Mono.fx().alert().createWarning().setHeader("Warning")
                                .setMessage(filterManager.getMessage())
                                .show();
                    }
                });
        textId.clone().setTextSource(txt_bulsu_id).applyFilter();

        StringFilter textName = TextInputFilters.string()
                .setFilterMode(StringFilter.LETTER_SPACE)
                .setMaxCharacters(100)
                .setNoLeadingTrailingSpaces(false)
                .setFilterManager(filterManager -> {
                    if (!filterManager.isValid()) {
                        Mono.fx().alert().createWarning().setHeader("Warning")
                                .setMessage(filterManager.getMessage())
                                .show();
                    }
                });
        textName.clone().setTextSource(txt_lastname).applyFilter();
        textName.clone().setTextSource(txt_firstname).applyFilter();
        textName.clone().setTextSource(txt_middlename).applyFilter();
    }

    private void setComboBox() {
        cmb_rank.getItems().clear();
        cmb_rank.getItems().add("Part Time");
        cmb_rank.getItems().add("Associate Professor I");
        cmb_rank.getItems().add("Associate Professor II");
        cmb_rank.getItems().add("Associate Professor III");
        cmb_rank.getItems().add("Associate Professor III");
        cmb_rank.getItems().add("Professor I");
        cmb_rank.getItems().add("Professor II");
        cmb_rank.getItems().add("Professor III");

        cmb_des.getItems().clear();
        cmb_des.getItems().add("Instructor");
        cmb_des.getItems().add("Faculty");
        cmb_des.getItems().add("System Administrator");
        cmb_des.getItems().add("Local Registrar");
        cmb_des.getItems().add("Department Head");
        cmb_des.getItems().add("Associate Dean");
        cmb_des.getItems().add("Dean");

        cmb_dept.getItems().clear();
        ArrayList<AcademicProgramMapping> acads = Mono.orm().newSearch(Database.connect().academic_program())
                //                .eq(DB.academic_program().implemented, 1)
                .active(Order.asc(DB.academic_program().code))
                .all();
        //----------------------------------------------------------------------
        // returns null when there is no academic program in the database
        if (acads == null) {
            return;
        }
        //----------------------------------------------------------------------
        for (AcademicProgramMapping acad : acads) {
            cmb_dept.getItems().add(acad.getCode());
        }
    }

    @Override
    public void onEventHandling() {
        // back to home
        super.addClickEvent(btn_home, () -> {
            this.onBackToHome();
        });

        this.cmb_sort.valueProperty().addListener((observable) -> {
            onSortChanged();
        });

        /**
         * New Faculty
         */
        this.addClickEvent(btn_new_faculty, () -> {
            txt_bulsu_id.setText("");
            txt_firstname.setText("");
            txt_lastname.setText("");
            txt_middlename.setText("");
            cmb_rank.getSelectionModel().select(null);
            cmb_des.getSelectionModel().select(-1);
            this.hbox_tools.setDisable(true);
//            vbox_no_found.setVisible(false);
//            vbox_no_found1.setVisible(false);
//            anchor_new_faculty.setVisible(true);
            this.cmb_sort.setDisable(true);
            this.btn_print.setDisable(true);
            changeView(anchor_new_faculty);
        });

        /**
         * Add
         */
        this.addClickEvent(btn_save_new, () -> {
            addNewFaculty();
        });

        /**
         * Cancel creation.
         */
        this.addClickEvent(btn_back_new, () -> {
//            vbox_list.setVisible(false);
            //
            this.hbox_tools.setDisable(false);
            this.cmb_sort.setDisable(false);
            this.btn_print.setDisable(false);
            this.onSortChanged();
        });

        this.addClickEvent(btn_search, () -> {
            onSearch(true);
        });

        Mono.fx().key(KeyCode.ENTER).release(application_root, () -> {
            onSearch(true);
        });

        super.addClickEvent(btn_print, () -> {
            this.printResult();
        });
    }

    private String reportDescription;

    private void printResult() {
        if (preview == null || preview.isEmpty()) {
            Notifications.create()
                    .title("No Result")
                    .text("No result found to print.")
                    .showWarning();
            return;
        }
        String[] colNames = new String[]{"BulSU ID", "Name", "Department", "Username", "Access Level"};
        String[] colDescprtion = new String[]{"Registered BulSU ID of the faculty.", "Full name of the faculty.", "Department of the faculty.", "Registered username of faculty in the system.", "Current access level."};
        //--------------
        ArrayList<Object> colDetails = ReportsUtility.paperSizeChooserwithCustomize(this.getStage(), colNames, colDescprtion);
        Document doc = (Document) colDetails.get(0);
        if(doc==null) {
            return;
        }
        HashMap<Integer, Object[]> customized = (HashMap<Integer, Object[]>) colDetails.get(1);
        ArrayList<String> newColNames = new ArrayList<>();
        for (int i = 0; i < customized.size(); i++) {
            Object[] details = customized.get(i);
            if(details != null) {
                Boolean isChecked = (Boolean) details[0];
                if(isChecked) {
                    newColNames.add((String) details[1]);
                }
            }
        }
        //
        
        ArrayList<String[]> rowData = new ArrayList<>();
        for (int i = 0; i < preview.size(); i++) {
            FacultyInformation result = preview.get(i);
            String[] row = new String[]{(i + 1) + ".  " + result.getBulsuID(),
                WordUtils.capitalizeFully(result.getFullName()),
                (result.getDepartment() == null || result.getDepartment().isEmpty() ? "NONE" : result.getDepartment()),
                (result.getAccountFacultyMapping() == null ? "NO ACCOUNT" : result.getAccountFacultyMapping().getUsername().toUpperCase()),
                WordUtils.capitalizeFully(result.getAccessLevel().equalsIgnoreCase(Access.ACCESS_CO_REGISTRAR) ? "Assistant Registrar" : (result.getAccessLevel()==null || result.getAccessLevel().isEmpty()? "NONE" : result.getAccessLevel().replace("_", " ")))};
            rowData.add(row);
        }
        PrintResult print = new PrintResult();
        print.setDocumentFormat(doc, customized);
        print.columnNames = newColNames;
        print.ROW_DETAILS = rowData;
        print.fileName = "Faculty Result List " + cmb_sort.getSelectionModel().getSelectedItem().toLowerCase();

        print.reportTitleIntro = SystemProperties.instance().getCurrentTermString();
        print.reportOtherDetail = reportDescription;

        print.reportTitleHeader = "Faculty Result List";
        print.whenStarted(() -> {
            btn_print.setDisable(true);
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
            btn_print.setDisable(false);
            Notifications.create()
                    .title("Printing Results")
                    .text("Please wait a moment.")
                    .showInformation();
        });
        print.whenFinished(() -> {
            btn_print.setDisable(false);
            super.cursorDefault();
        });
        //----------------------------------------------------------------------
        if(ReportsUtility.savePrintLogs(null, "Faculty Result List".toUpperCase(), "FACULTY", "INITIAL"))
            print.transact();
    }

    private void requestFocus(Node control) {
        Stage stage = Mono.fx().getParentStage(control);
        stage.requestFocus();
        control.requestFocus();
    }

    private void onSortChanged() {
        vbox_list.setVisible(false);
        this.hbox_tools.setDisable(false);
        if (cmb_sort.getSelectionModel().getSelectedIndex() == 1) {
            message = "No inactive faculty found.";
            this.fetchFaculty(false);
        } else {
            message = "No active faculty found.";
            this.fetchFaculty(true);
        }
    }

    private void onBackToHome() {
        Home.callHome(this);
    }

    private ArrayList<FacultyInformation> activeFaculty = new ArrayList<>();
    private ArrayList<FacultyInformation> deactivatedFaculty = new ArrayList<>();

    public void fetchFaculty(Boolean active) {
        FetchFacultyInfo ffInfo = new FetchFacultyInfo();
        vbox_list.setVisible(false);
        ffInfo.setOnSuccess(onSuccess -> {
            activeFaculty = ffInfo.getActiveFaculty();
            deactivatedFaculty = ffInfo.getDeactivatedFaculty();
            if (active) {
                reportDescription = "List of Active Faculty";
                this.createFacultyTable(activeFaculty);
            } else if (!active) {
                reportDescription = "List of Inactive Faculty";
                this.createFacultyTable(deactivatedFaculty);
            }
        });
        ffInfo.setOnFailure(onFailure -> {
            System.out.println("FAILED");
        });
        ffInfo.transact();
    }

    private final static String KEY_MORE_INFO = "MORE_INFO";
    private SimpleTable tableFaculty = new SimpleTable();
    private String message = null;
    private ArrayList<FacultyInformation> preview;

    private void createFacultyTable(ArrayList<FacultyInformation> facultyToDisplay) {
        try {
            preview = facultyToDisplay;
            tableFaculty.getChildren().clear();
            if (facultyToDisplay.isEmpty()) {
                System.out.println("EMPTY");
                if (message == null) {
                    changeView(vbox_no_found);
                } else {
                    lbl_no_found1.setText(message);
                    changeView(vbox_no_found1);
                    message = null;
                }
                return;
            }
            for (FacultyInformation currentFacultyInfo : facultyToDisplay) {
                createRow(currentFacultyInfo, tableFaculty);
            }

            SimpleTableView simpleTableView = new SimpleTableView();
            simpleTableView.setTable(tableFaculty);
            simpleTableView.setFixedWidth(true);
            simpleTableView.setParentOnScene(vbox_list);
            changeView(vbox_list);
        } catch (NullPointerException a) {
            a.printStackTrace();
        }
    }

    private boolean refreshInfo = true;

    private void createRow(FacultyInformation currentFacultyInfo, SimpleTable table) {

        FacultyMapping faculty = currentFacultyInfo.getFacultyMapping();
        SimpleTableRow row = new SimpleTableRow();
        row.setRowHeight(70.0);

        HBox facultyRow = (HBox) Mono.fx().create()
                .setPackageName("org.cict.accountmanager.faculty.layout")
                .setFxmlDocument("faculty-row")
                .makeFX()
                .pullOutLayout();

        VBox bg = searchAccessibilityText(facultyRow, "bg");
        Label lbl_id = searchAccessibilityText(facultyRow, "id");
        Label lbl_name = searchAccessibilityText(facultyRow, "name");
        Label lbl_access = searchAccessibilityText(facultyRow, "access");
        Label lbl_rank = searchAccessibilityText(facultyRow, "rank");
        Label lbl_dept = searchAccessibilityText(facultyRow, "dept");
        Label lbl_designation = searchAccessibilityText(facultyRow, "designation");

        lbl_id.setText(faculty.getBulsu_id().toUpperCase());
        lbl_name.setText(WordUtils.capitalizeFully(faculty.getFirst_name() + " " + faculty.getLast_name()));
        lbl_access.setText(currentFacultyInfo.getAccessLevel() == null || currentFacultyInfo.getAccessLevel().isEmpty() ? "NO ACCESS" : (currentFacultyInfo.getAccessLevel().equalsIgnoreCase(Access.ACCESS_CO_REGISTRAR) ? "Assistant Registrar".toUpperCase() : currentFacultyInfo.getAccessLevel().replace("_", " ")));
        lbl_rank.setText((faculty.getRank() == null || faculty.getRank().isEmpty()) ? "UNRANKED" : faculty.getRank());
        lbl_dept.setText((faculty.getDepartment() == null
                || faculty.getDepartment().isEmpty()
                || faculty.getDepartment().equals("NONE"))
                ? "NO"
                : faculty.getDepartment());
        lbl_designation.setText((faculty.getDesignation() == null || faculty.getDesignation().isEmpty()) ? "NONE" : faculty.getDesignation().replace("_", " "));

        SimpleTableCell cellParent = new SimpleTableCell();
        cellParent.setResizePriority(Priority.ALWAYS);
        cellParent.setContent(facultyRow);

        row.addCell(cellParent);

        row.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            createInfo(currentFacultyInfo, lbl_id, lbl_name, row);
            anchor_new_faculty.setVisible(false);
        });
        row.getRowMetaData().put(KEY_MORE_INFO, faculty);
        table.addRow(row);
    }

    private Date DATE_TODAY = Mono.orm().getServerTime().getDateWithFormat();
    private final String SECTION_BASE_COLOR = "#414852";

    private void createInfo(FacultyInformation currentFacultyInfo, Label lbl_bulsu_id,
            Label lbl_full_name,
            SimpleTableRow rowFaculty) {

        FacultyInfoController controller = new FacultyInfoController(currentFacultyInfo);

        LayoutDataFX homeFX = new LayoutDataFX(application_root, this);
        controller.setHomeFx(homeFX);

        Pane pane = Mono.fx().create()
                .setPackageName("org.cict.accountmanager.faculty.layout")
                .setFxmlDocument("faculty-info")
                .makeFX()
                .setController(controller)
                .pullOutLayout();

        super.setSceneColor(SECTION_BASE_COLOR); // call once on entire scene lifecycle

        Animate.fade(this.application_root, SectionConstants.FADE_SPEED, () -> {
            super.replaceRoot(pane);
        }, pane);
    }

    private void addNewFaculty() {
        String gender = "";
        if (rbtn_male_new.isSelected()) {
            gender = "MALE";
        } else if (rbtn_female_new.isSelected()) {
            gender = "FEMALE";
        }

        String bulsu_id = MonoString.removeExtraSpace(txt_bulsu_id.getText().toUpperCase()), lastname = MonoString.removeExtraSpace(txt_lastname.getText().toUpperCase()), firstname = MonoString.removeExtraSpace(txt_firstname.getText().toUpperCase()), middlename = MonoString.removeExtraSpace(txt_middlename.getText().toUpperCase()), rank = cmb_rank.getSelectionModel().getSelectedItem() == null ? "" : MonoString.removeExtraSpace(cmb_rank.getSelectionModel().getSelectedItem().toString().toUpperCase()), designation = cmb_des.getSelectionModel().getSelectedItem() == null ? "" : MonoString.removeExtraSpace(cmb_des.getSelectionModel().getSelectedItem().toString().toUpperCase()), dept = cmb_dept.getSelectionModel().getSelectedItem() == null ? "" : MonoString.removeExtraSpace(cmb_dept.getSelectionModel().getSelectedItem().toString().toUpperCase());

        if (bulsu_id.isEmpty() || lastname.isEmpty() || firstname.isEmpty()) {
            showWarning("Incomplete Data", "Please fill the required fields*.");
            return;
        }
        if (middlename == null) {
            middlename = "";
        }
        if (rank == null) {
            rank = "";
        }
        if (designation == null) {
            designation = "";
        }
        if (dept == null) {
            dept = "";
        }

        ArrayList<FacultyMapping> exist = Mono.orm().newSearch(Database.connect().faculty())
                .eq(DB.faculty().bulsu_id, bulsu_id)
                .execute().all();
        if (exist != null) {
            showWarning("BulSU ID Exist", "A faculty is already using\n"
                    + "this BulSU ID.");
            return;
        }
        FacultyMapping new_faculty = new FacultyMapping();
        new_faculty.setBulsu_id(bulsu_id);
        new_faculty.setLast_name(lastname);
        new_faculty.setFirst_name(firstname);
        new_faculty.setMiddle_name(middlename);
        new_faculty.setRank(rank);
        new_faculty.setDesignation(designation);
        new_faculty.setGender(gender);
        new_faculty.setDepartment(dept);
        int id = Database.connect().faculty().insert(new_faculty);
        if (id != -1) {
            Notifications.create().darkStyle()
                    .title("Added Successfully")
                    .text("Faculty: " + new_faculty.getBulsu_id().toUpperCase())
                    .showInformation();
            new_faculty = Mono.orm().newSearch(Database.connect().faculty())
                    .eq(DB.faculty().bulsu_id, bulsu_id)
                    .active().first();
            FacultyInformation fInfo = new FacultyInformation(new_faculty);
            activeFaculty.add(fInfo);
            createRow(fInfo, tableFaculty);
            txt_bulsu_id.setText("");
            txt_firstname.setText("");
            txt_lastname.setText("");
            txt_middlename.setText("");
            setComboBox();
        }
    }

    private void showWarning(String title, String text) {
        Notifications.create().darkStyle()
                .title(title)
                .text(text)
                .showWarning();
    }

    private boolean confirmCurrentPassword(PasswordField txt_update_pass, AccountFacultyMapping afMap) {
        String entered_pass = Mono.security().hashFactory().hash_sha512(txt_update_pass.getText());
        return (afMap.getPassword() == null ? entered_pass == null : afMap.getPassword().equals(entered_pass));
    }

    private boolean validateNewRecovery(AccountFacultyMapping afMap, String entered_old_ans, TextField txt_new_ans, TextField txt_confirm_ans) {
        boolean okToSave = false;
        String old_ans = afMap.getRecovery_answer();

        if (old_ans == null || old_ans.equals(entered_old_ans)) {
            String new_ans = MonoString.removeExtraSpace(txt_new_ans.getText().toUpperCase());
            String confirm_ans = MonoString.removeExtraSpace(txt_confirm_ans.getText().toUpperCase());
            if (new_ans == null ? confirm_ans == null : new_ans.equals(confirm_ans)) {
                okToSave = true;
            } else {
                showWarning("Change Recovery Information:", "New answer not matched to its confirmation.");
                System.out.println("NEW ANS NOT MATCHED");
            }
        } else {
            showWarning("Change Recovery Information:", "Please provide the old answer correctly.");
            System.out.println("OLD ANS NOT MATCHED");
        }
        return okToSave;
    }

    private boolean validateNewPass(AccountFacultyMapping afMap, TextField txt_confirm_new, String entered_new_pass, TextField txt_old_pass) {
        boolean okToSave = false;
        String old_pass = afMap.getPassword();
        String entered_old__pass = Mono.security().hashFactory().hash_sha512(txt_old_pass.getText());
        if (entered_old__pass == null ? old_pass == null : entered_old__pass.equals(old_pass)) {
            if (entered_new_pass.length() < 6) {
                showWarning("Change Password: Invalid Length", "Valid password length is at least six (6)\n"
                        + "characters.");
                System.out.println("LENGTH MUST BE AT LEAST 6");
            } else if (txt_confirm_new.getText().equals(entered_new_pass)) {
                okToSave = true;
            } else {
                showWarning("Change Password: New Password Not Matched", "New password not matched to its confirmation.");
                System.out.println("NEW PASS NOT MATCHED");
            }
        } else {
            showWarning("Change Password: Old Password Not Matched", "Please provide your correct password to change it.");
            System.out.println("OLD PASS NOT MATCHED");
        }
        return okToSave;
    }

    private boolean checkExist(String username, AccountFacultyMapping afMap) {
        boolean okToSave = false;
        ArrayList<AccountFacultyMapping> exist = Mono.orm().newSearch(Database.connect().account_faculty())
                .eq(DB.account_faculty().username, username)
                .execute().all();
        if (exist != null) {
            boolean reallyExist = false;
            for (AccountFacultyMapping af : exist) {
                if (!Objects.equals(af.getId(), afMap.getId())) {
                    System.out.println("USERNAME EXIST");
                    showWarning("Username Exist", "Please provide a unique username or\n"
                            + " just use your old one.");
                    reallyExist = true;
                    break;
                }
            }
            okToSave = !reallyExist;
        } else {
            okToSave = true;
        }
        return okToSave;
    }

    private void setAccessLevels(ComboBox cmb_access_level) {
        cmb_access_level.getItems().add("NONE");
        cmb_access_level.getItems().add("EVALUATOR");
        cmb_access_level.getItems().add("ADMINISTRATOR");
    }

    private void setComboBoxQuestions(ComboBox cmb_questions) {
        ArrayList<String> questions = new ArrayList<>();
        questions.add("What was your favorite place to visit as a child?");
        questions.add("Who is your favorite actor, musician, or artist?");
        questions.add("What is the name of your favorite pet?");
        questions.add("In what city were you born?");
        questions.add("Which is your favorite web browser?");
        questions.add("What is the name of your first school?");
        questions.add("What is your favorite movie?");
        questions.add("What is your mother's maiden name?");
        questions.add("What is your favorite color?");
        questions.add("What is your father's middle name?");
        cmb_questions.getItems().addAll(questions);
        cmb_questions.getSelectionModel().selectFirst();
    }

    private void setBasicInfo(Label lbl_id,
            Label lbl_name,
            Label lbl_gender,
            Label lbl_rank,
            Label lbl_designation,
            Label lbl_initial_letter,
            VBox bg,
            FacultyMapping faculty) {
        lbl_id.setText(faculty.getBulsu_id().toUpperCase());
        lbl_name.setText(WordUtils.capitalizeFully(faculty.getFirst_name() + " " + faculty.getLast_name()));
        lbl_gender.setText(WordUtils.capitalizeFully(faculty.getGender()));
        lbl_rank.setText(faculty.getRank().toUpperCase());
        lbl_designation.setText(WordUtils.capitalizeFully(faculty.getDesignation()));
        lbl_initial_letter.setText(faculty.getFirst_name().toUpperCase().charAt(0) + "");

        if (faculty.getGender().equalsIgnoreCase("FEMALE")) {
            bg.setStyle(bg.getStyle() + " -fx-background-color: #D64651;");
        }

    }

    private void setValues(TextField txt_id,
            TextField txt_lastname,
            TextField txt_firstname,
            TextField txt_middlename,
            RadioButton rbtn_male,
            RadioButton rbtn_female,
            TextField txt_rank,
            TextField txt_designation,
            FacultyMapping faculty) {
        txt_id.setText(faculty.getBulsu_id());
        txt_lastname.setText(faculty.getLast_name());
        txt_firstname.setText(faculty.getFirst_name());
        txt_middlename.setText(faculty.getMiddle_name());
        if (faculty.getGender().equalsIgnoreCase("MALE")) {
            rbtn_male.setSelected(true);
        } else if (faculty.getGender().equalsIgnoreCase("FEMALE")) {
            rbtn_female.setSelected(true);
        }
        txt_rank.setText(faculty.getRank());
        txt_designation.setText(faculty.getDesignation());
    }

    private void changeView(Node showThis) {
        Animate.fade(showThis, 150, () -> {
            vbox_no_found.setVisible(false);
            vbox_no_found1.setVisible(false);
            anchor_new_faculty.setVisible(false);
            vbox_list.setVisible(false);
            showThis.setVisible(true);
        }, vbox_no_found, vbox_no_found1, anchor_new_faculty, vbox_list);
    }

    private String NAME = "NAME", ID = "ID", DEPT = "DEPT";

    public void onSearch(boolean focus) {
        if (focus) {
            this.requestFocus(txt_search);
        }
        String key = MonoString.removeExtraSpace(txt_search.getText().toUpperCase());
        key = (key == null ? "" : key);
        if (key.isEmpty()) {
            if (cmb_sort.getSelectionModel().getSelectedIndex() == 0) {
                this.fetchFaculty(true);
            } else {
                this.fetchFaculty(false);
            }
            return;
        }
        ArrayList<FacultyInformation> temp_search;
        if (cmb_sort.getSelectionModel().getSelectedIndex() == 0) {
            search(key, true);
        } else {
            search(key, false);
        }
    }

    private String prefix;

    private void search(String key, boolean isActive) {
        String[] str = key.split(" ");
        prefix = str[0];
        ArrayList<FacultyMapping> facultyResults = null;
        if (prefix.equalsIgnoreCase(ID)) {
            try {
                reportDescription = "Faculty Search Result For ID " + str[1].toUpperCase();
                onSearchFaculty((str[1] == null ? "" : str[1]), isActive, ID);
            } catch (IndexOutOfBoundsException e) {
                reportDescription = "";
                changeView(vbox_no_found);
            }
        } else if (prefix.equalsIgnoreCase(DEPT) || prefix.equalsIgnoreCase("DEPARTMENT")) {
            try {
                reportDescription = "Faculty Search Result For Department " + str[1].toUpperCase();
                onSearchFaculty((str[1] == null ? "" : str[1]), isActive, DEPT);
            } catch (IndexOutOfBoundsException e) {
                reportDescription = "";
                changeView(vbox_no_found);
            }
        } else {
            reportDescription = "Faculty Search Result For Name " + WordUtils.capitalizeFully(key);
            onSearchFaculty(key, isActive, NAME);
        }
    }

    //------------------------------
    private void onSearchFaculty(String key, boolean isActive, String mode) {
        SearchFaculty searchTx = new SearchFaculty();
        searchTx.setSearchValue(key.trim());
        searchTx.setSearchMode(mode);
        searchTx.whenStarted(() -> {
            this.btn_search.setDisable(true);
            preview = null;
        });
        searchTx.whenCancelled(() -> {
            reportDescription = "";
            changeView(vbox_no_found);
        });
        searchTx.whenFailed(() -> {
            reportDescription = "";
            changeView(vbox_no_found);
        });
        searchTx.whenSuccess(() -> {
            ArrayList<FacultyInformation> temp_search = new ArrayList<>();
            ArrayList<FacultyMapping> facultyResults = searchTx.getFacultyList();

            if (facultyResults == null) {
                System.out.println("No found in search.");
                reportDescription = "";
                changeView(vbox_no_found);
                return;
            }
            for (FacultyMapping facultyResult : facultyResults) {
                FacultyInformation info = new FacultyInformation(facultyResult);
                if (isActive) {
                    if (info.isAccountActived()) {
                        temp_search.add(info);
                    }
                } else {
                    if (!info.isAccountActived()) {
                        temp_search.add(info);
                    }
                }
            }
            if (temp_search.isEmpty()) {
                reportDescription = "";
                changeView(vbox_no_found);
                return;
            }
            createFacultyTable(temp_search);
            changeView(vbox_list);
        });
        searchTx.whenFinished(() -> {
            this.btn_search.setDisable(false);
        });

        searchTx.transact();
    }

    public static class SearchFaculty extends Transaction {

        private String searchValue;

        public void setSearchValue(String searchValue) {
            this.searchValue = searchValue;
        }

        private String mode;

        public void setSearchMode(String mode) {
            this.mode = mode;
        }

        protected ArrayList<FacultyMapping> facultyList = new ArrayList<>();

        public ArrayList<FacultyMapping> getFacultyList() {
            return facultyList;
        }

        @Override
        protected boolean transaction() {
            Searcher searchFaculty = Mono.orm()
                    .newSearch(Database.connect().faculty())
                    .pull();
            facultyList = this.recursiveQuery(searchFaculty)
                    .execute()
                    .all();
            return true;
        }

        private Searcher recursiveQuery(Searcher searchQuery) {
            if (mode.equalsIgnoreCase("NAME")) {
                for (String textPart : this.searchValue.split(" ")) {
                    if (textPart.isEmpty()) {
                        continue;
                    }
                    searchQuery.put(forAllNames(textPart));
                }
            } else if (mode.equalsIgnoreCase("ID")) {
                searchQuery.put(SQL.or(Restrictions.ilike(DB.faculty().bulsu_id, (searchValue), MatchMode.ANYWHERE)));
            } else if (mode.equalsIgnoreCase("DEPT")) {
                searchQuery.put(SQL.or(Restrictions.ilike(DB.faculty().department, (searchValue), MatchMode.ANYWHERE)));
            }
            return searchQuery.pull();
        }

        private Criterion forAllNames(String textPart) {
            return SQL.or(
                    Restrictions.ilike(DB.faculty().first_name, textPart, MatchMode.ANYWHERE),
                    Restrictions.ilike(DB.faculty().middle_name, textPart, MatchMode.ANYWHERE),
                    Restrictions.ilike(DB.faculty().last_name, textPart, MatchMode.ANYWHERE)
            );
        }

        @Override
        protected void after() {

        }

    }
}

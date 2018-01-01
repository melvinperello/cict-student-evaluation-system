package org.bsu.cict.dev.profile;

import app.lazy.models.Database;
import app.lazy.models.StudentMapping;
import app.lazy.models.StudentProfileMapping;
import artifacts.MonoString;
import com.jhmvin.Mono;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.EmailValidator;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * A Class to import student information from the spreadsheet.
 *
 * @author Jhon Melvin
 */
public class ReadExcelProfile {

    /**
     * Excel Columns.
     */
    public final static int COL_STUDENT_NUMBER = 0,
            COL_LAST_NAME = 1,
            COL_FIRST_NAME = 2,
            COL_MIDDLE_NAME = 3,
            COL_YEAR = 4,
            COL_SECTION = 5,
            COL_GROUP = 6,
            COL_ADDRESS = 7,
            COL_MOBILE = 8,
            COL_EMAIL = 9,
            COL_GUARDIAN = 10,
            COL_GUARDIAN_MOBILE = 11,
            COL_GUARDIAN_ADDRESS = 12;

    private final static int LAST_COLUMN = 12;

    /**
     * Reserved column for inserting remarks.
     */
    private final static int COL_REMARKS = 13;

    /**
     * Excel Headers.
     */
    private final static String[] HEADERS = new String[]{
        "STUDENT NUMBER",
        "LAST NAME",
        "FIRST NAME",
        "MIDDLE NAME",
        "YEAR",
        "SECTION",
        "GROUP",
        "ADDRESS",
        "MOBILE",
        "EMAIL",
        "GUARDIAN",
        "GUARDIAN MOBILE",
        "GUARDIAN ADDRESS"
    };

    /**
     * Default Constructor.
     */
    public ReadExcelProfile() {
        //
    }

    public static void main(String[] args) {
        ReadExcelProfile reader = new ReadExcelProfile();
        try {
            reader.readExcelFile("C:\\Users\\Jhon Melvin\\Desktop\\4A-G1.xlsx");
        } catch (IOException e) {
            // cant open
            e.printStackTrace();
        } catch (ReadExcelProfile.InvalidSpreadSheetHeaderException he) {
            // invalid header
            he.printStackTrace();
        }

    }

    public void readExcelFile(String path) throws InvalidSpreadSheetHeaderException, IOException {
        this.open(path);
        this.selectFirstSheet();
        this.checkHeaders();
        this.readEntries();
        this.save();
    }

    private XSSFWorkbook spreadSheet;
    private XSSFSheet excelSheet;

    /**
     * Open the excel file and save its instance.
     *
     * @param filePath
     * @throws IOException
     */
    private void open(String filePath) throws IOException {
        FileInputStream file = null;
        try {
            // open the file
            file = new FileInputStream(new File(filePath));
            // Get the workbook instance for XLS file
            this.spreadSheet = new XSSFWorkbook(file);
            // close the file
        } finally {
            try {
                if (file != null) {
                    file.close();
                }
            } catch (IOException e) {
                // ignore closing exception
            }
        }
    }

    /**
     * Save the spreadsheet document.
     *
     * @param spreadSheet
     * @param filePath
     * @throws IOException
     */
    public void save() throws IOException {
        // create a file
        FileOutputStream saveFile = new FileOutputStream("C:\\Users\\Jhon Melvin\\Desktop\\file.xlsx");
        // write this excel instance to file.
        this.spreadSheet.write(saveFile);
        // close the file
        saveFile.close();
    }

    /**
     * Select the first sheet.
     */
    private void selectFirstSheet() {
        this.excelSheet = this.spreadSheet.getSheetAt(0);
    }

    /**
     * Gets the string value of a cellIndex.
     *
     * @param excelCell
     * @return
     */
    private String getCellStringValue(XSSFCell excelCell) {
        DataFormatter fmt = new DataFormatter();
        String value = fmt.formatCellValue(excelCell);
        return value;
    }

    /**
     * Set background color to a cell.
     *
     * @param cell
     * @param rgbColor
     */
    private void setCellColor(XSSFCell cell, int[] rgb) {
        XSSFCellStyle style = this.spreadSheet.createCellStyle();
        // create style from spreadsheet
        /**
         * RGB Color selection.
         */
        Color awtColor = new Color(rgb[0], rgb[1], rgb[2]);
        XSSFColor myColor = new XSSFColor(awtColor);
        style.setFillForegroundColor(myColor);
        //style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setAlignment(HorizontalAlignment.CENTER);
        cell.setCellStyle(style);
    }

    /**
     * Class throw for invalid header.
     */
    public static class InvalidSpreadSheetHeaderException extends RuntimeException {

        public InvalidSpreadSheetHeaderException(String message) {
            super(message);
        }

    }

    /**
     * Checks the headers.
     *
     * @param sheet
     * @param headers
     * @throws ReadExcelProfile.InvalidSpreadSheetHeaderException
     */
    private void checkHeaders() throws ReadExcelProfile.InvalidSpreadSheetHeaderException {
        XSSFSheet sheet = this.excelSheet;
        String[] headers = HEADERS;
        //
        if (sheet.getLastRowNum() == 0) {
            throw new InvalidSpreadSheetHeaderException("No Headers Found");
        }
        XSSFRow spreadSheetRow = sheet.getRow(0);
        if (spreadSheetRow.getLastCellNum() != headers.length) {
            // header count do not match
            throw new InvalidSpreadSheetHeaderException("Spreadsheet Column Count Does Not Match");
        }

        for (int headerIndex = 0; headerIndex < headers.length; headerIndex++) {
            try {
                XSSFCell cell = spreadSheetRow.getCell(headerIndex);
                String cellValue = getCellStringValue(cell);
                String headerValue = headers[headerIndex];
                if (!cellValue.equalsIgnoreCase(headerValue)) {
                    throw new InvalidSpreadSheetHeaderException("Invalid Header " + cellValue);
                }
            } catch (IndexOutOfBoundsException e) {
                throw new InvalidSpreadSheetHeaderException("Header " + headers[headerIndex] + " was not found on the expected column");
            }
        }
    }

    private ReadExcelProfile.ProfileObject studentProfile;

    private final static int[] RGB_ERROR = new int[]{247, 108, 131};
    private final static int[] RGB_FINE = new int[]{97, 221, 187};

    /**
     * Read the contents of the excel file.
     */
    public void readEntries() {
        System.out.println(this.excelSheet.getLastRowNum());
        rowLoop:
        for (int row = 1; row <= this.excelSheet.getLastRowNum(); row++) {
            XSSFRow currentRow = this.excelSheet.getRow(row);
            //------------------------------------------------------------------
            // every row is a profile entry
            this.studentProfile = new ReadExcelProfile.ProfileObject();
            //------------------------------------------------------------------
            for (int col = 0; col <= LAST_COLUMN; col++) {
                XSSFCell currentCol = currentRow.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                if (currentCol == null) {
                    createRemarksColumn(currentRow, "Contains Blank Field", RGB_ERROR);
                    continue rowLoop; // skip this blank cell
                }
                String cellValue = this.getCellStringValue(currentCol);
                // validate contents of the spreadsheet.
                String remarks = validateContent(col, cellValue);
                // check validation remarks
                if (remarks.equalsIgnoreCase("OK")) {
                    createRemarksColumn(currentRow, "VALIDATED", RGB_FINE);
                } else {
                    createRemarksColumn(currentRow, remarks, RGB_ERROR);
                    continue rowLoop;
                }
            }
            // code will only proceed here if OK
            // for insertion in the database
            //------------------------------------------------------------------
            // Insert To Database
            //------------------------------------------------------------------
            this.insertToDb();
        }
    }

    /**
     * Insert Current Row to Database.
     */
    public void insertToDb() {
        final Date sysDate = Mono.orm().getServerTime().getDateWithFormat();

        StudentMapping studentMap = new StudentMapping();
        studentMap.setId(this.studentProfile.getStudentNumber());
        studentMap.setLast_name(this.studentProfile.getLastName());
        studentMap.setFirst_name(this.studentProfile.getFirstName());
        studentMap.setMiddle_name(this.studentProfile.getMiddleName());
        studentMap.setYear_level(Integer.valueOf(this.studentProfile.getYear()));
        studentMap.setSection(this.studentProfile.getSection());
        studentMap.set_group(Integer.valueOf(this.studentProfile.getGroup()));
        //----------------------------------------------------------------------
        studentMap.setHas_profile(1);
        studentMap.setGender(null);
        studentMap.setCreated_by("PROFILE UPLOAD");
        studentMap.setCreated_date(sysDate);
        studentMap.setUpdated_by("PROFILE UPLOAD");
        studentMap.setUpdated_date(sysDate);
        //----------------------------------------------------------------------
        StudentProfileMapping studentProfile = new StudentProfileMapping();

    }

    /**
     * Creates a remarks cell.
     *
     * @param currentRow
     * @param remarks
     * @param color
     */
    private void createRemarksColumn(XSSFRow currentRow, String remarks, int[] color) {
        XSSFCell remarksCell = currentRow.createCell(COL_REMARKS);
        remarksCell.setCellValue(remarks);
        this.setCellColor(remarksCell, color);
    }

    private String validateContent(int col, String value) {
        switch (col) {
            case COL_STUDENT_NUMBER:
                return validateStudentNumber(value);
            case COL_LAST_NAME:
                return validateName(value, "Last Name");
            case COL_FIRST_NAME:
                return validateName(value, "First Name");
            case COL_MIDDLE_NAME:
                return validateName(value, "Middle Name");
            case COL_YEAR:
                return validateYearLevel(value);
            case COL_SECTION:
                return validateSection(value);
            case COL_GROUP:
                return validateGroup(value);
            case COL_ADDRESS:
                return validateAddress(value, "Student Address");
            case COL_MOBILE:
                return validateMobileNumber(value, "Student Mobile");
            case COL_EMAIL:
                return validateEmail(value);
            case COL_GUARDIAN:
                return validateName(value, "Guardian Name");
            case COL_GUARDIAN_MOBILE:
                return validateMobileNumber(value, "Guardian Mobile");
            case COL_GUARDIAN_ADDRESS:
                return validateAddress(value, "Guardian Address");
            default:
                return "NOT_OK";
        }

    }

    private String removeCharacters(String string, char... removeChars) {
        String tempString = "";
        outLoop:
        for (char c : string.toCharArray()) {
            // if contains in the char remove continue to next character
            for (char charchar : removeChars) {
                if (c == charchar) {
                    continue outLoop;
                }
            }
            tempString += (Character.toString(c));
        }
        return tempString;
    }

    /**
     * Validates the student Number.
     *
     * @param studentNumber
     * @return
     */
    private String validateStudentNumber(String studentNumber) {
        if (studentNumber.isEmpty()) {
            return "No Student Number";
        }
        // removed dashes
        studentNumber = removeCharacters(studentNumber, ' ', '-');

        //----------------------------------------------------------------------
        // insert field
        this.studentProfile.setStudentNumber(studentNumber);
        //----------------------------------------------------------------------
        // check if alphanumeric
        boolean is_alpha_numeric = StringUtils.isAlphanumeric(studentNumber);
        if (is_alpha_numeric) {
            return "OK";
        } else {
            return "Invalid Student Number";
        }
    }

    /**
     * Validates the student name including last name first name and middle
     * name.
     *
     * @param studentName
     * @param name first_name, last_name middle_name and guardian_name
     * @return
     */
    private String validateName(String studentName, String field) {

        if (studentName.isEmpty() && (!field.equalsIgnoreCase("Middle Name"))) {
            return "No " + field;
        }

        if (field.equalsIgnoreCase("Middle Name")) {
            if (studentName.equalsIgnoreCase("N/A")) {
                studentName = "";
            } else if (studentName.equalsIgnoreCase("NONE")) {
                studentName = "";
            }
        }

        studentName = removeCharacters(studentName, '.', ',');
        studentName = MonoString.removeExtraSpace(studentName);
        //----------------------------------------------------------------------
        // insert field
        if (field.equalsIgnoreCase("Last Name")) {
            this.studentProfile.setLastName(studentName);
        } else if (field.equalsIgnoreCase("First Name")) {
            this.studentProfile.setFirstName(studentName);
        } else if (field.equalsIgnoreCase("Middle Name")) {
            this.studentProfile.setMiddleName(studentName);
        } else if (field.equalsIgnoreCase("Guardian Name")) {
            this.studentProfile.setGuardian(studentName);
        }
        //----------------------------------------------------------------------
        // dashes are allowed
        String studentNameWithoutDash = removeCharacters(studentName, '-');

        boolean is_alpha_space = StringUtils.isAlphaSpace(studentNameWithoutDash);
        if (is_alpha_space) {
            return "OK";
        } else {
            return "Invalid " + field;
        }
    }

    /**
     * Validate the year level of the student.
     *
     * @param yearLevel
     * @return
     */
    private String validateYearLevel(String yearLevel) {
        String validYear = "1234";
        boolean valid = false;
        for (Character c : validYear.toCharArray()) {
            if (yearLevel.trim().equals(c.toString())) {
                valid = true;
                break;
            }
        }

        this.studentProfile.setYear(yearLevel);
        if (valid) {
            return "OK";
        } else {
            return "Invalid Year Level";
        }
    }

    /**
     * Validate the section of the student.
     *
     * @param section
     * @return
     */
    private String validateSection(String section) {
        boolean valid = false;
        String validSections = "abcdefghijklmnopqrstuvwxyz";
        for (Character c : validSections.toCharArray()) {
            if (section.toLowerCase(Locale.ENGLISH).trim().equals(c.toString())) {
                valid = true;
                break;
            }
        }

        this.studentProfile.setSection(section);

        if (valid) {
            return "OK";
        } else {
            return "Invalid Section";
        }
    }

    /**
     * Validate the group of the student.
     *
     * @param group
     * @return
     */
    private String validateGroup(String group) {
        boolean valid = false;
        String validSections = "12";
        for (Character c : validSections.toCharArray()) {
            if (group.trim().equals(c.toString())) {
                valid = true;
                break;
            }
        }

        this.studentProfile.setGroup(group);

        if (valid) {
            return "OK";
        } else {
            return "Invalid Section Group";
        }
    }

    /**
     * Address Validation.
     *
     * @param studentAddress
     * @return
     */
    private String validateAddress(String studentAddress, String field) {
        if (studentAddress.isEmpty()) {
            return "No " + field;
        }

        //----------------------------------------------------------------------
        // insert field
        if (field.equalsIgnoreCase("Student Address")) {
            this.studentProfile.setAddress(studentAddress);
        } else if (field.equalsIgnoreCase("Guardian Address")) {
            this.studentProfile.setGuardianAddress(studentAddress);
        }
        //----------------------------------------------------------------------

        return "OK";
    }

    /**
     * Validate mobile number accepts (936) (+6396) (6396) (0936)
     *
     * @param mobileNumber
     * @return
     */
    private String validateMobileNumber(String mobileNumber, String field) {
        if (mobileNumber.isEmpty()) {
            return "No " + field;
        }
        // filter characters
        mobileNumber = removeCharacters(mobileNumber, '.', ',', '-', ' ', '+');

        boolean is_numeric = StringUtils.isNumeric(mobileNumber);

        if (!is_numeric) {
            return "Invalid " + field;
        }

        //----------------------------------------------------------------------
        String tempNumber = "";
        if (mobileNumber.length() == 10 && mobileNumber.charAt(0) == '9') {
            tempNumber = "0" + mobileNumber;
        } else if (mobileNumber.length() == 12
                && mobileNumber.charAt(0) == '6'
                && mobileNumber.charAt(1) == '3'
                && mobileNumber.charAt(2) == '9') {
            tempNumber = "0" + mobileNumber.substring(2, mobileNumber.length());
        } else if (mobileNumber.length() == 11
                && mobileNumber.charAt(0) == '0'
                && mobileNumber.charAt(1) == '9') {
            tempNumber = mobileNumber;
        } else {
            return "Invalid " + field;
        }

        //----------------------------------------------------------------------
        // insert field
        if (field.equalsIgnoreCase("Student Mobile")) {
            this.studentProfile.setMobile(tempNumber);
        } else if (field.equalsIgnoreCase("Guardian Mobile")) {
            this.studentProfile.setGuardianMobile(tempNumber);
        }
        //----------------------------------------------------------------------

        return "OK";
    }

    /**
     * Validates Email Address.
     *
     * @param email
     * @return
     */
    private String validateEmail(String email) {
        EmailValidator validator = EmailValidator.getInstance();

        this.studentProfile.setEmail(email);
        if (validator.isValid(email)) {
            // is valid, do something
            return "OK";
        } else {
            // is invalid, do something
            return "Invalid Email";
        }
    }

    public static class ProfileObject {

        private String charLimit(String str, int limit) {
            if (str.length() > limit) {
                return str.substring(0, limit - 1);
            }
            return str;
        }

        private String studentNumber,
                lastName,
                firstName,
                middleName,
                year,
                section,
                group,
                address,
                mobile,
                email,
                guardian,
                guardianMobile,
                guardianAddress;

        public String getStudentNumber() {
            return studentNumber;
        }

        public void setStudentNumber(String studentNumber) {
            this.studentNumber = charLimit(studentNumber, 50);
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = charLimit(lastName, 100);
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = charLimit(firstName, 100);
        }

        public String getMiddleName() {
            return middleName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = charLimit(middleName, 100);
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getSection() {
            return section;
        }

        public void setSection(String section) {
            this.section = section;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = charLimit(address, 300);
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = charLimit(email, 100);
        }

        public String getGuardian() {
            return guardian;
        }

        public void setGuardian(String guardian) {
            this.guardian = charLimit(guardian, 100);
        }

        public String getGuardianMobile() {
            return guardianMobile;
        }

        public void setGuardianMobile(String guardianMobile) {
            this.guardianMobile = guardianMobile;
        }

        public String getGuardianAddress() {
            return guardianAddress;
        }

        public void setGuardianAddress(String guardianAddress) {
            this.guardianAddress = charLimit(guardianAddress, 300);
        }

    } // end of profile class

}

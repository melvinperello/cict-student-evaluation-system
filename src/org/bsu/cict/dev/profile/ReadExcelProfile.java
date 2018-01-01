package org.bsu.cict.dev.profile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
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
            reader.readExcelFile("C:\\Users\\Jhon Melvin\\Desktop\\4H-G2.xlsx");
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

    /**
     * Read the contents of the excel file.
     */
    public void readEntries() {
        for (int row = 1; row <= this.excelSheet.getLastRowNum(); row++) {
        }
    }

    /**
     * Class throw for invalid header.
     */
    public static class InvalidSpreadSheetHeaderException extends RuntimeException {

        public InvalidSpreadSheetHeaderException(String message) {
            super(message);
        }

    }
}

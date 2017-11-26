/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package update3.org.excelreader;

import com.jhmvin.docs.SpreadSheetUtils;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Define A Class for an excel reader for a specific file. this class is for
 * reading the sample file.
 *
 * @author Jhon Melvin
 */
public class ExcelRead {

    // The path for the excel file
    private String excelPath;
    // the loaded excel file
    private XSSFWorkbook excelWorkBook;
    // sheet of the excel file
    private XSSFSheet excelSheet;

    public void setExcelPath(String excelPath) {
        this.excelPath = excelPath;
    }

    //--------------------------------------------------------------------------
    // Define the excel columns
    public final int COL_STUDENT_NUMBER = 0; // begin with 0
    public final int COL_FULL_NAME = 1;
    public final static int COL_GRADE = 2;
    public final static int COL_CLEARANCE = 3;

    /**
     * Loads the excel file in the excelWorkBook Variable.
     *
     * @return
     */
    public boolean loadExcel() {
        try {
            this.excelWorkBook = SpreadSheetUtils.openSpreadSheet(this.excelPath);
            this.excelSheet = this.excelWorkBook.getSheetAt(0);
        } catch (IOException io) {
            return false;
        }
        if (this.excelSheet == null) {
            return false;
        }
        // Excel file successfully loaded.
        return true;
    }

    /**
     * Checks the headers of the excel file to make sure the the reader is
     * reading at the right columns.
     *
     * @return
     */
    public boolean checkHeader() {
        // get the header row it is always the first row
        XSSFRow headerRow = this.excelSheet.getRow(0);
        // Spreadheet Utils is part of the mono-fw
        String headerStudentNumber = SpreadSheetUtils.getCellValue(headerRow.getCell(COL_STUDENT_NUMBER));
        String headerName = SpreadSheetUtils.getCellValue(headerRow.getCell(COL_FULL_NAME));
        String headerGrade = SpreadSheetUtils.getCellValue(headerRow.getCell(COL_GRADE));
//        String headerClearance = SpreadSheetUtils.getCellValue(headerRow.getCell(COL_CLEARANCE));

        if (!headerStudentNumber.equalsIgnoreCase("STUDENT NUMBER")) {
            return false;
        }

        if (!headerName.equalsIgnoreCase("FULL NAME")) {
            return false;
        }
        // headers are placed correctly
        
        if (!headerGrade.equalsIgnoreCase("GRADE")) {
            return false;
        }
        
//        if (!headerClearance.equalsIgnoreCase("CLEARED")) {
//            return false;
//        }
        return true;
    }

    public ArrayList<ReadData> readData() {
        // where the data will be stored
        ArrayList<ReadData> readDatas = new ArrayList<>();
        
        // get the last row of this excel sheet
        int last_row = this.excelSheet.getLastRowNum(); // 0 base if the total was 20 this will give 19
        if (last_row == 0) {
            // no contents to be read
            return null;
        }

        for (int x = 1 /* Start at one because 0 is the header */; x < (last_row + 1); x++) {
            // READING PART
            // get the current row
            XSSFRow row = this.excelSheet.getRow(x);

            String studentNumber = SpreadSheetUtils.getCellValue(row.getCell(COL_STUDENT_NUMBER));
            String fullName = SpreadSheetUtils.getCellValue(row.getCell(COL_FULL_NAME));
            String grade = SpreadSheetUtils.getCellValue(row.getCell(COL_GRADE));
//            String clearance = SpreadSheetUtils.getCellValue(row.getCell(COL_CLEARANCE));

            System.out.println(studentNumber);
            System.out.println(fullName);
            
            // store the readable data
            ReadData readData = new ReadData();
            readData.setSTUDENT_NUMBER(studentNumber);
            readData.setSTUDENT_NAME(fullName);
            readData.setSTUDENT_GRADE(grade);
//            readData.setSTUDENT_CLEARANCE(clearance);
            
            readDatas.add(readData);
        }
        return readDatas;
    }

}

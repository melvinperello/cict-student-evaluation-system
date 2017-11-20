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
package update3.org.excelprinter;

import com.jhmvin.docs.SpreadSheetUtils;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cict.reports.ReportsDirectory;

/**
 *
 * @author Jhon Melvin
 */
public class StudentMasterListPrinter {

    public final static int COL_STUDENT_NUMBER = 0;
    public final static int COL_STUDENT_NAME = 1;
    public final static int COL_GRADE = 2;
    public final static int COL_CLEARANCE = 3;

    public void logs(Object message) {
        System.out.println(message.toString());
    }

    private String excelPath = "reports/excel";
    private String excelName = "";
    private XSSFSheet excelSheet;
    private XSSFWorkbook excelWorkBook;

    /**
     * Sets the excel file name. please do not include the extension.
     *
     * @param excelName
     */
    private void setExcelName(String excelName) {
        this.excelName = excelName;
    }

    private boolean createExcel() {
        this.excelWorkBook = new XSSFWorkbook();
        this.excelSheet = this.excelWorkBook.createSheet("Students");

        if (!ReportsDirectory.check(excelPath)) {
            System.err.println("Cannot Create Directory.");
            return false;
        } else {
            System.out.println("Directory Created");
            this.excelPath = new File(excelPath).getAbsolutePath()
                    + "/"
                    + this.excelName + " "
                    + String.valueOf(Calendar.getInstance().getTimeInMillis())
                    + ".xlsx";
            return true;
        }
    }

    private void printHeader() {
        Row row = this.excelSheet.createRow(0);
        // write details.
        Cell id = row.createCell(COL_STUDENT_NUMBER);
        id.setCellValue("STUDENT NUMBER");
        //
        Cell name = row.createCell(COL_STUDENT_NAME);
        name.setCellValue("FULL NAME");
        //
        Cell section = row.createCell(COL_GRADE);
        section.setCellValue("GRADE");
        //
        Cell clearance = row.createCell(COL_CLEARANCE);
        clearance.setCellValue("CLEARANCE");
    }

    private void printData(ArrayList<StudentMasterListData> data) {
        int rowPointer = 1;
        for (StudentMasterListData student : data) {
            Row row = this.excelSheet.createRow(rowPointer);
            // write details.
            Cell id = row.createCell(COL_STUDENT_NUMBER);
            id.setCellValue(student.getStudentNumber());
            //
            Cell name = row.createCell(COL_STUDENT_NAME);
            name.setCellValue(student.getFullName());
            //
            Cell section = row.createCell(COL_GRADE);
            section.setCellValue("");

            Cell clearance = row.createCell(COL_CLEARANCE);
            clearance.setCellValue("");
            //
            rowPointer++;
        }
    }

    private boolean saveChanges() {
        try {
            SpreadSheetUtils.saveSpreadSheet(this.excelWorkBook, this.excelPath);
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private boolean openExcel() {
        if (Desktop.isDesktopSupported()) {
            try {
                File myFile = new File(this.excelPath);
                Desktop.getDesktop().open(myFile);
                return true;
            } catch (IOException ex) {
                // no application registered for PDFs
                return false;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        ArrayList<StudentMasterListData> studentData = new ArrayList<>();
        studentData.add(new StudentMasterListData("2014113844", "PERELLO, JHON MELVIN NIETO"));
        studentData.add(new StudentMasterListData("2014112470", "MERCADO, ELYSSA JELYN CARLOS"));
        studentData.add(new StudentMasterListData("2014123456", "DAZO, ARLEALYN"));

        StudentMasterListPrinter.export("SY 2017 2018 BSIT 1A G2 COMP 113",
                studentData);
    }

    public static boolean export(String fileName, ArrayList<StudentMasterListData> data) {
        StudentMasterListPrinter print = new StudentMasterListPrinter();
        print.setExcelName(fileName);

        if (!print.createExcel()) {
            System.err.println("CANNOT CREATE DIRECTORY");
            return false;
        }

        print.printHeader();
        print.printData(data);

        if (!print.saveChanges()) {
            return false;
        }

        if (!print.openExcel()) {
            return false;
        }

        return true;
    }

}

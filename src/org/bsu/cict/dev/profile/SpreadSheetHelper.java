/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bsu.cict.dev.profile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Jhon Melvin
 */
public class SpreadSheetHelper {

    /**
     * Open a spreadsheet file.
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static XSSFWorkbook open(String filePath) throws IOException {
        // open the file
        FileInputStream file = new FileInputStream(new File(filePath));
        // Get the workbook instance for XLS file
        XSSFWorkbook spreadSheet = new XSSFWorkbook(file);
        // close the file
        file.close();
        return spreadSheet;
    }

    /**
     * Save the spreadsheet document.
     *
     * @param spreadSheet
     * @param filePath
     * @throws IOException
     */
    public static void save(XSSFWorkbook spreadSheet, String filePath) throws IOException {
        // create a file
        FileOutputStream saveFile = new FileOutputStream(filePath);
        // write this excel instance to file.
        spreadSheet.write(saveFile);
        // close the file
        saveFile.close();
    }

    /**
     * Gets the string value of a cellIndex.
     *
     * @param excelCell
     * @return
     */
    public static String getCellStringValue(XSSFCell excelCell) {
        DataFormatter fmt = new DataFormatter();
        String value = fmt.formatCellValue(excelCell);
        return value;
    }

    public static void checkHeaders(XSSFSheet sheet, String... headers) throws InvalidSpreadSheetHeaderException {
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

    public static void readSpreadSheet(XSSFSheet sheet, OnReadSheetHandler handler) {
        // iterate all over the rows of the spreadsheet
        for (int row = 0; row < sheet.getLastRowNum(); row++) {
            // get the current iterative row
            XSSFRow spreadSheetRow = sheet.getRow(row);
            // iterate all over the cells of the excel row
            handler.read(row, spreadSheetRow);
        }
    }

    public static void readSpreadSheetRow(XSSFRow spreadSheetRow, OnReadRowHandler handler) {
        for (int cellIndex = 0; cellIndex < spreadSheetRow.getLastCellNum(); cellIndex++) {
            // get the current cellIndex of this row
            XSSFCell currentCell = spreadSheetRow.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            String cellStringValue = SpreadSheetHelper.getCellStringValue(currentCell);
            handler.read(cellIndex, currentCell, cellStringValue);
        }
    }

    @FunctionalInterface
    public interface OnReadSheetHandler {

        void read(int index, XSSFRow row);
    }

    @FunctionalInterface
    public interface OnReadRowHandler {

        void read(int index, XSSFCell currentCell, String cellStringValue);
    }

}

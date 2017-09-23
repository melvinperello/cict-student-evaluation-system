/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jhmvin.docs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Jhon Melvin
 */
public class SpreadSheetUtils {

    public static XSSFWorkbook openSpreadSheet(String filePath) throws IOException {
        // open the file
        FileInputStream file = new FileInputStream(new File(filePath));
        // Get the workbook instance for XLS file
        XSSFWorkbook spreadSheet = new XSSFWorkbook(file);
        // close the file
        file.close();
        return spreadSheet;
    }

    public static void saveSpreadSheet(XSSFWorkbook spreadSheet, String filePath) throws IOException {
        // create a file
        FileOutputStream saveFile = new FileOutputStream(filePath);
        // write this excel instance to file.
        spreadSheet.write(saveFile);
        // close the file
        saveFile.close();
    }

    public static XSSFCellStyle setCellBackground(XSSFCellStyle style, short color) {
        // create style from spreadsheet
        style.setFillForegroundColor(color);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    public static String getCellValue(Cell excelCell) {
        //CellType cellType = excelCell.getCellTypeEnum();
        String cellValue = "";
//        switch (cellType) {
//            //
//            case BLANK:
//                break;
//            case BOOLEAN:
//                //
//                cellValue = Boolean.toString(excelCell.getBooleanCellValue());
//                break;
//            case ERROR:
//                //
//                cellValue = Byte.toString(excelCell.getErrorCellValue());
//                break;
//            case FORMULA:
//                //
//                cellValue = excelCell.getCellFormula();
//                break;
//            case NUMERIC:
//                //
//                cellValue = Double.toString(excelCell.getNumericCellValue());
//
//                break;
//            case STRING:
//                //
//                cellValue = excelCell.getStringCellValue();
//                break;
//            //
//            case _NONE:
//                break;
//            default:
//                break;
//        }
        /**
         * Gets the value of the cell in strong format regardless of cell type.
         */
        DataFormatter fmt = new DataFormatter();
        String value = fmt.formatCellValue(excelCell);
        //
        cellValue = value;
        //
        return cellValue.trim();
    }

}

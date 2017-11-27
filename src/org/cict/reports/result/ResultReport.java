package org.cict.reports.result;

import app.lazy.models.SubjectMapping;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import java.io.FileOutputStream;
import java.io.IOException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import org.cict.SubjectClassification;
import org.cict.reports.ReportsUtility;

public class ResultReport {

    /**
     * Path to the resulting PDF file.
     */
    public static String RESULT;

    public ResultReport( String res) {
        RESULT = res;
    }
    
    private Document documentFormat;

    public void setDocumentFormat(Document documentFormat) {
        this.documentFormat = documentFormat;
    }

    public int print() {
        try {
            init();
            int val = createPdf(RESULT);
            if(val==1)
                System.out.println("Please close the previous report");
            /* -------- run the created pdf */
            if (Desktop.isDesktopSupported()) {
                try {

                    File myFile = new File(RESULT);
                    Desktop.getDesktop().open(myFile);
                } catch (IOException ex) {
                    // no application registered for PDFs
                }
            }
            /**
             * ****************** end run
             */
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    /**
     * PUBLIC VARIABLES
     */
    public String STUDENT_NAME = null,
            STUDENT_ADDRESS = null,
            STUDENT_NUMBER = null,
            REPORT_TITLE = null,
            DATETIME = "DATE NOT FOUND",
            USER = "USER NOT FOUND",
            REPORT_DESCRIPTION = null,
            TERMINAL = null,
            REPORT_OTHER_DETAIL = null; 
    public String[] COLUMN_NAMES;
    public ArrayList<String[]> ROW_DETAILS;
    public void init() {
        
    }
    
    private static PdfWriter writer;
    /**
     * Creates a PDF document.
     *
     * @param filename the path to the new PDF document
     * @throws DocumentException
     * @throws IOException
     */
    public int createPdf(String filename)
            throws DocumentException, IOException {
        Document document = this.documentFormat==null? ReportsUtility.createLongDocument() : this.documentFormat;
        try{
            writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
            PageFooter event = new PageFooter((DATETIME==null? "NOT FOUND" : DATETIME), (USER==null? "NOT FOUND" : USER), (TERMINAL==null? "NOT FOUND" : TERMINAL));
            writer.setPageEvent(event);
        }catch(FileNotFoundException es){
            return 1;
        }
        document.open();
        ReportsUtility.createHeader(document, this.REPORT_TITLE, this.REPORT_DESCRIPTION, this.REPORT_OTHER_DETAIL);
        if(STUDENT_NAME!=null)
            ReportsUtility.createStudentInfoHeader(document, STUDENT_NAME, STUDENT_NUMBER, this.STUDENT_ADDRESS);
        
        document.add(ReportsUtility.createPdfPTable(COLUMN_NAMES.length, COLUMN_NAMES, ROW_DETAILS));
        
        document.close();
        return 0;
    }
}



class PageFooter extends PdfPageEventHelper {
    /** The template with the total number of pages. */
    PdfTemplate total;

    private Font font_footer, font_footer2;
    private String dateTime, user, terminal;

    public PageFooter (String dateTime, String user, String terminal){
        try{
            this.dateTime = dateTime;
            this.user = user;
            this.terminal = terminal;
            font_footer = new Font(FontFamily.HELVETICA, 9, Font.NORMAL);
            font_footer2 = new Font(FontFamily.HELVETICA, 7, Font.NORMAL);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the PdfTemplate that will hold the total number of pages.
     * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(
     *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
     */
    public void onOpenDocument(PdfWriter writer, Document document) {
        total = writer.getDirectContent().createTemplate(30, 12);
    }

    /**
     * Adds a header to every page
     * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(
     *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
     */
    public void onEndPage(PdfWriter writer, Document document) {
        PdfPTable table = new PdfPTable(3);
        try {
            table.setWidths(new int[]{150, 24, 2});
            table.getDefaultCell().setFixedHeight(10);
            table.getDefaultCell().setBorder(Rectangle.TOP);
            PdfPCell cell = new PdfPCell();
            cell.setBorder (0);
            cell.setBorderWidthTop (1);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setPhrase(new Phrase("Printed By: " + user + "  |  Date Printed: " + dateTime + "  |  Terminal: " + terminal
                    +"\nPowered by Monosync", font_footer2));
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorder (0);
            cell.setBorderWidthTop (1);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setPhrase(new Phrase(String.format("Page %d of ", writer.getPageNumber()), font_footer));
            table.addCell(cell);

            cell = new PdfPCell(Image.getInstance(total));
            cell.setBorder (0);
            cell.setBorderWidthTop (1);
            table.addCell(cell);
            table.setTotalWidth(document.getPageSize().getWidth()
                    - document.leftMargin() - document.rightMargin());
            table.writeSelectedRows(0, -1, document.leftMargin(),
                    document.bottomMargin() - 10, writer.getDirectContent());
        }
        catch(DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    /**
     * Fills out the total number of pages before the document is closed.
     * @see com.itextpdf.text.pdf.PdfPageEventHelper#onCloseDocument(
     *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
     */
    public void onCloseDocument(PdfWriter writer, Document document) {
        ColumnText.showTextAligned(total, Element.ALIGN_LEFT,
                new Phrase(String.valueOf(writer.getPageNumber()), font_footer),
                2, 1, 0);
    }
}  

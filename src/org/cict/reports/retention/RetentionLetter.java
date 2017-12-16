package org.cict.reports.retention;

import artifacts.ResourceManager;
import com.itextpdf.text.BadElementException;
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
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import static com.itextpdf.text.pdf.PdfDictionary.FONT;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import org.cict.reports.ReportsDirectory;
import org.cict.reports.ReportsUtility;
import static org.cict.reports.result.ResultReport.RESULT;

public class RetentionLetter {
    
    public static String RESULT;
    public RetentionLetter(String res) {
        RESULT = res;
    }
    
    public int print() {
        try {
            int val = createPdf(RESULT);
            if(val==1) {
                System.out.println("Please close the previous report");
            }
            if (Desktop.isDesktopSupported()) {
                try {
                    File myFile = new File(RESULT);
                    Desktop.getDesktop().open(myFile);
                } catch (IOException ex) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public String STUDENT_NAME,
            STUDENT_SECTION,
            DEAN,
            NUMBER_OF_FAILED_SUBJECTS,
            PREV_SCHOOL_YEAR,
            PREV_SEMESTER;
    public String[] SENDER_NAMES;
    public Date SERVER_DATE;
    
    private static final Font font7Plain = new Font(FontFamily.HELVETICA, 7);
    private static final Font font7Bold = new Font(FontFamily.HELVETICA, 7, Font.BOLD);
    private static final Font font8Plain = new Font(FontFamily.HELVETICA, 8);
    private static final Font font8Bold = new Font(FontFamily.HELVETICA, 8, Font.BOLD);
    private static final Font font9Plain = new Font(FontFamily.HELVETICA, 9);
    private static final Font font9Bold = new Font(FontFamily.HELVETICA, 9, Font.BOLD);
    private static final Font font10Plain = new Font(FontFamily.HELVETICA, 10);
    private static final Font font10Bold = new Font(FontFamily.HELVETICA, 10, Font.BOLD);
    private static final Font font11Plain = new Font(FontFamily.HELVETICA, 11);
    private static final Font font11Bold = new Font(FontFamily.HELVETICA, 11, Font.BOLD);

    private Document documentFormat;
    public void setDocumentFormat(Document documentFormat) {
        this.documentFormat = documentFormat;
    }
    
    private static PdfWriter writer;
    public int createPdf(String filename)
            throws DocumentException, IOException {
        Document document = documentFormat;
        try{
            writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        }catch(FileNotFoundException es){
            return 1;
        }
        document.open();
        document.add(createLetterHeader());
        document.add(createReceieverInformation());
        document.add(createBody());
        PdfPTable tableSender = createSenderInfo();
        if(tableSender != null) {
            document.add(tableSender);
        }
        document.add(createDeanInfo());
        addText(STUDENT_NAME, 7, 240, document.getPageSize().getHeight()- 212);
        addText(STUDENT_SECTION, 7, 455,  document.getPageSize().getHeight()- 212);
        addText(NUMBER_OF_FAILED_SUBJECTS, 7, 50,  document.getPageSize().getHeight()- 225);
        addText(PREV_SCHOOL_YEAR, 7, 370,  document.getPageSize().getHeight()- 225);
        addText(PREV_SEMESTER, 7, 515,  document.getPageSize().getHeight()- 225);
        document.close();
        return 0;
    }
    
    private void addText(String text, float fontSize, float x, float y) throws DocumentException, IOException{
        PdfContentByte over = writer.getDirectContent();
        over.saveState();
        BaseFont bf = BaseFont.createFont();
        over.beginText();
        over.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL);
        over.setFontAndSize(bf, fontSize);
        over.setTextMatrix(x, y);
        over.showText(text);
        over.endText();
        over.restoreState();
    }
    
    
    private static PdfPTable createLetterHeader() throws DocumentException, BadElementException, IOException{
        
        String BULSU_logo = ReportsDirectory.REPORTS_DIR_IMAGES + "checklist/BULSU.png",
        CICT_logo = ReportsDirectory.REPORTS_DIR_IMAGES + "checklist/CICT.png";
        
        Image imgBULSU = Image.getInstance(ResourceManager.fetchFromResource(RetentionLetter.class, BULSU_logo));
        imgBULSU.scaleAbsolute(55, 55); //size
        
        Image imgCICT = Image.getInstance(ResourceManager.fetchFromResource(RetentionLetter.class, CICT_logo));
        imgCICT.scaleAbsolute(55, 55); //size
        
        PdfPTable tblHeader = new PdfPTable(3);
        tblHeader.setWidths(new int[]{1,7,1});
        tblHeader.addCell(createCellWithObject(imgBULSU, false, true));
   
        Paragraph pHeader = new Paragraph(14);
        pHeader.setAlignment(Element.ALIGN_CENTER);
        pHeader.add(new Chunk("Bulacan State University\n",font9Bold));
        pHeader.add(new Chunk("COLLEGE OF INFORMATION AND COMMUNICATIONS TECHNOLOGY\n",font8Bold));
        pHeader.add(new Chunk("LETTER TO THE PARENT / GUARDIAN\n\n\n\n",font11Bold));
        
        tblHeader.addCell(createCellWithObject(pHeader, false, true));
        tblHeader.addCell(createCellWithObject(imgCICT, false, true));
        return tblHeader;
    }
    
    private PdfPTable createReceieverInformation() throws DocumentException, DocumentException {
        PdfPTable tblHeader = new PdfPTable(2);
        tblHeader.setHorizontalAlignment(Element.ALIGN_LEFT);
        tblHeader.setWidths(new int[]{2,1});
        
        Paragraph pHeader = new Paragraph(14);
        pHeader.setAlignment(Element.ALIGN_LEFT);
        pHeader.add(new Chunk("To the Parent/Guardian of:\n",font9Plain));
        pHeader.add(new Chunk(this.STUDENT_NAME + "\n",font9Plain));
        pHeader.add(new Chunk(this.STUDENT_SECTION + "\n\n",font9Plain));
        tblHeader.addCell(createCellWithObject(pHeader, false, false));
        
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, YYYY");
        Paragraph date = new Paragraph();
        date.setAlignment(Element.ALIGN_RIGHT);
        date.add(getTitleContent("Date: ", font9Plain, formatter.format(SERVER_DATE) + "\n", font9Plain, "", true));
        tblHeader.addCell(createCellWithObject(date, false, true));
        return tblHeader;
    }
    
    private Paragraph createBody() {
        Paragraph pHeader = new Paragraph(14);
        pHeader.setAlignment(Element.ALIGN_LEFT);
        pHeader.add(new Chunk("Sir and M a d a m:\n",font9Plain));
        pHeader.add(new Chunk("    We wish to inform you that your Son / Daughter: ________________________________________ of ___________ has obtain "
                + "____________________ failing Grade of his enrolled subject for the school year _____________________ Semester _________.\n\n",font9Plain));
        pHeader.add(new Chunk("    With regards to this, the college will implement certain measures and standards for STUDENT’s academic delinquency as stated"
                + " on page 14 of the BulSU Student Handbook.\n\n",font9Plain));
        
        Paragraph centerHeader = new Paragraph(14);
        centerHeader.setAlignment(Element.ALIGN_CENTER);
        centerHeader.add(new Chunk("________________________________________________________________________________________\n\n",font11Bold));
        centerHeader.add(new Chunk("ACADEMIC DELINQUENCY as stated on BulSU-student Handbook\n\n",font9Bold));
        pHeader.add(centerHeader);
        
        pHeader.add(new Chunk("The faculty of each academic unit shall remedy academic delinquency by implementing the following measures and minimum standards:\n\n",font8Plain));
        pHeader.add(new Chunk(" a.	Warning. Any student who obtained a failing grade in one (1) subject shall receive a warning  from the dean.\n\n",font8Plain));
        pHeader.add(new Chunk(" b.	Probation. Any student who dropped or obtained failing grades in two (2) subjects shall be placed under probation for the succeeding semester. His load shall be reduced as determined by the unit head concerned. Any student on probation who dropped or obtained failing grades in two (2) subjects shall be dropped from his course. He may, however, apply to shift to another course, subject to the approval by the Vice President for Academic Affairs.\n\n",font8Plain));
        pHeader.add(new Chunk(" c.	Permanent Disqualification. Any student who, at the end of the term or semester, obtained failing grades in at least fifty percent (50%) of his enrolled subjects disqualified himself to continue his/her studies in the University.\n\n",font8Plain));
         
        
        Paragraph centerHeader2 = new Paragraph(14);
        centerHeader2.setAlignment(Element.ALIGN_CENTER);
        centerHeader2.add(new Chunk("________________________________________________________________________________________\n\n",font11Bold));
        pHeader.add(centerHeader2);
        pHeader.add(new Chunk("    We hope this letter will merit your attention regarding your Son/Daughter academic status on our college as of the present school year "
                + "and semester.\n\n",font9Plain));
        
        return pHeader;
    }

    private PdfPTable createSenderInfo(){
        if(SENDER_NAMES.length==0) {
            return null;
        }
        PdfPTable tbl_sender = new PdfPTable(SENDER_NAMES.length);
        tbl_sender.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tbl_sender.setTotalWidth(200);
        tbl_sender.setLockedWidth(true);
        tbl_sender.addCell(createSimpleCell("Very Truly Yours,\n\n\n", font8Plain, SENDER_NAMES.length, true, false, 0, false));
        
        for(String sender : SENDER_NAMES) {
            String[] names = sender.split(",");
            if(!names[0].equals("")) {
                Paragraph senderName = new Paragraph(15);
                senderName.setAlignment(Element.ALIGN_CENTER);
                String info = "";
                try {
                    info = names[1];
                } catch (Exception e) {
                }
                senderName.add(underlineTitle(names[0], font8Bold, info, font7Plain, "\n"));
                tbl_sender.addCell(createCellWithObject(senderName, false, true));
            }
        }
        return tbl_sender;
    }
    
    private PdfPTable createDeanInfo() {
        PdfPTable tbl_dean = new PdfPTable(1);
        tbl_dean.setHorizontalAlignment(Element.ALIGN_LEFT);
        tbl_dean.setTotalWidth(200);
        tbl_dean.setLockedWidth(true);
        
        Paragraph notedBy = new Paragraph();
        notedBy.setAlignment(Element.ALIGN_LEFT);
        notedBy.setSpacingBefore(10f);
        notedBy.add(new Chunk("Noted by:\n\n", font8Bold));
        tbl_dean.addCell(createCellWithObject(notedBy, false, true));
        
        Paragraph dean = new Paragraph();
        dean.setAlignment(Element.ALIGN_CENTER);
        dean.setSpacingBefore(10f);
        
        String[] deanInfo = this.DEAN.split(",");
        String info = "";
        try {
            info = deanInfo[1];
        } catch (Exception e) {
        }
        dean.add(underlineTitle( deanInfo[0], font8Bold, info, font7Plain, "\n"));
        tbl_dean.addCell(createCellWithObject(dean, false, true));
        
        return tbl_dean;
    }
    
    private static PdfPCell createCellWithObject(Object element, boolean border, boolean center){
        PdfPCell cell = new PdfPCell();
        if(!border)
            cell.setBorder(PdfPCell.NO_BORDER);
        if(center){
            cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        }
        cell.addElement((Element)element);
        return cell;
    }
    
     private static PdfPCell createSimpleCell(String content, Font font, int colspan, boolean center, boolean colored, float padding, boolean border) {
        PdfPCell cell = new PdfPCell();
        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPaddingTop(padding);
        cell.setPaddingBottom(padding);
        Paragraph p = new Paragraph(7);
        if(center)
            p.setAlignment(Element.ALIGN_CENTER);
        if(colored)
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        p.add(new Chunk(content, font));
        cell.addElement(p);
        if(colspan != 0)
            cell.setColspan(colspan);
        if(!border) {
            cell.setBorder(PdfPCell.NO_BORDER);
        } else {
            cell.setBorder(1);
        }
        return cell;
    }
    
    private static Phrase getTitleContent(String title, Font font1, String info, Font font2, String space, boolean underlined){
        Phrase phr = new Phrase(10);
        phr.add(new Chunk(title, font1));
        Chunk chunks = new Chunk(info, font2);
        if(underlined)
            chunks.setUnderline(0.1f, -2f);
        phr.add(chunks);
        phr.add(new Chunk(space, font2));
        return phr;
    }
    
    private static Phrase underlineTitle(String title, Font font1, String details, Font font2, String space){
        Phrase phr = new Phrase(10);
        Chunk chunks = new Chunk(title, font1);
        chunks.setUnderline(0.1f, -2f);
        phr.add(chunks);
        phr.add(new Chunk(space, font2));
        phr.add(new Chunk(details, font2));
        return phr;
    }
    
}

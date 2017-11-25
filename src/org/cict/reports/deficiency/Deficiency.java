package org.cict.reports.deficiency;

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

public class Deficiency {

    /**
     * Path to the resulting PDF file.
     */
    public static String RESULT;

    public Deficiency( String res) {
        RESULT = res;
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
    public String STUDENT_NAME = "DE LA CRUZ, JOEMAR NUCOM",
            STUDENT_ADDRESS = "8-152 SUCAD APALIT, PAMPANGA",
            STUDENT_NUMBER = "09123456789",
            CURRICULUM_NAME = "BACHELOR OF SCIENCE IN INFORMATION TECHNOLOGY",
            DATETIME = null,
            USER = null,
            TERMINAL = null; 
    public HashMap<String,ArrayList<Object[]>> SUBJECTS_PER_SEM = new HashMap<String, ArrayList<Object[]>>();
    
    /**
     * FONTS
     */
    private static final Font font19Plain = new Font(FontFamily.COURIER, 19);
    private static final Font font17Bold = new Font(FontFamily.UNDEFINED, 17, Font.BOLD);
    private static final Font font13Plain = new Font(FontFamily.HELVETICA, 13);
    private static final Font font8Plain = new Font(FontFamily.HELVETICA, 8);
    private static final Font font9Plain = new Font(FontFamily.HELVETICA, 9);
    private static final Font font7Bold = new Font(FontFamily.HELVETICA, 7, Font.BOLD);
    private static final Font font6Plain = new Font(FontFamily.HELVETICA, 6);
    private static final Font font6Bold = new Font(FontFamily.HELVETICA, 6, Font.BOLD);
    private static final Font font5Plain = new Font(FontFamily.HELVETICA, 5);
    private static final Font font5Bold = new Font(FontFamily.HELVETICA, 5, Font.BOLD);
    private static final Font font5BoldItalic = new Font(FontFamily.HELVETICA, 5, Font.BOLDITALIC);
    /**
     * PRIVATE VARIABLES
     */
//    private String name,
//            address,
//            studentNo;
//    private static String curriculumName;
//    private HashMap<String,ArrayList<Object[]>> subjectsPerSem = new HashMap<String, ArrayList<Object[]>>();
    
    public void init() {
//        this.studentNo = this.STUDENT_NUMBER;
//        this.name = this.STUDENT_NAME;
//        this.address = this.STUDENT_ADDRESS;
//        this.subjectsPerSem = this.SUBJECTS_PER_SEM;
//        this.curriculumName = CURRICULUM_NAME;
    }
    
    //---------------------------
    private Document documentFormat;
    public void setDocumentFormat(Document documentFormat) {
        this.documentFormat = documentFormat;
    }
    private String reportsIntroTitle = null, reportsTitleHead = null, reportsOtherDetail = null;
    //

    public void setReportsIntroTitle(String reportsIntroTitle) {
        this.reportsIntroTitle = reportsIntroTitle;
    }

    public void setReportsTitleHead(String reportsTitleHead) {
        this.reportsTitleHead = reportsTitleHead;
    }

    public void setReportsOtherDetail(String reportsOtherDetail) {
        this.reportsOtherDetail = reportsOtherDetail;
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
        Document document = this.documentFormat;
        try{
            writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
            PageFooter event = new PageFooter((DATETIME==null? "NOT FOUND" : DATETIME), (USER==null? "NOT FOUND" : USER), (TERMINAL==null? "NOT FOUND" : TERMINAL));
            writer.setPageEvent(event);
        }catch(FileNotFoundException es){
            return 1;
        }
        document.open();
//        String location_logo1 = ReportsDirectory.REPORTS_DIR_IMAGES + "checklist/BULSU.png",
//        location_logo2 = ReportsDirectory.REPORTS_DIR_IMAGES + "checklist/CICT.png";
//        Image img = Image.getInstance(ResourceManager.fetchFromResource(Deficiency.class, location_logo1));
//        img.setAbsolutePosition(100, 825); //position
//        img.scaleAbsolute(70, 70); //size
//        document.add(img);
//        Image img2 = Image.getInstance(ResourceManager.fetchFromResource(Deficiency.class, location_logo2));
//        img2.setAbsolutePosition(445, 825); //position
//        img2.scaleAbsolute(70, 70); //size
//        document.add(img2);
        
        ReportsUtility.createHeader(document, this.reportsTitleHead, this.reportsIntroTitle, this.reportsOtherDetail);
        ReportsUtility.createStudentInfoHeader(document, STUDENT_NAME, STUDENT_NUMBER, STUDENT_ADDRESS);
//        document.add(createBody());
//        writer.setPageEvent(new MyFooter());
        document.add(createCurriculumTable(0,1));
        document.add(createCurriculumTable(1,2));
        document.add(createCurriculumTable(2,3));
        document.add(createCurriculumTable(3,4));
        document.close();
        return 0;
    }
    
//    private static Paragraph createHeader() throws DocumentException{
//        Paragraph header = new Paragraph(15);
//        header.setAlignment(Element.ALIGN_CENTER);
//        header.add(new Chunk("Republic of the Philippines\n",font13Plain));
//        header.add(new Chunk("Bulacan State University\n",font17Bold));
//        header.add(new Chunk("City of Malolos, Bulacan\n",font13Plain));
//        header.add(new Chunk("Tel/Fax (044) 9197800 local 1101\n\n",font9Plain));
//        header.add(new Chunk("COLLEGE OF INFORMATION AND COMMUNICATIONS TECHNOLOGY\n",font19Plain));
//        header.add(new Chunk("_____________________________________________________\n\n",font17Bold));
//        header.add(createTitle());
//        return header;
//    }
    
//    private static Paragraph createTitle() {
//        Paragraph p = new Paragraph(10);
//        p.setAlignment(Element.ALIGN_CENTER);
//        p.add(new Chunk("Deficiency Report\n",font13Plain));
//        p.add(getTextUnderlined(curriculumName + "\n",font7Bold));
//        return p;
//    }
    
//    private PdfPTable createStudentInfo() throws DocumentException {
//        PdfPTable tbl_stud = new PdfPTable(2);
//        tbl_stud.setTotalWidth(500);
//        tbl_stud.setLockedWidth(true);
//        tbl_stud.setHorizontalAlignment(Element.ALIGN_CENTER);
//        tbl_stud.setSpacingAfter(7f);
//        /**
//         * STUDENT INFO
//         */
//        tbl_stud.addCell(createCellWithObject(getTitleContent("NAME: ", font8Plain, getShortenedDetail(this.name, 40), font8Plain, "", true), false, true));
//        tbl_stud.addCell(createCellWithObject(getTitleContent("STUDENT #: ", font8Plain, getShortenedDetail(this.studentNo, 47), font8Plain, "", true), false, true));
//        tbl_stud.addCell(createCellWithObject(getTitleContent("ADDRESS: ", font8Plain, getShortenedDetail(this.address, 39), font8Plain, "", true), false, false));
//        tbl_stud.addCell(createCellWithObject(new Chunk("") ,false, true));
//        
//        return tbl_stud;
//    }
    
    private PdfPTable createBody() throws DocumentException {
        PdfPTable tbl_stud = new PdfPTable(1);
        tbl_stud.setPaddingTop(10f);
        tbl_stud.setTotalWidth(575);
        tbl_stud.setLockedWidth(true);
        try {
            tbl_stud.addCell(createCellWithObject(createCurriculumTable(0,2),false,true));
            tbl_stud.addCell(createCellWithObject(createCurriculumTable(2,4),false,true));
        } catch (Exception e) {
        }
        return tbl_stud;
    }
      
    private String objectKey, yearLevel, semester;
    private int  year = 1;
    private Double totalUnitsLabOverall = 0.0,
                totalUnitsLecOverall = 0.0,
                totalUnitsCreOverall = 0.0;
    private Integer totalHrsWkOverall = 0;
    private PdfPTable createCurriculumTable(int start, int end) throws DocumentException {
        PdfPTable tbl_stud = new PdfPTable(8);
        tbl_stud.setWidths(new float[]{2, 5, 1, 1, 1, 2, 2, 2}); 
        tbl_stud.setTotalWidth(500);
        tbl_stud.setLockedWidth(true);
        tbl_stud.setSpacingAfter(10f);
        for (int i = start; i < end; i++) {
            this.setTitleHeader();
            /**
             * HEADER
             */
            tbl_stud.addCell(createSimpleCell(yearLevel, font5Bold, 11, true, true));
            for (int k = 0; k < 2; k++) {
                this.setTitleHeader();
                //check if there is a subject in this sem
                ArrayList<Object[]> subjects = this.SUBJECTS_PER_SEM.get(objectKey);
                //0-subjectmap
                //1-total hrs
                //2-prereq
                //3-co-req
//                if(subjects.isEmpty())
//                    continue;
                tbl_stud.addCell(createSimpleCell(semester, font5Bold, 11, true, false));
               
                if(subjects.isEmpty()){
                    tbl_stud.addCell(createSimpleCell("*** No Missing Grades ***", font5Plain, 8, true, false));
//                    tbl_stud.addCell(createSimpleCell("MISSING", font5Plain, 0, true, false));
//                    tbl_stud.addCell(createSimpleCell(getShortenedDetail("RECORD"
//                            , 38), font5Plain, 0, false, false));
//                    tbl_stud.addCell(createSimpleCell("OF", font5Plain, 0, true, false));
//                    tbl_stud.addCell(createSimpleCell("GRADE", font5Plain, 0, true, false));
//                    tbl_stud.addCell(createSimpleCell("IN", font5Plain, 0, true, false));
//                    tbl_stud.addCell(createSimpleCell("THIS", font5Plain, 0, true, false));
//                    tbl_stud.addCell(createSimpleCell("SEMESTER", font5Plain, 0, true, false));
                    //change sem
                    if(sem == 1)
                        sem = 2;
                    else
                        sem = 1;
                    
                    continue;
                }
                
                tbl_stud.addCell(createSimpleCell("COURSE CODE", font5Bold, 0, true, false));
                tbl_stud.addCell(createSimpleCell("DESCRIPTIVE TITLE", font5Bold, 0, true, false));
                tbl_stud.addCell(createSimpleCell("LEC\nUNITS", font5Bold, 0, true, false));
                tbl_stud.addCell(createSimpleCell("LAB\nUNITS", font5Bold, 0, true, false));
                tbl_stud.addCell(createSimpleCell("TOTAL UNITS", font5Bold, 0, true, false));
                tbl_stud.addCell(createSimpleCell("HOURS PER\nWEEK", font5Bold, 0, true, false));
                tbl_stud.addCell(createSimpleCell("PREREQ.", font5Bold, 0, true, false));
                tbl_stud.addCell(createSimpleCell("Co-REQ.", font5Bold, 0, true, false));

                /**
                 * DATA
                 */
                Double totalUnitsLab = 0.0,
                        totalUnitsLec = 0.0,
                        totalUnitsCre = 0.0;
                Integer totalHrsWk = 0;
                for (int j = 0; j < subjects.size(); j++) {
                    SubjectMapping subject = (SubjectMapping) subjects.get(j)[0];
                    tbl_stud.addCell(createSimpleCell(subject.getCode(), font5Plain, 0, false, false));
                    tbl_stud.addCell(createSimpleCell(getShortenedDetail(subject.getDescriptive_title()
                            , 100), font5Plain, 0, false, false));
                    Double totalUnits = 0.0;
                    if(subject.getType().equalsIgnoreCase(SubjectClassification.TYPE_NSTP)) {
                        tbl_stud.addCell(createSimpleCell("", font5Plain, 0, true, false));
                        tbl_stud.addCell(createSimpleCell("", font5Plain, 0, true, false));
                        tbl_stud.addCell(createSimpleCell("(3.0)", font5Plain, 0, true, false));
                    } else {
                        tbl_stud.addCell(createSimpleCell(subject.getLec_units()+"", font5Plain, 0, true, false));
                        tbl_stud.addCell(createSimpleCell(subject.getLab_units()+"", font5Plain, 0, true, false));
                        totalUnits = (subject.getLec_units() + subject.getLab_units());
                        tbl_stud.addCell(createSimpleCell(totalUnits+"", font5Plain, 0, true, false));
                    }
                    
                    
                    tbl_stud.addCell(createSimpleCell(subjects.get(j)[1].toString(), font5Plain, 0, true, false));
                    tbl_stud.addCell(createSimpleCell(subjects.get(j)[2].toString(), font5Plain, 0, true, false));
                    tbl_stud.addCell(createSimpleCell(subjects.get(j)[3].toString(), font5Plain, 0, true, false));
                    
                    try {
                        totalUnitsLec += subject.getLec_units();
                        totalUnitsLab += subject.getLab_units();
                        totalUnitsCre += totalUnits;
                        totalHrsWk += Integer.valueOf(subjects.get(j)[1].toString());
                    } catch(NumberFormatException a) {
                    }
                }

                /**
                 * TOTALS
                 */
                tbl_stud.addCell(createSimpleCell("", font5Bold, 0, true, false));
                tbl_stud.addCell(createSimpleCell("TOTAL", font5Bold, 0, false, false));
                tbl_stud.addCell(createSimpleCell(String.valueOf(totalUnitsLec), font5Bold, 0, true, false));
                tbl_stud.addCell(createSimpleCell(String.valueOf(totalUnitsLab), font5Bold, 0, true, false));
                tbl_stud.addCell(createSimpleCell(String.valueOf(totalUnitsCre), font5Bold, 0, true, false));
                tbl_stud.addCell(createSimpleCell(String.valueOf(totalHrsWk), font5Bold, 0, true, false));
                tbl_stud.addCell(createSimpleCell("", font5Bold, 0, true, false));
                tbl_stud.addCell(createSimpleCell("", font5Bold, 0, true, false));

                //change sem
                if(sem == 1)
                    sem = 2;
                else
                    sem = 1;
            }
            year++;
        }
        return tbl_stud;
    }
    
    private int sem = 1;
    private void setTitleHeader() {
        switch (year) {
            case 1:
                this.yearLevel = "FIRST YEAR";
                if(sem == 1){
                    this.semester = "First Semester";
                } else if(sem == 2)
                    this.semester = "Second Semester";
                objectKey = 1 + String.valueOf(sem);
                break;
            case 2:
                this.yearLevel = "SECOND YEAR";
                if(sem == 1){
                    this.semester = "First Semester";
                } else if(sem == 2)
                    this.semester = "Second Semester";
                objectKey = 2 + String.valueOf(sem);
                break;
            case 3:
                this.yearLevel = "THIRD YEAR";
                if(sem == 1){
                    this.semester = "First Semester";
                } else if(sem == 2)
                    this.semester = "Second Semester";
                objectKey = 3 + String.valueOf(sem);
                break;
            case 4:
                this.yearLevel = "FOURTH YEAR";
                if(sem == 1){
                    this.semester = "First Semester";
                } else if(sem == 2)
                    this.semester = "Second Semester";
                objectKey = 4 + String.valueOf(sem);
                break;
            default:
                break;
        }
    }
    
    private static PdfPCell createCellWithPhrase(Phrase phr, Font font, boolean border, boolean center){
        PdfPCell cell = new PdfPCell();
        if(!border)
            cell.setBorder(PdfPCell.NO_BORDER);
        if(center){
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        }
        cell.addElement(phr);
        return cell;
    }
    
    private static PdfPCell createCellWithObject(Object tbl, boolean border, boolean center){
        PdfPCell cell = new PdfPCell();
        if(!border)
            cell.setBorder(PdfPCell.NO_BORDER);
        if(center){
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        }
        cell.addElement((Element)tbl);
        return cell;
    }
    private static PdfPCell createCellWithObject(Object tbl, boolean border, boolean center, int colspan){
        PdfPCell cell = new PdfPCell();
        if(!border)
            cell.setBorder(PdfPCell.NO_BORDER);
        if(center){
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        }
        cell.addElement((Element)tbl);
        if(colspan != 0)
            cell.setColspan(colspan);
        return cell;
    }
    
    private PdfPCell createSimpleCell(String content, Font font, int colspan, boolean center, boolean colored) {
        PdfPCell cell = new PdfPCell();
        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Paragraph p = new Paragraph(5);
        if(center)
            p.setAlignment(Element.ALIGN_CENTER);
        if(colored)
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        p.add(new Chunk(content, font));
        cell.addElement(p);
        if(colspan != 0)
            cell.setColspan(colspan);
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
     
    private static Chunk getTextUnderlined(String text, Font font2){
        Chunk chunks = new Chunk(text, font2);
        chunks.setUnderline(0.1f, -2f);
        return chunks;
    }
    
    private static String getShortenedDetail(String str, int count){
        if(str.length()>count){
            return str.substring(0, count) + "...";
        } else
            return str;
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
            cell.setPhrase(new Phrase("Printed By:  " + user + "  |   Date Printed:  " + dateTime + "  |  Terminal:  " + terminal
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
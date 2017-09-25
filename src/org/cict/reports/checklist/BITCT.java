package org.cict.reports.checklist;

import app.lazy.models.SubjectMapping;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import java.io.FileOutputStream;
import java.io.IOException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import static org.cict.reports.checklist.BSIT1516.RESULT;
import static org.cict.reports.checklist.BSIT1516.document;

public class BITCT {

    public static String document = "";
    public static String RESULT = "";

    public BITCT(String filename) {
        BSIT1516.document = filename;
        RESULT = "reports/checklist/" + BSIT1516.document + ".pdf";
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
    public String STUDENT_NUMBER = "",
            STUDENT_NAME = "",
            STUDENT_ADDRESS = "",
            STUDENT_HS = "",
            IMAGE_LOCATION = "src/images/me.jpg";
    public Integer ADMISSION_YEAR = 2014;
    public HashMap<String,ArrayList<Object[]>> SUBJECTS_PER_SEM = new HashMap<String, ArrayList<Object[]>>();
    /**
     * FONTS
     */
    private static final Font font7Plain = new Font(FontFamily.HELVETICA, 7);
    private static final Font font7Bold = new Font(FontFamily.HELVETICA, 7, Font.BOLD);
    private static final Font font6Plain = new Font(FontFamily.HELVETICA, 6);
    private static final Font font6Bold = new Font(FontFamily.HELVETICA, 6, Font.BOLD);
    private static final Font font5BoldItalic = new Font(FontFamily.HELVETICA, 5, Font.BOLDITALIC);
    private static final Font font5Plain = new Font(FontFamily.HELVETICA, 5);
    private static final Font font5Bold = new Font(FontFamily.HELVETICA, 5, Font.BOLD);
    /**
     * PRIVATE VARIABLES
     */
    private String studentNo,
            name,
            address,
            highSchool,
            image1x1;
    private Integer admissionYear; 
    private HashMap<String,ArrayList<Object[]>> subjectsPerSem = new HashMap<String, ArrayList<Object[]>>();
    
    
    public void init() {
        this.studentNo = this.STUDENT_NUMBER;
        this.name = this.STUDENT_NAME;
        this.address = this.STUDENT_ADDRESS;
        this.highSchool = this.STUDENT_HS;
        this.subjectsPerSem = this.SUBJECTS_PER_SEM;
        this.admissionYear = this.ADMISSION_YEAR;
        this.image1x1 = IMAGE_LOCATION;
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
        Document document = new Document(new Rectangle(Utilities.inchesToPoints(8.5f),
                Utilities.inchesToPoints(13f)), 50, 50, 70, 50); //lrtb
        try{
            writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        }catch(FileNotFoundException es){
            return 1;
        }
        document.open();
        String location_logo1 = "src/org/cict/reports/checklist/images/BULSU.png",
        location_logo2 = "src/org/cict/reports/checklist/images/CICT.png";
        // add bulsu logo
        Image img = Image.getInstance(location_logo1);
        img.setAbsolutePosition(32, 815); //position
        img.scaleAbsolute(52, 52); //size
        document.add(img);
        // add cict logo
        Image img2 = Image.getInstance(location_logo2);
        img2.setAbsolutePosition(85, 815); //position
        img2.scaleAbsolute(52, 52); //size
        document.add(img2);
        
        document.add(createHeader());
        
        Image image1X1 = Image.getInstance(image1x1);
        image1X1.setAbsolutePosition(450f,750); //position
        image1X1.scaleAbsolute(Utilities.inchesToPoints(2),Utilities.inchesToPoints(2)); //size
        document.add(image1X1);
        create2x2Box(writer);
        document.add(createStudentInfo());
        document.add(createBody(0,2));
        Paragraph p = new Paragraph(0);
        p.setFirstLineIndent(-25f);
        p.setSpacingAfter(7f);
        p.add(new Chunk("(Upon completion of the requirements of the two-year course,"
                + " a Certificate in Two-Year Industrial Technology will be granted.)", font5BoldItalic));
        document.add(p); 
        document.add(createBody(2,4));
        writer.setPageEvent(new MyFooter());
        document.close();
        return 0;
    }
    
    private static Paragraph createHeader() throws DocumentException{
        Paragraph header = new Paragraph(10);
        header.setAlignment(Element.ALIGN_CENTER);
        header.add(new Chunk("BULACAN STATE UNIVERSITY\n",font7Plain));
        header.add(new Chunk("COLLEGE OF INFORMATION AND COMMUNICATIONS TECHNOLOGY\n",font7Bold));
        header.add(new Chunk("City of Malolos, Bulacan\n",font7Plain));
        header.add(new Chunk("Tele Fax No. (044) 796-0147\n",font7Plain));
        header.add(new Chunk("Email: bsuice@pldtdsl.net\n",font7Plain));
        header.add(new Chunk("http://www.bulsu.edu.ph\n\n\n\n",font7Plain));
        header.add(new Chunk("CHECKLIST IN\n",font7Bold));
        header.add(new Chunk("BACHELOR IN INDUSTRIAL TECHNOLOGY\n",font7Bold));
        header.add(new Chunk("MAJOR IN COMPUTER TECHNOLOGY\n",font7Bold));
        header.add(new Chunk("\n",font7Plain));
        return header;
    }
    
    private PdfPTable createStudentInfo() throws DocumentException {
        PdfPTable tbl_stud = new PdfPTable(2);
        tbl_stud.setTotalWidth(575);
        tbl_stud.setLockedWidth(true);
        Paragraph p_title = new Paragraph(5);
        p_title.add(new Phrase(10, "PROPOSED CURRICULUM FOR S.Y. 2011-2012", font7Bold));
        p_title.setLeading(0, 1);
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setColspan(2);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        p_title.setAlignment(Element.ALIGN_CENTER);
        
        cell.addElement(p_title);
        tbl_stud.addCell(cell);
        /**
         * STUDENT INFO
         */
        tbl_stud.addCell(createCellWithObject(getTitleContent("NAME: ", font7Plain, getShortenedDetail(this.name, 40), font7Plain, "", true), false, true));
        tbl_stud.addCell(createCellWithObject(getTitleContent("STUDENT NO: ", font7Plain, getShortenedDetail(this.studentNo, 47), font7Plain, "", true), false, true));
        tbl_stud.addCell(createCellWithObject(getTitleContent("ADDRESS: ", font7Plain, getShortenedDetail(this.STUDENT_ADDRESS, 39), font7Plain, "", true), false, false));
        
        boolean underlined = true;
        if(highSchool.isEmpty()) {
            highSchool = "__________________________________________";
            underlined = false;
        }
        tbl_stud.addCell(createCellWithObject(getTitleContent("HIGH SCHOOL: ", font7Plain, getShortenedDetail(this.highSchool,42), font7Plain, "\n\n", underlined) ,false, true));
        return tbl_stud;
    }
    
    private String objectKey;
    private int sem = 1;
    private PdfPTable createBody(int start, int end) throws DocumentException {
        PdfPTable tbl_stud = new PdfPTable(3);
        tbl_stud.setWidths(new float[]{3,0,3}); 
        tbl_stud.setTotalWidth(575);
        tbl_stud.setLockedWidth(true);
        /**
         * SEMESTERS
         */
        for (int i = start; i < end; i++) {
            String t_header = this.getTitleHeader(i);
            tbl_stud.addCell(createCellWithObject(createSemesterTable(t_header, objectKey),false,true));
            tbl_stud.addCell(createCellWithObject(new Phrase("",font7Plain), false, true));
            
            t_header = this.getTitleHeader(i);
            tbl_stud.addCell(createCellWithObject(createSemesterTable(t_header, objectKey),false,true));   
            
            admissionYear++;
        }
        return tbl_stud;
    }
    
    private String getTitleHeader(int counter) {
        String t_header = "",
                sy = admissionYear + "-" + (admissionYear+1);
        switch (counter+1) {
            case 1:
                if(sem == 1){
                    t_header = "FIRST YEAR 1st Semester SY ";
                } else if(sem == 2)
                    t_header = "FIRST YEAR 2nd Semester SY ";
                objectKey = 1 + String.valueOf(sem);
                break;
            case 2:
                if(sem == 1){
                    t_header = "SECOND YEAR 1st Semester SY ";
                } else if(sem == 2)
                    t_header = "SECOND YEAR 2nd Semester SY ";
                objectKey = 2 + String.valueOf(sem);
                break;
            case 3:
                if(sem == 1){
                    t_header = "THIRD YEAR 1st Semester SY ";
                } else if(sem == 2)
                    t_header = "THIRD YEAR 2nd Semester SY ";
                objectKey = 3 + String.valueOf(sem);
                break;
            case 4:
                if(sem == 1){
                    t_header = "FOURTH YEAR 1st Semester SY ";
                } else if(sem == 2)
                    t_header = "FOURTH YEAR 2nd Semester SY ";
                objectKey = 4 + String.valueOf(sem);
                break;
            default:
                break;
        }
        //change sem
        if(sem == 1)
            sem = 2;
        else
            sem = 1;
        return t_header + sy;
    }
     
    private Integer OVERALL_TOTAL = 0;
    private PdfPTable createSemesterTable(String titleHeader, String key) throws DocumentException {
        PdfPTable tbl_stud = new PdfPTable(6);
        tbl_stud.setWidths(new float[]{2, 4, 1, 2, 2, 1}); 
        tbl_stud.setTotalWidth(280);
        tbl_stud.setLockedWidth(true);
        tbl_stud.setSpacingAfter(10f);
        tbl_stud.setHorizontalAlignment(Element.ALIGN_TOP);
        /**
         * HEADER
         */
        tbl_stud.addCell(createSimpleCell(titleHeader, font5Bold, 6, true));
        tbl_stud.addCell(createSimpleCell("Subject Code", font5Plain, 0, true));
        tbl_stud.addCell(createSimpleCell("Description", font5Plain, 0, true));
        
        if(objectKey.equals("22") || objectKey.equals("42")){
            tbl_stud.addCell(createSimpleCell("Industry Immersion", font5Plain, 2, true));
        } else 
            tbl_stud.addCell(createSimpleCell("Units", font5Plain, 0, true));
        
        tbl_stud.addCell(createSimpleCell("Pre-requisite/s", font5Plain, 0, true));
        
        if(objectKey.equals("22") || objectKey.equals("42")){
            tbl_stud.addCell(createSimpleCell("Hours", font6Plain, 0, true));
        } else {
            tbl_stud.addCell(createSimpleCell("Co-requisite/s", font5Plain, 0, true));
            tbl_stud.addCell(createSimpleCell("Grade", font5Plain, 0, true));
        }
        /**
         * DATA
         */
        Integer totalUnits = 0;
        ArrayList<Object[]> subjects = this.subjectsPerSem.get(objectKey);
        //0-subjectmap
        //1-total hrs
        //2-prereq
        //3-co-req
        for (int i = 0; i < subjects.size(); i++) {
            SubjectMapping subject = (SubjectMapping) subjects.get(i)[0];
            tbl_stud.addCell(createSimpleCell(subject.getCode(), font5Plain, 0, false));
            tbl_stud.addCell(createSimpleCell(subject.getDescriptive_title(), font5Plain, 0, false));
   
            if(objectKey.equals("22") || objectKey.equals("42")){
                tbl_stud.addCell(createSimpleCell((subject.getLab_units() + subject.getLec_units())+"", font5Plain, 2, true));

                String str = "Second Year Standing";
                if(objectKey.equals("42"))
                    str = "Fourth Year Standing";
                tbl_stud.addCell(createSimpleCell(str, font5Plain, 0, true));
                tbl_stud.addCell(createSimpleCell("520", font5Plain, 0, true));
            } else {
                tbl_stud.addCell(createSimpleCell((subject.getLab_units() + subject.getLec_units())+"", font5Plain, 0, true));

                String prereq = "";
                if(!subjects.get(i)[2].toString().equalsIgnoreCase("NONE"))
                    prereq = subjects.get(i)[2]+"";
                tbl_stud.addCell(createSimpleCell(prereq, font5Plain, 0, true));
                String coreq = "";
                if(!subjects.get(i)[3].toString().equalsIgnoreCase("NONE"))
                    coreq = subjects.get(i)[3]+"";
                tbl_stud.addCell(createSimpleCell(coreq, font5Plain, 0, true));
                tbl_stud.addCell(createSimpleCell("", font5Plain, 0, true));
            }
            Double total = (subject.getLab_units() + subject.getLec_units());
            totalUnits += total.intValue();
        }
        /**
         * TOTAL
         */
        if(!objectKey.equals("22") && !objectKey.equals("42")){
            tbl_stud.addCell(createSimpleCell("", font5Plain, 0, true));
            tbl_stud.addCell(createSimpleCell("TOTAL LAB / LEC UNITS", font5Bold, 0, false));
            tbl_stud.addCell(createSimpleCell(String.valueOf(totalUnits), font5Bold, 0, true));
            tbl_stud.addCell(createSimpleCell("", font5Plain, 0, true));
            tbl_stud.addCell(createSimpleCell("", font5Plain, 0, true));
            tbl_stud.addCell(createSimpleCell("", font5Plain, 0, true));
        }
        /**
         * 
         */
        tbl_stud.addCell(createSimpleCell("", font5Plain, 0, true));
        tbl_stud.addCell(createSimpleCell("TOTAL UNITS", font5Bold, 0, true));
        tbl_stud.addCell(createSimpleCell(String.valueOf(totalUnits), font5Bold, 2, true));
        tbl_stud.addCell(createSimpleCell("", font5Plain, 0, true));
        tbl_stud.addCell(createSimpleCell("", font5Plain, 0, true));
        OVERALL_TOTAL += totalUnits;
        if(objectKey.equals("22")){
            tbl_stud.addCell(createSimpleCell("TOTAL UNITS FOR Certificate in "
                    + "BIT COMPUTER TECHNOLOGY \n " + OVERALL_TOTAL, font5BoldItalic, 12, true));
        } else if(objectKey.equals("42")){
            tbl_stud.addCell(createSimpleCell("TOTAL UNITS FOR BIT major in "
                    + "COMPUTER TECHNOLOGY \n " + OVERALL_TOTAL, font5BoldItalic, 12, true));
        }
        return tbl_stud;
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
    
    private PdfPCell createSimpleCell(String content, Font font, int colspan, boolean center) {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(5);
        if(center)
            p.setAlignment(Element.ALIGN_CENTER);
        p.add(new Chunk(content, font));
        cell.addElement(p);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        if(colspan != 0)
            cell.setColspan(colspan);
        return cell;
    }
    
    private PdfPCell createSimpleCell(String content, Font font, int colspan, boolean center, int rowspan) {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(5);
        if(center)
            p.setAlignment(Element.ALIGN_CENTER);
        p.add(new Chunk(content, font));
        cell.addElement(p);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        if(colspan != 0)
            cell.setColspan(colspan);
        cell.setRowspan(rowspan);
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
    
    private static String getShortenedDetail(String str, int count){
        if(str.length()>count){
            return str.substring(0, count) + "...";
        } else
            return str;
    }
    
    private static void create2x2Box(PdfWriter writer) {

        PdfContentByte cb = writer.getDirectContent();
        try {
            cb.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, false), 24);
            cb.rectangle(450f,750,Utilities.inchesToPoints(2f),Utilities.inchesToPoints(2f));
            cb.stroke();
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

class MyFooter extends PdfPageEventHelper {
    
    private static final Font font_footer = new Font(FontFamily.HELVETICA, 8);
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        Phrase footer = new Phrase("BulSU-OP-CICT-03F2",font_footer);
        Phrase footer2 = new Phrase("Revision: 0",font_footer);
        
        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                footer,
                30, //x
                document.bottom() - 20, //y
                0);
        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                footer2,
                30,
                (document.bottom() - 30), 
                0);
    }
}

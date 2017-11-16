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
 * JOEMAR N. DE LA CRUZ
 * GRETHEL EINSTEIN BERNARDINO
 *
 * OTHER LIBRARIES THAT ARE USED BELONGS TO THEIR RESPECTFUL OWNERS AND AUTHORS.
 * NO COPYRIGHT ARE INTENTIONAL OR INTENDED.
 * THIS PROJECT IS NOT PROFITABLE HENCE FOR EDUCATIONAL PURPOSES ONLY.
 * THIS PROJECT IS ONLY FOR COMPLIANCE TO OUR REQUIREMENTS.
 * THIS PROJECT DOES NOT INCLUDE DISTRIBUTION FOR OTHER PURPOSES.
 *
 */
package org.cict.reports;

import artifacts.ResourceManager;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.cict.reports.profile.student.StudentProfile;

/**
 *
 * @author Joemar
 */
public class ReportsUtility {
    //--------------------------------
    // FONTS
    //-------------------------------
    private static final Font font19Plain = new Font(FontFamily.COURIER, 19);
    private static final Font font17Bold = new Font(FontFamily.UNDEFINED, 17, Font.BOLD);
    private static final Font font13Plain = new Font(FontFamily.HELVETICA, 13);
    private static final Font font10Plain = new Font(FontFamily.HELVETICA, 10);
    private static final Font font8Plain = new Font(FontFamily.HELVETICA, 8);
    private static final Font font9Plain = new Font(FontFamily.HELVETICA, 9);
    private static final Font font7Bold = new Font(FontFamily.HELVETICA, 7, Font.BOLD);
    private static final Font font7Plain = new Font(FontFamily.HELVETICA, 7, Font.NORMAL);
    private static final Font font6Plain = new Font(FontFamily.HELVETICA, 6);
    private static final Font font6Bold = new Font(FontFamily.HELVETICA, 6, Font.BOLD);
    private static final Font font5Plain = new Font(FontFamily.HELVETICA, 5);
    private static final Font font5Bold = new Font(FontFamily.HELVETICA, 5, Font.BOLD);
    private static final Font font5BoldItalic = new Font(FontFamily.HELVETICA, 5, Font.BOLDITALIC);
    
    public static final SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy hh:mm aa");
    public static final SimpleDateFormat formatter2 = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss aa");
    
    public static Document createLongDocument(){
        return new Document(new Rectangle(Utilities.inchesToPoints(8.5f),
                Utilities.inchesToPoints(13f)), Utilities.inchesToPoints(0.5f), Utilities.inchesToPoints(0.5f), 50, 50); //lrtb
    }
    
    private static String reportTitle_, reportDescription_;
    public static void createHeader(Document document, String reportTitle, String reportDescription) throws DocumentException, BadElementException, IOException {
        String location_logo1 = ReportsDirectory.REPORTS_DIR_IMAGES + "checklist/BULSU.png",
        location_logo2 = ReportsDirectory.REPORTS_DIR_IMAGES + "checklist/CICT.png";
        Image img = Image.getInstance(ResourceManager.fetchFromResource(ReportsUtility.class, location_logo1));
        img.setAbsolutePosition(100, 825); //position
        img.scaleAbsolute(70, 70); //size
        document.add(img);
        Image img2 = Image.getInstance(ResourceManager.fetchFromResource(ReportsUtility.class, location_logo2));
        img2.setAbsolutePosition(445, 825); //position
        img2.scaleAbsolute(70, 70); //size
        document.add(img2);
        reportTitle_ = reportTitle;
        reportDescription_ = reportDescription;
        document.add(create());
    }
    
    private static String studentName_, studentNumber_, address_;
    public static void createStudentInfoHeader(Document document, String studentName, String studentNumber, String address) throws DocumentException {
        studentName_ = studentName;
        studentNumber_ = studentNumber;
        address_ = address;
        document.add(createStudentInfo());
    }
    
    /**
     * NUMBER OF COL AND COLNAMES MUST BE EQUAL IN NUMBER ELSE RETURN NULL
     * @param numColumns
     * @param colNames
     * @return
     * @throws DocumentException 
     */
    public static PdfPTable createPdfPTable(int numColumns, String[] colNames, ArrayList<String[]> rowData) throws DocumentException {
        PdfPTable tbl_stud = new PdfPTable(numColumns);
        tbl_stud.setTotalWidth(500);
        tbl_stud.setLockedWidth(true);
        tbl_stud.setSpacingAfter(10f);
        tbl_stud.setPaddingTop(20f);
        
        // insertion of col names
        for (int i = 0; i < numColumns; i++) {
            tbl_stud.addCell(createSimpleCell(colNames[i], font6Plain, 0, true, true, 5f, true));
        }
        
        // insertion of row data
        for (int i = 0; i < rowData.size(); i++) {
            try {
                String lastCol = rowData.get(i)[numColumns];
                if(lastCol != null) {
                    tbl_stud.addCell(createSimpleCell(lastCol, font6Plain, numColumns, true, true, 4f, false));
                }
            } catch (IndexOutOfBoundsException e) {
            }
            for (int j = 0; j < numColumns; j++) {
                tbl_stud.addCell(createSimpleCell(rowData.get(i)[j], font6Plain, 0, (j!=0), false, 4f, false));
            }
        }
        return tbl_stud;
    }
    
    
    
    
    //-------------------------------------
    //-----------------------
    private static PdfPTable createStudentInfo() throws DocumentException {
        PdfPTable tbl_stud = new PdfPTable(2);
        tbl_stud.setTotalWidth(500);
        tbl_stud.setLockedWidth(true);
        tbl_stud.setHorizontalAlignment(Element.ALIGN_CENTER);
        tbl_stud.setSpacingAfter(7f);
        /**
         * STUDENT INFO
         */
        tbl_stud.addCell(createCellWithObject(getTitleContent("NAME: ", font8Plain, getShortenedDetail((studentName_==null? "" : studentName_), 40), font8Plain, "", true), false, true));
        tbl_stud.addCell(createCellWithObject(getTitleContent("STUDENT #: ", font8Plain, getShortenedDetail((studentNumber_==null? "" : studentNumber_), 47), font8Plain, "", true), false, true));
        tbl_stud.addCell(createCellWithObject(getTitleContent("ADDRESS: ", font8Plain, getShortenedDetail((address_==null? "" : address_), 39), font8Plain, "", true), false, false));
        tbl_stud.addCell(createCellWithObject(new Chunk("\n") ,false, true));
        
        return tbl_stud;
    }
    
    private static Paragraph create() throws DocumentException{
        Paragraph header = new Paragraph(15);
        header.setAlignment(Element.ALIGN_CENTER);
        header.add(new Chunk("Republic of the Philippines\n",font13Plain));
        header.add(new Chunk("Bulacan State University\n",font17Bold));
        header.add(new Chunk("City of Malolos, Bulacan\n",font13Plain));
        header.add(new Chunk("Tel/Fax (044) 9197800 local 1101\n\n",font9Plain));
        header.add(new Chunk("COLLEGE OF INFORMATION AND COMMUNICATIONS TECHNOLOGY\n",font19Plain));
        header.add(new Chunk("_____________________________________________________\n\n",font17Bold));
        header.add(createTitle());
        return header;
    }
    
    private static Paragraph createTitle() {
        Paragraph p = new Paragraph(10);
        p.setAlignment(Element.ALIGN_CENTER);
        p.add(getTextUnderlined((reportTitle_==null? "" : reportTitle_) + "\n",font10Plain));
        p.add(new Chunk((reportDescription_==null? "" : reportDescription_) + "\n\n\n",font7Plain));
        return p;
    }
    
    public static Chunk getTextUnderlined(String text, Font font2){
        Chunk chunks = new Chunk(text, font2);
        chunks.setUnderline(0.1f, -2f);
        return chunks;
    }
      
    public static PdfPCell createCellWithObject(Object tbl, boolean border, boolean center){
        PdfPCell cell = new PdfPCell();
        if(!border)
            cell.setBorder(PdfPCell.NO_BORDER);
        if(center){
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        }
        cell.addElement((Element)tbl);
        return cell;
    }
    
    public static String getShortenedDetail(String str, int count){
        if(str.length()>count){
            return str.substring(0, count) + "...";
        } else
            return str;
    }
    
    public static Phrase getTitleContent(String title, Font font1, String info, Font font2, String space, boolean underlined){
        Phrase phr = new Phrase(10);
        phr.add(new Chunk(title, font1));
        Chunk chunks = new Chunk(info, font2);
        if(underlined)
            chunks.setUnderline(0.1f, -2f);
        phr.add(chunks);
        phr.add(new Chunk(space, font2));
        return phr;
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
        if(!border)
            cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }
    
}

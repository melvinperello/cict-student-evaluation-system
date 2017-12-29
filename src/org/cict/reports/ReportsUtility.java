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

import app.lazy.models.Database;
import app.lazy.models.PrintLogsMapping;
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
import com.jhmvin.Mono;
import com.melvin.mono.fx.bootstrap.M;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.cict.PublicConstants;
import org.cict.authentication.authenticator.CollegeComputer;
import org.cict.authentication.authenticator.CollegeFaculty;

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
    private static final Font font10Bold= new Font(FontFamily.HELVETICA, 10, Font.BOLD);
    private static final Font font8Plain = new Font(FontFamily.HELVETICA, 8);
    private static final Font font9Plain = new Font(FontFamily.HELVETICA, 9);
    private static final Font font7Bold = new Font(FontFamily.HELVETICA, 7, Font.BOLD);
    private static final Font font7Plain = new Font(FontFamily.HELVETICA, 7, Font.NORMAL);
    private static final Font font6Plain = new Font(FontFamily.HELVETICA, 6);
    private static final Font font6Bold = new Font(FontFamily.HELVETICA, 6, Font.BOLD);
    private static final Font font5Plain = new Font(FontFamily.HELVETICA, 5);
    private static final Font font5Bold = new Font(FontFamily.HELVETICA, 5, Font.BOLD);
    private static final Font font5BoldItalic = new Font(FontFamily.HELVETICA, 5, Font.BOLDITALIC);
    
    public static final SimpleDateFormat formatter_mmmm = new SimpleDateFormat("MMMM dd, yyyy hh:mm aa");
    public static final SimpleDateFormat formatter_mm = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss aa");
    
    public static boolean rotate = false;
    public static Document createLongDocument(){
        return new Document(rotate? new Rectangle(Utilities.inchesToPoints(8.5f),
                Utilities.inchesToPoints(13f)).rotate(): new Rectangle(Utilities.inchesToPoints(8.5f),
                Utilities.inchesToPoints(13f)), Utilities.inchesToPoints(0.5f), Utilities.inchesToPoints(0.5f), Utilities.inchesToPoints(0.5f), 50); //lrtb
    }
    
    public static Document createShortDocument(){
        System.out.println("Rotate: "+rotate);
        return new Document(rotate? new Rectangle(Utilities.inchesToPoints(8.5f),
                Utilities.inchesToPoints(11f)).rotate() : new Rectangle(Utilities.inchesToPoints(8.5f),
                Utilities.inchesToPoints(11f)), Utilities.inchesToPoints(0.5f), Utilities.inchesToPoints(0.5f), Utilities.inchesToPoints(0.5f), 50); //lrtb
    }
    
    public static Document createA4Document(){
        return new Document(rotate?new Rectangle(Utilities.inchesToPoints(8.26f),
                Utilities.inchesToPoints(11.69f)).rotate() : new Rectangle(Utilities.inchesToPoints(8.26f),
                Utilities.inchesToPoints(11.69f)), Utilities.inchesToPoints(0.5f), Utilities.inchesToPoints(0.5f), Utilities.inchesToPoints(0.5f), 50); //lrtb
    }
    
    private static String reportTitle_, reportDescription_, reportsOtherDetails;
    public static void createHeader(Document document, String reportTitle, String reportDescription, String otherDetails) throws DocumentException, BadElementException, IOException {
        PdfPTable table = new PdfPTable(3);
//        table.setWidthPercentage(100);
        table.setWidths(new int[]{1, 2, 1});
        String location_logo1 = ReportsDirectory.REPORTS_DIR_IMAGES + "checklist/BULSU.png",
        location_logo2 = ReportsDirectory.REPORTS_DIR_IMAGES + "checklist/CICT.png";
        Image img = Image.getInstance(ResourceManager.fetchFromResource(ReportsUtility.class, location_logo1));
//        img.setAbsolutePosition(100, 825); //position
        img.scaleAbsolute(50, 50); //size
        table.addCell(createCellWithObject(img, false, true));
        
        reportTitle_ = reportTitle;
        reportDescription_ = reportDescription;
        reportsOtherDetails = otherDetails;
        table.addCell(createCellWithObject(create(), false, true));
        
        Image img2 = Image.getInstance(ResourceManager.fetchFromResource(ReportsUtility.class, location_logo2));
//        img2.setAbsolutePosition(445, 825); //position
        img2.scaleAbsolute(50, 50); //size
        table.addCell(createCellWithObject(img2, false, true));
        document.add(table);
        
        Paragraph header = new Paragraph(15);
        header.setAlignment(Element.ALIGN_CENTER);
        header.add(new Chunk("COLLEGE OF INFORMATION AND COMMUNICATIONS TECHNOLOGY\n",font19Plain));
        header.add(new Chunk("_____________________________________________________\n\n",font17Bold));
        header.add(createTitle());
        document.add(header);
    }
    
    private static String studentName_, studentNumber_, address_;
    public static void createStudentInfoHeader(Document document, String studentName, String studentNumber, String address) throws DocumentException {
        studentName_ = studentName;
        studentNumber_ = studentNumber;
        address_ = address;
        document.add(createStudentInfo());
    }
    
    /**
     * 
     * @param numColumns
     * @param colNames
     * @return
     * @throws DocumentException 
     */
    public static PdfPTable createPdfPTable(int numColumns, ArrayList<String> colNames, ArrayList<String[]> rowData, HashMap<Integer, Object[]> customized, boolean showExtraHeader) throws DocumentException {
        
        System.out.println("numColumns " + numColumns);
        
        if(numColumns==0) {
            return null;
        }
        
        PdfPTable tbl_stud = new PdfPTable(numColumns);
        tbl_stud.setTotalWidth(500);
        tbl_stud.setLockedWidth(true);
        tbl_stud.setSpacingAfter(10f);
        tbl_stud.setPaddingTop(20f);
        
        tbl_stud.addCell(createSimpleCell("Total Result"+(rowData.size()>1? "s" : "")+" Found: " + rowData.size(), font6Bold, numColumns, false, false, 4f, false));
        
        if(!showExtraHeader) {
            // insertion of col names
            for (int i = 0; i < numColumns; i++) {
                if(customized.get(i) != null) {
                    String newColname = (String) colNames.get(i);
                    tbl_stud.addCell(createSimpleCell(newColname/*colNames.get(i)*/.toUpperCase(), font6Bold, 0, true, true, 5f, true));
                }
            }
        }
        // insertion of row data
        for (int i = 0; i < rowData.size(); i++) {
            if(showExtraHeader) {
                try {
                    String lastCol = rowData.get(i)[rowData.get(i).length-1];
                    if(lastCol != null) {
                        tbl_stud.addCell(createSimpleCell(lastCol.toUpperCase(), font6Plain, numColumns, true, true, 4f, true));
                        for (int a = 0; a < numColumns; a++) {
                            if(customized.get(a) != null) {
                                String newColname = (String) colNames.get(a);
                                tbl_stud.addCell(createSimpleCell(newColname/*colNames.get(i)*/.toUpperCase(), font6Bold, 0, true, true, 5f, true));
                            }
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                }
            } 
            for (int j = 0; j < customized.size(); j++) {
                if(customized.get(j) != null) {
                    Boolean isChecked = (Boolean) customized.get(j)[0];
                    if(isChecked != null && isChecked) {
                        System.out.println("ADDED " + rowData.get(i)[j] + " IN THE TABLE");
                        tbl_stud.addCell(createSimpleCell(rowData.get(i)[j], font6Plain, 0, true, false, 4f, false));
                    } else {
                        System.out.println("NOT ADDED " + rowData.get(i)[j] + " IN THE TABLE");
                    }
                } else {
                    System.out.println("ITS NULL " + rowData.get(i)[j]);
                }
            }
        }
        tbl_stud.addCell(createSimpleCell("*** Nothing Follows ***", font6Plain, numColumns, true, true, 4f, false));
        return tbl_stud;
    }
    
    
    
    
    //-------------------------------------
    //-----------------------
    private static PdfPTable createStudentInfo() throws DocumentException {
        PdfPTable tbl_stud = new PdfPTable(1);
        tbl_stud.setTotalWidth(500);
        tbl_stud.setLockedWidth(true);
        tbl_stud.setHorizontalAlignment(Element.ALIGN_CENTER);
        tbl_stud.setSpacingAfter(7f);
        /**
         * STUDENT INFO
         */
        tbl_stud.addCell(createCellWithObject(getTitleContent("Student No.: ", font8Plain, getShortenedDetail((studentNumber_==null? "" : studentNumber_), 47), font8Plain, "", true), false, true));
        tbl_stud.addCell(createCellWithObject(getTitleContent("Full Name: ", font8Plain, getShortenedDetail((studentName_==null? "" : studentName_), 40), font8Plain, "", true), false, true));
//        tbl_stud.addCell(createCellWithObject(getTitleContent("ADDRESS: ", font8Plain, getShortenedDetail((address_==null? "" : address_), 39), font8Plain, "", true), false, false));
//        tbl_stud.addCell(createCellWithObject(new Chunk("\n") ,false, true));
        
        return tbl_stud;
    }
    
    private static Paragraph create() throws DocumentException{
        Paragraph header = new Paragraph(15);
        header.setAlignment(Element.ALIGN_CENTER);
        header.add(new Chunk("Republic of the Philippines\n",font13Plain));
        header.add(new Chunk("Bulacan State University\n",font17Bold));
        header.add(new Chunk("City of Malolos, Bulacan\n",font13Plain));
        header.add(new Chunk("Tel/Fax " + PublicConstants.getSystemVar_BULSU_TEL().toString() + "\n\n",font9Plain));
//        header.add(new Chunk("COLLEGE OF INFORMATION AND COMMUNICATIONS TECHNOLOGY\n",font19Plain));
//        header.add(new Chunk("_____________________________________________________\n\n",font17Bold));
//        header.add(createTitle());
        return header;
    }
    
    private static Paragraph createTitle() {
        Paragraph p = new Paragraph(10);
        p.setSpacingAfter(3);
        p.setAlignment(Element.ALIGN_CENTER);
        if(reportDescription_!=null)
            p.add(new Chunk(reportDescription_ + "\n",font7Plain));
        
        p.add(new Chunk((reportTitle_==null? "" : reportTitle_.toUpperCase()) + "\n",font10Bold));
        
        if(reportsOtherDetails!=null)
            p.add(new Chunk(reportsOtherDetails + "\n",font7Plain));
        p.add(new Chunk("\n", font7Plain));
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
            if(tbl instanceof Image)
                cell.setImage((Image) tbl);
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
        if(!border) {
            cell.setBorder(PdfPCell.NO_BORDER);
        } else {
            cell.setBorder(1);
        }
        return cell;
    }
    
    public static Document paperSizeChooser(Stage stage) {
        PageSizeChooser sizeChooser = M.load(PageSizeChooser.class);
        sizeChooser.onDelayedStart(); // do not put database transactions on startUp
        try {
            sizeChooser.getCurrentStage().showAndWait();
        } catch (NullPointerException e) {
            Stage a = sizeChooser.createChildStage(stage);
            a.initStyle(StageStyle.UNDECORATED);
            a.showAndWait();
        }
        return sizeChooser.getChoosenSize();
    }
    
    public static ArrayList<Object> paperSizeChooserwithCustomize(Stage stage, String[] COLUMN_NAMES, String[] COLUMN_Descriptions) {
        PaperSizeChooserWithCustomize sizeChooser = M.load(PaperSizeChooserWithCustomize.class);
        sizeChooser.setCOLUMN_NAMES(COLUMN_NAMES);
        sizeChooser.setCOLUMN_Descriptions(COLUMN_Descriptions);
        sizeChooser.onDelayedStart(); // do not put database transactions on startUp
        try {
            sizeChooser.getCurrentStage().showAndWait();
        } catch (NullPointerException e) {
            Stage a = sizeChooser.createChildStage(stage);
            a.initStyle(StageStyle.UNDECORATED);
            a.showAndWait();
        }
        return sizeChooser.getChoosenSize();
    }
    
    public static boolean savePrintLogs(Integer STUDENT_id, String title, String module, String... type) {
        PrintLogsMapping logs = new PrintLogsMapping();
        logs.setModule(module);
        try {
            logs.setEVALUATION_id(Integer.valueOf(type[1]));
        } catch (Exception e) {
//            e.printStackTrace();
            logs.setEVALUATION_id(null);
        }
        logs.setPrinted_by(CollegeFaculty.instance().getFACULTY_ID());
        logs.setPrinted_date(Mono.orm().getServerTime().getDateWithFormat());
        logs.setSTUDENT_id(STUDENT_id);
        logs.setTerminal(CollegeComputer.instance().getPC_NAME() + " " +  CollegeComputer.instance().getPC_USERNAME());
        logs.setTitle(title);
        logs.setType(type[0]);
        return (!Database.connect().print_logs().insert(logs).equals(-1));
    }
}

package update3.org.cict.scheduling.printing;

import java.io.FileOutputStream;
import java.io.IOException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.jhmvin.Mono;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import org.cict.reports.ReportsDirectory;
import org.cict.reports.ReportsUtility;

public class ImageToPdf {

    public static String RESULT = "";
    public final static String SAVE_DIRECTORY = "reports/schedule";

    public ImageToPdf(String filename) {
        RESULT = SAVE_DIRECTORY + "/" + filename + ".pdf";
    }

    private Document documentFormat;
    public void setDocumentFormat(Document documentFormat) {
        this.documentFormat = documentFormat;
    }
    
    public int print() {
        try {
            init();
            boolean isCreated = ReportsDirectory.check(SAVE_DIRECTORY);

            if (!isCreated) {
                // some error message that the directory is not created
                System.err.println("Directory is not created.");
                return 0;
            }
            int val = createPdf(RESULT);
            if (val == 1) {
                System.out.println("Please close the previous report");
            }
            /* -------- run the created pdf */
            if (Desktop.isDesktopSupported()) {
                try {

                    File myFile = new File(RESULT);
                    Desktop.getDesktop().open(myFile);
                } catch (IOException ex) {
                    // no application registered for PDFs
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String imageLocation = "";

    private String location;

    public void init() {
        this.location = this.imageLocation;
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
        ReportsUtility.rotate = true;
        Document document = documentFormat==null? ReportsUtility.createShortDocument(): documentFormat;//new Document(PageSize.LEGAL_LANDSCAPE.rotate()/*);new Rectangle(Utilities.inchesToPoints(8.5f),
//                Utilities.inchesToPoints(11f))*/, 50, 50, 70, 50); //lrtb
        ReportsUtility.rotate = false;
        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
            PageFooter event = new PageFooter(ReportsUtility.formatter_mmmm.format(Mono.orm().getServerTime().getDateWithFormat()));
            writer.setPageEvent(event);
        } catch (FileNotFoundException es) {
            return 1;
        }
        document.open();
        Image img = Image.getInstance(location);
//        img.setAbsolutePosition(10, 10); //position
        float width, height;
        if(document.getPageSize().getWidth()==Utilities.inchesToPoints(11f)) /* is short */ { 
            width = document.getPageSize().getWidth() - 70;
            height = document.getPageSize().getHeight()- 190;
        } else if(document.getPageSize().getWidth()==Utilities.inchesToPoints(13f)) {
            width = document.getPageSize().getWidth() - 70;
            height = document.getPageSize().getHeight()- 85;
        } else {
            width = document.getPageSize().getWidth() - 70;
            height = document.getPageSize().getHeight()- 195;
        }
        img.scaleAbsolute(width, height); //size
        document.add(img);
        document.close();
        return 0;
    }
}

class PageFooter extends PdfPageEventHelper {
    /** The template with the total number of pages. */
    PdfTemplate total;

    private Font font_footer, font_footer2;
    private String dateTime;

    public PageFooter (String dateTime){
        try{
            this.dateTime = dateTime;
            font_footer = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL);
            font_footer2 = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL);
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
            cell.setPhrase(new Phrase("Date Printed:  " + dateTime + "  |  Powered by Monosync", font_footer2));
            table.addCell(cell);
            
            cell = new PdfPCell(Image.getInstance(total));
            cell.setBorder (0);
            cell.setBorderWidthTop (1);
            table.addCell(cell);
            table.setTotalWidth(document.getPageSize().getWidth()
                    - document.leftMargin() - document.rightMargin());
            table.writeSelectedRows(0, -1, document.leftMargin() + 70,
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
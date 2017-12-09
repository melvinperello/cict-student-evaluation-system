package update.org.cict.reports.add_change_form;

import artifacts.ResourceManager;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cict.reports.ReportsDirectory;
import org.cict.reports.advisingslip.AdvisingSlip;

public class AddChangeForm {

    public static String doc = "";
    /**
     * Path to the resulting PDF file.
     */
    public static String SAVE_DIRECTORY = "reports/adding";
    public static String RESULT = SAVE_DIRECTORY + "/" + doc + ".pdf";

    public AddChangeForm(String filename) {
        this.doc = filename;
        RESULT = "reports/adding/" + doc + ".pdf";
    }

    public int print(boolean secondCopy) {
        try {
            init();
            /**
             * Check if the directory is existing.
             */
            if (!ReportsDirectory.check(SAVE_DIRECTORY)) {
                System.err.println("Cannot Create Directory.");
            }
            int val = createPdf(RESULT, secondCopy);
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
            STUDENT_SECTION = "BSIT 4A-G1",
            STUDENT_NUMBER = "2014112478",
            COLLEGE_DEAN = "DR. NOEMI REYES",
            STUDENT_CURRENT_UNITS,
            REGISTRAR = "LEILANI M. LIZARDO";
    public Boolean IS_ADDED = true,
            IS_CHANGED = true;
    public Date DATE = new Date();
    public ArrayList<String[]> STUDENT_SUBJECTS = new ArrayList();
    /**
     * FONTS
     */
    private static final Font font8Plain = new Font(FontFamily.HELVETICA, 8);
    private static final Font font11Plain = new Font(FontFamily.HELVETICA, 11);
    private static final Font font11Bold = new Font(FontFamily.HELVETICA, 11, Font.BOLD);
    /**
     * PRIVATE VARIABLES
     */
    private String name,
            section,
            studentNumber,
            dean,
            currentUnits,
            registrar;
    private Date date;
    private ArrayList<String[]> studentSubjects = new ArrayList();
    private Boolean isAdded, isChanged;

    public void init() {
        this.name = this.STUDENT_NAME;
        this.section = this.STUDENT_SECTION;
        this.studentNumber = this.STUDENT_NUMBER;
        this.dean = this.COLLEGE_DEAN;
        this.studentSubjects = this.STUDENT_SUBJECTS;
        this.currentUnits = this.STUDENT_CURRENT_UNITS;
        this.date = this.DATE;
        this.isAdded = IS_ADDED;
        this.isChanged = IS_CHANGED;
        this.registrar = REGISTRAR;
    }

    private static PdfWriter writer;

    /**
     * Creates a PDF document.
     *
     * @param filename the path to the new PDF document
     * @throws DocumentException
     * @throws IOException
     */
    public int createPdf(String filename, boolean secondCopy)
            throws DocumentException, IOException {
        Document document = new Document(new Rectangle(Utilities.inchesToPoints(8.5f),
                Utilities.inchesToPoints(13f)), 50, 50, 70, 50); //lrtb
        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
            PageNumeration event = new PageNumeration(secondCopy);
            writer.setPageEvent(event);
        } catch (FileNotFoundException es) {
            return 1;
        }
        document.open();
        String location_logo1 = "update/org/cict/reports/images/BULSU.png";
        Image img = Image.getInstance(ResourceManager.fetchFromResource(AddChangeForm.class, location_logo1));
        img.setAbsolutePosition(287, 865); //position
        img.scaleAbsolute(55, 55); //size
        document.add(img);

        SimpleDateFormat formatter = new SimpleDateFormat("MMMMM dd, YYYY");
        Paragraph date = new Paragraph();
        date.setAlignment(Element.ALIGN_RIGHT);
        date.add(getTitleContent("Date: ", font11Plain, getShortenedDetail(formatter.format(this.date) + "\n", 40), font11Plain, "", true));
        document.add(date);

        document.add(createHeader());

        Paragraph body = new Paragraph();
        body.setFirstLineIndent(50f);
        Phrase paragraph1 = new Phrase(15);
        paragraph1.add(new Chunk("I have the honor to request that I be allowed to (", font11Plain));
        paragraph1.add(putCheckBox(isAdded));
        paragraph1.add(new Chunk(")add (", font11Plain));
        paragraph1.add(putCheckBox(isChanged));
        paragraph1.add(new Chunk(")change the following subject(s)\n", font11Plain));
        paragraph1.add(getTitleContent("this semester/trimester/summer to my present load of ", font11Plain, this.currentUnits, font11Plain, "", true));

//        body.add(new Paragraph("I have the honor to request that I be allowed to "
//                + "(" + putCheckBox(isAdded) + ")add (" + putCheckBox(isChanged) + ")change the following subject(s)\n",font11Plain));
//        body.add(getTitleContent("this semester/trimester/summer to my present load of ", font11Plain, this.currentUnits, font11Plain, "", true));
//        body.add(new Chunk(" units:",font11Plain));
//        document.add(body);
        document.add(paragraph1);
        document.add(createSubjectInfo());

        Paragraph body2 = new Paragraph();
        body2.setFirstLineIndent(50f);
        body2.setSpacingBefore(10f);
        body2.add(new Chunk("My reason for adding the subject(s) is:\n", font11Plain));
        body2.add(new Chunk("_________________________________________________________________________\n"));
        body2.add(new Chunk("_________________________________________________________________________\n"));
        document.add(body2);

        Paragraph body3 = new Paragraph();
        body3.setFirstLineIndent(50f);
        body3.setSpacingBefore(10f);
        body3.add(new Chunk("My reason for changing the subject(s) is:\n", font11Plain));
        body3.add(new Chunk("_________________________________________________________________________\n"));
        body3.add(new Chunk("_________________________________________________________________________\n"));
        document.add(body3);

        document.add(createSenderInfo());

        document.add(createDeanInfo());

        document.add(createApprovedInfo());
        document.add(new Chunk("_________________________________________________________________________\n"));
        document.add(new Chunk("(To be filled by the Accounting Office)\n"));
        document.add(createAccountingInfo());

        document.close();
        return 0;
    }

    private static Paragraph putCheckBox(boolean checked) throws DocumentException {
        BaseFont base;
        Font font = null;
        try {
            base = BaseFont.createFont(ResourceManager.fetchFromResource(AdvisingSlip.class, ReportsDirectory.REPORTS_DIR_MAIN + "advisingslip/wingding_0.ttf").toString(), BaseFont.IDENTITY_H, false);
            font = new Font(base, 12f, Font.BOLD);
        } catch (IOException ex) {
            System.out.println("IOException");
            Logger.getLogger(AdvisingSlip.class.getName()).log(Level.SEVERE, null, ex);
        }
        char checked_ = '\u00FE';
        char unchecked = '\u00A8';
        if (checked) {
            return new Paragraph(String.valueOf(checked_), font);
        } else {
            return new Paragraph(String.valueOf(unchecked), font);
        }
    }

    private static Paragraph createHeader() throws DocumentException {
        Paragraph header = new Paragraph(14);
        header.setAlignment(Element.ALIGN_LEFT);
        header.add(new Chunk("The Registrar\n", font11Plain));
        header.add(new Chunk("Bulacan State University\n", font11Plain));
        header.add(new Chunk("City of Malolos, Bulacan\n\n", font11Plain));
        header.add(new Chunk("M a d a m : \n", font11Plain));
        return header;
    }

    private PdfPTable createSubjectInfo() throws DocumentException {
        PdfPTable tbl_stud = new PdfPTable(4);
        tbl_stud.setWidths(new float[]{3, 3, 2, 4});
        tbl_stud.setTotalWidth(520);
        tbl_stud.setLockedWidth(true);
        tbl_stud.setSpacingBefore(10f);
        tbl_stud.addCell(createCellWithObject(createParagraph("SUBJECT(S)", font11Plain, false), false, true));
        tbl_stud.addCell(createCellWithObject(createParagraph("SECTION(S)", font11Plain, false), false, true));
        tbl_stud.addCell(createCellWithObject(createParagraph("NO. OF UNIT(S)", font11Plain, false), false, true));
        tbl_stud.addCell(createCellWithObject(createParagraph("SIGNATURE OF SUBJECT", font11Plain, false), false, true));
        tbl_stud.addCell(createCellWithObject(new Chunk("", font11Plain), false, true));
        tbl_stud.addCell(createCellWithObject(new Chunk("", font11Plain), false, true));
        tbl_stud.addCell(createCellWithObject(new Chunk("", font11Plain), false, true));
        tbl_stud.addCell(createCellWithObject(createParagraph("INSTRUCTOR/PROFESSOR", font11Plain, false), false, true));
        /**
         * CONTENT
         */
        for (int i = 0; i < this.studentSubjects.size(); i++) {
            String subject_from = this.studentSubjects.get(i)[3],
                    section_from = "",
                    units_from = "";
            if (!subject_from.isEmpty()) {
                subject_from = subject_from + " - ";
                section_from = this.studentSubjects.get(i)[4];
                if (!section_from.isEmpty()) {
                    if (!section_from.equalsIgnoreCase(this.studentSubjects.get(i)[1])) {
                        section_from = section_from + " - ";
                    } else {
                        section_from = "";
                    }
                }

                units_from = this.studentSubjects.get(i)[5];
                if (!units_from.isEmpty()) {
                    units_from = units_from + " to ";
                } else {
                    units_from = "";
                }
            }
            tbl_stud.addCell(createCellWithObject(createParagraph(subject_from + this.studentSubjects.get(i)[0], font8Plain, true), false, true));
            tbl_stud.addCell(createCellWithObject(createParagraph(section_from + this.studentSubjects.get(i)[1], font8Plain, true), false, true));
            tbl_stud.addCell(createCellWithObject(createParagraph(units_from + this.studentSubjects.get(i)[2], font8Plain, true), false, true));
            tbl_stud.addCell(createCellWithObject(createParagraph("______________________", font11Plain, false), false, true));
        }
        return tbl_stud;
    }

    private PdfPTable createSenderInfo() {
        PdfPTable tbl_sender = new PdfPTable(1);
        tbl_sender.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tbl_sender.setTotalWidth(200);
        tbl_sender.setLockedWidth(true);
        Paragraph bdy4 = new Paragraph();
        bdy4.setAlignment(Element.ALIGN_LEFT);
        bdy4.add(new Chunk(createContentWithSpaceOf("Very truly yours,", 24 + 19), font11Plain));
        tbl_sender.addCell(createCellWithObject(bdy4, false, true));
        Paragraph body4 = new Paragraph();
        body4.setAlignment(Element.ALIGN_CENTER);
        body4.setSpacingBefore(10f);
        body4.add(getTitleContent("\n", font11Plain, this.name, font11Plain, "\n", true));
        body4.add(new Chunk("(Signature Over Printed Name)", font11Plain));
        body4.add(getTitleContent("\n\n", font11Plain, this.section, font11Plain, "\n", true));
        body4.add(new Chunk("(Course-Year and Section)", font11Plain));
        body4.add(getTitleContent("\n\nStudent No: ", font11Plain, this.studentNumber, font11Plain, "\n", true));
        tbl_sender.addCell(createCellWithObject(body4, false, true));
        return tbl_sender;
    }

    private PdfPTable createDeanInfo() {
        Paragraph body5 = new Paragraph();
        body5.setAlignment(Element.ALIGN_CENTER);
        body5.setSpacingBefore(10f);
        body5.add(new Chunk("RECOMMENDING APPROVAL:", font11Plain));
        body5.add(getTitleContent("\n\n", font11Plain, this.dean, font11Plain, "\n", true));
        body5.add(new Chunk("(DEAN)\n", font11Plain));

        PdfPTable tbl_dean = new PdfPTable(1);
        tbl_dean.setHorizontalAlignment(Element.ALIGN_LEFT);
        tbl_dean.setTotalWidth(200);
        tbl_dean.setLockedWidth(true);
        tbl_dean.addCell(createCellWithObject(body5, false, true));
        return tbl_dean;
    }

    private PdfPTable createApprovedInfo() {
        PdfPTable tbl_approved = new PdfPTable(1);
        tbl_approved.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tbl_approved.setTotalWidth(200);
        tbl_approved.setLockedWidth(true);
        Paragraph bdy4 = new Paragraph();
        bdy4.setAlignment(Element.ALIGN_LEFT);
        bdy4.add(new Chunk("APPROVED:\n", font11Plain));
        tbl_approved.addCell(createCellWithObject(bdy4, false, true));
        Paragraph body4 = new Paragraph();
        body4.setAlignment(Element.ALIGN_CENTER);
        body4.setSpacingBefore(10f);
        body4.add(getTitleContent("\n", font11Plain, registrar, font11Bold, "\n", true));
        body4.add(new Chunk("Registrar IV", font11Plain));
        tbl_approved.addCell(createCellWithObject(body4, false, true));
        return tbl_approved;
    }

    private PdfPTable createAccountingInfo() throws DocumentException {
        PdfPTable tbl_accntng = new PdfPTable(2);
        tbl_accntng.setWidths(new float[]{2, 3});
        tbl_accntng.setHorizontalAlignment(Element.ALIGN_LEFT);
        tbl_accntng.setTotalWidth(255);
        tbl_accntng.setLockedWidth(true);
        tbl_accntng.addCell(createCellWithObject(new Chunk("Amount Paid: "), false, false));
        tbl_accntng.addCell(createCellWithObject(new Chunk("_____________________"), false, false));
        tbl_accntng.addCell(createCellWithObject(new Chunk("O.R. No:"), false, false));
        tbl_accntng.addCell(createCellWithObject(new Chunk("_____________________"), false, false));
        tbl_accntng.addCell(createCellWithObject(new Chunk("Assessed By: "), false, false));
        tbl_accntng.addCell(createCellWithObject(new Chunk("_____________________"), false, false));
        return tbl_accntng;
    }

    private Paragraph createParagraph(String str, Font font, boolean underline) {
        Paragraph paragraph = new Paragraph(10);
        paragraph.setSpacingAfter(0);
        paragraph.setSpacingBefore(0);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        Chunk chnk = new Chunk(createContentWithSpaceOf(str, 50), font);
        if (underline) {
            chnk.setUnderline(0.9f, -2f);
        }
        paragraph.add(chnk);
        return paragraph;
    }

    private static void putTitleContent(Paragraph phr, String title, Font font1, String info, Font font2, String space, boolean underlined) {
        phr.add(new Chunk(title, font1));
        Chunk chunks = new Chunk(info + space, font2);
        if (underlined) {
            chunks.setUnderline(0.1f, -2f);
        }
        phr.add(chunks);
    }

    private String createContentWithSpaceOf(String content, int total) {
        if (content.length() < total) {
            int left = total - content.length();
            return content + String.join("", Collections.nCopies(left, " "));
        } else {
            return content;
        }
    }

    private static PdfPCell createCellWithObject(Object tbl, boolean border, boolean center) {
        PdfPCell cell = new PdfPCell();
        if (!border) {
            cell.setBorder(PdfPCell.NO_BORDER);
        }
        if (center) {
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        }
        cell.addElement((Element) tbl);
        return cell;
    }

    private static Phrase getTitleContent(String title, Font font1, String info, Font font2, String space, boolean underlined) {
        Phrase phr = new Phrase(10);
        phr.add(new Chunk(title, font1));
        Chunk chunks = new Chunk(info, font2);
        if (underlined) {
            chunks.setUnderline(0.1f, -2f);
        }
        phr.add(chunks);
        phr.add(new Chunk(space, font2));
        return phr;
    }

    private static String getShortenedDetail(String str, int count) {
        if (str.length() > count) {
            return str.substring(0, count) + "...";
        } else {
            return str;
        }
    }
}

class PageNumeration extends PdfPageEventHelper {

    /**
     * The template with the total number of pages.
     */
    PdfTemplate total;

    private Font font_footer, font_footer2, font_footer3;
    private boolean secondCopy = false;
    
    public PageNumeration(boolean secondCopy) {
        try {
            font_footer = new Font(FontFamily.HELVETICA, 9, Font.BOLD);
            font_footer3 = new Font(FontFamily.HELVETICA, 11, Font.BOLD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.secondCopy = secondCopy;
    }

    /**
     * Creates the PdfTemplate that will hold the total number of pages.
     *
     * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(
     * com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
     */
    public void onOpenDocument(PdfWriter writer, Document document) {
        total = writer.getDirectContent().createTemplate(30, 12);
    }

    /**
     * Adds a header to every page
     *
     * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(
     * com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
     */
    public void onEndPage(PdfWriter writer, Document document) {
        PdfPTable table = new PdfPTable(2);
        try {
            table.setWidths(new int[]{24, 2});
            table.getDefaultCell().setFixedHeight(10);
            PdfPCell cell = new PdfPCell();
            cell.setBorder(0);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setPhrase(new Phrase(String.format((secondCopy? "(SECOND COPY) " : "") + "Page %d of ", writer.getPageNumber()), font_footer));
            table.addCell(cell);

            cell = new PdfPCell(Image.getInstance(total));
            cell.setBorder(0);
            table.addCell(cell);
            table.setTotalWidth(document.getPageSize().getWidth()
                    - document.leftMargin() - document.rightMargin());
            table.writeSelectedRows(0, -1, document.leftMargin() + 50,
                    document.bottomMargin() - 10, writer.getDirectContent());
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }

        PdfContentByte cb = writer.getDirectContent();
        Phrase footer = new Phrase("BulSU-OP-OUR-01F4", font_footer3);
        Phrase footer2 = new Phrase("Revision: 0", font_footer3);

        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                footer,
                50, //x
                document.bottom() - 20, //y
                0);
        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                footer2,
                50,
                (document.bottom() - 35),
                0);
    }

    /**
     * Fills out the total number of pages before the document is closed.
     *
     * @see com.itextpdf.text.pdf.PdfPageEventHelper#onCloseDocument(
     * com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
     */
    public void onCloseDocument(PdfWriter writer, Document document) {
        ColumnText.showTextAligned(total, Element.ALIGN_LEFT,
                new Phrase(String.valueOf(writer.getPageNumber()), font_footer),
                2, 1, 0);
    }
}

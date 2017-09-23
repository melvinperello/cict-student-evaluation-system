package org.cict.reports.advisingslip;

import com.itextpdf.text.Chunk;
import java.io.FileOutputStream;
import java.io.IOException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdvisingSlip {

    /**
     * Path to the resulting PDF file.
     */
    private String RESULT;
    public String document;

    public AdvisingSlip(String filename) {
        document = filename;
        RESULT = "reports/advising/" + document + ".pdf";
    }
    
    public int print() {
        try {

            init();

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
            /**
             * ****************** end run
             */
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //##########################################################################
    //                          INITIALIZATION                                 #
    //##########################################################################
    /* private all class variables that is not allowed to be accessed outside the class */
    private static String university, campus, course, title, school_year, semester, date,
            stud_num, stud_name, adviser,
            major = "_____________________________________________",
            remarks =          " _______________________________________________\n"
            + "                  _______________________________________________\n"
            + "                  _______________________________________________\n"
            + "                  _______________________________________________\n"
            + "                  _______________________________________________";
    private static int ttl_subjects;
    private static Double ttl_lec_units, ttl_lab_units;
    private static final Font FONT_HEADER = new Font(FontFamily.HELVETICA, 12, Font.BOLD);
    
    private static final Font FONT_PAGE_DETAILS = new Font(FontFamily.HELVETICA, 9);
    private static final Font FONT_CELL_HEADER = new Font(FontFamily.HELVETICA, 8, Font.BOLD);
    private static final Font FONT_CELL_HEADER_2 = new Font(FontFamily.HELVETICA, 6, Font.BOLD);
    private static final Font FONT_EXCESS_INFO = new Font(FontFamily.HELVETICA, 8);
    private final SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyyhhmmss");
    private static boolean old, new_, regular, irregular, 
            underline_remarks = false,
            underline_major = false;

    public ArrayList<AdvisingSlipData> INFO_SUBJECTS = new ArrayList<>();
    public String INFO_STUD_NUM = "NOTHING TO DISPLAY";
    public String INFO_STUD_NAME = "NOTHING TO DISPLAY";
    public String INFO_COURSE = "NOTHING TO DISPLAY";
    public String INFO_DATE = "NOTHING TO DISPLAY"; //formatter.format(new Date());
    public String INFO_ACAD_YEAR = "NOTHING TO DISPLAY";
    public String INFO_TERM = "NOTHING TO DISPLAY";
    public String INFO_CAMPUS = "NOTHING TO DISPLAY";

    public boolean INFO_OLD = false;
    public boolean INFO_NEW = false;
    public boolean INFO_REGULAR = false;
    public boolean INFO_IRREGULAR = false;

    public String INFO_MAJOR = "NOTHING TO DISPLAY";
    public String INFO_REMARKS = "NOTHING TO DISPLAY";
    public String INFO_ADVISER = "NOTHING TO DISPLAY";

    public void init() {
        // static data_
        university = "BULACAN STATE UNIVERSITY";
        title = "Registration Advising Slip";
        school_year = this.INFO_ACAD_YEAR;
        semester = this.INFO_TERM;
        campus = this.INFO_CAMPUS;
        course = this.INFO_COURSE;
        if(!this.INFO_MAJOR.isEmpty()){
            major = this.INFO_MAJOR;
            underline_major = true;
        }
        date = this.INFO_DATE;
        stud_num = this.INFO_STUD_NUM;
        stud_name = this.INFO_STUD_NAME;
        old = this.INFO_OLD;
        new_ = this.INFO_NEW;
        regular = this.INFO_REGULAR;
        irregular = this.INFO_IRREGULAR;
        if(!this.INFO_REMARKS.isEmpty()){
            remarks = this.INFO_REMARKS;
            underline_remarks = true;
        }
        adviser = INFO_ADVISER;
        ttl_subjects = 0;
        ttl_lab_units = 0.0;
        ttl_lec_units = 0.0;
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
        Document document = new Document(new Rectangle(Utilities.inchesToPoints(8.27f), Utilities.inchesToPoints(5.845f)),
                0, 0, 10, 5);
        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        } catch (FileNotFoundException es) {
            return 1;
        }
        document.open();
        document.add(createHeaderInfo());
        document.add(createSubjectTable(this.INFO_SUBJECTS));
        document.add(createBelowTable(createRegistrationProcedure(), createTotalTable()));
        document.add(createFooter());
        document.close();
        return 0;
    }

    public static PdfPTable createHeaderInfo() throws DocumentException {
        PdfPTable tbl_stud = new PdfPTable(2);
        tbl_stud.setWidths(new float[]{3, 3});
        tbl_stud.setHorizontalAlignment(Element.ALIGN_CENTER);
        tbl_stud.setTotalWidth(565);
        tbl_stud.setLockedWidth(true);

        //University Name
        tbl_stud.addCell(createCell(university, FONT_HEADER, false, false));

        //Title - Date
        Phrase phr_details2 = new Phrase();
        phr_details2.add(new Chunk(title, FONT_HEADER));
        putTitleContent(phr_details2, " Date: ", FONT_PAGE_DETAILS, getShortenedDetail(date, 22), new Font(FontFamily.HELVETICA, 7), "", true);
        PdfPCell cell_6 = new PdfPCell();
        cell_6.setBorder(PdfPCell.NO_BORDER);
        Paragraph p = new Paragraph(8);
        p.add(phr_details2);
        cell_6.addElement(p);
        tbl_stud.addCell(cell_6);

        //Space
        tbl_stud.addCell(createCell(" ", FONT_PAGE_DETAILS, false, false));

        //Advising details
        Phrase advising_details = new Phrase();
        putTitleContent(advising_details, "Academic Year: ", FONT_PAGE_DETAILS, school_year, FONT_PAGE_DETAILS, "", true);
        putTitleContent(advising_details, " Term: ", FONT_PAGE_DETAILS, semester, FONT_PAGE_DETAILS, "", true);
        putTitleContent(advising_details, " Campus: ", FONT_PAGE_DETAILS, getShortenedDetail(campus, 15), FONT_PAGE_DETAILS, " ", true);
        PdfPCell cell_3 = new PdfPCell();
        cell_3.setBorder(PdfPCell.NO_BORDER);
        Paragraph p1 = new Paragraph(8);
        p1.add(advising_details);
        cell_3.addElement(p1);
        tbl_stud.addCell(cell_3);

        //Student No.
        Phrase phr_details = new Phrase();
        putTitleContent(phr_details, "Student No: ", FONT_PAGE_DETAILS, getShortenedDetail(stud_num, 40), FONT_PAGE_DETAILS, "\n", true);
        PdfPCell cell_ = new PdfPCell();
        cell_.setBorder(PdfPCell.NO_BORDER);
        Paragraph p2 = new Paragraph(8);
        p2.add(phr_details);
        cell_.addElement(p2);
        tbl_stud.addCell(cell_);

        //Advising details2
        Phrase advising_details2 = new Phrase(15);
        advising_details2.add(new Chunk("Old Student ", FONT_PAGE_DETAILS));
        advising_details2.add(putCheckBox(old));
        advising_details2.add(new Chunk(" New Student ", FONT_PAGE_DETAILS));
        advising_details2.add(putCheckBox(new_));
        advising_details2.add(new Chunk(" Regular ", FONT_PAGE_DETAILS));
        advising_details2.add(putCheckBox(regular));
        advising_details2.add(new Chunk(" Irregular ", FONT_PAGE_DETAILS));
        advising_details2.add(putCheckBox(irregular));
        PdfPCell cell_4 = new PdfPCell();
        cell_4.setPadding(0);
        cell_4.setBorder(PdfPCell.NO_BORDER);
        cell_4.addElement(advising_details2);
        tbl_stud.addCell(cell_4);

        //Student Name
        PdfPCell cell1 = new PdfPCell();
        cell1.setBorder(PdfPCell.NO_BORDER);
        Paragraph phr_details1 = new Paragraph(8);
        putTitleContent(phr_details1, "Full Name: ", FONT_PAGE_DETAILS, getShortenedDetail(stud_name, 41), FONT_PAGE_DETAILS, "\n", true);
        cell1.addElement(phr_details1);
        tbl_stud.addCell(cell1);

        //Space
        tbl_stud.addCell(createCell(" ", FONT_PAGE_DETAILS, false, false));

        PdfPCell cell3 = new PdfPCell();
        cell3.setBorder(PdfPCell.NO_BORDER);
        Phrase phr_details3 = new Phrase();
        putTitleContent(phr_details3, "Course: ", FONT_PAGE_DETAILS, getShortenedDetail(course, 83), FONT_PAGE_DETAILS, "\n\n", true);
        Paragraph p3 = new Paragraph(8);
        p3.add(phr_details3);
        cell3.addElement(p3);
        tbl_stud.addCell(cell3);

        PdfPCell cell4 = new PdfPCell();
        cell4.setBorder(PdfPCell.NO_BORDER);
        Paragraph phr_details4 = new Paragraph(8);
        putTitleContent(phr_details4, "Major: ", FONT_PAGE_DETAILS, getShortenedDetail(major, 45), FONT_PAGE_DETAILS, "", underline_major);
        cell4.addElement(phr_details4);
        tbl_stud.addCell(cell4);
        return tbl_stud;
    }

    public static PdfPTable createSubjectTable(ArrayList<AdvisingSlipData> table_data) throws DocumentException {
        PdfPTable table = new PdfPTable(7);
        table.setPaddingTop(10f);
        table.setTotalWidth(565);
        table.setLockedWidth(true);
        table.setWidths(new float[]{2, 5, 2, 2, 2, 1, 1});
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        float lineSpacing = 10;

        /* TABLE HEADERS */
        table.addCell(createCell("Subject Code", FONT_CELL_HEADER, true, true));
        table.addCell(createCell("Subject Descriptive Title", FONT_CELL_HEADER, true, true));
        table.addCell(createCell("Section", FONT_CELL_HEADER, true, true));
        table.addCell(createCell("Schedule", FONT_CELL_HEADER, true, true));
        table.addCell(createCell("Room", FONT_CELL_HEADER, true, true));
        table.addCell(createCell("Lecture Units", FONT_CELL_HEADER, true, true));
        table.addCell(createCell("Laboratory Units", FONT_CELL_HEADER_2, true, true));

        /* TABLE CONTENTS */
        String sc, t, s, scd, rm, lec, lab;
        for (int i = 0; i < 12; i++) {
            sc = " ";
            t = " ";
            s = " ";
            scd = " ";
            rm = " ";
            lec = " ";
            lab = " ";
            try {
                if (!table_data.get(i).subject_code.isEmpty()) {
                    sc = getShortenedDetail(table_data.get(i).subject_code, 11);
                }
                if (!table_data.get(i).subj_title.isEmpty()) {
                    t = getShortenedDetail(table_data.get(i).subj_title, 40);
                }
                if (!table_data.get(i).section.isEmpty()) {
                    s = getShortenedDetail(table_data.get(i).section, 11);
                }
                if (!table_data.get(i).schedule.isEmpty()) {
                    scd = getShortenedDetail(table_data.get(i).schedule, 11);
                }
                if (!table_data.get(i).room.isEmpty()) {
                    rm = getShortenedDetail(table_data.get(i).room, 11);
                }
                if (!table_data.get(i).lec_units.isEmpty()) {
                    lec = getShortenedDetail(table_data.get(i).lec_units, 4);
                }
                if (!table_data.get(i).lab_units.isEmpty()) {
                    lab = getShortenedDetail(table_data.get(i).lab_units, 4);
                }
                ttl_subjects++;
                ttl_lab_units += Double.valueOf(lab);
                ttl_lec_units += Double.valueOf(lec);
            } catch (IndexOutOfBoundsException a) {
            }
            table.addCell(createCell(sc, FONT_EXCESS_INFO, true, true));
            table.addCell(createCell(t, new Font(FontFamily.HELVETICA, 7), true, true));
            table.addCell(createCell(s, FONT_EXCESS_INFO, true, true));
            table.addCell(createCell(scd, FONT_EXCESS_INFO, true, true));
            table.addCell(createCell(rm, FONT_EXCESS_INFO, true, true));
            table.addCell(createCell(lec, FONT_EXCESS_INFO, true, true));
            table.addCell(createCell(lab, FONT_EXCESS_INFO, true, true));
        }
        return table;
    }

    public static PdfPTable createRegistrationProcedure() throws DocumentException {
        PdfPTable tbl_procedure = new PdfPTable(1);
        tbl_procedure.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        tbl_procedure.setTotalWidth(265);
        tbl_procedure.setLockedWidth(true);
        tbl_procedure.addCell(createCell("Registration Procedure", FONT_CELL_HEADER, true, true));
        PdfPCell cell1 = new PdfPCell();
        cell1.setPadding(5f);
        Phrase details = new Phrase();
        details.add(new Chunk("Copy subject(s)/section to enroll in this form.\n", FONT_EXCESS_INFO));
        details.add(new Chunk("Attach Report of Grades from the last Semester attended.\n", FONT_EXCESS_INFO));
        details.add(new Chunk("Proceed to the Advising Section of your respective Colleges/Institutes & have this form verified and signed by the adviser.\n", FONT_EXCESS_INFO));
        details.add(new Chunk("Proceed to the Accounting for the encoding of subjects and assessment print-out.\n", FONT_EXCESS_INFO));
        details.add(new Chunk("Proceed to the Cashier for payment.\n", FONT_EXCESS_INFO));
        details.add(new Chunk("Proceed to the Registrar for the printing and release of the Certificate of Registration.\n", FONT_EXCESS_INFO));
        Paragraph p = new Paragraph(10);
        p.setAlignment(Element.ALIGN_LEFT);
        p.setIndentationLeft(0);
        p.setIndentationRight(0);
        p.add(details);
        cell1.addElement(p);
        tbl_procedure.addCell(cell1);
        return tbl_procedure;
    }

    public static PdfPTable createTotalTable() throws DocumentException {
        PdfPTable tbl_procedure = new PdfPTable(1);
        tbl_procedure.setWidths(new float[]{3});
        tbl_procedure.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        tbl_procedure.setTotalWidth(275);
        tbl_procedure.setLockedWidth(true);

        PdfPCell cell1 = new PdfPCell();
        cell1.setPadding(5f);
        Phrase details = new Phrase(11);
        details.add(new Chunk("TOTALS: ", FONT_HEADER));
        putTitleContent(details, "Subjects ", FONT_CELL_HEADER, String.valueOf(ttl_subjects), FONT_CELL_HEADER, "", true);
        putTitleContent(details, " Lecture Units ", FONT_EXCESS_INFO, String.valueOf(ttl_lec_units), FONT_CELL_HEADER, "", true);
        putTitleContent(details, " Lab Units ", FONT_EXCESS_INFO, String.valueOf(ttl_lab_units), FONT_CELL_HEADER, "\n\n", true);
        putTitleContent(details, "Remarks: ", FONT_CELL_HEADER, getShortenedDetail(remarks, 400), FONT_EXCESS_INFO, "\n\n", underline_remarks);
        putTitleContent(details, "Adviser: ", FONT_CELL_HEADER, adviser, FONT_EXCESS_INFO, "", true);
        putTitleContent(details, " Date: ", FONT_CELL_HEADER, date, FONT_EXCESS_INFO, "", true);
        cell1.addElement(details);
        Paragraph p = new Paragraph(8);
        p.add(details);
        tbl_procedure.addCell(cell1);
        return tbl_procedure;
    }

    public static PdfPTable createBelowTable(PdfPTable tbl1, PdfPTable tbl2) throws DocumentException {
        PdfPTable tbl_procedure = new PdfPTable(2);
        tbl_procedure.setTotalWidth(580);
        tbl_procedure.setLockedWidth(true);
        tbl_procedure.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell cell1 = new PdfPCell();
        cell1.setPadding(5f);
        cell1.addElement(tbl1);
        cell1.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        cell1.setBorder(PdfPCell.NO_BORDER);
        tbl_procedure.addCell(cell1);

        PdfPCell cell2 = new PdfPCell();
        cell2.setPadding(5f);
        cell2.setBorder(PdfPCell.NO_BORDER);
        cell2.addElement(tbl2);
        cell2.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        tbl_procedure.addCell(cell2);

        return tbl_procedure;
    }

    private static void putTitleContent(Phrase phr, String title, Font font1, String info, Font font2, String space, boolean underlined) {
        phr.add(new Chunk(title, font1));
        Chunk chunks = new Chunk(info + space, font2);
        if(underlined)
            chunks.setUnderline(0.1f, -2f);
        phr.add(chunks);
    }

    private static Paragraph putCheckBox(boolean checked) throws DocumentException {
        BaseFont base;
        Font font = null;
        try {
            base = BaseFont.createFont("src/org/cict/reports/advisingslip/wingding_0.ttf", BaseFont.IDENTITY_H, false);
            font = new Font(base, 16f, Font.BOLD);
        } catch (IOException ex) {
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

    private static Paragraph createFooter() {
        Paragraph p = new Paragraph(10);
        p.setSpacingAfter(0);
        p.setSpacingBefore(0);
        p.setAlignment(Element.ALIGN_LEFT);
        p.setIndentationLeft(12);
        p.add(new Chunk("Note: FOR YOUR REGISTRATION TO BE PROCESSED, YOU MUST SETTLE ALL YOUR OUTSTANDING BALANCES.\n", FONT_CELL_HEADER));
        p.add(new Chunk("BulSU-OP-OUR-01F5\n", FONT_CELL_HEADER));
        p.add(new Chunk("Revision: 0", FONT_EXCESS_INFO));
        return p;
    }

    private static PdfPCell createCell(String title, Font font, boolean border, boolean center) {
        Paragraph p_title = new Paragraph(5);
        p_title.add(new Phrase(10, title, font));
        p_title.setLeading(0, 1);
        PdfPCell cell = new PdfPCell();
        if (!border) {
            cell.setBorder(PdfPCell.NO_BORDER);
        }
        if (center) {
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            p_title.setAlignment(Element.ALIGN_CENTER);
        }
        cell.addElement(p_title);
        return cell;
    }

    private static String getShortenedDetail(String str, int count) {
        if (str.length() > count) {
            return str.substring(0, count) + "...";
        } else {
            return str;
        }
    }
}

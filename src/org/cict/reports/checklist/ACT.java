package org.cict.reports.checklist;

import app.lazy.models.SubjectMapping;
import artifacts.ResourceManager;
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
import org.cict.SubjectClassification;
import org.cict.reports.ReportsDirectory;
import org.cict.reports.profile.student.StudentProfile;

public class ACT {

//    public static String doc;
    public static String RESULT;

    public ACT(String filename) {
//        doc = filename;
        RESULT = filename;//"reports/checklist/" + doc + ".pdf";
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
    /**
     * PUBLIC VARIABLES
     */
    public String STUDENT_NAME = "DE LA CRUZ, JOEMAR NUCOM",
            STUDENT_COURSE_YR_SEC_GRP = "8-152 SUCAD APALIT, PAMPANGA",
            STUDENT_NUMBER = "09123456789";
    public HashMap<String, ArrayList<Object[]>> SUBJECTS_PER_SEM = new HashMap<String, ArrayList<Object[]>>();

    /**
     * FONTS
     */
    private static final Font font7Plain = new Font(FontFamily.HELVETICA, 7);
    private static final Font font7Bold = new Font(FontFamily.HELVETICA, 7, Font.BOLD);
    private static final Font font6Plain = new Font(FontFamily.HELVETICA, 6);
    private static final Font font6Bold = new Font(FontFamily.HELVETICA, 6, Font.BOLD);
    private static final Font font5Plain = new Font(FontFamily.HELVETICA, 5);
    private static final Font font5Bold = new Font(FontFamily.HELVETICA, 5, Font.BOLD);
    private static final Font font5BoldItalic = new Font(FontFamily.HELVETICA, 5, Font.BOLDITALIC);
    /**
     * PRIVATE VARIABLES
     */
    private String name,
            courseYrSecGrp,
            studentNo;
    private HashMap<String, ArrayList<Object[]>> subjectsPerSem = new HashMap<String, ArrayList<Object[]>>();

    public void init() {
        this.studentNo = this.STUDENT_NUMBER;
        this.name = this.STUDENT_NAME;
        this.courseYrSecGrp = this.STUDENT_COURSE_YR_SEC_GRP;
        this.subjectsPerSem = this.SUBJECTS_PER_SEM;
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
                Utilities.inchesToPoints(13f)), 70, 70, 70, 50); //lrtb
        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        } catch (FileNotFoundException es) {
            return 1;
        }
        document.open();
        String location_logo1 = ReportsDirectory.REPORTS_DIR_IMAGES + "checklist/BULSU.png",
                location_logo2 = ReportsDirectory.REPORTS_DIR_IMAGES + "checklist/CICT.png";
        Image img = Image.getInstance(ResourceManager.fetchFromResource(ACT.class, location_logo1));
        img.setAbsolutePosition(100, 815); //position
        img.scaleAbsolute(50, 50); //size
        document.add(img);
        Image img2 = Image.getInstance(ResourceManager.fetchFromResource(StudentProfile.class, location_logo2));
        img2.setAbsolutePosition(455, 815); //position
        img2.scaleAbsolute(50, 50); //size
        document.add(img2);

        document.add(createHeader());
        document.add(createStudentInfo());
        document.add(createBody());
        document.add(createOverallTable());
        writer.setPageEvent(new MyFooter1());
        document.close();
        return 0;
    }

    private static Paragraph createHeader() throws DocumentException {
        Paragraph header = new Paragraph(10);
        header.setAlignment(Element.ALIGN_CENTER);
        header.add(new Chunk("BULACAN STATE UNIVERSITY\n", font7Plain));
        header.add(new Chunk("COLLEGE OF INFORMATION AND COMMUNICATIONS TECHNOLOGY\n", font7Bold));
        header.add(new Chunk("City of Malolos, Bulacan\n\n", font7Plain));
        header.add(new Chunk("Associate in Computer Technology\n", font7Bold));
        header.add(new Chunk("CURRICULUM FOR S.Y. 2015-2016\n", font7Bold));
        header.add(new Chunk("\n", font7Plain));
        return header;
    }

    private PdfPTable createStudentInfo() throws DocumentException {
        PdfPTable tbl_stud = new PdfPTable(2);
        tbl_stud.setTotalWidth(500);
        tbl_stud.setLockedWidth(true);
        tbl_stud.setHorizontalAlignment(Element.ALIGN_CENTER);
        tbl_stud.setSpacingAfter(7f);
        /**
         * STUDENT INFO
         */
        tbl_stud.addCell(createCellWithObject(getTitleContent("NAME: ", font7Bold, getShortenedDetail(this.name, 40), font7Plain, "", true), false, true));
        tbl_stud.addCell(createCellWithObject(getTitleContent("STUDENT #: ", font7Bold, getShortenedDetail(this.studentNo, 47), font7Plain, "", true), false, true));
        tbl_stud.addCell(createCellWithObject(getTitleContent("COURSE YR, SEC & GRP: ", font7Bold, getShortenedDetail(this.courseYrSecGrp, 39), font7Plain, "", true), false, false));
        tbl_stud.addCell(createCellWithObject(new Chunk(""), false, true));

        return tbl_stud;
    }

    private PdfPTable createBody() throws DocumentException {
        PdfPTable tbl_stud = new PdfPTable(1);
        tbl_stud.setPaddingTop(10f);
        tbl_stud.setTotalWidth(575);
        tbl_stud.setLockedWidth(true);
        /**
         * SEMESTERS
         */
        for (int i = 0; i < 4; i++) {
            if (i / 2 != 0) {
                year = 2;
            }
            tbl_stud.addCell(createCellWithObject(createSemesterTable(), false, true));
        }
        return tbl_stud;
    }

    private String objectKey, yearLevel, semester;
    private int year = 1;
    private Double totalHrLabOverall = 0.0,
            totalHrLecOverall = 0.0,
            totalUnitsLabOverall = 0.0,
            totalUnitsLecOverall = 0.0,
            totalUnitsCreOverall = 0.0,
            totalHrsWkOverall = 0.0;

    private PdfPTable createSemesterTable() throws DocumentException {
        PdfPTable tbl_stud = new PdfPTable(11);
        tbl_stud.setWidths(new float[]{2, 5, 1, 1, 1, 1, 2, 2, 2, 2, 2});
        tbl_stud.setTotalWidth(500);
        tbl_stud.setLockedWidth(true);
        tbl_stud.setSpacingAfter(10f);
        this.setTitleHeader();
        /**
         * HEADER
         */
        tbl_stud.addCell(createSimpleCell(yearLevel, font5Bold, 11, true, true));
        tbl_stud.addCell(createSimpleCell(semester, font5Bold, 11, true, true));
        tbl_stud.addCell(createSimpleCell("COURSE CODE", font5Bold, 0, true, false));
        tbl_stud.addCell(createSimpleCell("DESCRIPTIVE TITLE", font5Bold, 0, true, false));
        tbl_stud.addCell(createSimpleCell("HOUR\nLAB", font5Bold, 0, true, false));
        tbl_stud.addCell(createSimpleCell("HOUR\nLEC", font5Bold, 0, true, false));
        tbl_stud.addCell(createSimpleCell("UNITS\nLAB", font5Bold, 0, true, false));
        tbl_stud.addCell(createSimpleCell("UNITS\nLEC", font5Bold, 0, true, false));
        tbl_stud.addCell(createSimpleCell("TOTAL UNITS\nCREDIT", font5Bold, 0, true, false));
        tbl_stud.addCell(createSimpleCell("Pre-REQ.", font5Bold, 0, true, false));
        tbl_stud.addCell(createSimpleCell("Co-REQ.", font5Bold, 0, true, false));
        tbl_stud.addCell(createSimpleCell("TOTAL\nHrs/Wk", font5Bold, 0, true, false));
        tbl_stud.addCell(createSimpleCell("Grade", font5Bold, 0, true, false));

        /**
         * DATA
         */
        Double totalHrLab = 0.0,
                totalHrLec = 0.0,
                totalUnitsLab = 0.0,
                totalUnitsLec = 0.0,
                totalUnitsCre = 0.0,
                totalHrsWk = 0.0;
        ArrayList<Object[]> subjects = this.subjectsPerSem.get(objectKey);
        //0-subjectmap
        //1-total hrs
        //2-prereq
        //3-co-req
        for (int i = 0; i < subjects.size(); i++) {
            SubjectMapping subject = (SubjectMapping) subjects.get(i)[0];
            tbl_stud.addCell(createSimpleCell(subject.getCode(), font5Plain, 0, true, false));
            tbl_stud.addCell(createSimpleCell(subject.getDescriptive_title(), font5Plain, 0, false, false));
            String hrLab = "0.0", lec = "0.0", lab = "0.0", totalUnits = "";
            if (subject.getType().equals(SubjectClassification.TYPE_NSTP)) {
                hrLab = "";
                lec = "";
                lab = "";
                totalUnits = "(1.5)";
            } else {
                if (subject.getLab_units() != 0) {
                    hrLab = (subject.getLab_units() * 3) + "";
                }
                lec = subject.getLec_units() + "";
                lab = subject.getLab_units() + "";

                totalHrLab += Double.valueOf(hrLab);
                totalHrLec += Double.valueOf(lec);
                totalUnitsLab += Double.valueOf(lab);
                totalUnitsLec += Double.valueOf(lec);
                totalUnits = (subject.getLec_units() + subject.getLab_units()) + "";
                totalUnitsCre += Double.valueOf(totalUnits);
            }
            tbl_stud.addCell(createSimpleCell(hrLab, font5Plain, 0, true, false));

            tbl_stud.addCell(createSimpleCell(lec, font5Plain, 0, true, false));

            tbl_stud.addCell(createSimpleCell(lab, font5Plain, 0, true, false));

            tbl_stud.addCell(createSimpleCell(lec, font5Plain, 0, true, false));

            tbl_stud.addCell(createSimpleCell(totalUnits, font5Plain, 0, true, false));

            tbl_stud.addCell(createSimpleCell(subjects.get(i)[2] + "", font5Plain, 0, true, false));
            tbl_stud.addCell(createSimpleCell(subjects.get(i)[3] + "", font5Plain, 0, true, false));
            Double hrsWk;
            try {
                hrsWk = Double.valueOf(hrLab) + Double.valueOf(lec);
                totalHrsWk += hrsWk;
            } catch (Exception e) {
                hrsWk = 0.0;
            }
            tbl_stud.addCell(createSimpleCell(hrsWk + "", font5Plain, 0, true, false));

            //------------------------------------------------------------------
            // Grade Column
            String grade = "";
            try {
                grade = subjects.get(i)[5].toString();
            } catch (Exception e) {
                //
            }
            tbl_stud.addCell(createSimpleCell(grade, font5Plain, 0, true, false));
            //------------------------------------------------------------------

        }

        /**
         * CALCULATE OVERALL
         */
        totalHrLabOverall += totalHrLab;
        totalHrLecOverall += totalHrLec;
        totalUnitsLabOverall += totalUnitsLab;
        totalUnitsLecOverall += totalUnitsLec;
        totalUnitsCreOverall += totalUnitsCre;
        totalHrsWkOverall += totalHrsWk;

        /**
         * TOTALS
         */
        tbl_stud.addCell(createSimpleCell("", font5Bold, 0, true, false));
        tbl_stud.addCell(createSimpleCell("TOTAL", font5Bold, 0, false, false));
        tbl_stud.addCell(createSimpleCell(String.valueOf(totalHrLab), font5Bold, 0, true, false));
        tbl_stud.addCell(createSimpleCell(String.valueOf(totalHrLec), font5Bold, 0, true, false));
        tbl_stud.addCell(createSimpleCell(String.valueOf(totalUnitsLab), font5Bold, 0, true, false));
        tbl_stud.addCell(createSimpleCell(String.valueOf(totalUnitsLec), font5Bold, 0, true, false));
        tbl_stud.addCell(createSimpleCell(String.valueOf(totalUnitsCre), font5Bold, 0, true, false));
        tbl_stud.addCell(createSimpleCell("", font5Bold, 0, true, false));
        tbl_stud.addCell(createSimpleCell("", font5Bold, 0, true, false));
        tbl_stud.addCell(createSimpleCell(String.valueOf(totalHrsWk), font5Bold, 0, true, false));
        tbl_stud.addCell(createSimpleCell("", font5Bold, 0, true, false));

        return tbl_stud;
    }

    private int sem = 1;

    private void setTitleHeader() {
        switch (year) {
            case 1:
                this.yearLevel = "FIRST YEAR";
                if (sem == 1) {
                    this.semester = "First Semester";
                } else if (sem == 2) {
                    this.semester = "Second Semester";
                }
                objectKey = 1 + String.valueOf(sem);
                break;
            case 2:
                this.yearLevel = "SECOND YEAR";
                if (sem == 1) {
                    this.semester = "First Semester";
                } else if (sem == 2) {
                    this.semester = "Second Semester";
                }
                objectKey = 2 + String.valueOf(sem);
                break;
            default:
                break;
        }
        //change sem
        if (sem == 1) {
            sem = 2;
        } else {
            sem = 1;
        }
    }

    private PdfPTable createOverallTable() throws DocumentException {
        PdfPTable tbl_stud = new PdfPTable(9);
        tbl_stud.setWidths(new float[]{5, 1, 1, 1, 1, 2, 2, 2, 2});
        tbl_stud.setTotalWidth(500);
        tbl_stud.setLockedWidth(true);
        tbl_stud.setSpacingAfter(10f);
        /**
         * HEADER
         */
        tbl_stud.addCell(createCellWithObject(new Chunk("(Upon completion of the requirements of the two-year course"
                + ", a Certificate in Two-Year Associate in Computer Technology will be granted.)", font5BoldItalic),
                false, false, 11));
        tbl_stud.addCell(createSimpleCell("Associate in Computer Technology", font5BoldItalic, 0, false, true));
        tbl_stud.addCell(createSimpleCell("HOUR\nLAB", font5Bold, 0, true, true));
        tbl_stud.addCell(createSimpleCell("HOUR\nLEC", font5Bold, 0, true, true));
        tbl_stud.addCell(createSimpleCell("UNITS\nLAB", font5Bold, 0, true, true));
        tbl_stud.addCell(createSimpleCell("UNITS\nLEC", font5Bold, 0, true, true));
        tbl_stud.addCell(createSimpleCell("TOTAL UNITS\nCREDIT", font5Bold, 0, true, true));
        tbl_stud.addCell(createSimpleCell("Pre-REQ.", font5Bold, 0, true, true));
        tbl_stud.addCell(createSimpleCell("Co-REQ.", font5Bold, 0, true, true));
        tbl_stud.addCell(createSimpleCell("TOTAL\nHRS", font5Bold, 0, true, true));
        /**
         * DATA
         */
        tbl_stud.addCell(createSimpleCell("Overall Total", font5Bold, 0, false, false));
        ALIGNMENT = Element.ALIGN_RIGHT;
        tbl_stud.addCell(createSimpleCell(String.valueOf(totalHrLabOverall), font5Bold, 0, false, false));
        ALIGNMENT = Element.ALIGN_RIGHT;
        tbl_stud.addCell(createSimpleCell(String.valueOf(totalHrLecOverall), font5Bold, 0, false, false));
        ALIGNMENT = Element.ALIGN_RIGHT;
        tbl_stud.addCell(createSimpleCell(String.valueOf(totalUnitsLabOverall), font5Bold, 0, false, false));
        ALIGNMENT = Element.ALIGN_RIGHT;
        tbl_stud.addCell(createSimpleCell(String.valueOf(totalUnitsLecOverall), font5Bold, 0, false, false));
        ALIGNMENT = Element.ALIGN_RIGHT;
        tbl_stud.addCell(createSimpleCell(String.valueOf(totalUnitsCreOverall), font5Bold, 0, false, false));
        tbl_stud.addCell(createSimpleCell("", font5Bold, 0, false, false));
        tbl_stud.addCell(createSimpleCell("", font5Bold, 0, false, false));
        ALIGNMENT = Element.ALIGN_RIGHT;
        tbl_stud.addCell(createSimpleCell(String.valueOf(totalHrsWkOverall), font5Bold, 0, false, false));
        return tbl_stud;
    }

    private static PdfPCell createCellWithPhrase(Phrase phr, Font font, boolean border, boolean center) {
        PdfPCell cell = new PdfPCell();
        if (!border) {
            cell.setBorder(PdfPCell.NO_BORDER);
        }
        if (center) {
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        }
        cell.addElement(phr);
        return cell;
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

    private static PdfPCell createCellWithObject(Object tbl, boolean border, boolean center, int colspan) {
        PdfPCell cell = new PdfPCell();
        if (!border) {
            cell.setBorder(PdfPCell.NO_BORDER);
        }
        if (center) {
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        }
        cell.addElement((Element) tbl);
        if (colspan != 0) {
            cell.setColspan(colspan);
        }
        return cell;
    }

    private Integer ALIGNMENT;

    private PdfPCell createSimpleCell(String content, Font font, int colspan, boolean center, boolean colored) {
        PdfPCell cell = new PdfPCell();
        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Paragraph p = new Paragraph(5);
        int align = Element.ALIGN_CENTER;
        if (ALIGNMENT != null) {
            align = ALIGNMENT;
            ALIGNMENT = null;
        }
        if (center) {
            p.setAlignment(align);
        }
        if (colored) {
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        }
        p.add(new Chunk(content, font));
        cell.addElement(p);
        if (colspan != 0) {
            cell.setColspan(colspan);
        }
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

class MyFooter1 extends PdfPageEventHelper {

    private static final Font font_footer = new Font(FontFamily.HELVETICA, 6);

    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        Phrase footer = new Phrase("BulSU-OP-CICT-03F2", font_footer);
        Phrase footer2 = new Phrase("Revision: 0", font_footer);

        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                footer,
                55, //x
                document.bottom() - 20, //y
                0);
        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                footer2,
                55,
                (document.bottom() - 30),
                0);
    }
}

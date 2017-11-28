package org.cict.reports.checklist;

import app.lazy.models.SubjectMapping;
import artifacts.ResourceManager;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import java.io.FileOutputStream;
import java.io.IOException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
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
import org.cict.reports.ReportsUtility;

public class BSIT1516 {

//    public static String document;
    public static String RESULT;

    private static Boolean asStandardView;

    public BSIT1516(String res, Boolean asStandard) {
//        document = filename;
        RESULT = res;//"reports/checklist/" + document + ".pdf";
        asStandardView = asStandard;
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
            STUDENT_ADDRESS = "8-152 SUCAD APALIT, PAMPANGA",
            STUDENT_NUMBER = "09123456789",
            COURSE = "BACHELOR OF SCIENCE IN INFORMATION TECHNOLOGY",
            SCHOOL_YEAR = "2015-2016",
            MAJOR = "";
    public Integer STUDY_YEARS = 4;

    //--------------------
    private Document documentFormat;

    public void setDocumentFormat(Document documentFormat) {
        this.documentFormat = documentFormat;
    }
    //

    public Boolean IS_LADDERIZED = true, PRINT_ORIGINAL = false;
    public HashMap<String, ArrayList<Object[]>> SUBJECTS_PER_SEM = new HashMap<String, ArrayList<Object[]>>();

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
    private String name,
            address,
            studentNo;
    private static String course, sy, major;
    private static Boolean isLadderized, print_ORIGINAL;
    private HashMap<String, ArrayList<Object[]>> subjectsPerSem = new HashMap<String, ArrayList<Object[]>>();

    public void init() {
        this.studentNo = this.STUDENT_NUMBER;
        this.name = this.STUDENT_NAME;
        this.address = this.STUDENT_ADDRESS;
        this.subjectsPerSem = this.SUBJECTS_PER_SEM;
        course = COURSE;
        //----------------------------------------------------------------------
        // Useless Variable
        isLadderized = IS_LADDERIZED;
        //----------------------------------------------------------------------
        sy = SCHOOL_YEAR;
        major = MAJOR;
        print_ORIGINAL = PRINT_ORIGINAL;
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
        Document document = documentFormat == null ? ReportsUtility.createLongDocument() : documentFormat;
        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        } catch (FileNotFoundException es) {
            return 1;
        }
        document.open();
//        String location_logo1 = ReportsDirectory.REPORTS_DIR_IMAGES + "checklist/BULSU.png",
//        location_logo2 = ReportsDirectory.REPORTS_DIR_IMAGES + "checklist/CICT.png";
//        Image img = Image.getInstance(ResourceManager.fetchFromResource(BSIT1516.class, location_logo1));
//        img.setAbsolutePosition(100, 825); //position
//        img.scaleAbsolute(70, 70); //size
//        document.add(img);
//        Image img2 = Image.getInstance(ResourceManager.fetchFromResource(BSIT1112.class, location_logo2));
//        img2.setAbsolutePosition(445, 825); //position
//        img2.scaleAbsolute(70, 70); //size
//        document.add(img2);
//        document.add(createHeader());

        Paragraph p = new Paragraph(10);
        p.setAlignment(Element.ALIGN_CENTER);

        if (!asStandardView || print_ORIGINAL) {
            ReportsUtility.createHeader(document, null, null, null);
            p.add(new Chunk("A ladderized curriculum leading to the degree of\n", font6Plain));
            p.add(getTextUnderlined(course + "\n", font7Bold));
            if (!major.isEmpty()) {
                p.add(getTextUnderlined("MAJOR IN " + major + "\n", font7Bold));
            }
            p.add(new Chunk("AY " + sy + "\n\n", font6Plain));
        } else {
            if (!major.isEmpty()) {
                ReportsUtility.createHeader(document, "AY " + sy, course, "MAJOR IN " + major);
            } else {
                ReportsUtility.createHeader(document, course, "CHECKLIST IN", "AY " + sy);
            }
        }
        document.add(createStudentInfo());
//        document.add(createBody());
//        document.add(createTitle());
        document.add(createCurriculumTable(0, 1));
        document.add(createCurriculumTable(1, 2));
        //----------------------------------------------------------------------
        // @bugLocation: org.cict.reports.checklist.BSIT1516.java
        // @bugFound: 11/27/2017 10:23 PM
        // @bugInfo: Cannot print checklist return null pointer error
        // @bugInfo: Using curriculum act rev2 no prep yet
        //----------------------------------------------------------------------
        // @startBugSegment
        // @tempSolution: Wrap inside try catch statement
//        try {
        if(STUDY_YEARS.equals(4)) {
            document.add(createCurriculumTable(2, 3));
            document.add(createCurriculumTable(3, 4));
        }
//        } catch (Exception e) {
//            System.err.println("@bug: org.cict.reports.checklist.BSIT1516.java");
//            e.printStackTrace();
//        }
        // @endBugSegment
        //----------------------------------------------------------------------
        writer.setPageEvent(new MyFooter3());
        document.close();
        return 0;
    }

    private static Paragraph createHeader() throws DocumentException {
        Paragraph header = new Paragraph(15);
        header.setAlignment(Element.ALIGN_CENTER);
        header.add(new Chunk("Republic of the Philippines\n", font13Plain));
        header.add(new Chunk("Bulacan State University\n", font17Bold));
        header.add(new Chunk("City of Malolos, Bulacan\n", font13Plain));
        header.add(new Chunk("Tel/Fax (044) 9197800 local 1101\n\n", font9Plain));
        header.add(new Chunk("COLLEGE OF INFORMATION AND COMMUNICATIONS TECHNOLOGY\n", font19Plain));
        header.add(new Chunk("_____________________________________________________\n\n", font17Bold));
        header.add(createTitle());
        return header;
    }

    private static Paragraph createTitle() {
        Paragraph p = new Paragraph(10);
        p.setAlignment(Element.ALIGN_CENTER);

        if (!asStandardView || print_ORIGINAL) {
            p.add(new Chunk("A ladderized curriculum leading to the degree of\n", font6Plain));
            p.add(getTextUnderlined(course + "\n", font7Bold));
            if (!major.isEmpty()) {
                p.add(getTextUnderlined("MAJOR IN " + major + "\n", font7Bold));
            }
            p.add(new Chunk("AY " + sy + "\n\n", font6Plain));
        }
        return p;
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
        tbl_stud.addCell(createCellWithObject(getTitleContent("NAME: ", font8Plain, getShortenedDetail(this.name, 40), font8Plain, "", true), false, true));
        tbl_stud.addCell(createCellWithObject(getTitleContent("STUDENT #: ", font8Plain, getShortenedDetail(this.studentNo, 47), font8Plain, "", true), false, true));
        boolean u = true;
        if (address == null || address.isEmpty()) {
            u = false;
            address = "______________________________________";
        }
        tbl_stud.addCell(createCellWithObject(getTitleContent("ADDRESS: ", font8Plain, getShortenedDetail(this.address, 39), font8Plain, "", u), false, false));
        tbl_stud.addCell(createCellWithObject(new Chunk(""), false, true));

        return tbl_stud;
    }

//    private PdfPTable createBody() throws DocumentException {
//        PdfPTable tbl_stud = new PdfPTable(1);
//        tbl_stud.setPaddingTop(10f);
//        tbl_stud.setTotalWidth(575);
//        tbl_stud.setLockedWidth(true);
//        try {
//            tbl_stud.addCell(createCellWithObject(createCurriculumTable(0,2),false,true));
//            tbl_stud.addCell(createCellWithObject(createCurriculumTable(2,4),false,true));
//        } catch (Exception e) {
//        }
//        return tbl_stud;
//    }
    private String objectKey, yearLevel, semester;
    private int year = 1;
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
                ArrayList<Object[]> subjects = this.subjectsPerSem.get(objectKey);
                //0-subjectmap
                //1-total hrs
                //2-prereq
                //3-co-req
                if (subjects==null || subjects.isEmpty()) {
                    continue;
//                    return null;
                }
                tbl_stud.addCell(createSimpleCell(semester, font5Bold, 11, true, false));
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
                    tbl_stud.addCell(createSimpleCell(subject.getDescriptive_title(), font5Plain, 0, false, false));

                    Double totalUnits = 0.0;
                    if (subject.getType().equalsIgnoreCase(SubjectClassification.TYPE_NSTP)) {
                        tbl_stud.addCell(createSimpleCell("", font5Plain, 0, true, false));
                        tbl_stud.addCell(createSimpleCell("", font5Plain, 0, true, false));
                        tbl_stud.addCell(createSimpleCell("(3.0)", font5Plain, 0, true, false));
                    } else {
                        tbl_stud.addCell(createSimpleCell(subject.getLec_units() + "", font5Plain, 0, true, false));
                        tbl_stud.addCell(createSimpleCell(subject.getLab_units() + "", font5Plain, 0, true, false));
                        totalUnits = (subject.getLec_units() + subject.getLab_units());
                        tbl_stud.addCell(createSimpleCell(totalUnits + "", font5Plain, 0, true, false));
                    }

                    tbl_stud.addCell(createSimpleCell(subjects.get(j)[1].toString(), font5Plain, 0, true, false));
                    tbl_stud.addCell(createSimpleCell(subjects.get(j)[2].toString(), font5Plain, 0, true, false));
                    tbl_stud.addCell(createSimpleCell(subjects.get(j)[3].toString(), font5Plain, 0, true, false));

                    try {
                        totalUnitsLec += subject.getLec_units();
                        totalUnitsLab += subject.getLab_units();
                        totalUnitsCre += totalUnits;
                        totalHrsWk += Integer.valueOf(subjects.get(j)[1].toString());
                    } catch (NumberFormatException a) {
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
                String totalHrsWkStr = "";
                if (totalHrsWk != 0) {
                    totalHrsWkStr = String.valueOf(totalHrsWk);
                }
                tbl_stud.addCell(createSimpleCell(totalHrsWkStr, font5Bold, 0, true, false));
                tbl_stud.addCell(createSimpleCell("", font5Bold, 0, true, false));
                tbl_stud.addCell(createSimpleCell("", font5Bold, 0, true, false));

                if (year == 2 && sem == 2 && print_ORIGINAL) {
                    value = 10;
                    tbl_stud.addCell(createSimpleCell("At the end of Second Year (All comprising subjects are taken and passed), "
                            + "the student earns a 2 - year Certificate if Associate in\nComputer Technology (ACT)", font7Bold, 8, true, false));
                }

                //change sem
                if (sem == 1) {
                    sem = 2;
                } else {
                    sem = 1;
                }
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
            case 3:
                this.yearLevel = "THIRD YEAR";
                if (sem == 1) {
                    this.semester = "First Semester";
                } else if (sem == 2) {
                    this.semester = "Second Semester";
                }
                objectKey = 3 + String.valueOf(sem);
                break;
            case 4:
                this.yearLevel = "FOURTH YEAR";
                if (sem == 1) {
                    this.semester = "First Semester";
                } else if (sem == 2) {
                    this.semester = "Second Semester";
                }
                objectKey = 4 + String.valueOf(sem);
                break;
            default:
                break;
        }
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

    private Integer value;

    private PdfPCell createSimpleCell(String content, Font font, int colspan, boolean center, boolean colored) {
        PdfPCell cell = new PdfPCell();
        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Integer a = 5;
        if (value != null) {
            a = value;
        }
        Paragraph p = new Paragraph(a);
        value = null;
        if (center) {
            p.setAlignment(Element.ALIGN_CENTER);
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

    private static Chunk getTextUnderlined(String text, Font font2) {
        Chunk chunks = new Chunk(text, font2);
        chunks.setUnderline(0.1f, -2f);
        return chunks;
    }

    private static String getShortenedDetail(String str, int count) {
        if (str.length() > count) {
            return str.substring(0, count) + "...";
        } else {
            return str;
        }
    }
}

class MyFooter3 extends PdfPageEventHelper {

    private static final Font font_footer = new Font(FontFamily.HELVETICA, 8);

    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        Phrase footer = new Phrase("BulSU-OP-CICT-03F2", font_footer);
        Phrase footer2 = new Phrase("Revision: 0", font_footer);

        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                footer,
                30, //x
                document.bottom() - 0, //y
                0);
        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                footer2,
                30,
                (document.bottom() - 10),
                0);
    }
}

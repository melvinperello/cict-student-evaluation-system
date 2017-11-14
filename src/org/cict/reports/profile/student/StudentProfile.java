package org.cict.reports.profile.student;

import artifacts.ResourceManager;
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
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.jhmvin.Mono;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Collections;
import javafx.application.Platform;
import org.cict.reports.ReportsDirectory;

public class StudentProfile {

    public static String RESULT;

    public StudentProfile(String filename) {
        RESULT = filename;
    }

    //--------------------------------------------------------------------------
    // Downloading Profile Picture From Server
    // STEP 1 - Create Default Image Path if no image was loaded or assigned to a student.
    public final static String DEFAULT_IMAGE = ResourceManager.fetchFromResource(StudentProfile.class, ReportsDirectory.DEFAULT_IMAGE1x1).toString(); //"profile/student/images/default-1x1.png";
    // STEP 2 - Create a class variable for the image path
    // STEP 2.1 - For assurance assign default values
    // event when setter is not called there will be a default value
    private String profileImage = DEFAULT_IMAGE;

    // STEP 3 - Create a setter for the image
    public void setProfileImage(String profileImage) {
        // STEP 4 - Check value if null load the default Image
        this.profileImage = profileImage == null ? DEFAULT_IMAGE : profileImage;
        // STEP 5 - Create a image downloader from where this report was called.
        // for this instance this report was create at PrintStudentProfile.java
        // see this class for more information on how this setter was used.
    }

    //--------------------------------------------------------------------------
    public void debugger(Object message) {
        Platform.runLater(() -> {
            Mono.fx().alert().createWarning().setTitle("DEBUGGER")
                    .setHeader("DEBUGGING MESSAGE")
                    .setMessage(String.valueOf(message))
                    .show();
        });
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
    public String SEMESTER = "1st",
            SCHOOL_YEAR = "2017-2018",
            STUDENT_NAME = "DE LA CRUZ, JOEMAR NUCOM",
            STUDENT_ADDRESS = "8-152 SUCAD APALIT, PAMPANGA",
            STUDENT_CONTACT_NO = "09123456789",
            STUDENT_EMAIL_ADD = "joemardc98@gmail.com",
            GUARDIAN_NAME = "MARILYN D. DE JESUS",
            GUARDIAN_CONTACT_NO = "09123456789",
            GUARDIAN_ADDRESS = "SUCAD APALIT, PAMPANGA";
    /**
     * FONTS
     */
    private static final Font font_header1 = new Font(FontFamily.HELVETICA, 9);
    private static final Font font_header2 = new Font(FontFamily.HELVETICA, 8, Font.BOLD);
    private static final Font font_header3 = new Font(FontFamily.HELVETICA, 8);
    private static final Font font_footer = new Font(FontFamily.HELVETICA, 9);
    private static final Font font_title = new Font(FontFamily.HELVETICA, 11, Font.BOLD);
    private static final Font font_title2 = new Font(FontFamily.HELVETICA, 11, Font.UNDERLINE);
    private static final Font font_title3 = new Font(FontFamily.HELVETICA, 11);
    private static final Font font_body = new Font(FontFamily.HELVETICA, 10, Font.BOLD);
    private static final Font font_body2 = new Font(FontFamily.HELVETICA, 10);
    /**
     * PRIVATE VARIABLES
     */
    private String semester,
            schoolYear,
            name,
            address,
            contactNo,
            emailAdd,
            guardianName,
            guardianContacNo,
            guardianAddress;

    public void init() {
        this.semester = this.SEMESTER;
        this.schoolYear = this.SCHOOL_YEAR;
        this.name = this.STUDENT_NAME;
        this.address = this.STUDENT_ADDRESS;
        this.contactNo = this.STUDENT_CONTACT_NO;
        this.emailAdd = this.STUDENT_EMAIL_ADD;
        this.guardianName = this.GUARDIAN_NAME;
        this.guardianContacNo = this.GUARDIAN_CONTACT_NO;
        this.guardianAddress = this.GUARDIAN_ADDRESS;
    }

    private static PdfWriter writer;

    /**
     * Creates a PDF document.
     *
     * @param filename the path to the new PDF document
     * @throws DocumentException
     * @throws IOException
     */
    public int createPdf(String filename) throws DocumentException, IOException {
        Document document = new Document(new Rectangle(Utilities.inchesToPoints(8.27f),
                Utilities.inchesToPoints(5.845f)), 100, 100, 30, 50); //lrtb
        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        } catch (FileNotFoundException es) {
            debugger("File not found exception: " + es.getMessage());
            return 1;
        }
        document.open();

        //----------------------------------------------------------------------
       
        //----------------------------------------------------------------------
        // add bulsu logo
        Image img = Image.getInstance(ResourceManager.fetchFromResource(StudentProfile.class, ReportsDirectory.REPORTS_DIR_IMAGES + "checklist/BULSU.png"));
        img.setAbsolutePosition(85, 335); //position
        img.scaleAbsolute(52, 52); //size
        document.add(img);
        // add cict logo
        Image img2 = Image.getInstance(ResourceManager.fetchFromResource(StudentProfile.class, ReportsDirectory.REPORTS_DIR_IMAGES + "checklist/CICT.png"));
        img2.setAbsolutePosition(465, 335); //position
        img2.scaleAbsolute(52, 52); //size
        document.add(img2);

        document.add(createHeader());
        String standard = this.profileImage;
        Image image1X1 = Image.getInstance(standard);
        image1X1.setAbsolutePosition(430f, 245f); //position
        image1X1.scaleAbsolute(Utilities.inchesToPoints(1), Utilities.inchesToPoints(1)); //size
        document.add(image1X1);
        create1x1Box(writer);
        document.add(createTitle());
        document.add(createBody());
        addText(name, 10, 135, 202);
        addText(address, 10, 147, 172);
        addText(contactNo, 10, 162, 142);
        addText(emailAdd, 10, 357, 142);
        addText(guardianName, 10, 150, 112);
        addText(guardianContacNo, 10, 360, 112);
        addText(guardianAddress, 10, 225, 81);
        writer.setPageEvent(new MyFooter());
        document.close();
        return 0;
    }

    private void addText(String text, float fontSize, float x, float y) throws DocumentException, IOException {
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

    private static Paragraph createHeader() throws DocumentException {
        Paragraph header = new Paragraph(10);
        header.setAlignment(Element.ALIGN_CENTER);
        header.add(new Chunk("Republic of the Philippines\n", font_header1));
        header.add(new Chunk("BULACAN STATE UNIVERSITY\n", font_header1));
        header.add(new Chunk("COLLEGE OF INFORMATION AND COMMUNICATIONS TECHNOLOGY\n", font_header2));
        header.add(new Chunk("City of Malolos, Bulacan\n", font_header1));
        header.add(new Chunk("(044) 919-7800 local 1101-1102\n", font_header1));
        header.add(new Chunk("Level III Re-Accredited by the Accrediting Agency of Chartered Colleges and\n"
                + "University of the Philippines (AACCUP)", font_header3));
        return header;
    }

    private Paragraph createTitle() {
        Paragraph title = new Paragraph(15);
        title.setAlignment(Element.ALIGN_CENTER);
        title.add(new Chunk("\n\nNEW STUDENT PROFILE\n", font_title));
        title.add(new Chunk(semester, font_title2));
        title.add(new Chunk(" Sem. S.Y. ", font_title3));
        title.add(new Chunk(schoolYear, font_title2));
        return title;
    }

    private Paragraph createBody() {
        Paragraph body = new Paragraph(30);
        body.setSpacingAfter(0);
        body.setSpacingBefore(0);
        body.setAlignment(Element.ALIGN_LEFT);

        putTitleContent(body, "\nName: ", font_body, createContentWithSpaceOf("", 130), font_body2, "\n", true);
        putTitleContent(body, "Address: ", font_body, createContentWithSpaceOf("", 125), font_body2, "\n", true);
        putTitleContent(body, "Contact No.: ", font_body, createContentWithSpaceOf("", 40), font_body2, "", true);
        putTitleContent(body, "   Email Address: ", font_body, createContentWithSpaceOf("", 55), font_footer, "\n", true);
        putTitleContent(body, "Guardian: ", font_body, createContentWithSpaceOf("", 50), font_body2, "", true);
        putTitleContent(body, "   Contact No.: ", font_body, createContentWithSpaceOf("", 54), font_footer, "\n", true);
        putTitleContent(body, "Address of the Guardian: ", font_body, createContentWithSpaceOf("", 108), font_footer, "", true);
        return body;
    }

    private static void putTitleContent(Paragraph phr, String title, Font font1, String info, Font font2, String space, boolean underlined) {
        phr.add(new Chunk(title, font1));
        Chunk chunks = new Chunk(info + space, font2);
        if (underlined) {
            chunks.setUnderline(0.1f, -2f);
        }
        phr.add(chunks);
    }

    private static String getShortenedDetail(String str, int count) {
        if (str.length() > count) {
            return str.substring(0, count) + "...";
        } else {
            return str;
        }
    }

    private String createContentWithSpaceOf(String content, int total) {
        if (content.length() < total) {
            int left = total - content.length();
            return content + String.join("", Collections.nCopies(left, " "));
        } else {
            return content;
        }
    }

    private static void create1x1Box(PdfWriter writer) {
        PdfContentByte cb = writer.getDirectContent();
        try {
            cb.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, false), 24);
            cb.rectangle(430f, 245f, Utilities.inchesToPoints(1), Utilities.inchesToPoints(1));
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
        Phrase footer = new Phrase("BulSU-OP-CICT-03F1", font_footer);
        Phrase footer2 = new Phrase("Revision: 0", font_footer);

        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                footer,
                50,
                document.bottom() - 20, 0);
        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                footer2,
                50,
                (document.bottom() - 30), 0);
    }
}

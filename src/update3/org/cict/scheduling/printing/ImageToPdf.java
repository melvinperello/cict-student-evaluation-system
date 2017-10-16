package update3.org.cict.scheduling.printing;

import java.io.FileOutputStream;
import java.io.IOException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import org.cict.reports.ReportsDirectory;

public class ImageToPdf {

    public static String RESULT = "";
    public final static String SAVE_DIRECTORY = "reports/schedule";

    public ImageToPdf(String filename) {
        RESULT = SAVE_DIRECTORY + "/" + filename + ".pdf";
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
        Document document = new Document(PageSize.LEGAL_LANDSCAPE.rotate()/*);new Rectangle(Utilities.inchesToPoints(8.5f),
                Utilities.inchesToPoints(11f))*/, 50, 50, 70, 50); //lrtb
        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        } catch (FileNotFoundException es) {
            return 1;
        }
        document.open();
        Image img = Image.getInstance(location);
        img.setAbsolutePosition(10, 10); //position
        img.scaleAbsolute(990, 600); //size
        document.add(img);
        document.close();
        return 0;
    }
}

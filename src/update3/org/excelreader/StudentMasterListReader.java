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
 * JOEMAR N. DELA CRUZ
 * GRETHEL EINSTEIN BERNARDINO
 *
 * OTHER LIBRARIES THAT ARE USED BELONGS TO THEIR RESPECTFUL OWNERS AND AUTHORS.
 * NO COPYRIGHT ARE INTENTIONAL OR INTENDED.
 * THIS PROJECT IS NOT PROFITABLE HENCE FOR EDUCATIONAL PURPOSES ONLY.
 * THIS PROJECT IS ONLY FOR COMPLIANCE TO OUR REQUIREMENTS.
 * THIS PROJECT DOES NOT INCLUDE DISTRIBUTION FOR OTHER PURPOSES.
 *
 */
package update3.org.excelreader;

import java.io.File;
import java.util.ArrayList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author Joemar
 */
public class StudentMasterListReader {
    
    public static String LOG = "";
    public static ArrayList<ReadData> readStudentGrade(Stage stage, Integer subjectID) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Excel File");
                       
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Excel File", "*.xlsx")
        );
        File file = fileChooser.showOpenDialog(stage);
        if(file == null) {
            LOG = "No file selected.";
            return null;
        }
        ExcelRead reader = new ExcelRead();
        reader.setExcelPath(file.getAbsolutePath());// + "/" + sectionName + " " + subjectCode + " " + CollegeFaculty.instance().getBULSU_ID());
        boolean isLoaded = reader.loadExcel();
        if(!isLoaded) {
            LOG = "Excel file not loaded.";
            return null;
        }
        boolean headersChecked = reader.checkHeader();
        if(!headersChecked) {
            LOG = "Wrong header. You can use the exported file to upload\n"
                    + "or use the following as header format.\n"
                    + "   (Student Number, Full Name, Grade, Cleared)";
            return null;
        }
        ArrayList<ReadData> receivedData =  reader.readData(); // this should return an array list of objects that was read
        if(receivedData==null || receivedData.isEmpty()) {
            LOG = "No data found in the file.";
            return null;
        }
        return receivedData;
    }
}

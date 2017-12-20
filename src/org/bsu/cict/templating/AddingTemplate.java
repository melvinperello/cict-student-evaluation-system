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
package org.bsu.cict.templating;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.bsu.cict.alerts.MessageBox;

/**
 *
 * @author Jhon Melvin
 */
public class AddingTemplate {

    private final static String DATE = "DATE";
    private final static String CHK_ADD = "CHK_ADD";
    private final static String CHK_CHANGE = "CHK_CHANGE";
    private final static String TOTAL_UNITS = "TOTAL_UNITS";
    //-----------------------------------------------------
    // table
    private final static String PREFIX_SUBJECTS = "SUBJECTS ";
    private final static String PREFIX_SECTIONS = "SECTIONS ";
    private final static String PREFIX_UNITS = "NO OF UNITS ";
    // on foot
    private final static String FULL_NAME = "FULL_NAME";
    private final static String COURSE_YEAR_SECTION = "COURSE_YEAR_SECTION";
    private final static String STUDENT_NO = "STUDENT_NO";
    private final static String DEAN = "DEAN";
    private final static String REGISTRAR = "REGISTRAR";
    //--------------------------------------------------------------------------
    // TEMPLATE SOURCE
    private final static String ADDING_ACROFORM = "templates/ADDING_TEMPLATE.pdf";
    //--------------------------------------------------------------------------

    /**
     * Static inner class this class does not depend on the instance of its
     * parent class.
     */
    public static class TableData {

        private String subject;
        private String section;
        private String units;

        public TableData() {
        }

        public TableData(String subject, String section, String units) {
            this.subject = subject;
            this.section = section;
            this.units = units;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public void setSection(String section) {
            this.section = section;
        }

        public void setUnits(String units) {
            this.units = units;
        }

        public String getSubject() {
            return subject;
        }

        public String getSection() {
            return section;
        }

        public String getUnits() {
            return units;
        }

    }

    // head
    private String date;
    private String checkBoxAdd;
    private String checkBoxChange;
    private String totalUnits;
    // body
    private ArrayList<TableData> groupData;
    // foot
    private String fullName;
    private String courseYearSection;
    private String studentNumber;
    private String dean;
    private String registrar;

    public void setDate(String date) {
        this.date = date;
    }

    // default
    private String chkOnValue = "Yes";
    private String chkOffValue = "Off";

    public void setCheckBoxAdd(boolean checkBoxAdd) {
        this.checkBoxAdd = checkBoxAdd ? chkOnValue : chkOffValue;
    }

    public void setCheckBoxChange(boolean checkBoxChange) {
        this.checkBoxChange = checkBoxChange ? chkOnValue : chkOffValue;
    }

    public void setTotalUnits(String totalUnits) {
        this.totalUnits = totalUnits;
    }

    public void setGroupData(ArrayList<TableData> groupData) {
        this.groupData = groupData;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setCourseYearSection(String courseYearSection) {
        this.courseYearSection = courseYearSection;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    /**
     * Recommending Approval.
     *
     * @param dean
     */
    public void setDean(String dean) {
        this.dean = dean;
    }

    public void setRegistrar(String registrar) {
        this.registrar = registrar;
    }

    private String savePath;

    /**
     * Directory where to save.
     *
     * @param savePath
     */
    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    //--------------------------------------------------------------------------
    public void stampTemplate() throws DocumentException, IOException {
        //----------------------------------------------------------------------
        // check existence
        File template = new File(ADDING_ACROFORM);
        if (!template.exists()) {
            MessageBox.showError("No Template", ADDING_ACROFORM + " was not found in the local directory.");
            return;
        }
        //----------------------------------------------------------------------

        PdfReader reader = new PdfReader(ADDING_ACROFORM);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(this.savePath));
        AcroFields form = stamper.getAcroFields();
        //----------------------------------------------------------------------
        // check states
        String[] states = form.getAppearanceStates(CHK_ADD);
        if (states.length == 1) {
            this.chkOnValue = states[0];
        } else if (states.length == 2) {
            this.chkOnValue = states[0];
            this.chkOffValue = states[1];
        }
        //----------------------------------------------------------------------
        // head
        form.setField(DATE, this.date);
        form.setField(CHK_ADD, this.checkBoxAdd);
        form.setField(CHK_CHANGE, this.checkBoxChange);
        form.setField(TOTAL_UNITS, this.totalUnits);
        // body
        int counter = 1;
        for (TableData tableData : groupData) {
            form.setField(PREFIX_SUBJECTS + String.valueOf(counter), tableData.subject);
            form.setField(PREFIX_SECTIONS + String.valueOf(counter), tableData.section);
            form.setField(PREFIX_UNITS + String.valueOf(counter), tableData.units);
            counter++;
            // can only read the first 5 elements.
            if (counter >= 6) {
                break;
            }
        }
        // foot
        form.setField(FULL_NAME, this.fullName);
        form.setField(COURSE_YEAR_SECTION, this.courseYearSection);
        form.setField(STUDENT_NO, this.studentNumber);
        form.setField(DEAN, this.dean);
        form.setField(REGISTRAR, this.registrar);
        //----------------------------------------------------------------------
        stamper.setFormFlattening(true);
        stamper.close();
        reader.close();
    }

    public static void main(String[] args) {
        AddingTemplate a = new AddingTemplate();
        // head
        a.setDate("TODAY");
        a.setCheckBoxAdd(true);
        a.setCheckBoxChange(true);
        a.setTotalUnits("20.0");
        // body
        ArrayList<AddingTemplate.TableData> data = new ArrayList<>();
        data.add(new AddingTemplate.TableData("MATH 113", "BSIT 4H-G2", "3.0"));
        data.add(new AddingTemplate.TableData("MATH 113", "BSIT 4H-G2", "3.0"));
        data.add(new AddingTemplate.TableData("MATH 113", "BSIT 4H-G2", "3.0"));
        data.add(new AddingTemplate.TableData("MATH 113", "BSIT 4H-G2", "3.0"));
        data.add(new AddingTemplate.TableData("MATH 113", "BSIT 4H-G2", "3.0"));
        a.setGroupData(data);
        // foot
        a.setFullName("PERELLO, JHON MELVIN NIETO");
        a.setCourseYearSection("BSIT 4A-G1");
        a.setStudentNumber("2014113844");
        a.setDean("NOEMI P. REYES");
        a.setRegistrar("THE REGISTRAR");
        //
        a.setSavePath("C:\\Users\\Jhon Melvin\\Desktop\\asd.pdf");
        try {
            a.stampTemplate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

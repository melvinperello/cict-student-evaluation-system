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
package org.bsu.cict.dev.exportgrades;

import app.lazy.models.GradeMapping;
import app.lazy.models.StudentMapping;
import com.jhmvin.Mono;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Jhon Melvin
 */
public class GradeExportModule {

    public static final String studentNumber = "studentNumber";
    public static final String lastName = "lastName";
    public static final String firstName = "firstName";
    public static final String middleName = "middleName";
    public static final String curriculumID = "curriculumID";
    public static final String curriculumCode = "curriculumCode";
    public static final String programCode = "programCode";
    //------------------------
    public static final String prepID = "prepID";
    public static final String prepCode = "prepCode";
    public static final String prepProgramCode = "prepProgramCode";
    //------------------------
    public static final String yearLevel = "yearLevel";
    public static final String section = "section";
    public static final String group = "group";

    //----------------------------------------------------------------
    public static final String gradeID = "gradeID";
    public static final String subjectID = "subjectID";
    public static final String subjectCode = "subjectCode";
    public static final String subjectTitle = "subjectTitle";
    //------------------------
    public static final String rating = "rating";
    public static final String remarks = "remarks";
    public static final String credit = "credit";
    public static final String creditMethod = "creditMethod";
    public static final String posted = "posted";
    public static final String incExpireDate = "incExpireDate";
    public static final String description = "description";
    //------------------------
    public static final String createdDate = "createdDate";
    public static final String state = "state";
    public static final String active = "active";
    //--------------------------------------------------------------------------

    /**
     * XML Document.
     */
    private Document doc;

    private ArrayList<ExportGrade> exportGrade;
    private ExportStudent exportStudent;
    private String savePath;

    public void setExportGrade(ArrayList<ExportGrade> exportGrade) {
        this.exportGrade = exportGrade;
    }

    public void setExportStudent(ExportStudent exportStudent) {
        this.exportStudent = exportStudent;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    /**
     * Create XML Document.
     *
     * @throws ParserConfigurationException
     */
    private void createXMLDocument() throws ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        this.doc = docBuilder.newDocument();
        this.doc.setXmlVersion("1.0");
        this.doc.setXmlStandalone(true);
    }

    /**
     * Add attribute to an element.
     *
     * @param element
     * @param key
     * @param value
     */
    public void addAttribute(Element element, String key, String value) {
        // set attribute to staff element
        Attr attr = this.doc.createAttribute(key);
        attr.setValue(value);
        element.setAttributeNode(attr);
    }

    public void addContents() throws Exception {
        //----------------------------------------------------------------------
        // root element
        Element rootElement = doc.createElement("students");
        doc.appendChild(rootElement);
        //----------------------------------------------------------------------
        // root child
        Element childElement = doc.createElement("student");
        this.addAttribute(childElement, studentNumber, exportStudent.getStudentNumber());
        this.addAttribute(childElement, lastName, exportStudent.getLastName());
        this.addAttribute(childElement, firstName, exportStudent.getFirstName());
        this.addAttribute(childElement, middleName, exportStudent.getMiddleName());
        this.addAttribute(childElement, curriculumID, exportStudent.getCurriculumID());
        this.addAttribute(childElement, curriculumCode, exportStudent.getCurriculumCode());
        this.addAttribute(childElement, programCode, exportStudent.getProgramCode());
        //
        this.addAttribute(childElement, prepID, exportStudent.getPrepID());
        this.addAttribute(childElement, prepCode, exportStudent.getPrepCode());
        this.addAttribute(childElement, prepProgramCode, exportStudent.getPrepProgramCode());
        //
        this.addAttribute(childElement, yearLevel, exportStudent.getYearLevel());
        this.addAttribute(childElement, section, exportStudent.getSection());
        this.addAttribute(childElement, group, exportStudent.getGroup());
        // append to root
        rootElement.appendChild(childElement);
        //----------------------------------------------------------------------
        // insert grade entries.
        for (ExportGrade export : this.exportGrade) {
            // addContents grade elements
            Element grade = doc.createElement("grade");
            // this field is not usable
            this.addAttribute(grade, gradeID, export.getGradeID());

            this.addAttribute(grade, subjectID, export.getSubjectID());
            //------------------------------------------------------------------
            this.addAttribute(grade, subjectCode, export.getSubjectCode());
            this.addAttribute(grade, subjectTitle, export.getSubjectTitle());
            //------------------------------------------------------------------
            this.addAttribute(grade, rating, export.getRating());
            this.addAttribute(grade, remarks, export.getRemarks());
            this.addAttribute(grade, credit, export.getCredit());
            this.addAttribute(grade, creditMethod, export.getCreditMethod());
            this.addAttribute(grade, posted, export.getPosted());
            this.addAttribute(grade, incExpireDate, export.getIncExpireDate());
            this.addAttribute(grade, description, export.getDescription());
            //------------------------------------------------------------------
            this.addAttribute(grade, createdDate, export.getCreatedDate());
            this.addAttribute(grade, state, export.getState());
            this.addAttribute(grade, active, export.getActive());
            //------------------------------------------------------------------
            // add to student
            childElement.appendChild(grade);
            //------------------------------------------------------------------
        }

    }

    /**
     * Save XML Document to file.
     *
     * @throws TransformerException
     */
    private void saveXMLDocument() throws TransformerException {
        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(this.savePath));
        //----------------------------------------------------------------------
        // pretty format
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
        //----------------------------------------------------------------------
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        //----------------------------------------------------------------------
        // doctype
//        DOMImplementation domImpl = this.doc.getImplementation();
//        DocumentType doctype = domImpl.createDocumentType("hibernate-mapping",
//                "-//Hibernate/Hibernate Mapping DTD 3.0//EN",
//                "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd");
//        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
//        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
        //----------------------------------------------------------------------
        transformer.transform(source, result);
    }

    /**
     * Create the XML file.
     */
    public /*private*/ void create() {
        try {
            this.createXMLDocument();
            this.addContents();
            this.saveXMLDocument();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // fill up the following
        ExportStudent student = new ExportStudent();

        // and arraylist of all the grades of that student
        ArrayList<ExportGrade> grade = new ArrayList<>();

        GradeExportModule eg = new GradeExportModule();
        eg.setSavePath("C:\\Users\\Jhon Melvin\\Desktop\\file.xml");
        eg.setExportStudent(student);
        eg.setExportGrade(grade);
        eg.create();
    }
}

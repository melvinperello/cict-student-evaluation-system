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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Jhon Melvin
 */
public class ImportGradeModule {

    /**
     * XML Document.
     */
    private Document doc;

    private String documentPath;

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    /**
     * Open and reads the XML Document.
     *
     * @throws ParserConfigurationException
     */
    public void readXMLDocument() throws ParserConfigurationException, SAXException, IOException {
        File xmlFile = new File(this.documentPath);
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        this.doc = docBuilder.parse(xmlFile);
        //optional, but recommended
        //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
        this.doc.getDocumentElement().normalize();
    }

    private ExportStudent importedStudentData;
    private ArrayList<ExportGrade> importedGradeData;

    public ExportStudent getImportedStudentData() {
        return importedStudentData;
    }

    public ArrayList<ExportGrade> getImportedGradeData() {
        return importedGradeData;
    }

    public void readContents() {
        // get the root node
        NodeList rootNode = this.doc.getElementsByTagName("students");
        if (rootNode.getLength() == 0) {
            return;
        }
        // elements from the root node
        ArrayList<Element> students = getChildElements(rootNode);
        if (students.isEmpty()) {
            // no elements
            return;
        }
        // get the student nodes
        ArrayList<Element> student = getChildElements(students.get(0).getChildNodes());
        // no student element
        if (student.isEmpty()) {
            return;
        }
        // now the student element
        Element studentElement = student.get(0);
        // get the student information
        ExportStudent importStudentData = new ExportStudent();
        importStudentData.setStudentNumber(studentElement.getAttribute(GradeExportModule.studentNumber));
        importStudentData.setLastName(studentElement.getAttribute(GradeExportModule.lastName));
        importStudentData.setFirstName(studentElement.getAttribute(GradeExportModule.firstName));
        importStudentData.setMiddleName(studentElement.getAttribute(GradeExportModule.middleName));
        importStudentData.setCurriculumID(studentElement.getAttribute(GradeExportModule.curriculumID));
        importStudentData.setCurriculumCode(studentElement.getAttribute(GradeExportModule.curriculumCode));
        importStudentData.setProgramCode(studentElement.getAttribute(GradeExportModule.programCode));
        //
        importStudentData.setPrepID(studentElement.getAttribute(GradeExportModule.prepID));
        importStudentData.setPrepCode(studentElement.getAttribute(GradeExportModule.prepCode));
        importStudentData.setPrepProgramCode(studentElement.getAttribute(GradeExportModule.prepProgramCode));
        //
        importStudentData.setYearLevel(studentElement.getAttribute(GradeExportModule.yearLevel));
        importStudentData.setSection(studentElement.getAttribute(GradeExportModule.section));
        importStudentData.setGroup(studentElement.getAttribute(GradeExportModule.group));
        //----------------------------------------------------------------------

        ArrayList<Element> grades = getChildElements(studentElement.getChildNodes());
        if (grades.isEmpty()) {
            // no grades
            return;
        }
        //
        ArrayList<ExportGrade> gradeData = new ArrayList<>();
        for (Element grade : grades) {
            ExportGrade gradeInfo = new ExportGrade();
            gradeInfo.setGradeID(grade.getAttribute(GradeExportModule.gradeID));
            gradeInfo.setSubjectID(grade.getAttribute(GradeExportModule.subjectID));
            gradeInfo.setSubjectCode(grade.getAttribute(GradeExportModule.subjectCode));
            gradeInfo.setSubjectTitle(grade.getAttribute(GradeExportModule.subjectTitle));
            //------------------------
            gradeInfo.setRating(grade.getAttribute(GradeExportModule.rating));
            gradeInfo.setRemarks(grade.getAttribute(GradeExportModule.remarks));
            gradeInfo.setCredit(grade.getAttribute(GradeExportModule.credit));
            gradeInfo.setCreditMethod(grade.getAttribute(GradeExportModule.creditMethod));
            gradeInfo.setPosted(grade.getAttribute(GradeExportModule.posted));
            gradeInfo.setIncExpireDate(grade.getAttribute(GradeExportModule.incExpireDate));
            gradeInfo.setDescription(grade.getAttribute(GradeExportModule.description));
            gradeInfo.setCreatedDate(grade.getAttribute(GradeExportModule.createdDate));
            gradeInfo.setState(grade.getAttribute(GradeExportModule.state));
            gradeInfo.setActive(grade.getAttribute(GradeExportModule.active));
            // add to list
            gradeData.add(gradeInfo);
        }

        //----------------------------------------------------------------------
        this.importedStudentData = importStudentData;
        this.importedGradeData = gradeData;
        //----------------------------------------------------------------------

    }

    /**
     * get elements from a node list.
     *
     * @param nodeList
     * @return
     */
    private ArrayList<Element> getChildElements(NodeList nodeList) {
        ArrayList<Element> elements = new ArrayList<>();

        for (int x = 0; x < nodeList.getLength(); x++) {
            Node node = nodeList.item(x);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                elements.add(element);
            }
        }
        return elements;
    }

    public static void main(String[] args) {
        ImportGradeModule a = new ImportGradeModule();
        a.setDocumentPath("C:\\Users\\Jhon Melvin\\Desktop\\3A_G2_2015166650_MORALES.xml");
        try {
            a.readXMLDocument();
            a.readContents();
            // after reading you can now fetch vaues
            ExportStudent student = a.getImportedStudentData();
            ArrayList<ExportGrade> grades = a.getImportedGradeData();
            for (ExportGrade grade : grades) {
                System.out.println(grade.getSubjectCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

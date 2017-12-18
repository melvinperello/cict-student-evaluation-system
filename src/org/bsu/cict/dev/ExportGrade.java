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
package org.bsu.cict.dev;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
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
public class ExportGrade {

    private Document doc;

    public void addAttribute(Element element, String key, String value) {
        // set attribute to staff element
        Attr attr = this.doc.createAttribute(key);
        attr.setValue(value);
        element.setAttributeNode(attr);
    }

    public void create() throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        //----------------------------------------------------------------------
        // create root element student
        this.doc = docBuilder.newDocument();
//        this.doc.setXmlVersion("1.0");
//        this.doc.setXmlStandalone(true);
        Element rootElement = doc.createElement("students");
        doc.appendChild(rootElement);

        Element childElement = doc.createElement("student");
        this.addAttribute(childElement, "id", "2014113844");
        this.addAttribute(childElement, "last_name", "PERELLO");
        this.addAttribute(childElement, "first_name", "JHON MELVIN");
        this.addAttribute(childElement, "middle_name", "NIETO");
        this.addAttribute(childElement, "curriculum_id", "11");
        this.addAttribute(childElement, "curriculum", "BSIT 15-16");
        this.addAttribute(childElement, "year", "4");
        this.addAttribute(childElement, "section", "A");
        this.addAttribute(childElement, "group", "1");
        rootElement.appendChild(childElement);
        //----------------------------------------------------------------------
        for (int x = 0; x < 100; x++) {
            // create grade elements
            Element grade = doc.createElement("grade");
            this.addAttribute(grade, "id", "152");
            //------------------------------------------------------------------
            this.addAttribute(grade, "code", "MATH 123");
            this.addAttribute(grade, "title", "PLANE TRIGONOMETRY");
            //------------------------------------------------------------------
            this.addAttribute(grade, "rating", "1.00");
            this.addAttribute(grade, "remarks", "PASSED");
            this.addAttribute(grade, "credit", "3");
            this.addAttribute(grade, "credit_method", "REGULAR");
            this.addAttribute(grade, "posted", "1");
            this.addAttribute(grade, "inc_expire", "2017-12-13 11:50:26.000000");
            this.addAttribute(grade, "description", "ADDED USING CREDIT TREE");
            //------------------------------------------------------------------
            this.addAttribute(grade, "state", "ACCEPTED");
            this.addAttribute(grade, "active", "1");
            //------------------------------------------------------------------
            // add to student
            childElement.appendChild(grade);
            //------------------------------------------------------------------
        }
        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File("C:\\Users\\Jhon Melvin\\Desktop\\file.xml"));
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.transform(source, result);
    }

    public static void main(String[] args) {
        ExportGrade eg = new ExportGrade();
        try {
            eg.create();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

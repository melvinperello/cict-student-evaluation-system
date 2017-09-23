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
package org.cict.evaluation.student.history;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.text.SimpleDateFormat;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Joemar
 */
public class History extends RecursiveTreeObject<History> {
    public StringProperty schoolYear;
    public StringProperty year;
    public StringProperty semester;
    public StringProperty evaluationDate;
    public StringProperty type;
    public StringProperty remarks;
    public StringProperty evaluatedBy;
    public StringProperty canceledBy;
    public StringProperty canceledDate;
    
    public History(String school_year, String year, String semester, String type,
            String evaluated_by, String evaluation_date,
            String canceled_by, String canceled_date, String remarks) {
        this.schoolYear = new SimpleStringProperty(school_year);
        this.year = new SimpleStringProperty(year);
        this.semester = new SimpleStringProperty(semester);
        this.type = new SimpleStringProperty(type);
        this.evaluatedBy = new SimpleStringProperty(evaluated_by);
        this.evaluationDate = new SimpleStringProperty(evaluation_date);
        this.canceledBy = new SimpleStringProperty(canceled_by);
        this.canceledDate = new SimpleStringProperty(canceled_date);
        this.remarks = new SimpleStringProperty(remarks);
    }
}

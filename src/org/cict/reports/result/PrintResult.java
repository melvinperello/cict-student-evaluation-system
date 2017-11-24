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
package org.cict.reports.result;

import com.itextpdf.text.Document;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.apache.commons.lang3.text.WordUtils;
import org.cict.authentication.authenticator.CollegeFaculty;
import org.cict.reports.ReportsDirectory;

/**
 *
 * @author Joemar
 */
public class PrintResult extends Transaction {

    public String reportTitleHeader = null, reportTitleIntro = null, reportOtherDetail = null;
    public String[] columnNames = null;
    public ArrayList<String[]> ROW_DETAILS = null;
    public String fileName = "";
    private Document documentFormat;

    public void setDocumentFormat(Document documentFormat) {
        this.documentFormat = documentFormat;
    }
    
    
    @Override
    protected boolean transaction() {
        return true;
    }
    
    public final static String SAVE_DIRECTORY = "reports/results";
    @Override
    protected void after() {
        String doc = fileName + "_" + Mono.orm().getServerTime().getCalendar().getTimeInMillis();

        String RESULT = SAVE_DIRECTORY + "/" + doc + ".pdf";

        //------------------------------------------------------------------------------
        //------------------------------------------------------------------------------
        /**
         * Check if the report save directory is already existing and created if
         * not this will try to create the needed directories.
         */
        boolean isCreated = ReportsDirectory.check(SAVE_DIRECTORY);

        if (!isCreated) {
            // some error message that the directory is not created
            System.err.println("Directory is not created.");
            return;
        }
        //------------------------------------------------------------------
        //------------------------------------------------------------------

        ResultReport def = new ResultReport(RESULT);
        def.setDocumentFormat(documentFormat);
        def.REPORT_TITLE = reportTitleHeader;
        def.COLUMN_NAMES = columnNames;
        def.ROW_DETAILS = ROW_DETAILS;
        def.REPORT_DESCRIPTION = reportTitleIntro;
        def.REPORT_OTHER_DETAIL = reportOtherDetail;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy hh:mm: aa");
        def.DATETIME = formatter.format(Mono.orm().getServerTime().getDateWithFormat());
        def.USER = WordUtils.capitalizeFully(CollegeFaculty.instance().getFirstLastName());
        def.TERMINAL = Mono.sys().getTerminal();
        def.print();
    }
    
}

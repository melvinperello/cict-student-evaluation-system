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
package artifacts;

import com.jhmvin.Mono;
import com.jhmvin.orm.lazy.TextWriter;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Jhon Melvin
 */
public class ErrorLogger {

    private final static String LOG_DIR = "error_logs";
    private final static SimpleDateFormat logTime = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

    public static void record(Throwable e) {
        ErrorLogger el = new ErrorLogger();
        el.header();
        el.setCaughtThrowable(e);
        el.printError();
        el.writeToText();
        JOptionPane.showMessageDialog(null, "A System Error Has Occured.\nThe error was saved in your local log files for information,\nthe system needs to be terminated.", "ERROR", JOptionPane.ERROR_MESSAGE);
        Runtime.getRuntime().halt(0);
    }
    private String exceptionLog;
    private Throwable caughtThrowable;

    private void setCaughtThrowable(Throwable caughtThrowable) {
        this.caughtThrowable = caughtThrowable;
    }

    private ErrorLogger() {
        this.exceptionLog = "";
    }

    private void write(String str) {
        this.exceptionLog += (str + "\n");
    }

    private void header() {
        write("================================================================");
        // since this is an error date should be fetch locally.
        write("-----------------------");
        write("System Information");
        write("-----------------------");
        write("DATE: " + logTime.format(new Date()));
        write("OS: " + Mono.sys().getOS());
        write("USER: " + Mono.sys().getLoggedUser() + " @ " + Mono.sys().getTerminal());
        write("IP: " + Mono.sys().getIP());
        write("-----------------------");
        write("Recorded Exception");
        write("-----------------------");
    }

    private void printError() {
        //----------------------------------------------------------------------
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        caughtThrowable.printStackTrace(pw);
        String exceptionText = sw.toString();
        //----------------------------------------------------------------------
        String throwMessage = caughtThrowable.getMessage();
        if (throwMessage != null) {
            write("Message: " + throwMessage);
        } else {
            write("Message: No Included Message");
        }

        if (caughtThrowable.getCause() != null) {
            write("Cause: " + caughtThrowable.getCause().getMessage());
        } else {
            write("Cause: No Definite Cause or Cause Clause Not Reachable");
        }
        write("Complete Stack Trace: " + exceptionText);
        write("================================================================");
    }

    private void writeToText() {
        //----------------------------------------------------------------------
        // print to console
        System.err.println(this.exceptionLog);
        //----------------------------------------------------------------------
        // save to file
        Calendar c = Calendar.getInstance();
        String timeStamp = String.valueOf(c.getTimeInMillis());
        String fileName = LOG_DIR + "/" + timeStamp + ".txt";
        File logFile = new File(LOG_DIR);
        if (!logFile.exists()) {
            logFile.mkdirs();
        }
        TextWriter tw = new TextWriter(fileName);
        tw.forRewrite();
        tw.write(this.exceptionLog);
        tw.close();
    }

    // Thread occurence
    // Exception Information
}

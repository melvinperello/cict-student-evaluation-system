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
package com.jhmvin.orm;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author Jhon Melvin
 */
public class ServerTime {

    private Session session;

    public ServerTime(Session session) {
        this.session = session;
    }

    private Object fetchInterface(String time) {
        Query query = this.session.createNativeQuery("select " + time.toUpperCase());
        return query.getSingleResult();
    }

    private Timestamp getTimeStamp() {
        Timestamp timestamp = (Timestamp) this.fetchInterface("CURRENT_TIMESTAMP");
        return timestamp;
    }

    private Time getTime() {
        Time time = (Time) this.fetchInterface("CURRENT_TIME");
        return time;
    }

    private Date getDate() {
        Date date = (Date) this.fetchInterface("CURRENT_DATE");
        return date;
    }

    //--------------------------------------------------------------------------
    /**
     * Use this to insert/update date in database.
     *
     * @return java.util.Date object for insert or update
     */
    public java.util.Date getDateWithFormat() {
        SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp currentTime = this.getTimeStamp();
        try {
            java.util.Date currentDateTime = sqlFormat.parse(currentTime.toString());
            return currentDateTime;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * More Date Functions.
     *
     * @return
     */
    public Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.getDateWithFormat());
        return calendar;
    }

    /**
     * Server's time value in milliseconds. the current time as UTC milliseconds
     * from the epoch.
     *
     * @return
     */
    public long getEpochTime() {
        return this.getCalendar().getTimeInMillis();
    }

    /**
     * Checks if the given date is past server time. (expired)
     *
     * @param date to check
     * @return
     */
    public boolean isPastServerTime(java.util.Date date) {
        Calendar checkDate = Calendar.getInstance();
        checkDate.setTime(date);

        return this.getEpochTime() > checkDate.getTimeInMillis();
    }

}

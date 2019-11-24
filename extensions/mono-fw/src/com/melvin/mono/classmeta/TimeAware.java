/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melvin.mono.classmeta;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Class form Formatting time.
 *
 * @author Jhon Melvin
 */
public class TimeAware {

    /**
     * To avoid creating redundant date formatter objects.
     */
    private final static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat();
    /**
     * The default format to apply when there is no given format.
     */
    private final static String DEFAULT_FORMAT = "MM/dd/yyyy hh:mm:ss a";

    /**
     * Current System Time based on local PC.
     *
     * @param pattern Date Pattern.
     * @return
     */
    public final static String getSystemTime(String pattern) {
        DATE_FORMATTER.applyPattern(pattern);
        return DATE_FORMATTER.format(Calendar.getInstance().getTime());
    }

    /**
     * Current System Time based on local PC.
     *
     * @return
     */
    public final static String getSystemTime() {
        return getSystemTime(DEFAULT_FORMAT);
    }

    //--------------------------------------------------------------------------
    public final static String formatDate(Date source, String pattern) {
        DATE_FORMATTER.applyPattern(pattern);
        return DATE_FORMATTER.format(source);
    }

    public final static String formatDate(Date source) {
        return formatDate(source, DEFAULT_FORMAT);
    }
}

package org.cict.reports.advisingslip;

public class AdvisingSlipData {

    /* this holds table for the data to ensure proper arrangement */
    public String subject_code = "";
    public String subj_title = "";
    public String section = "";
    public String schedule = "";
    public String room = "";
    public String lec_units = "";
    public String lab_units = "";

    public AdvisingSlipData(String subj_code, String title, String section, String schedule, String room, String lec, String lab) {
        //empty constructor
        this.subject_code = subj_code;
        this.subj_title = title;
        this.section = section;
        this.schedule = schedule;
        this.room = room;
        this.lec_units = lec;
        this.lab_units = lab;
    }

    public AdvisingSlipData() {
        //
    }

}

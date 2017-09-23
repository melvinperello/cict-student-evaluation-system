package org.cict.reports.advisingslip;

import java.util.ArrayList;

public class AdvisingSlipMain {

    public ArrayList<AdvisingSlipData> subject_list;

    public AdvisingSlipMain() {
        AdvisingSlip advising_data = new AdvisingSlip("2014112478");
        advising_data.INFO_DATE = "ngayon";

        ArrayList<AdvisingSlipData> table_data = new ArrayList<>();
        for (int x = 0; x < 10; x++) {
            AdvisingSlipData row = new AdvisingSlipData();
            row.subject_code = "IT 113";
            row.subj_title = "Computer Programming";
            row.section = "BSIT 4A-G1";
            row.schedule = "Schedule";
            row.room = "IT3";
            row.lab_units = "0.0";
            row.lec_units = "3.0";

            table_data.add(row);
            
        }
        advising_data.INFO_SUBJECTS = table_data;

        int val = advising_data.print();
//        if(val==1)
//            return 1;
//        return 0;
    }

    public static void main(String[] args) {
        new AdvisingSlipMain();
    }
}

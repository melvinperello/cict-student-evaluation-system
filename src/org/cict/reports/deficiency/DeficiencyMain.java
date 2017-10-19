
package org.cict.reports.deficiency;

import java.util.ArrayList;

public class DeficiencyMain {
    public DeficiencyMain() {
        Deficiency deficiency = new Deficiency("deficiency");
        deficiency.STUDENT_NUMBER = "2014112478";
        deficiency.STUDENT_NAME = "DE LA CRUZ, JOEMAR NUCOM";
        deficiency.STUDENT_ADDRESS = "8-152 SUCAD APALIT, PAMPANGA";
        deficiency.CURRICULUM_NAME = "BACHELOR OF SCIENCE IN INFORMATION TECHNOLOGY";
        ArrayList<String> subjects = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            subjects.add("PSYCH 113");
            subjects.add("General Psychology");
            subjects.add("3.0");
            subjects.add("0.0");
            subjects.add("3.0");
            subjects.add("3");
            subjects.add("NONE");
            subjects.add("NONE");
        }
//        deficiency.SUBJECTS_PER_SEM.put("11", subjects);
//        deficiency.SUBJECTS_PER_SEM.put("12", subjects);
//        deficiency.SUBJECTS_PER_SEM.put("21", subjects);
//        deficiency.SUBJECTS_PER_SEM.put("22", subjects);
//        deficiency.SUBJECTS_PER_SEM.put("31", subjects);
//        deficiency.SUBJECTS_PER_SEM.put("32", subjects);
//        deficiency.SUBJECTS_PER_SEM.put("41", subjects);
//        deficiency.SUBJECTS_PER_SEM.put("42", subjects);
        int val = deficiency.print();
    }

    public static void main(String[] args) {
        new DeficiencyMain();
    }
}



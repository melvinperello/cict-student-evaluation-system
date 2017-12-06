
package update.org.cict.reports.add_change_form;

public class AddChangeFormMain {
    public AddChangeFormMain() {
        AddChangeForm addChangeForm = new AddChangeForm("add_change_form");
        addChangeForm.STUDENT_NUMBER = "2014112478";
        addChangeForm.STUDENT_NAME = "DE LA CRUZ, JOEMAR NUCOM";
        addChangeForm.STUDENT_SECTION = "BSIT 4A-G1";
        addChangeForm.COLLEGE_DEAN = "DR. NOEMI REYES";
        addChangeForm.STUDENT_CURRENT_UNITS = "20.0";
        String[] sampleData = {"IT 113", "BSIT 4A-G1", "1.0"};
        addChangeForm.STUDENT_SUBJECTS.add(sampleData);
        addChangeForm.STUDENT_SUBJECTS.add(sampleData);
        addChangeForm.STUDENT_SUBJECTS.add(sampleData);
        addChangeForm.STUDENT_SUBJECTS.add(sampleData);
        addChangeForm.STUDENT_SUBJECTS.add(sampleData);
        int val = addChangeForm.print(false);
    }

    public static void main(String[] args) {
        new AddChangeFormMain();
    }
}



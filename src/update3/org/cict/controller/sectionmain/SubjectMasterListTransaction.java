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
package update3.org.cict.controller.sectionmain;

import app.lazy.models.DB;
import app.lazy.models.Database;
import app.lazy.models.LoadSubjectMapping;
import app.lazy.models.StudentMapping;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import java.util.ArrayList;
import update3.org.excelprinter.StudentMasterListData;

/**
 *
 * @author Jhon Melvin
 */
public class SubjectMasterListTransaction extends Transaction {

    private Integer id;

    public void setId(Integer id) {
        this.id = id;
    }

    // result values
    private ArrayList<StudentMasterListData> studentData;

    public ArrayList<StudentMasterListData> getStudentData() {
        return studentData;
    }

    @Override
    protected boolean transaction() {
        ArrayList<LoadSubjectMapping> accepted_cluster
                = Mono.orm()
                        .newSearch(Database.connect().load_subject())
                        .eq(DB.load_subject().LOADGRP_id, id)
                        .active()
                        .all();

        if (accepted_cluster == null) {
            return false; // no data
        }

        studentData = new ArrayList<>();

        for (LoadSubjectMapping load : accepted_cluster) {
            StudentMapping studentInfo = Database.connect()
                    .student()
                    .getPrimary(load.getSTUDENT_id());

            if (studentInfo == null) {
                continue;
            }

            String studentNumber = studentInfo.getId(); // student number
            String studentFullName = studentInfo.getLast_name().toUpperCase()
                    + ", "
                    + studentInfo.getFirst_name().toUpperCase();

            if (studentInfo.getMiddle_name() != null) {
                studentFullName += (" " + studentInfo.getMiddle_name());
            }
            studentData.add(new StudentMasterListData(studentNumber, studentFullName));
        }

        if (studentData.isEmpty()) {
            return false;
        }

        return true;
    }

    @Override
    protected void after() {

    }

}

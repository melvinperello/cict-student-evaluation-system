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
package org.cict.accountmanager.faculty;

import app.lazy.models.DB;
import static app.lazy.models.DB.faculty;
import app.lazy.models.Database;
import app.lazy.models.FacultyMapping;
import com.jhmvin.Mono;
import com.jhmvin.fx.async.Transaction;
import java.util.ArrayList;
import org.hibernate.criterion.Order;

/**
 *
 * @author Joemar
 */
public class FetchFacultyInfo extends Transaction {

    private ArrayList<FacultyInformation> activeFaculty = new ArrayList<>();
    private ArrayList<FacultyInformation> deactivatedFaculty = new ArrayList<>();

    public ArrayList<FacultyInformation> getActiveFaculty() {
        return activeFaculty;
    }

    public ArrayList<FacultyInformation> getDeactivatedFaculty() {
        return deactivatedFaculty;
    }

    @Override
    protected boolean transaction() {
        ArrayList<FacultyMapping> facultyMaps = Mono.orm().newSearch(Database.connect().faculty())
                .execute(Order.asc(DB.faculty().first_name))
                .all();
        for (FacultyMapping facultyMap : facultyMaps) {
            FacultyInformation fInfo = new FacultyInformation(facultyMap);
            if (fInfo.getAccountFacultyMapping() == null) {
                if(fInfo.getFacultyMapping().getActive().equals(0)) {
                    deactivatedFaculty.add(fInfo);
                } else {
                    activeFaculty.add(fInfo);
                }
                continue;
            }
            if (fInfo.getAccountFacultyMapping().getActive().equals(0)) {
                deactivatedFaculty.add(fInfo);
            } else {
                activeFaculty.add(fInfo);
            }
        }
        return true;
    }

    @Override
    protected void after() {

    }

}

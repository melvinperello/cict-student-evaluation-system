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
 * JOEMAR N. DE LA CRUZ
 * GRETHEL EINSTEIN BERNARDINO
 *
 * OTHER LIBRARIES THAT ARE USED BELONGS TO THEIR RESPECTFUL OWNERS AND AUTHORS.
 * NO COPYRIGHT ARE INTENTIONAL OR INTENDED.
 * THIS PROJECT IS NOT PROFITABLE HENCE FOR EDUCATIONAL PURPOSES ONLY.
 * THIS PROJECT IS ONLY FOR COMPLIANCE TO OUR REQUIREMENTS.
 * THIS PROJECT DOES NOT INCLUDE DISTRIBUTION FOR OTHER PURPOSES.
 *
 */
package org.cict.evaluation.student;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Joemar
 */
public class StudentValues {
    
    public static final String NO_AVAILABLE = "NO AVAILABLE";
    public static final String NOT_SPECIFIED = "NOT SPECIFIED";
    public static final String ALL = "ALL";
    public static final String SELECT_HERE = "-- Select here --";

    //Enrollment types
    public static final String REGULAR = "REGULAR";
    public static final String SHIFTER = "SHIFTER";
    public static final String TRANSFEREE = "TRANSFEREE";
    public static final String CROSS_ENROLLEE = "CROSS-ENROLLEE";

    private ArrayList<String> ENROLLMENT_TYPE = new ArrayList<String>();

    public ArrayList<String> getEnrollmentTypes() {
        ENROLLMENT_TYPE.add(REGULAR);
        ENROLLMENT_TYPE.add(SHIFTER);
        ENROLLMENT_TYPE.add(TRANSFEREE);
        ENROLLMENT_TYPE.add(CROSS_ENROLLEE);
        return ENROLLMENT_TYPE;
    }

    //Gender
    public static final String MALE = "MALE";
    public static final String FEMALE = "FEMALE";

    private ArrayList<String> GENDER = new ArrayList<String>();

    public ArrayList<String> getGenders() {
        GENDER.add(MALE);
        GENDER.add(FEMALE);
        return GENDER;
    }

    public static final String FIRST_YR = "FIRST YEAR";
    public static final String SECOND_YR = "SECOND YEAR";
    public static final String THIRD_YR = "THIRD YEAR";
    public static final String FOURTH_YR = "FOURTH YEAR";
    
    //Year levels
    private ArrayList<String> YEAR_LEVEL = new ArrayList<String>();
    public ArrayList<String> getYearLevels() {
        YEAR_LEVEL.add("FIRST YEAR");
        YEAR_LEVEL.add("SECOND YEAR");
        YEAR_LEVEL.add("THIRD YEAR");
        YEAR_LEVEL.add("FOURTH YEAR");
        return YEAR_LEVEL;
    }

    public static String getYearLevel(Integer yrlvl) {
        HashMap<Integer, String> intToStringYearLvl = new HashMap<>();
        intToStringYearLvl.put(1, "FIRST YEAR");
        intToStringYearLvl.put(2, "SECOND YEAR");
        intToStringYearLvl.put(3, "THIRD YEAR");
        intToStringYearLvl.put(4, "FOURTH YEAR");
        return intToStringYearLvl.get(yrlvl);
    }

    public Integer getYearLevel(String yrlvl) {
        Integer yr = 0;
        switch (yrlvl) {
            case FIRST_YR: {
                yr = 1;
                break;
            }
            case SECOND_YR: {
                yr = 2;
                break;
            }
            case THIRD_YR: {
                yr = 3;
                break;
            }
            case FOURTH_YR: {
                yr = 4;
                break;
            }
        }
        return yr;
    }
}

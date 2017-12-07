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
package update3.org.cict.access;

import org.apache.commons.lang3.text.WordUtils;

/**
 *
 * @author Jhon Melvin
 */
public class SystemOverriding {

    public static final String CATEGORY_EVALUATION = "EVALUATION";

    /**
     * Evaluation Related System Overriding.
     */
    public static final String EVAL_EXCEED_MAX_UNITS = "EXCEEDED_MAX_UNITS";
    public static final String EVAL_INTERNSHIP_WITH_OTHERS = "INTERNSHIP_WITH_OTHERS";
    public static final String EVAL_BYPASSED_PRE_REQUISITES = "BYPASSED_PRE_REQUISITES";
    public static final String EVAL_INTERN_GRADE_REQUIREMENT = "INTERN_GRADE_REQUIREMENT";
    public static final String EVAL_GRADE_REQUIREMENT_FOR_MOVING_UP = "GRADE_REQUIREMENT_FOR_MOVING_UP";
    public static final String EVAL_EXCEED_MAX_POPULATION = "EXCEEDED_MAX_POPULATION";
    public static final String EVAL_CHANGED_YEAR_LEVEL_BACKWARD = "CHANGED_YEAR_LEVEL_BACKWARD";
    public static final String EVAL_STUDENT_OVERSTAY = "OVERSTAYED_STUDENT";


    public static String getACRONYM(int maxChar, String... strng) {
        String temp = null;
        String str = strng[0];
        if(str==null || str.isEmpty()) {
            return "";
        }
        try {
            String mode = (strng[1]);
            System.out.println(mode);
            temp = str.replaceAll("_", "");
            String[] name = temp.split(" ");
            temp = "";
            for(String each: name) {
                temp += get(each, 5);
            }
        } catch (IndexOutOfBoundsException e) {
            temp = str.replaceAll(" ", "").replaceAll("_", "");
            temp = get(temp, maxChar);
        }
        return temp;
    }
    
    private static String get(String temp, int maxChar) {
        if(temp.length()>maxChar) {
            temp = temp.replaceAll("A", "");
            System.out.println(temp);
            if(temp.length()>maxChar) {
                temp = temp.replaceAll("E", "");
                System.out.println(temp);
                if(temp.length()>maxChar) {
                    temp = temp.replaceAll("I", "");
                    System.out.println(temp);
                    if(temp.length()>maxChar) {
                        temp = temp.replaceAll("O", "");
                        System.out.println(temp);
                        if(temp.length()>maxChar) {
                            temp = temp.replaceAll("U", "");
                            System.out.println(temp);
                                if(temp.length()>maxChar) {
                                    temp = temp = temp.substring(0, maxChar);
                                    System.out.println(temp);
                                    if(temp.length()>maxChar) {
                                        temp = temp = WordUtils.initials(temp);
                                        System.out.println(temp);
                                    }
                                }
                        }
                    }
                }
            }
        }
        return temp;
    }
}

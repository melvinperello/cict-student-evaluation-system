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
package artifacts;

import app.lazy.models.StudentMapping;

/**
 *
 * @author Jhon Melvin
 */
public class CurriculumFix {

    /**
     * Get the correct curriculum of a student depending on what year specified.
     *
     * @param studentMap
     * @param onWhatYear even if the student is already 4th year and you type 1
     * the curriculum that will be returned is the curriculum during year 1.
     * @return
     */
    public static Integer getCorrectCurriculum(StudentMapping studentMap, Integer onWhatYear) {
        //----------------------------------------------------------------------
        Integer correctCurriculum;
        // check student if student has prep id
        if (studentMap.getPREP_id() != null) {
            if (onWhatYear.equals(1) || onWhatYear.equals(2)) {
                // if first year or second year
                correctCurriculum = studentMap.getPREP_id();
            } else {
                correctCurriculum = studentMap.getCURRICULUM_id();
            }
        } else {
            // if no prep id
            correctCurriculum = studentMap.getCURRICULUM_id();
        }
        //----------------------------------------------------------------------
        return correctCurriculum;
    }
}

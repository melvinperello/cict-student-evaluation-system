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
package update2.org.cict.controller.academicprogram;

import app.lazy.models.AcademicProgramMapping;
import app.lazy.models.CurriculumMapping;
import java.util.ArrayList;

/**
 *
 * @author Jhon Melvin
 */
public class AcademicProgramInfo {

    private AcademicProgramMapping academicProgram;
    private String apCreatedBy;
    private String apImplementedBy;

    //------------
    public String getApCreatedBy() {
        return apCreatedBy;
    }

    public void setApCreatedBy(String apCreatedBy) {
        this.apCreatedBy = apCreatedBy;
    }

    public String getApImplementedBy() {
        return apImplementedBy;
    }

    public void setApImplementedBy(String apImplementedBy) {
        this.apImplementedBy = apImplementedBy;
    }

    //------------
    private ArrayList<CurriculumMapping> curriculums;

    public AcademicProgramMapping getAcademicProgram() {
        return academicProgram;
    }

    public void setAcademicProgram(AcademicProgramMapping academicProgram) {
        this.academicProgram = academicProgram;
    }

    public ArrayList<CurriculumMapping> getCurriculums() {
        return curriculums;
    }

    public void setCurriculums(ArrayList<CurriculumMapping> curriculums) {
        this.curriculums = curriculums;
    }

}

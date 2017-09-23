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
package update.org.cict.controller.adding.subjectviewer;

import update.org.cict.controller.adding.SubjectInformationHolder;

/**
 *
 * @author Jhon Melvin
 */
public class AddingDataPipe {

    private static AddingDataPipe INSTANCE;

    public static AddingDataPipe instance() {
        if (INSTANCE == null) {
            INSTANCE = new AddingDataPipe();
        }
        return INSTANCE;
    }

    /**
     * Make the variable public to make use of easier access.
     */
    public boolean isChanged;
    public boolean isChangedValueRecieved;
    public SubjectInformationHolder isChangedValue;

    private AddingDataPipe() {
        //
        // assign default values to avoid null pointer exception.
        isChanged = false;
        isChangedValueRecieved = false;
        // we do not need to assign default value for isChangedValue
        // we need to test if the value is null

    }

    public void resetIsChanged() {
        this.isChanged = false;
        this.isChangedValueRecieved = false;
        this.isChangedValue = null;
    }

}

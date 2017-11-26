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
package org.cict;

import com.jhmvin.Mono;
import com.jhmvin.fx.display.WindowFX;

/**
 *
 * @author Jhon Melvin
 */
public class GenericLoadingShow {

    private static GenericLoadingShow INSTANCE;

    public static GenericLoadingShow instance() {
        if (INSTANCE == null) {
            INSTANCE = new GenericLoadingShow();
        }
        return INSTANCE;
    }

    private WindowFX loadingWindow;

    private GenericLoadingShow() {
        loadingWindow = Mono.fx()
                .create()
                .setPackageName("org.cict")
//                .setFxmlDocument("GenericLoading")
                .setFxmlDocument("GenericLoading2")
                .makeFX()
                .makeScene()
                .makeStageApplication()
                .stageResizeable(false)
                .stageUndecorated(true);
    }

    public void show() {
        loadingWindow.pullOutStage().show();
    }

    public void hide() {
        loadingWindow.pullOutStage().hide();
    }

}

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

import app.lazy.models.AccountFacultyMapping;
import com.jhmvin.Mono;
import org.cict.authentication.authenticator.CollegeFaculty;

/**
 *
 * @author Jhon Melvin
 */
public class Access {

    /**
     * System Access Level.
     */
    private final static int LEVEL_SYSTEM = 1;
    private final static int LEVEL_ADMIN = 2;
    private final static int LEVEL_ASST_ADMIN = 3;
    private final static int LEVEL_LOCAL_REGISTRAR = 4;
    private final static int LEVEL_CO_REGISTRAR = 5;
    private final static int LEVEL_EVALUATOR = 6;
    private final static int LEVEL_FACULTY = 7;
    private final static int LEVEL_DENIED = Integer.MAX_VALUE;

    /**
     * System Access Types.
     */
    public final static String ACCESS_SYSTEM = "SYSTEM";
    public final static String ACCESS_ADMIN = "ADMINISTRATOR";
    public final static String ACCESS_ASST_ADMIN = "ASSISTANT_ADMINISTRATOR";
    public final static String ACCESS_LOCAL_REGISTRAR = "LOCAL_REGISTRAR";
    public final static String ACCESS_CO_REGISTRAR = "CO_REGISTRAR";
    public final static String ACCESS_EVALUATOR = "EVALUATOR";
    public final static String ACCESS_FACULTY = "FACULTY";

    /**
     * Get access level in Integer format.
     *
     * @param access type from the database. possessed
     * @return
     */
    public static int getAccessLevel(String access) {
        return access.equals(ACCESS_SYSTEM) ? LEVEL_SYSTEM
                : access.equals(ACCESS_ADMIN) ? LEVEL_ADMIN
                : access.equals(ACCESS_ASST_ADMIN) ? LEVEL_ASST_ADMIN
                : access.equals(ACCESS_LOCAL_REGISTRAR) ? LEVEL_LOCAL_REGISTRAR
                : access.equals(ACCESS_CO_REGISTRAR) ? LEVEL_CO_REGISTRAR
                : access.equals(ACCESS_EVALUATOR) ? LEVEL_EVALUATOR
                : access.equals(ACCESS_FACULTY) ? LEVEL_FACULTY
                : LEVEL_DENIED;
    }

    /**
     * Specific access to a component.
     *
     * @param required
     * @param possessed
     * @return
     */
    public static boolean isGranted(String required, String possessed) {
        return required.equalsIgnoreCase(possessed);
    }

    /**
     * Grants access by level. user with higher level of access than the
     * required can access the component.
     *
     * @param required
     * @param possessed
     * @param byLevel
     * @return
     */
    public static boolean isGranted(String lowestRequired, String possessed, boolean byLevel) {
        if (byLevel == false) {
            return isGranted(lowestRequired, possessed);
        } else {
            return getAccessLevel(lowestRequired) >= getAccessLevel(possessed);
        }
    }
    //--------------------------------------------------------------------------

    public static boolean isGrantedIfFrom(String possessed, String... allowed) {
        for (String string : allowed) {
            if (isGranted(string, possessed)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isGrantedIf(String... allowed) {
        return isGrantedIfFrom(CollegeFaculty.instance().getACCESS_LEVEL(), allowed);
    }

    /**
     * Shorthand method.
     *
     * @param lowestRequired
     * @param byLevel
     * @return
     */
    public static boolean isGranted(String lowestRequired, boolean byLevel) {
        return isGranted(lowestRequired,
                CollegeFaculty.instance().getACCESS_LEVEL(),
                byLevel);
    }

    public static boolean isGranted(String required) {
        return isGranted(required, CollegeFaculty.instance().getACCESS_LEVEL());
    }

    //--------------------------------------------------------------------------
    public static boolean isDeniedIfNot(String required) {
        return !isGranted(required);
    }

    public static boolean isDeniedIfNot(String lowestRequired, boolean byLevel) {
        if (lowestRequired == null) {
            // deny if null access
            return true;
        }
        return !isGranted(lowestRequired, byLevel);
    }

    public static boolean isDeniedIfNotFrom(String... required) {
        for (String string : required) {
            if (isGranted(string)) {
                return false;
            }
        }
        return true;
    }

    //-------------------------------------------------------------------------
    /**
     * Function to determine if a faculty is allowed to revoke evaluation
     * including adding/changing permission given by the registrar.
     *
     * @return
     */
    public static AccountFacultyMapping isAllowedToRevoke() {
        ReEvaluationAccess controller = new ReEvaluationAccess();
        Mono.fx().create()
                .setPackageName("update3.org.cict.access")
                .setFxmlDocument("ReEvaluationAccess")
                .makeFX()
                .setController(controller)
                .makeScene()
                .makeStageApplication()
                .stageResizeable(false)
                .stageUndecorated(true)
                .stageTitle("Revocation Access")
                .stageCenter()
                .stageShowAndWait();

        return controller.getAllowedUser();
    }

    /**
     * Allows the registrar to override existing system rules regarding
     * evaluation and limited to evaluation only.
     *
     * @param giveAccess
     * @return
     */
    public static Object[] isEvaluationOverride(boolean giveAccess) {
        EvaluationOverride controller = new EvaluationOverride(giveAccess);
        Mono.fx().create()
                .setPackageName("update3.org.cict.access")
                .setFxmlDocument("EvaluationOverride")
                .makeFX()
                .setController(controller)
                .makeScene()
                .makeStageApplication()
                .stageResizeable(false)
                .stageUndecorated(true)
                .stageTitle("System Override")
                .stageCenter()
                .stageShowAndWait();
        Object[] result = new Object[2];
        result[0] = controller.isAuthorized();
        result[1] = controller.getAttachedFile();
        return result;
    }
}

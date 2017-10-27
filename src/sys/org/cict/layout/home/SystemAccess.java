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
package sys.org.cict.layout.home;

import com.jhmvin.Mono;

/**
 *
 * @author Jhon Melvin
 */
public class SystemAccess {

    // m o n o s y n c s t u d i o p h - c i c t e v a l u a t i o n - [06101997]
    private final static String userKey = "23a9d7657cbbe9bfc21c1d401d6114dc2af3240903bde4ef4cf93c057d784112a14b5abfb727f5f881cb10d3de07ae4906cf5e395444968b16db9777c036d2d7";

    // 5521300850
    private final static String passKey = "eee23372a7d69555f240b8d178437b0d8491519b0347abf26b59c566629d7ca82e9fd24df280fed718129109e1ab6cc05b9f38a1ce1b41a11c45ea76478b5bc0";

    public final static boolean checkSystemAccess(String user, String pass) {
        String hashedUser = Mono.security().hashFactory().hash_sha512(user);
        String hashedPass = Mono.security().hashFactory().hash_sha512(pass);
        if (userKey.equals(hashedUser)) {
            if (passKey.equals(hashedPass)) {
                return true;
            }
        }
        return false;
    }

}

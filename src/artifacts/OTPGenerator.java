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
package artifacts;

import java.util.Random;

/**
 *
 * @author Joemar
 */
public class OTPGenerator {

    public static void main(String[] args) {
        System.out.println(generateOTP());
    }

    /**
     * Default method for 6 characters
     *
     * @return
     */
    public static String generateOTP() {
        return generateOTP(6);
    }

    /**
     * Create a random String with high entropy.
     *
     * @param charLength number of characters (MAX 36 Characters)
     * @return
     */
    public static String generateOTP(int charLength) {
//        String code = UUID.randomUUID().toString();
//        return code.replace("-", "").substring(0, 6).toUpperCase();
        int RANDOM_LENGTH = charLength;
        // Create A Random String
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        int SALTCHAR_LENGTH = SALTCHARS.length();
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < SALTCHAR_LENGTH) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        // Sttore the random string
        String randomString = salt.toString();
        // From the random string pick a random number
        int min = 0;
        int max = randomString.length() - 1;

        int random_int = rnd.nextInt(max - min + 1) + 1;

        if ((random_int + RANDOM_LENGTH) > max) {
            random_int = max;
            random_int = random_int - RANDOM_LENGTH;
        }

        return randomString.substring(random_int, random_int + RANDOM_LENGTH);
    }
}

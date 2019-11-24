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
package app.lazy.models;

import com.jhmvin.Mono;
import com.jhmvin.orm.lazy.HibernateWizard;
import com.jhmvin.orm.lazy.LazyConfiguration;

/**
 *
 * @author Jhon Melvin
 */
public class ABlissfulWayToConnect {

    public static void main(String[] args) {
        /**
         * Run Me. Hey There I will sync the models to the database with just a
         * few clicks away.
         */
        Mono.version();
        LazyConfiguration lazy = LazyConfiguration.config();
        lazy.setDriverClass("org.mariadb.jdbc.Driver");
        lazy.setProvider("jdbc:mariadb");
        lazy.setHost("127.0.0.1");
        lazy.setPort("3306");
        lazy.setDatabase("cictems");
        lazy.setUsername("root");
        lazy.setPassword("root");
        lazy.setPackageName("app.lazy.models");

        HibernateWizard.sync();

    }
}

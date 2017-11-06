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
package com.jhmvin;

/**
 *
 * @author Jhon Melvin
 */
public class ChangeLogs {
    
    private static void write(String message) {
        System.out.println(" ~ " + message);
    }
    
    private static void writeChanges(String message) {
        System.out.println(" ~ * " + message);
    }
    
    private static void fixes(String message) {
        System.out.println(" ~ ♥ FIXED  : " + message);
    }
    
    private static void updates(String message) {
        System.out.println(" ~ ☻ UPDATES: " + message);
    }
    
    private static void addons(String message) {
        System.out.println(" ~ √ NEW    : " + message);
    }
    
    public static void showChanges() {
        write("");
        write("");
        write("Monosync Framework Changelogs:");
        _07252017();
        _07292017();
        _08022017();
        _08032017();
        _08082017();
        _08162017();
        _08202017();
        _08252017();
        _release();
    }
    
    private static void _07252017() {
        write("");
        writeChanges("07/25/2017");
        fixes("Stage Modalities show and showAndWait");
        updates("Get Parent stage using Mono.fx().getParentStage()");
        updates("Access server date using Mono.orm().getServerTime()");
        addons("Added changelogs.");
    }
    
    private static void _07292017() {
        write("");
        writeChanges("07/29/2017");
        addons("Added HibernateWizard.sync, no need to use psv5 module in php");
        write("This wizard is restricted only for cictems project.");
    }
    
    private static void _08022017() {
        write("");
        writeChanges("08/02/2017");
        updates("Allows unknown host ip");
    }
    
    private static void _08032017() {
        write("");
        writeChanges("08/03/2017");
        addons("SQL Aggregate Functions: Projection Class.");
    }
    
    private static void _08082017() {
        write("");
        writeChanges("08/08/2017");
        addons("Cron Jobs: Timer and Thread.");
        addons("Simple Task");
    }
    
    private static void _08162017() {
        write("");
        writeChanges("08/16/2017");
        addons("DB Tables");
        write("\t --Please do not use hardcoded strings for table properties use this instead.");
        addons("Map Factory Generation");
        updates("session() updated to -> openSession()");
        write("\t --please check the return value if null for safe execution");
        updates("closeSessionFactory updated to -> disconnect()");
        write("\t --returns true if disconnected successfully, false if not");
        addons("added Mono.orm().shutdown() clear cache before disconnection");
    }
    
    private static void _08202017() {
        write("");
        writeChanges("08/20/2017");
        addons("Added Fancy Simple Table");
        updates("Lazy Settings");
    }
    
    private static void _08252017() {
        write("");
        writeChanges("08/25/2017");
        addons("Added SceneFX");
        updates("Fixed Algorithim of SceneFX Search");
    }
    
    private static void _release() {
        writeChanges("08/27/2017");
        updates("Fixed SceneFX Events");
        writeChanges("09/11/2017");
        updates("Fixed Transaction Class and onFailure Callback");
        addons("Transaction whenEvents and whenFinished Callback");
    }
    
}

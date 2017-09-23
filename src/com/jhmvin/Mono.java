/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jhmvin;

import com.jhmvin.fx.FxLibrary;
import com.jhmvin.orm.MonoHibernate;
import com.jhmvin.security.ProtectorOfTheRealm;
import com.jhmvin.system.SystemEnvironment;

/**
 *
 * @author Jhon Melvin
 */
public class Mono {

    // my instance
    private static Mono FRAMEWORK_INSTANCE;
    // versioning info
    private final static String BUILD = "v1.8.12-rel";
    private final static String BUILD_NAME = "TEACUP RICE ROLL";
    private final static String BUILD_DATE = "SEPTEMBER 11, 2017";
    private final static String BUILD_AUTHOR = "JHON MELVIN NIETO PERELLO";
    private final static String HIBERNATE_VERSION = "hibernate-release-5.2.8.Final";
    private final static String CONTROLS_FX_VERSION = "controls fx 8.40.9";
    private final static String MATERIAL_FX_VERSION = "jfoenix";

    private Mono() {
        // restriction
    }

    /**
     * <strong> Framework Gate Way </strong>
     *
     * @return Rice Roll Instance
     */
    public static Mono start() {
        if (FRAMEWORK_INSTANCE == null) {
            FRAMEWORK_INSTANCE = new Mono();
        }
        return FRAMEWORK_INSTANCE;
    }

    /**
     * <strong> Framework Version Information </strong>
     */
    public static void version() {
        System.out.println("Monosync Framework");
        System.out.println("Build: " + BUILD);
        System.out.println("Code Name: " + BUILD_NAME);
        System.out.println("Release: " + BUILD_DATE);
        System.out.println("Author: " + BUILD_AUTHOR);
        System.out.println("Hibernate: " + HIBERNATE_VERSION);
        System.out.println("ControlsFX: " + CONTROLS_FX_VERSION);
        System.out.println("MaterialFX: " + MATERIAL_FX_VERSION);
        //
        ChangeLogs.showChanges();
    }

    // Class Starts Here !
    /**
     *
     * @return
     */
    public static MonoHibernate orm() {
        return MonoHibernate.getInstance();
    }

    public static FxLibrary fx() {
        return FxLibrary.instance();
    }

    public static SystemEnvironment sys() {
        return new SystemEnvironment();
    }

    public static ProtectorOfTheRealm security() {
        return ProtectorOfTheRealm.instance();
    }

    /**
     * Control Managers
     */
    public static class ControlManager {

        public static class Container extends com.jhmvin.fx.controls.Container {
        }

        public static class SimpleImage extends com.jhmvin.fx.controls.SimpleImage {
        }

    }

    /**
     * Property Managers.
     */
    public static class PropertyManager extends com.jhmvin.propertymanager.PropertyManager {
        //
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melvin.mono.fx.bootstrap;

import com.melvin.mono.fx.MonoLauncher;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import org.reflections.Reflections;

/**
 *
 * @author Jhon Melvin
 */
public class M {

    private static void logger(Object message) {
        System.out.println(String.valueOf(message));
    }

    private static void prettyLog(Object... message) {
        for (Object object : message) {
            logger(" - " + object);
        }

    }

    /**
     * Framework FXML Instance.
     */
    private static M MONO_INSTANCE;

    /**
     * Caller Class.
     *
     * @return
     */
    public static synchronized M app() {
        if (MONO_INSTANCE == null) {
            MONO_INSTANCE = new M();
        }
        return MONO_INSTANCE;
    }

    /**
     * Private constructor upon initialization.
     */
    private M() {
        // constructor
        // pre boot
        this.fxClasses = this.getLaunchableClass();
        // post boot
        this.boot();
    }
    //--------------------------------------------------------------------------
    /**
     * Reflections to get the children of MonoLauncher.
     */
    private final Reflections reflections = new Reflections();
    /**
     * Contains the list of the class that will be loaded.
     */
    private final ArrayList<Class> fxClasses;
    /**
     * Ahead of time Loaded Classes.
     */
    private final HashMap<String, MonoLauncher> launcherList = new HashMap<>();
    //--------------------------------------------------------------------------

    /**
     * all bootstrap method should be placed in here.
     */
    private void boot() {
        prettyLog(
                "",
                "--------------------------------------------------------------",
                "MONOSYNC FRAMEWORK v2.0ad ♥",
                "Author: JHON MELVIN NIETO PERELLO",
                "E-Mail: jhmvinperello@gmail.com",
                "       \"I don't know what to say.\"",
                "--------------------------------------------------------------",
                "",
                "Loading Classes . . .",
                ""
        );
        loadClasses();
    }

    //--------------------------------------------------------------------------
    /**
     * Search for classes that extends the MonoLauncher class.
     *
     * @return
     */
    private ArrayList<Class> getLaunchableClass() {
        prettyLog(
                "Detecting Launchable Classes in Your Project",
                "All Classes that extends the MonoLauncher class are Launchable",
                ""
        );
        Set<Class<? extends MonoLauncher>> classes = reflections.getSubTypesOf(MonoLauncher.class);
        ArrayList<Class> fx_Classes = new ArrayList<>();
        for (Class<? extends MonoLauncher> clazz : classes) {
            fx_Classes.add(clazz);
            prettyLog("Launchable Class Located -> " + clazz.getName());
        }
        prettyLog("", fx_Classes.size() + " Classes Found");
        return fx_Classes;
    }

    /**
     * Loads all FXML Controller and Document before starting up the
     * application.
     */
    private void loadClasses() {
        prettyLog(
                "",
                "AOTL (Ahead Of Time Loading) FXML Started . . .",
                "FXML are loaded in advance to avoid sloppy loading during runtime",
                "You can just restore views to give performance boost in your application",
                "If reloading a view is necessary you can use RELOAD method.",
                ""
        );
        this.fxClasses.forEach(launchables -> {
            try {
                MonoLauncher fxClass = (MonoLauncher) launchables.newInstance();
                fxClass.open();
                launcherList.put(fxClass.getClass().getName(), fxClass);
                prettyLog("Class Loaded -> " + fxClass.getClass().getName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        prettyLog("", "Class Loading Completed.", "", "");

    }

    /**
     * Retrieves a pre loaded class in the list. if the class was reloaded it
     * will return the latest instance of the class.
     *
     * @param <T>
     * @param launchableClass
     * @return
     */
    public <T extends MonoLauncher> T restore(Class launchableClass) {
        prettyLog("RESTORING -> " + launchableClass.getName());
        return (T) this.launcherList.get(launchableClass.getName());
    }

    /**
     * Reloads a pre loaded class.
     *
     * @param <T>
     * @param launchableClass
     * @return
     */
    public <T extends MonoLauncher> T reload(Class launchableClass) {
        try {
            MonoLauncher fxClass = (MonoLauncher) launchableClass.newInstance();
            fxClass.open();
            launcherList.put(fxClass.getClass().getName(), fxClass);
            prettyLog("RELOADING -> " + fxClass.getClass().getName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return (T) launcherList.get(launchableClass.getName());
    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    // Eperimental Features.
    private final HashMap<String, ArrayList<MonoLauncher>> copyClasses = new HashMap<>();

    public <T extends MonoLauncher> T copy(Class launchableClass) {
        String keyClass = launchableClass.getName();
        if (copyClasses.get(keyClass) == null) {
            copyClasses.put(keyClass, new ArrayList<>());
        }
        try {
            MonoLauncher fxClass = (MonoLauncher) launchableClass.newInstance();
            fxClass.open();
            copyClasses.get(keyClass).add(fxClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) copyClasses.get(keyClass).get(copyClasses.get(keyClass).size() - 1);
    }

    /**
     * Basic Call.
     *
     * @param <T>
     * @param launchableClass
     * @return
     */
    public static <T extends MonoLauncher> T load(Class launchableClass) {
        try {
            MonoLauncher fxClass = (MonoLauncher) launchableClass.newInstance();
            fxClass.open();
            return (T) fxClass;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    //--------------------------------------------------------------------------
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.melvin.mono.classmeta;

/**
 * Provides basic information about the class. with increase verbosity for
 * beginners.
 *
 * @author Jhon Melvin
 */
public interface Informable {

    public default String getClassName() {
        return getClass().getSimpleName();
    }

    public default String getClassPackage() {
        return this.getClass().getPackage().getName();
    }

    /**
     *
     * @return
     */
    public default String[] getLaunchData() {
        return new String[]{getClassPackage(), getClassName()};
    }
}

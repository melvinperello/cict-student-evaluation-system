/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jhmvin.orm.lazy;

/**
 *
 * @author Jhon Melvin
 */
public class LazyConfiguration {

    /**
     * Allow the use of the sync function.
     */
    private final boolean allowSync = true;

    private static LazyConfiguration LAZY_INSTANCE;

    public static LazyConfiguration config() {
        if (LAZY_INSTANCE == null) {
            LAZY_INSTANCE = new LazyConfiguration();
        }
        return LAZY_INSTANCE;
    }

    public LazyConfiguration() {
        //
        //driverClass = "org.mariadb.jdbc.Driver";
        //provider = "jdbc:mariadb";
        //host = "127.0.0.1";
        //port = "3306";
        //database = "cictems";
        //username = "root";
        //password = "root";
        //packageName = "app.lazy.models";
    }

    /**
     * Sync Permission
     */
    public boolean isAllowSync() {
        return allowSync;
    }

    private String driverClass;
    private String provider;
    private String host;
    private String port;
    private String database;
    private String username;
    private String password;
    private String packageName;

    public static LazyConfiguration getLAZY_INSTANCE() {
        return LAZY_INSTANCE;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public String getProvider() {
        return provider;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPackageName() {
        return packageName;
    }

    //-------------------------------------------------------------------------
    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

}

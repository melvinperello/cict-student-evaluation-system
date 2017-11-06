/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jhmvin.orm;

import java.util.ArrayList;
import java.util.logging.Level;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author Jhon Melvin
 */
public class MonoHibernate {

    private static MonoHibernate ORM_INSTANCE;

    /**
     * Construct Once
     */
    private MonoHibernate() {

    }

    public static synchronized MonoHibernate getInstance() {
        if (ORM_INSTANCE == null) {
            ORM_INSTANCE = new MonoHibernate();
        }
        return ORM_INSTANCE;
    }

    // connection information
    private String Host;
    private String Port;
    private String DatabaseName;
    private String DatabaseUser;
    private String DatabasePassword;
    /**
     * Extended connection info
     */
    private String connectionDriverClass;// = "org.mariadb.jdbc.Driver";
    private String connectionProvider;// = "jdbc:mariadb";
    // settings
    private String ShowSQL;
    private Level ShowLogs;
    // c3p0 configuration
    private String c3p0_max;// = "5";
    private String c3p0_min;// = "2";
    private String c3p0_timeout;// = "10";
    private String c3p0_max_statements;// = "0";
    private String c3p0_idle_period;// = "0";

    //--------------------------------------------------------------------------
    // Extended setters added 08/20/2017
    public void setConnectionDriverClass(String connectionDriverClass) {
        this.connectionDriverClass = connectionDriverClass;
    }

    public void setConnectionProvider(String connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public void setC3p0_max(Integer c3p0_max) {
        this.c3p0_max = c3p0_max.toString();
    }

    public void setC3p0_min(Integer c3p0_min) {
        this.c3p0_min = c3p0_min.toString();
    }

    public void setC3p0_timeout(Integer c3p0_timeout) {
        this.c3p0_timeout = c3p0_timeout.toString();
    }

    public void setC3p0_max_statements(Integer c3p0_max_statements) {
        this.c3p0_max_statements = c3p0_max_statements.toString();
    }

    public void setC3p0_idle_period(Integer c3p0_idle_period) {
        this.c3p0_idle_period = c3p0_idle_period.toString();
    }

    //-------------------------------------------------------------------------
    // Mapping Files
    private String MappingsLocation;
    private final ArrayList<String> ModelMappings = new ArrayList<>();

    // required setters for connection
    public void setHost(String Host) {
        this.Host = Host;
    }

    public void setPort(Integer Port) {
        this.Port = String.valueOf(Port);
    }

    public void setDatabaseName(String DatabaseName) {
        this.DatabaseName = DatabaseName;
    }

    public void setDatabaseUser(String DatabaseUser) {
        this.DatabaseUser = DatabaseUser;
    }

    public void setDatabasePassword(String DatabasePassword) {
        this.DatabasePassword = DatabasePassword;
    }

    public void setShowSQL(boolean ShowSQL) {
        this.ShowSQL = ShowSQL ? "true" : "false";
    }

    public void setShowLogs(boolean ShowLogs) {
        this.ShowLogs = ShowLogs ? Level.ALL : Level.OFF;
    }

    // mapping
    public void setMappingsLocation(String MappingsLocation) {
        this.MappingsLocation = MappingsLocation;
    }

    public ArrayList<String> getModelMappings() {
        return ModelMappings;
    }

    public void addMappings(String MappingName) {
        this.ModelMappings.add(MappingName);
    }

    // Create Configuration Information
    private final String MAP_EXTENSION = ".hbm.xml";
    private Configuration DatabaseConfiguration;
    private SessionFactory sessionFactory;
    // state indicator
    private boolean isStarted = false;

    /**
     * Tells whether the ORM is already launched.
     *
     * @return
     */
    public boolean isStarted() {
        return isStarted;
    }

    private void configure() {
        // initialize
        this.DatabaseConfiguration = new Configuration();
        // connection
        this.DatabaseConfiguration.setProperty("hibernate.connection.driver_class", this.connectionDriverClass);
        this.DatabaseConfiguration.setProperty("hibernate.connection.url", connectionProvider + "://" + this.Host + ":" + this.Port + "/" + this.DatabaseName + "");
        this.DatabaseConfiguration.setProperty("connection.autocommit", "false");
        this.DatabaseConfiguration.setProperty("hibernate.connection.username", this.DatabaseUser);
        this.DatabaseConfiguration.setProperty("hibernate.connection.password", this.DatabasePassword);
        this.DatabaseConfiguration.setProperty("hibernate.show_sql", this.ShowSQL);

        /**
         * c3p0 Extended features.
         */
        // hibernate.c3p0.min_size – Minimum number of JDBC connections in the pool. Hibernate default: 1
        this.DatabaseConfiguration.setProperty("hibernate.c3p0.min_size", this.c3p0_min);
        // hibernate.c3p0.max_size – Maximum number of JDBC connections in the pool. Hibernate default: 100
        this.DatabaseConfiguration.setProperty("hibernate.c3p0.max_size", this.c3p0_max);
        // When an idle connection is removed from the pool (in second). Hibernate default: 0, never expire.
        this.DatabaseConfiguration.setProperty("hibernate.c3p0.timeout", this.c3p0_timeout);
        // Number of prepared statements will be cached. Increase performance. Hibernate default: 0 , caching is disable.
        this.DatabaseConfiguration.setProperty("hibernate.c3p0.max_statements", this.c3p0_max_statements);
        // idle time in seconds before a connection is automatically validated. Hibernate default: 0
        this.DatabaseConfiguration.setProperty("hibernate.c3p0.idle_test_period", this.c3p0_idle_period);

        // logging
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(this.ShowLogs);
    }

    private void build() {
        this.ModelMappings.forEach(map -> {
            this.DatabaseConfiguration.addResource(this.MappingsLocation + map + this.MAP_EXTENSION);
        });

        /**
         * Finalizes build.
         */
        reconnect();
    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    private void logs(Object message) {
        System.out.println("@MonoHibernate: " + message.toString());
    }

    /**
     * Starts the hibernate engine.
     */
    public void connect() {
        if (!this.isStarted) {
            this.configure();
            this.build();
            this.isStarted = true;
        } else {
            logs("Failed to connect Hibernate Engine already started.");
        }
    }

    /**
     * Please use openSession instead.
     *
     * @return
     * @throws HibernateException
     * @deprecated
     */
    public Session session() {
//        Session session = sessionFactory.openSession();
//        if (!session.isConnected()) {
//            this.reconnect();
//        }
//        return session;
        return this.openSession();
    }

    /**
     * Creates new session.
     *
     * @return a new Localized Session.
     */
    public Session openSession() {
        Session session;
        try {
            session = sessionFactory.openSession();
        } catch (HibernateException e) {
            session = null;
            logs("Unable to create new session.");
        }
        return session;
    }

    /**
     * Rebuilds the session factory.
     */
    private void reconnect() {
        try {
            this.sessionFactory = DatabaseConfiguration.buildSessionFactory();
        } catch (HibernateException e) {
            logs("Failed to build session factory.");
        }
    }

    /**
     * Safely disconnects and shutdown hibernate engine.
     *
     * @return
     */
    public boolean disconnect() {
        boolean disconnected = false;
        if (this.sessionFactory.isOpen()) {
            try {
                this.sessionFactory.close();
                disconnected = true;
            } catch (HibernateException e) {
                logs("Failed to close Hibernate Session Factory . . .");
            }
        }
        return disconnected;
    }

    /**
     * Safely disconnects and clear cache of hibernate engine.
     */
    public void shutdown() {
        if (this.sessionFactory.isOpen()) {
            this.sessionFactory.getCache().evictAllRegions();
            disconnect();
        }
    }

    /**
     * Please use disconnect() instead of this. Unsafe closing, may result to
     * unexpected errors.
     *
     */
    public void closeSessionFactory() {
//        if (this.sessionFactory.isOpen()) {
//            this.sessionFactory.close();
//        }
        this.disconnect();
    }

    /**
     * ORM Functions.
     */
    /**
     *
     * @param model
     * @return
     */
    public Searcher newSearch(MonoModels model) {
        return new Searcher(model);
    }

    public MonoModels createModel(Class clazz) {
        return new MonoModels(clazz);
    }

    public ServerTime getServerTime() {
        return new ServerTime(this.session());
    }

    public Projector projection(Searcher search) {
        return new Projector(search);
    }

    //--------------------------------------------------------------------------
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}

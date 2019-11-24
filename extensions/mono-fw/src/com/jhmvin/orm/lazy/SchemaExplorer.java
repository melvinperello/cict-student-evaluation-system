/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jhmvin.orm.lazy;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jhon Melvin
 */
public class SchemaExplorer {

    private void logs(Object message) {
        System.out.println(message.toString());
    }

    /**
     * Database Connection.
     */
    private String driverClass;
    private String provider;
    private String host;
    private String port;
    private String database;
    private String username;
    private String password;
    private Connection con;

    /**
     * Connection Methods
     */
    private String getConnectionUrl() {
        return provider + "://" + host + ":" + port + "/" + database;
    }

    private void connect() {
        try {
            Class.forName(driverClass);

            con = DriverManager.getConnection(getConnectionUrl(), username, password);
        } catch (ClassNotFoundException | SQLException e) {

        }
    }

    /**
     * Class variables and methods.
     */
    private String tableName = "";

    public static SchemaExplorer tables() {
        return new SchemaExplorer();
    }

    private SchemaExplorer() {
        // configure before connection;
        LazyConfiguration config = LazyConfiguration.config();
        driverClass = config.getDriverClass();
        provider = config.getProvider();
        host = config.getHost();
        port = config.getPort();
        database = config.getDatabase();
        username = config.getUsername();
        password = config.getPassword();
        // connect
        connect();
    }

    public ArrayList<String> getTableNames() {
        ArrayList<String> tableNames = new ArrayList<>();
        try {
            DatabaseMetaData md = con.getMetaData();
            ResultSet rs = md.getTables(null, null, "%", null);
            while (rs.next()) {
                String tbl_name = rs.getString("TABLE_NAME");
                tableNames.add(tbl_name);
            }
        } catch (SQLException ex) {

        }
        return tableNames;
    }

    /**
     * Get the tables where keys are exported.
     *
     * @return set of keys.
     */
    public ArrayList<KeySet> getExportedKeys() {
        ArrayList<KeySet> exported_keys = new ArrayList<>();
        try {
            DatabaseMetaData columnmeta = con.getMetaData();
            ResultSet columnSet = columnmeta.getExportedKeys(null, null, this.tableName);
            while (columnSet.next()) {
                String pk_table = columnSet.getString("PKTABLE_NAME");
                String pk_column = columnSet.getString("PKCOLUMN_NAME");
                String fk_table = columnSet.getString("FKTABLE_NAME");
                String fk_column = columnSet.getString("FKCOLUMN_NAME");

                KeySet keys = new KeySet();
                keys.setPrimaryTable(pk_table);
                keys.setPrimaryColumn(pk_column);
                keys.setForeignTable(fk_table);
                keys.setForeignColumn(fk_column);

                exported_keys.add(keys);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return exported_keys;
    }

    public ArrayList<KeySet> getImportedKeys() {
        ArrayList<KeySet> imported_keys = new ArrayList<>();
        try {
            DatabaseMetaData columnmeta = con.getMetaData();
            ResultSet columnSet = columnmeta.getImportedKeys(null, null, this.tableName);
            while (columnSet.next()) {
                String pk_table = columnSet.getString("PKTABLE_NAME");
                String pk_column = columnSet.getString("PKCOLUMN_NAME");
                String fk_table = columnSet.getString("FKTABLE_NAME");
                String fk_column = columnSet.getString("FKCOLUMN_NAME");

                KeySet keys = new KeySet();
                keys.setPrimaryTable(pk_table);
                keys.setPrimaryColumn(pk_column);
                keys.setForeignTable(fk_table);
                keys.setForeignColumn(fk_column);

                imported_keys.add(keys);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return imported_keys;
    }

    public SchemaExplorer select(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public ArrayList<TableColumns> columns() {
        ArrayList<TableColumns> columns = new ArrayList<>();
        try {
            DatabaseMetaData columnmeta = con.getMetaData();
            ResultSet columnSet = columnmeta.getColumns(null, null, this.tableName, null);

            while (columnSet.next()) {
                String columnName = columnSet.getString("COLUMN_NAME");
                String columnDataSize = columnSet.getString("COLUMN_SIZE");
                String columnDataType = columnSet.getString("TYPE_NAME");
                String isNullable = columnSet.getString("IS_NULLABLE");
                String isAutoIncrement = columnSet.getString("IS_AUTOINCREMENT");
                String defaultValue = columnSet.getString("COLUMN_DEF");

                TableColumns tableColumns = new TableColumns();
                tableColumns.setColumnName(columnName);
                tableColumns.setColumnDataType(columnDataType);
                tableColumns.setColumnDataSize(columnDataSize);
                tableColumns.setNullable(isNullable);
                tableColumns.setAutoIncrement(isAutoIncrement);
                tableColumns.setDefaultValue(defaultValue);
                columns.add(tableColumns);
            }
        } catch (SQLException ex) {

        }
        return columns;
    }

}

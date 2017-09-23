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
public class TableColumns {

    public TableColumns() {
        //construct
    }
    /**
     * Basic Column Information
     */
    private String columnName;
    private String columnDataSize;
    private String columnDataType;
    private Boolean nullable;
    private Boolean autoIncrement;
    private String defaultValue;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnDataSize() {
        return columnDataSize;
    }

    public void setColumnDataSize(String columnDataSize) {
        this.columnDataSize = columnDataSize;
    }

    public String getColumnDataType() {
        return columnDataType;
    }

    public void setColumnDataType(String columnDataType) {
        this.columnDataType = columnDataType;
    }

    public Boolean isNullable() {
        return nullable;
    }

    public void setNullable(String nullable) {
        this.nullable = nullable.equalsIgnoreCase("YES") ? true
                : nullable.equalsIgnoreCase("NO") ? false
                : null;
    }

    public Boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(String autoIncrement) {
        this.autoIncrement = autoIncrement.equalsIgnoreCase("YES") ? true
                : autoIncrement.equalsIgnoreCase("NO") ? false
                : null;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

}

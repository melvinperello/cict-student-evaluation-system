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
public class KeySet {

    private String primaryTable;
    private String primaryColumn;
    private String foreignTable;
    private String foreignColumn;

    public KeySet() {
        //
    }

    public String getPrimaryTable() {
        return primaryTable;
    }

    public void setPrimaryTable(String primaryTable) {
        this.primaryTable = primaryTable;
    }

    public String getPrimaryColumn() {
        return primaryColumn;
    }

    public void setPrimaryColumn(String primaryColumn) {
        this.primaryColumn = primaryColumn;
    }

    public String getForeignTable() {
        return foreignTable;
    }

    public void setForeignTable(String foreignTable) {
        this.foreignTable = foreignTable;
    }

    public String getForeignColumn() {
        return foreignColumn;
    }

    public void setForeignColumn(String foreignColumn) {
        this.foreignColumn = foreignColumn;
    }

    @Override
    public String toString() {
        return ("PK: " + primaryColumn + " @ tbl: " + primaryTable + "\tFK: " + foreignColumn + " @ tbl: " + foreignTable);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jhmvin.orm.lazy;

import java.util.HashMap;

/**
 * This class converts the SQL data types into a Java Recognizable data.
 *
 * @since 1.0
 * @author Jhon Melvin
 */
public class MapEquivalence {

    private final HashMap SQL_EQUIVALENCE;
    private final HashMap CHRONO_TYPES;
    /**
     * Packages
     */
    private final String pkgString = "java.lang.String";
    private final String pkgBigDecimal = "java.math.BigDecimal";
    private final String pkgBoolean = "java.lang.Boolean";
    private final String pkgInteger = "java.lang.Integer";
    private final String pkgLong = "java.lang.Long";
    private final String pkgFloat = "java.lang.Float";
    private final String pkgDouble = "java.lang.Double";
    private final String pkgByte = "java.lang.Byte[]";

    public MapEquivalence() {
        SQL_EQUIVALENCE = new HashMap();
        CHRONO_TYPES = new HashMap();
        constructMappings();
        constructChronoMappings();
    }

    private void constructMappings() {
        SQL_EQUIVALENCE.put("CHARACTER", pkgString);
        SQL_EQUIVALENCE.put("VARCHAR", pkgString);
        SQL_EQUIVALENCE.put("LONGVARCHAR", pkgString);
        SQL_EQUIVALENCE.put("NUMERIC", pkgBigDecimal);
        SQL_EQUIVALENCE.put("DECIMAL", pkgBigDecimal);
        SQL_EQUIVALENCE.put("BIT", pkgBoolean);
        SQL_EQUIVALENCE.put("TINYINT", pkgInteger);
        SQL_EQUIVALENCE.put("SMALLINT", pkgInteger);
        SQL_EQUIVALENCE.put("INTEGER", pkgInteger);
        SQL_EQUIVALENCE.put("INT", pkgInteger);
        SQL_EQUIVALENCE.put("BIGINT", pkgLong);
        SQL_EQUIVALENCE.put("REAL", pkgLong);
        SQL_EQUIVALENCE.put("FLOAT", pkgDouble);
        SQL_EQUIVALENCE.put("DOUBLE PRECISION", pkgDouble);
        SQL_EQUIVALENCE.put("DOUBLE", pkgDouble);
        SQL_EQUIVALENCE.put("BINARY", pkgByte);
        SQL_EQUIVALENCE.put("VARBINARY", pkgByte);
        SQL_EQUIVALENCE.put("LONGVARBINARY", pkgByte);

    }

    private void constructChronoMappings() {
        CHRONO_TYPES.put("DATE", "java.sql.Date");
        CHRONO_TYPES.put("TIME", "java.sql.Time");
        CHRONO_TYPES.put("TIMESTAMP", "java.sql.Timestamp");
        CHRONO_TYPES.put("DATETIME", "java.util.Date");
        SQL_EQUIVALENCE.putAll(CHRONO_TYPES);
    }

    public String map(String sql_data) {
        Object data = SQL_EQUIVALENCE.get(sql_data);
        if (data == null) {
            System.err.println(sql_data);
            return "";
        }
        return data.toString();
    }

    public boolean isChrono(String sqlType) {
        return CHRONO_TYPES.get(sqlType) != null ? true : false;
    }
}

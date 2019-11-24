/**
 * General Notice.
 * Getter and Setters should follow the naming of the variables.
 * The ORM maps the variable name from Java not the name of the field from the db.
 *
 * For safest option.
 * The variables in Java should have the same name in the columns from the db.
 */
package com.jhmvin.orm.lazy;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * This class was discontinued due to the conventions from the project.
 * <project>CICT EMS</project>
 *
 * @author Jhon Melvin
 */
@Deprecated
public class MappingWizard {

//    public static void main(String[] args) {
//        SchemaExplorer.tables().getTableNames().forEach(tables -> {
//            MappingWizard aom = new MappingWizard();
//            aom.setPackageName("lazy.db");
//            aom.setDatabaseName("cictems");
//            aom.setTableName(tables);
//            aom.makeJava();
//            aom.makeHbm();
//        });
//
//    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    /**
     * The Master Text File.
     */
    private String MASTER_TEXT = "";
    private String MASTER_XML_TEXT = "";

    private void writeln(String text) {
        this.MASTER_TEXT += (text + "\n");
    }

    private void writehb(String text) {
        this.MASTER_XML_TEXT += (text + "\n");
    }

    private String packageName = "lazy.db";
    private String saveDirectory = "src/lazy/db/";

    private String tableName = "grade";
    private String databaseName = "cictems";

    private String className;

    public MappingWizard() {
        //
    }

    public void makeJava() {
        headers();
        imports();
        prologue();
        declarations();
        contents();
        epilogue();
        writeJava();
    }

    private void headers() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
        java.util.Date now = new java.util.Date();

        writeln("// SQL_db: " + this.databaseName);
        writeln("// SQL_table: " + this.tableName);
        writeln("// Mono Models");
        writeln("// Monosync Framewrok v1.0.x");
        writeln("// Created: " + dateFormat.format(now));
        writeln("// Generated using LazyMono");
        writeln("// This code is computer generated, do not modify");
        writeln("// Author: Jhon Melvin Nieto Perello");
        writeln("// Contact: jhmvinperello@gmail.com");
        writeln("//");
        writeln("// The Framework uses Hibernate as its ORM");
        writeln("// For more information about Hibernate visit hibernate.org");

    }

    private void imports() {
        writeln("");
        writeln("package " + this.packageName + ";");
        writeln("");
        writeln("import javax.persistence.Column;");
        writeln("import javax.persistence.Entity;");
        writeln("import javax.persistence.GeneratedValue;");
        writeln("import static javax.persistence.GenerationType.IDENTITY;");
        writeln("import javax.persistence.Id;");
        writeln("import javax.persistence.Table;");

    }

    private void prologue() {
        writeln("");
        writeln("");
        writeln("@Entity");
        writeln("@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)");
        writeln("@Table(name = \"" + this.tableName + "\", catalog = \"" + this.databaseName + "\")");

        this.className = MappingWizard.beanNamer("", this.tableName, "Mapping");

        writeln("public class " + className + " implements java.io.Serializable {");
    }

    private void declarations() {
        writeln("");
        writeln("");
        SchemaExplorer
                .tables()
                .select(this.tableName)
                .columns()
                .forEach(column -> {
                    MapEquivalence me = new MapEquivalence();
                    String type = me.map(column.getColumnDataType());

                    String name = "field" + beanNamer("", column.getColumnName(), "");

                    String declare = "private " + type + " " + name + ";";
                    writeln(declare);
                });
    }

    private void contents() {
        SchemaExplorer
                .tables()
                .select(this.tableName)
                .columns()
                .forEach(column -> {
                    createGetter(column);
                    createSetter(column);
                });
    }

    /**
     *
     * @param col
     */
    private void createGetter(TableColumns col) {
        MapEquivalence me = new MapEquivalence();
        writeln("");
        /**
         * Annotations
         */
        // for primary
        if (col.isAutoIncrement()) {
            writeln("@Id");
            writeln("@GeneratedValue(strategy = IDENTITY)");
        }
        // basic
        writeln("@Column(name = \"" + col.getColumnName() + "\", nullable = " + col.isNullable().toString() + ", length = " + col.getColumnDataSize() + ")");
        // time based
        if (me.isChrono(col.getColumnDataType())) {
            writeln("@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)");
        }

        String type = me.map(col.getColumnDataType());
        String functionName = getterName(col.getColumnName());

        String returnString = "\treturn this.field" + beanNamer("", col.getColumnName(), "");
        writeln("public " + type + " " + functionName + "() {");
        writeln(returnString + ";");
        writeln("}");
        writeln("");
    }

    private void createSetter(TableColumns col) {
        MapEquivalence me = new MapEquivalence();
        String type = me.map(col.getColumnDataType());

        String functionName = setterName(col.getColumnName());
        String fieldName = "field" + beanNamer("", col.getColumnName(), "");
        writeln("public void " + functionName + "(" + type + " " + fieldName + ") {");
        writeln("\tthis." + fieldName + " = " + fieldName + ";");
        writeln("}");
    }

    private void epilogue() {
        writeln("");
        writeln("}");
    }

    private void writeJava() {
        String file = saveDirectory + this.className + ".java";
        TextWriter writer = new TextWriter(file);
        writer.forRewrite();
        writer.write(this.MASTER_TEXT);
        writer.close();
    }

    //--------------------------------------------------------------------------
    /**
     * XML Hibernate files.
     */
    private void makeHbm() {
        hbmHeader();
        hbmContent();
        hbmFooter();
        writeHbm();
    }

    private void hbmHeader() {
        writehb("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        writehb("<!DOCTYPE hibernate-mapping PUBLIC \"-//Hibernate/Hibernate Mapping DTD 3.0//EN\"");
        writehb("\"http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd\">");
        writehb("<hibernate-mapping>");

        String fullClassName = this.packageName + "." + this.className;
        writehb("<class name=\"" + fullClassName + "\" table=\"" + this.tableName + "\" catalog=\"" + this.databaseName + "\" dynamic-update=\"true\" dynamic-insert=\"true\">");
    }

    private void hbmContent() {
        SchemaExplorer
                .tables()
                .select(this.tableName)
                .columns()
                .forEach(column -> {
                    createProperties(column);
                });
    }

    private void createProperties(TableColumns col) {
        MapEquivalence me = new MapEquivalence();
        String type = me.map(col.getColumnDataType());
        String fieldName = "field" + beanNamer("", col.getColumnName(), "");

        if (col.isAutoIncrement()) {
            writehb("<id name=\"" + fieldName + "\" type=\"" + type + "\">");
            writehb("<column name=\"" + col.getColumnName() + "\"/>");
            writehb("<generator class=\"identity\"/>");
            writehb("</id>");
        } else {
            writehb("<property name=\"" + fieldName + "\" type=\"" + type + "\">");
            writehb("<column name=\"" + col.getColumnName() + "\" length=\"" + col.getColumnDataSize() + "\"/>");
            writehb("</property>");
        }

    }

    private void hbmFooter() {
        writehb("</class>");
        writehb("</hibernate-mapping>");
    }

    private void writeHbm() {
        String file = saveDirectory + this.className + ".hbm.xml";
        TextWriter writer = new TextWriter(file);
        writer.forRewrite();
        writer.write(this.MASTER_XML_TEXT);
        writer.close();
    }

    /**
     * Statics.
     *
     * @param column
     * @return
     */
    private static String setterName(String column) {
        return MappingWizard.beanNamer("setField", column, "");
    }

    private static String getterName(String column) {
        return MappingWizard.beanNamer("getField", column, "");
    }

    private static String beanNamer(String prefix, String column, String suffix) {
        column = column.toLowerCase(Locale.ENGLISH);
        String[] words = column.split("_");
        // split words
        String beanName = prefix;
        for (String word : words) {
            // split letters
            boolean first = true;
            String temp_word = "";
            for (Character c : word.toCharArray()) {
                if (first) {
                    temp_word += c.toString().toUpperCase();
                    first = false;
                } else {
                    temp_word += c.toString().toLowerCase();
                }
            }
            beanName += temp_word;
        }

        return beanName + suffix;
    }

}

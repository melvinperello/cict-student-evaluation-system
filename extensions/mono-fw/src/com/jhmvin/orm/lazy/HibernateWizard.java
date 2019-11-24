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
/**
 * General Notice. Getter and Setters should follow the naming of the variables.
 * The ORM maps the variable name from Java not the name of the field from the
 * db.
 *
 * For safest option. The variables in Java should have the same name in the
 * columns from the db.
 */
import com.jhmvin.Mono;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 *
 * @author Jhon Melvin
 */
public class HibernateWizard {

    private static int tableCursor;

    public static void sync() {

        if (!LazyConfiguration.config().isAllowSync()) {
            // if sync is disabled
            System.err.println("THIS FEATURE IS DISABLED.");
            return;
        }

        /**
         * Generated files configuration.
         */
        // lazy inheritance.
        String app_package = LazyConfiguration.config().getPackageName();

        // on package heeader and hbm.xml
        String model_package = app_package;
        // location to be save must be relative to model_package
        String model_directory = "src/" + app_package.replace('.', '/') + "/";
        // used in add maps same as package
        String model_locations = app_package.replace('.', '/') + "/";

        String host = LazyConfiguration.config().getHost();
        String port = LazyConfiguration.config().getPort();
        String db = LazyConfiguration.config().getDatabase();
        // end of config

        tableCursor = 1;
        out("For Exclusive Use of Monosync Developers Only.");
        out("Syncing models from database . . .");
        int tableCount = SchemaExplorer.tables().getTableNames().size();
        out(tableCount + " table(s) were found.");
        //

        //
        SchemaExplorer.tables().getTableNames().forEach(tables -> {
            out("Syncing table: " + tables + " [" + tableCursor + "/" + tableCount + "]");
            HibernateWizard aom = new HibernateWizard();
            aom.setPackageName(model_package);
            aom.setSaveDirectory(model_directory);
            aom.setDatabaseName(db);
            aom.setTableName(tables);
            aom.makeJava();
            aom.makeHbm();
            tableCursor++;
        });
        // create database interface
        out("Creating Database Interface.");
        HibernateWizard aom = new HibernateWizard();
        aom.setPackageName(model_package);
        aom.setSaveDirectory(model_directory);
        aom.setDatabaseName(db);
        aom.setHost(host);
        aom.setPort(port);
        aom.setDriverClass(LazyConfiguration.config().getDriverClass());
        aom.setProvider(LazyConfiguration.config().getProvider());
        aom.setMapLocations(model_locations);
        aom.makeDatabase();
        out("Database Interface Created. ORM Framewrok Ready.");
        out("Syncing Complete.");

        ///
        aom.col_make();
        out("Database Table Columns Successfully Created");

        //
        aom.make_factory();
        out("Map Factory Created");
        out("");
        out("MonoSynchronized Succeessfully");
    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    /**
     * Additional Added 08/20/2017
     */
    private String driverClass;
    private String provider;

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    /**
     * Class Instance.
     */
    private String packageName = "";
    private String saveDirectory = "";
    private String mapLocations = "";
    private String tableName = "";
    private String databaseName = "";
    private String host = "";
    private String port = "";

    public void setPort(String port) {
        this.port = port;
    }

    private static void out(Object message) {
        System.out.println(message);
    }

    /**
     * SEtters
     *
     * @param packageName
     */
    private void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    private void setTableName(String tableName) {
        this.tableName = tableName;
    }

    private void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    private void setSaveDirectory(String saveDirectory) {
        this.saveDirectory = saveDirectory;
    }

    private void setMapLocations(String mapLocations) {
        this.mapLocations = mapLocations;
    }

    private void setHost(String host) {
        this.host = host;
    }

    /**
     * The Master Text Files.
     */
    private String MASTER_TEXT = "";
    private String MASTER_XML_TEXT = "";
    private String MASTER_DB_TEXT = "";

    private void writejv(String text) {
        this.MASTER_TEXT += (text + "\n");
    }

    private void writehb(String text) {
        this.MASTER_XML_TEXT += (text + "\n");
    }

    private void writedb(String text) {
        this.MASTER_DB_TEXT += (text + "\n");
    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    /**
     * Added 08/12/2017.
     */
    private String MASTER_COL_TEXT = "";

    private void col_make() {
        ArrayList<String> tables = SchemaExplorer.tables().getTableNames();
        out("Creating Database Tables Columns . . .");
        int x = 1;
        for (String table : tables) {
            if (x < 10) {
                out("[ 0" + x + " ] Generating Table Column: " + table);
            } else {
                out("[ " + x + " ] Generating Table Column: " + table);
            }

            ArrayList<TableColumns> columns = SchemaExplorer
                    .tables()
                    .select(table)
                    .columns();
            // date
            //SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
            //java.util.Date now = new java.util.Date();

            String col_header = "// SQL_db: " + this.databaseName + "\n"
                    + "// SQL_table: " + table + "\n"
                    + "// Mono Models\n"
                    + "// Monosync Framewrok " + Mono.VERSION + "\n"
                    //+ "// Created: " + dateFormat.format(now) + "\n"
                    + "// Generated using LazyMono\n"
                    + "// This code is computer generated, do not modify\n"
                    + "// Author: Jhon Melvin Nieto Perello\n"
                    + "// Contact: jhmvinperello@gmail.com\n"
                    + "//\n"
                    + "// The Framework uses Hibernate as its ORM\n"
                    + "// For more information about Hibernate visit hibernate.org"
                    + "\n"
                    + "package " + this.packageName + ";\n"
                    + "\n"
                    + "/**\n"
                    + " *\n"
                    + " * @author Jhon Melvin\n"
                    + " */\n"
                    + "public class Tbl_" + table + " {";
            col_writes(col_header);
            for (TableColumns column : columns) {
                String col_content = "\npublic final String " + column.getColumnName() + " = \"" + column.getColumnName() + "\";";
                col_writes(col_content);
            }
            /**
             * Footer
             */

            col_writes("\n}");
            col_save(table);
            MASTER_COL_TEXT = "";
            x++;
        }

        /**
         * Management Class.
         */
        col_manager();
    }

    private void col_writes(String writable) {
        this.MASTER_COL_TEXT += writable;
    }

    private void col_save(String table) {
        String file = saveDirectory + "Tbl_" + table + ".java";
        TextWriter writer = new TextWriter(file);
        writer.forRewrite();
        writer.write(this.MASTER_COL_TEXT);
        writer.close();
    }

    private void col_manager() {
        String class_header = "package " + this.packageName + ";\n"
                + "\n"
                + "/**\n"
                + " *\n"
                + " * @author Jhon Melvin\n"
                + " */\n"
                + "public class DB {";

        String content = "";
        ArrayList<String> tables = SchemaExplorer.tables().getTableNames();
        for (String table : tables) {
            content += "private final static Tbl_" + table + " " + table + " = new Tbl_" + table + "();\n";
            content += "    public static Tbl_" + table + " " + table + "() {\n"
                    + "        return " + table + ";\n"
                    + "    }";
        }

        String class_footer = "\n}";

        String WRITABLE_TEXT = class_header + content + class_footer;
        String file = saveDirectory + "DB.java";
        TextWriter writer = new TextWriter(file);
        writer.forRewrite();
        writer.write(WRITABLE_TEXT);
        writer.close();
    }

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    /**
     * Added 08/16/2014.
     */
    private String MASTER_FACTORY_TEXT = "";

    private void make_factory() {
        factory_add_text("package " + this.packageName + ";\n"
                + "\n"
                + "/**\n"
                + " *\n"
                + " * @author Jhon Melvin\n"
                + " */\n"
                + "public class MapFactory {\n"
                + "\n"
                + "    public MapFactory() {\n"
                + "\n"
                + "    }\n"
                + "    private static MapFactory MAP_FACTORY_INSTANCE;\n"
                + "\n"
                + "    public static MapFactory map() {\n"
                + "        if (MAP_FACTORY_INSTANCE == null) {\n"
                + "            MAP_FACTORY_INSTANCE = new MapFactory();\n"
                + "        }\n"
                + "        return MAP_FACTORY_INSTANCE;\n"
                + "    }");

        ArrayList<String> tables = SchemaExplorer.tables().getTableNames();
        for (String table : tables) {
            String class_name = beanNamer("", table, "Mapping");

            factory_add_text("public " + class_name + " " + table + "() {");
            factory_add_text("return new " + class_name + "();");
            factory_add_text("}");
        }

        factory_add_text("}");

        // write
        String file = saveDirectory + "MapFactory.java";
        TextWriter writer = new TextWriter(file);
        writer.forRewrite();
        writer.write(MASTER_FACTORY_TEXT);
        writer.close();
    }

    private void factory_add_text(String text) {
        MASTER_FACTORY_TEXT += (text + "\n");
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    private String className;

    public HibernateWizard() {
        //
    }

    private void makeJava() {
        headers();
        imports();
        prologue();
        declarations();
        contents();
        //----------------
        createClone();
        //----------------
        epilogue();
        writeJava();
    }

    private void headers() {
        //SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
        //java.util.Date now = new java.util.Date();

        writejv("// SQL_db: " + this.databaseName);
        writejv("// SQL_table: " + this.tableName);
        writejv("// Mono Models");
        writejv("// Monosync Framewrok " + Mono.VERSION);
        //writejv("// Created: " + dateFormat.format(now));
        writejv("// Generated using LazyMono");
        writejv("// This code is computer generated, do not modify");
        writejv("// Author: Jhon Melvin Nieto Perello");
        writejv("// Contact: jhmvinperello@gmail.com");
        writejv("//");
        writejv("// The Framework uses Hibernate as its ORM");
        writejv("// For more information about Hibernate visit hibernate.org");

    }

    private void imports() {
        writejv("");
        writejv("package " + this.packageName + ";");
        writejv("");
        writejv("import javax.persistence.Column;");
        writejv("import javax.persistence.Entity;");
        writejv("import javax.persistence.GeneratedValue;");
        writejv("import static javax.persistence.GenerationType.IDENTITY;");
        writejv("import javax.persistence.Id;");
        writejv("import javax.persistence.Table;");

    }

    private void prologue() {
        writejv("");
        writejv("");
        writejv("@Entity");
        writejv("@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)");
        writejv("@Table(name = \"" + this.tableName + "\", catalog = \"" + this.databaseName + "\")");

        this.className = HibernateWizard.beanNamer("", this.tableName, "Mapping");

        writejv("public class " + className + " implements java.io.Serializable, com.jhmvin.orm.MonoMapping {");
    }

    private void declarations() {
        writejv("");
        writejv("");
        SchemaExplorer
                .tables()
                .select(this.tableName)
                .columns()
                .forEach(column -> {
                    MapEquivalence me = new MapEquivalence();
                    String type = me.map(column.getColumnDataType());

                    //String name = "field" + beanNamer("", column.getColumnName(), "");
                    String declare = "private " + type + " " + column.getColumnName() + ";";
                    writejv(declare);
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
        writejv("");
        /**
         * Annotations
         */
        // for primary
        if (col.isAutoIncrement()) {
            writejv("@Id");
            writejv("@GeneratedValue(strategy = IDENTITY)");
        }
        // basic
        writejv("@Column(name = \"" + col.getColumnName() + "\", nullable = " + col.isNullable().toString() + ", length = " + col.getColumnDataSize() + ")");
        // time based
        if (me.isChrono(col.getColumnDataType())) {
            writejv("@javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)");
        }

        String type = me.map(col.getColumnDataType());
        String functionName = getterName(col.getColumnName());

        String returnString = "\treturn this." + col.getColumnName();
        writejv("public " + type + " " + functionName + "() {");
        writejv(returnString + ";");
        writejv("}");
        writejv("");
    }

    private void createSetter(TableColumns col) {
        MapEquivalence me = new MapEquivalence();
        String type = me.map(col.getColumnDataType());

        String functionName = setterName(col.getColumnName());
        String fieldName = "field" + beanNamer("", col.getColumnName(), "");
        writejv("public void " + functionName + "(" + type + " " + fieldName + ") {");
        writejv("\tthis." + col.getColumnName() + " = " + fieldName + ";");
        writejv("}");
    }

    private void createClone() {
        String varName = "copyMe";
        writejv("");
        writejv("@Override");
        writejv("public " + this.className + " copy() {");
        //
        writejv(this.className + " " + varName + " = new " + this.className + "();");
        //----------------------------------------------------------------------
        // fill up the values
        SchemaExplorer
                .tables()
                .select(this.tableName)
                .columns()
                .forEach(column -> {
                    String colName = column.getColumnName();
                    if (column.isAutoIncrement()) {
                        writejv("        /**\n"
                                + "         * A.I. Field Do Not Copy.\n"
                                + "         *\n"
                                + "         * " + varName + "." + colName + " = this." + colName + ";\n"
                                + "         */");
                        return; // skip auto keys
                    }

                    writejv(varName + "." + colName + " = " + "this." + colName + ";");
                });
        //----------------------------------------------------------------------
        // return the created object
        writejv("return " + varName + ";");
        // end this function
        writejv("}");
    }

    private void epilogue() {
        writejv("");
        writejv("}");
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
        //String fieldName = "field" + beanNamer("", col.getColumnName(), "");

        if (col.isAutoIncrement()) {
            writehb("<id name=\"" + col.getColumnName() + "\" type=\"" + type + "\">");
            writehb("<column name=\"" + col.getColumnName() + "\"/>");
            writehb("<generator class=\"identity\"/>");
            writehb("</id>");
        } else {
            writehb("<property name=\"" + col.getColumnName() + "\" type=\"" + type + "\">");
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

    //--------------------------------------------------------------------------
    /**
     * Write Database.java here.
     */
    private void makeDatabase() {
        db_headers();
        db_constructor();
        db_static();
        db_declare();
        db_setup();
        db_getter();
        db_footer();
        db_write();
    }

    private void db_headers() {
        writedb("");
        writedb("package " + this.packageName + ";");
        writedb("");
        writedb("import com.jhmvin.Mono;");
        writedb("import com.jhmvin.orm.MonoModels;");
        writedb("");
        writedb("");
        writedb("public class Database {");
    }

    private void db_constructor() {
        writedb("// construct");
        writedb("private Database() {setup();}");
    }

    private void db_static() {
        writedb("private static Database DATABASE_INSTANCE;");
        writedb("    public static Database connect() {\n"
                + "        if (DATABASE_INSTANCE == null) {\n"
                + "            DATABASE_INSTANCE = new Database();\n"
                + "        }\n"
                + "        return DATABASE_INSTANCE;\n"
                + "    }");

    }

    private void db_declare() {
        SchemaExplorer.tables().getTableNames().forEach(tables -> {
            writedb("private MonoModels tbl_" + tables + ";");
        });
    }

    private void db_setup() {
        writedb("private void setup() {");
        writedb("Mono.orm().setConnectionDriverClass(\"" + this.driverClass + "\");");
        writedb("Mono.orm().setConnectionProvider(\"" + this.provider + "\");");
        writedb("Mono.orm().setHost(\"" + this.host + "\");");
        writedb("Mono.orm().setPort(3306);");
        writedb("Mono.orm().setDatabaseName(\"" + this.databaseName + "\");");
        writedb("Mono.orm().setDatabaseUser(\"root\");");
        writedb("Mono.orm().setDatabasePassword(\"root\");");
        writedb("Mono.orm().setShowSQL(false);");
        writedb("Mono.orm().setShowLogs(false);");
        writedb("// c3p0 settings");
        writedb("Mono.orm().setC3p0_max(5);");
        writedb("Mono.orm().setC3p0_min(3);");
//        writedb("Mono.orm().setC3p0_timeout(300);");
//        writedb("Mono.orm().setC3p0_max_statements(0);");
//        writedb("Mono.orm().setC3p0_idle_period(0);");
        writedb("// mapping information");
        writedb("Mono.orm().setMappingsLocation(\"" + this.mapLocations + "\");");

        SchemaExplorer.tables().getTableNames().forEach(tables -> {
            String mapName = beanNamer("", tables, "Mapping");
            writedb("Mono.orm().addMappings(\"" + mapName + "\");");
            writedb("this.tbl_" + tables + " = Mono.orm().createModel(" + mapName + ".class);");
        });

        writedb("Mono.orm().connect();");
        writedb("}");
    }

    private void db_getter() {
        SchemaExplorer.tables().getTableNames().forEach(tables -> {
            writedb("public MonoModels " + tables + "() {");
            writedb("return tbl_" + tables + ";");
            writedb("}");
        });
    }

    private void db_footer() {
        writedb("}");

    }

    private void db_write() {
        String file = saveDirectory + "Database" + ".java";
        TextWriter writer = new TextWriter(file);
        writer.forRewrite();
        writer.write(this.MASTER_DB_TEXT);
        writer.close();
    }

    /**
     * Statics.
     *
     * @param column
     * @return
     */
    private static String setterName(String column) {
        return "set" + beanFormat(column);
    }

    private static String getterName(String column) {
        return "get" + beanFormat(column);
    }

    private static String beanFormat(String name) {
        String temp_name = "";
        boolean first = true;
        for (Character c : name.toCharArray()) {
            if (first) {
                temp_name += c.toString().toUpperCase(Locale.ENGLISH);
                first = false;
            } else {
                temp_name += c.toString();
            }
        }
        return temp_name;
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

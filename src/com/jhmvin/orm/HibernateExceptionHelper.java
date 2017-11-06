/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jhmvin.orm;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.hibernate.HibernateException;
import org.hibernate.InvalidMappingException;
import org.hibernate.boot.MappingNotFoundException;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author Jhon Melvin
 */
public class HibernateExceptionHelper {

    public static void errorChecker(String sourceOperation, Exception he) {
        header();
        if (he instanceof MappingNotFoundException) {
            mapNotFound(sourceOperation, he);
        } else if (he instanceof InvalidMappingException) {
            invalidMap(sourceOperation, he);
        } else if (he instanceof ConstraintViolationException) {
            constraintViolation(sourceOperation, he);
        } else {
            genericError(sourceOperation, he);
        }
        footer();
    }

    private static void header() {
        log("<help>");
        log("");
        log("");
        log("");
        log("+ Iris (ORM Error Helper) + ");
        log("");
        log("- Hi There! Im here to guide you with your errors.");
        log("- Here's a description of your error:");
    }

    private static void footer() {
        log("");
        log("+ Happy Coding +");
        log("");
        log("");
        log("</help>");
    }

    private static void log(String msg) {
        System.err.println(msg);
    }

    private static void mapNotFound(String sourceOperation, Exception he) {
        MappingNotFoundException map_not_found = (MappingNotFoundException) he;
        log("\t- Method Source: " + sourceOperation);
        log("\t- Error Description: Mapping Resource Not Found.");
        log("\t- Error Message: " + map_not_found.getMessage());
        log("\t- Solutions:");
        log("\t\t* Please add the missing .hbm.xml file in your models directory.");
    }

    private static void invalidMap(String sourceOperation, Exception he) {
        InvalidMappingException invalid_map = (InvalidMappingException) he;
        log("\t- Method Source: " + sourceOperation);
        log("\t- Error Description: Invalid .hbm.xml Syntanx.");
        log("\t- Error Message: " + invalid_map.getMessage());
        log("\t- Solutions:");
        log("\t\t* Please check the syntax of your .hbm.xml file.");
        log("\t\t* If you have used the auto-generating tool please remove the <br>.");
    }

    private static void constraintViolation(String sourceOperation, Exception he) {
        ConstraintViolationException constraint = (ConstraintViolationException) he;
        log("\t- Method Source: " + sourceOperation);
        log("\t- Error Description: Database Constraints Violated.");
        log("\t- Error Message: " + constraint.getMessage());
        log("\t- Solutions:");
        log("\t\t* Please consult your sql database for the constraints that have beent set.");
        log("\t\t* Do not delete an entry that is a foreign key to another table.");
        log("\t\t* Primary key must be unique.");
        log("\t\t* Learn more about sql constraints at www.google.com");

        log("");
        log(" # Here are the sql error information:");
        log(" # - Constraint: " + constraint.getConstraintName());
        log(" # - SQL Statement: " + constraint.getSQL());
        log(" # - SQL Error Code: " + constraint.getErrorCode());
        log(" # - SQL State Error: " + constraint.getSQLState());
        log("");
        log("<sql_error_trace>");
        log("");
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        constraint.getSQLException().printStackTrace(pw);
        sw.toString(); // stack trace as a string
        System.err.println(sw);
        log("");
        System.err.println("</sql_error_trace>");
    }

    private static void genericError(String sourceOperation, Exception he) {
        log("\t- Method Source: " + sourceOperation);
        log("\t- Error Description: " + he.getClass().getSimpleName());
        log("\t- Error Message: " + he.getMessage());
        log("\t- Solutions:");
        log("\t\t* This is a generic error.");
        log("\t\t* Solutions can be found at www.google.com");
        log("");
        log("More Details: Please Read it Carefully !");
        log("");
        log("<error_info>");
        he.printStackTrace();
        log("</error_info>");
    }

    //
    public static void unlistedMapping(String name) {
        header();
        log("\t- Method Source: Integrity Check");
        log("\t- Error Description: .hbm.xml file not listed");
        log("\t- Error Message: You will not recieve any errors, But your queries will also have no values.");
        log("\t- Solutions:");
        log("\t\t* Please add " + name + " to your mapping list.");
        log("\t\t* The Mapping file is existing but it is not added.");
        footer();
    }
}

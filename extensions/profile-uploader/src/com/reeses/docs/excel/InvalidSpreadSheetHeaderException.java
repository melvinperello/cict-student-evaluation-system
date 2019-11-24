/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reeses.docs.excel;

/**
 *
 * @author Jhon Melvin
 */
public class InvalidSpreadSheetHeaderException extends Exception {

    public InvalidSpreadSheetHeaderException() {
        super();
    }

    public InvalidSpreadSheetHeaderException(String message) {
        super(message);
    }

    public InvalidSpreadSheetHeaderException(Throwable cause) {
        super(cause);
    }

    public InvalidSpreadSheetHeaderException(String message, Throwable cause) {
        super(message, cause);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.izum.fx.textinputfilters;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextInputControl;

/**
 *
 * @author Jhon Melvin
 */
public abstract class Filter {

    // instance variables
    protected StringProperty textProperty;
    private String historyOld = "";

    /**
     * Get the text from the source field. any child of TextInputControl
     *
     * @param textField
     * @return
     */
    public Filter setTextSource(TextInputControl textField) {
        this.textProperty = textField.textProperty();
        return this;
    }

    /**
     * Adds a listener to the text property. skips if nothing was changed.
     */
    public void applyFilter() {
        this.textProperty.addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (historyOld.equals(newValue)) {
                return;
            } else {
                historyOld = oldValue;
            }
            filter(oldValue, newValue);
        });
    }

    /**
     * Methods that applies filter from other child classes.
     *
     * @param oldValue
     * @param newValue
     */
    protected abstract void filter(String oldValue, String newValue);

}

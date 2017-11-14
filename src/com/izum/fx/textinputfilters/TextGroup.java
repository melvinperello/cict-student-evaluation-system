/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.izum.fx.textinputfilters;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.scene.control.TextInputControl;

/**
 *
 * @author Jhon Melvin
 */
public class TextGroup {

    /**
     * ************************************************************************
     *
     * Constructors
     *
     * ************************************************************************
     */
    public TextGroup() {

    }

    public TextGroup(TextInputControl... textGroup) {
        this.textGroup = textGroup;
    }

    /**
     * ************************************************************************
     *
     * Properties
     *
     * ************************************************************************
     */
    private TextInputControl[] textGroup;

    /**
     * ************************************************************************
     *
     * Methods
     *
     * ************************************************************************
     */
    public void createTextGroup(TextInputControl... textGroup) {
        this.textGroup = textGroup;
    }

    private boolean hasEmptyFields(boolean trim) {
        if (textGroup != null) {
            // if not null loop every text controls
            for (TextInputControl text : textGroup) {
                String textString = text.getText();
                // trim
                if (trim) {
                    textString = textString.trim();
                }

                if (textString.isEmpty()) {
                    return true;
                }
            }
            // if everything is not empty
            return false;
        } else {
            // if null
            return true;
        }
    }

    /**
     * Checks whether one of the members of this text group has and empty field.
     *
     * @return
     */
    public boolean hasEmptyFields() {
        return this.hasEmptyFields(false);
    }

    /**
     * Checks whether one of the members of this text group has and empty field
     * After Applying trimming.
     *
     * @return
     */
    public boolean hasEmptyFieldsTrimmed() {
        return this.hasEmptyFields(true);
    }

    /**
     * Clears the text of all the members of this textGroup.
     */
    public void clearGroup() {
        if (textGroup != null) {
            for (TextInputControl textcontrol : textGroup) {
                textcontrol.setText("");
            }
        }
    }

    /**
     * Array List of members of this group.
     *
     * @return
     */
    public ArrayList<TextInputControl> getMembers() {
        return new ArrayList<>(Arrays.asList(textGroup));
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.izum.fx.textinputfilters;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Jhon Melvin
 */
public class StringFilter extends Filter {

    // different filters
    public final static String LETTER_DIGIT_SPACE = "ALPHA_NUMERIC_SPACE";
    public final static String LETTER_DIGIT = "ALPHA_NUMERIC";
    public final static String LETTER = "ALPHA";
    public final static String LETTER_SPACE = "ALPHA_SPACE";
    public final static String DIGIT = "NUMERIC";
    public final static String DIGIT_SPACE = "NUMERIC_SPACE";

    // instance variables - filter settings
    private String filterMode;
    private boolean noLeadingTrailingSpaces = true;
    private Integer maxCharacters = Integer.MAX_VALUE;
    private FilterManager filterManager;

    /**
     * Clones this settings.
     *
     * @return
     */
    @Override
    public StringFilter clone() {
        StringFilter copied = new StringFilter();
        copied.setFilterManager(filterManager);
        copied.setNoLeadingTrailingSpaces(noLeadingTrailingSpaces);
        copied.setMaxCharacters(maxCharacters);
        copied.setFilterMode(filterMode);
        return copied;
    }

    /**
     * Sets a filter mode for this filter.
     *
     * @see StringFilter
     * @param filterMode
     * @return
     */
    public StringFilter setFilterMode(String filterMode) {
        this.filterMode = filterMode;
        return this;
    }

    /**
     * Restricts spaces from the beginning or at the end.
     *
     * @param noLeadingTrailingSpaces
     * @return
     */
    public StringFilter setNoLeadingTrailingSpaces(boolean noLeadingTrailingSpaces) {
        this.noLeadingTrailingSpaces = noLeadingTrailingSpaces;
        return this;
    }

    /**
     * Default is Integer Max Value. Sets the allowable length of characters in
     * the text field.
     *
     * @param maxCharacters
     * @return
     */
    public StringFilter setMaxCharacters(Integer maxCharacters) {
        this.maxCharacters = maxCharacters;
        return this;
    }

    /**
     * Handles the filter invalidation or validation results.
     *
     * @param filterManager
     * @return
     */
    public StringFilter setFilterManager(FilterManager filterManager) {
        this.filterManager = filterManager;
        return this;
    }

    @Override
    protected void filter(String oldValue, String newValue) {
        // check if empty
        if (newValue.isEmpty()) {
            return;
        }

        // check trailing spaces
        if (noLeadingTrailingSpaces) {
            // check if space only
            if (newValue.trim().isEmpty()) {
                super.textProperty.setValue("");
                applyFilterManager(false, FilterResult.RESULT_LTS_VIOLATED);
                return;
            } else {
                super.textProperty.setValue(newValue.trim());
            }
        }

        // check max characters
        if (maxCharacters != null) {
            if (newValue.length() > this.maxCharacters) {
                super.textProperty.setValue(oldValue);
                applyFilterManager(false, FilterResult.RESULT_MAX_VIOLATED);
                return;
            }
        }

        // check filter mode
        if (filterMode != null) {
            if (testFilters(newValue)) {
                applyFilterManager(true, FilterResult.RESULT_VALID);
            } else {
                reverIfNotMatch(oldValue);
            }
        }

    }

    /**
     * Adds a filter manager to handle results.
     *
     * @param result
     * @param filterResult
     */
    private void applyFilterManager(boolean result, String filterResult) {
        if (this.filterManager != null) {
            this.filterManager.applyFilter(new FilterResult(result, filterResult));
        }
    }

    /**
     * Tests the text against the filter mode.
     *
     * @param newValue
     * @return
     */
    private boolean testFilters(String newValue) {
        switch (filterMode) {
            case StringFilter.LETTER_DIGIT_SPACE:
                return StringUtils.isAlphanumericSpace(newValue);
            case StringFilter.LETTER:
                return StringUtils.isAlpha(newValue);
            case StringFilter.LETTER_DIGIT:
                return StringUtils.isAlphanumeric(newValue);
            case StringFilter.LETTER_SPACE:
                return StringUtils.isAlphaSpace(newValue);
            case StringFilter.DIGIT:
                return StringUtils.isNumeric(newValue);
            case StringFilter.DIGIT_SPACE:
                return StringUtils.isNumericSpace(newValue);
            default:
                return true;
        }
    }

    /**
     * Revert to old value if not allowed by the filter mode.
     *
     * @param oldValue
     */
    private void reverIfNotMatch(String oldValue) {
        super.textProperty.setValue(oldValue);
        applyFilterManager(false, FilterResult.RESULT_MODE_VIOLATED);
    }

}

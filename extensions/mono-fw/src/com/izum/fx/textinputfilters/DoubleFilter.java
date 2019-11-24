/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.izum.fx.textinputfilters;

/**
 *
 * @author Jhon Melvin
 */
public class DoubleFilter extends Filter {

    // filter settings
    private Integer maxCharacters = Integer.MAX_VALUE;
    private FilterManager filterManager;

    @Override
    public DoubleFilter clone() {
        DoubleFilter cloned = new DoubleFilter();
        cloned.setMaxCharacters(maxCharacters);
        cloned.setFilterManager(filterManager);
        return cloned;
    }

    public DoubleFilter setFilterManager(FilterManager filterManager) {
        this.filterManager = filterManager;
        return this;
    }

    /**
     * Max length of the string including the decimal point.
     *
     * @param maxCharacters
     * @return
     */
    public DoubleFilter setMaxCharacters(Integer maxCharacters) {
        this.maxCharacters = maxCharacters;
        return this;
    }

    @Override
    protected void filter(String oldValue, String newValue) {
        if (newValue.isEmpty()) {
            return;
        }

        if (newValue.length() > maxCharacters) {
            revertIfNotValid(oldValue);
            this.applyFilterManager(false, FilterResult.RESULT_MAX_VIOLATED);
            return;
        }

        try {
            Double converted = Double.valueOf(newValue);
        } catch (NumberFormatException e) {
            revertIfNotValid(oldValue);
            this.applyFilterManager(false, FilterResult.RESULT_NUMERIC_INVALID);
            return;
        }

        this.applyFilterManager(true, FilterResult.RESULT_VALID);
    }

    private void revertIfNotValid(String oldValue) {
        super.textProperty.setValue(oldValue);
    }

    private void applyFilterManager(boolean result, String filterResult) {
        if (this.filterManager != null) {
            this.filterManager.applyFilter(new FilterResult(result, filterResult));
        }
    }

}

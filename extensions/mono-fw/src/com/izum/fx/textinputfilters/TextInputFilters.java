package com.izum.fx.textinputfilters;

/**
 *
 * @author Jhon Melvin
 */
public class TextInputFilters {

    public static StringFilter string() {
        return new StringFilter();
    }

    public static DoubleFilter doubleFloating() {
        return new DoubleFilter();
    }
}

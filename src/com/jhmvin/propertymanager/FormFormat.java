/**
 * CAPSTONE PROJECT.
 * BSIT 4A-G1.
 * MONOSYNC TECHNOLOGIES.
 * MONOSYNC FRAMEWORK VERSION 1.0.0 TEACUP RICE ROLL.
 * THIS PROJECT IS PROPRIETARY AND CONFIDENTIAL ANY PART THEREOF.
 * COPYING AND DISTRIBUTION WITHOUT PERMISSION ARE NOT ALLOWED.
 *
 * COLLEGE OF INFORMATION AND COMMUNICATIONS TECHNOLOGY.
 * LINKED SYSTEM.
 *
 * PROJECT MANAGER: JHON MELVIN N. PERELLO
 * DEVELOPERS:
 * JOEMAR N. DELA CRUZ
 * GRETHEL EINSTEIN BERNARDINO
 *
 * OTHER LIBRARIES THAT ARE USED BELONGS TO THEIR RESPECTFUL OWNERS AND AUTHORS.
 * NO COPYRIGHT ARE INTENTIONAL OR INTENDED.
 * THIS PROJECT IS NOT PROFITABLE HENCE FOR EDUCATIONAL PURPOSES ONLY.
 * THIS PROJECT IS ONLY FOR COMPLIANCE TO OUR REQUIREMENTS.
 * THIS PROJECT DOES NOT INCLUDE DISTRIBUTION FOR OTHER PURPOSES.
 *
 */
package com.jhmvin.propertymanager;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author Jhon Melvin
 */
public class FormFormat {

    /**
     * Filter for Integer Values.
     */
    public class IntegerFormat {

        private Integer minLimit;
        private Integer maxLimit;
        private FilterInterface filterAction;

        public void setFilterAction(FilterInterface filter) {
            this.filterAction = filter;
        }

        public void setMinLimit(Integer minLimit) {
            this.minLimit = minLimit;
        }

        public void setMaxLimit(Integer maxLimit) {
            this.maxLimit = maxLimit;
        }

        public void filter(StringProperty textProperty) {
            textProperty.addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                /**
                 * Skip Filtering if empty.
                 */
                if (newValue.isEmpty()) {
                    return;
                }
                /**
                 * Start Filtering.
                 */
                FilterEvent filterEvent = new FilterEvent();

                try {
                    Integer value = Integer.parseInt(newValue);
                    if (value > maxLimit) {
                        filterEvent.setFilterMessage("MAX_LIMIT");
                        filterAction.filterEvent(filterEvent);
                        textProperty.setValue(oldValue);
                        return;
                    }
                    if (value < minLimit) {
                        filterEvent.setFilterMessage("MIN_LIMIT");
                        filterAction.filterEvent(filterEvent);
                        textProperty.setValue(oldValue);
                        return;
                    }
                } catch (NumberFormatException ne) {
                    filterEvent.setFilterMessage("NOT_NUMBER");
                    filterAction.filterEvent(filterEvent);
                    textProperty.setValue(oldValue);
                    return;
                }
            });
        }
    }

    public class CustomFormat {

        private Integer maxCharacters;
        private CustomFilter stringFilter;
        private FilterInterface filterAction;

        public void setMaxCharacters(Integer maxCharacters) {
            this.maxCharacters = maxCharacters;
        }

        public void setStringFilter(CustomFilter stringFilter) {
            this.stringFilter = stringFilter;
        }

        public void setFilterAction(FilterInterface filter) {
            this.filterAction = filter;
        }

        public void filter(StringProperty textProperty) {
            textProperty.addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                if (newValue.isEmpty()) {
                    return;
                }
                /**
                 * Start Filtering.
                 */
                FilterEvent filterEvent = new FilterEvent();

                if (newValue.length() > maxCharacters) {
                    filterEvent.setFilterMessage("MAX_LIMIT");
                    filterAction.filterEvent(filterEvent);
                    textProperty.setValue(oldValue);
                    return;
                }

                if (stringFilter.customFilter(newValue)) {
                    
                } else {
                    filterEvent.setFilterMessage("UNMATCHED");
                    filterAction.filterEvent(filterEvent);
                    textProperty.setValue(oldValue);
                }

            });
        }
    }

}

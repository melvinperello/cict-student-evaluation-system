/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cict.evaluation.encoder.view;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Jhon Melvin
 */
public class RatingTableClass extends RecursiveTreeObject<RatingTableClass> {

    public StringProperty rating;
    public StringProperty equivalent;
    public StringProperty remarks;

    public RatingTableClass(String rating, String equivalent, String remarks) {
        this.rating = new SimpleStringProperty(rating);
        this.equivalent = new SimpleStringProperty(equivalent);
        this.remarks = new SimpleStringProperty(remarks);
    }
}

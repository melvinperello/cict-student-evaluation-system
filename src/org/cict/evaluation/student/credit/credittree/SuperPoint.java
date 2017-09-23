/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cict.evaluation.student.credit.credittree;

import javafx.beans.binding.DoubleBinding;

/**
 *
 * @author Jhon Melvin
 */
public class SuperPoint {

    private DoubleBinding X;
    private DoubleBinding Y;

    public SuperPoint() {

    }

    public SuperPoint(DoubleBinding X, DoubleBinding Y) {
        this.X = X;
        this.Y = Y;
    }

    public DoubleBinding getX() {
        return X;
    }

    public void setX(DoubleBinding X) {
        this.X = X;
    }

    public DoubleBinding getY() {
        return Y;
    }

    public void setY(DoubleBinding Y) {
        this.Y = Y;
    }

    public String toString() {
        
        return "[X:" + this.X.toString() + " , Y:" + this.Y.get() + "]";
    }

}
